/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import jpa.entities.Files;
import jpa.entities.Project;
import jpa.entities.Users;
import jpa.session.FilesFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author aaron
 */
@Named("filesProController")
@SessionScoped
public class FilesProjectController implements Serializable{
    public static final String PATH = PathFiles.PathFilesProject;
    public static final String URL = "http://www.uusmb.unam.mx/projectFiles";
    
     @EJB
    private FilesFacade facade;
    private UploadedFile file;
    private String urlFile;
    private Files upladed;
    private Files selected;
    private String comment;
    
    
    //variable para la visualización de archivos en web
    String fileProjectPath = PathFiles.LinkFilesProject;

    public String getFileProjectPath() {
        return fileProjectPath;
    }

    public void setFileProjectPath(String fileProjectPath) {
        this.fileProjectPath = fileProjectPath;
    }
    
    



    public String getComment() {

        return comment;

    }



    public void setComment(String comment) {

        this.comment = comment;

    }

    public Files getSelected() {
        if (selected!=null) {
           System.out.println(selected.getPath());  
        }
       
        return selected;
    }

    public void setSelected(Files selected) {
        this.selected = selected;
    }
    
    

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public Files getUpladed() {
        if (upladed==null) {
            upladed=new Files();
        }
        return upladed;
    }

    public void setUpladed(Files upladed) {
        this.upladed = upladed;
    }
    

    public UploadedFile getFile() {
      
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    
  
    
    
    public void create(){
        try{
         FacesContext context = FacesContext.getCurrentInstance();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
     Project pj=(Project) context.getExternalContext().getSessionMap().get("project");
     Users us=(Users) context.getExternalContext().getSessionMap().get("usuario");  
     
        if (upladed.getFileType()==null) {
            upladed.setFileType("Otro");
        }
       
        upladed.setIdProject(pj);
        upladed.setUploader(us.getUserName());
        upladed.setPath(urlFile);
        upladed.setFileName(file.getFileName());
        upladed.setFileDate(timestamp);
        
        facade.create(upladed);
        
        upladed.setComment("");
        }
        catch(Exception e ){
        FacesMessage message = new FacesMessage("INFO", "No se ha cargado ningun archivo" + "\n"+"Vuelva a intentarlo por favor");
        FacesContext.getCurrentInstance().addMessage(null, message);
                }
        
   
    }

	public void CreateQuotFile(){

      FacesContext context = FacesContext.getCurrentInstance();
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      Project pj=(Project) context.getExternalContext().getSessionMap().get("project");
      Users us=(Users) context.getExternalContext().getSessionMap().get("usuario");

      try{

      upladed.setFileType("Cotizacion");
      upladed.setIdProject(pj);
      upladed.setUploader(us.getUserName());
      upladed.setPath(urlFile);
      upladed.setFileName(file.getFileName());
      upladed.setFileDate(timestamp);
      upladed.setComment(comment);
      facade.create(upladed);
      context.getExternalContext().redirect("ViewProject.xhtml");
      }catch(Exception e){
          System.out.println(e);
      }
 }



 public void createVoucherFile(){

      FacesContext context = FacesContext.getCurrentInstance();
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      Project pj=(Project) context.getExternalContext().getSessionMap().get("project");
      Users us=(Users) context.getExternalContext().getSessionMap().get("usuario");

      try{

      upladed.setFileType("Pago");
      upladed.setIdProject(pj);
      upladed.setUploader(us.getUserName());
      upladed.setPath(urlFile);
      upladed.setFileName(file.getFileName());
      upladed.setFileDate(timestamp);
      upladed.setComment(comment);
      facade.create(upladed);

      context.getExternalContext().redirect("ViewProject.xhtml");

      }catch(Exception e){
          System.out.println(e);
      }
 }

    public List <Files> getItems (){
         FacesContext context = FacesContext.getCurrentInstance();
        Project   pj=  (Project) context.getExternalContext().getSessionMap().get("project");
        /*
        List <Files> list=facade.findAll();
        List <Files> filteredList= new ArrayList<>();
        for (Files files : list) {
            if (files.getIdProject().equals(pj)) {
                filteredList.add(files);
            }
        }
        return filteredList;
        */
        return facade.allFilesByProject(pj);
    }
    /*
    Este metodo elimina un archivo asociado a un proyecto
    
    @author: Fernando Betanzos
    
    @param: int id, recibe el id del archivo que se quiere eliminar
    
    @Return:
    
    @version:28/01/20
    */
    public void deleteFile(int id){
        
        RequestContext Context = RequestContext.getCurrentInstance();
        List <Files> list=facade.findAll();
        try{
             for (Files files : list) {
            
         
            if (files.getIdFile().equals(id)) {
                
                System.out.println(files.getFileName());
                facade.remove(files);
                System.out.println("Borra el archivo: "+files.getFileName());
                            }
        }
        Context.execute("PF('dlg3').show();");
        }catch(Exception e){
            Context.execute("PF('dlg4').show();");
            System.out.println("Error Log: " +e);
            
        }
        
    }
    
     public void uploadFile(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        System.out.println(us.getUserName());
        file = event.getFile();
        int num1 = (int)(Math.random()*10+1);
        int num2 = (int)(Math.random()*10+1);
        
        String nameFile =  num1+""+num2 +file.getFileName();

        String url = PATH + pj.getIdProject().replace(":", "_");

        File folder = new File(url);
        if (!folder.exists()) {
            folder.mkdir();
        }
        try {
            java.nio.file.Files.deleteIfExists(new File(url, nameFile).toPath());
            java.nio.file.Files.copy(file.getInputstream(), new File(url, nameFile).toPath());
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //xochi
        //urlFile = URL+ "/" + pj.getIdProject()+"/"+ file.getFileName();
        //windows mantenimiento: windows no acepta ":" o caracteres especiales en sus rutas
        urlFile = pj.getIdProject().replace(":", "_") + "/" + nameFile;

        System.out.println(event.getFile().getFileName());
        FacesMessage message = new FacesMessage("Comprobante cargado satisfactoriamente", nameFile + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);

    }
     
      public void handleFileUpload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
      
    public String verCotizacion() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        if(facade.filesByProject(pj, "Cotizacion").isEmpty()){
            return null;
        }
        Files lastFile = facade.filesByProject(pj, "Cotizacion").get(0);
        return getLinkCompleteFile(lastFile.getPath());
    }
    public String verComprobante() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        if(facade.filesByProject(pj, "Pago").isEmpty()){
            return null;
        }
        Files lastFile = facade.filesByProject(pj, "Pago").get(0);
        return getLinkCompleteFile(lastFile.getPath());
    }
      
