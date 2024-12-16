/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aaron
 */
@Entity
@Table(name = "files")
@XmlRootElement

public class Files implements Serializable {

     private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_file")
    private Integer idFile;
    
    @JoinColumn(name = "id_project", referencedColumnName = "id_project")
    @ManyToOne(optional = false)
    private Project idProject;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "file_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fileDate;
    
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "path")
    private String path;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "uploader")
    private String uploader;
    
    @Size(max = 2000)
    @Column(name = "comment")
    private String comment;
   
    @Size(max = 250)
    @Column(name = "file_name")
    private String fileName;
    
    @Size(max = 20)
    @Column(name = "file_type")
    private String fileType;
    
    
    
     public Files() {
    }

    public Files(Integer idFile) {
        this.idFile = idFile;
    }

    public Files(Integer idFile, String path, String uploader, Project idProject, Date fileDate) {
        this.idFile = idFile;
        this.path = path;
        this.uploader = uploader;
        this.idProject = idProject;
        this.fileDate = fileDate;
    }

    public Integer getIdFile() {
        return idFile;
    }

    public void setIdFile(Integer idFile) {
        this.idFile = idFile;
    }

    public Project getIdProject() {
        return idProject;
    }

    public void setIdProject(Project idProject) {
        this.idProject = idProject;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Genome)) {
            return false;
        }
        Files other = (Files) object;
        if ((this.idFile == null && other.idFile != null) || (this.idFile != null && !this.idFile.equals(other.idFile))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
      hash += (idFile != null ? idFile.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return path;
    }

    
}
