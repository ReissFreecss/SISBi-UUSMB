/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.Barcodes;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import jpa.entities.Kit;
import jpa.entities.PlataformLinkKit;
import jpa.session.KitFacade;
import jsf.exceptions.IllegalOrphanException;
import jsf.exceptions.NonexistentEntityException;
import jsf.exceptions.PreexistingEntityException;
import jsf.exceptions.RollbackFailureException;
import jsf.util.JsfUtil;

/**
 *
 * @author leslie
 */
@Named("kitjpaController")
@SessionScoped
public class KitJpaController implements Serializable {
    
    private Kit current;
    @EJB
    private jpa.session.KitFacade ejbKitFacade;    

  
    private jpa.session.PlataformLinkKitFacade ejbPlatKitLinkFacade;
    
    private int selectedItemIndex;
    private int id_kit;
    private String kit_name;
    private String note;
    private List <Kit> allkits;

   

    
    public KitJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    public KitJpaController(){
        
    }

    
      public KitFacade getEjbKitFacade() {
        return ejbKitFacade;
    }

    public void setEjbKitFacade(KitFacade ejbKitFacade) {
        this.ejbKitFacade = ejbKitFacade;
    }
       
    
     public List<Kit> getAllkits() {
         allkits=  ejbKitFacade.findAll();
        return allkits;
    }
     
     

    public void setAllkits(List<Kit> allkits) {
        this.allkits = allkits;
    }
    
