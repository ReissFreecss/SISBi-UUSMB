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
import javax.validation.constraints.Size;

/**
 *
 * @author aaron
 */
@Embeddable
public class SampleGenomeLinkPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_sample")
    private int idSample;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_genome")
    private String idGenome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "type")
    private String type;

    public SampleGenomeLinkPK() {
    }

    public SampleGenomeLinkPK(int idSample, String idGenome, String type) {
        this.idSample = idSample;
        this.idGenome = idGenome;
        this.type = type;
    }

    public int getIdSample() {
        return idSample;
    }

    public void setIdSample(int idSample) {
        this.idSample = idSample;
    }

    public String getIdGenome() {
        return idGenome;
    }

    public void setIdGenome(String idGenome) {
        this.idGenome = idGenome;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idSample;
        hash += (idGenome != null ? idGenome.hashCode() : 0);
        hash += (type != null ? type.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SampleGenomeLinkPK)) {
            return false;
        }
        SampleGenomeLinkPK other = (SampleGenomeLinkPK) object;
        if (this.idSample != other.idSample) {
            return false;
        }
        if (this.idGenome != other.idGenome) {
            return false;
        }
        if ((this.type == null && other.type != null) || (this.type != null && !this.type.equals(other.type))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.SampleGenomeLinkPK[ idSample=" + idSample + ", idGenome=" + idGenome + ", type=" + type + " ]";
    }
    
}
