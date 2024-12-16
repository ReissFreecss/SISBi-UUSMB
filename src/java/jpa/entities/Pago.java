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
@Table(name = "pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p")
    , @NamedQuery(name = "Pago.findByPagoId", query = "SELECT p FROM Pago p WHERE p.pagoId = :pagoId")
    , @NamedQuery(name = "Pago.findByReferen", query = "SELECT p FROM Pago p WHERE p.referen = :referen")
    , @NamedQuery(name = "Pago.findByPagoFecha", query = "SELECT p FROM Pago p WHERE p.pagoFecha = :pagoFecha")
    , @NamedQuery(name = "Pago.findByMonto", query = "SELECT p FROM Pago p WHERE p.monto = :monto")
    , @NamedQuery(name = "Pago.findByObservaciones", query = "SELECT p FROM Pago p WHERE p.observaciones = :observaciones")
    , @NamedQuery(name = "Pago.findByPoliza", query = "SELECT p FROM Pago p WHERE p.poliza = :poliza")})
public class Pago implements Serializable {

    @OneToMany(mappedBy = "pagoId")
    private Collection<BalancePago> balancePagoCollection;
 
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pago_id")
    private Integer pagoId;
    @Size(max = 50)
    @Column(name = "referen")
    private String referen;
    @Column(name = "pago_fecha")
    @Temporal(TemporalType.DATE)
    private Date pagoFecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    private BigDecimal monto;
    @Size(max = 1000)
    @Column(name = "observaciones")
    private String observaciones;
    @Size(max = 100)
    @Column(name = "poliza")
    private String poliza;
    @Size(max = 50)
    @Column(name = "status")
    private String status;
    @Size(max = 150)
    @Column(name = "url_voucher")
    private String url_voucher;
    @OneToMany(mappedBy = "pagoId")
    private Collection<ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ManyToOne
    private Users idUser;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl_voucher() {
        return url_voucher;
    }

    public void setUrl_voucher(String url_voucher) {
        this.url_voucher = url_voucher;
    }
    

    public Pago() {
    }

    public Pago(Integer pagoId) {
        this.pagoId = pagoId;
    }

    public Integer getPagoId() {
        return pagoId;
    }

    public void setPagoId(Integer pagoId) {
        this.pagoId = pagoId;
    }

    public String getReferen() {
        return referen;
    }

    public void setReferen(String referen) {
        this.referen = referen;
    }

    public Date getPagoFecha() {
        return pagoFecha;
    }

    public void setPagoFecha(Date pagoFecha) {
        this.pagoFecha = pagoFecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getPoliza() {
        return poliza;
    }

    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }

    @XmlTransient
    public Collection<ProyCotizaFacPagoLink> getProyCotizaFacPagoLinkCollection() {
        return proyCotizaFacPagoLinkCollection;
    }

    public void setProyCotizaFacPagoLinkCollection(Collection<ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection) {
        this.proyCotizaFacPagoLinkCollection = proyCotizaFacPagoLinkCollection;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagoId != null ? pagoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.pagoId == null && other.pagoId != null) || (this.pagoId != null && !this.pagoId.equals(other.pagoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Pago[ pagoId=" + pagoId + " ]";
    }

    @XmlTransient
    public Collection<BalancePago> getBalancePagoCollection() {
        return balancePagoCollection;
    }

    public void setBalancePagoCollection(Collection<BalancePago> balancePagoCollection) {
        this.balancePagoCollection = balancePagoCollection;
    }
    
}
