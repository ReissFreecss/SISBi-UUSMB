package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Library;
import jpa.entities.Sample;
import jpa.entities.SampleLibraryLinkPK;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(SampleLibraryLink.class)
public class SampleLibraryLink_ { 

    public static volatile SingularAttribute<SampleLibraryLink, String> note;
    public static volatile SingularAttribute<SampleLibraryLink, Library> library;
    public static volatile SingularAttribute<SampleLibraryLink, SampleLibraryLinkPK> sampleLibraryLinkPK;
    public static volatile SingularAttribute<SampleLibraryLink, Sample> sample;

}