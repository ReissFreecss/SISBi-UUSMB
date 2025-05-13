/*
En esta clase se encuentran los metodos referentes las muestras y tienen las siguientes funciones:
-Dar nombre a la muestra segun el usuario responsable al proyecto asociado.
-Crear las muestras
-Editar una o varias muestras.
-Crear las propiedades necesarias para poder crear las bibliotecas asociadas.
-Corroborar que al momento de seleccionar las muestras y redireccionar a la pagina para construir
 las blibliotecas valide lo siguiente:
    *Que las muestras tengan llenos los campos requieridos (tipo, tamaño de lectura, tipo de lectura y plataforma).
    *Que las muestras tengan el mismo tipo de lectura (2x o 1x).
    *Que las muestras tengan el mismo tamaño de lectura.
    *Que las muestras tengan el mismo tipo (RNA o DNA).
    *Que las muestras por lo menos tengan asociado un tipo de plataforma en común.
-Obtener las listas de las muestras según el proyecto asociado o por búsqueda.
-Redireccionar a las diferentes paginas segun la accion requerida
 */
package jsf;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import static com.sun.xml.ws.spi.db.BindingContextFactory.LOGGER;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import jpa.entities.Sample;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.SampleFacade;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletRequest;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.BioinformaticAnalysisSampleLink;

import jpa.entities.Comments;
import jpa.entities.Plataform;

import jpa.entities.Project;
import jpa.entities.UserProjectLink;
import jpa.entities.Users;
import jpa.session.BioinformaticAnalysisFacade;
import jpa.session.BioinformaticAnalysisSampleLinkFacade;
import jpa.session.CommentsFacade;
import jpa.session.PlataformFacade;
import jpa.session.ProjectFacade;
import jpa.session.UserProjectLinkFacade;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("sampleController")
@SessionScoped
public class SampleController implements Serializable {

    private Sample current;
    private DataModel items = null;
    @EJB
    private jpa.session.SampleFacade ejbFacade;
    @EJB
    private CommentsFacade commentFac;
    @EJB
    private ProjectFacade proFac;
    @EJB
    private BioinformaticAnalysisSampleLinkFacade BAFac;
    @EJB
    private PlataformFacade ejbPlataform;

    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Sample selectedSample = new Sample();
    private String unidades;
    private String samStat;
    private String genExtra;
    private String orgName;
    private String refName;
    private String contName;
    private String samVolume;
    private String des; // descripcion
    private String inSize;
    private String readSize;
    private String tube;
    private String expPerfo;
    private String realPerfo;
    private String samName;
    private String samQuality;
    private String samConcent;
    private String labVolume;
    private String labConcent;
    private String comments;
    private Date delDate;
    private String type;
    private String comment;
    private String acept;
    private String plataform;
    private Date recepDate;
    private String delivery;
    private String Abs260_280;
    private String Abs260_230;
    private String folio;
    private String Abs260_280_usmb;
    private String Abs260_230_usmb;
    private String op_service = "0"; //variable que recibe el tipo de servicio
    private Boolean seq = false;

    private String edit;
    private String prevUrL;
    private String profundity;
    private String expPerfoOxford;
    private String apptype; //nuevo atributo
    private String organimstype; // nuevo atributo
    private String kitlib; // nuevo atributo
    private String taglib; //nuevo atributo
    private String Rdeplesion; //nuevo atributo
    private String Jgenextrac = "Si"; //nuevo atributo
    private String Jqanalysis; //nuevo atributo
    private String Jlib; //nuevo atributo
    private String Jseq; //nuevo atributo
    private String Bulib; //nuevo atributo
    private String Doseq; //nuevo atributo

    private BioinformaticAnalysisSampleLink currentBioinformaticAnalysisSampleLink;// Variable current Linabat
    @EJB
    private BioinformaticAnalysisFacade ejbFacadeBioinformaticAnalysis;

    List<Sample> samplesDiferent = new ArrayList<>();
    String messageDialog = "";
    String tableDialog;

    private boolean haveBioAn = true;

    private List<String> analysisCost;

    private Boolean requireInput = false;

    private Boolean requireInputUnidades = false;

    private List<Sample> listSample;

    List<Sample> errorSampleList = new ArrayList<>();

    private List<Sample> samplesRedirect;

    String[] all_stat = {
        "Registrada", //0
        "Recibida", //1
        "Entrega solo extraccion", //2
        "En analisis de calidad", //3
        "Entrega solo analisis de calidad", //4
        "En espera de instrucciones del usuario", //5
        "En construccion de biblioteca", //6
        "Biblioteca entregada", //7
        "Construccion ineficiente", //8
        "En espera de secuenciacion", //9
        "Secuenciandose", //10
        "Secuenciada", //11
        "Basecalling y QC en proceso", //12
        "Basecalling y QC terminado", //13
        "Para resecuenciar", //14
        "Rechazada (Forsake)", //15
        "Entregado fastq", //16
        "En Analisis Bioinformatico", //17
        "Analisis Bioinformatico Entregado"//18
    };
    //posiciones del 0-18
    // Carlos Perez Calderon 30/03/2025

    List<String> lista_status = Arrays.asList(all_stat);
    //fin leslie

    public String getOp_service() {
        return op_service;
    }

    public void setOp_service(String op_service) {
        this.op_service = op_service;
    }

    public String getJgenextrac() {
        return Jgenextrac;
    }

    public void setJgenextrac(String Jgenextrac) {
        this.Jgenextrac = Jgenextrac;
    }

    public String getJqanalysis() {
        return Jqanalysis;
    }

    public void setJqanalysis(String Jqanalysis) {
        this.Jqanalysis = Jqanalysis;
    }

    public String getJlib() {
        return Jlib;
    }

    public void setJlib(String Jlib) {
        this.Jlib = Jlib;
    }

    public String getJseq() {
        return Jseq;
    }

    public void setJseq(String Jseq) {
        this.Jseq = Jseq;
    }

    public String getBulib() {
        return Bulib;
    }

    public void setBulib(String Bulib) {
        this.Bulib = Bulib;
    }

    public String getDoseq() {
        return Doseq;
    }

    public void setDoseq(String Doseq) {
        this.Doseq = Doseq;
    }

    public String getRdeplesion() {
        return Rdeplesion;
    }

    public void setRdeplesion(String Rdeplesion) {
        this.Rdeplesion = Rdeplesion;
    }

    public String getApptype() {
        return apptype;
    }//nuevo atributo

    public void setApptype(String apptype) {
        this.apptype = apptype;
    } //nuevo atributo

    public String getOrganimstype() {
        return organimstype;
    }//nuew

    public void setOrganimstype(String organimstype) {
        this.organimstype = organimstype;
    }//new

    public String getKitlib() {
        return kitlib;
    }//new

    public void setKitlib(String kitlib) {
        this.kitlib = kitlib;
    }//new

    public String getTaglib() {
        return taglib;
    }//new

    public void setTaglib(String taglib) {
        this.taglib = taglib;
    }//new

    public List<Sample> getSamplesRedirect() {
        return samplesRedirect;
    }

    public void setSamplesRedirect(List<Sample> samplesRedirect) {
        this.samplesRedirect = samplesRedirect;
    }

    public List<Sample> getErrorSampleList() {
        return errorSampleList;
    }

    public void setErrorSampleList(List<Sample> errorSampleList) {
        this.errorSampleList = errorSampleList;
    }

    public List<Sample> getListSample() {
        return listSample;
    }

    public void setListSample(List<Sample> listSample) {
        this.listSample = listSample;
    }

    public Boolean getRequireInput() {
        return requireInput;
    }

    public void setRequireInput(Boolean requireInput) {
        this.requireInput = requireInput;
    }

    public List<String> getAnalysisCost() {
        return analysisCost;
    }

    public void setAnalysisCost(List<String> analysisCost) {
        this.analysisCost = analysisCost;
    }

    public boolean isHaveBioAn() {
        return haveBioAn;
    }

    public void setHaveBioAn(boolean haveBioAn) {
        this.haveBioAn = haveBioAn;
    }

    public String getTableDialog() {
        return tableDialog;
    }

    public void setTableDialog(String tableDialog) {
        this.tableDialog = tableDialog;
    }

    public List<Sample> getSamplesDiferent() {
        return samplesDiferent;
    }

    public void setSamplesDiferent(List<Sample> samplesDiferent) {
        this.samplesDiferent = samplesDiferent;
    }

    public String getMessageDialog() {
        return this.messageDialog;
    }

    public void setMessageDialog(String messageDialog) {
        this.messageDialog = messageDialog;
    }

    String PathCreateReports = PathFiles.PathFileSampleReports;
    String separatorPath = PathFiles.separatorPath;
    String FilePath = PathFiles.LinkFileSampleReports;

    public String getProfundity() {
        return profundity;
    }

    public void setProfundity(String profundity) {
        this.profundity = profundity;
    }

    public String getPrevUrL() {
        return prevUrL;
    }

