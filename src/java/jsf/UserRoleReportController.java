package jsf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jpa.entities.UserRoleReport;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.UserRoleReportFacade;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import jpa.entities.Users;
import jpa.session.UsersFacade;
import org.apache.poi.ss.util.ImageUtils;
//import org.apache.poi.ss.util.ImageUtils;
import org.apache.poi.util.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("userRoleReportController")
@SessionScoped
public class UserRoleReportController implements Serializable {

    private UserRoleReport current;
    private DataModel items = null;
    @EJB
    private jpa.session.UserRoleReportFacade ejbFacadeURR;
    @EJB
    private jpa.session.UsersFacade ejbFacadeUsers;
    private List<Users> ItemsNombresUsers;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private String DirectoryImageSing = PathFiles.DirectoryImageSing;
    private String LinkDirectoryImageSing = PathFiles.LinkDirectoryImageSing;

    private int itemIdSeleccionUsers;

    public String getLinkDirectoryImageSing() {
        return LinkDirectoryImageSing;
    }

    public void setLinkDirectoryImageSing(String LinkDirectoryImageSing) {
        this.LinkDirectoryImageSing = LinkDirectoryImageSing;
    }

    
    
    

    public int getItemIdSeleccionUsers() {
        return itemIdSeleccionUsers;
    }

    public void setItemIdSeleccionUsers(int itemIdSeleccionUsers) {
        this.itemIdSeleccionUsers = itemIdSeleccionUsers;
    }
    
    UploadedFile filep;

    public UploadedFile getFilep() {
        return filep;
    }

    public void setFilep(UploadedFile filep) {
        this.filep = filep;
    }
    
    private Users usersClass;

    public Users getUsersClass() {
        return usersClass;
    }

    public void setUsersClass(Users usersClass) {
        this.usersClass = usersClass;
    }
    
    
    public UsersFacade getEjbFacadeUsers() {
        return ejbFacadeUsers;
    }

    public void setEjbFacadeUsers(UsersFacade ejbFacadeUsers) {
        this.ejbFacadeUsers = ejbFacadeUsers;
    }
    
    
    
    

    public List<Users> getItemsNombresUsers() {
        return ItemsNombresUsers;
    }

    public void setItemsNombresUsers(List<Users> ItemsNombresUsers) {
        this.ItemsNombresUsers = ItemsNombresUsers;
    }
    
    //Lista join
    private List<UserRoleReport> userRoleReportXUsers;

    public List<UserRoleReport> getUserRoleReportXUsers() {
        return userRoleReportXUsers;
    }

    public void setUserRoleReportXUsers(List<UserRoleReport> userRoleReportXUsers) {
        this.userRoleReportXUsers = userRoleReportXUsers;
    }
    

    public UserRoleReportController() {
    }

