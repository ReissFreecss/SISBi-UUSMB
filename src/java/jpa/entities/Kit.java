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
@Table(name = "kit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kit.findAll", query = "SELECT k FROM Kit k"),
    @NamedQuery(name = "Kit.findByIdKit", query = "SELECT k FROM Kit k WHERE k.idKit = :idKit"),
    @NamedQuery(name = "Kit.findByKitName", query = "SELECT k FROM Kit k WHERE k.kitName = :kitName"),
    @NamedQuery(name = "Kit.findByNote", query = "SELECT k FROM Kit k WHERE k.note = :note")})
public class Kit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_kit")
    private Integer idKit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "kit_name")
    private String kitName;
    @Size(max = 1000)
    @Column(name = "note")
    private String note;
    @OneToMany(mappedBy = "idKit")
    private List<Barcodes> barcodesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kit")
    private List<PlataformLinkKit> plataformLinkKitList;

    public Kit() {
    }

    public Kit(Integer idKit) {
        this.idKit = idKit;
    }

    public Kit(Integer idKit, String kitName) {
        this.idKit = idKit;
        this.kitName = kitName;
    }

    public Integer getIdKit() {
        return idKit;
    }

    public void setIdKit(Integer idKit) {
        this.idKit = idKit;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @XmlTransient
    public List<Barcodes> getBarcodesList() {
        return barcodesList;
    }

    public void setBarcodesList(List<Barcodes> barcodesList) {
        this.barcodesList = barcodesList;
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
        hash += (idKit != null ? idKit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kit)) {
            return false;
        }
        Kit other = (Kit) object;
        if ((this.idKit == null && other.idKit != null) || (this.idKit != null && !this.idKit.equals(other.idKit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Kit[ idKit=" + idKit + " ]";
    }
    
}
