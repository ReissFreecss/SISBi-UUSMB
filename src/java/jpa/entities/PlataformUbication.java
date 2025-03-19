package jpa.entities;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "plataform_ubication")
public class PlataformUbication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ub_id", nullable = false)
    private Integer ubId;
    
    @Column(name = "id_plataform", nullable = false)
    private Integer idPlataform;
    
    @Column(name = "identifier", length = 50, nullable = false)
    private String identifier;
    
    @Column(name = "instalation", length = 200, nullable = false)
    private String instalation;
    
    @Column(name = "calle_no", length = 255)
    private String calleNo;
    
    @Column(name = "colonia", length = 255)
    private String colonia;
    
    @Column(name = "municipio", length = 255)
    private String municipio;
    
    @Column(name = "estado", length = 255)
    private String estado;
    
    @Column(name = "pais", length = 100)
    private String pais;
    
    public PlataformUbication() {
    }

    public Integer getUbId() {
        return ubId;
    }

    public void setUbId(Integer ubId) {
        this.ubId = ubId;
    }

    public Integer getIdPlataform() {
        return idPlataform;
    }

    public void setIdPlataform(Integer idPlataform) {
        this.idPlataform = idPlataform;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getInstalation() {
        return instalation;
    }

    public void setInstalation(String instalation) {
        this.instalation = instalation;
    }

    public String getCalleNo() {
        return calleNo;
    }

    public void setCalleNo(String calleNo) {
        this.calleNo = calleNo;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
