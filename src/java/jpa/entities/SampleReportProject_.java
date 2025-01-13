package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.ReportProject;
import jpa.entities.Sample;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(SampleReportProject.class)
public class SampleReportProject_ { 

    public static volatile SingularAttribute<SampleReportProject, Sample> idSample;
    public static volatile SingularAttribute<SampleReportProject, String> statusPrevious;
    public static volatile SingularAttribute<SampleReportProject, ReportProject> idReportProject;
    public static volatile SingularAttribute<SampleReportProject, String> statusCurrent;
    public static volatile SingularAttribute<SampleReportProject, Date> dateUpgrade;
    public static volatile SingularAttribute<SampleReportProject, Integer> idSampleReportProject;

}