    public void setPrevUrL(String prevUrL) {
        this.prevUrL = prevUrL;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public BioinformaticAnalysisSampleLinkFacade getBAFac() {
        return BAFac;
    }

    public void setBAFac(BioinformaticAnalysisSampleLinkFacade BAFac) {
        this.BAFac = BAFac;
    }

    public Boolean getSeq() {
        return seq;
    }

    public void setSeq(Boolean seq) {
        this.seq = seq;
    }

    public String getAbs260_280_usmb() {
        return Abs260_280_usmb;
    }

    public void setAbs260_280_usmb(String Abs260_280_usmb) {
        this.Abs260_280_usmb = Abs260_280_usmb;
    }

    public String getAbs260_230_usmb() {
        return Abs260_230_usmb;
    }

    public void setAbs260_230_usmb(String Abs260_230_usmb) {
        this.Abs260_230_usmb = Abs260_230_usmb;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    private Boolean user;

    public Boolean getUser() {
        return user;
    }

    public void setUser(Boolean user) {
        this.user = user;
    }

    public String getAbs260_280() {
        return Abs260_280;
    }

    public void setAbs260_280(String Abs260_280) {
        this.Abs260_280 = Abs260_280;
    }

    public String getAbs260_230() {
        return Abs260_230;
    }

    public void setAbs260_230(String Abs260_230) {
        this.Abs260_230 = Abs260_230;
    }

    public Boolean getRequireInputUnidades() {
        return requireInputUnidades;
    }

    public void setRequireInputUnidades(Boolean requireInputUnidades) {
        this.requireInputUnidades = requireInputUnidades;
    }

    private boolean sendMail = false;

    @EJB
    private UserProjectLinkFacade UserProjFac;

    private List<Sample> sampleTable;
    private List<Sample> sampleList;

    @PostConstruct
    public void init() {
        sampleList = new ArrayList<>();
        sampleList = ejbFacade.findAll();

    }

    public boolean isSendMail() {
        return sendMail;
    }

    public void setSendMail(boolean sendMail) {
        this.sendMail = sendMail;
    }

    public String getSamConcent() {
        return samConcent;
    }

    public void setSamConcent(String samConcent) {
        this.samConcent = samConcent;
    }

    public String getLabVolume() {
        return labVolume;
    }

    public void setLabVolume(String labVolume) {
        this.labVolume = labVolume;
    }

    public String getLabConcent() {
        return labConcent;
    }

    public void setLabConcent(String labConcent) {
        this.labConcent = labConcent;
    }

    public List<Sample> getSampleList() {
        return sampleList;
    }

    public void setSampleList(List<Sample> sampleList) {
        this.sampleList = sampleList;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUnidades() {
        return unidades;
    }

    public String getAcept() {
        return acept;
    }

    public void setAcept(String acept) {
        this.acept = acept;
    }

    public String getGenExtra() {
        return genExtra;
    }

    public void setGenExtra(String genExtra) {
        this.genExtra = genExtra;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public String getSamVolume() {
        return samVolume;
    }

    public void setSamVolume(String samVolume) {
        this.samVolume = samVolume;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getInSize() {

        return inSize;
    }

    public void setInSize(String inSize) {
        this.inSize = inSize;
    }

    public String getReadSize() {

        return readSize;
    }

    public void setReadSize(String readSize) {
        this.readSize = readSize;
    }

    public String getTube() {
        return tube;
    }

    public void setTube(String tube) {
        this.tube = tube;
    }

    public String getExpPerfo() {
        return expPerfo;
    }

    public void setExpPerfo(String expPerfo) {
        this.expPerfo = expPerfo;
    }

    public String getRealPerfo() {
        return realPerfo;
    }

    public void setRealPerfo(String realPerfo) {
        this.realPerfo = realPerfo;
    }

    public String getSamName() {
        return samName;
    }

    public void setSamName(String samName) {
        this.samName = samName;
    }

    public String getSamQuality() {
        return samQuality;
    }

    public void setSamQuality(String samQuality) {
        this.samQuality = samQuality;
    }

    public String getSamConcen() {
        return samConcent;
    }

    public void setSamConcen(String samQuantity) {
        this.samConcent = samQuantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getDelDate() {
        return delDate;
    }

    public void setDelDate(Date delDate) {
        this.delDate = delDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getRecepDate() {
        return recepDate;
    }

    public void setRecepDate(Date recepDate) {
        this.recepDate = recepDate;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }

    public String getPlataform() {
        return plataform;
    }

    public void setPlataform(String plataform) {
        this.plataform = plataform;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public static final String NOMBRE_DE_MUESTRA = "";

    public SampleController() {
    }

    public Sample getSelected() {
        if (current == null) {
            current = new Sample();
            selectedItemIndex = -1;
        }
        return current;
    }

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

    public void setSampleTable_test(List<Sample> sampleTable) {
        this.sampleTable = sampleTable;

    }

    public String getSamStat() {
        return samStat;
    }

    public void setSamStat(String samStat) {
        this.samStat = samStat;
    }

    public Sample getSelectedSample() {
        return selectedSample;
    }

    public void setSelectedSample(Sample selectedSample) {
        this.selectedSample = selectedSample;
    }

    private SampleFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {

        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        pagination = new PaginationHelper(100) {

            @Override
            public int getItemsCount() {
                return getFacade().count();
            }

            @Override
            public DataModel createPageDataModel() {
                return new ListDataModel(getFacade().findSampProj(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, proj));
            }
        };

        return pagination;
    }

    public String getExpPerfoOxford() {
        return expPerfoOxford;
    }

    public void setExpPerfoOxford(String expPerfoOxford) {
        this.expPerfoOxford = expPerfoOxford;
    }

    public String prepareList() {

        return "List";
    }

    public String prepareView() {
        current = (Sample) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {

        current = new Sample();
        selectedItemIndex = -1;
        return "/sample/Create";
    }

    public boolean checkUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (us.getUserType().equals("Admin")) {
            user = true;
            return user;
        } else {
            user = false;
            return user;
        }

    }

    public void redirectProject() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (us.getUserType().equals("Usuario")) {
            try {
                context.getExternalContext().redirect("../project/List.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            try {
                context.getExternalContext().redirect("../project/ListAll.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public Date getLastSampleAnalysisDate(List<Sample> samples) {
        //Devuelve cuándo terminó el anàlsis de calidad de la última muestra
        List<Date> dates = new ArrayList<>();
        for (Sample sample : samples) {
            List<Comments> comms = ejbFacade.getCommentsByIdTypeAndTypeSBD(sample.getIdSample().toString(), "Sample");
            for (Comments comm : comms) {
                //Ej Estatus cambia de -En analisis de calidad- a -Rechazada (Forsake)
                if (comm.getComment().startsWith("Estatus cambia de -")) {
                    if (!comm.getComment().endsWith("-En analisis de calidad-")) {
                        //esta es la fecha a elegir
                        dates.add(comm.getCommentDate());
                        break;
                    }
                }

            }
        }
        dates.sort(new DatesComparator());
        if (dates.isEmpty()) {
            return null;
        } else {
            return dates.get(dates.size() - 1);
        }
    }

    public Date getLastSampleFastQcDate(List<Sample> samples) {
        //Devuelve cuándo terminó el anàlsis de calidad de la última muestra
        List<Date> dates = new ArrayList<>();
        for (Sample sample : samples) {
            List<Comments> comms = ejbFacade.getCommentsByIdTypeAndTypeSBD(sample.getIdSample().toString(), "Sample");
            for (Comments comm : comms) {
                if (comm.getComment().startsWith("Estatus cambia de -")) {
                    if (!comm.getComment().endsWith("-Entregado fastq-")) {
                        //esta es la fecha a elegir
                        dates.add(comm.getCommentDate());
                        break;
                    }
                }

            }
        }
        dates.sort(new DatesComparator());
        if (dates.isEmpty()) {
            return null;
        } else {
            return dates.get(dates.size() - 1);
        }
    }

    public boolean checkListSamples(List<Sample> samples) {
        List<Object> propieties = new ArrayList<>();
        FacesContext context = FacesContext.getCurrentInstance();
        samplesDiferent.clear();
        errorSampleList.clear();
        /* System.out.println("Datos");
        System.out.println("Dato: "+samples.get(0).getIdSample());
        System.out.println("Dato: "+samples.get(0).getGeneticExtraction());
        System.out.println("Dato: "+samples.get(0).getAceptation());
        System.out.println("Dato: "+samples.get(0).getSamplePlataform());
        System.out.println("Dato: "+samples.get(0).getOrganismName());
        System.out.println("Dato: "+samples.get(0).getReferenceName());
        System.out.println("Dato: "+samples.get(0).getContaminantName());
        System.out.println("Dato: "+samples.get(0).getSampleVolume());
        System.out.println("Dato: "+samples.get(0).getDescription());
        System.out.println("Dato: "+samples.get(0).getReadSize());
        System.out.println("Dato: "+samples.get(0).getIdTube());
        System.out.println("Dato: "+samples.get(0).getDelivery());
        System.out.println("Dato: "+samples.get(0).getExpectedPerformance());
        System.out.println("Dato: "+samples.get(0).getLabConcen());
        System.out.println("Dato: "+samples.get(0).getLabVolume());
        System.out.println("Dato: "+samples.get(0).getSampleName());
        System.out.println("Dato: "+samples.get(0).getSampleQuality());
        System.out.println("Dato: "+samples.get(0).getSampleQuantity());
        System.out.println("Dato: "+samples.get(0).getStatus());
        System.out.println("Dato: "+samples.get(0).getDeliveryDate());
        System.out.println("Dato: "+samples.get(0).getType());
        System.out.println("Dato: "+samples.get(0).getReceptionDate());
        System.out.println("Dato: "+samples.get(0).getAbs260_280());
        System.out.println("Dato: "+samples.get(0).getAbs260_230());
        System.out.println("Dato: "+samples.get(0).getFolio());
        System.out.println("Dato: "+samples.get(0).getAbs260_280_usmb());
        System.out.println("Dato: "+samples.get(0).getAbs260_230_usmb());
        System.out.println("Dato: "+samples.get(0).getExpectedPerformanceOxford());*/

        try {
            /*
            String platform = samples.get(0).getSamplePlataform();
            String platformComp = samples.get(0).getSamplePlataform();
            for (int i = 0; i <= samples.size() - 1; i++) {
                samplesDiferent.add(samples.get(i));
                if (platformComp.length() <= samples.get(i).getSamplePlataform().length()) {
                    platformComp = samples.get(i).getSamplePlataform();
                }

                if (platform.length() >= samples.get(i).getSamplePlataform().length()) {
                    platform = samples.get(i).getSamplePlataform();
                }

            }
             */

            samplesDiferent.clear();
            samplesDiferent = sampleTable;
            tableDialog = "";
            //iteración para validar campos vacíos
            for (Sample itemSample : sampleTable) {
                //Validamos que la muestra contenga plataforma
                if (itemSample.getSamplePlataform() == null || itemSample.getSamplePlataform().equals("")) {
                    messageDialog = "La muestra: " + itemSample.getIdSample() + " : " + itemSample.getSampleName() + " no tiene asignada una plataforma, favor de asignar una.";
                    return false;
                }
                //Validammos que la muestra contenga tamaño de lectura
                if (itemSample.getReadSize() == null || itemSample.getReadSize().equals("")) {
                    messageDialog = "La muestra: " + itemSample.getIdSample() + " : " + itemSample.getSampleName() + " no contiene un tamaño de lectura, favor de llenar el campo.";
                    return false;
                }
            }

            for (int i = 0; i < samples.size(); i++) {
                //Validamos que las plataformas sean iguales
                if (!(samples.get(0).getSamplePlataform().equals(samples.get(i).getSamplePlataform()))) {
                    //errorSampleList.add(samples.get((i-1)));
                    errorSampleList.add(samples.get(i));
                    errorSampleList.add(samples.get((i == 0) ? (i + 1) : (i - 1)));
                    messageDialog = "Existen plataformas diferentes";
                    tableDialog = "Plataform";
                    return false;

                }
                //validamos que el tamaño de lectura sean iguales
                if (!(samples.get(0).getReadSize().equals(samples.get(i).getReadSize()))) {
                    //errorSampleList.add(samples.get((i-1)));
                    errorSampleList.add(samples.get(i));
                    errorSampleList.add(samples.get((i == 0) ? (i + 1) : (i - 1)));
                    messageDialog = "Existen tamaños de lectura diferentes";
                    tableDialog = "Size";
                    return false;

                }
                //Validamos que el tipo de muestra sean iguales
                if (!(samples.get(0).getType().equals(samples.get(i).getType()))) {
                    errorSampleList.add(samples.get(i));
                    errorSampleList.add(samples.get((i == 0) ? (i + 1) : (i - 1)));
                    tableDialog = "Type";
                    messageDialog = "Existen tipos de muestras diferentes";
                    return false;

                }

            }
            for (Sample sam : samples) {

                if (sam.getStatus().equals("Rechazada (Forsake)") || sam.getStatus().equals("Calidad rechazada") || sam.getAceptation().equals("") || sam.getAceptation().equals("Rechazada")) {
                    //tableDialog = "Type";
                    messageDialog = "Existen muestras con estado 'Rechazada (Forsake)' - 'Calidad rechazada' / Aceptacion  'Rechazada' o sin Aceptacion ";
                    return false;
                }

            }

            //Codigo para separar el tamaño de lectura, ahora innecesario porque la biblioteca ya no tiene esos campos
            /*
            String formated = samples.get(0).getReadSize().toLowerCase();
            String[] typeAndCycles = formated.split("x");
            String types = typeAndCycles[0];
            String cycles = typeAndCycles[1];
            propieties.add(platform);
            propieties.add(types);
            propieties.add(cycles);
            if (samples.get(0).getInsertSize() == null) {
                propieties.add(0 + "");
            } else {
                propieties.add(samples.get(0).getInsertSize() + "");
            }
            propieties.add(samples.size());

            context.getExternalContext().getSessionMap().put("libraryProperties", propieties);
            List<Object> listaPropiedades = (List<Object>) context.getExternalContext().getSessionMap().get("libraryProperties");

            for (Object propiedad : listaPropiedades) {
                System.out.println("--->>" + propiedad);
            }
             */
            return true;

        } catch (NullPointerException ex) {
            //System.out.println("Se valida con el error: " + ex.getMessage());
            messageDialog = "Ups, al parecer no has llenado algunos campos, para crear bibliotecas necesitas aceptacion,estatus,rendimiento y tamaño de lectura y plataforma";
            //messageDialog = "Ups, error inesperado, por favor contacte al mantenimiento:  Código de error LCCLS1";
            return false;

        }
    }

    public void redirectLibrary() {
        RequestContext dialog = RequestContext.getCurrentInstance();
        FacesContext context = FacesContext.getCurrentInstance();
        samplesRedirect = (List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");
        //boolean cierto=checkListSamples(samples);
        //System.out.println("EL VALOR ES: "+checkListSamples(samples));
        if (samplesRedirect.isEmpty() == true) {
            setMessageDialog("¡Para la construcción de bibliotecas, debe de seleccionar muestras!");
            dialog.execute("PF('samplesEmptyError').show();");
            return;
        }

        //boolean cierto=checkListSamples(samples);
        if (checkListSamples(samplesRedirect)) {

            System.out.println("El valor es true");
            try {
                context.getExternalContext().redirect("../library/CreateL.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("El valor es false");
            //setMessageDialog("Para la construcción de bibliotecas, debe de seleccionar muestras!");
            dialog.execute("PF('biblioErrDialog').show();");

        }

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public CellStyle colorCell(XSSFWorkbook workbook) {

        CellStyle styleUser = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();

        font.setBold(true);

        styleUser.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styleUser.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleUser.setBorderBottom(BorderStyle.THICK);
        styleUser.setAlignment(HorizontalAlignment.CENTER);
        styleUser.setBorderTop(BorderStyle.THICK);
        styleUser.setFont(font);

        return styleUser;
    }

    public CellStyle EstiloSubtitulos(XSSFWorkbook workbook) {

        CellStyle styleSub = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();

        font.setBold(true);

        styleSub.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styleSub.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleSub.setBorderBottom(BorderStyle.THICK);
        styleSub.setBorderLeft(BorderStyle.THICK);
        styleSub.setAlignment(HorizontalAlignment.CENTER);
        styleSub.setFont(font);

        return styleSub;
    }

    public CellStyle EstiloEncabezado(XSSFWorkbook workbook) {

        CellStyle styleEnc = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();

        font.setBold(true);

        styleEnc.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        styleEnc.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleEnc.setBorderBottom(BorderStyle.THICK);
        styleEnc.setBorderTop(BorderStyle.THICK);
        styleEnc.setFont(font);

        return styleEnc;
    }

    public CellStyle EstiloSubEncabezado(XSSFWorkbook workbook) {

        CellStyle styleSubEnc = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();

        font.setBold(true);

        styleSubEnc.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        styleSubEnc.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleSubEnc.setBorderBottom(BorderStyle.THICK);
        styleSubEnc.setBorderLeft(BorderStyle.THICK);
        styleSubEnc.setAlignment(HorizontalAlignment.CENTER);
        styleSubEnc.setFont(font);

        return styleSubEnc;
    }

    public CellStyle EstiloCeldas(XSSFWorkbook workbook) {

        CellStyle styleCeldas = workbook.createCellStyle();

        styleCeldas.setBorderBottom(BorderStyle.THIN);
        styleCeldas.setBorderLeft(BorderStyle.THIN);
        styleCeldas.setBorderRight(BorderStyle.THIN);
        styleCeldas.setBorderTop(BorderStyle.THIN);
        styleCeldas.setAlignment(HorizontalAlignment.LEFT);

        return styleCeldas;
    }

    public CellStyle EstiloLibreriaEnc(XSSFWorkbook workbook) {

        CellStyle styleLibreriasEnc = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        styleLibreriasEnc.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleLibreriasEnc.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleLibreriasEnc.setBorderBottom(BorderStyle.THICK);
        styleLibreriasEnc.setBorderTop(BorderStyle.THICK);
        styleLibreriasEnc.setAlignment(HorizontalAlignment.LEFT);
        styleLibreriasEnc.setFont(font);

        return styleLibreriasEnc;
    }

    public CellStyle EstiloCerrado(XSSFWorkbook workbook) {

        CellStyle styleClose = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        styleClose.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleClose.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleClose.setBorderBottom(BorderStyle.THICK);
        styleClose.setBorderTop(BorderStyle.THICK);
        styleClose.setBorderLeft(BorderStyle.THICK);
        styleClose.setBorderRight(BorderStyle.THICK);
        styleClose.setAlignment(HorizontalAlignment.CENTER);
        styleClose.setFont(font);

        return styleClose;
    }

    public XSSFSheet sizeCell(XSSFWorkbook book, String SheetName) {
        XSSFSheet sheet = book.createSheet(SheetName);

        sheet.setColumnWidth(0, 500);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 7000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 7000);
        sheet.setColumnWidth(10, 4000);
        sheet.setColumnWidth(11, 4000);
        sheet.setColumnWidth(12, 4000);
        sheet.setColumnWidth(13, 5000);
        sheet.setColumnWidth(14, 5000);
        sheet.setColumnWidth(15, 5000);
        sheet.setColumnWidth(16, 5000);
        sheet.setColumnWidth(17, 8000);

        return sheet;
    }

    public XSSFSheet version(XSSFWorkbook book, String SheetName) {
        XSSFSheet sheet = book.getSheet(SheetName);

        XSSFRow row2 = sheet.createRow(0);

        XSSFCell version = row2.createCell(7);

        row2.setHeight((short) 700);

        version.setCellValue("Codigo: F01_PT01\nversion:01");

        return sheet;
    }

    /*
    Metodo:
     */
    public void createExcel() {

        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Sample> samples = (List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");

        String url = "jdbc:postgresql://localhost:5432/sisbi_db";
        String usuario = "sisbi";
        String pswd = "SISBI123@";
        try {

            if (samples.isEmpty()) {
                System.out.println("no han seleccionado ninguna muestra");

                context.addMessage(null, new FacesMessage("¡ERROR!", "No han seleccionado ninguna muestra para Generar el reporte DNA"));
            } else {

                Class.forName("org.postgresql.Driver");
                Connection conexion = DriverManager.getConnection(url, usuario, pswd);
                java.sql.Statement st = conexion.createStatement();
                String folio = "Select max(id_folio) from folio";
                ResultSet rs = st.executeQuery(folio);

                Timestamp time = new Timestamp(System.currentTimeMillis());
                String[] titulos = {"", "Usuario", ""};//nombre de las columnas
                String[] subtitulos = {"ID de la \nmuestra", "ID del" + "\n" + "proyecto", "Etiqueta del" + "\n" + "tubo"};
                String[] encabezado = {"", "", "", "Análisis de calidad", "", "", "", "", ""};
                String[] subencabezado = {"Conc." + "\n" + "(ng/μl)", "vol.(μl)", "A" + "\n" + "260/280", "A" + "\n" + "260/230", "Fluorometro" + "\n" + "(Q/D)", "DNA total", "Archivo" + "\n" + "gel 0.5%", "Fecha de \n realización", "Observaciones"};

                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet pagina = sizeCell(workbook, "DNA");

                String Nombre = us.getFirstName().substring(0, 1);
                String apellido_p = us.getPLastName().substring(0, 1);
                String apellido_m = us.getMLastName().substring(0, 1);
                String iniciales = Nombre + apellido_p + apellido_m;
                System.out.println(iniciales);

                ////////////////////////////////////////////////////14/01/2020
                XSSFRow Row = pagina.createRow(4);
                int serial = 0;
                while (rs.next()) {

                    int fol = rs.getInt(1);
                    System.out.println("folio anterior:" + fol);
                    serial = fol + 1;
                    String[] info = {"No. Folio: " + serial, "Realizó:", "Equipos utilizados:", "lote de reactivos"};

                    for (int t = 0; t < info.length; t++) {

                        Row = pagina.createRow(t + 3);

                        for (int x = 0; x < 1; x++) {

                            XSSFCell cell = Row.createCell(2);
                            cell.setCellValue(info[t]);
                        }
                    }

                }

                ////////////////////////////////////////////////////////////
                String nombreFichero = PathCreateReports + "reportesDNA" + separatorPath + "F01_PT06_" + pj.getIdProject().replaceAll("[:.-]", "_") + "_" + time.toString().replaceAll("[:.\\ ]", "_") + "_" + iniciales + "_" + serial + ".xls";//directorio donde se crea el archivo;
                File file = new File(nombreFichero);

                CellStyle style6 = workbook.createCellStyle();
                CellStyle estilo = workbook.createCellStyle();
                XSSFFont font = workbook.createFont();

                font.setBold(true);
                style6.setBorderLeft(BorderStyle.THICK);
                estilo.setAlignment(HorizontalAlignment.CENTER);
                estilo.setFont(font);

                XSSFRow row2 = pagina.createRow(1);
                XSSFCell titulo = row2.createCell(3);
                titulo.setCellStyle(estilo);
                titulo.setCellValue("Analisis de calidad de muestras DNA");

                XSSFRow row = pagina.createRow(9);//crea una fila en la pocision 10 del archivo

                ///creamos el titulo de la celda 1 a la 3 del archivo con el nombre Usuario
                for (int t = 0; t < titulos.length; t++) {
                    XSSFCell cell = row.createCell(t + 1);

                    cell.setCellStyle(colorCell(workbook));
                    cell.setCellValue(titulos[t]);
                    row.createCell(0).setCellStyle(EstiloCeldas(workbook));
                    row.createCell(13).setCellStyle(style6);

                    //creamos el titulo de la celda 4 a la 10 del archivo con el nombre Analisis de calidad
                    for (int x = 0; x < encabezado.length; x++) {
                        XSSFCell cell2 = row.createCell(x + 4);
                        cell2.setCellStyle(EstiloEncabezado(workbook));
                        cell2.setCellValue(encabezado[x]);

                    }

                }

                row = pagina.createRow(10);//se crea una fila en la posicion 11 del archivo
                //creamos los nombres de las columnas B,C,D como: consecutivo,Nombre del proyecto,Nombre de la muestra
                for (int i = 0; i < subtitulos.length; i++) {

                    XSSFCell cell = row.createCell(i + 1);
                    cell.setCellStyle(EstiloSubtitulos(workbook));
                    row.setHeight((short) 1000);
                    cell.setCellValue(subtitulos[i]);

                    //creamos los nombres de las columnas de la E a la K.
                    for (int s = 0; s < subencabezado.length; s++) {

                        XSSFCell cel = row.createCell(s + 4);
                        cel.setCellStyle(EstiloSubEncabezado(workbook));
                        row.createCell(13).setCellStyle(style6);
                        cel.setCellValue(subencabezado[s]);

                    }

                }
                for (int i = 0; i < samples.size(); i++) {

                    row = pagina.createRow(i + 11);

                    String lib = samples.get(i).getIdTube();
                    int id = samples.get(i).getIdSample();
                    String proj = pj.getIdProject();
                    ////////////////////////////////
                    for (int x = 0; x < 1; x++) {

                        System.out.println("valor de lib:" + lib);
                        XSSFCell id_muestra = row.createCell(1);
                        XSSFCell project = row.createCell(2);
                        XSSFCell cell = row.createCell(3);

                        cell.setCellStyle(EstiloCeldas(workbook));
                        cell.setCellValue(lib);

                        id_muestra.setCellStyle(EstiloCeldas(workbook));
                        id_muestra.setCellValue(id);

                        project.setCellStyle(EstiloCeldas(workbook));
                        project.setCellValue(proj);

                        row.createCell(4).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(5).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(6).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(7).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(8).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(9).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(10).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(11).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(12).setCellStyle(EstiloCeldas(workbook));

                    }//agrega los valores al archivo de excel 

                }
                /////////////////////////// agrega el numero de folio a las muestras seleccionadas automaticamente cuando se descarga el reporte/////////////////

                for (Sample sample : sampleTable) {
                    String statusAnt;
                    statusAnt = sample.getStatus();

                    //Se agrega validación para cambiar estado de muestra y agregar a la tabla de comentarios
                    if (!statusAnt.equals("En analisis de calidad")) {
                        sample.setFolio(Integer.toString(serial));
                        sample.setStatus("En analisis de calidad");
                        getFacade().edit(sample);

                        Comments commentsSample = new Comments();
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En analisis de calidad" + "- por " + us.getUserName());
                        commentsSample.setIdType(sample.getIdSample() + "");
                        commentsSample.setType("Sample");
                        commentsSample.setUserName("SISBI");
                        commentsSample.setCommentDate(timestamp);

                        commentFac.createComment(commentsSample);

                    }

                }

                ////////////////////////////////////////////////////////////////////////////  
                FileOutputStream fileOut = new FileOutputStream(file);

                workbook.write(fileOut);//
                fileOut.flush();
                fileOut.close();// Cerramos el libro para concluir operaciones
                st.executeUpdate("insert into folio values(default)");
                st.close();
                conexion.close();

                String nombre = file.getName();
                String pathDownload = FilePath + "reportesDNA/" + nombre;
                System.out.println("nombre del archivo: " + nombre);
                context.getExternalContext().redirect(pathDownload);

                LOGGER.log(Level.INFO, "Archivo creado existosamente en {0}", file.getAbsolutePath());
            }
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de entrada/salida");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int c = 0; c < samples.size(); c++) {
            System.out.println(samples.get(c).getType());
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void createExcelRNA() throws ClassNotFoundException, SQLException {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Sample> samples = (List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");

        String url = "jdbc:postgresql://localhost:5432/sisbi_db";
        String usuario = "sisbi";
        String pswd = "SISBI123@";
        try {
            if (samples.isEmpty()) {
                System.out.println("no han seleccionado ninguna muestra");

                context.addMessage(null, new FacesMessage("¡ERROR!", "No han seleccionado ninguna muestra para Generar el reporte RNA"));
            } else {

                Class.forName("org.postgresql.Driver");
                Connection conexion = DriverManager.getConnection(url, usuario, pswd);
                java.sql.Statement st = conexion.createStatement();
                String folio = "Select max(id_folio) from folio";
                ResultSet rs = st.executeQuery(folio);

                Timestamp time = new Timestamp(System.currentTimeMillis());

                String[] titulos = {"", "Usuario", ""};//nombre de las columnas
                String[] subtitulos = {"ID de la \nmuestra", "ID del" + "\n" + "proyecto", "Etiqueta del" + "\n" + "tubo"};
                String[] encabezado = {"", "", "", "Análisis de calidad", "", "", "", ""};
                String[] subencabezado = {"Conc." + "\n" + "(ng/μl)", "vol.(μl)", "RNA total", "Valor RIN", "Archivo" + "\n" + "Bioanalizador", "Archivo gel de agarosa\n2 %", "Fecha de \n realización", "Observaciones"};

                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet pagina = sizeCell(workbook, "RNA");

                String Nombre = us.getFirstName().substring(0, 1);
                String apellido_p = us.getPLastName().substring(0, 1);
                String apellido_m = us.getMLastName().substring(0, 1);
                String iniciales = Nombre + apellido_p + apellido_m;
                System.out.println(iniciales);

                ////////////////////////////////////////////////////14/01/2020
                XSSFRow Row = pagina.createRow(4);
                int serial = 0;
                while (rs.next()) {

                    int fol = rs.getInt(1);
                    System.out.println("folio anterior:" + fol);
                    serial = fol + 1;
                    String[] info = {"No. Folio: " + serial, "Realizó:", "Equipos utilizados:", "lote de reactivos"};

                    for (int t = 0; t < info.length; t++) {

                        Row = pagina.createRow(t + 3);

                        for (int x = 0; x < 1; x++) {

                            XSSFCell cell = Row.createCell(2);
                            cell.setCellValue(info[t]);
                        }
                    }

                }

                ////////////////////////////////////////////////////////////
                String nombreFichero = PathCreateReports + "reportesRNA" + separatorPath + "F01_PT06_" + pj.getIdProject().replaceAll("[:.-]", "_") + "_" + time.toString().replaceAll("[:.\\ ]", "_") + "_" + iniciales + "_" + serial + ".xls";//directorio donde se crea el archivo;
                File file = new File(nombreFichero);

                CellStyle style6 = workbook.createCellStyle();
                CellStyle estilo = workbook.createCellStyle();
                XSSFFont font = workbook.createFont();

                font.setBold(true);

                style6.setBorderLeft(BorderStyle.THICK);

                estilo.setAlignment(HorizontalAlignment.CENTER);
                estilo.setFont(font);

                XSSFRow row2 = pagina.createRow(1);
                XSSFCell titulo = row2.createCell(3);
                titulo.setCellStyle(estilo);
                titulo.setCellValue("Analisis de calidad de muestras RNA");

                XSSFRow row = pagina.createRow(9);//crea una fila en la pocision 10 del archivo
                ///creamos el titulo de la celda 1 a la 3 del archivo con el nombre Usuario

                for (int t = 0; t < titulos.length; t++) {
                    XSSFCell cell = row.createCell(t + 1);

                    cell.setCellStyle(colorCell(workbook));
                    cell.setCellValue(titulos[t]);
                    row.createCell(0).setCellStyle(EstiloCeldas(workbook));
                    row.createCell(12).setCellStyle(style6);

                    //creamos el titulo de la celda 4 a la 10 del archivo con el nombre Analisis de calidad
                    for (int x = 0; x < encabezado.length; x++) {
                        XSSFCell cell2 = row.createCell(x + 4);
                        cell2.setCellStyle(EstiloEncabezado(workbook));
                        cell2.setCellValue(encabezado[x]);

                    }

                }

                row = pagina.createRow(10);//se crea una fila en la posicion 11 del archivo
                //creamos los nombres de las columnas B,C,D como: consecutivo,Nombre del proyecto,Nombre de la muestra
                for (int i = 0; i < subtitulos.length; i++) {

                    XSSFCell cell = row.createCell(i + 1);
                    cell.setCellStyle(EstiloSubtitulos(workbook));
                    row.setHeight((short) 1000);
                    cell.setCellValue(subtitulos[i]);
                }
                for (int s = 0; s < subencabezado.length; s++) {

                    XSSFCell cel = row.createCell(s + 4);
                    cel.setCellStyle(EstiloSubEncabezado(workbook));
                    cel.setCellValue(subencabezado[s]);
                    row.createCell(12).setCellStyle(style6);
                }
                for (int i = 0; i < samples.size(); i++) {

                    row = pagina.createRow(i + 11);

                    String lib = samples.get(i).getIdTube();
                    int id = samples.get(i).getIdSample();
                    String proj = pj.getIdProject();
                    ////////////////////////////////
                    for (int x = 0; x < 1; x++) {

                        System.out.println("valor de lib:" + lib);
                        XSSFCell id_muestra = row.createCell(1);
                        XSSFCell project = row.createCell(2);
                        XSSFCell cell = row.createCell(3);

                        cell.setCellStyle(EstiloCeldas(workbook));
                        cell.setCellValue(lib);

                        id_muestra.setCellStyle(EstiloCeldas(workbook));
                        id_muestra.setCellValue(id);

                        project.setCellStyle(EstiloCeldas(workbook));
                        project.setCellValue(proj);

                        row.createCell(4).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(5).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(6).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(7).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(8).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(9).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(10).setCellStyle(EstiloCeldas(workbook));
                        row.createCell(11).setCellStyle(EstiloCeldas(workbook));

                    }//agrega los valores al archivo de excel

                }

                /////////////////////////// agrega el numero de folio a las muestras seleccionadas automaticamente cuando se descarga el reporte/////////////////
                for (Sample sample : sampleTable) {
                    String statusAnt;
                    statusAnt = sample.getStatus();

                    //Se agrega validación para cambiar estado de muestra y agregar a la tabla de comentarios
                    if (!statusAnt.equals("En analisis de calidad")) {
                        sample.setFolio(Integer.toString(serial));
                        sample.setStatus("En analisis de calidad");
                        getFacade().edit(sample);

                        Comments commentsSample = new Comments();
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En analisis de calidad" + "- por " + us.getUserName());
                        commentsSample.setIdType(sample.getIdSample() + "");
                        commentsSample.setType("Sample");
                        commentsSample.setUserName("SISBI");
                        commentsSample.setCommentDate(timestamp);

                        commentFac.createComment(commentsSample);
                    }

                }

                ////////////////////////////////////////////////////////////////////////////
                FileOutputStream fileOut = new FileOutputStream(file);

                workbook.write(fileOut);//
                fileOut.flush();
                fileOut.close();// Cerramos el libro para concluir operaciones
                st.executeUpdate("insert into folio values(default)");
                st.close();
                conexion.close();

                String nombre = file.getName();
                String pathDownload = FilePath + "reportesRNA/" + nombre;
                System.out.println("nombre del archivo: " + nombre);
                context.getExternalContext().redirect(pathDownload);

                LOGGER.log(Level.INFO, "Archivo creado existosamente en {0}", file.getAbsolutePath());
            }

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de entrada/salida");
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void Reports(FileUploadEvent event) throws FileNotFoundException, IOException, InvalidFormatException {
        DataInputStream entrada = new DataInputStream(event.getFile().getInputstream());
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext Context = RequestContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        //List<Sample> samples = ejbFacade.findSamples(pj.getIdProject());

        XSSFWorkbook workbook = new XSSFWorkbook(entrada);
        XSSFSheet sheet = workbook.getSheetAt(0);

        int countRow = 0;
        int countFila = 0;

        String typeFile = null;
        try {

            System.out.println("primer for");
            for (Row fila : sheet) {
                countFila++;
                System.out.println("primer if");
                if (countFila >= 1) {
                    System.out.println("entra al primer if");
                    List<String> files = new ArrayList<>();
                    for (int cn = 0; cn < fila.getLastCellNum(); cn++) {
                        Cell cell = fila.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case STRING:
                                files.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                files.add("" + (double) cell.getNumericCellValue());
                                break;
                            case BLANK:
                                files.add("-------");
                                break;
                        }

                    }

                    typeFile = files.get(3);
                    System.out.println(typeFile);
                    break;
                }

            }

            for (Row row : sheet) {
                countRow++;

                if (countRow >= 8) {
                    List<String> parameters = new ArrayList<>();
                    for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                        Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case STRING:
                                parameters.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                parameters.add("" + (double) cell.getNumericCellValue());
                                break;
                            case BLANK:
                                parameters.add(null);
                                break;
                        }
                    }
                    // System.out.println("id_de la muestra: "+parameters.get(1).replaceAll("\\.0", ""));
                    List<Sample> samples = ejbFacade.findSamplebyReports(Integer.parseInt(parameters.get(1).replaceAll("\\.0", "").replaceAll("ID de la \n" + "muestra", "0")));

                    for (Sample sample : samples) {

                        // if(sample.getIdSample()==(Integer.parseInt(parameters.get(1).replaceAll("\\.0", "")))){
                        if (typeFile.equals("Analisis de calidad de muestras DNA")) {
                            //if(sample.getAceptation()!="Rechazada"){
                            //String statusAnt;
                            //statusAnt = sample.getStatus();
                            sample.setLabConcen(Double.parseDouble(parameters.get(4)));
                            sample.setLabVolume(Double.parseDouble(parameters.get(5)));
                            sample.setAbs260_280_usmb(parameters.get(6));
                            sample.setAbs260_230_usmb(parameters.get(7));
                            // sample.setStatus("En analisis de calidad");
                            ejbFacade.edit(sample);

                            Comments commentsSample = new Comments();
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            commentsSample.setComment("Subida del reporte de calidad llenado por " + us.getUserName());
                            commentsSample.setIdType(sample.getIdSample() + "");
                            commentsSample.setType("Sample");
                            commentsSample.setUserName("SISBI");
                            commentsSample.setCommentDate(timestamp);

                            commentFac.createComment(commentsSample);
                            //}*/
                        } else {
                            // if(sample.getAceptation()!="Rechazada"){
                            //String statusAnt;
                            //statusAnt = sample.getStatus();

                            sample.setLabConcen(Double.parseDouble(parameters.get(4)));
                            sample.setLabVolume(Double.parseDouble(parameters.get(5)));
                            //sample.setStatus("En analisis de calidad");
                            getFacade().edit(sample);

                            Comments commentsSample = new Comments();
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            commentsSample.setComment("Subida del reporte de calidad llenado por " + us.getUserName());
                            commentsSample.setIdType(sample.getIdSample() + "");
                            commentsSample.setType("Sample");
                            commentsSample.setUserName("SISBI");
                            commentsSample.setCommentDate(timestamp);

                            commentFac.createComment(commentsSample);//*/
                            //}
                        }
                        //}else{
                        //   System.out.println("no modifica nada");
                        //}

                    }
                }
                entrada.close();

                Context.execute("PF('updateSamples').show();");
                // System.out.println("Muestras modificadas");
            }
        } catch (Exception e) {
            System.out.println(e);

        }

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////    

    public void create() {
        try {

            FacesContext context = FacesContext.getCurrentInstance();
            Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String[] tiempo = timestamp.toString().split(":");
            String[] tiempo2 = tiempo[0].split("-");
            String[] dia = tiempo2[2].split(" ");
            String año = tiempo2[0].substring(2, 4);
            String fecha = año + tiempo2[1] + dia[0];

            String[] formated = pj.getIdProject().split("_");
            System.out.println(formated[0]);
            String manager = us.getUserName();
            System.out.println(manager);
            current.setIdProject(pj);
            current.setSampleQuality("Aun no determinada");
            current.setStatus("Registrada");
            String expected = current.getExpectedPerformance();

            if (expected != null && expected.length() > 0) {
                current.setExpectedPerformance(current.getExpectedPerformance() + " " + unidades);

                //Se realiza validación cuando el rendimiento contenga información  ----------------------------------------------- INICIO
                //Hacemos las operaciones correspondientes sobre el rendimiento de illumina
                System.out.println("Se imprime el rendimiento: " + current.getExpectedPerformance().replaceAll(" ", ""));
                String itemFiltroRendimiento = current.getExpectedPerformance().replaceAll(" ", "");
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
                    current.setExpectedPerformance(itemDoubleRendimiento + "");
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
                        current.setExpectedPerformance(itemIntegerRendimiento + "");
                    } else {
                        //Si no solo es 0, entonces se lo asignamos tal cual
                        current.setExpectedPerformance(itemDoubleRendimiento + "");
                    }

                }
                //Se realiza validación cuando el rendimiento contenga información  ----------------------------------------------- FINAL
            }

            current.setSampleName(manager + "_" + fecha + "_" + current.getSampleName());

            current.setOrganismName(orgName);
            current.setReferenceName(refName);
            current.setContaminantName(contName);

            getFacade().create(current);

            try {

                Comments com = new Comments();

                com.setComment("Esta muestra fue dada de alta por el usuario: " + us.getUserName());
                com.setIdType(current.getIdSample() + "");
                com.setType("Sample");
                com.setUserName("SISBI");
                com.setCommentDate(timestamp);

                commentFac.createComment(com);

            } catch (Exception e) {
                System.out.println("algo paso" + e);

            }

            EmailController ec = new EmailController();
            ec.sendManagerSamEmail(us, pj, 1);
            //ec.sendUserSamEmail(us, pj, 1);

            //JsfUtil.addSuccessMessage("Muestra " + current.getSampleName() + " Registrada");
            //context.addMessage("growl_success", new FacesMessage("Registro exitoso", "Muestra " + current.getSampleName() + " Registrada"));
            FacesContext.getCurrentInstance().addMessage("msgSuccess", new FacesMessage("Registro exitoso", "Muestra " + current.getSampleName() + " Registrada"));

            current.setSampleName("");
            current.setIdTube("");
            current.setExpectedPerformance(expected);

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sample", current);

            pj.setStatus("En proceso");
            proFac.edit(pj);

            current = null;

            //Mensaje sobre el recibimiento de muestras
            RequestContext rcontext = RequestContext.getCurrentInstance();
            rcontext.execute("PF('dlgInfo').show();");

            requireInputUnidades = false;

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            e.printStackTrace();
        }
    }

    public void prepareEdit() {

        current = (Sample) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (us.getUserType().equals("Admin")) {
            try {
                context.getExternalContext().redirect("../sample/AdminSampleEdit.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                context.getExternalContext().redirect("../sample/Edit.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void clearVars() {
        samStat = null;
        genExtra = null;
        orgName = null;
        refName = null;
        contName = null;
        samVolume = null;
        des = null;
        inSize = null;
        readSize = null;
        tube = null;
        expPerfo = null;
        expPerfoOxford = null;
        realPerfo = null;
        samName = null;
        samQuality = null;
        samConcent = null;
        comments = null;
        delDate = null;
        type = null;
        recepDate = null;
        comment = null;
        acept = null;
        sendMail = true;
        labVolume = null;
        labConcent = null;
        plataform = null;
        delivery = null;
        sampleTable.clear();
    }

    // Metodo para actualizar muestras y generar los cambios de status
    public void updateManySamples() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

        String statusAnt = "";
        boolean statusChange = false;
        boolean qualityChange = false;
        int iteracion = 0;
        String strMessage = "";
        
        List<Sample> samplesParaCorreo = new ArrayList<>(sampleTable);

        for (Sample sample : sampleTable) {
            iteracion++;
            if (samName != null && !samName.isEmpty()) {

                String[] name = sample.getSampleName().split("_");

                // ——— Renombrado de muestra si aplica ———
                if (samName != null && !samName.isEmpty()) {
                    String[] nameParts = sample.getSampleName().split("_");
                    String fecha = new SimpleDateFormat("yyMMdd").format(new Date());
                    String manager = pj.getIdProject().split("_")[1];
                    sample.setSampleName(manager + "_" + fecha + "_" + samName);
                }
            }
            if (tube != null && !tube.isEmpty()) {
                sample.setIdTube(tube);
            }

            if (acept != null && !acept.equals("---")) {
                sample.setAceptation(acept);
                if (!acept.equals("Rechazada")) {
                    //cuando la aceptacion es distinta de Rechazada 
                    System.out.println("Cambio la aceptacion de la muestra a: " + acept);
                    statusAnt = sample.getStatus();
                    sample.setStatus("En construccion de biblioteca");

                    // siempre guarda el comentario de la aceptación
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                    commentsSample.setComment("La aceptación de la muestra cambió a -" + acept + "- por " + us.getUserName());
                    commentsSample.setIdType(sample.getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);
                    commentFac.createComment(commentsSample);
                }
                // Carlos 15-04-25 Nuevo comentario adicional por cambio de estatus a la tabla de comentarios
                Comments statusComment = new Comments();
                Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
                Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                statusComment.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En construccion de biblioteca" + "- por " + us.getUserName());
                statusComment.setIdType(sample.getIdSample() + "");
                statusComment.setType("Sample");
                statusComment.setUserName("SISBI");
                statusComment.setCommentDate(timestamp2);
                commentFac.createComment(statusComment);
            }

            if (genExtra != null && !genExtra.isEmpty()) {
                sample.setGeneticExtraction(genExtra);
            }
            if (Bulib != null && !Bulib.isEmpty()) {
                sample.setBuild_lib(Bulib);
            }
            if (Doseq != null && !Doseq.isEmpty()) {
                sample.setDo_seq(Doseq);
            }
            if (labVolume != null && !labVolume.isEmpty()) {
                sample.setLabVolume(Double.parseDouble(labVolume));
            }
            if (samVolume != null && !samVolume.isEmpty()) {
                sample.setSampleVolume(samVolume);
            }
            if (Abs260_280 != null && !Abs260_280.isEmpty()) {
                sample.setAbs260_280(Abs260_280);
            }
            if (Abs260_230 != null && !Abs260_230.isEmpty()) {
                sample.setAbs260_230(Abs260_230);
            }
            if (folio != null && !folio.isEmpty()) {
                sample.setFolio(folio);
            }
            if (Abs260_280_usmb != null && !Abs260_280_usmb.isEmpty()) {
                sample.setAbs260_280_usmb(Abs260_280_usmb);
            }
            if (Abs260_230_usmb != null && !Abs260_230_usmb.isEmpty()) {
                sample.setAbs260_230_usmb(Abs260_230_usmb);
            }
            if (delivery != null && !delivery.equals("---")) {
                sample.setDelivery(delivery);
            }
            if (labConcent != null && !labConcent.isEmpty()) {
                sample.setLabConcen(Double.parseDouble(labConcent));
            }
            if (samConcent != null && !samConcent.isEmpty()) {
                sample.setSampleQuantity(samConcent);
            }
            if (apptype != null && !apptype.isEmpty()) {
                sample.setApp_type(apptype);
            }
            if (plataform != null && !plataform.isEmpty()) {
                sample.setSamplePlataform(plataform);
            }
            if (readSize != null && !readSize.equals("---")) {
                sample.setReadSize(readSize);
            }
            if (inSize != null && !inSize.equals("---")) {
                sample.setInsertSize(inSize);
            }
            if (samQuality != null && !samQuality.equals("---")) {
                qualityChange = true;
                sample.setSampleQuality(samQuality);
                Comments commentsSample = new Comments();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                commentsSample.setComment("La calidad de la muestra cambio a -" + samQuality + "- por " + us.getUserName());
                commentsSample.setIdType(sample.getIdSample() + "");
                commentsSample.setType("Sample");
                commentsSample.setUserName("SISBI");
                commentsSample.setCommentDate(timestamp);

                commentFac.createComment(commentsSample);

                //leslie 02/22/2024
                statusAnt = sample.getStatus();

                if (!statusAnt.equals("En analisis de calidad") && samStat.equals("---")) {
                    sample.setStatus("En analisis de calidad");
                    //aqui el status cambia  si el estatus anterior no era ya en analisis de calida

                    Comments commentsSample2 = new Comments();
                    Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
                    Users us2 = (Users) context.getExternalContext().getSessionMap().get("usuario");
                    commentsSample2.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En analisis de calidad" + "- por " + us.getUserName());
                    commentsSample2.setIdType(sample.getIdSample() + "");
                    commentsSample2.setType("Sample");
                    commentsSample2.setUserName("SISBI");
                    commentsSample2.setCommentDate(timestamp2);

                    commentFac.createComment(commentsSample2);

                }

            }
            // Inicio de los cambios de status 
            if (samStat != null && !samStat.equals("---")) {
                statusAnt = sample.getStatus();
                //leslie06 mayo 2024                              
                int pos_stat_ant = 0;
                int pos_stat_nuevo = 0;

                //calcula la posicion del viejo status vs la lista ordenada de status
                for (int i = 0; i < lista_status.size(); i++) {
                    if (statusAnt.equals(lista_status.get(i))) {
                        System.out.println("-* VIEJO estatus: valor en la lista :" + lista_status.get(i));
                        pos_stat_ant = i;
                    }
                }

                //calcula la posicion del nuevo status vs la lista ordenada de status
                for (int i = 0; i < lista_status.size(); i++) {
                    if (samStat.equals(lista_status.get(i))) {
                        System.out.println("NUEVO estatus: valor en la lista :" + lista_status.get(i));
                        pos_stat_nuevo = i;
                    }
                }

                // Carlos Perez Calderon 02/04/2025
                // Se reestructuro la validacion de los cambios de estatus respetando la logica anterior del codigo
                if (pos_stat_nuevo == 2) {
                    // Siempre aplica para “En espera de instrucciones” (5) y 
                    // “Entrega solo análisis de calidad” (4)
                    System.out.println("--> EXCEPCIÓN o EN ESPERA DE INSTRUCCIONES: Se actualiza el status");
                    sample.setStatus(samStat);
                    statusChange = true;
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + samStat + "- por " + us.getUserName());
                    commentsSample.setIdType(sample.getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);
                    commentFac.createComment(commentsSample);

                } else if (pos_stat_ant == 15) {
                    // Prohibido si está Rechazada (Forsake)
                    System.out.println(" xX  FORSAKE: No se va actualizar el status item " + iteracion);
                    RequestContext dialog = RequestContext.getCurrentInstance();
                    strMessage = "¡No se puede realizar el cambio de estatus, la muestra con ID "
                            + sample.getIdSample() + " está RECHAZADA (Forsake)!";
                    setMessageDialog(strMessage);
                    dialog.execute("PF('samplesStateBefore').show();");
                    return;

                } else if (pos_stat_ant == 14) {
                    // Siempre permitir desde “Para resecuenciar”
                    System.out.println("--> RESECUENCIACIÓN: Se actualiza el status");
                    sample.setStatus(samStat);
                    statusChange = true;
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + samStat + "- por " + us.getUserName());
                    commentsSample.setIdType(sample.getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);
                    commentFac.createComment(commentsSample);

                } else if (pos_stat_nuevo <= pos_stat_ant) {
                    // No permitir retroceder ni mismo estatus
                    System.out.println("xX INTENTO CAMBIO ANTERIOR O MISMO ESTATUS: No se va actualizar el estatus");
                    RequestContext dialog = RequestContext.getCurrentInstance();
                    strMessage = "¡La muestra con ID " + sample.getIdSample()
                            + " no se puede cambiar al mismo estatus o a uno anterior!";
                    setMessageDialog(strMessage);
                    dialog.execute("PF('samplesStateBefore').show();");
                    return;

                } else {
                    // Flujo normal
                    System.out.println("--> SE SIGUE EL FLUJO: Se actualiza el status");
                    sample.setStatus(samStat);
                    statusChange = true;
                    if ("Recibida".equals(samStat)) {
                        sample.setReceptionDate(new Timestamp(System.currentTimeMillis()));
                    }
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + samStat + "- por " + us.getUserName());
                    commentsSample.setIdType(sample.getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);
                    commentFac.createComment(commentsSample);
                }

            } else if (recepDate != null && !recepDate.toString().isEmpty()) {
                //validar el cambio de estatus
                statusAnt = sample.getStatus();
                Comments commentsSample = new Comments();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -Recibida- por " + us.getUserName());
                commentsSample.setIdType(sample.getIdSample() + "");
                commentsSample.setType("Sample");
                commentsSample.setUserName("SISBI");
                commentsSample.setCommentDate(timestamp);

                commentFac.createComment(commentsSample);

                sample.setReceptionDate(recepDate);
                sample.setStatus("Recibida");
            }

            if (des != null && !des.isEmpty()) {
                sample.setDescription(des);
            }

            if (delDate != null && !delDate.toString().isEmpty()) {
                sample.setDeliveryDate(delDate);
            }

            if (type != null && !type.equals("---")) {
                sample.setType(type);
            }

            if (expPerfo != null && !expPerfo.isEmpty()) {
                sample.setExpectedPerformance(expPerfo);
            }

            if (expPerfoOxford != null && !expPerfoOxford.isEmpty()) {
                sample.setExpectedPerformanceOxford(expPerfoOxford);
            }

            if (realPerfo != null && !realPerfo.isEmpty()) {
                sample.setRealPerformance(realPerfo);
            }
            if (contName != null && !contName.isEmpty()) {
                sample.setContaminantName(contName);
            }
            if (orgName != null && !orgName.isEmpty()) {
                sample.setOrganismName(orgName);
            }
            if (refName != null && !refName.isEmpty()) {
                sample.setReferenceName(refName);
            }

            if (comment != null && !comment.isEmpty()) {

                Comments commentsSample = new Comments();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                commentsSample.setComment(comment);
                commentsSample.setIdType(sampleTable.get(0).getIdSample() + "");
                commentsSample.setType("Sample");
                commentsSample.setUserName(us.getUserName());
                commentsSample.setCommentDate(timestamp);

                commentFac.createComment(commentsSample);
                System.out.println("pasa por el ejbFacade");
                comment = null;

            }
            //sendMail = false;
        }

        // add maintenance ------------------------------------------Start 
        System.out.println("dijo que: " + haveBioAn);
        if (haveBioAn == true) {
            BioinformaticAnalysis bio = new BioinformaticAnalysis();
            int idBioinformaticAnalysis;
            for (int i = 0; i < sampleTable.size(); i++) {

                for (int j = 0; j < analysisCost.size(); j++) {
                    System.out.println("Nombre de la muestra: " + sampleTable.get(i).getSampleName() + " con tipo de reporte: " + analysisCost.get(j));
                    bio = ejbFacadeBioinformaticAnalysis.getIdBioinformaticAnalysisFindByName(analysisCost.get(j).trim());
                    System.out.println("id del tipo de análisis: " + bio.getIdAnalysis());

                    //Validar si ya existe ese registro en la tabla
                    List<BioinformaticAnalysisSampleLink> bioAnalysisSampleink = getBAFac().getListBioAnalysisSampleLinkByIDAnalysisSample(bio.getIdAnalysis(), sampleTable.get(i).getIdSample());
                    if (bioAnalysisSampleink.isEmpty()) {
                        //Insertar en la tabla currentBioinformaticAnalysisSampleLink
                        currentBioinformaticAnalysisSampleLink = new BioinformaticAnalysisSampleLink();
                        currentBioinformaticAnalysisSampleLink.setBioinformaticAnalysisSampleLinkPK(new jpa.entities.BioinformaticAnalysisSampleLinkPK());
                        currentBioinformaticAnalysisSampleLink.getBioinformaticAnalysisSampleLinkPK().setIdSample(sampleTable.get(i).getIdSample());
                        currentBioinformaticAnalysisSampleLink.getBioinformaticAnalysisSampleLinkPK().setIdAnalysis(bio.getIdAnalysis());
                        getBAFac().create(currentBioinformaticAnalysisSampleLink);
                    }
                }
            }
            System.out.println("Se insertaron las relaciones con exito");
            sampleTable = new ArrayList<>();
            analysisCost = new ArrayList<>();
        } else {
            //borrar las relaciones de las dos tablas
            //iteramos las muestras
            for (int i = 0; i < sampleTable.size(); i++) {
                System.out.println("se eliminará el id sample: " + sampleTable.get(i).getIdSample());

                //validamos si existen registros para eliminar
                //iteramos los tipos de análisis que tiene esa muestra
                List<BioinformaticAnalysisSampleLink> bioAnalisysSampleLink = getBAFac().getListBioAnalysisSampleLinkByIDSample(sampleTable.get(i).getIdSample());
                if (bioAnalisysSampleLink.size() > 0) {
                    for (int j = 0; j < bioAnalisysSampleLink.size(); j++) {
                        currentBioinformaticAnalysisSampleLink = getBAFac().getListBioAnalysisSampleLinkByIDSample(sampleTable.get(i).getIdSample()).get(0);
                        getBAFac().remove(currentBioinformaticAnalysisSampleLink);
                    }
                } else {
                    System.out.println("No hay registros para eliminar para esta muestra: " + sampleTable.get(i).getIdSample());
                }
            }
            System.out.println("Se eliminaron las relaciones con exito!");
            sampleTable = new ArrayList<>();
            analysisCost = new ArrayList<>();
        }

        // add maintenance ------------------------------------------End 
        if (statusChange && sendMail) {

            List<UserProjectLink> uplc = UserProjFac.findAll();
            List<String> emails = new ArrayList<>();
            for (UserProjectLink userProjectLink : uplc) {
                if (pj.getIdProject().equals(userProjectLink.getProject().getIdProject())) {
                    emails.add(userProjectLink.getUsers().getEmail());
                }
            }
            EmailController ec = new EmailController();
            System.out.println("Manda email de cambio de estatus");
            ec.sendUpdateStatusSamplesEmail(samplesParaCorreo, samStat, statusAnt, emails, pj.getProjectName());

        }

        if (qualityChange && sendMail) {

            List<UserProjectLink> uplc = UserProjFac.findAll();

            List<String> emails = new ArrayList<>();

            for (UserProjectLink userProjectLink : uplc) {
                if (pj.getIdProject().equals(userProjectLink.getProject().getIdProject())) {
                    emails.add(userProjectLink.getUsers().getEmail());
                }
            }
            EmailController ec = new EmailController();
            System.out.println("Manda email de cambio de calidad");
            ec.sendUpdateQualitySamplesEmail(samplesParaCorreo, samQuality, emails, pj.getProjectName());

        }

        clearVars();

        try {
            context.getExternalContext().redirect("List.xhtml");
            System.out.println();
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
        sendMail = false;;
    }

    public void updateManySamplesView() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

        String statusAnt = "";
        boolean statusChange = false;
        boolean qualityChange = false;
        for (Sample sample : sampleTable) {

            if (samName != null && !samName.isEmpty()) {
                String[] name = sample.getSampleName().split("_");
                if (name.length >= 1) {

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String[] tiempo = timestamp.toString().split(":");
                    String[] tiempo2 = tiempo[0].split("-");
                    String[] dia = tiempo2[2].split(" ");
                    String año = tiempo2[0].substring(2, 4);
                    String fecha = año + tiempo2[1] + dia[0];

                    String[] formated = pj.getIdProject().split("_");
                    String manager = formated[1];
                    sample.setSampleName(manager + "_" + fecha + "_" + samName);

                } else {
                    sample.setSampleName(name[0] + "_" + name[1] + "_" + samName);
                }
            }
            if (tube != null && !tube.isEmpty()) {
                sample.setIdTube(tube);
            }

            if (acept != null && !acept.equals("---")) {
                sample.setAceptation(acept);
                if (!acept.equals("Rechazada")) {
                    // statusAnt = sample.getStatus();
                    // sample.setStatus("En construccion de biblioteca");

                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                    //leslie modifico
                    commentsSample.setComment("La aceptacion de la muestra cambio a -" + acept + "- " + "- por " + us.getUserName());
                    commentsSample.setIdType(sample.getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);
                    commentFac.createComment(commentsSample);

                }
            }
            if (genExtra != null && !genExtra.isEmpty()) {
                sample.setGeneticExtraction(genExtra);
            }
            if (labVolume != null && !labVolume.isEmpty()) {
                sample.setLabVolume(Double.parseDouble(labVolume));
            }
            if (samVolume != null && !samVolume.isEmpty()) {
                sample.setSampleVolume(samVolume);
            }
            if (Abs260_280 != null && !Abs260_280.isEmpty()) {
                sample.setAbs260_280(Abs260_280);
            }
            if (Abs260_230 != null && !Abs260_230.isEmpty()) {
                sample.setAbs260_230(Abs260_230);
            }
            if (folio != null && !folio.isEmpty()) {
                sample.setFolio(folio);
            }
            if (Abs260_280_usmb != null && !Abs260_280_usmb.isEmpty()) {
                sample.setAbs260_280_usmb(Abs260_280_usmb);
            }
            if (Abs260_230_usmb != null && !Abs260_230_usmb.isEmpty()) {
                sample.setAbs260_230_usmb(Abs260_230_usmb);
            }
            if (delivery != null && !delivery.equals("---")) {
                sample.setDelivery(delivery);
            }
            if (labConcent != null && !labConcent.isEmpty()) {
                sample.setLabConcen(Double.parseDouble(labConcent));
            }
            if (samConcent != null && !samConcent.isEmpty()) {
                sample.setSampleQuantity(samConcent);
            }
            if (plataform != null && !plataform.isEmpty()) {
                sample.setSamplePlataform(plataform);
            }
            if (readSize != null && !readSize.equals("---")) {
                sample.setReadSize(readSize);
            }

            if (inSize != null && !inSize.equals("---")) {
                sample.setInsertSize(inSize);
            }
            if (samQuality != null && !samQuality.equals("---")) {
                qualityChange = true;
                sample.setSampleQuality(samQuality);
            }
            if (samStat != null && !samStat.equals("---")) {
                statusAnt = sample.getStatus();

                //leslie 07 mayo 2024
                //leslie06 mayo 2024                              
                int pos_stat_ant = 0;
                int pos_stat_nuevo = 0;
                int fin_viejo = 0;

                //veo las posiciones y sus elementos de la lista que contiene todos los status en sisbi
                for (int i = 0; i < lista_status.size(); i++) {
                    System.out.println("posicion: " + i + " = " + lista_status.get(i));
                }
                //calcula la posicion del viejo status vs la lista ordenada de status
                for (int i = 0; i < lista_status.size(); i++) {
                    if (statusAnt.equals(lista_status.get(i))) {
                        System.out.println("-* VIEJO estatus: valor en la lista :" + lista_status.get(i));
                        pos_stat_ant = i;
                    }
                }
                //calcula la posicion del nuevo status vs la lista ordenada de status
                for (int i = 0; i < lista_status.size(); i++) {
                    if (samStat.equals(lista_status.get(i))) {
                        System.out.println("NUEVO estatus: valor en la lista :" + lista_status.get(i));
                        pos_stat_nuevo = i;
                    }
                }

                if (pos_stat_nuevo == 5) {//en espera de instrucciones por parte del usuario
                    System.out.println("--> EN ESPERA DE INSTRUCCIONES: Se actualiza el status");
                    sample.setStatus(samStat);
                    statusChange = true;
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + samStat + "- por " + us.getUserName());
                    commentsSample.setIdType(sample.getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);
                    commentFac.createComment(commentsSample);
                } else {
                    if (pos_stat_ant == 15) {
                        System.out.println(" xX  FOSRSAKE:  No se va actualizar el status");
                        RequestContext dialog_stat = RequestContext.getCurrentInstance();
                        setMessageDialog("¡No se puede realizar el cambio de estatus, la muestra esta RECHAZADA (Forsake)!");
                        dialog_stat.execute("PF('samplesStateBefore').show();");
                        return;
                    } else {
                        if (pos_stat_ant == 14) { //si es resecuenciacion 
                            System.out.println("--> RESECUENCIACION: Se actualiza el status");
                            sample.setStatus(samStat);
                            statusChange = true;
                            Comments commentsSample = new Comments();
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                            commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + samStat + "- por " + us.getUserName());
                            commentsSample.setIdType(sample.getIdSample() + "");
                            commentsSample.setType("Sample");
                            commentsSample.setUserName("SISBI");
                            commentsSample.setCommentDate(timestamp);
                            commentFac.createComment(commentsSample);
                        } else {
                            if (pos_stat_nuevo <= pos_stat_ant) {
                                System.out.println("xX INTENTO CAMBIO DE ESTADO ANTERIOR O MISMO ESTATUS : No se va actualizar el estatus");
                                RequestContext dialog_stat = RequestContext.getCurrentInstance();
                                // messageDialog="¡No se puede realizar el cambio de estatus a un paso anterior o el mismo estatus!";
                                setMessageDialog("¡No se puede realizar el cambio de estatus a un paso anterior o el mismo estatus!");
                                dialog_stat.execute("PF('samplesStateBefore').show();");
                                return;
                            } else {
                                System.out.println("--> SE SIGUE EL FLUJO: Se actualiza el status ");
                                sample.setStatus(samStat);
                                statusChange = true;
                                if (samStat.equals("Recibida")) {
                                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                    sample.setReceptionDate(timestamp);
                                }
                                Comments commentsSample = new Comments();
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                                commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + samStat + "- por " + us.getUserName());
                                commentsSample.setIdType(sample.getIdSample() + "");
                                commentsSample.setType("Sample");
                                commentsSample.setUserName("SISBI");
                                commentsSample.setCommentDate(timestamp);
                                commentFac.createComment(commentsSample);
                            }
                        }
                    }
                }
            }

            if (des != null && !des.isEmpty()) {
                sample.setDescription(des);
            }

            if (delDate != null && !delDate.toString().isEmpty()) {
                sample.setDeliveryDate(delDate);
            }
            if (recepDate != null && !recepDate.toString().isEmpty()) {
                sample.setReceptionDate(recepDate);
            }

            if (type != null && !type.equals("---")) {
                sample.setType(type);
            }

            if (expPerfo != null && !expPerfo.isEmpty()) {
                sample.setExpectedPerformance(expPerfo);
            }

            if (expPerfoOxford != null && !expPerfoOxford.isEmpty()) {
                sample.setExpectedPerformanceOxford(expPerfoOxford);
            }

            if (realPerfo != null && !realPerfo.isEmpty()) {
                sample.setRealPerformance(realPerfo);
            }
            if (contName != null && !contName.isEmpty()) {
                sample.setContaminantName(contName);
            }
            if (orgName != null && !orgName.isEmpty()) {
                sample.setOrganismName(orgName);
            }
            if (refName != null && !refName.isEmpty()) {
                sample.setReferenceName(refName);
            }

            if (comment != null && !comment.isEmpty()) {

                Comments commentsSample = new Comments();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                commentsSample.setComment(comment);
                commentsSample.setIdType(sampleTable.get(0).getIdSample() + "");
                commentsSample.setType("Sample");
                commentsSample.setUserName(us.getUserName());
                commentsSample.setCommentDate(timestamp);

                commentFac.createComment(commentsSample);
                System.out.println("pasa por el ejbFacade");
                comment = null;

            }
            getFacade().edit(sample);

        }
        if (statusChange && sendMail) {

            List<UserProjectLink> uplc = UserProjFac.findAll();
            List<String> emails = new ArrayList<>();
            for (UserProjectLink userProjectLink : uplc) {
                if (pj.getIdProject().equals(userProjectLink.getProject().getIdProject())) {
                    emails.add(userProjectLink.getUsers().getEmail());
                }
            }
            EmailController ec = new EmailController();
            System.out.println("Manda email de cambio de estatus");
            ec.sendUpdateStatusSamplesEmail(sampleTable, samStat, statusAnt, emails, pj.getProjectName());

        }

        if (qualityChange && sendMail) {

            List<UserProjectLink> uplc = UserProjFac.findAll();

            List<String> emails = new ArrayList<>();

            for (UserProjectLink userProjectLink : uplc) {
                if (pj.getIdProject().equals(userProjectLink.getProject().getIdProject())) {
                    emails.add(userProjectLink.getUsers().getEmail());
                }
            }
            EmailController ec = new EmailController();
            System.out.println("Manda email de cambio de calidad");
            ec.sendUpdateQualitySamplesEmail(sampleTable, samQuality, emails, pj.getProjectName());

        }

        clearVars();

        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
            System.out.println();
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean areMoreThanOne() {
        if (sampleTable != null) {
            return sampleTable.size() <= 1;
        } else {
            return false;
        }

    }

    public void update() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            getFacade().edit(current);
            context.getExternalContext().redirect("List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateSample() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            getFacade().edit(selectedSample);
            context.getExternalContext().redirect("adminSampleList.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateAllSample() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            getFacade().edit(selectedSample);
            context.getExternalContext().redirect("AllSamples.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getNumberSamplesProj(Project proj) {

        List<Sample> samples = ejbFacade.findSampProj2(proj);
        return samples.size();
    }

    public int getNumberSamplesBioAn(Project proj) {
        List<Sample> samples = ejbFacade.findSampProj2(proj);
        List<Sample> newList = new ArrayList<>();
        for (Sample sam : samples) {

            List<BioinformaticAnalysis> BA = BAFac.findBionformaticAnalysisBySample(sam);

            for (BioinformaticAnalysis ba : BA) {
                newList.add(sam);

            }
        }
        return newList.size();
    }

    public String getNumberSamplesBioAnCon(Project proj) {
        List<Sample> samples = ejbFacade.findSampProj2(proj);
        List<Sample> newList = new ArrayList<>();
        String AnalisisBio = "";
        for (Sample sam : samples) {

            List<BioinformaticAnalysis> BA = BAFac.findBionformaticAnalysisBySample(sam);

            for (BioinformaticAnalysis ba : BA) {
                newList.add(sam);

            }
        }

        if (newList.size() > 0) {
            AnalisisBio = "Con análisis bioinformático";
        } else {
            AnalisisBio = "Sin analisis bioinformatico";
        }
        return AnalisisBio;
    }

    public List<Sample> getSamplesProj(Project proj) {
        //Devuelve las muestras que estàn en un análisis bioinformático
        //TODO devolver ahì también los reportProject

        List<Sample> samples = ejbFacade.findSampProj2(proj);
        List<Sample> newList = new ArrayList<>();
        for (Sample sam : samples) {
            // System.out.println(sam.getIdSample());
            List<BioinformaticAnalysis> BA = BAFac.findBionformaticAnalysisBySample(sam);

            for (BioinformaticAnalysis ba : BA) {
                newList.add(sam);
                // newList.add(ba.getAnalysisName());
                //BioinfoAnalysisBySample(newList);

            }
        }
        List<Sample> newList2 = newList.stream().distinct().collect(Collectors.toList());
        return newList2;
        //return newList;
    }

    public List<String> getAnalysisbySample(Sample id) {

        List<String> newList = new ArrayList<>();

        // System.out.println(sam.getIdSample());
        List<BioinformaticAnalysis> BA = BAFac.findBionformaticAnalysisBySample(id);

        for (BioinformaticAnalysis ba : BA) {
            newList.add(ba.getAnalysisName());
            // newList.add(ba.getAnalysisName());
            //BioinfoAnalysisBySample(newList);

        }

        List<String> newList2 = newList.stream().distinct().collect(Collectors.toList());
        return newList2;
        //return newList;
    }

    public void updateView() {

        FacesContext context = FacesContext.getCurrentInstance();
        getFacade().edit(selectedSample);
        try {

            context.getExternalContext().redirect("adminSampleList.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateViewList() {

        FacesContext context = FacesContext.getCurrentInstance();
        getFacade().edit(selectedSample);
        try {

            context.getExternalContext().redirect("List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean canEdit() {
        //TODO al parecee esta función se usa 'siempre' que se tieneque checar si el usuario es o no administrador,
        // entonces el nombre está confuso
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        return us.getUserType().equals("Admin");

    }

    public DataModel getItems() {

        items = getPagination().createPageDataModel();

        return items;
    }

    public List<Sample> getItemsProj() {

        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        return ejbFacade.findSampProj2(proj);
        //return ejbFacade.sampleByProject(proj.getIdProject());
    }

    public List<Sample> getItemsProj(Project proj) {
        // DEvulelve todas las muestras (calificables para anàlisis bioinformático o no, en cualquier estado de aceptacion), para determinado proyecto
        return ejbFacade.findSampProj2(proj);
    }

    public List<Sample> getItemsProjSam(Sample sm) {

        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        List<Sample> samples = ejbFacade.findSampProj2(proj);
        List<Sample> selectedSamples = new ArrayList<>();
        for (Sample selectedSample1 : samples) {

            if (selectedSample1.getIdSample().equals(sm.getIdSample())) {
                selectedSamples.add(sm);
            }
        }

        return selectedSamples;
    }

    public List<Sample> getItemsProjSelected() {

        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        return ejbFacade.findSampProj2(proj);
    }

    public List<Sample> getAllSamples() {

        return ejbFacade.findAll();

    }

    public List<Sample> getSamples() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        return ejbFacade.findSamples(proj.toString());

    }

    public void uploadFileSam(FileUploadEvent event) {

        UploadedFile file = event.getFile();
        BufferedReader br;
        try {
            InputStream stream = file.getInputstream();
            br = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while (br.ready() && (line = br.readLine()) != null) {

                sb.append(line + "\r\n");

            }

            String[] corteEnters = sb.toString().split("\n");

            if (checkEditSamples(corteEnters)) {

                for (int i = 0; i < corteEnters.length; i++) {
                    if (i > 0) {
                        String[] corteComas = corteEnters[i].replace("\"", "").split(",");
                        String id = corteComas[0]; //id_colocado desde el excel

                        for (Sample sam : sampleTable) {
                            String idsample = sam.getIdSample().toString(); //id de la consulta al objeto

                            System.out.println("*** excel : " + id);
                            System.out.println("+++seleccion : " + idsample);

                            if (id.equals(idsample)) {

                                String labcon = corteComas[3].trim();
                                String labvol = corteComas[4].trim();

                                if (!labcon.equals("")) {
                                    System.out.println("lab concentracion :" + labcon);
                                    //selectedLibrary.setLibraryName(nombrelib);
                                    sam.setLabConcen(Double.parseDouble(labcon));
                                } else {
                                    System.out.println("concentracion vacio");
                                }

                                if (!labvol.equals("")) {
                                    System.out.println("lab volumen :" + labvol);
                                    //selectedLibrary.setLibraryName(nombrelib);
                                    sam.setLabVolume(Double.parseDouble(labvol));
                                } else {
                                    System.out.println("volumen vacio");
                                }

                                ejbFacade.edit(sam);

                                System.out.println("se editaron concentracion y/o volumen de la muestra");

                            }

                        }
                    }
                }

                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('samAceptDialog').show();");

            } else {

                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('samErrorDialog').show();");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LibraryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(LibraryController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en el archivo");
        }

    }

    public boolean checkEditSamples(String[] listSamples) {
        System.out.println("Entra a checkSamples");
        if (listSamples.length - 1 != sampleTable.size()) {
            System.out.println("No son iguales");
            return false;
        }

        List<String> names = new ArrayList<>();
        List<String> namesToCompare = new ArrayList<>();

        for (int i = 0; i < listSamples.length - 1; i++) {

            if (i > 0) {

                String[] comaSeparated = listSamples[i].replace("\"", "").split(",");
                try {
                    double num = Double.parseDouble(comaSeparated[3]);
                    double num2 = Double.parseDouble(comaSeparated[4]);

                } catch (NumberFormatException e) {
                    System.out.println("Caractér no numerico encontrado en las concentraciones");
                    return false;
                }

                names.add(comaSeparated[1] + comaSeparated[2]);

            }
        }
        for (Sample sam : sampleTable) {

            namesToCompare.add(sam.getSampleName() + sam.getIdTube());

        }
        Collections.sort(names);
        Collections.sort(namesToCompare);

        for (int i = 0; i < names.size() - 1; i++) {

            if (names.get(i).equals(names.get(i + 1))) {

                System.out.println("Existe un nombre igual");
                return false;

            }
        }
        for (int i = 0; i < names.size(); i++) {
            if (!(names.get(i).equals(namesToCompare.get(i)))) {
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

    public void DeleteSample(int idSample) {
        RequestContext Context = RequestContext.getCurrentInstance();
        List<Sample> delSample = ejbFacade.findAll();
        try {
            for (Sample deleteSam : delSample) {
                if (deleteSam.getIdSample().equals(idSample)) {
                    System.out.println("nombre de la muestra: " + deleteSam.getSampleName());
                    ejbFacade.remove(deleteSam);
                    System.out.println("Muestra elminada");

                }

            }
            Context.execute("PF('dialogDeleteSample').show();");

        } catch (Exception e) {
            System.out.println(e);

        }

    }

    public void updateSampleByRange() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        RequestContext cont = RequestContext.getCurrentInstance();
        String statusAnt = "";
        int sam = 0;
        String separate[] = edit.split(",");

        for (String id : separate) {
            if (Pattern.matches("[0-9]*-[0-9]*", id)) {

                String sep[] = id.split("-");
                List<Sample> Samples = ejbFacade.findSampleByRange(Integer.parseInt(sep[0]), Integer.parseInt(sep[1]), proj);
                if (Samples.size() > 0) {
                    for (Sample sm : Samples) {
                        sam++;
                        System.out.println("Muestra: " + sm.getIdSample());
                        if (samStat != null && !samStat.equals("---")) {
                            statusAnt = sm.getStatus();
                            if (!statusAnt.equals(samStat)) {
                                sm.setStatus(samStat);
                                //statusChange = true;
                                if (samStat.equals("Recibida")) {
                                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                    sm.setReceptionDate(timestamp);
                                }

                                Comments commentsSample = new Comments();
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                                commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + samStat + "- por " + us.getUserName());
                                commentsSample.setIdType(sm.getIdSample() + "");
                                commentsSample.setType("Sample");
                                commentsSample.setUserName("SISBI");
                                commentsSample.setCommentDate(timestamp);

                                commentFac.createComment(commentsSample);

                            }
                        }
                        if (acept != null && !acept.equals("---")) {
                            sm.setAceptation(acept);
                        }
                        ejbFacade.edit(sm);

                    }
                    /*cont.execute("PF('UpdateSamDialog').hide();");
                    JsfUtil.addSuccessMessage(sam + " muestras han sido actualizadas exitosamente");*/
                } else {
                    JsfUtil.addErrorMessage("El rango: " + id + " de las muestras que proporciono no pertenecen a este proyecto.");
                }

            } else {

                System.out.println("solo una muestra: " + id);
                List<Sample> Samples = ejbFacade.findSamplesByIdAndProject(Integer.parseInt(id), proj);
                if (Samples.size() > 0) {
                    sam++;

                    for (Sample sm : Samples) {
                        System.out.println("Nombre de la muestra: " + sm.getSampleName());

                        if (samStat != null && !samStat.equals("---")) {
                            statusAnt = sm.getStatus();
                            if (!statusAnt.equals(samStat)) {
                                sm.setStatus(samStat);
                                //statusChange = true;
                                if (samStat.equals("Recibida")) {
                                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                    sm.setReceptionDate(timestamp);
                                }

                                Comments commentsSample = new Comments();
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                                commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + samStat + "- por " + us.getUserName());
                                commentsSample.setIdType(sm.getIdSample() + "");
                                commentsSample.setType("Sample");
                                commentsSample.setUserName("SISBI");
                                commentsSample.setCommentDate(timestamp);

                                commentFac.createComment(commentsSample);

                            }
                        }
                        if (acept != null && !acept.equals("---")) {
                            sm.setAceptation(acept);
                        }
                        ejbFacade.edit(sm);

                    }
                    cont.execute("PF('UpdateSamDialog').hide();");
                    JsfUtil.addSuccessMessage(sam + " muestras han sido actualizadas exitosamente");
                } else {
                    JsfUtil.addErrorMessage("La muestra con el ID:" + id + " no pertenece a este proyecto");
                }
            }
        }

    }

    public void getPrevURL() {
        System.out.println("obtenemos el url de la pagina anterior");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String referrer = externalContext.getRequestHeaderMap().get("referer");
        prevUrL = referrer;

        System.out.println("prevURL:" + prevUrL);

    }

    public void getURLPreviousPage() throws IOException {
        System.out.println("metodo getURL");
        FacesContext context = FacesContext.getCurrentInstance();
        // HttpServletRequest.getHeader("Referer");
        /* ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
   String referrer = externalContext.getRequestHeaderMap().get("referer"); */
        //context.getExternalContext().redirect(URl);

        System.out.println(prevUrL);
        context.getExternalContext().redirect(prevUrL);

    }

    public void continueCreateUser() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            context.getExternalContext().redirect("../users/Create.xhtml");
            //context.getExternalContext().redirect("../project/ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void continueCreateProject() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            context.getExternalContext().redirect("../project/Create.xhtml");
            //context.getExternalContext().redirect("../project/ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void resetSelectionSampleTable() {
        sampleTable.clear();
    }

    public void asignarValorCampos() { //metodo que ayuda a validar los campos 

        op_service = op_service;
        current.getJust_genextrac();
        current.getJust_qanalysis();
        current.getJust_lib();
        current.getJust_seq();
        current.getBuild_lib();
        current.getDo_seq();

        seq = seq; // pasa directo desde el controller
        current.getSampleName(); //sale del selected
        current.getGeneticExtraction(); //sale del selected
        current.getIdTube();//sale del selected
        selectedSample.getDelivery();
        selectedSample.getExpectedPerformance();
        selectedSample.getExpectedPerformanceOxford();
        unidades = unidades;
        comment = comment;
        selectedSample.getInsertSize();
        refName = refName;
        orgName = orgName;
        selectedSample.getDescription();
        selectedSample.getSampleQuantity();
        selectedSample.getSampleVolume();
        selectedSample.getAbs260_280();
        selectedSample.getAbs260_230();
        selectedSample.getSamplePlataform();
        System.out.println("Se asigna valor al campo nombre " + current.getSampleName() + current.getGeneticExtraction());

    }

    public void cleanFormSample() {

        //inicio leslie
        type = "";
        delivery = "";
        Rdeplesion = "";
        organimstype = "";
        Bulib = "";
        Doseq = "";

        //orgName="";
        //refName="";
        //fin leslie 
        /*
        orgName = null;
        refName = null;
        contName = "";
        unidades = null;
        type="";
        delivery="";*/
    }

    public void redirectCreateSample() {

        current = new Sample();
        selectedItemIndex = -1;

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            context.getExternalContext().redirect("../sample/Create.xhtml");
            //context.getExternalContext().redirect("../project/ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void opcion_service() {
        switch (op_service) {
            case "1":
                current.setJust_genextrac("Si");
                break;
            case "2":
                current.setJust_qanalysis("Si");
                break;
            case "3":
                current.setJust_lib("Si");
                break;
            case "4":
                current.setJust_seq("Si");
                break;
        }
        asignarValorCampos();
    }

    public void addRequireInput() {
        // para hacer obligatorios los campos de concentracion y abs
        if (op_service.equals("3") || op_service.equals("4") || (op_service.equals("5") && current.getGeneticExtraction().equals("No"))) {
            requireInput = true;
        } else {
            requireInput = false;
        }
        asignarValorCampos();

        /*
        if (current.getGeneticExtraction().equals("No")) {
            requireInput = true;
        } else {
            requireInput = false;
        }
        asignarValorCampos();*/
    }

    public void requireUnidades() {
        asignarValorCampos();
        System.out.println("Cantidad de rendimeinto: " + current.getExpectedPerformance().trim().length());
        if (current.getExpectedPerformance().trim().length() > 0) {

            requireInputUnidades = true;
        } else {
            requireInputUnidades = false;
        }
    }

    //Obtener lista de muestras
    public void updateListSample() {

        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        listSample = ejbFacade.findSampProj2(proj);
        //return ejbFacade.sampleByProject(proj.getIdProject());
    }

    //Método para eliminar las relaciones que hay entre la tabla sample y 
    public void cancelBioinformaticAnalysis() {
        RequestContext rcontext = RequestContext.getCurrentInstance();
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        //Consultamos todas las muestras relacionadas al proyecto
        List<Sample> listSamples = ejbFacade.findSamplesByProject(proj);

        try {
            for (int i = 0; i < listSamples.size(); i++) {
                System.out.println("se eliminará el id sample: " + listSamples.get(i).getIdSample());

                //validamos si existen registros para eliminar
                //iteramos los tipos de análisis que tiene esa muestra
                List<BioinformaticAnalysisSampleLink> bioAnalisysSampleLink = getBAFac().getListBioAnalysisSampleLinkByIDSample(listSamples.get(i).getIdSample());
                if (bioAnalisysSampleLink.size() > 0) {
                    for (int j = 0; j < bioAnalisysSampleLink.size(); j++) {
                        currentBioinformaticAnalysisSampleLink = getBAFac().getListBioAnalysisSampleLinkByIDSample(listSamples.get(i).getIdSample()).get(0);
                        getBAFac().remove(currentBioinformaticAnalysisSampleLink);
                    }
                } else {
                    System.out.println("No hay registros para eliminar para esta muestra: " + listSamples.get(i).getIdSample());
                }
            }

            rcontext.execute("PF('dialogConfirmationCancelAB').hide();");
            rcontext.execute("PF('dialogCancelAB').show();");
        } catch (Exception e) {
            rcontext.execute("PF('dialogConfirmationCancelAB').hide();");
            rcontext.execute("PF('dialogErrorL').show();");
        }
    }

    public boolean isIllumina(String selectPlataform) {
        /*
        //Constantes para validar tamaños de secuencias
        List<String> listPlatform = new ArrayList<>();
        //Llenado de arreglos para validar
        listPlatform.add("NextSeq 500");
        listPlatform.add("MiSeq");
        listPlatform.add("HiSeq 2000/2500");
        listPlatform.add("HiSeq X ");
        listPlatform.add("NovaSeq 6000");
        
        listPlatform.add("NextSeq 500 - Oxford Nanopore");
        listPlatform.add("MiSeq - Oxford Nanopore");
        listPlatform.add("HiSeq 2000/2500 - Oxford Nanopore");
        listPlatform.add("HiSeq X - Oxford Nanopore");
        listPlatform.add("NovaSeq 6000 - Oxford Nanopore");
        
        if (listPlatform.indexOf(platform) < 0) {
            return false;
        } else {
            return true;
        }
         */
        // 1.- Buscamos en la taba 'plataform' el nombre de la plataforma
        if (!selectPlataform.equals("---") && !selectPlataform.equals("")) {
            if (ejbPlataform.findPlataformByName(selectPlataform).size() > 0) {
                Plataform itemPlataform = ejbPlataform.findPlataformByName(selectPlataform).get(0);
                if (itemPlataform.getNote().equals("ILLUMINA")) {
                    System.out.println("Es individual y es illumina");
                    return true;
                } else {
                    return false;
                }
            } else {
                //Si no contiene nada, puede ser que sea una combinación de plataformas
                //Hacemos un ciclo de las plataformas para buscar coincidencias en la combinación
                System.out.println("Buscamos una combinación con illumina");
                for (Plataform itemPlataform : ejbFacade.findAllPlataforms()) {
                    if (selectPlataform.indexOf(itemPlataform.getPlataformName()) >= 0) {
                        if (itemPlataform.getNote().equals("OXFORD NANOPORE")) {
                            System.out.println("Correcto es una combinación y una de ellas es illumina");
                            return true;
                        }
                    }
                }
            }

        }
        return false;

    }

    public boolean isOxford(String selectPlataform) {
        /*
        //Constantes para validar tamaños de secuencias
        List<String> listPlatform = new ArrayList<>();
        //Llenado de arreglos para validar
        listPlatform.add("Oxford Nanopore");
        
        listPlatform.add("NextSeq 500 - Oxford Nanopore");
        listPlatform.add("MiSeq - Oxford Nanopore");
        listPlatform.add("HiSeq 2000/2500 - Oxford Nanopore");
        listPlatform.add("HiSeq X - Oxford Nanopore");
        listPlatform.add("NovaSeq 6000 - Oxford Nanopore");

        if (listPlatform.indexOf(platform) < 0) {
            return false;
        } else {
            return true;
        }*/

        // 1.- Buscamos en la taba 'plataform' el nombre de la plataforma
        if (!selectPlataform.equals("---") && !selectPlataform.equals("")) {
            System.out.println("Contenido de selectPlataform: " + selectPlataform);
            //Condicionamos si la lista no tiene registros
            if (ejbPlataform.findPlataformByName(selectPlataform).size() > 0) {
                //Si tiene registros entonces evaluamos si la plataforma seleccionada es Oxford
                System.out.println("Buscamos un individual oxford");
                Plataform itemPlataform = ejbPlataform.findPlataformByName(selectPlataform).get(0);
                if (itemPlataform.getNote().equals("OXFORD NANOPORE")) {
                    System.out.println("Correcto es individual y es oxfrod");
                    return true;
                } else {
                    return false;
                }
            } else {
                //Si no contiene nada, puede ser que sea una combinación de plataformas
                //Hacemos un ciclo de las plataformas para buscar coincidencias en la combinación
                System.out.println("Buscamos una combinación con oxford");
                for (Plataform itemPlataform : ejbFacade.findAllPlataforms()) {
                    if (selectPlataform.indexOf(itemPlataform.getPlataformName()) >= 0) {
                        if (itemPlataform.getNote().equals("OXFORD NANOPORE")) {
                            System.out.println("Correcto es una combinación y una de ellas es Oxford");
                            return true;
                        }
                    }
                }
            }

        }
        return false;

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

    private static class DatesComparator implements Comparator<Date> {

        public DatesComparator() {

        }

        @Override
        public int compare(Date datea, Date dateb) {
            return datea.compareTo(dateb);
        }
    }

    public String getRowClass(Sample item) {
        if (item == null || item.getComments() == null) {
            return ""; // Si el elemento o el comentario son nulos, no aplica estilo
        }

        String comments = item.getComments();

        if (comments.contains("SISBI")) {
            return "comentario-sisbi"; // Clase para comentarios de SISBI
        } else {
            return "comentario-otro-usuario"; // Clase para otros comentarios
        }
    }

    public String ligaPruebasFormatoMuestras() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress != "132.248.32.95" ? "http://www.uusmb.unam.mx/F02_PG03_Registro_oficial_de_Muestras.xlsx" : "http://132.248.32.95/F02_PG03_Registro_oficial_de_Muestras.xlsx";
    }
}
