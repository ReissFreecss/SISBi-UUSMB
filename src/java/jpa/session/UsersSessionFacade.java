/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import jpa.entities.Project;
import jpa.entities.Users;

/**
 *
 * @author aaron
 */
@Stateless(name="ejbFacade")
public class UsersSessionFacade extends AbstractFacade<Users> implements UsersFacadeSessionLocal{

    @PersistenceContext(unitName = "SISBIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersSessionFacade() {
        super(Users.class);
    }
    
   
    @Override
    public Users iniciarSesion(Users us){
        Users usuario=null;
        String consulta,consulta2;
        try {
            consulta="SELECT u FROM Users u WHERE u.userName=?1 and u.password=?2";
            consulta2="SELECT u FROM Users u WHERE u.email=?3 and u.password=?2";
            Query query = em.createQuery(consulta);
            query.setParameter(1,us.getUserName());
            query.setParameter(2,us.getPassword());
            
            Query query2 =em.createQuery(consulta2);
            query2.setParameter(2,us.getPassword());
            query2.setParameter(3,us.getUserName());
            
            List<Users> lista=query.getResultList();
            List<Users> lista2=query2.getResultList();
            
            System.out.println(us.getUserName());
            System.out.println(lista.size()+"\n"+lista2.size());
            
            if (!lista.isEmpty()) {
                usuario=lista.get(0);
                
            }
            if (!lista2.isEmpty()) {
                usuario=lista2.get(0);
            }
        } catch (Exception e) {
            throw e; 
        }
    return usuario;
    
    }
    
 
    @Override
    public List<Project> projectUser(Users us) {

        String consulta;
        try {
            consulta="SELECT p.id_project, p.project_name, p.project_description, p.request_date, p.status, p.quotation_number, "
                    + "p.bill_number FROM user_project_link up, users u, project p where u.id_user=up.id_user and p.id_project=up.id_project "
                    + "and u.user_name=?1;";
            Query query = em.createQuery(consulta);
            
            query.setParameter(1,us.getUserName());
                   
            List<Project> lista=query.getResultList();
            
           return lista;
    
    }catch(Exception e){
        
        
    }
    return null;
        
}
}
