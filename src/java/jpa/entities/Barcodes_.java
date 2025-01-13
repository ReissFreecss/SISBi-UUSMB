package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Kit;
import jpa.entities.Library;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Barcodes.class)
public class Barcodes_ { 

    public static volatile SingularAttribute<Barcodes, String> basei5Nextseq;
    public static volatile ListAttribute<Barcodes, Library> libraryList1;
    public static volatile SingularAttribute<Barcodes, String> notes;
    public static volatile SingularAttribute<Barcodes, String> basei5Miseq;
    public static volatile SingularAttribute<Barcodes, String> indexName;
    public static volatile SingularAttribute<Barcodes, Integer> idBarcode;
    public static volatile SingularAttribute<Barcodes, String> basei7;
    public static volatile ListAttribute<Barcodes, Library> libraryList;
    public static volatile SingularAttribute<Barcodes, Kit> idKit;

}