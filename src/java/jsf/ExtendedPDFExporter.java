/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.component.celleditor.CellEditor;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.component.export.PDFExporter;

/**
 *
 * @author aaron
 */
@Named("exporter")
@SessionScoped
public class ExtendedPDFExporter extends PDFExporter implements Serializable{
    
    public void exportPDF(DataTable table, String filename) throws IOException {
    FacesContext context = FacesContext.getCurrentInstance();
    Exporter exporter = new ExtendedPDFExporter();
    
   context.responseComplete();
}

    @Override
    protected String exportValue(FacesContext context, UIComponent component) {
        if (component instanceof CellEditor) {
            return exportValue(context, ((CellEditor) component).getFacet("output"));
        }
        else if (component instanceof HtmlGraphicImage) {
            return (String) component.getAttributes().get("alt");
        }
        else {
            return super.exportValue(context, component);
        }
    }
}
