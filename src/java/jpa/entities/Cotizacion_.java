package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.ProyCotizaFacPagoLink;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Cotizacion.class)
public class Cotizacion_ { 

    public static volatile SingularAttribute<Cotizacion, String> typeService;
    public static volatile SingularAttribute<Cotizacion, Double> cotizaMontoPesos;
    public static volatile SingularAttribute<Cotizacion, String> cotizaNum;
    public static volatile SingularAttribute<Cotizacion, String> typeClient;
    public static volatile SingularAttribute<Cotizacion, String> nameContact;
    public static volatile CollectionAttribute<Cotizacion, ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;
    public static volatile SingularAttribute<Cotizacion, Integer> cotizaId;
    public static volatile SingularAttribute<Cotizacion, String> cotizaStatus;
    public static volatile SingularAttribute<Cotizacion, Integer> totalSamples;
    public static volatile SingularAttribute<Cotizacion, Date> cotizaFecha;

}