/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import jsf.ReportProjectController;
import jsf.SampleController;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 *
 * @author uusmb
 */
@Entity
@Table(name = "bioinformatics_reports")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BioinformaticsReports.findAll", query = "SELECT b FROM BioinformaticsReports b")
    , @NamedQuery(name = "BioinformaticsReports.findByIdBioinformaticsReports", query = "SELECT b FROM BioinformaticsReports b WHERE b.bioinformaticsReportsPK.idBioinformaticsReports = :idBioinformaticsReports")
    , @NamedQuery(name = "BioinformaticsReports.findByType", query = "SELECT b FROM BioinformaticsReports b WHERE b.type = :type")
    , @NamedQuery(name = "BioinformaticsReports.findByPathWork", query = "SELECT b FROM BioinformaticsReports b WHERE b.pathWork = :pathWork")
    , @NamedQuery(name = "BioinformaticsReports.findByPdfReport", query = "SELECT b FROM BioinformaticsReports b WHERE b.pdfReport = :pdfReport")
    , @NamedQuery(name = "BioinformaticsReports.findByOwner", query = "SELECT b FROM BioinformaticsReports b WHERE b.owner = :owner")
    , @NamedQuery(name = "BioinformaticsReports.findByCreateDate", query = "SELECT b FROM BioinformaticsReports b WHERE b.createDate = :createDate")
    , @NamedQuery(name = "BioinformaticsReports.findByIdAnalysis", query = "SELECT b FROM BioinformaticsReports b WHERE b.bioinformaticsReportsPK.idAnalysis = :idAnalysis")})
public class BioinformaticsReports implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BioinformaticsReportsPK bioinformaticsReportsPK;
    @Size(max = 45)
    @Column(name = "type")
    private String type;
    @Size(max = 250)
    @Column(name = "path_work")
    private String pathWork;
    @Size(max = 250)
    @Column(name = "pdf_report")
    private String pdfReport;
    @Size(max = 45)
    @Column(name = "owner")
    private String owner;
    @Column(name = "create_date")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @JoinColumn(name = "id_analysis", referencedColumnName = "id_analysis", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private BioinformaticAnalysis bioinformaticAnalysis;
    @JoinColumn(name = "id_project", referencedColumnName = "id_project")
    @ManyToOne
    private Project idProject;
    @JoinColumn(name = "id_run", referencedColumnName = "id_run")
    @ManyToOne
    private Run idRun;
    @Column(name = "description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    

    public BioinformaticsReports() {
    }

    public BioinformaticsReports(BioinformaticsReportsPK bioinformaticsReportsPK) {
        this.bioinformaticsReportsPK = bioinformaticsReportsPK;
    }

    public BioinformaticsReports(int idBioinformaticsReports, int idAnalysis) {
        this.bioinformaticsReportsPK = new BioinformaticsReportsPK(idBioinformaticsReports, idAnalysis);
    }

    public BioinformaticsReportsPK getBioinformaticsReportsPK() {
        return bioinformaticsReportsPK;
    }

    public void setBioinformaticsReportsPK(BioinformaticsReportsPK bioinformaticsReportsPK) {
        this.bioinformaticsReportsPK = bioinformaticsReportsPK;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPathWork() {
        return pathWork;
    }

    public void setPathWork(String pathWork) {
        this.pathWork = pathWork;
    }

    public String getPdfReport() {
        return pdfReport;
    }

    public void setPdfReport(String pdfReport) {
        this.pdfReport = pdfReport;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BioinformaticAnalysis getBioinformaticAnalysis() {
        return bioinformaticAnalysis;
    }

    public void setBioinformaticAnalysis(BioinformaticAnalysis bioinformaticAnalysis) {
        this.bioinformaticAnalysis = bioinformaticAnalysis;
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
        hash += (bioinformaticsReportsPK != null ? bioinformaticsReportsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BioinformaticsReports)) {
            return false;
        }
        BioinformaticsReports other = (BioinformaticsReports) object;
        if ((this.bioinformaticsReportsPK == null && other.bioinformaticsReportsPK != null) || (this.bioinformaticsReportsPK != null && !this.bioinformaticsReportsPK.equals(other.bioinformaticsReportsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.BioinformaticsReports[ bioinformaticsReportsPK=" + bioinformaticsReportsPK + " ]";
    }
    
}
