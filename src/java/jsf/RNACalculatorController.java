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

@Named("calculatorRNAController")
@SessionScoped
public class RNACalculatorController implements Serializable{
    
    
      int libPrice;
      double readPrice;
      double bioninfo=50;
      
      
      String who;
      boolean small=true;
      boolean analysis=true;
      int numsamples=1;
      int numreads=1;
          @EJB
    private DependencyFacade ejbFac;
      
      
    String costoBiblioteca;
    String costoTotalBiblioteca;
    String seq;
    String costoSeq;
    String totalSeq;
    
    String costoBioinfo;
    String totalBioinfo;
    
     String greatTotal;

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

    public String getCostoTotalBiblioteca() {
        return costoTotalBiblioteca;
    }

    public void setCostoTotalBiblioteca(String costoTotalBiblioteca) {
        this.costoTotalBiblioteca = costoTotalBiblioteca;
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

    

    public boolean isSmall() {
        return small;
    }

    public void setSmall(boolean small) {
        this.small = small;
    }

    public boolean isAnalysis() {
        return analysis;
    }

    public void setAnalysis(boolean analysis) {
        this.analysis = analysis;
    }

    public int getNumsamples() {
        return numsamples;
    }

    public void setNumsamples(int numsamples) {
        this.numsamples = numsamples;
    }

    public int getNumreads() {
        return numreads;
    }

    public void setNumreads(int numreads) {
        this.numreads = numreads;
    }
     DecimalFormat df =new DecimalFormat("#.00");
     
    public void rnaCalculator(){
    
             if (small) {
            libPrice=235;
            readPrice=88.81;
        }else{
            libPrice=146;
            readPrice=142.09;
             
             
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
        
        cotiza();
         double newBioinfo=bioninfo;
        if (who.equals("acad")) {
            newBioinfo= bioninfo*1.3;
        }
        
        if (who.equals("other")) {
            newBioinfo= bioninfo*1.5;
        }
       double costoBio=0.0;
        if (analysis) {
          
            costoBioinfo="$"+df.format(newBioinfo)+" USD";
            if (!who.equals("UNAM")) {
               costoBioinfo+=" + IVA";
            }
            costoBio=newBioinfo*numsamples;
            totalBioinfo="Para "+numsamples+" muestras= $ "+df.format(costoBio)+" USD";
           
            if (!who.equals("UNAM")) {
                 totalBioinfo+=" + IVA";
            }
           
        }
    String aux[]= totalSeq.split(" ");
      
    greatTotal="$"+df.format(Double.parseDouble(aux[1])+(costoBio))+" USD";
    
     if (!who.equals("UNAM")) {
                
                greatTotal+="+ IVA";
             
            }
    }
    
    public void cotiza(){

    double price;
    String seqConf;
    String sample;
    String reads;
    if (small) 
        seqConf="2x38";
    else
        seqConf="2x75";
    
    switch (who) {
        case "acad":
            costoBiblioteca="$"+(libPrice*1.3)+" USD + IVA por muestra";
            price=libPrice*1.3;
            sample=df.format(numsamples*price)+"";
          
            reads= df.format((readPrice*1.3)*numreads*numsamples)+" USD + IVA";
            
            break;
        case "other":
            costoBiblioteca="$"+(libPrice*1.5)+" USD + IVA por muestra";
             price=libPrice*1.5;
            sample=df.format(numsamples*price)+"";
           
            reads= df.format(readPrice*numreads*numsamples)+" USD + IVA";
            break;
        default:
            costoBiblioteca="$"+libPrice+" USD por muestra";
             price=libPrice;
            sample=df.format(numsamples*price)+"";
            reads= df.format(readPrice*numreads*numsamples)+" USD";
            
    }
    
   
    costoTotalBiblioteca="Para "+numsamples+" muestras = $"+sample;
    
  
    if (numreads>1) {
        
        seq=(numreads*10)+" millones de reads "+seqConf+" NextSeq500 (solicitado por usuario por muestra) $"+readPrice+" USD";
        
     
    }else{
        
        seq="10 millones de reads "+seqConf+" NextSeq500 (recomendado por muestra) $"+readPrice+" USD";
        

    
    }
    if (!who.equals("UNAM")) {
        seq+=" + IVA";
      
            }
    
    costoSeq="Para "+ numsamples+" muestras = $"+reads;
    
    
    
    String [] aux1=sample.split(" ");
    String [] aux2=reads.split(" ");
    String total=df.format(Double.parseDouble(aux1[0])+Double.parseDouble(aux2[0]))+" USD";
    
    totalSeq="$ "+total;
     
     if (!who.equals("UNAM")) {
         totalSeq+=" + IVA";
            
            }
    
}
    
    
      
}
