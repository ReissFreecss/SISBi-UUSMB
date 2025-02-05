/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import jpa.entities.BalancePago;
import jpa.entities.BalancePago_;
import jpa.entities.Barcodes;
import jpa.entities.BarcodesCons;
import jpa.entities.Barcodes_;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.BioinformaticAnalysisSampleLink;
import jpa.entities.BioinformaticAnalysis_;
import jpa.entities.BioinformaticsReports;
import jpa.entities.BioinformaticsReports_;
import jpa.entities.Comments;
import jpa.entities.Cotizacion;
import jpa.entities.Cotizacion_;
import jpa.entities.Dependency;
import jpa.entities.Dependency_;
import jpa.entities.FieldReport;
import jpa.entities.Files;
//import jpa.entities.Files_;
import jpa.entities.Genome;
import jpa.entities.Genome_;
import jpa.entities.Kit;
import jpa.entities.Kit_;
import jpa.entities.Library;
import jpa.entities.LibraryRunLink;
import jpa.entities.LibraryRunLink_;
import jpa.entities.Library_;
import jpa.entities.Pago;
import jpa.entities.Pago_;
import jpa.entities.Plataform;
import jpa.entities.PlataformLinkKit;
import jpa.entities.Plataform_;
import jpa.entities.Project;
import jpa.entities.Project_;
import jpa.entities.ProyCotizaFacPagoLink;
import jpa.entities.QualityReports;
import jpa.entities.QualityReports_;
import jpa.entities.ReportProject;
import jpa.entities.Run;
import jpa.entities.Run_;
import jpa.entities.Sample;
import jpa.entities.SampleDetails;
import jpa.entities.SampleLibraryLink;
import jpa.entities.SampleLibraryLink_;
import jpa.entities.SampleReportProject;
import jpa.entities.Sample_;
import jpa.entities.UserProjectLink;
import jpa.entities.UserProjectLink_;
import jpa.entities.UserRole;
import jpa.entities.UserRoleReport;
import jpa.entities.UserRole_;
import jpa.entities.UserSample;
import jpa.entities.UserSample_;
import jpa.entities.Users;
import jpa.entities.Users_;
import jsf.util.JsfUtil;

