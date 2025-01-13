package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Dependency;
import jpa.entities.UserProjectLink;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile SingularAttribute<Users, Dependency> idFiscalAddress;
    public static volatile CollectionAttribute<Users, UserProjectLink> userProjectLinkCollection;
    public static volatile SingularAttribute<Users, String> userName;
    public static volatile SingularAttribute<Users, Integer> idUser;
    public static volatile SingularAttribute<Users, String> firstName;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile SingularAttribute<Users, String> mLastName;
    public static volatile SingularAttribute<Users, String> phoneNumber;
    public static volatile SingularAttribute<Users, Dependency> idDependency;
    public static volatile SingularAttribute<Users, Date> registrationDate;
    public static volatile SingularAttribute<Users, String> pLastName;
    public static volatile SingularAttribute<Users, String> userType;
    public static volatile SingularAttribute<Users, String> department;
    public static volatile SingularAttribute<Users, String> email;

}