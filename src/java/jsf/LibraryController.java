
/*
Esta clase se compone de los diferentes metodos y subrutinas para el funcionamiento de las páginas referentes 
a las bibliotecas, dichos métodos realizan las siguientes tareas:
-Obtener los datos que vienen procedentes de las muestras y que servirán para la construcción de la biblioteca.
-Consultar y armar la lista de bibliotecas para la tablas en las páginas correspondientes.
-Controlar el número de objetos que aparecen en la tabla para crear las bibliotecas.
-Hacer disponibles las opciones dependiendo del tipo de bibliotecas a construir.
-Corroborar que la combinación de los tags 1 y 2 en cada renglon en la lista sean diferentes entre sí.
-Corroborar que los nombres de las bibliotecas sean distintos entre sí.
-Armar el nombre de la biblioteca por default.
-Desplegar los avisos en función al resultado de los registros.
-Procesa un archivo csv el cual toma elementos para editar las bibliotecas validando lo siguiente:
    *Que las muestras seleccionadas a editar sean las mismas que las que las que estan en el archivo.
    *Que los campos sean del tipo correcto (numerico, cadena de caracteres, etc).
    *Que el numero de elmentos en el archivo sea el mismo que los que se seleccionaron.
 */
package jsf;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import jpa.entities.Library;
import jsf.util.JsfUtil;
import jpa.session.LibraryFacade;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import jpa.entities.AuxLibrary;
import jpa.entities.Barcodes;
import jpa.entities.BarcodesCons;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.Comments;
import jpa.entities.Kit;
import jpa.entities.LibraryRunLink;
import jpa.entities.Plataform;
import jpa.entities.PlataformLinkKit;
import jpa.entities.Project;
import jpa.entities.QualityReports;
import jpa.entities.Run;
import jpa.entities.Sample;
import jpa.entities.SampleLibraryLink;
import jpa.entities.Users;
import jpa.session.BioinformaticAnalysisSampleLinkFacade;
import jpa.session.CommentsFacade;
import jpa.session.FilesFacade;
import jpa.session.LibraryRunLinkFacade;
import jpa.session.ProjectFacade;
import jpa.session.RunFacade;
import jpa.session.SampleLibraryLinkFacade;
import jpa.session.UsersFacade;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle; 
import org.apache.poi.hssf.usermodel.HSSFCellStyle; //leslie lo agregeue para decargar las lista de crea biblio
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("libraryController")
@SessionScoped
public class LibraryController implements Serializable {

    private Library current;

    @EJB
    private jpa.session.LibraryFacade ejbLib;

    @EJB
    private jpa.session.SampleFacade ejbSam;

    @EJB
    private jpa.session.BarcodesFacade ejbBar;
    @EJB
    private jpa.session.SampleLibraryLinkFacade ejbSll;
    @EJB
    private jpa.session.LibraryRunLinkFacade ejbLibraryRun;
    @EJB
    private jpa.session.RunFacade ejbRun;
    @EJB
    private jpa.session.UsersFacade ejbUser;
    @EJB
    private CommentsFacade commentFac;
    @EJB
    private jpa.session.ProjectFacade ejbProject;
    @EJB
    private jpa.session.SampleLibraryLinkFacade ejbSLLF;
    @EJB
    private FilesFacade ejbFiles;
    @EJB
    private BioinformaticAnalysisSampleLinkFacade BAFac;
    @EJB
    private jpa.session.RunFacade ejbFacadeRun;
    @EJB
    private jpa.session.QualityReportsFacade ejbQR;

    @EJB
    private jpa.session.PlataformLinkKitFacade ejbPlataformLinkKit;

    @EJB
    private jpa.session.PlataformFacade ejbPlataform;

    @EJB
    private jpa.session.KitFacade ejbKit;

    private List<TemplateLibrary> listSampleLibraries;

    private List<Barcodes> listBarcodesi7;

    private List<Barcodes> listBarcodesi5;

    private Boolean diferentKit;

    private String runNameSelected;

    private Boolean disabledBtn;

    private Boolean EditLibrariesName = false;

    public Boolean getEditLibrariesName() {
        return EditLibrariesName;
    }

    public void setEditLibrariesName(Boolean EditLibrariesName) {
        this.EditLibrariesName = EditLibrariesName;
    }

    public Boolean getDisabledBtn() {
        return disabledBtn;
    }

    public void setDisabledBtn(Boolean disabledBtn) {
        this.disabledBtn = disabledBtn;
    }

    public String getRunNameSelected() {
        return runNameSelected;
    }

    public void setRunNameSelected(String runNameSelected) {
        this.runNameSelected = runNameSelected;
    }

    //leslie22 agosto
    //leslie fin
    //Método inutilizable por la nueva BD
    /*
    public List<Barcodes> getListBarcodesi5() {

        List<Barcodes> barcodeEmpty = new ArrayList<>();
        //Agregamos una validación para eliminar el error de la tabla
        if (kit == "" || kit == null) {

            barcodeEmpty.add(new Barcodes());
            return barcodeEmpty;
        }

        String auxKit = kit;

        if (kit.equals("Nextera MatePair")) {
            auxKit = "TruSeq HT";
        }

        if (kit.equals("Nextera XT v2")) {
            if (platafrom.equals("MiSeq")) {
                auxKit = "Nextera XT v2 MiSeq";
            } else {
                auxKit = "Nextera XT v2 NextSeq";

            }

        }
        if (kit.equals("IDT-ILMN Nextera DNA UD")) {
            auxKit = "IDT-ILMN Nextera DNA UD";

            return ejbBar.findBarcodes("-i5", auxKit);
        }

        return ejbBar.findBarcode("i5", auxKit);
        //return listBarcodesi5;
    }*/
    public void setListBarcodesi5(List<Barcodes> listBarcodesi5) {
        this.listBarcodesi5 = listBarcodesi5;
    }

    //Método inutilizable : funcionaba para devolver el la lista de barcodes con i7 : desechado con la nueva BD
    /*
    public List<Barcodes> getListBarcodesi7() {

        List<Barcodes> barcodeEmpty = new ArrayList<>();
        //Agregamos una validación para eliminar el error de la tabla
        if (kit == "" || kit == null) {

            barcodeEmpty.add(new Barcodes());
            return barcodeEmpty;
        } else {
            String auxKit = kit;

            if (kit.equals("Nextera MatePair")) {
                auxKit = "TruSeq HT";
            }

            if (kit.equals("Nextera Rapid Capture Enrichment") || kit.equals("Nextera XT")) {
                auxKit = "Nextera DNA";
            }

            if (kit.equals("Pac Bio Default")) {
                auxKit = "Pac Bio";
            }
            if (kit.equals("Nanopore Default")) {
                auxKit = "Oxford Nanopore";
            }
            if (kit.equals("EXP-NBD196")) {
                auxKit = "EXP-NBD104";
                return ejbBar.findBarcode("NB", auxKit);
            }
            if (kit.equals("EXP-NBD196")) {
                auxKit = "EXP-PCB096";
                return ejbBar.findBarcode("BC", auxKit);
            }
            if (kit.equals("IDT-ILMN Nextera DNA UD")) {
                auxKit = "IDT-ILMN Nextera DNA UD";

                return ejbBar.findBarcodes("-i7", auxKit);
            }

            return ejbBar.findBarcode("i7", auxKit);
        }
    }
     */
    //Nuevo método para la consulta a la BD
    public List<Barcodes> getListBarcodesi7() {
        List<Barcodes> listBarcodes = new ArrayList<>();
        if (ejbKit.findKitByName(kit).size() > 0) {
            Kit objectKit = ejbKit.findKitByName(kit).get(0);
            for (Barcodes itemBarcode : objectKit.getBarcodesList()) {
                if (itemBarcode.getBasei7() != null) {
                    //Agregamos los que tengan valores en el campo de basei7
                    listBarcodes.add(itemBarcode);
                }
            }
        }
        return listBarcodes;
    }

    public List<Barcodes> getListBarcodesi5() {
        List<Barcodes> listBarcodes = new ArrayList<>();

        boolean basei5miseq = false;
        boolean basei5nextseq = false;
        //Verificamos el tipo de plataforma 
        if (platafrom.equals("HiSeq") || platafrom.equals("MiSeq") || platafrom.equals("Oxford Nanopore")) {
            basei5miseq = true;
        } else if (platafrom.equals("NovaSeq") || platafrom.equals("NextSeq500") || platafrom.equals("iSeq")) { //leslie agregue la opcion iSeq
            basei5nextseq = true;
        }

        if (ejbKit.findKitByName(kit).size() > 0) {
            Kit objectKit = ejbKit.findKitByName(kit).get(0);
            for (Barcodes itemBarcode : objectKit.getBarcodesList()) {

                //Dependiendo del tipo de plataforma es como se llenará la lista de barcodesi5
                if (basei5miseq) {
                    if (itemBarcode.getBasei5Miseq() != null) {
                        //Agregamos barcodesi5 miseq
                        listBarcodes.add(itemBarcode);
                    }
                } else if (basei5nextseq) {
                    if (itemBarcode.getBasei5Nextseq() != null) {
                        //Agregamos los que tengan valores en el campo de basei7
                        listBarcodes.add(itemBarcode);
                    }
                }
            }
        }
        return listBarcodes;
    }

    //Método para obtener el basei5 en la vista, al momento de crear las bibliotecas en la vista CreateL.xhtml
    public String getIndexBasei5Create(Barcodes itemBarcode) {
        if (itemBarcode != null) {
            //Verificamos el tipo de plataforma 
            if (platafrom.equals("HiSeq") || platafrom.equals("MiSeq") || platafrom.equals("Oxford Nanopore")) {
                System.out.println("Se recibe: " + itemBarcode);
                return itemBarcode.getIndexName() + "-" + itemBarcode.getBasei5Miseq();
            } else if (platafrom.equals("NovaSeq") || platafrom.equals("iSeq") || platafrom.equals("NextSeq500")) {
                return itemBarcode.getIndexName() + "-" + itemBarcode.getBasei5Nextseq();
            }
        }

        return null;
    }

    //Método para obtener el basei5 en la vista, en cualquier vista
    public String getIndexBasei5View(Barcodes itemBarcode, String itemPlataform) {
        if (itemBarcode != null) {
            //Verificamos el tipo de plataforma 
            if (itemPlataform.equals("HiSeq") || itemPlataform.equals("MiSeq") || itemPlataform.equals("Oxford Nanopore")) {
                return itemBarcode.getIndexName() + "-" + itemBarcode.getBasei5Miseq();
            } else if (itemPlataform.equals("NovaSeq") || platafrom.equals("iSeq") || itemPlataform.equals("NextSeq500")) {
                return itemBarcode.getIndexName() + "-" + itemBarcode.getBasei5Nextseq();
            }
        }

        return null;
    }

    //Método inutilizable por la nueva BD
    /*
    public List<Barcodes> getListBarcodesi7DiferentKits(String itemKit) {

        String auxKit = itemKit;

        if (itemKit.equals("Nextera MatePair")) {
            auxKit = "TruSeq HT";
        }

        if (itemKit.equals("Nextera Rapid Capture Enrichment") || itemKit.equals("Nextera XT")) {
            auxKit = "Nextera DNA";
        }

        if (itemKit.equals("Pac Bio Default")) {
            auxKit = "Pac Bio";
        }
        if (itemKit.equals("Nanopore Default")) {
            auxKit = "Oxford Nanopore";
        }
        if (itemKit.equals("EXP-NBD104")) {
            auxKit = "EXP-NBD104";
            return ejbBar.findBarcode("NB", auxKit);
        }
        if (itemKit.equals("EXP-PCB096")) {
            auxKit = "EXP-PCB096";
            return ejbBar.findBarcode("BC", auxKit);
        }
        if (itemKit.equals("IDT-ILMN Nextera DNA UD")) {
            auxKit = "IDT-ILMN Nextera DNA UD";

            return ejbBar.findBarcodes("-i7", auxKit);
        }

        return ejbBar.findBarcode("i7", auxKit);
    }*/
    //Método inutilizable por la nueva BD
    /*
    public List<Barcodes> getListBarcodesi5Kits(String itemKit, String itemPlataform) {

        String auxKit = itemKit;

        if (itemKit.equals("Nextera MatePair")) {
            auxKit = "TruSeq HT";
        }

        if (itemKit.equals("Nextera XT v2")) {
            if (itemPlataform.equals("MiSeq")) {
                auxKit = "Nextera XT v2 MiSeq";
            } else {
                auxKit = "Nextera XT v2 NextSeq";

            }

        }
        if (itemKit.equals("IDT-ILMN Nextera DNA UD")) {
            auxKit = "IDT-ILMN Nextera DNA UD";

            return ejbBar.findBarcodes("-i5", auxKit);
        }

        return ejbBar.findBarcode("i5", auxKit);
        //return listBarcodesi5;
    }*/
    public void setListBarcodesi7(List<Barcodes> listBarcodesi7) {
        this.listBarcodesi7 = listBarcodesi7;
    }

    public List<TemplateLibrary> getListSampleLibraries() {
        return listSampleLibraries;
    }

    public void setListSampleLibraries(List<TemplateLibrary> listSampleLibraries) {
        this.listSampleLibraries = listSampleLibraries;
    }

    private List<Sample> sampleTable;

    public List<Sample> getSampleTable() {

        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listSamples", sampleTable);
        return sampleTable;
    }

