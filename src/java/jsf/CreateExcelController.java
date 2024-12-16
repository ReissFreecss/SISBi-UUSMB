
package jsf;

import static com.sun.xml.ws.spi.db.BindingContextFactory.LOGGER;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import jpa.entities.Comments;
import jpa.entities.Project;
import jpa.entities.Sample;
import jpa.entities.Users;
import jpa.session.CommentsFacade;
import jpa.session.SampleFacade;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.FillPatternType;
/**
 *
 * @author Fernando Serdan Betanzos 30/10/19
 */
@Named("createExcelController")
@SessionScoped
public class CreateExcelController implements Serializable {
     @EJB
    private jpa.session.SampleFacade ejbFacade;
    @EJB
    private CommentsFacade commentFac;
    
    String FilePath = PathFiles.LinkCreateExcel;
    
    String separatorPath = PathFiles.separatorPath;
    
    String PathCreateReports = PathFiles.DirectoryCreateExcel;

    public SampleFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(SampleFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public CommentsFacade getCommentFac() {
        return commentFac;
    }

    public void setCommentFac(CommentsFacade commentFac) {
        this.commentFac = commentFac;
    }
    
    
       public CellStyle EstiloCeldas(XSSFWorkbook workbook){
        
         CellStyle styleCeldas=workbook.createCellStyle();
         
         styleCeldas.setBorderBottom( BorderStyle.THIN );
         styleCeldas.setBorderLeft(BorderStyle.THIN);
         styleCeldas.setBorderRight(BorderStyle.THIN);
         styleCeldas.setBorderTop(BorderStyle.THIN);
         styleCeldas.setAlignment(HorizontalAlignment.LEFT);
         
         return styleCeldas;
    }
       public XSSFSheet sizeCell(XSSFWorkbook book,String SheetName){
         XSSFSheet sheet = book.createSheet(SheetName);
         
         sheet.setColumnWidth(0, 500);
         sheet.setColumnWidth(1, 4000);
         sheet.setColumnWidth(2, 8000);
         sheet.setColumnWidth(3, 8000);
         sheet.setColumnWidth(4, 4000);
         sheet.setColumnWidth(5, 7000);
         sheet.setColumnWidth(6, 6000);
         sheet.setColumnWidth(7, 6000);
         sheet.setColumnWidth(8, 6000);
         sheet.setColumnWidth(9, 7000);
         sheet.setColumnWidth(10, 4000);
         sheet.setColumnWidth(11, 4000);
         sheet.setColumnWidth(12, 4000);
         sheet.setColumnWidth(13, 5000);
         sheet.setColumnWidth(14, 5000);
         sheet.setColumnWidth(15, 5000);
         sheet.setColumnWidth(16, 5000);
         sheet.setColumnWidth(17, 8000);
         
         return sheet;         
    }
       public XSSFSheet version(XSSFWorkbook book,String SheetName){
         XSSFSheet sheet = book.getSheet(SheetName);
         
         XSSFRow row2= sheet.createRow(0);
         
         XSSFCell version=row2.createCell(7);
       
         row2.setHeight((short)700);
         
         version.setCellValue("Codigo: F01_PT01\nversion:01");
        
         return sheet;
       }
       public CellStyle colorCell(XSSFWorkbook workbook){
    
         CellStyle styleUser=workbook.createCellStyle();
          
         XSSFFont font = workbook.createFont();
         
         font.setBold(true);
         
         styleUser.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
         styleUser.setFillPattern(FillPatternType.SOLID_FOREGROUND);
         styleUser.setBorderBottom(BorderStyle.THICK);
         styleUser.setAlignment(HorizontalAlignment.CENTER);
         styleUser.setBorderTop(BorderStyle.THICK);
         styleUser.setFont(font);
         
         return styleUser;
    }
        public CellStyle EstiloLibreriaEnc(XSSFWorkbook workbook){
        
         CellStyle styleLibreriasEnc=workbook.createCellStyle();
         XSSFFont font = workbook.createFont();
         font.setBold(true);
         styleLibreriasEnc.setFillForegroundColor(IndexedColors.WHITE.getIndex());
         styleLibreriasEnc.setFillPattern( FillPatternType.SOLID_FOREGROUND);
         styleLibreriasEnc.setBorderBottom(BorderStyle.THICK );
         styleLibreriasEnc.setBorderTop(BorderStyle.THICK);
         styleLibreriasEnc.setAlignment(HorizontalAlignment.LEFT);
         styleLibreriasEnc.setFont(font);
         
         return styleLibreriasEnc;
    }
        public CellStyle EstiloSubtitulos(XSSFWorkbook workbook){
     
         CellStyle styleSub=workbook.createCellStyle();
          
         XSSFFont font = workbook.createFont();
         
         font.setBold(true);
         
         styleSub.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
         styleSub.setFillPattern(FillPatternType.SOLID_FOREGROUND);
         styleSub.setBorderBottom(BorderStyle.THICK );
         styleSub.setBorderLeft(BorderStyle.THICK);
         styleSub.setAlignment(HorizontalAlignment.CENTER);
         styleSub.setFont(font);
         
         return styleSub;
    }
        public CellStyle EstiloCerrado(XSSFWorkbook workbook){
        
         CellStyle styleClose=workbook.createCellStyle();
         XSSFFont font = workbook.createFont();
         font.setBold(true);
         styleClose.setFillForegroundColor(IndexedColors.WHITE.getIndex());
         styleClose.setFillPattern(FillPatternType.SOLID_FOREGROUND);
         styleClose.setBorderBottom(BorderStyle.THICK );
         styleClose.setBorderTop(BorderStyle.THICK );
         styleClose.setBorderLeft(BorderStyle.THICK);
         styleClose.setBorderRight(BorderStyle.THICK);
         styleClose.setAlignment(HorizontalAlignment.CENTER);
         styleClose.setFont(font);
         
         
         return styleClose;
    }
     ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
         public void createExcelNextera() throws ClassNotFoundException, SQLException{
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Sample> samples=(List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");
        
      
         try {
             if(samples.isEmpty()){
                 System.out.println("no han seleccionado ninguna muestra");

                    context.addMessage(null, new FacesMessage("¡ERROR!",  "No han seleccionado ninguna muestra para Generar el reporte Nextera" ) );
             }else{
             
        Timestamp time = new Timestamp(System.currentTimeMillis());
        
                    
         String[] titulos = {"","Usuario",""};//nombre de las columnas
         String[] subtitulos = {"ID de la \nmuestra","ID del"+"\n"+"proyecto","Etiqueta del"+"\n"+"tubo"};
         String[] encabezado = {"Tagmentación"};
         String[] tags={"","Tags"};
         String[] tamanio={"Selección de tamaño"};
         String[] validacion={"validación de Biblioteca",""};
         String[] subencabezado = {"Tiempo(min)","N7","S5","Tamaño\npromedio","Archivo gel\n1.5%","[ng/µL]","[nM]","tamaño","Archivo\nBioanalizador","Fecha de\ninicio de\nconstrucción","Fecha de\nTermino de\nconstrucción","Corrida \n secuencia"};
         
         XSSFWorkbook workbook = new XSSFWorkbook();
         XSSFSheet pagina = sizeCell(workbook,"Nextera");
         
         String Nombre=us.getFirstName().substring(0,1);
         String apellido_p=us.getPLastName().substring(0,1);
         String apellido_m=us.getMLastName().substring(0,1);
         String iniciales=Nombre+apellido_p+apellido_m;
         System.out.println(iniciales);
         
         String nombreFichero = PathCreateReports + "reportesNextera"+separatorPath+"F01_PT01_" + pj.getIdProject().replaceAll("[:.-]", "_") + "_" + time.toString().replace(" ", "_").replaceAll("[:.-]", "_") + "_" + iniciales + ".xlsx";//directorio donde se crea el archivo
         File file = new File(nombreFichero);
         
         
         
       
         CellStyle styleTopRight=workbook.createCellStyle();
         CellStyle style7=workbook.createCellStyle();
         CellStyle estilo=workbook.createCellStyle();
         XSSFFont font = workbook.createFont();
         
         font.setBold(true);
         
         styleTopRight.setBorderTop(BorderStyle.THICK);
         styleTopRight.setBorderRight(BorderStyle.THICK);
         
         style7.setBorderTop(BorderStyle.THICK);
         style7.setBorderLeft(BorderStyle.THICK);
         
         estilo.setAlignment(HorizontalAlignment.CENTER);
         estilo.setFont(font);
        
         
         XSSFRow row2= pagina.createRow(1);
         XSSFCell titulo=row2.createCell(3);
         titulo.setCellStyle(estilo);
         titulo.setCellValue("Construcción de Bibliotecas Nextera XT");
         version(workbook,"Nextera");
         XSSFRow row= pagina.createRow(9);//crea una fila en la pocision 10 del archivo
            ///creamos el titulo de la celda 1 a la 3 del archivo con el nombre Usuario
            
          for(int t=0;t<titulos.length;t++){
              XSSFCell cell=row.createCell(t+1);
           
            cell.setCellStyle(colorCell(workbook));
            cell.setCellValue(titulos[t]);
            row.createCell(0).setCellStyle(EstiloCeldas(workbook));
         
           
            //creamos el titulo de la celda 4 a la 10 del archivo con el nombre Analisis de calidad
            for(int x=0;x<encabezado.length;x++){
              XSSFCell cell2=row.createCell(x+4);
              cell2.setCellStyle(EstiloCerrado(workbook));
              cell2.setCellValue(encabezado[x]);
             
            }
            for (int s = 0; s < tags.length; s++) {
              
              XSSFCell cel=row.createCell(s+5);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(tags[s]);
              
            }
            for (int c = 0; c < tamanio.length; c++) {
              row.createCell(7).setCellStyle(style7);
              
              XSSFCell cel=row.createCell(c+8);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(tamanio[c]);
            }
             for (int d = 0; d < validacion.length; d++) {
              row.createCell(9).setCellStyle(style7);
              //row.createCell(10).setCellStyle(style7);
              XSSFCell cel=row.createCell(d+10);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(validacion[d]);
              row.createCell(12).setCellStyle(styleTopRight);
            }
          }
          
          row= pagina.createRow(10);//se crea una fila en la posicion 11 del archivo
              //creamos los nombres de las columnas B,C,D como: consecutivo,Nombre del proyecto,Nombre de la muestra
          for (int i = 0; i < subtitulos.length; i++) {
              
              XSSFCell cell=row.createCell(i+1);
              cell.setCellStyle(EstiloSubtitulos(workbook));
              row.setHeight((short)1000);
              cell.setCellValue(subtitulos[i]);
              //creamos los nombres de las columnas de la E a la K.
          }
           
          for (int s = 0; s < subencabezado.length; s++) {
              
              XSSFCell cel=row.createCell(s+4);
              cel.setCellStyle(EstiloCerrado(workbook));
              cel.setCellValue(subencabezado[s]);
            }
          
           
          for (int i = 0; i < samples.size(); i++) {
            
            
            row= pagina.createRow(i+11);
            
             String lib=samples.get(i).getIdTube();
             int id=samples.get(i).getIdSample();
             String proj=pj.getIdProject();
             ////////////////////////////////
            for(int x=0;x<1;x++){
                
                 System.out.println("valor de lib:"+lib);
                 XSSFCell id_muestra=row.createCell(1);
                 XSSFCell project=row.createCell(2);
                 XSSFCell cell=row.createCell(3);
               
                cell.setCellStyle(EstiloCeldas(workbook));
                cell.setCellValue(lib);
                
                id_muestra.setCellStyle(EstiloCeldas(workbook));
                id_muestra.setCellValue(id);
                
                project.setCellStyle(EstiloCeldas(workbook));
                project.setCellValue(proj);
                
                row.createCell(4).setCellStyle(EstiloCeldas(workbook));
                row.createCell(5).setCellStyle(EstiloCeldas(workbook));
                row.createCell(6).setCellStyle(EstiloCeldas(workbook));
                row.createCell(7).setCellStyle(EstiloCeldas(workbook));
                row.createCell(8).setCellStyle(EstiloCeldas(workbook));
                row.createCell(9).setCellStyle(EstiloCeldas(workbook));
                row.createCell(10).setCellStyle(EstiloCeldas(workbook));
                row.createCell(11).setCellStyle(EstiloCeldas(workbook));
                row.createCell(12).setCellStyle(EstiloCeldas(workbook));
                row.createCell(13).setCellStyle(EstiloCeldas(workbook));
                row.createCell(14).setCellStyle(EstiloCeldas(workbook));
                row.createCell(15).setCellStyle(EstiloCeldas(workbook));
                
           
            }//agrega los valores al archivo de excel

             String statusAnt;
                    statusAnt = samples.get(i).getStatus();
                   
                    samples.get(i).setStatus("En espera para secuenciación");
                    ejbFacade.edit(samples.get(i));
                    
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                   
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En espera para secuenciación" + "- por " + us.getUserName());
                    commentsSample.setIdType(samples.get(i).getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);

                    commentFac.createComment(commentsSample);
            }             
                           String[] info = {"No. Folio: ","Realizó:","Equipos utilizados:","lote de reactivos"};
                        
                           for(int t=0;t<info.length;t++){
                               
                           row= pagina.createRow(t+2);
                           
                            for(int x=0;x<1;x++){
                                
                                XSSFCell cell=row.createCell(2);
                                cell.setCellValue(info[t]);
                            }
                        }
                       
                      
          
          
         //////////////////////////////////////////////////// 
                FileOutputStream fileOut = new FileOutputStream(file);

                workbook.write(fileOut);//
                fileOut.flush();
                fileOut.close();// Cerramos el libro para concluir operaciones
                
                 String nombre=file.getName();
                 String pathDownload = FilePath + "reportesNextera/" + nombre;
                 System.out.println("nombre del archivo: "+nombre);
                  context.getExternalContext().redirect(pathDownload);
                
            LOGGER.log(Level.INFO, "Archivo creado existosamente en {0}", file.getAbsolutePath());
             }

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de entrada/salida");
        }
    }
    
    public void createExcelTruseq() throws ClassNotFoundException, SQLException{
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Sample> samples=(List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");
        
       
         try {
             if(samples.isEmpty()){
                 System.out.println("no han seleccionado ninguna muestra");

                    context.addMessage(null, new FacesMessage("¡ERROR!",  "No han seleccionado ninguna muestra para Generar el reporte Nextera" ) );
             }else{
                 
        Timestamp time = new Timestamp(System.currentTimeMillis());
        
                    
         String[] titulos = {"","Usuario",""};//nombre de las columnas
         String[] subtitulos = {"ID de la \nmuestra","ID del"+"\n"+"proyecto","Etiqueta del"+"\n"+"tubo"};
         String[] encabezado = {"Fragmentación"};
         String[] tags={"Tags"};
         String[] tamanio={"Selección de tamaño"};
         String[] validacion={"validación de Biblioteca",""};
         String[] subencabezado = {"Tamaño","Archivo del gel de agarosa\n1.5%","Index","Tamaño","Archivo gel\n1.5%","[ng/µL]","[nM]","Tamaño","Archivo\nBioanalizador","Fecha de\ninicio de\nconstrucción","Fecha de\nTermino de\nconstrucción","Corrida \n secuencia"};
         
         XSSFWorkbook workbook = new XSSFWorkbook();
         XSSFSheet pagina = sizeCell(workbook,"TruSeq PCR DNA");
       
         String Nombre=us.getFirstName().substring(0,1);
         String apellido_p=us.getPLastName().substring(0,1);
         String apellido_m=us.getMLastName().substring(0,1);
         String iniciales=Nombre+apellido_p+apellido_m;
         System.out.println(iniciales);
         
         String nombreFichero = PathCreateReports + "reportesTruseq"+separatorPath+"F01_PT01_" + pj.getIdProject().replaceAll("[:.-]", "_") + "_" + time.toString().replace(" ", "_").replaceAll("[:.-]", "_") + "_" + iniciales + ".xlsx";//directorio donde se crea el archivo
         File file = new File(nombreFichero);
         
  
         CellStyle styleTopRigth=workbook.createCellStyle();
         CellStyle styleCloseLeft=workbook.createCellStyle();
         CellStyle estiloEncabezado=workbook.createCellStyle();
         CellStyle estilo=workbook.createCellStyle();
         
         XSSFFont font = workbook.createFont();
         
         font.setBold(true);
         
      
         
         estiloEncabezado.setBorderTop(BorderStyle.THICK);
         estiloEncabezado.setBorderBottom(BorderStyle.THICK);
         estiloEncabezado.setFont(font);
         estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
       
      
         styleTopRigth.setBorderTop(BorderStyle.THICK);
         styleTopRigth.setBorderRight(BorderStyle.THICK);
         
         styleCloseLeft.setBorderTop(BorderStyle.THICK);
         styleCloseLeft.setBorderLeft(BorderStyle.THICK);
         styleCloseLeft.setBorderBottom(BorderStyle.THICK);
         styleCloseLeft.setAlignment(HorizontalAlignment.CENTER);
         styleCloseLeft.setFont(font);
         
         estilo.setAlignment(HorizontalAlignment.CENTER);
         estilo.setFont(font);
         
         XSSFRow row2= pagina.createRow(1);
         XSSFCell titulo=row2.createCell(3);
         titulo.setCellStyle(estilo);
         titulo.setCellValue("Construcción de Bibliotecas TruSeq PCR DNA");
         version(workbook,"TruSeq PCR DNA");
         
         
         XSSFRow row= pagina.createRow(9);//FSB221019 crea una fila en la pocision 10 del archivo
            ///creamos el titulo de la celda 1 a la 3 del archivo con el nombre Usuario
            
          for(int t=0;t<titulos.length;t++){
              XSSFCell cell=row.createCell(t+1);
           
            cell.setCellStyle(colorCell(workbook));
            cell.setCellValue(titulos[t]);
            row.createCell(0).setCellStyle(EstiloCeldas(workbook));//cambiar
            
            //creamos el titulo de la celda 4 a la 10 del archivo con el nombre Analisis de calidad
            for(int x=0;x<encabezado.length;x++){
                 row.createCell(5).setCellStyle(styleTopRigth);
              XSSFCell cell2=row.createCell(x+4);
              cell2.setCellStyle(estiloEncabezado);
              cell2.setCellValue(encabezado[x]);
              
            }
            for (int s = 0; s < tags.length; s++) {
              //row.createCell(6).setCellStyle(styleCloseLeft);
              row.createCell(7).setCellStyle(styleCloseLeft);
              XSSFCell cel=row.createCell(s+6);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(tags[s]);
              
            }
            for (int c = 0; c < tamanio.length; c++) {
              row.createCell(9).setCellStyle(styleCloseLeft);
              
              XSSFCell cel=row.createCell(c+8);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(tamanio[c]);
            }
             for (int d = 0; d < validacion.length; d++) {
              //row.createCell(11).setCellStyle(styleCloseLeft);
              //row.createCell(12).setCellStyle(EstiloLibreriaEnc(workbook));
              XSSFCell cel=row.createCell(d+10);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(validacion[d]);
              row.createCell(12).setCellStyle(styleTopRigth);
            }
          }
          
          row= pagina.createRow(10);//se crea una fila en la posicion 11 del archivo
              //creamos los nombres de las columnas B,C,D como: ID de la muesta,ID del proyecto,Nombre de la muestra
          for (int i = 0; i < subtitulos.length; i++) {
              
              XSSFCell cell=row.createCell(i+1);
              cell.setCellStyle(EstiloSubtitulos(workbook));
              row.setHeight((short)1000);
              cell.setCellValue(subtitulos[i]);
              //creamos los nombres de las columnas de la E a la K.
          }
           
          for (int s = 0; s < subencabezado.length; s++) {
              XSSFCell cel=row.createCell(s+4);
              cel.setCellStyle(EstiloCerrado(workbook));
              cel.setCellValue(subencabezado[s]);
            }
          
           
          for (int i = 0; i < samples.size(); i++) {
            
              row= pagina.createRow(i+11);
            
             String lib=samples.get(i).getIdTube();
             int id=samples.get(i).getIdSample();
             String proj=pj.getIdProject();
             ////////////////////////////////
            for(int x=0;x<1;x++){
                
                 System.out.println("valor de lib:"+lib);
                
                 XSSFCell cell=row.createCell(3);
                 XSSFCell id_muestra=row.createCell(1);
                 XSSFCell project=row.createCell(2);
                
                
                id_muestra.setCellStyle(EstiloCeldas(workbook));
                id_muestra.setCellValue(id);
                
                project.setCellStyle(EstiloCeldas(workbook));
                project.setCellValue(proj);
                
                cell.setCellStyle(EstiloCeldas(workbook));
                cell.setCellValue(lib);//col 3
                
                for(int cl=4;cl<16;cl++){
                    row.createCell(cl).setCellStyle(EstiloCeldas(workbook));
                }//crea la celdas a partir de la columna E en la fila 12
            
            }//agrega los Datos de ID de la muestra,ID del proyecto,Etiqueta del tubo al archivo de excel, de la fila B a la D
             String statusAnt;
                    statusAnt = samples.get(i).getStatus();
                   
                    samples.get(i).setStatus("En espera para secuenciación");
                    ejbFacade.edit(samples.get(i));
                    
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                   
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En espera para secuenciación" + "- por " + us.getUserName());
                    commentsSample.setIdType(samples.get(i).getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);

                    commentFac.createComment(commentsSample);
            
            }        
                           
                           String[] info = {"No. Folio: ","Realizó:","Equipos utilizados:","lote de reactivos"};
                        
                           for(int t=0;t<info.length;t++){
                           row= pagina.createRow(t+2);
                            for(int x=0;x<1;x++){
                                XSSFCell cell=row.createCell(2);
                                cell.setCellValue(info[t]);
                            }//agrega los valores en la columna C fila 3,4,5 y6
                        }
                       
                      
         //////////////////////////////////////////////////// 
                FileOutputStream fileOut = new FileOutputStream(file);

                workbook.write(fileOut);//
                fileOut.flush();
                fileOut.close();// Cerramos el libro para concluir operaciones
                //st.executeUpdate("insert into folio values(default)");
                ;
               
                 String nombre=file.getName();
                 String pathDownload = FilePath + "reportesTruseq/" + nombre;
                 System.out.println("nombre del archivo: "+nombre);
                  context.getExternalContext().redirect(pathDownload);
                
            LOGGER.log(Level.INFO, "Archivo creado existosamente en {0}", file.getAbsolutePath());
             }

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de entrada/salida");
        }
    }
    
    public void createExcelMetaGenomica() throws ClassNotFoundException, SQLException{
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Sample> samples=(List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");
        
       
         try {
             if(samples.isEmpty()){
                 System.out.println("no han seleccionado ninguna muestra");

                    context.addMessage(null, new FacesMessage("¡ERROR!",  "No han seleccionado ninguna muestra para Generar el reporte 16S Metagenómica" ) );
             }else{

        Timestamp time = new Timestamp(System.currentTimeMillis());
        
                    
         String[] titulos = {"","Usuario",""};//nombre de las columnas
         String[] subtitulos = {"ID de la \nmuestra","ID del"+"\n"+"proyecto","Etiqueta del"+"\n"+"tubo"};
         String[] encabezado = {"Tags"};
         String[] validacion={"validación de Biblioteca",""};
         String[] subencabezado = {"N7","S5","[ng/µL]","[nM]","Tamaño","Archivo\nBioanalizador o de\n gel de agarosa 1.5%","Fecha de\ninicio de\nconstrucción","Fecha de\nTermino de\nconstrucción","Corrida \n secuencia"};
         
         XSSFWorkbook workbook = new XSSFWorkbook();
         XSSFSheet pagina = sizeCell(workbook,"16S Meta");
         
         String Nombre=us.getFirstName().substring(0,1);
         String apellido_p=us.getPLastName().substring(0,1);
         String apellido_m=us.getMLastName().substring(0,1);
         String iniciales=Nombre+apellido_p+apellido_m;
         System.out.println(iniciales);
         
         String nombreFichero =PathCreateReports + "reportesMeta"+separatorPath+"F01_PT01_" + pj.getIdProject().replaceAll("[:.-]", "_") + "_" + time.toString().replace(" ", "_").replaceAll("[:.-]", "_") + "_" + iniciales + ".xlsx";//directorio donde se crea el archivo
         File file = new File(nombreFichero);
         
       
         
         CellStyle styleTopRigth=workbook.createCellStyle();
         CellStyle styleCloseLeft=workbook.createCellStyle();
         CellStyle estiloEncabezado=workbook.createCellStyle();
         CellStyle estilo=workbook.createCellStyle();
         
         XSSFFont font = workbook.createFont();
         
         font.setBold(true);
         
        
         
         estiloEncabezado.setBorderTop(BorderStyle.THICK);
         estiloEncabezado.setBorderBottom(BorderStyle.THICK);
         estiloEncabezado.setFont(font);
         estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
         
         styleTopRigth.setBorderTop(BorderStyle.THICK);
         styleTopRigth.setBorderRight(BorderStyle.THICK);
         
         styleCloseLeft.setBorderTop(BorderStyle.THICK);
         styleCloseLeft.setBorderLeft(BorderStyle.THICK);
         styleCloseLeft.setBorderBottom(BorderStyle.THICK);
         styleCloseLeft.setAlignment(HorizontalAlignment.CENTER);
         styleCloseLeft.setFont(font);
         
         estilo.setAlignment(HorizontalAlignment.CENTER);
         estilo.setFont(font);
        
         XSSFRow row2= pagina.createRow(1);
         XSSFCell titulo=row2.createCell(3);
         titulo.setCellStyle(estilo);
         titulo.setCellValue("Construcción de Bibliotecas 16S Metagenómica");
         version(workbook,"16S Meta");
         
         XSSFRow row= pagina.createRow(9);//FSB221019 crea una fila en la pocision 10 del archivo
            ///creamos el titulo de la celda 1 a la 3 del archivo con el nombre Usuario
            
          for(int t=0;t<titulos.length;t++){
              XSSFCell cell=row.createCell(t+1);
           
            cell.setCellStyle(colorCell(workbook));
            cell.setCellValue(titulos[t]);
            row.createCell(0).setCellStyle(EstiloCeldas(workbook));//cambiar
            
            //creamos el titulo de la celda 4 a la 10 del archivo con el nombre Analisis de calidad
            for(int x=0;x<encabezado.length;x++){
                 row.createCell(4).setCellStyle(estiloEncabezado);
              XSSFCell cell2=row.createCell(x+5);
              cell2.setCellStyle(estiloEncabezado);
              cell2.setCellValue(encabezado[x]);
              
            }
            
             for (int d = 0; d < validacion.length; d++) {
              row.createCell(6).setCellStyle(styleCloseLeft);
              //row.createCell(12).setCellStyle(styleTopBot);
              XSSFCell cel=row.createCell(d+7);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(validacion[d]);
              row.createCell(9).setCellStyle(styleTopRigth);//
            }
          }
          
          row= pagina.createRow(10);//se crea una fila en la posicion 11 del archivo
              //creamos los nombres de las columnas B,C,D como: ID de la muesta,ID del proyecto,Nombre de la muestra
          for (int i = 0; i < subtitulos.length; i++) {
              
              XSSFCell cell=row.createCell(i+1);
              cell.setCellStyle(EstiloSubtitulos(workbook));
              row.setHeight((short)1000);
              cell.setCellValue(subtitulos[i]);
              //creamos los nombres de las columnas de la E a la K.
          }
           
          for (int s = 0; s < subencabezado.length; s++) {
              XSSFCell cel=row.createCell(s+4);
              cel.setCellStyle(EstiloCerrado(workbook));
              cel.setCellValue(subencabezado[s]);
            }
          
           
          for (int i = 0; i < samples.size(); i++) {
            
              row= pagina.createRow(i+11);
            
             String lib=samples.get(i).getIdTube();
             int id=samples.get(i).getIdSample();
             String proj=pj.getIdProject();
             ////////////////////////////////
            for(int x=0;x<1;x++){
                
                 System.out.println("valor de lib:"+lib);
                
                 XSSFCell cell=row.createCell(3);
                 XSSFCell id_muestra=row.createCell(1);
                 XSSFCell project=row.createCell(2);
                
                
                id_muestra.setCellStyle(EstiloCeldas(workbook));
                id_muestra.setCellValue(id);
                
                project.setCellStyle(EstiloCeldas(workbook));
                project.setCellValue(proj);
                
                cell.setCellStyle(EstiloCeldas(workbook));
                cell.setCellValue(lib);//col 3
                
                for(int cl=4;cl<13;cl++){
                    row.createCell(cl).setCellStyle(EstiloCeldas(workbook));
                }//crea la celdas a partir de la columna E en la fila 12
            
            }//agrega los Datos de ID de la muestra,ID del proyecto,Etiqueta del tubo al archivo de excel, de la fila B a la D

             String statusAnt;
                    statusAnt = samples.get(i).getStatus();
                   
                    samples.get(i).setStatus("En espera para secuenciación");
                    ejbFacade.edit(samples.get(i));
                    
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                   
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En espera para secuenciación" + "- por " + us.getUserName());
                    commentsSample.setIdType(samples.get(i).getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);

                    commentFac.createComment(commentsSample);
            }    
                           String[] info = {"No. Folio: ","Realizó:","Equipos utilizados:","lote de reactivos"};
                        
                           for(int t=0;t<info.length;t++){
                           row= pagina.createRow(t+2);
                            for(int x=0;x<1;x++){
                                XSSFCell cell=row.createCell(2);
                                cell.setCellValue(info[t]);
                            }//agrega los valores en la columna C fila 3,4,5 y6
                        }
                       
                      
         //////////////////////////////////////////////////// 
                FileOutputStream fileOut = new FileOutputStream(file);

                workbook.write(fileOut);//
                fileOut.flush();
                fileOut.close();// Cerramos el libro para concluir operaciones
                //st.executeUpdate("insert into folio values(default)");
               
               
                 String nombre=file.getName();
                 String pathDownload = FilePath + "reportesMeta/" + nombre;
                 System.out.println("nombre del archivo: "+nombre);
                  context.getExternalContext().redirect(pathDownload);
                
            LOGGER.log(Level.INFO, "Archivo creado existosamente en {0}", file.getAbsolutePath());
             }

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de entrada/salida");
        }
    }
  
    public void createExcelmRNA() throws ClassNotFoundException, SQLException{
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Sample> samples=(List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");
       
         try {
             if(samples.isEmpty()){
                 System.out.println("no han seleccionado ninguna muestra");

                    context.addMessage(null, new FacesMessage("¡ERROR!",  "No han seleccionado ninguna muestra para Generar el reporte TruSeq mRNA" ) );
             }else{
            
        Timestamp time = new Timestamp(System.currentTimeMillis());
        
         String[] titulos = {"","Usuario",""};//nombre de las columnas
         String[] subtitulos = {"ID de la \nmuestra","ID del"+"\n"+"proyecto","Etiqueta del"+"\n"+"tubo"};
         String[] encabezado = {"Fragmentación"};
         String[] tags = {"Tags"};
         String[] validacion={"Validacion de bibliotecas",""};
         String[] seleccion={"Selección de bibliotecas"};
         String[] subencabezado = {"tiempo\n(min)","Index","Archivo gel","Tamaño\n promedio","[ng/µL]","[nM]","Tamaño","Archivo\nBioanalizador","Fecha de\ninicio de\nconstrucción","Fecha de\nTermino de\nconstrucción","Corrida \n secuencia"};
         
         XSSFWorkbook workbook = new XSSFWorkbook();
         XSSFSheet pagina = sizeCell(workbook,"TruSeq mRNA");
         
         String Nombre=us.getFirstName().substring(0,1);
         String apellido_p=us.getPLastName().substring(0,1);
         String apellido_m=us.getMLastName().substring(0,1);
         String iniciales=Nombre+apellido_p+apellido_m;
         System.out.println(iniciales);
         
         String nombreFichero = PathCreateReports + "reportesmRNA"+separatorPath+"F01_PT01_" + pj.getIdProject().replaceAll("[:.-]", "_") + "_" + time.toString().replace(" ", "_").replaceAll("[:.-]", "_") + "_" + iniciales + ".xlsx";//directorio donde se crea el archivo
         File file = new File(nombreFichero);
         
         
    
   
         CellStyle styleTopRigth=workbook.createCellStyle();
         CellStyle styleCloseLeft=workbook.createCellStyle();
         CellStyle estiloEncabezado=workbook.createCellStyle();
         CellStyle estilo=workbook.createCellStyle();
         
         XSSFFont font = workbook.createFont();
         
         font.setBold(true);
         
         
         estiloEncabezado.setBorderTop(BorderStyle.THICK);
         estiloEncabezado.setBorderBottom(BorderStyle.THICK);
         estiloEncabezado.setFont(font);
         estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
         
       
         styleTopRigth.setBorderTop(BorderStyle.THICK);
         styleTopRigth.setBorderRight(BorderStyle.THICK);
         
         styleCloseLeft.setBorderTop(BorderStyle.THICK);
         styleCloseLeft.setBorderLeft(BorderStyle.THICK);
         styleCloseLeft.setBorderBottom(BorderStyle.THICK);
         styleCloseLeft.setAlignment(HorizontalAlignment.CENTER);
         styleCloseLeft.setFont(font);
         
         estilo.setAlignment(HorizontalAlignment.CENTER);
         estilo.setFont(font);

         XSSFRow row2= pagina.createRow(1);
         XSSFCell titulo=row2.createCell(3);
         titulo.setCellStyle(estilo);
         titulo.setCellValue("Construcción de Bibliotecas TruSeq mRNA");
         version(workbook,"TruSeq mRNA");
         XSSFRow row= pagina.createRow(9);//FSB221019 crea una fila en la pocision 10 del archivo
            ///creamos el titulo de la celda 1 a la 3 del archivo con el nombre Usuario
            
          for(int t=0;t<titulos.length;t++){
              XSSFCell cell=row.createCell(t+1);
           
            cell.setCellStyle(colorCell(workbook));
            cell.setCellValue(titulos[t]);
            row.createCell(0).setCellStyle(EstiloCeldas(workbook));//cambiar
            
            //creamos el titulo de la celda 4 a la 10 del archivo con el nombre Analisis de calidad
            for(int x=0;x<encabezado.length;x++){
             
              XSSFCell cell2=row.createCell(x+4);
              cell2.setCellStyle(EstiloCerrado(workbook));
              cell2.setCellValue(encabezado[x]);
              
            }
            for (int s = 0; s < tags.length; s++) {
              //row.createCell(5).setCellStyle(styleCloseLeft);
              //row.createCell(7).setCellStyle(styleTopRigth);
              XSSFCell cel=row.createCell(s+5);
              cel.setCellStyle(EstiloCerrado(workbook));
              cel.setCellValue(tags[s]);
              
            }
            
             for (int d = 0; d < seleccion.length; d++) {
              row.createCell(6).setCellStyle(styleCloseLeft);
              //row.createCell(12).setCellStyle(styleTopBot);
              XSSFCell cel=row.createCell(d+7);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(seleccion[d]);
              row.createCell(8).setCellStyle(styleCloseLeft);//
              //row.createCell(12).setCellStyle(EstiloLibreriaEnc(workbook));
              //row.createCell(13).setCellStyle(styleTopRigth);
              
            }
             for (int d = 0; d < validacion.length; d++) {
              //row.createCell(6).setCellStyle(styleCloseLeft);
              //row.createCell(12).setCellStyle(styleTopBot);
              XSSFCell cel=row.createCell(d+9);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(validacion[d]);
              row.createCell(11).setCellStyle(styleTopRigth);//
            }
          }
          
          row= pagina.createRow(10);//se crea una fila en la posicion 11 del archivo
              //creamos los nombres de las columnas B,C,D como: ID de la muesta,ID del proyecto,Nombre de la muestra
          for (int i = 0; i < subtitulos.length; i++) {
              
              XSSFCell cell=row.createCell(i+1);
              cell.setCellStyle(EstiloSubtitulos(workbook));
              row.setHeight((short)1000);
              cell.setCellValue(subtitulos[i]);
              //creamos los nombres de las columnas de la E a la K.
          }
           
          for (int s = 0; s < subencabezado.length; s++) {
              XSSFCell cel=row.createCell(s+4);
              cel.setCellStyle(EstiloCerrado(workbook));
              cel.setCellValue(subencabezado[s]);
            }
          
           
          for (int i = 0; i < samples.size(); i++) {
            
              row= pagina.createRow(i+11);
            
             String lib=samples.get(i).getIdTube();
             int id=samples.get(i).getIdSample();
             String proj=pj.getIdProject();
             ////////////////////////////////
            for(int x=0;x<1;x++){
                
                 System.out.println("valor de lib:"+lib);
                
                 XSSFCell cell=row.createCell(3);
                 XSSFCell id_muestra=row.createCell(1);
                 XSSFCell project=row.createCell(2);
                
                
                id_muestra.setCellStyle(EstiloCeldas(workbook));
                id_muestra.setCellValue(id);
                
                project.setCellStyle(EstiloCeldas(workbook));
                project.setCellValue(proj);
                
                cell.setCellStyle(EstiloCeldas(workbook));
                cell.setCellValue(lib);//col 3
                
                for(int cl=4;cl<15;cl++){
                    row.createCell(cl).setCellStyle(EstiloCeldas(workbook));
                }//crea la celdas a partir de la columna E en la fila 12
            
            }//agrega los Datos de ID de la muestra,ID del proyecto,Etiqueta del tubo al archivo de excel, de la fila B a la D

             String statusAnt;
                    statusAnt = samples.get(i).getStatus();
                   
                    samples.get(i).setStatus("En espera para secuenciación");
                    ejbFacade.edit(samples.get(i));
                    
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                   
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En espera para secuenciación" + "- por " + us.getUserName());
                    commentsSample.setIdType(samples.get(i).getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);

                    commentFac.createComment(commentsSample);
            }     
                           String[] info = {"No. Folio: ","Realizó:","Equipos utilizados:","lote de reactivos"};
                        
                           for(int t=0;t<info.length;t++){
                           row= pagina.createRow(t+2);
                            for(int x=0;x<1;x++){
                                XSSFCell cell=row.createCell(2);
                                cell.setCellValue(info[t]);
                            }//agrega los valores en la columna C fila 3,4,5 y6
                        }
                       
                      
         //////////////////////////////////////////////////// 
                FileOutputStream fileOut = new FileOutputStream(file);

                workbook.write(fileOut);//
                fileOut.flush();
                fileOut.close();// Cerramos el libro para concluir operaciones
                //st.executeUpdate("insert into folio values(default)");
              
                 String nombre=file.getName();
                 String pathDownload = FilePath + "reportesmRNA/" + nombre;
                 System.out.println("Usuario: "+us.getFirstName()+" "+us.getPLastName()+"\n nombre del archivo: "+nombre);
                  context.getExternalContext().redirect(pathDownload);
                
            LOGGER.log(Level.INFO, "Archivo creado existosamente en {0}", file.getAbsolutePath());
             }

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de entrada/salida");
        }
    }
    
    public void createExcelsmallRNA() throws ClassNotFoundException, SQLException{
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Sample> samples=(List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");
       
         try {
             if(samples.isEmpty()){
                 System.out.println("no han seleccionado ninguna muestra");

                    context.addMessage(null, new FacesMessage("¡ERROR!",  "No han seleccionado ninguna muestra para Generar el reporte smallRNA" ) );
             }else{
            
        Timestamp time = new Timestamp(System.currentTimeMillis());
        
         String[] titulos = {"","Usuario",""};//nombre de las columnas
         String[] subtitulos = {"ID de la \nmuestra","ID del"+"\n"+"proyecto","Etiqueta del"+"\n"+"tubo"};
         String[] encabezado = {"Tag"};
         
         String[] validacion={"validación de Biblioteca",""};
         String[] subencabezado = {"RIP","[ng/µL]","[nM]","Tamaño","Archivo\nBioanalizador","Fecha de\ninicio de\nconstrucción","Fecha de\nTermino de\nconstrucción","Corrida \n secuencia"};
         
         XSSFWorkbook workbook = new XSSFWorkbook();
         XSSFSheet pagina = sizeCell(workbook,"smallRNA");
         
         String Nombre=us.getFirstName().substring(0,1);
         String apellido_p=us.getPLastName().substring(0,1);
         String apellido_m=us.getMLastName().substring(0,1);
         String iniciales=Nombre+apellido_p+apellido_m;
         System.out.println(iniciales);
         
         String nombreFichero = PathCreateReports + "reportessmallRNA"+separatorPath+"F01_PT01_" + pj.getIdProject().replaceAll("[:.-]", "_") + "_" + time.toString().replace(" ", "_").replaceAll("[:.-]", "_") + "_" + iniciales + ".xlsx";//directorio donde se crea el archivo
         File file = new File(nombreFichero);
         
       
         
         CellStyle styleTopRigth=workbook.createCellStyle();
         CellStyle styleCloseLeft=workbook.createCellStyle();
      
         CellStyle estiloEncabezado=workbook.createCellStyle();
         CellStyle estilo=workbook.createCellStyle();
         
         XSSFFont font = workbook.createFont();
         
         font.setBold(true);
         
         estiloEncabezado.setBorderTop(BorderStyle.THICK);
         estiloEncabezado.setBorderBottom(BorderStyle.THICK);
         estiloEncabezado.setFont(font);
         estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
 
    
         styleTopRigth.setBorderTop(BorderStyle.THICK);
         styleTopRigth.setBorderRight(BorderStyle.THICK);
         
         styleCloseLeft.setBorderTop(BorderStyle.THICK);
         styleCloseLeft.setBorderLeft(BorderStyle.THICK);
         styleCloseLeft.setBorderBottom(BorderStyle.THICK);
         styleCloseLeft.setAlignment(HorizontalAlignment.CENTER);
         styleCloseLeft.setFont(font);
         
         estilo.setAlignment(HorizontalAlignment.CENTER);
         estilo.setFont(font);
  
         XSSFRow row2= pagina.createRow(1);
         XSSFCell titulo=row2.createCell(3);
         titulo.setCellStyle(estilo);
         titulo.setCellValue("Construcción de Bibliotecas smallRNA");
         version(workbook,"smallRNA");
         XSSFRow row= pagina.createRow(9);//FSB221019 crea una fila en la pocision 10 del archivo
            ///creamos el titulo de la celda 1 a la 3 del archivo con el nombre Usuario
            
          for(int t=0;t<titulos.length;t++){
              XSSFCell cell=row.createCell(t+1);
           
            cell.setCellStyle(colorCell(workbook));
            cell.setCellValue(titulos[t]);
            row.createCell(0).setCellStyle(EstiloCeldas(workbook));//cambiar
            
            //creamos el titulo de la celda 4 a la 10 del archivo con el nombre Analisis de calidad
            for(int x=0;x<encabezado.length;x++){
             
              XSSFCell cell2=row.createCell(x+4);
              cell2.setCellStyle(estiloEncabezado);
              cell2.setCellValue(encabezado[x]);
              
            }
            
            
             for (int d = 0; d < validacion.length; d++) {
              row.createCell(5).setCellStyle(styleCloseLeft);
             
              XSSFCell cel=row.createCell(d+6);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(validacion[d]);
              row.createCell(8).setCellStyle(styleTopRigth);//
            }
          }
          
          row= pagina.createRow(10);//se crea una fila en la posicion 11 del archivo
              //creamos los nombres de las columnas B,C,D como: ID de la muesta,ID del proyecto,Nombre de la muestra
          for (int i = 0; i < subtitulos.length; i++) {
              
              XSSFCell cell=row.createCell(i+1);
              cell.setCellStyle(EstiloSubtitulos(workbook));
              row.setHeight((short)1000);
              cell.setCellValue(subtitulos[i]);
              //creamos los nombres de las columnas de la E a la K.
          }
           
          for (int s = 0; s < subencabezado.length; s++) {
              XSSFCell cel=row.createCell(s+4);
              cel.setCellStyle(EstiloCerrado(workbook));
              cel.setCellValue(subencabezado[s]);
            }
          
           
          for (int i = 0; i < samples.size(); i++) {
            
              row= pagina.createRow(i+11);
            
             String lib=samples.get(i).getIdTube();
             int id=samples.get(i).getIdSample();
             String proj=pj.getIdProject();
             ////////////////////////////////
            for(int x=0;x<1;x++){
                
                 System.out.println("valor de lib:"+lib);
                
                 XSSFCell cell=row.createCell(3);
                 XSSFCell id_muestra=row.createCell(1);
                 XSSFCell project=row.createCell(2);
                
                
                id_muestra.setCellStyle(EstiloCeldas(workbook));
                id_muestra.setCellValue(id);
                
                project.setCellStyle(EstiloCeldas(workbook));
                project.setCellValue(proj);
                
                cell.setCellStyle(EstiloCeldas(workbook));
                cell.setCellValue(lib);//col 3
                
                for(int cl=4;cl<12;cl++){
                    row.createCell(cl).setCellStyle(EstiloCeldas(workbook));
                }//crea la celdas a partir de la columna E en la fila 12
            
            }//agrega los Datos de ID de la muestra,ID del proyecto,Etiqueta del tubo al archivo de excel, de la fila B a la D
             String statusAnt;
                    statusAnt = samples.get(i).getStatus();
                   
                    samples.get(i).setStatus("En espera para secuenciación");
                    ejbFacade.edit(samples.get(i));
                    
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                   
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En espera para secuenciación" + "- por " + us.getUserName());
                    commentsSample.setIdType(samples.get(i).getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);

                    commentFac.createComment(commentsSample);
            
            }     
                           String[] info = {"No. Folio: ","Realizó:","Equipos utilizados:","lote de reactivos"};
                        
                           for(int t=0;t<info.length;t++){
                           row= pagina.createRow(t+2);
                            for(int x=0;x<1;x++){
                                XSSFCell cell=row.createCell(2);
                                cell.setCellValue(info[t]);
                            }//agrega los valores en la columna C fila 3,4,5 y6
                        }
                       
                      
         //////////////////////////////////////////////////// 
                FileOutputStream fileOut = new FileOutputStream(file);

                workbook.write(fileOut);//
                fileOut.flush();
                fileOut.close();// Cerramos el libro para concluir operaciones
                //st.executeUpdate("insert into folio values(default)");
              
                 String nombre=file.getName();
                 String pathDownload = FilePath + "reportessmallRNA/" + nombre;
                 System.out.println("Usuario: "+us.getFirstName()+" "+us.getPLastName()+"\n nombre del archivo: "+nombre);
                  context.getExternalContext().redirect(pathDownload);
                
            LOGGER.log(Level.INFO, "Archivo creado existosamente en {0}", file.getAbsolutePath());
             }

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de entrada/salida");
        }
    }
        
        
        
        
    public void createExcelItsMetaGenomica() throws ClassNotFoundException, SQLException{
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        Users us = (Users) context.getExternalContext().getSessionMap().get("usuario");
        List<Sample> samples=(List<Sample>) context.getExternalContext().getSessionMap().get("listSamples");
        
       
         try {
             if(samples.isEmpty()){
                 System.out.println("no han seleccionado ninguna muestra");

                    context.addMessage(null, new FacesMessage("¡ERROR!",  "No han seleccionado ninguna muestra para Generar el reporte ITS Metagenómica" ) );
             }else{

        Timestamp time = new Timestamp(System.currentTimeMillis());
        
                    
         String[] titulos = {"","Usuario",""};//nombre de las columnas
         String[] subtitulos = {"ID de la \nmuestra","ID del"+"\n"+"proyecto","Etiqueta del"+"\n"+"tubo"};
         String[] encabezado = {"Tags"};
         String[] validacion={"validación de Biblioteca",""};
         String[] subencabezado = {"N7","S5","[ng/µL]","[nM]","Tamaño","Archivo\nBioanalizador o de\n gel de agarosa 1.5%","Fecha de\ninicio de\nconstrucción","Fecha de\nTermino de\nconstrucción","Corrida \n secuencia"};
         
         XSSFWorkbook workbook = new XSSFWorkbook();
         XSSFSheet pagina = sizeCell(workbook,"ITS Meta");
         
         String Nombre=us.getFirstName().substring(0,1);
         String apellido_p=us.getPLastName().substring(0,1);
         String apellido_m=us.getMLastName().substring(0,1);
         String iniciales=Nombre+apellido_p+apellido_m;
         System.out.println(iniciales);
         
         String nombreFichero = PathCreateReports + "reportesITSMeta"+separatorPath+"F01_PT01_" + pj.getIdProject().replaceAll("[:.-]", "_") + "_" + time.toString().replace(" ", "_").replaceAll("[:.-]", "_") + "_" + iniciales + ".xlsx";//directorio donde se crea el archivo
         File file = new File(nombreFichero);
         
       
         
         CellStyle styleTopRigth=workbook.createCellStyle();
         CellStyle styleCloseLeft=workbook.createCellStyle();
         CellStyle estiloEncabezado=workbook.createCellStyle();
         CellStyle estilo=workbook.createCellStyle();
         
         XSSFFont font = workbook.createFont();
         
         font.setBold(true);
         
        
         
         estiloEncabezado.setBorderTop(BorderStyle.THICK);
         estiloEncabezado.setBorderBottom(BorderStyle.THICK);
         estiloEncabezado.setFont(font);
         estiloEncabezado.setAlignment(HorizontalAlignment.CENTER);
         
         styleTopRigth.setBorderTop(BorderStyle.THICK);
         styleTopRigth.setBorderRight(BorderStyle.THICK);
         
         styleCloseLeft.setBorderTop(BorderStyle.THICK);
         styleCloseLeft.setBorderLeft(BorderStyle.THICK);
         styleCloseLeft.setBorderBottom(BorderStyle.THICK);
         styleCloseLeft.setAlignment(HorizontalAlignment.CENTER);
         styleCloseLeft.setFont(font);
         
         estilo.setAlignment(HorizontalAlignment.CENTER);
         estilo.setFont(font);
        
         XSSFRow row2= pagina.createRow(1);
         XSSFCell titulo=row2.createCell(3);
         titulo.setCellStyle(estilo);
         titulo.setCellValue("Construcción de Bibliotecas ITS Metagenómica");
         version(workbook,"ITS Meta");
         
         XSSFRow row= pagina.createRow(9);//FSB221019 crea una fila en la pocision 10 del archivo
            ///creamos el titulo de la celda 1 a la 3 del archivo con el nombre Usuario
            
          for(int t=0;t<titulos.length;t++){
              XSSFCell cell=row.createCell(t+1);
           
            cell.setCellStyle(colorCell(workbook));
            cell.setCellValue(titulos[t]);
            row.createCell(0).setCellStyle(EstiloCeldas(workbook));//cambiar
            
            //creamos el titulo de la celda 4 a la 10 del archivo con el nombre Analisis de calidad
            for(int x=0;x<encabezado.length;x++){
                 row.createCell(4).setCellStyle(estiloEncabezado);
              XSSFCell cell2=row.createCell(x+5);
              cell2.setCellStyle(estiloEncabezado);
              cell2.setCellValue(encabezado[x]);
              
            }
            
             for (int d = 0; d < validacion.length; d++) {
              row.createCell(6).setCellStyle(styleCloseLeft);
              //row.createCell(12).setCellStyle(styleTopBot);
              XSSFCell cel=row.createCell(d+7);
              cel.setCellStyle(EstiloLibreriaEnc(workbook));
              cel.setCellValue(validacion[d]);
              row.createCell(9).setCellStyle(styleTopRigth);//
            }
          }
          
          row= pagina.createRow(10);//se crea una fila en la posicion 11 del archivo
              //creamos los nombres de las columnas B,C,D como: ID de la muesta,ID del proyecto,Nombre de la muestra
          for (int i = 0; i < subtitulos.length; i++) {
              
              XSSFCell cell=row.createCell(i+1);
              cell.setCellStyle(EstiloSubtitulos(workbook));
              row.setHeight((short)1000);
              cell.setCellValue(subtitulos[i]);
              //creamos los nombres de las columnas de la E a la K.
          }
           
          for (int s = 0; s < subencabezado.length; s++) {
              XSSFCell cel=row.createCell(s+4);
              cel.setCellStyle(EstiloCerrado(workbook));
              cel.setCellValue(subencabezado[s]);
            }
          
           
          for (int i = 0; i < samples.size(); i++) {
            
              row= pagina.createRow(i+11);
            
             String lib=samples.get(i).getIdTube();
             int id=samples.get(i).getIdSample();
             String proj=pj.getIdProject();
             ////////////////////////////////
            for(int x=0;x<1;x++){
                
                 System.out.println("valor de lib:"+lib);
                
                 XSSFCell cell=row.createCell(3);
                 XSSFCell id_muestra=row.createCell(1);
                 XSSFCell project=row.createCell(2);
                
                
                id_muestra.setCellStyle(EstiloCeldas(workbook));
                id_muestra.setCellValue(id);
                
                project.setCellStyle(EstiloCeldas(workbook));
                project.setCellValue(proj);
                
                cell.setCellStyle(EstiloCeldas(workbook));
                cell.setCellValue(lib);//col 3
                
                for(int cl=4;cl<13;cl++){
                    row.createCell(cl).setCellStyle(EstiloCeldas(workbook));
                }//crea la celdas a partir de la columna E en la fila 12
            
            }//agrega los Datos de ID de la muestra,ID del proyecto,Etiqueta del tubo al archivo de excel, de la fila B a la D
             String statusAnt;
                    statusAnt = samples.get(i).getStatus();
                   
                    samples.get(i).setStatus("En espera para secuenciación");
                    ejbFacade.edit(samples.get(i));
                    
                    Comments commentsSample = new Comments();
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                   
                    commentsSample.setComment("Estatus cambia de -" + statusAnt + "- a -" + "En espera para secuenciación" + "- por " + us.getUserName());
                    commentsSample.setIdType(samples.get(i).getIdSample() + "");
                    commentsSample.setType("Sample");
                    commentsSample.setUserName("SISBI");
                    commentsSample.setCommentDate(timestamp);

                    commentFac.createComment(commentsSample);
             
            }    
                           String[] info = {"No. Folio: ","Realizó:","Equipos utilizados:","lote de reactivos"};
                        
                           for(int t=0;t<info.length;t++){
                           row= pagina.createRow(t+2);
                            for(int x=0;x<1;x++){
                                XSSFCell cell=row.createCell(2);
                                cell.setCellValue(info[t]);
                            }//agrega los valores en la columna C fila 3,4,5 y6
                        }
                       
                      
         //////////////////////////////////////////////////// 
                FileOutputStream fileOut = new FileOutputStream(file);

                workbook.write(fileOut);//
                fileOut.flush();
                fileOut.close();// Cerramos el libro para concluir operaciones
                //st.executeUpdate("insert into folio values(default)");
               
               
                 String nombre=file.getName();
                 String pathDownload = FilePath + "reportesITSMeta/" + nombre;
                 System.out.println("nombre del archivo: "+nombre);
                  context.getExternalContext().redirect(pathDownload);
                
            LOGGER.log(Level.INFO, "Archivo creado existosamente en {0}", file.getAbsolutePath());
             }

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Archivo no localizable en sistema de archivos");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de entrada/salida");
        }
    }
}
