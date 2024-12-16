/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author omarflores
 */
@Entity
@Table(name = "user_role_report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserRoleReport.findAll", query = "SELECT u FROM UserRoleReport u")
    , @NamedQuery(name = "UserRoleReport.findByIdUserRoleReport", query = "SELECT u FROM UserRoleReport u WHERE u.idUserRoleReport = :idUserRoleReport")
    , @NamedQuery(name = "UserRoleReport.findByTocreate", query = "SELECT u FROM UserRoleReport u WHERE u.tocreate = :tocreate")
    , @NamedQuery(name = "UserRoleReport.findByTorevise", query = "SELECT u FROM UserRoleReport u WHERE u.torevise = :torevise")
    , @NamedQuery(name = "UserRoleReport.findByToaauthorize", query = "SELECT u FROM UserRoleReport u WHERE u.toaauthorize = :toaauthorize")
    , @NamedQuery(name = "UserRoleReport.findByPathimage", query = "SELECT u FROM UserRoleReport u WHERE u.pathimage = :pathimage")})
public class UserRoleReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_user_role_report")
    private Integer idUserRoleReport;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tocreate")
    private boolean tocreate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "torevise")
    private boolean torevise;
    @Basic(optional = false)
    @NotNull
    @Column(name = "toaauthorize")
    private boolean toaauthorize;
    @Size(max = 50)
    @Column(name = "pathimage")
    private String pathimage;
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ManyToOne(optional = false)
    private Users idUser;

    public UserRoleReport() {
    }

    public UserRoleReport(Integer idUserRoleReport) {
        this.idUserRoleReport = idUserRoleReport;
    }

    public UserRoleReport(Integer idUserRoleReport, boolean tocreate, boolean torevise, boolean toaauthorize) {
        this.idUserRoleReport = idUserRoleReport;
        this.tocreate = tocreate;
        this.torevise = torevise;
        this.toaauthorize = toaauthorize;
    }

    public Integer getIdUserRoleReport() {
        return idUserRoleReport;
    }

    public void setIdUserRoleReport(Integer idUserRoleReport) {
        this.idUserRoleReport = idUserRoleReport;
    }

    public boolean getTocreate() {
        return tocreate;
    }

    public void setTocreate(boolean tocreate) {
        this.tocreate = tocreate;
    }

    public boolean getTorevise() {
        return torevise;
    }

    public void setTorevise(boolean torevise) {
        this.torevise = torevise;
    }

    public boolean getToaauthorize() {
        return toaauthorize;
    }

    public void setToaauthorize(boolean toaauthorize) {
        this.toaauthorize = toaauthorize;
    }

    public String getPathimage() {
        return pathimage;
    }

    public void setPathimage(String pathimage) {
        this.pathimage = pathimage;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUserRoleReport != null ? idUserRoleReport.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserRoleReport)) {
            return false;
        }
        UserRoleReport other = (UserRoleReport) object;
        if ((this.idUserRoleReport == null && other.idUserRoleReport != null) || (this.idUserRoleReport != null && !this.idUserRoleReport.equals(other.idUserRoleReport))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.UserRoleReport[ idUserRoleReport=" + idUserRoleReport + " ]";
    }
    
}