    public void setSampleTable(List<Sample> sampleTable) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listSamples", sampleTable);
        this.sampleTable = sampleTable;

    }

    List<String> rowSampleExcel = new ArrayList<>();

    public List<String> getRowSampleExcel() {
        return rowSampleExcel;
    }

    public void setRowSampleExcel(List<String> rowSampleExcel) {
        this.rowSampleExcel = rowSampleExcel;
    }

    public RunFacade getEjbFacadeRun() {
        return ejbFacadeRun;
    }

    public void setEjbFacadeRun(RunFacade ejbFacadeRun) {
        this.ejbFacadeRun = ejbFacadeRun;
    }

    String messageDialog;

    //RUTA PARA SUBIR ARCHIVOS
    String pathCreateSampleFiles = PathFiles.DirectorySampleFiles;

    //RUTA PARA VISUALIZAR WEB
    String fileProjectPath = PathFiles.LinkLibrarySampleFiles;

    public String getFileProjectPath() {
        return fileProjectPath;
    }

    public void setFileProjectPath(String fileProjectPath) {
        this.fileProjectPath = fileProjectPath;
    }

    public String getMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(String messageDialog) {
        this.messageDialog = messageDialog;
    }

    public BioinformaticAnalysisSampleLinkFacade getBAFac() {
        return BAFac;
    }

    public void setBAFac(BioinformaticAnalysisSampleLinkFacade BAFac) {
        this.BAFac = BAFac;
    }

    public FilesFacade getEjbFiles() {
        return ejbFiles;
    }

    public void setEjbFiles(FilesFacade ejbFiles) {
        this.ejbFiles = ejbFiles;
    }

    public SampleLibraryLinkFacade getEjbSLLF() {
        return ejbSLLF;
    }

    public void setEjbSLLF(SampleLibraryLinkFacade ejbSLLF) {
        this.ejbSLLF = ejbSLLF;
    }

    public ProjectFacade getEjbProject() {
        return ejbProject;
    }

    public void setEjbProject(ProjectFacade ejbProject) {
        this.ejbProject = ejbProject;
    }

    public CommentsFacade getCommentFac() {
        return commentFac;
    }

    public void setCommentFac(CommentsFacade commentFac) {
        this.commentFac = commentFac;
    }

    //variables para el aprtado de creacion de corridas 
    //leslie16 agosto2024
    private String plat_sequencing;
    private String nom_equipo;

    //fin leslie
    private Users user;
    private String Investigator;
    private String runName;
    private String Description;
    private String nm1;
    private String nm2;
    private String run1;
    private String run2;
    private Date runDate;
    private String readType;
    private String kitPerformance;
    private String NameRun;
    private String ExperimentName;
    private List<Library> libRun;
    private List<Run> edit;
    private String scode;
    private String corrida;
    private String flowcell;
    private boolean cycles = true;
    private String project;
    private String range;
    private Users users;
    private List<String> projs = new ArrayList<>();
    private String usuario;
    private List<String> usersList = new ArrayList<>();
    List<String> plataformKit = new ArrayList<>();

    //leslie16 agosto 
    public String getPlat_sequencing() {
        return plat_sequencing;
    }

    public void setPlat_sequencing(String plat_sequencing) {
        this.plat_sequencing = plat_sequencing;
    }

    public void asignarValorCampos() { //metodo que ayuda a validar los campos 

        plat_sequencing = plat_sequencing;
        nom_equipo = nom_equipo;
        scode = scode;
    }

    public String getNom_equipo() {
        return nom_equipo;
    }

    public void setNom_equipo(String nom_equipo) {
        this.nom_equipo = nom_equipo;
    }
    //leslie

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public List<String> getProjs() {
        return projs;
    }

    public void setProjs(List<String> projs) {
        this.projs = projs;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public List<Library> getLibraryTable() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listLibrary", LibraryTable);
        return LibraryTable;
    }

    public void setLibraryTable(List<Library> LibraryTable) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listLibrary", LibraryTable);
        this.LibraryTable = LibraryTable;
    }

    public UsersFacade getEjbUser() {
        return ejbUser;
    }

    public void setEjbUser(UsersFacade ejbUser) {
        this.ejbUser = ejbUser;
    }

    public boolean isCycles() {
        return cycles;
    }

    public void setCycles(boolean cycles) {
        this.cycles = cycles;
    }

    private List<Library> LibraryTable;

    public RunFacade getEjbRun() {
        return ejbRun;
    }

    public void setEjbRun(RunFacade ejbRun) {
        this.ejbRun = ejbRun;
    }

    public LibraryRunLinkFacade getEjbLibraryRun() {
        return ejbLibraryRun;
    }

    public void setEjbLibraryRun(LibraryRunLinkFacade ejbLibraryRun) {
        this.ejbLibraryRun = ejbLibraryRun;
    }

    public String getInvestigator() {
        return Investigator;
    }

    public void setInvestigator(String Investigator) {
        this.Investigator = Investigator;
    }

    public String getRunName() {
        return runName;
    }

    public void setRunName() {
        this.runName = runName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getNm1() {
        return nm1;
    }

    public void setNm1(String nm1) {
        this.nm1 = nm1;
    }

    public String getNm2() {
        return nm2;
    }

    public void setNm2(String nm2) {
        this.nm2 = nm2;
    }

    public String getRun1() {
        return run1;
    }

    public void setRun1(String run1) {
        this.run1 = run1;
    }

    public String getRun2() {
        return run2;
    }

    public void setRun2(String run2) {
        this.run2 = run2;
    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }

    public String getKitPerformance() {
        return kitPerformance;
    }

    public void setKitPerformance(String kitPerformance) {
        this.kitPerformance = kitPerformance;
    }

    public String getNameRun() {
        return NameRun;
    }

    public void setNameRun(String NameRun) {
        this.NameRun = NameRun;
    }

    public String getExperimentName() {
        return ExperimentName;
    }

    public void setExperimentName(String ExperimentName) {
        this.ExperimentName = ExperimentName;
    }

    public List<Library> getLibRun() {
        return libRun;
    }

    public void setLibRun(List<Library> libRun) {
        this.libRun = libRun;
    }

    public List<Run> getEdit() {
        return edit;
    }

    public void setEdit(List<Run> edit) {
        this.edit = edit;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getCorrida() {
        return corrida;
    }

    public void setCorrida(String corrida) {
        this.corrida = corrida;
    }

    public String getFlowcell() {
        return flowcell;
    }

    public void setFlowcell(String flowcell) {
        this.flowcell = flowcell;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    int countLibraries = 0;
    // Constante que indica la direccion donde son almacenados los archivos excel  
    public static final String PATH = "/var/www/html/sampleFiles";
// -------------------------------------------------------

// Constante que indica desde que renglon empiece a leer el archivo excel
    public static final int COMIENZO_CONTA = 2;
//------------------------------------------------------

    public static final int MUESTRAS = 1;

    public static final int TAG_1 = 2;
    public static final int TAG_2 = 3;
    public static final int PLATAFORM = 4;
    public static final int KIT = 5;

    List<String> listPlataform = new ArrayList<>();
    List<String> listKits = new ArrayList<>();

    FacesContext contexto = FacesContext.getCurrentInstance();
    List<Object> propieties = (List<Object>) contexto.getExternalContext().getSessionMap().get("libraryProperties");

    String platafrom = "";
    String optPlataform;
    String kit = "";
    List<String> samTypes = new ArrayList<>();
    String libraryType = "Paired End";

    int numLib;
    int propNumLib;

    Date fechaPrep = new Date();
    //List<AuxLibrary> libs = new ArrayList<>();
    List<Sample> samples;

    boolean dnaRender = true;
    boolean rnaRender = true;
    boolean ampRender = true;
    boolean smallRender = true;
    int index;

    List<Library> selectedLibraries = new ArrayList<>();

    public List<Library> getSelectedLibraries() {
        return selectedLibraries;
    }

    public void setSelectedLibraries(List<Library> selectedLibraries) {
        this.selectedLibraries = selectedLibraries;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {

        this.index = index;
    }

    public boolean isAmpRender() {
        return ampRender;
    }

    public void setAmpRender(boolean ampRender) {
        this.ampRender = ampRender;
    }

    public LibraryFacade getEjbFacade() {
        return ejbLib;
    }

    public void setDnaRender(boolean dnaRender) {
        this.dnaRender = dnaRender;
    }

    public boolean isRnaRender() {
        return rnaRender;
    }

    public void setRnaRender(boolean rnaRender) {
        this.rnaRender = rnaRender;
    }

    public boolean isDnaRender() {
        return dnaRender;
    }

    public boolean isSmallRender() {
        return smallRender;
    }

    public void setSmallRender(boolean smallRender) {
        this.smallRender = smallRender;
    }

    public List<Sample> getSamples() {
        FacesContext context = FacesContext.getCurrentInstance();
        List<Sample> samp = (List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");
        return samp;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    public List<String> getSamTypes() {
        return samTypes;
    }

    public void setSamTypes(List<String> samTypes) {
        this.samTypes = samTypes;
    }

    /*
    public List<AuxLibrary> getLibs() {
        return libs;
    }

    public void setLibs(List<AuxLibrary> libs) {
        this.libs = libs;
    }*/
    public int getNumLib() {

        return numLib;
    }

    public void setNumLib(int numLib) {

        this.numLib = numLib;
    }

    public String getLibraryType() {
        return libraryType;
    }

    public void setLibraryType(String libraryType) {
        this.libraryType = libraryType;
    }

    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
    }

    public String getPlatafrom() {

        return platafrom;
    }

    public void setPlatafrom(String platafrom) {

        this.platafrom = platafrom;
    }

    public Library getSelected() {
        if (current == null) {
            current = new Library();

        }
        return current;
    }

    private LibraryFacade getFacade() {
        return ejbLib;
    }

    private List<Library> bibliotecas;

    public List<Library> getBibliotecas() {
        bibliotecas = ejbLib.findAll();
        return bibliotecas;
    }

    public void setBibliotecas(List<Library> bibliotecas) {
        this.bibliotecas = bibliotecas;
    }

    public List<Library> getLibrariesByKit() {

        return ejbLib.findLibraryByKit(kit);

    }

    public Date getFechaPrep() {
        return fechaPrep;
    }

    public void setFechaPrep(Date fechaPrep) {
        this.fechaPrep = fechaPrep;
    }

    public List<Users> getUsersSamples() {
        List<Users> us = ejbUser.findAllUsers();

        return us;

    }

    public List<String> getProjects() {
        List<String> pID = new ArrayList<>();
        pID.add("");
        List<Project> projects = ejbProject.findAll();
        for (Project pj : projects) {
            pID.add(pj.getIdProject());
        }
        return pID;
    }

    public List<String> getProjectLibrary(Library id) {
        List<String> idProject = new ArrayList<>();
        List<SampleLibraryLink> lib = ejbSLLF.findSamplesByID(id);
        for (SampleLibraryLink lb : lib) {
            idProject.add(lb.getSample().getIdProject().getIdProject());
            //lb.getSample().getIdProject();

        }
        return idProject;
    }

    public List<Integer> getIdSample(Library id) {
        List<Integer> idSample = new ArrayList<>();
        List<SampleLibraryLink> libreria = ejbSLLF.findSamplesByID(id);

        for (SampleLibraryLink lb : libreria) {
            System.out.println("Se imprime el item de SLL: " + lb);
            idSample.add(lb.getSample().getIdSample());
            //lb.getSample().getIdProject();

        }
        System.out.println("Se imprime el id de la muestra idSample: " + idSample);
        return idSample;
    }

    public List<Library> getLibsRun() {
        //resetValues();
        List<Library> libByProject = new ArrayList<>();
        Integer num = 0;
        System.out.println("valor de range: " + range);
        System.out.println("valor de proyecto en boolean: " + projs.isEmpty());
        System.out.println("valor de proyecto en size: " + projs.size());
        System.out.println("valor de user: " + user);
        System.out.println("valor de usersList.size: " + usersList.size());

        if ((range == null || range == "") && projs.size() == 0 && user == null && usersList.size() == 0) {
            return null;
        }

        if (projs.isEmpty() && usersList.isEmpty() && (range != null || range != "")) {
            System.out.println("busqueda por rango");
            num = 3;
        } else {
            if (!projs.isEmpty()) {
                System.out.println("busqueda por proyectos");
                num = 1;
            } else {
                System.out.println("nusqueda por usuarios");
                num = 2;
            }
        }

        try {
            switch (num) {
                case 1:
                    System.out.println("caso 1");
                    for (String pj : projs) {
                        System.out.println("proyectos:" + pj);
                        List<Sample> samplebyProject = ejbSam.findSamplesByProject(pj);
                        for (Sample sm : samplebyProject) {

                            List<Library> libraryByProject = ejbLib.getLibraries(sm);
                            for (Library lb : libraryByProject) {
                                libByProject.add(lb);
                            }
                        }

                    }

                    break;
                case 2:
                    System.out.println("caso 2");
                    Users uName = null;
                    List<Users> findUser = ejbUser.findUsersByUName(usuario);
                    for (String us : usersList) {
                        uName = (Users) ejbUser.findUsersByUName(us).get(0);
                        //System.out.println(us.getUserName());

                        List<Project> projectsByUser = ejbProject.findProUsers(uName);
                        for (Project usProj : projectsByUser) {
                            List<Sample> samplebyProject = ejbSam.findSamplesByProject(usProj.getIdProject());

                            for (Sample sm : samplebyProject) {

                                List<Library> libraryByProject = ejbLib.getLibraries(sm);
                                for (Library lb : libraryByProject) {

                                    libByProject.add(lb);
                                }
                            }

                        }
                    }

                    break;
                case 3:
                    System.out.println("caso 3");
                    String separate[] = range.split(",");
                    for (String id : separate) {
                        if (Pattern.matches("[0-9]*-[0-9]*", id)) {
                            String sep[] = id.split("-");
                            System.out.println(Integer.parseInt(sep[0]));
                            List<Sample> Samples = ejbSam.findSampleByRange(Integer.parseInt(sep[0]), Integer.parseInt(sep[1]));
                            for (Sample sm : Samples) {
                                List<Library> libraryByRange = ejbLib.LibrariesBySample(sm);
                                for (Library lb : libraryByRange) {
                                    libByProject.add(lb);

                                }
                            }

                        } else {

                            List<Sample> Samples = ejbSam.findSampleById(Integer.parseInt(id));
                            for (Sample sm : Samples) {
                                List<Library> libraryByRange = ejbLib.LibrariesBySample(sm);
                                for (Library lb : libraryByRange) {
                                    libByProject.add(lb);

                                }
                            }

                        }

                    }

                    break;
                case 0:
                    System.out.println("caso 0");
                    libByProject = null;
                    break;

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("Imprimimos lo que retorna libByProject: " + libByProject);
        return libByProject;
    }

    public void reset() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            disabledBtn = false;
            context.getExternalContext().redirect("../library/runLibraries.xhtml");

        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            projs.clear();
            usersList.clear();
            user = null;
            range = null;
            cleanListSelectionLibrary();
        }
    }

    public void checkSelectedRunLibraries() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (LibraryTable.isEmpty()) {
            context.addMessage(null, new FacesMessage("¡ERROR!", "No han seleccionado ninguna libreria para crear la corrida"));
        } else {

            boolean validateCompatiblePlataform = validatePlataform();
            //boolean validateCompatiblePlataform = true;

            boolean checkTags = true;
            String us = "";
            String Kit = "";
            String LibName = "";
            //Lista para guardar el nombre de la biblioteca
            List<String> listaLibraryName = new ArrayList();

            List<String> tags = new ArrayList<>();
            for (Library userLib : LibraryTable) {

                //Validamos si la biblioteca contiene barcodes;
                if (userLib.getIdBarcode1() == null) {
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('modulDialog').show();");
                    messageDialog = "No se puede crear corrida con la biblioteca: " + userLib.getLibraryName() + ", debido a que no contiene tag 1 y 2.";
                    return;
                }
                String valor = userLib.getIdBarcode1().getIndexName() + "-" + userLib.getIdBarcode1().getBasei7();
                // us=userLib.getLibrary().getUserName();
                //Kit=userLib.getLibrary().getKit();
                LibName = userLib.getLibraryName();
                if (userLib.getIdBarcode2() != null) {
                    valor = valor + getIndexBasei5View(userLib.getIdBarcode2(), userLib.getPlataformLinkKit().getPlataform().getPlataformName());
                }
                tags.add(valor);

                //Comprobamos que la biblioteca no sea repetida
                if (listaLibraryName.indexOf(userLib.getLibraryName()) >= 0) {
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('modulDialog').show();");
                    messageDialog = "Existen bibliotecas repetidas en la selección: ";
                    return;
                }
                listaLibraryName.add(userLib.getLibraryName());
            }
            Collections.sort(tags);

            for (int i = 0; i < tags.size() - 1; i++) {
                checkTags = true;
                if (tags.get(i).equals(tags.get(i + 1))) {

                    checkTags = false;
                    nameRepeted = tags.get(i);
                    break;
                }
            }
            if (checkTags) {
                if (validateCompatiblePlataform) {
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('DialogCreateRun').show()");
                } else {
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('dlgerror2').show();");
                }

            } else {
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('dlgerror').show();");
            }
        }
    }

    public boolean validatePlataform() {
        List<String> selectionTypePlatform = new ArrayList<>();
        for (Library itemLibrary : LibraryTable) {
            //selectionPlatform.add(itemLibrary.getPlataform());
            //Ahora estamos obteniendo una plataforma
            selectionTypePlatform.add(itemLibrary.getPlataformLinkKit().getPlataform().getNote());
        }
        boolean illumina;
        //Tomamos el primer item
        if (selectionTypePlatform.get(0).equals("ILLUMINA")) {
            illumina = true;
        } else {
            illumina = false;
        }

        if (illumina == true) {
            if (selectionTypePlatform.indexOf("OXFORD NANOPORE") >= 0) {
                System.out.println("illumina true: No se puede crear corrida con plataformas combinadas entre Illumina y Oxford Nanopore");
                return false;
            }
        } else {
            for (String itemPlataform : selectionTypePlatform) {
                if (!itemPlataform.equals("OXFORD NANOPORE")) {
                    System.out.println((itemPlataform != "Oxford Nanopore"));
                    System.out.println("Se imprime comparacion: " + itemPlataform);
                    System.out.println("illumina false: No se puede crear corrida con plataformas combinadas entre Illumina y Oxford Nanopore");
                    return false;
                }
            }
        }
        return true;
    }

    //Método que sirve para validar la creación de corridas con bibliotecas compatibles y valida que el rendimiento en el archivo CSV sea el indicado a la plataforma de la biblioteca
    public boolean isIllumina(String platform) {
        //Constantes para validar tamaños de secuencias
        List<String> listPlatform = new ArrayList<>();
        //Llenado de arreglos para validar
        listPlatform.add("NextSeq500");
        listPlatform.add("MiSeq");
        listPlatform.add("HiSeq");
        listPlatform.add("iSeq");
        listPlatform.add("NovaSeq");

        if (listPlatform.indexOf(platform) < 0) {
            return false;
        } else {
            return true;
        }
    }

    public void checkTags() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean checkTags = true;
        String us = "";
        String Kit = "";
        //"C:\\Users\\uusmb\\Desktop\\PE_MP_User_Fecha.csv"
        List<String> tags = new ArrayList<>();
        for (Library userLib : LibraryTable) {
            String valor = userLib.getIdBarcode1().toString();
            us = userLib.getUserName();
            //Kit = userLib.getKit();
            Kit = userLib.getPlataformLinkKit().getKit().getKitName();
            if (userLib.getIdBarcode2() != null) {

                valor = valor + userLib.getIdBarcode2().toString();

            }
            tags.add(valor);

        }
        Collections.sort(tags);

        for (int i = 0; i < tags.size() - 1; i++) {
            checkTags = true;
            if (tags.get(i).equals(tags.get(i + 1))) {

                checkTags = false;
                nameRepeted = tags.get(i);
                break;
            }
        }

        if (checkTags) {
        } else {
            System.out.println("tags repetidos");
            System.out.println(nameRepeted);
            context.execute("PF('dlgerror').show();");
        }
    }

    public String validateFormGenerarCorrida() {

        //reasignando valores con trim
        flowcell = flowcell.trim();

        Description = Description.trim();
        run1 = run1.trim();
        //run2 = run2.trim();

        if (scode.equals("---") || scode == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo 'Secuenciador' es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return null;
        } else if (corrida.equals("") || corrida == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo 'Número de corrida' es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return null;
        } else if (flowcell.equals("") || flowcell == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo 'Flowcell' es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return null;
        } else if (runDate == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo 'Fecha' es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return null;
        } else if (readType == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "Seleccione un Tipo de lectura";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return null;
        } else if (kitPerformance == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "Seleccione un Tipo de Rendimiento de kit";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return null;
            //} else if (Description.equals("") || Description == null) {
            //System.out.println("Existen campos vacíos");
            //messageDialog = "El campo descripción es obligatorio";
            // RequestContext rc = RequestContext.getCurrentInstance();
            //rc.execute("PF('dlgerrorcsvrepetido').show();");
            //return null;
        } else if (run1.equals("") || run1 == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo Ciclo 1 es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return null;
        } else {
            return "siguiente";
        }
    }

    public String createRun() {

        //Validación de los campos del formulario
        if (validateFormGenerarCorrida() == null) {
            return null;
        }
        //run2 = run1;


        /*Se obtiene la fecha junto con el dia de la insercion del registro*/
        System.out.println("entra al metodo createrun");
        String modifiedDate = new SimpleDateFormat("yyyy/MM/dd").format(runDate);
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String[] tiempo = time.toString().split("-");
        String[] dia = tiempo[2].split(" ");

        try {
            for (Library runLib : LibraryTable) {
                System.out.println(" ....................................... iteracion en createRun, runLib: ......................... INICIO");
                System.out.println("objeto runlib: " + runLib);
                System.out.println("Nombre biblioteca" + runLib.getLibraryName());
                System.out.println("barcode 1 antes de insertar set" + runLib.getIdBarcode1());
                System.out.println("barcode 2 antes de insertar set" + runLib.getIdBarcode2());

                /*   Dejamos de consultar los barcode 1 y 2 desde la vista ******************+
                System.out.println("Asignamos valor de los barcodes mediante vista ---------------------------------- *************+");
                ViewIdLibraryBarcodes12 vlb = ejbLib.finIdLibraryBarcodesByIdLibrary(runLib.getIdLibrary()).get(0);
                System.out.println("vlb id_library: " + vlb.getIdLibrary());
                System.out.println("vlb getIdIndex1: " + vlb.getIdIndex1());
                System.out.println("vlb getIdIndex2: " + vlb.getIdIndex2());

                System.out.println("------ Vista ViewBarcodes   ------------------");
                ViewBarcodes vBarcodes1 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);
                System.out.println("Objeto vBarcodes: " + vBarcodes1);
                System.out.println("Objeto vBarcodes.getIdIndex: " + vBarcodes1.getIdIndex());
                System.out.println("Objeto vBarcodes.getIdBarcode: " + vBarcodes1.getIdBarcode());
                System.out.println("Objeto vBarcodes.getKitName: " + vBarcodes1.getKitName());
                System.out.println("Objeto vBarcodes.getSequence: " + vBarcodes1.getSequence());
                System.out.println("------ Vista ViewBarcodes   ------------------");

                System.out.println("------ Barcodes barExp1   ------------------");
                Barcodes barExp1 = new Barcodes();
                barExp1.setIdIndex(vBarcodes1.getIdIndex());
                barExp1.setSequence(vBarcodes1.getSequence());
                barExp1.setIdBarcode(vBarcodes1.getIdBarcode());
                barExp1.setTagType(vBarcodes1.getTagType());
                barExp1.setKitName(vBarcodes1.getKitName());
                System.out.println("Objeto barExp1: " + barExp1);
                System.out.println("objeto barExp1 getIdBarcode: " + barExp1.getIdBarcode());
                System.out.println("objeto barExp1 getSequence: " + barExp1.getSequence());
                System.out.println("------ Barcodes barExp1   ------------------");

                System.out.println("------ Barcodes barExp2   ------------------");
                ViewBarcodes vBarcodes2 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);
                Barcodes barExp2 = new Barcodes();
                barExp2.setIdIndex(vBarcodes2.getIdIndex());
                barExp2.setSequence(vBarcodes2.getSequence());
                barExp2.setIdBarcode(vBarcodes2.getIdBarcode());
                barExp2.setTagType(vBarcodes2.getTagType());
                barExp2.setKitName(vBarcodes2.getKitName());
                System.out.println("Objeto barExp2: " + barExp2);
                System.out.println("objeto barExp2 getIdBarcode: " + barExp2.getIdBarcode());
                System.out.println("objeto barExp2 getSequence: " + barExp2.getSequence());
                System.out.println("------ Barcodes barExp2   ------------------");
                System.out.println("RunLib antes de asignar barcodes:" + runLib);
                runLib.setIdBarcode1(barExp1);
                runLib.setIdBarcode2(barExp2);
                System.out.println("RunLib después de asignar barcodes:" + runLib);
                System.out.println("Asignamos valor de los barcodes mediante vista ---------------------------------- *************+");

                System.out.println(" ....................................... iteracion en createRun, runLib: ......................... FINAL");
                
                 */
                if (!runLib.getPlataformLinkKit().getPlataform().getPlataformName().equals("Oxford Nanopore")) {
                    modifiedDate = new SimpleDateFormat("yy/MM/dd").format(runDate);
                    runName = modifiedDate.replaceAll("[/]", "") + "_" + scode + "_" + corrida + "_" + flowcell;
                    System.out.println("Volvemos a generar el nombre del archivo caso 1 *********************************************");
                } else {
                    runName = modifiedDate.replaceAll("[/]", "") + "_" + scode + "_" + corrida + "_" + flowcell;
                    System.out.println("Volvemos a generar el nombre del archivo caso 2 *********************************************");
                }
            }

            System.out.println("Etapa 1: Se cumple el primer ciclo for de Library runLib : LibraryTable  ----------------------------------------------------------------   *******************");

            //try para identificar el error de sample_sheet_name
            //búsqueda a la tabla de run para identificar el error...
            List<Run> listRun = ejbRun.getRunByRunName(runName);
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("run name:  " + runName);
            System.out.println("Tamaño del arreglo: " + listRun.size());
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Etapa 2: Se consulta, se guarda en lista y se imprime propiedades de List<Run> listRun = ejbRun.getRunByRunName(runName)  ----------------------------------------------------------------   *******************");

            if (listRun.isEmpty()) {
                System.out.println("Etapa 3: Entramos a la condicional listRun.isEmpty()  ----------------------------------------------------------------   *******************");

                Run runLibrary = new Run();
                runLibrary.setRunType(readType);
                runLibrary.setRunStartday(runDate);
                runLibrary.setRunFinishday(null);
                runLibrary.setSampleSheetName(null);
                runLibrary.setStatus("Preparing");
                runLibrary.setCycles(Integer.valueOf(run1));
                runLibrary.setPerformance(kitPerformance);
                runLibrary.setWorkSubdirectory(null);
                runLibrary.setRunName(runName);
                runLibrary.setSampleSheetName(runName + ".csv");
                //leslie 19 Agosto
                runLibrary.setEstatusArchivos(scode);
                ejbRun.create(runLibrary);

                System.out.println("Crea la corrida");
                System.out.println("Etapa 4: Se crea la biblioteca Run runLibrary = new Run();  ----------------------------------------------------------------   *******************");

                try {

                    for (Library runLib : LibraryTable) {
                        LibraryRunLink lrl = new LibraryRunLink();
                        lrl.setLibraryRunLinkPK(new jpa.entities.LibraryRunLinkPK());
                        lrl.getLibraryRunLinkPK().setIdLibrary(runLib.getIdLibrary());
                        lrl.getLibraryRunLinkPK().setIdRun(runLibrary.getIdRun());
                        lrl.setNote("");
                        //lrl.setPerformance(0);
                        lrl.setRealPerformance(0);
                        ejbLibraryRun.create(lrl);

                        System.out.println("Crea el link de la corrida");
                    }

                    System.out.println("Etapa 6: Se itera la seleccion de librerías y se crean las relaciones Library runLib : LibraryTable  ----------------------------------------------------------------   *******************");

                    System.out.println("Etapa 7: Llamamos y entramos al método  createSampleSheet() ----------------------------------------------------------------   *******************");
                    //LLamando método para crear el archivo
                    createSampleSheet(1, runLibrary.getIdRun(), null);
                    System.out.println("Etapa 8: Salidmos del método createSampleSheet() ----------------------------------------------------------------   *******************");

                    //limpiar formulario generar corrida
                    cleanFormCreateRun();

                    //Ejecutar metodo de reset implementado en la vista
                    resetValues();
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.getExternalContext().redirect("../run/runs.xhtml");
                } catch (Exception e) {
                    //limpiar formulario generar corrida
                    cleanFormCreateRun();
                    System.out.println("Ocurrio un error en el ciclo de creación de llaves foráneas, Info: " + e);
                }

            } else {
                messageDialog = "Ya existe un registro con el mismo nombre de corrida: " + runName;
                runName = null;
                //cleanFormCreateRun();
                //FacesContext context = FacesContext.getCurrentInstance();
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('dlgerrorcsvrepetido').show();");
                //context.addMessage(null, new FacesMessage("Nombre de archivo repetido"));

            }

        } catch (Exception e) {
            //limpiar formulario generar corrida
            cleanFormCreateRun();
            System.out.println("Ocurrio un error al crear la corrida, Info: " + e);
        }

        return null;
    }

    public String validateFormCreateSampleSheet() {
        //reasignando valores con trim
        Description = Description.trim();
        run1 = run1.trim();
        run2 = run2.trim();

        //Validación de campos
        System.out.println("Los variables son: -----------------------------------------------------");
        System.out.println("description: " + Description);
        System.out.println("readType: " + run1);
        System.out.println("kitPerformance: " + run2);

        System.out.println("Los variables son: -----------------------------------------------------");

        //if (Description.equals("") || Description == null) {
        //  System.out.println("Existen campos vacíos");
        //messageDialog = "El campo descripción es obligatorio";
        //RequestContext rc = RequestContext.getCurrentInstance();
        //rc.execute("PF('dlgerrorcsvrepetido').show();");
        //return null;
        //} else 
        if (run1.equals("") || run1 == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo Ciclo 1 es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return null;
        } else if (run2.equals("") || run2 == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo Ciclo 2 es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return null;
        } else {
            return "siguiente";
        }
    }

    public String deleteUserFromNameSample(String nameSample) {
        String newNameSample = "";
        int conteo = 0;
        for (int i = 0; i < nameSample.length(); i++) {
            if (conteo == 2) {
                for (int j = i; j < nameSample.length(); j++) {
                    newNameSample = newNameSample + nameSample.charAt(j);
                }
                break;
            } else {
                if (nameSample.charAt(i) == '_') {
                    conteo++;
                }
            }
        }
        System.out.println("newSample3: " + newNameSample);

        return newNameSample;
    }

    public String createSampleSheet(int caseOrigin, int idRun, Run runCurrent) throws FileNotFoundException {

        run2 = run1;

        //Validación de los campos del formulario
        System.out.println("CSS Etapa 1: Entra----------------------------------------------------------------   *******************");
        if (validateFormCreateSampleSheet() == null) {
            return null;
        }
        System.out.println("CSS Etapa 2: Sale de la validación exitosamente----------------------------------------------------------------   *******************");

        /*
        //reasignando valores con trim
        Description = Description.trim();
        run1 = run1.trim();
        run2 = run2.trim();

        //Validación de campos
        System.out.println("Los variables son: -----------------------------------------------------");
        System.out.println("description: " + Description);
        System.out.println("readType: " + run1);
        System.out.println("kitPerformance: " + run2);

        System.out.println("Los variables son: -----------------------------------------------------");

        if (Description.equals("") || Description == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo descripción es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return;
        } else if (run1.equals("") || run1 == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo Ciclo 1 es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return;
        } else if (run2.equals("") || run2 == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo Ciclo 2 es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgerrorcsvrepetido').show();");
            return;
        }
        
         */
        try {
            //Control para saber desde donde se ejecuta le método
            if (caseOrigin == 1) {//leslie: metodo ejecutado desde librray/runlibraries.xhtml =creacion corrida desde 0
                //metodos para buscar las librerías de la corrida creada
                System.out.println("CSS Etapa 3: Entramos en la condición caseOrigin----------------------------------------------------------------   *******************");

                /*
                System.out.println("CSS Etapa 4: Usamos la vista para obtener el arreglo de lasbibliotecas----------------------------------------------------------------   *******************");
                //Usamos vistas para obetejer el objeto de las bibliotecas, solución a problema de registros null
                List<Library> listLibrary = new ArrayList<>();
                //Usamos vista para obtener registros de la tabla LibraryRunLink enviando id de corrida como parámetro
                List<ViewIdLibraryLibraryRunLink> list = getFacade().findIdLibrariesByIdRun(idRun);
                for (ViewIdLibraryLibraryRunLink viewLibrary : list) {
                    System.out.println(" objeto ViewIdLibraryLibraryRunLink: " + viewLibrary);
                    System.out.println(" id_library de ViewIdLibraryLibraryRunLink: " + viewLibrary.getIdLibrary());
                    //Creamoe variable de tipo Library
                    Library lib = new Library();
                    //lib = ejbFacadeLibrary.findLibraryByIdLibrary(viewLibrary.getIdLibrary()).get(0);
                    //asignamos valor de la consulta de biblioteca por medio del parámetro id de biblioteca de la vista anter creada
                    lib = ejbLib.find(viewLibrary.getIdLibrary());
                    
                    System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");
                    //Consultamos desde la vista los id de biblioteca y barcodes 1 y 2
                    ViewIdLibraryBarcodes12 vlb = ejbLib.finIdLibraryBarcodesByIdLibrary(viewLibrary.getIdLibrary()).get(0);

                    System.out.println("------ Vista ViewBarcodes   ------------------");
                    //consultamos la vista de barcode para obtener sus propiedades
                    ViewBarcodes vBarcodes1 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);

                    System.out.println("------ Barcodes barExp1   ------------------");
                    //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag1
                    Barcodes barExp1 = new Barcodes();
                    barExp1.setIdIndex(vBarcodes1.getIdIndex());
                    barExp1.setSequence(vBarcodes1.getSequence());
                    barExp1.setIdBarcode(vBarcodes1.getIdBarcode());
                    barExp1.setTagType(vBarcodes1.getTagType());
                    barExp1.setKitName(vBarcodes1.getKitName());
                    System.out.println("------ Barcodes barExp1   ------------------");

                    System.out.println("------ Barcodes barExp2   ------------------");
                    //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag2
                    ViewBarcodes vBarcodes2 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);
                    Barcodes barExp2 = new Barcodes();
                    barExp2.setIdIndex(vBarcodes2.getIdIndex());
                    barExp2.setSequence(vBarcodes2.getSequence());
                    barExp2.setIdBarcode(vBarcodes2.getIdBarcode());
                    barExp2.setTagType(vBarcodes2.getTagType());
                    barExp2.setKitName(vBarcodes2.getKitName());
                    System.out.println("------ Barcodes barExp2   ------------------");
                    System.out.println("Biblioteca antes de asignar barcodes:" + lib);
                    //Asignamos los barcodes 1 (tag1) y barcodes 2 (tag2) 
                    lib.setIdBarcode1(barExp1);
                    lib.setIdBarcode2(barExp2);
                    System.out.println("Biblioteca después de asignar barcodes:" + lib);
                    System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");

                    //Agregamos la bilbioteca a la lista
                    listLibrary.add(lib);
                    //listLibrary.set(selectedItemIndex, lib);
                    //listLibrary.add(lib);
                }
                
                 */
                System.out.println("CSS Etapa 5: Pasamos la vista para obtener el arreglo de lasbibliotecas----------------------------------------------------------------   *******************");

                List<Library> librerias = new ArrayList<>();
                //aqui consulto con getLibraryRunLinkByIdRun que me da la lista ordenada por id_sample atraves de un leftjoin 
                List<LibraryRunLink> libraryRun = ejbLibraryRun.getLibraryRunLinkByIdRun(idRun);
                for (int i = 0; i < libraryRun.size(); i++) {

                    /*
                    //Validamos si la biblioteca contiene barcodes;
                if (libraryRun.get(i).getLibrary().getIdBarcode1() == null) {
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('modulDialog').show();");
                    messageDialog = "No se puede crear el sampleSheet, la biblioteca "+libraryRun.get(i).getLibrary().getLibraryName()+", no contiene tag 1 y 2.";
                    System.out.println("la bilbioteca no contiene barcodes 1");
                    return null;
                }
                     */
                    System.out.println("Resultado de la iteracion: " + libraryRun.get(i));
                    System.out.println("id de Libreria: " + libraryRun.get(i).getLibraryRunLinkPK().getIdLibrary());
                    System.out.println("objeto libreria: " + ejbLib.find(libraryRun.get(i).getLibraryRunLinkPK().getIdLibrary()));
                    Library lib = ejbLib.getLibraryByIdLibrary(libraryRun.get(i).getLibraryRunLinkPK().getIdLibrary()).get(0);

                    /*
                    System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");
                    //Consultamos desde la vista los id de biblioteca y barcodes 1 y 2
                    ViewIdLibraryBarcodes12 vlb = ejbLib.finIdLibraryBarcodesByIdLibrary(lib.getIdLibrary()).get(0);

                    System.out.println("------ Vista ViewBarcodes   ------------------");
                    //consultamos la vista de barcode para obtener sus propiedades
                    ViewBarcodes vBarcodes1 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);

                    System.out.println("------ Barcodes barExp1   ------------------");
                    //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag1
                    Barcodes barExp1 = new Barcodes();
                    barExp1.setIdIndex(vBarcodes1.getIdIndex());
                    barExp1.setSequence(vBarcodes1.getSequence());
                    barExp1.setIdBarcode(vBarcodes1.getIdBarcode());
                    barExp1.setTagType(vBarcodes1.getTagType());
                    barExp1.setKitName(vBarcodes1.getKitName());
                    System.out.println("------ Barcodes barExp1   ------------------");

                    System.out.println("------ Barcodes barExp2   ------------------");
                    //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag2
                    ViewBarcodes vBarcodes2 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);
                    Barcodes barExp2 = new Barcodes();
                    barExp2.setIdIndex(vBarcodes2.getIdIndex());
                    barExp2.setSequence(vBarcodes2.getSequence());
                    barExp2.setIdBarcode(vBarcodes2.getIdBarcode());
                    barExp2.setTagType(vBarcodes2.getTagType());
                    barExp2.setKitName(vBarcodes2.getKitName());
                    System.out.println("------ Barcodes barExp2   ------------------");
                    System.out.println("Biblioteca antes de asignar barcodes:" + lib);
                    //Asignamos los barcodes 1 (tag1) y barcodes 2 (tag2) 
                    lib.setIdBarcode1(barExp1);
                    lib.setIdBarcode2(barExp2);
                    System.out.println("Biblioteca después de asignar barcodes:" + lib);
                    System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");
                     */
                    librerias.add(i, lib); //agrega a la lista la posiicon y el objeto
                    //libRun.add(libraryRun.get(i));
                    //System.out.println("idLibrary: "+libraryRun.get(i).getLibrary().getIdLibrary());
                    //libRun.add(ejbLib.find(libraryRun.get(i).getLibrary().getIdLibrary()));
                }

                libRun = new ArrayList<>();
                libRun = librerias;

                //aqui puedo ordenarlas pero solo por id_library //leslie 22 agos
                //Collections.sort(libRun, (Library a,Library b) -> a.getIdLibrary().compareTo(b.getIdLibrary()));
                //desde aqui debo sortearlas
                System.out.println("CSS Etapa 6: Se asigna a libRun = listLibrary ----------------------------------------------------------------   *******************");
                System.out.println("Contenido libRun ------------------------------------**************");
                System.out.println(libRun);
                System.out.println("Contenido libRun ------------------------------------**************");

                getRunName(ejbRun.find(idRun));
                System.out.println("CSS Etapa 7: Se ejecuta el método getRunName(ejbRun.find(idRun)) para obtener valores ----------------------------------------------------------------   *******************");

            }
        } catch (Exception e) {
            System.out.println("Error al recuperar la lista de librerias de la corrida: " + e.getMessage());
        }

        FacesContext con = FacesContext.getCurrentInstance();
        Users us = (Users) con.getExternalContext().getSessionMap().get("usuario");

        System.out.println("nameRun: " + NameRun);
        FacesContext context = FacesContext.getCurrentInstance();
        String Project = null;
        String SamName = null;
        String Kit = "";
        String plataform = "";
        String rName = "";
        String Scode = "";
        String Index = "";
        //leslie 22 agosto : 
        String namelibrary = "";
        String LibName = "";
        try {
            for (Library libRuns : libRun) {

                /*
                //Validamos si la biblioteca contiene barcodes;
                if (libRuns.getIdBarcode1() == null) {
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('modulDialog').show();");
                    messageDialog = "No se puede crear el sampleSheet, la biblioteca "+libRuns.getLibraryName()+", no contiene tag 1 y 2.";
                    System.out.println("la bilbioteca no contiene barcodes 2");
                    return null;
                }
                 */
                System.out.println("CSS Etapa 8: Entramos al ciclo for (Library libRuns : libRun)  ----------------------------------------------------------------   *******************");
                System.out.println("-----------------------------------------------------------");
                System.out.println("Iterando libreria: " + libRun);
                System.out.println("-----------------------------------------------------------");
                List<SampleLibraryLink> runLib = getFacade().findRuns(libRuns.getIdLibrary());
                //leslie 22 agosto aqui itero el orden en el metodo finRuns 
                //System.out.println("Valor de objeto runLib: "+ runLib);
                /*
                 System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");
                //Consultamos desde la vista los id de biblioteca y barcodes 1 y 2
                ViewIdLibraryBarcodes12 vlb = ejbLib.finIdLibraryBarcodesByIdLibrary(libRuns.getIdLibrary()).get(0);

                System.out.println("------ Vista ViewBarcodes   ------------------");
                //consultamos la vista de barcode para obtener sus propiedades
                ViewBarcodes vBarcodes1 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);

                System.out.println("------ Barcodes barExp1   ------------------");
                //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag1
                Barcodes barExp1 = new Barcodes();
                barExp1.setIdIndex(vBarcodes1.getIdIndex());
                barExp1.setSequence(vBarcodes1.getSequence());
                barExp1.setIdBarcode(vBarcodes1.getIdBarcode());
                barExp1.setTagType(vBarcodes1.getTagType());
                barExp1.setKitName(vBarcodes1.getKitName());
                System.out.println("------ Barcodes barExp1   ------------------");

                System.out.println("------ Barcodes barExp2   ------------------");
                //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag2
                ViewBarcodes vBarcodes2 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);
                Barcodes barExp2 = new Barcodes();
                barExp2.setIdIndex(vBarcodes2.getIdIndex());
                barExp2.setSequence(vBarcodes2.getSequence());
                barExp2.setIdBarcode(vBarcodes2.getIdBarcode());
                barExp2.setTagType(vBarcodes2.getTagType());
                barExp2.setKitName(vBarcodes2.getKitName());
                System.out.println("------ Barcodes barExp2   ------------------");
                System.out.println("Biblioteca antes de asignar barcodes:" + libRuns);
                //Asignamos los barcodes 1 (tag1) y barcodes 2 (tag2) 
                libRuns.setIdBarcode1(barExp1);
                libRuns.setIdBarcode2(barExp2);
                System.out.println("Biblioteca después de asignar barcodes:" + libRuns);
                System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");

                 */
 /*      Eliminando consulta con método de vista de ViewSampleLibraryLink
                //Eliminando join con valores nullos   -------------------------------------------------- Inicio
                //Consultamos en la tabla SampleLibraryLink los registros que tengan el id de la biblioteca mediante la vista
                List<ViewSampleLibraryLink> runLibView = ejbSll.findRunByIdLibrary(libRuns.getIdLibrary());
                SampleLibraryLink runSLL = new SampleLibraryLink();
                //variable runSLL se asigna el valor de la consulta con parámetros de id's obtenidos mediante la vista
                runSLL = ejbSll.findRunByIdSampleIdLibrary(runLibView.get(0).getIdSample(), runLibView.get(0).getIdLibrary()).get(0);
                //Creamos vairbale del tipo Library
                Library lib = new Library();
                //Variable lib se asigna el valor de consulta de biblioteca por medio del id de la vista
                lib = ejbLib.find(runLibView.get(0).getIdLibrary());
                //Agregamos las bibliotecas a la lista
                runSLL.setLibrary(lib);
                //Creamos variable del tipo Sample
                Sample sample = new Sample();
                //Variable sample se asigna el valor de consulta de biblioteca por medio del id de la vista
                sample = ejbSam.find(runLibView.get(0).getIdSample());
                //Se asigna el valor de las variables al runSLL mediante el set
                runSLL.setLibrary(lib);
                runSLL.setSample(sample);
                List<SampleLibraryLink> runLib = new ArrayList<>();
                //runSLL.getLibrary().setIdBarcode1(barExp1);
                //runSLL.getLibrary().setIdBarcode2(barExp2);
                //Agregamos el valor del SLL a la litsa runLib
                runLib.add(runSLL);
                System.out.println("obtener id de biblioteca: " + libRuns.getIdLibrary());
                System.out.println("objeto SampleLibraryLink encontrado: " + runLib);
                //Eliminando join con valores nullos  --------------------------------------------------- Final
                System.out.println("obtener id de biblioteca: " + libRuns.getIdLibrary());
                System.out.println("objeto SampleLibraryLink encontrado: " + runLib);
                
                 */
                for (SampleLibraryLink runs : runLib) {
                    //Kit = runs.getLibrary().getKit();
                    Kit = runs.getLibrary().getPlataformLinkKit().getKit().getKitName();
                    //plataform = runs.getLibrary().getPlataform();
                    plataform = runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName();
                    //leslie22 agosto
                    namelibrary = runs.getLibrary().getLibraryName().toString();
                    if (plataform.equals("Oxford Nanopore")) {
                        rName = NameRun.substring(18, 21);
                        Scode = NameRun.substring(9, 17);
                    } else {
                        rName = NameRun.substring(16, 18);
                        Scode = NameRun.substring(7, 15);
                    }

                    break;
                }

                System.out.println("Se imprime valor de variable rName: " + rName);
                System.out.println("Se imprime valor de variable Scode: " + Scode);
            }

            System.out.println("CSS Etapa 9: Pasamos exitosamente el ciclo for (Library libRuns : libRun)  ----------------------------------------------------------------   *******************");

            String modifiedDate = new SimpleDateFormat("yyyy/MM/dd").format(runDate);

            //time.toString().replaceAll("[\\-\\:\\.\\ ]", "")
            //ExperimentName = modifiedDate.replaceAll("[/]", "") + "_" + scode + "_" + corrida + "_" + flowcell;
            String SAMPLE_CSV_FILE = NameRun + ".csv";
            //String SAMPLE_CSV_FILE = ExperimentName + "_samplesheet.csv";
            //FileOutputStream fos= new FileOutputStream("/var/www/html/sampleFiles/SampleSheetFiles/"+SAMPLE_CSV_FILE,true);

            FileOutputStream fos = null;
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String[] tiempo = time.toString().split("-");
            String[] dia = tiempo[2].split(" ");
            String[] min = dia[1].split(":");
            String[] segs = min[2].split("\\.");
            String fecha = tiempo[0] + "_" + tiempo[1] + "_" + dia[0] + "_" + min[0] + "_" + min[1] + "_" + segs[0];

            String url = pathCreateSampleFiles;
            File file = new File(url + SAMPLE_CSV_FILE);
            File folder = new File(url);

            fos = new FileOutputStream(file, false);

            String flow = NameRun.replaceAll(".*_", "");

            try (PrintWriter pw = new PrintWriter(fos)) {
                pw.println("[Header],,,,,,,,,");
                pw.println("IEMFileVersion,4,,,,,,,,");
                pw.println("Investigator Name," + us.getUserName() + ",,,,,,,,");
                pw.println("Experiment Name," + NameRun + ",,,,,,,,");
                pw.println("Date," + modifiedDate + ",,,,,,,,");
                pw.println("WorkFlow,GenerateFASTQ,,,,,,,,");
                //pw.println("Application,NextSeq FASTQ Only,,,,,,,,");
                //leslie 22 agosto
                pw.println("Application," + plataform + "FASTQ Only,,,,,,,,");
                //pw.println("Application,FASTQ Only,,,,,,,,");
                pw.println("Assay," + Kit + ",,,,,,,,");
                pw.println("Description," + Description + ",,,,,,,,");
                pw.println("Chemistry,Amplicon,,,,,,,,");
                pw.println("Flowcell Version," + kitPerformance + ",,,,,,,,");
                pw.println("Run," + rName + ",,,,,,,,");
                pw.println("Scode," + Scode + ",,,,,,,,");

                pw.println("[Reads],,,,,,,,,");
                pw.println(String.valueOf(run1).replaceAll("null", "") + ",,,,,,,,,");
                pw.println(String.valueOf(run2).replaceAll("null", "") + ",,,,,,,,,");

                pw.println("[Settings],,,,,,,,,");

                pw.println(",,,,,,,,,");

                pw.println("[Data],,,,,,,,,");

                if (plataform.equals("Oxford Nanopore")) {
                    // pw.println("ProjectID,Locate,Barcode,Sample_Id,Sample_Name,kit,FlowcellVersion");
                    //leslie 22 agosto edit Sample_Name x Library_Name ,Sample_ID  tiene como dato el mismo que LibraryName
                    pw.println("Sample_ID,Library_Name,Sample_Plate,Sample_Well,Index_Plate,Index_Plate_Well,I7_Index_ID,index,I5_Index_ID,index2,Sample_Project,"
                            + "Description,Rendimiento real(M),Tamanio biblioteca,Rendimiento solicitado(M),abs_260_280,abs_260_230,"
                            + "Locate,kit,id_sample,Aceptation,Comprobante_Pago,Cotizacion,Analisis BioInfo");

                    /*-----------------*/
                    for (Library libRuns : libRun) {
                        List<SampleLibraryLink> runLib = getFacade().findRuns(libRuns.getIdLibrary());

                        /*   Eliminamos la cosnulta de vistas de barcodes 1 y 2 
                        System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");
                        //Consultamos desde la vista los id de biblioteca y barcodes 1 y 2
                        ViewIdLibraryBarcodes12 vlb = ejbLib.finIdLibraryBarcodesByIdLibrary(libRuns.getIdLibrary()).get(0);

                        System.out.println("------ Vista ViewBarcodes   ------------------");
                        //consultamos la vista de barcode para obtener sus propiedades
                        ViewBarcodes vBarcodes1 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);

                        System.out.println("------ Barcodes barExp1   ------------------");
                        //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag1
                        Barcodes barExp1 = new Barcodes();
                        barExp1.setIdIndex(vBarcodes1.getIdIndex());
                        barExp1.setSequence(vBarcodes1.getSequence());
                        barExp1.setIdBarcode(vBarcodes1.getIdBarcode());
                        barExp1.setTagType(vBarcodes1.getTagType());
                        barExp1.setKitName(vBarcodes1.getKitName());
                        System.out.println("------ Barcodes barExp1   ------------------");

                        System.out.println("------ Barcodes barExp2   ------------------");
                        //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag2
                        ViewBarcodes vBarcodes2 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);
                        Barcodes barExp2 = new Barcodes();
                        barExp2.setIdIndex(vBarcodes2.getIdIndex());
                        barExp2.setSequence(vBarcodes2.getSequence());
                        barExp2.setIdBarcode(vBarcodes2.getIdBarcode());
                        barExp2.setTagType(vBarcodes2.getTagType());
                        barExp2.setKitName(vBarcodes2.getKitName());
                        System.out.println("------ Barcodes barExp2   ------------------");
                        System.out.println("Biblioteca antes de asignar barcodes:" + libRuns);
                        //Asignamos los barcodes 1 (tag1) y barcodes 2 (tag2) 
                        libRuns.setIdBarcode1(barExp1);
                        libRuns.setIdBarcode2(barExp2);
                        System.out.println("Biblioteca después de asignar barcodes:" + libRuns);
                        System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");
                        
                        //Eliminando consultas para eliminar join nulos de ViewSampleLibraryLink
                        
                        //Eliminando join con valores nullos   -------------------------------------------------- Inicio
                        //Consultamos en la tabla SampleLibraryLink los registros que tengan el id de la biblioteca mediante la vista
                        List<ViewSampleLibraryLink> runLibView = ejbSll.findRunByIdLibrary(libRuns.getIdLibrary());
                        SampleLibraryLink runSLL = new SampleLibraryLink();
                        //variable runSLL se asigna el valor de la consulta con parámetros de id's obtenidos mediante la vista
                        runSLL = ejbSll.findRunByIdSampleIdLibrary(runLibView.get(0).getIdSample(), runLibView.get(0).getIdLibrary()).get(0);
                        //Creamos vairbale del tipo Library
                        Library lib = new Library();
                        //Variable lib se asigna el valor de consulta de biblioteca por medio del id de la vista
                        lib = ejbLib.find(runLibView.get(0).getIdLibrary());
                        //Agregamos las bibliotecas a la lista
                        runSLL.setLibrary(lib);
                        //Creamos variable del tipo Sample
                        Sample sample = new Sample();
                        //Variable sample se asigna el valor de consulta de biblioteca por medio del id de la vista
                        sample = ejbSam.find(runLibView.get(0).getIdSample());
                        //Se asigna el valor de las variables al runSLL mediante el set
                        runSLL.setLibrary(lib);
                        runSLL.setSample(sample);
                        List<SampleLibraryLink> runLib = new ArrayList<>();
                        runSLL.getLibrary().setIdBarcode1(barExp1);
                        runSLL.getLibrary().setIdBarcode2(barExp2);
                        //Agregamos el valor del SLL a la litsa runLib
                        runLib.add(runSLL);
                        System.out.println("obtener id de biblioteca: " + libRuns.getIdLibrary());
                        System.out.println("objeto SampleLibraryLink encontrado: " + runLib);
                        //Eliminando join con valores nullos  --------------------------------------------------- Final
                        
                         */
                        for (SampleLibraryLink runs : runLib) {
                            System.out.println("id del proyecto: " + runs.getSample().getIdProject());
                            //System.out.println("kit: " + runs.getLibrary().getKit());
                            System.out.println("kit: " + runs.getLibrary().getPlataformLinkKit().getKit());
                            List<Sample> findAllSamples = ejbSam.findSamplebySampleSheet(runs.getSample().getIdSample());
                            Project = runs.getSample().getIdProject().getIdProject();
                            SamName = runs.getSample().getSampleName();
                            //leslie 22 agosto para obtener el nombre de la biblioteca 
                            LibName = runs.getLibrary().getLibraryName();
                            String libm = Normalizer.normalize(LibName, Normalizer.Form.NFD);
                            String FormatedLibrary = libm.replaceAll("[^\\p{ASCII}]", "");
                            String newFromatedLibrary = deleteUserFromNameSample(FormatedLibrary);
                            //fin leslie
                            String sm = Normalizer.normalize(SamName, Normalizer.Form.NFD);
                            String Pj = Normalizer.normalize(Project, Normalizer.Form.NFD);
                            String Formated = Pj.replaceAll("[^\\p{ASCII}]", "");
                            Formated = Formated.replace(":", "_");
                            Formated = Formated.replace("-", "_");
                            String FormatedSample = sm.replaceAll("[^\\p{ASCII}]", "");
                            String newFormatedSample = deleteUserFromNameSample(FormatedSample);
                            //if (runs.getLibrary().getKit().equals("IDT-ILMN Nextera DNA UD")) {
                            if (runs.getLibrary().getPlataformLinkKit().getKit().getKitName().equals("IDT-ILMN Nextera DNA UD")) {
                                //Index = runs.getLibrary().getIdBarcode1().getIdBarcode();
                                Index = runs.getLibrary().getIdBarcode1().getIndexName();
                            } else {
                                Index = "";
                            }

                            pw.println(
                                    /* runs.getSample().getIdProject().getIdProject()+","+""+","+runs.getLibrary().getIdBarcode1().getIdBarcode()+","+
                                runs.getSample().getIdSample()+","+runs.getSample().getSampleName()+","+runs.getLibrary().getKit()+","+""*/
                                    //leslie 22 agosto edit newFormatedSample x newFromatedLibrary
                                    newFromatedLibrary + "," + newFromatedLibrary + "," + "" + "," + "" + "," + Index.replaceAll("[A-H]*[0-9]*_", "") + "," + Index.replaceAll("_.*", "")
                                    + "," + runs.getLibrary().getIdBarcode1().getIndexName().replace("-", "_") + "," + runs.getLibrary().getIdBarcode1().getBasei7().replace("-", "_")
                                    + "," + "" + "," + "" + "," + Formated + "," + String.valueOf(runs.getSample().getComments()).replaceAll("null", "") + ","
                                    + "" + "," + "" + "," + (isIllumina(runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName()) ? String.valueOf(runs.getSample().getExpectedPerformance()).replaceAll("null", "") : String.valueOf(runs.getSample().getExpectedPerformanceOxford()).replaceAll("null", "")) + "," + String.valueOf(runs.getSample().getAbs260_280_usmb()).replaceAll("null", "") + ","
                                    + String.valueOf(runs.getSample().getAbs260_230_usmb()).replaceAll("null", "") + "," + "" + "," + runs.getLibrary().getPlataformLinkKit().getKit().getKitName().replace(" ", "_") + "," + runs.getSample().getIdSample()
                                    + "," + runs.getSample().getAceptation() + "," + checkPaymentProj(runs.getSample().getIdProject().getIdProject()) + ","
                                    + checkQuatationProj(runs.getSample().getIdProject().getIdProject()) + ","
                                    + getSamplesProj(runs.getSample().getIdProject())
                            );

                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            // Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                            for (Sample sam : findAllSamples) {

                                if (!sam.getStatus().equals("Basecalling y QC en proceso")) {
                                    System.out.println("Nombre de la muestra: " + sam.getSampleName());

                                    /* Comments commentsSample = new Comments();
                            commentsSample.setComment("Estatus cambia de -" + sam.getStatus() + "- a -" + "Basecalling y QC en proceso" + "- por " + us.getUserName());
                            commentsSample.setIdType(sam.getIdSample() + "");
                            commentsSample.setType("Sample");
                            commentsSample.setUserName("SISBI");
                            commentsSample.setCommentDate(timestamp);

                            commentFac.createComment(commentsSample);
                            
                            sam.setStatus("Basecalling y QC en proceso");
                            ejbSam.edit(sam);*/
                                }

                            }

                        }
                    }

                    pw.close();

                } else {
                    //leslie 22 agosto edit Sample_Name x Library_Name ,Sample_ID  tiene como dato el mismo que LibraryName
                    pw.println("Sample_ID,Library_Name,Sample_Plate,Sample_Well,Index_Plate,Index_Plate_Well,I7_Index_ID,index,I5_Index_ID,index2,Sample_Project,"
                            + "Description,Rendimiento real(M),Tamanio biblioteca,Rendimiento solicitado(M),abs_260_280,abs_260_230,"
                            + "Locate,kit,id_sample,Aceptation,Comprobante_Pago,Cotizacion,Analisis BioInfo");
                    System.out.println("Entra a la condicional");

                    for (Library libRuns : libRun) {
                        List<SampleLibraryLink> runLib = getFacade().findRuns(libRuns.getIdLibrary());
                        /*
                        System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");
                        //Consultamos desde la vista los id de biblioteca y barcodes 1 y 2
                        ViewIdLibraryBarcodes12 vlb = ejbLib.finIdLibraryBarcodesByIdLibrary(libRuns.getIdLibrary()).get(0);

                        System.out.println("------ Vista ViewBarcodes   ------------------");
                        //consultamos la vista de barcode para obtener sus propiedades
                        ViewBarcodes vBarcodes1 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);

                        System.out.println("------ Barcodes barExp1   ------------------");
                        //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag1
                        Barcodes barExp1 = new Barcodes();
                        barExp1.setIdIndex(vBarcodes1.getIdIndex());
                        barExp1.setSequence(vBarcodes1.getSequence());
                        barExp1.setIdBarcode(vBarcodes1.getIdBarcode());
                        barExp1.setTagType(vBarcodes1.getTagType());
                        barExp1.setKitName(vBarcodes1.getKitName());
                        System.out.println("------ Barcodes barExp1   ------------------");

                        System.out.println("------ Barcodes barExp2   ------------------");
                        //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag2
                        ViewBarcodes vBarcodes2 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);
                        Barcodes barExp2 = new Barcodes();
                        barExp2.setIdIndex(vBarcodes2.getIdIndex());
                        barExp2.setSequence(vBarcodes2.getSequence());
                        barExp2.setIdBarcode(vBarcodes2.getIdBarcode());
                        barExp2.setTagType(vBarcodes2.getTagType());
                        barExp2.setKitName(vBarcodes2.getKitName());
                        System.out.println("------ Barcodes barExp2   ------------------");
                        System.out.println("Biblioteca antes de asignar barcodes:" + libRuns);
                        //Asignamos los barcodes 1 (tag1) y barcodes 2 (tag2) 
                        libRuns.setIdBarcode1(barExp1);
                        libRuns.setIdBarcode2(barExp2);
                        System.out.println("Biblioteca después de asignar barcodes:" + libRuns);
                        System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");

                        //List<SampleLibraryLink> runLib = getFacade().findRuns(libRuns.getIdLibrary());
                        //Eliminando join con valores nullos   -------------------------------------------------- Inicio
                        //Consultamos en la tabla SampleLibraryLink los registros que tengan el id de la biblioteca mediante la vista
                        List<ViewSampleLibraryLink> runLibView = ejbSll.findRunByIdLibrary(libRuns.getIdLibrary());
                        SampleLibraryLink runSLL = new SampleLibraryLink();
                        //variable runSLL se asigna el valor de la consulta con parámetros de id's obtenidos mediante la vista
                        runSLL = ejbSll.findRunByIdSampleIdLibrary(runLibView.get(0).getIdSample(), runLibView.get(0).getIdLibrary()).get(0);
                        //Creamos vairbale del tipo Library
                        Library lib = new Library();
                        //Variable lib se asigna el valor de consulta de biblioteca por medio del id de la vista
                        lib = ejbLib.find(runLibView.get(0).getIdLibrary());
                        //Agregamos las bibliotecas a la lista
                        runSLL.setLibrary(lib);
                        //Creamos variable del tipo Sample
                        Sample sample = new Sample();
                        //Variable sample se asigna el valor de consulta de biblioteca por medio del id de la vista
                        sample = ejbSam.find(runLibView.get(0).getIdSample());
                        //Se asigna el valor de las variables al runSLL mediante el set
                        runSLL.setLibrary(lib);
                        runSLL.setSample(sample);
                        List<SampleLibraryLink> runLib = new ArrayList<>();
                        runSLL.getLibrary().setIdBarcode1(barExp1);
                        runSLL.getLibrary().setIdBarcode2(barExp2);
                        //Agregamos el valor del SLL a la litsa runLib
                        runLib.add(runSLL);
                        System.out.println("obtener id de biblioteca: " + libRuns.getIdLibrary());
                        System.out.println("objeto SampleLibraryLink encontrado: " + runLib);
                        //Eliminando join con valores nullos  --------------------------------------------------- Final
                        
                         */

                        for (SampleLibraryLink runs : runLib) {
                            System.out.println("id del proyecto: " + runs.getSample().getIdProject());
                            //System.out.println("kit: " + runs.getLibrary().getKit());
                            Project = runs.getSample().getIdProject().getIdProject();
                            SamName = runs.getSample().getSampleName();
                            //leslie 22 agosto para obtener el nombre de la biblioteca 
                            LibName = runs.getLibrary().getLibraryName();
                            String libm = Normalizer.normalize(LibName, Normalizer.Form.NFD);
                            String FormatedLibrary = libm.replaceAll("[^\\p{ASCII}]", "");
                            String newFromatedLibrary = deleteUserFromNameSample(FormatedLibrary);
                            //fin leslie                           
                            String sm = Normalizer.normalize(SamName, Normalizer.Form.NFD);
                            String Pj = Normalizer.normalize(Project, Normalizer.Form.NFD);
                            String Formated = Pj.replaceAll("[^\\p{ASCII}]", "");
                            Formated = Formated.replace(":", "_");
                            Formated = Formated.replace("-", "_");
                            String FormatedSample = sm.replaceAll("[^\\p{ASCII}]", "");
                            String barcode1 = "";
                            String barcode2 = "";
                            String barcode1Sec = "";
                            String barcode2Sec = "";
                            String newFormatedSample = deleteUserFromNameSample(FormatedSample);
                            if (runs.getLibrary().getPlataformLinkKit().getKit().getKitName().equals("IDT-ILMN Nextera DNA UD")) {
                                //Index = runs.getLibrary().getIdBarcode1().getIdBarcode();
                                Index = runs.getLibrary().getIdBarcode1().getIndexName();
                            } else {
                                Index = "";
                            }

                            //barcode1 = runs.getLibrary().getIdBarcode1().getIdBarcode().replace("-", "_");
                            barcode1 = runs.getLibrary().getIdBarcode1().getIndexName().replace("-", "_");
                            //barcode1Sec = runs.getLibrary().getIdBarcode1().getSequence().replace("-", "_");
                            barcode1Sec = runs.getLibrary().getIdBarcode1().getBasei7().replace("-", "_");

                            try {
                                /*
                                barcode2 = runs.getLibrary().getIdBarcode2().getIndexName().replace("-", "_");
                                barcode2Sec = runs.getLibrary().getIdBarcode2().getBasei7().replace("-", "_");
                                 */
                                if (getIndexBasei5View(runs.getLibrary().getIdBarcode2(), runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName()) == null) {
                                    barcode2 = "";
                                    barcode2Sec = "";
                                } else {

                                    //Verificamos el tipo de plataforma 
                                    if (runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName().equals("HiSeq") || runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName().equals("MiSeq") || runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName().equals("Oxford Nanopore")) {
                                        barcode2 = runs.getLibrary().getIdBarcode2().getIndexName().replace("-", "_");
                                        barcode2Sec = runs.getLibrary().getIdBarcode2().getBasei5Miseq().replace("-", "_");
                                        System.out.println("la plataforma es hiseq/miseq, el barcode index 5 forward" + barcode2Sec);
                                        //leslie 22 agosto 2024: agrege la opcion de iseq y cambie minION por oxford
                                    } else if (runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName().equals("NovaSeq") || runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName().equals("NextSeq500") || runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName().equals("iSeq")) {
                                        barcode2 = runs.getLibrary().getIdBarcode2().getIndexName().replace("-", "_");
                                        barcode2Sec = runs.getLibrary().getIdBarcode2().getBasei5Nextseq().replace("-", "_");
                                        System.out.println("la plataforma es novaseq/nextseq,iseq, el barcode index 5 reverse forward" + barcode2Sec);
                                    }
                                    System.out.println("El barcode 2 NO es nulo");
                                }

                            } catch (Exception ex) {
                                barcode2 = "";
                                barcode2Sec = "";
                                System.out.println("El barcode 2 es nulo");
                            }

                            pw.println(
                                    //leslie 22 agosto cambie newFormatedSample x newFromatedLibrary
                                    newFromatedLibrary + "," + newFromatedLibrary + "," + "" + "," + "" + "," + Index.replaceAll("[A-H]*[0-9]*_", "") + "," + Index.replaceAll("_.*", "")
                                    + "," + barcode1 + "," + barcode1Sec
                                    + "," + barcode2 + "," + barcode2Sec
                                    + "," + Formated + "," + String.valueOf(runs.getSample().getComments()).replaceAll("null", "") + ","
                                    + "" + "," + "" + "," + (isIllumina(runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName()) ? String.valueOf(runs.getSample().getExpectedPerformance()).replaceAll("null", "") : String.valueOf(runs.getSample().getExpectedPerformanceOxford()).replaceAll("null", "")) + "," + String.valueOf(runs.getSample().getAbs260_280_usmb()).replaceAll("null", "") + ","
                                    + String.valueOf(runs.getSample().getAbs260_230_usmb()).replaceAll("null", "") + "," + "" + "," + runs.getLibrary().getPlataformLinkKit().getKit().getKitName().replace(" ", "_") + "," + runs.getSample().getIdSample()
                                    + "," + runs.getSample().getAceptation() + "," + checkPaymentProj(runs.getSample().getIdProject().getIdProject()) + ","
                                    + checkQuatationProj(runs.getSample().getIdProject().getIdProject()) + ","
                                    + getSamplesProj(runs.getSample().getIdProject())
                            );
                            List<Sample> findAllSamples = ejbSam.findSamplebySampleSheet(runs.getSample().getIdSample());
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            // Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                            for (Sample sam : findAllSamples) {

                                //
                                if (!sam.getStatus().equals("Basecalling y QC en proceso")) {
                                    /*   Comments commentsSample = new Comments();
                            commentsSample.setComment("Estatus cambia de -" + sam.getStatus() + "- a -" + "Basecalling y QC en proceso" + "- por " + us.getUserName());
                            commentsSample.setIdType(sam.getIdSample() + "");
                            commentsSample.setType("Sample");
                            commentsSample.setUserName("SISBI");
                            commentsSample.setCommentDate(timestamp);
                            commentFac.createComment(commentsSample);
                            
                            sam.setStatus("Basecalling y QC en proceso");
                              ejbSam.edit(runs.getSample());
                                     */

                                }

                            }

                        }
                    }
                    /*------------------*/
                    pw.close();
                }
            }

            //RunController editRun= new RunController();
            //editRun.editRun(SAMPLE_CSV_FILE);
            // editRun(SAMPLE_CSV_FILE);
            //cleanFormCreateRun();
            resetValues();
            if (caseOrigin == 2) {
                Run runEdit = runCurrent;
                runEdit.setCycles(Integer.valueOf(run1));
                getEjbFacadeRun().edit(runEdit);

                cleanFormCreateRun();
                System.out.println("Crea archivo exitosamente");
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('DialogCreate').hide()");
                rc.execute("PF('DialogRun').hide()");

                //context.getExternalContext().redirect(file.getAbsolutePath().replaceAll("/var/www/html/sampleFiles/SampleSheetFiles/", "http://www.uusmb.unam.mx/sampleFiles/SampleSheetFiles/"));
                System.out.println("ruta del archivo" + file.getPath());
            }
            //cleanFormCreateSampleSheet();

//Instancia de los metodos desarrollados por jerome, en donde la samplesheet se envia a su servidor para continuar con el proceso de copiado de datos.
            //  ToRsync Call = new ToRsync();
            //Call.ToRsync(file.toPath());
            //System.out.println("Manda el archivo a la clase ToRSync");
        } catch (Exception e) {
            System.out.println("Ocurrio el siguiente error en método createSampleSheet:" + e.getMessage());
            return null;
        }

        return null;
    }

    public void cleanFormCreateRun() {
        scode = "";
        corrida = "";
        flowcell = null;
        runDate = null;
        readType = null;
        kitPerformance = null;
        //LibraryTable = null;
        //getLibsRun();

        Description = null;
        run1 = null;
        run2 = null;
    }

    public void resetValues() {
        projs.clear();
        user = null;
        range = null;
        disabledBtn = false;
        usersList.clear();

    }

    public String getOptPlataform() {

        FacesContext con = FacesContext.getCurrentInstance();
        List<Object> prop = (List<Object>) con.getExternalContext().getSessionMap().get("libraryProperties");

        init();
        return (String) prop.get(0);
    }

    //Obtenemos la plataforma de las muestras seleccionadas mediante una lista llmada 'sampleTable' en la vista 'sample/List.xhtml' 
    public String getPlataformSamplesL() {

        String plataform = sampleTable.get(0).getSamplePlataform();
        return plataform;
    }

    public void setOptPlataform(String optPlataform) {
        this.optPlataform = optPlataform;
    }

    public int getPropNumLib() {
        FacesContext con = FacesContext.getCurrentInstance();
        List<Object> prop = (List<Object>) con.getExternalContext().getSessionMap().get("libraryProperties");
        setNumLib((int) prop.get(4));
        return (int) prop.get(4);
    }

    public void setPropNumLib(int propNumLib) {
        this.propNumLib = propNumLib;
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LibraryUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    private List<Library> lstLibrary_Project;

    public List<Library> getLstLibrary_Project() {
        return lstLibrary_Project;
    }

    public void setLstLibrary_Project(List<Library> lstLibrary_Project) {
        this.lstLibrary_Project = lstLibrary_Project;
    }

    public void getLib() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        List<Sample> s = ejbSam.findSampProj2(proj);
        List<Library> mainList = new ArrayList<>();
        //System.out.println("A BUSCAR: " + proj.getIdProject() + "\tTamaño Sample:" + s.size());
        for (Sample sample : s) {//51
            List<Library> L = getFacade().getLibraries(sample);
            for (Library library : L) {
                //System.out.println("ID1: " + library.getIdBarcode1() + "\tID2: " + library.getIdBarcode2());
                BarcodesCons b = getFacade().findBarcodesByIdBarcode(library.getIdLibrary());
                Barcodes bar = new Barcodes();
                bar.setIdBarcode(Integer.parseInt(b.getIdBarcode1()));
                //System.out.println("TAG1: " + (++i) + " :\t" + b.getIdBarcode1() + "\tTAG2: " + b.getIdBarcode2());
                library.setIdBarcode1(bar);
                bar = new Barcodes();
                bar.setIdBarcode(Integer.parseInt(b.getIdBarcode2()));
                library.setIdBarcode2(bar);
                mainList.add(library);
            }
        }
        setLstLibrary_Project(mainList);
        //System.out.println("BUSCA TAGS");
        //return mainList;
    }

    public List<List<String>> getListaMuestras(List<Sample> list) {

        List<List<String>> listList = new ArrayList<>();

        for (Sample s : list) {
            List<String> muestras = new ArrayList<>();
            muestras.add(s.toString());
            listList.add(muestras);
        }

        return listList;
    }

    public void init() {

    }

    //Método para bloquear la columna de editar en la tabla de CreateL.xhtml
    public Boolean canEditListSampleLibrary() {
        try {
            if (kit.equals("") || kit == null) {
                return false;
            }
            return true;

        } catch (Exception e) {
            System.out.println("Error en: " + e.getMessage());
            return false;
        }

    }

    //Metodo para actualizar la lista utilizada en la tabla de "Muestras para las nuevas bibliotecas"
    public void updateListSampleLibrary(TemplateLibrary item, String id_tag1, String id_tag2, String nameLibrary) {
        System.out.println("Accedemos al método para actualizar barcodes");
        EditLibrariesName = true;
        System.out.println("SE HIZO EL EDIT");
        //Creamos las variables del tipo Barcodes para asignarlos al tag1 y 2 
        Barcodes barcode1 = new Barcodes();
        Barcodes barcode2 = new Barcodes();
        //Creamos una nueva variable del tipo Libreria para asignar los barcodes
        Library libCurrent = new Library();
        //Asignamos el valor a las variables buscando en BD los parámetros de id de tag1y 2
        if (!id_tag1.equals("")) {
            barcode1 = (Barcodes) ejbBar.getBarcodeById(id_tag1).get(0);
            libCurrent.setIdBarcode1(barcode1);
        }
        if (!id_tag2.equals("")) {
            barcode2 = (Barcodes) ejbBar.getBarcodeById(id_tag2).get(0);
            libCurrent.setIdBarcode2(barcode2);
        }
        //Creamos una condición, si el nombre campo nombre no tiene nada o igual a null
        if (!nameLibrary.isEmpty() || nameLibrary != "") {
            libCurrent.setLibraryName(nameLibrary);
        }
        item.setLibrary(libCurrent);
        listSampleLibraries.get(listSampleLibraries.indexOf(item)).setLibrary(libCurrent);
        System.out.println("Se hizo el edit correctamente");
    }

    //Metodo para actualizar la lista utilizada en la tabla de "Muestras para las nuevas bibliotecas" para diferentes kits
    public void updateListSampleLibraryKits(TemplateLibrary item, String id_tag1, String id_tag2, String nameLibrary, String itemKit) {
        //Creamos las variables del tipo Barcodes para asignarlos al tag1 y 2 
        Barcodes barcode1 = new Barcodes();
        Barcodes barcode2 = new Barcodes();
        //Creamos una nueva variable del tipo Libreria para asignar los barcodes
        Library libCurrent = new Library();
        //Asignamos el valor a las variables buscando en BD los parámetros de id de tag1y 2
        if (!id_tag1.equals("")) {
            barcode1 = (Barcodes) ejbBar.getBarcodeById(id_tag1).get(0);
            libCurrent.setIdBarcode1(barcode1);
        }
        if (!id_tag2.equals("")) {
            barcode2 = (Barcodes) ejbBar.getBarcodeById(id_tag2).get(0);
            libCurrent.setIdBarcode2(barcode2);
        }
        //Creamos una condición, si el nombre campo nombre no tiene nada o igual a null
        if (!nameLibrary.isEmpty() || nameLibrary != "") {
            libCurrent.setLibraryName(nameLibrary);
        }
        /*      ADAPTAR CODIGO
        if (!itemKit.isEmpty() || itemKit != "") {
            libCurrent.setKit(itemKit);
        }
         */
        item.setLibrary(libCurrent);
        listSampleLibraries.get(listSampleLibraries.indexOf(item)).setLibrary(libCurrent);
    }

    public void deleteItemListSampleLibrary(TemplateLibrary item) {
        int positionItem = listSampleLibraries.indexOf(item);
        listSampleLibraries.remove(positionItem);
        numLib = listSampleLibraries.size();
    }

    //Método para identificar en que vista estamos
    public void changeAKit() {
        diferentKit = false;
    }

    public void changeDiferentsKits() {
        diferentKit = true;
    }

    // Método para redireccionar a la vista donde se podrán seleccionar varios kits a la vez
    public void viewDiferentKit() {
        diferentKit = true;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("../library/CreateKitsL.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Método para regresar a la selección de muestras desde la vista CreateKitsL.xhtml
    public void viewSampleList() {
        diferentKit = false;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("../sample/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Método inutilizable por la nueva BD
    /*
    public List<Barcodes> getBarcodesI7() {
        List<Barcodes> barcodeEmpty = new ArrayList<>();
        //Agregamos una validación para eliminar el error de la tabla
        if (kit == "" || kit == null) {

            barcodeEmpty.add(new Barcodes());
            return barcodeEmpty;
        } else {
            String auxKit = kit;

            if (kit.equals("Nextera MatePair")) {
                auxKit = "TruSeq HT";
            }

            if (kit.equals("Nextera Rapid Capture Enrichment") || kit.equals("Nextera XT")) {
                auxKit = "Nextera DNA";
            }

            if (kit.equals("Pac Bio Default")) {
                auxKit = "Pac Bio";
            }
            if (kit.equals("Nanopore Default")) {
                auxKit = "Oxford Nanopore";
            }
            if (kit.equals("EXP-NBD104")) {
                auxKit = "EXP-NBD104";
                return ejbBar.findBarcode("NB", auxKit);
            }
            if (kit.equals("EXP-PCB096")) {
                auxKit = "EXP-PCB096";
                return ejbBar.findBarcode("BC", auxKit);
            }
            if (kit.equals("IDT-ILMN Nextera DNA UD")) {
                auxKit = "IDT-ILMN Nextera DNA UD";

                return ejbBar.findBarcodes("-i7", auxKit);
            }

            return ejbBar.findBarcode("i7", auxKit);
        }

        
    }*/
    //Método inutilizable por la nueva BD
    /*
    public List<Barcodes> barcodesI7L() {
        List<Barcodes> barcodeEmpty = new ArrayList<>();
        //Agregamos una validación para eliminar el error de la tabla
        if (kit == "" || kit == null) {

            barcodeEmpty.add(new Barcodes());
            return barcodeEmpty;
        } else {
            String auxKit = kit;

            if (kit.equals("Nextera MatePair")) {
                auxKit = "TruSeq HT";
            }

            if (kit.equals("Nextera Rapid Capture Enrichment") || kit.equals("Nextera XT")) {
                auxKit = "Nextera DNA";
            }

            if (kit.equals("Pac Bio Default")) {
                auxKit = "Pac Bio";
            }
            if (kit.equals("Nanopore Default")) {
                auxKit = "Oxford Nanopore";
            }
            if (kit.equals("EXP-NBD104")) {
                auxKit = "EXP-NBD104";
                return ejbBar.findBarcode("NB", auxKit);
            }
            if (kit.equals("EXP-PCB096")) {
                auxKit = "EXP-PCB096";
                return ejbBar.findBarcode("BC", auxKit);
            }
            if (kit.equals("IDT-ILMN Nextera DNA UD")) {
                auxKit = "IDT-ILMN Nextera DNA UD";

                return ejbBar.findBarcodes("-i7", auxKit);
            }

            return ejbBar.findBarcode("i7", auxKit);
        }

    }*/
    //Método inutilizable por la nueva BD
    /*
    public List<Barcodes> getBarcodesI5() {
        String auxKit = kit;

        if (kit.equals("Nextera MatePair")) {
            auxKit = "TruSeq HT";
        }

        if (kit.equals("Nextera XT v2")) {
            if (platafrom.equals("MiSeq")) {
                auxKit = "Nextera XT v2 MiSeq";
            } else {
                auxKit = "Nextera XT v2 NextSeq";

            }

        }
        if (kit.equals("IDT-ILMN Nextera DNA UD")) {
            auxKit = "IDT-ILMN Nextera DNA UD";

            return ejbBar.findBarcodes("-i5", auxKit);
        }

        return ejbBar.findBarcode("i5", auxKit);

    }*/
    //Método obsoleto : eliminar
    //Método que devuelve los kits dependiendo de la plataforma selecciona en al vista de "Registro de bibliotecas"
    /*
    public List<String> getKits() {

        List<String> kitList = new ArrayList<>();

        switch (platafrom) {

            case "NextSeq 500":
                kitList.add("----");
                kitList.add("TruSeq HT");
                kitList.add("TruSeq LT");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq Small RNA");
                kitList.add("IDT-ILMN Nextera DNA UD");
                return kitList;

            case "MiSeq":
                kitList.add("----");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq HT");
                return kitList;

            case "HiSeq 2000/2500":
                kitList.add("----");
                kitList.add("TruSeq HT");
                kitList.add("TruSeq LT");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq Small RNA");
                kitList.add("IDT-ILMN Nextera DNA UD");
                return kitList;

            case "HiSeq X":
                kitList.add("----");
                kitList.add("TruSeq HT");
                kitList.add("TruSeq LT");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq Small RNA");
                kitList.add("IDT-ILMN Nextera DNA UD");
                return kitList;

            case "NovaSeq 6000":
                kitList.add("----");
                kitList.add("TruSeq HT");
                kitList.add("TruSeq LT");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq Small RNA");
                kitList.add("IDT-ILMN Nextera DNA UD");
                return kitList;

            case "Oxford Nanopore":
                kitList.add("----");
                kitList.add("EXP-NBD196");
                return kitList;

        }

        return null;

    }*/
    //Método nuevo para devolver los kits de acuerdo a la plataforma seleccionada
    public List<String> getKits() {
        List<String> listaKitNames = new ArrayList<>();
        //Ejecutamos siempre y cuando sea 0
        if (ejbPlataform.findPlataformByName(platafrom).size() > 0) {
            // 1.- Buscamos la plataforma por medio del nombre seleccionado
            Plataform itemPlataform = ejbPlataform.findPlataformByName(platafrom).get(0);
            // 2.- obtenemos el objeto del tipo plataforma y en la siguiente consulta buscamos los kits relacionados a esa plataforma
            List<PlataformLinkKit> listPlataformLinkKit = ejbPlataformLinkKit.findPlataformLinkKitByIdPlataform(itemPlataform.getIdPlataform());
            // 3.- Agregamos a la lista el nombre de los kits
            listaKitNames.add("----");
            for (PlataformLinkKit itemlistPLK : listPlataformLinkKit) {
                listaKitNames.add(itemlistPLK.getKit().getKitName());
            }
            System.out.println(listaKitNames);
        }
        return listaKitNames;
    }

    //Método que devuelve los kits dependiendo de la plataforma selecciona en al vista "Registro de bibliotecas con siferentes kits"
    public List<String> getDiferentsKits(String itemPlataform) {

        List<String> kitList = new ArrayList<>();

        switch (itemPlataform) {
            case "NextSeq500":
                kitList.add("----");
                kitList.add("TruSeq HT");
                kitList.add("TruSeq LT");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq Small RNA");
                kitList.add("IDT-ILMN Nextera DNA UD");
                return kitList;

            case "MiSeq":
                kitList.add("----");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq HT");
                return kitList;

            case "HiSeq":
                kitList.add("----");
                kitList.add("TruSeq HT");
                kitList.add("TruSeq LT");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq Small RNA");
                kitList.add("IDT-ILMN Nextera DNA UD");
                return kitList;

            case "iSeq":
                kitList.add("----");
                kitList.add("TruSeq HT");
                kitList.add("TruSeq LT");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq Small RNA");
                kitList.add("IDT-ILMN Nextera DNA UD");
                return kitList;

            case "NovaSeq":
                kitList.add("----");
                kitList.add("TruSeq HT");
                kitList.add("TruSeq LT");
                kitList.add("Nextera XT v2");
                kitList.add("TruSeq Small RNA");
                kitList.add("IDT-ILMN Nextera DNA UD");
                return kitList;

            case "Oxford Nanopore":
                kitList.add("----");
                kitList.add("EXP-NBD196");
                return kitList;

        }

        return null;

    }

    //Método obsoleto : Eliminar 
    /*
    public List<String> getPlataforms() {

        List<String> platList = new ArrayList<>();

        switch (getPlataformSamplesL()) {
            case "NextSeq 500":
                platList.add("----");
                platList.add("NextSeq 500");
                return platList;

            case "MiSeq":
                platList.add("----");
                platList.add("MiSeq");
                return platList;

            case "HiSeq 2000/2500":
                platList.add("----");
                platList.add("HiSeq 2000/2500");
                return platList;

            case "HiSeq X":
                platList.add("----");
                platList.add("HiSeq X");
                return platList;

            case "NovaSeq 6000":
                platList.add("----");
                platList.add("NovaSeq 6000");
                return platList;

            case "Oxford Nanopore":
                platList.add("----");
                platList.add("Oxford Nanopore");
                return platList;

            case "NextSeq 500 - Oxford Nanopore":
                platList.add("----");
                platList.add("NextSeq 500");
                platList.add("Oxford Nanopore");
                return platList;

            case "MiSeq - Oxford Nanopore":
                platList.add("----");
                platList.add("MiSeq");
                platList.add("Oxford Nanopore");
                return platList;

            case "HiSeq 2000/2500 - Oxford Nanopore":
                platList.add("----");
                platList.add("HiSeq 2000/2500");
                platList.add("Oxford Nanopore");
                return platList;
                
            case "HiSeq X - Oxford Nanopore":
                platList.add("----");
                platList.add("HiSeq X");
                platList.add("Oxford Nanopore");
                return platList;

            case "NovaSeq 6000 - Oxford Nanopore":
                platList.add("----");
                platList.add("NovaSeq 6000");
                platList.add("Oxford Nanopore");
                return platList;

        }

        return null;

    }*/
    //Método actualizado
    //Damos opciones en el select de 'Plataforma' en la vista library/CreateL.xhtml
    //Se realiza una consulta a la BD
    public List<String> getPlataforms() {
        List<String> listNamePaltaform = new ArrayList<>();
        //listNamePaltaform = ejbPlataformLinkKit.findNamePlataformsbyName(getPlataformSamplesL());
        listNamePaltaform.add("----");

        //Comprobamos si la consulta arroja un resultado
        if (ejbPlataform.findPlataformByName(sampleTable.get(0).getSamplePlataform()).size() == 0) {
            for (Plataform itemPlataform : ejbPlataform.findAll()) {
                if (sampleTable.get(0).getSamplePlataform().indexOf(itemPlataform.getPlataformName()) >= 0) {
                    listNamePaltaform.add(itemPlataform.getPlataformName());
                }
            }
        } else {
            for (Plataform itemPlataform : ejbPlataformLinkKit.findPlataformByName(sampleTable.get(0).getSamplePlataform())) {
                listNamePaltaform.add(itemPlataform.getPlataformName());
            }
        }

        return listNamePaltaform;

    }

    //leslie 27 agosto
    public List<String> getAllPlataforms() {
        List<String> listPlataformName = new ArrayList<>();
        // 1.- obtenemos todas las plataformas y las agregamos a la lista de manera individual
        for (Plataform itemPlataform : ejbPlataform.findAllPlataforms()) {
            listPlataformName.add(itemPlataform.getPlataformName());
        }

        return listPlataformName;
    }

    public void asignarValorCamposEditLib() { //metodo que ayuda a validar los campos 

        current.getLibraryName();
        current.getQpcrConcentration();
        current.getQubitConcentration();
        platafrom = platafrom;
        kit = kit;
        //current.getPlataformLinkKit();
        current.getIdBarcode1();
        current.getIdBarcode2();

    }

    public String putName(List<String> sams) {

        String[] aux = sams.get(0).split("_");
        String name = aux[0] + "_" + aux[1] + "_";

        for (int i = 0; i <= sams.size() - 1; i++) {
            String[] a = sams.get(i).split("_");
            for (int j = 2; j < a.length; j++) {
                name += a[j];
                name += "_";
            }
        }
        name = name.substring(0, name.length() - 1);
        name = name.replace(" ", "");
        return name;

    }

    public List<Sample> getSampleIds(List<String> samNames) {
        List<Sample> muestras = ejbSam.findAll();
        List<Sample> ids = new ArrayList<>();
        for (Sample sample : muestras) {
            for (String name : samNames) {
                if (name.equals(sample.toString())) {
                    ids.add(sample);
                }
            }
        }

        return ids;
    }

    public String getNameRepeted() {
        return nameRepeted;
    }

    public void setNameRepeted(String nameRepeted) {
        this.nameRepeted = nameRepeted;
    }

    String nameRepeted = "";

    /*
    public void create(ActionEvent actionEvent) {
        boolean checkNames = true;
        boolean checkTags = true;

        //Asegura que ningun tag este repetido.
        List<String> tags = new ArrayList<>();
        List<String> names = new ArrayList<>();

        //Guardamos los 2 tags en la variable valor y añadimos el nombre de la bibloteca en las listas
        for (AuxLibrary aux : libs) {
            String valor = aux.getLibrary().getIdBarcode1().toString();
            if (aux.getLibrary().getIdBarcode2() != null) {
                valor = valor + aux.getLibrary().getIdBarcode2().toString();
                System.out.println("tag repetido: iteracion dentro del if: " + valor);
            }
            System.out.println("tag repetido: iteracion fuera del if: " + valor);
            tags.add(valor);
            names.add(aux.getLibrary().getLibraryName());
        }

        Collections.sort(tags);
        Collections.sort(names);

        for (int i = 0; i < tags.size() - 1; i++) {
            checkTags = true;
            if (tags.get(i).equals(tags.get(i + 1))) {

                checkTags = false;
                nameRepeted = tags.get(i);
                break;
            }
        }
        for (int i = 0; i < names.size() - 1; i++) {
            checkNames = true;
            if (names.get(i).equals(names.get(i + 1))) {
                checkNames = false;
                nameRepeted = names.get(i);
                break;
            }
        }
        //Asegura que ningun nombre este repetido

        if (checkNames && checkTags) {
            FacesContext con = FacesContext.getCurrentInstance();
            Users us = (Users) con.getExternalContext().getSessionMap().get("usuario");
            List<Object> prop = (List<Object>) con.getExternalContext().getSessionMap().get("libraryProperties");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++   que es prop libraryProperties");
            for (int i = 0; i < prop.size(); i++) {
                System.out.println("Contenido de la lista " + i + " : " + prop.get(i));
            }

            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++   que es prop libraryProperties");
            for (AuxLibrary item : libs) {

                System.out.println("-----------------");
                System.out.println("Nombre de biblioteca: " + putName(item.getListSample()));
                System.out.println("Estatus: Construida");
                System.out.println("Tag 1: " + item.getLibrary().getIdBarcode1());
                System.out.println("Tag 2: " + item.getLibrary().getIdBarcode2());
                System.out.println("Fecha de preparacion: " + fechaPrep);
                System.out.println("Plataforma: " + platafrom);
                System.out.println("Kit: " + kit);
                if (prop.get(1).equals("1") || platafrom.equals("Oxford Nanopore") || platafrom.equals("Pac Bio")) {

                    System.out.println("Tipo: Sigle Read");
                    libraryType = "Sigle Read";
                }

                System.out.println("Tamaño del inserto: " + prop.get(3));
                System.out.println("Concentración " + item.getLibrary().getQpcrConcentration());
                System.out.println("Concentración nanomolar " + item.getLibrary().getNanomolarConcentration());

                System.out.println("Ciclos 1: " + prop.get(2));

                if (libraryType.equals("Paired End")) {
                    System.out.println("Ciclos 2: " + prop.get(2));

                } else {
                    System.out.println("Ciclos 2: null");
                }

                Library library = new Library();
                library.setLibraryName(item.getLibrary().getLibraryName());
                library.setPreparationStatus("Construida");
                library.setIdBarcode1(item.getLibrary().getIdBarcode1());

                library.setIdBarcode2(item.getLibrary().getIdBarcode2());
                library.setPreparationDate(fechaPrep);
                //  ADAPTAR CODIGO
                //library.setPlataform(platafrom);
                //library.setKit(kit);
                
                if (prop.get(1).equals("1") || platafrom.equals("Oxford Nanopore") || platafrom.equals("Pac Bio")) {

                    libraryType = "Sigle Read";
                }
                // ADAPTAR CODIGO PREGUNTAR
                //library.setType(libraryType);
                String inse = (String) prop.get(3);
                int insert = Integer.parseInt(inse);
                
                //ADAPTAR CODIGO PREGUNTAR
                
                //library.setInsertSize(insert);
                //int cycle = Integer.parseInt(prop.get(2).toString().replaceAll("[ pP|bB]bB$", "").replaceAll("\\s", ""));
                //library.setCycles_1(cycle);

                //if (libraryType.equals("Paired End") || libraryType.equals("Mate Pair")) {
                //    library.setCycles_2(cycle);
                //}

                library.setUserName(us.getUserName());

                getFacade().create(library);

                List<Sample> ids = getSampleIds(item.getListSample());

                for (Sample id : ids) {
                    SampleLibraryLink sll = new SampleLibraryLink();
                    sll.setSampleLibraryLinkPK(new jpa.entities.SampleLibraryLinkPK());
                    sll.getSampleLibraryLinkPK().setIdSample(id.getIdSample());
                    sll.getSampleLibraryLinkPK().setIdLibrary(library.getIdLibrary());

                    ejbSll.create(sll);
                    id.setStatus("En espera de secuenciacion");
                    ejbSam.edit(id);
                }
                System.out.println("-----------------");

            }

            libs.clear();

//          RequestContext requestContext = RequestContext.getCurrentInstance();
//UIComponent component = actionEvent.getComponent();
//UIComponent hiddenNonAjaxExportButton = component.findComponent("hiddenButton");
//String hiddenNonAjaxExportButtonClientId = hiddenNonAjaxExportButton.getClientId();
//requestContext.execute("document.getElementById('" + hiddenNonAjaxExportButtonClientId + "').click();");
//          
            showDialog(3);

        } else {
            showDialog(1);
            System.out.println("Se encontraron repetidos: " + nameRepeted);
        }

    }*/
    //Método Para crear bibliotecas - Linabat
    public void createLibraryL(ActionEvent actionEvent) {
        //Declaramos las variables de contexto y sesión
        RequestContext dialog = RequestContext.getCurrentInstance();
        FacesContext con = FacesContext.getCurrentInstance();
        Users us = (Users) con.getExternalContext().getSessionMap().get("usuario");

        //Validación de nommbre de biblioteca y combinación de tag...
        int nameRepeat = 0;
        int tag12Repeat = 0;
        int contlistSampleLibraries = 0;
        //Realizamos las validaciones
        for (int i = 0; i < listSampleLibraries.size(); i++) {
            //Validamos si existen campos vacíos 
            if (listSampleLibraries.get(i).getLibrary().getLibraryName().isEmpty()) {
                messageDialog = "Ups, en la fila " + (i + 1) + " el nombre de la biblioteca está vacía";
                dialog.execute("PF('biblioErrDialog').show();");
                return;
            }
            //System.out.println(listSampleLibraries.get(i).getLibrary().getLibraryName().toString());
            if (listSampleLibraries.get(i).getLibrary().getIdBarcode1() == null) {
                messageDialog = "Ups, en la fila " + (i + 1) + " el tag1 está vacío";
                dialog.execute("PF('biblioErrDialog').show();");
                return;
            }

            //Inicia la validacion de que si cuando se edita el nombre este no este registrado en la base de datos
            if (EditLibrariesName == true) {
                for (int j = 0; j < getLibsByProject().size(); j++) {
                    System.out.println("Se manda a llamar getLibsByProject en CreateL  3333-------------------------");
                    if (listSampleLibraries.get(i).getLibrary().getLibraryName().equals(getLibsByProject().get(j).getLibraryName())) {
                        contlistSampleLibraries++;
                    }
                }
            }

            if (contlistSampleLibraries > 0) {
                System.out.println("EL NOMBRE SE REPITE EN LA BASE DE DATOS");
                messageDialog = "Ups, en la fila " + (i + 1) + " El nombre ya esta registrado, favor de ingresar otro";
                dialog.execute("PF('biblioErrDialog').show();");
                return;
            }
            contlistSampleLibraries = 0;
            //Fin de la validacion del nombre

            /*Inicia la validacion del _numerico*/
            /*cadLibraryName = listSampleLibraries.get(i).getLibrary().getLibraryName().split("_");        
             for(int z=0; z<cadLibraryName.length-1; z++){
                 cad=cad+cadLibraryName[z];
             }
             
             for (int j = 0; j <getLibsByProject().size(); j++) {jkjd
                 cadgetLibsByProject=getLibsByProject().
                 cadgetLibsByProject=null;  
             }*/
 /*Termina la validacion del _numerico*/
            if (diferentKit == false) {
                //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                //Entonces no habrá tag 2
                if (!platafrom.equals("Oxford Nanopore")) {
                    if (!kit.equals("TruSeq smallRNA")) {
                        if (!kit.equals("TruSeq HT")) {
                            if (listSampleLibraries.get(i).getLibrary().getIdBarcode2() == null) {
                                messageDialog = "Ups, en la fila " + (i + 1) + " el tag2 está vacío";
                                dialog.execute("PF('biblioErrDialog').show();");
                                return;
                            }
                        }
                    }
                }
            }
            //Cuando estemos en la otra vista de CreateKitsL.xhtml
            if (diferentKit == true) {
                if (listSampleLibraries.get(i).getLibrary().getPlataformLinkKit().getKit().getKitName().isEmpty()) {
                    messageDialog = "Ups, en la fila " + (i + 1) + " el kit está vacío";
                    dialog.execute("PF('biblioErrDialog').show();");
                    return;
                }

                //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                //Entonces no habrá tag 2
                if (!platafrom.equals("Oxford Nanopore")) {
                    if (!listSampleLibraries.get(i).getLibrary().getPlataformLinkKit().getKit().getKitName().equals("TruSeq Small RNA")) {
                        if (!listSampleLibraries.get(i).getLibrary().getPlataformLinkKit().getKit().getKitName().equals("TruSeq HT")) {
                            if (listSampleLibraries.get(i).getLibrary().getIdBarcode2() == null) {
                                messageDialog = "Ups, en la fila " + (i + 1) + " el tag2 está vacío";
                                dialog.execute("PF('biblioErrDialog').show();");
                                return;
                            }
                        }
                    }
                }
            }
        }

        //Procedemos a realizar el reigstro de las bibliotecas
        for (int i = 0; i < listSampleLibraries.size(); i++) {
            String currentNameLibrary = listSampleLibraries.get(i).getLibrary().getLibraryName();
            String nameIBarcode2 = "";
            if (listSampleLibraries.get(i).getLibrary().getIdBarcode2() == null) {
                nameIBarcode2 = "";
            } else {
                nameIBarcode2 = getIndexBasei5Create(listSampleLibraries.get(i).getLibrary().getIdBarcode2());
                System.out.println("Lo que tiene el barcode 2 es: " + nameIBarcode2);
            }

            //Validación de nombre repetido
            for (int j = 0; j < listSampleLibraries.size(); j++) {
                if (listSampleLibraries.get(j).getLibrary().getLibraryName().equals(currentNameLibrary)) {
                    nameRepeat++;
                }
                if (nameRepeat >= 2) {
                    messageDialog = "En al fila " + (j + 1) + " el nombre de librería: " + listSampleLibraries.get(j).getLibrary().getLibraryName() + " ya ha sido repedito";
                    dialog.execute("PF('biblioErrDialog').show();");
                    return;
                }
            }
            nameRepeat = 0;

            String currentTag12 = listSampleLibraries.get(i).getLibrary().getIdBarcode1().getIndexName() + "-" + listSampleLibraries.get(i).getLibrary().getIdBarcode1().getBasei7() + nameIBarcode2;
            System.out.println("Nombre de librería " + (i + 1) + ": " + currentNameLibrary + " Combinación de barcodes: " + currentTag12);
            //Validación de combinación de tag1 y 2 repetidos
            for (int k = 0; k < listSampleLibraries.size(); k++) {
                String nameKBarcode2 = "";
                if (listSampleLibraries.get(k).getLibrary().getIdBarcode2() == null) {
                    nameKBarcode2 = "";
                } else {
                    nameKBarcode2 = getIndexBasei5Create(listSampleLibraries.get(k).getLibrary().getIdBarcode2());
                }
                String itemTag12 = listSampleLibraries.get(k).getLibrary().getIdBarcode1().getIndexName() + "-" + listSampleLibraries.get(k).getLibrary().getIdBarcode1().getBasei7() + nameKBarcode2;
                if (itemTag12.trim().equals(currentTag12.trim())) {
                    tag12Repeat++;
                }
                if (tag12Repeat >= 2) {
                    messageDialog = "En la fila " + (k + 1) + ": el tag " + itemTag12 + " ya ha sido repedito";
                    dialog.execute("PF('biblioErrDialog').show();");
                    return;
                }
            }
            tag12Repeat = 0;
        }

        //Una vez pasada las validaciones, guardamos los registros
        for (int i = 0; i < listSampleLibraries.size(); i++) {
            //insertar a la BD ---------------------------------------------------
            /*Agregamos y con plsit separamos en tipos y ciclos el campo de tamaño de lectura
            deberian guardar solamente 2 datos con el split x, si se guardan mas el dato es incorrecto*/
            String formated = listSampleLibraries.get(i).getSample().getReadSize().toLowerCase();
            String[] typeAndCycles = formated.split("x");
            String types = typeAndCycles[0];
            String cycles = typeAndCycles[1];

            //Agregamos la validaciónd de plataforma y kit
            if (diferentKit == true) {
                platafrom = listSampleLibraries.get(i).getSample().getSamplePlataform();
                //kit = listSampleLibraries.get(i).getLibrary().getKit();
                kit = listSampleLibraries.get(i).getLibrary().getPlataformLinkKit().getKit().getKitName();
            }

            //Aplicamos la primera condición
            if (types.equals("1") || platafrom.equals("Oxford Nanopore") || platafrom.equals("Pac Bio")) {
                System.out.println("Tipo: Sigle Read");
                libraryType = "Sigle Read";
            }
            System.out.println("Pasamos la primera validación --------------------------------1");
            //Creamos el objeto de tipo library
            Library library = new Library();
            library.setLibraryName(listSampleLibraries.get(i).getLibrary().getLibraryName());
            library.setPreparationStatus("Construida");
            library.setIdBarcode1(listSampleLibraries.get(i).getLibrary().getIdBarcode1());
            library.setIdBarcode2(listSampleLibraries.get(i).getLibrary().getIdBarcode2());
            library.setPreparationDate(fechaPrep);
            //ADAPTAR CODIGO debido a que ahora se debe meter el id
            //Buscamos el link y plataforma
            Kit findKit = ejbKit.findKitByName(kit).get(0);
            Plataform findPlataform = ejbPlataform.findPlataformByName(platafrom).get(0);
            //Creamos un nuevo objeto de link de plataforma/kit
            PlataformLinkKit plataformKit = new PlataformLinkKit();
            plataformKit = (PlataformLinkKit) ejbPlataformLinkKit.findPlataformLinkKitByIds(findPlataform.getIdPlataform(), findKit.getIdKit()).get(0);
            //Asignamos el objeto al set de paltaforma/kit de library
            library.setPlataformLinkKit(plataformKit);

            //library.setPlataform(platafrom);
            //library.setKit(kit);
            if (types.equals("1") || platafrom.equals("Oxford Nanopore") || platafrom.equals("Pac Bio")) {
                libraryType = "Sigle Read";
            }
            //ADAPTAR CODIGO debido a que ahora no tiene type
            //library.setType(libraryType);
            String inse = "";
            if (listSampleLibraries.get(i).getSample().getInsertSize() == null) {
                inse = "0";
            } else {
                inse = (String) listSampleLibraries.get(i).getSample().getInsertSize();
            }
            int insert = Integer.parseInt(inse);
            //ADAPTAR CODIGO debido a que ahora no tiene size
            //library.setInsertSize(insert);
            int cycle = Integer.parseInt(cycles.toString().replaceAll("[ pP|bB]bB$", "").replaceAll("\\s", ""));
            //ADAPTAR CODIGO debido a que ahora no tiene ciclos
            /*
            System.out.println("El CYCLE ES: " + cycle);
            library.setCycles_1(cycle);
            if (libraryType.equals("Paired End") || libraryType.equals("Mate Pair")) {
                library.setCycles_2(cycle);
            }*/
            //Asignamos el valor del usuario
            library.setUserName(us.getUserName());
            //Creamos el registro de la biblioteca
            getFacade().create(library);
            //Creamos la relacion de sample y library en sample_library_link
            //Del arreglo listSampleLibraries obtenemos el objeto actual de la iteración y creamos un nuevo objeto del tipo Sample
            Sample itemSample = listSampleLibraries.get(i).getSample();
            //Creamos el objeto de tipo SampleLibraryLink
            //y asignamos las propiedades de relación, en este caso solo las "id" de sample y library y guardamos
            SampleLibraryLink sll = new SampleLibraryLink();
            sll.setSampleLibraryLinkPK(new jpa.entities.SampleLibraryLinkPK());
            sll.getSampleLibraryLinkPK().setIdSample(itemSample.getIdSample());
            sll.getSampleLibraryLinkPK().setIdLibrary(library.getIdLibrary());
            sll.setSample(itemSample);
            sll.setLibrary(library);
            //guardamos el objeto 
            ejbSll.create(sll);

            //Llenado en la tabla de coments
            String statusAnt = itemSample.getStatus();
            Comments commentsSample = new Comments();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -En construccion de biblioteca- por " + us.getUserName());
            commentsSample.setIdType(itemSample.getIdSample() + "");
            commentsSample.setType("Sample");
            commentsSample.setUserName("SISBI");
            commentsSample.setCommentDate(timestamp);
            commentFac.createComment(commentsSample);

            //Al objeto de tipo sample le mandamos la propiedad de estatus como "En espera de secuenciacion"
            itemSample.setStatus("En construccion de biblioteca");
            //Editamos el objeto de tipo Sample
            ejbSam.edit(itemSample);
        }

        //Mostramos el mensaje de registro exitoso
        showDialog(3);
    }

    public void showDialog(int i) {

        RequestContext context = RequestContext.getCurrentInstance();

        switch (i) {
            case 1:
                context.execute("PF('dlg1').show();");
                break;
            case 2:
                context.execute("PF('dlg2').show();");
                break;
            case 3:
                context.execute("PF('libCreateDialog').show();");
                break;

        }

    }

    public void loadFieldGenerateRun(Run runCurrent) {
        run1 = runCurrent.getCycles().toString();
        run2 = runCurrent.getCycles().toString();
    }

    public void keyUpInputRun1() {
        run2 = run1;
    }

    public void limpiaLista() {
        // Preparación inicial
        diferentKit = false; // Variable para identificar en qué vista estamos
        platafrom = "";
        kit = "";

        // Obtener las muestras seleccionadas desde la sesión
        FacesContext context = FacesContext.getCurrentInstance();
        sampleTable = (List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");

        listSampleLibraries = new ArrayList<>();

        // Crear la lista inicial de bibliotecas basadas en las muestras seleccionadas
        for (Sample sample : sampleTable) {
            Library library = new Library();
            library.setLibraryName(sample.getSampleName());
            listSampleLibraries.add(new TemplateLibrary(sample, library, new Barcodes()));
        }

        numLib = listSampleLibraries.size();

        // Preprocesar los nombres de bibliotecas existentes en un HashMap
        Map<String, Integer> libraryNameCounts = new HashMap<>();
        for (Library lib : getLibsByProject()) {
            String baseName = getOriginalLibraryName(lib.getLibraryName());
            libraryNameCounts.put(baseName, libraryNameCounts.getOrDefault(baseName, 0) + 1);
        }

        System.out.println("Total de librerías construidas: " + libraryNameCounts.size());
        System.out.println("Hash de nombres de librerías: " + libraryNameCounts); // Debug

        // Asignar nombres únicos a las bibliotecas seleccionadas
        for (TemplateLibrary templateLibrary : listSampleLibraries) {
            String baseName = getOriginalLibraryName(templateLibrary.getLibrary().getLibraryName());

            // Verificar si ya existe el nombre base en las bibliotecas procesadas
            int suffix = libraryNameCounts.getOrDefault(baseName, 0);
            if (suffix > 0) {
                templateLibrary.getLibrary().setLibraryName(baseName + "_" + (suffix + 1));
            }

            // Actualizar el contador en el mapa
            libraryNameCounts.put(baseName, suffix + 1);
        }

        // Debug: Imprimir el resultado final (opcional para pruebas)
        for (TemplateLibrary library : listSampleLibraries) {
            System.out.println("Biblioteca asignada: " + library.getLibrary().getLibraryName());
        }
    }

    // Método auxiliar para obtener el nombre base de una biblioteca (incluye identificadores adicionales)
    private String getOriginalLibraryName(String libraryName) {
        String[] parts = libraryName.split("_");
        StringBuilder baseName = new StringBuilder();

        // Concatenar todas las partes excepto la última si es un número
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                baseName.append("_");
            }
            baseName.append(parts[i]);
        }

        return baseName.toString();
    }

    public boolean render() {
        FacesContext con = FacesContext.getCurrentInstance();
        List<Object> prop = (List<Object>) con.getExternalContext().getSessionMap().get("libraryProperties");
        System.out.println(!(platafrom.equals("Oxford Nanopore") || platafrom.equals("Pac Bio") || prop.get(1).equals("1")));

        return !(platafrom.equals("Oxford Nanopore") || platafrom.equals("Pac Bio") || prop.get(1).equals("1"));

    }

    public int getCountLibraries() {
        return countLibraries;
    }

    public void setCountLibraries(int countLibraries) {
        this.countLibraries = countLibraries;
    }

    public void procesFile(FileUploadEvent event) throws FileNotFoundException, IOException, InvalidFormatException {
        RequestContext dialog = RequestContext.getCurrentInstance();
        rowSampleExcel.clear();
        FacesContext con = FacesContext.getCurrentInstance();
        Users us = (Users) con.getExternalContext().getSessionMap().get("usuario");

        //Carga de arreglos a utilizar para validar 
        //llenado de los arreglos para kits y plataformas
        listPlataform.add("HiSeq");
        listPlataform.add("iSeq");
        listPlataform.add("NovaSeq");
        listPlataform.add("MiSeq");
        listPlataform.add("NextSeq500");
        //listPlataform.add("Pac Bio");
        listPlataform.add("Oxford Nanopore");
        /*
        listKits.add("TruSeq HT");
        listKits.add("TruSeq LT");
        listKits.add("Nextera XT v2");
        listKits.add("TruSeq Small RNA");
        listKits.add("IDT-ILMN Nextera DNA UD");
        listKits.add("EXP-NBD196");
        listKits.add("IDT-ILMN-TruSeq-DNA-and-RNA-UD");*/

        listKits.add("IDT-ILMN Nextera DNA UD");// cambio de -IDT-ILMN Nextera DNA UD- a -IDT FOR ILLUMINA UD INDEXES, IDT FOR ILLUMINA DNA-RNA UD INDEXES, IDT FOR ILLUMINA PCR UD INDEXES, IDT FOR ILLUMINA NEXTERA DNA UD INDEXES V2-                
        listKits.add("Nextera XT v2");// cambio de -Nextera XT v2- a -NEXTERA DNA INDEXES, NEXTERA DNA CD INDEXES, NEXTERA INDEX KIT, NEXTERA XT INDEX KIT V2-
        listKits.add("AmpliSeq for Illumina Panels"); // nuevo kit
        listKits.add("IDT-ILMN-TruSeq-DNA-and-RNA-UD"); // nuevo kit
        listKits.add("TruSeq HT"); //cambio de -TruSeq HT- a -TRUSEQ DNA AND RNA CD INDEXES, TRUSEQ HT-               
        listKits.add("TruSeq smallRNA");// cambio de -TruSeq Small RNA- a -TRUSEQ SMALL RNA-   
        listKits.add("EXP-NBD196");

        //Inicio de conteo de validación
        int rowStartValidation = 2;
        int finalValidation = 1;
        int rowStartRegistration = 2;

        //llenado de nombre de muestras
        List<String> sampleNames = new ArrayList<String>();
        for (int i = 0; i < listSampleLibraries.size(); i++) {
            sampleNames.add(listSampleLibraries.get(i).getSample().getSampleName());
        }
        List<String> sampleIds = new ArrayList<String>();
        for (int i = 0; i < listSampleLibraries.size(); i++) {
            sampleNames.add(listSampleLibraries.get(i).getSample().getIdSample().toString());
        }

        for (int i = 0; i < listSampleLibraries.size(); i++) {
            //insertar a la BD ---------------------------------------------------
            /*Agregamos y con plsit separamos en tipos y ciclos el campo de tamaño de lectura
            deberian guardar solamente 2 datos con el split x, si se guardan mas el dato es incorrecto*/
            String formated = listSampleLibraries.get(i).getSample().getReadSize().toLowerCase();
            String[] typeAndCycles = formated.split("x");
            String types = typeAndCycles[0];
            String cycles = typeAndCycles[1];
        }

        DataInputStream entrada = new DataInputStream(event.getFile().getInputstream());
        XSSFWorkbook workbook = new XSSFWorkbook(entrada);
        XSSFSheet sheet = workbook.getSheetAt(0);

        //String url = "jdbc:postgresql://localhost:5432/sisbi_db";
        //String usuario = "sisbi";
        //String pswd = "SISBI123@";
        if (diferentKit == false) {
            if (platafrom.isEmpty() || platafrom == null) {
                messageDialog = "Por favor seleccione la plaforma";
                closeStatusDialogUploadFile();
                dialog.execute("PF('biblioErrDialog').show();");
                return;
            }
            if (kit.isEmpty() || kit == null) {
                messageDialog = "Por favor seleccione el kit";
                closeStatusDialogUploadFile();
                dialog.execute("PF('biblioErrDialog').show();");
                return;
            }
        }

        try {
            //Validar que todas las filas sean correctas  ----------------------------------------------------------------INICIO
            int count1 = 0;
            for (Row row : sheet) {
                count1++;
                //Entramos a la validación de numero de librerías, se realizan las iteraciones con el numero de librerías
                if (count1 >= (numLib + rowStartValidation)) {
                    finalValidation = 2;
                    System.out.println("Se termina la iteración: completado count: " + count1 + " Y lib = " + numLib);
                    break;
                }
                //Comienza a realizar las validaciones cuando se llega a la fila con información de las muetsras
                if (count1 >= rowStartValidation) {
                    List<String> parameters = new ArrayList<>();  //Se guarda la información de la fila actual de la iteración
                    //List<String> tags1 = new ArrayList<>();
                    // List<String> tags2 = new ArrayList<>();
                    System.out.println("lim " + row.getFirstCellNum() + "  " + row.getLastCellNum());
                    System.out.println("filas: " + row.getRowNum());
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        System.out.println("Valor de cn :" + cn);
                        System.out.println("ultima celda : " + row.getLastCellNum());
                        Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case STRING:
                                parameters.add(cell.getStringCellValue());
                                // tags1.add(cell.getStringCellValue());
                                // tags2.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                parameters.add("" + (int) cell.getNumericCellValue());

                                break;
                            case BLANK:
                                parameters.add(cell.getStringCellValue()); //aun cuando el campo o celda está vacío, se agrega al arreglo para validarlo
                                //parameters.add("-------");
                                break;
                        }
                    }

                    if (parameters.get(0).trim().equals("")) {

                        break;
                    }

                    System.out.println("id Muestra: " + parameters.get(0));
                    System.out.println("Muestra: " + parameters.get(MUESTRAS));
                    System.out.println("Tag 1: " + parameters.get(TAG_1));
                    System.out.println("Tag 2: " + parameters.get(TAG_2));
                    //System.out.println("Plataforma: " + parameters.get(PLATAFORM));
                    //System.out.println("Kit: " + parameters.get(KIT));
                    //System.out.println("campo 3: " + paramete  rs.get(TAG_2));
                    //String nameSample = parameters.get(MUESTRAS).trim();
                    //String tag1 = parameters.get(TAG_1).trim();
                    //String tag2 = parameters.get(TAG_2).trim();

                    //El valor se le asignará a la variable "plataforma" dependiendo de si usa o no kits diferentes
                    String itemPlataform;
                    String itemKit;
                    if (diferentKit == true) {
                        itemPlataform = parameters.get(PLATAFORM).trim();
                        itemKit = parameters.get(KIT).trim();
                    } else {
                        itemPlataform = platafrom;
                        itemKit = kit;
                    }

                    if (parameters.get(0).isEmpty()) {
                        messageDialog = "En la fila " + count1 + ": el id de la muestra está vacío";
                        closeStatusDialogUploadFile();
                        dialog.execute("PF('biblioErrDialog').show();");
                        return;
                    }
                    if (parameters.get(MUESTRAS).isEmpty()) {
                        messageDialog = "En la fila " + count1 + ": el nombre de la biblioteca está vacío";
                        closeStatusDialogUploadFile();
                        dialog.execute("PF('biblioErrDialog').show();");
                        return;
                    }
                    if (parameters.get(TAG_1).isEmpty()) {

                        System.out.println("esta vacio no pasa la validación");
                        messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + "  el tag 1 está vacío";
                        closeStatusDialogUploadFile();
                        dialog.execute("PF('biblioErrDialog').show();");
                        //rowSampleExcel.clear();
                        return;
                    }
                    //Validamos cuando el kit sea ‘TruSeq Small RNA’  y para todos los kit's de la plataforma ‘Oxford Nanopore’
                    //No habrá ningún tipo de validación para el tag 2
                    if (diferentKit == false) {
                        //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                        //Entonces no habrá tag 2
                        if (!itemPlataform.equals("Oxford Nanopore")) {
                            if (!itemKit.equals("TruSeq smallRNA")) {
                                if (!itemKit.equals("TruSeq HT")) {
                                    if (parameters.get(TAG_2).isEmpty()) {

                                        System.out.println("esta vacio no pasa la validación");
                                        messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " el tag 2 está vacío";
                                        closeStatusDialogUploadFile();
                                        dialog.execute("PF('biblioErrDialog').show();");
                                        //rowSampleExcel.clear();
                                        return;
                                    }
                                }
                            }
                        }
                    } //Cuando estemos en la vista de diferentes kits
                    else {
                        //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                        //Entonces no habrá tag 2
                        if (!itemPlataform.equals("Oxford Nanopore")) {
                            if (!itemKit.equals("TruSeq Small RNA")) {
                                if (!itemKit.equals("TruSeq HT")) {
                                    if (parameters.get(TAG_2).isEmpty()) {
                                        System.out.println("esta vacio no pasa la validación");
                                        messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " el tag 2 está vacío";
                                        closeStatusDialogUploadFile();
                                        dialog.execute("PF('biblioErrDialog').show();");
                                        //rowSampleExcel.clear();
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    //Validamos si usará kits diferentes
                    if (diferentKit == true) {
                        if (itemPlataform.isEmpty()) {

                            System.out.println("esta vacio no pasa la validación");
                            messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " la plataforma está vacía";
                            closeStatusDialogUploadFile();
                            dialog.execute("PF('biblioErrDialog').show();");
                            //rowSampleExcel.clear();
                            return;
                        }

                        if (itemKit.isEmpty()) {

                            System.out.println("esta vacio no pasa la validación");
                            messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " el kit está vacío";
                            closeStatusDialogUploadFile();
                            dialog.execute("PF('biblioErrDialog').show();");
                            //rowSampleExcel.clear();
                            return;
                        }
                    }

                    System.out.println("no esta vacio **para kits diferentes");
                    List<Object> prop = (List<Object>) con.getExternalContext().getSessionMap().get("libraryProperties");

                    String NameLib = parameters.get(MUESTRAS).trim();
                    String tag1 = parameters.get(TAG_1).trim();
                    String tag2 = parameters.get(TAG_2).trim();

                    //validamos si existe la muestra con el id_muestra de la columna 1 del excel
                    String idNameSample = (parameters.get(0).replaceAll(" -.*", "")).replace("[", "");
                    //Validamos si es nombre o id de muestra
                    //comprobamos si solo son números
                    if (idNameSample.matches("[0-9]+")) {
                        int itemIdSample = Integer.parseInt(idNameSample);
                        if (ejbSam.findSampleById(itemIdSample).isEmpty()) {
                            messageDialog = "No existe la muestra con id: " + parameters.get(0);
                            closeStatusDialogUploadFile();
                            dialog.execute("PF('biblioErrDialog').show();");
                            //rowSampleExcel.clear();
                            return;
                        }
                    } else {
                        if (ejbSam.findSampleByName(idNameSample).isEmpty()) {
                            messageDialog = "No existe la muestra con nombre: " + parameters.get(0);
                            closeStatusDialogUploadFile();
                            dialog.execute("PF('biblioErrDialog').show();");
                            //rowSampleExcel.clear();
                            return;
                        }
                    }

                    //Valimos que la muestra escrita en la columna del excel pertenezca a las muestras seleccionadas en la vista 
                    System.out.println("LLegamos a la validación de detectar si existe la muestra en el listado");
                    if (sampleNames.indexOf(idNameSample) < 0 && sampleIds.indexOf(idNameSample) < 0) {
                        messageDialog = "La muestra: " + idNameSample + " No pertenece a la selección que realizó";
                        closeStatusDialogUploadFile();
                        dialog.execute("PF('biblioErrDialog').show();");
                        return;
                    }

                    System.out.println("Pasamos a la validación de detectar si existe la muestra en el listado");

                    // Sevalida si el tag1 escrito en el excel es correcto
                    if (ejbBar.findBarcodeByIndexName(tag1).isEmpty()) {
                        messageDialog = "Para la muestra " + NameLib + " no se reconoce el TAG 1: " + tag1 + "  , favor de revisarlo";
                        closeStatusDialogUploadFile();
                        dialog.execute("PF('biblioErrDialog').show();");
                        //rowSampleExcel.clear();
                        return;
                    }

                    //Se valida si el tag1 tiene valor en base i7
                    if (ejbBar.findBarcodeByIndexName(tag1).get(0).getBasei7() == null) {
                        messageDialog = "Para la muestra " + NameLib + " no es correcto el TAG 1: " + tag1 + "  , favor de revisarlo";
                        closeStatusDialogUploadFile();
                        dialog.execute("PF('biblioErrDialog').show();");
                        //rowSampleExcel.clear();
                        return;
                    }

                    //Validar si el tag1 pertenece al kit seleccionado
                    // 1.- Buscamos el kit en la BD
                    Kit kitValidation = ejbKit.findKitByName(itemKit).get(0);
                    // 2.- Comparamos si el tag1(Barcode) del excel tiene relación con el kit seleccionado en el formulario
                    if (ejbBar.findBarcodeByIndexName(tag1).get(0).getIdKit().getIdKit() != kitValidation.getIdKit()) {
                        messageDialog = "Para la muestra " + NameLib + " El TAG 1: " + tag1 + ", no es compatible con el kit seleccionado, favor de revisarlo";
                        closeStatusDialogUploadFile();
                        dialog.execute("PF('biblioErrDialog').show();");
                        //rowSampleExcel.clear();
                        return;
                    }

                    //Validamos cuando el kit sea ‘TruSeq Small RNA’  y para todos los kit's de la plataforma ‘Oxford Nanopore’
                    //No habrá ningún tipo de validación para el tag 2
                    if (diferentKit == false) {
                        //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                        //Entonces no habrá tag 2
                        if (!itemPlataform.equals("Oxford Nanopore")) {
                            if (!itemKit.equals("TruSeq smallRNA")) {
                                if (!itemKit.equals("TruSeq HT")) {
                                    // Se valida si el tag2 escrito en el excel es correcto
                                    if (ejbBar.findBarcodeByIndexName(tag2).isEmpty()) {
                                        messageDialog = "Para la muetsra " + NameLib + " no se reconoce el TAG 2, favor de revisarlo";
                                        closeStatusDialogUploadFile();
                                        dialog.execute("PF('biblioErrDialog').show();");
                                        //rowSampleExcel.clear();
                                        return;
                                    }

                                    //Se valida si el tag2 tiene valor en base i5 dependiendo de la plataforma seleccionada
                                    //Verificamos el tipo de plataforma 
                                    if (itemPlataform.equals("HiSeq") || itemPlataform.equals("MiSeq") || itemPlataform.equals("Oxford Nanopore")) {
                                        if (ejbBar.findBarcodeByIndexName(tag2).get(0).getBasei5Miseq() == null) {
                                            messageDialog = "Para la muestra " + NameLib + " no es correcto el TAG 2 basei5miseq: " + tag2 + "  , favor de revisarlo";
                                            closeStatusDialogUploadFile();
                                            dialog.execute("PF('biblioErrDialog').show();");
                                            //rowSampleExcel.clear();
                                            return;
                                        }
                                    } else if (itemPlataform.equals("NovaSeq") || platafrom.equals("iSeq") || itemPlataform.equals("NextSeq500")) {
                                        if (ejbBar.findBarcodeByIndexName(tag2).get(0).getBasei5Nextseq() == null) {
                                            messageDialog = "Para la muestra " + NameLib + " no es correcto el TAG 2 basei5nextSeq: " + tag2 + "  , favor de revisarlo";
                                            closeStatusDialogUploadFile();
                                            dialog.execute("PF('biblioErrDialog').show();");
                                            //rowSampleExcel.clear();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    } //Cuando estemos en la otra vista de diferentes kits
                    else {
                        //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                        //Entonces no habrá tag 2
                        if (!itemPlataform.equals("Oxford Nanopore")) {
                            if (!itemKit.equals("TruSeq smallRNA")) {
                                if (!itemKit.equals("TruSeq HT")) {
                                    // Se valida si el tag2 escrito en el excel es correcto
                                    if (ejbBar.findBarcodeByIndexName(tag2).isEmpty()) {
                                        messageDialog = "Para la muetsra " + NameLib + " no se reconoce el TAG 2, favor de revisarlo";
                                        closeStatusDialogUploadFile();
                                        dialog.execute("PF('biblioErrDialog').show();");
                                        //rowSampleExcel.clear();
                                        return;
                                    }
                                }
                            }
                        }
                    }
//asas
                    //Valida si la combinación de tag 1 y 2 se repite
                    //Buscamos el kit en la BD
                    String conTag1 = ejbBar.findBarcodeByIndexName(tag1).get(0).getIndexName() + "-" + ejbBar.findBarcodeByIndexName(tag1).get(0).getBasei7();
                    String conTag2 = "";
                    if (!itemKit.equals("TruSeq smallRNA")) {
                        conTag2 = getIndexBasei5Create(ejbBar.findBarcodeByIndexName(tag2).get(0));

                        //recibo esto en consola-- Se imprime combinación de tags: N701-TAAGGCGAS501-GCGATCTA]]
                    }
                    System.out.println("Se imprime combinación de tags: " + conTag1 + conTag2);
                    if (rowSampleExcel.indexOf(conTag1 + conTag2) >= 0) {
                        messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " la combinación de Tag's ya ha sido repetido";
                        closeStatusDialogUploadFile();
                        dialog.execute("PF('biblioErrDialog').show();");
                        return;
                    }
                    rowSampleExcel.add(conTag1 + conTag2);
                    System.out.println("Se imprime roxSampleExcel: " + rowSampleExcel);
                    // recibo esto en consola -- Se imprime roxSampleExcel: [N701-TAAGGCGAS501-GCGATCTA]]]

                    if (diferentKit == true) {
                        //validar si la plataforma está bien escrita
                        if (listPlataform.indexOf(itemPlataform) < 0) {
                            messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " la plataforma es incorrecta, favor de revisarlo";
                            closeStatusDialogUploadFile();
                            dialog.execute("PF('biblioErrDialog').show();");
                            return;
                        }

                        //Validar que la plataforma sea la correcta para la muestra
                        //validamos si existe la muestra con el id_muestra de la columna 1 del excel
                        //String idNameSample = (parameters.get(0).replaceAll(" -.*", "")).replace("[", "");
                        //Validamos si es nombre o id de muestra
                        //comprobamos si solo son números
                        if (idNameSample.matches("[0-9]+")) {
                            int itemIdSample = Integer.parseInt(idNameSample);
                            if (!ejbSam.findSampleById(itemIdSample).get(0).getSamplePlataform().equals(itemPlataform)) {
                                messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " la plataforma no es la indicada, favor de revisarlo";
                                closeStatusDialogUploadFile();
                                dialog.execute("PF('biblioErrDialog').show();");
                                return;
                            }
                        } else {
                            if (!ejbSam.findSampleByName(idNameSample).get(0).getSamplePlataform().equals(itemPlataform)) {
                                messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " la plataforma no es la indicada, favor de revisarlo";
                                closeStatusDialogUploadFile();
                                dialog.execute("PF('biblioErrDialog').show();");
                                return;
                            }
                        }

                        //validar si el kit está bien escrito
                        if (listKits.indexOf(itemKit) < 0) {
                            messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " el kit puede estar mal escrito, favor de revisarlo";
                            closeStatusDialogUploadFile();
                            dialog.execute("PF('biblioErrDialog').show();");
                            return;
                        }

                        //Validar que el kit escrito pertenezca a la plataforma escrita
                        if (plataformKit.indexOf(itemPlataform + " " + itemKit) < 0) {
                            messageDialog = "Para la muestra " + parameters.get(MUESTRAS) + " el kit no pertenece a la plataforma escrita, favor de revisarlo";
                            closeStatusDialogUploadFile();
                            dialog.execute("PF('biblioErrDialog').show();");
                            return;
                        }
                    }

                }
            }

            //Comparamos si la cantidad de muestras es la misma a la del archivo excel
            System.out.println("L validación final queda: count1: " + count1 + ", numLib: " + numLib + ", finalValidation: " + finalValidation);

            // en consola recibo --  L validación final queda: count1: 2, numLib: 1, finalValidation: 1]]
            if (numLib != (count1 - finalValidation)) {

                System.out.println("Se imprime el número de filas: " + count1 + " y numero de muestras: " + numLib);
                messageDialog = "La cantidad de muestras no es la misma a la del archivo con los seleccionados, verifique su documento o vuelva a seleccionar las muestras";
                closeStatusDialogUploadFile();
                dialog.execute("PF('biblioErrDialog').show();");
                return;
            }

            //Validar que todas las filas sean correctas ------------------------------------------------------------------FINAL
        } catch (Exception e) {
            messageDialog = "Ups ocurrió un error duranto el proceso, por favor contacte al mantenimiento, tome una captura de pantalla y anexe lo siguiente: " + e.getMessage();
            closeStatusDialogUploadFile();
            dialog.execute("PF('biblioErrDialog').show();");
            return;
        }

        System.out.println("********************************************************************* Pasamos la validación");
        try {

            System.out.println("---------------------------------- Entramos a crear las librerías");
            System.out.println("numero de librerías: " + numLib);
            int count = 0;
            for (Row row : sheet) {
                count++;
                System.out.println("*************************** count: " + count);
                System.out.println("*************************** librerías: " + numLib);
                if (count >= (numLib + rowStartRegistration)) {
                    //Entramos a la validación de numero de librerias;
                    break;
                }
                // System.out.println("fila "+row);
                //System.out.println(row.);
                ////////conexion a la BD///
                //Class.forName("org.postgresql.Driver");
                //Connection conexion = DriverManager.getConnection(url, usuario, pswd);
                //java.sql.Statement st = conexion.createStatement();
                //Connection conex = DriverManager.getConnection(url, usuario, pswd);
                //java.sql.Statement St = conex.createStatement();
                /////////////////////////////////////////////////////////////////////
                //System.out.println("valor del contador "+count);
                if (count >= rowStartRegistration) {

                    List<String> parameters = new ArrayList<>();

                    //List<String> tags1 = new ArrayList<>();
                    // List<String> tags2 = new ArrayList<>();
                    System.out.println("lim " + row.getFirstCellNum() + "  " + row.getLastCellNum());
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        System.out.println("Valor de cn :" + cn);
                        System.out.println("ultima celda: " + row.getLastCellNum());
                        Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case STRING:
                                parameters.add(cell.getStringCellValue());
                                // tags1.add(cell.getStringCellValue());
                                // tags2.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                parameters.add("" + (int) cell.getNumericCellValue());

                                break;
                            case BLANK:
                                parameters.add(cell.getStringCellValue());
                                //parameters.add("-------");
                                break;
                        }
                    }
                    System.out.println("campo 1, nombre biblioteca: " + parameters.get(MUESTRAS));
                    System.out.println("campo 2,tag1: " + parameters.get(TAG_1));
                    System.out.println("campo 3,tag2: " + parameters.get(TAG_2));

                    if (parameters.get(0).trim().equals("")) {
                        //rowStartValidation = 1;
                        break;
                    }

                    /*
                    if (parameters.get(TAG_1).isEmpty() | parameters.get(TAG_2).isEmpty()) {

                        System.out.println("esta vacio");
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("Error", "El archivo contiene columnas vacias,verifique su contenido por favor"));
                        break;
                    } else {   */
                    rowSampleExcel.add(parameters.get(MUESTRAS).trim());
                    rowSampleExcel.add(parameters.get(TAG_1).trim());
                    rowSampleExcel.add(parameters.get(TAG_2).trim());
                    System.out.println("no esta vacio");
                    List<Object> prop = (List<Object>) con.getExternalContext().getSessionMap().get("libraryProperties");

                    String NameLib = parameters.get(MUESTRAS).trim();
                    String tag1 = parameters.get((TAG_1)).trim();
                    String tag2 = parameters.get(TAG_2).trim();

                    String itemPlataform;
                    String itemKit;
                    if (diferentKit == true) {
                        itemPlataform = parameters.get(PLATAFORM).trim();
                        itemKit = parameters.get(KIT).trim();
                    } else {
                        itemPlataform = platafrom;
                        itemKit = kit;
                    }

                    //String tg1 = "Select id_index from barcodes where id_barcode='" + tag1 + "'" + "and tag_type='i7'";//consulta para obtener el id_index del tag1
                    //String tg2 = null;
                    //ResultSet rs = st.executeQuery(tg1);
                    //ResultSet rst = null;
                    //Remplazando el Resultset
                    Barcodes itemBarcode1 = ejbBar.findBarcodeByIndexName(tag1).get(0);
                    Barcodes itemBarcode2 = null;

                    System.out.println("Barcode1 encontrado:           ------------------------------------------ Barcode 1");
                    System.out.println(itemBarcode1);
                    System.out.println("Barcode1 encontrado:           ------------------------------------------ Barcode 1");

                    //Validamos cuando el kit sea ‘TruSeq Small RNA’  y para todos los kit's de la plataforma ‘Oxford Nanopore’
                    //No habrá ningún tipo de validación para el tag 2
                    if (diferentKit == false) {
                        //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                        //Entonces no habrá tag 2
                        if (!itemPlataform.equals("Oxford Nanopore")) {
                            if (!itemKit.equals("TruSeq smallRNA")) {
                                if (!itemKit.equals("TruSeq HT")) {
                                    //tg2 = "Select id_index from barcodes where id_barcode='" + tag2 + "'" + "and tag_type='i5'";//consulta para obtener el id_index del tag2
                                    //rst = St.executeQuery(tg2);
                                    itemBarcode2 = ejbBar.findBarcodeByIndexName(tag2).get(0);
                                    System.out.println("Barcode2 encontrado:           ------------------------------------------ Barcode 2");
                                    System.out.println("diffkit=false   :" + itemBarcode2);

                                }
                            }
                        }
                    } else {
                        //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                        //Entonces no habrá tag 2
                        if (!itemPlataform.equals("Oxford Nanopore")) {
                            if (!itemKit.equals("TruSeq Small RNA")) {
                                if (!itemKit.equals("TruSeq HT")) {
                                    //tg2 = "Select id_index from barcodes where id_barcode='" + tag2 + "'" + "and tag_type='i5'";//consulta para obtener el id_index del tag2
                                    //rst = St.executeQuery(tg2);
                                    itemBarcode2 = ejbBar.findBarcodeByIndexName(tag2).get(0);
                                    System.out.println("barcode 2: " + itemBarcode2);

                                }
                            }
                        }
                    }
                    System.out.println("Nombre de la biblioteca");
                    System.out.println(NameLib);
                    // hast aqui llego subida x csv
                    //System.out.println("Ciclos 1: " + prop.get(2).toString().replace("pb", ""));

                    /*if (libraryType.equals("Paired End")) {
                        System.out.println("Ciclos 2: " + prop.get(2).toString().replace("pb", ""));

                    } else {
                        System.out.println("Ciclos 2: null");
                    }*/
                    Library library = new Library();
                    library.setLibraryName(NameLib);
                    library.setPreparationStatus("Construida");

                    System.out.println("ya se asignaron nombte y status ");

                    //Validamos cuando el kit sea ‘TruSeq Small RNA’  y para todos los kit's de la plataforma ‘Oxford Nanopore’
                    //No habrá ningún tipo de validación para el tag 2
                    if (diferentKit == false) {
                        //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                        //Entonces no habrá tag 2
                        if (!itemPlataform.equals("Oxford Nanopore")) {
                            if (!itemKit.equals("TruSeq smallRNA")) {
                                if (!itemKit.equals("TruSeq HT")) {

                                    //ADAPTAR CODIGO validar  --------------------------------------------------------------------    debido a que ahora la biblioteca recibe el id del barcode
                                    System.out.println("tag1: " + tag1 + " id_index: " + itemBarcode1);
                                    library.setIdBarcode1(itemBarcode1);
                                    System.out.println("tag2: " + tag2 + " id_index: " + itemBarcode2);
                                    library.setIdBarcode2(itemBarcode2);
                                } else {
                                    System.out.println("tag1: " + tag1 + " id_index: " + itemBarcode1);
                                    library.setIdBarcode1(itemBarcode1);
                                    //System.out.println("tag2: " + tag2 + " id_index: " + rst.getString(1));
                                    //library.setIdBarcode2(new Barcodes());
                                }
                            } else {
                                //ADAPTAR 

                                System.out.println("tag1: " + tag1 + " id_index: " + itemBarcode1);
                                library.setIdBarcode1(itemBarcode1);
                                //System.out.println("tag2: " + tag2 + " id_index: " + rst.getString(1));
                                //library.setIdBarcode2(new Barcodes());

                            }
                        } else {
                            //ADAPTAR CODIGO debido a que ahora no tiene esta propiedad

                            System.out.println("tag1: " + tag1 + " id_index: " + itemBarcode1);
                            library.setIdBarcode1(itemBarcode1);
                            //System.out.println("tag2: " + tag2 + " id_index: " + rst.getString(1));
                            //library.setIdBarcode2(new Barcodes());
                        }
                    } else {
                        //Validamos cuando el kit sea "TruSeq Small RNA"  y para todos los kit's de la plataforma "Oxford Nanopore"
                        //Entonces no habrá tag 2
                        if (!itemPlataform.equals("Oxford Nanopore")) {
                            if (!itemKit.equals("TruSeq smallRNA")) {
                                if (!itemKit.equals("TruSeq HT")) {

                                    System.out.println("tag1: " + tag1 + " id_index: " + itemBarcode1);
                                    library.setIdBarcode1(itemBarcode1);
                                    System.out.println("tag2: " + tag2 + " id_index: " + itemBarcode2);
                                    library.setIdBarcode2(itemBarcode2);

                                } else {

                                    System.out.println("tag1: " + tag1 + " id_index: " + itemBarcode1);
                                    library.setIdBarcode1(itemBarcode1);
                                    //System.out.println("tag2: " + tag2 + " id_index: " + rst.getString(1));
                                    //library.setIdBarcode2(new Barcodes());
                                }

                            } else {
                                //ADAPTAR CODIGO debido a que ahora no tiene esta propiedad

                                System.out.println("tag1: " + tag1 + " id_index: " + itemBarcode1);
                                library.setIdBarcode1(itemBarcode1);
                                //System.out.println("tag2: " + tag2 + " id_index: " + rst.getString(1));
                                //library.setIdBarcode2(new Barcodes());
                            }
                        } else {
                            //ADAPTAR CODIGO debido a que ahora no tiene esta propiedad

                            System.out.println("tag1: " + tag1 + " id_index: " + itemBarcode1);
                            library.setIdBarcode1(itemBarcode1);
                            //System.out.println("tag2: " + tag2 + " id_index: " + rst.getString(1));
                            //library.setIdBarcode2(new Barcodes());
                        }
                    }

                    System.out.println("Barcode2 encontrado:           ------------------------------------------ Barcode 2");
                    //System.out.println(itemBarcode2);
                    System.out.println("Barcode2 encontrado:           ------------------------------------------ Barcode 2");

                    library.setPreparationDate(fechaPrep);
                    //ADAPTAR CODIGO debido a que ahora no tiene esta propiedad
                    /*
                    library.setPlataform(itemPlataform);
                    library.setKit(itemKit);
                     */
                    System.out.println("se asigno la fecha" + fechaPrep.toString());
                    //Buscamos el link y plataforma
                    Kit findKit = ejbKit.findKitByName(itemKit).get(0);
                    Plataform findPlataform = ejbPlataform.findPlataformByName(itemPlataform).get(0);
                    //Creamos un nuevo objeto de link de plataforma/kit
                    PlataformLinkKit plataformKit = new PlataformLinkKit();
                    plataformKit = (PlataformLinkKit) ejbPlataformLinkKit.findPlataformLinkKitByIds(findPlataform.getIdPlataform(), findKit.getIdKit()).get(0);
                    //Asignamos el objeto al set de paltaforma/kit de library
                    library.setPlataformLinkKit(plataformKit);
                    System.out.println("se asigno laplataforma");

                    //Codigo inutilizable; ya no tiene la propiedad type la biblioteca
                    /* if (prop.get(1).equals("1") || platafrom.equals("Oxford Nanopore") || platafrom.equals("Pac Bio")) {

                        libraryType = "Sigle Read";
                    }*/
                    //ADAPTAR CODIGO debido a que ahora no tiene esta propiedad
                    //library.setType(libraryType);
                    //String inse = (String) prop.get(3);
                    //int insert = Integer.parseInt(inse);
                    // System.out.println("tamaño de insert"+inse);
                    //ADAPTAR CODIGO debido a que ahora no tiene esta propiedad
                    /*
                    library.setInsertSize(insert);
                    int cycle = Integer.parseInt(prop.get(2).toString().replaceAll("[ p|b]b$", "").replaceAll("\\s", ""));
                    library.setCycles_1(cycle);

                    if (libraryType.equals("Paired End") || libraryType.equals("Mate Pair")) {
                        library.setCycles_2(cycle);
                    }
                     */
                    library.setUserName(us.getUserName());
                    System.out.println("asigno user: " + us.getUserName().toString());

                    getFacade().create(library);
                    System.out.println("-------Libreria creada----------");
                    String name = parameters.get(0).replaceAll(" -.*", "");

                    //Se realiza el registro en la tabla SampleLibraryLink 
                    //Se realiza la busqueda del Sample actual de la iteración
                    //List<Sample> ids = ejbSam.findSampleByName(name.replace("[", ""));
                    Sample itemSample;

                    //Validamos si es nombre o id de muestra
                    //comprobamos si solo son números
                    if (name.matches("[0-9]+")) {
                        int itemIdSample = Integer.parseInt(name);
                        itemSample = ejbSam.findSampleById(itemIdSample).get(0);

                    } else {
                        itemSample = ejbSam.findSampleByName(name.replace("[", "")).get(0);
                    }

                    //List <Sample> ids=getSampleIds(item.getListSample());
                    SampleLibraryLink sll = new SampleLibraryLink();
                    sll.setSampleLibraryLinkPK(new jpa.entities.SampleLibraryLinkPK());
                    sll.getSampleLibraryLinkPK().setIdSample(itemSample.getIdSample());
                    sll.getSampleLibraryLinkPK().setIdLibrary(library.getIdLibrary());
                    sll.setSample(itemSample);
                    sll.setLibrary(library);
                    ejbSll.create(sll);

                    //Llenado en la tabla de coments
                    String statusAnt = itemSample.getStatus();
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -En espera de secuenciacion- por " + us.getUserName());
                    commentsSample.setIdType(itemSample.getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);
                    commentFac.createComment(commentsSample);

                    itemSample.setStatus("En espera de secuenciacion");
                    ejbSam.edit(itemSample);

                    //}//libs.clear();
                    showDialog(3);

                    //Nos olvidamos de las conexiones en este controlador
                    /*
                    st.close();
                    System.out.println("cierra primer statement");
                    St.close();
                    System.out.println("Cierra 2do statement");
                    conexion.close();
                    System.out.println("cierra primer conexion");
                    conex.close();
                    System.out.println("cierra 2da conexion");
                     */
                }

                entrada.close();

            }
            //libs.clear();

            System.out.println("limpia la lista libs");

        } catch (Exception e) {
            System.out.println("algo sucedio " + e);
            /*
            if (e.getMessage().equals("java.util.ConcurrentModificationException")) {
                //Comentario realizado por los antiguos programadores andes de Linabat
                //System.out.println("aqui vale madre");

            } else {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Error", "Ocurrio un error en la carga del archivo, revise su contenido por favor"));
            }//context.getExternalContext().redirect("Error.xhtml");
             */
        }

    }

    //Método para cerrar mensaje modular de statusDialogUploadFile
    public void closeStatusDialogUploadFile() {
        RequestContext dialog = RequestContext.getCurrentInstance();
        dialog.execute("PF('statusDialogUploadFile').hide();");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void uploadFileLib(FileUploadEvent event) {

        UploadedFile file = event.getFile();
        BufferedReader br;
        int cont = 0;
        try {
            InputStream stream = file.getInputstream();
            br = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while (br.ready() && (line = br.readLine()) != null) {

                sb.append(line + "\r\n");

                cont++;
                System.out.println("cont ahora vale: " + cont);
                //el contador incluye la fila de titulos de columnas
            }

            String[] corteEnters = sb.toString().split("\n");
            // System.out.println(" corte enters="+c);

            if (checkEditLibraries(corteEnters)) {

                for (int i = 0; i < corteEnters.length; i++) {
                    if (i > 0) {
                        String[] corteComas = corteEnters[i].replace("\"", "").split(",");
                        //String id = corteComas[0] + corteComas[1] + corteComas[2];
                        String id = corteComas[0]; //id_colocado desde el excel

                        for (Library selectedLibrary : selectedLibraries) {

                            //String idLib = selectedLibrary.getLibraryName() + selectedLibrary.getIdBarcode1().toString().replace("[", "").replace("]", "");
                            String idLib = selectedLibrary.getIdLibrary().toString();
                            /*if (selectedLibrary.getIdBarcode2() != null) {
                                idLib += selectedLibrary.getIdBarcode2().toString().replace("[", "").replace("]", "").replace("null", "");
                            }*/
                            System.out.println("***" + id);
                            System.out.println("+++" + idLib);
                            if (id.equals(idLib)) {

                                String nombrelib = corteComas[1].trim();
                                String cons_qpcr = corteComas[2].trim();
                                String cons_qubit = corteComas[3].trim();
                                String sizelib = corteComas[4].trim();

                                if (!nombrelib.equals("")) {
                                    System.out.println("NOMBRE LIB :" + nombrelib);
                                    selectedLibrary.setLibraryName(nombrelib);
                                } else {
                                    System.out.println("nombre vacio");
                                }

                                if (!cons_qpcr.equals("")) {
                                    System.out.println("QPCR :" + cons_qpcr);
                                    selectedLibrary.setQpcrConcentration(Double.parseDouble(cons_qpcr));
                                } else {
                                    System.out.println("qpcr vacio");
                                }

                                if (!cons_qubit.equals("")) {
                                    System.out.println("QUBIT :" + cons_qubit);
                                    selectedLibrary.setQubitConcentration(Double.parseDouble(cons_qubit));
                                } else {
                                    System.out.println("qubit vacio");
                                }

                                if (!sizelib.equals("")) {
                                    System.out.println("SIZE :" + sizelib);
                                    selectedLibrary.setLibrarySize(Integer.parseInt(sizelib));
                                } else {
                                    System.out.println("size vacio");
                                }

                                if (!sizelib.equals("") & !cons_qubit.equals("")) {
                                    double nano = (Double.parseDouble(cons_qubit) / (660 * Double.parseDouble(sizelib))) * 1000000;
                                    selectedLibrary.setNanomolarConcentration(nano);
                                } else {
                                    System.out.println("nanomolar sin cambios");
                                }
                                /*
                                selectedLibrary.setQpcrConcen(Double.parseDouble(corteComas[3]));
                                selectedLibrary.setQubitConcen(Double.parseDouble(corteComas[4]));
                                selectedLibrary.setInsertSize(Integer.parseInt(corteComas[5].trim()));
                                selectedLibrary.setNanoConcen(selectedLibrary.getNanoConcen());

                                ejbLib.edit(selectedLibrary);
                                 */
                                ejbLib.edit(selectedLibrary);
                                System.out.println("se actualizo la biblioteca");
                            }

                        }
                    }//si la i es mayor a 0  es decir si la fila no es la del titulo
                } ///fin del for

//            for (String corteEnter : corteEnters) {
//                 String [] corteComas=corteEnter.split(",");
//                 System.out.println("+++>>>"+corteEnter);
//                 for (String corteComa : corteComas) {
//                     System.out.println(">>>"+corteComa);
//                }
//               
//            }
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('libAceptDialog').show();");

            } else {

                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('libErrorDialog').show();");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LibraryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(LibraryController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en el archivo");
        }

    }

    public boolean checkEditLibraries(String[] listLibraries) {
        System.out.println("Entra a checkLibraries");
        if (listLibraries.length - 1 != selectedLibraries.size()) {
            System.out.println("No son iguales");
            return false;
        }

        List<String> names = new ArrayList<>();
        List<String> ids_excel = new ArrayList<>();
        List<String> namesToCompare = new ArrayList<>();

        for (int i = 0; i < listLibraries.length - 1; i++) {

            if (i > 0) {

                String[] comaSeparated = listLibraries[i].replace("\"", "").split(",");
                //posicion0=idlib ;posicion1=nomlib ;posicion2=qpcr ;posicion3= qubit ;posicion4=sizelib                                       
                String idlib_ = comaSeparated[0].trim();
                String nombrelib_ = comaSeparated[1].trim();
                String cons_qpcr_ = comaSeparated[2].trim();
                String cons_qubit_ = comaSeparated[3].trim();
                String sizelib_ = comaSeparated[4].trim();

                System.out.println(" Cadena...  para la fila:" + i + "id= " + idlib_ + "  nom= " + nombrelib_ + "  qpcr= " + cons_qpcr_ + " qubit= " + cons_qubit_ + " size= " + sizelib_);

                //para poder  comparar los nombres repetidos de biblioetcas agregamos ese campo a una lista  solo el nombre de la lib 
                //posicion0=idlib ;posicion1=nomlib ;posicion2=qpcr ;posicion3= qubit ;posicion4=sizelib  
                names.add(nombrelib_);
                ids_excel.add(idlib_);
            }
        }
        for (Library selectedLibrary : selectedLibraries) {

            //String line = selectedLibrary.getLibraryName() + selectedLibrary.getIdBarcode1().toString().replace("[", "").replace("]", "");
            String line = selectedLibrary.getIdLibrary().toString();
            //if (selectedLibrary.getIdBarcode2() != null) {
            //  line += selectedLibrary.getIdBarcode2().toString().replace("[", "").replace("]", "").replace("null", "");
            //}
            //aqui deberia ser una lista de ids de bibliotecas 
            namesToCompare.add(line);
            //aqui debe cambiar lo que se va a comparar

        }
        Collections.sort(names);
        Collections.sort(ids_excel); //ids de las lib desde el excel
        Collections.sort(namesToCompare);//ids de las lib seleccionadas

        for (int i = 0; i < names.size() - 1; i++) {

            if (names.get(i).equals(names.get(i + 1))) {

                System.out.println("Existe un nombre igual dentro del excel");
                return false;

            }
        }
        for (int i = 0; i < names.size(); i++) {
            //aqui debe compararse los ids no los nombres, porque eso se puede modificar
            if (!(ids_excel.get(i).equals(namesToCompare.get(i)))) {
                System.out.println("No son las mismas muestras");
                return false;

            } else {
                System.out.println("-------------------");

                System.out.println(names.get(i));
                System.out.println(namesToCompare.get(i));
                System.out.println("-------------------");

            }

        }

        return true;
    }

    public boolean rederLibraryUpdate() {

        return selectedLibraries.size() <= 0;

    }

    public void redirectToProj() {

        FacesContext context = FacesContext.getCurrentInstance();
        try {

            context.getExternalContext().redirect("../project/ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void NewsBarcodes(FileUploadEvent event) throws FileNotFoundException, IOException, InvalidFormatException {
        DataInputStream entrada = new DataInputStream(event.getFile().getInputstream());
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext Context = RequestContext.getCurrentInstance();

        XSSFWorkbook workbook = new XSSFWorkbook(entrada);
        XSSFSheet sheet = workbook.getSheetAt(0);

        int countRow = 0;
        try {
            for (Row row : sheet) {
                countRow++;
                if (countRow >= 2) {
                    List<String> parameters = new ArrayList<>();
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case STRING:
                                parameters.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                parameters.add("" + (int) cell.getNumericCellValue());
                                break;
                            case BLANK:
                                parameters.add("-------");
                                break;
                        }
                    }
                    //ADAPTAR CODIGO debido a que ahora no tiene esta propiedad
                    /*
                    System.out.println(parameters.get(0));
                    Barcodes bar = new Barcodes();
                    bar.setIdIndex(parameters.get(0));
                    bar.setIdBarcode(parameters.get(1));
                    bar.setSequence(parameters.get(2));
                    bar.setTagType(parameters.get(3));
                    bar.setKitName(parameters.get(4));
                    ejbBar.create(bar);
                    System.out.println("crea nueva biblioteca");
                     */

                }
                entrada.close();

            }

        } catch (Exception e) {
            System.out.println("Ocurrio un error: " + e);
        }
    }

    public void getRunName(Run name) {
        System.out.println("Entra al metodo que obtiene el nombre de la corrida");
        System.out.println(name);
        NameRun = name.getRunName();
        runDate = name.getRunStartday();
        kitPerformance = name.getPerformance();
    }

    public void DeleteLibraries() {
        System.out.println("entra al metodo para eliminar librerias");
        for (Library delete : libRun) {
            List<LibraryRunLink> lr = ejbLibraryRun.findRunByLibrary(delete.getIdLibrary());
            for (LibraryRunLink libs : lr) {
                ejbLibraryRun.remove(libs);
                System.out.println("Se elimino la libreria");
            }

        }
    }

    public void DeleteLib(int idLi) {
        //27 agosto chaos
        //primero debo validar que no existan ligas de esa libreria con una corrida
        int count = 0;
        for (Library lib_encontrada : libRun) {
            List<LibraryRunLink> lr = ejbLibraryRun.findRunByLibrary(lib_encontrada.getIdLibrary());
            for (LibraryRunLink libs : lr) {
                //ejbLibraryRun.remove(libs);
                count = count + 1;
                System.out.println("Existe una liga a corrida, no se puede eliminar la biblioteca");
            }
        }
        if (count <= 0) {
            //No hay liga a corridas
            //debo buscar los registros en  SampleLibraryLink -->ejbSLLF

            //busca entre todas la bibliotecas registrdadas en library
            RequestContext Context = RequestContext.getCurrentInstance();
            List<Library> delLib = ejbLib.findAll();
            //comparo las filas que me devolvio la consulta y comparo con el id de las libs que seleecione
            //si concide con la busqueda, mando a llamar el metodo remove
            try {
                for (Library deleteLib : delLib) {
                    if (deleteLib.getIdLibrary().equals(idLi)) {
                        System.out.println("nombre de la biblioteca: " + deleteLib.getLibraryName());
                        ejbLib.remove(deleteLib);
                        System.out.println("biblioteca elminada");
                    }
                }

                Context.execute("PF('dialogDeleteSample').show();");

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println("Existe una liga a corrida, no se puede eliminar la biblioteca");
        }

    }

    public void checkSelectedLibraries() {
        FacesContext context = FacesContext.getCurrentInstance();

        for (Library itemLibrary : libRun) {
            //Validamos si la biblioteca contiene barcodes;
            if (itemLibrary.getIdBarcode1() == null) {
                RequestContext rc = RequestContext.getCurrentInstance();
                rc.execute("PF('modulDialog').show();");
                messageDialog = "No se puede crear corrida con la biblioteca: " + itemLibrary.getLibraryName() + ", debido a que no contiene tag 1 y 2.";
                return;
            }
        }

        if (libRun.isEmpty()) {
            RequestContext rc = RequestContext.getCurrentInstance();
            messageDialog = "Debe seleccionar bibliotecas";
            rc.execute("PF('modulDialog').show();");

        } else {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('DialogCreate').show()");
        }
    }

    public boolean checkPlataform() {
        boolean type = false;
        try {
            for (Library libRuns : libRun) {
                List<SampleLibraryLink> runLib = getFacade().findRuns(libRuns.getIdLibrary());
                for (SampleLibraryLink runs : runLib) {

                    if (runs.getLibrary().getPlataformLinkKit().getPlataform().getPlataformName().equals("Oxford Nanopore")) {
                        type = false;

                    } else {
                        type = true;

                    }

                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(type);
        return type;
    }

    public boolean checkQuatationProj(String pj) {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        boolean quotation = false;
        List<jpa.entities.Files> list = ejbFiles.findAll();

        for (jpa.entities.Files files : list) {
            if ((files.getIdProject().getIdProject() == pj) && (files.getFileType().equals("Cotizacion"))) {
                //System.out.println(files.getFileName());
                quotation = true;
            }
        }
        return quotation;
    }

    public boolean checkPaymentProj(String pj) {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        boolean pay = false;
        List<jpa.entities.Files> list = ejbFiles.findAll();

        for (jpa.entities.Files files : list) {
            if ((files.getIdProject().getIdProject() == pj) && (files.getFileType().equals("Pago"))) {
                //System.out.println(files.getFileName());
                pay = true;
            }
        }
        return pay;

    }

    public String getSamplesProj(Project proj) {

        List<Sample> sm = ejbSam.findSampProj2(proj);
        String bioInfo = "false";
        for (Sample sam : sm) {
            // System.out.println(sam.getIdSample());
            List<BioinformaticAnalysis> BA = BAFac.findBionformaticAnalysisBySample(sam);
            if (BA.size() > 0) {
                bioInfo = "true";
            }
            System.out.println("Requiere analisis? " + bioInfo);
        }
        return bioInfo;

    }

    public String linkFileSampleSheet(String nameFile) {
        File f = new File(pathCreateSampleFiles + nameFile);
        if (f.exists()) {
            return fileProjectPath + nameFile;
        }
        return null;
    }

    //Métetodo para bloquear el botón de "Registrar con diferentes kits"
    public boolean canDiferentKits() {
        if (listSampleLibraries.get(0).getSample().getSamplePlataform().equals("Oxford Nanopore - NextSeq500")) {
            return true;
        } else if (listSampleLibraries.get(0).getSample().getSamplePlataform().equals("Oxford Nanopore - MiSeq")) {
            return true;
        } else if (listSampleLibraries.get(0).getSample().getSamplePlataform().equals("Oxford Nanopore - HiSeq")) {
            return true;
        } else if (listSampleLibraries.get(0).getSample().getSamplePlataform().equals("Oxford Nanopore - iSeq")) {
            return true;
            // }else if (listSampleLibraries.get(0).getSample().getSamplePlataform().equals("NovaSeq 6000 - Oxford Nanopore")) {
            //   return true;
        }

        return false;

    }

    //obtener barcode 1 mediente idLibrary por medio de una vista
    public String getBarcode1ByIdLibrary(int idLibrary) {
        BarcodesCons barcode = getEjbFacade().findBarcodeByIdLibrary(idLibrary);
        return barcode.getIdBarcode1();
    }

    //obtener barcode 2 mediente idLibrary por medio de una vista
    public String getBarcode2ByIdLibrary(int idLibrary) {
        BarcodesCons barcode = getEjbFacade().findBarcodeByIdLibrary(idLibrary);
        return barcode.getIdBarcode2();
    }

    //lista de bibliotecas por proyecto
    public List<Library> getLibsByProject() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        List<Sample> s = ejbSam.findSampProj2(proj);
        List<Library> mainList = new ArrayList<>();
        for (Sample sample : s) {
            List<Library> L = getFacade().getLibraries(sample);
            for (Library library : L) {
                mainList.add(library);
            }
        }

        //System.out.println("Se imprime lista: ***************************************************");
        System.out.println(mainList);
        System.out.println("Se imprime lista: ***************************************************");

        return mainList;
    }

    public void resetListLibRun() {
        libRun.clear();
    }

    //Obtener bibliotecas por nombre de corrida
    public List<Library> librariesByRunName() {

        if (runNameSelected != null) {

            System.out.println("Seleccionamos la corrida: " + runNameSelected);

            //Buscamos la corrida por medio del nombre
            List<Run> findRn = ejbRun.findRun(runNameSelected);

            Run selectedRun = findRn.get(0);
            /*
            List<Library> listLibrary = new ArrayList<>();
            if (selectedRun != null) {
                System.out.println("//// ********************************************************* ViewIdLibraryLibraryRunLink");
                //Se hace la consulta de bilciotecas con idRun por medio de una vista
                List<ViewIdLibraryLibraryRunLink> list = getFacade().findIdLibrariesByIdRun(selectedRun.getIdRun());
                for (ViewIdLibraryLibraryRunLink viewLibrary : list) {
                    System.out.println(" objeto ViewIdLibraryLibraryRunLink: " + viewLibrary);
                    System.out.println(" objeto id_library: " + viewLibrary.getIdLibrary());
                    System.out.println(" id de barcode:");
                    Library lib = new Library();
                    lib = ejbLib.find(viewLibrary.getIdLibrary());
                    System.out.println("objeto lib antes de insertar barcodes: " + lib);
                    //Obtener los barcodes y asignarlos al nuevo library
                    BarcodesCons b = ejbBar.findBarcodesByIdBarcode(lib.getIdLibrary());

                    System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");
                    //Consultamos desde la vista los id de biblioteca y barcodes 1 y 2
                    ViewIdLibraryBarcodes12 vlb = ejbLib.finIdLibraryBarcodesByIdLibrary(viewLibrary.getIdLibrary()).get(0);

                    System.out.println("------ Vista ViewBarcodes   ------------------");
                    //consultamos la vista de barcode para obtener sus propiedades
                    ViewBarcodes vBarcodes1 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);

                    System.out.println("------ Barcodes barExp1   ------------------");
                    //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag1
                    Barcodes barExp1 = new Barcodes();
                    barExp1.setIdIndex(vBarcodes1.getIdIndex());
                    barExp1.setSequence(vBarcodes1.getSequence());
                    barExp1.setIdBarcode(vBarcodes1.getIdBarcode());
                    barExp1.setTagType(vBarcodes1.getTagType());
                    barExp1.setKitName(vBarcodes1.getKitName());
                    System.out.println("------ Barcodes barExp1   ------------------");

                    System.out.println("------ Barcodes barExp2   ------------------");
                    //Asignamos las propiedades de la vista de Barcodes a la entidad de Barcodes como tag2
                    ViewBarcodes vBarcodes2 = ejbBar.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);
                    Barcodes barExp2 = new Barcodes();
                    barExp2.setIdIndex(vBarcodes2.getIdIndex());
                    barExp2.setSequence(vBarcodes2.getSequence());
                    barExp2.setIdBarcode(vBarcodes2.getIdBarcode());
                    barExp2.setTagType(vBarcodes2.getTagType());
                    barExp2.setKitName(vBarcodes2.getKitName());
                    System.out.println("------ Barcodes barExp2   ------------------");
                    System.out.println("Biblioteca antes de asignar barcodes:" + lib);
                    //Asignamos los barcodes 1 (tag1) y barcodes 2 (tag2) 
                    lib.setIdBarcode1(barExp1);
                    lib.setIdBarcode2(barExp2);
                    System.out.println("Biblioteca después de asignar barcodes:" + lib);
                    System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");

                    System.out.println("ibjeto lib: " + lib);
                    listLibrary.add(lib);
                }
            }
             */
            //return listLibrary;
            return getFacade().findLibraryRUn(selectedRun);
        }
        return null;
    }

    //Obtenemos la lista de los reportes de calidad
    public List<QualityReports> qualityReportByRunName() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        List<Run> run = ejbRun.findRun(runNameSelected);
        Run idRn = null;
        for (Run rn : run) {
            idRn = rn;
        }
        return ejbQR.findQRByProject(proj, idRn);
    }

    public List<QualityReports> qualityReportByRunName(String runName) throws IOException {
        List<QualityReports> qrs;
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        List<Run> run = ejbRun.findRun(runName);
        Run idRn = null;
        for (Run rn : run) {
            idRn = rn;
        }
        qrs = ejbQR.findQRByProject(proj, idRn);
        return qrs;
        //context.getExternalContext().redirect("../library/runLibraries.xhtml");
        //String url=qrs.get(0).getUrlQualityReport();
        //context.getExternalContext().redirect(url);
        //String javaScriptText = "window.open('"+url+"', 'popupWindow', 'dependent=yes, menubar=no, toolbar=no');";

        // Add the Javascript to the rendered page's header for immediate execution
        //AddResource addResource = AddResourceFactory.getInstance(context);
        //addResource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, javaScriptText);         
    }

    public String getQualityReportLink0(String runName) throws IOException {
        List<QualityReports> qrs = qualityReportByRunName(runName);
        if (qrs.isEmpty()) {
            return "";
        } else {
            if (qrs.size() > 1) {
                int i = 2;
            }
            //return  "<a target=\"_blank\" href=\"'+qrs.get(0).getUrlQualityReport()+'\" class=\"button\">Ver HTML</a>";
            return qrs.get(0).getUrlQualityReport();
        }
    }

    //Método para limpiar la lista
    public void cleanListSelectionLibrary() {
        LibraryTable.clear();
        System.out.println("Se ejecuta limpieza de select");
    }

    private BigInteger BigInteger(String c_qpcr) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
