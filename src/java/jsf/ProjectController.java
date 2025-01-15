package jsf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import jpa.entities.Project;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.ProjectFacade;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.BioinformaticsReports;
import jpa.entities.Comments;
import jpa.entities.Files;
import jpa.entities.Sample;
import jpa.entities.UserRole;
import jpa.entities.Pago;
import jpa.entities.ProyCotizaFacPagoLink;
import jpa.entities.Cotizacion;
import jpa.entities.Library;
import jpa.entities.LibraryRunLink;
import jpa.entities.QualityReports;
import jpa.entities.ReportProject;
import jpa.entities.Run;
import jpa.entities.UserProjectLink;

import jpa.entities.Users;
import jpa.session.BioinformaticAnalysisFacade;
import jpa.session.BioinformaticsReportsFacade;
import jpa.session.CommentsFacade;
import jpa.session.FilesFacade;
import jpa.session.SampleFacade;
import jpa.session.UserRoleFacade;
import jpa.session.PagoFacade;
import jpa.session.ProyCotizaFacPagoLinkFacade;
import jpa.session.CotizacionFacade;
import jpa.session.LibraryRunLinkFacade;
import jpa.session.QualityReportsFacade;
import jpa.session.RunFacade;
import jpa.session.SampleLibraryLinkFacade;
import jpa.session.UserProjectLinkFacade;
import static jsf.LibraryController.PATH;
import org.apache.commons.lang3.ArrayUtils;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.json.JSONObject;
import org.primefaces.model.SortMeta;
import org.primefaces.model.UploadedFile;

@Named("projectController")
@SessionScoped
public class ProjectController implements Serializable {

    private Project current;

    @EJB
    private jpa.session.ProjectFacade ejbFacade;
    @EJB
    private CommentsFacade commentFac;
    @EJB
    private UserRoleFacade roleFac;
    @EJB
    private SampleFacade sampleFac;
    @EJB
    private BioinformaticAnalysisFacade bioFac;
    @EJB
    private FilesFacade facade;
    @EJB
    private jpa.session.CotizacionFacade Ejbcot;
    @EJB
    private jpa.session.ProyCotizaFacPagoLinkFacade EjbCotLink;
    @EJB
    private jpa.session.ProjectFacade uplFacade;
    @EJB
    private jpa.session.QualityReportsFacade QRFacade;
    @EJB
    private jpa.session.RunFacade ejbRun;
    @EJB
    private jpa.session.BioinformaticsReportsFacade BioInfoFac;
    @EJB
    private jpa.session.SampleLibraryLinkFacade SampleLibraryLinkFacade;
    @EJB
    private jpa.session.LibraryRunLinkFacade LibraryRunFacade;
    @EJB
    //private UserProjectLinkFacade UserProjFac;
    private jpa.session.UserProjectLinkFacade UserProjFac;
    //leslie 23 agosto
    @EJB
    private jpa.session.LibraryFacade libFac;
    
    
    private String messageDialog;
    private String rangoSample;

    private PaginationHelper pagination;
    private PaginationHelper paginationAll;
    private PaginationHelper paginationSome;
    private PaginationHelper paginationSeach;
    private int selectedItemIndex;
    private int numberCol = 0;
    private boolean check = true;
    private boolean checkCol1 = false;
    private boolean checkCol2 = false;
    private boolean checkCol3 = false;
    private boolean checkCol4 = false;
    private boolean checkCol5 = false;
    private boolean checkAdd = false;
    private boolean checkSubs = true;
    private String idProject;
    private boolean checkTerms;
    private Project selectedProject;
    private List<Project> projects;
    private int opt;
    private Date date2;
    private boolean estatus;
    private boolean quotation;
    private boolean cot = false;
    private Project pay;
    private UserProjectLink role;
    private List<SortMeta> sortBy;
    private List<String> runs;
    private String url_Qr;
    private String type_Qr;
    private String pathWork = "";
    private String type_BR = "";
    public static final String URL = "http://www.uusmb.unam.mx/projectFiles";
    public static final String PATH = "/var/www/html/projectFiles/";
    private UploadedFile file = null;
    private String description;
    private Integer analysis = 0;
    private List<Sample> listSamples;
     //leslie 24 nov 2023
    private String typeProject;
    //leslie 23 agosto 2024
    //private List<Library> listLibraries;

   
    

    public String getTypeProject() {
        return typeProject;
    }

    public void setTypeProject(String typeProject) {
        this.typeProject = typeProject;
    }
    

    public String getRangoSample() {
        return rangoSample;
    }

    public void setRangoSample(String rangoSample) {
        this.rangoSample = rangoSample;
    }

    public String getMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(String messageDialog) {
        this.messageDialog = messageDialog;
    }

    public SampleLibraryLinkFacade getSampleLibraryLinkFacade() {
        return SampleLibraryLinkFacade;
    }

    public void setSampleLibraryLinkFacade(SampleLibraryLinkFacade SampleLibraryLinkFacade) {
        this.SampleLibraryLinkFacade = SampleLibraryLinkFacade;
    }

    public LibraryRunLinkFacade getLibraryRunFacade() {
        return LibraryRunFacade;
    }

    public void setLibraryRunFacade(LibraryRunLinkFacade LibraryRunFacade) {
        this.LibraryRunFacade = LibraryRunFacade;
    }

    public BioinformaticsReportsFacade getBioInfoFac() {
        return BioInfoFac;
    }

    public void setBioInfoFac(BioinformaticsReportsFacade BioInfoFac) {
        this.BioInfoFac = BioInfoFac;
    }

    public QualityReportsFacade getQRFacade() {
        return QRFacade;
    }

    public void setQRFacade(QualityReportsFacade QRFacade) {
        this.QRFacade = QRFacade;
    }

    public RunFacade getEjbRun() {
        return ejbRun;
    }

    public void setEjbRun(RunFacade ejbRun) {
        this.ejbRun = ejbRun;
    }

    public ProjectFacade getUplFacade() {
        return uplFacade;
    }

    public void setUplFacade(ProjectFacade uplFacade) {
        this.uplFacade = uplFacade;
    }

    public ProyCotizaFacPagoLinkFacade getEjbCotLink() {

        return EjbCotLink;
    }

    public void setEjbCotLink(ProyCotizaFacPagoLinkFacade EjbCotLink) {

        this.EjbCotLink = EjbCotLink;
    }

    public CotizacionFacade getEjbcot() {

        return Ejbcot;
    }

    public void setEjbcot(CotizacionFacade Ejbcot) {

        this.Ejbcot = Ejbcot;

    }

    public String validateBtnGenerarFastQC(){
        if (runs==null){
            return "false";
        } else {
            return runs.isEmpty()?"true":"false";
        }
    }

    public List<Sample> getListSamples() {
        return listSamples;
    }
    
    
  

