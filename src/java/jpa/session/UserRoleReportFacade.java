package jpa.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entities.UserRoleReport;

/**
 *
 * @author omarflores
 */
@Stateless
public class UserRoleReportFacade extends AbstractFacade<UserRoleReport> {

    @PersistenceContext(unitName = "SISBIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserRoleReportFacade() {
        super(UserRoleReport.class);
    }
    
}