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
 * @author uusmb
 */
@Embeddable
public class BioinformaticsReportsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_bioinformatics_reports")
    private int idBioinformaticsReports;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_analysis")
    private int idAnalysis;

    public BioinformaticsReportsPK() {
    }

    public BioinformaticsReportsPK(int idBioinformaticsReports, int idAnalysis) {
        this.idBioinformaticsReports = idBioinformaticsReports;
        this.idAnalysis = idAnalysis;
    }

    public int getIdBioinformaticsReports() {
        return idBioinformaticsReports;
    }

    public void setIdBioinformaticsReports(int idBioinformaticsReports) {
        this.idBioinformaticsReports = idBioinformaticsReports;
    }

    public int getIdAnalysis() {
        return idAnalysis;
    }

    public void setIdAnalysis(int idAnalysis) {
        this.idAnalysis = idAnalysis;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idBioinformaticsReports;
        hash += (int) idAnalysis;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BioinformaticsReportsPK)) {
            return false;
        }
        BioinformaticsReportsPK other = (BioinformaticsReportsPK) object;
        if (this.idBioinformaticsReports != other.idBioinformaticsReports) {
            return false;
        }
        if (this.idAnalysis != other.idAnalysis) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.BioinformaticsReportsPK[ idBioinformaticsReports=" + idBioinformaticsReports + ", idAnalysis=" + idAnalysis + " ]";
    }
    
}
