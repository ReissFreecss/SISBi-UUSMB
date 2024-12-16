package jsf;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import jpa.entities.Run;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.RunFacade;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import jpa.entities.Barcodes;
import jpa.entities.BarcodesCons;
import jpa.entities.Comments;
import jpa.entities.Library;
import jpa.entities.LibraryRunLink;
import jpa.entities.Project;
import jpa.entities.QualityReports;
import jpa.entities.Sample;
import jpa.entities.SampleLibraryLink;
import jpa.entities.Users;
import jpa.session.CommentsFacade;
import jpa.session.LibraryRunLinkFacade;
import jpa.session.QualityReportsFacade;
import jpa.session.SampleFacade;
import jpa.session.SampleLibraryLinkFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DualListModel;

@Named("runController")
@SessionScoped
public class RunController implements Serializable {

    private Run current;
    private DataModel items = null;
    @EJB
    private jpa.session.RunFacade ejbFacade;
    @EJB
    private jpa.session.LibraryFacade ejbFacadeLibrary;
    @EJB
    private jpa.session.BarcodesFacade ejbBarcodes;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private DualListModel libraries;
    private Run selectedRun;
    private String workDirectory;
    private List<Run> edit;
    private List<Library> libs;
    private Date finishDate;
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
    private String SampleSheetName;
    private String Estatus;
    private List<Library> LibraryTable;
    private String url_Qr;

    private String messageDialog;
    private String titleDialog;

    public String getTitleDialog() {
        return titleDialog;
    }

    public void setTitleDialog(String titleDialog) {
        this.titleDialog = titleDialog;
    }

    public String getMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(String messageDialog) {
        this.messageDialog = messageDialog;
    }

    private Run runSelected;

    public Run getRunSelected() {
        return runSelected;
    }

    public void setRunSelected(Run runSelected) {
        this.runSelected = runSelected;
    }

    public String getUrl_Qr() {
        return url_Qr;
    }

    public void setUrl_Qr(String url_Qr) {
        this.url_Qr = url_Qr;
    }

    @EJB
    private jpa.session.LibraryRunLinkFacade ejbLibraryRun;
    @EJB
    private jpa.session.QualityReportsFacade QRFacade;
    @EJB
    private jpa.session.SampleLibraryLinkFacade ejbSLLF;
    @EJB
    private jpa.session.SampleFacade ejbSam;
    @EJB
    private CommentsFacade commentFac;

    public CommentsFacade getCommentFac() {
        return commentFac;
    }

    public void setCommentFac(CommentsFacade commentFac) {
        this.commentFac = commentFac;
    }

    public SampleFacade getEjbSam() {
        return ejbSam;
    }

    public void setEjbSam(SampleFacade ejbSam) {
        this.ejbSam = ejbSam;
    }

    public SampleLibraryLinkFacade getEjbSLLF() {
        return ejbSLLF;
    }

    public void setEjbSLLF(SampleLibraryLinkFacade ejbSLLF) {
        this.ejbSLLF = ejbSLLF;
    }

    public QualityReportsFacade getQRFacade() {
        return QRFacade;
    }

    public void setQRFacade(QualityReportsFacade QRFacade) {
        this.QRFacade = QRFacade;
    }

    public String getEstatus() {
        return Estatus;
    }

    public void setEstatus(String Estatus) {
        this.Estatus = Estatus;
    }

    public String getSampleSheetName() {
        return SampleSheetName;
    }

    public void setSampleSheetName(String SampleSheetName) {
        this.SampleSheetName = SampleSheetName;
    }

    public LibraryRunLinkFacade getEjbLibraryRun() {
        return ejbLibraryRun;
    }

    public void setEjbLibraryRun(LibraryRunLinkFacade ejbLibraryRun) {
        this.ejbLibraryRun = ejbLibraryRun;
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

    public String getKitPerformance() {
        return kitPerformance;
    }

    public void setKitPerformance(String kitPerformance) {
        this.kitPerformance = kitPerformance;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
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

    public String getInvestigator() {
        return Investigator;
    }

    public void setInvestigator(String Investigator) {
        this.Investigator = Investigator;
    }

    public String getRunName() {
        return runName;
    }

    public void setRunName(String runName) {
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

    public List<Library> getLibs() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listLibrary", libs);
        return libs;
    }

    public void setLibs(List<Library> libs) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listLibrary", libs);
        this.libs = libs;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public List<Run> getEdit() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("Runs", edit);
        return edit;
    }

    public void setEdit(List<Run> edit) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("Runs", edit);
        this.edit = edit;
    }

    public String getWorkDirectory() {
        return workDirectory;
    }

    public void setWorkDirectory(String workDirectory) {
        this.workDirectory = workDirectory;
    }

    public Run getSelectedRun() {
        return selectedRun;
    }

    public void setSelectedRun(Run selectedRun) {
        this.selectedRun = selectedRun;
    }

    public DualListModel getLibraries() {
        return libraries;
    }

