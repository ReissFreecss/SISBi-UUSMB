package jpa.entities;

import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.BalancePago;
import jpa.entities.ProyCotizaFacPagoLink;
import jpa.entities.Users;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Pago.class)
public class Pago_ { 

    public static volatile SingularAttribute<Pago, Users> idUser;
    public static volatile SingularAttribute<Pago, Integer> pagoId;
    public static volatile SingularAttribute<Pago, BigDecimal> monto;
    public static volatile SingularAttribute<Pago, String> referen;
    public static volatile SingularAttribute<Pago, String> observaciones;
    public static volatile CollectionAttribute<Pago, ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;
    public static volatile SingularAttribute<Pago, Date> pagoFecha;
    public static volatile CollectionAttribute<Pago, BalancePago> balancePagoCollection;
    public static volatile SingularAttribute<Pago, String> poliza;
    public static volatile SingularAttribute<Pago, String> status;
    public static volatile SingularAttribute<Pago, String> url_voucher;

}