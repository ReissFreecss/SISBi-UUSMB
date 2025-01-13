package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.BioinformaticsReportsPK;
import jpa.entities.Project;
import jpa.entities.Run;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-11-24T12:17:16")
@StaticMetamodel(BioinformaticsReports.class)
public class BioinformaticsReports_ { 

    public static volatile SingularAttribute<BioinformaticsReports, String> owner;
    public static volatile SingularAttribute<BioinformaticsReports, Project> idProject;
    public static volatile SingularAttribute<BioinformaticsReports, String> pathWork;
    public static volatile SingularAttribute<BioinformaticsReports, String> pdfReport;
    public static volatile SingularAttribute<BioinformaticsReports, Run> idRun;
    public static volatile SingularAttribute<BioinformaticsReports, BioinformaticAnalysis> bioinformaticAnalysis;
    public static volatile SingularAttribute<BioinformaticsReports, String> description;
    public static volatile SingularAttribute<BioinformaticsReports, BioinformaticsReportsPK> bioinformaticsReportsPK;
    public static volatile SingularAttribute<BioinformaticsReports, String> type;
    public static volatile SingularAttribute<BioinformaticsReports, Date> createDate;

}