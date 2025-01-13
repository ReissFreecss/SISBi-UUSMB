package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.PlataformLinkKit;
import jpa.entities.Run;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Plataform.class)
public class Plataform_ { 

    public static volatile SingularAttribute<Plataform, String> note;
    public static volatile ListAttribute<Plataform, PlataformLinkKit> plataformLinkKitList;
    public static volatile ListAttribute<Plataform, Run> runList;
    public static volatile SingularAttribute<Plataform, String> location;
    public static volatile SingularAttribute<Plataform, String> plataformName;
    public static volatile SingularAttribute<Plataform, Integer> idPlataform;

}