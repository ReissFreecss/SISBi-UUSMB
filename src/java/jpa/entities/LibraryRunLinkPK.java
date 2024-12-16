/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author omarflores
 */
@Embeddable
public class LibraryRunLinkPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_library")
    private int id_library;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_run")
    private int idRun;

    public LibraryRunLinkPK() {
    }

    public LibraryRunLinkPK(int idLibrary, int idRun) {
        this.id_library = idLibrary;
        this.idRun = idRun;
    }

    public int getIdLibrary() {
        return id_library;
    }

    public void setIdLibrary(int idLibrary) {
        this.id_library = idLibrary;
    }

    public int getIdRun() {
        return idRun;
    }

    public void setIdRun(int idRun) {
        this.idRun = idRun;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id_library;
        hash += (int) idRun;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LibraryRunLinkPK)) {
            return false;
        }
        LibraryRunLinkPK other = (LibraryRunLinkPK) object;
        if (this.id_library != other.id_library) {
            return false;
        }
        if (this.idRun != other.idRun) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.LibraryRunLinkPK[ id_library=" + id_library + ", idRun=" + idRun + " ]";
    }
    
}
