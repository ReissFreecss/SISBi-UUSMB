/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
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
@Table(name = "balance_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BalancePago.findAll", query = "SELECT b FROM BalancePago b")
    , @NamedQuery(name = "BalancePago.findByFecha", query = "SELECT b FROM BalancePago b WHERE b.fecha = :fecha")
    , @NamedQuery(name = "BalancePago.findByAbono", query = "SELECT b FROM BalancePago b WHERE b.abono = :abono")
    , @NamedQuery(name = "BalancePago.findBySaldo", query = "SELECT b FROM BalancePago b WHERE b.saldo = :saldo")
    , @NamedQuery(name = "BalancePago.findByCargo", query = "SELECT b FROM BalancePago b WHERE b.cargo = :cargo")
    , @NamedQuery(name = "BalancePago.findByDescription", query = "SELECT b FROM BalancePago b WHERE b.description = :description")
    , @NamedQuery(name = "BalancePago.findByIdBalance", query = "SELECT b FROM BalancePago b WHERE b.idBalance = :idBalance")})
public class BalancePago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "abono")
    private Double abono;
    @Column(name = "saldo")
    private Double saldo;
    @Column(name = "cargo")
    private Double cargo;
    @Size(max = 300)
    @Column(name = "description")
    private String description;
    @Size(max=50)
    @Column(name="cotizacion")
    private String cotizacion;
    @Column(name="id_user")
    private Integer id_user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_balance")
    private Integer idBalance;
    @JoinColumn(name = "pago_id", referencedColumnName = "pago_id")
    @ManyToOne
    private Pago pagoId;

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

  
    

    public BalancePago() {
    }

    public String getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(String cotizacion) {
        this.cotizacion = cotizacion;
    }

    public BalancePago(Integer idBalance) {
        this.idBalance = idBalance;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getAbono() {
        return abono;
    }

    public void setAbono(Double abono) {
        this.abono = abono;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getCargo() {
        return cargo;
    }

    public void setCargo(Double cargo) {
        this.cargo = cargo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdBalance() {
        return idBalance;
    }

    public void setIdBalance(Integer idBalance) {
        this.idBalance = idBalance;
    }

    public Pago getPagoId() {
        return pagoId;
    }

    public void setPagoId(Pago pagoId) {
        this.pagoId = pagoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBalance != null ? idBalance.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BalancePago)) {
            return false;
        }
        BalancePago other = (BalancePago) object;
        if ((this.idBalance == null && other.idBalance != null) || (this.idBalance != null && !this.idBalance.equals(other.idBalance))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.BalancePago[ idBalance=" + idBalance + " ]";
    }
    
}
