/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author omarflores
 */
@Entity
@Table(name = "library_run_link")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LibraryRunLink.findAll", query = "SELECT l FROM LibraryRunLink l"),
    @NamedQuery(name = "LibraryRunLink.findByIdLibrary", query = "SELECT l FROM LibraryRunLink l WHERE l.libraryRunLinkPK.id_library = :idLibrary"),
    @NamedQuery(name = "LibraryRunLink.findByIdRun", query = "SELECT l FROM LibraryRunLink l WHERE l.libraryRunLinkPK.idRun = :idRun"),
    @NamedQuery(name = "LibraryRunLink.findByNote", query = "SELECT l FROM LibraryRunLink l WHERE l.note = :note"),
    @NamedQuery(name = "LibraryRunLink.findByRealPerformance", query = "SELECT l FROM LibraryRunLink l WHERE l.realPerformance = :realPerformance")})
public class LibraryRunLink implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LibraryRunLinkPK libraryRunLinkPK;
    @Size(max = 100)
    @Column(name = "note")
    private String note;
    @Column(name = "real_performance")
    private Integer realPerformance;
    @JoinColumn(name = "id_library", referencedColumnName = "id_library", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Library library;
    @JoinColumn(name = "id_run", referencedColumnName = "id_run", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Run run;

    public LibraryRunLink() {
    }

    public LibraryRunLink(LibraryRunLinkPK libraryRunLinkPK) {
        this.libraryRunLinkPK = libraryRunLinkPK;
    }

    public LibraryRunLink(int idLibrary, int idRun) {
        this.libraryRunLinkPK = new LibraryRunLinkPK(idLibrary, idRun);
    }

    public LibraryRunLinkPK getLibraryRunLinkPK() {
        return libraryRunLinkPK;
    }

    public void setLibraryRunLinkPK(LibraryRunLinkPK libraryRunLinkPK) {
        this.libraryRunLinkPK = libraryRunLinkPK;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getRealPerformance() {
        return realPerformance;
    }

    public void setRealPerformance(Integer realPerformance) {
        this.realPerformance = realPerformance;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Run getRun() {
        return run;
    }

    public void setRun(Run run) {
        this.run = run;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (libraryRunLinkPK != null ? libraryRunLinkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LibraryRunLink)) {
            return false;
        }
        LibraryRunLink other = (LibraryRunLink) object;
        if ((this.libraryRunLinkPK == null && other.libraryRunLinkPK != null) || (this.libraryRunLinkPK != null && !this.libraryRunLinkPK.equals(other.libraryRunLinkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.LibraryRunLink[ libraryRunLinkPK=" + libraryRunLinkPK + " ]";
    }
    
}
