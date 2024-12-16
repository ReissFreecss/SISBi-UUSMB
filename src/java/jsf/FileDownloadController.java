/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;
 
import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
 
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
 
@ManagedBean
public class FileDownloadController {
     
    private StreamedContent file;
     
    public FileDownloadController() {        
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/RegistroOficialDeMuestras.xlsx");
        file = new DefaultStreamedContent(stream, "xlsx", "RegistroOficialDeMuestras.xlsx");
    }
 
    public StreamedContent getFile() {
        return file;
    }
}