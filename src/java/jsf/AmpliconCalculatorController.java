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

@Named("calculatorAmpliconController")
@SessionScoped
public class AmpliconCalculatorController implements Serializable {

    int libPrice = 24;
    double readPrice = 26.393;
    double bioninfo = 50;
    String who;

    boolean analysis;
    int numsamples=1;
    int numreads=1;
    int numampli=1;

    String totalAmpl;
    String costoAmpl;
    String seq;
    String costoSeq;
    String totalSeq;
    
    String totalSeqAmpl;
    String costoBioinfo;
    String totalBioinfo;
    
    String greatTotal;
    
    

    @EJB
    private DependencyFacade ejbFac;

    public String getTotalSeqAmpl() {
        return totalSeqAmpl;
    }

    public void setTotalSeqAmpl(String totalSeqAmpl) {
        this.totalSeqAmpl = totalSeqAmpl;
    }

    
    
    public String getCostoSeq() {
        return costoSeq;
    }

    public void setCostoSeq(String costoSeq) {
        this.costoSeq = costoSeq;
    }

    public String getGreatTotal() {
        return greatTotal;
    }

    public void setGreatTotal(String greatTotal) {
        this.greatTotal = greatTotal;
    }

    
    public String getTotalAmpl() {
        return totalAmpl;
    }

    public void setTotalAmpl(String totalAmpl) {
        this.totalAmpl = totalAmpl;
    }

    public String getCostoAmpl() {
        return costoAmpl;
    }

    public void setCostoAmpl(String costoAmpl) {
        this.costoAmpl = costoAmpl;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
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

    public int getNumampli() {
        return numampli;
    }

    public void setNumampli(int numampli) {
        this.numampli = numampli;
    }
 DecimalFormat df =new DecimalFormat("#.00");
    public void ampliconCalculator() {

     

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
          newBioinfo=bioninfo * 1.3;
        }

        if (who.equals("other")) {
          newBioinfo=bioninfo * 1.5;
        }
       double costoBio=0.0;
        if (analysis) {
            System.out.println("Si requiere analisis bioinformatico agregar: \n");
            System.out.println("Costo por muestra = " + bioninfo + " USD");
            
            costoBioinfo="$"+df.format(newBioinfo) + " USD";
            
            if (!who.equals("UNAM")) {
                costoBioinfo+=" + IVA";
               
            }
            
            costoBio=newBioinfo*numsamples;
            totalBioinfo="Para " + numsamples + " muestras = $" + df.format(costoBio) + " USD";
         
            if (!who.equals("UNAM")) {
                
                totalBioinfo+=" + IVA";
             
            }

            System.out.println("\n\n");

        }
        String aux []=totalSeqAmpl.split(" ");
      
              
        greatTotal="$"+df.format(Double.parseDouble(aux[1])+(costoBio))+" USD ";
         if (!who.equals("UNAM")) {
                
                greatTotal+="+ IVA";
             
            }
    }

    public void cotiza() {

        String total;
        String priceString;
        String libPriceString;
        int amplisam = numsamples * numampli;
        double costoLib=libPrice;
        totalAmpl=amplisam+"";
        
        switch (who) {
            case "acad":
                costoLib=readPrice*1.3;
                priceString = df.format((amplisam) * (libPrice*1.3)) + " USD + IVA ";
                libPriceString = df.format((readPrice*1.3) * numreads * amplisam) + " USD + IVA ";
                break;
            case "other":
                costoLib=readPrice*1.5;
                priceString = df.format((amplisam) * (libPrice+1.5)) + " USD + IVA ";
                libPriceString =df.format((readPrice*1.5) * numreads * amplisam) + " USD + IVA ";
                break;
            default:
                priceString = df.format(amplisam * libPrice) + " USD";
                libPriceString = df.format(readPrice * numreads * amplisam) + " USD";

        }
         costoAmpl="Para " + amplisam + " amplicon(es) a $"+df.format(costoLib)+" c/u = $" + priceString;

       costoSeq="100mil reads 2x300 MiSeq (recomendado por muestra por amplicon) $" +df.format(costoLib)+" USD";
         if (!who.equals("UNAM")) {
           costoSeq+=" + IVA";
        
        }
        if (numreads > 1) {
            seq="Lecturas solicitadas = " + numreads + "00mil";
        }
        String[] aux1 = priceString.split(" ");
        String[] aux2 = libPriceString.split(" ");

      totalSeq="Para " + amplisam + " amplicon(es): $" + aux2[0] + " USD";
         if (!who.equals("UNAM")) {
           totalSeq+=" + IVA";
           
        }
      
       total = df.format(Double.parseDouble(aux1[0]) + Double.parseDouble(aux2[0]))+ " USD";
       if (!who.equals("UNAM")) {
            total+=" + IVA";
           
        }
        
        totalSeqAmpl="$ "+total;

    }

}
