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
 * @author aaron
 */
@Entity
@Table(name = "sample_genome_link")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SampleGenomeLink.findAll", query = "SELECT s FROM SampleGenomeLink s")
    , @NamedQuery(name = "SampleGenomeLink.findByIdSample", query = "SELECT s FROM SampleGenomeLink s WHERE s.sampleGenomeLinkPK.idSample = :idSample")
    , @NamedQuery(name = "SampleGenomeLink.findByIdGenome", query = "SELECT s FROM SampleGenomeLink s WHERE s.sampleGenomeLinkPK.idGenome = :idGenome")
    , @NamedQuery(name = "SampleGenomeLink.findByType", query = "SELECT s FROM SampleGenomeLink s WHERE s.sampleGenomeLinkPK.type = :type")
    , @NamedQuery(name = "SampleGenomeLink.findByNote", query = "SELECT s FROM SampleGenomeLink s WHERE s.note = :note")})
public class SampleGenomeLink implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SampleGenomeLinkPK sampleGenomeLinkPK;
    @Size(max = 255)
    @Column(name = "note")
    private String note;
    @JoinColumn(name = "id_genome", referencedColumnName = "id_genome", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Genome genome;
    @JoinColumn(name = "id_sample", referencedColumnName = "id_sample", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Sample sample;

 

  
    public SampleGenomeLink() {
    }

    public SampleGenomeLink(SampleGenomeLinkPK sampleGenomeLinkPK) {
        this.sampleGenomeLinkPK = sampleGenomeLinkPK;
    }

    public SampleGenomeLink(int idSample, String idGenome, String type) {
        this.sampleGenomeLinkPK = new SampleGenomeLinkPK(idSample, idGenome, type);
    }

    public SampleGenomeLinkPK getSampleGenomeLinkPK() {
        return sampleGenomeLinkPK;
    }

    public void setSampleGenomeLinkPK(SampleGenomeLinkPK sampleGenomeLinkPK) {
        this.sampleGenomeLinkPK = sampleGenomeLinkPK;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Genome getGenome() {
        return genome;
    }

    public void setGenome(Genome genome) {
        this.genome = genome;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sampleGenomeLinkPK != null ? sampleGenomeLinkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SampleGenomeLink)) {
            return false;
        }
        SampleGenomeLink other = (SampleGenomeLink) object;
        if ((this.sampleGenomeLinkPK == null && other.sampleGenomeLinkPK != null) || (this.sampleGenomeLinkPK != null && !this.sampleGenomeLinkPK.equals(other.sampleGenomeLinkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.SampleGenomeLink[ sampleGenomeLinkPK=" + sampleGenomeLinkPK + " ]"+sampleGenomeLinkPK.getIdGenome();
    }
    
}
