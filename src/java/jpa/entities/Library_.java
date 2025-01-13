package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Barcodes;
import jpa.entities.LibraryRunLink;
import jpa.entities.PlataformLinkKit;
import jpa.entities.SampleLibraryLink;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Library.class)
public class Library_ { 

    public static volatile SingularAttribute<Library, String> libraryName;
    public static volatile SingularAttribute<Library, Date> preparationDate;
    public static volatile SingularAttribute<Library, Double> qubitConcentration;
    public static volatile SingularAttribute<Library, Integer> id_library;
    public static volatile SingularAttribute<Library, String> preparationStatus;
    public static volatile SingularAttribute<Library, String> userName;
    public static volatile SingularAttribute<Library, Double> qpcrConcentration;
    public static volatile ListAttribute<Library, LibraryRunLink> libraryRunLinkList;
    public static volatile SingularAttribute<Library, Integer> librarySize;
    public static volatile SingularAttribute<Library, Double> nanomolarConcentration;
    public static volatile SingularAttribute<Library, PlataformLinkKit> plataformLinkKit;
    public static volatile SingularAttribute<Library, Barcodes> idBarcode1;
    public static volatile SingularAttribute<Library, Barcodes> idBarcode2;
    public static volatile ListAttribute<Library, SampleLibraryLink> sampleLibraryList;

}