/**
 *
 * @author aaron
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void createComment(T entity) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (!constraintViolations.isEmpty()) {
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());

                JsfUtil.addErrorMessage(cv.getRootBeanClass().getSimpleName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
            }
        } else {
            getEntityManager().persist(entity);
        }
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void create2(T entity) {
        EntityTransaction ut = getEntityManager().getTransaction();
        ut.begin();
        getEntityManager().persist(entity);
        ut.commit();
    }

    public void createMany(List<T> list) {

        for (T t : list) {
            getEntityManager().persist(t);
        }
    }

    public void edit(T entity) {
        try {
            getEntityManager().merge(entity);
        } catch (ConstraintViolationException e) {
            for (ConstraintViolation actual : e.getConstraintViolations()) {
                System.out.println(actual.toString());
            }
        }
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<Project> findProjectByIdRange(int[] range, String id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Project> cq = cb.createQuery(Project.class);
        Root<Project> p = cq.from(Project.class);
        cq.select(p);
        cq.where(cb.like(
                cb.lower(p.get("idProject")),
                "%" + id.toLowerCase() + "%"));

        javax.persistence.Query q = getEntityManager().createQuery(cq);
        List<Project> proj = q.getResultList();

        if (proj.isEmpty()) {

            cq.select(p);
            cq.where(cb.like(
                    cb.lower(p.get("projectName")),
                    "%" + id.toLowerCase() + "%"));

            q = getEntityManager().createQuery(cq);
            proj = q.getResultList();

        }
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);

        return proj;

    }

    public List<Project> findProjectById(String id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Project> cq = cb.createQuery(Project.class);
        Root<Project> p = cq.from(Project.class);
        cq.select(p);
        cq.where(cb.like(
                cb.lower(p.get("idProject")),
                "%" + id.toLowerCase() + "%"));

        javax.persistence.Query q = getEntityManager().createQuery(cq);
        List<Project> proj = q.getResultList();

        if (proj.isEmpty()) {

            cq.select(p);
            cq.where(cb.like(
                    cb.lower(p.get("projectName")),
                    "%" + id.toLowerCase() + "%"));

            q = getEntityManager().createQuery(cq);
            proj = q.getResultList();

        }

        return proj;

    }

    public List<Users> findUserByEmail(String email) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> u = cq.from(Users.class);
        cq.select(u);
        cq.where(cb.equal(u.get("email"), email));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    // Método para localizar dependencias en base de datos Institucion, Nombre, Localizacion
    public List<Dependency> findInstitutionByNameAndLocationAndDependencyName(String institution, String location, String dependencyName) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Dependency> cq = cb.createQuery(Dependency.class);
        Root<Dependency> root = cq.from(Dependency.class);

        Predicate institutionPredicate = cb.equal(cb.upper(root.get("institution")), institution.toUpperCase());
        Predicate locationPredicate = location != null && !location.isEmpty()
                ? cb.equal(cb.upper(root.get("location")), location.toUpperCase())
                : cb.isNull(root.get("location"));
        Predicate dependencyNamePredicate = cb.equal(cb.upper(root.get("dependencyName")), dependencyName.toUpperCase());

        cq.select(root).where(cb.and(institutionPredicate, locationPredicate, dependencyNamePredicate));

        return getEntityManager().createQuery(cq).getResultList();
    }

    // Método para localizar dependencias en base de datos Institucion, Nombre
    public List<Object[]> findAllInstitutionsAndDependencies() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<Dependency> root = cq.from(Dependency.class);

        // Seleccionar solo las columnas institution y dependencyName
        cq.multiselect(root.get("institution"), root.get("dependencyName"));

        // Ejecutar la consulta y devolver la lista de resultados
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<Comments> findCommentByType(String type) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Comments> cq = cb.createQuery(Comments.class);
        Root<Comments> com = cq.from(Comments.class);
        cq.select(com);
        cq.where(cb.equal(com.get("type"), type));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<T> findAll() {

        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));

        return getEntityManager().createQuery(cq).getResultList();

    }

    //-------- búsqueda de comentarios por id de proyecto
    public List<Comments> commentsByProject(String idProject) {
        String sql = "select * from comments where type = 'Project' and id_type=" + "'" + idProject + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    //-------- búsqueda de comentarios por id de muestra
    public List<Comments> commentsByIdSample(Integer idSample) {
        String sql = "select * from comments where type = 'Sample' and id_type=" + "'" + idSample.toString() + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    public List<Comments> non_automated_commentsByIdSample(Integer idSample) {
        /* para que no sean comentarios tócnicos, no debe empezar con:
        Estatus cambia de
        Esta muestra fue dada de alta por el usuario:
        El usuario X ha cambiado el estatus del proyecto a
        Se cambia el estatus de
        Subida del reporte de calidad llenado por
        La aceptacion de la muestra cambio a
        La calidad de la muestra cambio a
         */
        String rejected_cases = "comment LIKE 'Esta muestra fue dada de alta por el usuario:%' or comment LIKE 'El usuario % ha cambiado el estatus del proyecto a %' or comment LIKE 'Se cambia el estatus de %' or comment LIKE 'Subida del reporte de calidad llenado por %' or comment LIKE 'La aceptacion de la muestra cambio a %' or comment LIKE 'La calidad de la muestra cambio a %'";
        String sql = "select * from comments where type = 'Sample' and id_type='" + idSample.toString() + "' and user_name!='SISBI' and NOT (" + rejected_cases + ");";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    public List<Comments> non_SISBI_commentsByProject(String idProject) {
        String sql = "select * from comments where type = 'Project' and user_name!='SISBI' and id_type=" + "'" + idProject + "' ";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    public List<Comments> SISBI_commentsByProject(String idProject) {
        String sql = "select * from comments where type = 'Project' and user_name='SISBI' and id_type=" + "'" + idProject + "' ";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    public List<UserProjectLink> findRoles(Project Proj) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserProjectLink> cq = cb.createQuery(UserProjectLink.class);
        Root<UserProjectLink> roles = cq.from(UserProjectLink.class);
        cq.select(roles);
        cq.where(cb.equal(roles.get(UserProjectLink_.project), Proj));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();

    }

    public List<Genome> findAllGenomes() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Genome> cq = cb.createQuery(Genome.class);
        Root<Genome> gen = cq.from(Genome.class);
        cq.select(gen);
        cq.orderBy(cb.asc(gen.get(Genome_.genomeName)));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    //leslie 05 septiembre
    public List<Kit> findAllKits() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Kit> cq = cb.createQuery(Kit.class);
        Root<Kit> kitseq = cq.from(Kit.class);
        cq.select(kitseq);
        cq.orderBy(cb.asc(kitseq.get(Kit_.kitName)));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<T> findAllDependencies(int value) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Dependency> cq = cb.createQuery(Dependency.class);
        Root<Dependency> dep = cq.from(Dependency.class);
        cq.select(dep);
        if (value == 1) {
            cq.orderBy(cb.asc(dep.get(Dependency_.dependencyName)));
        } else {
            cq.orderBy(cb.asc(dep.get(Dependency_.institution)));
        }

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Users> findAllUsers() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> dep = cq.from(Users.class);
        cq.select(dep);
        cq.orderBy(cb.asc(dep.get(Users_.userName)));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);

        return q.getResultList();
    }

    public List<T> findRangeUsers(int[] range, Users us) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();

        Root<T> c = cq.from(Users.class);
        cq.select(c);
        cq.where(cb.equal(c.get("idUser"), us.getIdUser()));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);

        return q.getResultList();

    }

    public List<UserRole> findRangeUsersProj(String pro) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();

        Root<UserRole> c = cq.from(UserRole.class);
        cq.select(c);
        cq.where(cb.equal(c.get(UserRole_.idProject), pro));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<T> findSampProj(int[] range, Project pj) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);

        Root<Sample> sampleRoot = cq.from(Sample.class);

        Join<Sample, Project> sampProj = sampleRoot.join(Sample_.idProject);

        cq.select(sampleRoot);
        cq.where(cb.equal(sampProj.get("idProject"), pj.getIdProject()));

        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);

        return q.getResultList();

    }

    public List<T> findSampProj2(Project pj) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);

        Root<Sample> sampleRoot = cq.from(Sample.class);

        Join<Sample, Project> sampProj = sampleRoot.join(Sample_.idProject);

        cq.select(sampleRoot);
        cq.where(cb.equal(sampProj.get("idProject"), pj.getIdProject()));
        cq.orderBy(cb.asc(sampleRoot.get(Sample_.idSample)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Sample> sampleByProject(String idProject) {
        //javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        String sql = "select * from sample where id_project=" + "'" + idProject + "' order by id_sample";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Sample.class);
        return q.getResultList();
    }

    public List<Sample> findSamplesByProject(Project pj) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);

        Root<Sample> sampleRoot = cq.from(Sample.class);

        Join<Sample, Project> sampProj = sampleRoot.join(Sample_.idProject);

        cq.select(sampleRoot);
        cq.where(cb.equal(sampProj.get("idProject"), pj.getIdProject()));
        cq.orderBy(cb.asc(sampleRoot.get(Sample_.idSample)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Sample> findSamplesByProject(String pj) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);

        Root<Sample> sampleRoot = cq.from(Sample.class);

        Join<Sample, Project> sampProj = sampleRoot.join(Sample_.idProject);

        cq.select(sampleRoot);
        cq.where(cb.equal(sampProj.get("idProject"), pj));
        cq.orderBy(cb.asc(sampleRoot.get(Sample_.sampleName)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Sample> findSampleByRange(Integer rg1, Integer rg2) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
        Root<Sample> sampleRoot = cq.from(Sample.class);
        //id_Sample>= rg1
        Predicate firstID = cb.greaterThanOrEqualTo(sampleRoot.get(Sample_.idSample), rg1);
        //id_Sample<= rg2
        Predicate SecondID = cb.lessThanOrEqualTo(sampleRoot.get(Sample_.idSample), rg2);
        //project_id=project
        //Predicate proj=cb.equal(sampleRoot.get(Sample_.idProject), project);
        //id_Sample>= rg1 and id_sample <=rg2
        Predicate result1 = cb.and(firstID, SecondID);
        //  true                                        true
        //(id_Sample>= rg1 and id_sample <=rg2) and project_id=project
        //Predicate result2=cb.and(result1,proj);

        /*
        Select * from samples where id_Sample>= rg1 and id_sample <=rg2 and project_id=project;
         */
        cq.select(sampleRoot);
        cq.where(result1);
        //Sample
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Library> LibrariesBySample(Sample sam) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Library> cq = cb.createQuery(Library.class);

        Root<Library> libraryRoot = cq.from(Library.class);
        //Join<Library, SampleLibraryLink> sll = libraryRoot.join(Library_.sampleLibraryLinkCollection);
        Join<Library, SampleLibraryLink> sll = libraryRoot.join(Library_.sampleLibraryList);

        cq.select(libraryRoot);
        cq.where(cb.equal(sll.get("sample"), sam));
        cq.orderBy(cb.desc(libraryRoot.get(Library_.preparationDate)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Sample> findSampleById(Integer id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
        Root<Sample> sampleRoot = cq.from(Sample.class);
        Predicate idSam = cb.equal(sampleRoot.get(Sample_.idSample), id);
        cq.select(sampleRoot);
        cq.where(idSam);
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<SampleLibraryLink> findSamplesByID(Library id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SampleLibraryLink> cq = cb.createQuery(SampleLibraryLink.class);

        Root<SampleLibraryLink> libraryRoot = cq.from(SampleLibraryLink.class);
        // Join<Library, SampleLibraryLink> sll = libraryRoot.join(Library_.sampleLibraryLinkCollection);

        cq.select(libraryRoot);
        cq.where(cb.equal(libraryRoot.get(SampleLibraryLink_.library), id));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Sample> findSamples(String pj) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);

        Root<Sample> sampleRoot = cq.from(Sample.class);

        Join<Sample, Project> sampProj = sampleRoot.join(Sample_.idProject);

        cq.select(sampleRoot);
        cq.where(cb.equal(sampProj.get("idProject"), pj));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Sample> findSamplebyReports(int id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);

        Root<Sample> sampleRoot = cq.from(Sample.class);

        //Join<Sample, Project> sampProj = sampleRoot.join(Sample_.idProject);
        cq.select(sampleRoot);
        cq.where(cb.equal(sampleRoot.get(Sample_.idSample), id));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<SampleDetails> findSampleDetails() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SampleDetails> cq = cb.createQuery(SampleDetails.class);
        Root<SampleDetails> gen = cq.from(SampleDetails.class);
        cq.select(gen);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Project> findRangeProUsers(int[] range, Users us) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Project> cq = cb.createQuery(Project.class);

        Root<Project> projectRoot = cq.from(Project.class);

        Join<Project, UserProjectLink> upl = projectRoot.join(Project_.userProjectLinkCollection);

        cq.select(projectRoot);
        cq.where(cb.equal(upl.get("users"), us));

        cq.orderBy(cb.asc(projectRoot.get(Project_.requestDate)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public List<Project> findProUsers(Users us) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Project> cq = cb.createQuery(Project.class);

        Root<Project> projectRoot = cq.from(Project.class);

        Join<Project, UserProjectLink> upl = projectRoot.join(Project_.userProjectLinkCollection);

        cq.select(projectRoot);
        cq.where(cb.equal(upl.get("users"), us));

        cq.orderBy(cb.desc(projectRoot.get(Project_.requestDate)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<BioinformaticAnalysis> findBioanSample(Sample sm) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<BioinformaticAnalysis> cq = cb.createQuery(BioinformaticAnalysis.class);

        Root<BioinformaticAnalysis> BionAnRoot = cq.from(BioinformaticAnalysis.class);

        Join<BioinformaticAnalysis, BioinformaticAnalysisSampleLink> upl = BionAnRoot.join(BioinformaticAnalysis_.bioinformaticAnalysisSampleLinkCollection);

        cq.select(BionAnRoot);
        cq.where(cb.equal(upl.get("sample"), sm));

        cq.orderBy(cb.asc(BionAnRoot.get(BioinformaticAnalysis_.type)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Sample> findLibSamples(boolean type) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
        Root<Sample> sampleRoot = cq.from(Sample.class);
        Predicate restrict;
        if (type) {
            restrict = cb.equal(sampleRoot.get("status"), "En construccion de biblioteca");

        } else {
            restrict = cb.equal(sampleRoot.get("status"), "Para resecuenciar");
        }

        cq.select(sampleRoot);
        cq.where(restrict);
        cq.orderBy(cb.asc(sampleRoot.get(Sample_.receptionDate)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        List<Sample> samList = q.getResultList();
        return samList;

    }

    public List<UserSample> findUserSampByName(String userName, Date fechaInicio, Date fechaTermino, String estatus, Integer id, String NomMuestra, String NomIdTubo) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserSample> cq = cb.createQuery(UserSample.class);
        Root<UserSample> u = cq.from(UserSample.class);

        javax.persistence.Query q = getEntityManager().createQuery(cq);
        Predicate restrictUserName = cb.equal(u.get("userName"), userName);
        Predicate predicFechas = cb.between(u.get("receptionDate"), fechaInicio, fechaTermino);
        // Predicate restrictFechas = cb.or(predicFechas, u.get("receptionDate").isNull()); original
        Predicate restrictFechas = cb.or(predicFechas, u.get("receptionDate").isNotNull());
        Predicate restrictStatus = cb.equal(u.get("status"), estatus);
        // Predicate restrictNomMuestra=cb.like(u.get("sampleName"), "%"+NomMuestra); original cambio leslie 30/11/23
        Predicate restrictNomMuestra = cb.like(cb.lower(u.get("sampleName")), "%" + NomMuestra.toLowerCase() + "%");
        // Predicate restrictNomIdTubo=cb.equal(u.get("idTube"), NomIdTubo); original cambio leslie 30/11/23
        Predicate restrictNomIdTubo = cb.like(cb.lower(u.get("idTube")), "%" + NomIdTubo.toLowerCase() + "%");

        Predicate restrictID = cb.equal(u.get(UserSample_.idSample), id);

        cq.select(u);
        int var = 17;
        /* System.out.println("El estatus es: "+estatus+" y su predicate es: "+restrictStatus);
        System.out.println("El username es: "+userName+" y su predicate es: "+restrictUserName);
          System.out.println("El nombre de la muestra es: "+NomMuestra+" y el predicate es: "+restrictNomMuestra);*/

        if (userName.equals("---")) {
            var = 1;
        }

        if (estatus.equals("---")) {
            var = 2;
        }

        if (NomMuestra.equals("")) {
            var = 3;
        }

        if (NomIdTubo.equals("")) {
            var = 4;
        }

        if (estatus.equals("---") && userName.equals("---") && NomMuestra.equals("") && NomIdTubo.equals("")) {
            var = 5;
        }

        if (estatus.equals("---") && userName.equals("---") && NomMuestra.equals("") && !NomIdTubo.equals("")) {
            var = 6;
        }

        if (estatus.equals("---") && userName.equals("---") && NomIdTubo.equals("") && !NomMuestra.equals("")) {
            var = 7;
        }

        if (estatus.equals("---") && NomIdTubo.equals("") && NomMuestra.equals("") && !userName.equals("---")) {
            var = 8;
        }

        if (NomIdTubo.equals("") && userName.equals("---") && NomMuestra.equals("") && !estatus.equals("---")) {
            var = 9;
        }

        if (estatus.equals("---") && NomIdTubo.equals("") && !userName.equals("---") && !NomMuestra.equals("")) {
            var = 10;
        }

        if (estatus.equals("---") && NomMuestra.equals("") && !NomIdTubo.equals("") && !userName.equals("---")) {
            var = 11;
        }

        if (estatus.equals("---") && userName.equals("---") && !NomIdTubo.equals("") && !NomMuestra.equals("")) {
            var = 12;
        }

        if (NomIdTubo.equals("") && NomMuestra.equals("") && !estatus.equals("---") && !userName.equals("---")) {
            var = 13;
        }

        if (NomIdTubo.equals("") && userName.equals("---") && !estatus.equals("---") && !NomMuestra.equals("")) {
            var = 14;
        }

        if (NomMuestra.equals("") && userName.equals("---") && !estatus.equals("---") && !NomIdTubo.equals("")) {
            var = 15;
        }

        if (id != null && estatus.equals("---") && userName.equals("---") && NomIdTubo.equals("") && NomMuestra.equals("")) {
            var = 16;
        }

        /*Empiezan las nuevas condicionales*/
        //System.out.println("Fecha inicio: "+ fechaInicio+ " - "+"Fecha termino: "+fechaTermino );
        System.out.println("La opcion es: " + var);

        switch (var) {
            case 17:
                cq.where(cb.and(cb.and(cb.and(restrictUserName, restrictFechas), restrictStatus, restrictNomMuestra), restrictNomIdTubo));
                q = getEntityManager().createQuery(cq);
                //cq.distinct(true);
                break;
            case 1:
                cq.where(cb.and(cb.and(restrictFechas, restrictStatus), restrictNomIdTubo, restrictNomMuestra));
                q = getEntityManager().createQuery(cq);
                //cq.distinct(true);
                break;
            case 2:
                cq.where(cb.and(cb.and(restrictFechas, restrictUserName), restrictNomIdTubo, restrictNomMuestra));
                q = getEntityManager().createQuery(cq);
                //cq.distinct(true);
                break;
            case 3:
                cq.where(cb.and(cb.and(restrictFechas, restrictUserName), restrictNomIdTubo, restrictStatus));
                q = getEntityManager().createQuery(cq);
                //cq.distinct(true);
                break;
            case 4:
                cq.where(cb.and(cb.and(restrictFechas, restrictUserName), restrictNomMuestra, restrictStatus));
                q = getEntityManager().createQuery(cq);
                break;
            case 5:
                cq.where(restrictFechas);
                q = getEntityManager().createQuery(cq);
                //cq.distinct(true);
                break;
            case 6:
                cq.where(restrictFechas, restrictNomIdTubo);
                q = getEntityManager().createQuery(cq);
                break;
            case 7:
                cq.where(restrictFechas, restrictNomMuestra);
                q = getEntityManager().createQuery(cq);
                break;
            case 8:
                cq.where(restrictFechas, restrictUserName);
                q = getEntityManager().createQuery(cq);
                break;
            case 9:
                cq.where(restrictFechas, restrictStatus);
                q = getEntityManager().createQuery(cq);
                break;
            case 10:
                cq.where(cb.and(restrictFechas, restrictUserName), restrictNomMuestra);
                q = getEntityManager().createQuery(cq);
                break;
            case 11:
                cq.where(cb.and(restrictFechas, restrictUserName), restrictNomIdTubo);
                q = getEntityManager().createQuery(cq);
                break;
            case 12:
                cq.where(cb.and(restrictFechas, restrictNomMuestra), restrictNomIdTubo);
                q = getEntityManager().createQuery(cq);
                break;
            case 13:
                cq.where(cb.and(restrictFechas, restrictStatus), restrictUserName);
                q = getEntityManager().createQuery(cq);
                break;
            case 14:
                cq.where(cb.and(restrictFechas, restrictStatus), restrictNomMuestra);
                q = getEntityManager().createQuery(cq);
                break;
            case 15:
                cq.where(cb.and(restrictFechas, restrictStatus), restrictNomIdTubo);
                q = getEntityManager().createQuery(cq);
                break;
            case 16:
                cq.where(restrictID);
                q = getEntityManager().createQuery(cq).setMaxResults(1);
                break;
        }

        return q.getResultList();

    }

    public List<BioinformaticAnalysis> findBionformaticAnalysisBySample(Sample id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<BioinformaticAnalysis> cq = cb.createQuery(BioinformaticAnalysis.class);

        Root<BioinformaticAnalysis> BionAnRoot = cq.from(BioinformaticAnalysis.class);

        Join<BioinformaticAnalysis, BioinformaticAnalysisSampleLink> upl = BionAnRoot.join(BioinformaticAnalysis_.bioinformaticAnalysisSampleLinkCollection);
        Predicate notLC = cb.notEqual(BionAnRoot.get(BioinformaticAnalysis_.analysisName), "Limpieza por calidad");
        Predicate notLGC = cb.notEqual(BionAnRoot.get(BioinformaticAnalysis_.analysisName), "Limpieza de genoma contaminante");
        Predicate notAED = cb.notEqual(BionAnRoot.get(BioinformaticAnalysis_.analysisName), "Alineamiento vs genoma de referencia");
        Predicate notLA = cb.notEqual(BionAnRoot.get(BioinformaticAnalysis_.analysisName), "Limpieza de adaptador");
        Predicate sm = cb.equal(upl.get("sample"), id);
        //Predicate sm = cb.equal(upl.get("sample"), id.getIdSample());
        Predicate Results = cb.and(sm, notLC, notLGC, notAED, notLA);
        cq.select(BionAnRoot);
        cq.where(Results);
        cq.distinct(true);
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public BioinformaticAnalysis findBionformaticAnalysisByAnalysisId(int id) {
        //javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        TypedQuery<BioinformaticAnalysis> consultaBio = getEntityManager().createNamedQuery("BioinformaticAnalysis.findByIdAnalysis", BioinformaticAnalysis.class);
        consultaBio.setParameter("idAnalysis", id);
        List<BioinformaticAnalysis> results = consultaBio.getResultList();
        BioinformaticAnalysis gt = results.get(0);
        return gt;
    }

    /*
    public List<Barcodes> findBarcode(String position, String kit) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Barcodes> cq = cb.createQuery(Barcodes.class);
        Root<Barcodes> BarcodesRoot = cq.from(Barcodes.class);
        
        Predicate Restrict1
               // = cb.and(cb.equal(BarcodesRoot.get("tagType"), position), cb.equal(BarcodesRoot.get("kitName"), kit));
                = cb.and(cb.equal(BarcodesRoot.get("tagType"), position), cb.equal(BarcodesRoot.get("kitName"), kit));
        cq.select(BarcodesRoot);
        cq.where(Restrict1);
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }
     */
    //Nueva consulta para la nueva BD
    public List<Library> findLibraryByStatus() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Library> cq = cb.createQuery(Library.class);
        Root<Library> LibraryRoot = cq.from(Library.class);

        cq.where(cb.equal(LibraryRoot.get("preparationStatus"), "Construida"));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Library> findLibraryByKit(String kit) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Library> cq = cb.createQuery(Library.class);
        Root<Library> LibraryRoot = cq.from(Library.class);
        Predicate Restrict1 = cb.and(cb.equal(LibraryRoot.get("preparationStatus"), "Construida"));
        Join<Library, PlataformLinkKit> sll = LibraryRoot.join(Library_.plataformLinkKit);
        Predicate lb = cb.equal(sll.get("kit").get("kitName"), kit);
        Predicate results = cb.and(Restrict1, lb);

        cq.select(LibraryRoot);
        cq.where(results);
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Project> findProjectUserById(String id, Users us) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Project> cq = cb.createQuery(Project.class);

        Root<Project> projectRoot = cq.from(Project.class);

        Join<Project, UserProjectLink> upl = projectRoot.join(Project_.userProjectLinkCollection);

        cq.select(projectRoot);

        Predicate Restrict1
                = cb.and(cb.like(cb.lower(upl.get("project")), "%" + id.toLowerCase() + "%"),
                        cb.equal(upl.get("users"), us)
                );

        cq.select(projectRoot);
        cq.where(Restrict1);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        List<Project> proj = q.getResultList();

        if (proj.isEmpty()) {
            Predicate Restrict2
                    = cb.and(cb.like(cb.lower(upl.get("project")), "%" + id.toLowerCase() + "%"),
                            cb.equal(upl.get("idUser"), us)
                    );

            cq.select(projectRoot);
            cq.where(Restrict2);

            q = getEntityManager().createQuery(cq);
            proj = q.getResultList();

        }

        return proj;

    }

    public List<Library> getLibraries(Sample sam) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Library> cq = cb.createQuery(Library.class);

        Root<Library> libraryRoot = cq.from(Library.class);
        //Join<Library, SampleLibraryLink> sll = libraryRoot.join(Library_.sampleLibraryLinkCollection);
        Join<Library, SampleLibraryLink> sll = libraryRoot.join(Library_.sampleLibraryList);

        cq.select(libraryRoot);
        cq.where(cb.equal(sll.get("sample"), sam));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        //System.out.println("Se imprime la consulta nueva: " +q.getResultList());
        return q.getResultList();

    }

    //leslie obtener el id_sample de las bibliotecas construidas a traves de la tabla sample librarylink
    public List<Sample> findSamplebyIdLib(Library lib) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);

        Root<Sample> sampleRoot = cq.from(Sample.class);
        //sampleLibraryCollection;
        Join<Sample, SampleLibraryLink> sll = sampleRoot.join(Sample_.sampleLibraryCollection);

        cq.select(sampleRoot);
        cq.where(cb.equal(sll.get(SampleLibraryLink_.library), lib));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }
    //fin leslie

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    //fher
    public List<Cotizacion> findCotProject(Project id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Cotizacion> cq = cb.createQuery(Cotizacion.class);

        Root<Cotizacion> CotRoot = cq.from(Cotizacion.class);

        Join<Cotizacion, ProyCotizaFacPagoLink> jn = CotRoot.join(Cotizacion_.proyCotizaFacPagoLinkCollection);

        cq.select(CotRoot);
        cq.where(cb.equal(jn.get("idProject"), id));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }
///

    public List<Users> findResponsibleUsers() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);

        Root<Users> dep = cq.from(Users.class);
        Join<Users, UserProjectLink> jn = dep.join(Users_.userProjectLinkCollection);

        cq.select(dep);
        cq.where(cb.equal(jn.get("role"), "Responsable"));
        cq.orderBy(cb.asc(dep.get(Users_.userName)));
        cq.distinct(true);
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<BalancePago> findBalanceByUser(Users id, Integer us) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<BalancePago> cq = cb.createQuery(BalancePago.class);

        Root<BalancePago> BalanceRoot = cq.from(BalancePago.class);

        Join<BalancePago, Pago> jn = BalanceRoot.join(BalancePago_.pagoId, JoinType.LEFT);
        Predicate pr = cb.equal(jn.get("idUser"), id);
        Predicate pr2 = cb.equal(BalanceRoot.get("id_user"), us);
        Predicate pr3 = cb.or(pr, pr2);
        cq.select(BalanceRoot);
        cq.where(pr3);

        cq.orderBy(cb.asc(BalanceRoot.get(BalancePago_.idBalance)));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<BalancePago> findUserBalance(Users id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<BalancePago> cq = cb.createQuery(BalancePago.class);

        Root<BalancePago> BalanceRoot = cq.from(BalancePago.class);

        Join<BalancePago, Pago> jn = BalanceRoot.join(BalancePago_.pagoId);

        cq.select(BalanceRoot);
        cq.where(cb.equal(jn.get("idUser"), id));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<ProyCotizaFacPagoLink> findPayment(Project idProj) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<ProyCotizaFacPagoLink> cq = cb.createQuery(ProyCotizaFacPagoLink.class);

        Root<ProyCotizaFacPagoLink> payment = cq.from(ProyCotizaFacPagoLink.class);

        cq.select(payment);

        cq.where(cb.equal(payment.get("idProject"), idProj));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Pago> findPaymentByproject(Project idProject) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Pago> cq = cb.createQuery(Pago.class);

        Root<Pago> payment = cq.from(Pago.class);

        Join<Pago, ProyCotizaFacPagoLink> join = payment.join(Pago_.proyCotizaFacPagoLinkCollection);

        cq.select(payment);

        cq.where(cb.equal(join.get("idProject"), idProject));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Project> findProUsersResponsible(Users us) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Project> cq = cb.createQuery(Project.class);

        Root<Project> projectRoot = cq.from(Project.class);

        Join<Project, UserProjectLink> upl = projectRoot.join(Project_.userProjectLinkCollection);

        cq.select(projectRoot);
        cq.where(cb.equal(upl.get("users"), us));

        cq.orderBy(cb.asc(projectRoot.get(Project_.requestDate)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Project> findProjectAssociatedAPayment(Pago id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Project> cq = cb.createQuery(Project.class);

        Root<Project> projectRoot = cq.from(Project.class);

        Join<Project, ProyCotizaFacPagoLink> join = projectRoot.join(Project_.proyCotizaFacPagoLinkCollection);

        cq.select(projectRoot);
        cq.where(cb.equal(join.get("pagoId"), id));

        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();

    }

    public List<Pago> findDetailsPayment(int id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Pago> cq = cb.createQuery(Pago.class);

        Root<Pago> paymentRoot = cq.from(Pago.class);
        cq.select(paymentRoot);
        cq.where(cb.equal(paymentRoot.get("pagoId"), id));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();

    }

    public List<Pago> findDetailsPaymentUpdate(int id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Pago> cq = cb.createQuery(Pago.class);

        Root<Pago> paymentRoot = cq.from(Pago.class);
        cq.select(paymentRoot);
        cq.where(cb.equal(paymentRoot.get("idUser"), id));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();

    }

    public List<Sample> findSampleByName(String sm) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
        Root<Sample> sampleRoot = cq.from(Sample.class);
        cq.select(sampleRoot);
        cq.where(cb.equal(sampleRoot.get(Sample_.sampleName), sm));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Sample> findSampleById(String sm) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
        Root<Sample> sampleRoot = cq.from(Sample.class);
        cq.select(sampleRoot);
        cq.where(cb.equal(sampleRoot.get(Sample_.idSample), sm));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Sample> findSampleByRange(Integer rg1, Integer rg2, Project project) {

        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
        Root<Sample> sampleRoot = cq.from(Sample.class);
        //id_Sample>= rg1
        Predicate firstID = cb.greaterThanOrEqualTo(sampleRoot.get(Sample_.idSample), rg1);
        //id_Sample<= rg2
        Predicate SecondID = cb.lessThanOrEqualTo(sampleRoot.get(Sample_.idSample), rg2);
        //project_id=project
        Predicate proj = cb.equal(sampleRoot.get(Sample_.idProject), project);
        //id_Sample>= rg1 and id_sample <=rg2
        Predicate result1 = cb.and(firstID, SecondID);
        //  true                                        true
        //(id_Sample>= rg1 and id_sample <=rg2) and project_id=project
        Predicate result2 = cb.and(result1, proj);

        /*
        Select * from samples where id_Sample>= rg1 and id_sample <=rg2 and project_id=project;
         */
        cq.select(sampleRoot);
        cq.where(result2);
        //Sample
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Sample> findSamplesByIdAndProject(Integer id, Project proj) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
        Root<Sample> sampleRoot = cq.from(Sample.class);

        Predicate idSam = cb.equal(sampleRoot.get(Sample_.idSample), id);
        Predicate idProj = cb.equal(sampleRoot.get(Sample_.idProject), proj);
        Predicate condition = cb.and(idSam, idProj);

        cq.select(sampleRoot);
        cq.where(condition);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<SampleLibraryLink> findLibraryRun(String User, String plataform, String kit) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SampleLibraryLink> cq = cb.createQuery(SampleLibraryLink.class);

        Root<SampleLibraryLink> SampleLinkRoot = cq.from(SampleLibraryLink.class);

        //  Join<SampleLibraryLink,Sample> sll=SampleLinkRoot.join(SampleLibraryLink_.sample);
        Join<SampleLibraryLink, Library> Lsll = SampleLinkRoot.join(SampleLibraryLink_.library);

        List<Predicate> predicates = new ArrayList<>();

        if (User != "---" && User != null && User != "") {
            System.out.println("usuario: " + User);
            //predicates.add(cb.equal(Lsll.get(Library_.userName), User));
            cq.where(cb.equal(Lsll.get(Library_.userName), User));
            //cq.distinct(true);
        }
        /*
        if (!plataform.equals("---") && plataform != "") {
            System.out.println("Plataforma: " + plataform);
            //predicates.add(cb.equal(Lsll.get(Library_.plataform), plataform));
            cq.where(cb.equal(Lsll.get(Library_.id), plataform));
            //cq.distinct(true);
        }
         */
 /*
        if (!kit.equals("---") && kit != "") {
            System.out.println("Kit: " + kit);
            //predicates.add(cb.equal(Lsll.get(Library_.kit), kit));
            cq.where(cb.equal(Lsll.get(Library_.kit), kit));

        }
         */
        // 
        cq.select(SampleLinkRoot).distinct(true);
        //cq.distinct(true);
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        /*TypedQuery<SampleLibraryLink> typedQuery = getEntityManager().createQuery(cq.select(SampleLinkRoot).where(predicates.toArray(new Predicate[predicates.size()])).orderBy(cb.asc(SampleLinkRoot)).distinct(true));
        return  typedQuery.getResultList();*/
        return q.getResultList();
    }

    public List<Run> findRunLibraries() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Run> cq = cb.createQuery(Run.class);

        Root<Run> runRoot = cq.from(Run.class);

        //Join<Run, LibraryRunLink> join = runRoot.join(Run_.libraryRunLinkCollection);
        //Join<Library, LibraryRunLink> sll = runRoot.join("id_sample", JoinType.INNER);
        cq.select(runRoot);
        //cq.where(cb.equal(join.get("run_id"), id));
        cq.distinct(true);
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    public List<Run> findRunlibraries() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Run> cq = cb.createQuery(Run.class);

        Root<Run> runRoot = cq.from(Run.class);
        cq.select(runRoot);
        cq.orderBy(cb.desc(runRoot.get(Run_.runStartday)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();

    }

    public List<Library> findLibraryRUn(Run id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Library> cq = cb.createQuery(Library.class);

        Root<Library> runRoot = cq.from(Library.class);

        //Join<Library, LibraryRunLink> join = runRoot.join(Library_.libraryRunLinkCollection);
        Join<Library, LibraryRunLink> join = runRoot.join(Library_.libraryRunLinkList);
        Join<Library, SampleLibraryLink> sll = runRoot.join(Library_.sampleLibraryList);
        cq.select(runRoot);
        cq.where(cb.equal(join.get("run"), id));
        //leslie 22 agosto agrgue el join SampleLibraryLink, ordenarliberias en la vista de corridas por id_library 
        //cq.orderBy(cb.asc(join.get(LibraryRunLink_.library)));       
        cq.orderBy(cb.asc(sll.get(SampleLibraryLink_.sample)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    //27 agosto
    public List<Library> findLibrariesInRunByIdLib(Library id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Library> cq = cb.createQuery(Library.class);

        Root<Library> runRoot = cq.from(Library.class);

        //Join<Library, LibraryRunLink> join = runRoot.join(Library_.libraryRunLinkCollection);
        Join<Library, LibraryRunLink> join = runRoot.join(Library_.libraryRunLinkList);
        Join<Library, SampleLibraryLink> sll = runRoot.join(Library_.sampleLibraryList);
        cq.select(runRoot);
        cq.where(cb.equal(join.get("run"), id));
        //leslie 22 agosto agrgue el join SampleLibraryLink, ordenarliberias en la vista de corridas por id_library 
        //cq.orderBy(cb.asc(join.get(LibraryRunLink_.library)));       
        cq.orderBy(cb.asc(sll.get(SampleLibraryLink_.sample)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    public List<LibraryRunLink> findRunByLibrary(Integer id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<LibraryRunLink> cq = cb.createQuery(LibraryRunLink.class);

        Root<LibraryRunLink> libraryRoot = cq.from(LibraryRunLink.class);
        Join<LibraryRunLink, Library> jn = libraryRoot.join(LibraryRunLink_.library);
        cq.select(libraryRoot);
        cq.where(cb.equal(jn.get(Library_.id_library), id));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    public List<SampleLibraryLink> findRuns(Integer id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SampleLibraryLink> cq = cb.createQuery(SampleLibraryLink.class);

        Root<SampleLibraryLink> SampleLinkRoot = cq.from(SampleLibraryLink.class);

        Join<SampleLibraryLink, Library> Lsll = SampleLinkRoot.join(SampleLibraryLink_.library);

        cq.select(SampleLinkRoot).distinct(true);
        cq.where(cb.equal(Lsll.get(Library_.id_library), id));
        //leslie 22 agosto : para ordenar por el id de la muestrra
        cq.orderBy(cb.asc(SampleLinkRoot.get(SampleLibraryLink_.sample)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        /*List<Predicate> predicates = new ArrayList<>();
       
       predicates.add(cb.equal(Lsll.get(Library_.id_library),id )); 
       TypedQuery<SampleLibraryLink> typedQuery = getEntityManager().createQuery(cq.select(SampleLinkRoot).distinct(true).where(predicates.toArray(new Predicate[predicates.size()])));

       return  typedQuery.getResultList();*/
        return q.getResultList();

    }

    public List<Sample> findSamplebySampleSheet(int id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);

        Root<Sample> sampleRoot = cq.from(Sample.class);

        //Join<Sample, Project> sampProj = sampleRoot.join(Sample_.idProject);
        cq.select(sampleRoot);
        cq.where(cb.equal(sampleRoot.get(Sample_.idSample), id));
        //leslie 22 agosto order by
        cq.orderBy(cb.asc(sampleRoot.get(Sample_.idSample)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<UserProjectLink> findUserByProject(Project Proj) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<UserProjectLink> cq = cb.createQuery(UserProjectLink.class);

        Root<UserProjectLink> rt = cq.from(UserProjectLink.class);
        Join<UserProjectLink, Users> jn = rt.join(UserProjectLink_.users);
        cq.select(rt);
        cq.where(cb.equal(rt.get(UserProjectLink_.project), Proj));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Project> findAllProjects() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Project> cq = cb.createQuery(Project.class);
        Root<Project> rt = cq.from(Project.class);
        cq.select(rt);
        cq.orderBy(cb.desc(rt.get(Project_.requestDate)));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();

    }

    public List<Users> findUsersByUName(String UserName) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Users> cq = cb.createQuery(Users.class);
        Root<Users> dep = cq.from(Users.class);
        cq.select(dep);
        cq.where(cb.equal(dep.get(Users_.userName), UserName));
        cq.orderBy(cb.asc(dep.get(Users_.userName)));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<QualityReports> findQRB(Run idRun) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<QualityReports> cq = cb.createQuery(QualityReports.class);
        Root<QualityReports> Root = cq.from(QualityReports.class);

        Predicate run = cb.equal(Root.get(QualityReports_.idRun), idRun);
        Predicate type = cb.equal(Root.get(QualityReports_.type), "General");
        Predicate result = cb.and(run, type);

        cq.select(Root);
        cq.where(result);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<QualityReports> findQRByProject(Project id, Run idRun) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<QualityReports> cq = cb.createQuery(QualityReports.class);
        Root<QualityReports> Root = cq.from(QualityReports.class);
        Predicate proj = cb.equal(Root.get(QualityReports_.idProject), id);
        Predicate run = cb.equal(Root.get(QualityReports_.idRun), idRun);
        Predicate result = cb.and(proj, run);

        cq.select(Root);
        cq.where(result);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<BioinformaticsReports> findBRByProject(Project id, Run idRun) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<BioinformaticsReports> cq = cb.createQuery(BioinformaticsReports.class);
        Root<BioinformaticsReports> Root = cq.from(BioinformaticsReports.class);
        Predicate proj = cb.equal(Root.get(BioinformaticsReports_.idProject), id);
        Predicate run = cb.equal(Root.get(BioinformaticsReports_.idRun), idRun);
        Predicate Results = cb.and(proj, run);

        cq.select(Root);
        cq.where(Results);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Run> findRun(String RName) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Run> cq = cb.createQuery(Run.class);
        Root<Run> rt = cq.from(Run.class);
        cq.select(rt);
        cq.where(cb.equal(rt.get(Run_.runName), RName));
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Barcodes> findBarcodes(String position, String kit) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Barcodes> cq = cb.createQuery(Barcodes.class);
        Root<Barcodes> BarcodesRoot = cq.from(Barcodes.class);

        Predicate Restrict1
                = cb.and(cb.equal(BarcodesRoot.get("kitName"), kit));
        cq.select(BarcodesRoot);
        cq.where(Restrict1);
        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    public List<Library> findLibraryByRun(Run id) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Library> cq = cb.createQuery(Library.class);

        Root<Library> libraryRoot = cq.from(Library.class);
        Join<Library, LibraryRunLink> jn;
        //Join<Library, LibraryRunLink> jn = libraryRoot.join(Library_.libraryRunLinkCollection);
        jn = libraryRoot.join("id_library", JoinType.INNER);
        //Join<Library, LibraryRunLink> jn = libraryRoot.join("id_library", JoinType.INNER);

        cq.select(libraryRoot);
        cq.where(cb.equal(jn.get(LibraryRunLink_.run), id));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    public List<Sample> getSamples(Library lib, Project pj) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Sample> cq = cb.createQuery(Sample.class);
        Root<Sample> Root = cq.from(Sample.class);
        Join<Sample, SampleLibraryLink> sll = Root.join(Sample_.sampleLibraryCollection);
        Predicate lb = cb.equal(sll.get("library"), lib);
        Predicate proj = cb.equal(Root.get(Sample_.idProject), pj);
        Predicate results = cb.and(lb, proj);
        cq.select(Root);
        cq.where(results);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    //búsqueda de nombre del sample sheet en la tabla run
    public List<Run> getRunByRunName(String runName) {
        String sql = "select * from run where run_name = '" + runName + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Run.class);
        return q.getResultList();
    }

    //busqueda de librerías por id de corrida
    //leslie 22 agosto orderby id_library
    //ordenamiento por id_sample
    //select * from library_run_link lrl LEFT JOIN sample_library_link sll 
    //on lrl.id_library=sll.id_library where id_run=63 order by sll.id_sample;
    public List<LibraryRunLink> getLibraryRunLinkByIdRun(int idRun) {
        //String sql = "select * from library_run_link where id_run = '" + idRun + "' order by id_library;";
        String sql = "select * from library_run_link  lrl LEFT JOIN sample_library_link sll on lrl.id_library=sll.id_library  where id_run = '" + idRun + "' order by sll.id_sample;";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, LibraryRunLink.class);
        return q.getResultList();
    }

    public List<Library> getLibraryByIdLibrary(int idLibrary) {
        String sql = "select * from library where id_library = '" + idLibrary + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Library.class);
        return q.getResultList();
    }

    //Búsqueda de archvios por proyecto
    /*
    public List<Files> filesByProject(Project idProject, String typeFile){
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Files> c = cq.from(Files.class);
        cq.select(c);
        cq.orderBy(cb.desc(c.get(Files_.idFile)));
        cq.where(cb.equal(c.get(Files_.idProject), idProject));
        cq.where(cb.equal(c.get(Files_.fileType), typeFile));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }
     */
    public List<Files> filesByProject(Project idProject, String typeFile) {
        String sql = "select * from files where id_project = '" + idProject.getIdProject() + "' and file_type = '" + typeFile + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Files.class);
        return q.getResultList();
    }

    public List<Files> allFilesByProject(Project idProject) {
        String sql = "select * from files where id_project = '" + idProject.getIdProject() + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Files.class);
        return q.getResultList();
    }

    public List<UserProjectLink> usersProjectLinkCB(Project idProject) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<UserProjectLink> c = cq.from(UserProjectLink.class);
        cq.select(c);
        cq.where(cb.equal(c.get("project"), idProject));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return q.getResultList();
    }

    // LINABAT REPORTES   -------------------------------------------------INICIO
    /* Pruebas Luis Miguel */
    public List<BioinformaticAnalysisSampleLink> PruebaSql() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        CriteriaQuery<BioinformaticAnalysisSampleLink> cq = cb.createQuery(BioinformaticAnalysisSampleLink.class);
        Root<BioinformaticAnalysisSampleLink> Root = cq.from(BioinformaticAnalysisSampleLink.class);
        //cq.select(Root.get("sample").get("idSample")).distinct(true);
        //cq.select(Root.get(BioinformaticAnalysisSampleLink_.bioinformaticAnalysisSampleLinkPK)).distinct(true);
        cq.select(Root);
        //cq.select(Root.get("sample").get("idSample")).distinct(true);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    //Obtener Lista de Proyectos que cunplen con los requisitos min.
    public List<Project> proyectos_con_analisis_bioinformatico_iniciado_o_entregado() {
        String sql = "select * from project where id_project in (\n"
                + "select id_project from sample where id_project \n"
                + "in (select id_project from sample where status='En Analisis Bioinformatico' or status='Analisis Bioinformatico' or status='Analisis Bioinformatico Entregado' group by id_project) \n"
                + "group by id_project order by id_project)";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Project.class);
        return q.getResultList();
    }

    //Busca si existen registro en la tabla de report_project por id_project
    public List<ReportProject> reportProjectByIdProject(String idProject) {
        String sql = "select * from report_project where id_project = '" + idProject + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, ReportProject.class);
        return q.getResultList();
    }

    //Conteo del Tamaño d ela lista de los Proyectos que se puede iniciar un reporte
    public int countProyReport() {
        String sql = "select * from project where id_project in (\n"
                + "select id_project from sample where id_project \n"
                + "in (select id_project from sample where status='En Analisis Bioinformatico' or status='Analisis Bioinformatico' or status='Analisis Bioinformatico Entregado' or status='Entregado fastq' group by id_project) \n"
                + "group by id_project order by id_project)";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Project.class);
        int tam = q.getResultList().size();
        return tam;
    }

    //Obtiene la lista de Muestras segun el id del Proyecto
    public List<Sample> samplesByRepoProject(String idProject) {
        String sql = "select * from sample where id_project=" + "'" + idProject + "' order by id_sample";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Sample.class);
        return q.getResultList();
    }

    //Obtener un rango de muestras para Tabla de seleccion de Muestras y actualizacion de Estatus
    public List<Sample> rangoSample(String r1, String r2, String idProject) {
        String sql = "select * from sample where id_sample between " + r1 + " and " + r2 + "and id_project='" + idProject + "' order by id_sample";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Sample.class);
        return q.getResultList();
    }

    //Obtiene la lista de Las Muestras que fueron actualizadas en la tabla sample_report_project
    public List<SampleReportProject> EditRangeSample(int idReportProject) {
        String sql = "select * from sample_report_project where id_report_project=" + idReportProject;
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, SampleReportProject.class);
        return q.getResultList();
    }

    //Borra todos los elementos de saple_report_project asociados con el reporte mencionado
    public void DeleteRangeSample(int idReportProject) {
        EntityManager model = getEntityManager();
        CriteriaBuilder cb = model.getCriteriaBuilder();
        CriteriaDelete<SampleReportProject> criteriaDelete = cb.createCriteriaDelete(SampleReportProject.class);
        Root<SampleReportProject> root = criteriaDelete.from(SampleReportProject.class);
        criteriaDelete.where(root.get("idReportProject").in(idReportProject));
        javax.persistence.Query q = model.createQuery(criteriaDelete);

        int r = q.executeUpdate();
        model.flush();
        //List<SampleReportProject> rs = q.getResultList();
        //int i=5;
    }

    //Obitiene el proyecto, busqueda por id del proyecto   
    public List<Project> findProjectXIdProject(String idProject) {
        String sql = "select * from project where id_project = '" + idProject + "'";
        //String sql ="select * from user_role_report where exists (select * from user_role_report where id_user = "+idUser+")";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Project.class);
        return q.getResultList();
    }

    //Comprobacion de Existencia de un Proyecto.
    public int existsSampleReportProject(int idReportProject) {
        String sql = "select * from sample_report_project where id_report_project =" + idReportProject;
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, ReportProject.class);
        int longList = q.getResultList().size();
        return longList;
    }
    ///Pruebas omar

    //Obtener Lista de tabla users join user_role_report;
    public List<UserRoleReport> userRoleReportJoin() {
        String sql = "select * from user_role_report as ur join users as us on ur.id_user=us.id_user order by ur.id_user_role_report DESC";

        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, UserRoleReport.class);

        return q.getResultList();
    }

    public List<UserRoleReport> findUserRoleReportJoinForIdUser(int idUser) {
        String sql = "select * from user_role_report where id_user = " + idUser + "";
        //String sql ="select * from user_role_report where exists (select * from user_role_report where id_user = "+idUser+")";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, UserRoleReport.class);
        return q.getResultList();
    }

    public int existsFieldReport(int idReportProject) {
        String sql = "select * from field_report where id_report_project = " + idReportProject + "";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, FieldReport.class);
        int longList = q.getResultList().size();
        return longList;
    }

    public List<FieldReport> findFieldReportByReportProject(int idReportProject) {
        String sql = "select * from field_report where id_report_project = " + idReportProject + "";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, FieldReport.class);
        return q.getResultList();
    }

    public void deleteFieldReportByReportProject(int idReportProject) {
        EntityManager model = getEntityManager();
        CriteriaBuilder cb = model.getCriteriaBuilder();
        CriteriaDelete<FieldReport> criteriaDelete = cb.createCriteriaDelete(FieldReport.class);
        Root<FieldReport> root = criteriaDelete.from(FieldReport.class);
        criteriaDelete.where(root.get("idReportProject").in(idReportProject));
        javax.persistence.Query q = model.createQuery(criteriaDelete);
        q.executeUpdate();
    }

    public int deleteReportByReportProject(int idReportProject) {
        EntityManager model = getEntityManager();
        CriteriaBuilder cb = model.getCriteriaBuilder();
        CriteriaDelete<ReportProject> criteriaDelete = cb.createCriteriaDelete(ReportProject.class);
        Root<ReportProject> root = criteriaDelete.from(ReportProject.class);
        criteriaDelete.where(root.get("idReportProject").in(idReportProject));
        javax.persistence.Query q = model.createQuery(criteriaDelete);
        int result = q.executeUpdate();
        return result;
    }

    /**
     *
     * @param idProject
     * @param typeReport
     * @return
     */
    public int existsReportProject(String idProject, String typeReport) {
        String sql = "select * from report_project where id_project = '" + idProject + "' and name = '" + typeReport + "'";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, ReportProject.class);
        return q.getResultList().size();
    }

    /**
     *
     * @param idProject
     * @return
     */
    public int existsReportProject(String idProject) {
        String sql = "select * from report_project where id_project = '" + idProject + "'";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, ReportProject.class);
        return q.getResultList().size();
    }

    public List<ReportProject> findReportProjectidPType(String idProject, String typeReport) {
        String sql = "select * from report_project where id_project = '" + idProject + "' and name = '" + typeReport + "'";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, ReportProject.class);
        return q.getResultList();
    }

    public List<FieldReport> findFieldReporProjectByIDReportProject(int idReportProject) {
        String sql = "select * from field_report where id_report_project =" + idReportProject + "";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, FieldReport.class);
        return q.getResultList();
    }

    public List<Users> optionItemsSelectUsers() {
        //String sql ="select id_user, first_name, p_last_name from users where user_type = 'Admin'";
        String sql = "select id_user, first_name, p_last_name from users where user_type = 'Admin' and id_user not in(select id_user from user_role_report)";

        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);

        return q.getResultList();
    }

    //ootener la lista de los proyectos y sus reportes con su respectivo estado
    public List<ReportProject> reportProjectStatus() {
        String sql = "select * from report_project as r inner join project as p on p.id_project = r.id_project order by r.id_report_project DESC";
        //String sql = "select * from report_project as r inner join project as p on p.id_project = r.id_project order by r.id_report_project DESC";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, ReportProject.class);

        return q.getResultList();
    }

    //buscar reportProject por id del proyecto
    public List<ReportProject> findReportProjectByIdProject(String idProject) {
        String sql = "select * from report_project as r inner join project as p on p.id_project = r.id_project where r.id_project LIKE '%" + idProject + "%' order by r.id_report_project ASC";

        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, ReportProject.class);

        return q.getResultList();
    }

    //leslie 24 mayo
    public List<ReportProject> findIdReportProjectByIdProject(String idProject) {
        String sql = "select id_report_project from report_project as r inner join project as p on p.id_project = r.id_project where r.id_project LIKE '%" + idProject + "%' order by r.id_report_project ASC";

        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, ReportProject.class);

        return q.getResultList();
    }

    //Busca UserRoleRepor por  idUser
    public List<UserRoleReport> findUserRoleReportById(int idUser) {
        String sql = "select * from user_role_report where id_user= " + idUser + "";

        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, UserRoleReport.class);

        return q.getResultList();
    }

    //Obtener roles por revise
    public List<UserRoleReport> findUsersRoleReportByRevise() {
        //javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        String sql = "select * from user_role_report where torevise= " + true;
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, UserRoleReport.class);
        return q.getResultList();
    }

    //Obtener roles por autorizar
    public List<UserRoleReport> findUsersRoleReportByAuthorize() {
        String sql = "select * from user_role_report where toaauthorize= " + true;
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, UserRoleReport.class);
        return q.getResultList();
    }

    public List<Comments> dateQualityAnalysis(String idSample) {
        String sql = "select * from comments where id_type='" + idSample + "' and type='Sample' and comment like '%a -%alidad%' order by comment_date desc";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    public List<QualityReports> urlQualityReportsByIdProject(String idProject) {
        // urlQualityReport es un valor como http://www.uusmb.unam.mx/reportesQC/230609_NS500502_0177_AHVTLFBGXN//Project_MTrujillo_2023_03_23_11_58_59.html
        // entonces, en teoría se podría usar para hacer ordenamiento por fecha
        String sql = "select * from quality_reports where id_project='" + idProject + "' order by url_quality_report desc";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, QualityReports.class);
        return q.getResultList();
    }
    // LINABAT REPORTES   -------------------------------------------------FINAL

    //Mantenimiento Linabat
    public BioinformaticAnalysis getIdBioinformaticAnalysisFindByName(String nameAnalysis) {
        String sql = "select id_analysis from bioinformatic_analysis where analysis_name = '" + nameAnalysis + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, BioinformaticAnalysis.class);
        //int idBioinformaticAnalysis = (int) q.getResultList().get(0);
        return (BioinformaticAnalysis) q.getResultList().get(0);
    }

    public List<BioinformaticAnalysisSampleLink> getListBioAnalysisSampleLinkByIDAnalysisSample(int idAnalysis, int idSample) {
        String sql = "select * from bioinformatic_analysis_sample_link where id_analysis = " + idAnalysis + " and id_sample = " + idSample + ";";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, BioinformaticAnalysisSampleLink.class);
        return q.getResultList();
    }

    public List<BioinformaticAnalysisSampleLink> getListBioAnalysisSampleLinkByIDSample(int idSample) {
        String sql = "select * from bioinformatic_analysis_sample_link where id_sample = " + idSample + ";";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, BioinformaticAnalysisSampleLink.class);
        return q.getResultList();
    }

    //Creación de librerías ticket 3540
    //Consulta de plataformas con tag1
    public List<Barcodes> getBarcodeByTag1Unusable(String tag1) {
        String sql = "select * from barcodes where id_barcode='" + tag1 + "' " + "and tag_type like '%i7'";
        System.out.println("Se imprime la consulta para TAG1: " + sql);
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Barcodes.class);
        return q.getResultList();
    }

    //Consulta de plataformas con tag1
    public List<Barcodes> getBarcodeByTag2Unusable(String tag2) {
        String sql = "select * from barcodes where id_barcode='" + tag2 + "' " + "and tag_type like '%i5'";
        System.out.println("Se imprime la consulta para TAG2: " + sql);
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Barcodes.class);
        return q.getResultList();
    }

    //Consulta buscar barcode por id
    public List<Barcodes> getBarcodeById(String id_index) {
        String sql = "Select * from barcodes where id_barcode='" + id_index + "'";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Barcodes.class);
        return q.getResultList();
    }

