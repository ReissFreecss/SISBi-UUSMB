/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import jpa.entities.Sample;

/**
 *
 * @author aaron
 */
@Stateless
public class SampleFacade extends AbstractFacade<Sample> {

    @PersistenceContext(unitName = "SISBIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SampleFacade() {
        super(Sample.class);
    }
    
    public Boolean sampleUpdateRealPerformance(String idProject, int idSample, int performance ) {        
        //  javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Sample.class);
        //  javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Sample.class);        
        
        /*EntityManager model = getEntityManager();
        CriteriaBuilder cb = model.getCriteriaBuilder();
        CriteriaUpdate<Sample> criteriaUpdate = cb.createCriteriaUpdate(Sample.class);
        Root<Sample> sampleRoot = criteriaUpdate.from(Sample.class);
        criteriaUpdate.where(cb.and(cb.equal( sampleRoot.get("idProject") , idProject), cb.equal(sampleRoot.get("idSample"), idSample) ));
        criteriaUpdate.set("realPerformance", performance);
        
        javax.persistence.Query q = model.createQuery(criteriaUpdate);
        */
        
        //  int updated = q.executeUpdate();
        
        //  return updated > 0;
        
        /*
        String sql_performance = "SELECT real_performance from sample WHERE id_project=" + "'" + idProject + "' AND  id_sample = "+ idSample+";";
        javax.persistence.Query qRealPerformance = getEntityManager().createNativeQuery(sql_performance, Sample.class);
        Sample sample = (Sample) qRealPerformance.getSingleResult();
        */        
        String UpdateRealPerformance;
        
        /*
        if(sample.getRealPerformance() == null ){             
          UpdateRealPerformance = performance; 
        } else {            
           UpdateRealPerformance = " CAST(real_performance as float) +" + performance+" )";
        }
        */       
        //  25/02/2025  Juan Antonio Villalba Luna
        //  String sql = "update sample set real_performance = ((CASE WHEN real_performance IS NULL THEN 0::varchar ELSE real_performance END)::int + "+ performance +")::varchar WHERE id_project='"+idProject+"' AND  id_sample ="+idSample+";";  // OK 25/02/2025
        //  String sql = "Update sample set real_performance = ((COALESCE(real_performance,0::varchar))::int + performance)::varchar WHERE id_project=" + "'" + idProject + "' AND  id_sample = "+ idSample+";";
        
        String sql = "update sample "+ 
                     "set real_performance = ((CASE "
                        + "WHEN real_performance IS NULL THEN 0::varchar "
                        + "WHEN real_performance~E'^\\\\d+$' = 'f' THEN 0::varchar "
                        + "ELSE real_performance "
                        + "END)::int + "+ performance +")::varchar WHERE id_project='"+idProject+"' AND  id_sample ="+idSample+";";  // OK 25/02/2025
        
        int executeUpdate = 0;
        try{
            javax.persistence.Query q = getEntityManager().createNativeQuery(sql);
            
            executeUpdate = q.executeUpdate();
        } catch(EJBException e) {
            System.out.print(e);
        }
        
        return executeUpdate > 0;
    }
    
    public Sample sampleByIdProjectIdSample(String idProject, int idSample) {        
        String sql = "SELECT * FROM sample WHERE id_project=" + "'" + idProject + "' AND  id_sample = "+ idSample +" order by id_sample";
        //  javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Sample.class);
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Sample.class);
        return (Sample) q.getSingleResult();
    }
}
