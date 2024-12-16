package jsf;

import jpa.entities.Cotizacion;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.CotizacionFacade;

import java.io.Serializable;
import java.util.Date;
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
import jpa.entities.Project;
import jpa.entities.ProyCotizaFacPagoLink;
import jpa.entities.Users;
import jpa.session.ProyCotizaFacPagoLinkFacade;

@Named("cotizacionController")
@SessionScoped
public class CotizacionController implements Serializable {

    private Cotizacion current;
    private DataModel items = null;
    @EJB
    private jpa.session.CotizacionFacade ejbFacade;
    @EJB
    private jpa.session.ProyCotizaFacPagoLinkFacade ProyCotLink;

    public ProyCotizaFacPagoLinkFacade getProyCotLink() {
        return ProyCotLink;
    }

    public void setProyCotLink(ProyCotizaFacPagoLinkFacade ProyCotLink) {
        this.ProyCotLink = ProyCotLink;
    }
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public CotizacionController() {
    }

    public Cotizacion getSelected() {
        if (current == null) {
            current = new Cotizacion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CotizacionFacade getFacade() {
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
        current = (Cotizacion) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Cotizacion();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CotizacionCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    public void CreateCot(String quot,Double Mount, String Stat,String Contact,String Service,Date date,String typeClient,Users us,Project pj){
         
        try{
       Cotizacion newCot= new Cotizacion();
        newCot.setCotizaNum(quot);
        newCot.setCotizaMontoPesos(Mount);
        newCot.setCotizaStatus(Stat);
        //newCot.setIdUser(us);
        newCot.setTypeService(Service);
        newCot.setNameContact(Contact);
        newCot.setCotizaFecha(date);
        newCot.setTypeClient(typeClient);
       // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cotizacion", current);
        getFacade().create(newCot);
        System.out.println("Insercion en la tabla cotizacion");
        
        try{
            
            ProyCotizaFacPagoLink CotLink = new ProyCotizaFacPagoLink();
            CotLink.setCotizaId(newCot);
            CotLink.setIdProject(pj);
            //CotLink.setIdLinkPcfp(202.0);
            //
            
            ProyCotLink.create(CotLink);
            System.out.println("Insercion en la tabla ProyCotFacPag");
        }catch(Exception e){
            System.out.println(e);
        }
        
        }catch(Exception e){
            System.out.println(e);
        }
        
    }
    public List<Cotizacion>getCotizaciones(){
         FacesContext context=FacesContext.getCurrentInstance();
         Project p = (Project) context.getExternalContext().getSessionMap().get("project");
         System.out.println("proyecto: "+p);
         return ejbFacade.findCotProject(p);
        
    }

    public String prepareEdit() {
        current = (Cotizacion) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CotizacionUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Cotizacion) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CotizacionDeleted"));
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

    public Cotizacion getCotizacion(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Cotizacion.class)
    public static class CotizacionControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CotizacionController controller = (CotizacionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cotizacionController");
            return controller.getCotizacion(getKey(value));
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
            if (object instanceof Cotizacion) {
                Cotizacion o = (Cotizacion) object;
                return getStringKey(o.getCotizaId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Cotizacion.class.getName());
            }
        }

    }

}
