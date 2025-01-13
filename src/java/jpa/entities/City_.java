package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Country;
import jpa.entities.Dependency;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(City.class)
public class City_ { 

    public static volatile CollectionAttribute<City, Dependency> dependencyCollection;
    public static volatile SingularAttribute<City, String> cityName;
    public static volatile SingularAttribute<City, String> idCity;
    public static volatile SingularAttribute<City, Country> idCountry;

}