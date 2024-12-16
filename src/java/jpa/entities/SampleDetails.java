/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
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
@Table(name = "sample_details ")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SampleDetails.findAll", query = "SELECT s FROM SampleDetails s")
    , @NamedQuery(name = "SampleDetails.findByUserName", query = "SELECT s FROM SampleDetails s WHERE s.userName = :userName")
    , @NamedQuery(name = "SampleDetails.findByFirstName", query = "SELECT s FROM SampleDetails s WHERE s.firstName = :firstName")
    , @NamedQuery(name = "SampleDetails.findByPLastName", query = "SELECT s FROM SampleDetails s WHERE s.pLastName = :pLastName")
    , @NamedQuery(name = "SampleDetails.findByMLastName", query = "SELECT s FROM SampleDetails s WHERE s.mLastName = :mLastName")
    , @NamedQuery(name = "SampleDetails.findByEmail", query = "SELECT s FROM SampleDetails s WHERE s.email = :email")
    , @NamedQuery(name = "SampleDetails.findByDependencyName", query = "SELECT s FROM SampleDetails s WHERE s.dependencyName = :dependencyName")
    , @NamedQuery(name = "SampleDetails.findByRole", query = "SELECT s FROM SampleDetails s WHERE s.role = :role")
    , @NamedQuery(name = "SampleDetails.findByIdProject", query = "SELECT s FROM SampleDetails s WHERE s.idProject = :idProject")
    , @NamedQuery(name = "SampleDetails.findByProjectName", query = "SELECT s FROM SampleDetails s WHERE s.projectName = :projectName")
    , @NamedQuery(name = "SampleDetails.findByProjectDescription", query = "SELECT s FROM SampleDetails s WHERE s.projectDescription = :projectDescription")
    , @NamedQuery(name = "SampleDetails.findByRequestDate", query = "SELECT s FROM SampleDetails s WHERE s.requestDate = :requestDate")
    , @NamedQuery(name = "SampleDetails.findByStatus", query = "SELECT s FROM SampleDetails s WHERE s.status = :status")
    , @NamedQuery(name = "SampleDetails.findByQuotationNumber", query = "SELECT s FROM SampleDetails s WHERE s.quotationNumber = :quotationNumber")
    , @NamedQuery(name = "SampleDetails.findByBillNumber", query = "SELECT s FROM SampleDetails s WHERE s.billNumber = :billNumber")
    , @NamedQuery(name = "SampleDetails.findByComments", query = "SELECT s FROM SampleDetails s WHERE s.comments = :comments")
    , @NamedQuery(name = "SampleDetails.findByIdSample", query = "SELECT s FROM SampleDetails s WHERE s.idSample = :idSample")
    , @NamedQuery(name = "SampleDetails.findBySampleName", query = "SELECT s FROM SampleDetails s WHERE s.sampleName = :sampleName")})
public class SampleDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 45)
    @Column(name = "user_name")
    @Id
    private String userName;
    @Size(max = 100)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 200)
    @Column(name = "p_last_name")
    private String pLastName;
    @Size(max = 200)
    @Column(name = "m_last_name")
    private String mLastName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "email")
    @Id
    private String email;
    @Size(max = 100)
    @Column(name = "dependency_name")
    private String dependencyName;
    @Size(max = 45)
    @Column(name = "role")
    private String role;
    @Size(max = 50)
    @Column(name = "id_project")
    @Id
    private String idProject;
    @Size(max = 200)
    @Column(name = "project_name")
    private String projectName;
    @Size(max = 1000)
    @Column(name = "project_description")
    private String projectDescription;
    @Column(name = "request_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;
    @Size(max = 45)
    @Column(name = "status")
    private String status;
    @Size(max = 45)
    @Column(name = "quotation_number")
    private String quotationNumber;
    @Size(max = 45)
    @Column(name = "bill_number")
    private String billNumber;
    @Size(max = 500)
    @Column(name = "comments")
    private String comments;
    @Id
    @Column(name = "id_sample")
    private Integer idSample;
    @Size(max = 250)
    @Column(name = "sample_name")
    private String sampleName;

    public SampleDetails() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPLastName() {
        return pLastName;
    }

    public void setPLastName(String pLastName) {
        this.pLastName = pLastName;
    }

    public String getMLastName() {
        return mLastName;
    }

    public void setMLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDependencyName() {
        return dependencyName;
    }

    public void setDependencyName(String dependencyName) {
        this.dependencyName = dependencyName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
    
}
