/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author omarflores
 */
@Entity
@Table(name = "run")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Run.findAll", query = "SELECT r FROM Run r"),
    @NamedQuery(name = "Run.findByIdRun", query = "SELECT r FROM Run r WHERE r.idRun = :idRun"),
    @NamedQuery(name = "Run.findByRunType", query = "SELECT r FROM Run r WHERE r.runType = :runType"),
    @NamedQuery(name = "Run.findByRunStartday", query = "SELECT r FROM Run r WHERE r.runStartday = :runStartday"),
    @NamedQuery(name = "Run.findByRunFinishday", query = "SELECT r FROM Run r WHERE r.runFinishday = :runFinishday"),
    @NamedQuery(name = "Run.findByStatus", query = "SELECT r FROM Run r WHERE r.status = :status"),
    @NamedQuery(name = "Run.findByRunName", query = "SELECT r FROM Run r WHERE r.runName = :runName"),
    @NamedQuery(name = "Run.findByWorkSubdirectory", query = "SELECT r FROM Run r WHERE r.workSubdirectory = :workSubdirectory"),
    @NamedQuery(name = "Run.findBySampleSheetName", query = "SELECT r FROM Run r WHERE r.sampleSheetName = :sampleSheetName"),
    @NamedQuery(name = "Run.findByCycles", query = "SELECT r FROM Run r WHERE r.cycles = :cycles"),
    @NamedQuery(name = "Run.findByPerformance", query = "SELECT r FROM Run r WHERE r.performance = :performance"),
    @NamedQuery(name = "Run.findByEstatusArchivos", query = "SELECT r FROM Run r WHERE r.estatusArchivos = :estatusArchivos")})
public class Run implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_run")
    private Integer idRun;
    @Size(max = 45)
    @Column(name = "run_type")
    private String runType;
    @Column(name = "run_startday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date runStartday;
    @Column(name = "run_finishday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date runFinishday;
    @Size(max = 100)
    @Column(name = "status")
    private String status;
    @Size(max = 200)
    @Column(name = "run_name")
    private String runName;
    @Size(max = 500)
    @Column(name = "work_subdirectory")
    private String workSubdirectory;
    @Size(max = 500)
    @Column(name = "sample_sheet_name")
    private String sampleSheetName;
    @Column(name = "cycles")
    private Integer cycles;
    @Size(max = 20)
    @Column(name = "performance")
    private String performance;
    @Size(max = 100)
    @Column(name = "estatus_archivos")
    private String estatusArchivos;
    @JoinColumn(name = "id_plataform", referencedColumnName = "id_plataform")
    @ManyToOne
    private Plataform idPlataform;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "run")
    private List<LibraryRunLink> libraryRunLinkList;

    public Run() {
    }

    public Run(Integer idRun) {
        this.idRun = idRun;
    }

    public Integer getIdRun() {
        return idRun;
    }

    public void setIdRun(Integer idRun) {
        this.idRun = idRun;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }

    public Date getRunStartday() {
        return runStartday;
    }

    public void setRunStartday(Date runStartday) {
        this.runStartday = runStartday;
    }

    public Date getRunFinishday() {
        return runFinishday;
    }

    public void setRunFinishday(Date runFinishday) {
        this.runFinishday = runFinishday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRunName() {
        return runName;
    }

    public void setRunName(String runName) {
        this.runName = runName;
    }

    public String getWorkSubdirectory() {
        return workSubdirectory;
    }

    public void setWorkSubdirectory(String workSubdirectory) {
        this.workSubdirectory = workSubdirectory;
    }

    public String getSampleSheetName() {
        return sampleSheetName;
    }

    public void setSampleSheetName(String sampleSheetName) {
        this.sampleSheetName = sampleSheetName;
    }

    public Integer getCycles() {
        return cycles;
    }

    public void setCycles(Integer cycles) {
        this.cycles = cycles;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getEstatusArchivos() {
        return estatusArchivos;
    }

    public void setEstatusArchivos(String estatusArchivos) {
        this.estatusArchivos = estatusArchivos;
    }

    public Plataform getIdPlataform() {
        return idPlataform;
    }

    public void setIdPlataform(Plataform idPlataform) {
        this.idPlataform = idPlataform;
    }

    @XmlTransient
    public List<LibraryRunLink> getLibraryRunLinkList() {
        return libraryRunLinkList;
    }

    public void setLibraryRunLinkList(List<LibraryRunLink> libraryRunLinkList) {
        this.libraryRunLinkList = libraryRunLinkList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRun != null ? idRun.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Run)) {
            return false;
        }
        Run other = (Run) object;
        if ((this.idRun == null && other.idRun != null) || (this.idRun != null && !this.idRun.equals(other.idRun))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Run[ idRun=" + idRun + " ]";
    }
    
}
