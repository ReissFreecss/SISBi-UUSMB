/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import jpa.entities.Dependency;
import jpa.entities.Users;
import jpa.session.DependencyFacade;

/**
 *
 * @author aaron
 */
@Named("calculatorController")
@SessionScoped
public class CalculatorController implements Serializable{
    int libPrice=130;
    double readPrice;
    String machine;
    int len=75;
    String size="1";
    int bioninfo;
    
    String costoBiblioteca;
    String seq;
    String costoSeq;
    String totalSeq;
    String costoBioinfo;
    String totalBioinfo;
    String costoExtrac;
    String totalExtrac;
    
     double several;
    String greatTotal;
    boolean analysis;
    boolean extract;
    int depth=100;
    int genomes=1;
    
    @EJB
    private DependencyFacade ejbFac;

    public String getCostoBiblioteca() {
        return costoBiblioteca;
    }

    public void setCostoBiblioteca(String costoBiblioteca) {
        this.costoBiblioteca = costoBiblioteca;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getCostoSeq() {
        return costoSeq;
    }

    public void setCostoSeq(String costoSeq) {
        this.costoSeq = costoSeq;
    }

    public String getTotalSeq() {
        return totalSeq;
    }

    public void setTotalSeq(String totalSeq) {
        this.totalSeq = totalSeq;
    }

    public String getCostoBioinfo() {
        return costoBioinfo;
    }

    public void setCostoBioinfo(String costoBioinfo) {
        this.costoBioinfo = costoBioinfo;
    }

    public String getTotalBioinfo() {
        return totalBioinfo;
    }

    public void setTotalBioinfo(String totalBioinfo) {
        this.totalBioinfo = totalBioinfo;
    }

    public String getCostoExtrac() {
        return costoExtrac;
    }

    public void setCostoExtrac(String costoExtrac) {
        this.costoExtrac = costoExtrac;
    }

    public String getTotalExtrac() {
        return totalExtrac;
    }

    public void setTotalExtrac(String totalExtrac) {
        this.totalExtrac = totalExtrac;
    }

    
    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

  

    public int getBioninfo() {
        return bioninfo;
    }

    public void setBioninfo(int bioninfo) {
        this.bioninfo = bioninfo;
    }

    public boolean isAnalysis() {
        return analysis;
    }

    public void setAnalysis(boolean analysis) {
        this.analysis = analysis;
    }

    public boolean isExtract() {
        return extract;
    }

    public void setExtract(boolean extract) {
        this.extract = extract;
    }

    public int getDepth() {
        return depth;
    } 

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getGenomes() {
        return genomes;
    }

    public void setGenomes(int genomes) {
        this.genomes = genomes;
    }

    public String getGreatTotal() {
        return greatTotal;
    }

    public void setGreatTotal(String greatTotal) {
        this.greatTotal = greatTotal;
    }

 DecimalFormat df =new DecimalFormat("#.00");
    
 
 
 public void genomicCalculator(){
         bioninfo=50;
        double sizeDouble=Double.parseDouble(size);   
        if (sizeDouble>10 && sizeDouble<100) {
           bioninfo=250;
        }
        
        machine="NextSeq 500";
        readPrice=14.209;
        if (len==100) {
            machine="HiSeq 2000";
            readPrice=11.33;
        }else if (len==300) {
            machine="MiSeq";
            readPrice=263.93;
        }

        double reads;
    reads= ((sizeDouble*1000000)*(double)depth)/((double)len*2)/1000000;
   double price;
           price=reads * readPrice;   
     
   FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List <Dependency> dep = ejbFac.findAll();
        Dependency userDep=new Dependency();
        for (Dependency dependency : dep) {
            if ( us.getIdDependency().equals(dependency)) {
                userDep=dependency;
            }
        }
        
        String who;
        if (userDep.toString().contains("UNAM")) {
            who="UNAM";
        }else{
            if (userDep.getType().equals("Academica o sin fines de lucro")) {
                who="acad";
            }else{
               who="other";
            
            }
        
        
        }
        
        
        double newBioinfo=bioninfo;
        cotiza(price, reads, who);
        
        if (who.equals("acad")) {
           newBioinfo= bioninfo*1.3;
        }
        if (who.equals("other")) {
           newBioinfo= bioninfo*1.5;
        }
         double severalBio=0.0;
        if(analysis){
            
            costoBioinfo="$"+df.format(newBioinfo)+" USD";
           
            if (!who.equals("UNAM")) {
                costoBioinfo+="+ IVA";
                 
            }
           
               severalBio=newBioinfo*genomes;
                  totalBioinfo="$"+severalBio+" USD para "+genomes+" genomas ";
              
                if (!who.equals("UNAM")) {
                      totalBioinfo+="+ IVA";
                  
                }
              
                
      
         
           
        
        }
        int extractPrice=0;
        if (extract) {
            
            switch (who) {
                case "acad":
                    extractPrice=30;
                    break;
                case "other":
                    extractPrice=40;
                    break;
                default:
                    extractPrice=24;
            }
     
          costoExtrac="$"+extractPrice+" USD";
   
             if (!who.equals("UNAM")) {
                 costoExtrac+="+ IVA";
                 
                }
             extractPrice*=genomes;
             totalExtrac="$"+extractPrice+" USD para "+genomes+" muestras ";
            
        }if (!who.equals("UNAM")) {
            totalExtrac+="+ IVA";
                  
                }
        
     greatTotal="$"+df.format(several+severalBio+extractPrice)+" USD";
        if (!who.equals("UNAM")) {
            greatTotal+=" + IVA";
                  
                }
    
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    
     public void cotiza(double price,double reads, String who){
      
        String total;
      String  severalString;
         String lib;
         double libNewPrice;
         double newPrice;
         String priceString;
        
        switch (who) {
            case "acad":
                libNewPrice=libPrice * 1.3;
                newPrice=price*1.3;
                
                total=df.format(libNewPrice+newPrice)+" USD + IVA";
             
                    several=(libNewPrice+newPrice)*genomes;
                    severalString=df.format(several)+" USD + IVA";
                lib=df.format(libNewPrice)+" USD + IVA";
                priceString=df.format(newPrice)+" USD + IVA";
                break;
            case "other":
                libNewPrice=libPrice * 1.5;
                newPrice=price*1.5;
              
                total=df.format(libNewPrice+newPrice)+" USD + IVA";
                
                several=(libNewPrice+newPrice)*genomes;
               severalString=df.format(several)+" USD + IVA";
                lib=df.format(libNewPrice)+" USD + IVA";
                priceString=df.format(newPrice)+" USD + IVA";
                
                break;
            default:
                 total=df.format(libPrice+price)+" USD";
               
                several=(libPrice+price)*genomes;
                severalString=df.format(several)+" USD";
                lib=df.format(libPrice)+" USD";
                priceString=df.format(price)+" USD";
                
              
        }
        costoBiblioteca="$"+lib+" por muestra";
        seq="Para un genoma de "+size+" Mb a una profundidad de "+depth+" X: ";
        seq+="Se requieren: ";
        if (reads>1) {
            seq+=df.format(reads)+" millones de ";
            
            
        }else{
            reads*=1000000;
            seq+=df.format(reads);
                  
        
        }
        seq+=" reads 2x"+len+" "+machine+" $ "+priceString;
        
        costoSeq="$"+total+"";
        totalSeq="$"+severalString+" para "+genomes+" muestras";
        
    
    
    }
    
    
}
