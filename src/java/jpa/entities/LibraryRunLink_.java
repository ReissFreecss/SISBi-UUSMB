package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Library;
import jpa.entities.LibraryRunLinkPK;
import jpa.entities.Run;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(LibraryRunLink.class)
public class LibraryRunLink_ { 

    public static volatile SingularAttribute<LibraryRunLink, LibraryRunLinkPK> libraryRunLinkPK;
    public static volatile SingularAttribute<LibraryRunLink, String> note;
    public static volatile SingularAttribute<LibraryRunLink, Library> library;
    public static volatile SingularAttribute<LibraryRunLink, Run> run;
    public static volatile SingularAttribute<LibraryRunLink, Integer> realPerformance;

}