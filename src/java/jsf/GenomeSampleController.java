package jsf;

import jpa.entities.GenomeSample;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.GenomeSampleFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import jpa.entities.Sample;
import jpa.entities.SampleDetails;

@Named("genomeSampleController")
@SessionScoped
public class GenomeSampleController implements Serializable {

    private GenomeSample current;
    private Sample selectedSample;
    private DataModel items = null;
    @EJB
    private jpa.session.GenomeSampleFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public Sample getSelectedSample() {
        FacesContext context=FacesContext.getCurrentInstance();

      context.getExternalContext().getSessionMap().put("sample", selectedSample);
        return selectedSample;
    }

    public void setSelectedSample(Sample selectedSample) {
                  FacesContext context=FacesContext.getCurrentInstance();

      context.getExternalContext().getSessionMap().put("sample", selectedSample);
        this.selectedSample = selectedSample;
    }
    
    public void asignaMuestra(Sample sam){
    
      FacesContext context=FacesContext.getCurrentInstance();

      context.getExternalContext().getSessionMap().put("sample", sam);
    
    }

    
    public GenomeSampleController() {
    }

    public GenomeSample getSelected() {
        if (current == null) {
            current = new GenomeSample();
            selectedItemIndex = -1;
        }
        return current;
    }

    private GenomeSampleFacade getFacade() {
        return ejbFacade;
    }

   

    public String prepareList() {
        recreateModel();
        return "List";
    }

   
    public String prepareCreate() {
        current = new GenomeSample();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GenomeSampleCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

  

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("GenomeSampleUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }





    
    public List<GenomeSample> getGnmSmp() {
        
   
   
    ArrayList <GenomeSample> some=new ArrayList<>();
      
  FacesContext context= FacesContext.getCurrentInstance();
      
            Sample sam=(Sample) context.getExternalContext().getSessionMap().get("sample");
        if (sam!=null) {
            System.out.println("Genomas: "+sam.toString());
         List<GenomeSample> gs=ejbFacade.findAll();
    
     for (GenomeSample gnmsmp : gs) {
         if (gnmsmp.getIdSample().equals(sam.getIdSample())) {
             some.add(gnmsmp);
         }
    }
 
    }
         return some;
}
    private void recreateModel() {
        items = null;
    }

    



}
