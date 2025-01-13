package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.BioinformaticAnalysisSampleLink;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(BioinformaticAnalysis.class)
public class BioinformaticAnalysis_ { 

    public static volatile SingularAttribute<BioinformaticAnalysis, String> note;
    public static volatile SingularAttribute<BioinformaticAnalysis, Integer> idAnalysis;
    public static volatile CollectionAttribute<BioinformaticAnalysis, BioinformaticAnalysisSampleLink> bioinformaticAnalysisSampleLinkCollection;
    public static volatile SingularAttribute<BioinformaticAnalysis, String> type;
    public static volatile SingularAttribute<BioinformaticAnalysis, String> softwareVersion;
    public static volatile SingularAttribute<BioinformaticAnalysis, String> analysisName;

}