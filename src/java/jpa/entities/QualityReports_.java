package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Project;
import jpa.entities.Run;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-11-24T12:17:16")
@StaticMetamodel(QualityReports.class)
public class QualityReports_ { 

    public static volatile SingularAttribute<QualityReports, Integer> idQualityReports;
    public static volatile SingularAttribute<QualityReports, Project> idProject;
    public static volatile SingularAttribute<QualityReports, String> urlQualityReport;
    public static volatile SingularAttribute<QualityReports, Run> idRun;
    public static volatile SingularAttribute<QualityReports, String> type;
    public static volatile SingularAttribute<QualityReports, String> sampleSheetFile;

}