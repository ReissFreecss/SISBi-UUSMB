package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.City;
import jpa.entities.Users;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Dependency.class)
public class Dependency_ { 

    public static volatile CollectionAttribute<Dependency, Users> usersCollection1;
    public static volatile SingularAttribute<Dependency, String> institution;
    public static volatile SingularAttribute<Dependency, Integer> idDependency;
    public static volatile SingularAttribute<Dependency, String> dependencyName;
    public static volatile SingularAttribute<Dependency, String> acronym;
    public static volatile SingularAttribute<Dependency, String> street;
    public static volatile SingularAttribute<Dependency, String> postalCode;
    public static volatile SingularAttribute<Dependency, City> idCity;
    public static volatile SingularAttribute<Dependency, String> location;
    public static volatile SingularAttribute<Dependency, String> type;
    public static volatile CollectionAttribute<Dependency, Users> usersCollection;

}