/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p")
    , @NamedQuery(name = "Project.findByIdProject", query = "SELECT p FROM Project p WHERE p.idProject = :idProject")
    , @NamedQuery(name = "Project.findByProjectName", query = "SELECT p FROM Project p WHERE p.projectName = :projectName")
    , @NamedQuery(name = "Project.findByProjectDescription", query = "SELECT p FROM Project p WHERE p.projectDescription = :projectDescription")
    , @NamedQuery(name = "Project.findByRequestDate", query = "SELECT p FROM Project p WHERE p.requestDate = :requestDate")
    , @NamedQuery(name = "Project.findByStatus", query = "SELECT p FROM Project p WHERE p.status = :status")
    , @NamedQuery(name = "Project.findByQuotationNumber", query = "SELECT p FROM Project p WHERE p.quotationNumber = :quotationNumber")
    , @NamedQuery(name = "Project.findByBillNumber", query = "SELECT p FROM Project p WHERE p.billNumber = :billNumber")})
public class Project implements Serializable {
        @OneToMany(mappedBy = "idProject")
    private Collection<ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;
    @Size(max = 500)
    @Column(name = "comments")
    private String comments;
    
    @Size(max = 100)
    @Column(name = "delivery_method")
    private String deliveryMethod;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "id_project")
    private String idProject;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "project_name")
    private String projectName;
    @Size(max = 1000)
    @Column(name = "project_description")
    private String projectDescription;
    @Basic(optional = false)
    @NotNull
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
    
    //leslie 24 nov 2023
    
    @Size(max = 50)
    @Column(name = "type_project")
    private String type_project;

   
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProject")
    private Collection<Sample> sampleCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Collection<UserProjectLink> userProjectLinkCollection;

    public Project() {
    }
    

    public Project(String idProject) {
        this.idProject = idProject;
    }

    public Project(String idProject, String projectName, Date requestDate) {
        this.idProject = idProject;
        this.projectName = projectName;
        this.requestDate = requestDate;
    }

    public String getIdProject() {
        return idProject;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
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
    
    public String getProjectNameSplit() {
        if (projectName!=null) {
           if (projectName.length()>60) {
            return projectName.substring(0, 57)+"...";
        }  
        }
       
        return projectName;
    }

    public void setProjectNameSplit(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
      
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
    
    public String getProjectDescriptionSplit() {
       if (projectDescription!=null) {
           if (projectDescription.length()>60) {
            return projectDescription.substring(0,57)+"...";
        }
          
        }
        
        return projectDescription;
    }

    public void setProjectDescriptionSplit(String projectDescription) {
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
    
     public String getType_project() {
        return type_project;
    }

    public void setType_project(String type_project) {
        this.type_project = type_project;
    }

    @XmlTransient
    public Collection<Sample> getSampleCollection() {
        return sampleCollection;
    }

    public void setSampleCollection(Collection<Sample> sampleCollection) {
        this.sampleCollection = sampleCollection;
    }

    @XmlTransient
    public Collection<UserProjectLink> getUserProjectLinkCollection() {
        return userProjectLinkCollection;
    }

    public void setUserProjectLinkCollection(Collection<UserProjectLink> userProjectLinkCollection) {
        this.userProjectLinkCollection = userProjectLinkCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProject != null ? idProject.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.idProject == null && other.idProject != null) || (this.idProject != null && !this.idProject.equals(other.idProject))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return projectName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
     @XmlTransient
    public Collection<ProyCotizaFacPagoLink> getProyCotizaFacPagoLinkCollection() {
        return proyCotizaFacPagoLinkCollection;
    }

    public void setProyCotizaFacPagoLinkCollection(Collection<ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection) {
        this.proyCotizaFacPagoLinkCollection = proyCotizaFacPagoLinkCollection;
    }
    
    
}
