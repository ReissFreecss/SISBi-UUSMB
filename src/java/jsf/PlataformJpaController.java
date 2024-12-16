/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.Serializable;
//leslie
import static com.sun.xml.ws.spi.db.BindingContextFactory.LOGGER;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.Run;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import jsf.exceptions.IllegalOrphanException;
import jsf.exceptions.NonexistentEntityException;
import jsf.exceptions.PreexistingEntityException;
import jsf.exceptions.RollbackFailureException;
import jpa.entities.Plataform;
import jpa.session.PlataformFacade;
import jpa.entities.PlataformLinkKit;

import jpa.entities.Kit;
import jpa.session.KitFacade;
import jpa.entities.Plataform;
import jsf.util.JsfUtil;
import jsf.util.PaginationHelper;
import javax.ejb.EJB;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.Persistence;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;



/**
 *
 * @author leslie
 */

@Named("plataformjpaController")
@SessionScoped
public class PlataformJpaController implements Serializable {
    
    private Plataform current;
    private DataModel items = null;
    @EJB
    private jpa.session.PlataformFacade ejbFacade;    
    @EJB
    private KitFacade KitFac;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;
    //private Plataform selectedPlataform = new Plataform();
    
    private int id_plat;
    private String namePlataform;
    private String notes;
    private String location;
    
   // private Plataform selectedPlat;
    
    
    //leslie 04 sep
    public String getNamePlataform() {
        return namePlataform;
    }

