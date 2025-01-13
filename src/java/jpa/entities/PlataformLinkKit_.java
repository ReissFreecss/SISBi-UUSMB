package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Kit;
import jpa.entities.Library;
import jpa.entities.Plataform;
import jpa.entities.PlataformLinkKitPK;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(PlataformLinkKit.class)
public class PlataformLinkKit_ { 

    public static volatile SingularAttribute<PlataformLinkKit, PlataformLinkKitPK> plataformLinkKitPK;
    public static volatile SingularAttribute<PlataformLinkKit, String> notes;
    public static volatile SingularAttribute<PlataformLinkKit, Plataform> plataform;
    public static volatile SingularAttribute<PlataformLinkKit, Kit> kit;
    public static volatile ListAttribute<PlataformLinkKit, Library> libraryList;

}