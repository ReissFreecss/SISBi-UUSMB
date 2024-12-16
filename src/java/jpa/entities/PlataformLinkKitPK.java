/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author omarflores
 */
@Embeddable
public class PlataformLinkKitPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "id_plataform")
    private int idPlataform;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_kit")
    private int idKit;

    public PlataformLinkKitPK() {
    }

    public PlataformLinkKitPK(int idPlataform, int idKit) {
        this.idPlataform = idPlataform;
        this.idKit = idKit;
    }


    public int getIdPlataform() {
        return idPlataform;
    }

    public void setIdPlataform(int idPlataform) {
        this.idPlataform = idPlataform;
    }

    public int getIdKit() {
        return idKit;
    }

    public void setIdKit(int idKit) {
        this.idKit = idKit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idPlataform;
        hash += (int) idKit;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlataformLinkKitPK)) {
            return false;
        }
        PlataformLinkKitPK other = (PlataformLinkKitPK) object;
        if (this.idPlataform != other.idPlataform) {
            return false;
        }
        if (this.idKit != other.idKit) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.PlataformLinkKitPK[ idPlataform=" + idPlataform + ", idKit=" + idKit + " ]";
    }
    
}
