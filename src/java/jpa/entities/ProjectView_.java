package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Sample;
import jpa.entities.UserProjectLink;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(ProjectView.class)
public class ProjectView_ { 

    public static volatile SingularAttribute<ProjectView, String> idProject;
    public static volatile SingularAttribute<ProjectView, String> quotationNumber;
    public static volatile SingularAttribute<ProjectView, String> comments;
    public static volatile CollectionAttribute<ProjectView, Sample> sampleCollection;
    public static volatile SingularAttribute<ProjectView, String> projectDescription;
    public static volatile SingularAttribute<ProjectView, Date> requestDate;
    public static volatile CollectionAttribute<ProjectView, UserProjectLink> userProjectLinkCollection;
    public static volatile SingularAttribute<ProjectView, Integer> id_user;
    public static volatile SingularAttribute<ProjectView, String> projectName;
    public static volatile SingularAttribute<ProjectView, String> billNumber;
    public static volatile SingularAttribute<ProjectView, String> status;

}