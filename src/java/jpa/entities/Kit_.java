package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Barcodes;
import jpa.entities.PlataformLinkKit;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Kit.class)
public class Kit_ { 

    public static volatile SingularAttribute<Kit, String> note;
    public static volatile ListAttribute<Kit, PlataformLinkKit> plataformLinkKitList;
    public static volatile ListAttribute<Kit, Barcodes> barcodesList;
    public static volatile SingularAttribute<Kit, Integer> idKit;
    public static volatile SingularAttribute<Kit, String> kitName;

}