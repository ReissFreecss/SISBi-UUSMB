/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "user_project_link")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserProjectLink.findAll", query = "SELECT u FROM UserProjectLink u")
    , @NamedQuery(name = "UserProjectLink.findByIdUser", query = "SELECT u FROM UserProjectLink u WHERE u.userProjectLinkPK.idUser = :idUser")
    , @NamedQuery(name = "UserProjectLink.findByIdProject", query = "SELECT u FROM UserProjectLink u WHERE u.userProjectLinkPK.idProject = :idProject")
    , @NamedQuery(name = "UserProjectLink.findByNote", query = "SELECT u FROM UserProjectLink u WHERE u.note = :note")
    , @NamedQuery(name = "UserProjectLink.findByRole", query = "SELECT u FROM UserProjectLink u WHERE u.role = :role")})
public class UserProjectLink implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserProjectLinkPK userProjectLinkPK;
    @Size(max = 255)
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    
    @Size(min = 1, max = 45)
    @Column(name = "role")
    private String role;
    @JoinColumn(name = "id_project", referencedColumnName = "id_project", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Project project;
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;

    public UserProjectLink() {
    }

    public UserProjectLink(UserProjectLinkPK userProjectLinkPK) {
        this.userProjectLinkPK = userProjectLinkPK;
    }

    public UserProjectLink(UserProjectLinkPK userProjectLinkPK, String role) {
        this.userProjectLinkPK = userProjectLinkPK;
        this.role = role;
    }

    public UserProjectLink(int idUser, String idProject) {
        this.userProjectLinkPK = new UserProjectLinkPK(idUser, idProject);
    }

    public UserProjectLinkPK getUserProjectLinkPK() {
        return userProjectLinkPK;
    }

    public void setUserProjectLinkPK(UserProjectLinkPK userProjectLinkPK) {
        this.userProjectLinkPK = userProjectLinkPK;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userProjectLinkPK != null ? userProjectLinkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserProjectLink)) {
            return false;
        }
        UserProjectLink other = (UserProjectLink) object;
        if ((this.userProjectLinkPK == null && other.userProjectLinkPK != null) || (this.userProjectLinkPK != null && !this.userProjectLinkPK.equals(other.userProjectLinkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.UserProjectLink[ userProjectLinkPK=" + userProjectLinkPK + " ]";
    }
    
}
