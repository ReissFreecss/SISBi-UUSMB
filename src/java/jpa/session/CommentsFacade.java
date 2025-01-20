/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import java.util.List;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.entities.Comments;

/**
 *
 * @author aaron
 */
@Stateless
public class CommentsFacade extends AbstractFacade<Comments> {

    @PersistenceContext(unitName = "SISBIPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @PreDestroy
    public void destruct() {
        em.close();
    }

    public CommentsFacade() {
        super(Comments.class);
    }

    public Comments findCommentByIdTypeUserAndType(String idType, String userName, String type) {
        try {
            return em.createNamedQuery("Comments.findByIdTypeAndUserNameAndType", Comments.class)
                    .setParameter("idType", idType)
                    .setParameter("userName", userName)
                    .setParameter("type", type)
                    .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null; // No existe un comentario que cumpla los criterios.
        }
    }

    void corregirComentariosErroneos() {
        List<Comments> comentariosErroneos = em.createQuery(
                "SELECT c FROM Comments c WHERE c.comment LIKE '%En Analisis Bioinformatico a - En Analisis Bioinformatico -%'",
                Comments.class
        ).getResultList();

        for (Comments comentario : comentariosErroneos) {
            String correctedComment = "Se cambia el estatus de En Analisis Bioinformatico a - En Analisis Bioinformatico";
            comentario.setComment(correctedComment);
            em.merge(comentario);
        }
    }

}
