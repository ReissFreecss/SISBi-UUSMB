/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "bioinformatic_analysis")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BioinformaticAnalysis.findAll", query = "SELECT b FROM BioinformaticAnalysis b")
    , @NamedQuery(name = "BioinformaticAnalysis.findByIdAnalysis", query = "SELECT b FROM BioinformaticAnalysis b WHERE b.idAnalysis = :idAnalysis")
    , @NamedQuery(name = "BioinformaticAnalysis.findByAnalysisName", query = "SELECT b FROM BioinformaticAnalysis b WHERE b.analysisName = :analysisName")
    , @NamedQuery(name = "BioinformaticAnalysis.findByNote", query = "SELECT b FROM BioinformaticAnalysis b WHERE b.note = :note")
    , @NamedQuery(name = "BioinformaticAnalysis.findBySoftwareVersion", query = "SELECT b FROM BioinformaticAnalysis b WHERE b.softwareVersion = :softwareVersion")})
public class BioinformaticAnalysis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_analysis")
    private Integer idAnalysis;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "analysis_name")
    private String analysisName;
    @Size(max = 1000)
    @Column(name = "note")
    private String note;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "software_version")
    private String softwareVersion;
    @Basic(optional = false)
    @Size(min = 1, max = 10)
    @Column(name = "type")
    private String type;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bioinformaticAnalysis")
    private Collection<BioinformaticAnalysisSampleLink> bioinformaticAnalysisSampleLinkCollection;

    public BioinformaticAnalysis() {
    }

    public BioinformaticAnalysis(Integer idAnalysis) {
        this.idAnalysis = idAnalysis;
    }

    public BioinformaticAnalysis(Integer idAnalysis, String type,String analysisName, String softwareVersion) {
        this.idAnalysis = idAnalysis;
        this.analysisName = analysisName;
        this.type = type;
        this.softwareVersion = softwareVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    public Integer getIdAnalysis() {
        return idAnalysis;
    }

    public void setIdAnalysis(Integer idAnalysis) {
        this.idAnalysis = idAnalysis;
    }

    public String getAnalysisName() {
        return analysisName;
    }

    public void setAnalysisName(String analysisName) {
        this.analysisName = analysisName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    @XmlTransient
    public Collection<BioinformaticAnalysisSampleLink> getBioinformaticAnalysisSampleLinkCollection() {
        return bioinformaticAnalysisSampleLinkCollection;
    }

    public void setBioinformaticAnalysisSampleLinkCollection(Collection<BioinformaticAnalysisSampleLink> bioinformaticAnalysisSampleLinkCollection) {
        this.bioinformaticAnalysisSampleLinkCollection = bioinformaticAnalysisSampleLinkCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAnalysis != null ? idAnalysis.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BioinformaticAnalysis)) {
            return false;
        }
        BioinformaticAnalysis other = (BioinformaticAnalysis) object;
        if ((this.idAnalysis == null && other.idAnalysis != null) || (this.idAnalysis != null && !this.idAnalysis.equals(other.idAnalysis))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return analysisName;
    }
    
}
