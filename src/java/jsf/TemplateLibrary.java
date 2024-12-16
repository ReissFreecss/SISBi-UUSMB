/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.Serializable;
import java.util.List;
import jpa.entities.Barcodes;
import jpa.entities.Library;
import jpa.entities.Sample;

/**
 *
 * @author Omar
 */
public class TemplateLibrary implements Serializable{
    private Sample sample;
    private Library library;
    private Barcodes barcodes;

    public Barcodes getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(Barcodes barcodes) {
        this.barcodes = barcodes;
    }
    
    
    
    public Sample getSample() {
        return sample;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
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
    
    TemplateLibrary(Sample sample, Library library, Barcodes barcodes) {
        this.sample = sample;
        this.library = library;
        this.barcodes = barcodes;
    }
    /*
    public TemplateLibrary(Sample sample, Library library){
        this.sample = sample;
        this.library = library;
    }
    
    */
    
}