      public void setFileInvoice(int option){
      
       FacesContext context = FacesContext.getCurrentInstance();
       Project pj=  (Project) context.getExternalContext().getSessionMap().get("project");
       
       List <Files> list=facade.findAll();
        List <Files> filteredList= new ArrayList<>();
   
        for (Files files : list) {
            
         
            if (files.getIdProject().equals(pj)) {
                switch (option) {
                    case 1:
                         if (files.getFileType().equals("Cotizacion")) 
                             filteredList.add(files);  
                
                        break;
                    case 2:
                         if (files.getFileType().equals("Pago")) 
                             filteredList.add(files); 
                        
                        break;
                    default:
                        throw new AssertionError();
                }
               
              
            }
        }
          System.out.println(filteredList.size());
          if (filteredList.size()>=1) {
            setSelected(filteredList.get(filteredList.size()-1));  
          }
          
      
      
      
      }
      
      public boolean checkFile(int option){
      
       FacesContext context = FacesContext.getCurrentInstance();
       Project pj=  (Project) context.getExternalContext().getSessionMap().get("project");
       
       List <Files> list=facade.findAll();
        List <Files> filteredList= new ArrayList<>();
   
        for (Files files : list) {
            
         
            if (files.getIdProject().equals(pj)) {
                switch (option) {
                    case 1:
                         if (files.getFileType().equals("Cotizacion")) 
                             filteredList.add(files);  
                
                        break;
                    case 2:
                         if (files.getFileType().equals("Pago")) 
                             filteredList.add(files); 
                        
                        break;
                    default:
                        throw new AssertionError();
                }
               
              
            }
        }
          System.out.println(filteredList.size());
        return filteredList.size()<1;
          
      
      
      
      }
      
      //metodo par devolver liga del archivo
      public String getLinkCompleteFile(String pathFileBD){
         if(pathFileBD.charAt(0) == 'h'){
             return pathFileBD;
         }else{
             return fileProjectPath + pathFileBD;
         }
     }
      

}