    public void setLibraries(DualListModel libraries) {
        this.libraries = libraries;
    }

    @PostConstruct
    public void init() {
        //Librearies
        List<Library> librarySource = new ArrayList<>();
        List<Library> libraryTarget = new ArrayList<>();
        librarySource = ejbFacade.findLibraryByStatus();

        libraries = new DualListModel<>(librarySource, libraryTarget);

    }

    public RunController() {
    }

    public Run getSelected() {
        if (current == null) {
            current = new Run();
            selectedItemIndex = -1;
        }
        return current;
    }

    private RunFacade getFacade() {
        return ejbFacade;
    }

    public void getValue(Run id) {
        selectedRun = id;
        System.out.println("nombre de la corrida1: " + selectedRun.getRunName());
        runName(selectedRun);
    }

    public void runName(Run name) {
        System.out.println("Nombre de la corrida2: " + selectedRun.getRunName());
        LibraryController sendName = new LibraryController();
        //sendName.getRunName(name);
    }

    public List<Run> getRunLibraries() {
        return getFacade().findRunLibraries();
    }

    public List<Run> getRuns() {
        return getFacade().findRunlibraries();
    }
    


    public List<Library> getLibrariesRun() {
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
                lib = ejbFacadeLibrary.find(viewLibrary.getIdLibrary());
                System.out.println("objeto lib antes de insertar barcodes: "+lib);
                //Obtener los barcodes y asignarlos al nuevo library
                BarcodesCons b = ejbBarcodes.findBarcodesByIdBarcode(lib.getIdLibrary());
                
                System.out.println("Asignamos valor de los barcodes mediante vista y elimando valores nulos en relación ---------------------------------- *************+");
                //Consultamos desde la vista los id de biblioteca y barcodes 1 y 2
                ViewIdLibraryBarcodes12 vlb = ejbFacadeLibrary.finIdLibraryBarcodesByIdLibrary(viewLibrary.getIdLibrary()).get(0);

                System.out.println("------ Vista ViewBarcodes   ------------------");
                //consultamos la vista de barcode para obtener sus propiedades
                ViewBarcodes vBarcodes1 = ejbBarcodes.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);

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
                ViewBarcodes vBarcodes2 = ejbBarcodes.finIdBarcodesByIdIndex(vlb.getIdIndex1()).get(0);
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

                
                /-*
                Barcodes barExp = new Barcodes();
                System.out.println("");
                barExp = ejbBarcodes.find(vlb.getIdIndex1());
                System.out.println("Objeto barExp: "+barExp);
                System.out.println("objeto barExp getIdBarcode: "+barExp.getIdBarcode());
                System.out.println("objeto barExp getSequence: "+barExp.getSequence());
                System.out.println("---------------");
                System.out.println("valor de b.getIdBarcode1()"+ b.getIdBarcode1());
                System.out.println("valor de b.getIdBarcode2()"+ b.getIdBarcode2());
                Barcodes bar = new Barcodes();
                bar.setIdBarcode(b.getIdBarcode1());
                //System.out.println("TAG1: " + (++i) + " :\t" + b.getIdBarcode1() + "\tTAG2: " + b.getIdBarcode2());
                lib.setIdBarcode1(bar);
                bar = new Barcodes();
                bar.setIdBarcode(b.getIdBarcode2());
                lib.setIdBarcode2(bar);
                *-/
                
                System.out.println("ibjeto lib: " + lib);
                listLibrary.add(lib);
            }
        }
        return listLibrary;
