/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author omarflores
 */
@Entity
@Table(name = "library")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Library.findAll", query = "SELECT l FROM Library l"),
    @NamedQuery(name = "Library.findByIdLibrary", query = "SELECT l FROM Library l WHERE l.id_library = :idLibrary"),
    @NamedQuery(name = "Library.findByLibraryName", query = "SELECT l FROM Library l WHERE l.libraryName = :libraryName"),
    @NamedQuery(name = "Library.findByPreparationStatus", query = "SELECT l FROM Library l WHERE l.preparationStatus = :preparationStatus"),
    @NamedQuery(name = "Library.findByPreparationDate", query = "SELECT l FROM Library l WHERE l.preparationDate = :preparationDate"),
    @NamedQuery(name = "Library.findByQpcrConcentration", query = "SELECT l FROM Library l WHERE l.qpcrConcentration = :qpcrConcentration"),
    @NamedQuery(name = "Library.findByNanomolarConcentration", query = "SELECT l FROM Library l WHERE l.nanomolarConcentration = :nanomolarConcentration"),
    @NamedQuery(name = "Library.findByLibrarySize", query = "SELECT l FROM Library l WHERE l.librarySize = :librarySize"),
    @NamedQuery(name = "Library.findByQubitConcentration", query = "SELECT l FROM Library l WHERE l.qubitConcentration = :qubitConcentration"),
    @NamedQuery(name = "Library.findByUserName", query = "SELECT l FROM Library l WHERE l.userName = :userName")})
public class Library implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JoinColumn(name = "idLibrary", referencedColumnName = "id_library")
    private Integer id_library;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "library_name")
    private String libraryName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "preparation_status")
    private String preparationStatus;
    @Column(name = "preparation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date preparationDate;
    @Column(name = "qpcr_concentration")
    private Double qpcrConcentration;
    @Column(name = "nanomolar_concentration")
    private Double nanomolarConcentration;
    @Column(name = "library_size")
    private Integer librarySize;
    @Column(name = "qubit_concentration")
    private Double qubitConcentration;
    @Size(max = 45)
    @Column(name = "user_name")
    private String userName;
    @JoinColumn(name = "id_barcode_1", referencedColumnName = "id_barcode")
    @ManyToOne
    private Barcodes idBarcode1;
    @JoinColumn(name = "id_barcode_2", referencedColumnName = "id_barcode")
    @ManyToOne
    private Barcodes idBarcode2;
    @JoinColumns({
        @JoinColumn(name = "id_plataform", referencedColumnName = "id_plataform"),
        @JoinColumn(name = "id_kit", referencedColumnName = "id_kit")})
    @ManyToOne(optional = false)
    private PlataformLinkKit plataformLinkKit;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "library")
    private List<LibraryRunLink> libraryRunLinkList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "library")
    private List<SampleLibraryLink> sampleLibraryList;

    public List<SampleLibraryLink> getSampleLibraryList() {
        return sampleLibraryList;
    }

    public void setSampleLibraryList(List<SampleLibraryLink> sampleLibraryList) {
        this.sampleLibraryList = sampleLibraryList;
    }
    
    

    public Library() {
    }

    public Library(Integer idLibrary) {
        this.id_library = idLibrary;
    }

    public Library(Integer idLibrary, String libraryName, String preparationStatus) {
        this.id_library = idLibrary;
        this.libraryName = libraryName;
        this.preparationStatus = preparationStatus;
    }

    public Integer getIdLibrary() {
        return id_library;
    }

    public void setIdLibrary(Integer idLibrary) {
        this.id_library = idLibrary;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getPreparationStatus() {
        return preparationStatus;
    }

    public void setPreparationStatus(String preparationStatus) {
        this.preparationStatus = preparationStatus;
    }

    public Date getPreparationDate() {
        return preparationDate;
    }

    public void setPreparationDate(Date preparationDate) {
        this.preparationDate = preparationDate;
    }

    public Double getQpcrConcentration() {
        return qpcrConcentration;
    }

    public void setQpcrConcentration(Double qpcrConcentration) {
        this.qpcrConcentration = qpcrConcentration;
    }

    public Double getNanomolarConcentration() {
        return nanomolarConcentration;
    }

    public void setNanomolarConcentration(Double nanomolarConcentration) {
        this.nanomolarConcentration = nanomolarConcentration;
    }

    public Integer getLibrarySize() {
        return librarySize;
    }

    public void setLibrarySize(Integer librarySize) {
        this.librarySize = librarySize;
    }

    public Double getQubitConcentration() {
        return qubitConcentration;
    }

    public void setQubitConcentration(Double qubitConcentration) {
        this.qubitConcentration = qubitConcentration;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Barcodes getIdBarcode1() {
        return idBarcode1;
    }

    public void setIdBarcode1(Barcodes idBarcode1) {
        this.idBarcode1 = idBarcode1;
    }

    public Barcodes getIdBarcode2() {
        return idBarcode2;
    }

    public void setIdBarcode2(Barcodes idBarcode2) {
        this.idBarcode2 = idBarcode2;
    }

    public PlataformLinkKit getPlataformLinkKit() {
        return plataformLinkKit;
    }

    public void setPlataformLinkKit(PlataformLinkKit plataformLinkKit) {
        this.plataformLinkKit = plataformLinkKit;
    }

    @XmlTransient
    public List<LibraryRunLink> getLibraryRunLinkList() {
        return libraryRunLinkList;
    }

    public void setLibraryRunLinkList(List<LibraryRunLink> libraryRunLinkList) {
        this.libraryRunLinkList = libraryRunLinkList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id_library != null ? id_library.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Library)) {
            return false;
        }
        Library other = (Library) object;
        if ((this.id_library == null && other.id_library != null) || (this.id_library != null && !this.id_library.equals(other.id_library))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Library[ id_library=" + id_library + " ]";
    }
    
}
