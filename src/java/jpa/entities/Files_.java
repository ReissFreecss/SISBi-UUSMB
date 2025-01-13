package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.Project;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-11-24T12:17:16")
@StaticMetamodel(Files.class)
public class Files_ { 

    public static volatile SingularAttribute<Files, Project> idProject;
    public static volatile SingularAttribute<Files, String> path;
    public static volatile SingularAttribute<Files, String> fileName;
    public static volatile SingularAttribute<Files, String> uploader;
    public static volatile SingularAttribute<Files, Integer> idFile;
    public static volatile SingularAttribute<Files, String> comment;
    public static volatile SingularAttribute<Files, Date> fileDate;
    public static volatile SingularAttribute<Files, String> fileType;

}