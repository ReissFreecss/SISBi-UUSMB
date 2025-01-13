package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.BioinformaticAnalysisSampleLinkPK;
import jpa.entities.Sample;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(BioinformaticAnalysisSampleLink.class)
public class BioinformaticAnalysisSampleLink_ { 

    public static volatile SingularAttribute<BioinformaticAnalysisSampleLink, String> note;
    public static volatile SingularAttribute<BioinformaticAnalysisSampleLink, Date> startday;
    public static volatile SingularAttribute<BioinformaticAnalysisSampleLink, Date> finishdate;
    public static volatile SingularAttribute<BioinformaticAnalysisSampleLink, String> link;
    public static volatile SingularAttribute<BioinformaticAnalysisSampleLink, BioinformaticAnalysis> bioinformaticAnalysis;
    public static volatile SingularAttribute<BioinformaticAnalysisSampleLink, Sample> sample;
    public static volatile SingularAttribute<BioinformaticAnalysisSampleLink, BioinformaticAnalysisSampleLinkPK> bioinformaticAnalysisSampleLinkPK;

}