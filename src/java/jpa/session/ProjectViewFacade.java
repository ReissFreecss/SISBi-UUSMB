/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.entities.Project;
import jpa.entities.ProjectView;
import jpa.entities.Users;

/**
 *
 * @author aaron
 */
@Stateless
public class ProjectViewFacade extends AbstractFacade<ProjectView>{

    @PersistenceContext(unitName = "SISBIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectViewFacade() {
        super(ProjectView.class);
    }
    
    
}
