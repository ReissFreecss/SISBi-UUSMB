/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author omarflores
 */
@Entity
@Table(name = "sample_report_project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SampleReportProject.findAll", query = "SELECT s FROM SampleReportProject s")
    , @NamedQuery(name = "SampleReportProject.findByIdSampleReportProject", query = "SELECT s FROM SampleReportProject s WHERE s.idSampleReportProject = :idSampleReportProject")
    , @NamedQuery(name = "SampleReportProject.findByStatusPrevious", query = "SELECT s FROM SampleReportProject s WHERE s.statusPrevious = :statusPrevious")
    , @NamedQuery(name = "SampleReportProject.findByStatusCurrent", query = "SELECT s FROM SampleReportProject s WHERE s.statusCurrent = :statusCurrent")
    , @NamedQuery(name = "SampleReportProject.findByDateUpgrade", query = "SELECT s FROM SampleReportProject s WHERE s.dateUpgrade = :dateUpgrade")})
public class SampleReportProject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sample_report_project")
    private Integer idSampleReportProject;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "status_previous")
    private String statusPrevious;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "status_current")
    private String statusCurrent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_upgrade")
    @Temporal(TemporalType.DATE)
    private Date dateUpgrade;
    @JoinColumn(name = "id_report_project", referencedColumnName = "id_report_project")
    @ManyToOne(optional = false)
    private ReportProject idReportProject;
    @JoinColumn(name = "id_sample", referencedColumnName = "id_sample")
    @ManyToOne(optional = false)
    private Sample idSample;

    public SampleReportProject() {
    }

    public SampleReportProject(Integer idSampleReportProject) {
        this.idSampleReportProject = idSampleReportProject;
    }

    public SampleReportProject(Integer idSampleReportProject, String statusPrevious, String statusCurrent, Date dateUpgrade) {
        this.idSampleReportProject = idSampleReportProject;
        this.statusPrevious = statusPrevious;
        this.statusCurrent = statusCurrent;
        this.dateUpgrade = dateUpgrade;
    }

    public Integer getIdSampleReportProject() {
        return idSampleReportProject;
    }

    public void setIdSampleReportProject(Integer idSampleReportProject) {
        this.idSampleReportProject = idSampleReportProject;
    }

    public String getStatusPrevious() {
        return statusPrevious;
    }

    public void setStatusPrevious(String statusPrevious) {
        this.statusPrevious = statusPrevious;
    }

    public String getStatusCurrent() {
        return statusCurrent;
    }

    public void setStatusCurrent(String statusCurrent) {
        this.statusCurrent = statusCurrent;
    }

    public Date getDateUpgrade() {
        return dateUpgrade;
    }

    public void setDateUpgrade(Date dateUpgrade) {
        this.dateUpgrade = dateUpgrade;
    }

    public ReportProject getIdReportProject() {
        return idReportProject;
    }

    public void setIdReportProject(ReportProject idReportProject) {
        this.idReportProject = idReportProject;
    }

    public Sample getIdSample() {
        return idSample;
    }

    public void setIdSample(Sample idSample) {
        this.idSample = idSample;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSampleReportProject != null ? idSampleReportProject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SampleReportProject)) {
            return false;
        }
        SampleReportProject other = (SampleReportProject) object;
        if ((this.idSampleReportProject == null && other.idSampleReportProject != null) || (this.idSampleReportProject != null && !this.idSampleReportProject.equals(other.idSampleReportProject))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.SampleReportProject[ idSampleReportProject=" + idSampleReportProject + " ]";
    }
    
}