*/
        if(selectedRun != null){
            System.out.println("Id de la corrida: "+ selectedRun.getIdRun());
        }
                       
        return getFacade().findLibraryRUn(selectedRun);
    }
    

    public void showTableLibraries() {
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('').show();");
    }
    
    //Método para obtener el basei5 en la vista, en cualquier vista
    public String getIndexBasei5View(Barcodes itemBarcode, String itemPlataform){
        if (itemBarcode != null) {
            //Verificamos el tipo de plataforma 
            if (itemPlataform.equals("HiSeq") || itemPlataform.equals("MiSeq") || itemPlataform.equals("Oxford Nanopore")) {
                return itemBarcode.getIndexName() + "-" + itemBarcode.getBasei5Miseq();
            } else if (itemPlataform.equals("NovaSeq") || itemPlataform.equals("NextSeq500")|| itemPlataform.equals("iSeq")) {
                return itemBarcode.getIndexName() + "-" + itemBarcode.getBasei5Nextseq();
            }
        }
        
        return null;
    }

    public void insertLibraries() {
        try {
            System.out.println("Etapa 1: Entra al metodo para agregar librerias a la corrida insertLibraries");
            List<String> tags = new ArrayList<>();
            FacesContext context = FacesContext.getCurrentInstance();
            //Primero validamos
            String nameRepeted = "";
            boolean checkTags = true;
            String combinationTag = "";
            
            //Lista para guardar el nombre de la biblioteca
            List<String> listaLibraryName = new ArrayList<>();
            
            //Consultamos las biblitecas asociadas a la corrida
            
            List<Library> listLibrarThisRun = getFacade().findLibraryRUn(selectedRun);
            System.out.println("Tamaño de las bibliotecas ya registradas11: "+ listLibrarThisRun.size());
            
            for(Library itemLibrary : LibraryTable){
                
                //Validamos si la biblioteca contiene barcodes;
                if (itemLibrary.getIdBarcode1() == null) {
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('modulDialog').show();");
                    messageDialog = "No se puede agregar a la corrida la biblioteca: "+itemLibrary.getLibraryName()+", debido a que no contiene tag 1 y 2.";
                    return;
                }
                
                String valor = itemLibrary.getIdBarcode1().getIndexName() +"-"+itemLibrary.getIdBarcode1().getBasei7();

                if (itemLibrary.getIdBarcode2() != null) {
                    valor = valor + getIndexBasei5View(itemLibrary.getIdBarcode2(), itemLibrary.getPlataformLinkKit().getPlataform().getPlataformName());
                }
                tags.add(valor);
                
                //Llenamos la lista con los nombre de las bibliotecas seleccionadas
                
                
                
                
                //Comprobamos que la biblioteca no sea repetida
                if (listaLibraryName.indexOf(itemLibrary.getLibraryName()) >= 0) {
                    RequestContext rc = RequestContext.getCurrentInstance();
                    rc.execute("PF('modulDialog').show();");
                    messageDialog = "Existen bibliotecas repetidas en la selección: ";
                    System.out.println("Se imprime lista de nombres de biblioteca: " + listaLibraryName);
                    System.out.println("Condicion: listaLibraryName.indexOf(itemLibrary.getLibraryName()) : " + listaLibraryName.indexOf(itemLibrary.getLibraryName()));
                    listaLibraryName.clear();
                    return;
                }
                System.out.println("Se agrega a la lista listaLibraryName");
                listaLibraryName.add(itemLibrary.getLibraryName());
                
                //Ciclo de las bibliotecas ya registradas a la corrida seleccionada
                System.out.println("Tamaño de las bibliotecas ya registradas22: "+ listLibrarThisRun.size());
                if (listLibrarThisRun.size() > 0) {
                    
                    //Consultamos si las bibliotecas seleccionadas son compatibles con las que ya han sido registradas
                    if (!listLibrarThisRun.get(0).getPlataformLinkKit().getPlataform().getNote().equals(itemLibrary.getPlataformLinkKit().getPlataform().getNote())) {
                        RequestContext rc = RequestContext.getCurrentInstance();
                        rc.execute("PF('modulDialog').show();");
                        messageDialog = "Plataformas incompatibles. La plataforma debe ser del tipo: " + listLibrarThisRun.get(0).getPlataformLinkKit().getPlataform().getNote();
                        context.addMessage(null, new FacesMessage("Las bibliotecas seleccionadas no son compatibles en sus plataformas"));
                        return;
                    }
                    
                    for (Library itemRunLibrary : listLibrarThisRun) {

                        //Consultamos si las bibliotecas seleccionadas no contienen tag repetido con las que ya ahn sido registradas
                        combinationTag = itemRunLibrary.getIdBarcode1().getIndexName() + "-" + itemRunLibrary.getIdBarcode1().getBasei7()
                                + ((itemRunLibrary.getIdBarcode2() != null) ? getIndexBasei5View(itemRunLibrary.getIdBarcode2(), itemRunLibrary.getPlataformLinkKit().getPlataform().getPlataformName()) : "");
                        if (combinationTag.equals(itemLibrary.getIdBarcode1().getIndexName() + "-" + itemLibrary.getIdBarcode1().getBasei7() + getIndexBasei5View(itemLibrary.getIdBarcode2(), itemLibrary.getPlataformLinkKit().getPlataform().getPlataformName()))) {
                            RequestContext rc = RequestContext.getCurrentInstance();
                            rc.execute("PF('modulDialog').show();");
                            messageDialog = "Algunas bibliotecas ya registradas contienen los tag de las biblitoecas seleccionadas: " + itemLibrary.getLibraryName() + ", Tags: " + combinationTag;
                            return;
                        }

                        //Consultamos si ya existe el nombre de la biblioteca en la corrida actual
                        if (itemRunLibrary.getLibraryName().equals(itemLibrary.getLibraryName())) {
                            RequestContext rc = RequestContext.getCurrentInstance();
                            rc.execute("PF('modulDialog').show();");
                            messageDialog = "La biblioteca: " + itemLibrary.getLibraryName() + ", Ya ha sido registrada a esta corrida";
                            return;
                        }
                    }
                }
                
            }
            
            //Verifica que los tags de las bibliotecas seleccionadas no sean repetidas
            Collections.sort(tags);
            for (int i = 0; i < tags.size() - 1; i++) {
                checkTags = true;
                if (tags.get(i).equals(tags.get(i + 1))) {
                    checkTags = false;
                    nameRepeted = tags.get(i);
                    break;
                }
            }
            if (!checkTags) {
                RequestContext rc = RequestContext.getCurrentInstance();
                System.out.println("tags repetidos");
                System.out.println(nameRepeted);
                rc.execute("PF('modulDialog').show();");
                messageDialog = "Se encontraron tags repetidos en la selección";
                context.addMessage(null, new FacesMessage("Tag Repetido" + "\n" + nameRepeted));
                return;
            }
            
            
            
            
            for (Library runLib : LibraryTable) {
                System.out.println("Etapa 2: Entramos al ciclo Library runLib : LibraryTable");
                System.out.println("id de la libreria: " + runLib.getIdLibrary());
                LibraryRunLink lrl = new LibraryRunLink();
                lrl.setLibraryRunLinkPK(new jpa.entities.LibraryRunLinkPK());
                lrl.getLibraryRunLinkPK().setIdLibrary(runLib.getIdLibrary());
                lrl.getLibraryRunLinkPK().setIdRun(selectedRun.getIdRun());
                lrl.setNote("");
                //ADAPTAR CODIGO debido a que ahora no tiene esta propiedad
                lrl.setRealPerformance(0);
                ejbLibraryRun.create(lrl);
                System.out.println("agrega librerias a la corrida");
            }
            System.out.println("Etapa 3: Salimos del ciclo Library runLib : LibraryTable");
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgSearch').hide()");
        } catch (Exception e) {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('dlgInfoError').show()");
        }

    }
    
    
    public boolean validatePlataform(){
        List<String> selectionTypePlatform = new ArrayList<>();
        for(Library itemLibrary : LibraryTable){
            //selectionPlatform.add(itemLibrary.getPlataform());
            //Ahora estamos obteniendo una plataforma
            selectionTypePlatform.add(itemLibrary.getPlataformLinkKit().getPlataform().getNote());
        }
        boolean illumina;
        //Tomamos el primer item
        if(selectionTypePlatform.get(0).equals("ILLUMINA")){
            illumina = true;
        }else{
            illumina = false;
        }
        
        if(illumina == true){
            if(selectionTypePlatform.indexOf("OXFORD NANOPORE") >= 0){
                System.out.println("illumina true: No se puede crear corrida con plataformas combinadas entre Illumina y Oxford Nanopore");
                return false;
            }
        }else{
            for(String itemPlataform : selectionTypePlatform){
                if(!itemPlataform.equals("OXFORD NANOPORE")){
                    System.out.println((itemPlataform != "Oxford Nanopore"));
                    System.out.println("Se imprime comparacion: "+itemPlataform);
                    System.out.println("illumina false: No se puede crear corrida con plataformas combinadas entre Illumina y Oxford Nanopore");
                    return false;
                }
            }
        }  
        return true;
    }
    
    

    public String validateBTNEditRun() {
        System.out.println("tamaño de edit: " + runSelected);
        if (runSelected == null) {
            return "true";
        } else {
            return "false";
        }

    }

    public String validateBTNLibraries(Run runItem) {
        System.out.println("tamaño de edit: " + runSelected);
        if (runSelected == null) {
            return "true";
        } else {
            //return "false";
            if (runSelected.getIdRun() == runItem.getIdRun()) {
                return "false";
            } else {
                return "true";
            }
        }
    }

    public void loadFieldEditRun() {
        workDirectory = runSelected.getWorkSubdirectory();
        Estatus = runSelected.getStatus();
        finishDate = runSelected.getRunFinishday();
    }

    public void createRun() {
        System.out.println("Entra al metodo createRun()");
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Run runLibrary = new Run();
        Short Cycles = Short.valueOf("75");
        runLibrary.setRunType("Paired End");
        runLibrary.setRunStartday(time);
        runLibrary.setRunFinishday(time);
        runLibrary.setSampleSheetName("prueba");
        runLibrary.setStatus("");
        runLibrary.setCycles(75);
        runLibrary.setPerformance("High");
        runLibrary.setWorkSubdirectory("");
        runLibrary.setRunName("Prueba de insercion en la tabla run");
        getFacade().create(runLibrary);
        System.out.println("Crea la corrida");
    }

    public String validateFormEditRun() {
        //reasignando valores con trim
        workDirectory = workDirectory.trim();
        if (workDirectory.equals("") || workDirectory == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo 'Subdirectorio de trabajo' es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('modulDialog').show();");
            return null;
        } else if (Estatus.equals("---") || Estatus == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo 'Estatus' es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('modulDialog').show();");
            return null;
        } else if (finishDate == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo 'Fecha de entrega' es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('modulDialog').show();");
            return null;
        } else {
            return "siguiente";
        }
    }

    public void resetFormEditRun() {
        runName=null;
        workDirectory = null;
        Estatus = null;
        finishDate = null;
    }

    public String editRun() {
        //Validación de los campos del formulario
       // if (validateFormEditRun() == null) {
         //   return null;
        //}
        

        /*
        System.out.println("RCER  1 Objeto runSelected antes de la consulta con vista: " + runSelected + "    **************************************");
        ViewRun varViewRun = ejbFacade.findRunByIdRun(runSelected.getIdRun()).get(0);

        runSelected = ejbFacade.find(varViewRun.getIdRun());
        System.out.println("RCER  2 Objeto runSelected después de la consulta con vista: " + runSelected + "    **************************************");
        */
        FacesContext con = FacesContext.getCurrentInstance();
        Users us = (Users) con.getExternalContext().getSessionMap().get("usuario");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                
        System.out.println("Entra al metodo para editar una corrida");
        //System.out.println("runName: "+runName);
        //System.out.println("workdir: "+workDirectory);
        //System.out.println("estatus: "+Estatus);
        //System.out.println("finishdate: "+finishDate);
              
        //FacesContext context = FacesContext.getCurrentInstance();
        

        //edit.add((Run) runSelected);
        System.out.println("RCER  3 Objeto runSelected antes de consulta con view: " + runSelected + "     **************************************");

        Run runEdit = runSelected;
        System.out.println("entra al ciclo for");
        //System.out.println(runEdit);

        if (runName != null && !runName.isEmpty()) {
            runEdit.setRunName(runName);
            runEdit.setSampleSheetName(runName+".csv");
            System.out.println(runName+".csv");            
        }
        
        if (workDirectory != null && !workDirectory.isEmpty()) {
            runEdit.setWorkSubdirectory(workDirectory);
        }
        if (Estatus != null && !Estatus.equals("---")) {
            runEdit.setStatus(Estatus);
        }
        if (finishDate != null && !finishDate.toString().isEmpty()) {
            runEdit.setRunFinishday(finishDate);
        }
        
        getFacade().edit(runEdit);

        System.out.println("Edita la corrida");

        //Validación para el cambio de estado de acuerdo al estado de la corrida
        if (Estatus != null && !Estatus.equals("---")) {
            System.out.println("RCER  4 c Entramos al if  Estatus != null && !Estatus.equals(\"---\")    **************************************");
            if (Estatus.equals("Complete")) {
                System.out.println("RCER C  5 Entramos al if  Estatus.equals(\"Complete\"    **************************************");
                // inicio cambio de estados    -------------------------------------------------------------------------------------
                List<Library> libsRun = getFacade().findLibraryRUn(runEdit);
                System.out.println("RCER C 6 buscamos las librerías por medio de id run    **************************************");
                System.out.println("Objetos lista libsRun: " + libsRun);
                for (Library lb : libsRun) {
                    System.out.println("RCER C 7 entramos al ciclo for para iterar las bibliotecas encontradas dec  libsRun   **************************************");
                    List<SampleLibraryLink> samples = ejbSLLF.findSamplesByID(lb);

                    /*
                    //Eliminando join con valores nullos   -------------------------------------------------- Inicio
                    //Consultamos en la tabla SampleLibraryLink los registros que tengan el id de la biblioteca mediante la vista
                    List<ViewSampleLibraryLink> runLibView = ejbSLLF.findRunByIdLibrary(lb.getIdLibrary());
                    SampleLibraryLink runSLL = new SampleLibraryLink();
                    //variable runSLL se asigna el valor de la consulta con parámetros de id's obtenidos mediante la vista
                    runSLL = ejbSLLF.findRunByIdSampleIdLibrary(runLibView.get(0).getIdSample(), runLibView.get(0).getIdLibrary()).get(0);
                    //Creamos vairbale del tipo Library
                    Library lib = new Library();
                    //Variable lib se asigna el valor de consulta de biblioteca por medio del id de la vista
                    lib = ejbFacadeLibrary.find(runLibView.get(0).getIdLibrary());
                    //Agregamos las bibliotecas a la lista
                    runSLL.setLibrary(lib);
                    //Creamos variable del tipo Sample
                    Sample sample = new Sample();
                    //Variable sample se asigna el valor de consulta de biblioteca por medio del id de la vista
                    sample = ejbSam.find(runLibView.get(0).getIdSample());
                    //Se asigna el valor de las variables al runSLL mediante el set
                    runSLL.setLibrary(lib);
                    runSLL.setSample(sample);
                    List<SampleLibraryLink> samples = new ArrayList<>();
                    //Agregamos el valor del SLL a la litsa runLib
                    samples.add(runSLL);
                    */

                    System.out.println("RCER C 8 Consultamos registros SampleLibraryLink enviando objeto biblioteca lb   **************************************");
                     //leslie 14 mayo 2024System.out.println("Objetos lista samples: " + samples);
                    for (SampleLibraryLink sm : samples) {
                        System.out.println("RCER C 9 iteramos SampleLibraryLink samples   **************************************");
                        List<Sample> editSm = ejbSam.findSampleById(sm.getSample().getIdSample());
                        System.out.println("RCER C 10 Consultamos las muestras por medio de SampleLibraryLink enviando id de sample   **************************************");
                        //leslie 14 mayo 2024System.out.println("Objetos lista editSm: " + editSm);
                        for (Sample Sam : editSm) {
                            System.out.println("RCER C 11 entramos al ciblo for de  (Sample Sam : editSm)  **************************************");
                            if (!Sam.getStatus().equals("Secuenciada") || !Sam.getStatus().equals("Basecalling y QC en proceso")) {
                                System.out.println("RCER C 12 entramos condicional de !Sam.getStatus().equals(\"Secuenciada\") || !Sam.getStatus().equals(\"Basecalling y QC en proceso\") **************************************");
                                Comments commentsSample = new Comments();
                                System.out.println("Las muestras cambian a Secuenciada");
                                commentsSample.setComment("Estatus cambia de -" + Sam.getStatus() + "- a -" + "Secuenciada" + "- por " + us.getUserName());
                                commentsSample.setIdType(Sam.getIdSample() + "");
                                commentsSample.setType("Sample");
                                commentsSample.setUserName("SISBI");
                                commentsSample.setCommentDate(timestamp);
                                commentFac.createComment(commentsSample);

                                System.out.println("RCER C 13 Se crea y se registra el comentario de cambio de status para la muestra iterada  **************************************");

                                Sam.setStatus("Secuenciada");
                                ejbSam.edit(Sam);
                                System.out.println("RCER C 14 Se cambia el status para la muestra iterada  **************************************");
                                //////////////////////////////////////////////////////////////////
                                System.out.println("Las muestras cambian a BaseCalling");
                                commentsSample.setComment("Estatus cambia de -" + Sam.getStatus() + "- a -" + "Basecalling y QC en proceso" + "- por " + us.getUserName());
                                commentsSample.setIdType(Sam.getIdSample() + "");
                                commentsSample.setType("Sample");
                                commentsSample.setUserName("SISBI");
                                commentsSample.setCommentDate(timestamp);
                                commentFac.createComment(commentsSample);

                                System.out.println("RCER C 15 Se crea y se registra el comentario de cambio de status para la muestra iterada 2  **************************************");

                                Sam.setStatus("Basecalling y QC en proceso");
                                ejbSam.edit(Sam);

                                System.out.println("RCER C 16 Se cambia el status para la muestra iterada 2  **************************************");
                            }
                        }
                    }

                }

                // final cambio de estados    -------------------------------------------------------------------------------------
            }//final if complete
            else if (Estatus.equals("Running")) {
                System.out.println("RCER R  5 Entramos al if  Estatus.equals(\"Running\"    **************************************");
                // inicio cambio de estados    -------------------------------------------------------------------------------------
                List<Library> libsRun = getFacade().findLibraryRUn(runEdit);
                System.out.println("RCER R  6 libsRun: Se consultan las bibliotecas enviando parámetro del objeto run (id)    **************************************");
                for (Library lb : libsRun) {
                    System.out.println("RCER R  7  Se itera libsRun   **************************************");
                    List<SampleLibraryLink> samples = ejbSLLF.findSamplesByID(lb);
                    
                    
                    /*
                    //Eliminando join con valores nullos   -------------------------------------------------- Inicio
                    //Consultamos en la tabla SampleLibraryLink los registros que tengan el id de la biblioteca mediante la vista
                    List<ViewSampleLibraryLink> runLibView = ejbSLLF.findRunByIdLibrary(lb.getIdLibrary());
                    SampleLibraryLink runSLL = new SampleLibraryLink();
                    //variable runSLL se asigna el valor de la consulta con parámetros de id's obtenidos mediante la vista
                    runSLL = ejbSLLF.findRunByIdSampleIdLibrary(runLibView.get(0).getIdSample(), runLibView.get(0).getIdLibrary()).get(0);
                    //Creamos vairbale del tipo Library
                    Library lib = new Library();
                    //Variable lib se asigna el valor de consulta de biblioteca por medio del id de la vista
                    lib = ejbFacadeLibrary.find(runLibView.get(0).getIdLibrary());
                    //Agregamos las bibliotecas a la lista
                    runSLL.setLibrary(lib);
                    //Creamos variable del tipo Sample
                    Sample sample = new Sample();
                    //Variable sample se asigna el valor de consulta de biblioteca por medio del id de la vista
                    sample = ejbSam.find(runLibView.get(0).getIdSample());
                    //Se asigna el valor de las variables al runSLL mediante el set
                    runSLL.setLibrary(lib);
                    runSLL.setSample(sample);
                    List<SampleLibraryLink> samples = new ArrayList<>();
                    //Agregamos el valor del SLL a la litsa runLib
                    samples.add(runSLL);
                    */
                    
                    System.out.println("RCER R  8 List samples de SampleLibraryLink: Se consultan las SampleLibraryLink enviando parámetro del objeto library lb (id)    **************************************");
                    for (SampleLibraryLink sm : samples) {
                        System.out.println("RCER R  9  Se itera sm   **************************************");
                        List<Sample> editSm = ejbSam.findSampleById(sm.getSample().getIdSample());
                        System.out.println("RCER R  10 List editSm de Sample: Se consultan las muestras enviando parámetro del objeto library lb (id)    **************************************");
                        for (Sample Sam : editSm) {
                            System.out.println("RCER R  11  se iteran las muestras ciclo for Sample Sam : editSm  **************************************");
                            if (!Sam.getStatus().equals("Secuenciandose")) {
                                System.out.println("RCER R  12  Entramos a la validación si el estado no es 'Secuenciandose'  **************************************");
                                Comments commentsSample = new Comments();
                                System.out.println("Las muestras cambian a Secuenciandose");
                                commentsSample.setComment("Estatus cambia de -" + Sam.getStatus() + "- a -" + "Secuenciandose" + "- por " + us.getUserName());
                                commentsSample.setIdType(Sam.getIdSample() + "");
                                commentsSample.setType("Sample");
                                commentsSample.setUserName("SISBI");
                                commentsSample.setCommentDate(timestamp);
                                commentFac.createComment(commentsSample);
                                System.out.println("RCER R  13 Se registra el comentario en comments de cambio de estado de muestra **************************************");

                                Sam.setStatus("Secuenciandose");
                                ejbSam.edit(Sam);
                                System.out.println("RCER R  14 Se cambia el estado de la muestra a 'Secuenciandose' **************************************");
                            }
                        }
                    }
                }

                // final cambio de estados    -------------------------------------------------------------------------------------
            }
        }

        runSelected = null;
        resetFormEditRun();
        System.out.println("RCER  5 se asigna el valor de null a  runSelected  **************************************");

        System.out.println("RCER  6 Completado  **************************************");
        RequestContext rc = RequestContext.getCurrentInstance();
        rc.execute("PF('DialogEditRun').hide()");

        titleDialog = "Edición de corrida.";
        messageDialog = "Se editó la corrida correctamente";
        rc.execute("PF('modulDialog').show();");

        return null;
    }

    public void createReport() {
        System.out.println("Entra al metodo createQR");
        FacesContext context = FacesContext.getCurrentInstance();

        RequestContext rc = RequestContext.getCurrentInstance();
        for (Run rn : edit) {

            System.out.println("runID:" + rn.getIdRun());
            System.out.println("file:" + rn.getSampleSheetName());

            QualityReports qr = new QualityReports();
            qr.setIdProject(null);
            qr.setIdRun(rn);
            qr.setSampleSheetFile(rn.getSampleSheetName());
            qr.setUrlQualityReport(url_Qr);
            qr.setType("General");
            QRFacade.create(qr);
            System.out.println("Crea reporte Gral.");
            CreateReportByProject(rn, rn.getSampleSheetName(), url_Qr);
        }
        rc.execute("PF('QualityDialog').hide();");
    }

    public String addLinkReportRun() {
        url_Qr = url_Qr.trim();
        if (url_Qr.equals("") || url_Qr == null) {
            System.out.println("Existen campos vacíos");
            messageDialog = "El campo 'Link de reporte' es obligatorio";
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('modulDialog').show();");
            return null;
        }

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext rc = RequestContext.getCurrentInstance();
        
        System.out.println("Link a guardar: " + url_Qr);
        Run rn = runSelected;

        System.out.println("runID:" + rn.getIdRun());
        System.out.println("file:" + rn.getSampleSheetName());

        QualityReports qr = new QualityReports();
        qr.setIdProject(null);
        qr.setIdRun(rn);
        qr.setSampleSheetFile(rn.getSampleSheetName());
        qr.setUrlQualityReport(url_Qr);
        qr.setType("General");
        QRFacade.create(qr);
        System.out.println("Crea reporte Gral.");
        CreateReportByProject(rn, rn.getSampleSheetName(), url_Qr);

        //resetear campo url
        url_Qr = null;
        rc.execute("PF('QualityDialog').hide();");

        return null;
    }

    //Modificar y usar listas para tratar las excepciones de nulos
    public void CreateReportByProject(Run idRun, String SampleSheet, String URL) {
        //leslie
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //fin leslie
        List<Project> idProject = new ArrayList<>();
        String proyecto = "";
        //for (Run rn : edit) {
        Run rn = runSelected;
        List<Library> runLibs = getFacade().findLibraryRUn(rn);
        
        //leslie 17 sep: busca las librerias segun el id de la corrida
        for (Library lb : runLibs) {
            //leslie 17 sep: crea un objeto lb para cada elemeto de la lista run Libs
            List<SampleLibraryLink> sll = ejbSLLF.findSamplesByID(lb);
 
            /*
            //Llenar valores nulos usando vistas -----------------------------------------------   ViewSampleLibraryLink  Inicio
            //Eliminando join con valores nullos   -------------------------------------------------- Inicio
            //Consultamos en la tabla SampleLibraryLink los registros que tengan el id de la biblioteca mediante la vista
            List<ViewSampleLibraryLink> runLibView = ejbSLLF.findRunByIdLibrary(lb.getIdLibrary());
            SampleLibraryLink runSLL = new SampleLibraryLink();
            //variable runSLL se asigna el valor de la consulta con parámetros de id's obtenidos mediante la vista
            runSLL = ejbSLLF.findRunByIdSampleIdLibrary(runLibView.get(0).getIdSample(), runLibView.get(0).getIdLibrary()).get(0);
            //Creamos vairbale del tipo Library
            Library lib = new Library();
            //Variable lib se asigna el valor de consulta de biblioteca por medio del id de la vista
            lib = ejbFacadeLibrary.find(runLibView.get(0).getIdLibrary());
            //Agregamos las bibliotecas a la lista
            runSLL.setLibrary(lib);
            //Creamos variable del tipo Sample
            Sample sample = new Sample();
            //Variable sample se asigna el valor de consulta de biblioteca por medio del id de la vista
            sample = ejbSam.find(runLibView.get(0).getIdSample());
            //Se asigna el valor de las variables al runSLL mediante el set
            runSLL.setLibrary(lib);
            runSLL.setSample(sample);
            List<SampleLibraryLink> sll = new ArrayList<>();
            //Agregamos el valor del SLL a la litsa runLib
            sll.add(runSLL);
*/
            
            //Llenar valores nulos usando vistas -----------------------------------------------   ViewSampleLibraryLink  Final
            
            for (SampleLibraryLink proj : sll) {
                idProject.add(proj.getSample().getIdProject());
                //lb.getSample().getIdProject();
                
                //leslie 17 sep: aqui debo agregar los comments  de edicion de estatus para cuando agrege el reporte QC
                
                List<Sample> editSm = ejbSam.findSampleById(proj.getSample().getIdSample());
                for (Sample Sam : editSm) {
                            System.out.println("verifico si la muestra esta en el estatus Basecalling y QC en proceso");
                            if (Sam.getStatus().equals("Basecalling y QC en proceso")) {
                                System.out.println("Entramos a cambiar el status de la muestra a Basecalling y QC terminado");
                                Comments commentsSample = new Comments();
                                System.out.println("Las muestras cambian a Basecalling y QC terminado");
                                commentsSample.setComment("Estatus cambia de -" + Sam.getStatus() + "- a -" + "Basecalling y QC terminado" + "- por " + us.getUserName());
                                commentsSample.setIdType(Sam.getIdSample() + "");
                                commentsSample.setType("Sample");
                                commentsSample.setUserName("SISBI");
                                commentsSample.setCommentDate(timestamp);
                                commentFac.createComment(commentsSample);

                                System.out.println("Se crea y se registra el comentario de cambio de status para la muestra iterada");

                                Sam.setStatus("Basecalling y QC terminado");
                                ejbSam.edit(Sam);
                                System.out.println("Se cambia el status para la muestra iterada");                               
                            }
                 }               
                //fin leslie
            }
        }

        //}
        List<Project> Distinct = idProject.stream().distinct().collect(Collectors.toList());
        for (Project pj : Distinct) {
            proyecto = pj.getIdProject();
            String pjFormated = Normalizer.normalize(proyecto, Normalizer.Form.NFD);
            String Formated = pjFormated.replaceAll("[^\\p{ASCII}]", "");
            QualityReports qr = new QualityReports();
            qr.setIdProject(pj);
            qr.setIdRun(idRun);
            qr.setSampleSheetFile(SampleSheet);
            qr.setUrlQualityReport(URL + "/" + Formated + ".html");
            qr.setType("Project");
            QRFacade.create(qr);
            System.out.println("Crea reporte por proyecto");

        }
    }

    public void editSampleSheetName(String name) {
        for (Run runEdit : edit) {

            runEdit.setSampleSheetName(name);

            getFacade().edit(runEdit);

            System.out.println("Edita la corrida");
        }

    }

    public void checkSelectedLibraries() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (libs.isEmpty()) {
            context.addMessage(null, new FacesMessage("¡ERROR!", "No han seleccionado ninguna libreria para Generar la SampleSheet"));
        } else {
            RequestContext rc = RequestContext.getCurrentInstance();
            rc.execute("PF('DialogCreate').show()");
        }
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
        current = (Run) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Run();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RunCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Run) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RunUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Run) getItems().getRowData();
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
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RunDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
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

    public Run getRun(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Run.class)
    public static class RunControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RunController controller = (RunController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "runController");
            return controller.getRun(getKey(value));
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
            if (object instanceof Run) {
                Run o = (Run) object;
                return getStringKey(o.getIdRun());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Run.class.getName());
            }
        }

    }

}
