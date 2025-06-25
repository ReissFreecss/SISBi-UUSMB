/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//SISBI//
package jsf;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.BioinformaticAnalysisSampleLink;
import jpa.entities.Comments;
import jpa.entities.Genome;
import jpa.entities.Plataform;
import jpa.entities.Project;
import jpa.entities.Sample;
import jpa.entities.SampleGenomeLink;
import jpa.entities.UserProjectLink;
import jpa.entities.Users;
import jpa.session.BioinformaticAnalysisFacade;
import jpa.session.BioinformaticAnalysisSampleLinkFacade;
import jpa.session.CommentsFacade;
import jpa.session.GenomeFacade;
import jpa.session.ProjectFacade;
import jpa.session.SampleFacade;
import jpa.session.SampleGenomeLinkFacade;
import jpa.session.UserProjectLinkFacade;
import jsf.util.JsfUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author aaron
 */
@Named("fileController")
@SessionScoped
public class FileController implements Serializable {

    @EJB
    private BioinformaticAnalysisFacade bioFac;
    @EJB
    private BioinformaticAnalysisSampleLinkFacade bioAnSamLiFac;
    @EJB
    private SampleFacade ejbFacade;
    @EJB
    private SampleGenomeLinkFacade sglFac;
    @EJB
    private GenomeFacade genomeFac;
    @EJB
    private CommentsFacade commentFac;
    ///linea de codigo agregadada///
    @EJB
    private ProjectFacade proFac;
    ///////////////////////////////
    @EJB
    private UserProjectLinkFacade UserProjFac;

    private Sample currentSample;
    private BioinformaticAnalysisSampleLink currentBioanSamLink;

    private SampleGenomeLink currentOrg;
    private SampleGenomeLink currentCont;
    private SampleGenomeLink currentRef;

// Constante que indica la direccion donde son almacenados los archivos excel  
    //public static final String PATH = "/var/www/html/sampleFiles";
    public static final String PATH = PathFiles.PathFileSample;
// -------------------------------------------------------

// Constante que indica desde que renglon empiece a leer el archivo excel
    public static final int COMIENZO_CONTA = 31;
//------------------------------------------------------

// Constantes de las columnas del archivo de excel
    public static final int TIPO = 1;
    public static final int TAMANIO_SECUENCIAS = 2;
    public static final int ETIQUETA_MUESTRA = 3;
    public static final int NOMBRE_MUESTRA = 4;
    public static final int EXTRACCION_GENETICA = 5;
    public static final int PLATAFORMA = 6;
    public static final int DESCRIPCION = 7;
    public static final int ORGANISMO_FUENTE = 8;
    public static final int ORGANISMO_REFERENCIA = 9;
    public static final int CONTAMINANTE = 10;
    public static final int CONSENTRACION = 11;
    public static final int VOLUMEN = 12;
    public static final int ABS_260_280 = 13;
    public static final int ABS_260_230 = 14;
    public static final int RENDIMIENTO = 15;
    public static final int COMENTARIO = 27;
//---------------------------------------------------

//Constantes que indican los nombres de los analisis bioinformaticos
    public static final String LIMPIEZA_POR_CALIDAD = "Limpieza por calidad";
    public static final String LIMPIEZA_DE_ADAPTADOR = "Limpieza de adaptador";
    public static final String LIMPIEZA_DE_GENOMA_CONTAMINANTE = "Limpieza de genoma contaminante";
    public static final String ALINEAMIENTO_VS_GENOMA = "Alineamiento vs genoma de referencia";
    public static final String ANALISIS_EXPRESION_DIFERENCIAL = "Analisis de expresion diferencial";
    public static final String ENSAMBLADO_DE_NOVO = "Ensamblado de Novo";
    public static final String RESECUENCIACION_DE_GENOMAS = "Resecuenciacion de genomas";
    public static final String VARIANTES_GENETICAS = "Variantes Geneticas";
    public static final String METAGENOMAS = "Metagenomas";
    public static final String NINGUN_ANALISIS = "Ningun Analisis";
//--------------------------------------------------------------------------------

    //Variable para mostrar mensaje de ifnormación error o advertencias
    private String messageDialog;
    private String messageLongDialog = "";
    private String messageDialog2 = "";

    public String getMessageLongDialog() {
        return messageLongDialog;
    }

    public void setMessageLongDialog(String messageLongDialog) {
        this.messageLongDialog = messageLongDialog;
    }

    public String getMessageDialog2() {
        return messageDialog2;
    }

    public void setMessageDialog2(String messageDialog2) {
        this.messageDialog2 = messageDialog2;
    }

    public String getMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(String messageDialog) {
        this.messageDialog = messageDialog;
    }

    private SampleFacade getFacade() {
        return ejbFacade;
    }
    boolean checkSame;
    boolean checkNames;
    boolean checkLabels;
    boolean checkPlataforms;
    String nameRepeted;
    String labelRepeted;
    String orgNotFound;
    String invalidPlataform;
    int countSamples = 0;

    public int getCountSamples() {
        return countSamples;
    }

    public void setCountSamples(int countSamples) {
        this.countSamples = countSamples;
    }

    public String getOrgNotFound() {
        return orgNotFound;
    }

    public void setOrgNotFound(String orgNotFound) {
        this.orgNotFound = orgNotFound;
    }

    public String getNameRepeted() {
        return nameRepeted;
    }

    public void setNameRepeted(String nameRepeted) {
        this.nameRepeted = nameRepeted;
    }

    public String getLabelRepeted() {
        return labelRepeted;
    }

    public void setLabelRepeted(String labelRepeted) {
        this.labelRepeted = labelRepeted;
    }

    public boolean isCheckSame() {
        return checkSame;
    }

    public void setCheckSame(boolean checkSame) {
        this.checkSame = checkSame;
    }

    public boolean isCheckNames() {
        return checkNames;
    }

    public void setCheckNames(boolean checkNames) {
        this.checkNames = checkNames;
    }

    public boolean isCheckLabels() {
        return checkLabels;
    }

    public void setCheckLabels(boolean checkLabels) {
        this.checkLabels = checkLabels;
    }

    public String getInvalidPlataform() {
        return invalidPlataform;
    }

    public void setInvalidPlataform(String invalidPlataform) {
        this.invalidPlataform = invalidPlataform;
    }

    public boolean isCheckPlataforms() {
        return checkPlataforms;
    }

    public void setCheckPlataforms(boolean checkPlataforms) {
        this.checkPlataforms = checkPlataforms;
    }

    //MÉTODOS GENERALES  --------------------------------------------------------------------------------------------
    //Método para enviar mensjaes por medio de un dialog estandarizado
    public void showMessageDialog(String ms1, String ms2, String msLong) {

    }

