/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entities.BalancePago;

/**
 *
 * @author aaron
 */
@Stateless
public class BalancePagoFacade extends AbstractFacade<BalancePago> {

    @PersistenceContext(unitName = "SISBIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BalancePagoFacade() {
        super(BalancePago.class);
    }
    
}
