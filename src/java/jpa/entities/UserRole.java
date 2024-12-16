/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "user_role")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserRole.findAll", query = "SELECT u FROM UserRole u")
    , @NamedQuery(name = "UserRole.findByFirstName", query = "SELECT u FROM UserRole u WHERE u.firstName = :firstName")
    , @NamedQuery(name = "UserRole.findByPLastName", query = "SELECT u FROM UserRole u WHERE u.pLastName = :pLastName")
    , @NamedQuery(name = "UserRole.findByMLastName", query = "SELECT u FROM UserRole u WHERE u.mLastName = :mLastName")
    , @NamedQuery(name = "UserRole.findByUserName", query = "SELECT u FROM UserRole u WHERE u.userName = :userName")
    , @NamedQuery(name = "UserRole.findByDependencyName", query = "SELECT u FROM UserRole u WHERE u.dependencyName = :dependencyName")
    , @NamedQuery(name = "UserRole.findByRole", query = "SELECT u FROM UserRole u WHERE u.role = :role")
    , @NamedQuery(name = "UserRole.findByIdProject", query = "SELECT u FROM UserRole u WHERE u.idProject = :idProject")})
public class UserRole implements Serializable {

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @Size(max = 50)
    @Column(name = "phone_number")
    private String phoneNumber;

    private static final long serialVersionUID = 1L;
    @Size(max = 100)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 200)
    @Column(name = "p_last_name")
    private String pLastName;
    @Size(max = 200)
    @Column(name = "m_last_name")
    private String mLastName;
    @Size(max = 45)
    @Column(name = "user_name")
    @Id
    private String userName;
    @Size(max = 100)
    @Column(name = "dependency_name")
    private String dependencyName;
    @Size(max = 45)
    @Column(name = "role")
    private String role;
    @Size(max = 50)
    @Column(name = "id_project")
    private String idProject;

    public UserRole() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPLastName() {
        return pLastName;
    }

    public void setPLastName(String pLastName) {
        this.pLastName = pLastName;
    }

    public String getMLastName() {
        return mLastName;
    }

    public void setMLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDependencyName() {
        return dependencyName;
    }

    public void setDependencyName(String dependencyName) {
        this.dependencyName = dependencyName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
}
