package jsf;

import java.io.IOException;
import jpa.entities.Users;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.UsersFacade;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
 
@Named("usersController")
@SessionScoped
public class UsersController implements Serializable {

    private Users current;
    private DataModel items = null;
    private DataModel allitems = null;
    @EJB
    private jpa.session.UsersFacade ejbFacade;
    private PaginationHelper pagination;
    private PaginationHelper paginationAll;
    private int selectedItemIndex;
    private Users selectedUser;
    private String email;
    private boolean verifiedEmail=true;
    private String varPLastName;
    
    private String txtFilUserName;
    private String txtFilFirstName;
    private String txtFilMLastName;
    private Date txtFilRegistrationName;
    private String txtFilEmail;
    private String txtUserName;
    private String txtFildDepartment;
    private String txtFieldGeneralUser;
    
    private String selectTypeUser;

    public String getSelectTypeUser() {
        return selectTypeUser;
    }

    public void setSelectTypeUser(String selectTypeUser) {
        this.selectTypeUser = selectTypeUser;
    }
    

    public String getTxtFieldGeneralUser() {
        return txtFieldGeneralUser;
    }

    public void setTxtFieldGeneralUser(String txtFieldGeneralUser) {
        this.txtFieldGeneralUser = txtFieldGeneralUser;
    }
    
    

    public String getTxtFildDepartment() {
        return txtFildDepartment;
    }

    public void setTxtFildDepartment(String txtFildDepartment) {
        this.txtFildDepartment = txtFildDepartment;
    }

    public String getTxtUserName() {
        return txtUserName = current.getUserName();
    }
    public void setTxtUserName(String txtUserName) {
        this.txtUserName = txtUserName;
    }
    public String getTxtFilEmail() {
        return txtFilEmail;
    }

    public void setTxtFilEmail(String txtFilEmail) {
        this.txtFilEmail = txtFilEmail;
    }

    public Date getTxtFilRegistrationName() {
        return txtFilRegistrationName;
    }

    public void setTxtFilRegistrationName(Date txtFilRegistrationName) {
        this.txtFilRegistrationName = txtFilRegistrationName;
    }
    
    

    public String getTxtFilUserName() {
        return txtFilUserName;
    }

    public void setTxtFilUserName(String txtFilUserName) {
        this.txtFilUserName = txtFilUserName;
    }

    public String getTxtFilFirstName() {
        return txtFilFirstName;
    }

    public void setTxtFilFirstName(String txtFilFirstName) {
        this.txtFilFirstName = txtFilFirstName;
    }

    public String getTxtFilMLastName() {
        return txtFilMLastName;
    }

    public void setTxtFilMLastName(String txtFilMLastName) {
        this.txtFilMLastName = txtFilMLastName;
    }

    
    
    

    public String getVarPLastName() {
        return varPLastName;
    }

    public void setVarPLastName(String varPLastName) {
        this.varPLastName = varPLastName;
    }

   
    

