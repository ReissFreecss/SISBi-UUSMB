package jsf;

import java.io.IOException;
import jpa.entities.Comments;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import jpa.session.CommentsFacade;

import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import jpa.entities.Project;
import jpa.entities.Sample;
import jpa.entities.UserRole;
import jpa.entities.Users;
import jpa.session.UserRoleFacade;

@Named("commentsController")
@SessionScoped
public class CommentsController implements Serializable {

    private Comments current;

    @EJB
    private jpa.session.CommentsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String comment;
    private List<Comments> CommentsTable;
    private boolean User;
    @EJB
    private UserRoleFacade roleFac;

    private String veditComment;

    private String commentBA;

    private String commentEditBA;

    public String getCommentEditBA() {
        return commentEditBA;
    }

    public void setCommentEditBA(String commentEditBA) {
        this.commentEditBA = commentEditBA;
    }

    public String getCommentBA() {
        return commentBA;
    }

    public void setCommentBA(String commentBA) {
        this.commentBA = commentBA;
    }

    public String getVeditComment() {
        return veditComment;
    }

    public void setVeditComment(String veditComment) {
        this.veditComment = veditComment;
    }

    public boolean isUser() {
        return User;
    }

    public void setUser(boolean User) {
        this.User = User;
    }

    public List<Comments> getCommentsTable() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listComments", CommentsTable);
        return CommentsTable;
    }

    public void setCommentsTable(List<Comments> CommentsTable) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("listComments", CommentsTable);
        this.CommentsTable = CommentsTable;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CommentsController() {
    }

    public Comments getSelected() {
        if (current == null) {
            current = new Comments();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CommentsFacade getFacade() {
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

    public String prepareCreate() {
        current = new Comments();
        selectedItemIndex = -1;
        return "Create";
    }

    public void create(Project pro) {

        System.out.println("Llega a CreateComment");
        System.out.println(pro);
        Comments comments = new Comments();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        FacesContext context = FacesContext.getCurrentInstance();

        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        comments.setComment(comment);
        comments.setIdType(pro.getIdProject());
        comments.setType("Project");
        comments.setUserName(us.getUserName());
        comments.setCommentDate(timestamp);

        getFacade().createComment(comments);
        System.out.println("pasa por el ejbFacade");
        
         //envio de correo con comentario del proyecto obligatorio para todos los usuarios que no sean admin
        if (us.getUserType().equals("Usuario")) {
        //Obtenemos el registro de colaboradores en el proyecto
        List<UserRole> usrole = roleFac.findRangeUsersProj(pro.getIdProject());
        List<String> emails_colab = new ArrayList<>();
        
            try {
                for (UserRole user : usrole) {
                        //Todos esos registros de UserRole con el id de poyecto los guardamos en la lista de email
                        emails_colab.add(user.getEmail());
                        System.out.println(user.getEmail());
                    }
                //System.out.println("comentarios del usuario"+us.getUserName()+" para uusmb: " + comment);
                EmailController ec = new EmailController();
                
                String comment_by_user="El usuario <strong>"+us.getUserName()+"</strong>"
                        +" comentó sobre el proyecto: <strong>"+pro.getIdProject()+"</strong><br>"
                        +" El siguiente comentario: <br><strong>"+ comment+"</strong>";
                ec.sendCommentsProject(emails_colab, pro.getIdProject() , comment_by_user, us.getEmail());
            } catch (Exception ex) {
                System.out.println("error en el envio de comentario del usuario a uusmb  " + ex.getMessage());
            }       
        }                          
        comment = null;

    }

    public void redirectViewProject(Project pro) {

        try {
            create(pro);
            FacesContext.getCurrentInstance().getExternalContext().redirect("ViewProject.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(CommentsController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public void redirectView(Project pro) {

        try {
            create(pro);
            FacesContext.getCurrentInstance().getExternalContext().redirect("ViewProject.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(CommentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void redirectProject(Project pro) {

        try {
            create(pro);
            FacesContext.getCurrentInstance().getExternalContext().redirect("AltaExitosa.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(CommentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void create4ListAll(Project pro) {
        try {
            System.out.println("Llega a CreateComment");
            System.out.println(pro);
            Comments comments = new Comments();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            FacesContext context = FacesContext.getCurrentInstance();

            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            System.out.println(us.toString() + " " + pro.toString());
            System.out.println("->" + comment);
            comments.setComment(comment);
            comments.setIdType(pro.getIdProject());
            comments.setType("Project");
            comments.setUserName(us.getUserName());
            comments.setCommentDate(timestamp);

            getFacade().createComment(comments);
            System.out.println("pasa por el ejbFacade");
            comment = null;
            FacesContext.getCurrentInstance().getExternalContext().redirect("ListAll.xhtml");
        } catch (Exception e) {
            System.out.println("algo paso" + e);

        }
    }

    public void create4Sample() {
        try {
            System.out.println("Coment --> " + comment);
            if (comment != null && comment.length() != 0) {

                Comments comments = new Comments();
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                FacesContext context = FacesContext.getCurrentInstance();

                Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

                Sample sam = (Sample) context.getExternalContext().getSessionMap().get("sample");
                comments.setComment(comment);
                comments.setIdType(sam.getIdSample() + "");
                comments.setType("Sample");
                comments.setUserName(us.getUserName());
                comments.setCommentDate(timestamp);
                System.out.println(comment.length());
                getFacade().createComment(comments);
                System.out.println("pasa por el ejbFacade");
                comment = null;
            }
        } catch (NullPointerException e) {
            System.out.println("algo paso" + e);

        }

    }

    public void create4Project() {
        try {
            System.out.println("Llega a CreateComment");

            Comments comments = new Comments();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            FacesContext context = FacesContext.getCurrentInstance();

            Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
            Project pro = (Project) context.getExternalContext().getSessionMap().get("project");
            System.out.println(us.toString() + " " + pro.toString());
            System.out.println("->" + comment);
            if (!comment.equals("")) {
                comments.setComment(comment);
                comments.setIdType(pro.getIdProject());
                comments.setType("Project");
                comments.setUserName(us.getUserName());
                comments.setCommentDate(timestamp);
                System.out.println("antes del facade");
                getFacade().createComment(comments);
                System.out.println("pasa por el ejbFacade");

            }

            FacesContext.getCurrentInstance().getExternalContext().redirect("AltaExitosa.xhtml");

        } catch (Exception e) {
            System.out.println("algo paso" + e);

        }

    }

    public boolean checkUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (us.getUserType().equals("Admin")) {
            User = true;
            return User;
        } else {
            User = false;
            return User;
        }
    }

    public void updateComment() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

        for (Comments Comment : CommentsTable) {
            System.out.println("id_ del comentario " + Comment.getIdComment());
            Comment.setComment(comment + "\n(comentario editado por: " + us.getUserName() + ")");
            getFacade().edit(Comment);
        }

        comment = null;
        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
        } catch (IOException ex) {

            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void calcelUpdateComment() {
        comment = null;
    }

    //Validación de edición de comentario
    public String canEditComment(String userComment) {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

        if (us.getUserName().equals(userComment)) {
            return "false";
        }
        return "true";
    }

    public boolean checkUserComment(String itemComment) {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        if (us.getUserType().equals("Admin")) {
            User = true;

            //No se mostrará la leyenda de '(comentario editado por: xxxxxxx)'
            String frase = itemComment;
            System.out.println(frase);
            //System.out.println(frase.indexOf("("));
            //System.out.println(frase.charAt(11));

            String nuevaFrase = "";

            System.out.println("Existe la frase: " + frase.contains("(comentario editado por:"));

            if (frase.contains("(comentario editado por:") == true) {
                System.out.println("index: " + frase.indexOf("(comentario editado por:"));
                for (int i = 0; i < frase.indexOf("(comentario editado por:"); i++) {
                    nuevaFrase += frase.charAt(i);
                }
            } else {
                nuevaFrase = frase;
            }
            System.out.println(nuevaFrase);
            comment = nuevaFrase;
            return User;
        } else {
            User = false;
            return User;
        }
    }

    public void sendComments() {
        /*Obtenemos el contexto para asi obtener las variables de session de tipo project y usuarop*/
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<String> Coms = new ArrayList();
        /*Obtenemos el registro UserRole con todos los atributos que tengan el id del proyecto*/
        List<UserRole> url = roleFac.findRangeUsersProj(pj.getIdProject());
        List<String> emails = new ArrayList<>();
        String coments = "";
        System.out.println("entra al metodo para enviar correo de comentarios");
        int i = 0;
        try {
            for (Comments Comment : CommentsTable) {
                Coms.add("Comentario " + (i + 1) + ": " + Comment.getComment());
                coments = coments + Coms.get(i) + "\n";
                i++;
                for (UserRole user : url) {
                    /*Todos esos registros de UserRole con el id de poyecto los guardamos en la lista de email*/
                    emails.add(user.getEmail());
                    System.out.println(user.getEmail());
                }
            }
            System.out.println("comentarios: " + coments);
            EmailController ec = new EmailController();
            ec.sendCommentsProject(emails, pj.getProjectName(), coments, us.getEmail());
        } catch (Exception ex) {
            System.out.println("error en el envio de comentarios " + ex.getMessage());
        }

    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CommentsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public List<Comments> getProItems(Project idProj) {

        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("item", idProj);
        ArrayList<Comments> filteredList = new ArrayList<>();
        List<Comments> commentsList = ejbFacade.findAll();

        for (Comments comments : commentsList) {
            if (comments.getIdType().equals(idProj.getIdProject())) {
                filteredList.add(comments);

            }
        }

        return filteredList;
    }

    /*
    public List<Comments> getItems() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        context.getExternalContext().getSessionMap().put("item", proj);
        ArrayList<Comments> filteredList = new ArrayList<>();
        List<Comments> commentsList = ejbFacade.findAll();

        for (Comments comments : commentsList) {
            if (comments.getIdType().equals(proj.getIdProject())) {
                filteredList.add(comments);

            }
        }

        for (Comments comments : filteredList) {
            // System.out.println(comments);
        }

        return filteredList;
    }
     */
    public List<Comments> getItems() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        context.getExternalContext().getSessionMap().put("item", proj);
        List<Comments> commentsList = ejbFacade.commentsByProject(proj.getIdProject());
        return commentsList;
    }

    public List<Comments> getSamplesItems() {
        FacesContext context = FacesContext.getCurrentInstance();
        Sample sam = (Sample) context.getExternalContext().getSessionMap().get("sample");

        ArrayList<Comments> filteredList = new ArrayList<>();
        if (sam != null) {
            System.out.println("->Comments: " + sam.toString());

            if (sam.getIdSample() != null) {

                List<Comments> commentsList = ejbFacade.findAll();

                for (Comments comments : commentsList) {
                    if (comments.getIdType().equals(sam.getIdSample().toString())) {
                        filteredList.add(comments);

                    }
                }

            }

        }
        return filteredList;
    }

    //Obtener lista de comentarios de sample por medio del id
    public List<Comments> getSamplesItemsL() {
        FacesContext context = FacesContext.getCurrentInstance();
        Sample sam = (Sample) context.getExternalContext().getSessionMap().get("sample");

        /*
        List<Comments> listSample = new ArrayList<>();
        if(ejbFacade.getCommentsByIdType(sam.getIdSample().toString()) == null){
            listSample = ejbFacade.getCommentsByIdType(sam.getIdSample().toString());
        }*/
        List<Comments> filteredList = new ArrayList<>();
        if (sam != null) {
            System.out.println("->Comments: " + sam.toString());

            if (sam.getIdSample() != null) {

                System.out.println("Se imprime el valor de id de la muestra: " + sam.getIdSample().toString() + "Se imprime el arreglo: " + ejbFacade.getCommentsByIdType(sam.getIdSample().toString()));
                //TODO creo que aqui lo correcto sería también restringir la búsquwda por tipo de comentartio
                filteredList = ejbFacade.getCommentsByIdType(sam.getIdSample().toString());

            }

        }
        return filteredList;

    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Comments getComments(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Comments.class)
    public static class CommentsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CommentsController controller = (CommentsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "commentsController");
            return controller.getComments(getKey(value));
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
            if (object instanceof Comments) {
                Comments o = (Comments) object;
                return getStringKey(o.getIdComment());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Comments.class.getName());
            }
        }

    }

    public List<Comments> getCommentByProject(Project idProj) {

        List<Comments> commentsList = ejbFacade.commentsByProject(idProj.getIdProject());
        return commentsList;
    }

    //Resetear comment
    public void cleanStringComment() {
        comment = "";
    }

    //Método preparar modal para editar comentario
    public void prepareEditComment(Comments itemComment) {
        veditComment = itemComment.getComment();
        System.out.println("Se le asigna el valor a veditComment: " + veditComment);
        //No se mostrará la leyenda de '(comentario editado por: xxxxxxx)'
        String frase = veditComment;
        System.out.println(frase);
        //System.out.println(frase.indexOf("("));
        //System.out.println(frase.charAt(11));

        String nuevaFrase = "";

        System.out.println("Existe la frase: " + frase.contains("(comentario editado por:"));

        if (frase.contains("(comentario editado por:") == true) {
            System.out.println("index: " + frase.indexOf("(comentario editado por:"));
            for (int i = 0; i < frase.indexOf("(comentario editado por:"); i++) {
                nuevaFrase += frase.charAt(i);
            }
        } else {
            nuevaFrase = frase;
        }

        veditComment = nuevaFrase;

        current = itemComment;
    }

    //Método para editar comentario
    public void editComment() {
        FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");

        Comments Comment = current;

        System.out.println("id_ del comentario " + Comment.getIdComment());
        Comment.setComment(veditComment + "\n(comentario editado por: " + us.getUserName() + ")");
        getFacade().edit(Comment);

        veditComment = null;
        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Comments> getCommentsBAProject() {
        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");
        context.getExternalContext().getSessionMap().put("item", proj);
        List<Comments> commentsABList = ejbFacade.getCommentsBAbyProject(proj.getIdProject());
        return commentsABList;
    }

    //Método preparar current por el item enviado por el parámetro
    public void prepareCreateCommentBA() {
        System.out.println("Se ejecuta: prepareCreateCommentBA y se asigna valor de nulo a commentBA");
        commentBA = null;
    }

    public void createCommentBA() {

        FacesContext context = FacesContext.getCurrentInstance();
        Project proj = (Project) context.getExternalContext().getSessionMap().get("project");

        Comments comments = new Comments();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        comments.setComment(commentBA);
        comments.setIdType(proj.getIdProject());
        comments.setType("ProjectBA");
        comments.setUserName(us.getUserName());
        comments.setCommentDate(timestamp);
        getFacade().createComment(comments);
        commentBA = null;

        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Método preparar current por el item enviado por el parámetro
    public void prepareEditCommentBA(Comments itemComment) {
        System.out.println("Se ejecuta: prepareEditCommentBA y se asigna el valor de commentBA");
        commentEditBA = itemComment.getComment();
        current = itemComment;
    }

    //Método para editar comentario
    public void editCommentBA() {
        FacesContext context = FacesContext.getCurrentInstance();

        Comments Comment = current;
        Comment.setComment(commentEditBA);
        getFacade().edit(Comment);
        try {
            context.getExternalContext().redirect("ViewProject.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
