package jsf;

//bibliotecas para la creacion de documentos word
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import java.io.File;
import java.io.FileNotFoundException;

//Conjuntos
import java.util.Set;
import java.util.HashSet;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.velocity.internal.VelocityTemplateEngine;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import jpa.entities.ReportProject;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.ReportProjectFacade;

import java.io.Serializable;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.el.EvaluationException;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.Comments;
import jpa.entities.FieldReport;
import jpa.entities.LibraryRunLink;
import jpa.entities.Project;
import jpa.entities.Sample;
import jpa.entities.SampleReportProject;
import jpa.entities.UserRole;
import jpa.entities.UserRoleReport;
import jpa.entities.Users;
import jpa.session.CommentsFacade;
import jpa.session.FieldReportFacade;
import jpa.session.ProjectFacade;
import jpa.session.SampleFacade;
import jpa.session.SampleReportProjectFacade;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.poi.util.Units;
import org.apache.xmlbeans.XmlException;
import org.apache.poi.xwpf.usermodel.*;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import jpa.entities.QualityReports;
import jpa.entities.Run;
import jpa.entities.SampleLibraryLink;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;

class SignatureException extends IOException {

    public SignatureException(String errorMessage) {
        super(errorMessage);
    }
}

@Named("reportProjectController")
@SessionScoped
public class ReportProjectController implements Serializable {

    String DirectoryImageSing = PathFiles.DirectoryImageSing;

    private ReportProject current;

    private DataModel items = null;
    @EJB
    private jpa.session.ReportProjectFacade ejbFacade;
    @EJB
    private jpa.session.FieldReportFacade ejbFacadeFieldReport;
    @EJB
    private jpa.session.UserRoleReportFacade ejbFacadeUserRoleReport;
    @EJB
    private jpa.session.ProjectFacade ejbFacadeProject;
    @EJB
    private jpa.session.SampleFacade ejbFacadeSample;
    @EJB
    private jpa.session.UserRoleFacade ejbFacadeUserRole;
    @EJB
    private jpa.session.SampleReportProjectFacade ejbSampleRP;
    @EJB
    private jpa.session.CommentsFacade ejbCommentsFacade;
    @EJB
    private jpa.session.QualityReportsFacade ejbQualityReportsFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    //Clases, variables y metodos para agregar campos a la tabla field_project   Inicio
    private FieldReport classFieldReport;
    private Sample currentSample;
    // Variables para vista de reportes y sus respectivos status
    private List<ReportProject> projectReportProject;
    EmailController email = new EmailController();
    //Datos Para Pruebas de Reporte de Proyectos
    private List<Project> repoProject;
    private Project selectedRepoProject;
    List<Sample> rangoSampleVal;
    private List<Sample> repoEditSample;
    private SampleReportProject currentSampleRP;
    private Comments commentsReg;
    private List<Sample> repoSample;
    private Sample selectedRepoSample;
    private int flagList = 0;
    //Atributo para Seleecionar un rango de muestras
    private String rangoSample;
    private Project project;
    //paso de variable a otra vista
    private String varTypeReport;
    String subjetFinal; //Variable para asunto del mensaje para colaboradore y repsonsables
    String messageFinal; //Variable para cuerpo del mensaje para colaboradore y repsonsables
    private String findVarIdProject;  //Varibale para buscar reportes en la vista reportProjectStatus

    private final String DirectoryTemplateReport = PathFiles.DirectoryTemplateReport;
    private final String DirectoryreportDocuments = PathFiles.DirectoryreportDocuments;
    //private final String DirectoryDownloadWords = PathFiles.DirectoryDownloadWords;//Esto ya no es usado. TODO explicar esto

    public String getFindVarIdProject() {
        return findVarIdProject;
    }

    public void setFindVarIdProject(String findVarIdProject) {
        this.findVarIdProject = findVarIdProject;
    }

    public String getSubjetFinal() {
        return subjetFinal;
    }

    public void setSubjetFinal(String subjetFinal) {
        this.subjetFinal = subjetFinal;
    }

    public String getMessageFinal() {
        return messageFinal;
    }

    public void setMessageFinal(String messageFinal) {
        this.messageFinal = messageFinal;
    }

    public FieldReportFacade getEjbFacadeFieldReport() {
        return ejbFacadeFieldReport;
    }

    public void setEjbFacadeFieldReport(FieldReportFacade ejbFacadeFieldReport) {
        this.ejbFacadeFieldReport = ejbFacadeFieldReport;
    }

    public FieldReport getClassFieldReport() {
        return classFieldReport;
    }

    public void setClassFieldReport(FieldReport classFieldReport) {
        this.classFieldReport = classFieldReport;
    }

    //Clases, variables y metodos para agregar campos a la tabla field_project   Final
    public List<ReportProject> getProjectReportProject() {
        return projectReportProject;
    }

    public void setProjectReportProject(List<ReportProject> projectReportProject) {
        this.projectReportProject = projectReportProject;
    }

    public Sample getCurrentSample() {
        return currentSample;
    }

    public void setCurrentSample(Sample currentSample) {
        this.currentSample = currentSample;
    }

    public SampleFacade getEjbFacadeSample() {
        return ejbFacadeSample;
    }

    public void setEjbFacadeSample(SampleFacade ejbFacadeSample) {
        this.ejbFacadeSample = ejbFacadeSample;
    }

    public List<Project> getRepoProject() {
        return repoProject;
    }

    public void setRepoProject(List<Project> repoProject) {
        this.repoProject = repoProject;
    }

    public Project getSelectedRepoProject() {
        return selectedRepoProject;
    }

    public void setSelectedRepoProject(Project selectedRepoProject) {
        this.selectedRepoProject = selectedRepoProject;
    }

    public List<Sample> getRepoEditSample() {
        //repoEditSample = rangoEditSampleVal;
        return repoEditSample;
    }

    public void setRepoEditSample(List<Sample> repoEditSample) {
        this.repoEditSample = repoEditSample;
    }

    public List<Sample> getRepoSample() {
        if (flagList == 1) {
            repoSample = rangoSampleVal;
            return repoSample;

        } else {
            repoSample = ejbFacadeProject.samplesByRepoProject(varIdProject);
            return repoSample;

        }
    }

    public void setRepoSample(List<Sample> repoSample) {
        this.repoSample = repoSample;

    }

    public Sample getSelectedRepoSample() {
        return selectedRepoSample;
    }

    public void setSelectedRepoSample(Sample selectedRepoSample) {
        this.selectedRepoSample = selectedRepoSample;
    }

    public String getRangoSample() {
        return rangoSample;
    }

    public void setRangoSample(String rangoSample) {
        this.rangoSample = rangoSample;
    }

    public CommentsFacade getEjbCommentsFacade() {
        return ejbCommentsFacade;
    }

    public void setEjbCommentsFacade(CommentsFacade ejbCommentsFacade) {
        this.ejbCommentsFacade = ejbCommentsFacade;
    }

    public SampleReportProjectFacade getEjbSampleRP() {
        return ejbSampleRP;
    }

    public void setEjbSampleRP(SampleReportProjectFacade ejbSampleRP) {
        this.ejbSampleRP = ejbSampleRP;
    }

    public ProjectFacade getEjbFacadeProject() {
        return ejbFacadeProject;
    }

