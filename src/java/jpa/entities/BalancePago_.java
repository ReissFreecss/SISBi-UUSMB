package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Pago;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(BalancePago.class)
public class BalancePago_ { 

    public static volatile SingularAttribute<BalancePago, Date> fecha;
    public static volatile SingularAttribute<BalancePago, Pago> pagoId;
    public static volatile SingularAttribute<BalancePago, String> cotizacion;
    public static volatile SingularAttribute<BalancePago, String> description;
    public static volatile SingularAttribute<BalancePago, Integer> idBalance;
    public static volatile SingularAttribute<BalancePago, Double> abono;
    public static volatile SingularAttribute<BalancePago, Double> saldo;
    public static volatile SingularAttribute<BalancePago, Integer> id_user;
    public static volatile SingularAttribute<BalancePago, Double> cargo;

}