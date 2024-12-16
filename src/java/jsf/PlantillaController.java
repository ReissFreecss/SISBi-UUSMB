/*
Esta clase es utilizada para el control de redirrecciones y visualizacion segun el tipo de usuario
en la barra de menu del sistema. Contiene los siguientes metodos:
- Redirige a paginas segun el tipo de usuario
- Cierra la sesion del usuario actual
- Verifica si existe un usuario logeado
- Verifica el tipo de usuario conectado
- Da permisos de vista de elementos segun el tipo de usuario
 */
package jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import jpa.entities.Project;
import jpa.entities.UserRoleReport;
import jpa.entities.Users;

@Named
@SessionScoped
public class PlantillaController implements Serializable {

    @EJB
    private jpa.session.UserRoleReportFacade ejbFacadeURR;

    private boolean changeView;

    public boolean isChangeView() {
        return changeView;
    }

    public void setChangeView(boolean changeView) {
        this.changeView = changeView;
    }

    public boolean changing(boolean value) {
        return changeView = value;
    }

    public String changingView() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        changing(true);
        if ("Admin".equals(us.getUserType()) && changeView == true) {

            return "/Principal/Welcome?faces-redirect=true";

        } else {
            return "/Principal/opciones";
        }

    }

    public String changingView2() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        changing(false);
        if ("Admin".equals(us.getUserType()) && changeView == false) {
            return "/Principal/opciones?faces-redirect=true";

        } else {
            return "/Principal/Welcome?faces-redirect=true";
        }

    }

    public String redirectProyect() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if ("Admin".equals(us.getUserType())) {
            return "/Principal/opciones";
        } else {

            return "/Principal/Welcome";
        }

    }

    public String redirect() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if ("Admin".equals(us.getUserType()) && changeView == false) {
            return "/uusmb/Principal/opciones.xhtml";
        } else {

            return "/uusmb/Principal/Welcome.xhtml";
        }

    }
    
    
    public void redirectHome() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            if ("Admin".equals(us.getUserType()) && changeView == false) {
                //return "/uusmb/Principal/opciones.xhtml";
                context.getExternalContext().redirect("../Principal/opciones.xhtml");
            } else {
                context.getExternalContext().redirect("../Principal/Welcome.xhtml");
                //return "/uusmb/Principal/Welcome.xhtml";
            }
        } catch (Exception e) {
            System.out.println("Excepción " + e);
        }


    }
    
    

    public String nombreLogin() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        return us.getFirstName();

    }

    public boolean canView() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        return us.getUserType().equals("Admin");

    }

    public void verificaAdmin() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

            if (us == null) {
                context.getExternalContext().redirect("../Principal/permisos.xhtml");
            } else if (!"Admin".equals(us.getUserType())) {
                context.getExternalContext().redirect("../Principal/permisosAdmin.xhtml");
            }

        } catch (Exception e) {
            Logger.getLogger(PlantillaController.class.getName()).log(Level.SEVERE, "Error en verificaAdmin", e);
        }

    }

    public void verificarSesion() {

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            Project jkl = (Project) context.getExternalContext().getSessionMap().get("project");

            javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String uri = request.getRequestURI();

            if (us == null) {
                context.getExternalContext().redirect("../Principal/permisos.xhtml");
            }

            if ("/SISBI/uusmb/project/Create.xhtml".equals(uri)) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("project", null);
            }

        } catch (Exception e) {
            Logger.getLogger(PlantillaController.class.getName()).log(Level.SEVERE, "Error en verificarSesion", e);
        }

    }

    public void closeProjectSession() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("project", null);
        context.getExternalContext().redirect("../project/List.xhtml");

    }

    public void cerrarSesion() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../index.xhtml");
            addMessage("Sesion Cerrada", "");

        } catch (IOException ex) {
            Logger.getLogger(PlantillaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void showMessage() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "La Sesion ha sido cerrada", "");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void verificaAdminToCreateReport() throws IOException {
        try {
            verificaAdmin();
            FacesContext context = FacesContext.getCurrentInstance();
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            UserRoleReport current;
            int idUser = us.getIdUser();
            current = (UserRoleReport) ejbFacadeURR.findUserRoleReportJoinForIdUser(idUser).get(0);

            if (current.getTocreate() == false) {
                context.getExternalContext().redirect("../Principal/permisoCreateReport.xhtml");
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("../Principal/permisoCreateReport.xhtml");
        }
    }

    public void verificaAdminToReviseReport() {
        try {
            verificaAdmin();
            FacesContext context = FacesContext.getCurrentInstance();
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            UserRoleReport current;
            int idUser = us.getIdUser();
            current = (UserRoleReport) ejbFacadeURR.findUserRoleReportJoinForIdUser(idUser).get(0);

            if (current.getTorevise() == false) {
                context.getExternalContext().redirect("../Principal/permisoCreateReport.xhtml");
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void verificaAdminToAuthorizeReport() {
        try {
            verificaAdmin();
            FacesContext context = FacesContext.getCurrentInstance();
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            UserRoleReport current;
            int idUser = us.getIdUser();
            current = (UserRoleReport) ejbFacadeURR.findUserRoleReportJoinForIdUser(idUser).get(0);

            if (current.getToaauthorize() == false) {
                context.getExternalContext().redirect("../Principal/permisoCreateReport.xhtml");
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void RespaldoverificaAdminToCreateReport() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

            UserRoleReport current;
            int idUser = us.getIdUser();
            current = (UserRoleReport) ejbFacadeURR.findUserRoleReportJoinForIdUser(idUser).get(0);

            if (us == null) {
                context.getExternalContext().redirect("../Principal/permisos.xhtml");
            } else if (!"Admin".equals(us.getUserType())) {
                context.getExternalContext().redirect("../Principal/permisosAdmin.xhtml");
            } else if (current.getTocreate() == false) {
                context.getExternalContext().redirect("../Principal/permisoCreateReport.xhtml");
            }
        } catch (Exception e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error: " + e.getMessage(), "");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
    
    /* este metodo recolecta el id_ del usuario que va abrir la calculadora desde sisbi
    public String idUser() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        String id_user = Integer.toString(us.getIdUser());
        return id_user;

    }*/ 
    
    
    
    // el metodo idUser lo remplazamos por el siguiente
    
    
    public String link() {

        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        Integer id_user;
        id_user = us.getIdUser();
        String id;
        id=String.valueOf(id_user);
        char[] compara = id.toCharArray();
        String codi;
        codi = "";
        for(int i=0; i < compara.length; i++){
            if(compara[i]=='0'){
              codi=codi+'a';
            }
            
            if(compara[i]=='1'){
             codi=codi+'x';
            }

            if(compara[i]=='2'){
              codi=codi+'c';
            }

            if(compara[i]=='3'){
             codi=codi+'y';
            }

            if(compara[i]=='4'){
                codi=codi+'t';
            }

            if(compara[i]=='5'){
                codi=codi+'u';
            }

            if(compara[i]=='6'){
                codi=codi+'o';
            }

            if(compara[i]=='7'){
                codi=codi+'n';
            }

            if(compara[i]=='8'){
                codi=codi+'w';
            }

            if(compara[i]=='9'){
                codi=codi+'j';
            }

        }
        //132.248.32.95 sisbi 
        if ("Admin".equals(us.getUserType())) {
            return "http://www.uusmb.unam.mx/CalculadoraPro/calculator.php?xtd="+codi;
        } else {
            
            return "http://www.uusmb.unam.mx/Calculadora/calculator.php?xtd="+codi;
            
        }
       

    }
    
   

}
