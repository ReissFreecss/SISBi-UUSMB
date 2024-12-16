/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import jpa.entities.PlataformLinkKit;
import jpa.session.PlataformLinkKitFacade;
import jpa.entities.Plataform;
import jpa.entities.Kit;
import jpa.entities.PlataformLinkKitPK;
import jsf.util.JsfUtil;

/**
 *
 * @author leslie
 */

@Named("PlataformLinkKitJpaController")
@SessionScoped
public class PlataformLinkKitJpaController implements Serializable {
    
    private PlataformLinkKit current;
    @EJB
    private jpa.session.PlataformLinkKitFacade ejbFacade;
    @EJB
    private jpa.session.PlataformFacade PlatafromFacade;
    @EJB
    private jpa.session.KitFacade KitFacade;
    private int selectedItemIndex;
    private int idplataforma=0;
    private List<String> listaPlats;
    
       public PlataformLinkKitJpaController() {
    }

    public List<String> getListaPlats() {
        return listaPlats;
    }

    public void setListaPlats(List<String> listaPlats) {
        this.listaPlats = listaPlats;
    }
    
    public PlataformLinkKit getSelected() {
        if (current == null) {
            current = new PlataformLinkKit();
            current.setPlataformLinkKitPK(new jpa.entities.PlataformLinkKitPK()); 
            selectedItemIndex = -1;
        }
        return current;
    }
    
     private PlataformLinkKitFacade getFacade() {
        return ejbFacade;
    }
     
     /*public void asociaplatkit() {
        try {
            if (idplataforma != 0) {
                current = new PlataformLinkKit();
                current.setPlataformLinkKitPK(new jpa.entities.PlataformLinkKitPK()); 

                FacesContext context = FacesContext.getCurrentInstance();
                Plataform plat = (Plataform) context.getExternalContext().getSessionMap().get("plataform");

                current.getPlataformLinkKitPK().setIdPlataform(plat.getIdPlataform()); 

                List<Kit> lista = ejbFacade.findAllKits();
                int KitId = 0;
                for (Kit kitseq : lista) {
                    if (kitseq.toString().equals(idplataforma)) {
                        KitId = kitseq.getIdKit();
                    }
                }
                current.getPlataformLinkKitPK().setIdKit(KitId); 
                getFacade().create(current);
                JsfUtil.addSuccessMessage("Kits y plataforma asociados correctamente" );
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }*/
     

     
     
     public void createPlaKitLink(){
         try {
             
            FacesContext context = FacesContext.getCurrentInstance();
            Kit kit_now=(Kit) context.getExternalContext().getSessionMap().get("kit");

             //encuentra todos los kits registrados
            List <Plataform> root= PlatafromFacade.findAllPlataforms();
            //compara la lista de kits elegidos por el usuario y  compara los id con los ya registrads,
            // si estan los agrega a una nueva lista K que nos servira para ligarlos a la platafroma en la tabla link
            List <Plataform> listP=new ArrayList<>();
            for (String platselect :listaPlats) {
                for (Plataform pregis : root) {
                    if (platselect.equals(pregis.getPlataformName())) {
                        listP.add(pregis);
                    }
                }
            }
            //para cada elemento de la lista P (plataforms) toma su id y ligalo con el identificador del kit en la tabla link
            for (Plataform linkadd : listP) {
            current = new PlataformLinkKit();
            current.setPlataformLinkKitPK(new jpa.entities.PlataformLinkKitPK());                                 
            current.getPlataformLinkKitPK().setIdPlataform(linkadd.getIdPlataform()); 
            current.getPlataformLinkKitPK().setIdKit(kit_now.getIdKit()); 
            
            //crea la entidad con los dtaos que le pasamos atraves del obj current
            getFacade().create(current);             
            }
            
            System.out.println("salio del for linkadd");
        } catch (Exception e) {
            System.out.println("--------> "+e);          
        }              
    }

}
