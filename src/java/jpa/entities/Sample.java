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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "sample")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sample.findAll", query = "SELECT s FROM Sample s")
    , @NamedQuery(name = "Sample.findByIdSample", query = "SELECT s FROM Sample s WHERE s.idSample = :idSample")
    , @NamedQuery(name = "Sample.findBySampleName", query = "SELECT s FROM Sample s WHERE s.sampleName = :sampleName")
    , @NamedQuery(name = "Sample.findBySampleQuality", query = "SELECT s FROM Sample s WHERE s.sampleQuality = :sampleQuality")
    , @NamedQuery(name = "Sample.findBySampleQuantity", query = "SELECT s FROM Sample s WHERE s.sampleQuantity = :sampleQuantity")
    , @NamedQuery(name = "Sample.findByComments", query = "SELECT s FROM Sample s WHERE s.comments = :comments")
    , @NamedQuery(name = "Sample.findByStatus", query = "SELECT s FROM Sample s WHERE s.status = :status")
    , @NamedQuery(name = "Sample.findByDeliveryDate", query = "SELECT s FROM Sample s WHERE s.deliveryDate = :deliveryDate")
    , @NamedQuery(name = "Sample.findByType", query = "SELECT s FROM Sample s WHERE s.type = :type")
    , @NamedQuery(name = "Sample.findByReceptionDate", query = "SELECT s FROM Sample s WHERE s.receptionDate = :receptionDate")})
