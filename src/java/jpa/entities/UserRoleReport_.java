package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Users;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(UserRoleReport.class)
public class UserRoleReport_ { 

    public static volatile SingularAttribute<UserRoleReport, Users> idUser;
    public static volatile SingularAttribute<UserRoleReport, Boolean> toaauthorize;
    public static volatile SingularAttribute<UserRoleReport, Boolean> tocreate;
    public static volatile SingularAttribute<UserRoleReport, Boolean> torevise;
    public static volatile SingularAttribute<UserRoleReport, String> pathimage;
    public static volatile SingularAttribute<UserRoleReport, Integer> idUserRoleReport;

}