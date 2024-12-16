package jsf;

import jpa.entities.BioinformaticAnalysis;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.BioinformaticAnalysisFacade;

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

@Named("bioinformaticAnalysisController")
@SessionScoped
public class BioinformaticAnalysisController implements Serializable {

    private BioinformaticAnalysis current;
    private DataModel items = null;
    @EJB
    private jpa.session.BioinformaticAnalysisFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public BioinformaticAnalysisController() {
    }

    public BioinformaticAnalysis getSelected() {
        if (current == null) {
            current = new BioinformaticAnalysis();
            selectedItemIndex = -1;
        }
        return current;
    }

    private BioinformaticAnalysisFacade getFacade() {
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
        current = (BioinformaticAnalysis) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new BioinformaticAnalysis();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BioinformaticAnalysisCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (BioinformaticAnalysis) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BioinformaticAnalysisUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (BioinformaticAnalysis) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BioinformaticAnalysisDeleted"));
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
    
    public List<BioinformaticAnalysis> getBioAn() {
        FacesContext context=FacesContext.getCurrentInstance();
  Sample sam=(Sample) context.getExternalContext().getSessionMap().get("sample");
      
        return ejbFacade.findBioanSample(sam);
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
  
    public List<BioinformaticAnalysis> getBioAnCost(){
    
        List<BioinformaticAnalysis> root= ejbFacade.findAll();
        List<BioinformaticAnalysis> listCost= new ArrayList<>();
        for (BioinformaticAnalysis bio : root) {
            if (bio.getType().equals("Con costo")) {
              listCost.add(bio); 
           
            }  
        }
    
    return listCost;
    
    }
    public List<BioinformaticAnalysis> getBioAnFree(){
    
        List<BioinformaticAnalysis> root= ejbFacade.findAll();
        List<BioinformaticAnalysis> listFree= new ArrayList<>();
        for (BioinformaticAnalysis bio : root) {
            if (bio.getType().equals("Sin costo")) {
              listFree.add(bio);  
            }
        }
     
    return listFree;
    
    }

    public BioinformaticAnalysis getBioinformaticAnalysis(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

 

}
