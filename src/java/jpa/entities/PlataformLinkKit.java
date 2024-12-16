/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author omarflores
 */
@Entity
@Table(name = "plataform_link_kit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlataformLinkKit.findAll", query = "SELECT p FROM PlataformLinkKit p"),
    @NamedQuery(name = "PlataformLinkKit.findByIdPlataform", query = "SELECT p FROM PlataformLinkKit p WHERE p.plataformLinkKitPK.idPlataform = :idPlataform"),
    @NamedQuery(name = "PlataformLinkKit.findByIdKit", query = "SELECT p FROM PlataformLinkKit p WHERE p.plataformLinkKitPK.idKit = :idKit"),
    @NamedQuery(name = "PlataformLinkKit.findByNotes", query = "SELECT p FROM PlataformLinkKit p WHERE p.notes = :notes")})
public class PlataformLinkKit implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlataformLinkKitPK plataformLinkKitPK;
    @Size(max = 500)
    @Column(name = "notes")
    private String notes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plataformLinkKit")
    private List<Library> libraryList;
    @JoinColumn(name = "id_kit", referencedColumnName = "id_kit", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Kit kit;
    @JoinColumn(name = "id_plataform", referencedColumnName = "id_plataform", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Plataform plataform;

    public PlataformLinkKit() {
    }

    public PlataformLinkKit(PlataformLinkKitPK plataformLinkKitPK) {
        this.plataformLinkKitPK = plataformLinkKitPK;
    }

    public PlataformLinkKit(int idPlataform, int idKit) {
        this.plataformLinkKitPK = new PlataformLinkKitPK(idPlataform, idKit);
    }

    public PlataformLinkKitPK getPlataformLinkKitPK() {
        return plataformLinkKitPK;
    }

    public void setPlataformLinkKitPK(PlataformLinkKitPK plataformLinkKitPK) {
        this.plataformLinkKitPK = plataformLinkKitPK;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @XmlTransient
    public List<Library> getLibraryList() {
        return libraryList;
    }

    public void setLibraryList(List<Library> libraryList) {
        this.libraryList = libraryList;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public Plataform getPlataform() {
        return plataform;
    }

    public void setPlataform(Plataform plataform) {
        this.plataform = plataform;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plataformLinkKitPK != null ? plataformLinkKitPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlataformLinkKit)) {
            return false;
        }
        PlataformLinkKit other = (PlataformLinkKit) object;
        if ((this.plataformLinkKitPK == null && other.plataformLinkKitPK != null) || (this.plataformLinkKitPK != null && !this.plataformLinkKitPK.equals(other.plataformLinkKitPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.PlataformLinkKit[ plataformLinkKitPK=" + plataformLinkKitPK + " ]";
    }
    
}
