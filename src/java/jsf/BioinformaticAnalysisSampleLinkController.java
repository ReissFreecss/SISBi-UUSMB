package jsf;

import jpa.entities.BioinformaticAnalysisSampleLink;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.BioinformaticAnalysisSampleLinkFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.Sample;
import jpa.session.BioinformaticAnalysisFacade;

@Named("bioinformaticAnalysisSampleLinkController")
@SessionScoped
public class BioinformaticAnalysisSampleLinkController implements Serializable {

    private BioinformaticAnalysisSampleLink current;
    
    @EJB
    private jpa.session.BioinformaticAnalysisSampleLinkFacade ejbFacade;
    @EJB
    private BioinformaticAnalysisFacade bioFac;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<String> analysisCost;
    private List<String> analysisFree;
    
    public List<String> getAnalysisCost() {
        return analysisCost;
    }

    public void setAnalysisCost(List<String> analysisCost) {
        this.analysisCost = analysisCost;
    }

    public List<String> getAnalysisFree() {
    
        return analysisFree;
    }

    public void setAnalysisFree(List<String> analysisFree) {
        this.analysisFree = analysisFree;
    }
    
    public BioinformaticAnalysisSampleLinkController() {
    }

    public BioinformaticAnalysisSampleLink getSelected() {
        if (current == null) {
            current = new BioinformaticAnalysisSampleLink();
            current.setBioinformaticAnalysisSampleLinkPK(new jpa.entities.BioinformaticAnalysisSampleLinkPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    private BioinformaticAnalysisSampleLinkFacade getFacade() {
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
        
        return "List";
    }

    public String prepareCreate() {
        current = new BioinformaticAnalysisSampleLink();
        current.setBioinformaticAnalysisSampleLinkPK(new jpa.entities.BioinformaticAnalysisSampleLinkPK());
        selectedItemIndex = -1;
        return "Create";
    }
    
    public void createBioanSamLink(){
         try {
             
              FacesContext context = FacesContext.getCurrentInstance();
              Sample sm=(Sample) context.getExternalContext().getSessionMap().get("sample");

             
            List <BioinformaticAnalysis> root= bioFac.findAll();
            List <BioinformaticAnalysis> listB=new ArrayList<>();
            for (String an :analysisCost) {
                for (BioinformaticAnalysis bioin : root) {
                    if (an.equals(bioin.getAnalysisName())) {
                        listB.add(bioin);
                    }
                }
            }
            for (String an :analysisFree) {
                for (BioinformaticAnalysis bioin : root) {
                    if (an.equals(bioin.getAnalysisName())) {
                        listB.add(bioin);
                    }
                }
            }
           
            
            
            for (BioinformaticAnalysis bioan : listB) {
          current = new BioinformaticAnalysisSampleLink();
          current.setBioinformaticAnalysisSampleLinkPK(new jpa.entities.BioinformaticAnalysisSampleLinkPK());
          current.getBioinformaticAnalysisSampleLinkPK().setIdSample(sm.getIdSample());
          current.getBioinformaticAnalysisSampleLinkPK().setIdAnalysis(bioan.getIdAnalysis());
          
         getFacade().create(current);
    
            
          
            }
            
            System.out.println("salio del for bioan");
        } catch (Exception e) {
            System.out.println("--------> "+e);
           
        }
        
        
}

    public void create(Sample sm) {
       
        
        try {
            List <BioinformaticAnalysis> root= bioFac.findAll();
            List <BioinformaticAnalysis> listB=new ArrayList<>();
            for (String an :analysisCost) {
                for (BioinformaticAnalysis bioin : root) {
                    if (an.equals(bioin.getAnalysisName())) {
                        listB.add(bioin);
                    }
                }
            }
            for (String an :analysisFree) {
                for (BioinformaticAnalysis bioin : root) {
                    if (an.equals(bioin.getAnalysisName())) {
                        listB.add(bioin);
                    }
                }
            }
           
            
            
            for (BioinformaticAnalysis bioan : listB) {
          current = new BioinformaticAnalysisSampleLink();
          current.setBioinformaticAnalysisSampleLinkPK(new jpa.entities.BioinformaticAnalysisSampleLinkPK());
          current.getBioinformaticAnalysisSampleLinkPK().setIdSample(sm.getIdSample());
          current.getBioinformaticAnalysisSampleLinkPK().setIdAnalysis(bioan.getIdAnalysis());
          
         getFacade().create(current);
         JsfUtil.addSuccessMessage("Creado el link de analisis");
            
          
            }
            
            System.out.println("salio del for bioan");
        } catch (Exception e) {
            System.out.println("--------> "+e);
           
        }
    }

   

    public String update() {
        try {
            current.getBioinformaticAnalysisSampleLinkPK().setIdAnalysis(current.getBioinformaticAnalysis().getIdAnalysis());
            current.getBioinformaticAnalysisSampleLinkPK().setIdSample(current.getSample().getIdSample());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BioinformaticAnalysisSampleLinkUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

 

 
  
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public BioinformaticAnalysisSampleLink getBioinformaticAnalysisSampleLink(jpa.entities.BioinformaticAnalysisSampleLinkPK id) {
        return ejbFacade.find(id);
    }
    
    
    //Limpiar valores del formulario
    public void cleanFormSample(){
        analysisCost = null; 
    }
  

}