    public void setEjbFacadeProject(ProjectFacade ejbFacadeProject) {
        this.ejbFacadeProject = ejbFacadeProject;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getVarTypeReport() {
        return varTypeReport;
    }

    public void setVarTypeReport(String varTypeReport) {
        this.varTypeReport = varTypeReport;
    }

    private int varIdReportProject;

    public int getVarIdReportProject() {
        return varIdReportProject;
    }

    public void setVarIdReportProject(int varIdReportProject) {
        this.varIdReportProject = varIdReportProject;
    }

    private int varIdFieldReport;

    public int getVarIdFieldReport() {
        return varIdFieldReport;
    }

    public void setVarIdFieldReport(int varIdFieldReport) {
        this.varIdFieldReport = varIdFieldReport;
    }

    private String varIdProject;

    public String getVarIdProject() {
        return varIdProject;
    }

    public void setVarIdProject(String varIdProject) {
        this.varIdProject = varIdProject;
    }

    @PostConstruct
    public void init() {
        //repoProject = new ArrayList<>();
        repoProject = ejbFacadeProject.proyectos_con_analisis_bioinformatico_iniciado_o_entregado();
        repoSample = new ArrayList<>();
        //consulta para lista de reportes y sus estados
        //projectReportProject = new ArrayList<>();
        //projectReportProject = ejbFacadeProject.reportProjectStatus();
        messageFinal = "Mensaje predeterminado";
        findReportProject();
    }

    public void findReportProject() {
        if (findVarIdProject == null || findVarIdProject.trim().equals("")) {
            projectReportProject = new ArrayList<>();
            projectReportProject = ejbFacadeProject.reportProjectStatus();
            setProjectReportProject(projectReportProject);
            //System.out.println("Arreglo sin busqueda: "+projectReportProject);
        } else {
            projectReportProject = new ArrayList<>();
            projectReportProject = ejbFacade.findReportProjectByIdProject(findVarIdProject);
            setProjectReportProject(projectReportProject);
            //System.out.println("Arreglo con busqueda: "+projectReportProject);
        }

    }

    public void redirectRepoProject(String idProject) {
        varIdProject = idProject;
        //System.out.println("El id del proyecto essssss::::::   " + idProject);
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("../reportDocuments/SeleccionTipoReporte.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectRepoProjectStatus() {
        //System.out.println("El id del proyecto essssss::::::   " + idProject);
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("../reportDocuments/ReportProjectStatus.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String RepoProjectName() {
        return this.selectedRepoProject.getProjectName();
    }

    public String actBotonReporte(SampleController sampleController, String idProject) {
        Project proj = getEjbFacadeProject().find(idProject);
        this.selectedRepoProject = proj;
        //Metodo obtiene el userRoleReport de la sesion iniciada actualmente, si devueleve null es que el usrio no tiene permisos, si sientoce me devuleve el id del user
        UserRoleReport userRol = getUserRoleReportContext();
        if (userRol == null) {
            System.out.println("no tengo rol de crear reportes");
            return "true"; //null no tiene permisos,
        }

        if (userRol.getTocreate() == false) {
            System.out.println("no tengo permisos para crear");
            return "true"; //si no tinen permisos para crear reportes
        }
        return "false";//Actualmente, la generación de reportes no se bloquea más que por permisos del usuario

        //Si quieren el bloqueo que estaba antes, comenten la línea anterior y descomenten el siguente blouqe de código:
        /*
        String compId = proj.getIdProject();
        String n = "true";//true

        // Condiciones en https://docs.google.com/document/d/1Q5r9XEYptUXLorDzcTPsz2Bqq9Ufx2c0/edit
        //Comprueba que haya evidencia de pago
        // TODO está buen actualizar billNumber?
        
        //if (proj.getBillNumber() == null){
        //    return "true";
        //}
        
        //Distintas condiciones de la muestra:
        //SELECT DISTINCT status FROM "public".sample LIMIT 100;
        //Entrega solo analisis de calidad
        //Basecalling y QC en proceso
        //Rechazada (Forsake)
        //En espera de secuenciacion
        //Entrega solo extraccion
        //Calidad Rechazada
        //Preparada
        //Recibida
        //Construccion ineficiente
        //En construcción
        //Biblioteca entregada
        //En espera para secuenciacion
        //Calidad Aceptada
        //En analisis de calidad
        //En construccion de biblioteca
        //Analisis Bioinformatico
        //Forsake (Forsake)
        //Analisis Bioinformatico Entregado
        //Entregado fastq
        //Basecalling y QC terminado
        //En Analisis Bioinformatico
        //Registrada
        //En análisis de calidad
        //Para resecuenciar
        //Forsake
        //Secuenciandose
        //Calidad rechazada
        //Secuenciada

        int cont = 0;
        List<Project> projects;
        //p = new ArrayList<>();
        projects = ejbFacadeProject.proyectos_con_analisis_bioinformatico_iniciado_o_entregado();
        //Agregamos una validación extra, si en caso ya tiene reportes generados, entonces que no se bloquee el botón, aunque las muestras cambien de estado
        if(ejbFacade.reportProjectByIdProject(idProject).size() >= 1 ){ //si tierne regstros en la tabla report_projetc
            return "false";
        }
        for (Project project_: projects) {
            if (project_.getIdProject() == null ? compId == null : project_.getIdProject().equals(compId)) {
                n = "false";
                break;
            }
            cont = cont + 1;
        }
        if (n.equals("true")){
            //No hay reportes actuales. Verifica si ya existen resultados de FastQC del proyecto
            List<Sample> all_project_samples = sampleController.getItemsProj(proj);
            for (Sample sample: all_project_samples) {
                if (sample.getStatus().contains("Analisis Bioinformatico")){
                    //Analisis Bioinformatico, Analisis Bioinformatico entregado o cualquier frase que contenga
                    // Analisis Bioinformatico, así, sin acentos y con mayúscula sólo en la primera letra de cada palabra
                    n = "false";
                    break;//Ya encontró una muestra con análisis bioinformático, entonces ya está autorizada
                    // la generación de reportes
                } else {
                    switch (sample.getStatus()){
                        case "Secuenciada":
                        case "Entregado fastq":
                            n = "false";
                            break;
                    }
                    if (n.equals("false")){
                        break;
                    }
                }
            }
        }
        return n; */
    }

    public boolean renderedBtnDescargarReporteFinal(String idProject, String run_name) {
        try {
            List<ReportProject> reportsProject = ejbFacade.findReportProjectByIdProject(idProject);
            ReportProject itemReportProject;
            for (int i = 0; i < reportsProject.size(); i++) {
                itemReportProject = reportsProject.get(i);
                if (itemReportProject.getStatus().equals("Autorizado") || itemReportProject.getStatus().equals("Entregado")) {
                    List<QualityReports> qualreports = ejbQualityReportsFacade.urlQualityReportsByIdProject(idProject);
                    qualreports.sort(new SortQualityReportsByRecency());
                    QualityReports qrep = qualreports.get(0);
                    Run qrep_run = qrep.getIdRun();
                    //System
                    System.out.println(qrep_run.getRunName());
                    if (qrep_run.getRunName().equals(run_name)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, "Error al renderizar el boton Descargar Reporte Final", e);
            return false;
        }
    }

    public String validateBtnLista(String idProject, String run_name) {
        List<ReportProject> lrp = list_ReportProject(idProject, run_name);
        return lrp.isEmpty() ? "true" : "false";
    }

    public List<ReportProject> list_ReportProject(String idProject, String run_name) {
        //TOPDO Considerar herencia de listBR()
        List<ReportProject> reportsProject = ejbFacade.findReportProjectByIdProject(idProject);
        List<ReportProject> matching_projects = new ArrayList<>();
        List<QualityReports> qualreports = ejbQualityReportsFacade.urlQualityReportsByIdProject(idProject);
        //qualreports.sort( new SortQualityReportsByRecency());
        for (ReportProject itemReportProject : reportsProject) {
            for (QualityReports qrep : qualreports) {
                Run qrep_run = qrep.getIdRun();
                if (qrep_run.getRunName().equals(run_name)) {
                    if (itemReportProject.getStatus().equals("Entregado") || itemReportProject.getStatus().equals("Autorizado")) {
                        matching_projects.add(itemReportProject);
                    }
                }
            }
        }
        return matching_projects;
    }

    public List<ReportProject> ReportsProjectByIdProject(String idProject) {
        List<ReportProject> reportsProject = ejbFacade.findReportProjectByIdProject(idProject);
        List<ReportProject> matching_projects = new ArrayList<>();
        for (ReportProject itemReportProject : reportsProject) {
            if (itemReportProject.getStatus().equals("Entregado") || itemReportProject.getStatus().equals("Autorizado")) {
                matching_projects.add(itemReportProject);
            }
        }
        return matching_projects;
    }

    public String downloadReportsFinal(String idProject, String run_name) {
        try {
            List<ReportProject> reportsProject = ejbFacade.findReportProjectByIdProject(idProject);
            ReportProject itemReportProject;
            System.out.println("tamaño de la lista: " + reportsProject.size());
            for (int i = 0; i < reportsProject.size(); i++) {
                itemReportProject = reportsProject.get(i);
                //itemReportProject.getIdProject().getSampleCollection().get(0).
                if (itemReportProject.getStatus().equals("Autorizado") || itemReportProject.getStatus().equals("Entregado")) {
                    List<QualityReports> qualreports = ejbQualityReportsFacade.urlQualityReportsByIdProject(idProject);
                    qualreports.sort(new SortQualityReportsByRecency());
                    QualityReports qrep = qualreports.get(0);
                    Run qrep_run = qrep.getIdRun();
                    if (qrep_run.getRunName().equals(run_name)) {
                        String ruteDoc;
                        ruteDoc = DirectoryreportDocuments + itemReportProject.getPathauthorizePDF();
                        File ficheroXLS = new File(ruteDoc);
                        FacesContext ctx2 = FacesContext.getCurrentInstance();
                        FileInputStream fis2 = new FileInputStream(ficheroXLS);
                        byte[] bytes = new byte[1000];
                        int read = 0;
                        if (!ctx2.getResponseComplete()) {
                            String nameDocDowload;
                            String contentType;
                            contentType = "application/pdf";
                            nameDocDowload = "F01_PT05_LNATCG_" + itemReportProject.getIdProject().getIdProject().replace(":", "_") + "_" + typeReportInitials(itemReportProject.getName()) + ".pdf";
                            HttpServletResponse response = (HttpServletResponse) ctx2.getExternalContext().getResponse();
                            response.setContentType(contentType);
                            response.setHeader("Content-Disposition", "attachment;filename=\"" + nameDocDowload + "\"");
                            ServletOutputStream out = response.getOutputStream();
                            while ((read = fis2.read(bytes)) != -1) {
                                out.write(bytes, 0, read);
                            }
                            out.flush();
                            out.close();
                            ctx2.responseComplete();
                        }
                        fis2.close();
                        System.out.println("descarga finalizada para " + itemReportProject.getPathauthorize());
                        showMessage("downloadReportsFinal ejecutado satisfactoriamente.");
                    }

                }
            }
        } catch (IOException e) {
            showError("Error al descargar el archivo", e);
        }
        return "ViewProject?faces-redirect=true&includeViewParams=true";
    }

    private void restartArray() {
        flagList = 0;
    }

    public void imprimirRango() {
        String rangoSampleValue = rangoSample;
        rangoSample = null;
        System.out.println("Rango a Validar:" + rangoSampleValue);
        //temp para verificar si el anterior es mayor al siguiente
        int temp = 0;

        //Rango Validado
        String rangoValido = "";
        String r1 = "";
        String r2 = "";
        String idProy = this.selectedRepoProject.getIdProject();
        boolean flag2 = true;

        //Listas Para Mostrar
        rangoSampleVal = new ArrayList();
        List<Sample> listaCom = new ArrayList();
        List<Sample> listaGui = new ArrayList();
        List<Sample> listaRango = new ArrayList();
        //Bandera Para validar el rango.
        boolean mayor = false;
        String pattern = "(\\d+-\\d+|\\d+)+(,(\\d+-\\d+|\\d+))*";
        Pattern pat = Pattern.compile(pattern);
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
                        listaCom = ejbFacadeProject.rangoSample(r1, r2, idProy);//Se realiza la consulta, enviando el rango y el ID del proyecto actual.
                        System.out.println("Tamaño de la lista Coma:" + listaCom.size());
                        if (listaCom.isEmpty()) {//Si la consulta devuelve un Cero, entonces existe un error en la muestra, no pertenece al proyecto, se finaliza el proceso.
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
                        listaGui = ejbFacadeProject.rangoSample(r1, r2, idProy);//Se realiza la consulta, enviando el rango y el ID del proyecto actual.
                        System.out.println("Tamaño de la lista Guion:" + listaGui.size());
                        if (listaGui.isEmpty()) {//Si la consulta devuelve un Cero, entonces existe un error en el rango de muestras, no pertenece al proyecto, se finaliza el proceso.
                            flag2 = false;
                            flagList = 0;
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "El rango de muestras: " + r1 + "-" + r2 + " no pertenece a este Proyecto", "Credenciales Incorrectas"));
                            break;
                        } else {//Si la consulta es diferente de Cero, entonces la consuta fue exitosa y se agrega a la lista de rangos, para continuar con otra consulta.
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
            }
        }
    }

    public void impMT() {
        System.out.println("SAMPLE TAMAÑO : " + repoSample.size());
        Iterator<Sample> sampleIterator = repoSample.iterator();

        while (sampleIterator.hasNext()) {
            Sample elementoSample = sampleIterator.next();
            System.out.println("ID Sample: " + elementoSample.getIdSample());
        }
    }

    //Metodo Actualiza el estatus de las muestras en la tabla Sample, registra el cambio en la tabla Sample Report Project.
    //Registra las actualizaciones de las muestras en la tabla comments
    public String actualizarSampleReport() throws IOException {
        try {
            //flag Muestras
            flagList = 0;
            ReportProject currentItemReportProject = (ReportProject) (ReportProject) ejbFacade.findReportProjectidPType(varIdProject, varTypeReport).get(0);

            FacesContext context = FacesContext.getCurrentInstance();
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

            System.out.println("ACTUALIZANDO MUESTRAS");
            List<Sample> upSample = new ArrayList();
            upSample.addAll(this.repoEditSample);
            java.util.Date datePer = new Date();
            Iterator<Sample> upSampleIterator = upSample.iterator();
            while (upSampleIterator.hasNext()) {
                Sample idUpSample = upSampleIterator.next();
                int idSample = idUpSample.getIdSample();
                //Actualiza el estatus de las muestras en la tabla Sample.
                currentSample = getEjbFacadeSample().find(idSample);
                String currentStatusSample = currentSample.getStatus();
                if (!currentStatusSample.equals("Entregado fastq")
                        && !currentStatusSample.startsWith("Analisis Bioinformatico")) {

                    // Se realiza un cambio de estatus sobre la muestra
                    currentSample.setStatus("Analisis Bioinformatico");
                    getEjbFacadeSample().edit(currentSample);

                    // Buscar si existe un comentario previo para esta muestra y usuario
                    Comments existingComment = getEjbCommentsFacade().findCommentByIdTypeUserAndType(
                            String.valueOf(idSample), us.getUserName(), "Sample"
                    );

                    String correctComment = "Se cambia el estatus de " + currentStatusSample + " a - En Analisis Bioinformatico";

                    if (existingComment != null) {
                        // Verificar si el comentario existente tiene un error de nomenclatura
                        if (!existingComment.getComment().equals(correctComment)) {
                            // Corregir el comentario existente
                            existingComment.setComment(correctComment);
                            existingComment.setCommentDate(datePer); // Actualizar la fecha
                            getEjbCommentsFacade().edit(existingComment);
                            System.out.println("Comentario corregido para muestra: " + idSample);
                        }
                    } else {
                        // Crear un nuevo comentario si no existe
                        commentsReg = new Comments();
                        commentsReg.setType("Sample");
                        commentsReg.setIdType(String.valueOf(idSample));
                        commentsReg.setUserName(us.getUserName()); // Usuario que realizó el cambio
                        commentsReg.setComment(correctComment);
                        commentsReg.setCommentDate(datePer); // Fecha del cambio
                        getEjbCommentsFacade().create(commentsReg);
                        System.out.println("Nuevo comentario creado para muestra: " + idSample);
                    }

                    System.out.println("Muestra Actualizada:" + idUpSample + "    OBJETO:" + currentSample);
                }

                //Inserta el registro del cambio de estatus en la tabla sample_report_project
                currentSampleRP = new SampleReportProject();
                currentSampleRP.setIdReportProject(currentItemReportProject);
                currentSampleRP.setIdSample(idUpSample);
                currentSampleRP.setStatusCurrent("Analisis Bioinformatico");
                currentSampleRP.setStatusPrevious(currentStatusSample);
                currentSampleRP.setDateUpgrade(datePer);
                getEjbSampleRP().create(currentSampleRP);
            }
            System.out.println("MUESTRAS ACTUALIZADAS");
            return redirectFormReport(null);
        } catch (Exception e) {
            showError("Error en actualizarSampleReport", e);
            return redirectFormReport(null);
        }
    }

    public static String getDateStringFormat(Date _fecha) {
        if (_fecha == null) {
            return " ";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String fechaString = sdf.format(_fecha);
            return fechaString;
        }
    }

    //Genera una fecha del Sistema, retorna un String con el formato yyyy-MM-dd.
    public static String getCurrentDate() {
        java.util.Date fecha = new Date();
        String s_fecha = getDateStringFormat(fecha);
        return s_fecha;
    }

    //Obtiene la fecha de analisis bioinformático (de la bitácora)
    public Date getBioinfoAnalysisDate() {
        Date badate;
        if (repoEditSample == null) {
            badate = getLastBioinfoAnlDate(repoSample);
        } else {
            badate = getLastBioinfoAnlDate(repoEditSample);
        }
        /*
        Date dateByList;
        List<Comments> commentsABList = ejbFacade.getCommentsBAbyProjectSortedByDate(current.getIdProject().getIdProject());
        if (commentsABList.isEmpty()){
            dateByList = null;
        } else {
            dateByList = commentsABList.get(0).getCommentDate();
        }*/
        return badate;
    }

    public void setBioinfoAnalysisDate(Date badate) {
        //No hace nada, pero para que no se pare por que quiere un setter...
    }

    public static String periodReport(Date _fecha) {
        if (_fecha == null) {
            return "";
        }
        String year = String.valueOf(_fecha.getYear() + 1900);
        int month = _fecha.getMonth() + 1;
        String monthS = "";

        switch (month) {
            case 1:
                monthS = "ENERO";
                break;
            case 2:
                monthS = "FEBRERO";
                break;
            case 3:
                monthS = "MARZO";
                break;
            case 4:
                monthS = "ABRIL";
                break;
            case 5:
                monthS = "MAYO";
                break;
            case 6:
                monthS = "JUNIO";
                break;
            case 7:
                monthS = "JULIO";
                break;
            case 8:
                monthS = "AGOSTO";
                break;
            case 9:
                monthS = "SEPTIEMBRE";
                break;
            case 10:
                monthS = "OCTUBRE";
                break;
            case 11:
                monthS = "NOVIEMBRE";
                break;
            case 12:
                monthS = "DICIEMBRE";
                break;
        }
        return monthS + " " + year;
    }

    public void redirectEditSampleSelection() {
        rangoSampleVal = new ArrayList();
        System.out.println("Inicio de Redireccion");
        ReportProject editRangeReportProject = (ReportProject) (ReportProject) ejbFacade.findReportProjectidPType(varIdProject, varTypeReport).get(0);
        int idSampleReportProject = editRangeReportProject.getIdReportProject();//25
        System.out.println("idSampleReportProject:" + idSampleReportProject);
        //Se contiene la lista de muestras Actualizadas.
        List<SampleReportProject> rangeListEditSampleReportProject = (List<SampleReportProject>) ejbSampleRP.EditRangeSample(idSampleReportProject);
        List<Sample> rangeEditSample;// = new ArrayList();
        List<Sample> rangeEditSampleComplete = new ArrayList();
        String idSample2 = rangeListEditSampleReportProject.get(0).getIdSample().getIdSample().toString();
        System.out.println("idSample:" + idSample2);

        for (int i = 0; i < rangeListEditSampleReportProject.size(); i++) {
            String idSample = rangeListEditSampleReportProject.get(i).getIdSample().getIdSample().toString();
            rangeEditSample = ejbSampleRP.rangoSample(idSample, idSample, varIdProject);
            rangeEditSampleComplete.addAll(rangeEditSample);
        }
        repoEditSample = rangeEditSampleComplete;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("../reportDocuments/EditSelectionSample.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirectSampleSelection() {
        FacesContext contextUs = FacesContext.getCurrentInstance();
        Users us = (Users) contextUs.getExternalContext().getSessionMap().get("usuario");
        java.util.Date datePer = new Date();
        //Se Actualizan las Muestras A su Estado Anterior. COMENTARIO :)
        ReportProject editRangeReportProject = (ReportProject) (ReportProject) ejbFacade.findReportProjectidPType(varIdProject, varTypeReport).get(0);
        int idSampleReportProject = editRangeReportProject.getIdReportProject();//25
        System.out.println("IDS: " + varIdProject + " : " + varTypeReport + " : " + idSampleReportProject);
        // las muestras escogidas ahora están en repoEditSample;
        List<SampleReportProject> rangeListEditSampleReportProject = (List<SampleReportProject>) ejbSampleRP.EditRangeSample(idSampleReportProject);
        for (SampleReportProject sampleRp : rangeListEditSampleReportProject) {
            Sample the_sample = sampleRp.getIdSample();
            int idSample = the_sample.getIdSample();//Id del sample para actualizar
            System.out.println("idSample: " + idSample);
            //Actualiza el estatus de las muestras en la tabla Sample.
            //currentSample = getEjbFacadeSample().find(idSample);
            currentSample = the_sample;
            System.out.println("currentSample: " + currentSample);
            String currentStatusSample = currentSample.getStatus();
            currentSample.setStatus(sampleRp.getStatusPrevious());
            getEjbFacadeSample().edit(currentSample);
            System.out.println("Muestra Actualizada: " + currentSample);

            //Se elimina el registro en la tabla sample_report_project
            //currentSampleRP = new SampleReportProject();
            //currentSampleRP = getEjbSampleRP().find(sampleRp.getIdSampleReportProject());
            //getEjbSampleRP().remove(currentSampleRP);
            getEjbSampleRP().remove(sampleRp);
            flagList = 0;

            //Se documenta el Cambio, en la tabla Comments.
            commentsReg = new Comments();
            commentsReg.setType("Sample");
            commentsReg.setIdType(String.valueOf(idSample));
            commentsReg.setUserName(us.getUserName());//Usuario que realizo el cambio
            commentsReg.setComment("Se cambia el estatus de " + currentStatusSample + " a " + sampleRp.getStatusPrevious());//Comentario explicito del registro
            commentsReg.setCommentDate(datePer);
            getEjbCommentsFacade().create(commentsReg);
        }
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("../reportDocuments/SelectionSample.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //paso de variable a otra vista   ----- Inicio
    //Metodoss desde boton generar reporte         Inicio    LUIS
    public void startReport(String idProject) throws IOException {
        try {
            Project project_ = (Project) ejbFacadeProject.findProjectXIdProject(idProject).get(0);
            varIdProject = project_.getIdProject();
            //return "../Reportes/SelectionSample.xhtml";
            System.out.println("id project " + varIdProject);
            //FacesContext context = FacesContext.getCurrentInstance();
            //context.getExternalContext().redirect("../reportDocuments/SelectionSample.xhtml");
        } catch (Exception e) {
            showError("Error en método startReport", e);
        }
    }

    //Metodo para redireccionar a la interfaz para seleccionar la muestras o redirigir para continuar con el reporte al
    public String redirectSelectionSample(List<ReportProject> reportes_mismo_proyecto_y_tipo_reporte) {
        //Comprobar si para este proyecto, ya se encuentran muestras actualizadas en la tabla sample_report_project
        //Verificar si el id del proyecto seleccionaddo existe en la tabla report_project.
        ReportProject itemReportProject = (ReportProject) (ReportProject) ejbFacade.findReportProjectidPType(varIdProject, varTypeReport).get(0);
        int sizeSampleRP = ejbSampleRP.existsSampleReportProject(itemReportProject.getIdReportProject());
        if (sizeSampleRP > 0) {//Si existe un registro en sample_report_project, entonces lo redirige a rellenar los field Manual.
            return redirectFormReport(reportes_mismo_proyecto_y_tipo_reporte);
        } else {//si no existe un registro en sample_report_project, entonces lo redirige para actualizar la muestras.
            restartArray();
            return "SelectionSample?faces-redirect=true&includeViewParams=true&similar_report_exists=true";
        }
    }

    //Metodoss desde boton generar reporte         Final    LUIS
    //Metodo para redireccionar al formulario del reporte, incluye validación para por si ya existen campos registrados
    public String redirectFormReport(List<ReportProject> reportes_mismo_proyecto_y_tipo_reporte) {
        ReportProject itemReportProject = (ReportProject) (ReportProject) ejbFacade.findReportProjectidPType(varIdProject, varTypeReport).get(0);
        //ejbFacadeFieldReport.deleteFieldReportByReportProject(itemReportProject.getIdReportProject());
        int sizeField = ejbFacadeFieldReport.existsFieldReport(itemReportProject.getIdReportProject());
        if (sizeField > 0) {
            classFieldReport = (FieldReport) ejbFacadeFieldReport.findFieldReportByReportProject(itemReportProject.getIdReportProject()).get(0);
            if (reportes_mismo_proyecto_y_tipo_reporte == null) {
                return "menuReport?faces-redirect=true&includeViewParams=true";
            } else {
                return "";
            }
        } else {
            classFieldReport = new FieldReport();
            varIdReportProject = itemReportProject.getIdReportProject();
            return formTypeReport(varTypeReport, reportes_mismo_proyecto_y_tipo_reporte);
        }
        //Preguntarle a luis sobre los mensajes emergentes
    }

    public String getTypeMethodologyFromSamples(List<Sample> samples) {
        Set<Object> options = new HashSet<>();
        for (Sample sample : samples) {
            options.add(sample.getType());
        }
        if (options.size() == 1) {
            return options.iterator().next().toString();
        } else {
            return options.iterator().next().toString();
        }
    }

    private void resetReport(String tipoReport, String idProject, Users us) {
        //Resets the report to 0
        project = getEjbFacadeProject().find(idProject);
        //ejbFacadeFieldReport.DeleteRangeSample(current.getIdReportProject());
        //List<FieldReport> irpsi = ejbFacadeFieldReport.findFieldReportByReportProject(current.getIdReportProject());

        current.setName(tipoReport);
        current.setStatus("Creado");
        //current.setDateCreate(current.getIdProject().getRequestDate());
        //current.setDateRevise(new Date());
        current.setDateAuthorize(null);
        current.setPathauthorize(null);
        current.setPathcreate(null);
        current.setPathrevise(null);
        current.setIdProject(project);
        current.setIdUser(us);
        //TODO poner en todos lados la validacion de que no se repita usuario en crear, revisar y autorizar
        if (current.getIdUserRevise() == null) {
            for (Users userrev : this.getUsersRevise()) {
                if (!Objects.equals(userrev.getIdUser(), us.getIdUser())) {
                    current.setIdUserRevise(userrev);
                    break;
                }
            }
        }
        if (current.getIdUserAuthorize() == null) {
            for (Users userauth : this.getUsersAuthorize()) {
                if (!Objects.equals(userauth.getIdUser(), us.getIdUser()) && !Objects.equals(current.getIdUserRevise().getIdUser(), userauth.getIdUser())) {
                    current.setIdUserAuthorize(userauth);
                    break;
                }
            }
        }
        int idReportProject = current.getIdReportProject();//25
        ejbSampleRP.DeleteRangeSample(idReportProject);
        ejbSampleRP.deleteFieldReportByReportProject(idReportProject);
        restartArray();

        getFacade().edit(current);
    }

    public String createTypeReportProject(String tipoReport, SampleController sampleController, List<Run> runs) {
        String mensaje;

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

        varTypeReport = tipoReport;
        String idProject = varIdProject;
        String to_return;

        try {
            int sizeReport = ejbFacade.existsReportProject(idProject, tipoReport);//Valida solo si el proyecto existe en report_project, si no, lo registra.
            if (sizeReport > 0) {
                List<ReportProject> report_projects = ejbFacade.findReportProjectidPType(idProject, tipoReport);
                ReportProject itemReportProject = (ReportProject) (ReportProject) report_projects.get(0);
                varIdReportProject = itemReportProject.getIdReportProject();
                current = getFacade().find(varIdReportProject);

                //current.setTypeMethodology(typeMethodology);
                //Si el proyecto existe en report_project:
                //Validar, si para este proyecto ya se ha iniciado un reporte.
                //Si se ha iniciado un reporte verificar el estatus, para saber a donde redirigirlo.
                //Verificar si existe un registro relacionado en sample_report_project
                //return redirectSelectReport();
                //por ahora aqui pone el mensaje predeterminado del correo electrónico
                //TODO durante las pruebas, al dar crear reporte en un reporte de un tipo ya existente, se resetea
                //el estado al inicio. Se deberìa de hecho sì poder volver a crear reported de un mismo tipo, tambien, pero hay que ver què serìa
                // lo que cambiaría en cada reporte
                //Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
                //flash.setKeepMessages(true);
                //showError("El reporte "+tipoReport+" para el proyecto "+idProject+" ya existe, y se sobreescribirá", "Ya existen "+sizeReport+" para este proyecto y tipo de reporte");
                System.out.println("Recreando el reporte de " + tipoReport + " para " + current.getIdProject().getProjectName() + " - " + current.getIdProject().getIdProject());
                //resetReport(tipoReport, idProject, us);
                messageFinal = "Su reporte con nombre " + current.getIdProject().getProjectName() + " con tipo de reporte: " + current.getName() + " esta disponible para revisión desde la plataforma de SISBI";
                subjetFinal = "Reporte del proyecto listo para revisión: " + current.getIdProject().getIdProject();

                to_return = redirectFormTypeMethodology(report_projects);

            } else {//Si el no existe un registro relacionado con el proyecto en la tabla report_project
                FieldReportController fieldReportController = new FieldReportController();
                Date date = new Date();
                //String typeMethodology = getTypeMethodologyFromSamples(samples);
                classFieldReport = new FieldReport();
                current = new ReportProject();
                //String idProject = "Project_HSalgado_2018-03-20_10:30:30";
                project = getEjbFacadeProject().find(idProject);
                current.setName(tipoReport);
                current.setStatus("Creado");
                current.setDateCreate(date);
                current.setDateRevise(null);
                current.setDateAuthorize(null);
                current.setPathauthorize(null);
                current.setPathcreate(null);
                current.setPathrevise(null);
                current.setIdProject(project);
                current.setIdUser(us);
                getFacade().create(current);

                int idCurrent = current.getIdReportProject();
                mensaje = "Se registro el documento y el id es " + idCurrent;
                System.out.println(mensaje);
                FacesMessage msj = new FacesMessage(mensaje);
                context.addMessage(null, msj);
                fieldReportController.setVarIdReportProject(idCurrent);
                varIdReportProject = idCurrent;
                messageFinal = "Su reporte con nombre " + current.getIdProject().getProjectName() + " con tipo de reporte: " + current.getName() + " esta disponible para revisión desde la plataforma de SISBI";
                subjetFinal = "Reporte del proyecto listo para revisión: " + current.getIdProject().getIdProject();
                /* ValidacionLuisMuestras :   aqui debe estar tu validacion para las muestras
                De preferencia crea un metodo para hacer todo modular

                por ejemplo par ala validacion y el redireccionamiento para el formulario
                es un metodo que esta en la linea 516: redirectFormReport()
                 */
                to_return = redirectFormTypeMethodology(null);
            }
        } catch (Exception e) {
            showError("Error al crear TypeReportProject", e);
            return "SeleccionTipoReporte.xhtml";
        }
        //List<QualityReports> qualreports = ejbQualityReportsFacade.urlQualityReportsByIdProject(current.getIdProject().getIdProject());
        //qualreports.sort( new SortQualityReportsByRecency());
        //QualityReports qrep = qualreports.get(0);
        //repoEditSample = getSamplesInRun(qrep.getIdRun().getRunName(), repoSample);//secuiencias del proyecto más reciente
        repoSample = sampleController.getItemsProj(project);
        repoEditSample = sampleController.getItemsProj(project);//todas las muestras del proyecto (en otra llamada a funcioǹ, para que sea otra lista, no la de la variable de arriba,
        // y así la lista se pueda mutar sin problema).
        return to_return;
    }

    public String redirectFormTypeMethodology(List<ReportProject> reportes_mismo_proyecto_y_tipo_reporte) {
        System.out.println("Impresion del metodo redirectoFormTypeMethodology  valor del tipo:   " + current.getTypeMethodology());
        if (current.getTypeMethodology() == null) {
            return "selectTypeMethodology?faces-redirect=true&includeViewParams=true";
        } else {
            return redirectSelectionSample(reportes_mismo_proyecto_y_tipo_reporte);
        }
    }

    //Guardar tipo de metodologia seleccionada
    public String saveTypeMethodology(String typeMethodology) {
        current.setTypeMethodology(typeMethodology);
        getFacade().edit(current);
        System.out.println("Se guardó el tipo de metodologia");
        return redirectSelectionSample(null);
    }

    public String redirectFormEditTypeMethodology() {
        return "editTypeMethodology?faces-redirect=true&includeViewParams=true";
    }

    public String redirectFormSelectTypeMethodology() {
        return "selectTypeMethodology?faces-redirect=true&includeViewParams=true";
    }

    public String editTypeMethodology(String typeMethodology) {
        current.setTypeMethodology(typeMethodology);
        getFacade().edit(current);
        System.out.println("Se editó el tipo de metodologia a " + typeMethodology + " en " + current.toString());
        //return "menuReport?faces-redirect=true&includeViewParams=true";
        return redirectSelectionSample(null);
    }

    public String getTypeMethodology() {
        if (current == null) {
            return "(No hay muestra seleccionada)";
        } else {
            String gtm = current.getTypeMethodology();
            if (gtm == null) {
                return "(nulo)";
            } else {
                return gtm;
            }
        }
    }

    public String redirectMenuReportCreate() {
        return redirectFormTypeMethodology(null);
    }

    //Metodos para crear los tipos de reportes
    public String formTypeReport(String typeReport, List<ReportProject> reportes_mismo_proyecto_y_tipo_reporte) {
        String query_parameters;
        if (reportes_mismo_proyecto_y_tipo_reporte == null) {
            query_parameters = "faces-redirect=true&includeViewParams=true";
        } else {
            query_parameters = "faces-redirect=true&includeViewParams=true&similar_report_exists=true";
        }
        switch (typeReport) {
            case "Expresion Diferencial":
                return "formExpresionDiferencial?" + query_parameters;
            case "Analisis Metagenomico":
                return "formAnalisisMetagenomico?" + query_parameters;
            case "Ensamble de Genoma":
                return "formEnsambleGenoma?" + query_parameters;
            case "Transcriptoma de Novo y Expresion Diferencial":
                return "formTranscriptomaNovoExpresionDiferencial?" + query_parameters;
            case "Busqueda de Variantes":
                return "formBusquedaVariantes?" + query_parameters;
            case "SARS-CoV2": //leslie 23 septiembre 
                return "formSarsCov2?" + query_parameters;
            default:
                break;
        }

        return null;
    }
    //Metodos para crear los tipos de reportes --------------------------Final

    //Metodo para crear un fieldrrpot ------------------------------------ Inicio
    public String createFieldReport() {
        getFacade().edit(current);
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

        Map<String, String> query_params = context.
                getExternalContext().getRequestParameterMap();
        try {
            //current = new ReportProject();
            //this.resetReport(varTypeReport, varIdProject, us);
            //current = getFacade().find(varIdReportProject);
            //condicion si existe algun registro de sus campos
            //classFieldReport = new FieldReport();
            classFieldReport.setIdReportProject(current);
            getEjbFacadeFieldReport().create(classFieldReport);
            showMessage("Se registron los campos");
            return "menuReport?faces-redirect=true&includeViewParams=true";

        } catch (Exception e) {
            showError(ResourceBundle.getBundle("/Bundlefield").getString("PersistenceErrorOccured" + varIdReportProject), e.getLocalizedMessage());
            return "formExpresionDiferencial?faces-redirect=true&includeViewParams=true";
        }
    }
    //Metodo para crear un fieldrepot ------------------------------------ Fin

    // metodo para preparar el formulario para editar el los campos del reporte
    public String prepareEditFieldReport() {
        switch (varTypeReport) {
            case "Expresion Diferencial":
                return "editFormExpresionDiferencial?faces-redirect=true&includeViewParams=true";
            case "Analisis Metagenomico":
                return "editFormAnalisisMetagenomico?faces-redirect=true&includeViewParams=true";
            case "Ensamble de Genoma":
                return "editFormEnsambleGenoma?faces-redirect=true&includeViewParams=true";
            case "Transcriptoma de Novo y Expresion Diferencial":
                return "editFormTranscriptomaNovoExpresionDiferencial?faces-redirect=true&includeViewParams=true";
            case "Busqueda de Variantes":
                return "editFormBusquedaVariantes?faces-redirect=true&includeViewParams=true";
            default:
                break;
        }
        return null;
    }

    public String editFieldReport() {
        String mensaje;
        try {
            getEjbFacadeFieldReport().edit(classFieldReport);
            getFacade().edit(current);
            mensaje = "Campos actualizados en ::: " + classFieldReport;
            FacesMessage msj = new FacesMessage(mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msj);
            return "menuReport?faces-redirect=true&includeViewParams=true";
        } catch (Exception e) {
            showError(ResourceBundle.getBundle("/Bundlefield").getString("PersistenceErrorOccured" + varIdReportProject), e.getLocalizedMessage());
            return "Error al editar los campos";
        }

    }

    //Metodo Para obtener las Muestras Actualizadas desde la BD en la tabla sample_report_projec
    public List<SampleReportProject> sampleInSampleReportProject() {
        List<SampleReportProject> rangeListEditSampleReportProject;
        if (varIdProject == null || varTypeReport == null) {
            System.out.println("Como varIdProject or varTypeReport es nulo (!!!!!!), no se pudo obtener sampleInSampleReportProject()");
            rangeListEditSampleReportProject = new ArrayList<>();
        } else {
            ReportProject editRangeReportProject = (ReportProject) ejbFacade.findReportProjectidPType(varIdProject, varTypeReport).get(0);
            int idSampleReportProject = editRangeReportProject.getIdReportProject();//25
            rangeListEditSampleReportProject = (List<SampleReportProject>) ejbSampleRP.EditRangeSample(idSampleReportProject);
        }
        return rangeListEditSampleReportProject;
    }

    public List<SampleReportProject> sampleInSampleReportProject(ReportProject report_project) {
        //TODO dejar esto aqui o ponerlo en la entidad ReportProject
        List<SampleReportProject> rangeListEditSampleReportProject;
        int idSampleReportProject = report_project.getIdReportProject();
        rangeListEditSampleReportProject = (List<SampleReportProject>) ejbSampleRP.EditRangeSample(idSampleReportProject);
        return rangeListEditSampleReportProject;
    }

    public ArrayList<Sample> sampleBySRP() {
        ArrayList<Sample> samp = new ArrayList();
        for (SampleReportProject srp : sampleInSampleReportProject()) {
            Sample sample = srp.getIdSample();
            samp.add(sample);
        }
        return samp;
    }

    public String sample_descr(SampleReportProject srp) {
        Sample sample = srp.getIdSample();
        return sample.getSampleName();
    }

    public String strListSampleBySRP(ReportProject report_project) {
        Stream<String> strsrp = sampleInSampleReportProject(report_project).stream().map(SRP -> SRP.getIdSample().getSampleName()
        );
        return strsrp.collect(Collectors.joining(",\n"));
    }

    //Metodo Para Obtener los datos del Usuario. sin uso, se puede borrar.
    public UserRole userResponsable() {
        Project proj = ejbFacadeProject.find(varIdProject);
        UserRole user = new UserRole();
        List<UserRole> list = ejbFacadeUserRole.findRangeUsersProj(proj.getIdProject());
        for (UserRole userRole : list) {
            System.out.println("Rol del usuario:" + userRole.getRole());
            if (userRole.getRole().equals("Responsable")) {
                user = userRole;
            }
        }
        return user;
    }

    //Metodo Para Obtener los datos de Usuarios
    public List<UserRole> userRole() {
        List<UserRole> list;
        if (varIdProject == null) {
            System.out.println("No se encontró ningún usuario responsable!");
            list = new ArrayList<>();
        } else {
            Project proj = ejbFacadeProject.find(varIdProject);
            list = ejbFacadeUserRole.findRangeUsersProj(proj.getIdProject());
        }
        return list;
    }

    public List<UserRole> userRole(String varIdProject) {
        //Cuando exista la maldita variable varIdProject, no va a ser null!
        List<UserRole> list;
        Project proj = ejbFacadeProject.find(varIdProject);
        list = ejbFacadeUserRole.findRangeUsersProj(proj.getIdProject());
        return list;
    }

    //Metodo para manejar Null a String.
    public String exceptionNull(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            System.out.println("Es null: " + obj);
            return " ";
        }
    }

    //Metodo quitar repeticiones de la palabra millones.
    public String desduplica_millones(Object obj) {
        if (obj != null) {
            String regex = "millones(\\s+millones)+";
            return obj.toString().replaceAll(regex, "millones");
        } else {
            System.out.println("Es null el rendimiento: " + obj);
            return " ";
        }
    }

    public void fieldBD() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        java.util.Date datePer = new Date();
        //Identificacion del Proyecto
        String idProjectname = proj.getIdProject();
        String periodo = periodReport(datePer);//Del Sistema Formatear a: Mes YYYY
        String piePag = proj.getIdProject();
        String idProject = proj.getIdProject();
        String nameProject = proj.getProjectName();
        String desc = proj.getProjectDescription();
        String dateReq = getDateStringFormat(proj.getRequestDate());
        String dateInforme = getCurrentDate();//Del Sistema Formatear a YYYY-MM-dd
        String dateAnalys = getCurrentDate();//Del Sistema Formatear a YYYY-MM-dd
        String numSample = String.valueOf(rangoSampleVal.size());

        //Identificacion del Usuario y Colaboradores
        UserRole user = new UserRole();

        List<UserRole> list = ejbFacadeUserRole.findRangeUsersProj(proj.getIdProject());
        for (UserRole userRole : list) {
            //System.out.println("Rol del usuario:" + userRole.getRole());
            if (userRole.getRole().equals("Responsable")) {
                user = userRole;
            }
        }
        String idUser = user.getUserName();
        String nomCompUser = user.getFirstName() + " " + user.getPLastName() + " " + user.getMLastName();
        String depUser = user.getDependencyName();
        String phoneUser = user.getPhoneNumber();
        String emailUser = user.getEmail();
        String roleUser = user.getRole();

        System.out.println("idProjectname:" + idProjectname);
        System.out.println("periodo:" + periodo);
        System.out.println("piePag:" + piePag);
        System.out.println("idProject:" + idProject);
        System.out.println("nameProject:" + nameProject);
        System.out.println("desc:" + desc);
        System.out.println("dateReq:" + dateReq);
        System.out.println("dateInforme:" + dateInforme);
        System.out.println("dateAnalys:" + dateAnalys);
        System.out.println("numSample:" + numSample);
        System.out.println("idUser:" + idUser);
        System.out.println("nomCompUser:" + nomCompUser);
        System.out.println("depUser:" + depUser);
        System.out.println("phoneUser:" + phoneUser);
        System.out.println("emailUser:" + emailUser);
        System.out.println("roleUser:" + roleUser);
    }

    public Project projectSampleReportProject() {
        Project proj = ejbFacadeProject.find(varIdProject);
        return proj;
    }

    public void downloadReport(String ruteDoc, String localBaseName) throws FileNotFoundException, IOException {
        try {
            File ficheroXLS = new File(ruteDoc);
            FacesContext ctx2 = FacesContext.getCurrentInstance();
            FileInputStream fis2 = new FileInputStream(ficheroXLS);
            byte[] bytes = new byte[1000];
            int read = 0;

            if (!ctx2.getResponseComplete()) {
                //String fileName2 = ficheroXLS.getName();
                String contentType;
                if (localBaseName.endsWith(".pdf")) {
                    contentType = "application/pdf";
                } else {
                    contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                }
                HttpServletResponse response = (HttpServletResponse) ctx2.getExternalContext().getResponse();
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment;filename=\"" + localBaseName + "\"");
                ServletOutputStream out = response.getOutputStream();

                while ((read = fis2.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
                System.out.println("\nDescargado " + ruteDoc + ". Encabezado de nombre " + localBaseName + "\n");
                ctx2.responseComplete();
            }
            showMessage("Descarga Finalizada de " + ruteDoc);
            fis2.close();
            //ficheroXLS.delete();
            //System.out.println("Archivo Eliminado");
        } catch (IOException e) {
            showError("Error en downloadReport:", e);
        }
    }

    public String dateQualityAnalisys(String idSample) {
        List<Comments> listDateQA = ejbCommentsFacade.dateQualityAnalysis(idSample);
        int sizeListDateQA = listDateQA.size();
        String dateQA;

        if (sizeListDateQA > 0) {
            dateQA = getDateStringFormat(listDateQA.get(0).getCommentDate());
        } else {
            dateQA = " ";
        }
        return dateQA;
    }

    public String urlQualityReports(String id_Project) {
        String url;
        List<QualityReports> qualreports = ejbQualityReportsFacade.urlQualityReportsByIdProject(id_Project);
        if (qualreports.isEmpty()) {
            url = "http://www.sisbi-sin-liga.com";
        } else {
            qualreports.sort(new SortQualityReportsByRecency());
            url = qualreports.get(0).getUrlQualityReport();
        }
        return url;
    }

    public XWPFHyperlinkRun createHyperlinkRun(XWPFParagraph paragraph, String uri) {
        String rId = paragraph.getDocument().getPackagePart().addExternalRelationship(
                uri,
                XWPFRelation.HYPERLINK.getRelation()
        ).getId();

        CTHyperlink cthyperLink = paragraph.getCTP().addNewHyperlink();
        cthyperLink.setId(rId);
        cthyperLink.addNewR();

        return new XWPFHyperlinkRun(
                cthyperLink,
                cthyperLink.getRArray(0),
                paragraph
        );
    }

    void tablas_datos_proyecto(XWPFDocument doc, Project proj, java.util.Date dateInforme, java.util.Date dateAnalisis, List<Sample> all_project_samples, List<Sample> selected_samples) throws FileNotFoundException, InvalidFormatException, IOException {
        //Tablas de Datos del Proyecto
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            for (int ipos = 0; ipos < 10; ipos++) {
                                //Cada subindice de getText es un elemento de texto separado de otros por un retorno de carro
                                //No encuentro como sacar el número de estos elementos, pero un màximo de la cantidad de estos
                                //elementos de 10 me parece razonable
                                String text;
                                try {
                                    text = r.getText(ipos);
                                } catch (java.lang.IndexOutOfBoundsException e) {
                                    break;
                                }
                                if (text != null) {
                                    if (text.contains("FIELDBD")) {
                                        text = text.replace("FIELDBDc", proj.getIdProject());
                                        text = text.replace("FIELDBDd", proj.getProjectName());
                                        text = text.replace("FIELDBDe", proj.getProjectDescription());
                                        text = text.replace("FIELDBDf", getDateStringFormat(proj.getRequestDate()));
                                        text = text.replace("FIELDBDg", String.valueOf(all_project_samples.size()));
                                        text = text.replace("FIELDBDk", String.valueOf(selected_samples.size()));
                                        r.setText(text, ipos);
                                    }
                                    if (text.contains("FIELDMANUAL")) {
                                        text = text.replace("FIELDMANUALa", getDateStringFormat(dateInforme));
                                        text = text.replace("FIELDMANUALb", getDateStringFormat(dateAnalisis));
                                        r.setText(text, ipos);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            for (XWPFRun r : paragraph.getRuns()) {
                for (int ipos = 0; ipos < 10; ipos++) {
                    String text;
                    try {
                        text = r.getText(ipos);
                    } catch (java.lang.IndexOutOfBoundsException e) {
                        break;
                    }
                    if (text != null) {
                        //Datos de Portada.
                        if (text.contains("FIELDBD") || text.contains("FIELDSYSTEM")) {
                            text = text.replace("FIELDBDa", proj.getIdProject());
                            text = text.replace("FIELDSYSTEMa", periodReport(dateInforme));
                            r.setText(text, ipos);
                        }
                    }
                }
            }
        }
        //Tabla de contenido
        System.out.println("Se reemplaza las tablas de Datos del Proyecto");
    }

    void tablas_datos_proyecto_sample_qc(XWPFDocument doc, Project proj, java.util.Date dateInforme, java.util.Date dateAnl, List<Sample> all_project_samples) throws FileNotFoundException, InvalidFormatException, IOException {
        //Tablas de Datos del Proyecto
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            for (int ipos = 0; ipos < 10; ipos++) {
                                String text;
                                try {
                                    text = r.getText(ipos);
                                } catch (java.lang.IndexOutOfBoundsException e) {
                                    break;
                                }
                                if (text != null) {
                                    if (text.contains("FIELDBD")) {

                                        text = text.replace("FIELDBDc", proj.getIdProject());
                                        text = text.replace("FIELDBDd", proj.getProjectName());
                                        text = text.replace("FIELDBDe", proj.getProjectDescription());
                                        text = text.replace("FIELDBDf", getDateStringFormat(proj.getRequestDate()));//Fecha de solicitud
                                        text = text.replace("FIELDBDg", String.valueOf(all_project_samples.size()));
                                        text = text.replace("FIELDBDk", String.valueOf(all_project_samples.size()));
                                        r.setText(text, ipos);
                                    }
                                    if (text.contains("FIELDMANUAL")) {
                                        text = text.replace("FIELDMANUALa", getDateStringFormat(dateInforme));//Fecha de informe
                                        text = text.replace("FIELDMANUALb", getDateStringFormat(dateAnl));//Fecha de analisis
                                        r.setText(text, ipos);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        for (XWPFParagraph paragraph : doc.getParagraphs()) {
            for (XWPFRun r : paragraph.getRuns()) {
                for (int ipos = 0; ipos < 10; ipos++) {
                    String text;
                    try {
                        text = r.getText(ipos);
                    } catch (java.lang.IndexOutOfBoundsException e) {
                        break;
                    }
                    if (text != null) {
                        //Datos de Portada.
                        if (text.contains("FIELDBD") || text.contains("FIELDSYSTEM")) {
                            text = text.replace("FIELDBDa", proj.getIdProject());
                            text = text.replace("FIELDSYSTEMa", periodReport(dateInforme));
                            r.setText(text, ipos);
                        }
                    }
                }
            }
        }
        //Tabla de contenido
        System.out.println("Se reemplaza las tablas de Datos del Proyecto");
    }

    void copia_parrafo(XWPFDocument destiny, XWPFParagraph paragraph) {
        XWPFParagraph ndp = destiny.createParagraph();
        ndp.setStyle(paragraph.getStyle());
        ndp.setAlignment(paragraph.getAlignment());
        for (XWPFRun r : paragraph.getRuns()) {
            XWPFRun new_run = null;
            System.out.println("llego a copia_parrafo");
            for (int ipos = 0; ipos < 100; ipos++) {
                String text;
                try {
                    text = r.getText(ipos);
                } catch (java.lang.IndexOutOfBoundsException e) {
                    System.out.println("manda excepion indexout");
                    break;
                }
                if (ipos == 99) {
                    int y = 3;
                }
                if (ipos == 0) {
                    new_run = crea_corrida_con_texto_y_formato(ndp, text, r);
                } else {
                    new_run.addBreak(BreakType.TEXT_WRAPPING);
                    new_run.setText(text, ipos);
                }
            }
        }
    }

    void copia_parrafo(XWPFParagraph destiny, XWPFParagraph paragraph) {
        destiny.setStyle(paragraph.getStyle());
        destiny.setAlignment(paragraph.getAlignment());

        for (XWPFRun r : paragraph.getRuns()) {
            XWPFRun new_run = null;
            for (int ipos = 0; ipos < 100; ipos++) {
                String text;
                try {
                    text = r.getText(ipos);
                } catch (java.lang.IndexOutOfBoundsException e) {
                    break;
                }
                if (ipos == 99) {
                    int y = 3;
                }
                if (ipos == 0) {
                    new_run = crea_corrida_con_texto_y_formato(destiny, text, r);
                } else {
                    new_run.addBreak(BreakType.TEXT_WRAPPING);
                    new_run.setText(text, ipos);
                }
            }
        }
    }

    void copia_tabla(XWPFDocument destiny, XWPFTable table) {
        int irow = 0;
        XWPFTable destinyTable = destiny.createTable(table.getNumberOfRows(), 1);
        destinyTable.setColBandSize(table.getColBandSize());
        destinyTable.setRowBandSize(table.getRowBandSize());
        if (table.getTableAlignment() != null) {
            destinyTable.setTableAlignment(table.getTableAlignment());
        }
        for (XWPFTableRow row : table.getRows()) {
            XWPFTableRow destrow = destinyTable.getRow(irow++);
            int icol = 0;
            for (XWPFTableCell cell : row.getTableCells()) {
                XWPFTableCell destCell;
                if (icol == 0) {
                    destCell = destrow.getCell(icol);
                } else {
                    destCell = destrow.addNewTableCell();
                }
                for (XWPFParagraph par : cell.getParagraphs()) {
                    XWPFParagraph new_paragraph;
                    new_paragraph = destCell.addParagraph();
                    copia_parrafo(new_paragraph, par);
                }
                try {
                    XWPFVertAlign xpwfva = cell.getVerticalAlignment();
                    if (xpwfva != null) {
                        destCell.setVerticalAlignment(xpwfva);
                    }
                } catch (NullPointerException exc) {
                    //No hace nada
                }
                destCell.setColor(cell.getColor());
                destCell.setWidthType(cell.getWidthType());
                destCell.setWidth(String.valueOf(cell.getWidth()));
                icol++;
            }
            try {
                destrow.setHeightRule(row.getHeightRule());
            } catch (NullPointerException e) {
                //
            }
            destrow.setHeight(row.getHeight());
        }
        format_table(destinyTable);
    }

    void tabla_pie_pagina_usuario_responsable(XWPFDocument doc, Project proj) throws IOException, XmlException {
        //Pie de Pagina
        XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(doc);
        XWPFFooter footer = policy.getDefaultFooter();
        for (XWPFParagraph p : footer.getParagraphs()) {
            for (XWPFRun r : p.getRuns()) {
                String text = r.getText(0);
                if (text != null && text.contains("FIELDBD")) {
                    text = text.replace("FIELDBDb", proj.getIdProject());
                    r.setText(text, 0);
                }
            }
        }
        System.out.println("Se reemplaza el footer"); //hasta aca llega  bien

        //Tabla para Datos del Usuario Responsable.
        XWPFTable tableUserRole = doc.getTables().get(2);
        for (UserRole usRole : userRole(proj.getIdProject())) {
            XWPFTableRow tableRowTwo = tableUserRole.createRow();
            tableRowTwo.getCell(0).setText(usRole.getUserName());
            tableRowTwo.getCell(1).setText(usRole.getFirstName() + " " + usRole.getPLastName() + " " + exceptionNull(usRole.getMLastName()));
            tableRowTwo.getCell(2).setText(usRole.getDependencyName());
            tableRowTwo.getCell(3).setText(usRole.getPhoneNumber());
            tableRowTwo.getCell(4).setText(usRole.getEmail());
            tableRowTwo.getCell(5).setText(usRole.getRole());
            for (XWPFTableCell cell : tableRowTwo.getTableCells()) {
                cell.getParagraphs().get(0).setStyle("LO-normal");
            }
        }
        format_table(tableUserRole);
        System.out.println("Se reemplaza:   Tabla para Datos del Usuario Responsable."); //aca llega bien
    }

    ArrayList<Sample> muestras_validas(List<Sample> samples) {
        // Aquí obtiene las muestras que son válidas para bioinformática
        /*
                Estados de aceptation:
        Rechazada
        Condicionada
        Aceptada
        también hay, sin deber, null, hello y una cadena vacía

         */
        ArrayList<Sample> mstras_validas = new ArrayList();
        for (Sample sample : samples) {
            String aceptacion = sample.getAceptation();
            if (aceptacion != null && (aceptacion.equals("Aceptada") || aceptacion.equals("Condicionada"))) {
                mstras_validas.add(sample);
            }
        }
        return mstras_validas;
    }

    boolean hay_muestra_condicionada_o_rechazada(List<Sample> samples) {
        for (Sample sample : samples) {
            String acc = sample.getAceptation();
            if (acc != null && (acc.equals("Condicionada") || acc.equals("Rechazada"))) {
                return true;
            }
        }
        return false;
    }

    void format_table(XWPFTable tbl) {
        tbl.setTopBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 1, tbl.getTopBorderColor());
        tbl.setBottomBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 1, tbl.getBottomBorderColor());
        tbl.setLeftBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 1, tbl.getLeftBorderColor());
        tbl.setRightBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 1, tbl.getRightBorderColor());
        tbl.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 1, tbl.getInsideHBorderColor());
        tbl.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 1, tbl.getInsideVBorderColor());
    }

    void tablas_info_muestras(XWPFDocument doc, Project proj, List<Sample> all_project_samples, List<Sample> muestras_seleccionadas, String typeMethodology) throws FileNotFoundException, InvalidFormatException, IOException {
        //Tabla Descripcion de las Muestras seleccionadas para actualizacion de estatus.
        //ArrayList<Sample> mstras_validas = muestras_validas(samples);
        //boolean hay_mstra_condicionada_o_rechazada = hay_muestra_condicionada_o_rechazada(mstras_validas);
        boolean samples_are_RNA = typeMethodology.contains("RNA");
        XWPFTable tableSampleDesc = doc.getTables().get(3);
        for (Sample sampleDesc : all_project_samples) {
            XWPFTableRow tableRowSampleDesc = tableSampleDesc.createRow();
            //tableRowSampleDesc.getCell(0).
            tableRowSampleDesc.getCell(0).setText(sampleDesc.getIdSample().toString());
            tableRowSampleDesc.getCell(1).setText(sampleDesc.getSampleName());
            tableRowSampleDesc.getCell(2).setText(exceptionNull(sampleDesc.getIdTube()));
            tableRowSampleDesc.getCell(3).setText(getDateStringFormat(sampleDesc.getReceptionDate()));
            tableRowSampleDesc.getCell(4).setText(dateQualityAnalisys(sampleDesc.getIdSample().toString()));//la fecha de Analisis,necesita algoritmo de busqueda.
            tableRowSampleDesc.getCell(5).setText(exceptionNull(sampleDesc.getAceptation()));
        }
        format_table(tableSampleDesc);
        System.out.println("Se reemplaza: Tabla Descripcion de las Muestras seleccionadas para actualizacion de estatus."); //aqui imprime la linea y empeiza  a mandar los  null

        //Tabla Comentarios de Muestras Seleccionadas.
        XWPFTable tableSampleComms = doc.getTables().get(4);
        for (Sample sampleComms : muestras_seleccionadas) {
            XWPFTableRow tableRowSampleComms = tableSampleComms.createRow();
            tableRowSampleComms.getCell(0).setText(sampleComms.getIdSample().toString());
            tableRowSampleComms.getCell(1).setText(sampleComms.getSampleName());
            tableRowSampleComms.getCell(2).setText(exceptionNull(sampleComms.getIdTube()));

            XWPFTableCell cell_3 = tableRowSampleComms.getCell(3);
            // usar idtype de la tabla comments, cuando el tipo de comentario es comment
            List<Comments> comments = ejbFacade.non_automated_commentsByIdSample(sampleComms.getIdSample());
            for (Comments comment : comments) {
                XWPFParagraph paragraph = cell_3.addParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(comment.getComment().trim() + "\n");
            }
        }
        format_table(tableSampleComms);
        System.out.println("Se reemplaza:  Tabla Comentarios de Muestras Seleccionadas");

        //Tabla Descripcion de Volumen y Concenracion de Muestras Seleccionadas
        XWPFTable tableSampleVol = doc.getTables().get(5);
        for (Sample sampleVol : muestras_seleccionadas) {
            XWPFTableRow tableRowSampleVol = tableSampleVol.createRow();
            tableRowSampleVol.getCell(0).setText(sampleVol.getIdSample().toString());
            tableRowSampleVol.getCell(1).setText(sampleVol.getSampleName());
            tableRowSampleVol.getCell(2).setText(exceptionNull(sampleVol.getIdTube()));
            tableRowSampleVol.getCell(3).setText(exceptionNull(sampleVol.getSampleVolume()));//Vol usuario
            tableRowSampleVol.getCell(4).setText(exceptionNull(sampleVol.getLabVolume()));//Vol uusmb
            tableRowSampleVol.getCell(5).setText(exceptionNull(sampleVol.getLabConcen()));//Concentracion
        }
        format_table(tableSampleVol);
        System.out.println("Se reemplaza:  Tabla Descripcion de Volumen y Concenracion de Muestras Seleccionadas");

        //Tabla Absorbancia de Muestras Seleccionadas.
        XWPFTable tableSampleAbs = doc.getTables().get(6);
        if (samples_are_RNA) {
            XWPFTableRow title_row = tableSampleAbs.getRow(0);
            tableSampleAbs.removeRow(1);
            // total width 12895
            title_row.removeCell(4);
            title_row.removeCell(3);
            title_row.getCell(0).setWidth("1700");
            title_row.getCell(1).setWidth("6000");
            title_row.getCell(2).setWidth("1700");
            title_row.getCell(3).setWidth("1700");
            title_row.getCell(3).setWidth("1700");
            for (Sample sampleAbs : muestras_seleccionadas) {
                XWPFTableRow tableRowSampleAbs = tableSampleAbs.createRow();
                tableRowSampleAbs.getCell(0).setWidth("1700");
                tableRowSampleAbs.getCell(0).setText(sampleAbs.getIdSample().toString());
                tableRowSampleAbs.getCell(1).setWidth("6000");
                tableRowSampleAbs.getCell(1).setText(sampleAbs.getSampleName());
                tableRowSampleAbs.getCell(2).setWidth("1700");
                tableRowSampleAbs.getCell(2).setText(exceptionNull(sampleAbs.getIdTube()));
                tableRowSampleAbs.getCell(3).setWidth("1700");
                tableRowSampleAbs.getCell(3).setText(exceptionNull(sampleAbs.getType()));//Tipo
                XWPFTableCell expPerformanceCell = tableRowSampleAbs.getCell(4);
                String sample_platform = sampleAbs.getSamplePlataform();
                expPerformanceCell.setWidth("1700");
                if (sample_platform == null || sample_platform.equals("Oxford Nanopore")) {
                    expPerformanceCell.setText(desduplica_millones(sampleAbs.getExpectedPerformanceOxford()));//Rendimiento: Expected Oxford
                } else {
                    expPerformanceCell.setText(desduplica_millones(sampleAbs.getExpectedPerformance()));//Rendimiento: Expected Illumina
                }
            }
        } else {
            for (Sample sampleAbs : muestras_seleccionadas) {
                XWPFTableRow tableRowSampleAbs = tableSampleAbs.createRow();
                tableRowSampleAbs.getCell(0).setText(sampleAbs.getIdSample().toString());
                tableRowSampleAbs.getCell(1).setText(sampleAbs.getSampleName());
                tableRowSampleAbs.getCell(2).setText(exceptionNull(sampleAbs.getIdTube()));
                tableRowSampleAbs.getCell(3).setText(exceptionNull(sampleAbs.getAbs260_280()));//Usuario 260_280
                tableRowSampleAbs.getCell(4).setText(exceptionNull(sampleAbs.getAbs260_230()));//Usuario 260_230
                tableRowSampleAbs.getCell(5).setText(exceptionNull(sampleAbs.getAbs260_280_usmb()));//Uusmb 260_280
                tableRowSampleAbs.getCell(6).setText(exceptionNull(sampleAbs.getAbs260_230_usmb()));//Uusmb 260_230
                // addNewTableCell o createCell?
                XWPFTableCell typeCell = tableRowSampleAbs.addNewTableCell();
                typeCell.setText(exceptionNull(sampleAbs.getType()));//Tipo
                XWPFTableCell expPerformanceCell = tableRowSampleAbs.addNewTableCell();
                String sample_platform = sampleAbs.getSamplePlataform();
                if (sample_platform == null || sample_platform.equals("Oxford Nanopore")) {
                    expPerformanceCell.setText(desduplica_millones(sampleAbs.getExpectedPerformanceOxford()));//Rendimiento: Expected Oxford
                } else {
                    expPerformanceCell.setText(desduplica_millones(sampleAbs.getExpectedPerformance()));//Rendimiento: Expected Illumina
                }
            }
        }
        format_table(tableSampleAbs);
        System.out.println("Se reemplaza: Tabla Absorbancia de Muestras Seleccionadas.");
    }

    void tablas_info_muestras_sample_qc(XWPFDocument doc, Project proj, List<Sample> all_project_samples) throws FileNotFoundException, InvalidFormatException, IOException {
        //Tabla Descripcion de las Muestras seleccionadas para actualizacion de estatus.
        //ArrayList<Sample> mstras_validas = muestras_validas(samples);
        //boolean hay_mstra_condicionada_o_rechazada = hay_muestra_condicionada_o_rechazada(mstras_validas);
        XWPFTable tableSampleDesc = doc.getTables().get(3);
        for (Sample sampleDesc : all_project_samples) {
            XWPFTableRow tableRowSampleDesc = tableSampleDesc.createRow();
            tableRowSampleDesc.getCell(0).setText(sampleDesc.getIdSample().toString());
            tableRowSampleDesc.getCell(1).setText(sampleDesc.getSampleName());
            tableRowSampleDesc.getCell(2).setText(exceptionNull(sampleDesc.getIdTube()));
            tableRowSampleDesc.getCell(3).setText(getDateStringFormat(sampleDesc.getReceptionDate()));
            tableRowSampleDesc.getCell(4).setText(dateQualityAnalisys(sampleDesc.getIdSample().toString()));//la fecha de Analisis,necesita algoritmo de busqueda.
            tableRowSampleDesc.getCell(5).setText(exceptionNull(sampleDesc.getAceptation()));
        }
        format_table(tableSampleDesc);
        System.out.println("Se reemplaza: Tabla Descripcion de las Muestras seleccionadas para actualizacion de estatus."); //aqui imprime la linea y empeiza  a mandar los  null

        //Tabla Comentarios de Muestras Seleccionadas.
        XWPFTable tableSampleComms = doc.getTables().get(4);
        for (Sample sampleComms : all_project_samples) {
            XWPFTableRow tableRowSampleComms = tableSampleComms.createRow();
            tableRowSampleComms.getCell(0).setText(sampleComms.getIdSample().toString());
            tableRowSampleComms.getCell(1).setText(sampleComms.getSampleName());
            tableRowSampleComms.getCell(2).setText(exceptionNull(sampleComms.getIdTube()));

            XWPFTableCell cell_3 = tableRowSampleComms.getCell(3);
            // usar idtype de la tabla comments, cuando el tipo de comentario es comment
            List<Comments> comments = ejbFacade.non_automated_commentsByIdSample(sampleComms.getIdSample());
            for (Comments comment : comments) {
                XWPFParagraph paragraph = cell_3.addParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(comment.getComment() + "\n");
            }
        }
        format_table(tableSampleComms);
        System.out.println("Se reemplaza:  Tabla Comentarios de Muestras Seleccionadas");
    }

    private String sufijo_tipo_muestra(String typeMethodology) {
        if (null == typeMethodology) {
            System.out.println("tipoMetNull");
            return "";
        } else {
            switch (typeMethodology) {
                case "Perfiles taxonómicos de amplicones":
                case "Amplicones 16SoITS":
                    //Era 16SoITS
                    System.out.println("tipoMetA16");
                    return "A16";
                case "DNA":
                    System.out.println("tipoMetDNA");
                    return "DNA";
                case "RNAseq Bac":
                    System.out.println("tipoMetRNAB");
                    return "RNAB";
                case "RNAseq Euc":
                    System.out.println("tipoMetRNAE");
                    return "RNAE";
                default:
                    System.out.println("tipoMetExtraño " + typeMethodology);
                    return "";
            }
        }

    }

    public XWPFDocument mergeMethodsIn(XWPFDocument document, XWPFDocument to_merge, Project proj) {
        // 'document' will be mutated with the contents of to_merge
        // For now this document only merger in paragraphs
        List<XWPFParagraph> paragraphs = to_merge.getParagraphs();
        for (XWPFParagraph paragraph_to_merge : paragraphs) {
            XWPFParagraph new_paragraph = document.createParagraph();
            try {
                String style = paragraph_to_merge.getStyle();
                new_paragraph.setStyle(style);
                System.out.println("mess desde  a mergemethodsIn");
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                // do nothing, as often let to be the single option
                System.out.println(" entro la excepcion desde merge methodsin");
            }
            for (XWPFRun original_run : paragraph_to_merge.getRuns()) {
                XWPFRun new_run = new_paragraph.createRun();
                String text = original_run.getText(0);
                if (text != null) {
                    //Se va a llamar FIELDBDj, pero se me hace que los nombre de campos se pueden comprimir quizá en algún ticket.
                    if (text.contains("FIELDBDj")) {
                        List<QualityReports> qualreports = ejbQualityReportsFacade.urlQualityReportsByIdProject(proj.getIdProject());
                        if (qualreports.isEmpty()) {
                            text = text.replace("FIELDBDj", "(sin reporte de FasstQC)");
                        } else {
                            QualityReports qrep = qualreports.get(0);
                            String run_name = qrep.getIdRun().getRunName();
                            text = text.replace("FIELDBDj", run_name);
                        }

                        System.out.println("agrega url  o no si hay corrida");
                    }
                    new_run.setText(text);
                }
                if (original_run.isItalic()) {
                    new_run.setItalic(true);
                }
                if (original_run.isBold()) {
                    new_run.setBold(true);
                }
                if (original_run.isStrikeThrough()) {
                    new_run.setStrikeThrough(true);
                }
                //new_run.setVerticalAlignment(original_run.getVerticalAlignment());
            }
            System.out.println("saliendo del metodo");
        }
        return document;
    }

    public XWPFDocument mergeQualityReportIn(XWPFDocument document, XWPFDocument to_merge, Project proj, String field_manual_c) {
        // 'document' will be muteted with the contents of to_merge
        // For now this document only merger in paragraphs, not tables
        List<XWPFParagraph> paragraphs = to_merge.getParagraphs();
        for (XWPFParagraph paragraph_to_merge : paragraphs) {
            XWPFParagraph new_paragraph = document.createParagraph();
            String style = paragraph_to_merge.getStyle();
            new_paragraph.setStyle(style);
            for (XWPFRun original_run : paragraph_to_merge.getRuns()) {
                XWPFRun new_run = new_paragraph.createRun();
                String text = original_run.getText(0);
                if (text != null) {
                    if (text.contains("FIELDMANUAL")) {
                        text = text.replace("FIELDMANUALc", field_manual_c);
                    }
                    //Reporte de calidad
                    if (text.contains("FIELDBDh")) {
                        text = text.replace("FIELDBDh", "");
                        String urlQualReports = urlQualityReports(proj.getIdProject());
                        XWPFHyperlinkRun hyperlinkrun = createHyperlinkRun(new_paragraph, urlQualReports);
                        hyperlinkrun.setText(urlQualReports);
                        hyperlinkrun.setColor("0000FF");
                        hyperlinkrun.setUnderline(UnderlinePatterns.SINGLE);
                    }
                    new_run.setText(text);
                    if (original_run.isItalic()) {
                        new_run.setItalic(true);
                    }
                    if (original_run.isBold()) {
                        new_run.setBold(true);
                    }
                    if (original_run.isStrikeThrough()) {
                        new_run.setStrikeThrough(true);
                    }
                    //new_run.setSubscript(original_run.getSubscript());
                }
            }
        }
        return document;
    }

    private void resultados_ED(XWPFDocument doc, XWPFDocument destino, FieldReport itemFieldReport, Project proj, java.util.Date datePer) {
        String IDEAMex_link;
        IDEAMex_link = "http://www.uusmb.unam.mx/ideamex/" + itemFieldReport.getField17();//¿O 40?
        for (XWPFParagraph p : doc.getParagraphs()) {
            XWPFParagraph destino_p = destino.createParagraph();
            destino_p.setStyle(p.getStyle());
            List<XWPFRun> runs = p.getRuns();
            int i = 0;
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null) {
                        if (text.contains("FIELDMANUAL")) {
                            text = text.replace("FIELDMANUALd", itemFieldReport.getField4());
                            text = text.replace("FIELDMANUALe", itemFieldReport.getField5());
                            text = text.replace("FIELDMANUALf", itemFieldReport.getField6());
                            text = text.replace("FIELDMANUALg", itemFieldReport.getField7());
                            text = text.replace("FIELDMANUALh", itemFieldReport.getField8());
                            text = text.replace("FIELDMANUALi", itemFieldReport.getField9());
                            text = text.replace("FIELDMANUALj", itemFieldReport.getField10());
                            text = text.replace("FIELDMANUALk", itemFieldReport.getField11());
                            text = text.replace("FIELDMANUALl", itemFieldReport.getField12());
                            text = text.replace("FIELDMANUALm", itemFieldReport.getField13());
                            text = text.replace("FIELDMANUALn", itemFieldReport.getField14());
                            text = text.replace("FIELDMANUALo", itemFieldReport.getField15());
                            text = text.replace("FIELDMANUALp", itemFieldReport.getField16());
                            text = text.replace("FIELDMANUALq", IDEAMex_link);//17
                            text = text.replace("FIELDMANUALr", itemFieldReport.getField18());
                            text = text.replace("FIELDMANUALs", IDEAMex_link + "/VennDiagram.php");
                            // text = text.replace("FIELDMANUALt", itemFieldReport.getField20());
                            // text = text.replace("FIELDMANUALu", itemFieldReport.getField21());
                            // text = text.replace("FIELDMANUALv", itemFieldReport.getField22());
                            // text = text.replace("FIELDMANUALw", itemFieldReport.getField23());
                            // text = text.replace("FIELDMANUALx", itemFieldReport.getField24());
                            // text = text.replace("FIELDMANUALy", itemFieldReport.getField25());
                            // 4 carpetas
                            text = text.replace("FIELDMANUALz", IDEAMex_link + "/EdgeR.php");//16 mencionado por Vero. Campo 26.
                            text = text.replace("FIELDMANUALZa", IDEAMex_link + "/DESeq2.php");//17, campo 27
                            text = text.replace("FIELDMANUALZb", IDEAMex_link + "/limma.php");//18, campo 28
                            text = text.replace("FIELDMANUALZc", IDEAMex_link + "/NOISeq.php");//19, campo 29

                            // TODO substituir G6, L6, Lactosa y Glucosa por variables
                            //String txts1= IDEAMex_link+"/edgeR_Results/G6vsL6.txt\n"+
                            //      IDEAMex_link+"/edgeR_Results/L6vsLactosa.txt\n"+
                            //    IDEAMex_link+"/edgeR_Results/GlucosavsLactosa.txt";//22
                            //text = text.replace("FIELDMANUALZd", txts1);//campo 30
                            //text = text.replace("FIELDMANUALZe", itemFieldReport.getField31());
                            //text = text.replace("FIELDMANUALZf", itemFieldReport.getField32());
                            //text = text.replace("FIELDMANUALZg", itemFieldReport.getField33());
                            //text = text.replace("FIELDMANUALZh", itemFieldReport.getField34());
                            //  text = text.replace("FIELDMANUALZi", itemFieldReport.getField35());
                            // Tops (el basename del archivo en la secciòn anterior+"_TOP.txt")
                            //String txts2=IDEAMex_link+"/edgeR_Results/G6vsL6_TOP.txt\n"+
                            //      IDEAMex_link+"/edgeR_Results/L6vsLactosa_TOP.txt\n"+
                            //    IDEAMex_link+"/edgeR_Results/GlucosavsLactosa_TOP.txt";//25
                            //text = text.replace("FIELDMANUALZj", txts2);
                            // Secciones (el nombre del archivo de la penúltima sección+"_Intersect.txt")
                            //String txts3=IDEAMex_link+"/edgeR_Results/G6vsL6_Intersect.txt\n"+
                            //      IDEAMex_link+"/edgeR_Results/L6vsLactosa_Intersect.txt\n"+
                            //    IDEAMex_link+"/edgeR_Results/GlucosavsLactosa_Intersect.txt";
                            //text = text.replace("FIELDMANUALZk", txts3);
                            //text = text.replace("FIELDMANUALZl", itemFieldReport.getField38());
                            //text = text.replace("FIELDMANUALZm", itemFieldReport.getField39());
                            //text = text.replace("FIELDMANUALZn", itemFieldReport.getField40());
                            System.out.println("Se reemplazan todos los FIELDMANUAL");
                        }

                        if (text.contains("FIELDBDi")) {
                            try {
                                text = text.replace("FIELDBDi", String.valueOf(rangoSampleVal.size()));
                            } catch (Exception e) {
                                System.out.println("Error al cambiar el numero de muestras,no hay FIELDBDi");
                            }
                        }
                        if (text.contains("FIELDEDTABLE")) {
                            System.out.println("llego al FIELDTABLE");
                            text = text.replace("FIELDEDTABLE", "");
                            XWPFTable tbl = destino.createTable(4, 2);
                            XWPFParagraph title0 = tbl.getRow(0).getCell(0).getParagraphs().get(0);
                            XWPFRun run0 = title0.createRun();

                            run0.setText("Ensamblado");
                            run0.setBold(true);
                            System.out.println("asigna setText Ensamblado");
                            XWPFParagraph title1 = tbl.getRow(0).getCell(1).getParagraphs().get(0);
                            XWPFRun run1 = title1.createRun();
                            run1.setText("Número de transcritos");
                            run1.setBold(true);
                            System.out.println("asigna setText #transcritos");
                            tbl.getRow(1).getCell(0).setText("trinity_itemFieldReport.getField26()/Trinity.fasta");
                            tbl.getRow(1).getCell(1).setText("itemFieldReport.getField27()");
                            tbl.getRow(2).getCell(0).setText("trinity_itemFieldReport.getField28()/Trinity.fasta");
                            tbl.getRow(2).getCell(1).setText("itemFieldReport.getField29()");
                            tbl.getRow(3).getCell(0).setText("trinity_itemFieldReport.getField30()/Trinity.fasta");
                            tbl.getRow(3).getCell(1).setText("itemFieldReport.getField31()");
                        }
                        crea_corrida_con_texto_y_formato(destino_p, text, r);
                    }

                }//fin for
                i = i++;
                System.out.println("Se reemplazan todos los FIELDMANUAL vuelta" + i);
            }
        }
    }

    void resultados_AM(XWPFDocument doc, XWPFDocument destino, FieldReport itemFieldReport, Project proj, java.util.Date datePer) {
        for (XWPFParagraph p : doc.getParagraphs()) {
            XWPFParagraph destino_p = destino.createParagraph();
            destino_p.setStyle(p.getStyle());
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    if (r != null) {
                        String text = r.getText(0);
                        if (text != null) {
                            crea_corrida_con_texto_y_formato(destino_p, text, r);
                        }
                    }
                }
            }
        }
    }

    void resultados_EG(XWPFDocument doc, XWPFDocument destino, FieldReport itemFieldReport, Project proj, java.util.Date datePer) {
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            XWPFParagraph destino_p = destino.createParagraph();
            destino_p.setStyle(p.getStyle());
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null && text.contains("FIELDMANUAL")) {
                        text = text.replace("FIELDMANUALd", itemFieldReport.getField4());
                        text = text.replace("FIELDMANUALe", itemFieldReport.getField5());
                    }
                    crea_corrida_con_texto_y_formato(destino_p, text, r);
                }
            }
        }
    }

    void resultados_TN(XWPFDocument doc, XWPFDocument destino, FieldReport itemFieldReport, Project proj, java.util.Date datePer) {
        String IDEAMex_link;
        IDEAMex_link = "http://www.uusmb.unam.mx/ideamex/" + itemFieldReport.getField32();
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            XWPFParagraph destino_p = destino.createParagraph();
            destino_p.setStyle(p.getStyle());
            if (runs != null) {
                for (XWPFRun r : runs) {
                    if (r != null) {
                        String text = r.getText(0);
                        if (text != null) {
                            if (text.contains("FIELDMANUAL")) {
                                text = text.replace("FIELDMANUALd", itemFieldReport.getField4());
                                text = text.replace("FIELDMANUALe", itemFieldReport.getField5());
                                text = text.replace("FIELDMANUALf", itemFieldReport.getField6());
                                text = text.replace("FIELDMANUALg", itemFieldReport.getField7()); //Tabla de abundancias
                                // 4 carpetas
                                // Los campos desde FIELDMANUALh hasta FieldMANUALt ahora son archivos Ideamex con una subpath fijo
                                // pero que la raíz es el vìnculo de proyecto (en vez de leer desde el campo 8 hasa el 20)

                                text = text.replace("FIELDMANUALh", IDEAMex_link + "/EdgeR.php");//16 mencionado por Vero
                                text = text.replace("FIELDMANUALi", IDEAMex_link + "/DESeq2.php");//17
                                text = text.replace("FIELDMANUALj", IDEAMex_link + "/limma.php");//18
                                text = text.replace("FIELDMANUALk", IDEAMex_link + "/NOISeq.php");//19

                                // substituir G6, L6, Lactosa y Glucosa por variables
                                List<String> names = new ArrayList();
                                //names.add("G6vsL6");
                                //names.add("L6vsLactosa");
                                //names.add("GlucosavsLactosa");
                                names.add(itemFieldReport.getField26());
                                names.add(itemFieldReport.getField28());
                                names.add(itemFieldReport.getField30());
                                text = text.replace("FIELDMANUALl", IDEAMex_link + "/edgeR_Results/" + names.get(0) + ".txt");//20
                                text = text.replace("FIELDMANUALm", IDEAMex_link + "/edgeR_Results/" + names.get(1) + ".txt");//21
                                text = text.replace("FIELDMANUALn", IDEAMex_link + "/edgeR_Results/" + names.get(2) + ".txt");//22

                                // Tops (el basename del archivo en la secciòn anterior+"_TOP.txt")
                                text = text.replace("FIELDMANUALo", IDEAMex_link + "/edgeR_Results/" + names.get(0) + "_TOP.txt");//23
                                text = text.replace("FIELDMANUALp", IDEAMex_link + "/edgeR_Results/" + names.get(1) + "_TOP.txt");//24
                                text = text.replace("FIELDMANUALq", IDEAMex_link + "/edgeR_Results/" + names.get(2) + "_TOP.txt");//25

                                // Secciones (el nombre del archivo de la penúltima sección+"_Intersect.txt")
                                text = text.replace("FIELDMANUALr", IDEAMex_link + "/edgeR_Results/" + names.get(0) + "_Intersect.txt");
                                text = text.replace("FIELDMANUALs", IDEAMex_link + "/edgeR_Results/" + names.get(1) + "_Intersect.txt");
                                text = text.replace("FIELDMANUALt", IDEAMex_link + "/edgeR_Results/" + names.get(2) + "_Intersect.txt");
                                //
                                text = text.replace("FIELDMANUALu", itemFieldReport.getField21());
                                text = text.replace("FIELDMANUALv", itemFieldReport.getField22());
                                text = text.replace("FIELDMANUALw", itemFieldReport.getField23());
                                text = text.replace("FIELDMANUALx", itemFieldReport.getField24());
                                text = text.replace("FIELDMANUALy", itemFieldReport.getField25());
                                text = text.replace("FIELDMANUALz", itemFieldReport.getField26());
                                text = text.replace("FIELDMANUALZa", itemFieldReport.getField27());
                                text = text.replace("FIELDMANUALZb", itemFieldReport.getField28());
                                text = text.replace("FIELDMANUALZc", itemFieldReport.getField29());
                                text = text.replace("FIELDMANUALZd", itemFieldReport.getField30());
                                text = text.replace("FIELDMANUALZe", itemFieldReport.getField31());
                                text = text.replace("FIELDMANUALZf", itemFieldReport.getField32());
                            }
                            if (text.contains("FIELDEDTABLE")) {
                                text = text.replace("FIELDEDTABLE", "");
                                XWPFTable tbl = destino.createTable(4, 2);
                                XWPFParagraph title0 = tbl.getRow(0).getCell(0).getParagraphs().get(0);
                                XWPFRun run0 = title0.createRun();
                                run0.setText("Ensamblado");
                                run0.setBold(true);
                                XWPFParagraph title1 = tbl.getRow(0).getCell(1).getParagraphs().get(0);
                                XWPFRun run1 = title1.createRun();
                                run1.setText("Número de transcritos");
                                run1.setBold(true);
                                tbl.getRow(1).getCell(0).setText("trinity_" + itemFieldReport.getField26() + "/Trinity.fasta");
                                tbl.getRow(1).getCell(1).setText(itemFieldReport.getField27());
                                tbl.getRow(2).getCell(0).setText("trinity_" + itemFieldReport.getField28() + "/Trinity.fasta");
                                tbl.getRow(2).getCell(1).setText(itemFieldReport.getField29());
                                tbl.getRow(3).getCell(0).setText("trinity_" + itemFieldReport.getField30() + "/Trinity.fasta");
                                tbl.getRow(3).getCell(1).setText(itemFieldReport.getField31());
                            }
                            crea_corrida_con_texto_y_formato(destino_p, text, r);
                        }
                    }
                }
            }
        }
    }

    void resultados_BV(XWPFDocument doc, XWPFDocument destino, FieldReport itemFieldReport, Project proj, java.util.Date datePer) {
        for (XWPFParagraph p : doc.getParagraphs()) {
            XWPFParagraph destino_p = destino.createParagraph();
            destino_p.setStyle(p.getStyle());
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null) {
                        if (text.contains("FIELDMANUAL")) {
                            text = text.replace("FIELDMANUALd", itemFieldReport.getField4());
                            text = text.replace("FIELDMANUALe", itemFieldReport.getField5());
                            text = text.replace("FIELDMANUALf", itemFieldReport.getField6());
                            text = text.replace("FIELDMANUALg", itemFieldReport.getField7());
                            text = text.replace("FIELDMANUALh", itemFieldReport.getField8());
                            text = text.replace("FIELDMANUALi", itemFieldReport.getField9());
                            text = text.replace("FIELDMANUALj", itemFieldReport.getField10());
                            text = text.replace("FIELDMANUALk", itemFieldReport.getField11());
                            text = text.replace("FIELDMANUALl", itemFieldReport.getField12());
                            text = text.replace("FIELDMANUALm", itemFieldReport.getField13());
                            text = text.replace("FIELDMANUALn", itemFieldReport.getField14());
                            text = text.replace("FIELDMANUALo", itemFieldReport.getField15());
                            text = text.replace("FIELDMANUALp", itemFieldReport.getField16());
                            text = text.replace("FIELDMANUALq", itemFieldReport.getField17());
                            text = text.replace("FIELDMANUALr", itemFieldReport.getField18());
                            text = text.replace("FIELDMANUALs", itemFieldReport.getField19());
                            text = text.replace("FIELDMANUALt", itemFieldReport.getField20());
                            text = text.replace("FIELDMANUALu", itemFieldReport.getField21());
                            text = text.replace("FIELDMANUALv", itemFieldReport.getField22());
                            text = text.replace("FIELDMANUALw", itemFieldReport.getField23());
                            text = text.replace("FIELDMANUALx", itemFieldReport.getField24());
                            text = text.replace("FIELDMANUALy", itemFieldReport.getField25());
                            text = text.replace("FIELDMANUALz", itemFieldReport.getField26());
                            text = text.replace("FIELDMANUALz", itemFieldReport.getField27());
                            text = text.replace("FIELDMANUALzg", itemFieldReport.getField28());
                            text = text.replace("FIELDMANUALze", itemFieldReport.getField29());
                            text = text.replace("FIELDMANUALzf", itemFieldReport.getField30());
                        }
                        if (text.contains("FIELDBDi")) {
                            try {
                                text = text.replace("FIELDBDi", String.valueOf(rangoSampleVal.size()));
                            } catch (Exception e) {
                                System.out.println("Error al cambiar el numero de muestras");
                            }
                        }
                        crea_corrida_con_texto_y_formato(destino_p, text, r);
                    }
                }
            }
        }
    }

    XWPFRun crea_corrida_con_texto_y_formato(XWPFParagraph destino, String texto, XWPFRun molde) {
        XWPFRun new_run;
        if (XWPFFieldRun.class.isInstance(molde)) {
            new_run = destino.createFieldRun();
        } else if (XWPFFieldRun.class.isInstance(molde)) {
            XWPFHyperlinkRun hlrun;
            XWPFHyperlinkRun hlmolde = (XWPFHyperlinkRun) molde;
            hlrun = createHyperlinkRun(destino, hlmolde.getAnchor());
            new_run = hlrun;
        } else {
            new_run = destino.createRun();
        }
        if (texto != null) {
            new_run.setText(texto);
        }
        if (molde.isItalic()) {
            new_run.setItalic(true);
        }
        if (molde.isBold()) {
            new_run.setBold(true);
        }
        if (molde.isStrikeThrough()) {
            new_run.setStrikeThrough(true);
        }
        new_run.setVerticalAlignment(molde.getVerticalAlignment().toString());
        System.out.println("paso por crea_corrida_con_texto_y_formato");
        return new_run;
    }

    private void pasa_elementos(XWPFDocument destiny, XWPFDocument source) {
        //Modified from https://stackoverflow.com/questions/23008540/how-to-merge-two-word-documents-which-are-saved-with-docx-to-a-third-file/36894991#36894991
        int i = 0;
        int j = 0;
        for (IBodyElement e : source.getBodyElements()) {
            if (e instanceof XWPFParagraph) {
                XWPFParagraph p = (XWPFParagraph) e;
                if (p.getCTP().getPPr() != null && p.getCTP().getPPr().getSectPr() != null) {
                } else {
                    copia_parrafo(destiny, p);
                    System.out.println("volvio de copia_parrao con ne_run");
                    i++;
                }
            } else if (e instanceof XWPFTable) {
                XWPFTable t = (XWPFTable) e;
                copia_tabla(destiny, t);
                j++;
            }
        }
        System.out.println("ejecutando elementos");
    }

    public boolean reportProjectControllerEx(SampleController sampleController) {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        if (proj == null) {
            return false; // Si no hay un proyecto seleccionado, el botón debe estar deshabilitado
        }

        // Obtener todas las muestras asociadas al proyecto
        List<Sample> all_project_samples = sampleController.getItemsProj(proj);

        // Verificar si hay al menos una muestra registrada
        if (all_project_samples == null || all_project_samples.isEmpty()) {
            return false;
        }

        // Validar datos críticos necesarios para el reporte
        if (sampleController.getLastSampleAnalysisDate(all_project_samples) == null) {
            return false;
        }

        // Puedes agregar más validaciones según sea necesario
        return true; // Si todos los datos están presentes, el botón se habilita
    }
    //Creacion de documento Word desde una plantilla --------------------------------Inicio
    public String createReportWordSampleQC(SampleController sampleController, Project proj, String format) throws IOException {

        java.util.Date datePer = new Date();
        // Reporte de muestras por ahora está aquí
        //TODO poder escoger aquí las muestras
        XWPFDocument doc;
        try {
            List<Sample> all_project_samples;
            all_project_samples = sampleController.getItemsProj(proj);

            // El documento de la introducción será la base que iremos creciendo para agregar más cosas
            doc = new XWPFDocument(new FileInputStream(new File(DirectoryTemplateReport + "intro_muestras.docx")));
            System.out.println("Se cargó el machote para el QC de muestras");

            //doc = mergeMethodsIn(doc, docMethodol, proj);
            tabla_pie_pagina_usuario_responsable(doc, proj);
            tablas_info_muestras_sample_qc(doc, proj, all_project_samples);
            tablas_datos_proyecto_sample_qc(doc, proj, datePer, sampleController.getLastSampleAnalysisDate(all_project_samples), all_project_samples);
            agrega_leyenda_si_hay_muestras_rechazadas_o_condicionadas(doc, all_project_samples);
            //doc.enforceUpdateFields();
            //doc.createTOC();

            String doc_base_name = "F01_PT05_LNATCG_" + proj.getIdProject().replace(":", "_") + ".docx";
            String ruteDoc = DirectoryreportDocuments + doc_base_name;
            String pdf_base_name = "F01_PT05_LNATCG_" + proj.getIdProject().replace(":", "_") + ".pdf";
            String rutePdf = DirectoryreportDocuments + pdf_base_name;

            doc.write(new FileOutputStream(ruteDoc));
            System.out.println("Terminado QC de muestras -->Desde genera Genera QC material genetico");
            //La conversión del documento en cuestión a PDF es mediante Word_to_PDF(ruteDoc, rutePdf)
            File filpdf = new File(rutePdf);
            switch (format) {
                case "docx":
                    downloadReport(ruteDoc, doc_base_name);
                    try {
                        Word_to_PDF(ruteDoc, rutePdf);
                    } catch (IOException | org.docx4j.openpackaging.exceptions.InvalidFormatException e) {
                        showError("Hubo un error al convertir a PDF. -->Desde genera Genera QC material genetico", e);
                    }
                    break;
                case "pdf":
                    //Ahora solo crea el reporte si no existe ya en PDF
                    if (!filpdf.exists()) {
                        Word_to_PDF(ruteDoc, rutePdf);
                    }
                    downloadReport(rutePdf, pdf_base_name);
                    break;
            }

        } catch (IOException | InvalidFormatException | XmlException | org.docx4j.openpackaging.exceptions.InvalidFormatException e) {
            showError("Hubo un error al generar el reporte. -->Desde genera Genera QC material genetico", e);
        }
        return null;
    }

    public List<Sample> getSamplesInRun(String runName, List<Sample> project_samples) {
        //HashSet<Sample> samplesHM = new HashSet<>();
        ArrayList<Sample> samplesarr = new ArrayList<>();
        List<Run> runs = ejbQualityReportsFacade.findRun(runName);
        Run run = runs.get(0);
        List<LibraryRunLink> libraries = run.getLibraryRunLinkList();
        for (LibraryRunLink lbr : libraries) {
            List<SampleLibraryLink> samples = ejbQualityReportsFacade.findSamplesByID(lbr.getLibrary());
            for (SampleLibraryLink samplell : samples) {
                Sample sample = samplell.getSample();
                if (project_samples.contains(sample)) {
                    //Integer id_sample = sample.getIdSample();
                    if (!samplesarr.contains(sample)) {
                        //System.out.println("nosample");
                        //samplesHM.add(sample);
                        samplesarr.add(sample);
                    }

                }
            }
        }
        return samplesarr;
    }

    public String sample_types(List<Sample> samples) throws Exception {
        HashMap<String, Integer> hm = new HashMap<>();
        for (Sample sample : samples) {
            String sample_type = sample.getType();
            if (hm.containsKey(sample_type)) {
                hm.replace(sample_type, hm.get(sample_type) + 1);
            } else {
                hm.put(sample_type, 1);
            }
        }
        if (hm.size() > 1) {
            throw new Exception("More than one sample type");
        }
        return hm.keySet().iterator().next();
    }

    public String obtiene_tipos_metodologia(List<Sample> samples, Project proj) throws Exception {
        List<ReportProject> repps = ejbFacadeProject.findReportProjectByIdProject(proj.getIdProject());
        HashMap<String, Integer> hm = new HashMap<>();
        for (ReportProject repp : repps) {
            String methodology_type = repp.getTypeMethodology();
            if (methodology_type != null) {
                if (hm.containsKey(methodology_type)) {
                    hm.replace(methodology_type, hm.get(methodology_type) + 1);
                } else {
                    hm.put(methodology_type, 1);
                }
            }
        }
        if (hm.size() > 1) {
            //TODO desambiguar cuando hay más de tipo de metodología en los anàlisis bioinformáticos
            //throw new Exception("More than one sample type");
        } else if (hm.isEmpty()) {
            String sample_type = sample_types(samples);
            return sample_type;
        }
        return hm.keySet().iterator().next();
    }

    public String verReportPDFFastQC(Project proj, String runName) throws IOException {
        String doc_base_name = "F02_PT05_LNATCG_" + proj.getIdProject().replace(":", "_") + "_" + runName + ".pdf";
        String ruteDoc = DirectoryreportDocuments + doc_base_name;
        downloadReport(ruteDoc, doc_base_name);
        return "ViewProject?includeViewParams=true";
    }

    public String validateBtnReportPDFFastQC(Project proj, String runName) throws IOException {
        String doc_base_name = "F02_PT05_LNATCG_" + proj.getIdProject().replace(":", "_") + "_" + runName + ".pdf";
        String rutePdf = DirectoryreportDocuments + doc_base_name;
        File file = new File(rutePdf);
        if (file.exists()) {
            return "false";
        } else {
            return "true";
        }
    }

    public String validateBtnReportHTMLFastQC(Project proj, String runName) throws IOException {
        List<QualityReports> qualreports = ejbQualityReportsFacade.urlQualityReportsByIdProject(proj.getIdProject());
        if (qualreports.isEmpty()) {
            return "false";
        } else {
            return "true";
        }
    }
    String messagelocation = "";

    public String findlocationseq(List<String> runnames) {
        System.out.println("obteniendo nombres de equipo segun la lista de corridas seleccionada");
        List<String> devices = new ArrayList<>();
        //String []devices={};
        String addlocationseq = "";
        String device = "";
        String company = "Illumina";
        /*si se selecciono mas de una corrida entonces guarda los nombres del 
        equipo en devices y usa el contador para despues recorrer con otro for y sacar las ubicaciones
         */
        for (int i = 0; i < runnames.size(); i++) {
            System.out.println("entro al for para hacer el split en la vuelta" + i);
            String[] devsplit = runnames.get(i).toString().split("_");
            devices.add(devsplit[1]); //la posicion 0 es la fecha, la posicion 1 el nombre del equipo--> 220607_NS500502_0153_AHJWCNBGXL  
            System.out.println("imprimo el nombre del equipo: " + devices.get(i));
            System.out.println("agrego el valor a la lista devices");
        }
        System.out.println("fin el for que hace el split");

        if (devices.size() > 0) {
            System.out.println("entro al if del switch");
            for (int i = 0; i <= devices.size(); i++) {
                switch (devices.get(i).toUpperCase()) {
                    case "A01314":
                        addlocationseq = "del Instituto Tecnológico y de Estudios Superiores de Monterrey en Monterrey, Nuevo León, México";
                        device = "NovaSeq X";
                        break;
                    case "M06162":
                        addlocationseq = "del la compañia Abalat en la Ciudad de México";
                        device = "MiSeq";
                        break;
                    case "M07836":
                        addlocationseq = "del Instituto de Ecologia de la UNAM en la Ciudad de México";
                        device = "MiSeq";
                        break;
                    case "M02676":
                        addlocationseq = "de la Red de Apoyo a la Investigación la UNAM en la Ciudad de México";
                        device = "MiSeq";
                        break;
                    case "FS10001306":
                        addlocationseq = "de la compañia Analitek Life en la Ciudad de México";
                        device = "iSeq";
                        break;
                    case "M07079":
                        addlocationseq = "de la compañia Analitek Life en la Ciudad de México";
                        device = "MiSeq";
                        break;
                    case "NB502037":
                        addlocationseq = "del Laboratorio de Genética Genos Médica en la Ciudad de México";
                        device = "NextSeq500";
                        break;
                    case "KHS0062":
                        addlocationseq = "de la Unidad de Genómica Avanzada LANGEBIO del CINVESTAV IPN";
                        device = "HiSeq";
                        break;
                    case "MG01HX05":
                        addlocationseq = "de la compañia MACROGEN en los Estados Unidos";
                        device = "HiSeq";
                        break;
                    case "LH00586":
                        addlocationseq = "de la compañia PSOMAGEN en los Estados Unidos";
                        device = "NovaSeq";
                        break;
                    case "NS500560":
                        addlocationseq = "del Instituto Nacional de Medicina Genómica";
                        device = "NextSeq500";
                        break;
                    case "VH01014":
                        addlocationseq = "del Instituto Nacional de Medicina Genómica";
                        device = "NextSeq2000";
                        break;
                    case "FS10002358":
                        addlocationseq = "de la Unidad Universitaria de Secuenciación Masiva y Bioinformática ";
                        device = "iSeq 100";
                        break;
                    case "NS500502":
                        addlocationseq = "de la Unidad Universitaria de Secuenciación Masiva y Bioinformática ";
                        device = "NextSeq 500";
                        break;
                    case "MN18784":
                        addlocationseq = "de la Unidad Universitaria de Secuenciación Masiva y Bioinformática ";
                        device = "MinION";
                        break;
                    case "MN22733":
                        addlocationseq = "de la Unidad Universitaria de Secuenciación Masiva y Bioinformática ";
                        device = "MinION";
                        company = "Oxford Nanopore";
                        break;
                    case "MN22784":
                        addlocationseq = "de la Unidad Universitaria de Secuenciación Masiva y Bioinformática ";
                        device = "MinION";
                        company = "Oxford Nanopore";
                        break;
                    case "MC-115680":
                        addlocationseq = "de la Unidad Universitaria de Secuenciación Masiva y Bioinformática ";
                        device = "MinION";
                        company = "Oxford Nanopore";
                        break;
                    default:
                        addlocationseq = "[ubicacion]";
                        device = "[equipo]";
                        company = "[Illumina/Oxford nanopore]";
                }

                messagelocation = "La secuenciación se realizó en un equipo " + device + " de la compañía " + company + " ubicado en las instalaciones " + addlocationseq;
                System.out.println("La secuenciación se realizó en un equipo " + device + " de la compañía " + company + " ubicado en las instalaciones " + addlocationseq);
                /* La secuenciación se realizó en un equipo [MiSeq|NexSeq|..] 
                   de la compañía Illumina ubicado en las instalaciones [ubicación] */

            }
        }
        return messagelocation;
        //fin leslie
    }

    public String createReportWordFastQC(SampleController sampleController, ProjectController projc, ReportProjectController rpc) throws IOException {
        Project proj = projc.getSelectedProject();
        java.util.Date datePer = new Date();
        XWPFDocument doc;

        List<Sample> all_project_samples;
        all_project_samples = sampleController.getItemsProj(proj);
        List<Sample> samples;
        samples = new ArrayList<>();

        //leslie
        // List<String> namesrun = new ArrayList<>();
        //finleslie
        if (projc.getRuns().isEmpty()) {
            showError("Debe seleccionar al menos una corrida", "");
            return "ViewProject?includeViewParams=true";
        }
        for (String runName : projc.getRuns()) {

            List<Sample> run_samples = getSamplesInRun(runName, all_project_samples);
            for (Sample run_sample : run_samples) {
                if (!samples.contains(run_sample)) {
                    samples.add(run_sample);
                }
            }

            //leslie
//            namesrun.add(runName);
            //finleslie
        }
        String runs_suffix = String.join("_and_", projc.getRuns());

        // findlocationseq(namesrun);
        // String messagesec=findlocationseq(namesrun);
        try {
            String typeMethodology = obtiene_tipos_metodologia(samples, proj);
            if (typeMethodology.equals("RNA")) {
                //10 sep 2024
                //leslie modifico la linea oroginal de lucio typeMethodology = "RNAseq Euc"; por typeMethodology = "RNAE";
                typeMethodology = "RNAseq Euc";//TODO por lo pronto, va a siggnar el tipoi de metodologìa a RNA eucariótico
            }

            // El documento de la introducción será la base que iremos creciendo para agregar más cosas
            doc = new XWPFDocument(new FileInputStream(new File(DirectoryTemplateReport + "intro_secuenciacion.docx")));
            XWPFDocument docMethodol = new XWPFDocument(new FileInputStream(new File(DirectoryTemplateReport + "metodos" + sufijo_tipo_muestra(typeMethodology) + ".docx")));
            XWPFDocument docCalidad = new XWPFDocument(new FileInputStream(new File(DirectoryTemplateReport + "Reporte_de_calidad.docx")));
            System.out.println("Se cargaron los machotes para FastQC");
            doc = mergeMethodsIn(doc, docMethodol, proj);
            doc = mergeQualityReportIn(doc, docCalidad, proj, "arriba");

            tabla_pie_pagina_usuario_responsable(doc, proj);
            tablas_info_muestras(doc, proj, all_project_samples, samples, getTypeMethodologyFromSamples(samples));
            tablas_datos_proyecto(doc, proj, datePer, sampleController.getLastSampleFastQcDate(samples), all_project_samples, samples);

            //leslie 10 septiembre cambie f02 por f01 , noexiste f02_pt05
            //leslie remplaza runs_suffix por la palabara secuenciacion
            String doc_base_name = "F01_PT05_LNATCG_" + proj.getIdProject().replace(":", "_") + "_Secuenciacion" + runs_suffix + ".docx";
            String ruteDoc = DirectoryreportDocuments + doc_base_name;
            doc.write(new FileOutputStream(ruteDoc));
            System.out.println("Terminado QC de corridas");
            downloadReport(ruteDoc, doc_base_name);
            showMessage("El reporte de calidad de corrida fue generado satisfactoriamente.");

        } catch (Exception e) {
            showError("Hubo un error al generar el reporte.", e);
        }
        return "";
    }

    public void bionInfoRep(ProjectController pctrl) {
        System.out.println("Entra al metodo BionInfoRep");

        FacesContext context = FacesContext.getCurrentInstance();
        Map sessmap = context.getExternalContext().getSessionMap();
        Project proj = (Project) sessmap.get("project");
        BioinformaticAnalysis banl = ejbFacade.findBionformaticAnalysisByAnalysisId(pctrl.getAnalysis());
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        current = getFacade().find(varIdReportProject);
        String ganl = typeReportInitials(pctrl.getAnalysis());
        String trn = tipoReporteNormalizado(banl.getAnalysisName());
        List<ReportProject> report_projects = ejbFacade.findReportProjectByIdProject(proj.getIdProject());
        List<ReportProject> rpmatches = new ArrayList<>();
        for (ReportProject rp : report_projects) {
            if (rp.getIdUser().equals(us)) {
                if (tipoReporteNormalizado(rp.getName()).equals(trn)) {
                    rpmatches.add(rp);
                }
            }
        }
        File destpath;

        if (rpmatches.isEmpty()) {

            //Aun no se agrega la parte current.getIdReportProject(), puesto que al guardarse el archivo todavìa no se tiene
            // un idReportProject pues todavia no se ha persistido el ReportProject.
            ReportProject new_reportproject = new ReportProject();
            System.out.println("mostramos la ruta o nombre::  " + pctrl.getFile().getFileName());
            new_reportproject.setDateCreate(proj.getRequestDate());
            new_reportproject.setDateRevise(new Date());
            new_reportproject.setStatus("Autorizado");
            new_reportproject.setName(trn);
            new_reportproject.setIdProject(proj);
            new_reportproject.setIdUser(us);
            getFacade().create(new_reportproject);
            String nameFile = "authorize_F01_PT05_LNATCG_" + proj.getIdProject().replace(":", "_") + "_" + new_reportproject.getIdReportProject().toString() + "_" + ganl + ".pdf";
            new_reportproject.setPathauthorize_PDF(nameFile);
            getFacade().edit(new_reportproject);
            destpath = new File(DirectoryreportDocuments, nameFile);
        } else {
            ReportProject to_overwrite = rpmatches.get(0);
            String nameFile = "authorize_F01_PT05_LNATCG_" + proj.getIdProject().replace(":", "_") + "_" + to_overwrite.getIdReportProject().toString() + "_" + ganl + ".pdf";
            to_overwrite.setPathauthorize_PDF(nameFile);
            getFacade().edit(to_overwrite);
            System.out.println("RepportProject used: " + to_overwrite.toString());
            destpath = new File(DirectoryreportDocuments, to_overwrite.getPathauthorizePDF());
        }

        try {
            if (destpath.exists()) {
                System.out.println("Reemplazando el archivo preexistente " + destpath.getName());
                java.nio.file.Files.delete(destpath.toPath());
            }
            java.nio.file.Files.copy(pctrl.getFile().getInputstream(), destpath.toPath());
            showMessage("Subido el PDF autorizado a " + destpath.toPath());
        } catch (IOException e) {
            showError("Error al subir el PDF autorizado a " + destpath.toPath(), e);
        }

    }

    public String createReportWordBioinfo(SampleController sampleController) throws IOException {
        //TODO assertar si projectReportProject es un arreglo que sòlo contiene a current
        Project proj = current.getIdProject();
        FieldReport itemFieldReport = getSelectedFieldReport();
        System.out.println("Tipo de reporte ::: " + varTypeReport);

        System.out.println("varidproy" + proj.getIdProject());

        java.util.Date datePer = current.getDateCreate();
        // Reporte de muestras por ahora está aquí
        System.out.println("El report proyecto actual es :::::: " + current);
        XWPFDocument doc;
        String doc_base_name;
        String ruteDoc;
        String tipoAbr;
        tipoAbr = typeReportInitials(varTypeReport);
        doc_base_name = "F01_PT05_LNATCG_" + proj.getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + tipoAbr + ".docx";
        ruteDoc = DirectoryreportDocuments + doc_base_name;
        String tipoReporte;
        String doc_download_base_name;
        if (repoEditSample == null) {
            repoEditSample = this.sampleBySRP();
        }
        try {
            List<Sample> all_project_samples;
            all_project_samples = sampleController.getItemsProj(proj);
            String typeMethodology = current.getTypeMethodology();
            String tipoMetPrefix = typeMetPrefix(varTypeReport);
            tipoReporte = tipoReporteNormalizado(varTypeReport);
            if (tipoAbr == null) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, "Tipo de reporte no registrado: {0}", varTypeReport);
                showError("Error al generar el reporte", "E001");
                return "menuReport?faces-redirect=true&includeViewParams=true";
            }
            doc_download_base_name = "F01_PT05_LNATCG_" + proj.getIdProject().replace(":", "_") + "_" + tipoAbr + ".docx";

            // El documento de la introducción será la base que iremos creciendo para agregar más cosas
            doc = new XWPFDocument(new FileInputStream(new File(DirectoryTemplateReport + "intro_bioinformatica.docx")));
            Date badate = getLastBioinfoAnlDate(all_project_samples);
            tablas_datos_proyecto(doc, proj, current.getDateCreate(), badate, all_project_samples, repoEditSample);
            tabla_pie_pagina_usuario_responsable(doc, proj);
            tablas_info_muestras(doc, proj, all_project_samples, repoEditSample, current.getTypeMethodology());
            XWPFDocument docMethodol = new XWPFDocument(new FileInputStream(new File(DirectoryTemplateReport + "metodos" + sufijo_tipo_muestra(typeMethodology) + ".docx")));
            XWPFDocument docCalidad = new XWPFDocument(new FileInputStream(new File(DirectoryTemplateReport + "Reporte_de_calidad.docx")));
            XWPFDocument docRptBioinfo = new XWPFDocument(new FileInputStream(new File(DirectoryTemplateReport + "Bioinformatica_" + tipoMetPrefix + ".docx")));
            XWPFDocument docEval = new XWPFDocument(new FileInputStream(new File(DirectoryTemplateReport + "Evaluacion_y_firmas.docx")));
            System.out.println("Se cargaron los machotes para el reporte bioinformático");
            doc = mergeMethodsIn(doc, docMethodol, proj);
            //leslie 
            System.out.println("se hizo el merge en methodsin");
            doc = mergeQualityReportIn(doc, docCalidad, proj, itemFieldReport.getField3());
            //leslie
            System.out.println("paso el merge de Quality reports");
            switch (tipoReporte) {
                case "Expresion Diferencial":
                    resultados_ED(docRptBioinfo, doc, itemFieldReport, proj, datePer);
                    System.out.println("salio de resultados_ED");
                    break;
                case "Metagenomas":
                    resultados_AM(docRptBioinfo, doc, itemFieldReport, proj, datePer);
                    break;
                case "Ensamble de Genoma":
                    resultados_EG(docRptBioinfo, doc, itemFieldReport, proj, datePer);
                    break;
                case "Análisis de expresion diferencial y Ensamblado de transcriptoma":
                    resultados_TN(docRptBioinfo, doc, itemFieldReport, proj, datePer);
                    break;
                case "Análisis de variantes":
                    resultados_BV(docRptBioinfo, doc, itemFieldReport, proj, datePer);
                    break;
            }
            System.out.println("paso el switch de createepo");
            docEval = fillCRANames(docEval);
            System.out.println("salio de fillCRNames");
            pasa_elementos(doc, docEval);

            System.out.println("paso pasa_elemetos");
            /*
            ///doc.enforceUpdateFields();
            //doc.createTOC();
             */
            try (FileOutputStream outw = new FileOutputStream(ruteDoc)) {
                doc.write(outw);
                doc.close();
                outw.flush();
            }
        } catch (IOException | InvalidFormatException | XmlException e) {
            showError("Hubo un error al generar el reporte bioinformático.", e);
            doc = null;
            tipoReporte = null;
            doc_download_base_name = null;
        }
        if (doc != null) {
            current.setStatus("Elaboracion");
            current.setPathcreate(doc_base_name);//por ahora este atributo es sólamente el basename del nombre de archivo, para no exceder el máximo de tamaño de cadena de 100 caracteres.
            getFacade().edit(current);
            System.out.println("Creado el reporte " + tipoReporte + ", guardado en " + ruteDoc);
            downloadReport(ruteDoc, doc_download_base_name);
            showMessage("El reporte bioinformático fue generado satisfactoriamente.");
        }
        return "";
        //return "menuReport?faces-redirect=true&includeViewParams=true";
    }

    private void agrega_leyenda_si_hay_muestras_rechazadas_o_condicionadas(XWPFDocument doc, List<Sample> all_project_samples) {
        // Agrega una leyenda al final del documento donde se invita al usuario a aceptar el procesamiento
        // de las muestras aceptadas o rechazadas mediante un comentario en SISBI o bien comunicarse
        // con la UUSMB para acordar la reposición de las muestras.
        if (hay_muestra_condicionada_o_rechazada(all_project_samples)) {
            try {
                File myObj = new File(DirectoryTemplateReport + "procedimiento_aceptacion.txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    XWPFParagraph par = doc.createParagraph();
                    XWPFRun run = par.createRun();
                    run.setText(data);
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                showError("Plantilla de texto de procedimiento de aceptación no encontrada", e);
            }

        }
    }

    //Creacion de documento Word desde una plantilla --------------------------------Final
    public Date getLastBioinfoAnlDate(List<Sample> samples) {
        //Devuelve cuándo terminó el anàlsis de calidad de la última muestra
        List<Date> dates = new ArrayList<>();
        for (Sample sample : samples) {
            List<Comments> comms = ejbFacade.getCommentsByIdTypeAndTypeSBD(sample.getIdSample().toString(), "Sample");
            for (Comments comm : comms) {
                if (comm.getComment().startsWith("Estatus cambia de -")) {
                    if (!comm.getComment().endsWith("Analisis Bioinformatico-")) {
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

    //Metodos generales
    public void showMessage(String message) {
        java.util.logging.Logger.getLogger(ReportProjectController.class.getName()).log(Level.INFO, message);
        FacesMessage message_obj = new FacesMessage(message);
        FacesContext.getCurrentInstance().addMessage(null, message_obj);
    }

    public void showMessage(String message, String details) {
        java.util.logging.Logger.getLogger(ReportProjectController.class.getName()).log(Level.INFO, message + ": " + details);
        FacesMessage message_obj = new FacesMessage(message, details);
        FacesContext.getCurrentInstance().addMessage(null, message_obj);
    }

    public void showWarning(String message, String details) {
        java.util.logging.Logger.getLogger(ReportProjectController.class.getName()).log(Level.SEVERE, message + ": " + details);
        FacesMessage message_obj = new FacesMessage(FacesMessage.SEVERITY_WARN, message, details);
        FacesContext.getCurrentInstance().addMessage(null, message_obj);
    }

    public void showError(String message, Exception e) {
        java.util.logging.Logger.getLogger(ReportProjectController.class.getName()).log(Level.SEVERE, message + ":", e);
        email.sendEmailErrorTraceback(message, e);
        showError(message, e.getLocalizedMessage());
    }

    public void showError(String mensaje, String details) {
        //java.util.logging.Logger.getLogger(ReportProjectController.class.getName()).log(Level.SEVERE, "Error en downloadReport:", e);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, details);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    //validar si existe el archivo antes de descargar por tipo de rol
    public void validateFileExistRol(String typeRol) {
        if (typeRol.equals("revise")) {
            if (current.getPathcreate().equals("")) {
                showMessage("Ups, no existe el archivo, el rol creador debe estar modificandolo");
            }
        } else if (typeRol.equals("authorize")) {
            //filename = ctx.getRealPath("/resources/docReportsSign/"+ current.getPathrevise()) ;
        }
    }

    private XWPFDocument signByRole(String typeRol) throws FileNotFoundException, InvalidFormatException, IOException {
        String filename;
        if (typeRol.equals("revise")) {
            filename = DirectoryreportDocuments + current.getPathrevise();
        } else if (typeRol.equals("create")) {
            filename = DirectoryreportDocuments + current.getPathcreate();
        } else if (typeRol.equals("authorize")) {
            filename = DirectoryreportDocuments + current.getPathauthorize();
        } else {
            filename = null;
        }

        File archivodoc = new File(filename);
        FileInputStream fis = new FileInputStream(archivodoc);
        System.out.println("ruta para signByRole " + filename);
        XWPFDocument doc = new XWPFDocument(fis);
        signByRole(doc, typeRol);
        return doc;
    }

    private XWPFDocument fillCRANames(XWPFDocument doc) {
        //Fill in creator, authorizer and reviewer names

        //Tablas de Datos del Proyecto
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);

                            //INFORMACION DEL CREADOR, REVISOR O AUTORIZADOR DEL REPORTE
                            if (text != null) {
                                if (text.contains("CREATEUSER")) {
                                    text = text.replace("CREATEUSER", current.getIdUser().getFullName());
                                    r.setText(text, 0);
                                }
                                if (text.contains("REVISEUSER")) {
                                    text = text.replace("REVISEUSER", current.getIdUserRevise().getFullName());
                                    r.setText(text, 0);
                                }
                                if (text.contains("AUTHORIZEUSER")) {
                                    text = text.replace("AUTHORIZEUSER", current.getIdUserAuthorize().getFullName());
                                    r.setText(text, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("pasa por fillCRAmes");
        return doc;
    }

    private void signByRole(XWPFDocument doc, String typeRol) throws FileNotFoundException, InvalidFormatException, IOException {
        Date theDate;
        String roleName;
        switch (typeRol) {
            case "authorize":
                //involvedUser = current.getIdUserAuthorize();
                roleName = typeRol.toUpperCase();
                theDate = current.getDateAuthorize();
                break;
            case "revise":
                roleName = typeRol.toUpperCase();
                theDate = current.getDateRevise();
                break;
            case "create":
                roleName = typeRol.toUpperCase();
                theDate = current.getDateCreate();
                break;
            default:
                roleName = null;
                theDate = null;
        }
        //Tablas de Datos del Proyecto
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        for (XWPFRun r : p.getRuns()) {
                            String text = r.getText(0);

                            ////TABLA DE FIRMAS: Insercion de fecha de creación, revisión o autorización
                            if (text != null && text.contains(roleName + "DATE")) {
                                text = text.replace(roleName + "DATE", getDateStringFormat(theDate));
                                r.setText(text, 0);
                            }
                            //TABLA DE FIRMAS: Insercion de imagen de firma
                            UserRoleReport userRol = getUserRoleReportContext();
                            if (userRol.getPathimage() != null) {
                                if (text != null && text.contains(roleName + "SIGN")) {
                                    text = text.replace(roleName + "SIGN", "");
                                    r.setText(text, 0);
                                    String imgFile = DirectoryImageSing + userRol.getPathimage();
                                    File fImgFile = new File(imgFile);
                                    if (!fImgFile.exists()) {
                                        throw new SignatureException("Imagen de firma no encontrada");
                                    }
                                    FileInputStream is = new FileInputStream(fImgFile);
                                    r.addBreak();
                                    r.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, imgFile, Units.toEMU(150), Units.toEMU(70));
                                    is.close();
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    public String typeReportInitials(Integer typeReport) {
        BioinformaticAnalysis banl = ejbFacade.findBionformaticAnalysisByAnalysisId(typeReport);
        return typeReportInitials(banl.getAnalysisName());
    }

    public String typeReportInitials(String typeReport) {
        String typeReportInitials;

        switch (typeReport) {
            case "Metagenomas":
            case "Analisis Metagenomico":
                typeReportInitials = "AM";
                break;
            case "Variantes Geneticas":
            case "Variantes Genéticas":
            case "Busqueda de Variantes":
            case "Análisis de variantes":
                typeReportInitials = "BV";
                break;
            case "Ensamble de Genoma":
            case "Ensamblado de Novo":
                typeReportInitials = "EG";
                break;
            case "Expresion Diferencial":
            case "Analisis de expresion diferencial":
            case "Análisis de expresión diferencial":
                typeReportInitials = "ED";
                break;
            case "Análisis de expresion diferencial y Ensamblado de transcriptoma":
            case "Transcriptoma de Novo y Expresion Diferencial":
                typeReportInitials = "TN";
                break;
            default:
                typeReportInitials = null;

        }
        return typeReportInitials;
    }

    public String typeMetPrefix(String typeReport) {
        String tipoMetPrefix;
        switch (varTypeReport) {
            case "Expresion Diferencial":
            case "Analisis de expresion diferencial":
            case "Análisis de expresión diferencial":
                // Hay 2 entradas parecidas, una "Análisis de Expresión Diferencial" y otra "Analisis de expresión eiferencial"
                // Esta última es la que usamos ahora
                tipoMetPrefix = "expresiondiferencial";
                break;
            case "Metagenomas":
            case "Analisis Metagenomico":
                tipoMetPrefix = "analisismetagenomico";
                break;
            case "Ensamble de Genoma":
            case "Ensamblado de Novo":
                tipoMetPrefix = "ensamblegenoma";
                break;
            case "Análisis de expresion diferencial y Ensamblado de transcriptoma":
            case "Transcriptoma de Novo y Expresion Diferencial":
                tipoMetPrefix = "transcriptomanovo";
                break;
            case "Variantes Geneticas":
            case "Variantes Genéticas":
            case "Busqueda de Variantes":
            case "Análisis de variantes":
                tipoMetPrefix = "busquedavariantes";
                break;
            default:
                tipoMetPrefix = null;
        }
        return tipoMetPrefix;
    }

    public String tipoReporteNormalizado(String typeReport) {
        String tipoReporte;
        switch (typeReport) {
            case "Expresion Diferencial":
            case "Analisis de expresion diferencial":
            case "Análisis de expresión diferencial":
                // Hay 2 entradas parecidas, una "Análisis de Expresión Diferencial" y otra "Analisis de expresión eiferencial"
                // Esta última es la que usamos ahora
                tipoReporte = "Expresion Diferencial";
                break;
            case "Metagenomas":
            case "Analisis Metagenomico":
                tipoReporte = "Metagenomas";
                break;
            case "Ensamble de Genoma":
            case "Ensamblado de Novo":
            case "Ensamblado de novo":
                tipoReporte = "Ensamble de Genoma";
                break;
            case "Análisis de expresion diferencial y Ensamblado de transcriptoma":
            case "Transcriptoma de Novo y Expresion Diferencial":
                tipoReporte = "Análisis de expresion diferencial y Ensamblado de transcriptoma";
                break;
            case "Variantes Geneticas":
            case "Variantes Genéticas":
            case "Busqueda de Variantes":
            case "Análisis de variantes":
                tipoReporte = "Análisis de variantes";
                break;
            default:
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, "Tipo de reporte no registrado: {0}", varTypeReport);
                showError("Error al generar el reporte", "E001");
                //doc=null;
                return "menuReport?faces-redirect=true&includeViewParams=true";
        }
        return tipoReporte;
    }

    //Descargar archivo word y pdf proceso de firma
    public void downloadFile(String typeRol) throws FileNotFoundException, IOException {
        try {
            String fileBaseName;
            String contentType;
            String nameDocDowload;
            switch (typeRol) {
                case "create":
                    fileBaseName = current.getPathcreate();
                    nameDocDowload = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + typeReportInitials(current.getName()) + ".docx";
                    break;
                case "revise":
                    fileBaseName = current.getPathrevise();
                    nameDocDowload = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + typeReportInitials(current.getName()) + ".docx";
                    break;
                case "authorize":
                    fileBaseName = current.getPathauthorize();
                    nameDocDowload = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + typeReportInitials(current.getName()) + ".docx";
                    break;
                case "authorize_pdf":
                    fileBaseName = current.getPathauthorizePDF();
                    nameDocDowload = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + typeReportInitials(current.getName()) + ".pdf";
                    break;
                default:
                    //No debería llegar acá
                    fileBaseName = "";
                    nameDocDowload = "";
                    break;
            }

            if (fileBaseName.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            }

            File ficheroXLS = new File(DirectoryreportDocuments + fileBaseName);
            FacesContext ctx2 = FacesContext.getCurrentInstance();
            try (FileInputStream fis2 = new FileInputStream(ficheroXLS)) {
                byte[] bytes = new byte[1000];
                int read = 0;
                if (!ctx2.getResponseComplete()) {
                    HttpServletResponse response = (HttpServletResponse) ctx2.getExternalContext().getResponse();
                    response.setContentType(contentType);
                    response.setHeader("Content-Disposition", "attachment;filename=\"" + nameDocDowload + "\"");
                    ServletOutputStream out = response.getOutputStream();

                    while ((read = fis2.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    out.flush();
                    out.close();
                    System.out.println("\nDescargado " + fileBaseName + ". Encabezado de nombre: " + nameDocDowload + "\n");
                    ctx2.responseComplete();
                }
                System.out.println("Descarga Finalizada de " + fileBaseName);
            }
        } catch (IOException e) {
            showError("Hubo un error en el método downloadFile", e);
        }
    }

    //descragar el reporte actualizado
    public void downloadFileUpdated() throws FileNotFoundException, IOException {
        try {
            String ruteDoc = "";
            if (current.getPathauthorize() != null) {
                ruteDoc = DirectoryreportDocuments + current.getPathauthorize();
            } else if (current.getPathrevise() != null) {
                ruteDoc = DirectoryreportDocuments + current.getPathrevise();
            } else if (current.getPathcreate() != null) {
                ruteDoc = DirectoryreportDocuments + current.getPathcreate();
            }

            File ficheroXLS = new File(ruteDoc);
            FacesContext ctx2 = FacesContext.getCurrentInstance();
            FileInputStream fis2 = new FileInputStream(ficheroXLS);
            byte[] bytes = new byte[1000];
            int read = 0;
            String contentType;
            if (!ctx2.getResponseComplete()) {
                //String fileName2 = ficheroXLS.getName();
                String nameDocDowload;

                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                nameDocDowload = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + typeReportInitials(current.getName()) + ".docx";

                //String contentType = "application/pdf";
                HttpServletResponse response = (HttpServletResponse) ctx2.getExternalContext().getResponse();
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + nameDocDowload + "\"");
                ServletOutputStream out = response.getOutputStream();

                while ((read = fis2.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
                out.close();
                System.out.println("\nDescargado " + ruteDoc + ". Encabezado de nombre " + nameDocDowload + "\n");
                ctx2.responseComplete();
            }
            System.out.println("Descarga Finalizada de " + ruteDoc);
            fis2.close();
        } catch (IOException e) {
            showError("Hubo un error en el método downloadFileUpdated: ", e);
        }
    }

    //Validar boton tipod e reporte, solo el que lo creo puede editarlo
    public String validateUserTypeReport(int idUser, String idProject, String typeReport) {
        if (!ejbFacade.findReportProjectidPType(idProject, typeReport).isEmpty()) {
            ReportProject reportProject = ejbFacade.findReportProjectidPType(idProject, typeReport).get(0);
            if (Objects.equals(reportProject.getIdUser().getIdUser(), getUserContext().getIdUser())) {
                return "false";
            } else {
                return "true";
            }
        }
        return "false";
    }

    UploadedFile filep;

    public UploadedFile getFilep() {
        return filep;
    }

    public void setFilep(UploadedFile filep) {
        this.filep = filep;
    }

    //validar boton de subir imagen
    public String validateBtnUpload(String typeRol) {
        if (typeRol.equals("create")) {
            if (current.getPathcreate() == null || current.getPathcreate().equals("")) {
                return "false";
            } else {
                //return "true";
                return "false";//TODO al menos por ahora para probar
            }
        } else if (typeRol.equals("revise")) {
            if (current.getPathrevise() == null || current.getPathrevise().equals("")) {
                return "false";
                //return validateBtnFirmarRevisar();
                //return validateBtnQuitarFirmaRevisar();
            } else {
                return "false";
            }
        } else if (typeRol.equals("authorize")) {
            if (current.getPathauthorize() == null || current.getPathauthorize().equals("")) {
                return "false";
                //return validateBtnQuitarFirmaAutorizar();
            } else {
                return "false";
            }
        }
        return null;
    }

    public String validateBtnRegistrarReporteFastQC(ProjectController pctrl) {
        if (pctrl.getRuns() == null) {
            return "false";//TODO por el momento
        } else if (pctrl.getRuns().isEmpty()) {
            return "false";//TODO por el momento
        } else {
            return "false";
        }
    }

    public String uploadFileCreate(FileUploadEvent event) {
        String nameFile;
        filep = event.getFile();

        try {
            //Quitó current.getTypeMethodology()
            nameFile = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + typeReportInitials(varTypeReport) + ".docx";
            System.out.println("mostramos la ruta o nombre::  " + filep.getFileName());
            File destpath = new File(DirectoryreportDocuments, nameFile);
            String cadreemplazo;
            if (destpath.exists()) {
                System.out.println("Reemplazando el archivo preexistente " + destpath.getName());
                java.nio.file.Files.delete(destpath.toPath());
                cadreemplazo = ", reemplazando el archivo que existìa eon ese nombre";
            } else {
                cadreemplazo = "";
            }
            java.nio.file.Files.copy(filep.getInputstream(), destpath.toPath());
            current.setPathcreate(nameFile);
            current.setStatus("Elaboracion");
            getFacade().edit(current);
            showMessage("Archivo " + event.getFile().getFileName() + " subido satisfactoriamente" + cadreemplazo, event.getFile().getFileName() + " fue cargado.");
        } catch (IOException ex) {
            showError("There was a problem, your file was not uploaded.", ex);
            return "menuReport?faces-redirect=true&includeViewParams=true";
        }
        //sendEmailToRoleRevise();
        return "menuReport?faces-redirect=true&includeViewParams=true";
    }

    public void uploadFileRevise(FileUploadEvent event) {
        String nameFile;
        filep = event.getFile();
        try {
            nameFile = "revise_F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + typeReportInitials(varTypeReport) + ".docx";
            System.out.println("mostramos la ruta o nombre::  " + filep.getFileName());
            File destpath = new File(DirectoryreportDocuments, nameFile);
            String cadreemplazo;
            if (destpath.exists()) {
                System.out.println("Reemplazando el archivo preexistente " + destpath.getName());
                java.nio.file.Files.delete(destpath.toPath());
                cadreemplazo = ", reemplazando el archivo que existìa eon ese nombre";
            } else {
                cadreemplazo = "";
            }
            java.nio.file.Files.copy(filep.getInputstream(), destpath.toPath());
            current.setPathrevise(nameFile);
            current.setStatus("Revision");
            getFacade().edit(current);
            showMessage("Archivo " + event.getFile().getFileName() + " subido satisfactoriamente" + cadreemplazo, event.getFile().getFileName() + " fue cargado.");
        } catch (IOException ex) {
            showError("There was a problem, your file was not uploaded.", ex);
        }
        System.out.println(event.getFile().getFileName());
        FacesMessage message = new FacesMessage("Archivo Word subido satisfactoriamente", event.getFile().getFileName() + " fue cargado.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        //sendEmailToRoleAutorize();
        //return "menuReport?faces-redirect=true&includeViewParams=true";
    }

    public String uploadFileAuthorize(FileUploadEvent event) {
        String nameFile;
        filep = event.getFile();
        String ext;
        if (filep.getFileName().endsWith(".docx")) {
            ext = ".docx";
            nameFile = "authorize_F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + typeReportInitials(current.getName()) + ext;
            current.setStatus("Autorizacion");
            current.setPathauthorize(nameFile);
        } else if (filep.getFileName().endsWith(".pdf")) {
            ext = ".pdf";
            nameFile = "authorize_F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + typeReportInitials(current.getName()) + ext;
            current.setStatus("Autorizado");
            current.setPathauthorize_PDF(nameFile);
        } else {
            ext = null;
            showError("Error en uploadFileAuthorize", "Extensión no reconocida en archivo " + filep.getFileName());
            return "menuReportAuthorize?faces-redirect=true&includeViewParams=true";
        }
        try {
            System.out.println("mostramos la ruta o nombre::  " + filep.getFileName());
            File destpath = new File(DirectoryreportDocuments, nameFile);
            String cadreemplazo;
            if (destpath.exists()) {
                System.out.println("Reemplazando el archivo preexistente " + destpath.getName());
                java.nio.file.Files.delete(destpath.toPath());
                cadreemplazo = ", reemplazando el archivo que existìa eon ese nombre";
            } else {
                cadreemplazo = "";
            }
            java.nio.file.Files.copy(filep.getInputstream(), destpath.toPath());
            getFacade().edit(current);
            showMessage("Archivo " + event.getFile().getFileName() + " subido satisfactoriamente" + cadreemplazo, event.getFile().getFileName() + " fue cargado.");
        } catch (IOException ex) {
            showError("There was a problem your file was not uploaded.", ex);
        }
        return "menuReportAuthorize?faces-redirect=true&includeViewParams=true";
    }

    public String deleteUploadWordCreate() {
        //FacesContext context = FacesContext.getCurrentInstance();
        //ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
        File file = new File(DirectoryreportDocuments + current.getPathcreate());

        file.delete();
        current.setPathcreate(null);
        current.setStatus("Creado");
        getFacade().edit(current);
        return "menuReport?faces-redirect=true&includeViewParams=true";
    }

    public String deleteUploadWordRevise() {
        //FacesContext context = FacesContext.getCurrentInstance();
        //ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
        File file = new File(DirectoryreportDocuments + current.getPathrevise());

        file.delete();
        current.setPathrevise(null);
        //current.setStatus("Creado");
        current.setStatus("Revision");
        getFacade().edit(current);
        return "menuReportRevise?faces-redirect=true&includeViewParams=true";
    }

    public String deleteUploadWordAuthorize() {
        //FacesContext context = FacesContext.getCurrentInstance();
        //ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
        File file = new File(DirectoryreportDocuments + current.getPathauthorize());

        file.delete();
        current.setPathauthorize(null);
        //current.setStatus("Revisado");
        current.setStatus("Autorizacion");
        getFacade().edit(current);
        return "menuReportAuthorize?faces-redirect=true&includeViewParams=true";
    }

    public String deleteUploadWord(String typeRol) {
        String statusReport = "", pathReport = "";

        if (typeRol.equals("create")) {
            pathReport = current.getPathcreate();
            statusReport = "Creado";
        } else if (typeRol.equals("revise")) {
            //current.setIdUserRevise(null);
            pathReport = current.getPathrevise();
            statusReport = "Revision";
        } else if (typeRol.equals("authorize")) {
            //current.setIdUserRevise(null);
            pathReport = current.getPathauthorize();
            statusReport = "Autorizacion";
        }

        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
        File file = new File(scontext.getRealPath("../../web/resources/docReportsSign/" + pathReport));
        file.delete();
        current.setPathcreate(null);
        current.setStatus(statusReport);
        getFacade().edit(current);
        return "menuReport?faces-redirect=true&includeViewParams=true";
    }

    public void updateTableReportProject() {
        //projectReportProject = new ArrayList<>();
        //projectReportProject = ejbFacadeProject.reportProjectStatus();
        //init();
    }

    //Botón borrar reporte (para las pruebas)
    public String validateDisplayBtnDeleteReport(ReportProject report) {
        //El creador del reporte es el {unico que lo puede borrar
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (us.equals(report.getIdUser())) {
            return "true";
        } else {
            return "false";
        }
    }

    public void deleteReport(ReportProject report) {
        FacesContext context = FacesContext.getCurrentInstance();
        int idReportProject = report.getIdReportProject();//25
        ejbSampleRP.DeleteRangeSample(idReportProject);
        ejbSampleRP.deleteFieldReportByReportProject(idReportProject);

        ejbSampleRP.deleteReportByReportProject(idReportProject);
    }

    //Metodos para validar botones de generar, revisar y autorizar            Inicio
    public String validateBtnContinuar(Users idUserCreate, String statusReport) {
        //Obtenemos el id del usuario con la sesion actual iniciada
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        //consultamos que tipo de rol tiene sobre el reporte
        //System.out.println("resultado de consulta validateBtnContinuar: " + ejbFacadeUserRoleReport.findUserRoleReportById(us.getIdUser()).size());
        if (ejbFacadeUserRoleReport.findUserRoleReportById(us.getIdUser()).isEmpty()) {
            return "true";
        } else {
            UserRoleReport userDetails = (UserRoleReport) ejbFacadeUserRoleReport.findUserRoleReportById(us.getIdUser()).get(0);
            //System.out.println("permiso ::" + userDetails.getTorevise());
            //primera etapa de validacion
            if (userDetails == null || userDetails.getTocreate() == false || !Objects.equals(us.getIdUser(), idUserCreate.getIdUser())) {
                return "true";
            } else {
                //segunda etapa de validacion
                //validar que el reporte tenga estado de "Creado", "Revisar" o "Autorizar"
                //TODO preguntarle a Leslie si hay campos en infinitivo (Autorizar, Revisar)
                switch (statusReport) {
                    case "Creado":
                    case "Elaboracion":
                    case "Elaborado":
                    case "Revisar":
                    case "Revisado":
                    case "Autorizacion":
                    case "Autorizado":
                        return "false";
                    case "Entregado":
                        // Ya está autorizado y entregado (¿¿revertir??)
                        return "true";
                    default:
                        return "true";
                }
            }
        }
    }

    public String validateUserRoleFidelAlejandro() {
        if (getUserRoleReportContext().getIdUser().getIdUser() == 46) {
            return "false";
        }
        return "true";
    }

    public String validateBtnRevisar(ReportProject reportproj, Users idUserCreate, Users idUserRevise, String statusReport) {
        //Obtenemos el id del usuario con la sesion actual iniciada
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        //consultamos que tipo de rol tiene sobre el reporte
        if (ejbFacadeUserRoleReport.findUserRoleReportById(us.getIdUser()).isEmpty()) {
            return "true";
        } else {
            UserRoleReport userDetails = (UserRoleReport) ejbFacadeUserRoleReport.findUserRoleReportById(us.getIdUser()).get(0);
            //System.out.println("permiso :"+reportproj.getIdProject().getIdProject());
            //primera etapa de validacion
            //TODO quitè el candado aqui, para desactivar la autorizaciòn de revisar un documento si el usuario es igual al usuario creador
            // El candado va a estar en setUserRevise
            if (userDetails.getTorevise() == false || idUserRevise == null || userDetails == null || statusReport.equals("Creado")) {
                return "true";
            } else if (idUserRevise.getIdUser() > 0) {
                if (!Objects.equals(idUserRevise.getIdUser(), us.getIdUser())) {
                    return "true";
                    //return validateUserRoleFidelAlejandro();
                }
                //return validateUserRoleFidelAlejandro();
                //validar que el reporte tenga estado de "Revisiomn" o "Autorizacion"
                switch (statusReport) {
                    case "Creado":
                    case "Elaboracion":
                    case "Elabordo":
                        return "true";
                    case "Revision":
                    case "Revisado":
                    case "Autorizacion":
                    case "Autorizado":
                        return "false";
                    case "Entregado":
                        // Ya está autorizado y entregado (¿¿revertir??)
                        return "true";
                    default:
                        return "true";
                }
            } else {
                // TODO qué sería exactamante un ID 0???
                System.out.println("permite por iduser 0");
                return "false";
            }
        }
    }

    public String validateBtnAutorizar(Users idUserCreate, Users idUserRevise, Users idUserAuthorize, String statusReport) {
        //Obtenemos el id del usuario con la sesion actual iniciada
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        //consultamos que tipo de rol tiene sobre el reporte
        if (ejbFacadeUserRoleReport.findUserRoleReportById(us.getIdUser()).isEmpty()) {
            return "true";
        } else {
            //UserRoleReport userDetails = (UserRoleReport) ejbFacadeUserRoleReport.findUserRoleReportById(us.getIdUser()).get(0);
            //System.out.println("permiso botón autorizar::" + userDetails.getTorevise());
            //primera etapa de validacion
            //System.out.println("idUserAuthorize  : " + idUserAuthorize+" vs "+us.getIdUser());
            //if (userDetails.getToaauthorize() == false || us.getIdUser() == idUserCreate || us.getIdUser() == idUserRevise || userDetails == null || statusReport.equals("Creado") || statusReport.equals("Elaboracion") || statusReport.equals("Elaborado") || statusReport.equals("Revision")||statusReport.equals("Revisado")) {
            if (us.getIdUser() == 46) {
                //Usuario Alejandro Sánchez siempre puede autorizar
                return "false";
            } else if (idUserAuthorize == null) {
                //Si es 0 idUserauthorize, deshabilita el botón
                return "true";
            } else if (idUserAuthorize.getIdUser() == us.getIdUser()) {
                //Estatus previo a autorizacion, usuario no habilitado para autorizar, o usuario crrador o revisor
                //if(userDetails.getToaauthorize() == false  || idUserRevise ==  us.getIdUser()  || userDetails == null || statusReport.equals("Creado") || statusReport.equals("Elaboracion")) {

                //segunda etapa de validacion
                //validar que el reporte tenga estado de "Revision" o "Autorizacion"
                switch (statusReport) {
                    case "Creado":
                    case "Elaboracion":
                    case "Revision":
                    case "Entregado": // Ya está autorizado (¿¿revertir??)
                        return "true";
                    case "Autorizacion":
                    case "Autorizado":
                        return "false";
                    default:
                        return "true";
                }
            } else {
                //Si el usuario no es el autorizador del proyecto ni Aljandro Sánchez, deshabilita
                return "true";
            }
        }
    }

    //Metodo para validar si ya ha subido su firma
    public Users getUserContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users user = (Users) context.getExternalContext().getSessionMap().get("usuario");

        return user;
    }

    //Metodo obtiene el userRoleReport de la sesion iniciada actualmente
    public UserRoleReport getUserRoleReportContext() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users user = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (ejbFacadeUserRole.findUserRoleReportById(user.getIdUser()).isEmpty()) {
            return null;
        } else {
            UserRoleReport userRole = ejbFacadeUserRole.findUserRoleReportById(user.getIdUser()).get(0);
            return userRole;
        }
    }

    public String validateUploadImageSign() {
        UserRoleReport userRole = getUserRoleReportContext();
        System.out.println("rol de usuario " + userRole.getIdUser().getFirstName());
        System.out.println("imagen de usuario " + userRole.getPathimage());

        if (userRole.getPathimage() == null) {
            return "true";
        } else {
            return "false";
        }
    }

    //Metodos para validar botones de generar, revisar y autorizar            Final
    public String validateMenuReportImageSign() {
        UserRoleReport userRole = getUserRoleReportContext();
        if (userRole.getPathimage() == null) {
            return "true";
        } else {
            return "false";
        }
    }

    public String validateBtnGenerarWordCreate() {
        return null;
    }

    public String validateBtnMenuReportEdit() {//Valdida botones de edicion
        switch (current.getStatus()) {
            case "Creado":
            case "Elaboracion":
                return "false";
            default:
                return "true";
        }
    }

    public String validateBtnFirmaCrear() {
        switch (current.getStatus()) {
            case "Creado":
            case "Elaborado":
            case "Revision":
                return "true";
            default:
                if (current.getPathcreate() == null) {
                    return "true";
                } else {
                    return validateUploadImageSign();
                }
        }
    }

    public String validateBtnQuitarFirmaCrear() {
        switch (current.getStatus()) {
            case "Creado":
            case "Elaboracion":
                return "true";
            default:
                return "false";
        }
    }

    public String validateBtnFirmarRevisar() {
        switch (current.getStatus()) {
            case "Creado":
            case "Elaboracion":
            case "Elaborado":
            case "Revisado":
            case "Autorizacion":
                //Si está todavìa en creado o eleboración, se deshabilita
                return "true";
            default:
                if (current.getIdUserRevise() == null) {
                    return "true";
                } else {
                    if (current.getPathrevise() == null) {
                        return "true";
                    } else {
                        return validateUploadImageSign();
                    }
                }
        }
    }

    public String validateBtnQuitarFirmaRevisar() {
        switch (current.getStatus()) {
            case "Creado":
            case "Elaboracion":
            case "Elaborado":
            case "Revision":
                //Si está todavìa en creado o eleboración, se deshabilita
                return "true";
            default:
                if (current.getIdUserRevise() != null) {
                    //return "false";
                    if (current.getPathrevise() != null) {
                        return "false";
                    } else {
                        return validateUploadImageSign();
                        //return "false";
                    }
                } else {
                    return "true";
                }
        }
    }

    public String validateBtnEliminarWordRevise() {
        if (current.getStatus().equals("Revisado")) {
            return "false";
        } else {
            return "true";
        }
    }

    public String validateBtnFirmarAutorizar() {
        switch (current.getStatus()) {
            //case "Revisado":
            case "Autorizacion":
                if (current.getIdUserAuthorize() == null) {
                    return "true";
                } else {
                    if (current.getPathauthorize() == null) {
                        return "true";
                    } else {
                        return validateUploadImageSign();
                    }
                }
            default:
                return "true";
        }
    }

    public String validateBtnDescargarAutorizar() {
        switch (current.getStatus()) {
            case "Autorizado":
            case "Entregado":
                if (current.getIdUserAuthorize() == null) {
                    return validateUploadImageSign();
                } else {
                    return "false";
                }
            default:
                return "true";
        }
    }

    public String validateBtnQuitarFirmaAutorizar() {
        switch (current.getStatus()) {
            case "Autorizado":
            case "Entregado":
                if (current.getIdUserAuthorize() == null) {
                    return validateUploadImageSign();
                } else {
                    return "false";
                }
            default:
                return "true";
        }
    }

    public String validateBtnEliminarPDFAutorizar() {
        if (current.getStatus().equals("Autorizado")) {
            return "false";
        } else {
            return "true";
        }
    }

    public String validateBtnEnviarEmail(String reportStatus) {
        ///O sea, cuando está autorizado, no hay vuelta de hoja. pero sólamente así está deshabilitado el botoncito.
        if (current.getStatus().equals(reportStatus)) {
            if (reportStatus.equals("Autorizado")) {
                //Si estamos en la autorización, hacemos una validación extra, que exista el PDF
                if (current.getPathauthorizePDF_exists()) {
                    return "false";
                } else {
                    return "true";
                }
            } else {
                return "false";
            }
        } else {
            return "true";
        }
    }

    // metodo redireccionar a menu reporte de creador
    public String redirectMenuReportCreate(String idProject, String tipoReport) {
        String mensaje;
        //String idProject = "Project_HSalgado_2018-03-20_10:30:30";
        varTypeReport = tipoReport;
        varIdProject = idProject;
        //int sizeField = ejbFacade.existsReportProject("Project_HSalgado_2018-03-20_10:30:30", tipoReport);
        try {
            ReportProject itemReportProject = (ReportProject) (ReportProject) ejbFacade.findReportProjectidPType(idProject, tipoReport).get(0);
            varIdReportProject = itemReportProject.getIdReportProject();
            current = getFacade().find(varIdReportProject);
            Project proj = getEjbFacadeProject().find(idProject);
            this.selectedRepoProject = proj;
            //selectedRepoProject = getEjbFacadeProject().find(current.getIdProject());
            //Si el proyecto existe en report_project:
            //Validar, si para este proyecto ya se ha iniciado un reporte.
            //Si se ha iniciado un reporte verificar el estatus, para saber a donde redirigirlo.
            //Verificar si existe un registro relacionado en sample_report_project
            //return redirectSelectReport();
            messageFinal = "Su reporte con nombre " + current.getIdProject().getProjectName() + " con tipo de reporte: " + current.getName() + " esta disponible para revisión desde la plataforma de SISBI";
            subjetFinal = "Reporte del proyecto listo para revisión: " + current.getIdProject().getIdProject();
            return redirectFormTypeMethodology(null);

        } catch (Exception e) {
            showError("Error en redirectMenuReportCreate", e);
            return "SeleccionTipoReporte.xhtml";
        }
    }

    // metodo redireccionar a continuar creaciòn de reporte
    public String redirectMenuReportContinue(String idProject, String tipoReport) {
        String mensaje;
        varTypeReport = tipoReport;
        varIdProject = idProject;
        //int sizeField = ejbFacade.existsReportProject("Project_HSalgado_2018-03-20_10:30:30", tipoReport);
        try {
            ReportProject itemReportProject = (ReportProject) (ReportProject) ejbFacade.findReportProjectidPType(idProject, tipoReport).get(0);
            varIdReportProject = itemReportProject.getIdReportProject();
            current = getFacade().find(varIdReportProject);
            Project proj = getEjbFacadeProject().find(idProject);
            this.selectedRepoProject = proj;

            //Si el proyecto existe en report_project:
            //Validar, si para este proyecto ya se ha iniciado un reporte.
            //Si se ha iniciado un reporte verificar el estatus, para saber a donde redirigirlo.
            //Verificar si existe un registro relacionado en sample_report_project
            //return redirectSelectReport();
            messageFinal = "Su reporte con nombre " + current.getIdProject().getProjectName() + " con tipo de reporte: " + current.getName() + " esta disponible para revisión desde la plataforma de SISBI";
            subjetFinal = "Reporte del proyecto listo para revisión: " + current.getIdProject().getIdProject();
            return redirectFormTypeMethodology(null);

        } catch (Exception e) {
            showError("Error al ejecutar redirectMenuReportContinue", e);
            return "SeleccionTipoReporte.xhtml";
        }
    }

    // metodos para vista menuReportRevise
    public String redirectMenuReportRA(int idReportProject, String typeReport, String idProject, String typeRol) {
        //asignando valor a varIdReportProject
        varIdReportProject = idReportProject;
        //asignando valor a varIdReportProject
        varTypeReport = typeReport;
        //asignando valor a varIdProject
        varIdProject = idProject;
        //Asignando valor al objeto de la clase
        current = getFacade().find(varIdReportProject);

        System.out.println("varIdReportProject ::: " + varIdReportProject + "\n"
                + "varTypeReport  ::: " + varTypeReport + "\n"
                + "varIdProject :::: " + varIdProject
        );

        if (typeRol.equals("Revision")) {
            //inicializamos los valores para el envío de correo
            subjetFinal = "Reporte del proyecto listo para autorización: " + current.getIdProject().getIdProject();
            messageFinal = "Su reporte con nombre " + current.getIdProject().getProjectName() + " con tipo de reporte: " + current.getName() + " esta disponible para autorizarse desde la plataforma de SISBI";
            return "menuReportRevise?faces-redirect=true&includeViewParams=true";
        } else if (typeRol.equals("Autorizacion")) {
            //inicializamos los valores para el envío de correo
            subjetFinal = "Reporte del proyecto listo: " + current.getIdProject().getIdProject();
            messageFinal = "Su reporte con nombre " + current.getIdProject().getProjectName() + " con tipo de reporte: " + current.getName() + " esta disponible desde la plataforma de SISBI";
            return "menuReportAuthorize?faces-redirect=true&includeViewParams=true";
        }
        return null;
    }

    //Metodo para firmar en usuario creador
    public String finishCreate() {
        XWPFDocument doc;
        try {
            doc = signByRole("create");
            String ruteDoc = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + typeReportInitials(current.getName()) + ".docx";
            current.setPathcreate(ruteDoc);
            //TODO Acabo de cambiar DirectoryDownloadWords por Directory
            FileOutputStream outfw = new FileOutputStream(DirectoryreportDocuments + ruteDoc);
            doc.write(outfw);
            doc.close();
            outfw.close();
            //System.out.println("Descarga de documento firmado por su creador: " + DirectoryreportDocuments+ruteDoc);
            //downloadReport(DirectoryreportDocuments+ruteDoc);
            current.setStatus("Elaborado");
            getFacade().edit(current);
            showMessage("Reporte firmado");
        } catch (IOException | InvalidFormatException e) {
            showError("Hubo un error en el método FinishRevise", e);
        }
        return "menuReport?faces-redirect=false&includeViewParams=true";
    }

    public void unfinishCreate() {
        current.setStatus("Elaboracion");
        getFacade().edit(current);
        showMessage("Firma quitada (en la base de datos)");
    }

    public void changeStat() {
        current.setStatus("Elaboracion");
        getFacade().edit(current);
        showMessage("El reporte ha cambiado al estus -Elaboracion-");
        System.out.println("**Intenta enviar correo al elaborador");
        try {
            sendEmailRoleCreateCorrect();
        } catch (Exception e) {
            showError("Error avisando el quitado firma", e);
        }
        System.out.println("****Redirige a la lista de reportes");
        redirectRepoProjectStatus();
    }

    //Metodo para firmar en usuario revisar
    public String finishRevise() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users user = (Users) context.getExternalContext().getSessionMap().get("usuario");

        Date date = new Date();
        System.out.println("fecha " + date);
        current.setIdUserRevise(user);
        current.setDateReviseIfEmpty(date);
        getFacade().edit(current);
        XWPFDocument doc;
        try {
            doc = signByRole("revise");
            String suffix_ruteDoc = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + typeReportInitials(current.getName()) + ".docx";
            String ruteDoc = "revise_" + suffix_ruteDoc;
            current.setPathrevise(ruteDoc);
            //TODO Acabo de cambiar DirectoryDownloadWords por Directory
            try (FileOutputStream outf = new FileOutputStream(DirectoryreportDocuments + ruteDoc)) {
                doc.write(outf);
                outf.flush();
                outf.close();
                doc.close();
            }
            //System.out.println("Descarga de documento firmado por su revisor: " + DirectoryreportDocuments+ruteDoc);
            //downloadReport(DirectoryreportDocuments+ruteDoc);
            current.setStatus("Revisado");
            showMessage("Documento firmado");
            getFacade().edit(current);
        } catch (IOException | InvalidFormatException e) {
            showError("Hubo un error en el método FinishRevise", e);
        }
        return "";
    }

    public String unfinishRevise() {
        current.setDateRevise(null);
        current.setStatus("Revision");
        getFacade().edit(current);
        showMessage("Firma quitada (en la base de datos)");
        try {
            sendEmailRoleCreateCorrect();
        } catch (Exception e) {
            showError("Error avisando el quitado firma", e);
        }
        return "";
    }

    public String projects_label(String label, int anl_label) {
        FacesContext context = FacesContext.getCurrentInstance();
        String trn = tipoReporteNormalizado(label);
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        List<ReportProject> report_projects = ejbFacade.findReportProjectByIdProject(pj.getIdProject());
        //List <ReportProject> rpmatches = new ArrayList<>();
        int nrp = 0;
        for (ReportProject rp : report_projects) {
            if (rp.getIdUser().equals(us)) {
                String rpnorm = tipoReporteNormalizado(rp.getName());
                if (trn.equals(rpnorm)) {
                    //rpmatches.add(rp);
                    nrp++;
                }
            }
        }
        return label + " (" + String.valueOf(nrp) + " existent reports)";
    }

    /*
    
    Leslie 24 mayo 2024; 
    1.- Este metodo deberia buscar todo la lista de reportes bioinformaticos 
    (de preferencia solo los autorizados porque son la version final que vera el usuario)
    2.- Debe permitir obtener el id_report_project,idproject para poder actualizar el authorize path
     */
    public List<ReportProject> AllReportsProjectByIdProject(String idProject) {
        List<ReportProject> reportsProject = ejbFacade.findReportProjectByIdProject(idProject);
        List<ReportProject> matching_projects = new ArrayList<>();
        for (ReportProject itemReportProject : reportsProject) {
            if (itemReportProject.getStatus().equals("Revisado") || itemReportProject.getStatus().equals("Revision")
                    || itemReportProject.getStatus().equals("Autorizado") || itemReportProject.getStatus().equals("Autorizacion")) {
                matching_projects.add(itemReportProject);
            }
        }
        return matching_projects;
    }

    public void Word_to_PDF(String document_path, String PDF_path) throws IOException, org.docx4j.openpackaging.exceptions.InvalidFormatException {
        //Genera un archivo PDF desde un documento de Word
        //PdfOptions pdfoptns = PdfOptions.create();
        //String fp = pdfoptns.getFontEncoding();
        //pdfoptns.getFontProvider().
        FileInputStream newfis = new FileInputStream(document_path);
        File pdfFile = new File(PDF_path);
        if (pdfFile.exists()) {
            System.out.println("Va a generar el PDF reemplazando " + PDF_path);
        } else {
            System.out.println("Va a generar el PDF en la ruta " + PDF_path);
        }
        try (OutputStream out = new FileOutputStream(pdfFile)) {

            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(newfis, TemplateEngineKind.Freemarker);
            // Create context Java model
            Properties properties = new Properties();
            properties.setProperty("resource.loaders", "class");
            properties.setProperty(
                    "resource.loader.class.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            ITemplateEngine templateEngine = new VelocityTemplateEngine(properties);
            //ITemplateEngine templateEngine = new FreemarkerTemplateEngine();
            //Instalé la fuente Arial en Linux
            report.setTemplateEngine(templateEngine);
            IContext context = report.createContext();

            Options options = Options.getTo(ConverterTypeTo.PDF);//.via(
            options = options.via(ConverterTypeVia.XWPF);
            PdfOptions pdfOptions = PdfOptions.create();

            //pdfOptions.fontProvider(new MyFontProvider());
            options.subOptions(pdfOptions);
            report.convert(context, options, out);
            out.flush();
            out.close();
            newfis.close();

            // A partir de aquí se consdidera una generaciòn satisfactoria del PDF
            //current.setPathauthorize(rutePdf);
        } catch (NullPointerException | EvaluationException | XDocReportException e) {
            showError("Hubo un error al generar el PDF", e);
        }
    }

    //Metodo para firmar en usuario autorizar
    public String finishAuthorize() throws org.docx4j.openpackaging.exceptions.InvalidFormatException {
        FacesContext context = FacesContext.getCurrentInstance();
        Users user = (Users) context.getExternalContext().getSessionMap().get("usuario");

        current.setIdUserAuthorize(user);
        current.setDateAuthorizeIfEmpty(new Date());
        XWPFDocument doc;

        String suffix_ruteDoc = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + typeReportInitials(current.getName()) + ".docx";
        String ruteDoc = "authorize_" + suffix_ruteDoc;
        try {
            doc = signByRole("authorize");
            current.setPathauthorize(ruteDoc);
            //TODO Acabo de cambiar DirectoryDownloadWords por Directory
            try (FileOutputStream filedoc = new FileOutputStream(DirectoryreportDocuments + ruteDoc)) {
                doc.write(filedoc);
                filedoc.flush();
                filedoc.close();
                doc.close();
            }
            System.out.println("Documento firmado por su autorizador: " + DirectoryreportDocuments + ruteDoc);
            current.setStatus("Autorizado");
        } catch (IOException | InvalidFormatException e) {
            showError("Hubo un error en el método FinishAuthorize", e);
        }
        try {
            String suffix_rutePdf = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + typeReportInitials(current.getName()) + ".pdf";
            String rutePdf = "authorize_" + suffix_rutePdf;
            Word_to_PDF(DirectoryreportDocuments + ruteDoc, DirectoryreportDocuments + rutePdf);
            current.setPathauthorize_PDF(rutePdf);
        } catch (IOException e) {
            showError("Hubo un error en la conversión de documento a PDF", e);
        }
        getFacade().edit(current);
        return "";
        //return "menuReportAuthorize?faces-redirect=true&includeViewParams=true";
    }

    public void unfinishAuthorize() {
        current.setDateAuthorize(null);
        current.setStatus("Autorizacion");
        getFacade().edit(current);
        showMessage("Firma quitada (en la base de datos)");
        try {
            sendEmailRoleReviseCorrect();
        } catch (Exception e) {
            showError("Error avisando el quitado de firma", e);
        }
        //return "";
    }

    public ReportProjectController() {
    }

    public ReportProject getSelected() {
        if (current == null) {
            //showWarning("ReportProjectController().selected es null!", "en getSelected()");
            current = new ReportProject();
            selectedItemIndex = -1;
        }
        return current;
    }

    public FieldReport getSelectedFieldReport() {
        if (classFieldReport == null) {
            showWarning("ReportProjectController()sselectedFieldReport es null!", "en getSelectedFieldReport()");
            classFieldReport = new FieldReport();
            selectedItemIndex = -1;
        }
        return classFieldReport;
    }

    private ReportProjectFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

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
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (ReportProject) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new ReportProject();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle1").getString("ReportProjectCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle1").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prueba1() {
        String mensaje;

        mensaje = "el id actual es: " + varIdReportProject;
        FacesMessage msj = new FacesMessage(mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msj);

        return null;
    }

    public void EnviarCorreo() {
        email.enviarCorreoPrueba();
    }

    //Metodos para enviar correos para continuar con el proceso de firma
    public List<UserRoleReport> getUsersRoleRevise() {
        return ejbFacadeUserRoleReport.findUsersRoleReportByRevise();
    }

    public List<Users> getUsersRevise() {
        List<UserRoleReport> usersRoleRevise;
        List<Users> usersRevise = new ArrayList<>();
        usersRoleRevise = ejbFacadeUserRoleReport.findUsersRoleReportByRevise();
        for (UserRoleReport uRr : usersRoleRevise) {
            usersRevise.add(uRr.getIdUser());
        }
        return usersRevise;
    }

    public List<Users> getUsersAuthorize() {
        List<UserRoleReport> usersRoleAuthorize;
        List<Users> usersAuthorize = new ArrayList<>();
        usersRoleAuthorize = ejbFacadeUserRoleReport.findUsersRoleReportByAuthorize();
        for (UserRoleReport uRr : usersRoleAuthorize) {
            usersAuthorize.add(uRr.getIdUser());
        }
        return usersAuthorize;
    }

    public void sendEmailToRoleRevise() {
        //List<UserRoleReport> usersRevise;
        //usersRevise = ejbFacadeUserRoleReport.findUsersRoleReportByRevise();
        //UserRoleReport userRevise;
        ArrayList<String> listEmailsRevise = new ArrayList<>();
        /*for (int i = 0; i < usersRevise.size(); i++) {
            userRevise = usersRevise.get(i);
            listEmailsRevise.add(userRevise.getIdUser().getEmail());
        }*/ //Ahora hay us solo revisor
        listEmailsRevise.add(current.getIdUserRevise().getEmail());
        String nameUserCreate = current.getIdUser().getFirstName() + current.getIdUser().getPLastName() + current.getIdUser().getMLastName();
        String nameUserRevise;
        if (current.getIdUserRevise() == null) {
            nameUserRevise = "";
        } else {
            nameUserRevise = current.getIdUserRevise().getFirstName() + current.getIdUserRevise().getPLastName() + current.getIdUserRevise().getMLastName();
        }
        System.out.println(nameUserCreate);
        System.out.println(nameUserRevise);
        email.sendEmailRoleFinish("revise", current.getIdProject().getIdProject(), current.getName(), nameUserCreate, nameUserRevise, listEmailsRevise);
    }

    public void sendEmailToRoleAutorize() {
        ArrayList<String> listEmailsAuthorize = new ArrayList<>();
        /*for (int i = 0; i < usersAuthorize.size(); i++) {
            userAuthorize = usersAuthorize.get(i);
            listEmailsAuthorize.add(userAuthorize.getIdUser().getEmail());
        }*/ // Ahora hay un solo autorizador para el proyecto
        listEmailsAuthorize.add(current.getIdUserAuthorize().getEmail());
        try {
            String nameUserCreate = current.getIdUser().getFirstName() + current.getIdUser().getPLastName() + current.getIdUser().getMLastName();
            String nameUserRevise = current.getIdUserRevise().getFirstName() + current.getIdUserRevise().getPLastName() + current.getIdUserRevise().getMLastName();
            System.out.println(nameUserCreate);
            System.out.println(nameUserRevise);
            email.sendEmailRoleFinish("authorize", current.getIdProject().getIdProject(), current.getName(), nameUserCreate, nameUserRevise, listEmailsAuthorize);
        } catch (Exception e) {
            showError("Error al enviar correo", e);
        }
    }

    //Metodos para enviar correos para corregir errores
    public void sendEmailRoleCreateCorrect() {
        email.sendEmailRoleCorrect(current.getIdProject().getIdProject(), current.getName(), current.getIdUser().getEmail());
    }

    public void sendEmailRoleReviseCorrect() {
        email.sendEmailRoleCorrect(current.getIdProject().getIdProject(), current.getName(), current.getIdUserRevise().getEmail());
    }

    public void add_collaborators_to_emails_list(List<String> emails_list) {
        //Adds the collaborator email address to the provided list object
        for (UserRole usRole : userRole()) {
            emails_list.add(usRole.getEmail());
        }
    }

    //Metodo para enviar a colaboradores y responsables de proyecto
    public String sendEmailResponsablesColaboradores() {
        getFacade().edit(current);//Guadrda cambios que se hayan hecho al ReportProject
        ArrayList<String> listEmails = new ArrayList<>();
        //Esta función es para mandar el reporte autorizado
        //TODO quitar comentación a esta línea bloque después de las pruebas
        //leslie descomento add_collaborators 07/12/23
        add_collaborators_to_emails_list(listEmails);
        listEmails.add(current.getIdUserRevise().getEmail());//Por ahora el revisor, para las pruebas
        try {
            String TRI = typeReportInitials(varTypeReport);
            String doc_base_name;
            String downloaded_base_name;
            if (current.getPathauthorizePDF_exists()) {
                doc_base_name = current.getPathauthorizePDF();
                downloaded_base_name = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + TRI + ".pdf";
            } else {
                doc_base_name = current.getPathauthorize();
                downloaded_base_name = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + TRI + ".docx";
            }
            email.sendEmailRespoablesColaboradores(subjetFinal, messageFinal, current.getIdProject().getProjectName(), current, doc_base_name, downloaded_base_name, listEmails);
            current.setStatus("Entregado");
            //Una de las especificaciones de entrega del módulo de repores es que al entregar el
            // proyecto, se diera por terminado, pero después un acuerdo fue esperar a que se cheque el
            // estatus financiero antes de terminar el proyecto. Sin embargo, para terminar el 
            // proyecto, el comando sería ProjectController.updateStatus2(), pero esta llamada
            // dependería de que el atrubuto selectedProject del controlador de proyectos esté establecido
            // correctamente, pero habría que checar quer ese establecimiento no tuviera efectos colaterales
            getFacade().edit(current);
        } catch (Exception e) {
            showError("Error al enviar correo o cambiar estado del reporte: ", e);
        }
        return "";
        //return "menuReportAuthorize?faces-redirect=true&includeViewParams=true";
    }

    public String sendEmailResponsablesColaboradoresYRevisor() {
        getFacade().edit(current);
        ArrayList<String> listEmails = new ArrayList<>();
        //TODO quitar comentación a esta línea bloque después de las pruebas
        //add_collaborators_to_emails_list(listEmails);
        listEmails.add(current.getIdUserRevise().getEmail());
        String TRI = typeReportInitials(varTypeReport);
        String doc_base_name = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + current.getIdReportProject() + "_" + TRI + ".docx";
        String downloaded_base_name = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + TRI + ".docx";
        try {
            String revise_path = "revise_" + current.getPathcreate();
            File destiny = new File(PathFiles.DirectoryreportDocuments + revise_path);
            if (destiny.exists()) {
                destiny.delete();
            }
            Files.copy(new File(PathFiles.DirectoryreportDocuments + current.getPathcreate()).toPath(), destiny.toPath());
            current.setStatus("Revision");//Bueno, esto para reviar. Se deben hacer más especìficas las funciones
            current.setPathrevise(revise_path);
            getFacade().edit(current);
        } catch (Exception e) {
            showError("Error al cambiar el estado del reporte", e);
        }
        try {
            showMessage("Correo enviado", "Se ha enviado el reporte bioinformático a revisión exitosamente");
            email.sendEmailRespoablesColaboradores(subjetFinal, messageFinal, current.getIdProject().getProjectName(), current, doc_base_name, downloaded_base_name, listEmails);
            //aqui debe enviar la noti de envio
            //showMessage("Correo enviado", "Se ha enviado el reporte bioinformático a revisión exitosamente");

        } catch (Exception e) {
            showError("Error al enviar correo", e);
        }
        return "";
        //return "menuReport?faces-redirect=true&includeViewParams=true";
    }

    public String sendEmailResponsablesColaboradoresYAutorizador() {
        getFacade().edit(current);
        ArrayList<String> listEmails = new ArrayList<>();
        //TODO quitar comentación a esta línea bloque después de las pruebas
        //add_collaborators_to_emails_list(listEmails);
        listEmails.add(current.getIdUserAuthorize().getEmail());
        try {
            String TRI = typeReportInitials(varTypeReport);
            String doc_base_name = current.getPathrevise();
            String downloaded_base_name = "F01_PT05_LNATCG_" + current.getIdProject().getIdProject().replace(":", "_") + "_" + TRI + ".docx";
            email.sendEmailRespoablesColaboradores(subjetFinal, messageFinal, current.getIdProject().getProjectName(), current, doc_base_name, downloaded_base_name, listEmails);
            current.setStatus("Autorizacion");//TODO sí es así el status de 'en autorizaci¿on', 'Autorizar'?
            String authorize_path = "authorize_" + current.getPathcreate();
            File destiny = new File(PathFiles.DirectoryreportDocuments + authorize_path);
            if (destiny.exists()) {
                destiny.delete();
            }
            Files.copy(new File(PathFiles.DirectoryreportDocuments + current.getPathrevise()).toPath(), destiny.toPath());
            current.setPathauthorize(authorize_path);
            getFacade().edit(current);
        } catch (Exception e) {
            showError("Error al enviar correo o cambiar estado del reporte", e);
        }
        return "";
        //return "menuReportRevise?faces-redirect=true&includeViewParams=true";
    }

    public void backMakeChangesAuthorize() {
        subjetFinal = "Reporte del proyecto listo: " + current.getIdProject().getIdProject();
        messageFinal = "Su reporte con nombre " + current.getIdProject().getProjectName() + " con tipo de reporte: " + current.getName() + " esta disponible desde la plataforma de SISBI";
        current.setStatus("Autorizado");
        getFacade().edit(current);
    }

    public void btnCambiarRevisorOAutorizador(String cual) {
        // Se supone que el revisor ya esta cambiado, s+olo hay que guardar los cambios- Pero tambien se va a guardar también cualquier otro cambio pendiente
        getFacade().edit(current);
        showMessage("Cambiado el " + cual);
    }

    public void returnViewProject() {
        Project project1 = getEjbFacadeProject().find(varIdProject);
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", project1);
        try {
            context.getExternalContext().redirect("../project/ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String createdocumentWord() {
        return null;
    }

    public void createFormReportExpresion() {
        try {
        } catch (Exception e) {
        }
    }

    public String prepareEdit() {
        current = (ReportProject) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle1").getString("ReportProjectUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle1").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (ReportProject) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle1").getString("ReportProjectDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle1").getString("PersistenceErrorOccured"));
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

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public ReportProject getReportProject(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public List<ReportProject> getReportProjectsByIdProject(String idProject) {
        return ejbFacade.findReportProjectByIdProject(idProject);
    }

    @FacesConverter(forClass = ReportProject.class)
    public static class ReportProjectControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReportProjectController controller = (ReportProjectController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "reportProjectController");
            return controller.getReportProject(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ReportProject) {
                ReportProject o = (ReportProject) object;
                return getStringKey(o.getIdReportProject());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ReportProject.class.getName());
            }
        }
    }

    private static class SortQualityReportsByRecency implements Comparator<QualityReports> {

        public SortQualityReportsByRecency() {
        }

        // Used for sorting in ascending order of
        // roll number
        public int compare(QualityReports a, QualityReports b) {
            return b.getIdRun().getRunStartday().compareTo(a.getIdRun().getRunStartday());
        }
    }

    private static class DatesComparator implements Comparator<Date> {

        public DatesComparator() {

        }

        @Override
        public int compare(Date datea, Date dateb) {
            return datea.compareTo(dateb);
        }
    }

}
