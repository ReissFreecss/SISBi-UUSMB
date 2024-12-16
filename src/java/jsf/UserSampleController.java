package jsf;

import java.io.IOException;
import jpa.entities.UserProjectLink;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.UserProjectLinkFacade;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import jpa.entities.Comments;

import jpa.entities.Project;
import jpa.entities.UserSample;
import jpa.entities.Users;
import jpa.session.CommentsFacade;
import jpa.session.UserSampleFacade;
import org.primefaces.context.RequestContext;

@Named("userSampleController")
@SessionScoped
public class UserSampleController implements Serializable {
    long oneDay = 24 * 60 * 60 * 1000;
    Date today = new Date();
    Date maxDate = new Date(today.getTime());
    Date minDate = new Date((43*(366 * oneDay))); 
    String user="";
    Date fechaIncio=minDate;
    Date fechaTermino=maxDate;
    String estatus="";
    Integer ID=null;
    String nomMuestra="";
    String IdTubo="";
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

    public SimpleDateFormat getFormato() {
        return formato;
    }

    public void setFormato(SimpleDateFormat formato) {
        this.formato = formato;
    }

    public String getNomMuestra() {
         if (nomMuestra==null) {
            nomMuestra = "";
                    
        }
        return nomMuestra;
    }

    public void setNomMuestra(String nomMuestra) {
        this.nomMuestra = nomMuestra;
    }

    public String getIdTubo() {
        if (IdTubo==null) {
            IdTubo = "";
                    
        }
        return IdTubo;
    }

    public void setIdTubo(String IdTubo) {
        this.IdTubo = IdTubo;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
    

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }
   

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }
    

    public Date getFechaIncio() {
         if (fechaIncio==null) {
           fechaIncio=new Date(System.currentTimeMillis());
                    
        }
        /*String cad="";
        String cadFechaInicio[]=formato.format(fechaIncio.toString()).split("-");
        for(int i=0; i<cadFechaInicio.length; i++){
            cad=cad+cadFechaInicio[i];
        }*/
      System.out.println("El formato de fecha es: "+  "\'"+formato.format(fechaIncio)+"\'");
        return fechaIncio;
    }

    public void setFechaIncio(Date fechaIncio) {
        this.fechaIncio = fechaIncio;
    }

    public Date getFechaTermino() {
         if (fechaTermino==null) {
           fechaTermino=new Date(System.currentTimeMillis());
                    
        }
        return fechaTermino;
    }

    public void setFechaTermino(Date fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getEstatus() {
          if (estatus==null) {
            estatus = "";
                    
        }
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    

    public String getUser() {
          if (user==null) {
            user = "";
                    
        }
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
      private UserSampleFacade getFacade() {
        return UserSampFac;
    }
     
    
    @EJB
    private UserSampleFacade UserSampFac;
    
    
    public List<UserSample> getItems(){
      
      
    return getFacade().findUserSampByName(user,fechaIncio,fechaTermino,estatus,ID,nomMuestra,IdTubo);
    
    }
    
    public void reset(){
   /* estatus = "---";
    user = "---";
    ID=0;
    maxDate = new Date(today.getTime());
    minDate = new Date((43*(366 * oneDay)));
    fechaIncio=minDate;
    fechaTermino=maxDate;*/
     FacesContext context = FacesContext.getCurrentInstance();
        try { 
            context.getExternalContext().redirect("../sample/AllSamples.xhtml");
            System.out.println("Se realizo la redireccion");

        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ERROR: "+ex.getMessage());
        } finally {
            estatus = "";
            user = "";
            ID=null;
            IdTubo="";
            nomMuestra="";
            maxDate = new Date(today.getTime());
            minDate = new Date((43*(366 * oneDay)));
            fechaIncio=minDate;
            fechaTermino=maxDate;  
        }
   
   
    }
           
    
   

}
