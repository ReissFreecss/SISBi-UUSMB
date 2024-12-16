/*

En esta clase se definen los colaboradores de los proyectos, estos colaboradores deben ser usuarios registrados en el sistema.
La clase se componen de diferentes metodos:
-Creacion de la relacion Usuario-Proyecto y rol en el mismo.
-Controlar que el numero de colaboradores asociados a la vez no exceda los 5 usuarios.
-Controlar que por siempre haya exactamente un responsable.
-Verificar que los correos ingresados esten asociados a un usuario en la base de datos.
-Crear el colaborador en caso de que aún no haya sido dado de alta en el sistema.
-Crear el Id del proyecto a partir del usuario responsable.
-Termina de registrar el proyecto en la base de datos.

 */
package jsf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jpa.entities.UserProjectLink;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.UserProjectLinkFacade;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Locale;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import jpa.entities.Comments;
import jpa.entities.Cotizacion;

import jpa.entities.Project;
import jpa.entities.ProyCotizaFacPagoLink;
import jpa.entities.Users;
import jpa.session.CommentsFacade;
import jpa.session.CotizacionFacade;
import jpa.session.ProjectFacade;
import jpa.session.ProyCotizaFacPagoLinkFacade;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.json.JSONObject;

@Named("userProjectLinkController")
@SessionScoped
public class UserProjectLinkController implements Serializable {

    private UserProjectLink current;
    private UserProjectLink col1 = null;
    private UserProjectLink col2 = null;
    private UserProjectLink col3 = null;
    private UserProjectLink col4 = null;
    private UserProjectLink col5 = null;
    public static final String NOT_MATCH = "Uno o mas correos ingresados no se encuentran registrados en este sistema, verifique su información o registre el usuario.";
    public static final String MANAGER_NOTFOUND = "Por favor asigne al responsable para el proyecto (Max. 1)";
    private String existTxt = "";
    private String ManagerTxt = "";
    private EmailController ec = new EmailController();
    private int numberCol = 0;
    private boolean check = true;
    private boolean checkCol1 = false;
    private boolean checkCol2 = false;
    private boolean checkCol3 = false;
    private boolean checkCol4 = false;
    private boolean checkCol5 = false;
    private boolean checkMan = false;
    private boolean checkMan1 = false;
    private boolean checkMan2 = false;
    private boolean checkMan3 = false;
    private boolean checkMan4 = false;
    private boolean checkMan5 = false;
    private boolean checkAdd = false;
    private boolean checkSubs = true;
    private boolean checkUImage1 = false;
    private boolean checkCImage1 = false;
    private boolean checkUImage2 = false;
    private boolean checkCImage2 = false;
    private boolean checkTerms;
    private String invoice;
    private Project selectedProject;

    public boolean isCheckTerms() {
        return checkTerms;
    }

    public void setCheckTerms(boolean checkTerms) {
        this.checkTerms = checkTerms;
    }

