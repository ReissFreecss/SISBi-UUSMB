/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "cotizacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cotizacion.findAll", query = "SELECT c FROM Cotizacion c")
    , @NamedQuery(name = "Cotizacion.findByCotizaId", query = "SELECT c FROM Cotizacion c WHERE c.cotizaId = :cotizaId")
    , @NamedQuery(name = "Cotizacion.findByCotizaNum", query = "SELECT c FROM Cotizacion c WHERE c.cotizaNum = :cotizaNum")
    , @NamedQuery(name = "Cotizacion.findByCotizaFecha", query = "SELECT c FROM Cotizacion c WHERE c.cotizaFecha = :cotizaFecha")
    , @NamedQuery(name = "Cotizacion.findByCotizaMontoPesos", query = "SELECT c FROM Cotizacion c WHERE c.cotizaMontoPesos = :cotizaMontoPesos")
    , @NamedQuery(name = "Cotizacion.findByCotizaStatus", query = "SELECT c FROM Cotizacion c WHERE c.cotizaStatus = :cotizaStatus")
    , @NamedQuery(name = "Cotizacion.findByTypeService", query = "SELECT c FROM Cotizacion c WHERE c.typeService = :typeService")
    , @NamedQuery(name = "Cotizacion.findByTotalSamples", query = "SELECT c FROM Cotizacion c WHERE c.totalSamples = :totalSamples")
    , @NamedQuery(name=  "Cotizacion.findByNameContact", query= "SELECT c FROM Cotizacion c WHERE c.nameContact= :nameContact")
    , @NamedQuery(name=  "Cotizacion.findByTypeClient", query= "SELECT c FROM Cotizacion c WHERE c.typeClient= :typeClient")})
public class Cotizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cotiza_id")
    private Integer cotizaId;
    @Size(max = 100)
    @Column(name = "cotiza_num")
    private String cotizaNum;
    @Column(name = "cotiza_fecha")
    @Temporal(TemporalType.DATE)
    private Date cotizaFecha;
    @Column(name = "cotiza_monto_pesos")
    private Double cotizaMontoPesos;
    @Size(max = 50)
    @Column(name = "cotiza_status")
    private String cotizaStatus;
    @Size(max = 500)
    @Column(name = "type_service")
    private String typeService;
    @Column(name = "total_samples")
    private Integer totalSamples;
    @Size(max=100)
    @Column(name="name_contact")
    private String nameContact;
    @Size(max=100)
    @Column(name="type_client")
    private String typeClient;
    
    @OneToMany(mappedBy = "cotizaId")
    private Collection<ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;

    public String getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(String typeClient) {
        this.typeClient = typeClient;
    }
    

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }
    

    public Cotizacion() {
    }

    public Cotizacion(Integer cotizaId) {
        this.cotizaId = cotizaId;
    }

    public Integer getCotizaId() {
        return cotizaId;
    }

    public void setCotizaId(Integer cotizaId) {
        this.cotizaId = cotizaId;
    }

    public String getCotizaNum() {
        return cotizaNum;
    }

    public void setCotizaNum(String cotizaNum) {
        this.cotizaNum = cotizaNum;
    }

    public Date getCotizaFecha() {
        return cotizaFecha;
    }

    public void setCotizaFecha(Date cotizaFecha) {
        this.cotizaFecha = cotizaFecha;
    }

    

    public Double getCotizaMontoPesos() {
        return cotizaMontoPesos;
    }

    public void setCotizaMontoPesos(Double cotizaMontoPesos) {
        this.cotizaMontoPesos = cotizaMontoPesos;
    }

    public String getCotizaStatus() {
        return cotizaStatus;
    }

    public void setCotizaStatus(String cotizaStatus) {
        this.cotizaStatus = cotizaStatus;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public Integer getTotalSamples() {
        return totalSamples;
    }

    public void setTotalSamples(Integer totalSamples) {
        this.totalSamples = totalSamples;
    }

   
   

    @XmlTransient
    public Collection<ProyCotizaFacPagoLink> getProyCotizaFacPagoLinkCollection() {
        return proyCotizaFacPagoLinkCollection;
    }

    public void setProyCotizaFacPagoLinkCollection(Collection<ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection) {
        this.proyCotizaFacPagoLinkCollection = proyCotizaFacPagoLinkCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cotizaId != null ? cotizaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cotizacion)) {
            return false;
        }
        Cotizacion other = (Cotizacion) object;
        if ((this.cotizaId == null && other.cotizaId != null) || (this.cotizaId != null && !this.cotizaId.equals(other.cotizaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Cotizacion[ cotizaId=" + cotizaId + " ]";
    }
    
}