    public UserRoleReport getSelected() {
        if (current == null) {
            current = new UserRoleReport();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UserRoleReportFacade getFacade() {
        return ejbFacadeURR;
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
    
    //Lista de join criteria
    @PostConstruct
    public void init() {
        userRoleReportXUsers = new ArrayList<>();
        userRoleReportXUsers = ejbFacadeURR.userRoleReportJoin();  

        ItemsNombresUsers = new ArrayList<>();
        ItemsNombresUsers = ejbFacadeUsers.optionItemsSelectUsers();
        
    }
    //Redireccion a la pagina roleReport
    public String redirectUserRoleReport(){
        return "List?faces-redirect=true&includeViewParams=true";
    }
    //Metodo para el checkbox
    public String prepareList() {
        recreateModel();
        init();
        return "List";
    }

    public String prepareView() {
        current = (UserRoleReport) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new UserRoleReport();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String addUserRole(){
        String mensaje;
        try {
            usersClass = getEjbFacadeUsers().find(itemIdSeleccionUsers);
            current = new UserRoleReport();
            current.setIdUser(usersClass);
            current.setTocreate(Boolean.FALSE);
            current.setTorevise(Boolean.FALSE);
            current.setToaauthorize(Boolean.FALSE);
            getFacade().create(current);
            mensaje = "Se agregó el nuevo usuario";
            FacesMessage msj = new FacesMessage(mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } catch (Exception e) {
            mensaje = "Error al guardar";
            FacesMessage msj = new FacesMessage(mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msj);
        }
        return prepareList();
    }

    public String create() {
        try {
            current.setTocreate(Boolean.FALSE);
            current.setTorevise(Boolean.FALSE);
            current.setToaauthorize(Boolean.FALSE);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserRoleReportCreated"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
        
        return prepareList();
    }
    public String prepareEdit() {
        current = (UserRoleReport) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserRoleReportUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public void updateCheck(UserRoleReport role) {
        try {
            //role = new UserRoleReport();
            current.setIdUser(role.getIdUser());
            current.setIdUserRoleReport(role.getIdUserRoleReport());
            current.setTocreate(role.getTocreate());
            current.setTorevise(role.getTorevise());
            current.setToaauthorize(role.getToaauthorize());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserRoleReportUpdated"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    //Metodos globales 
    public void showMessage(String mensaje) {
        mensaje = "El permiso fué cambiado satisfactoriamente";
        FacesMessage message = new FacesMessage(mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public String validateBtnVerFirma(int idUser){
        
        FacesContext context = FacesContext.getCurrentInstance();
        Users user = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (ejbFacadeURR.findUserRoleReportById(user.getIdUser()).size() > 0) {
            UserRoleReport userRole = ejbFacadeURR.findUserRoleReportById(user.getIdUser()).get(0);
            if (userRole.getIdUser().getIdUser() == idUser) {
                return "false";
            } else {
                return "true";
            }
        }else{
            return "true";
        }

    }
    
    public void updateCheckCreate(int id, Boolean role) {
        String mensaje = "";
        try {
            current = getFacade().find(id);
            current.setTocreate(role);
            getFacade().edit(current);
            
            mensaje = "El permiso fué cambiado satisfactoriamente";
            showMessage(mensaje);

        } catch (Exception e) {
            mensaje = e.getMessage();
            showMessage(mensaje);
        }
    }
    
    public void updateCheckRevise(int id, Boolean role) {
        String mensaje = "";
        try {
            current = getFacade().find(id);
            current.setTorevise(role);
            getFacade().edit(current);
            
            mensaje = "El permiso fué cambiado satisfactoriamente";
            showMessage(mensaje);

        } catch (Exception e) {
            mensaje = e.getMessage();
            showMessage(mensaje);
        }
    }
    
    public void updateCheckAuthorize(int id, Boolean role) {
        String mensaje = "";
        try {
            current = getFacade().find(id);
            current.setToaauthorize(role);
            getFacade().edit(current);
            
            mensaje = "El permiso fué cambiado satisfactoriamente";
            showMessage(mensaje);

        } catch (Exception e) {
            mensaje = e.getMessage();
            showMessage(mensaje);
        }
    }
    
    public String destroyUserRoleReport(int id){
        try {
            current = getFacade().find(id);
            selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
            if(current.getPathimage()!=null){
                FacesContext context = FacesContext.getCurrentInstance();
                ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
                File file = new File(DirectoryImageSing + current.getPathimage());
                file.delete();
                current.setPathimage(null);
                getFacade().edit(current);
            }
            performDestroy();
            recreatePagination();
            recreateModel();
        } catch (Exception e) {
            System.out.println("Error al eliminar firma del usuario: "+e.getMessage());
        }
        if (selectedItemIndex >= 0) {
            return prepareList();
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }
    //metodo para cargar imagen
    public String redirectViewSing(int idUserRole){
        current = ejbFacadeURR.find(idUserRole);
        return "formImage?faces-redirect=true&includeViewParams=true";
    }
    
    public String upload() {
    if(filep != null) {
        try {
                FacesContext context = FacesContext.getCurrentInstance();
                ServletContext scontext = (ServletContext)context.getExternalContext().getContext();
                String rootpath = scontext.getRealPath("../../web/resources/imgSign");
                
                File fileImage=new File(rootpath+"/firma.jpg");
                InputStream inputStream=filep.getInputstream();
                SaveImage(inputStream,fileImage);
                return "formImage?faces-redirect=true&includeViewParams=true";
            }
        catch(IOException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage("There was a probleme your file was not uploaded.",e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
            return "formImage?faces-redirect=true&includeViewParams=true";
        }
    }
    return "formImage?faces-redirect=true&includeViewParams=true";
}

public void SaveImage(InputStream inputStream, File ImageFile) throws IOException {
    OutputStream outputStream=new FileOutputStream(ImageFile);
    IOUtils.copy(inputStream, outputStream);
}
//validar boton de subir imagen
public String validateBtnUpload(){
    if(current.getPathimage() == null || current.getPathimage().equals("")){
        return "false";
    }else{
        return "true";
    }
}

public String uploadFile(FileUploadEvent event) {
        String nameFile;
        filep = event.getFile();
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
        //String rootpath = scontext.getRealPath("../../web/resources/imgSign/");
        String rootpath = DirectoryImageSing;
        
        try {

            Random r = new Random();
            int nameIden = r.nextInt(50) + 1;
            nameFile = current.getIdUserRoleReport()+""+current.getIdUser().getFirstName()+nameIden+".jpg";
            nameFile = Normalizer.normalize(nameFile, Normalizer.Form.NFD);
            nameFile = nameFile.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
            System.out.println("mostramos la ruta o nombre::  " + filep.getFileName());
            java.nio.file.Files.deleteIfExists(new File(rootpath, filep.getFileName()).toPath());
            java.nio.file.Files.copy(filep.getInputstream(), new File(rootpath, nameFile).toPath());
            current.setPathimage(nameFile);
            getFacade().edit(current);
            return "formImage?faces-redirect=true&includeViewParams=true";
        } catch (IOException ex) {
            ex.printStackTrace();
            FacesMessage message = new FacesMessage("There was a probleme your file was not uploaded.",ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        System.out.println(event.getFile().getFileName());
        FacesMessage message = new FacesMessage("Comprobante cargado satisfactoriamente", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "formImage?faces-redirect=true&includeViewParams=true";
    }

public String deleteImageSign(){
    FacesContext context = FacesContext.getCurrentInstance();
    ServletContext scontext = (ServletContext) context.getExternalContext().getContext();
    File file = new File( DirectoryImageSing + current.getPathimage());
    file.delete();
    current.setPathimage(null);
    getFacade().edit(current);
    return "formImage?faces-redirect=true&includeViewParams=true";
}

    public String destroy() {
        current = (UserRoleReport) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        //return "List";
        if (selectedItemIndex >= 0) {
            return prepareList();
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
        //return prepareList();
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
        String mensaje="";
        try {
            getFacade().remove(current);
            
            mensaje = "Usuario eliminado ";
            FacesMessage msj = new FacesMessage(mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msj);
        } catch (Exception e) {
            mensaje = "Error al eliminar ::: " + e.getMessage();
            FacesMessage msj = new FacesMessage(mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msj);
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
        return JsfUtil.getSelectItems(ejbFacadeURR.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacadeURR.findAll(), true);
    }

    public UserRoleReport getUserRoleReport(java.lang.Integer id) {
        return ejbFacadeURR.find(id);
    }

    @FacesConverter(forClass = UserRoleReport.class)
    public static class UserRoleReportControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserRoleReportController controller = (UserRoleReportController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userRoleReportController");
            return controller.getUserRoleReport(getKey(value));
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
            if (object instanceof UserRoleReport) {
                UserRoleReport o = (UserRoleReport) object;
                return getStringKey(o.getIdUserRoleReport());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UserRoleReport.class.getName());
            }
        }

    }

}