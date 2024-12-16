/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
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
 * @author omarflores
 */
@Entity
@Table(name = "field_report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FieldReport.findAll", query = "SELECT f FROM FieldReport f")
    , @NamedQuery(name = "FieldReport.findByIdFieldReport", query = "SELECT f FROM FieldReport f WHERE f.idFieldReport = :idFieldReport")
    , @NamedQuery(name = "FieldReport.findByField1", query = "SELECT f FROM FieldReport f WHERE f.field1 = :field1")
    , @NamedQuery(name = "FieldReport.findByField2", query = "SELECT f FROM FieldReport f WHERE f.field2 = :field2")
    , @NamedQuery(name = "FieldReport.findByField3", query = "SELECT f FROM FieldReport f WHERE f.field3 = :field3")
    , @NamedQuery(name = "FieldReport.findByField4", query = "SELECT f FROM FieldReport f WHERE f.field4 = :field4")
    , @NamedQuery(name = "FieldReport.findByField5", query = "SELECT f FROM FieldReport f WHERE f.field5 = :field5")
    , @NamedQuery(name = "FieldReport.findByField6", query = "SELECT f FROM FieldReport f WHERE f.field6 = :field6")
    , @NamedQuery(name = "FieldReport.findByField7", query = "SELECT f FROM FieldReport f WHERE f.field7 = :field7")
    , @NamedQuery(name = "FieldReport.findByField8", query = "SELECT f FROM FieldReport f WHERE f.field8 = :field8")
    , @NamedQuery(name = "FieldReport.findByField9", query = "SELECT f FROM FieldReport f WHERE f.field9 = :field9")
    , @NamedQuery(name = "FieldReport.findByField10", query = "SELECT f FROM FieldReport f WHERE f.field10 = :field10")
    , @NamedQuery(name = "FieldReport.findByField11", query = "SELECT f FROM FieldReport f WHERE f.field11 = :field11")
    , @NamedQuery(name = "FieldReport.findByField12", query = "SELECT f FROM FieldReport f WHERE f.field12 = :field12")
    , @NamedQuery(name = "FieldReport.findByField13", query = "SELECT f FROM FieldReport f WHERE f.field13 = :field13")
    , @NamedQuery(name = "FieldReport.findByField14", query = "SELECT f FROM FieldReport f WHERE f.field14 = :field14")
    , @NamedQuery(name = "FieldReport.findByField15", query = "SELECT f FROM FieldReport f WHERE f.field15 = :field15")
    , @NamedQuery(name = "FieldReport.findByField16", query = "SELECT f FROM FieldReport f WHERE f.field16 = :field16")
    , @NamedQuery(name = "FieldReport.findByField17", query = "SELECT f FROM FieldReport f WHERE f.field17 = :field17")
    , @NamedQuery(name = "FieldReport.findByField18", query = "SELECT f FROM FieldReport f WHERE f.field18 = :field18")
    , @NamedQuery(name = "FieldReport.findByField19", query = "SELECT f FROM FieldReport f WHERE f.field19 = :field19")
    , @NamedQuery(name = "FieldReport.findByField20", query = "SELECT f FROM FieldReport f WHERE f.field20 = :field20")
    , @NamedQuery(name = "FieldReport.findByField21", query = "SELECT f FROM FieldReport f WHERE f.field21 = :field21")
    , @NamedQuery(name = "FieldReport.findByField22", query = "SELECT f FROM FieldReport f WHERE f.field22 = :field22")
    , @NamedQuery(name = "FieldReport.findByField23", query = "SELECT f FROM FieldReport f WHERE f.field23 = :field23")
    , @NamedQuery(name = "FieldReport.findByField24", query = "SELECT f FROM FieldReport f WHERE f.field24 = :field24")
    , @NamedQuery(name = "FieldReport.findByField25", query = "SELECT f FROM FieldReport f WHERE f.field25 = :field25")
    , @NamedQuery(name = "FieldReport.findByField26", query = "SELECT f FROM FieldReport f WHERE f.field26 = :field26")
    , @NamedQuery(name = "FieldReport.findByField27", query = "SELECT f FROM FieldReport f WHERE f.field27 = :field27")
    , @NamedQuery(name = "FieldReport.findByField28", query = "SELECT f FROM FieldReport f WHERE f.field28 = :field28")
    , @NamedQuery(name = "FieldReport.findByField29", query = "SELECT f FROM FieldReport f WHERE f.field29 = :field29")
    , @NamedQuery(name = "FieldReport.findByField30", query = "SELECT f FROM FieldReport f WHERE f.field30 = :field30")
    , @NamedQuery(name = "FieldReport.findByField31", query = "SELECT f FROM FieldReport f WHERE f.field31 = :field31")
    , @NamedQuery(name = "FieldReport.findByField32", query = "SELECT f FROM FieldReport f WHERE f.field32 = :field32")
    , @NamedQuery(name = "FieldReport.findByField33", query = "SELECT f FROM FieldReport f WHERE f.field33 = :field33")
    , @NamedQuery(name = "FieldReport.findByField34", query = "SELECT f FROM FieldReport f WHERE f.field34 = :field34")
    , @NamedQuery(name = "FieldReport.findByField35", query = "SELECT f FROM FieldReport f WHERE f.field35 = :field35")
    , @NamedQuery(name = "FieldReport.findByField36", query = "SELECT f FROM FieldReport f WHERE f.field36 = :field36")
    , @NamedQuery(name = "FieldReport.findByField37", query = "SELECT f FROM FieldReport f WHERE f.field37 = :field37")
    , @NamedQuery(name = "FieldReport.findByField38", query = "SELECT f FROM FieldReport f WHERE f.field38 = :field38")
    , @NamedQuery(name = "FieldReport.findByField39", query = "SELECT f FROM FieldReport f WHERE f.field39 = :field39")
    , @NamedQuery(name = "FieldReport.findByField40", query = "SELECT f FROM FieldReport f WHERE f.field40 = :field40")})
