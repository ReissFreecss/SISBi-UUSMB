package jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jpa.entities.Project;
import jpa.entities.Users;
import jpa.session.UsersSessionFacade;
import jpa.session.UsersFacadeSessionLocal;
import jpa.session.UsersFacadeSessionLocal;

/**
 *
 * @author aaron
 */
@Named("indexController")
@ViewScoped

public class IndexController implements Serializable{
    
    @EJB
    private UsersFacadeSessionLocal EJBUsuario;
    private Users usuario;
    private List<Project> projects;
    private String email;
    private String prevUrL;

    public String getPrevUrL() {
        return prevUrL;
    }

    public void setPrevUrL(String prevUrL) {
        this.prevUrL = prevUrL;
    }
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @PostConstruct
    public void init(){
        usuario=new Users();
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario) {
        this.usuario = usuario;
    }
    
    public List <String> images(){
    List <String> list=new ArrayList<>();
    list.add("IBT.JPG");
    list.add("SGC_2024.jpg");
    list.add("SG101696.JPG");
    list.add("SG101706.JPG");
    list.add("SG101718.JPG");
    list.add("SG101728.JPG");
    list.add("FLOWCELL1.JPG");
    list.add("NEXTSEQ1.JPG");
    list.add("NEXTSEQ2.JPG");
    list.add("MinION1.JPG");
    list.add("PacBio RS II.png");
    list.add("MiSeq.png");
    list.add("iSeq.jpg");
    list.add("Teopanzolco_cto.jpg");
    
   
   
    
    
    return list;
    }
    
    public List<Project> listarProyectos(){
   
        List<Project> proyectos;
        
        Users us;
   
   us=EJBUsuario.iniciarSesion(usuario);
    proyectos=EJBUsuario.projectUser(us);
        for (Project proyecto : proyectos) {
            System.out.println(proyecto.getProjectName());
        }
    return proyectos;
    
    
    
    }
    
    public String help(){
    
    return "Nombre de usario asignado por el sistema se compone de la primera letra del primer nombre en mayuscula y el primer apellido del usuario.  Ejemplo: Luis Perez Martinez -> LPerez ó  LUIS PEREZ MARTINEZ -> LPEREZ";
    
    }
    
    public String iniciarSesion(){
        
        Users us;
        String redireccion = null;
        
        try {
            us=EJBUsuario.iniciarSesion(usuario);
            if (us!=null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", us);
                
                if("Admin".equals(us.getUserType())){
                            redireccion="/Principal/opciones?faces-redirect=true";
                    System.out.println(us.getUserType());
               
                } else{
                    System.out.println(us.getUserType());
                redireccion="/Principal/Welcome?faces-redirect=true";
                  
                }
            }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nombre y/o contraseña invalidos. Por favor verifique sus datos","Credenciales Incorrectas"));
            
            }
            
            
           
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Aviso","Error"));
        
        }
        
        return redireccion;
    
    
    }
   
    public void getPrevURL() throws IOException {
        System.out.println("obtenemos el url de la pagina anterior");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String referrer = externalContext.getRequestHeaderMap().get("referer");
        prevUrL = referrer;
        System.out.println("prevURL:" + prevUrL);
        

    }

    public void getURLPreviousPage() throws IOException {
        System.out.println("metodo getURL");
        FacesContext context = FacesContext.getCurrentInstance();
        // HttpServletRequest.getHeader("Referer");
        /* ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
   String referrer = externalContext.getRequestHeaderMap().get("referer"); */
        //context.getExternalContext().redirect(URl);

        System.out.println(prevUrL);
        //getPrevURL();
        context.getExternalContext().redirect(prevUrL);

    }
    
}
