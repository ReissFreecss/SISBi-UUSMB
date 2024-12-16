/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author aaron
 */
@Embeddable
public class SampleLibraryLinkPK implements Serializable {

 
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_sample")
    private int idSample;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_library")
    private int id_library;
  

    public SampleLibraryLinkPK() {
    }

    public SampleLibraryLinkPK(int idSample, int idLibrary) {
        this.idSample = idSample;
        this.id_library = idLibrary;
        
    }

    public int getIdSample() {
        return idSample;
    }

    public void setIdSample(int idSample) {
        this.idSample = idSample;
    }

    public int getIdLibrary() {
        return id_library;
    }

    public void setIdLibrary(int idLibrary) {
        this.id_library = idLibrary;
    }



    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.idSample;
        hash = 23 * hash + this.id_library;
      
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SampleLibraryLinkPK other = (SampleLibraryLinkPK) obj;
        if (this.idSample != other.idSample) {
            return false;
        }
        if (this.id_library != other.id_library) {
            return false;
        }
    
        return true;
    }

    @Override
    public String toString() {
        return "SampleLibraryLinkPK{" + "idSample=" + idSample + ", id_library=" + id_library + '}';
    }

 

  
    
}
