package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.SampleGenomeLink;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Genome.class)
public class Genome_ { 

    public static volatile SingularAttribute<Genome, String> gi;
    public static volatile SingularAttribute<Genome, String> genomeVersion;
    public static volatile SingularAttribute<Genome, String> idGenome;
    public static volatile SingularAttribute<Genome, String> genomeName;
    public static volatile SingularAttribute<Genome, String> http;
    public static volatile SingularAttribute<Genome, String> genomeLength;
    public static volatile SingularAttribute<Genome, String> source;
    public static volatile CollectionAttribute<Genome, SampleGenomeLink> sampleGenomeLinkCollection;

}