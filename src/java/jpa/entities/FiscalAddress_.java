package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.ProyCotizaFacPagoLink;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(FiscalAddress.class)
public class FiscalAddress_ { 

    public static volatile SingularAttribute<FiscalAddress, String> city;
    public static volatile SingularAttribute<FiscalAddress, String> street;
    public static volatile SingularAttribute<FiscalAddress, Integer> intNum;
    public static volatile SingularAttribute<FiscalAddress, Integer> postalCode;
    public static volatile CollectionAttribute<FiscalAddress, ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;
    public static volatile SingularAttribute<FiscalAddress, Integer> idFiscalAddress;
    public static volatile SingularAttribute<FiscalAddress, String> location;
    public static volatile SingularAttribute<FiscalAddress, String> state;
    public static volatile SingularAttribute<FiscalAddress, String> socialReason;
    public static volatile SingularAttribute<FiscalAddress, String> rfc;
    public static volatile SingularAttribute<FiscalAddress, Integer> extNum;

}