/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entities.Library;

/**
 *
 * @author omarflores
 */
@Stateless
public class LibraryFacade extends AbstractFacade<Library> {

    @PersistenceContext(unitName = "SISBIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LibraryFacade() {
        super(Library.class);
    }
    
}
