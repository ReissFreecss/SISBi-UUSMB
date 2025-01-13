package jpa.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Project;
import jpa.entities.UserProjectLinkPK;
import jpa.entities.Users;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-11-24T12:17:16")
@StaticMetamodel(UserProjectLink.class)
public class UserProjectLink_ { 

    public static volatile SingularAttribute<UserProjectLink, String> note;
    public static volatile SingularAttribute<UserProjectLink, UserProjectLinkPK> userProjectLinkPK;
    public static volatile SingularAttribute<UserProjectLink, String> role;
    public static volatile SingularAttribute<UserProjectLink, Project> project;
    public static volatile SingularAttribute<UserProjectLink, Users> users;

}