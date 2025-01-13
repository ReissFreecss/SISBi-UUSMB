package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Genome;
import jpa.entities.Sample;
import jpa.entities.SampleGenomeLinkPK;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(SampleGenomeLink.class)
public class SampleGenomeLink_ { 

    public static volatile SingularAttribute<SampleGenomeLink, String> note;
    public static volatile SingularAttribute<SampleGenomeLink, Genome> genome;
    public static volatile SingularAttribute<SampleGenomeLink, SampleGenomeLinkPK> sampleGenomeLinkPK;
    public static volatile SingularAttribute<SampleGenomeLink, Sample> sample;

}