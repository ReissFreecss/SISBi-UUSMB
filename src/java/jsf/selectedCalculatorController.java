/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author aaron
 */
@Named("calculatorSelectedController")
@SessionScoped
public class selectedCalculatorController implements Serializable{
    
    int type;
    
    boolean dna;
    boolean rna;
    boolean amplicon;
    boolean meta;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isDna() {
        return dna;
    }

    public void setDna(boolean dna) {
        this.dna = dna;
    }

    public boolean isRna() {
        return rna;
    }

    public void setRna(boolean rna) {
        this.rna = rna;
    }

    public boolean isAmplicon() {
        return amplicon;
    }

    public void setAmplicon(boolean amplicon) {
        this.amplicon = amplicon;
    }

    public boolean isMeta() {
        return meta;
    }

    public void setMeta(boolean meta) {
        this.meta = meta;
    }
    
    
    public void witchCal(){
    
        switch (type) {
            case 1:
                amplicon=true;
                rna=false;
                dna=false;
                meta=false;
                break;
            case 2:
                amplicon=false;
                rna=false;
                dna=true;
                meta=false;
                break;
            case 3:
                amplicon=false;
                rna=true;
                dna=false;
                meta=false;
                break;
            case 4:
                amplicon=false;
                rna=false;
                dna=false;
                meta=true;
                break;
            default:
               amplicon=false;
                rna=false;
                dna=true;
                meta=false;
        }
    
    
    
    
    
    
    
    }
    
    
}
