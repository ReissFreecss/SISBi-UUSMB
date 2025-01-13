package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Comments.class)
public class Comments_ { 

    public static volatile SingularAttribute<Comments, Integer> idComment;
    public static volatile SingularAttribute<Comments, String> idType;
    public static volatile SingularAttribute<Comments, Date> commentDate;
    public static volatile SingularAttribute<Comments, String> comment;
    public static volatile SingularAttribute<Comments, String> type;
    public static volatile SingularAttribute<Comments, String> userName;

}