public class Sample implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_sample")
    private Integer idSample;
    
    @Size(max = 2)
    @Column(name = "genetic_extraction")
    private String geneticExtraction;
    
    @Size(max = 20)
    @Column(name = "aceptation")
    private String aceptation;
  
    @Size(max = 100)
    @Column(name = "sample_plataform")
    private String samplePlataform;

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
    
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 50)
    @Column(name = "insert_size")
    private String insertSize;
    @Size(max = 50)
    @Column(name = "read_size")
    private String readSize;

    @Size(max = 100)
    @Column(name = "id_tube")
    private String idTube;
    @Size(max = 100)
    @Column(name = "delivery_method")
    private String delivery;
    @Size(max = 100)
    @Column(name = "expected_performance")
    private String expectedPerformance;
    @Size(max = 100)
    @Column(name = "real_performance")
    private String realPerformance;
    
    @Column(name = "lab_sample_concentration")
    private Double labConcen;
    
    @Column(name = "lab_sample_volume")
    private Double labVolume;
    
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "status")
    private String status;
    @Column(name = "delivery_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "type")
    private String type;
    @Column(name = "reception_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receptionDate;
    @Column(name="abs_260_280")

    private String abs260_280;
    @Column(name="abs_260_230")
   
    private String abs260_230;
    @Column(name="folio")
   
    private String folio;
    @Column(name="abs_260_280_usmb")
    
    private String abs260_280_usmb;
    @Column(name="abs_260_230_usmb")
    
    private String abs260_230_usmb;
    
    @Size(max = 100)
    @Column(name = "expected_performance_oxford")
    private String expectedPerformanceOxford;
    
    
    @Size(max = 100)
    @Column(name = "organism_type")
    private String organism_type;
    
    @Size(max = 100)
    @Column(name = "app_type")
    private String app_type;
    
    @Size(max = 100)
    @Column(name = "kit_lib")
    private String kit_lib;
    
    @Size(max = 100)
    @Column(name = "tag_lib")
    private String tag_lib;
    
    @Size(max = 2)
    @Column(name = "deplesion")
    private String deplesion;
    
    @Size(max = 2)
    @Column(name = "just_genextrac")
    private String just_genextrac;
    
    @Size(max = 2)
    @Column(name = "just_qanalysis")
    private String just_qanalysis;
    
    @Size(max = 2)
    @Column(name = "just_lib")
    private String just_lib;
    
    @Size(max = 2)
    @Column(name = "just_seq")
    private String just_seq;
    
    @Size(max = 2)
    @Column(name = "build_lib")
    private String build_lib;
    
    @Size(max = 2)
    @Column(name = "do_seq")
    private String do_seq;
    
    public String getJust_genextrac() {
        return just_genextrac;
    }

    public void setJust_genextrac(String just_genextrac) {
        this.just_genextrac = just_genextrac;
    }

    public String getJust_qanalysis() {
        return just_qanalysis;
    }

    public void setJust_qanalysis(String just_qanalysis) {
        this.just_qanalysis = just_qanalysis;
    }

    public String getJust_lib() {
        return just_lib;
    }

    public void setJust_lib(String just_lib) {
        this.just_lib = just_lib;
    }

    public String getJust_seq() {
        return just_seq;
    }

    public void setJust_seq(String just_seq) {
        this.just_seq = just_seq;
    }

    public String getBuild_lib() {
        return build_lib;
    }

    public void setBuild_lib(String build_lib) {
        this.build_lib = build_lib;
    }

    public String getDo_seq() {
        return do_seq;
    }

    public void setDo_seq(String do_seq) {
        this.do_seq = do_seq;
    }
    

    public String getDeplesion() {
        return deplesion;
    }

    public void setDeplesion(String deplesion) {
        this.deplesion = deplesion;
    }
      

    public String getOrganism_type() {
        return organism_type;
    }

    public void setOrganism_type(String organism_type) {
        this.organism_type = organism_type;
    }

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public String getKit_lib() {
        return kit_lib;
    }

    public void setKit_lib(String kit_lib) {
        this.kit_lib = kit_lib;
    }

    public String getTag_lib() {
        return tag_lib;
    }

    public void setTag_lib(String tag_lib) {
        this.tag_lib = tag_lib;
    }
    
    
    

    public String getAbs260_280_usmb() {
        return abs260_280_usmb;
    }

    public void setAbs260_280_usmb(String abs260_280_usmb) {
        this.abs260_280_usmb = abs260_280_usmb;
    }

    public String getAbs260_230_usmb() {
        return abs260_230_usmb;
    }

    public void setAbs260_230_usmb(String abs260_230_usmb) {
        this.abs260_230_usmb = abs260_230_usmb;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getAbs260_280() {
        return abs260_280;
    }

    public void setAbs260_280(String abs260_280) {
        this.abs260_280 = abs260_280;
    }

    public String getAbs260_230() {
        return abs260_230;
    }

    public void setAbs260_230(String abs260_230) {
        this.abs260_230 = abs260_230;
    }

    public String getExpectedPerformanceOxford() {
        return expectedPerformanceOxford;
    }

    public void setExpectedPerformanceOxford(String expectedPerformanceOxford) {
        this.expectedPerformanceOxford = expectedPerformanceOxford;
    }
    
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sample")
    private Collection<SampleGenomeLink> sampleGenomeLinkCollection;
    @JoinColumn(name = "id_project", referencedColumnName = "id_project")
    
    @ManyToOne(optional = false)
    private Project idProject;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sample")
    private Collection<BioinformaticAnalysisSampleLink> bioinformaticAnalysisSampleLinkCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sample")
    private Collection<SampleLibraryLink> sampleLibraryCollection;

    public Sample() {
    }

    public Sample(Integer idSample) {
        this.idSample = idSample;
    }

    public Sample(Integer idSample, String sampleName, String status, String type) {
        this.idSample = idSample;
        this.sampleName = sampleName;
        this.status = status;
        this.type = type;
    }
    
    public Sample(Integer idSample, String geneticExtraction, String aceptation, String samplePlataform, String organismName, String referenceName, String contaminantName, String sampleVolume, String description, String readSize, String idTube, String delivery, String expectedPerformance, Double labConcen, Double labVolume, String sampleName, String sampleQuality, String sampleQuantity, String status, Date deliveryDate, String type, Date receptionDate, String abs260_280, String abs260_230, String folio, String abs260_280_usmb, String abs260_230_usmb, String expectedPerformanceOxford) {
        this.idSample = idSample;
        this.geneticExtraction = geneticExtraction;
        this.aceptation = aceptation;
        this.samplePlataform = samplePlataform;
        this.organismName = organismName;
        this.referenceName = referenceName;
        this.contaminantName = contaminantName;
        this.sampleVolume = sampleVolume;
        this.description = description;
        this.readSize = readSize;
        this.idTube = idTube;
        this.delivery = delivery;
        this.expectedPerformance = expectedPerformance;
        this.labConcen = labConcen;
        this.labVolume = labVolume;
        this.sampleName = sampleName;
        this.sampleQuality = sampleQuality;
        this.sampleQuantity = sampleQuantity;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.type = type;
        this.receptionDate = receptionDate;
        this.abs260_280 = abs260_280;
        this.abs260_230 = abs260_230;
        this.folio = folio;
        this.abs260_280_usmb = abs260_280_usmb;
        this.abs260_230_usmb = abs260_230_usmb;
        this.expectedPerformanceOxford = expectedPerformanceOxford;
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

    public String getAceptation() {
        return aceptation;
    }

    public void setAceptation(String aceptation) {
        this.aceptation = aceptation;
    }

    public Double getLabConcen() {
        return labConcen;
    }

    public void setLabConcen(Double labConcen) {
        this.labConcen = labConcen;
    }

    public Double getLabVolume() {
        return labVolume;
    }

    public void setLabVolume(Double labVolume) {
        this.labVolume = labVolume;
    }

    public String getSamplePlataform() {
        return samplePlataform;
    }

    public void setSamplePlataform(String samplePlataform) {
        this.samplePlataform = samplePlataform;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
    
    
    

    @XmlTransient
    public Collection<SampleGenomeLink> getSampleGenomeLinkCollection() {
        return sampleGenomeLinkCollection;
    }

    public void setSampleGenomeLinkCollection(Collection<SampleGenomeLink> sampleGenomeLinkCollection) {
        this.sampleGenomeLinkCollection = sampleGenomeLinkCollection;
    }

    public Project getIdProject() {
        return idProject;
    }

    public void setIdProject(Project idProject) {
        this.idProject = idProject;
    }

    @XmlTransient
    public Collection<BioinformaticAnalysisSampleLink> getBioinformaticAnalysisSampleLinkCollection() {
        return bioinformaticAnalysisSampleLinkCollection;
    }

    public void setBioinformaticAnalysisSampleLinkCollection(Collection<BioinformaticAnalysisSampleLink> bioinformaticAnalysisSampleLinkCollection) {
        this.bioinformaticAnalysisSampleLinkCollection = bioinformaticAnalysisSampleLinkCollection;
    }

    @XmlTransient
    public Collection<SampleLibraryLink> getLibraryCollection() {
        return sampleLibraryCollection;
    }

    public void setLibraryCollection(Collection<SampleLibraryLink> sampleLibraryCollection) {
        this.sampleLibraryCollection = sampleLibraryCollection;
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
        if (!(object instanceof Sample)) {
            return false;
        }
        Sample other = (Sample) object;
        if ((this.idSample == null && other.idSample != null) || (this.idSample != null && !this.idSample.equals(other.idSample))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
         return sampleName +" - "+ idTube;
    }
}
