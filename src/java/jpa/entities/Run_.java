package jpa.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import jpa.entities.LibraryRunLink;
import jpa.entities.Plataform;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2023-01-29T13:17:42")
@StaticMetamodel(Run.class)
public class Run_ { 

    public static volatile SingularAttribute<Run, String> estatusArchivos;
    public static volatile SingularAttribute<Run, Date> runStartday;
    public static volatile SingularAttribute<Run, Integer> idRun;
    public static volatile SingularAttribute<Run, String> runType;
    public static volatile SingularAttribute<Run, String> workSubdirectory;
    public static volatile SingularAttribute<Run, Integer> cycles;
    public static volatile SingularAttribute<Run, Plataform> idPlataform;
    public static volatile SingularAttribute<Run, String> sampleSheetName;
    public static volatile SingularAttribute<Run, String> performance;
    public static volatile ListAttribute<Run, LibraryRunLink> libraryRunLinkList;
    public static volatile SingularAttribute<Run, String> runName;
    public static volatile SingularAttribute<Run, Date> runFinishday;
    public static volatile SingularAttribute<Run, String> status;

}