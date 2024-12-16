/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "sample_library_link")
@XmlRootElement
public class SampleLibraryLink implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SampleLibraryLinkPK sampleLibraryLinkPK;
    @Size(max = 100)
    @Column(name = "note")
    private String note;
    
    @JoinColumn(name = "id_library", referencedColumnName = "id_library", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Library library;
    
    @JoinColumn(name = "id_sample", referencedColumnName = "id_sample", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Sample sample;

    public SampleLibraryLink() {
    }

    public SampleLibraryLinkPK getSampleLibraryLinkPK() {
        return sampleLibraryLinkPK;
    }

    public void setSampleLibraryLinkPK(SampleLibraryLinkPK sampleLibraryLinkPK) {
        this.sampleLibraryLinkPK = sampleLibraryLinkPK;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.note);
        hash = 97 * hash + Objects.hashCode(this.library);
        hash = 97 * hash + Objects.hashCode(this.sample);
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
        final SampleLibraryLink other = (SampleLibraryLink) obj;
        if (!Objects.equals(this.note, other.note)) {
            return false;
        }
        if (!Objects.equals(this.library, other.library)) {
            return false;
        }
        if (!Objects.equals(this.sample, other.sample)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "SampleLibraryLink{" + "sampleLibraryLinkPK=" + sampleLibraryLinkPK + ", note=" + note + ", library=" + library + ", sample=" + sample + '}';
    }
    
    
    
    
       
}
