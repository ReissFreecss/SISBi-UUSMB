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
@Table(name = "factura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f")
    , @NamedQuery(name = "Factura.findByFactId", query = "SELECT f FROM Factura f WHERE f.factId = :factId")
    , @NamedQuery(name = "Factura.findByFactFolio", query = "SELECT f FROM Factura f WHERE f.factFolio = :factFolio")
    , @NamedQuery(name = "Factura.findByFactFecha", query = "SELECT f FROM Factura f WHERE f.factFecha = :factFecha")
    , @NamedQuery(name = "Factura.findByPagoFecha", query = "SELECT f FROM Factura f WHERE f.pagoFecha = :pagoFecha")
    , @NamedQuery(name = "Factura.findByMonto", query = "SELECT f FROM Factura f WHERE f.monto = :monto")
    , @NamedQuery(name = "Factura.findByIva", query = "SELECT f FROM Factura f WHERE f.iva = :iva")
    , @NamedQuery(name = "Factura.findByOverhead", query = "SELECT f FROM Factura f WHERE f.overhead = :overhead")})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fact_id")
    private Integer factId;
    @Size(max = 50)
    @Column(name = "fact_folio")
    private String factFolio;
    @Column(name = "fact_fecha")
    @Temporal(TemporalType.DATE)
    private Date factFecha;
    @Column(name = "pago_fecha")
    @Temporal(TemporalType.DATE)
    private Date pagoFecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    private BigDecimal monto;
    @Column(name = "iva")
    private BigDecimal iva;
    @Column(name = "overhead")
    private BigDecimal overhead;
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ManyToOne
    private Users idUser;
    @OneToMany(mappedBy = "facId")
    private Collection<ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;

    public Factura() {
    }

    public Factura(Integer factId) {
        this.factId = factId;
    }

    public Integer getFactId() {
        return factId;
    }

    public void setFactId(Integer factId) {
        this.factId = factId;
    }

    public String getFactFolio() {
        return factFolio;
    }

    public void setFactFolio(String factFolio) {
        this.factFolio = factFolio;
    }

    public Date getFactFecha() {
        return factFecha;
    }

    public void setFactFecha(Date factFecha) {
        this.factFecha = factFecha;
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

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getOverhead() {
        return overhead;
    }

    public void setOverhead(BigDecimal overhead) {
        this.overhead = overhead;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
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
        hash += (factId != null ? factId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.factId == null && other.factId != null) || (this.factId != null && !this.factId.equals(other.factId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Factura[ factId=" + factId + " ]";
    }
    
}
