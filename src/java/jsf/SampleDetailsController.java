package jsf;

import jpa.entities.SampleDetails;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.SampleDetailsFacade;

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

@Named("sampleDetailsController")
@SessionScoped
public class SampleDetailsController implements Serializable {

    private SampleDetails current;
    private DataModel items = null;
    @EJB
    private jpa.session.SampleDetailsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private SampleDetails selectedSD;

    public SampleDetails getSelectedSD(Sample query) {
          List<SampleDetails> sd=ejbFacade.findSampleDetails();
        
        ArrayList <SampleDetails> some=new ArrayList<>();
          for (SampleDetails sampleDetails : sd) {
            if (sampleDetails.getIdSample().equals(query.getIdSample())) {
                some.add(sampleDetails);
            }
            
        }
        selectedSD=sd.get(0);
        
        
        return selectedSD;
    }

    public void setSelectedSD(SampleDetails selectedSD) {
        this.selectedSD = selectedSD;
    }
    

    public SampleDetailsController() {
    }

    public SampleDetails getSelected() {
        if (current == null) {
            current = new SampleDetails();
            selectedItemIndex = -1;
        }
        return current;
    }

    private SampleDetailsFacade getFacade() {
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

    public SampleDetails findSelectedProject(String query) {
        List<SampleDetails> sd=ejbFacade.findAll();
        ArrayList <SampleDetails> some=null;
        for (SampleDetails sampleDetails : sd) {
            if (sampleDetails.getSampleName().equals(query)) {
                some.add(sampleDetails);
            }
            
        }
        selectedSD=sd.get(0);
        
        return selectedSD;
    }
    
    public List<SampleDetails> findSD(Sample smp){
    
    List<SampleDetails> sd=ejbFacade.findAll();
    ArrayList <SampleDetails> some=new ArrayList<>();
     for (SampleDetails sampleDetails : sd) {
            if ((sampleDetails.getSampleName().equals(smp.getSampleName()))) {
                some.add(sampleDetails);
                break;
            }
  
    }
      return some;
    
    }
    public List<SampleDetails> findCompleteSD(Sample smp){
    
    List<SampleDetails> sd=ejbFacade.findSampleDetails();
   
    ArrayList <SampleDetails> some=new ArrayList<>();
     for (SampleDetails sampleDetails : sd) {
     
         
            if ((sampleDetails.getSampleName().equals(smp.getSampleName()))) {
                some.add(sampleDetails);
             
            }
  
    }
     
      return some;
    
    }
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (SampleDetails) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new SampleDetails();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SampleDetailsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (SampleDetails) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SampleDetailsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (SampleDetails) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("SampleDetailsDeleted"));
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

    public SampleDetails getSampleDetails(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = SampleDetails.class)
    public static class SampleDetailsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SampleDetailsController controller = (SampleDetailsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sampleDetailsController");
            return controller.getSampleDetails(getKey(value));
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
            if (object instanceof SampleDetails) {
                SampleDetails o = (SampleDetails) object;
                return getStringKey(o.getIdSample());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + SampleDetails.class.getName());
            }
        }

    }

}