    public String actualizar() {
        return "";
    }
    
   
     
    public void setListSamples(List<Sample> listSamples) {
        this.listSamples = listSamples;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAnalysis() {
        return analysis;
    }

    public void setAnalysis(Integer analysis) {
        this.analysis = analysis;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getUrl_Qr() {
        return url_Qr;
    }

    public void setUrl_Qr(String url_Qr) {
        this.url_Qr = url_Qr;
    }

    public String getType_Qr() {
        return type_Qr;
    }

    public void setType_Qr(String type_Qr) {
        this.type_Qr = type_Qr;
    }

    public String getPathWork() {
        return pathWork;
    }

    public void setPathWork(String pathWork) {
        this.pathWork = pathWork;
    }

    public String getType_BR() {
        return type_BR;
    }

    public void setType_BR(String type_BR) {
        this.type_BR = type_BR;
    }

    public List<String> getRuns() {
        return runs;
    }
    
    String re;
    public void setRunsEmpty(String state){
        re = state;
    }
    public String getRunsEmpty(){
        if (runs==null){
            return "false";
        }
        return runs.isEmpty()?"true": "false";
    }
    
    public void onRunSelect(org.primefaces.event.SelectEvent run){
        System.out.println("selev");
        setRunsEmpty(runs.isEmpty()?"true": "false");
    }

    public void onRunDeSelect(org.primefaces.event.UnselectEvent run){
        System.out.println("seldev");
        setRunsEmpty(runs.isEmpty()?"true": "false");
    }

    public void setRuns(List<String> runs) {
        this.runs = runs;
    }

    public List<SortMeta> getSortBy() {
        return sortBy;
    }

    public void setSortBy(List<SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    public List<Sample> getSamplesByProject() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        return sampleFac.findSamplesByProject(proj);
    }

    public Project getPay() {

        return pay;

    }

    public void setPay(Project pay) {

        this.pay = pay;

    }

    @EJB
    private jpa.session.PagoFacade ejbPago;

    public PagoFacade getEjbPago() {

        return ejbPago;

    }

    public void setEjbPago(PagoFacade ejbPago) {

        this.ejbPago = ejbPago;

    }

    public boolean isCot() {
        return cot;
    }

    public void setCot(boolean cot) {
        this.cot = cot;
    }
    private String quot;

    public String getQuot() {
        return quot;
    }

    public void setQuot(String quot) {
        this.quot = quot;
    }

    public boolean isQuotation() {
        return quotation;
    }

    public void setQuotation(boolean quotation) {
        this.quotation = quotation;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public int getOpt() {
        return opt;
    }

    public void setOpt(int opt) {
        this.opt = opt;
    }

    public Project getSelectedProject() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", selectedProject);
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", selectedProject);
        this.selectedProject = selectedProject;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public boolean isCheckTerms() {
        return checkTerms;
    }

    public void setCheckTerms(boolean checkTerms) {
        this.checkTerms = checkTerms;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheckAdd() {
        return checkAdd;
    }

    public void setCheckAdd(boolean checkAdd) {
        this.checkAdd = checkAdd;
    }

    public boolean isCheckSubs() {
        return checkSubs;
    }

    public void setCheckSubs(boolean checkSubs) {
        this.checkSubs = checkSubs;
    }

    public boolean isCheckCol1() {
        return checkCol1;
    }

    public void setCheckCol1(boolean checkCol1) {
        this.checkCol1 = checkCol1;
    }

    public boolean isCheckCol2() {
        return checkCol2;
    }

    public void setCheckCol2(boolean checkCol2) {
        this.checkCol2 = checkCol2;
    }

    public boolean isCheckCol4() {
        return checkCol4;
    }

    public void setCheckCol4(boolean checkCol4) {
        this.checkCol4 = checkCol4;
    }

    public boolean isCheckCol5() {
        return checkCol5;
    }

    public void setCheckCol5(boolean checkCol5) {
        this.checkCol5 = checkCol5;
    }

    public boolean isCheckCol3() {
        return checkCol3;
    }

    public void setCheckCol3(boolean checkCol3) {
        this.checkCol3 = checkCol3;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<UserProjectLink> findUsers() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

        return uplFacade.findUserByProject(pj);
    }

    public void addMessage() {
        String summary = check ? "Checked" : "Unchecked";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }

    public String haveProjects() {

        if (getItems().isEmpty()) {
            return "No hay proyectos registrados";
        } else {
            return "";
        }

    }

    public void addNumber(ActionEvent e) {
        numberCol++;
        if (numberCol >= 5) {
            checkAdd = true;
        } else {
            checkSubs = false;
        }

    }

    public void subsNumber(ActionEvent e) {
        numberCol--;
        if (numberCol <= 0) {
            checkSubs = true;
        } else {
            checkAdd = false;
        }

    }

    @PostConstruct
    public void init() {
        projects = new ArrayList<>();
        projects = ejbFacade.findAll();

    }

    public List<Project> findOrderProjects() {
        return getFacade().findAllProjects();
    }

    public boolean checkEstatusProj() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        if (proj.getStatus() == null) {
            estatus = false;
            return estatus;
        } else {
            if (proj.getStatus().equals("Terminado") || proj.getStatus().equals("Cancelado")) {
                estatus = true;
                return estatus;
            } else {
                estatus = false;
                return estatus;
            }
        }
    }

    /*
    Este metodo verifica si existe una cotizacion registrada en un proyecto y si el usuario que esta en la sesion actual es Responsable o colaborador del proyecto
    
    @author: Fernando Betanzos
    
    @param:
    
    @Return: Quuotation=True/False
    
    @version:17/12/19
     */
    public boolean checkQuatationProj() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<UserRole> role = roleFac.findRangeUsersProj(proj.getIdProject());
        List<Files> list = facade.findAll();

        for (Files files : list) {
            if ((files.getIdProject().equals(proj)) && (files.getFileType().equals("Cotizacion") || files.getFileType().equals("Pago"))) {
                //System.out.println(files.getFileName());
                quotation = false;
                return quotation;
            }
        }//Verifica si existe un archivo ligado al proyecto, ya sea una cotizacion o comprobante de pago. Si existe cualquiera de estos dos tipo de archivos, no le mostrara el mensaje en pantalla al usuario.

        for (UserRole user : role) {
            System.out.println("usuario: " + user.getFirstName() + " Rol: " + user.getRole());

            if ((user.getUserName().equals(us.getUserName())) && (user.getRole().equals("Responsable") || user.getRole().equals("Colaborador"))) {
                System.out.println("Colabora o es responsable del proyecto");

                if (proj.getQuotationNumber() == null) {
                    System.out.println("No hay cotizacion registrada");
                    quotation = true;
                } else {
                    System.out.println("Cotizacion registrada o cotizacion solicitada");
                    quotation = false;
                }
            } else {
                System.out.println("no Colabora ni es responsable del proyecto");
                quotation = false;
            }
        }
        return quotation;
    }

    public void initializeProject() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("project", null);
    }

    //Método para redirigir a la vista ViewProject.xhtml
    public void redirectViewProject(Project itemProject) {
        selectedProject = itemProject;
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", selectedProject);

        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
            //context.getExternalContext().redirect("../project/ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Método para redirigir a la vista ViewProject.xhtml recibiendo un parámetro de tipo String
    public void redirectViewProjectString(String idProject) {
        selectedProject = ejbFacade.find(idProject);
        //selectedProject = itemProject;
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", selectedProject);

        try {
            //context.getExternalContext().redirect("ViewProject.xhtml");
            context.getExternalContext().redirect("../project/ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    Este metodo actualiza el campo de cotiazacion a "Cotizacion solicitada" cuadno un proyecto no cuenta con una, y el usuario resposanble
    solicita la cotizacion, enviando un correo a las partes interesadas(Usuario Responsable o Coolaborador,Miembros de la UUSMB)
    
    @author: Fernando Betanzos
    
    @param:
    
    @Return: 
    
    @version:17/12/19
     */
    public void updateQuotationProject() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        Sample sm = (Sample) context.getExternalContext().getSessionMap().get("sample");

        List<Sample> samples = sampleFac.findSamples(proj.getIdProject());
        List<BioinformaticAnalysis> root = bioFac.findBioanSample(sm);
        String Type = "";

        if (us.getIdDependency().getAcronym().equals("IBT") || us.getIdDependency().getAcronym().equals("LIIGH") || us.getIdDependency().getAcronym().equals("UNAM")) {
            Type = us.getIdDependency().getType() + "-" + "UNAM";
        } else {
            Type = us.getIdDependency().getType() + "-" + us.getIdDependency().getAcronym();
        }

        String User = us.getFirstName() + " " + us.getPLastName() + " " + us.getMLastName();

        String DNA = "";
        String RNA = "";
        String PlatDNA = "";
        String PlatRNA = "";
        for (Sample sample : samples) {
            if (sample.getType().toLowerCase().equals("dna genómico") || sample.getType().toLowerCase().equals("dna genomico") || sample.getType().toLowerCase().equals("dna metagenomico") || sample.getType().toLowerCase().equals("amplicon 16s") || sample.getType().toLowerCase().equals("amplicon its")) {
                DNA = sample.getType();
                PlatDNA = sample.getSamplePlataform();
            } else {
                RNA = sample.getType();
                PlatRNA = sample.getSamplePlataform();
            }
        }
        EmailController ec = new EmailController();
        ec.SendMailQuotationRequest(proj.getProjectName(), samples, proj.getIdProject(), root, DNA, RNA, PlatDNA, PlatRNA, Type, User, us.getEmail());
        selectedProject.setQuotationNumber("Cotización solicitada");
        getFacade().edit(selectedProject);

        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
        } catch (IOException ex) {

            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    Este metodo ejecuta  un comando en linux (archivo de python) para obtener la inofmacion de una cotizacion consultando
    esta informacion en Sibiotec
    
    @author: Fernando Betanzos
    
    @param:
    
    @Return: 
    
    @version:17/12/19
     */
    public void ExecCommand() {
        FacesContext context = FacesContext.getCurrentInstance();
        String s = null;

        try {

            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("python3 /var/www/html/get_by_no_v2.py " + quot);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {

                if (s.replaceAll("\"", "").equals("{}")) {
                    System.out.println("no existe esa cotizacion en sibiotec");

                    context.addMessage(null, new FacesMessage("Ocurrio un error", "Por favor verifique el numero de cotizacion ya que en nuestros registros no existe ese numero de cotizacion"));
                } else {

                    String cadena = s.replaceAll("\"", "").replaceAll("\\\\", "").replaceAll("/", "");
                    System.out.println(cadena);
                    JSONObject json = new JSONObject(cadena);
                    System.out.println(json);
                    String Cot = json.getJSONObject(quot.substring(5)).getString("Estatus");
                    selectedProject.setQuotationNumber(quot + " - " + Cot.replace("u00f3", "ó").substring(4));
                    getFacade().edit(selectedProject);
                    context.getExternalContext().redirect("ViewProject.xhtml");
                    /*JSONObject json= new JSONObject(cadena.replaceAll("\"", "").replaceAll("\\\\", ""));
               
               System.out.println("existe una cotizacion");
               
               selectedProject.setQuotationNumber(quot+" - "+Cot.replace("u00f3", "ó").substring(4));
               getFacade().edit(selectedProject);*/

                    //context.getExternalContext().redirect("ViewProject.xhtml");
                }

            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println("no existe esa cotizacion en sibiotec");
                context.addMessage(null, new FacesMessage("Ocurrio un error", "Por favor verifique el numero de cotizacion ya que en nuestros registros no existe ese numero de cotizacion"));
                break;
            }

        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            context.addMessage(null, new FacesMessage("Ocurrio un error", "Intentelo mas tarde por favor"));
            e.printStackTrace();

        }

    }

    public Project checkPayments() {

        FacesContext context = FacesContext.getCurrentInstance();

        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        List<ProyCotizaFacPagoLink> findPayments = EjbCotLink.findPayment(proj);

        for (ProyCotizaFacPagoLink find : findPayments) {

            System.out.println("id del pago: " + find.getPagoId());

            //pay=Integer.parseInt(find.getPagoId().toString().replaceAll("[\\ [\\],A-z=.]]", ""));
            pay = find.getIdProject();

        }

        return pay;

    }

    public List<Pago> findPayment() {

        //checkPayments();
        FacesContext context = FacesContext.getCurrentInstance();

        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        return ejbPago.findPaymentByproject(proj);

    }

    public void renderCol() {

        switch (numberCol) {
            case 1:
                checkCol1 = true;
                checkCol2 = false;
                checkCol3 = false;
                checkCol4 = false;
                checkCol5 = false;

                break;
            case 2:
                checkCol1 = true;
                checkCol2 = true;
                checkCol3 = false;
                checkCol4 = false;
                checkCol5 = false;
                break;
            case 3:
                checkCol1 = true;
                checkCol2 = true;
                checkCol3 = true;
                checkCol4 = false;
                checkCol5 = false;
                break;
            case 4:
                checkCol1 = true;
                checkCol2 = true;
                checkCol3 = true;
                checkCol4 = true;
                checkCol5 = false;
                break;
            case 5:
                checkCol1 = true;
                checkCol2 = true;
                checkCol3 = true;
                checkCol4 = true;
                checkCol5 = true;
                break;
            case 0:
                checkCol1 = false;
                checkCol2 = false;
                checkCol3 = false;
                break;
            default:
                break;
        }

    }

    public int getNumberCol() {
        return numberCol;
    }

    public void setNumberCol(int numberCol) {
        this.numberCol = numberCol;
    }

    public ProjectController() {
    }

    public Project getSelected() {
        if (current == null) {
            current = new Project();
            selectedItemIndex = -1;
        }
        return current;
    }

    public Project getProjectId() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        current = proj;
        return current;
    }

    private ProjectFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

        if (pagination == null) {
            pagination = new PaginationHelper(20) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRangeProUsers(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, us));
                }
            };
        }
        return pagination;
    }