public class FieldReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_field_report")
    private Integer idFieldReport;
    @Column(name = "field1")
    @Temporal(TemporalType.DATE)
    private Date field1;
    @Column(name = "field2")
    @Temporal(TemporalType.DATE)
    private Date field2;
    @Size(max = 50)
    @Column(name = "field3")
    private String field3;
    @Size(max = 50)
    @Column(name = "field4")
    private String field4;
    @Size(max = 50)
    @Column(name = "field5")
    private String field5;
    @Size(max = 50)
    @Column(name = "field6")
    private String field6;
    @Size(max = 50)
    @Column(name = "field7")
    private String field7;
    @Size(max = 50)
    @Column(name = "field8")
    private String field8;
    @Size(max = 50)
    @Column(name = "field9")
    private String field9;
    @Size(max = 50)
    @Column(name = "field10")
    private String field10;
    @Size(max = 50)
    @Column(name = "field11")
    private String field11;
    @Size(max = 50)
    @Column(name = "field12")
    private String field12;
    @Size(max = 50)
    @Column(name = "field13")
    private String field13;
    @Size(max = 50)
    @Column(name = "field14")
    private String field14;
    @Size(max = 50)
    @Column(name = "field15")
    private String field15;
    @Size(max = 50)
    @Column(name = "field16")
    private String field16;
    @Size(max = 50)
    @Column(name = "field17")
    private String field17;
    @Size(max = 50)
    @Column(name = "field18")
    private String field18;
    @Size(max = 50)
    @Column(name = "field19")
    private String field19;
    @Size(max = 50)
    @Column(name = "field20")
    private String field20;
    @Size(max = 50)
    @Column(name = "field21")
    private String field21;
    @Size(max = 50)
    @Column(name = "field22")
    private String field22;
    @Size(max = 50)
    @Column(name = "field23")
    private String field23;
    @Size(max = 50)
    @Column(name = "field24")
    private String field24;
    @Size(max = 50)
    @Column(name = "field25")
    private String field25;
    @Size(max = 50)
    @Column(name = "field26")
    private String field26;
    @Size(max = 50)
    @Column(name = "field27")
    private String field27;
    @Size(max = 50)
    @Column(name = "field28")
    private String field28;
    @Size(max = 50)
    @Column(name = "field29")
    private String field29;
    @Size(max = 50)
    @Column(name = "field30")
    private String field30;
    @Size(max = 50)
    @Column(name = "field31")
    private String field31;
    @Size(max = 50)
    @Column(name = "field32")
    private String field32;
    @Size(max = 50)
    @Column(name = "field33")
    private String field33;
    @Size(max = 50)
    @Column(name = "field34")
    private String field34;
    @Size(max = 50)
    @Column(name = "field35")
    private String field35;
    @Size(max = 50)
    @Column(name = "field36")
    private String field36;
    @Size(max = 50)
    @Column(name = "field37")
    private String field37;
    @Size(max = 50)
    @Column(name = "field38")
    private String field38;
    @Size(max = 50)
    @Column(name = "field39")
    private String field39;
    @Size(max = 50)
    @Column(name = "field40")
    private String field40;
    @JoinColumn(name = "id_report_project", referencedColumnName = "id_report_project")
    @ManyToOne(optional = false)
    private ReportProject idReportProject;

    public FieldReport() {
    }

    public FieldReport(Integer idFieldReport) {
        this.idFieldReport = idFieldReport;
    }

    public Integer getIdFieldReport() {
        return idFieldReport;
    }

    public void setIdFieldReport(Integer idFieldReport) {
        this.idFieldReport = idFieldReport;
    }

    public Date getField1() {
        if(field1==null){
            field1=GregorianCalendar.getInstance().getTime();
            return field1;
        }else{
            return field1;
        } 
    }

    public void setField1(Date field1) {
        this.field1 = field1;
    }

    public Date getField2() {
        if(field2==null){
            field2=GregorianCalendar.getInstance().getTime();
            return field2;
        }else{
            return field2;
        } 
    }

    public void setField2(Date field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getField10() {
        return field10;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }

    public String getField11() {
        return field11;
    }

    public void setField11(String field11) {
        this.field11 = field11;
    }

    public String getField12() {
        return field12;
    }

    public void setField12(String field12) {
        this.field12 = field12;
    }

    public String getField13() {
        return field13;
    }

    public void setField13(String field13) {
        this.field13 = field13;
    }

    public String getField14() {
        return field14;
    }

    public void setField14(String field14) {
        this.field14 = field14;
    }

    public String getField15() {
        return field15;
    }

    public void setField15(String field15) {
        this.field15 = field15;
    }

    public String getField16() {
        return field16;
    }

    public void setField16(String field16) {
        this.field16 = field16;
    }

    public String getField17() {
        return field17;
    }

    public void setField17(String field17) {
        this.field17 = field17;
    }

    public String getField18() {
        return field18;
    }

    public void setField18(String field18) {
        this.field18 = field18;
    }

    public String getField19() {
        return field19;
    }

    public void setField19(String field19) {
        this.field19 = field19;
    }

    public String getField20() {
        return field20;
    }

    public void setField20(String field20) {
        this.field20 = field20;
    }

    public String getField21() {
        return field21;
    }

    public void setField21(String field21) {
        this.field21 = field21;
    }

    public String getField22() {
        return field22;
    }

    public void setField22(String field22) {
        this.field22 = field22;
    }

    public String getField23() {
        return field23;
    }

    public void setField23(String field23) {
        this.field23 = field23;
    }

    public String getField24() {
        return field24;
    }

    public void setField24(String field24) {
        this.field24 = field24;
    }

    public String getField25() {
        return field25;
    }

    public void setField25(String field25) {
        this.field25 = field25;
    }

    public String getField26() {
        return field26;
    }

    public void setField26(String field26) {
        this.field26 = field26;
    }

    public String getField27() {
        return field27;
    }

    public void setField27(String field27) {
        this.field27 = field27;
    }

    public String getField28() {
        return field28;
    }

    public void setField28(String field28) {
        this.field28 = field28;
    }

    public String getField29() {
        return field29;
    }

    public void setField29(String field29) {
        this.field29 = field29;
    }

    public String getField30() {
        return field30;
    }

    public void setField30(String field30) {
        this.field30 = field30;
    }

    public String getField31() {
        return field31;
    }

    public void setField31(String field31) {
        this.field31 = field31;
    }

    public String getField32() {
        return field32;
    }

    public void setField32(String field32) {
        this.field32 = field32;
    }

    public String getField33() {
        return field33;
    }

    public void setField33(String field33) {
        this.field33 = field33;
    }

    public String getField34() {
        return field34;
    }

    public void setField34(String field34) {
        this.field34 = field34;
    }

    public String getField35() {
        return field35;
    }

    public void setField35(String field35) {
        this.field35 = field35;
    }

    public String getField36() {
        return field36;
    }

    public void setField36(String field36) {
        this.field36 = field36;
    }

    public String getField37() {
        return field37;
    }

    public void setField37(String field37) {
        this.field37 = field37;
    }

    public String getField38() {
        return field38;
    }

    public void setField38(String field38) {
        this.field38 = field38;
    }

    public String getField39() {
        return field39;
    }

    public void setField39(String field39) {
        this.field39 = field39;
    }

    public String getField40() {
        return field40;
    }

    public void setField40(String field40) {
        this.field40 = field40;
    }

    public ReportProject getIdReportProject() {
        return idReportProject;
    }

    public void setIdReportProject(ReportProject idReportProject) {
        this.idReportProject = idReportProject;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFieldReport != null ? idFieldReport.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FieldReport)) {
            return false;
        }
        FieldReport other = (FieldReport) object;
        if ((this.idFieldReport == null && other.idFieldReport != null) || (this.idFieldReport != null && !this.idFieldReport.equals(other.idFieldReport))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.FieldReport[ idFieldReport=" + idFieldReport + " ]";
    }
    
}
