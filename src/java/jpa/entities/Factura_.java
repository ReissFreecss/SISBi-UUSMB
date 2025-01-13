package jpa.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.ProyCotizaFacPagoLink;
import jpa.entities.Users;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Factura.class)
public class Factura_ { 

    public static volatile SingularAttribute<Factura, Users> idUser;
    public static volatile SingularAttribute<Factura, String> factFolio;
    public static volatile SingularAttribute<Factura, BigDecimal> monto;
    public static volatile SingularAttribute<Factura, Integer> factId;
    public static volatile SingularAttribute<Factura, BigDecimal> iva;
    public static volatile SingularAttribute<Factura, BigDecimal> overhead;
    public static volatile CollectionAttribute<Factura, ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;
    public static volatile SingularAttribute<Factura, Date> factFecha;
    public static volatile SingularAttribute<Factura, Date> pagoFecha;

}