    public Kit getSelected() {
        if (current == null) {
            current = new Kit();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public void asignarValorCampos() { //metodo que ayuda a validar los campos                             
        current.getKitName();
        current.getNote();
        System.out.println("Se asigna valor al campo nombre " + current.getKitName() + current.getNote());        
    }

    
    public void create(Kit kit) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (kit.getBarcodesList() == null) {
            kit.setBarcodesList(new ArrayList<Barcodes>());
        }
        if (kit.getPlataformLinkKitList() == null) {
            kit.setPlataformLinkKitList(new ArrayList<PlataformLinkKit>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Barcodes> attachedBarcodesList = new ArrayList<Barcodes>();
            for (Barcodes barcodesListBarcodesToAttach : kit.getBarcodesList()) {
                barcodesListBarcodesToAttach = em.getReference(barcodesListBarcodesToAttach.getClass(), barcodesListBarcodesToAttach.getIdBarcode());
                attachedBarcodesList.add(barcodesListBarcodesToAttach);
            }
            kit.setBarcodesList(attachedBarcodesList);
            List<PlataformLinkKit> attachedPlataformLinkKitList = new ArrayList<PlataformLinkKit>();
            for (PlataformLinkKit plataformLinkKitListPlataformLinkKitToAttach : kit.getPlataformLinkKitList()) {
                plataformLinkKitListPlataformLinkKitToAttach = em.getReference(plataformLinkKitListPlataformLinkKitToAttach.getClass(), plataformLinkKitListPlataformLinkKitToAttach.getPlataformLinkKitPK());
                attachedPlataformLinkKitList.add(plataformLinkKitListPlataformLinkKitToAttach);
            }
            kit.setPlataformLinkKitList(attachedPlataformLinkKitList);
            em.persist(kit);
            for (Barcodes barcodesListBarcodes : kit.getBarcodesList()) {
                Kit oldIdKitOfBarcodesListBarcodes = barcodesListBarcodes.getIdKit();
                barcodesListBarcodes.setIdKit(kit);
                barcodesListBarcodes = em.merge(barcodesListBarcodes);
                if (oldIdKitOfBarcodesListBarcodes != null) {
                    oldIdKitOfBarcodesListBarcodes.getBarcodesList().remove(barcodesListBarcodes);
                    oldIdKitOfBarcodesListBarcodes = em.merge(oldIdKitOfBarcodesListBarcodes);
                }
            }
            for (PlataformLinkKit plataformLinkKitListPlataformLinkKit : kit.getPlataformLinkKitList()) {
                Kit oldKitOfPlataformLinkKitListPlataformLinkKit = plataformLinkKitListPlataformLinkKit.getKit();
                plataformLinkKitListPlataformLinkKit.setKit(kit);
                plataformLinkKitListPlataformLinkKit = em.merge(plataformLinkKitListPlataformLinkKit);
                if (oldKitOfPlataformLinkKitListPlataformLinkKit != null) {
                    oldKitOfPlataformLinkKitListPlataformLinkKit.getPlataformLinkKitList().remove(plataformLinkKitListPlataformLinkKit);
                    oldKitOfPlataformLinkKitListPlataformLinkKit = em.merge(oldKitOfPlataformLinkKitListPlataformLinkKit);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findKit(kit.getIdKit()) != null) {
                throw new PreexistingEntityException("Kit " + kit + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Kit kit) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Kit persistentKit = em.find(Kit.class, kit.getIdKit());
            List<Barcodes> barcodesListOld = persistentKit.getBarcodesList();
            List<Barcodes> barcodesListNew = kit.getBarcodesList();
            List<PlataformLinkKit> plataformLinkKitListOld = persistentKit.getPlataformLinkKitList();
            List<PlataformLinkKit> plataformLinkKitListNew = kit.getPlataformLinkKitList();
            List<String> illegalOrphanMessages = null;
            for (PlataformLinkKit plataformLinkKitListOldPlataformLinkKit : plataformLinkKitListOld) {
                if (!plataformLinkKitListNew.contains(plataformLinkKitListOldPlataformLinkKit)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PlataformLinkKit " + plataformLinkKitListOldPlataformLinkKit + " since its kit field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Barcodes> attachedBarcodesListNew = new ArrayList<Barcodes>();
            for (Barcodes barcodesListNewBarcodesToAttach : barcodesListNew) {
                barcodesListNewBarcodesToAttach = em.getReference(barcodesListNewBarcodesToAttach.getClass(), barcodesListNewBarcodesToAttach.getIdBarcode());
                attachedBarcodesListNew.add(barcodesListNewBarcodesToAttach);
            }
            barcodesListNew = attachedBarcodesListNew;
            kit.setBarcodesList(barcodesListNew);
            List<PlataformLinkKit> attachedPlataformLinkKitListNew = new ArrayList<PlataformLinkKit>();
            for (PlataformLinkKit plataformLinkKitListNewPlataformLinkKitToAttach : plataformLinkKitListNew) {
                plataformLinkKitListNewPlataformLinkKitToAttach = em.getReference(plataformLinkKitListNewPlataformLinkKitToAttach.getClass(), plataformLinkKitListNewPlataformLinkKitToAttach.getPlataformLinkKitPK());
                attachedPlataformLinkKitListNew.add(plataformLinkKitListNewPlataformLinkKitToAttach);
            }
            plataformLinkKitListNew = attachedPlataformLinkKitListNew;
            kit.setPlataformLinkKitList(plataformLinkKitListNew);
            kit = em.merge(kit);
            for (Barcodes barcodesListOldBarcodes : barcodesListOld) {
                if (!barcodesListNew.contains(barcodesListOldBarcodes)) {
                    barcodesListOldBarcodes.setIdKit(null);
                    barcodesListOldBarcodes = em.merge(barcodesListOldBarcodes);
                }
            }
            for (Barcodes barcodesListNewBarcodes : barcodesListNew) {
                if (!barcodesListOld.contains(barcodesListNewBarcodes)) {
                    Kit oldIdKitOfBarcodesListNewBarcodes = barcodesListNewBarcodes.getIdKit();
                    barcodesListNewBarcodes.setIdKit(kit);
                    barcodesListNewBarcodes = em.merge(barcodesListNewBarcodes);
                    if (oldIdKitOfBarcodesListNewBarcodes != null && !oldIdKitOfBarcodesListNewBarcodes.equals(kit)) {
                        oldIdKitOfBarcodesListNewBarcodes.getBarcodesList().remove(barcodesListNewBarcodes);
                        oldIdKitOfBarcodesListNewBarcodes = em.merge(oldIdKitOfBarcodesListNewBarcodes);
                    }
                }
            }
            for (PlataformLinkKit plataformLinkKitListNewPlataformLinkKit : plataformLinkKitListNew) {
                if (!plataformLinkKitListOld.contains(plataformLinkKitListNewPlataformLinkKit)) {
                    Kit oldKitOfPlataformLinkKitListNewPlataformLinkKit = plataformLinkKitListNewPlataformLinkKit.getKit();
                    plataformLinkKitListNewPlataformLinkKit.setKit(kit);
                    plataformLinkKitListNewPlataformLinkKit = em.merge(plataformLinkKitListNewPlataformLinkKit);
                    if (oldKitOfPlataformLinkKitListNewPlataformLinkKit != null && !oldKitOfPlataformLinkKitListNewPlataformLinkKit.equals(kit)) {
                        oldKitOfPlataformLinkKitListNewPlataformLinkKit.getPlataformLinkKitList().remove(plataformLinkKitListNewPlataformLinkKit);
                        oldKitOfPlataformLinkKitListNewPlataformLinkKit = em.merge(oldKitOfPlataformLinkKitListNewPlataformLinkKit);
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
                Integer id = kit.getIdKit();
                if (findKit(id) == null) {
                    throw new NonexistentEntityException("The kit with id " + id + " no longer exists.");
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
            Kit kit;
            try {
                kit = em.getReference(Kit.class, id);
                kit.getIdKit();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kit with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PlataformLinkKit> plataformLinkKitListOrphanCheck = kit.getPlataformLinkKitList();
            for (PlataformLinkKit plataformLinkKitListOrphanCheckPlataformLinkKit : plataformLinkKitListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Kit (" + kit + ") cannot be destroyed since the PlataformLinkKit " + plataformLinkKitListOrphanCheckPlataformLinkKit + " in its plataformLinkKitList field has a non-nullable kit field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Barcodes> barcodesList = kit.getBarcodesList();
            for (Barcodes barcodesListBarcodes : barcodesList) {
                barcodesListBarcodes.setIdKit(null);
                barcodesListBarcodes = em.merge(barcodesListBarcodes);
            }
            em.remove(kit);
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

    public List<Kit> findKitEntities() {
        return findKitEntities(true, -1, -1);
    }

    public List<Kit> findKitEntities(int maxResults, int firstResult) {
        return findKitEntities(false, maxResults, firstResult);
    }

    private List<Kit> findKitEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kit.class));
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

    public Kit findKit(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kit.class, id);
        } finally {
            em.close();
        }
    }

    public int getKitCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kit> rt = cq.from(Kit.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void create_kit() {

        try {
            //current.setIdKit(id_kit); // AUTOINCREMENT
            //current.setKitName(kit_name);
            //current.setNote(note);
            
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("plataform", current);
            //current = new Plataform();
            
            getEjbKitFacade().create(current);
            
             //FacesContext.getCurrentInstance().addMessage("msgSuccess", new FacesMessage("Registro exitoso", "Muestra " + current.getSampleName() + " Registrada"));
            FacesContext.getCurrentInstance().addMessage("msgSuccess", new FacesMessage("Se ha reguistrado una nueva plataforma, actualice la lista de kit que pueden trabajar con esta plataforma"));

        } catch (Exception e) {
           JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            e.printStackTrace();

        }             
    }
    
}
