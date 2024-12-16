/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Date;
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

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "bioinformatic_analysis_sample_link")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BioinformaticAnalysisSampleLink.findAll", query = "SELECT b FROM BioinformaticAnalysisSampleLink b")
    , @NamedQuery(name = "BioinformaticAnalysisSampleLink.findByIdAnalysis", query = "SELECT b FROM BioinformaticAnalysisSampleLink b WHERE b.bioinformaticAnalysisSampleLinkPK.idAnalysis = :idAnalysis")
    , @NamedQuery(name = "BioinformaticAnalysisSampleLink.findByIdSample", query = "SELECT b FROM BioinformaticAnalysisSampleLink b WHERE b.bioinformaticAnalysisSampleLinkPK.idSample = :idSample")
    , @NamedQuery(name = "BioinformaticAnalysisSampleLink.findByStartday", query = "SELECT b FROM BioinformaticAnalysisSampleLink b WHERE b.startday = :startday")
    , @NamedQuery(name = "BioinformaticAnalysisSampleLink.findByFinishdate", query = "SELECT b FROM BioinformaticAnalysisSampleLink b WHERE b.finishdate = :finishdate")
    , @NamedQuery(name = "BioinformaticAnalysisSampleLink.findByLink", query = "SELECT b FROM BioinformaticAnalysisSampleLink b WHERE b.link = :link")
    , @NamedQuery(name = "BioinformaticAnalysisSampleLink.findByNote", query = "SELECT b FROM BioinformaticAnalysisSampleLink b WHERE b.note = :note")})
public class BioinformaticAnalysisSampleLink implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BioinformaticAnalysisSampleLinkPK bioinformaticAnalysisSampleLinkPK;
    @Column(name = "startday")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startday;
    @Column(name = "finishdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishdate;
    @Size(max = 500)
    @Column(name = "link")
    private String link;
    @Size(max = 1000)
    @Column(name = "note")
    private String note;
    @JoinColumn(name = "id_analysis", referencedColumnName = "id_analysis", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private BioinformaticAnalysis bioinformaticAnalysis;
    @JoinColumn(name = "id_sample", referencedColumnName = "id_sample", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Sample sample;

    public BioinformaticAnalysisSampleLink() {
    }

    public BioinformaticAnalysisSampleLink(BioinformaticAnalysisSampleLinkPK bioinformaticAnalysisSampleLinkPK) {
        this.bioinformaticAnalysisSampleLinkPK = bioinformaticAnalysisSampleLinkPK;
    }

    public BioinformaticAnalysisSampleLink(int idAnalysis, int idSample) {
        this.bioinformaticAnalysisSampleLinkPK = new BioinformaticAnalysisSampleLinkPK(idAnalysis, idSample);
    }

    public BioinformaticAnalysisSampleLinkPK getBioinformaticAnalysisSampleLinkPK() {
        return bioinformaticAnalysisSampleLinkPK;
    }

    public void setBioinformaticAnalysisSampleLinkPK(BioinformaticAnalysisSampleLinkPK bioinformaticAnalysisSampleLinkPK) {
        this.bioinformaticAnalysisSampleLinkPK = bioinformaticAnalysisSampleLinkPK;
    }

    public Date getStartday() {
        return startday;
    }

    public void setStartday(Date startday) {
        this.startday = startday;
    }

    public Date getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(Date finishdate) {
        this.finishdate = finishdate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BioinformaticAnalysis getBioinformaticAnalysis() {
        return bioinformaticAnalysis;
    }

    public void setBioinformaticAnalysis(BioinformaticAnalysis bioinformaticAnalysis) {
        this.bioinformaticAnalysis = bioinformaticAnalysis;
    }

    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bioinformaticAnalysisSampleLinkPK != null ? bioinformaticAnalysisSampleLinkPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BioinformaticAnalysisSampleLink)) {
            return false;
        }
        BioinformaticAnalysisSampleLink other = (BioinformaticAnalysisSampleLink) object;
        if ((this.bioinformaticAnalysisSampleLinkPK == null && other.bioinformaticAnalysisSampleLinkPK != null) || (this.bioinformaticAnalysisSampleLinkPK != null && !this.bioinformaticAnalysisSampleLinkPK.equals(other.bioinformaticAnalysisSampleLinkPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.BioinformaticAnalysisSampleLink[ bioinformaticAnalysisSampleLinkPK=" + bioinformaticAnalysisSampleLinkPK + " ]";
    }
    
}
