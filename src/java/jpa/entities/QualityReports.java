/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Comparator;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author uusmb
 */
@Entity
@Table(name = "quality_reports")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QualityReports.findAll", query = "SELECT q FROM QualityReports q")
    , @NamedQuery(name = "QualityReports.findByIdQualityReports", query = "SELECT q FROM QualityReports q WHERE q.idQualityReports = :idQualityReports")
    , @NamedQuery(name = "QualityReports.findByUrlQualityReport", query = "SELECT q FROM QualityReports q WHERE q.urlQualityReport = :urlQualityReport")
    , @NamedQuery(name = "QualityReports.findBySampleSheetFile", query = "SELECT q FROM QualityReports q WHERE q.sampleSheetFile = :sampleSheetFile")
    , @NamedQuery(name = "QualityReports.findByType", query = "SELECT q FROM QualityReports q WHERE q.type = :type")})
public class QualityReports implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_quality_reports")
    private Integer idQualityReports;
    @Size(max = 250)
    @Column(name = "url_quality_report")
    private String urlQualityReport;
    @Size(max = 250)
    @Column(name = "sample_sheet_file")
    private String sampleSheetFile;
    @Size(max = 45)
    @Column(name = "type")
    private String type;
    @JoinColumn(name = "id_project", referencedColumnName = "id_project")
    @ManyToOne
    private Project idProject;
    @JoinColumn(name = "id_run", referencedColumnName = "id_run")
    @ManyToOne
    private Run idRun;

    public QualityReports() {
    }

    public QualityReports(Integer idQualityReports) {
        this.idQualityReports = idQualityReports;
    }

    public Integer getIdQualityReports() {
        return idQualityReports;
    }

    public void setIdQualityReports(Integer idQualityReports) {
        this.idQualityReports = idQualityReports;
    }

    public String getUrlQualityReport() {
        return urlQualityReport;
    }

    public void setUrlQualityReport(String urlQualityReport) {
        this.urlQualityReport = urlQualityReport;
    }

    public String getSampleSheetFile() {
        return sampleSheetFile;
    }

    public void setSampleSheetFile(String sampleSheetFile) {
        this.sampleSheetFile = sampleSheetFile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Project getIdProject() {
        return idProject;
    }

    public void setIdProject(Project idProject) {
        this.idProject = idProject;
    }

    public Run getIdRun() {
        return idRun;
    }

    public void setIdRun(Run idRun) {
        this.idRun = idRun;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idQualityReports != null ? idQualityReports.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QualityReports)) {
            return false;
        }
        QualityReports other = (QualityReports) object;
        if ((this.idQualityReports == null && other.idQualityReports != null) || (this.idQualityReports != null && !this.idQualityReports.equals(other.idQualityReports))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.QualityReports[ idQualityReports=" + idQualityReports + " ]";
    }
    
}