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
 * @author aaron
 */
@Embeddable
public class BioinformaticAnalysisSampleLinkPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_analysis")
    private int idAnalysis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_sample")
    private int idSample;

    public BioinformaticAnalysisSampleLinkPK() {
    }

    public BioinformaticAnalysisSampleLinkPK(int idAnalysis, int idSample) {
        this.idAnalysis = idAnalysis;
        this.idSample = idSample;
    }

    public int getIdAnalysis() {
        return idAnalysis;
    }

    public void setIdAnalysis(int idAnalysis) {
        this.idAnalysis = idAnalysis;
    }

    public int getIdSample() {
        return idSample;
    }

    public void setIdSample(int idSample) {
        this.idSample = idSample;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idAnalysis;
        hash += (int) idSample;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BioinformaticAnalysisSampleLinkPK)) {
            return false;
        }
        BioinformaticAnalysisSampleLinkPK other = (BioinformaticAnalysisSampleLinkPK) object;
        if (this.idAnalysis != other.idAnalysis) {
            return false;
        }
        if (this.idSample != other.idSample) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.BioinformaticAnalysisSampleLinkPK[ idAnalysis=" + idAnalysis + ", idSample=" + idSample + " ]";
    }
    
}