//<<<<<<< HEAD
    //Consulta todas las dependencias
    public List<Dependency> getAllDependency() {
        String sql = "select * from dependency;";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Dependency.class);
        return q.getResultList();
    }

    /*
    public List<UserProjectLink> findUserProjectLinkByIdProyecto(String id_proyect) {
        return getEntityManager().createNamedQuery("UserProjectLink.findByIdProject", UserProjectLink.class).setParameter("idProject", id_proyect).getResultList();
    }
     */
    //Consulta genomes por nombre
    public List<Genome> getGenomeLikeName(String name) {
        String sql = "select * from genome where genome_name ilike '" + name + "%';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Genome.class);
        return q.getResultList();
    }

    //Obtener comentarios por id_type
    public List<Comments> getCommentsByIdType(String idType) {
        String sql = "select * from comments where id_type = '" + idType + "';";
        System.out.println("Se imprime la consulta: " + sql);
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    public List<Comments> getCommentsByIdTypeAndType(String idType, String type) {
        //Esta función, a diferencia de getCommentsByIdType, se cerciora que los matches sean del
        //tipo esperado, no se confía a que no haya IDsd duplicados por ejempl entre muestras y proyectos
        String sql = "select * from comments where id_type = '" + idType + "' AND type = '" + type + "';";
        System.out.println("Se imprime la consulta: " + sql);
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    public List<Comments> getCommentsByIdTypeAndTypeSBD(String idType, String type) {
        //Esta función, es como getCommentsByIdTypeAndType pero ordena los comentaios por fecha
        String sql = "select * from comments where id_type = '" + idType + "' AND type = '" + type + "' ORDER BY comment_date;";
        System.out.println("Se imprime la consulta: " + sql);
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    public List<String> getSamplesWithComments() {
        // Consulta SQL para obtener todos los id_type con comentarios
        String sql = "SELECT DISTINCT id_type FROM comments WHERE user_name != 'SISBI'";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql);

        // Devuelve una lista de IDs
        return q.getResultList();
    }

    //Obtener dependencias por institución
    public List<Dependency> getDependencyByInstitution(String institution) {
        String sql = "select * from dependency where unaccent(lower(institution)) = '" + institution + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Dependency.class);
        return q.getResultList();
    }

    //Obtener usuarios por coincidencia apellido paterno
    public List<Users> getUersByPLastName(String pLatName) {
        String sql = "select * from users where unaccent(lower(p_last_name)) like '%" + pLatName + "%';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);
        return q.getResultList();
    }

    //Obtener usuarios por coincidencia apellido materno
    public List<Users> getUersByMLastName(String mLatName) {
        String sql = "select * from users where unaccent(lower(m_last_name)) like '%" + mLatName + "%';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);
        return q.getResultList();
    }

    //Obtener usuarios por coincidencia nombre de usuario
    public List<Users> getUersByNameUser(String nameUser) {
        String sql = "select * from users where unaccent(lower(user_name)) like '%" + nameUser + "%';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);
        return q.getResultList();
    }

    //Obtener usuarios por coincidencia nombre de usuario (first name)
    public List<Users> getUersByFirstName(String firstName) {
        String sql = "select * from users where unaccent(lower(first_name)) like '%" + firstName + "%';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);
        return q.getResultList();
    }

    //Obtener usuarios por coincidencia email
    public List<Users> getUersByEmail(String email) {
        String sql = "select * from users where unaccent(lower(email)) like '%" + email + "%';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);
        return q.getResultList();
    }

    //Obtener usuarios por coincidencia departamento (department)
    public List<Users> getUersByDepartment(String department) {
        String sql = "select * from users where unaccent(lower(department)) like '%" + department + "%';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);
        return q.getResultList();
    }

    //Obtener usuarios por fecha de registro
    public List<Users> getUersByRegistrationDate(String dateRegistration) {
        String sql = "select * from users where registration_date::date = '" + dateRegistration + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);
        return q.getResultList();
    }

    //Obtener usuarios por búsqueda en diferentes campos
    public List<Users> getUsersGeneralFields(String filtro) {
        String sql = "select * from users where unaccent(lower(first_name)) like '%" + filtro + "%' or unaccent(lower(p_last_name)) like '%" + filtro + "%' or unaccent(lower(user_name)) like '%" + filtro + "%' or unaccent(lower(email)) like '%" + filtro + "%' or unaccent(lower(department)) like '%" + filtro + "%' or unaccent(lower(m_last_name)) like '%" + filtro + "%';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);
        return q.getResultList();
    }

    //Obtener los comentarios
    public List<Comments> getCommentsBAbyProject(String idProject) {
        String sql = "select * from comments where type = 'ProjectBA' and id_type = '" + idProject + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    //Obtener los comentarios
    public List<Comments> getCommentsBAbyProjectSortedByDate(String idProject) {
        String sql = "select * from comments where type = 'ProjectBA' and id_type = '" + idProject + "' ORDER BY comment_date ASC;";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Comments.class);
        return q.getResultList();
    }

    //Obtener correo electrónico
    public List<Users> getEmailUserByEmail(String email) {
        String sql = "select * from users where email = '" + email + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Users.class);
        return q.getResultList();

    }
    // Merge yonbras  ----------------------- inicio
