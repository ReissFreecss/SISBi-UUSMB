package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.ProyCotizaFacPagoLink;
import jpa.entities.Sample;
import jpa.entities.UserProjectLink;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-11-24T12:17:14")
@StaticMetamodel(Project.class)
public class Project_ { 

    public static volatile SingularAttribute<Project, String> quotationNumber;
    public static volatile SingularAttribute<Project, String> comments;
    public static volatile SingularAttribute<Project, String> deliveryMethod;
    public static volatile CollectionAttribute<Project, ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;
    public static volatile CollectionAttribute<Project, UserProjectLink> userProjectLinkCollection;
    public static volatile SingularAttribute<Project, String> type_project;
    public static volatile SingularAttribute<Project, String> idProject;
    public static volatile CollectionAttribute<Project, Sample> sampleCollection;
    public static volatile SingularAttribute<Project, String> projectDescription;
    public static volatile SingularAttribute<Project, Date> requestDate;
    public static volatile SingularAttribute<Project, String> projectName;
    public static volatile SingularAttribute<Project, String> billNumber;
    public static volatile SingularAttribute<Project, String> status;

}