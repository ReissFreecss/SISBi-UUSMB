/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.session;

import java.util.List;
import javax.ejb.Local;
import jpa.entities.Project;
import jpa.entities.Users;

/**
 *
 * @author aaron
 */
@Local
public interface ProjectFacadeSessionLocal {

    void create(Project project);

    void edit(Project project);

    void remove(Project project);

    Project find(Object id);

    List<Project> findAll();

    List<Project> findRange(int[] range);

    int count();
    
    List<Project> findSome(Users us);
    
    Project seveCurrentProject(Project project);
    
    
    
    
}