    public void setNamePlataform(String namePlataform) {
        this.namePlataform = namePlataform;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public PlataformJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
     public PlataformJpaController() {
    }
    
   
    //leslie03 septiembre
    /*public PlataformJpaController(){
        emf= Persistence.createEntityManagerFactory("SISBIPU");
    }*/

    //leslie 03 septiembre
    public PlataformFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PlataformFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    //fin leslie
    
    public void create(Plataform plataform) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (plataform.getRunList() == null) {
            plataform.setRunList(new ArrayList<Run>());
        }
        if (plataform.getPlataformLinkKitList() == null) {
            plataform.setPlataformLinkKitList(new ArrayList<PlataformLinkKit>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Run> attachedRunList = new ArrayList<Run>();
            for (Run runListRunToAttach : plataform.getRunList()) {
                runListRunToAttach = em.getReference(runListRunToAttach.getClass(), runListRunToAttach.getIdRun());
                attachedRunList.add(runListRunToAttach);
            }
            plataform.setRunList(attachedRunList);
            List<PlataformLinkKit> attachedPlataformLinkKitList = new ArrayList<PlataformLinkKit>();
            for (PlataformLinkKit plataformLinkKitListPlataformLinkKitToAttach : plataform.getPlataformLinkKitList()) {
                plataformLinkKitListPlataformLinkKitToAttach = em.getReference(plataformLinkKitListPlataformLinkKitToAttach.getClass(), plataformLinkKitListPlataformLinkKitToAttach.getPlataformLinkKitPK());
                attachedPlataformLinkKitList.add(plataformLinkKitListPlataformLinkKitToAttach);
            }
            plataform.setPlataformLinkKitList(attachedPlataformLinkKitList);
            em.persist(plataform);
            for (Run runListRun : plataform.getRunList()) {
                Plataform oldIdPlataformOfRunListRun = runListRun.getIdPlataform();
                runListRun.setIdPlataform(plataform);
                runListRun = em.merge(runListRun);
                if (oldIdPlataformOfRunListRun != null) {
                    oldIdPlataformOfRunListRun.getRunList().remove(runListRun);
                    oldIdPlataformOfRunListRun = em.merge(oldIdPlataformOfRunListRun);
                }
            }
            for (PlataformLinkKit plataformLinkKitListPlataformLinkKit : plataform.getPlataformLinkKitList()) {
                Plataform oldPlataformOfPlataformLinkKitListPlataformLinkKit = plataformLinkKitListPlataformLinkKit.getPlataform();
                plataformLinkKitListPlataformLinkKit.setPlataform(plataform);
                plataformLinkKitListPlataformLinkKit = em.merge(plataformLinkKitListPlataformLinkKit);
                if (oldPlataformOfPlataformLinkKitListPlataformLinkKit != null) {
                    oldPlataformOfPlataformLinkKitListPlataformLinkKit.getPlataformLinkKitList().remove(plataformLinkKitListPlataformLinkKit);
                    oldPlataformOfPlataformLinkKitListPlataformLinkKit = em.merge(oldPlataformOfPlataformLinkKitListPlataformLinkKit);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlataform(plataform.getIdPlataform()) != null) {
                throw new PreexistingEntityException("Plataform " + plataform + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Plataform plataform) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Plataform persistentPlataform = em.find(Plataform.class, plataform.getIdPlataform());
            List<Run> runListOld = persistentPlataform.getRunList();
            List<Run> runListNew = plataform.getRunList();
            List<PlataformLinkKit> plataformLinkKitListOld = persistentPlataform.getPlataformLinkKitList();
            List<PlataformLinkKit> plataformLinkKitListNew = plataform.getPlataformLinkKitList();
            List<String> illegalOrphanMessages = null;
            for (PlataformLinkKit plataformLinkKitListOldPlataformLinkKit : plataformLinkKitListOld) {
                if (!plataformLinkKitListNew.contains(plataformLinkKitListOldPlataformLinkKit)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PlataformLinkKit " + plataformLinkKitListOldPlataformLinkKit + " since its plataform field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Run> attachedRunListNew = new ArrayList<Run>();
            for (Run runListNewRunToAttach : runListNew) {
                runListNewRunToAttach = em.getReference(runListNewRunToAttach.getClass(), runListNewRunToAttach.getIdRun());
                attachedRunListNew.add(runListNewRunToAttach);
            }
            runListNew = attachedRunListNew;
            plataform.setRunList(runListNew);
            List<PlataformLinkKit> attachedPlataformLinkKitListNew = new ArrayList<PlataformLinkKit>();
            for (PlataformLinkKit plataformLinkKitListNewPlataformLinkKitToAttach : plataformLinkKitListNew) {
                plataformLinkKitListNewPlataformLinkKitToAttach = em.getReference(plataformLinkKitListNewPlataformLinkKitToAttach.getClass(), plataformLinkKitListNewPlataformLinkKitToAttach.getPlataformLinkKitPK());
                attachedPlataformLinkKitListNew.add(plataformLinkKitListNewPlataformLinkKitToAttach);
            }
            plataformLinkKitListNew = attachedPlataformLinkKitListNew;
            plataform.setPlataformLinkKitList(plataformLinkKitListNew);
            plataform = em.merge(plataform);
            for (Run runListOldRun : runListOld) {
                if (!runListNew.contains(runListOldRun)) {
                    runListOldRun.setIdPlataform(null);
                    runListOldRun = em.merge(runListOldRun);
                }
            }
            for (Run runListNewRun : runListNew) {
                if (!runListOld.contains(runListNewRun)) {
                    Plataform oldIdPlataformOfRunListNewRun = runListNewRun.getIdPlataform();
                    runListNewRun.setIdPlataform(plataform);
                    runListNewRun = em.merge(runListNewRun);
                    if (oldIdPlataformOfRunListNewRun != null && !oldIdPlataformOfRunListNewRun.equals(plataform)) {
                        oldIdPlataformOfRunListNewRun.getRunList().remove(runListNewRun);
                        oldIdPlataformOfRunListNewRun = em.merge(oldIdPlataformOfRunListNewRun);
                    }
                }
            }
            for (PlataformLinkKit plataformLinkKitListNewPlataformLinkKit : plataformLinkKitListNew) {
                if (!plataformLinkKitListOld.contains(plataformLinkKitListNewPlataformLinkKit)) {
                    Plataform oldPlataformOfPlataformLinkKitListNewPlataformLinkKit = plataformLinkKitListNewPlataformLinkKit.getPlataform();
                    plataformLinkKitListNewPlataformLinkKit.setPlataform(plataform);
                    plataformLinkKitListNewPlataformLinkKit = em.merge(plataformLinkKitListNewPlataformLinkKit);
                    if (oldPlataformOfPlataformLinkKitListNewPlataformLinkKit != null && !oldPlataformOfPlataformLinkKitListNewPlataformLinkKit.equals(plataform)) {
                        oldPlataformOfPlataformLinkKitListNewPlataformLinkKit.getPlataformLinkKitList().remove(plataformLinkKitListNewPlataformLinkKit);
                        oldPlataformOfPlataformLinkKitListNewPlataformLinkKit = em.merge(oldPlataformOfPlataformLinkKitListNewPlataformLinkKit);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = plataform.getIdPlataform();
                if (findPlataform(id) == null) {
                    throw new NonexistentEntityException("The plataform with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Plataform plataform;
            try {
                plataform = em.getReference(Plataform.class, id);
                plataform.getIdPlataform();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plataform with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PlataformLinkKit> plataformLinkKitListOrphanCheck = plataform.getPlataformLinkKitList();
            for (PlataformLinkKit plataformLinkKitListOrphanCheckPlataformLinkKit : plataformLinkKitListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Plataform (" + plataform + ") cannot be destroyed since the PlataformLinkKit " + plataformLinkKitListOrphanCheckPlataformLinkKit + " in its plataformLinkKitList field has a non-nullable plataform field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Run> runList = plataform.getRunList();
            for (Run runListRun : runList) {
                runListRun.setIdPlataform(null);
                runListRun = em.merge(runListRun);
            }
            em.remove(plataform);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Plataform> findPlataformEntities() {
        return findPlataformEntities(true, -1, -1);
    }

    public List<Plataform> findPlataformEntities(int maxResults, int firstResult) {
        return findPlataformEntities(false, maxResults, firstResult);
    }

    private List<Plataform> findPlataformEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Plataform.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Plataform findPlataform(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Plataform.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlataformCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Plataform> rt = cq.from(Plataform.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    //leslie 03 septiembre 
    
    private List<Plataform> allplataforms;

    public List<Plataform> getAllplataforms() {
        allplataforms=  ejbFacade.findAll();
       // System.out.println("devuelve"+allplataforms);
      return allplataforms;
    }
    
    public void setAllplataforms(List<Plataform> allplataforms) {
        this.allplataforms = allplataforms;
    }
  
    public Plataform getSelected() {
        if (current == null) {
            current = new Plataform();
            selectedItemIndex = -1;
        }
        return current;
    }

    
     public void asignarValorCampos() { //metodo que ayuda a validar los campos                             
        current.getPlataformName();
        current.getNote();
        current.getLocation();
        System.out.println("Se asigna valor al campo nombre " + current.getPlataformName() + current.getNote()+current.getLocation());        
    }
     
     
       public void cleanForPlat() {
        
        namePlataform="";
        notes="";
        location="";
       
    }
     
    public void create_plat() {

        try {
            //current.setIdPlataform(0); // AUTOINCREMENT
            //current.setPlataformName(namePlataform);
            //current.setNote(notes);
            //current.setLocation(location);   
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("plataform", current);
            //current = new Plataform();
            
            getEjbFacade().create(current);
            
             //FacesContext.getCurrentInstance().addMessage("msgSuccess", new FacesMessage("Registro exitoso", "Muestra " + current.getSampleName() + " Registrada"));
            FacesContext.getCurrentInstance().addMessage("msgSuccess", new FacesMessage("Se ha reguistrado una nueva plataforma, actualice la lista de kit que pueden trabajar con esta plataforma"));
            cleanForPlat();

        } catch (Exception e) {
           JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            e.printStackTrace();

        }             
    }
    
    
    
}
