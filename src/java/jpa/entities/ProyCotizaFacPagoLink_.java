package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Cotizacion;
import jpa.entities.Factura;
import jpa.entities.FiscalAddress;
import jpa.entities.Pago;
import jpa.entities.Project;
import jpa.entities.Users;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-11-24T12:17:16")
@StaticMetamodel(ProyCotizaFacPagoLink.class)
public class ProyCotizaFacPagoLink_ { 

    public static volatile SingularAttribute<ProyCotizaFacPagoLink, Users> idUser;
    public static volatile SingularAttribute<ProyCotizaFacPagoLink, Project> idProject;
    public static volatile SingularAttribute<ProyCotizaFacPagoLink, Pago> pagoId;
    public static volatile SingularAttribute<ProyCotizaFacPagoLink, Integer> idLinkPcfp;
    public static volatile SingularAttribute<ProyCotizaFacPagoLink, Cotizacion> cotizaId;
    public static volatile SingularAttribute<ProyCotizaFacPagoLink, Factura> facId;
    public static volatile SingularAttribute<ProyCotizaFacPagoLink, FiscalAddress> idFiscalAddress;

}