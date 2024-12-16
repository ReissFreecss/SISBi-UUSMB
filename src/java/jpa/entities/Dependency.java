/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "dependency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dependency.findAll", query = "SELECT d FROM Dependency d")
    , @NamedQuery(name = "Dependency.findByIdDependency", query = "SELECT d FROM Dependency d WHERE d.idDependency = :idDependency")
    , @NamedQuery(name = "Dependency.findByStreet", query = "SELECT d FROM Dependency d WHERE d.street = :street")
    , @NamedQuery(name = "Dependency.findByLocation", query = "SELECT d FROM Dependency d WHERE d.location = :location")
    , @NamedQuery(name = "Dependency.findByPostalCode", query = "SELECT d FROM Dependency d WHERE d.postalCode = :postalCode")
    , @NamedQuery(name = "Dependency.findByType", query = "SELECT d FROM Dependency d WHERE d.type = :type")
    , @NamedQuery(name = "Dependency.findByDependencyName", query = "SELECT d FROM Dependency d WHERE d.dependencyName = :dependencyName")
    , @NamedQuery(name = "Dependency.findByInstitution", query = "SELECT d FROM Dependency d WHERE d.institution = :institution")
    , @NamedQuery(name = "Dependency.findByAcronym", query = "SELECT d FROM Dependency d WHERE d.acronym = :acronym")})
public class Dependency implements Serializable {

    private static final long serialVersionUID = 1L;
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_dependency")
    private Integer idDependency;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "street")
    private String street;
    @Size(max = 50)
    @Column(name = "location")
    private String location;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "postal_code")
    private String postalCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dependency_name")
    private String dependencyName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "institution")
    private String institution;
    @Size(max = 10)
    @Column(name = "acronym")
    private String acronym;
    
    @JoinColumn(name = "id_city", referencedColumnName = "id_city")
    @ManyToOne(optional = false)
    private City idCity;
    
    @OneToMany(mappedBy = "idFiscalAddress")
    private Collection<Users> usersCollection;
    @OneToMany(mappedBy = "idDependency")
    private Collection<Users> usersCollection1;

    public Dependency() {
    }

    public Dependency(Integer idDependency) {
        this.idDependency = idDependency;
    }

    public Dependency(Integer idDependency, String street, String postalCode, String type, String dependencyName, String institution) {
        this.idDependency = idDependency;
        this.street = street;
        this.postalCode = postalCode;
        this.type = type;
        this.dependencyName = dependencyName;
        this.institution = institution;
    }

    public Integer getIdDependency() {
        return idDependency;
    }

    public void setIdDependency(Integer idDependency) {
        this.idDependency = idDependency;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDependencyName() {
        return dependencyName;
    }

    public void setDependencyName(String dependencyName) {
        this.dependencyName = dependencyName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public City getIdCity() {
        return idCity;
    }

    public void setIdCity(City idCity) {
        this.idCity = idCity;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection1() {
        return usersCollection1;
    }

    public void setUsersCollection1(Collection<Users> usersCollection1) {
        this.usersCollection1 = usersCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDependency != null ? idDependency.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dependency)) {
            return false;
        }
        Dependency other = (Dependency) object;
        if ((this.idDependency == null && other.idDependency != null) || (this.idDependency != null && !this.idDependency.equals(other.idDependency))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String Depen= dependencyName+" - "+institution;
        if(acronym!=null){
        Depen+=" - "+acronym;
        }
        
        return Depen;
    }
    
}
