/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "fiscal_address")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FiscalAddress.findAll", query = "SELECT f FROM FiscalAddress f")
    , @NamedQuery(name = "FiscalAddress.findByIdFiscalAddress", query = "SELECT f FROM FiscalAddress f WHERE f.idFiscalAddress = :idFiscalAddress")
    , @NamedQuery(name = "FiscalAddress.findBySocialReason", query = "SELECT f FROM FiscalAddress f WHERE f.socialReason = :socialReason")
    , @NamedQuery(name = "FiscalAddress.findByStreet", query = "SELECT f FROM FiscalAddress f WHERE f.street = :street")
    , @NamedQuery(name = "FiscalAddress.findByExtNum", query = "SELECT f FROM FiscalAddress f WHERE f.extNum = :extNum")
    , @NamedQuery(name = "FiscalAddress.findByIntNum", query = "SELECT f FROM FiscalAddress f WHERE f.intNum = :intNum")
    , @NamedQuery(name = "FiscalAddress.findByLocation", query = "SELECT f FROM FiscalAddress f WHERE f.location = :location")
    , @NamedQuery(name = "FiscalAddress.findByPostalCode", query = "SELECT f FROM FiscalAddress f WHERE f.postalCode = :postalCode")
    , @NamedQuery(name = "FiscalAddress.findByCity", query = "SELECT f FROM FiscalAddress f WHERE f.city = :city")
    , @NamedQuery(name = "FiscalAddress.findByState", query = "SELECT f FROM FiscalAddress f WHERE f.state = :state")
    , @NamedQuery(name = "FiscalAddress.findByRfc", query = "SELECT f FROM FiscalAddress f WHERE f.rfc = :rfc")})
public class FiscalAddress implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_fiscal_address")
    private Integer idFiscalAddress;
    @Size(max = 100)
    @Column(name = "social_reason")
    private String socialReason;
    @Size(max = 50)
    @Column(name = "street")
    private String street;
    @Column(name = "ext_num")
    private Integer extNum;
    @Column(name = "int_num")
    private Integer intNum;
    @Size(max = 100)
    @Column(name = "location")
    private String location;
    @Column(name = "postal_code")
    private Integer postalCode;
    @Size(max = 100)
    @Column(name = "city")
    private String city;
    @Size(max = 100)
    @Column(name = "state")
    private String state;
    @Size(max = 50)
    @Column(name = "rfc")
    private String rfc;
    @OneToMany(mappedBy = "idFiscalAddress")
    private Collection<ProyCotizaFacPagoLink> proyCotizaFacPagoLinkCollection;

    public FiscalAddress() {
    }

    public FiscalAddress(Integer idFiscalAddress) {
        this.idFiscalAddress = idFiscalAddress;
    }

    public Integer getIdFiscalAddress() {
        return idFiscalAddress;
    }

    public void setIdFiscalAddress(Integer idFiscalAddress) {
        this.idFiscalAddress = idFiscalAddress;
    }

    public String getSocialReason() {
        return socialReason;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getExtNum() {
        return extNum;
    }

    public void setExtNum(Integer extNum) {
        this.extNum = extNum;
    }

    public Integer getIntNum() {
        return intNum;
    }

    public void setIntNum(Integer intNum) {
        this.intNum = intNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
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
        hash += (idFiscalAddress != null ? idFiscalAddress.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FiscalAddress)) {
            return false;
        }
        FiscalAddress other = (FiscalAddress) object;
        if ((this.idFiscalAddress == null && other.idFiscalAddress != null) || (this.idFiscalAddress != null && !this.idFiscalAddress.equals(other.idFiscalAddress))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.FiscalAddress[ idFiscalAddress=" + idFiscalAddress + " ]";
    }
    
}
