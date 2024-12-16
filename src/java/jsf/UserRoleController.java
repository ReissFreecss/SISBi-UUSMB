package jsf;

import jpa.entities.UserRole;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.UserRoleFacade;

import java.io.Serializable;
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
import javax.faces.model.SelectItem;
import jpa.entities.Project;
import jpa.entities.UserProjectLink;
import jpa.entities.Users;
import jpa.session.UserProjectLinkFacade;

@Named("userRoleController")
@SessionScoped
public class UserRoleController implements Serializable {

    private UserRole current;
    private DataModel items = null;
    @EJB
    private jpa.session.UserRoleFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @EJB
    private UserRoleFacade roleFac;

    @EJB
    private jpa.session.UserProjectLinkFacade ejbFacadeUsersProjectLink;

    public UserProjectLinkFacade getEjbFacadeUsersProjectLink() {
        return ejbFacadeUsersProjectLink;
    }

    public void setEjbFacadeUsersProjectLink(UserProjectLinkFacade ejbFacadeUsersProjectLink) {
        this.ejbFacadeUsersProjectLink = ejbFacadeUsersProjectLink;
    }

    private List<UserRole> listUsersProject;

    public List<UserRole> getListUsersProject() {
        return listUsersProject;
    }

    public void setListUsersProject(List<UserRole> listUsersProject) {
        this.listUsersProject = listUsersProject;
    }

    public UserRoleController() {
    }

    public UserRole getSelected() {
        if (current == null) {
            current = new UserRole();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UserRoleFacade getFacade() {
        return ejbFacade;
    }


    public List<UserRole> getItems() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        List<UserRole> list = getFacade().findRangeUsersProj(proj.getIdProject());
        List<UserRole> list2 = getFacade().findAll();
        for (UserRole us : list2) {
            if (us.getIdProject().equals(proj.getIdProject())) {
                //System.out.println(us.getUserName() + " " + us.getRole());
            }
        }
        for (UserRole userRole : list) {
            //System.out.println(userRole.getRole());
            //System.out.println("UserName: " + userRole.getUserName());
        }

        return getFacade().findRangeUsersProj(proj.getIdProject());
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareCreate() {
        current = new UserRole();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserRoleCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserRoleUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {

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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserRoleDeleted"));
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

    public List<UserRole> mylistUsersProject() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        List<UserRole> list = getFacade().findRangeUsersProj(proj.getIdProject());
        List<UserProjectLink> list2 = getFacade().findUserProjectLinkByIdProyecto(proj.getIdProject());

        int myIndex = 0;
        for (UserProjectLink userProjetc : list2) {
            UserRole nuevo = list.get(myIndex);
            Users user = getFacade().findUsersByIdUser(userProjetc.getUserProjectLinkPK().getIdUser());
            //Dependency dependencia = getFacade().findDependencyByIdDependency(user.getIdDependency().getIdDependency());

            nuevo.setUserName(user.getUserName());
            nuevo.setFirstName(user.getFirstName());
            nuevo.setPLastName(user.getPLastName());
            nuevo.setMLastName(user.getMLastName());
            nuevo.setEmail(user.getEmail());
            nuevo.setPhoneNumber(user.getPhoneNumber());
            //nuevo.setDependencyName();
            nuevo.setRole(userProjetc.getRole());
            //System.out.println(user.getEmail() + "\tRole: " + userProjetc.getRole());
            list.set(myIndex++, nuevo);
        }

        listUsersProject = list;
        return listUsersProject;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public UserRole getUserRole(java.lang.String id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = UserRole.class)
    public static class UserRoleControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserRoleController controller = (UserRoleController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userRoleController");
            return controller.getUserRole(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof UserRole) {
                UserRole o = (UserRole) object;
                return getStringKey(o.getUserName());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UserRole.class.getName());
            }
        }

    }

}