    public PaginationHelper getPaginationPU() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("UserProject");

        paginationSome = new PaginationHelper(20) {

            @Override
            public int getItemsCount() {
                return getFacade().count();
            }

            @Override
            public DataModel createPageDataModel() {
                return new ListDataModel(getFacade().findRangeProUsers(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, us));
            }
        };

        return paginationSome;
    }

    public PaginationHelper getPaginationSearched() {

        if (paginationSeach == null) {
            paginationSeach = new PaginationHelper(20) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findProjectByIdRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, idProject));
                }
            };

        }
        return paginationSeach;
    }

    public PaginationHelper getPaginationAll() {

        if (paginationAll == null) {
            paginationAll = new PaginationHelper(100) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return paginationAll;
    }

    public void prepareList() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (us.getUserType().equals("Usuario")) {
            try {
                context.getExternalContext().redirect("List.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            try {
                context.getExternalContext().redirect("ListAll.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void prepareProjectList() {

        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
//             Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
//        if (us.getUserType().equals("Usuario")) {
//               try {
//            context.getExternalContext().redirect("List.xhtml");
//        } catch (IOException ex) {
//            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//            
//        }else{
//        
//               try {
//            context.getExternalContext().redirect("../users/ListAll.xhtml");
//        } catch (IOException ex) {
//            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//          
//        }

    }

    public void Admin2View() {

        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("View.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void prepareIndex() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (us.getUserType().equals("Usuario")) {
            try {
                context.getExternalContext().redirect("../Principal/Welcome.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            try {
                context.getExternalContext().redirect("../Principal/opciones.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void sendWarning() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

        List<UserRole> url = roleFac.findRangeUsersProj(pj.getIdProject());
        List<String> emails = new ArrayList<>();

        for (UserRole user : url) {

            emails.add(user.getEmail());
            System.out.println(user.getEmail());
        }

        EmailController ec = new EmailController();
        ec.sendWarningProject(emails, opt, pj.getProjectName());

    }

    public void prepareViewUser(Project pj) {

        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", pj);
        Project p = (Project) context.getExternalContext().getSessionMap().get("project");
        selectedProject = pj;
        try {
            context.getExternalContext().redirect("../project/View.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String prepareCreate() {
        current = new Project();
        selectedItemIndex = -1;

        return "Create";
    }
    
    public void recordComment(String user) {

    }

    public void create() {
        System.out.println(checkTerms);

        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            String[] tiempo = timestamp.toString().split("-");
            String[] dia = tiempo[2].split(" ");
            String[] min = dia[1].split(":");
            String[] segs = min[2].split("\\.");
            String date = tiempo[0] + "_" + tiempo[1] + "_" + dia[0] + "_" + min[0] + "_" + min[1] + "_" + segs[0];
            FacesContext context = FacesContext.getCurrentInstance();
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            current.setIdProject("Project_" + us.getUserName() + "_" + date);

            current.setStatus("Registrado");
            current.setRequestDate(timestamp);
            //leslie24 nov2023
            current.setType_project("Pago efectivo");

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("project", current);
            current = new Project();

            EmailController ec = new EmailController();
            ec.sendNewProjectManagerEmail(us.getUserName(), role.getRole());
            System.out.println("Manda correo");

        } catch (Exception e) {
            System.out.println("EXcepcion: " + e + " : " + e.getLocalizedMessage());

        }

    }

    public void prepareEdit() {

        FacesContext context = FacesContext.getCurrentInstance();
        try {

            context.getExternalContext().redirect("../project/Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void prepareSomeEdit() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if ("Admin".equals(us.getUserType())) {
            try {

                context.getExternalContext().redirect("searchedEdit.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {

                context.getExternalContext().redirect("Edit.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void prepareSomeEdit2() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if ("Admin".equals(us.getUserType())) {
            try {

                context.getExternalContext().redirect("AdminEdit.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {

                context.getExternalContext().redirect("Edit.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void prepareSomeEdit3() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if ("Admin".equals(us.getUserType())) {
            try {

                context.getExternalContext().redirect("adminEdit.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {

                context.getExternalContext().redirect("Edit.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void prepareView() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("../project/View.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
        12/12/2024  Juan Antonio Villalba Luna
        Se tiene una area de oportunidad refactirzando estos metodos
        
        - prepareUView
        - openViewDetailProject
        
    
    */
    
    public void prepareUView() {

        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", selectedProject);
        try {
            context.getExternalContext().redirect("../project/ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepareViewProject() {

        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        context.getExternalContext().getSessionMap().put("project", proj);
        try {
            if (proj.getQuotationNumber() == null || proj.getQuotationNumber().equals("") || proj.getQuotationNumber().equals("Cotización solicitada")) {
                context.getExternalContext().redirect("../project/ViewProject.xhtml");
            } else {

                context.getExternalContext().redirect("../project/ViewProject.xhtml");
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepareViewSearch() {

        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", selectedProject);

        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String projectName() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

        return pj.getProjectName();

    }

    public String userProjectName() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("UserProject");

        return us.getUserName();

    }

    public void prepareSample() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

        try {
            context.getExternalContext().redirect("../sample/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void prepareSampleAdmin() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            context.getExternalContext().redirect("../sample/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepareSampleList() {

        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().getSessionMap().put("project", current);
        try {
            context.getExternalContext().redirect("../sample/SampleList.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepareSampleAll() {

        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().getSessionMap().put("project", current);
        try {
            context.getExternalContext().redirect("../sample/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepareSampleSome() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            context.getExternalContext().redirect("../sample/List.xhtml");
        } catch (IOException ex) {

            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update() {

        getFacade().edit(selectedProject);
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
        } catch (IOException ex) {

            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean comparationStatus(Sample s) {

        if (s.getStatus().equals("Recibida") || s.getStatus().equals("Registrada")
                || s.getStatus().equals("En analisis de calidad") || s.getStatus().equals("En construccion de biblioteca")
                || s.getStatus().equals("Construccion ineficiente") || s.getStatus().equals("En espera para secuenciacion")) {
            return true;
        }

        return false;

    }

    public boolean renderCancelProject() {

        List<Sample> listSamples = sampleFac.findSampProj2(getProjectId());

        boolean result = false;
        for (Sample sample : listSamples) {

            if (comparationStatus(sample)) {
                result = false;

            } else {
                result = true;
                break;
            }
        }
        return result;

    }

    
    public void updateStatus2(int opt) {
        //Termina o cancela el proyecto. Si el parámetro opt es 1=termina, 2=cancela
        boolean statusproChange;
        if (opt == 1) {
            selectedProject.setStatus("Terminado");
            statusproChange = true;
            
            List<Sample> listSamples = sampleFac.findSampProj2(selectedProject);
            for (Sample sample : listSamples) {
                if (comparationStatus(sample)) {
                    System.out.println("fecha de entrega: " + date2);
                    sample.setDeliveryDate(date2);
                    sampleFac.edit(sample);

                }
            }

        }
        else {
            if(opt==3){
                selectedProject.setStatus("En proceso");
                statusproChange = false;
            }else
                {//inicia else
                selectedProject.setStatus("Cancelado");
                statusproChange = false;
                } //fin else
        }
        
        getFacade().edit(selectedProject);
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        
        if (statusproChange) {

            List<UserProjectLink> uplc = UserProjFac.findAll();
            List<String> emails = new ArrayList<>();
            for (UserProjectLink userProjectLink : uplc) {
                if (pj.getIdProject().equals(userProjectLink.getProject().getIdProject())) {
                    emails.add(userProjectLink.getUsers().getEmail());
                }
            }
            EmailController ec = new EmailController();
            System.out.println("Manda email de cambio de estatus del proyecto");
            ec.sendUpdateStatusProjEmail(emails, pj.getIdProject());

        }

        Comments commentsProject = new Comments();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        commentsProject.setComment("El usuario " + us.getUserName() + " ha cambiado el estatus del proyecto a \"" + selectedProject.getStatus() + "\"");
        commentsProject.setIdType(selectedProject.getIdProject());
        commentsProject.setType("Project");
        commentsProject.setUserName("SISBI");
        commentsProject.setCommentDate(timestamp);

        commentFac.createComment(commentsProject);
        
       
        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
        } catch (IOException ex) {

            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateStatus(int opt) {

        if (opt == 1) {
            getProjectId().setStatus("Terminado");
            List<Sample> listSamples = sampleFac.findSampProj2(getProjectId());
            for (Sample sample : listSamples) {

                if (sample.getStatus().equals("Recibida") || sample.getStatus().equals("Registrada")
                        || sample.getStatus().equals("En analisis de calidad") || sample.getStatus().equals("En construccion de biblioteca")
                        || sample.getStatus().equals("Construccion ineficiente") || sample.getStatus().equals("En espera para secuenciacion")) {

                    sample.setStatus("Secuenciada");
                    System.out.println("fecha de entrega: " + date2);
                    //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    sample.setDeliveryDate(date2);
                    sampleFac.edit(sample);

                }
            }

        } else {

            getProjectId().setStatus("Cancelado");

        }
        getFacade().edit(getProjectId());
        FacesContext context = FacesContext.getCurrentInstance();
        Comments commentsProject = new Comments();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        commentsProject.setComment("El usuario " + us.getUserName() + " ha cambiado el estatus del proyecto a \"" + getProjectId().getStatus() + "\"");//TODO revisar que no haya còdigo que dependa de "el usuario a cambiado"
        commentsProject.setIdType(getProjectId().getIdProject());
        commentsProject.setType("Project");
        commentsProject.setUserName("SISBI");
        commentsProject.setCommentDate(timestamp);

        commentFac.createComment(commentsProject);

        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
        } catch (IOException ex) {

            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public List<Project> getItems() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Project> p = ejbFacade.findProUsers(us);
        return p;
    }

    public List<Project> getItemsAll() {
        List<Project> p = ejbFacade.findAll();

        return p;
    }

    public List<Project> getSomeItems() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("UserProject");

        return ejbFacade.findProUsers(us);

    }

    public List<Project> getSearchedItems() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        ArrayList<Project> userProj = new ArrayList<>();
        List<Project> p = ejbFacade.findProjectById(idProject);

        if (us.getUserType().equals("Admin")) {

            return p;

        } else {
            List<Project> userP = ejbFacade.findProUsers(us);

            for (Project project : userP) {

                if (project.getIdProject().toLowerCase().contains(idProject.toLowerCase()) || project.getProjectName().toLowerCase().contains(idProject.toLowerCase())) {

                    userProj.add(project);
                }

            }

            return userProj;

        }

    }

    //Método para buscar por nombre de proyecto
    public List<Project> getFindProjects() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        ArrayList<Project> userProj = new ArrayList<>();
        List<Project> p = ejbFacade.findProjectById(idProject);

        if (us.getUserType().equals("Admin")) {

            return p;

        } else {
            List<Project> userP = ejbFacade.findProUsers(us);

            for (Project project : userP) {

                if (project.getIdProject().toLowerCase().contains(idProject.toLowerCase()) || project.getProjectName().toLowerCase().contains(idProject.toLowerCase())) {

                    userProj.add(project);
                }

            }

            return userProj;

        }

    }
    
    
    /**/
    //Método para buscar ID de proyecto    
    @RequestScoped
    private String param1;        

    /**
     *
     * @return
     */
    public List<Project> getParamProjectId(){                
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        
        idProject = params.get("project");
        ArrayList<Project> userProj = new ArrayList<>();
        List<Project> p = ejbFacade.findProjectById(idProject);
                
        if (us.getUserType().equals("Admin")) {

            return p;

        } else {
            List<Project> userP = ejbFacade.findProUsers(us);

            for (Project project : userP) {

                if (project.getIdProject().toLowerCase().contains(idProject.toLowerCase()) || project.getProjectName().toLowerCase().contains(idProject.toLowerCase())) {

                    userProj.add(project);
                }

            }

            return userProj;

        }
    }        
    
    
    
    /**/
    //Método para buscar ID de proyecto    
    @RequestScoped
    private String param;        

    /**
     *  Viernes 10/ene/2025 
     *  Se crea nuevo metodo a partir de getParamProjectId
     * @return
     */
    public List<Project> getQueryParamProjectId(){                
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        
        idProject = params.get("project");
        ArrayList<Project> userProj = new ArrayList<>();
        List<Project> p = ejbFacade.findProjectById(idProject);
        
        //  TODO:    Mover a una funcion
        //  10/ene/2025 Juan Antonio Villalba Luna
        //  >>>>>
        setSelectedProject(p.get(0));
        //  <<<<<
        if (us.getUserType().equals("Admin")) {

            return p;

        } else {
            List<Project> userP = ejbFacade.findProUsers(us);

            for (Project project : userP) {

                if (project.getIdProject().toLowerCase().contains(idProject.toLowerCase()) || project.getProjectName().toLowerCase().contains(idProject.toLowerCase())) {

                    userProj.add(project);
                }

            }

            return userProj;

        }
    }        
    
    /**/
    
    public void cleanFindIdProject() {
        idProject = null;
    }

    public void existProjectUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Project> p;

        p = ejbFacade.findProUsers(us);

        if (idProject.equals("") | idProject.isEmpty()) {

        } else {
            if (p.isEmpty()) {
                JsfUtil.addErrorMessage("El Proyecto que estas buscando no existe");

            } else {
                try {
                    //3
                    context.getExternalContext().redirect("../project/SearchProject.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    public void existProject() {
        List<Project> p;
        System.out.println("Llega a llenar la lista");
        idProject = idProject.trim();
        p = getSearchedItems();
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext dialog = RequestContext.getCurrentInstance();

        if (!idProject.equals("") | !idProject.isEmpty()) {
            if (p.isEmpty()) {
                JsfUtil.addErrorMessage("No se ha dado de alta un proyecto "
                        + "con los criterios de busqueda ingresados. Inténtelo de nuevo");

                //Agregamos el mensaje de que no encuentra el proyecto
                messageDialog = "No se encontraron coincidencias";
                dialog.execute("PF('modularDialog').show();");

            } else {
                try {
                    //3                    
                    context.getExternalContext().redirect("../project/SearchProjectL.xhtml");

                } catch (IOException ex) {
                    Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Project getProject(java.lang.String id) {
        return ejbFacade.find(id);
    }
    

    @FacesConverter(forClass = Project.class)
    public static class ProjectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProjectController controller = (ProjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "projectController");
            return controller.getProject(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Project) {
                Project o = (Project) object;
                return getStringKey(o.getIdProject());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Project.class.getName());
            }
        }

    }

    public List<QualityReports> listQRGral(Run id) {
        FacesContext context = FacesContext.getCurrentInstance();

        return QRFacade.findQRB(id);

    }

    public List<QualityReports> listQR(String id) {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        List<Run> run = ejbRun.findRun(id);
        Run idRn = null;
        for (Run rn : run) {
            idRn = rn;
        }
        return QRFacade.findQRByProject(proj, idRn);

    }

    public List<BioinformaticsReports> listBR(String id) {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        List<Run> run = ejbRun.findRun(id);
        Run idRn = null;
        for (Run rn : run) {
            idRn = rn;
        }
        return BioInfoFac.findBRByProject(proj, idRn);

    }

    public List<String> findRunbyProject() throws ClassNotFoundException, SQLException {

        //System.out.println("entra al metodo findRun ");
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        String url = "jdbc:postgresql://localhost:5432/sisbi_db";
        String usuario = "sisbi";
        String pswd = "SISBI123@";
        ////////conexion a la BD///
        Class.forName("org.postgresql.Driver");
        Connection conexion = DriverManager.getConnection(url, usuario, pswd);
        java.sql.Statement st = conexion.createStatement();

        List<Sample> sm = sampleFac.findSampProj2(proj);
        List<String> rn = new ArrayList<>();
        try {
            for (Sample sample : sm) {

                List<Library> SLL = SampleLibraryLinkFacade.getLibraries(sample);

                for (Library lib : SLL) {

                    List<LibraryRunLink> findRuns = LibraryRunFacade.findRunByLibrary(lib.getIdLibrary());
                    String consulta = "select run_name from run where id_run in(select id_run from library_run_link where id_library=" + lib.getIdLibrary() + ");";
                    ResultSet rs = st.executeQuery(consulta);
                    while (rs.next()) {
                        //System.out.println("Corrida:+ " + rs.getString(1));
                        rn.add(rs.getString(1));
                    }

                }
            }
            st.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        List<String> DistinctRun = rn.stream().distinct().collect(Collectors.toList());
        return DistinctRun;

    }

    public List<String> findLibsByRun(String run) {

        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        List<String> lib = new ArrayList<>();

        try {
            if (runs != null) {
                System.out.println("es diferente de null");

                List<Run> findRn = ejbRun.findRun(run);
                for (Run rn : findRn) {
                    List<Library> corrida = LibraryRunFacade.findLibraryByRun(rn);

                    for (Library libs : corrida) {

                        List<Sample> findSamples = sampleFac.getSamples(libs, proj);
                        for (Sample sm : findSamples) {
                            List<Library> findLibsBySample = sampleFac.getLibraries(sm);

                            for (Library lb : findLibsBySample) {
                                System.out.println("Librerias:" + lb.getLibraryName());
                                lib.add(lb.getLibraryName());

                            }

                        }

                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Ocurrio un error" + e);
        } finally {

        }

        return lib;
    }

    public void createReport() {
        System.out.println("Entra al metodo createQR");
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        RequestContext rc = RequestContext.getCurrentInstance();
        for (String rn : runs) {
            List<Run> run = ejbRun.findRun(rn);
            Run idRn = null;
            for (Run Rn : run) {
                idRn = Rn;
            }

            QualityReports qr = new QualityReports();
            qr.setIdProject(proj);
            qr.setIdRun(idRn);
            qr.setSampleSheetFile(idRn.getSampleSheetName());
            qr.setUrlQualityReport(url_Qr);
            qr.setType("Project");
            QRFacade.create(qr);

        }
        rc.execute("PF('QualityDialog').hide();");
    }

    public void uploadFile(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        System.out.println("Uploading file for "+us.getUserName());
        String urlFile;
        boolean created;
        file = event.getFile();

        String url = PATH + "/" + pj.getIdProject().replaceAll(":", "_");

        File folder = new File(url);
        if (!folder.exists()) {
            created=folder.mkdir();
            if (!created) {
                String message="Folder "+url+" could not be created";
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, message);
            }
        }
        try {
            File destfile = new File(url, file.getFileName());
            if (destfile.exists()){
                Logger.getLogger(UsersController.class.getName()).log(Level.INFO, "Deleting the file {0} to replace it with the upload", destfile.toPath());
                java.nio.file.Files.delete(destfile.toPath());
            } else {
                Logger.getLogger(UsersController.class.getName()).log(Level.INFO, "Uploading the file into {0}", folder.toPath());
            }
            java.nio.file.Files.copy(file.getInputstream(), destfile.toPath());
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlFile = URL + "/" + pj.getIdProject().replaceAll(":", "_") + "/" + file.getFileName();

        System.out.println(event.getFile().getFileName());
        FacesMessage message = new FacesMessage("Reporte cargado satisfactoriamente", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }

    public void uploadSampleQC(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        //String run_names_suffix = String.join("_and_", runs);
        System.out.println("Uploading file for "+us.getUserName());
        file = event.getFile();

        String path = PathFiles.DirectoryreportDocuments + "F01_PT05_LNATCG_" + pj.getIdProject().replaceAll(":", "_")+".pdf";

        File download_file = new File(path);
        /*if (!folder.exists()) {
            created=folder.mkdir();
            if (!created) {
                String message="Folder "+url+" could not be created";
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, message);
            }
        }*/
        try {
            if (download_file.exists()){
                Logger.getLogger(UsersController.class.getName()).log(Level.INFO, "Deleting the file {0} to replace it with the upload", download_file.toPath());
                java.nio.file.Files.delete(download_file.toPath());
            } else {
                Logger.getLogger(UsersController.class.getName()).log(Level.INFO, "Uploading the file into {0}", download_file.toPath());
            }
            java.nio.file.Files.copy(file.getInputstream(), download_file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(event.getFile().getFileName());
        FacesMessage message = new FacesMessage("Reporte FastQC cargado satisfactoriamente", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void uploadFastQC(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        String run_names_suffix = String.join("_and_", runs);
        System.out.println("Uploading file for "+us.getUserName());
        file = event.getFile();

        String path = PathFiles.DirectoryreportDocuments + "F02_PT05_LNATCG_" + pj.getIdProject().replaceAll(":", "_")+"_"+run_names_suffix+".pdf";

        File download_file = new File(path);
        try {
            if (download_file.exists()){
                Logger.getLogger(UsersController.class.getName()).log(Level.INFO, "Deleting the file {0} to replace it with the upload", download_file.toPath());
                java.nio.file.Files.delete(download_file.toPath());
            } else {
                Logger.getLogger(UsersController.class.getName()).log(Level.INFO, "Uploading the file into {0}", download_file.toPath());
            }
            java.nio.file.Files.copy(file.getInputstream(), download_file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(event.getFile().getFileName());
        FacesMessage message = new FacesMessage("Reporte FastQC cargado satisfactoriamente", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void BionInfoRep(ReportProjectController reportProjectController) {
        System.out.println("Entra al metodo BionInfoRep");

        FacesContext context = FacesContext.getCurrentInstance();
        Map sessmap = context.getExternalContext().getSessionMap();
        Project proj = (Project) sessmap.get("project");
        Users us = (Users) sessmap.get("usuario");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        RequestContext rc = RequestContext.getCurrentInstance();
        Integer id_bio = 0;
        List<BioinformaticsReports> bio = BioInfoFac.findAll();
        if (bio.isEmpty()) {
            id_bio = 1;
        } else {
            for (BioinformaticsReports br : bio) {
                id_bio = br.getBioinformaticsReportsPK().getIdBioinformaticsReports() + 1;
            }
        }
        for (String rn : runs) {
            List<Run> run = ejbRun.findRun(rn);
            Run idRn = null;
            for (Run Rn : run) {
                idRn = Rn;
            }
            String full_url = URL + "/" + proj.getIdProject().replaceAll(":", "_") + "/" + file.getFileName();
            BioinformaticsReports BR = new BioinformaticsReports();
            BR.setBioinformaticsReportsPK(new jpa.entities.BioinformaticsReportsPK());
            BR.getBioinformaticsReportsPK().setIdBioinformaticsReports(id_bio);
            BR.getBioinformaticsReportsPK().setIdAnalysis(analysis);
            BR.setIdProject(proj);
            BR.setIdRun(idRn);
            BR.setOwner(us.getUserName());
            BR.setPdfReport(full_url);
            BR.setType("");
            BR.setPathWork(pathWork);
            BR.setCreateDate(timestamp);
            BR.setDescription(description);
            BioInfoFac.create(BR);
        }
        rc.execute("PF('BRDialog').hide();");
    }

    public String formatLinkReport(String link) {

        if (link.indexOf("https://") < 0 && link.indexOf("http://") < 0) {
            link = "https://" + link;
        }

        return link;

    }

    // método para redireccionar a redirectSampleList y cargar las muestras
    public void redirectSampleList() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        try {
            context.getExternalContext().redirect("../sample/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

        listSamples = sampleFac.findSamplesByProject(proj);
    }

      // método para redireccionar a redirectLibList y cargar las bibliotecas
    public void redirectLibList() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        try {
            context.getExternalContext().redirect("../library/Updatebyproject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

       // listSamples = sampleFac.findSamplesByProject(proj);
    }
    


    
    public void redirectSimpleViewProject() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            context.getExternalContext().redirect("../project/ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Método para seleccionar por rango de muestras
    public void imprimirRango() {

        FacesContext context = FacesContext.getCurrentInstance();
        Project currentProject = (Project) context.getExternalContext().getSessionMap().get("project");
        if (rangoSample.equals("") || rangoSample == null) {
            //flagList = 0;
            listSamples = sampleFac.findSamplesByProject(currentProject);
        }
        String rangoSampleValue = rangoSample;
        rangoSample = null;
        System.out.println("Rango a Validar:" + rangoSampleValue);
        //temp para verificar si el anterior es mayor al siguiente
        int temp = 0;
        int contflag = 0;
        int flagList = 0;

        //Rango Validado
        String rangoValido = "";
        String r1 = "";
        String r2 = "";
        String idProy = currentProject.getIdProject();
        boolean flag2 = true;

        //Listas Para Mostrar
        List<Sample> rangoSampleVal;
        rangoSampleVal = new ArrayList();
        List<Sample> listaCom = new ArrayList();
        List<Sample> listaGui = new ArrayList();
        List<Sample> listaRango = new ArrayList();
        //Bandera Para validar el rango.
        boolean mayor = false;
        String pattern = "(\\d+-\\d+|\\d+)+(,(\\d+-\\d+|\\d+))*";
        Pattern pat = Pattern.compile(pattern);
        List rangoComa = new ArrayList();
        List rangoGuion = new ArrayList();
        Matcher mat = pat.matcher(rangoSampleValue);

        if (rangoSampleValue.length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Introduzca un Rango o una Muestra", "Credenciales Incorrectas"));
            mayor = true;
            flagList = 0;
        } else {

            //Validar Si el Rango coincide con el Regex
            if (mat.matches()) {
                System.out.println("Rango Correcto:" + rangoSampleValue);
                String[] comas = rangoSampleValue.split(","); //Lista Dividida por comas(,)
                for (int i = 0; i < comas.length && !mayor; i++) { //For para recorrer la lista de Comas y validar todos los numeros del rango
                    String[] guion = comas[i].split("-"); //Lista Dividida por guiones(-)
                    for (int j = 0; j < guion.length; j++) { //For para recorrer la lista de Guiones
                        if (temp < Integer.parseInt(guion[j])) {//Se valida si la variable temp que guarda siempre el valor anterior , es menor que el siguiente 
                            System.out.println("Anterior:" + temp);
                            System.out.println("Actual  : " + guion[j]);
                            temp = Integer.parseInt(guion[j]); //Se actualiza el elemento al siguiente en la lista
                        } else {//Si la variable temp es mayor que el siguiente en la lista, hay un error en el rango 
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Verificar el Rango de la Muestras", "Credenciales Incorrectas"));
                            System.out.println("Temp:" + temp + " Es MAYOR que:" + guion[j]);
                            mayor = true; //Se activa la bandera, para indicar que hay un error en el rango
                        }
                    }//Finaliza el for de los Guiones   
                }//Finaliza el for de las Comas

            } else {//Si el rango no coincide con el regex, entonces 
                System.out.println("Rango Incorrecto:" + rangoSampleValue);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Rango de Muestras Incorrecta", "Credenciales Incorrectas"));
                mayor = true;
                flagList = 0;
            }
        }//VAlidacion de Rango Nulo      
        System.out.println("Estado de la bandera: " + mayor);

        //Si la bandera es True, termina el proceso, puesto que el rango es incorrecto
        if (mayor == true) {
            System.out.println("ES UN PRUEBA DE LA BANDERA: MAYOR=true");

        } else {//Valida la bandera, si es false entonces el rango es correcto y se continua con las consultas.
            rangoValido = rangoSampleValue;
            String[] comasVal = rangoValido.split(",");//Se crea una lista dividida por Comas para recorrer, ya validada
            for (int i = 0; i < comasVal.length; i++) {//Se recorre la lista para realizar las consultas por cada Muestra
                String[] guionVal = comasVal[i].split("-");//Se crea una lista dividida por Guiones para recorrer, ya validada.
                for (int j = 0; j < guionVal.length; j++) {//Se recorre la lista para realizar las consultas por cada rango de Muestras
                    if (guionVal.length == 1) {//Si la lista equivale a 1, corresponde una muestra.
                        r1 = guionVal[j];//Rango inicial y final, equivalen al mismo numero
                        r2 = guionVal[j];
                        System.out.println("Unico R1:" + r1);//De informacion, se pueden quitar
                        System.out.println("Unico R2:" + r2);
                        listaCom = ejbFacade.rangoSample(r1, r2, idProy);//Se realiza la consulta, enviando el rango y el ID del proyecto actual.
                        System.out.println("Tamaño de la lista Coma:" + listaCom.size());
                        if (listaCom.size() == 0) {//Si la consulta devuelve un Cero, entonces existe un error en la muestra, no pertenece al proyecto, se finaliza el proceso.
                            flag2 = false;
                            flagList = 0;
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "La muestra: " + r1 + " no pertenece a este Proyecto", "Credenciales Incorrectas"));
                            break;
                        } else {//Si la consulta es diferente de Cero, entonces la consuta fue exitosa y se agrega a la lista de rangos, para continuar con otra consulta.
                            listaRango.addAll(listaCom);
                        }
                    }

                    if (guionVal.length == 2) {//Si la lista equivale a 2, corresponde a un rango de Muestras
                        r1 = guionVal[j];//Rango inicial es el primer numero de la lista
                        r2 = guionVal[j + 1];//Rango final es el segundo numero de la lista
                        System.out.println("Rango R1:" + r1);
                        System.out.println("Rango R2:" + r2);
                        listaGui = ejbFacade.rangoSample(r1, r2, idProy);//Se realiza la consulta, enviando el rango y el ID del proyecto actual.
                        System.out.println("Tamaño de la lista Guion:" + listaGui.size());
                        if (listaGui.size() == 0) {//Si la consulta devuelve un Cero, entonces existe un error en el rango de muestras, no pertenece al proyecto, se finaliza el proceso.
                            flag2 = false;
                            flagList = 0;
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El rango de muestras: " + r1 + "-" + r2 + " no pertenece a este Proyecto", "Credenciales Incorrectas"));
                            break;
                        } else {//Si la consulta es diferente de Cero, entonces la consuta fue exitosa y se agrega a la lista de rangos, para continuar con otra consulta.
                            System.out.println("Consulta exitosa... se agregan las bibliotecas consultadas---------------------------------------");
                            listaRango.addAll(listaGui);
                        }
                        break; //Se rompe el if, para continuar con el siguiente rango y no con el siguiente numero en la lista.
                    }
                }
            }
            //El rango ya fue Validado y las muestras pertenecen al proyecto, se concatenan las dos listas y se agregan a la lista completa de rangos. 
            //Se envia la lista completa de Sample para actualizar la tabla-formulario    
            //Si todas las validaciones son correctas, la tabla de muestras se actualiza.
            if (flag2 == true) {
                rangoSampleVal.addAll(listaRango);
                flagList = 1;
                System.out.println("Se actualiza la lista con nuevos valores ************************************************");
                listSamples = rangoSampleVal;
            }
        }

    }

    //Método para redirigir a la vista de muestras sobre el proyecto
    public void redirectSampleListProject(Project itemProject) {
        selectedProject = itemProject;
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", selectedProject);

        try {
            context.getExternalContext().redirect("../sample/List.xhtml");
        } catch (IOException ex) {

            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

        listSamples = sampleFac.findSamplesByProject(itemProject);
    }

    //Redirigir a crear proyecto y limpiar valores
    public void redirectCreateProject() {
        current = null;
        UserProjectLinkController userPL = new UserProjectLinkController();
        userPL.resetInputEmail();
        
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("../project/Create.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //Buscar Corridas por medio del proyecto
    public List<String> runsByProject() throws ClassNotFoundException, SQLException {

        System.out.println("entra al metodo findRun ");
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        String url = "jdbc:postgresql://localhost:5432/sisbi_db";
        String usuario = "sisbi";
        String pswd = "SISBI123@";
        ////////conexion a la BD///
        Class.forName("org.postgresql.Driver");
        Connection conexion = DriverManager.getConnection(url, usuario, pswd);
        java.sql.Statement st = conexion.createStatement();

        List<Sample> sm = sampleFac.findSampProj2(proj);
        List<String> rn = new ArrayList<>();
        try {
            for (Sample sample : sm) {

                List<Library> SLL = SampleLibraryLinkFacade.getLibraries(sample);

                for (Library lib : SLL) {

                    List<LibraryRunLink> findRuns = LibraryRunFacade.findRunByLibrary(lib.getIdLibrary());
                    String consulta = "select run_name from run where id_run in(select id_run from library_run_link where id_library=" + lib.getIdLibrary() + ");";
                    ResultSet rs = st.executeQuery(consulta);
                    while (rs.next()) {
                        System.out.println("Corrida:+ " + rs.getString(1));
                        rn.add(rs.getString(1));
                    }

                }
            }
            st.close();

        } catch (Exception e) {
            System.out.println(e);
        }
        List<String> DistinctRun = rn.stream().distinct().collect(Collectors.toList());
        return DistinctRun;

    }
    
}