//=======

    public List<UserProjectLink> findUserProjectLinkByIdProyecto(String id_proyect) {
        return getEntityManager().createNamedQuery("UserProjectLink.findByIdProject", UserProjectLink.class).setParameter("idProject", id_proyect).getResultList();
    }

    public Users findUsersByIdUser(Integer id_user) {
        return getEntityManager().createNamedQuery("Users.findByIdUser", Users.class).setParameter("idUser", id_user).getResultList().get(0);
    }

    public Dependency findDependencyByIdDependency(Integer id_dependency) {
        return getEntityManager().createNamedQuery("Dependency.findByIdDependency", Dependency.class).setParameter("idDependency", id_dependency).getResultList().get(0);
//>>>>>>> 3567-RegistroProyecto
        // Merge yonbras  ----------------------- final

    }

    public BarcodesCons findBarcodesByIdBarcode(Integer id_library) {
        List<BarcodesCons> resultsList = getEntityManager().createNamedQuery("BarcodesCons.findByIdLibrary", BarcodesCons.class).setParameter("idLibrary", id_library).getResultList();
        return resultsList.get(0);
    }

    public BarcodesCons findBarcodeByIdLibrary(int idLibrary) {
        String sql = "SELECT row_number() OVER (ORDER BY l.id_library) AS id_registro,\n"
                + "    l.id_library,\n"
                + "    ( SELECT concat(b.id_barcode, '-', b.sequence) AS concat\n"
                + "           FROM barcodes b\n"
                + "          WHERE b.id_index::text = l.id_barcode_1::text) AS id_barcode_1,\n"
                + "    ( SELECT concat(b.id_barcode, '-', b.sequence) AS concat\n"
                + "           FROM barcodes b\n"
                + "          WHERE b.id_index::text = l.id_barcode_2::text) AS id_barcode_2\n"
                + "   FROM library l where l.id_library = " + idLibrary + "  ;";

        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, BarcodesCons.class);
        return (BarcodesCons) q.getResultList().get(0);
    }

    public List<Library> findLibraryByIdLibrary(int idLibrary) {
        String sql = "select * from library where id_library = " + idLibrary + ";";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Library.class);
        return q.getResultList();
    }

    public List<SampleLibraryLink> findRunByIdSampleIdLibrary(int idSample, int idLibrary) {
        String sql = "select * from sample_library_link as sll where sll.id_sample = " + idSample + " and sll.id_library = " + idLibrary + ";";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, SampleLibraryLink.class);
        return q.getResultList();
    }

    public List<Barcodes> findBarcodesByIdIndex(String idIndex) {
        String sql = "select * from barcodes where id_index = '" + idIndex + "';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Barcodes.class);
        return q.getResultList();
    }

    ////Busca relacion plataform_link_kit por medio del id de plataforma e id kit
    public List<PlataformLinkKit> findPlataformLinkKitByIds(int idPlataform, int idKit) {
        String sql = "select * from plataform_link_kit where id_plataform = " + idPlataform + " and id_kit = " + idKit + ";";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, PlataformLinkKit.class);
        return q.getResultList();
    }

    //Busca relacion plataform_link_kit por medio de id plataforma
    public List<PlataformLinkKit> findPlataformLinkKitByIdPlataform(int idPlataform) {
        String sql = "select * from plataform_link_kit where id_plataform = " + idPlataform + ";";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, PlataformLinkKit.class);
        return q.getResultList();
    }

    /*
    //Busca una plataforma por medio del nombre de la pataforma
    public List<Plataform> findNamePlataformsbyName(String namePlataform){
        String sql = "select * from plataform where plataform_name = '"+namePlataform+"';";
        javax.persistence.Query q = getEntityManager().createNativeQuery(sql, Plataform.class);
        return q.getResultList();
    }*/
    //Devuelve todas las plataformas
    public List<Plataform> findAllPlataforms() {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plataform> cq = cb.createQuery(Plataform.class);
        Root<Plataform> dep = cq.from(Plataform.class);
        cq.select(dep);
        //cq.orderBy(cb.asc(dep.get(Users_.userName)));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    //Devuelve todas las plataformas
    public List<Plataform> findPlataformByName(String plataformName) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Plataform> cq = cb.createQuery(Plataform.class);
        Root<Plataform> dep = cq.from(Plataform.class);
        cq.select(dep);
        cq.where(cb.equal(dep.get(Plataform_.plataformName), plataformName));
        //cq.where(cb.equal(c.get("idUser"), us.getIdUser()));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
        //return q.getResultList();
    }

    //Busqueda de kit por medio del nombre
    public List<Kit> findKitByName(String nameKit) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Kit> cq = cb.createQuery(Kit.class);
        Root<Kit> dep = cq.from(Kit.class);
        cq.select(dep);
        cq.where(cb.equal(dep.get(Kit_.kitName), nameKit));
        //cq.where(cb.equal(c.get("idUser"), us.getIdUser()));

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
        //return q.getResultList();
    }

    public List<Barcodes> findBarcodesi7byIdKit(int idKit) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Barcodes> cq = cb.createQuery(Barcodes.class);
        Root<Barcodes> BarcodesRoot = cq.from(Barcodes.class);

        Predicate Restrict1 = cb.equal(BarcodesRoot.get(Barcodes_.idKit), idKit);
        Predicate Restrict2 = cb.isNotNull(BarcodesRoot.get(Barcodes_.basei7));
        Predicate condition = cb.and(Restrict1, Restrict2);
        cq.select(BarcodesRoot);
        cq.where(condition);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

    //MÉTODOS USADOS PARA LA CREACIÓN DE BIBLIOTECAS POR MEDIO DEL EXCEL
    //buscar barcode por medio del index_name
    public List<Barcodes> findBarcodeByIndexName(String index_name) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Barcodes> cq = cb.createQuery(Barcodes.class);
        Root<Barcodes> BarcodesRoot = cq.from(Barcodes.class);

        Predicate pre1 = cb.equal(BarcodesRoot.get(Barcodes_.indexName), index_name);
        cq.select(BarcodesRoot);
        cq.where(pre1);

        javax.persistence.Query q = getEntityManager().createQuery(cq);

        return q.getResultList();
    }

}

/*
           SELECT *
            FROM 
            user_project_link
            INNER JOIN project ON user_project_link.id_project=project.id_project
            WHERE user_project_link.id_user=1;

 */
