package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.BioinformaticAnalysisSampleLink;
import jpa.entities.Project;
import jpa.entities.SampleGenomeLink;
import jpa.entities.SampleLibraryLink;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-11-24T12:17:16")
@StaticMetamodel(Sample.class)
public class Sample_ { 

    public static volatile SingularAttribute<Sample, Double> labConcen;
    public static volatile SingularAttribute<Sample, String> app_type;
    public static volatile SingularAttribute<Sample, Date> receptionDate;
    public static volatile SingularAttribute<Sample, String> do_seq;
    public static volatile SingularAttribute<Sample, String> description;
    public static volatile SingularAttribute<Sample, String> realPerformance;
    public static volatile SingularAttribute<Sample, String> insertSize;
    public static volatile SingularAttribute<Sample, String> expectedPerformance;
    public static volatile SingularAttribute<Sample, String> type;
    public static volatile SingularAttribute<Sample, String> just_genextrac;
    public static volatile SingularAttribute<Sample, String> just_seq;
    public static volatile SingularAttribute<Sample, String> sampleName;
    public static volatile SingularAttribute<Sample, String> abs260_230;
    public static volatile SingularAttribute<Sample, String> tag_lib;
    public static volatile SingularAttribute<Sample, Integer> idSample;
    public static volatile SingularAttribute<Sample, String> aceptation;
    public static volatile SingularAttribute<Sample, String> geneticExtraction;
    public static volatile SingularAttribute<Sample, String> organismName;
    public static volatile SingularAttribute<Sample, String> sampleQuality;
    public static volatile CollectionAttribute<Sample, SampleLibraryLink> sampleLibraryCollection;
    public static volatile SingularAttribute<Sample, Date> deliveryDate;
    public static volatile SingularAttribute<Sample, String> referenceName;
    public static volatile SingularAttribute<Sample, String> delivery;
    public static volatile SingularAttribute<Sample, String> deplesion;
    public static volatile SingularAttribute<Sample, String> sampleQuantity;
    public static volatile SingularAttribute<Sample, String> comments;
    public static volatile SingularAttribute<Sample, String> organism_type;
    public static volatile SingularAttribute<Sample, String> samplePlataform;
    public static volatile SingularAttribute<Sample, Double> labVolume;
    public static volatile SingularAttribute<Sample, String> contaminantName;
    public static volatile SingularAttribute<Sample, String> sampleVolume;
    public static volatile SingularAttribute<Sample, String> idTube;
    public static volatile SingularAttribute<Sample, String> kit_lib;
    public static volatile CollectionAttribute<Sample, BioinformaticAnalysisSampleLink> bioinformaticAnalysisSampleLinkCollection;
    public static volatile SingularAttribute<Sample, String> expectedPerformanceOxford;
    public static volatile CollectionAttribute<Sample, SampleGenomeLink> sampleGenomeLinkCollection;
    public static volatile SingularAttribute<Sample, String> just_lib;
    public static volatile SingularAttribute<Sample, String> just_qanalysis;
    public static volatile SingularAttribute<Sample, Project> idProject;
    public static volatile SingularAttribute<Sample, String> build_lib;
    public static volatile SingularAttribute<Sample, String> folio;
    public static volatile SingularAttribute<Sample, String> abs260_230_usmb;
    public static volatile SingularAttribute<Sample, String> readSize;
    public static volatile SingularAttribute<Sample, String> abs260_280;
    public static volatile SingularAttribute<Sample, String> status;
    public static volatile SingularAttribute<Sample, String> abs260_280_usmb;

}