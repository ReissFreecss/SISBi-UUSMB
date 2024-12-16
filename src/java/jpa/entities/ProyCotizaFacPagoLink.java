/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.registry.infomodel.User;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "proy_cotiza_fac_pago_link")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProyCotizaFacPagoLink.findAll", query = "SELECT p FROM ProyCotizaFacPagoLink p")
    , @NamedQuery(name = "ProyCotizaFacPagoLink.findByIdLinkPcfp", query = "SELECT p FROM ProyCotizaFacPagoLink p WHERE p.idLinkPcfp = :idLinkPcfp")})
public class ProyCotizaFacPagoLink implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    
    @Column(name = "id_link_pcfp")
    private Integer idLinkPcfp;
    @JoinColumn(name = "cotiza_id", referencedColumnName = "cotiza_id")
    @ManyToOne
    private Cotizacion cotizaId;
    @JoinColumn(name = "fac_id", referencedColumnName = "fact_id")
    @ManyToOne
    private Factura facId;
    @JoinColumn(name = "id_fiscal_address", referencedColumnName = "id_fiscal_address")
    @ManyToOne
    private FiscalAddress idFiscalAddress;
    @JoinColumn(name = "pago_id", referencedColumnName = "pago_id")
    @ManyToOne
    private Pago pagoId;
    @JoinColumn(name = "id_project", referencedColumnName = "id_project")
    @ManyToOne
    private Project idProject;

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ManyToOne
    private Users idUser;

    public ProyCotizaFacPagoLink() {
    }

    public ProyCotizaFacPagoLink(Integer idLinkPcfp) {
        this.idLinkPcfp = idLinkPcfp;
    }

    public Integer getIdLinkPcfp() {
        return idLinkPcfp;
    }

    public void setIdLinkPcfp(Integer idLinkPcfp) {
        this.idLinkPcfp = idLinkPcfp;
    }

    public Cotizacion getCotizaId() {
        return cotizaId;
    }

    public void setCotizaId(Cotizacion cotizaId) {
        this.cotizaId = cotizaId;
    }

    public Factura getFacId() {
        return facId;
    }

    public void setFacId(Factura facId) {
        this.facId = facId;
    }

    public FiscalAddress getIdFiscalAddress() {
        return idFiscalAddress;
    }

    public void setIdFiscalAddress(FiscalAddress idFiscalAddress) {
        this.idFiscalAddress = idFiscalAddress;
    }

    public Pago getPagoId() {
        return pagoId;
    }

    public void setPagoId(Pago pagoId) {
        this.pagoId = pagoId;
    }

    public Project getIdProject() {
        return idProject;
    }

    public void setIdProject(Project idProject) {
        this.idProject = idProject;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLinkPcfp != null ? idLinkPcfp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProyCotizaFacPagoLink)) {
            return false;
        }
        ProyCotizaFacPagoLink other = (ProyCotizaFacPagoLink) object;
        if ((this.idLinkPcfp == null && other.idLinkPcfp != null) || (this.idLinkPcfp != null && !this.idLinkPcfp.equals(other.idLinkPcfp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.ProyCotizaFacPagoLink[ idLinkPcfp=" + idLinkPcfp + " ]";
    }
    
}