    //Método para validar secuencia en muestras: Toma el parametro y lo compara
    public boolean validateSequenceSizeSample(String sequenceSize) {
        //Constantes para validar tamaños de secuencias
        List<String> listSequenceSize = new ArrayList<>();
        //Llenado de arreglos para validar
        listSequenceSize.add("1x75");
        listSequenceSize.add("2x75");
        listSequenceSize.add("2x100");
        listSequenceSize.add("2x150");
        listSequenceSize.add("2x250");
        listSequenceSize.add("2x300");

        if (listSequenceSize.indexOf(sequenceSize) < 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validatePlatformSample(String platform) {
        //Constantes para validar tamaños de secuencias
        List<String> listPlataformName = new ArrayList<>();
        List<String> listPlataformNameIllumina = new ArrayList<>();
        List<String> listPlataformNameOxford = new ArrayList<>();

        // 1.- obtenemos todas las plataformas y las agregamos a la lista de manera individual
        for (Plataform itemPlataform : ejbFacade.findAllPlataforms()) {
            listPlataformName.add(itemPlataform.getPlataformName());

            //PROCESO PARA CREAR LAS COMBINACIONES ENTRE PLATAFORMAS OXFORD E ILLUMINA
            //Cuando la plataforma sea del tipo illumina se guarda en la lista illumina
            if (itemPlataform.getNote().equals("ILLUMINA")) {
                listPlataformNameIllumina.add(itemPlataform.getPlataformName());
            }
            //Cuando la plataforma sea del tipo oxford se guarda en la lista oxford
            if (itemPlataform.getNote().equals("OXFORD NANOPORE")) {
                listPlataformNameOxford.add(itemPlataform.getPlataformName());
            }
        }

        for (String itemPlataformNameOxford : listPlataformNameOxford) {
            for (String itemPlataformNameIllumina : listPlataformNameIllumina) {
                listPlataformName.add(itemPlataformNameOxford + " - " + itemPlataformNameIllumina);
            }
        }

        if (listPlataformName.indexOf(platform) < 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isIllumina(String platform) {
        //Constantes para validar tamaños de secuencias
        List<String> listPlatform = new ArrayList<>();
        //Llenado de arreglos para validar
        listPlatform.add("NextSeq 2000");
        listPlatform.add("NextSeq 500");
        listPlatform.add("MiSeq");
        listPlatform.add("HiSeq 2000/2500");
        listPlatform.add("HiSeq X ");
        listPlatform.add("NovaSeq 6000");
        listPlatform.add("NovaSeq XPLUS");

        listPlatform.add("Oxford Nanopore - NextSeq500");
        listPlatform.add("Oxford Nanopore - MiSeq");
        listPlatform.add("Oxford Nanopore - iSeq");
        listPlatform.add("HiSeq X - Oxford Nanopore");
        listPlatform.add("Oxford Nanopore - HiSeq");
        listPlatform.add("Oxford Nanopore - NovaSeq XPLUS");
        listPlatform.add("Oxford Nanopore - NextSeq 2000");
        // cambie el nombre de las platafromas de acuerdo a lo que esta en la base de datos 

        if (listPlatform.indexOf(platform) < 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isOxford(String platform) {
        //Constantes para validar tamaños de secuencias
        List<String> listPlatform = new ArrayList<>();
        //Llenado de arreglos para validar
        listPlatform.add("Oxford Nanopore");

        listPlatform.add("Oxford Nanopore - NextSeq500");
        listPlatform.add("Oxford Nanopore - MiSeq");
        listPlatform.add("Oxford Nanopore - iSeq");
        listPlatform.add("Oxford Nanopore - HiSeq");
        listPlatform.add("NovaSeq 6000 - Oxford Nanopore");
        listPlatform.add("Oxford Nanopore - NovaSeq XPLUS");
        listPlatform.add("Oxford Nanopore - NextSeq 2000");
// cambie el nombre de las platafromas de acuerdo a lo que esta en la base de datos 
        if (listPlatform.indexOf(platform) < 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isNumberOrDecimal(String number) {
        return number.matches("[0-9]+||[0-9]+\\.[0-9]+");
    }

    public boolean validateRendimiento(String rendimiento) {
        String cadena = rendimiento.trim().replace(" ", "");
        return cadena.matches("[0-9]+(\\.[0-9]+){0,1}+(mil|miles|millon|millones)*");
    }

    public void procesFile(FileUploadEvent event) throws FileNotFoundException, IOException, InvalidFormatException {
        messageDialog = "";
        messageDialog2 = "";
        messageLongDialog = "";

        /*
        //Variables que representan las columnas
        String itemNumeroMuestra;                               //0
        String itemTipo;                                        //1
        String itemTamanioSecuencia;                            //2
        String itemEtiquetaTubo;                                //3
        String itemNombreMuestra;                               //4
        String itemExtraccionGenetica;                          //5
        String itemPlataforma;                                  //6
        String itemDescripcionRNAseq;                           //7
        String itemOrganismo;                                   //8
        String itemOrganismoReferencia;                         //9
        String itemFuenteContaminacion;                         //10
        String itemConcentracionMaterialGenetico;               //11
        String itemVolumenMuestra;                              //12
        String itemA260280;                                     //13
        String item260230;                                      //14
        String itemRendimientoIllumina;                         //15
        String itemRendimientoOxford;                           //16
            
        //Variables que representan el tipo de análisis
        String itemLimpiezaCalidad;                             //17
        String itemLimpiezaAdaptador;                           //18
        String itemAlineamientoVSGenoma;                        //19
        String itemLimpiezaGenoma;                              //20
        String itemAnalisisExpresionDiferencial;                //21
        String itemEnsambleNovo;                                //22
        String itemResecuenciacionGenomas;                      //23
        String itemVariantesGeneticas;                          //24
        String itemMetagenomas;                                 //25
        String itemNingunAnalisis;                              //26
        String itemObservaciones;                               //27
        
         */ // colunas originales antes de la preforma
        //Variables que representan las columnas 
        /*int colNumeroMuestra = 0;                               //0
        int colTipo = 1;                                        //1
        int colTamanioSecuencia = 2;                            //2
        int colEtiquetaTubo = 3;                                //3
        int colNombreMuestra = 4;                               //4
        int colExtraccionGenetica = 5;                          //5
        int colPlataforma = 6;                                  //6
        int colDescripcionRNAseq = 7;                           //7
        int colOrganismo = 8;                                   //8
        int colOrganismoReferencia = 9;                         //9
        int colFuenteContaminacion = 10;                         //10
        int colConcentracionMaterialGenetico = 11;               //11
        int colVolumenMuestra = 12;                              //12
        int colA260280 = 13;                                     //13
        int col260230 = 14;                                      //14
        int colRendimientoIllumina = 15;                         //15
        int colRendimientoOxford = 16;                           //16
         */
        //Variables que representan el tipo de análisis
        /*
        int colLimpiezaCalidad = 17;                             //17
        int colLimpiezaAdaptador = 18;                           //18
        int colAlineamientoVSGenoma = 19;                        //19
        int colLimpiezaGenoma = 20;                              //20
        int colAnalisisExpresionDiferencial = 21;                //21
        int colEnsambleNovo = 22;                                //22
        int colResecuenciacionGenomas = 23;                      //23
        int colVariantesGeneticas = 24;                          //24
        int colMetagenomas = 25;                                 //25
        int colNingunAnalisis = 26;                              //26
         */
        //int colmethodDelivery=32; // de la preforma
        ///nuevas columnas de la preforma
        //Variables que representan las columnas
        int colNumeroMuestra = 0;                               //0
        int colTipo = 1;                                        //1
        int colDeplesion = 2; // aqui va deplesion2
        int colJustquality = 3; // aqui va solo an.calidad3
        int colJustextrac = 4; // aqui va solo extraccion4
        int colJustlib = 5; // auqi va solo contr. biblio5
        int colJustseq = 6;// aqui va tengo lib quiero sec6        
        int colExtraccionGenetica = 7;                          //5
        int colEtiquetaTubo = 8;                                //3
        int colNombreMuestra = 9;                               //4
        int colDescripcionRNAseq = 10;                           //7
        int colVolumenMuestra = 11;                              //12
        int colConcentracionMaterialGenetico = 12;               //11
        int colA260280 = 13;                                     //13
        int col260230 = 14;                                       //14
        int colOrgtype = 15;  //de la preforma
        int colOrganismo = 16;                                   //8
        int colOrganismoReferencia = 17;                         //9
        //tamaño del genoma18
        int colFuenteContaminacion = 19;                         //10
        int colMetodoEntrega = 20;//medio de entrega 20
        int colAppType = 21;  // de la preforma       
        int colReCons = 22; //requiero constr22
        int colDosequence = 23;//requiero secuenciacion 23
        int colkitLib = 24; // de la preforma
        int coltagLib = 25; // de la preforma
        int colPlataforma = 26;                                  //6
        int colTamanioSecuencia = 27;                            //2
        int colRendimientoIllumina = 28;                         //15
        int colRendimientoOxford = 29;
        int colABioinfo = 30;//aqui va el analisis bioinformatico30        
        int colObservaciones = 31;                               //27

        // fin nuevas columnas de la preforma
        int rowStartExcel = 32; //leslie ,Excel antiguo en la 31

        //Variable para omitir validacion de rendimiento
        //boolean saveDataIllumina = false;
        //boolean saveDataOxford = false;
        // 24/abr/2025 Carlos Perez Calderon
        /*/ Nos aseguramos de convertir el numero a String
        String itemAppType = "";    //  inicializamos por si no entra en el switch    
        Map<String, String> appType = new HashMap<String, String>();
        appType.put("A1", "DNA - Genómico");
        appType.put("A2", "DNA - Plasmídico");
        appType.put("A3", "DNA - Mitocondrial");
        appType.put("A4", "DNA - ChipSeq");
        appType.put("A5", "DNA - Viral MOPOX");

        appType.put("B1", "RNA - Viral SARS-CoV-2");
        appType.put("B2", "RNA - Viral Dengue");
        appType.put("B3", "RNAseq");
        appType.put("B4", "smallRNA");

        appType.put("C1", "Amplicones - 16S V3");
        appType.put("C2", "Amplicones - 16S V4");
        appType.put("C3", "Amplicones - 16S FULL");
        appType.put("C4", "Amplicones - ITS1");
        appType.put("C5", "Amplicones - ITS V318S–28SD3");
        appType.put("C6", "Amplicones - trnL");
        appType.put("C7", "Amplicones - CO1");
        appType.put("C8", "Amplicones - 18S");
        appType.put("C9", "Amplicones - 28S");
        appType.put("C10", "Amplicones - 12S");
        appType.put("C11", "Amplicones - 16S V3-V4 e ITS1");
        appType.put("C12", "Amplicones - 16S V1-V9 e ITS");
        appType.put("C13", "Amplicones - Amplicon MCR");

        appType.put("D1", "Otros - 4Cseq");*/

        try {

            setCountSamples(0);
            String manager = "";
            String project = "";

            DataInputStream entrada = new DataInputStream(event.getFile().getInputstream());

            FacesContext context = FacesContext.getCurrentInstance();
            Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            XSSFWorkbook workbook = new XSSFWorkbook(entrada);

            XSSFSheet sheet = workbook.getSheetAt(0);
            List<Genome> parameter = genomeFac.findAllGenomes();

            List<String> names = new ArrayList<>();
            List<String> tubeLabels = new ArrayList<>();
            List<String> plataforms = new ArrayList<>();

            int countRow = 0;
            Date date = new Date();

            // ----------------------------------------------------------------- Iteración para validaciones   Inicio
            //Validación sobre los nombres
            List<String> tubesName = new ArrayList<>();
            List<String> samplesName = new ArrayList<>();
            int countRowValidation = 0;
            for (Row row : sheet) {
                countRowValidation++;
                if (countRowValidation >= rowStartExcel) {

                    List<String> parameters = new ArrayList<>();

                    // Obtener valor actual de la columna "Nombre de muestra"
                    Cell cellActual = row.getCell(colNombreMuestra);
                    String valorActual = (cellActual == null || cellActual.toString().trim().isEmpty()) ? ""
                            : cellActual.toString().trim();

                    // Obtener la fila siguiente
                    Row nextRow = sheet.getRow(row.getRowNum() + 1);
                    String valorSiguiente = "";
                    if (nextRow != null) {
                        Cell cellSiguiente = nextRow.getCell(colNombreMuestra);
                        valorSiguiente = (cellSiguiente == null || cellSiguiente.toString().trim().isEmpty()) ? ""
                                : cellSiguiente.toString().trim();
                    }

                    // Obtener la fila dos posiciones adelante
                    Row nextNextRow = sheet.getRow(row.getRowNum() + 2);
                    String valorSiguiente2 = "";
                    if (nextNextRow != null) {
                        Cell cellSiguiente2 = nextNextRow.getCell(colNombreMuestra);
                        valorSiguiente2 = (cellSiguiente2 == null || cellSiguiente2.toString().trim().isEmpty()) ? ""
                                : cellSiguiente2.toString().trim();
                    }

                    // Si los tres valores están vacíos, detener la lectura
                    if (valorActual.isEmpty() && valorSiguiente.isEmpty() && valorSiguiente2.isEmpty()) {
                        break;
                    }

                    for (int cn = 0; cn <= row.getRowNum(); cn++) {

                        Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case STRING:
                                parameters.add(cell.getStringCellValue());
                                System.out.println("CELL_TYPE_STRING: " + cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                parameters.add("" + (double) cell.getNumericCellValue());
                                System.out.println("CELL_TYPE_NUMERIC: " + "" + (double) cell.getNumericCellValue());
                                break;
                            case BLANK:
                                parameters.add("");
                                System.out.println("CELL_TYPE_BLANK: ");
                                break;
                            case ERROR:
                                parameters.add(String.valueOf("" + (Double) cell.getNumericCellValue()));
                                System.out.println("CELL_TYPE_ERROR: " + String.valueOf("" + (Double) cell.getNumericCellValue()));
                                break;

                        }

                    }

                    //cachamos la opcion del usuario 
                    String opciontamseq = parameters.get(colTamanioSecuencia).toLowerCase().trim();
                    String itemsSequenceSize = parameters.get(colTamanioSecuencia).trim(); //inicializamos por si no entra en el switch

                    // si la celda no es vacia comparmoa los valores
                    if (!opciontamseq.equals("")) {
                        //aqui vamos a hacer el remplazo para despues comparar                        
                        switch (opciontamseq) {
                            case "a":
                                itemsSequenceSize = "1x75";
                                break;
                            case "b":
                                itemsSequenceSize = "2x75";
                                break;
                            case "c":
                                itemsSequenceSize = "2x100";
                                break;
                            case "d":
                                itemsSequenceSize = "2x150";
                                break;
                            case "e":
                                itemsSequenceSize = "2x250";
                                break;
                            case "f":
                                itemsSequenceSize = "2x300";
                                break;

                        }
                        //System.out.println("el tamano de lectura dentro del swtich paso a: "+itemsSequenceSize);                                              

                        if (validateSequenceSizeSample(itemsSequenceSize) == false) {
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "La opción de tamaño de secuencia no es válida:  " + itemsSequenceSize + "   En la fila: " + countRowValidation + " del archivo";
                            //messageDialog2 = "";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }
                    }

                    //VALIDAMOS EL NOMBRE DEL TUBO
                    String itemNameTube = parameters.get(colEtiquetaTubo).trim();
                    System.out.println("itemName: " + itemNameTube);
                    //Validamos que sea obligatorio
                    if (itemNameTube.equals("")) {
                        RequestContext cont = RequestContext.getCurrentInstance();
                        cont.execute("PF('statusDialogUploadFile').hide();");
                        messageDialog = " El nombre del tubo es obligatorio en la fila: " + countRowValidation + " del archivo";
                        messageDialog2 = "Debe empezar con un caracter, no se aceptan acentos, guiones medio ni caracteres especiales dentro del nombre del tubo";
                        cont.execute("PF('dialogDetailError').show();");
                        return;
                    }

                    if (itemNameTube.matches("[0-9a-zA-Z_]+")) {
                    } else {
                        RequestContext cont = RequestContext.getCurrentInstance();
                        cont.execute("PF('statusDialogUploadFile').hide();");
                        messageDialog = "Se encontraron caracteres no válidos en el nombre del tubo:  " + itemNameTube + "   En la fila: " + countRowValidation + " del archivo";
                        messageDialog2 = "Debe empezar con un caracter, no se aceptan acentos, guiones medio ni caracteres especiales dentro del nombre del tubo";
                        cont.execute("PF('dialogDetailError').show();");
                        return;
                    }

                    //Validamos que no sea repetido
                    if (tubesName.indexOf(itemNameTube) >= 0) {
                        RequestContext cont = RequestContext.getCurrentInstance();
                        cont.execute("PF('statusDialogUploadFile').hide();");
                        messageDialog = "Se repite el nombre del tubo:  " + itemNameTube + "   En la fila: " + countRowValidation + " del archivo";
                        cont.execute("PF('dialogDetailError').show();");
                        return;
                    }
                    tubesName.add(itemNameTube);

                    //Validamos el nombre de la muestra
                    String itemNameSample = parameters.get(colNombreMuestra).trim();
                    //System.out.println("itemName: " + itemNameSample);
                    if (itemNameSample.equals("")) {
                        RequestContext cont = RequestContext.getCurrentInstance();
                        cont.execute("PF('statusDialogUploadFile').hide();");
                        messageDialog = " El nombre de la muestra es obligatorio en la fila: " + countRowValidation + " del archivo";
                        messageDialog2 = "Debe empezar con un caracter, no se aceptan acentos, guiones medio ni caracteres especiales dentro del nombre de la muestra";
                        cont.execute("PF('dialogDetailError').show();");
                        return;
                    }
                    if (itemNameSample.matches("[a-zA-Z0-9_]+")) {
                    } else {
                        RequestContext cont = RequestContext.getCurrentInstance();
                        cont.execute("PF('statusDialogUploadFile').hide();");
                        messageDialog = "Se encontraron caracteres no válidos en el nombre de la muestra:  " + itemNameSample + "   En la fila: " + countRowValidation + " del archivo";
                        messageDialog2 = "Debe empezar con un caracter, no se aceptan acentos, guiones medio ni caracteres especiales dentro del nombre de la muestra";
                        cont.execute("PF('dialogDetailError').show();");
                        return;
                    }
                    //Validamos que no se repita el nombre de la muestra
                    if (samplesName.indexOf(itemNameSample) >= 0) {
                        RequestContext cont = RequestContext.getCurrentInstance();
                        cont.execute("PF('statusDialogUploadFile').hide();");
                        messageDialog = "Se repite el nombre de la muestra:  " + itemNameSample + "  En la fila: " + countRowValidation + " del archivo";
                        cont.execute("PF('dialogDetailError').show();");
                        return;
                    }
                    samplesName.add(itemNameSample);

                    //Validamos plataforma
                    //cachamos la opcion del usuario 
                    String opcionplat = parameters.get(colPlataforma).toLowerCase().trim();
                    String itemPlatform = "";//inicializamos por si no entra en el switch

                    // si la celda no es vacia comparmoa los valores
                    if (!opcionplat.equals("")) {
                        //aqui vamos a hacer el remplazo para despues comparar                        
                        switch (opcionplat) {
                            case "a":
                                itemPlatform = "Oxford Nanopore";
                                break;
                            case "b":
                                itemPlatform = "NextSeq500";
                                break;
                            case "c":
                                itemPlatform = "MiSeq";
                                break;
                            case "d":
                                itemPlatform = "iSeq";
                                break;
                            case "e":
                                itemPlatform = "HiSeq";
                                break;
                            case "f":
                                itemPlatform = "Oxford Nanopore - NextSeq500";
                                break;
                            case "g":
                                itemPlatform = "Oxford Nanopore - MiSeq";
                                break;
                            case "h":
                                itemPlatform = "Oxford Nanopore - iSeq";
                                break;
                            case "i":
                                itemPlatform = "Oxford Nanopore - HiSeq";
                                break;
                            case "j":
                                itemPlatform = "Oxford Nanopore - NovaSeq XPLUS";
                                break;
                            case "k":
                                itemPlatform = "Oxford Nanopore - NextSeq 2000";
                                break;
                            case "l":
                                itemPlatform = "NovaSeq XPLUS";
                                break;
                            case "m":
                                itemPlatform = "NextSeq 2000";
                                System.out.println("Selecciono NextSeq 2000");
                                break;
                            case "n":
                                itemPlatform = "OTRA";
                                break;
                            /*case "j":
                               itemPlatform="Oxford Nanopore - NovaSeq"; 
                                break;
                              case "k":
                               itemPlatform="NovaSeq"; 
                                break;  
                             */
                        }
                        System.out.println("la plataforma dentro del swtich paso a: " + itemPlatform);
                    } else {
                        itemPlatform = "NA"; // Para celdas vacías
                        System.out.println("No se seleccionó plataforma, asignando valor por defecto: " + itemPlatform);
                    }

                    //  System.out.println("comparamos la variable platform : "+itemPlatform +" vs la lista dela bd");
                    if (validatePlatformSample(itemPlatform) == false) {
                        RequestContext cont = RequestContext.getCurrentInstance();
                        cont.execute("PF('statusDialogUploadFile').hide();");
                        messageDialog = "La opcion de plataforma no es válida:  " + itemPlatform + "    En la fila: " + countRowValidation + " del archivo";
                        messageDialog2 = "Seleccione una letra dentro del rango a - k, Las plataformas válidas son:";
                        for (String itemPlataformName : getAllPlataforms()) {
                            messageLongDialog = itemPlataformName + "\n" + messageLongDialog;
                        }
                        //messageLongDialog = "NextSeq 500\nMiSeq\nHiSeq 2000/2500\nHiSeq X\nNovaSeq 6000\nOxford Nanopore\nNextSeq 500 - Oxford Nanopore\nMiSeq - Oxford Nanopore\nHiSeq X - Oxford Nanopore";
                        cont.execute("PF('dialogDetailError').show();");
                        return;
                    }

                    // Carlos - Validamos que se seleccione DNA o RNA
                    String sType = parameters.get(colTipo).toUpperCase().trim();

                    //Validamos que sea obligatorio
                    if (sType.equals("")) {
                        RequestContext cont = RequestContext.getCurrentInstance();
                        cont.execute("PF('statusDialogUploadFile').hide();");
                        messageDialog = " El material genetico es obligatorio en la fila: " + countRowValidation + " del archivo";
                        messageDialog2 = "Material genético válido: DNA, ADN, RNA o ARN.";
                        cont.execute("PF('dialogDetailError').show();");
                        return;
                    }
                    if (!sType.equals("DNA") && !sType.equals("RNA")
                            && !sType.equals("ADN") && !sType.equals("ARN")) {
                        RequestContext cont = RequestContext.getCurrentInstance();
                        cont.execute("PF('statusDialogUploadFile').hide();");
                        messageDialog = "Se requiere un tipo de material genético válido: DNA, ADN, RNA o ARN." + "\n"
                                + "En la fila: " + countRowValidation + " Escribió: " + sType;
                        //messageDialog2 ="En la fila: " + countRowValidation + " Escribio: " + sType + "\n";
                        cont.execute("PF('dialogDetailError').show();");
                        return;
                    }

                    //Validamos la columna de Concentración de Material Genético entregado
                    String itemConcentracionMaterialGenetico = parameters.get(colConcentracionMaterialGenetico).trim();
                    if (!itemConcentracionMaterialGenetico.equals("")) {
                        if (!isNumberOrDecimal(itemConcentracionMaterialGenetico)) {
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "Concentración de material genético escrito es incorrecto: " + itemConcentracionMaterialGenetico + "    En la fila: " + countRowValidation + " del archivo";
                            messageDialog2 = "Solo se aceptan enteros y decimales";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }
                    }

                    //Validamos la columna de Volumen de muestra entregado
                    String itemVolumenEntregado = parameters.get(colVolumenMuestra).trim();
                    if (!itemVolumenEntregado.equals("")) {
                        if (!isNumberOrDecimal(itemVolumenEntregado)) {
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "Volumen de muestra entregado es incorrecto: " + itemVolumenEntregado + "    En la fila: " + countRowValidation + " del archivo";
                            messageDialog2 = "Solo se aceptan enteros y decimales";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }
                    }

                    //Validamos la columna de A 260/280
                    String itemA260280 = parameters.get(colA260280).trim();
                    if (!itemA260280.equals("")) {
                        if (!isNumberOrDecimal(itemA260280)) {
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "El campo A 260/280 es incorrecto: " + itemA260280 + "    En la fila: " + countRowValidation + " del archivo";
                            messageDialog2 = "Solo se aceptan enteros y decimales";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }
                    }

                    //Validamos la columna de A 260/230
                    String itemA260230 = parameters.get(col260230).trim();
                    if (!itemA260230.equals("")) {
                        if (!isNumberOrDecimal(itemA260230)) {
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "El campo A 260/230 es incorrecto: " + itemA260230 + "    En la fila: " + countRowValidation + " del archivo";
                            messageDialog2 = "Solo se aceptan enteros y decimales";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }
                    }

                    //Validamos la columna de abioinformatico de muestra 
                    String itemAnbio = parameters.get(colABioinfo).trim().replace(".0", "");
                    if (!itemAnbio.equals("")) {

                        if (itemAnbio.matches("[1-8,]+")) {
                        } else {
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "Se encontraron caracteres no válidos en las opciones del analisis bioinformatico :  " + itemAnbio + "   En la fila: " + countRowValidation + " del archivo";
                            messageDialog2 = "La selecion de análisis bioinformático debe ser números dentro del rango 1-7, si es más de un análisis por muestra,los números deben separarse por comas. Ejem. 1,2,3";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }
                        /*
                        if (!isNumberOrDecimal(itemAnbio)) {
                          
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "La selección de análisis bioinformático es incorrecta: " + itemAnbio + "    En la fila: " + countRowValidation + " del archivo";
                            messageDialog2 = "Solo se aceptan numeros dentro del rango 1-7";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }//agregar el else con el valor de la combinatoria*/

                    }

                    //Verificamos que plataforma tiene la muestra, y dependendiendo de la plataforma es la columna que se llenará
                    if (isIllumina(itemPlatform)) {

                        //Validando columna rendimiento illumina
                        String itemRendimientoIllumina = parameters.get(colRendimientoIllumina).trim();

                        //Validamos que no esté vacío
                        if (itemRendimientoIllumina.equals("")) {
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "En la fila " + countRowValidation + " del archivo, la muestra tiene una plataforma del tipo illumina, entonces la columna 'Rendimiento Illumina' es obligatorio.";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }

                        //Validamos que el contenido de la celda sea correcto
                        if (!validateRendimiento(itemRendimientoIllumina)) {
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "El rendimiento illumina " + itemRendimientoIllumina + ", no es un formato correcto en la fila: " + countRowValidation + " del archivo";
                            messageDialog2 = "Solo se aceptan enteros y decimales";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }

                        //Si se aplicará las operaciones al momento de registrar en la BD
                        //saveDataIllumina = true;
                    }
                    if (isOxford(itemPlatform)) {
                        //Validamos columna rendimiento oxford
                        String itemRendimientoOxford = parameters.get(colRendimientoOxford).trim();

                        System.out.println("Entramos a la validación de oxford y su valor es de: " + itemRendimientoOxford);

                        //Validamos que no esté vacío
                        if (itemRendimientoOxford.equals("")) {
                            RequestContext cont = RequestContext.getCurrentInstance();
                            cont.execute("PF('statusDialogUploadFile').hide();");
                            messageDialog = "En la fila " + countRowValidation + " del archivo, la muestra tiene una plataforma del tipo Oxford Nanopore, entonces la columna 'Rendimiento Oxford' es obligatorio.";
                            cont.execute("PF('dialogDetailError').show();");
                            return;
                        }

                        if (!itemRendimientoOxford.equals("")) {
                            if (!isNumberOrDecimal(itemRendimientoOxford)) {
                                RequestContext cont = RequestContext.getCurrentInstance();
                                cont.execute("PF('statusDialogUploadFile').hide();");
                                messageDialog = "El rendimiento oxford " + itemRendimientoOxford + ", no es un formato correcto en la fila: " + countRowValidation + " del archivo";
                                messageDialog2 = "Solo se aceptan enteros y decimales";
                                cont.execute("PF('dialogDetailError').show();");
                                return;
                            }
                        }

                        //Si se aplicará las operaciones al momento de registrar en la BD
                        //saveDataOxford = true;
                    }

                    String AppType = parameters.get(colAppType).trim().replace(".0", "").toUpperCase();

                    /*¨*if (!AppType.isEmpty()) {

                        // Separar por comas en caso de combinaciones
                        String[] codigos = AppType.split("\\s*,\\s*");
                        List<String> appLabels = new ArrayList<>();

                        boolean todosCodigosValidos = true;

                        for (String cod : codigos) {
                            if (appType.containsKey(cod)) {
                                appLabels.add(appType.get(cod));
                            } else {
                                todosCodigosValidos = false;
                                break;
                            }
                        }

                        if (todosCodigosValidos) {
                            // Si todos los códigos son válidos → concatenar nombres  asignar
                            if (currentSample == null) {
                                currentSample = new Sample();
                            }
                            itemAppType = String.join(", ", appLabels);
                            currentSample.setApp_type(AppType);
                        } else {
                            if (currentSample == null) {
                                currentSample = new Sample();
                            }
                            // Si no son códigos válidos → se toma como texto libre
                            currentSample.setApp_type(AppType);
                        }

                    }*/

                } //fin del primer if
            } //fin del for

            // ----------------------------------------------------------------- Iteración para validaciones   Final
            //if (checkNames && checkLabels && checkSame && checkPlataforms) {
            int count = 0;
            for (Row row : sheet) {
                count++;
                if (count >= rowStartExcel) {

                    //Lista de parametros a evaluar
                    List<String> parameters = new ArrayList<>();

                    // Obtener valor actual de la columna Nombre de muestra para la condicion de paro
                    Cell cellActual = row.getCell(colNombreMuestra);
                    String valorActual = (cellActual == null || cellActual.toString().trim().isEmpty()) ? "" : cellActual.toString().trim();

                    // Evaluamos la fila siguiente
                    Row nextRow = sheet.getRow(row.getRowNum() + 1);
                    String valorSiguiente = "";
                    if (nextRow != null) {
                        Cell cellSiguiente = nextRow.getCell(colNombreMuestra);
                        valorSiguiente = (cellSiguiente == null || cellSiguiente.toString().trim().isEmpty()) ? "" : cellSiguiente.toString().trim();
                    }

                    // Evaluamos dos posiciones adelante
                    Row nextNextRow = sheet.getRow(row.getRowNum() + 2);
                    String valorSiguiente2 = "";
                    if (nextNextRow != null) {
                        Cell cellSiguiente2 = nextNextRow.getCell(colNombreMuestra);
                        valorSiguiente2 = (cellSiguiente2 == null || cellSiguiente2.toString().trim().isEmpty()) ? "" : cellSiguiente2.toString().trim();
                    }

                    // Si los tres valores estan vacios detiene la lectura 
                    if (valorActual.isEmpty() && valorSiguiente.isEmpty() && valorSiguiente2.isEmpty()) {
                        break;
                    }

                    for (int cn = 0; cn <= row.getRowNum(); cn++) {

                        Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case STRING:
                                parameters.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                parameters.add(String.valueOf("" + (double) cell.getNumericCellValue()));
                                break;
                            case BLANK:
                                parameters.add("-------");
                                break;
                            case ERROR:
                                parameters.add(String.valueOf("" + (Double) cell.getNumericCellValue()));
                                break;

                        }

                    }

                    //Parando la iteración cuando la columna de "Número de muestra" es vacío
                    //Aqui debemos asignar el valor después de haber pasado el filtro
                    String sExpectedPerformance = "";

                    //Verificamos que plataforma tiene la muestra, y dependendiendo de la plataforma es la columna que se llenará
                    if (!parameters.get(colRendimientoIllumina).trim().equals("-------")) {

                        //Hacemos las operaciones correspondientes sobre el rendimiento de illumina
                        String itemFiltroRendimiento = parameters.get(colRendimientoIllumina).trim().replaceAll(" ", "");
                        double itemDoubleRendimiento = 0;

                        //SE REALIZAN LAS VALIDACIONES CUANDO TENGA MIL/MILES/MILLON/MILLONES
                        //Si la cadena contiene mil o miles
                        if (itemFiltroRendimiento.matches(".+(mil)") || itemFiltroRendimiento.matches(".+(miles)")) {
                            if (itemFiltroRendimiento.matches(".+(mil)")) {
                                itemDoubleRendimiento = Double.parseDouble(itemFiltroRendimiento.replaceAll("mil", ""));
                            } else {
                                itemDoubleRendimiento = Double.parseDouble(itemFiltroRendimiento.replaceAll("miles", ""));
                            }
                            //Aplicamos el punto 1 de división
                            itemDoubleRendimiento = (itemDoubleRendimiento / 1000);
                            //sExpectedPerformance = itemDoubleRendimiento+"";

                            //Si la cadena contiene millon o millones
                        } else if (itemFiltroRendimiento.matches(".+(millon)") || itemFiltroRendimiento.matches(".+(millones)")) {

                            if (itemFiltroRendimiento.matches(".+(millon)")) {
                                itemDoubleRendimiento = Double.parseDouble(itemFiltroRendimiento.replaceAll("millon", ""));
                            } else {
                                itemDoubleRendimiento = Double.parseDouble(itemFiltroRendimiento.replaceAll("millones", ""));
                            }
                            int itemIntegerRendimiento;
                            //Comprobamos si el decimal es n.0  y 0 puede ser 1 o varias veces
                            if ((itemDoubleRendimiento + "").matches(".+(\\.[0]+){1}")) {
                                //Si es así entonces lo convertimos a entero y lo asignamos a la variable
                                itemIntegerRendimiento = (int) itemDoubleRendimiento;
                                sExpectedPerformance = itemIntegerRendimiento + "";
                            } else {
                                //Si no solo es 0, entonces se lo asignamos tal cual
                                sExpectedPerformance = itemDoubleRendimiento + "";
                            }

                        } else {
                            //SE REALIZAN LAS VALIDACIONES CUANDO SOLO TENGA NUMEROS
                            itemDoubleRendimiento = Double.parseDouble(itemFiltroRendimiento.replaceAll("mil", ""));
                            if (itemDoubleRendimiento >= 1 && itemDoubleRendimiento <= 99) {
                                //Comprobamos si el decimal es n.0  y 0 puede ser 1 o varias veces
                                if ((itemDoubleRendimiento + "").matches(".+(\\.[0]+){1}")) {
                                    //Si es así entonces lo convertimos a entero y lo asignamos a la variable
                                    int itemIntegerRendimiento = (int) itemDoubleRendimiento;
                                    sExpectedPerformance = itemIntegerRendimiento + "";
                                } else {
                                    //Si no solo es 0, entonces se lo asignamos tal cual
                                    sExpectedPerformance = itemDoubleRendimiento + "";
                                }
                            } else if (itemDoubleRendimiento >= 100 && itemDoubleRendimiento <= 999) {
                                itemDoubleRendimiento = itemDoubleRendimiento / 1000;
                                sExpectedPerformance = itemDoubleRendimiento + "";
                            } else if (itemDoubleRendimiento >= 1000) {
                                itemDoubleRendimiento = itemDoubleRendimiento / 1000000;
                                sExpectedPerformance = itemDoubleRendimiento + "";
                            }
                        }

                    }

                    String sExpectedPerformanceOxford = "";

                    if (!parameters.get(colRendimientoOxford).trim().equals("-------")) {
                        //sExpectedPerformanceOxford = parameters.get(colRendimientoOxford).trim();

                        double itemDoubleRendimiento = Double.parseDouble(parameters.get(colRendimientoOxford).trim());
                        //Comprobamos si el decimal es n.0  y 0 puede ser 1 o varias veces
                        if ((itemDoubleRendimiento + "").matches(".+(\\.[0]+){1}")) {
                            //Si es así entonces lo convertimos a entero y lo asignamos a la variable
                            int itemIntegerRendimiento = (int) itemDoubleRendimiento;
                            sExpectedPerformanceOxford = itemIntegerRendimiento + "";
                        } else {
                            //Si no solo es 0, entonces se lo asignamos tal cual
                            sExpectedPerformanceOxford = itemDoubleRendimiento + "";
                        }
                    }

                    // String sExpectedPerformance = itemDoubleRendimiento+"";
                    //Se declaran las variables que representan los items de las columnas del archivo excel
                    String sType = parameters.get(colTipo).trim();
                    //requiere deplesion                       
                    String sDeplesion = "No";
                    //System.out.println("Requiere deplesion:" + parameters.get(colDeplesion));
                    if (!parameters.get(colDeplesion).trim().isEmpty()) {
                        if (parameters.get(colDeplesion).trim().toLowerCase().equals("x")) {
                            System.out.println("Requiere deplesion:" + parameters.get(colDeplesion));
                            sDeplesion = "Si";
                        } else {
                            sDeplesion = "No";
                        }

                    }
                    //solo a.calidad
                    String sjustquality = "No";
                    //System.out.println("Solo analisis de calidad:" + parameters.get(colJustquality));
                    if (!parameters.get(colJustquality).trim().isEmpty()) {
                        if (parameters.get(colJustquality).trim().toLowerCase().equals("x")) {
                            System.out.println("solo analisis de calidad:" + parameters.get(colJustquality));
                            sjustquality = "Si";
                        } else {
                            sjustquality = "No";
                        }

                    }

                    //solo ex.
                    String sjustextrac = "No";
                    // System.out.println("Solo extraccion:" + parameters.get(colJustextrac));
                    if (!parameters.get(colJustextrac).trim().isEmpty()) {
                        if (parameters.get(colJustextrac).trim().toLowerCase().equals("x")) {
                            System.out.println("solo extraccion: " + parameters.get(colJustextrac));
                            sjustextrac = "Si";
                        } else {
                            sjustextrac = "No";
                        }

                    }
                    //solo cons.
                    String sjustlib = "No";
                    // System.out.println("Solo contruccion:" + parameters.get(colJustlib));
                    if (!parameters.get(colJustlib).trim().isEmpty()) {
                        if (parameters.get(colJustlib).trim().toLowerCase().equals("x")) {
                            System.out.println("solo construccion: " + parameters.get(colJustlib));
                            sjustlib = "Si";
                        } else {
                            sjustlib = "No";
                        }

                    }
                    //solo sec.
                    String sjustseq = "No";
                    //  System.out.println("Solo secuenciacion:" + parameters.get(colJustseq));
                    if (!parameters.get(colJustseq).trim().isEmpty()) {
                        if (parameters.get(colJustseq).trim().toLowerCase().equals("x")) {
                            System.out.println("solo secuenciacion: " + parameters.get(colJustseq));
                            sjustseq = "Si";
                        } else {
                            sjustseq = "No";
                        }

                    }

                    //
                    String sGenExtrac = "No";
                    // System.out.println("Requiere extraccion:" + parameters.get(colExtraccionGenetica));
                    if (!parameters.get(colExtraccionGenetica).trim().isEmpty()) {
                        if (parameters.get(colExtraccionGenetica).toLowerCase().equals("") | !parameters.get(colExtraccionGenetica).trim().toLowerCase().equals("x")) {
                            System.out.println("Requiere extraccion:" + parameters.get(colExtraccionGenetica));
                            sGenExtrac = "No";
                        } else {
                            sGenExtrac = "Si";
                        }

                    }
                    // quiero contr-lib
                    String sConslib = "No";
                    // System.out.println("Requiere cons.lib:" + parameters.get(colReCons));
                    if (!parameters.get(colReCons).trim().isEmpty()) {
                        if (parameters.get(colReCons).toLowerCase().trim().equals("x")) {
                            System.out.println("Requiere cons.lib:" + parameters.get(colReCons));
                            sConslib = "Si";
                        } else {
                            sConslib = "No";
                        }

                    }

                    // quiero secuenciacion
                    String sSeq = "No";
                    //   System.out.println("Requiere secuenciacion:" + parameters.get(colDosequence));
                    if (!parameters.get(colDosequence).trim().isEmpty()) {
                        if (parameters.get(colDosequence).toLowerCase().trim().equals("x")) {
                            System.out.println("Requiere secuenciacion:" + parameters.get(colDosequence));
                            sSeq = "Si";
                        } else {
                            sSeq = "No";
                        }

                    }

                    String sReadSize = parameters.get(colTamanioSecuencia).toLowerCase().trim();
                    String sReadSize_insert = "";
                    if (!sReadSize.equals("")) {
                        switch (sReadSize) {
                            case "a":
                                sReadSize_insert = "1x75";
                                break;
                            case "b":
                                sReadSize_insert = "2x75";
                                break;
                            case "c":
                                sReadSize_insert = "2x100";
                                break;
                            case "d":
                                sReadSize_insert = "2x150";
                                break;
                            case "e":
                                sReadSize_insert = "2x250";
                                break;
                            case "f":
                                sReadSize_insert = "2x300";
                                break;
                        }
                    }

                    String sopcionplat = parameters.get(colPlataforma).toLowerCase().trim();
                    String itemPlatform_insert = "";//inicializamos por si no entra en el switch

                    // si la celda no es vacia comparmoa los valores
                    if (!sopcionplat.equals("")) {
                        //aqui vamos a hacer el remplazo para despues comparar                        
                        switch (sopcionplat) {
                            case "a":
                                itemPlatform_insert = "Oxford Nanopore";
                                break;
                            case "b":
                                itemPlatform_insert = "NextSeq500";
                                break;
                            case "c":
                                itemPlatform_insert = "MiSeq";
                                break;
                            case "d":
                                itemPlatform_insert = "iSeq";
                                break;
                            case "e":
                                itemPlatform_insert = "HiSeq";
                                break;
                            case "f":
                                itemPlatform_insert = "Oxford Nanopore - NextSeq500";
                                break;
                            case "g":
                                itemPlatform_insert = "Oxford Nanopore - MiSeq";
                                break;
                            case "h":
                                itemPlatform_insert = "Oxford Nanopore - iSeq";
                                break;
                            case "i":
                                itemPlatform_insert = "Oxford Nanopore - HiSeq";
                                break;
                            case "j":
                                itemPlatform_insert = "Oxford Nanopore - NovaSeq XPLUS";
                                break;
                            case "k":
                                itemPlatform_insert = "Oxford Nanopore - NextSeq 2000";
                                break;
                            case "l":
                                itemPlatform_insert = "NovaSeq XPLUS";
                                break;
                            case "m":
                                itemPlatform_insert = "NextSeq 2000";
                                break;
                            case "n":
                                itemPlatform_insert = "OTRA";
                                break;
                            /*case "j":
                               itemPlatform="Oxford Nanopore - NovaSeq"; 
                                break;
                              case "k":
                               itemPlatform="NovaSeq"; 
                                break;  
                             */
                        }
                        System.out.println("la plataforma dentro del swtich paso a: " + itemPlatform_insert);
                    } else {
                        itemPlatform_insert = "NA";
                    }
                    System.out.println("platafroma para insertar en la bd: " + itemPlatform_insert);

                    String sIdTube = parameters.get(colEtiquetaTubo).trim().replaceAll("\\.0", "");
                    String sName = parameters.get(colNombreMuestra).trim().replaceAll(" ", "");
                    String sDesc = parameters.get(colDescripcionRNAseq).trim();
                    String sVolume = parameters.get(colVolumenMuestra).trim();
                    String sQuantity = parameters.get(colConcentracionMaterialGenetico).trim();
                    String ABS280 = parameters.get(colA260280).trim();
                    String ABS230 = parameters.get(col260230).trim();
                    String org_ty = parameters.get(colOrgtype).trim();  //new preform
                    String sOrg = parameters.get(colOrganismo).trim();
                    String sRef = parameters.get(colOrganismoReferencia).trim();
                    //tamañogenoma
                    String sCont = parameters.get(colFuenteContaminacion).trim();
                    String metdeliv = parameters.get(colMetodoEntrega);  //new preform
                    String Aptype = parameters.get(colAppType).trim();  // 240425CPC Comento la linea para registrar los tipos de aplicacion por numero en preforma
                    String kit_lib = parameters.get(colkitLib).trim();  //new preform
                    String tag_lib = parameters.get(coltagLib).trim();  //new preform
                    //String sPlataform = parameters.get(colPlataforma).trim();
                    //String sReadSize = parameters.get(colTamanioSecuencia).trim();
                    //rendimientos illumina y oxford se asiganan antes 
                    //analisi bioinfo se hace la final se crean las ligas
                    String sComments = parameters.get(colObservaciones).trim();

                    //Se declara nuevo objeto de sample para asignarles las propiedades por medio de los setters
                    currentSample = new Sample();

                    //Variables para determinadar fecha
                    String[] tiempo = timestamp.toString().split(":");
                    String[] tiempo2 = tiempo[0].split("-");
                    String[] dia = tiempo2[2].split(" ");
                    String año = tiempo2[0].substring(2, 4);
                    String fecha = año + tiempo2[1] + dia[0];

                    List<UserProjectLink> upl = UserProjFac.findRoles(pj);
                    List<UserProjectLink> uplSelect = new ArrayList<>();

                    //Variables para complementar el nombre de la muestra
                    project = pj.getIdProject().replaceAll("Project_", "");
                    manager = project.replaceAll("[0-9-_:]", "");

                    // currentSample.setReceptionDate(date);
                    currentSample.setSampleName(manager + "_" + fecha + "_" + sName.replaceAll("\\.0", ""));

                    //Asignación de las variables al objeto sample por medio de los setters
                    if (!sIdTube.equals("-------")) {
                        currentSample.setIdTube(sIdTube);
                    }

                    if (!sQuantity.equals("-------")) {
                        currentSample.setSampleQuantity(sQuantity);
                    }

                    currentSample.setSampleQuality("Aun no determinada");

                    currentSample.setStatus("Registrada");
                    currentSample.setType(sType);

                    if (!sExpectedPerformance.equals("-------")) {
                        currentSample.setExpectedPerformance(sExpectedPerformance);
                    }

                    if (!sExpectedPerformanceOxford.equals("-------")) {
                        currentSample.setExpectedPerformanceOxford(sExpectedPerformanceOxford);
                    }

                    if (!sReadSize_insert.equals("-------")) {
                        currentSample.setReadSize(sReadSize_insert);
                    }
                    currentSample.setIdProject(pj);

                    if (!sDesc.equals("-------")) {
                        currentSample.setDescription(sDesc);
                    }

                    if (!sVolume.equals("-------")) {
                        currentSample.setSampleVolume(sVolume);
                    }
                    if (!ABS280.equals("-------")) {
                        currentSample.setAbs260_280(ABS280);
                    }
                    if (!ABS230.equals("-------")) {
                        currentSample.setAbs260_230(ABS230);
                    }

                    currentSample.setGeneticExtraction(sGenExtrac);

                    if (!sOrg.equals("-------")) {
                        currentSample.setOrganismName(sOrg);
                    }

                    if (!sCont.equals("-------")) {
                        currentSample.setContaminantName(sCont);
                    }

                    if (!sRef.equals("-------")) {
                        currentSample.setReferenceName(sRef);
                    }
                    if (!itemPlatform_insert.equals("-------")) {
                        currentSample.setSamplePlataform(itemPlatform_insert);
                    }

                    /// nuevas asiganacione para la preforma
                    if (!metdeliv.equals("-------")) {
                        currentSample.setDelivery(metdeliv);
                    }

                    /// nuevas asiganacione para la preforma
                    if (!Aptype.equals("-------")) {
                        currentSample.setApp_type(Aptype);
                    }
                    // nuevas asiganacioes para la preforma
                    if (!org_ty.equals("-------")) {
                        currentSample.setOrganism_type(org_ty);
                    }

                    if (!kit_lib.equals("-------")) {
                        currentSample.setKit_lib(kit_lib);
                    }
                    if (!tag_lib.equals("-------")) {
                        currentSample.setTag_lib(tag_lib);
                    }/*
                         if (!metdeliv.equals("-------")) {
                            currentSample.setDelivery(metdeliv);
                        }*/

                    //currentSample.setGeneticExtraction(sConslib); requiero deplesion
                    currentSample.setDeplesion(sDeplesion);
                    currentSample.setJust_genextrac(sjustextrac);
                    currentSample.setJust_qanalysis(sjustquality);
                    currentSample.setJust_lib(sjustlib);
                    currentSample.setJust_seq(sjustseq);
                    currentSample.setBuild_lib(sConslib);
                    currentSample.setDo_seq(sSeq);

                    if (!sName.equals("-------")) {

                        getFacade().create(currentSample);
                        countSamples++;

                        if (!sComments.equals("-------")) {
                            try {

                                Comments comments = new Comments();

                                comments.setComment(sComments);
                                comments.setIdType(currentSample.getIdSample() + "");
                                comments.setType("Sample");
                                comments.setUserName(us.getUserName());
                                comments.setCommentDate(timestamp);

                                commentFac.createComment(comments);

                            } catch (Exception e) {
                                System.out.println("algo paso" + e);

                            }

                        }

                        System.out.println("Pasamos la :  Asignación de las variables al objeto sample por medio de los setters");

                        try {

                            Comments comments = new Comments();

                            comments.setComment("Esta muestra fue dada de alta por el usuario: " + us.getUserName());
                            comments.setIdType(currentSample.getIdSample() + "");
                            comments.setType("Sample");
                            comments.setUserName("SISBI");
                            comments.setCommentDate(timestamp);

                            commentFac.createComment(comments);

                        } catch (Exception e) {
                            System.out.println("algo paso" + e);

                        }

                        System.out.println("Pasamos la creacion de comentario:   Esta muestra fue dada de alta por el usuario");

                        if (!sOrg.equals("-------")) {

                            currentOrg = new SampleGenomeLink();
                            currentOrg.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
                            currentOrg.getSampleGenomeLinkPK().setIdSample(currentSample.getIdSample());

                            String GenomeId = "";
                            String GenoString = "";
                            System.out.println("llega al for de createOrg");

                            for (Genome genome : parameter) {
                                if (genome.toString().equals(sOrg)) {
                                    GenomeId = genome.getIdGenome();
                                    GenoString = genome.toString();
                                }
                            }
                            System.out.println("id organismo fuente: " + GenomeId);
                            if (!GenoString.equals("")) {
                                System.out.println(GenomeId);
                                currentOrg.getSampleGenomeLinkPK().setIdGenome(GenomeId);
                                currentOrg.getSampleGenomeLinkPK().setType("Organismo Fuente");
                                sglFac.create(currentOrg);

                                JsfUtil.addSuccessMessage("Organismo: " + GenoString);
                            }
                            System.out.println("id organismo fuente: " + GenomeId);
                        }

                        if (!sRef.equals("-------")) {
                            currentRef = new SampleGenomeLink();
                            currentRef.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
                            currentRef.getSampleGenomeLinkPK().setIdSample(currentSample.getIdSample());

                            String GenomeId = "";
                            String GenoString = "";
                            System.out.println("llega al for de createRef");

                            for (Genome genome : parameter) {
                                if (genome.toString().equals(sRef)) {
                                    GenomeId = genome.getIdGenome();
                                    GenoString = genome.toString();
                                }
                            }
                            if (!GenoString.equals("")) {
                                System.out.println(GenomeId);
                                currentRef.getSampleGenomeLinkPK().setIdGenome(GenomeId);
                                currentRef.getSampleGenomeLinkPK().setType("Referencia");
                                sglFac.create(currentRef);

                                JsfUtil.addSuccessMessage("Referencia: " + GenoString);

                            }
                        }

                        if (!sCont.equals("-------")) {
                            currentCont = new SampleGenomeLink();
                            currentCont.setSampleGenomeLinkPK(new jpa.entities.SampleGenomeLinkPK());
                            currentCont.getSampleGenomeLinkPK().setIdSample(currentSample.getIdSample());

                            String GenomeId = "";
                            String GenoString = "";
                            System.out.println("llega al for de createCont");

                            for (Genome genome : parameter) {
                                if (genome.toString().equals(sCont)) {
                                    GenomeId = genome.getIdGenome();
                                    GenoString = genome.toString();
                                }
                            }
                            if (!GenoString.equals("")) {
                                System.out.println(GenomeId);
                                currentCont.getSampleGenomeLinkPK().setIdGenome(GenomeId);
                                currentCont.getSampleGenomeLinkPK().setType("Contaminante");
                                sglFac.create(currentCont);

                                JsfUtil.addSuccessMessage("Contaminante: " + GenoString);
                            }
                        }

                        System.out.println("Pasamos la creacion de links ");

                        System.out.println("Muestra creada");

                        String sAB = parameters.get(colABioinfo).trim();

                        //System.out.println("muestro la variable sAB: "+sAB);
                        if (!sAB.equals("-------")) {

                            if (sAB.contains(",")) {
                                System.out.println("es combinacion de AB");
                                String[] parts = sAB.split(",");
                                double combisab = 0;

                                for (int i = 0; i < parts.length; i++) {
                                    System.out.println("la opcion en la posicion :" + i + " es: " + parts[i]);
                                    combisab = Double.parseDouble(parts[i]);
                                    System.out.println("impirmo el cast adouble:" + combisab);
                                    // parseo a int para que pueda comparar.
                                    int intcombi = (int) combisab;
                                    System.out.println("casteo de double a int: " + intcombi);

                                    String type_analysisb = "";
                                    int aux_an = 0;
                                    System.out.println("va a entrar al switch en combi, aux= " + aux_an);
                                    switch (intcombi) {
                                        case 1:
                                            type_analysisb = "Ensamblado de genoma";
                                            aux_an = 11;
                                            break;
                                        case 2:
                                            type_analysisb = "Ensamblado de transcriptoma";
                                            aux_an = 12;
                                            break;
                                        case 3:
                                            type_analysisb = "Análisis de expresion diferencial";
                                            aux_an = 13;
                                            break;
                                        case 4:
                                            type_analysisb = "Perfiles taxonómicos de amplicones";
                                            aux_an = 14;
                                            break;
                                        case 5:
                                            type_analysisb = "Perfiles taxonómicos de WMS";
                                            aux_an = 15;
                                            break;
                                        case 6:
                                            type_analysisb = "Identificación de variantes virales";
                                            aux_an = 16;
                                            break;
                                        case 7:
                                            type_analysisb = "Análisis de variantes";
                                            aux_an = 17;
                                            break;
                                        case 8:
                                            type_analysisb = "Análisis de expresion diferencial y Ensamblado de transcriptoma";
                                            aux_an = 18; //cambiar este id.
                                            break;
                                    }//fin del switch
                                    System.out.println("AB de la posiicon :" + i + " es: " + type_analysisb + " id_ab: " + aux_an);
                                    System.out.println("mostramos el id que va almacenar abblink: " + aux_an + " muestra: " + currentSample.getIdTube());

                                    currentBioanSamLink = new BioinformaticAnalysisSampleLink();
                                    currentBioanSamLink.setBioinformaticAnalysisSampleLinkPK(new jpa.entities.BioinformaticAnalysisSampleLinkPK());
                                    currentBioanSamLink.getBioinformaticAnalysisSampleLinkPK().setIdSample(currentSample.getIdSample());
                                    currentBioanSamLink.getBioinformaticAnalysisSampleLinkPK().setIdAnalysis(aux_an);

                                    bioAnSamLiFac.create(currentBioanSamLink);
                                    JsfUtil.addSuccessMessage("Creado el link de analisis");

                                }//fin del for
                                System.out.println("salida del ciclo, terminamos te agregar los ab de la muestra");
                            } else {
                                System.out.println(" no es combinacion");

                                double dosab = Double.parseDouble(sAB);

                                //System.out.println("sAB casteado a double:" +dosab);  
                                int intsab = (int) dosab;
                                // System.out.println("casteo de double a int"+intsab);
                                //System.out.println("entro al if ");

                                //int intsab=Integer.parseInt(sAB);  
                                //System.out.println("sAB casteado a entero:" +intsab);                            
                                String type_analysisb = "";
                                int aux_an = 0;
                                System.out.println("va a entrar al switch, aux= " + aux_an);
                                //if (!parameters.get(colABioinfo).trim().isEmpty()) {

                                switch (intsab) {
                                    case 1:
                                        type_analysisb = "Ensamblado de genoma";
                                        aux_an = 11;
                                        break;
                                    case 2:
                                        type_analysisb = "Ensamblado de transcriptoma";
                                        aux_an = 12;
                                        break;
                                    case 3:
                                        type_analysisb = "Análisis de expresion diferencial";
                                        aux_an = 13;
                                        break;
                                    case 4:
                                        type_analysisb = "Perfiles taxonómicos de amplicones";
                                        aux_an = 14;
                                        break;
                                    case 5:
                                        type_analysisb = "Perfiles taxonómicos de WMS";
                                        aux_an = 15;
                                        break;
                                    case 6:
                                        type_analysisb = "Identificación de variantes virales";
                                        aux_an = 16;
                                        break;
                                    case 7:
                                        type_analysisb = "Análisis de variantes";
                                        aux_an = 17;
                                        break;
                                    case 8:
                                        type_analysisb = "Análisis de expresion diferencial y Ensamblado de transcriptoma";
                                        aux_an = 18; // cambiar este id
                                        break;
                                }

                                //System.out.println("AB:"+type_analysisb + " id_ab: "+aux_an);
                                System.out.println("mostramos el id que va almacenar abblink: " + aux_an + " muestra: " + currentSample.getIdTube());

                                currentBioanSamLink = new BioinformaticAnalysisSampleLink();
                                currentBioanSamLink.setBioinformaticAnalysisSampleLinkPK(new jpa.entities.BioinformaticAnalysisSampleLinkPK());
                                currentBioanSamLink.getBioinformaticAnalysisSampleLinkPK().setIdSample(currentSample.getIdSample());
                                currentBioanSamLink.getBioinformaticAnalysisSampleLinkPK().setIdAnalysis(aux_an);

                                bioAnSamLiFac.create(currentBioanSamLink);
                                JsfUtil.addSuccessMessage("Creado el link de analisis");

                            }//de noes combinaxcion
                        }

                        //fin creacion de link preforma
                    } //fin asignacion de variables para crear el objeto sample y links 

                } // fin fel if con el que comienz la iteracion de rowstart
                entrada.close();

            }//fin row:sheet

            EmailController ec = new EmailController();
            ec.sendManagerSamEmail(us, pj, countSamples);
            //ec.sendUserSamEmail(us, pj, countSamples);

            saveFile(manager + "_" + pj.getProjectName().replace(" ", "_"), event, timestamp);
            //////linea de codigo agregada//// 
            pj.setStatus("En proceso");
            proFac.edit(pj);
            ////////////////////////////////////
            context.getExternalContext().redirect("AltaExitosa.xhtml");

        } catch (Exception e) {
            System.out.println("Ocurrió un error en: " + e.toString());
            e.printStackTrace(); // <-- Esto imprime el stack trace completo en consola
            RequestContext cont = RequestContext.getCurrentInstance();
            cont.execute("PF('statusDialogUploadFile').hide();");
            JsfUtil.addErrorMessage("Error al cargar el archivo, por favor revise su contenido");
        }

    }

    public void saveFile(String project, FileUploadEvent event, Timestamp time) throws IOException {
        String fileName = "Muestras_" + project + "_" + time.toString().replaceAll("[\\:]", "_") + ".xlsx";

        String url = PATH + "/" + project;
        File folder = new File(url);
        if (!folder.exists()) {
            folder.mkdir();
        }
        Files.copy(event.getFile().getInputstream(), new File(url, fileName).toPath());

    }

    //Obetener todas las plataformas
    public List<String> getAllPlataforms() {
        List<String> listPlataformName = new ArrayList<>();
        List<String> listPlataformNameIllumina = new ArrayList<>();
        List<String> listPlataformNameOxford = new ArrayList<>();

        // 1.- obtenemos todas las plataformas y las agregamos a la lista de manera individual
        for (Plataform itemPlataform : ejbFacade.findAllPlataforms()) {
            listPlataformName.add(itemPlataform.getPlataformName());

            //PROCESO PARA CREAR LAS COMBINACIONES ENTRE PLATAFORMAS OXFORD E ILLUMINA
            //Cuando la plataforma sea del tipo illumina se guarda en la lista illumina
            if (itemPlataform.getNote().equals("ILLUMINA")) {
                listPlataformNameIllumina.add(itemPlataform.getPlataformName());
            }
            //Cuando la plataforma sea del tipo illumina se guarda en la lista illumina
            if (itemPlataform.getNote().equals("OXFORD NANOPORE")) {
                listPlataformNameOxford.add(itemPlataform.getPlataformName());
            }
        }

        for (String itemPlataformNameOxford : listPlataformNameOxford) {
            for (String itemPlataformNameIllumina : listPlataformNameIllumina) {
                listPlataformName.add(itemPlataformNameOxford + " - " + itemPlataformNameIllumina);
            }
        }

        return listPlataformName;
    }

}