    public boolean isVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    EmailController ec = new EmailController();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Users getSelectedUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("UserProject", selectedUser);
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("UserProject", selectedUser);
        this.selectedUser = selectedUser;
    }

    public void redirectProject(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("../project/ProjectList.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }
    public void redirectBalance(){
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("../balancePago/ListBalanceByUser.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public UsersController() {
    }

    public Users getSelected() {
        if (current == null) {
            current = new Users();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UsersFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRangeUsers(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, us));
                }
            };
        }
        return pagination;
    }

    public PaginationHelper getPaginationAll() {

        if (paginationAll == null) {
            paginationAll = new PaginationHelper(150) {

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
        return paginationAll;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Users) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Users();
        selectedItemIndex = -1;
        return "AltaExitosa";
    }

    public String processUName(String UName) {
        String a = UName;
        if (UName.contains("ó")) {
            a = UName.replace("ó", "o");
        }
        if (UName.contains("í")) {
            a = UName.replace("í", "i");
        }
        if (UName.contains("á")) {
            a = UName.replace("á", "a");
        }
        if (UName.contains("é")) {
            a = UName.replace("é", "e");
        }
        if (UName.contains("ú")) {
            a = UName.replace("ú", "u");
        }
        if (UName.contains("ñ")) {
            a = UName.replace("ñ","n");
        }

        return a;
    }
    
    //Metodo encargado de verificar que el email ya esta registrado en el sistema.
        public void verifyEmail() { 
        
        if (current.getEmail() != null) {
            List<Users> usE = ejbFacade.findUserByEmail(current.getEmail().toLowerCase());
           
            if (usE.isEmpty()) { verifiedEmail = false;
                JsfUtil.addSuccessMessage("ESTIMADO USUARIO: El correo que proporcionó no esta registrado en una cuenta de este sistema, "
                        + "por favor continue con su registro");
            } else {

                for (Users em : usE) {
                    System.out.println("Entra al for");
                    if (em.getEmail().toLowerCase().equals(current.getEmail().toLowerCase())) {

                        RequestContext rc = RequestContext.getCurrentInstance();
                        rc.execute("PF('warnDialog').show();");
                        verifiedEmail = true;
                    }
                }
            }
        } else {
            JsfUtil.addSuccessMessage("ESTIMADO USUARIO: Por favor proporcione un correo electronico");
        }

    }
    public void create() {
        try {
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String[] formated = current.getPLastName().split(" ");
            String apellido = "";
            for (String string : formated) {
                apellido += string;
            }
            String uName = processUName(current.getFirstName().substring(0, 1).toUpperCase() + apellido.substring(0, 1).toUpperCase() + apellido.toLowerCase().substring(1, apellido.length()));

            List<Users> same = ejbFacade.findAll();

            boolean change = true;
            int cont = 1;

            String auxUname = uName;
            while (change) {

                for (Users users : same) {
                    if (users.getUserName().equals(auxUname)) {
                        cont++;
                        auxUname = processUName(current.getFirstName().substring(0, 1).toUpperCase()+current.getFirstName().substring(1, cont).toLowerCase() + apellido.substring(0, 1).toUpperCase() + apellido.toLowerCase().substring(1, apellido.length()));
                        
                        change = true;
                        break;
                    } else {

                        change = false;

                    }

                }
            }

            String UName= Normalizer.normalize(auxUname, Normalizer.Form.NFD);  
            String FormatedUName=UName.replaceAll("[^\\p{ASCII}]", "");
            current.setUserName(FormatedUName);
            current.setRegistrationDate(timestamp);
            current.setUserType("Usuario");
            current.setEmail(current.getEmail().toLowerCase());
            getFacade().create(current);
            Users us = current;
            JsfUtil.addSuccessMessage(current.getUserName());
            System.out.println(us.getUserName());
       ec.sendManagerNewUserEmail(us);
       ec.sendNewUserEmail(us);
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("AltaExitosa.xhtml");
            current.setEmail("");
           current.setFirstName("");
           current.setPLastName("");
           current.setMLastName("");
           current.setPhoneNumber("");
           current.setIdDependency(null);
           verifiedEmail=true;
        } catch (Exception e) {
            System.out.println(e);
            JsfUtil.addErrorMessage("ERROR AL CREAR USUARIO: El correo que proporcionó ya esta registrado en una cuenta de este sistema");
           
        }
    }
    
    public void prepareEdit() {
        current = (Users) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("Edit.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void prepareProject() {

        current = (Users) getAllItems().getRowData();

        selectedItemIndex = paginationAll.getPageFirstItem() + getAllItems().getRowIndex();
        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().getSessionMap().put("UserProject", current);

        try {
            context.getExternalContext().redirect("../project/ProjectList.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void restorePass() {
        
        Users selected = null;
        List<Users> listUsr = ejbFacade.findUserByEmail(current.getEmail().toLowerCase());
        if(email==null){
            RequestContext RC = RequestContext.getCurrentInstance();
             RC.execute("PF('warnDialog').hide();");
          for (Users users : listUsr) {
            if (users.getEmail().toLowerCase().equals(current.getEmail().toLowerCase())) {
                selected = users;
            }  
        }
          if (selected != null) {
            //FacesContext context = FacesContext.getCurrentInstance();
            String newPass = PassGenerator.getPassword(
                    PassGenerator.MINUSCULAS
                    + PassGenerator.MAYUSCULAS
                    + PassGenerator.ESPECIALES
                    + PassGenerator.NUMEROS, 15);

            selected.setPassword(newPass);
            System.out.println(newPass);
            ejbFacade.edit(selected);
            ec.sendRestoreEmail(current.getEmail().toLowerCase(), selected.getUserName(), selected.getPassword());

            try {
                JsfUtil.addSuccessMessage("Se le ha enviado un correo electronico a la direccion proporcionada con la nueva contraseña. \n"
                        + "Puede cambiar la contraseña cuando desee en la sección 'Mi Perfil'");
                RequestContext rc = RequestContext.getCurrentInstance();
             rc.execute("PF('passDialog').show();");
             //rc.execute("PF('password').show();");
            } catch (Exception ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            JsfUtil.addErrorMessage("El correo proporcionado no ha sido dado de alta en este sistema");

        }
        
        
        }else{
        for (Users users : listUsr) {
            if (users.getEmail().equals(email.toLowerCase())) {
                selected = users;
            }
        }
        if (selected != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            String newPass = PassGenerator.getPassword(
                    PassGenerator.MINUSCULAS
                    + PassGenerator.MAYUSCULAS
                    + PassGenerator.ESPECIALES
                    + PassGenerator.NUMEROS, 15);

            selected.setPassword(newPass);
            System.out.println(newPass);
            ejbFacade.edit(selected);
            ec.sendRestoreEmail(email.toLowerCase(), selected.getUserName(), selected.getPassword());

            try {
                context.getExternalContext().redirect("../Principal/Success.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            JsfUtil.addErrorMessage("El correo proporcionado no ha sido dado de alta en este sistema");

        }
    }
    }

    public String update() {
        try {
            if(0 == ejbFacade.getEmailUserByEmail(current.getEmail()).size()){
                getFacade().edit(current);
                return "ModificacionExitosa";
            }else if(ejbFacade.getEmailUserByEmail(current.getEmail()).get(0).getIdUser() == current.getIdUser()){
                getFacade().edit(current);
                return "ModificacionExitosa";
            }else{
                //FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Error al editar", "El correo electrónico ya ha sido registrado"));
                FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al editar", "El correo electrónico ya ha sido registrado"));
                return null;
            }
            
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Error al editar", "Revise los datos en el formulario"));
            return null;
        }
    }

    public String destroy() {
        current = (Users) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsersDeleted"));
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

    public DataModel getAllItems() {
        if (allitems == null) {
            allitems = getPaginationAll().createPageDataModel();
        }
        return allitems;
    }

    private List<Users> usuarios;

    public List<Users> getUsuarios() {
        return usuarios;
    }
    public List<Users> getResponsibleUsers(){
        return ejbFacade.findResponsibleUsers();
    }

    public void setUsuarios(List<Users> usuarios) {
        this.usuarios = usuarios;
    }
    
    
  
     @PostConstruct
    public void init() {
    
    usuarios=new ArrayList<>();
    usuarios=ejbFacade.findAllUsers();
    
    
    }
    
    
      public List<String> getImages(){
    List <String> images=new ArrayList<>();
    
    images.add("miniseq.png");
    images.add("miseq.png");
    images.add("nextseq500.png");
    
   return images;
    
    
    }
      
      public String getDescription(String image){
      
          String ans;
          
          switch (image) {
              case "miniseq.png":
                  ans ="Tecnologia miniseq de secuenciacion de DNA";
                  break;
              case "miseq.png":
                   ans ="Tecnologia MiSeq de secuenciacion de DNA";
                  break;
              default:
                  ans ="Tecnologia NextSeq500 de secuenciacion de DNA";
          }
      
      return ans;
      
      }

    private void recreateModel() {
        items = null;
        allitems = null;
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

    public String nextAll() {
        getPaginationAll().nextPage();
        recreateModel();
        return "ListAll";
    }

    public String previousAll() {
        getPaginationAll().previousPage();
        recreateModel();
        return "ListAll";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAllUsers(), true);
    }

    public List<String> getUsersSamples(){
        List <Users> us=ejbFacade.findAllUsers();
        List <String> lista = new ArrayList<>();
        for (Users nombre : us) {
            lista.add(nombre.getUserName());
            
        }
                
    return lista;
    
    }
    
    
    public Users getUsers(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Users.class)
    public static class UsersControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsersController controller = (UsersController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usersController");
            return controller.getUsers(getKey(value));
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
            if (object instanceof Users) {
                Users o = (Users) object;
                return getStringKey(o.getIdUser());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Users.class.getName());
            }
        }

    }
      public List<String> us(){
        List<Users> Usuarios=ejbFacade.findAllUsers();
        List<String> newList=new ArrayList();
        for(Users U: Usuarios){
            newList.add(U.getUserName());
        }
        return newList;
    }
      
      
      public void asignarValorCampos() {
          current.getEmail();
          current.getFirstName();
          current.getPLastName();
          current.getMLastName();
          current.getPhoneNumber();
          current.getPassword();
          //current.setFirstName(selectedUser.getFirstName());
          System.out.println("Se asigna valor al campo nombre "+current.getFirstName());
          System.out.println("Se asigna valor al campo nombre "+current.getPLastName());
          System.out.println("Se asigna valor al campo nombre "+current.getMLastName());
          System.out.println("Se asigna valor al campo nombre "+current.getPhoneNumber());
          System.out.println("Se asigna valor al campo nombre "+current.getPassword());
          System.out.println("Se asigna valor al campo nombre "+current.getPassword());
    }
      
    /*
      Variables para las búsquedas
      
      varPLastName
    txtFilMLastName
      txtFilUserName
      txtFilFirstName
      txtFilEmail
      txtFilRegistrationName
      
      
     */
      public void cleanInputsFilterUsers(){
          txtFilFirstName = "";
        varPLastName = "";
        txtFilMLastName = "";
        txtFilUserName = "";
        txtFilEmail = "";
        txtFildDepartment = "";
        txtFilRegistrationName = null;
        txtFieldGeneralUser = "";
        
        
        usuarios = ejbFacade.findAllUsers();
        
          System.out.println("Se limpian los filtros");
      }
      
      //Búsqueda de users por varios campos de la tabla de users
      public void userGeneralFilter() {
        varPLastName = "";
        txtFilMLastName = "";
        txtFilFirstName = "";
        txtFilEmail = "";
        txtFildDepartment = "";
        txtFilUserName = "";
        txtFilRegistrationName = null;
        if (txtFieldGeneralUser.length() > 0) {
            txtFieldGeneralUser = txtFieldGeneralUser.toLowerCase();
            usuarios = ejbFacade.getUsersGeneralFields(txtFieldGeneralUser);
        } else {
            usuarios = ejbFacade.findAllUsers();
        }
    }
      
      
      
      
         //Método buscar por nombre de usuario ignorando acentos
    public void usersFilterNameUser() {
        varPLastName = "";
        txtFilMLastName = "";
        txtFilFirstName = "";
        txtFilEmail = "";
        txtFildDepartment = "";
        txtFilRegistrationName = null;
        if (txtFilUserName.length() > 0) {
            txtFilUserName = txtFilUserName.toLowerCase();
            usuarios = ejbFacade.getUersByNameUser(txtFilUserName);
        } else {
            usuarios = ejbFacade.findAllUsers();
        }
    }

    //Método buscar por nombre de usuario ignorando acentos
    public void usersFilterFirstName() {

        varPLastName = "";
        txtFilMLastName = "";
        txtFilUserName = "";
        txtFilEmail = "";
        txtFildDepartment = "";
        txtFilRegistrationName = null;

        if (txtFilFirstName.length() > 0) {
            txtFilFirstName = txtFilFirstName.toLowerCase();
            usuarios = ejbFacade.getUersByFirstName(txtFilFirstName);
        } else {
            usuarios = ejbFacade.findAllUsers();
        }
    }

    //Método buscar por apellido paterno ignorando acentos
    public void usersFilterPLastName() {
        txtFilMLastName = "";
        txtFilUserName = "";
        txtFilFirstName = "";
        txtFilEmail = "";
        txtFildDepartment = "";
        txtFilRegistrationName = null;
        if (varPLastName.length() > 0) {
            varPLastName = varPLastName.toLowerCase();
            usuarios = ejbFacade.getUersByPLastName(varPLastName);
        } else {
            usuarios = ejbFacade.findAllUsers();
        }
    }

    //Método buscar por apellido paterno ignorando acentos
    public void usersFilterMLastName() {

        varPLastName = "";
        txtFilUserName = "";
        txtFilFirstName = "";
        txtFilEmail = "";
        txtFildDepartment = "";
        txtFilRegistrationName = null;
        if (txtFilMLastName.length() > 0) {
            txtFilMLastName = txtFilMLastName.toLowerCase();
            usuarios = ejbFacade.getUersByMLastName(txtFilMLastName);
        } else {
            usuarios = ejbFacade.findAllUsers();
        }
    }

    //Método buscar por email ignorando acentos
    public void usersFilterEmail() {
        varPLastName = "";
        txtFilMLastName = "";
        txtFilUserName = "";
        txtFilFirstName = "";
        txtFildDepartment = "";
        txtFilRegistrationName = null;
        if (txtFilEmail.length() > 0) {
            txtFilEmail = txtFilEmail.toLowerCase();
            usuarios = ejbFacade.getUersByEmail(txtFilEmail);
        } else {
            usuarios = ejbFacade.findAllUsers();
        }
    }
    
    //Método buscar por email ignorando acentos
    public void usersFilterDepartment() {
        varPLastName = "";
        txtFilMLastName = "";
        txtFilUserName = "";
        txtFilFirstName = "";
        txtFilRegistrationName = null;
        txtFilEmail = "";
        if (txtFildDepartment.length() > 0) {
            txtFildDepartment = txtFildDepartment.toLowerCase();
            usuarios = ejbFacade.getUersByDepartment(txtFildDepartment);
        } else {
            usuarios = ejbFacade.findAllUsers();
        }
    }

    //Método buscar por fecha
    public void cleanFilterRegistrationDate() {
        System.out.println("Se resetea y se actualiza tabla");
        txtFilRegistrationName = null;
        usuarios = ejbFacade.findAllUsers();
    }

    public void usersFilterRegistrationDate() {
        varPLastName = "";
        txtFilMLastName = "";
        txtFilUserName = "";
        txtFilFirstName = "";
        txtFilEmail = "";
        txtFildDepartment = "";
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            String date = simpleDateFormat.format(txtFilRegistrationName);
            System.out.println("date: " + date);
            usuarios = ejbFacade.getUersByRegistrationDate(date);
        } catch (Exception e) {
            System.out.println("Fecha no reconocida, entonces se le asigna un null");
            txtFilRegistrationName = null;
            usuarios = ejbFacade.findAllUsers();
        }
    }
    
    public void editTypeUser(Users itemUser, String selection){
        if (selection.equals("Usuario") || selection.equals("Admin")) {
            Users user = itemUser;
            user.setUserType(selection);
            ejbFacade.edit(user);
            selectTypeUser = "";

            
            showMessage("info", "Permiso cambiado","El permiso fue cambiado satisfactoriamente");

        }
        
    }
    
    public void showMessage(String type, String title, String detail) {
        if(type == "info"){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, title, detail);
            FacesContext.getCurrentInstance().addMessage("", message);
        }
        if(type == "warn"){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, title, detail);
            FacesContext.getCurrentInstance().addMessage("", message);
        }
        if(type == "error"){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, title, detail);
            FacesContext.getCurrentInstance().addMessage("", message);
        }
        if(type == "fatal"){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, title, detail);
            FacesContext.getCurrentInstance().addMessage("", message);
        }
    }
}
