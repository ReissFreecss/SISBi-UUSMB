/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "genome_sample")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GenomeSample.findAll", query = "SELECT g FROM GenomeSample g")
    , @NamedQuery(name = "GenomeSample.findByIdSample", query = "SELECT g FROM GenomeSample g WHERE g.idSample = :idSample")
    , @NamedQuery(name = "GenomeSample.findBySampleName", query = "SELECT g FROM GenomeSample g WHERE g.sampleName = :sampleName")
    , @NamedQuery(name = "GenomeSample.findByGenomeName", query = "SELECT g FROM GenomeSample g WHERE g.genomeName = :genomeName")
    , @NamedQuery(name = "GenomeSample.findByIdGenome", query = "SELECT g FROM GenomeSample g WHERE g.idGenome = :idGenome")
    , @NamedQuery(name = "GenomeSample.findByType", query = "SELECT g FROM GenomeSample g WHERE g.type = :type")})
public class GenomeSample implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_sample")
    private Integer idSample;
    @Id
    @Size(max = 45)
    @Column(name = "sample_name")
    private String sampleName;
    @Id
    @Size(max = 100)
    @Column(name = "genome_name")
    private String genomeName;
    @Size(max = 14)
    @Column(name = "id_genome")
    private String idGenome;
    @Id
    @Size(max = 45)
    @Column(name = "type")
    private String type;

    public GenomeSample() {
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

    public String getGenomeName() {
        return genomeName;
    }

    public void setGenomeName(String genomeName) {
        this.genomeName = genomeName;
    }

    public String getIdGenome() {
        return idGenome;
    }

    public void setIdGenome(String idGenome) {
        this.idGenome = idGenome;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
