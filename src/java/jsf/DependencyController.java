package jsf;

import java.io.IOException;
import jpa.entities.Dependency;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.DependencyFacade;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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
import jpa.entities.Users;
import org.primefaces.context.RequestContext;
//import org.primefaces.context.RequestContext;

@Named("dependencyController")
@SessionScoped
public class DependencyController implements Serializable {

    private Dependency current;
    private DataModel items = null;
    @EJB
    private jpa.session.DependencyFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public DependencyController() {
    }

    public Dependency getSelected() {
        if (current == null) {
            current = new Dependency();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DependencyFacade getFacade() {
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
        current = (Dependency) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Dependency();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create(ActionEvent event) {
        try {

            getFacade().create(current);

            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    // Metodo para crear dependencia
    public void CreateDependency() {
        try {
            System.out.println("Entrando al método createDependency...");

            // Obtener el usuario actual de la sesión
            FacesContext context = FacesContext.getCurrentInstance();
            Users usuarioActual = (Users) context.getExternalContext().getSessionMap().get("usuario");

            // Se verifica que no haya ningun dato null excepto las siglas de la institucion
            if (getSelected().getInstitution() == null
                    || getSelected().getInstitution().isEmpty()
                    || getSelected().getDependencyName().isEmpty()
                    || getSelected().getLocation().isEmpty()
                    || getSelected().getStreet().isEmpty()
                    || getSelected().getPostalCode().isEmpty()) {
                JsfUtil.addErrorMessage("Hay un campo obligatorio sin llenar");
                return;
            }

            // Obtener valores originales antes de la normalización
            String originalInstitution = getSelected().getInstitution();
            String originalDependencyName = getSelected().getDependencyName();
            String location = getSelected().getLocation();

            // Verificar si la dependencia ya existe con el nombre, ubicación y dependencia
            List<Dependency> existingDependencies = ejbFacade.findInstitutionByNameAndDependencyName(
                    originalInstitution,
                    originalDependencyName
            );

            if (!existingDependencies.isEmpty()) {
                JsfUtil.addErrorMessage("La institución ya está registrada con el mismo nombre de dependencia.");
                RequestContext.getCurrentInstance().execute("PF('repeatDialog').show();");
            } else {
                // Normalizar el texto de la institución y el nombre de la dependencia
                String normalizedInstitution = normalizeText(originalInstitution);
                String normalizedDependencyName = normalizeText(originalDependencyName);

                // Actualizar los valores normalizados en el objeto seleccionado
                getSelected().setInstitution(normalizedInstitution);
                getSelected().setDependencyName(normalizedDependencyName);

                // Crear la nueva dependencia
                getFacade().create(getSelected());
                System.out.println("Institución registrada con éxito.");
                JsfUtil.addSuccessMessage("La dependencia se ha registrado exitosamente.");
                RequestContext.getCurrentInstance().execute("PF('CreateDialog').show();");
            }
        } catch (Exception e) {
            System.err.println("Error al intentar registrar la dependencia: " + e.getMessage());
            JsfUtil.addErrorMessage("Ocurrió un error al intentar registrar la dependencia. Por favor, inténtelo nuevamente.");
        }
    }

    public String prepareEdit() {
        current = (Dependency) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String redirectDependency() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (us != null) {
            return "/users/Edit.xhtml";
        } else {

            return "/users/Create.xhtml";

        }
    }

    //Seguir editando nuevo proyecto
    public String redirectCreateProject() {

        FacesContext context = FacesContext.getCurrentInstance();

        return "/project/Create.xhtml";

    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DependencyUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Dependency) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("DependencyDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public List<String> getInstitutions() {

        List<Dependency> inst = getFacade().findAllDependencies(2);
        ArrayList<String> filtered = new ArrayList<>();

        for (int i = 0; i < inst.size() - 2; i++) {
            if (!inst.get(i).getInstitution().equals(inst.get(i + 1).getInstitution())) {
                filtered.add(inst.get(i).getInstitution() + " - " + inst.get(i).getAcronym());
            }
        }
        filtered.add(inst.get(inst.size() - 1).getInstitution() + "-" + inst.get(inst.size() - 1).getAcronym());
        return filtered;

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
        return JsfUtil.getSelectItems(ejbFacade.findAllDependencies(1), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAllDependencies(1), true);
    }

    public Dependency getDependency(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Dependency.class)
    public static class DependencyControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DependencyController controller = (DependencyController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "dependencyController");
            return controller.getDependency(getKey(value));
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
            if (object instanceof Dependency) {
                Dependency o = (Dependency) object;
                return getStringKey(o.getIdDependency());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Dependency.class.getName());
            }
        }

    }

    //Método para obtener las dependencias 7
    public List<Dependency> getAllDependency() {
        List<Dependency> dependencies = ejbFacade.getAllDependency();
        return dependencies;
    }
    
    //Metodo para normalizar texto de dependencia:
    private String normalizeText(String text) {
        if (text == null) {
            return null;
        }
        text = text.trim(); // Eliminar espacios en blanco al inicio y al final
        text = text.toUpperCase(); // Convertir a mayúsculas
        text = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", ""); // Eliminar acentos y caracteres especiales
        return text;
    }

}
