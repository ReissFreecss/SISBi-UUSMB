package jsf;

import java.io.File;
import java.io.IOException;
import jpa.entities.BalancePago;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.BalancePagoFacade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import jpa.entities.Pago;
import jpa.entities.Project;
import jpa.entities.ProyCotizaFacPagoLink;
import jpa.entities.Users;
import jpa.session.PagoFacade;
import jpa.session.ProyCotizaFacPagoLinkFacade;
import static jsf.FilesProjectController.URL;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("balancePagoController")
@SessionScoped
public class BalancePagoController implements Serializable {

    private BalancePago current;
    private DataModel items = null;
    @EJB
    private jpa.session.BalancePagoFacade ejbFacade;
    @EJB
    private jpa.session.PagoFacade ejbPago;
    @EJB
    private jpa.session.ProyCotizaFacPagoLinkFacade EjbCotLink;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private Users selectedUser;
    private Pago slectedPago;
    private Date dateBalance;
    private  BigDecimal mount;
    private Double charge=0.0;
    private String ref;
    private String observations;
    private String observationsPayment;
    private String poliza;
    private String description;
    private String descriptionPayment;
    private Double abono=null;
    private Double saldo;
    private BalancePago selectedBalance;
    private List<Project> projectTable;
    private String payObservation;
    private int pagoId;
    private String cotizacion;
    private String cotCharge;
    private UploadedFile file;
    private String urlFile;
    private String polizaCharge;
    private String refCharge;
    private List<BalancePago> BalanceTable;
    private List<Pago> pagoTable;