    public Project getSelectedProject() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", selectedProject);
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("project", selectedProject);
        this.selectedProject = selectedProject;
    }

    @EJB
    private jpa.session.CotizacionFacade Ejbcot;
    @EJB
    private jpa.session.ProyCotizaFacPagoLinkFacade EjbCotLink;
    @EJB
    private jpa.session.ProjectFacade ejbFacadeProject;

    public ProjectFacade getEjbFacadeProject() {
        return ejbFacadeProject;
    }

    public void setEjbFacadeProject(ProjectFacade ejbFacadeProject) {
        this.ejbFacadeProject = ejbFacadeProject;
    }

    public CotizacionFacade getEjbcot() {
        return Ejbcot;
    }

    public void setEjbcot(CotizacionFacade Ejbcot) {
        this.Ejbcot = Ejbcot;
    }

    public ProyCotizaFacPagoLinkFacade getEjbCotLink() {
        return EjbCotLink;
    }

    public void setEjbCotLink(ProyCotizaFacPagoLinkFacade EjbCotLink) {
        this.EjbCotLink = EjbCotLink;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public boolean isCheckCot2() {
        return checkCot2;
    }

    public void setCheckCot2(boolean checkCot2) {
        this.checkCot2 = checkCot2;
    }
    private boolean checkUImage3 = false;
    private boolean checkCImage3 = false;
    private boolean checkUImage4 = false;
    private boolean checkCImage4 = false;
    private boolean checkUImage5 = false;
    private boolean checkCImage5 = false;
    private boolean checkCot = false;
    private boolean checkCot2 = false;

    public boolean isCheckCot() {
        return checkCot;
    }

    public void setCheckCot(boolean checkCot) {
        this.checkCot = checkCot;
    }
    private DataModel items = null;
    private String email1;
    private String email2;
    private String email3;
    private String email4;
    private String email5;
    private int times;
    private int numberOfManagers;
    private Users currentUser1;
    private Users currentUser2;
    private Users currentUser3;
    private Users currentUser4;
    private Users currentUser5;
    private String comment = "";

    @EJB
    private jpa.session.UserProjectLinkFacade ejbFacade;
    @EJB
    private jpa.session.UsersFacade ejbUsr;
    @EJB
    private jpa.session.ProjectFacade ejbPj;
    @EJB
    private CommentsFacade commentFac;

    private PaginationHelper pagination;
    private int selectedItemIndex;
    private boolean existCheck = false;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Users getSelectedUser1() {
        if (currentUser1 == null) {
            currentUser1 = new Users();
            selectedItemIndex = -1;
        }
        return currentUser1;
    }

    public Users getSelectedUser2() {
        if (currentUser2 == null) {
            currentUser2 = new Users();
            selectedItemIndex = -1;
        }
        return currentUser2;
    }

    public Users getSelectedUser3() {
        if (currentUser3 == null) {
            currentUser3 = new Users();
            selectedItemIndex = -1;
        }
        return currentUser3;
    }

    public Users getSelectedUser4() {
        if (currentUser4 == null) {
            currentUser4 = new Users();
            selectedItemIndex = -1;
        }
        return currentUser4;
    }

    public Users getSelectedUser5() {
        if (currentUser5 == null) {
            currentUser5 = new Users();
            selectedItemIndex = -1;
        }
        return currentUser5;
    }

    public boolean isCheckMan() {
        return checkMan;
    }

    public void setCheckMan(boolean checkMan) {
        this.checkMan = checkMan;
    }

    public boolean isCheckMan1() {
        return checkMan1;
    }

    public void setCheckMan1(boolean checkMan1) {
        this.checkMan1 = checkMan1;
    }

    public boolean isCheckMan2() {
        return checkMan2;
    }

    public void setCheckMan2(boolean checkMan2) {
        this.checkMan2 = checkMan2;
    }

    public boolean isCheckMan3() {
        return checkMan3;
    }

    public void setCheckMan3(boolean checkMan3) {
        this.checkMan3 = checkMan3;
    }

    public boolean isCheckMan4() {
        return checkMan4;
    }

    public void setCheckMan4(boolean checkMan4) {
        this.checkMan4 = checkMan4;
    }

    public boolean isCheckMan5() {
        return checkMan5;
    }

    public void setCheckMan5(boolean checkMan5) {
        this.checkMan5 = checkMan5;
    }

    public String getManagerTxt() {
        return ManagerTxt;
    }

    public void setManagerTxt(String ManagerTxt) {
        this.ManagerTxt = ManagerTxt;
    }

    public boolean isCheckUImage2() {
        return checkUImage2;
    }

    public void setCheckUImage2(boolean checkUImage2) {
        this.checkUImage2 = checkUImage2;
    }

    public boolean isCheckCImage2() {
        return checkCImage2;
    }

    public void setCheckCImage2(boolean checkCImage2) {
        this.checkCImage2 = checkCImage2;
    }

    public boolean isCheckUImage3() {
        return checkUImage3;
    }

    public void setCheckUImage3(boolean checkUImage3) {
        this.checkUImage3 = checkUImage3;
    }

    public boolean isCheckCImage3() {
        return checkCImage3;
    }

    public void setCheckCImage3(boolean checkCImage3) {
        this.checkCImage3 = checkCImage3;
    }

    public boolean isCheckUImage4() {
        return checkUImage4;
    }

    public void setCheckUImage4(boolean checkUImage4) {
        this.checkUImage4 = checkUImage4;
    }

    public boolean isCheckCImage4() {
        return checkCImage4;
    }

    public void setCheckCImage4(boolean checkCImage4) {
        this.checkCImage4 = checkCImage4;
    }

    public boolean isCheckUImage5() {
        return checkUImage5;
    }

    public void setCheckUImage5(boolean checkUImage5) {
        this.checkUImage5 = checkUImage5;
    }

    public boolean isCheckCImage5() {
        return checkCImage5;
    }

    public void setCheckCImage5(boolean checkCImage5) {
        this.checkCImage5 = checkCImage5;
    }

    public boolean isCheckUImage1() {
        return checkUImage1;
    }

    public void setCheckUImage1(boolean checkUImage1) {
        this.checkUImage1 = checkUImage1;
    }

    public boolean isCheckCImage1() {
        return checkCImage1;
    }

    public void setCheckCImage1(boolean checkCImage1) {
        this.checkCImage1 = checkCImage1;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheckAdd() {
        return checkAdd;
    }

    public void setCheckAdd(boolean checkAdd) {
        this.checkAdd = checkAdd;
    }

    public boolean isCheckSubs() {
        return checkSubs;
    }

    public void setCheckSubs(boolean checkSubs) {
        this.checkSubs = checkSubs;
    }

    public boolean isCheckCol1() {
        return checkCol1;
    }

    public void setCheckCol1(boolean checkCol1) {
        this.checkCol1 = checkCol1;
    }

    public boolean isCheckCol2() {
        return checkCol2;
    }

    public void setCheckCol2(boolean checkCol2) {
        this.checkCol2 = checkCol2;
    }

    public boolean isCheckCol4() {
        return checkCol4;
    }

    public void setCheckCol4(boolean checkCol4) {
        this.checkCol4 = checkCol4;
    }

    public boolean isCheckCol5() {
        return checkCol5;
    }

    public void setCheckCol5(boolean checkCol5) {
        this.checkCol5 = checkCol5;
    }

    public boolean isCheckCol3() {
        return checkCol3;
    }

    public void setCheckCol3(boolean checkCol3) {
        this.checkCol3 = checkCol3;
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

        return a;
    }

    public void createUser1() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String[] formated = currentUser1.getPLastName().split(" ");
            String apellido = "";
            for (String string : formated) {
                apellido += string;
            }

            String uName = processUName(currentUser1.getFirstName().substring(0, 1).toUpperCase() + apellido.substring(0, 1).toUpperCase() + apellido.toLowerCase().substring(1, apellido.length()));

            List<Users> same = ejbUsr.findAll();

            boolean change = true;
            int cont = 1;

            String auxUname = uName;
            while (change) {

                for (Users users : same) {
                    if (users.getUserName().equals(auxUname)) {
                        cont++;
                        auxUname = processUName(currentUser1.getFirstName().substring(0, 1).toUpperCase() + currentUser1.getFirstName().substring(1, cont).toLowerCase()
                                + apellido.substring(0, 1).toUpperCase() + apellido.toLowerCase().substring(1, apellido.length()));

                        change = true;
                        break;
                    } else {

                        change = false;

                    }

                }
            }

            String UName = Normalizer.normalize(auxUname, Normalizer.Form.NFD);
            String FormatedUName = UName.replaceAll("[^\\p{ASCII}]", "");
            currentUser1.setUserName(FormatedUName);
            currentUser1.setRegistrationDate(timestamp);
            currentUser1.setUserType("Usuario");
            currentUser1.setPassword(apellido.substring(0, 1).toUpperCase() + apellido.substring(1, apellido.length()) + "123@");
            currentUser1.setEmail(email1.toLowerCase());
            ejbUsr.create(currentUser1);
            RequestContext RC = RequestContext.getCurrentInstance();
            RC.execute("PF('InfoDialog').show();");
            JsfUtil.addSuccessMessage(currentUser1.getUserName());

            ec.sendNewUser4ProjEmail(currentUser1);

            currentUser1 = null;
            currentUser1 = new Users();

            /* if (pj != null) {
               FacesContext.getCurrentInstance().getExternalContext().redirect("Edit.xhtml"); 
            }else{
                
               FacesContext.getCurrentInstance().getExternalContext().redirect("Create.xhtml"); 
            
            }*/
            checkUImage1 = false;

        } catch (Exception e) {
            System.out.println(e);
            JsfUtil.addErrorMessage("ERROR AL CREAR USUARIO: El correo que proporcionó ya esta registrado en una cuenta de este sistema");

        }
    }

    public void createUser2() {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String[] formated = currentUser2.getPLastName().split(" ");
            String apellido = "";
            for (String string : formated) {
                apellido += string;
            }

            String uName = processUName(currentUser2.getFirstName().substring(0, 1).toUpperCase() + apellido.substring(0, 1).toUpperCase() + apellido.toLowerCase().substring(1, apellido.length()));

            List<Users> same = ejbUsr.findAll();

            boolean change = true;
            int cont = 1;

            String auxUname = uName;
            while (change) {

                for (Users users : same) {

                    if (users.getUserName().equals(auxUname)) {
                        cont++;
                        auxUname = processUName(currentUser2.getFirstName().substring(0, 1).toUpperCase() + currentUser2.getFirstName().substring(1, cont).toLowerCase()
                                + apellido.substring(0, 1).toUpperCase() + apellido.toLowerCase().substring(1, apellido.length()));

                        change = true;
                        break;
                    } else {

                        change = false;

                    }

                }
            }

            String UName = Normalizer.normalize(auxUname, Normalizer.Form.NFD);
            String FormatedUName = UName.replaceAll("[^\\p{ASCII}]", "");
            currentUser2.setUserName(FormatedUName);

            currentUser2.setRegistrationDate(timestamp);
            currentUser2.setUserType("Usuario");
            currentUser2.setPassword(apellido.substring(0, 1).toUpperCase() + apellido.substring(1, apellido.length()) + "123@");
            currentUser2.setEmail(email2.toLowerCase());
            ejbUsr.create(currentUser2);
            RequestContext RC = RequestContext.getCurrentInstance();
            RC.execute("PF('InfoDialog').show();");
            JsfUtil.addSuccessMessage(currentUser2.getUserName());

            ec.sendNewUser4ProjEmail(currentUser2);

            currentUser2 = null;
            currentUser2 = new Users();

            //FacesContext.getCurrentInstance().getExternalContext().redirect("Create.xhtml");
        } catch (Exception e) {
            System.out.println(e);
            JsfUtil.addErrorMessage("ERROR AL CREAR USUARIO: El correo que proporcionó ya esta registrado en una cuenta de este sistema");

        }
    }

    public void createUser3() {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String[] formated = currentUser3.getPLastName().split(" ");
            String apellido = "";
            for (String string : formated) {
                apellido += string;
            }

            String uName = processUName(currentUser3.getFirstName().substring(0, 1).toUpperCase() + apellido.substring(0, 1).toUpperCase()
                    + apellido.toLowerCase().substring(1, apellido.length()));

            List<Users> same = ejbUsr.findAll();

            boolean change = true;
            int cont = 1;

            String auxUname = uName;
            while (change) {

                for (Users users : same) {

                    if (users.getUserName().equals(auxUname)) {
                        cont++;
                        auxUname = processUName(currentUser3.getFirstName().substring(0, 1).toUpperCase() + currentUser3.getFirstName().substring(1, cont).toLowerCase()
                                + apellido.substring(0, 1).toUpperCase() + apellido.toLowerCase().substring(1, apellido.length()));

                        change = true;
                        break;
                    } else {

                        change = false;

                    }

                }
            }

            String UName = Normalizer.normalize(auxUname, Normalizer.Form.NFD);
            String FormatedUName = UName.replaceAll("[^\\p{ASCII}]", "");
            currentUser3.setUserName(FormatedUName);
            currentUser3.setRegistrationDate(timestamp);
            currentUser3.setUserType("Usuario");
            currentUser3.setPassword(apellido.substring(0, 1).toUpperCase() + apellido.substring(1, apellido.length()) + "123@");
            currentUser3.setEmail(email3.toLowerCase());
            ejbUsr.create(currentUser3);
            RequestContext RC = RequestContext.getCurrentInstance();
            RC.execute("PF('InfoDialog').show();");
            JsfUtil.addSuccessMessage(currentUser3.getUserName());

            ec.sendNewUser4ProjEmail(currentUser3);

            currentUser3 = null;
            currentUser3 = new Users();

            //FacesContext.getCurrentInstance().getExternalContext().redirect("Create.xhtml");
        } catch (Exception e) {
            System.out.println(e);
            JsfUtil.addErrorMessage("ERROR AL CREAR USUARIO: El correo que proporcionó ya esta registrado en una cuenta de este sistema");

        }
    }

    public void createUser4() {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String[] formated = currentUser4.getPLastName().split(" ");
            String apellido = "";
            for (String string : formated) {
                apellido += string;
            }
            System.out.println(apellido);

            String uName = processUName(currentUser4.getFirstName().substring(0, 1).toUpperCase() + apellido.substring(0, 1).toUpperCase()
                    + apellido.toLowerCase().substring(1, apellido.length()));

            List<Users> same = ejbUsr.findAll();

            boolean change = true;
            int cont = 1;

            String auxUname = uName;
            while (change) {

                for (Users users : same) {

                    if (users.getUserName().equals(auxUname)) {
                        cont++;
                        auxUname = processUName(currentUser4.getFirstName().substring(0, 1).toUpperCase() + currentUser4.getFirstName().substring(1, cont).toLowerCase()
                                + apellido.substring(0, 1).toUpperCase() + apellido.toLowerCase().substring(1, apellido.length()));

                        change = true;
                        break;
                    } else {
                        change = false;
                    }

                }
            }

            String UName = Normalizer.normalize(auxUname, Normalizer.Form.NFD);
            String FormatedUName = UName.replaceAll("[^\\p{ASCII}]", "");
            currentUser4.setUserName(FormatedUName);
            currentUser4.setRegistrationDate(timestamp);
            currentUser4.setUserType("Usuario");
            currentUser4.setPassword(apellido.substring(0, 1).toUpperCase() + apellido.substring(1, apellido.length()) + "123@");
            currentUser4.setEmail(email4.toLowerCase());
            ejbUsr.create(currentUser4);
            RequestContext RC = RequestContext.getCurrentInstance();
            RC.execute("PF('InfoDialog').show();");
            JsfUtil.addSuccessMessage(currentUser4.getUserName());

            ec.sendNewUser4ProjEmail(currentUser4);

            currentUser4 = null;
            currentUser4 = new Users();

            //FacesContext.getCurrentInstance().getExternalContext().redirect("Create.xhtml");
        } catch (Exception e) {
            System.out.println(e);
            JsfUtil.addErrorMessage("ERROR AL CREAR USUARIO: El correo que proporcionó ya esta registrado en una cuenta de este sistema");

        }
    }

    public void createUser5() {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String[] formated = currentUser5.getPLastName().split(" ");
            String apellido = "";
            for (String string : formated) {
                apellido += string;
            }

            String uName = processUName(currentUser5.getFirstName().substring(0, 1).toUpperCase() + apellido.substring(0, 1).toUpperCase()
                    + apellido.toLowerCase().substring(1, apellido.length()));

            List<Users> same = ejbUsr.findAll();

            boolean change = true;
            int cont = 1;

            String auxUname = uName;
            while (change) {

                for (Users users : same) {

                    if (users.getUserName().equals(auxUname)) {
                        cont++;
                        auxUname = processUName(currentUser5.getFirstName().substring(0, 1).toUpperCase() + currentUser5.getFirstName().substring(1, cont).toLowerCase()
                                + apellido.substring(0, 1).toUpperCase() + apellido.toLowerCase().substring(1, apellido.length()));

                        change = true;
                        break;
                    } else {

                        change = false;

                    }
                }
            }

            String UName = Normalizer.normalize(auxUname, Normalizer.Form.NFD);
            String FormatedUName = UName.replaceAll("[^\\p{ASCII}]", "");
            currentUser5.setUserName(FormatedUName);
            currentUser5.setRegistrationDate(timestamp);
            currentUser5.setUserType("Usuario");
            currentUser5.setPassword(apellido.substring(0, 1).toUpperCase() + apellido.substring(1, apellido.length()) + "123@");
            currentUser5.setEmail(email5.toLowerCase());
            ejbUsr.create(currentUser5);
            RequestContext RC = RequestContext.getCurrentInstance();
            RC.execute("PF('InfoDialog').show();");

            JsfUtil.addSuccessMessage(currentUser5.getUserName());
            ec.sendNewUser4ProjEmail(currentUser5);

            currentUser5 = null;
            currentUser5 = new Users();

            //FacesContext.getCurrentInstance().getExternalContext().redirect("Create.xhtml");
        } catch (Exception e) {
            System.out.println(e);
            JsfUtil.addErrorMessage("ERROR AL CREAR USUARIO: El correo que proporcionó ya esta registrado en una cuenta de este sistema");

        }
    }

    public void addNumber(ActionEvent e) {
        numberCol++;
        if (numberCol >= 5) {
            checkAdd = true;
        } else {
            checkSubs = false;
        }

    }

    public void subsNumber(ActionEvent e) {
        numberCol--;
        if (numberCol <= 0) {
            checkSubs = true;
        } else {
            checkAdd = false;
        }

    }

    int managers = 0;
    List<Boolean> ltsCheck = java.util.Arrays.asList(false, false, false, false, false, false);

    public void existManager() {
        if ((current.getRole().equals("Responsable"))) {
            checkMan = true;
            managers += 1;
        } else {
            checkMan = false;
        }
        ltsCheck.set(0, checkMan);
        checkManager();
    }

    public void existManager1() {
        if ((col1.getRole().equals("Responsable"))) {
            checkMan1 = true;
            managers += 1;
        } else {
            checkMan1 = false;
        }
        ltsCheck.set(1, checkMan1);
        checkManager();
    }

    public void existManager2() {
        if ((col2.getRole().equals("Responsable"))) {
            checkMan2 = true;
            managers += 1;
        } else {
            checkMan2 = false;
        }
        ltsCheck.set(2, checkMan2);
        checkManager();
    }

    public void existManager3() {
        if ((col3.getRole().equals("Responsable"))) {
            checkMan3 = true;
            managers += 1;
        } else {
            checkMan3 = false;
        }
        ltsCheck.set(3, checkMan3);
        checkManager();
    }

    public void existManager4() {
        if ((col4.getRole().equals("Responsable"))) {
            checkMan4 = true;
            managers += 1;
        } else {
            checkMan4 = false;
        }
        ltsCheck.set(4, checkMan4);
        checkManager();
    }

    public void existManager5() {
        if ((col5.getRole().equals("Responsable"))) {
            checkMan5 = true;
            managers += 1;
        } else {
            checkMan5 = false;
        }
        ltsCheck.set(5, checkMan5);
        checkManager();
    }

    public void existManagerB1() {
        if ((col1.getRole().equals("Responsable"))) {
            checkMan1 = true;
        } else {
            checkMan1 = false;

        }
        checkManager();
    }

    public void existManagerB2() {
        if ((col2.getRole().equals("Responsable"))) {
            checkMan2 = true;
        } else {
            checkMan2 = false;

        }
        checkManager();
    }

    public void existManagerB3() {
        if ((col3.getRole().equals("Responsable"))) {
            checkMan3 = true;

        } else {
            checkMan3 = false;

        }
        checkManager();
    }

    public void existManagerB4() {
        if ((col4.getRole().equals("Responsable"))) {
            checkMan4 = true;
        } else {
            checkMan4 = false;

        }
        checkManager();
    }

    public void existManagerB5() {
        if ((col5.getRole().equals("Responsable"))) {
            checkMan5 = true;
        } else {
            checkMan5 = false;

        }
        checkManager();

    }

    public void checkManager() {
        managers = 0;
        for (Boolean myCheck : ltsCheck) {
            if (myCheck) {
                managers += 1;
            }
        }

        System.out.println("MANAGER CLICK: " + managers);
        if (managers >= 1) {
            existCheck = false;
            ManagerTxt = "";
        } else {
            existCheck = true;
            ManagerTxt = MANAGER_NOTFOUND;
        }

        renderButton();
    }

    public void renderButton() {

        if ((checkUImage1)
                | (checkUImage2)
                | (checkUImage3)
                | (checkUImage4)
                | (checkUImage5)) {
            existCheck = true;

        } else {

            existTxt = "";

        }

    }

    public void reset1() {

        email1 = "";
        if (col1 != null) {
            col1.setRole(null);
        }

        existTxt = "";

        checkCImage1 = false;
        checkUImage1 = false;
        checkMan1 = false;
        renderButton();

    }

    public void reset2() {

        email2 = "";
        if (col2 != null) {
            col2.setRole(null);
        }

        checkCImage2 = false;
        checkUImage2 = false;
        checkMan2 = false;
        renderButton();

    }

    public void reset3() {

        email3 = "";
        if (col3 != null) {
            col3.setRole(null);
        }

        checkMan3 = false;
        checkCImage3 = false;
        checkUImage3 = false;
        renderButton();

    }

    public void reset4() {

        email4 = "";
        if (col4 != null) {
            col4.setRole(null);
        }

        checkMan4 = false;
        checkCImage4 = false;
        checkUImage4 = false;
        renderButton();

    }

    public void reset5() {

        email5 = "";
        if (col5 != null) {
            col5.setRole(null);
        }

        checkMan5 = false;
        checkCImage5 = false;
        checkUImage5 = false;
        renderButton();

    }

    public void renderCol() {

        switch (numberCol) {
            case 1:
                checkCol1 = true;
                checkCol2 = false;
                reset2();
                checkCol3 = false;
                checkCol4 = false;
                checkCol5 = false;
                checkManager();
                break;
            case 2:
                checkCol1 = true;
                checkCol2 = true;
                checkCol3 = false;
                reset3();
                checkCol4 = false;
                checkCol5 = false;
                checkManager();
                break;
            case 3:
                checkCol1 = true;
                checkCol2 = true;
                checkCol3 = true;
                checkCol4 = false;
                reset4();
                checkCol5 = false;
                checkManager();
                break;
            case 4:
                checkCol1 = true;
                checkCol2 = true;
                checkCol3 = true;
                checkCol4 = true;
                checkCol5 = false;
                reset5();
                checkManager();
                break;
            case 5:
                checkCol1 = true;
                checkCol2 = true;
                checkCol3 = true;
                checkCol4 = true;
                checkCol5 = true;
                checkManager();
                break;
            case 0:
                checkCol1 = false;
                reset1();
                checkManager();

                break;
            default:
                break;
        }

    }

    public int getNumberCol() {
        return numberCol;
    }

    public void setNumberCol(int numberCol) {
        this.numberCol = numberCol;
    }

    public boolean isExistCheck() {
        return existCheck;
    }

    public void setExistCheck(boolean existCheck) {
        this.existCheck = existCheck;
    }

    public String getExistTxt() {
        return existTxt;
    }

    public void setExistTxt(String existTxt) {
        this.existTxt = existTxt;
    }

    public String getEmail4() {
        return email4;
    }

    public void setEmail4(String email4) {
        this.email4 = email4;
    }

    public String getEmail5() {
        return email5;
    }

    public void setEmail5(String email5) {
        this.email5 = email5;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public UserProjectLinkController() {
    }

    public UserProjectLink getSelected() {
        if (current == null) {
            current = new UserProjectLink();
            current.setUserProjectLinkPK(new jpa.entities.UserProjectLinkPK());
            selectedItemIndex = -1;
        }
        return current;
    }

    public UserProjectLink getSelected1() {

        if (col1 == null) {
            col1 = new UserProjectLink();
            col1.setUserProjectLinkPK(new jpa.entities.UserProjectLinkPK());
            selectedItemIndex = -1;
        }
        return col1;
    }

    public UserProjectLink getSelected2() {
        if (col2 == null) {
            col2 = new UserProjectLink();
            col2.setUserProjectLinkPK(new jpa.entities.UserProjectLinkPK());
            selectedItemIndex = -1;
        }
        return col2;
    }

    public UserProjectLink getSelected3() {
        if (col3 == null) {
            col3 = new UserProjectLink();
            col3.setUserProjectLinkPK(new jpa.entities.UserProjectLinkPK());
            selectedItemIndex = -1;
        }
        return col3;
    }

    public UserProjectLink getSelected4() {
        if (col4 == null) {
            col4 = new UserProjectLink();
            col4.setUserProjectLinkPK(new jpa.entities.UserProjectLinkPK());
            selectedItemIndex = -1;
        }
        return col4;
    }

    public UserProjectLink getSelected5() {
        if (col5 == null) {
            col5 = new UserProjectLink();
            col5.setUserProjectLinkPK(new jpa.entities.UserProjectLinkPK());
            selectedItemIndex = -1;
        }
        return col5;
    }

    private UserProjectLinkFacade getFacade() {
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

    public String prepareView() {

        return "View";
    }

    public String prepareCreate() {

        current = new UserProjectLink();
        current.setUserProjectLinkPK(new jpa.entities.UserProjectLinkPK());
        selectedItemIndex = -1;

        return "AltaExitosa";
    }

    public void exist1() {
        if (email1 != null) {
            List<Users> u = ejbUsr.findUserByEmail(email1.toLowerCase());

            if (email1.isEmpty() | email1.equals("")) {//1
                existCheck = false;
                checkCImage1 = false;
                checkUImage1 = false;
                //JsfUtil.addErrorMessage("CORREO NO VERIFICADO"+"\n"+"El primer colaborador o responsable no esta registrado en nuestro sistema.");
            } else {
                if (u.isEmpty()) {//2
                    System.out.println("No existe el correo en la BD");
                    //existTxt = NOT_MATCH;
                    existCheck = true;
                    checkCImage1 = false;
                    checkUImage1 = true;
                    JsfUtil.addErrorMessage("CORREO NO VERIFICADO" + "\n" + "El primer colaborador o responsable no esta registrado en nuestro sistema.");

                } else {//3
                    System.out.println("correo ya registrado.");
                    existCheck = false;
                    checkCImage1 = true;
                    checkUImage1 = false;

                    JsfUtil.addSuccessMessage("CORREO VERIFICADO" + "\n" + "El primer colaborador o responsable ya esta registrado en nuestro sistema.");

                }

            }
        }
        checkManager();
        renderButton();

    }

    public void exist2() {
        List<Users> u;
        if (email3 != null) {
            u = ejbUsr.findUserByEmail(email2.toLowerCase());

            if (email2.isEmpty() | email2.equals("")) {//1

                existCheck = false;
                checkCImage2 = false;
                checkUImage2 = false;
                //JsfUtil.addErrorMessage("CORREO NO VERIFICADO"+"\n"+"El segundo colaborador o responsable no esta registrado en nuestro sistema.");
            } else {
                if (u.isEmpty()) {//2
                    //existTxt = NOT_MATCH;
                    existCheck = true;
                    checkCImage2 = false;
                    checkUImage2 = true;
                    JsfUtil.addErrorMessage("CORREO NO VERIFICADO" + "\n" + "El segundo colaborador o responsable no esta registrado en nuestro sistema.");
                } else {//3

                    existCheck = false;
                    checkCImage2 = true;
                    checkUImage2 = false;
                    JsfUtil.addSuccessMessage("CORREO VERIFICADO" + "\n" + "El segundo colaborador o responsable ya esta registrado en nuestro sistema.");
                }

            }
        }
        checkManager();
        renderButton();

    }

    public void exist3() {
        List<Users> u;
        if (email3 != null) {
            u = ejbUsr.findUserByEmail(email3.toLowerCase());

            if (email3.isEmpty() | email3.equals("")) {//1

                existCheck = false;
                checkCImage3 = false;
                checkUImage3 = false;
                //JsfUtil.addErrorMessage("CORREO NO VERIFICADO"+"\n"+"El tercer o responsable no esta registrado en nuestro sistema.");
            } else {
                if (u.isEmpty()) {//2
                    //existTxt = NOT_MATCH;
                    existCheck = true;
                    checkCImage3 = false;
                    checkUImage3 = true;
                    JsfUtil.addErrorMessage("CORREO NO VERIFICADO" + "\n" + "El tercer colaborador o responsable no esta registrado en nuestro sistema.");
                } else {//3

                    existCheck = false;
                    checkCImage3 = true;
                    checkUImage3 = false;
                    JsfUtil.addSuccessMessage("CORREO VERIFICADO" + "\n" + "El tercer colaborador o responsable ya esta registrado en nuestro sistema.");

                }

            }
        }
        checkManager();
        renderButton();

    }

    public void exist4() {
        List<Users> u;
        if (email4 != null) {
            u = ejbUsr.findUserByEmail(email4.toLowerCase());

            if (email4.isEmpty() | email4.equals("")) {//1

                existCheck = false;
                checkCImage4 = false;
                checkUImage4 = false;
                //JsfUtil.addErrorMessage("CORREO NO VERIFICADO"+"\n"+"El cuarto colaborador o responsable no esta registrado en nuestro sistema.");
            } else {
                if (u.isEmpty()) {//2
                    //existTxt = NOT_MATCH;
                    existCheck = true;
                    checkCImage4 = false;
                    checkUImage4 = true;
                    JsfUtil.addErrorMessage("CORREO NO VERIFICADO" + "\n" + "El cuarto colaborador o responsable no esta registrado en nuestro sistema.");
                } else {//3

                    existCheck = false;
                    checkCImage4 = true;
                    checkUImage4 = false;
                    JsfUtil.addSuccessMessage("CORREO VERIFICADO" + "\n" + "El cuarto colaborador o responsable ya esta registrado en nuestro sistema.");
                }

            }
        }
        checkManager();
        renderButton();

    }

    public void exist5() {
        List<Users> u;
        if (email5 != null) {
            u = ejbUsr.findUserByEmail(email5.toLowerCase());

            if (email5.isEmpty() | email5.equals("")) {//1

                existCheck = false;
                checkCImage5 = false;
                checkUImage5 = false;
                // JsfUtil.addErrorMessage("CORREO NO VERIFICADO"+"\n"+"El quinto colaborador o responsable no esta registrado en nuestro sistema.");
            } else {
                if (u.isEmpty()) {//2
                    //existTxt = NOT_MATCH;

                    existCheck = true;
                    checkCImage5 = false;
                    checkUImage5 = true;
                    JsfUtil.addErrorMessage("CORREO NO VERIFICADO" + "\n" + "El quinto colaborador o responsable no esta registrado en nuestro sistema.");
                } else {//3

                    existCheck = false;
                    checkCImage5 = true;
                    checkUImage5 = false;
                    JsfUtil.addSuccessMessage("CORREO VERIFICADO" + "\n" + "El quinto colaborador o responsable ya esta registrado en nuestro sistema.");
                }

            }
        }
        checkManager();
        renderButton();

    }

    public boolean checkProjectName() {
        /*FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

        String manager = us.getUserName();
        String correo1 = "";

        
        if (email1 != null && !email1.isEmpty()) {
            correo1 = email1.toLowerCase();
            if (!ejbUsr.findUserByEmail(correo1).isEmpty()) {              
                //col1.getRole()        
            }
        }*/ //chido
        
                 
        String manager = null;
         FacesContext context = FacesContext.getCurrentInstance();
         Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        //Users us = context.getExternalContext().getSessionMap().get("usuario");
        //Project pj = context.getExternalContext().getSessionMap().get("project");
        
        List<UserProjectLink> upl = new ArrayList<UserProjectLink>();
         List<Users> u = new ArrayList<Users>();
        upl.add(this.current);
        u.add(us);
        String correo1 = "";
        String correo2 = "";
        String correo3 = "";
        String correo4 = "";
        String correo5 = "";
        if (this.email1 != null) {
            correo1 = this.email1.toLowerCase();
        }
        if (this.email2 != null) {
            correo2 = this.email2.toLowerCase();
        }
        if (this.email3 != null) {
            correo3 = this.email3.toLowerCase();
        }
        if (this.email4 != null) {
            correo4 = this.email4.toLowerCase();
        }
        if (this.email5 != null) {
            correo5 = this.email5.toLowerCase();
        }
        if (!this.ejbUsr.findUserByEmail(correo1).isEmpty()) {
            upl.add(this.col1);
            u.add(this.ejbUsr.findUserByEmail(correo1).get(0));
        }
        if (!this.ejbUsr.findUserByEmail(correo2).isEmpty()) {
            upl.add(this.col2);
            u.add(this.ejbUsr.findUserByEmail(correo2).get(0));
        }
        if (!this.ejbUsr.findUserByEmail(correo3).isEmpty()) {
            upl.add(this.col3);
            u.add(this.ejbUsr.findUserByEmail(correo3).get(0));
        }
        if (!this.ejbUsr.findUserByEmail(correo4).isEmpty()) {
            upl.add(this.col4);
            u.add(this.ejbUsr.findUserByEmail(correo4).get(0));
        }
        if (!this.ejbUsr.findUserByEmail(correo5).isEmpty()) {
            upl.add(this.col5);
            u.add(this.ejbUsr.findUserByEmail(correo5).get(0));
        }
        int numManagers = 0;
        for (int i = 0; i < u.size(); ++i) {
            try {
                if (upl.get(i).getRole().equals("Responsable")) {
                    ++numManagers;
                    if (numManagers==1){
                    manager = u.get(i).getUserName();
                    }
                    break;
                }
            }
            catch (Exception e) {
                 RequestContext RC = RequestContext.getCurrentInstance();
                RC.execute("PF('AdvDialog').show();");
            }
        }
        
             //inicio del chido
        
        if (managers <= 0) {
            JsfUtil.addErrorMessage("Por favor agregue al menos un Responsable al proyecto");
            return false;
        } else {

            renameIdProject(pj, manager);
            return true;
        }
    }

    public void createEdit() {

        try {
            createRes();

            if (email1 != null && !"".equals(email1) && col1 != null) {
                createCol1();
            }
            if (email2 != null && !"".equals(email2) && col2 != null) {
                createCol2();
            }

            if (email3 != null && !"".equals(email3) && col3 != null) {
                createCol3();
            }

            if (email4 != null && !"".equals(email4) && col4 != null) {
                createCol4();
            }
            if (email5 != null && !"".equals(email5) && col5 != null) {
                createCol5();
            }

        } catch (Exception e) {
            System.out.println("ERROR COLS: " + e.getMessage() + " : " + e.getLocalizedMessage());

        }
    }

    public void create() {
        if (checkTerms == true) {
            if (checkProjectName()) {
                try {
                    FacesContext context = FacesContext.getCurrentInstance();
                    Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

                    if (pj != null) {
                        createRes();

                        if (email1 != null && !"".equals(email1) && col1 != null) {
                            createCol1();
                        }

                        if (email2 != null && !"".equals(email2) && col2 != null) {
                            createCol2();
                        }

                        if (email3 != null && !"".equals(email3) && col3 != null) {
                            createCol3();
                        }

                        if (email4 != null && !"".equals(email4) && col4 != null) {
                            createCol4();
                        }

                        if (email5 != null && !"".equals(email5) && col5 != null) {
                            createCol5();
                        }

                        Comments comments = new Comments();
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

                        if (!comment.equals("")) {
                            comments.setComment(comment);
                            comments.setIdType(pj.getIdProject());
                            comments.setType("Project");
                            comments.setUserName(us.getUserName());
                            comments.setCommentDate(timestamp);
                            System.out.println("antes del facade");
                            commentFac.createComment(comments);
                        }

                        initialize();
                        FacesContext.getCurrentInstance().getExternalContext().redirect("AltaExitosa.xhtml");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(UserProjectLinkController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // FacesContext context = FacesContext.getCurrentInstance();
            RequestContext RC = RequestContext.getCurrentInstance();
            RC.execute("PF('warnDialog').show();");
        }
    }

    public void initialize() {
        checkCol1 = false;
        checkCol2 = false;
        checkCol3 = false;
        checkCol4 = false;
        checkCol5 = false;
        reset1();
        reset2();
        reset3();
        reset4();
        reset5();
        numberCol = 0;
        current = null;
        checkManager();
        checkTerms = false;
        managers = 0;
        comment = "";
        ltsCheck = java.util.Arrays.asList(false, false, false, false, false, false);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("project", null);
    }

    public void editColaborators() {
        FacesContext context = FacesContext.getCurrentInstance();
        List<Users> u = new ArrayList<>();

        String correo1 = "";
        String correo2 = "";
        String correo3 = "";
        String correo4 = "";
        String correo5 = "";
        if (email1 != null) {
            correo1 = email1.toLowerCase();
        }
        if (email2 != null) {
            correo2 = email2.toLowerCase();
        }
        if (email3 != null) {
            correo3 = email3.toLowerCase();
        }
        if (email4 != null) {
            correo4 = email4.toLowerCase();
        }
        if (email5 != null) {
            correo5 = email5.toLowerCase();
        }

        if (!ejbUsr.findUserByEmail(correo1).isEmpty()) {

            u.add(ejbUsr.findUserByEmail(correo1).get(0));
        }
        if (!ejbUsr.findUserByEmail(correo2).isEmpty()) {

            u.add(ejbUsr.findUserByEmail(correo2).get(0));
        }
        if (!ejbUsr.findUserByEmail(correo3).isEmpty()) {

            u.add(ejbUsr.findUserByEmail(correo3).get(0));
        }
        if (!ejbUsr.findUserByEmail(correo4).isEmpty()) {

            u.add(ejbUsr.findUserByEmail(correo4).get(0));
        }
        if (!ejbUsr.findUserByEmail(correo5).isEmpty()) {

            u.add(ejbUsr.findUserByEmail(correo5).get(0));
        }
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

        for (Users user : u) {
            UserProjectLink upl = new UserProjectLink();
            upl.setUserProjectLinkPK(new jpa.entities.UserProjectLinkPK());

            upl.setRole("Colaborador");
            upl.getUserProjectLinkPK().setIdProject(pj.getIdProject());
            upl.getUserProjectLinkPK().setIdUser(user.getIdUser());
            getFacade().create(upl);
            //   ec.sendProject2UserEmail(user.getEmail(), upl.getRole());
        }

    }

    public void createRes() {
        try {

            FacesContext context = FacesContext.getCurrentInstance();

            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

            current.getUserProjectLinkPK().setIdUser(us.getIdUser());
            current.getUserProjectLinkPK().setIdProject(pj.getIdProject());
            getFacade().create(current);

            //ec.sendProject2UserEmail(us.getEmail(), current.getRole());
            // ec.sendNewProjectManagerEmail(us.getUserName(), current.getRole());
        } catch (Exception e) {
            System.out.println("ERROR CREATE_RES:" + e.getMessage());
        }

    }

    public void createCol1() {
        try {
            // PlantillaController pc=null;
            FacesContext context = FacesContext.getCurrentInstance();
            Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
            UserProjectLink upl = new UserProjectLink();
            upl.setUserProjectLinkPK(new jpa.entities.UserProjectLinkPK());

            try {
                List<Users> u = ejbUsr.findUserByEmail(email1.toLowerCase());
                upl.getUserProjectLinkPK().setIdUser(u.get(0).getIdUser());

            } catch (NullPointerException e) {
                System.out.println(e);
            }

            System.out.println(pj.getProjectName());
            upl.getUserProjectLinkPK().setIdProject(pj.getIdProject());
            if (upl.getRole() == null) {
                upl.setRole(col1.getRole());
            }
            getFacade().create(upl);

            ec.sendProject2UserEmail(email1, upl.getRole());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);

        } catch (EJBException e) {

        } catch (NullPointerException e) {
            System.out.println(e + " <--- No hubo colaborador 1");
        }

    }

    public void createCol2() {
        try {
            // PlantillaController pc=null;
            FacesContext context = FacesContext.getCurrentInstance();

            try {
                List<Users> u = ejbUsr.findUserByEmail(email2.toLowerCase());
                //u.get(0).getEmail();
                col2.getUserProjectLinkPK().setIdUser(u.get(0).getIdUser());

            } catch (NullPointerException e) {
                System.out.println(e);
            }

            Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
            col2.getUserProjectLinkPK().setIdProject(pj.getIdProject());

            if (col2.getRole() == null) {
                col2.setRole("Colaborador");
            }
            getFacade().create(col2);

           ec.sendProject2UserEmail(email2, col2.getRole());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);

        } catch (EJBException e) {

        } catch (NullPointerException e) {
            System.out.println(e + " <--- No hubo colaborador 2");
        }

    }

    public void createCol3() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            List<Users> u;

            try {
                u = ejbUsr.findUserByEmail(email3.toLowerCase());
                col3.getUserProjectLinkPK().setIdUser(u.get(0).getIdUser());

            } catch (NullPointerException e) {
                System.out.println(e);
            }

            Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

            col3.getUserProjectLinkPK().setIdProject(pj.getIdProject());
            if (col3.getRole() == null) {
                col3.setRole("Colaborador");
            }
            getFacade().create(col3);

           ec.sendProject2UserEmail(email3, col3.getRole());
        } catch (ArrayIndexOutOfBoundsException | EJBException e) {
            System.out.println(e);

        } catch (NullPointerException e) {
            System.out.println(e + " <--- No hubo colaborador 3");
        }

    }

    public void createCol4() {
        try {
            // PlantillaController pc=null;
            FacesContext context = FacesContext.getCurrentInstance();
            List<Users> u;

            try {
                u = ejbUsr.findUserByEmail(email4.toLowerCase());
                col4.getUserProjectLinkPK().setIdUser(u.get(0).getIdUser());

            } catch (NullPointerException e) {
                System.out.println(e);
            }

            Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

            col4.getUserProjectLinkPK().setIdProject(pj.getIdProject());
            if (col4.getRole() == null) {
                col4.setRole("Colaborador");
            }

            getFacade().create(col4);

                  ec.sendProject2UserEmail(email4, col4.getRole());
        } catch (ArrayIndexOutOfBoundsException | EJBException e) {
            System.out.println(e);

        } catch (NullPointerException e) {
            System.out.println(e + " <--- No hubo colaborador 4");
        }

    }

    public void createCol5() {
        try {
            // PlantillaController pc=null;
            FacesContext context = FacesContext.getCurrentInstance();
            List<Users> u;

            try {
                u = ejbUsr.findUserByEmail(email5.toLowerCase());
                col5.getUserProjectLinkPK().setIdUser(u.get(0).getIdUser());

            } catch (NullPointerException e) {
                System.out.println(e);
            }

            Project pj = (Project) context.getExternalContext().getSessionMap().get("project");

            col5.getUserProjectLinkPK().setIdProject(pj.getIdProject());
            if (col5.getRole() == null) {
                col5.setRole("Colaborador");
            }
            getFacade().create(col5);

              ec.sendProject2UserEmail(email5, col5.getRole());
        } catch (ArrayIndexOutOfBoundsException | EJBException e) {
            System.out.println(e);

        } catch (NullPointerException e) {
            System.out.println(e + " <--- No hubo colaborador 5");
        }

    }

    public void renameIdProject(Project pj, String userID) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            FacesContext context = FacesContext.getCurrentInstance();
            //Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            String[] tiempo = timestamp.toString().split("-");
            String[] dia = tiempo[2].split(" ");
            String[] min = dia[1].split(":");
            String[] segs = min[2].split("\\.");
            String date = tiempo[0] + "_" + tiempo[1] + "_" + dia[0] + "_" + min[0] + "_" + min[1] + "_" + segs[0];

            pj.setIdProject("Project_" + userID + "_" + date);

            // pj.setQuotationNumber(invoice);
            ejbPj.create(pj);

            //EmailController ec= new EmailController();
//            ec.sendNewProjectManagerEmail(us.getUserName(), current.getRole());
            System.out.println("Manda correo");

            Comments comments = new Comments();

            comments.setComment("Éste proyecto fue dado de alta por el usuario: " + us.getUserName());
            comments.setIdType(pj.getIdProject());
            comments.setType("Project");
            comments.setUserName("SISBI");
            comments.setCommentDate(timestamp);

            commentFac.createComment(comments);

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("project", pj);

            //createCot();
        } catch (Exception e) {
            System.out.println("ocurrio un problema: " + e + " - " + e.getLocalizedMessage());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("project", null);
        }
    }

    public String update() {
        try {
            current.getUserProjectLinkPK().setIdUser(current.getUsers().getIdUser());
            current.getUserProjectLinkPK().setIdProject(current.getProject().getIdProject());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserProjectLinkUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public UserProjectLink getUserProjectLink(jpa.entities.UserProjectLinkPK id) {
        return ejbFacade.find(id);
    }

    public void createCot() throws ParseException {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        String s = null;
        try {
            Process p = Runtime.getRuntime().exec("python3 /var/www/html/get_by_no.py " + invoice);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {

                if (s.replaceAll("\"", "").equals("{}")) {
                    System.out.println("no existe esa cotizacion en sibiotec");

                    context.addMessage(null, new FacesMessage("Ocurrio un error", "Por favor verifique el numero de cotizacion ya que en nuestros registros no existe ese numero de cotizacion"));
                } else {
                    //System.out.println(s.replaceAll("\"", "").replaceAll("\\\\", "").replaceAll("/", "").replaceAll("u00e1", "á").replaceAll("u00e9", "é").replaceAll("u00ed", "í").replaceAll("u00f3", "ó").replaceAll("u00fa", "ú"));

                    String cadena = s.replaceAll("\"", "").replaceAll("\\\\", "").replaceAll("/", "");
                    String cadena2 = cadena.replaceAll("u00c1", "Á").replaceAll("u00c9", "É").replaceAll("u00cd", "Í").replaceAll("u00d3", "Ó").replaceAll("u00da", "Ú");

                    JSONObject json = new JSONObject(cadena2);

                    Integer dt = json.getJSONObject(invoice.substring(5)).getInt("Fecha");
                    Double mt = json.getJSONObject(invoice.substring(5)).getDouble("Total");
                    String sv = json.getJSONObject(invoice.substring(5)).getString("Descripcion");
                    String tc = json.getJSONObject(invoice.substring(5)).getString("TipoCliente");
                    String nc = json.getJSONObject(invoice.substring(5)).getString("NombreContacto");

                    SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat newformat = new SimpleDateFormat("yyyy/MM/dd");
                    Date date = f.parse(dt.toString());
                    String formatedDate = newformat.format(date);
                    Date fecha = newformat.parse(formatedDate);

                    String CotStat = json.getJSONObject(invoice.substring(5)).getString("Estatus");

                    /* CotizacionController cq = new CotizacionController();
                     cq.CreateCot(invoice,mt,CotStat,nc,sv,fecha,tc,us,pj);*/
                    try {

                        Cotizacion newCot = new Cotizacion();
                        newCot.setCotizaNum(invoice.substring(5));
                        newCot.setCotizaFecha(fecha);
                        newCot.setCotizaMontoPesos(mt);
                        newCot.setCotizaStatus(CotStat.substring(4));
                        newCot.setNameContact(nc);
                        newCot.setTypeService(sv);
                        // newCot.setIdUser(us);
                        newCot.setTypeClient(tc);
                        Ejbcot.create(newCot);
                        System.out.println("crea la cotizacion");

                        try {

                            ProyCotizaFacPagoLink CotLink = new ProyCotizaFacPagoLink();

                            CotLink.setCotizaId(newCot);
                            CotLink.setIdProject(pj);
                            CotLink.setIdUser(us);
                            EjbCotLink.create(CotLink);
                            System.out.println("crea la relacion Proyecto-Cotizacion");
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }

                    pj.setQuotationNumber(invoice + " - " + CotStat.replace("u00f3", "ó").substring(4));
                    ejbFacadeProject.edit(selectedProject);

                    // context.getExternalContext().redirect("ViewProject.xhtml");
                }

            }
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println("no existe esa cotizacion en sibiotec" + s);
            }

        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            context.addMessage(null, new FacesMessage("Ocurrio un error", "Intentelo mas tarde por favor"));
            e.printStackTrace();

        }
    }

//limpiar campos de email al registrar un proyecto
public void resetInputEmail(){
    numberCol = 0;
    if (numberCol <= 0) {
            checkSubs = true;
        } else {
            checkAdd = false;
        }
    email1 = null;
    email2 = null;
    email3 = null;
    email4 = null;
    email5 = null;
    comment = null;
    current = null;
    checkCol1 = false;
    checkCol2 = false;
    checkCol3 = false;
    checkCol4 = false;
    checkCol5 = false;
    
    checkUImage1 = false;
    checkCImage1 = false;
    checkUImage2 = false;
    checkCImage2 = false;
    checkTerms = false;
}

}
