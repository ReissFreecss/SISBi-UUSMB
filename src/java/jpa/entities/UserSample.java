/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "user_sample")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserSample.findAll", query = "SELECT u FROM UserSample u")
    , @NamedQuery(name = "UserSample.findByUserName", query = "SELECT u FROM UserSample u WHERE u.userName = :userName")
    , @NamedQuery(name = "UserSample.findByIdSample", query = "SELECT u FROM UserSample u WHERE u.idSample = :idSample")
    , @NamedQuery(name = "UserSample.findBySampleName", query = "SELECT u FROM UserSample u WHERE u.sampleName = :sampleName")
    , @NamedQuery(name = "UserSample.findBySampleQuality", query = "SELECT u FROM UserSample u WHERE u.sampleQuality = :sampleQuality")
    , @NamedQuery(name = "UserSample.findBySampleQuantity", query = "SELECT u FROM UserSample u WHERE u.sampleQuantity = :sampleQuantity")
    , @NamedQuery(name = "UserSample.findByComments", query = "SELECT u FROM UserSample u WHERE u.comments = :comments")
    , @NamedQuery(name = "UserSample.findByIdProject", query = "SELECT u FROM UserSample u WHERE u.idProject = :idProject")
    , @NamedQuery(name = "UserSample.findByStatus", query = "SELECT u FROM UserSample u WHERE u.status = :status")
    , @NamedQuery(name = "UserSample.findByDeliveryDate", query = "SELECT u FROM UserSample u WHERE u.deliveryDate = :deliveryDate")
    , @NamedQuery(name = "UserSample.findByType", query = "SELECT u FROM UserSample u WHERE u.type = :type")
    , @NamedQuery(name = "UserSample.findByReceptionDate", query = "SELECT u FROM UserSample u WHERE u.receptionDate = :receptionDate")
    , @NamedQuery(name = "UserSample.findByIdTube", query = "SELECT u FROM UserSample u WHERE u.idTube = :idTube")
    , @NamedQuery(name = "UserSample.findByExpectedPerformance", query = "SELECT u FROM UserSample u WHERE u.expectedPerformance = :expectedPerformance")
    , @NamedQuery(name = "UserSample.findByRealPerformance", query = "SELECT u FROM UserSample u WHERE u.realPerformance = :realPerformance")
    , @NamedQuery(name = "UserSample.findByInsertSize", query = "SELECT u FROM UserSample u WHERE u.insertSize = :insertSize")
    , @NamedQuery(name = "UserSample.findByReadSize", query = "SELECT u FROM UserSample u WHERE u.readSize = :readSize")
    , @NamedQuery(name = "UserSample.findByDescription", query = "SELECT u FROM UserSample u WHERE u.description = :description")
    , @NamedQuery(name = "UserSample.findByGeneticExtraction", query = "SELECT u FROM UserSample u WHERE u.geneticExtraction = :geneticExtraction")
    , @NamedQuery(name = "UserSample.findByOrganismName", query = "SELECT u FROM UserSample u WHERE u.organismName = :organismName")
    , @NamedQuery(name = "UserSample.findByReferenceName", query = "SELECT u FROM UserSample u WHERE u.referenceName = :referenceName")
    , @NamedQuery(name = "UserSample.findByContaminantName", query = "SELECT u FROM UserSample u WHERE u.contaminantName = :contaminantName")
    , @NamedQuery(name = "UserSample.findBySampleVolume", query = "SELECT u FROM UserSample u WHERE u.sampleVolume = :sampleVolume")
    , @NamedQuery(name = "UserSample.findByLabSampleVolume", query = "SELECT u FROM UserSample u WHERE u.labSampleVolume = :labSampleVolume")
    , @NamedQuery(name = "UserSample.findByLabSampleConcentration", query = "SELECT u FROM UserSample u WHERE u.labSampleConcentration = :labSampleConcentration")
    , @NamedQuery(name = "UserSample.findByAceptation", query = "SELECT u FROM UserSample u WHERE u.aceptation = :aceptation")
    , @NamedQuery(name = "UserSample.findBySamplePlataform", query = "SELECT u FROM UserSample u WHERE u.samplePlataform = :samplePlataform")})
