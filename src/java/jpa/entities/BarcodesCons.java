/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.math.BigInteger;
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
 * @author Toshiba
 */
@Entity
@Table(name = "barcodes_cons")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BarcodesCons.findAll", query = "SELECT b FROM BarcodesCons b"),
    @NamedQuery(name = "BarcodesCons.findByIdRegistro", query = "SELECT b FROM BarcodesCons b WHERE b.idRegistro = :idRegistro"),
    @NamedQuery(name = "BarcodesCons.findByIdLibrary", query = "SELECT b FROM BarcodesCons b WHERE b.id_library = :idLibrary"),
    @NamedQuery(name = "BarcodesCons.findByIdBarcode1", query = "SELECT b FROM BarcodesCons b WHERE b.idBarcode1 = :idBarcode1"),
    @NamedQuery(name = "BarcodesCons.findByIdBarcode2", query = "SELECT b FROM BarcodesCons b WHERE b.idBarcode2 = :idBarcode2")})
public class BarcodesCons implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id_registro")
    private BigInteger idRegistro;
    @Column(name = "id_library")
    private Integer id_library;
    @Size(max = 2147483647)
    @Column(name = "id_barcode_1")
    private String idBarcode1;
    @Size(max = 2147483647)
    @Column(name = "id_barcode_2")
    private String idBarcode2;

    public BarcodesCons() {
    }

    public BigInteger getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(BigInteger idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Integer getIdLibrary() {
        return id_library;
    }

    public void setIdLibrary(Integer idLibrary) {
        this.id_library = idLibrary;
    }


    public String getIdBarcode1() {
        return idBarcode1;
    }

    public void setIdBarcode1(String idBarcode1) {
        this.idBarcode1 = idBarcode1;
    }

    public String getIdBarcode2() {
        return idBarcode2;
    }

    public void setIdBarcode2(String idBarcode2) {
        this.idBarcode2 = idBarcode2;
    }

}
