/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author omarflores
 */
@Entity
@Table(name = "plataform")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Plataform.findAll", query = "SELECT p FROM Plataform p"),
    @NamedQuery(name = "Plataform.findByIdPlataform", query = "SELECT p FROM Plataform p WHERE p.idPlataform = :idPlataform"),
    @NamedQuery(name = "Plataform.findByPlataformName", query = "SELECT p FROM Plataform p WHERE p.plataformName = :plataformName"),
    @NamedQuery(name = "Plataform.findByNote", query = "SELECT p FROM Plataform p WHERE p.note = :note"),
    @NamedQuery(name = "Plataform.findByLocation", query = "SELECT p FROM Plataform p WHERE p.location = :location")})
public class Plataform implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    //@NotNull
    @Column(name = "id_plataform")
    private Integer idPlataform;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "plataform_name")
    private String plataformName;
    @Size(max = 1000)
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "location")
    private String location;
    @OneToMany(mappedBy = "idPlataform")
    private List<Run> runList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plataform")
    private List<PlataformLinkKit> plataformLinkKitList;

    public Plataform() {
    }

    public Plataform(Integer idPlataform) {
        this.idPlataform = idPlataform;
    }

    //agrege al constructor note
    public Plataform(Integer idPlataform, String plataformName, String note, String location) {
        this.idPlataform = idPlataform;
        this.plataformName = plataformName;
        this.note=note;
        this.location = location;
    }

    public Integer getIdPlataform() {
        return idPlataform;
    }

    public void setIdPlataform(Integer idPlataform) {
        this.idPlataform = idPlataform;
    }

    public String getPlataformName() {
        return plataformName;
    }

    public void setPlataformName(String plataformName) {
        this.plataformName = plataformName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @XmlTransient
    public List<Run> getRunList() {
        return runList;
    }

    public void setRunList(List<Run> runList) {
        this.runList = runList;
    }

    @XmlTransient
    public List<PlataformLinkKit> getPlataformLinkKitList() {
        return plataformLinkKitList;
    }

    public void setPlataformLinkKitList(List<PlataformLinkKit> plataformLinkKitList) {
        this.plataformLinkKitList = plataformLinkKitList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlataform != null ? idPlataform.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Plataform)) {
            return false;
        }
        Plataform other = (Plataform) object;
        if ((this.idPlataform == null && other.idPlataform != null) || (this.idPlataform != null && !this.idPlataform.equals(other.idPlataform))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Plataform[ idPlataform=" + idPlataform + " ]";
    }
    
}