public class UserSample implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 45)
    @Column(name = "user_name")
    
    private String userName;
    @Id
    @Column(name = "id_sample")
    private Integer idSample;
    @Size(max = 250)
    @Column(name = "sample_name")
    private String sampleName;
    @Size(max = 45)
    @Column(name = "sample_quality")
    private String sampleQuality;
    @Size(max = 45)
    @Column(name = "sample_quantity")
    private String sampleQuantity;
    @Size(max = 255)
    @Column(name = "comments")
    private String comments;
    @Size(max = 50)
    @Column(name = "id_project")
    private String idProject;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @Column(name = "delivery_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    @Size(max = 50)
    @Column(name = "type")
    private String type;
    @Column(name = "reception_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receptionDate;
    @Size(max = 100)
    @Column(name = "id_tube")
    private String idTube;
    @Size(max = 100)
    @Column(name = "expected_performance")
    private String expectedPerformance;
    @Size(max = 100)
    @Column(name = "real_performance")
    private String realPerformance;
    @Size(max = 50)
    @Column(name = "insert_size")
    private String insertSize;
    @Size(max = 50)
    @Column(name = "read_size")
    private String readSize;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Size(max = 2)
    @Column(name = "genetic_extraction")
    private String geneticExtraction;
    @Size(max = 100)
    @Column(name = "organism_name")
    private String organismName;
    @Size(max = 100)
    @Column(name = "reference_name")
    private String referenceName;
    @Size(max = 100)
    @Column(name = "contaminant_name")
    private String contaminantName;
    @Size(max = 100)
    @Column(name = "sample_volume")
    private String sampleVolume;
    @Column(name = "lab_sample_volume")
    private BigInteger labSampleVolume;
    @Column(name = "lab_sample_concentration")
    private BigInteger labSampleConcentration;
    @Size(max = 20)
    @Column(name = "aceptation")
    private String aceptation;
    @Size(max = 100)
    @Column(name = "sample_plataform")
    private String samplePlataform;

    public UserSample() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getIdSample() {
        return idSample;
    }

    public void setIdSample(Integer idSample) {
        this.idSample = idSample;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSampleQuality() {
        return sampleQuality;
    }

    public void setSampleQuality(String sampleQuality) {
        this.sampleQuality = sampleQuality;
    }

    public String getSampleQuantity() {
        return sampleQuantity;
    }

    public void setSampleQuantity(String sampleQuantity) {
        this.sampleQuantity = sampleQuantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getReceptionDate() {
        return receptionDate;
    }

    public void setReceptionDate(Date receptionDate) {
        this.receptionDate = receptionDate;
    }

    public String getIdTube() {
        return idTube;
    }

    public void setIdTube(String idTube) {
        this.idTube = idTube;
    }

    public String getExpectedPerformance() {
        return expectedPerformance;
    }

    public void setExpectedPerformance(String expectedPerformance) {
        this.expectedPerformance = expectedPerformance;
    }

    public String getRealPerformance() {
        return realPerformance;
    }

    public void setRealPerformance(String realPerformance) {
        this.realPerformance = realPerformance;
    }

    public String getInsertSize() {
        return insertSize;
    }

    public void setInsertSize(String insertSize) {
        this.insertSize = insertSize;
    }

    public String getReadSize() {
        return readSize;
    }

    public void setReadSize(String readSize) {
        this.readSize = readSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGeneticExtraction() {
        return geneticExtraction;
    }

    public void setGeneticExtraction(String geneticExtraction) {
        this.geneticExtraction = geneticExtraction;
    }

    public String getOrganismName() {
        return organismName;
    }

    public void setOrganismName(String organismName) {
        this.organismName = organismName;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getContaminantName() {
        return contaminantName;
    }

    public void setContaminantName(String contaminantName) {
        this.contaminantName = contaminantName;
    }

    public String getSampleVolume() {
        return sampleVolume;
    }

    public void setSampleVolume(String sampleVolume) {
        this.sampleVolume = sampleVolume;
    }

    public BigInteger getLabSampleVolume() {
        return labSampleVolume;
    }

    public void setLabSampleVolume(BigInteger labSampleVolume) {
        this.labSampleVolume = labSampleVolume;
    }

    public BigInteger getLabSampleConcentration() {
        return labSampleConcentration;
    }

    public void setLabSampleConcentration(BigInteger labSampleConcentration) {
        this.labSampleConcentration = labSampleConcentration;
    }

    public String getAceptation() {
        return aceptation;
    }

    public void setAceptation(String aceptation) {
        this.aceptation = aceptation;
    }

    public String getSamplePlataform() {
        return samplePlataform;
    }

    public void setSamplePlataform(String samplePlataform) {
        this.samplePlataform = samplePlataform;
    }
    
      @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSample != null ? idSample.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
       UserSample other = (UserSample) object;
        if ((this.idSample == null && other.idSample != null) || (this.idSample != null && !this.idSample.equals(other.idSample))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return userName;
    }
    
}
