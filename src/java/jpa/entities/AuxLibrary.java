/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

/**
 *
 * @author aaron
 */
@Named("auxLibrary")
@SessionScoped
public class AuxLibrary implements Serializable{
 
    
    private List<String>  listSample;
    private Library library;

    public List<String> getListSample() {
        return listSample;
    }

    public void setListSample(List<String> listSample) {
        this.listSample = listSample;
    }

 
    public Library getLibrary() {
        if (library==null) {
            library= new Library();
        }
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }
    
      public AuxLibrary() {
    }
      
       public AuxLibrary(List<String> listSample,Library library) {
        this.library = library;
        this.listSample = listSample;
       }
       

}
