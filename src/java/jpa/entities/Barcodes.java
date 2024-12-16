/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.List;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author omarflores
 */
@Entity
@Table(name = "barcodes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Barcodes.findAll", query = "SELECT b FROM Barcodes b"),
    @NamedQuery(name = "Barcodes.findByIdBarcode", query = "SELECT b FROM Barcodes b WHERE b.idBarcode = :idBarcode"),
    @NamedQuery(name = "Barcodes.findByIndexName", query = "SELECT b FROM Barcodes b WHERE b.indexName = :indexName"),
    @NamedQuery(name = "Barcodes.findByBasei7", query = "SELECT b FROM Barcodes b WHERE b.basei7 = :basei7"),
    @NamedQuery(name = "Barcodes.findByBasei5Miseq", query = "SELECT b FROM Barcodes b WHERE b.basei5Miseq = :basei5Miseq"),
    @NamedQuery(name = "Barcodes.findByBasei5Nextseq", query = "SELECT b FROM Barcodes b WHERE b.basei5Nextseq = :basei5Nextseq"),
    @NamedQuery(name = "Barcodes.findByNotes", query = "SELECT b FROM Barcodes b WHERE b.notes = :notes")})
public class Barcodes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_barcode")
    private Integer idBarcode;
    @Size(max = 50)
    @Column(name = "index_name")
    private String indexName;
    @Size(max = 50)
    @Column(name = "basei7")
    private String basei7;
    @Size(max = 50)
    @Column(name = "basei5_miseq")
    private String basei5Miseq;
    @Size(max = 50)
    @Column(name = "basei5_nextseq")
    private String basei5Nextseq;
    @Size(max = 300)
    @Column(name = "notes")
    private String notes;
    @OneToMany(mappedBy = "idBarcode1")
    private List<Library> libraryList;
    @OneToMany(mappedBy = "idBarcode2")
    private List<Library> libraryList1;
    @JoinColumn(name = "id_kit", referencedColumnName = "id_kit")
    @ManyToOne
    private Kit idKit;

    public Barcodes() {
    }

    public Barcodes(Integer idBarcode) {
        this.idBarcode = idBarcode;
    }

    public Integer getIdBarcode() {
        return idBarcode;
    }

    public void setIdBarcode(Integer idBarcode) {
        this.idBarcode = idBarcode;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getBasei7() {
        return basei7;
    }

    public void setBasei7(String basei7) {
        this.basei7 = basei7;
    }

    public String getBasei5Miseq() {
        return basei5Miseq;
    }

    public void setBasei5Miseq(String basei5Miseq) {
        this.basei5Miseq = basei5Miseq;
    }

    public String getBasei5Nextseq() {
        return basei5Nextseq;
    }

    public void setBasei5Nextseq(String basei5Nextseq) {
        this.basei5Nextseq = basei5Nextseq;
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

    @XmlTransient
    public List<Library> getLibraryList1() {
        return libraryList1;
    }

    public void setLibraryList1(List<Library> libraryList1) {
        this.libraryList1 = libraryList1;
    }

    public Kit getIdKit() {
        return idKit;
    }

    public void setIdKit(Kit idKit) {
        this.idKit = idKit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBarcode != null ? idBarcode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Barcodes)) {
            return false;
        }
        Barcodes other = (Barcodes) object;
        if ((this.idBarcode == null && other.idBarcode != null) || (this.idBarcode != null && !this.idBarcode.equals(other.idBarcode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Barcodes[ idBarcode=" + idBarcode + " ]";
    }
    
}
