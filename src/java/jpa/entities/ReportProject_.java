package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Project;
import jpa.entities.Users;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-11-24T12:17:16")
@StaticMetamodel(ReportProject.class)
public class ReportProject_ { 

    public static volatile SingularAttribute<ReportProject, String> pathcreate;
    public static volatile SingularAttribute<ReportProject, Date> dateAuthorize;
    public static volatile SingularAttribute<ReportProject, Users> idUserAuthorize;
    public static volatile SingularAttribute<ReportProject, Date> dateRevise;
    public static volatile SingularAttribute<ReportProject, String> pathauthorize;
    public static volatile SingularAttribute<ReportProject, Integer> idReportProject;
    public static volatile SingularAttribute<ReportProject, Date> dateCreate;
    public static volatile SingularAttribute<ReportProject, Users> idUser;
    public static volatile SingularAttribute<ReportProject, Project> idProject;
    public static volatile SingularAttribute<ReportProject, String> pathrevise;
    public static volatile SingularAttribute<ReportProject, String> name;
    public static volatile SingularAttribute<ReportProject, String> typeMethodology;
    public static volatile SingularAttribute<ReportProject, Users> idUserRevise;
    public static volatile SingularAttribute<ReportProject, String> status;

}