    public List<Pago> getPagoTable() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listPago", pagoTable);
        return pagoTable;
    }

    public void setPagoTable(List<Pago> pagoTable) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listPago", pagoTable);
        this.pagoTable = pagoTable;
    }
    

    public List<BalancePago> getBalanceTable() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listBalance", BalanceTable);
        return BalanceTable;
    }

    public void setBalanceTable(List<BalancePago> BalanceTable) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listBalance", BalanceTable);
        this.BalanceTable = BalanceTable;
    }
    

    public String getPolizaCharge() {
        return polizaCharge;
    }

    public void setPolizaCharge(String polizaCharge) {
        this.polizaCharge = polizaCharge;
    }

    public String getRefCharge() {
        return refCharge;
    }

    public void setRefCharge(String refCharge) {
        this.refCharge = refCharge;
    }
    
    
    public static final String PATH = "/var/www/html/projectFiles";
    public static final String url_voucher="http://www.uusmb.unam.mx/projectFiles/BalancePago/";

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getCotCharge() {
        return cotCharge;
    }

    public void setCotCharge(String cotCharge) {
        this.cotCharge = cotCharge;
    }
    

    public String getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(String cotizacion) {
        this.cotizacion = cotizacion;
    }

    public int getPagoId() {
        return pagoId;
    }

    public void setPagoId(int pagoId) {
        this.pagoId = pagoId;
    }

    public String getPayObservation() {
        return payObservation;
    }

    public void setPayObservation(String payObservation) {
        this.payObservation = payObservation;
    }

    public Pago getSlectedPago() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("Pago", slectedPago);
        return slectedPago;
    }

    public void setSlectedPago(Pago slectedPago) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("Pago", slectedPago);
        this.slectedPago = slectedPago;
    }
    
    public ProyCotizaFacPagoLinkFacade getEjbCotLink() {
        return EjbCotLink;
    }

    public void setEjbCotLink(ProyCotizaFacPagoLinkFacade EjbCotLink) {
        this.EjbCotLink = EjbCotLink;
    }
    

    public List<Project> getProjectTable() {
          FacesContext context = FacesContext.getCurrentInstance();
          context.getExternalContext().getSessionMap().put("listProjects", projectTable);
        return projectTable;
    }

    public void setProjectTable(List<Project> projectTable) {
         FacesContext context = FacesContext.getCurrentInstance();
          context.getExternalContext().getSessionMap().put("listProjects", projectTable);
        this.projectTable = projectTable;
    }

    public String getObservationsPayment() {
        return observationsPayment;
    }

    public void setObservationsPayment(String observationsPayment) {
        this.observationsPayment = observationsPayment;
    }
    

    public String getDescriptionPayment() {
        return descriptionPayment;
    }

    public void setDescriptionPayment(String descriptionPayment) {
        this.descriptionPayment = descriptionPayment;
    }
    

    public BalancePago getSelectedBalance() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("balance", selectedBalance);
        return selectedBalance;
    }

    public void setSelectedBalance(BalancePago selectedBalance) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("balance", selectedBalance);
        this.selectedBalance = selectedBalance;
    }
    

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }
    
    

    public PagoFacade getEjbPago() {
        return ejbPago;
    }

    public void setEjbPago(PagoFacade ejbPago) {
        this.ejbPago = ejbPago;
    }
    
    

    public Double getAbono() {
        return abono;
    }

    public void setAbono(Double abono) {
        this.abono = abono;
    }
    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getPoliza() {
        return poliza;
    }

    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }
    

    public Date getDateBalance() {
        return dateBalance;
    }

    public void setDateBalance(Date dateBalance) {
        this.dateBalance = dateBalance;
    }

    public BigDecimal getMount() {
        return mount;
    }

    public void setMount(BigDecimal mount) {
        this.mount = mount;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }
    

    public Users getSelectedUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("UserBalance", selectedUser);
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("UserBalance", selectedUser);
        this.selectedUser = selectedUser;
    }

    public BalancePagoController() {
    }

    public BalancePago getSelected() {
        if (current == null) {
            current = new BalancePago();
            selectedItemIndex = -1;
        }
        return current;
    }

    private BalancePagoFacade getFacade() {
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
    
     public List<BalancePago>getBalanceUser(){
     
             FacesContext context=FacesContext.getCurrentInstance();
             Users user=(Users) context.getExternalContext().getSessionMap().get("usuario");
             
             return ejbFacade.findBalanceByUser(user,user.getIdUser());
        }
     
     public List<Project>getProjectUserResponsible(){
         FacesContext context=FacesContext.getCurrentInstance();
          Users us = (Users) context.getExternalContext().getSessionMap().get("UserBalance");
          return ejbFacade.findProUsersResponsible(us);
         
     }
     public List<Project>getProjectsUserResponsible(){
         FacesContext context=FacesContext.getCurrentInstance();
          Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
          return ejbFacade.findProUsersResponsible(us);
         
     }
     
     public List<BalancePago>getBalanceByUser(){
         FacesContext context=FacesContext.getCurrentInstance();
         Users us=(Users) context.getExternalContext().getSessionMap().get("UserBalance");
         
         return ejbFacade.findBalanceByUser(us,us.getIdUser());
         
     }
     
      public void getValue(Pago id){
          pagoId=id.getPagoId();
          slectedPago=id;
          paymentDetails();
        
          
      }
       public List<Pago> paymentDetails(){
      System.out.println(pagoId);
      return ejbFacade.findDetailsPayment(pagoId);
      
      }
       public List<Project> getProjectAssociated(){
            System.out.println(pagoId);
            if(slectedPago==null){
                System.out.println("esta vacio");
            }else{
           return ejbFacade.findProjectAssociatedAPayment(slectedPago);
            }
            return null;
       }
       
       public String URL(){
           String url="";
           List<Pago> file=ejbFacade.findDetailsPayment(pagoId);
           for(Pago dir:file){
               url=dir.getUrl_voucher();
           }
           return url;
       }
      public void imprimir(){
          
          System.out.println(pagoId);
      }
     
   
   public String selectedUserNameBalance(){
      String uName="";
      FacesContext context=FacesContext.getCurrentInstance(); 
      Users us = (Users) context.getExternalContext().getSessionMap().get("UserBalance");
                 if(us.getMLastName()==null){
                   uName=us.getFirstName()+" "+us.getPLastName();  
                 }else
                 uName=us.getFirstName()+" "+us.getPLastName() + " " +us.getMLastName();
      return uName;
   }
    public String userNameBalance(){
      String uName="";
      FacesContext context=FacesContext.getCurrentInstance(); 
      Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
                 if(us.getMLastName()==null){
                   uName=us.getFirstName()+" "+us.getPLastName();  
                 }else
                 uName=us.getFirstName()+" "+us.getPLastName() + " " +us.getMLastName();
      return uName;
   }
   public Date DateCharge(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp;
   }
      public void uploadFile(FileUploadEvent event) {
       FacesContext context = FacesContext.getCurrentInstance();
       Users us=(Users) context.getExternalContext().getSessionMap().get("UserBalance");
       
      // System.out.println(us.getUserName());
        file = event.getFile();

         String url = PATH + "/BalancePago/";
         
       File folder = new File(url);
        if (!folder.exists()) {
            folder.mkdir();
        }
        try {
            java.nio.file.Files.deleteIfExists(new File(url, file.getFileName().replaceAll("\\ ,", "_")).toPath());
            java.nio.file.Files.copy(file.getInputstream(), new File(url, file.getFileName().replaceAll("\\ ,", "_")).toPath());
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlFile = URL+ "/" + "UserBalance_/"+ file.getFileName();

        System.out.println(event.getFile().getFileName());
        FacesMessage message = new FacesMessage("Comprobante cargado satisfactoriamente", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }
   public void createBalance(){
       FacesContext context=FacesContext.getCurrentInstance();
       Users user=(Users) context.getExternalContext().getSessionMap().get("usuario");
       
       Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       dateBalance=timestamp;
       try{
           Pago newPayment = new Pago();
            newPayment.setPagoFecha(dateBalance);
            newPayment.setReferen(ref);
            newPayment.setPoliza(poliza);
            newPayment.setMonto(mount);
            newPayment.setObservaciones(observations);
            newPayment.setStatus("En revisión");
             if(file==null){
           newPayment.setUrl_voucher(null);
            }else{
           newPayment.setUrl_voucher(url_voucher+file.getFileName());
                }
            
            newPayment.setIdUser(user);
            
           ejbPago.create(newPayment);
           System.out.println("se hace el pago");
           file=null;
           if(projectTable.size()>0){
               try{
                for (Project projs: projectTable){
           //System.out.println("Nombre del proyecto asociado al pago: "+projs.getIdProject());
           ProyCotizaFacPagoLink CotLink= new ProyCotizaFacPagoLink();
           CotLink.setIdProject(projs);
           CotLink.setPagoId(newPayment);   
           EjbCotLink.create(CotLink);
           System.out.println("se crea el link de pago/proyecto");
                   
                   }
                }catch(Exception e){
                    System.out.println(e);
                }
               
           }
           System.out.println("sigue el proceso de creacion del balancePago");
            try{
               abono=mount.doubleValue();
               saldo=0.0;
             //  saldo=saldo-charge+abono;
               double total=0.0;
               
               List<BalancePago> Saldos=ejbFacade.findBalanceByUser(user,user.getIdUser());
               
               
               BalancePago newBalance= new BalancePago();
               newBalance.setAbono(abono);
               
               newBalance.setCargo(0.0);
               
               for(BalancePago saldos: Saldos)
               {
                   total=saldos.getSaldo();
                   
               }
               System.out.println(total);
               if(total!=0){
                    newBalance.setSaldo(total+abono);
                    newBalance.setPagoId(newPayment);
                    newBalance.setFecha(dateBalance);
                    newBalance.setDescription(descriptionPayment);
                    newBalance.setCotizacion(cotizacion);
                    getFacade().create(newBalance);
                    
               }else{
             
               newBalance.setSaldo(total+abono);
               newBalance.setPagoId(newPayment);
               newBalance.setFecha(dateBalance);
               newBalance.setDescription(descriptionPayment);
               newBalance.setCotizacion(cotizacion);
               getFacade().create(newBalance);
               System.out.println("se hace el balance");
                
               } //Esta 
              
            }catch(Exception e){
               System.out.println(e);
            }
            
       }catch(Exception e){
           System.out.println(e);
       }
       
   }
   
    public void createBalanceBySelectedUser(){
       FacesContext context=FacesContext.getCurrentInstance();
       Users user=(Users) context.getExternalContext().getSessionMap().get("UserBalance");
       
       Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       dateBalance=timestamp;
      
       try{
           Pago newPayment = new Pago();
            newPayment.setPagoFecha(dateBalance);
            newPayment.setReferen(ref);
            newPayment.setPoliza(poliza);
            newPayment.setMonto(mount);
            newPayment.setStatus("En Revisión");
            if(file==null){
                    newPayment.setUrl_voucher(null);
                }else{
                 newPayment.setUrl_voucher(url_voucher+file.getFileName());
                }
           
            newPayment.setObservaciones(observations);
            newPayment.setIdUser(user);
            
           ejbPago.create(newPayment);
           System.out.println("se hace el pago");
           file=null;
           if(projectTable.size()>0){
               try{
                for (Project projs: projectTable){
           //System.out.println("Nombre del proyecto asociado al pago: "+projs.getIdProject());
           ProyCotizaFacPagoLink CotLink= new ProyCotizaFacPagoLink();
           CotLink.setIdProject(projs);
           CotLink.setPagoId(newPayment);   
           EjbCotLink.create(CotLink);
           System.out.println("se crea el link de pago/proyecto");
                   
                   }
                }catch(Exception e){
                    System.out.println(e);
                }
               
           }
           System.out.println("sigue el proceso de creacion del balancePago");
            try{
               abono=mount.doubleValue();
              // saldo=0.0;
               //saldo=saldo-charge+abono;
               double total=0.0;
               
               List<BalancePago> Saldos=ejbFacade.findBalanceByUser(user,user.getIdUser());
               
               
               BalancePago newBalance= new BalancePago();
               newBalance.setAbono(abono);
               
               newBalance.setCargo(0.0);
               
               for(BalancePago saldos: Saldos)
               {
                   total=saldos.getSaldo();
                   
               }
               if(total!=0){
                    newBalance.setSaldo(total+abono);
                    newBalance.setPagoId(newPayment);
                    newBalance.setFecha(dateBalance);
                    newBalance.setDescription(descriptionPayment);
                    newBalance.setCotizacion(cotizacion);
               System.out.println("Cotizacion: "+cotizacion);
                    getFacade().create(newBalance);
                     description="";
               }else{
             
               newBalance.setSaldo(total+abono);
               newBalance.setPagoId(newPayment);
               newBalance.setFecha(dateBalance);
               newBalance.setDescription(descriptionPayment);
               newBalance.setCotizacion(cotizacion);
               System.out.println("Cotizacion: "+cotizacion);
               getFacade().create(newBalance);
               
               System.out.println("se hace el balance");
               } //System.out.println(current.getSaldo());
            }catch(Exception e){
               System.out.println(e);
            }
       }catch(Exception e){
           System.out.println(e);
       }
   }
   public void createBalanceCharge(){
       FacesContext context=FacesContext.getCurrentInstance();
       Users user=(Users) context.getExternalContext().getSessionMap().get("usuario");
       Pago payment=null;
       Double residue=0.0;
       Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       dateBalance=timestamp;
       List<BalancePago> Saldos=ejbFacade.findBalanceByUser(user,user.getIdUser());
       if(Saldos.size()>0){
       for(BalancePago saldos: Saldos)
               {
                   payment=saldos.getPagoId();
                   residue=saldos.getSaldo();
                   
               }
       try{
            BalancePago newBalance= new BalancePago();
            newBalance.setAbono(0.0);
            newBalance.setCargo(charge);
            newBalance.setSaldo(residue-charge);
            newBalance.setDescription(description);
            newBalance.setFecha(dateBalance);
            newBalance.setPagoId(payment);
            newBalance.setCotizacion(cotCharge);
            System.out.println("usuario:" + user.getIdUser());
            newBalance.setId_user(user.getIdUser());
            getFacade().create(newBalance);
            System.out.println("se agrega el cargo");
            charge=0.0;
             description="";
       }catch(Exception e){
          System.out.println(e);
       }
       }else{
       try{
            BalancePago newBalance= new BalancePago();
            newBalance.setAbono(0.0);
            newBalance.setCargo(charge);
            newBalance.setSaldo(residue-charge);
            newBalance.setDescription(description);
            newBalance.setFecha(dateBalance);
            newBalance.setPagoId(payment);
            newBalance.setCotizacion(cotCharge);
            newBalance.setId_user(user.getIdUser());
            getFacade().create(newBalance);
            System.out.println("se agrega el cargo");
            charge=0.0;
             description="";
       }catch(Exception e){
          System.out.println(e);
       }
       }
       
   }
   
   public void createBalanceChargeBySelectedUser(){
       System.out.println("Entra al metodo para crear cargo");
       FacesContext context=FacesContext.getCurrentInstance();
       Users user=(Users) context.getExternalContext().getSessionMap().get("UserBalance");
       Pago payment=null;
       Double residue=0.0;
       Timestamp timestamp = new Timestamp(System.currentTimeMillis());
       dateBalance=timestamp;
       List<BalancePago> Saldos=ejbFacade.findBalanceByUser(user,user.getIdUser());
       if(Saldos.size()>0){
       for(BalancePago saldos: Saldos)
               {
                   payment=saldos.getPagoId();
                   residue=saldos.getSaldo();
                   
               }
        try{
            BalancePago newBalance= new BalancePago();
            newBalance.setAbono(0.0);
            newBalance.setCargo(charge);
            newBalance.setSaldo(residue-charge);
            newBalance.setDescription(description);
            newBalance.setFecha(dateBalance);
            newBalance.setPagoId(payment);
            newBalance.setCotizacion(cotCharge);
            System.out.println("id del usuario: "+user.getIdUser());
            newBalance.setId_user(user.getIdUser());
            getFacade().create(newBalance);
            System.out.println("se agrega el cargo");
            charge=0.0;
            description="";
       }catch(Exception e){
          System.out.println(e);
       }
       
       }else{
       try{
            BalancePago newBalance= new BalancePago();
            newBalance.setAbono(0.0);
            newBalance.setCargo(charge);
            newBalance.setSaldo(residue-charge);
            newBalance.setDescription(description);
            newBalance.setFecha(dateBalance);
            newBalance.setPagoId(payment);
            newBalance.setCotizacion(cotCharge);
            System.out.println("id del usuario: "+user.getIdUser());
            newBalance.setId_user(user.getIdUser());
            getFacade().create(newBalance);
            System.out.println("se agrega el cargo");
            charge=0.0;
            description="";
       }catch(Exception e){
          System.out.println(e);
       }
       }
   }
   
   public boolean checkTypeSelectedUserUNAM(){
       System.out.println("Enta al metodo para verificar el tipo de usuario");
       FacesContext context = FacesContext.getCurrentInstance();
       Users us=(Users) context.getExternalContext().getSessionMap().get("UserBalance");
       System.out.println(us.getIdDependency().getAcronym());
       return us.getIdDependency().getAcronym()==null||us.getIdDependency().getAcronym().equals("CCG") || us.getIdDependency().getAcronym().equals("IBT")||us.getIdDependency().getAcronym().equals("UNAM") ||us.getIdDependency().getInstitution().equals("UNAM") || us.getIdDependency().getInstitution().equals("Universidad Nacional Autónoma de México");
       
   }
   public boolean checkTypeUserUNAM(){
       System.out.println("Enta al metodo para verificar el tipo de usuario");
       FacesContext context = FacesContext.getCurrentInstance();
       Users us=(Users) context.getExternalContext().getSessionMap().get("usuario");
       System.out.println(us.getIdDependency().getAcronym());
       return us.getIdDependency().getAcronym().equals("CCG") || us.getIdDependency().getAcronym().equals("IBT")||us.getIdDependency().getAcronym().equals("UNAM") ||us.getIdDependency().getInstitution().equals("UNAM") || us.getIdDependency().getInstitution().equals("Universidad Nacional Autónoma de México");
       
   }
   
   public boolean checkTypeSelectedUser(){
       System.out.println("Enta al metodo para verificar el tipo de usuario");
       FacesContext context = FacesContext.getCurrentInstance();
       Users us=(Users) context.getExternalContext().getSessionMap().get("UserBalance");
       Boolean ext;
       if(us.getIdDependency()==null||us.getIdDependency().getAcronym()==null||us.getIdDependency().getAcronym().equals("CCG")|| us.getIdDependency().getAcronym().equals("IBT") || us.getIdDependency().getAcronym().equals("UNAM") || us.getIdDependency().getInstitution().equals("UNAM")){
           
           ext=false;
            
       }else
       {
           ext=true;
       }
       
      return ext;
       
   }
    public boolean checkTypeUser(){
       System.out.println("Enta al metodo para verificar el tipo de usuario");
       FacesContext context = FacesContext.getCurrentInstance();
       Users us=(Users) context.getExternalContext().getSessionMap().get("usuario");
       Boolean ext;
       if(us.getIdDependency().getAcronym().equals("CCG")|| us.getIdDependency().getAcronym().equals("IBT") || us.getIdDependency().getAcronym().equals("UNAM") || us.getIdDependency().getInstitution().equals("UNAM")){
           
           ext=false;
            
       }else
       {
           ext=true;
       }
       
      return ext;
       
   }
   public boolean paymentOrCharge(double cant){
       Boolean pc;
       if(cant>0){
           pc=false;
       }else
       {
           pc=true;
       }
       return pc;
   }
    public void updateStatusAceptedVoucher(){
        List<Pago> file=ejbFacade.findDetailsPayment(pagoId);
           for(Pago dir:file){
               dir.setStatus("Aceptado");
              ejbPago.edit(dir);
              System.out.println("Cambia de status del voucher");
           }
    }
    
    public void updateStatusRejectedVoucher(){
        List<Pago> file=ejbFacade.findDetailsPayment(pagoId);
           for(Pago dir:file){
               dir.setStatus("Rechazado");
              ejbPago.edit(dir);
              System.out.println("Cambia de status del voucher");
           }
    }
    
   public void updateVoucher(){
        List<Pago> voucher=ejbFacade.findDetailsPayment(pagoId);
           for(Pago dir:voucher){
               dir.setUrl_voucher(url_voucher+file.getFileName().replaceAll("[\\ ,]", "_"));
              ejbPago.edit(dir);
              System.out.println("Cambia url del voucher");
           }
           file=null;
    }
    
    public void DeleteVoucher(){
        List<Pago> voucher=ejbFacade.findDetailsPayment(pagoId);
           for(Pago dir:voucher){
               dir.setUrl_voucher(null);
              ejbPago.edit(dir);
              System.out.println("Cambia a null el url del voucher");
           }
    }
    public boolean checkExistVoucher(){
        List<Pago> voucher=ejbFacade.findDetailsPayment(pagoId);
        if(voucher.size()>0){
             for(Pago dir:voucher){
             if(dir.getUrl_voucher() == null){
                 return true;
             }else{
                 return false;
             }
            }
            return false;
           
        }else{
            return true;
        }
    }
    public boolean checkStatusVoucher(){
         List<Pago> voucher=ejbFacade.findDetailsPayment(pagoId);
         boolean status=false;
           for(Pago dir:voucher){
               if(dir.getStatus().equals("Aceptado")&& dir.getUrl_voucher()!=null){
                   status= true;
               }else
               {
                   status=false;
               }
           }
           return status;
    }
    
     public boolean checkStatusVoucherDelete(){
         List<Pago> voucher=ejbFacade.findDetailsPayment(pagoId);
         boolean status=false;
           for(Pago dir:voucher){
               if(dir.getStatus().equals("Aceptado")|| dir.getUrl_voucher()==null){
                   status= true;
               }else
               {
                   status=false;
               }
           }
           return status;
    }
     public void updateBalance(){
          FacesContext context = FacesContext.getCurrentInstance();
          Users user=(Users) context.getExternalContext().getSessionMap().get("UserBalance");
            Pago payment=null;
            Double residue=0.0;
             List<BalancePago> Saldos=ejbFacade.findBalanceByUser(user,user.getIdUser());
      if(!Saldos.isEmpty()&&(Saldos.size()-2)>0){
       for(BalancePago saldos: Saldos)
               {
              //payment=saldos.getPagoId();
              BalancePago LastElement = Saldos.get(Saldos.size()-2);
              System.out.println("Penultimo valor de la lista: "+LastElement.getSaldo());
                  // residue=saldos.getSaldo();
                   residue=LastElement.getSaldo();
                   
               }
      }else{
          residue=0.0;
      }
           for(BalancePago balance:BalanceTable){
                System.out.println("id_ del comentario "+balance.getIdBalance());
               if(abono !=null && abono>0){
                balance.setAbono(abono);
                balance.setCargo(0.0);
                balance.setSaldo(residue+abono);
               }
               else
               {   
                   balance.setCargo(charge);
                   balance.setSaldo(residue-charge);
               }
               if(cotizacion !=null){
                   balance.setCotizacion(cotizacion);
                   System.out.println("Edita la cotizacion");
               }
                getFacade().edit(balance);
            }
            try {
            context.getExternalContext().redirect("ListBalanceByUser.xhtml");
        } catch (IOException ex) {
           
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
      
     public void UpdateProjectsPayment(){
          try{
                for (Project projs: projectTable){
           //System.out.println("Nombre del proyecto asociado al pago: "+projs.getIdProject());
           ProyCotizaFacPagoLink CotLink= new ProyCotizaFacPagoLink();
           CotLink.setIdProject(projs);
           CotLink.setPagoId(slectedPago);   
           EjbCotLink.create(CotLink);
           System.out.println("se crea el link de pago/proyecto");
                   
                   }
                }catch(Exception e){
                    System.out.println(e);
                }
     }
    
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (BalancePago) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new BalancePago();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BalancePagoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (BalancePago) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BalancePagoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (BalancePago) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BalancePagoDeleted"));
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

    public BalancePago getBalancePago(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = BalancePago.class)
    public static class BalancePagoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BalancePagoController controller = (BalancePagoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "balancePagoController");
            return controller.getBalancePago(getKey(value));
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
            if (object instanceof BalancePago) {
                BalancePago o = (BalancePago) object;
                return getStringKey(o.getIdBalance());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + BalancePago.class.getName());
            }
        }

       
    }

}
