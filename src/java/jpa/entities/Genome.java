/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "genome")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Genome.findAll", query = "SELECT g FROM Genome g")
    , @NamedQuery(name = "Genome.findByIdGenome", query = "SELECT g FROM Genome g WHERE g.idGenome = :idGenome")
    , @NamedQuery(name = "Genome.findByGi", query = "SELECT g FROM Genome g WHERE g.gi = :gi")
    , @NamedQuery(name = "Genome.findByGenomeName", query = "SELECT g FROM Genome g WHERE g.genomeName = :genomeName")
    , @NamedQuery(name = "Genome.findByGenomeLength", query = "SELECT g FROM Genome g WHERE g.genomeLength = :genomeLength")
    , @NamedQuery(name = "Genome.findByGenomeVersion", query = "SELECT g FROM Genome g WHERE g.genomeVersion = :genomeVersion")})
public class Genome implements Serializable {

    @Size(max = 150)
    @Column(name = "http")
    private String http;
    @Size(max = 100)
    @Column(name = "source")
    private String source;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_genome")
    private String idGenome;
    @Size(max = 45)
    @Column(name = "gi")
    private String gi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "genome_name")
    private String genomeName;
    @Size(max = 45)
    @Column(name = "genome_length")
    private String genomeLength;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "genome_version")
    private String genomeVersion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genome")
    private Collection<SampleGenomeLink> sampleGenomeLinkCollection;

    public Genome() {
    }

    public Genome(String idGenome) {
        this.idGenome = idGenome;
    }

    public Genome(String idGenome, String genomeName, String genomeVersion) {
        this.idGenome = idGenome;
        this.genomeName = genomeName;
        this.genomeVersion = genomeVersion;
    }

    public String getIdGenome() {
        return idGenome;
    }

    public void setIdGenome(String idGenome) {
        this.idGenome = idGenome;
    }

    public String getGi() {
        return gi;
    }

    public void setGi(String gi) {
        this.gi = gi;
    }

    public String getGenomeName() {
        return genomeName;
    }

    public void setGenomeName(String genomeName) {
        this.genomeName = genomeName;
    }

    public String getGenomeLength() {
        return genomeLength;
    }

    public void setGenomeLength(String genomeLength) {
        this.genomeLength = genomeLength;
    }

    public String getGenomeVersion() {
        return genomeVersion;
    }

    public void setGenomeVersion(String genomeVersion) {
        this.genomeVersion = genomeVersion;
    }

    @XmlTransient
    public Collection<SampleGenomeLink> getSampleGenomeLinkCollection() {
        return sampleGenomeLinkCollection;
    }

    public void setSampleGenomeLinkCollection(Collection<SampleGenomeLink> sampleGenomeLinkCollection) {
        this.sampleGenomeLinkCollection = sampleGenomeLinkCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGenome != null ? idGenome.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Genome)) {
            return false;
        }
        Genome other = (Genome) object;
        if ((this.idGenome == null && other.idGenome != null) || (this.idGenome != null && !this.idGenome.equals(other.idGenome))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return genomeName;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
}
