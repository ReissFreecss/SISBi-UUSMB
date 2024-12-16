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
public interface UsersFacadeSessionLocal {

    void create(Users users);

    void edit(Users users);

    void remove(Users users);

    Users find(Object id);

    List<Users> findAll();

    List<Users> findRange(int[] range);

    int count();
    
    Users iniciarSesion(Users us);
    
    List<Project> projectUser(Users us);
    
    
    
}
