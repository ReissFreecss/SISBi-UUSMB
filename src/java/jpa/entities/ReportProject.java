/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.io.File;
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
import jsf.PathFiles;

/**
 *
 * @author omarflores
 */
@Entity
@Table(name = "report_project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReportProject.findAll", query = "SELECT r FROM ReportProject r")
    , @NamedQuery(name = "ReportProject.findByIdReportProject", query = "SELECT r FROM ReportProject r WHERE r.idReportProject = :idReportProject")
    , @NamedQuery(name = "ReportProject.findByDateCreate", query = "SELECT r FROM ReportProject r WHERE r.dateCreate = :dateCreate")
    , @NamedQuery(name = "ReportProject.findByDateRevise", query = "SELECT r FROM ReportProject r WHERE r.dateRevise = :dateRevise")
    , @NamedQuery(name = "ReportProject.findByDateAuthorize", query = "SELECT r FROM ReportProject r WHERE r.dateAuthorize = :dateAuthorize")
    , @NamedQuery(name = "ReportProject.findByPathcreate", query = "SELECT r FROM ReportProject r WHERE r.pathcreate = :pathcreate")
    , @NamedQuery(name = "ReportProject.findByPathrevise", query = "SELECT r FROM ReportProject r WHERE r.pathrevise = :pathrevise")
    , @NamedQuery(name = "ReportProject.findByPathauthorize", query = "SELECT r FROM ReportProject r WHERE r.pathauthorize = :pathauthorize")
    , @NamedQuery(name = "ReportProject.findByName", query = "SELECT r FROM ReportProject r WHERE r.name = :name")
    , @NamedQuery(name = "ReportProject.findByTypeMethodology", query = "SELECT r FROM ReportProject r WHERE r.typeMethodology = :typeMethodology")
    , @NamedQuery(name = "ReportProject.findByStatus", query = "SELECT r FROM ReportProject r WHERE r.status = :status")})
public class ReportProject implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_report_project")
    private Integer idReportProject;
    @Column(name = "date_create")
    @Temporal(TemporalType.DATE)
    private Date dateCreate;
    @Column(name = "date_revise")
    @Temporal(TemporalType.DATE)
    private Date dateRevise;
    @Column(name = "date_authorize")
    @Temporal(TemporalType.DATE)
    private Date dateAuthorize;
    @Size(max = 100)
    @Column(name = "pathcreate")
    private String pathcreate;
    @Size(max = 100)
    @Column(name = "pathrevise")
    private String pathrevise;
    @Size(max = 100)
    @Column(name = "pathauthorize")
    private String pathauthorize;
    @Size(max = 100)
    @Column(name = "pathauthorize_pdf")
    private String pathauthorize_pdf;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 100)
    @Column(name = "type_methodology")
    private String typeMethodology;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "id_project", referencedColumnName = "id_project")
    @ManyToOne(optional = false)
    private Project idProject;
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ManyToOne(optional = false)
    private Users idUser;
    @JoinColumn(name = "id_user_authorize", referencedColumnName = "id_user")
    @ManyToOne
    private Users idUserAuthorize;
    @JoinColumn(name = "id_user_revise", referencedColumnName = "id_user")
    @ManyToOne
    private Users idUserRevise;

    public ReportProject() {
    }

    public ReportProject(Integer idReportProject) {
        this.idReportProject = idReportProject;
    }

    public ReportProject(Integer idReportProject, String name, String status) {
        this.idReportProject = idReportProject;
        this.name = name;
        this.status = status;
    }

    public Integer getIdReportProject() {
        return idReportProject;
    }

    public void setIdReportProject(Integer idReportProject) {
        this.idReportProject = idReportProject;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateRevise() {
        return dateRevise;
    }

    public void setDateRevise(Date dateRevise) {
        this.dateRevise = dateRevise;
    }

    public void setDateReviseIfEmpty(Date dateRevise) {
        if (this.dateRevise == null){
            this.dateRevise = dateRevise;
        }
    }

    public Date getDateAuthorize() {
        return dateAuthorize;
    }

    public void setDateAuthorize(Date dateAuthorize) {
        this.dateAuthorize = dateAuthorize;
    }

    public void setDateAuthorizeIfEmpty(Date dateAuthorize) {
        if (this.dateAuthorize == null){
            this.dateAuthorize = dateAuthorize;
        }
    }

    public String getPathcreate() {
        return pathcreate;
    }

    public void setPathcreate(String pathcreate) {
        this.pathcreate = pathcreate;
    }

    public String getPathrevise() {
        return pathrevise;
    }

    public void setPathrevise(String pathrevise) {
        this.pathrevise = pathrevise;
    }

    public String getPathauthorize() {
        return pathauthorize;
    }

    public String getPathauthorizePDF() {
        return pathauthorize_pdf;
    }
    public boolean getPathauthorizePDF_exists(){
        if (pathauthorize_pdf==null){
            return false;
        } else {
            File fil = new File(PathFiles.DirectoryreportDocuments+pathauthorize_pdf);
            return fil.exists();
        }
    }
    public String getLinkauthorizePDF(){
        // Obtiene el enlace al reporte final en PDF
        if (pathauthorize_pdf==null){
            return "";
        } else {
            return PathFiles.LinkProjectReportDocuments+pathauthorize_pdf;
        }    
    }

    public void setPathauthorize(String pathauthorize) {
        this.pathauthorize = pathauthorize;
    }

    public void setPathauthorize_PDF(String pathauthorize_pdf) {
        this.pathauthorize_pdf = pathauthorize_pdf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeMethodology() {
        return typeMethodology;
    }

    public void setTypeMethodology(String typeMethodology) {
        this.typeMethodology = typeMethodology;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Project getIdProject() {
        return idProject;
    }

    public void setIdProject(Project idProject) {
        this.idProject = idProject;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }

    public Users getIdUserAuthorize() {
        return idUserAuthorize;
    }

    public void setIdUserAuthorize(Users idUserAuthorize) {
        this.idUserAuthorize = idUserAuthorize;
    }

    public Users getIdUserRevise() {
        return idUserRevise;
    }

    public void setIdUserRevise(Users idUserRevise) {
        this.idUserRevise = idUserRevise;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReportProject != null ? idReportProject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReportProject)) {
            return false;
        }
        ReportProject other = (ReportProject) object;
        if ((this.idReportProject == null && other.idReportProject != null) || (this.idReportProject != null && !this.idReportProject.equals(other.idReportProject))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.ReportProject[ idReportProject=" + idReportProject + " ]";
    }
    
}
