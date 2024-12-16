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
@Named("calculatorMetaController")
@SessionScoped
public class MetagenomicCalculatorController implements Serializable{
     
       int libPrice=130;
       double readPrice=14.21;
       int bioninfo=550;
       
       int numsamples=1;
       int len=75;
       int numreads=1;
       String who;
       boolean analysis=true;
       String machine;
       
       
    String costoBiblioteca;
    String seq;
    String costoSeq;
    String totalSeq;
    String costoBioinfo;
    String totalBioinfo;
    
    String greatTotal;
    
    
    @EJB
    private DependencyFacade ejbFac;

    public String getGreatTotal() {
        return greatTotal;
    }

    public void setGreatTotal(String greatTotal) {
        this.greatTotal = greatTotal;
    }
    
    
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
    
    
    

    public int getNumsamples() {
        return numsamples;
    }

    public void setNumsamples(int numsamples) {
        this.numsamples = numsamples;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getNumreads() {
        return numreads;
    }

    public void setNumreads(int numreads) {
        this.numreads = numreads;
    }

    public boolean isAnalysis() {
        return analysis;
    }

    public void setAnalysis(boolean analysis) {
        this.analysis = analysis;
    }
        DecimalFormat df =new DecimalFormat("#.00");
    public void metagenomicCalculator(){
    
    
      if (len > 300 && len<75) {
            System.out.println("Ponga numero valido");
            
        }
       
        switch (len) {
            case 150:
                readPrice=20.72;
                break;
            case 100:
                machine="HiSeq 2000";
                readPrice=11.33;     
                break;
            case 300:
                machine="MiSeq";
                readPrice=263.93;
                break;
           default:
               machine="NextSeq500";
               readPrice=14.21;
        }
        
          
   FacesContext context = FacesContext.getCurrentInstance();
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List <Dependency> dep = ejbFac.findAll();
        Dependency userDep=new Dependency();
        for (Dependency dependency : dep) {
            if ( us.getIdDependency().equals(dependency)) {
                userDep=dependency;
            }
        }
        
       
        if (userDep.toString().contains("UNAM")) {
            who="UNAM";
        }else{
            if (userDep.getType().equals("Academica o sin fines de lucro")) {
                who="acad";
            }else{
               who="other";
            
            }
        
        
        }
        
        double price = (numreads*readPrice)*10;
        cotiza(price);
        
        
        double newBioinfo=bioninfo;
        
        if (who.equals("acad")) {
            
            newBioinfo=bioninfo*1.3;
        }
        
        if (who.equals("other")) {
            newBioinfo=bioninfo*1.5;
        }
        double severalBio=0.0;
         if (analysis) {
          
            
            costoBioinfo="$"+newBioinfo+"USD";
            
            if (!who.equals("UNAM")) {
                costoBioinfo+=" + IVA";
            }
            
            severalBio=newBioinfo*numsamples;
            
                totalBioinfo="$"+severalBio+" USD para "+numsamples+" muestras";
              
                if (!who.equals("UNAM")) {
                      totalBioinfo+=" + IVA";
                  
                }
              
                
            
            
        }
         String aux []=totalSeq.split(" ");
         greatTotal="$"+df.format(severalBio+Double.parseDouble(aux[1]))+" USD";
                
                
           if (!who.equals("UNAM")) {
                      greatTotal+=" + IVA";
                  
                }
         
    
    }
    
    
         
    public void cotiza(double price){
  
    String total;
    String priceString;
    String libPriceString;
     double libNewPrice=libPrice;
         double newPrice=price;
        
        switch (who) {
        case "acad":
             libNewPrice=libPrice * 1.3;
                newPrice=price*1.3;
        
            total=df.format((libNewPrice+newPrice)*numsamples)+" USD + IVA ";
            libPriceString=df.format(libNewPrice)+" USD + IVA"; 
            priceString=df.format(newPrice)+" USD + IVA";
                      
            break;
        case "other":
             libNewPrice=libPrice * 1.5;
                newPrice=price*1.5;
              total=df.format((libNewPrice+newPrice)*numsamples)+" USD + IVA ";
            libPriceString=df.format(libNewPrice)+" USD + IVA"; 
            priceString=df.format(newPrice)+" USD + IVA";
            break;
        default:
            total=df.format((libNewPrice+newPrice)*numsamples)+" USD ";
            libPriceString=df.format(libNewPrice)+" USD"; 
            priceString=df.format(newPrice)+" USD";
            
    }
        
  int totalreads=numreads*10;
  
  double allprice= newPrice*numsamples;
        costoBiblioteca="$"+libPriceString+" por muestra";
       seq="Se requieren "+totalreads+" millones de reads 2x"+len+" "+machine;
       
      costoSeq="$"+priceString+" Para "+numsamples+ " muestra(s): $"+df.format(allprice)+" USD";
         if (!who.equals("UNAM")) {
                costoSeq+="+ IVA";
            }
         
         totalSeq="$ "+total;
 
    }   
    
       
    
    
    
    
    
}
