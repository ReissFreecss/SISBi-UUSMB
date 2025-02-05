/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

/**
 *
 * @author Omar
 */
public class PathFiles {
       
    /*
    
    //RUTAS ZAZIL PRODUCCION     ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    public static String separatorPath = "/";
    
    //Rutas Xochi
    
    
    //Ruta para archivos FileController   ***************************************************************
    public static String PathFileSample = "/var/www/html/sampleFiles";
    
    
    //Ruta para archivos SampleController   ***************************************************************
    public static String PathFileSampleReports  = "/var/www/html/sampleFiles/reporteMuestras/";
    public static String LinkFileSampleReports = "http://www.uusmb.unam.mx/sampleFiles/reporteMuestras/";
    
    
    //Ruta para archivos FilesProjectController   ***************************************************************
    
    //xochi path
    public static String PathFilesProject = "/var/www/html/projectFiles/";
    public static String LinkFilesProject = "http://www.uusmb.unam.mx/projectFiles/";
    
    //Ruta para archivos LibraryController   ***************************************************************
    
    //ruta xoxhi
    //String pathSampleFiles = "/var/www/html/sampleFiles/";
    public static String DirectorySampleFiles = "/var/www/html/sampleFiles/SampleSheetFiles/";
    //xochi path
    public static String LinkLibrarySampleFiles = "http://www.uusmb.unam.mx/sampleFiles/SampleSheetFiles/";
    
    //Ruta para archivos CreateExcelController   ***************************************************************
    
    //xochi linux
    
    public static String LinkCreateExcel = "http://www.uusmb.unam.mx/sampleFiles/reporteMuestras/";
    //ruta xochi
    public static String DirectoryCreateExcel = "/var/www/html/sampleFiles/reporteMuestras/";
    
    //Ruta para archivos ReportProjectController y UserRoleReportController   ***************************************************************
    
    //IMAGENES DE FIRMAS DE USUARIOS
    //RUTA PARA CARGAR IMAGEN DE FIRMA "/var/www/html/reportProjects/reportDocuments/";
    
    public static String DirectoryImageSing = "/var/www/html/reportProjects/imgSign/";
    public static String DirectoryTemplateReport = "/var/www/html/reportProjects/templatesReports/";
    public static String DirectoryreportDocuments = "/var/www/html/reportProjects/reportDocuments/";
    public static String DirectoryDownloadWords = "/var/www/html/reportProjects/downloadWords/";
    
    public static String LinkDirectoryImageSing = "http://www.uusmb.unam.mx/reportProjects/imgSign/";
    
    */

    //Constantes:
    //public static String Xochi_IP="132.248.32.95:37703";
    //cambiar a produccion leslie 12/02/2024
    public static String Xochi_IP="www.uusmb.unam.mx";
    // Separador de rutas (Ahora windows acepta el slash derecho , entonces se vaa a usar ese siempre)
    public static String separatorPath = "/";

    // Raíces:
    //Si se hece una arquitectura donde se cambian las raìces según el entorno, y lo demàs se calcula
    // a partir de las raíces, se podría simplificar el código y evitar errores
    //Rutas de red  
    //public static String dev_host="local.uusmb.unam.mx";//localhost?
    //leslie 12/02/2024
    //public static String dev_host="www.uusmb.unam.mx";//dev_host solo se ocupa 1 vez y es debe ser lo mismo que Xochi_IP produccion
    public static String dev_host= Xochi_IP;//dev_host solo se ocupa 1 vez y es debe ser lo mismo que Xochi_IP

    //leslie 12/02/2024 estas no podria comentarlas... revisar si se usan en otra clase 
//public static String dev_host=Xochi_IP;
    public static String protocol="http";
   // public static String dev_port="8000";
    //public static String root_path="/SISBI";
   // public static String Xochi_port = "37703";
    // Entorno Windows Lucio
    //public static String html_folder="C:/Users/lucio/PycharmProjects/sisbi__/var_www_html";
    // Entorno Linux Lucio
    public static final String html_folder = "/var/www/html";
    
    //hosts comentables y descomentables según el entorno
    //static String host = dev_host+":"+dev_port;
    //static String host = Xochi_IP+":"+Xochi_port; //producción
        
    //Ligas a los reportes
    public static String LinkProjectReportDocuments = "http://"+dev_host+"/reportProjects/reportDocuments/";
    
    //RUTAS XOCHI TEST     ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    

    //Rutas Xochi
    
    //Ruta para archivos FileController   ***************************************************************
    public static String PathFileSample = html_folder+"/sampleFiles";
    
    //Ruta para archivos SampleController   ***************************************************************
    public static String PathFileSampleReports  = html_folder+"/sampleFiles/reporteMuestras/";
    public static String LinkFileSampleReports = "http://"+Xochi_IP+"/sampleFiles/reporteMuestras/";
    
    
    //Ruta para archivos FilesProjectController   ***************************************************************
    
    //xochi path
    public static String PathFilesProject = html_folder+"/projectFiles/";
    public static String LinkFilesProject = "http://"+Xochi_IP+"/projectFiles/";
    
    //Ruta para archivos LibraryController   ***************************************************************
    
    //ruta xoxhi
    //String pathSampleFiles = "/var/www/html/sampleFiles/";
    public static String DirectorySampleFiles = html_folder+"/sampleFiles/SampleSheetFiles/";
    //xochi path
    public static String LinkLibrarySampleFiles = "http://"+Xochi_IP+"/sampleFiles/SampleSheetFiles/";
    
    //Ruta para archivos CreateExcelController   ***************************************************************
    
    //xochi linux
    
    public static String LinkCreateExcel = "http://"+Xochi_IP+"/sampleFiles/reporteMuestras/";
    //ruta xochi
    public static String DirectoryCreateExcel = html_folder+"/sampleFiles/reporteMuestras/";
    
    //Ruta para archivos ReportProjectController y UserRoleReportController   ***************************************************************
    
    //IMAGENES DE FIRMAS DE USUARIOS
    //RUTA PARA CARGAR IMAGEN DE FIRMA "/var/www/html/reportProjects/reportDocuments/";
    
    public static String DirectoryImageSing = html_folder+"/reportProjects/imgSign/";
    public static String DirectoryTemplateReport = html_folder+"/reportProjects/templatesReports/";//O en qué ruta normalmente guardan SISBI?
    public static String DirectoryreportDocuments = html_folder+"/reportProjects/reportDocuments/";
    public static String DirectoryDownloadWords = html_folder+"/reportProjects/downloadWords/";
    /* Aquí hay que dar una limpieza, o una aclaración en los nombres de variable. Por ejemplo,
    la carpeta se llama DirectoryTemplateReport es donde se guardan algunos reportes, no (sólo)
    de donde vienen las plantillas. También, por ejemplo, hay una copia de las plantillas en la
    subcarpeta web/WEB-INF/borrar. Por el nombre parecería que esa versión de los archivos está
    en preparación para que se eliminen, pero no sé. Por lo pronto, yo, Lucio, estoy guardando
    las plantillas de los reportes en ~/SISBI/doc/PlantillasReportes, pero hay que acrodar dónde
    van a ir, por que la carpeta doc parece que es más adecuada para documentación para humanos que
    para datos para ser leìdos por la computadora.
    */
        
    public static String LinkDirectoryImageSing = "http://"+Xochi_IP+"/reportProjects/imgSign/";
    
    
    
    
    
    //RUTAS LESLIE TEST     ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    /*
    
    public static String separatorPath = "/";
    
    //Rutas Xochi
    
    //Ruta para archivos FileController   ***************************************************************
    public static String PathFileSample = html_folder+"/sampleFiles";
    
    //Ruta para archivos SampleController   ***************************************************************
    public static String PathFileSampleReports  = html_folder+"/sampleFiles/reporteMuestras/";
    public static String LinkFileSampleReports = "http://localhost/sampleFiles/reporteMuestras/";
    
    //Ruta para archivos FilesProjectController   ***************************************************************
    
    //xochi path
    public static String PathFilesProject = html_folder+"/projectFiles/";
    public static String LinkFilesProject = "http://localhost/projectFiles/";
    
    //Ruta para archivos LibraryController   ***************************************************************
    
    //ruta xoxhi
    //String pathSampleFiles = html_folder+"/sampleFiles/";
    public static String DirectorySampleFiles = "html_folder+/sampleFiles/SampleSheetFiles/";
    //xochi path
    public static String LinkLibrarySampleFiles = "http://localhost/sampleFiles/SampleSheetFiles/";
    
    //Ruta para archivos CreateExcelController   ***************************************************************
    
    //xochi linux
    
    public static String LinkCreateExcel = "http://localhost/sampleFiles/reporteMuestras/";
    //ruta xochi
    public static String DirectoryCreateExcel = html_folder+"/sampleFiles/reporteMuestras/";
    
    //Ruta para archivos ReportProjectController y UserRoleReportController   ***************************************************************
    
    //IMAGENES DE FIRMAS DE USUARIOS
    //RUTA PARA CARGAR IMAGEN DE FIRMA "/var/www/html/reportProjects/reportDocuments/";
    
    public static String DirectoryImageSing = html_folder+"/reportProjects/imgSign/";
    public static String DirectoryTemplateReport = html_folder+"/reportProjects/templatesReports/";
    public static String DirectoryreportDocuments = html_folder+"/reportProjects/reportDocuments/";
    public static String DirectoryDownloadWords = html_folder+"/reportProjects/downloadWords/";
    
    public static String LinkDirectoryImageSing = "http://localhost/reportProjects/imgSign/";
    
    
    
    //FIN
    
    */
    
    
    /*
    
    //RUTAS LINABAT SERVER    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    
    public static String separatorPath = "/";
    
    //Rutas LINABAT
    
    //Ruta para archivos FileController   ***************************************************************
    public static String PathFileSample = html_folder+"/sampleFiles";
    
    
    //Ruta para archivos SampleController   ***************************************************************
    public static String PathFileSampleReports  = html_folder+"/sampleFiles/reporteMuestras/";
    public static String LinkFileSampleReports = "https://sisbitest.xyz/sampleFiles/reporteMuestras/";
    
    
    //Ruta para archivos FilesProjectController   ***************************************************************
    
    //LINABAT path
    public static String PathFilesProject = html_folder+"/projectFiles/";
    public static String LinkFilesProject = "https://sisbitest.xyz/projectFiles/";
    
    //Ruta para archivos LibraryController   ***************************************************************
    
    //ruta LINABAT
    //String pathSampleFiles = "/var/www/html/sampleFiles/";
    public static String DirectorySampleFiles = html_folder+"/sampleFiles/SampleSheetFiles/";
    //xochi path
    public static String LinkLibrarySampleFiles = "https://sisbitest.xyz/sampleFiles/SampleSheetFiles/";
    
    //Ruta para archivos CreateExcelController   ***************************************************************
    
    //LINABAT linux
    
    public static String LinkCreateExcel = "https://sisbitest.xyz/sampleFiles/reporteMuestras/";
    //ruta LINABAT
    public static String DirectoryCreateExcel = html_folder+"sampleFiles/reporteMuestras/";
    
    //Ruta para archivos ReportProjectController y UserRoleReportController   ***************************************************************
    
    //IMAGENES DE FIRMAS DE USUARIOS
    //RUTA PARA CARGAR IMAGEN DE FIRMA html_folder+"/reportProjects/reportDocuments/";
    
    public static String DirectoryImageSing = html_folder+"/reportProjects/imgSign/";
    public static String DirectoryTemplateReport = html_folder"/reportProjects/templatesReports/";
    public static String DirectoryreportDocuments = html_folder+"/reportProjects/reportDocuments/";
    public static String DirectoryDownloadWords = html_folder+"/reportProjects/downloadWords/";
    
    public static String LinkDirectoryImageSing = "https://sisbitest.xyz/reportProjects/imgSign/";
    */
    
    
    
    
    /*
    //RUTAS WINDOWS MANTENIMIENTO     ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //windows
    public static String separatorPath = "\\";
    
    
    //Ruta para archivos FileController   ***************************************************************
    public static String PathFileSample = "C:\\Users\\Omar\\personal_domain\\docroot\\sisbi_test\\sampleFiles";
    
    
    //Ruta para archivos SampleController   ***************************************************************
    public static String PathFileSampleReports = "C:\\Users\\Omar\\personal_domain\\docroot\\sisbi_test\\sampleFiles\\reporteMuestras\\";
    public static String LinkFileSampleReports = "http://localhost:25346/sisbi_test/sampleFiles/reporteMuestras/";
    
    
    //Ruta para archivos FilesProjectController   ***************************************************************

    public static String PathFilesProject = "C:\\Users\\Omar\\personal_domain\\docroot\\sisbi_test\\projectFiles\\";
    public static String LinkFilesProject = "http://localhost:25346/sisbi_test/projectFiles/";
    
    
    //Ruta para archivos LibraryController   ***************************************************************

    public static String DirectorySampleFiles = "C:\\Users\\Omar\\personal_domain\\docroot\\sisbi_test\\sampleFiles\\SampleSheetFiles\\";
    public static String LinkLibrarySampleFiles = "http://localhost:25346/sisbi_test/sampleFiles/SampleSheetFiles/";
    
    
    //Ruta para archivos CreateExcelController   ***************************************************************

    public static String DirectoryCreateExcel = "C:\\Users\\Omar\\personal_domain\\docroot\\sisbi_test\\sampleFiles\\reporteMuestras\\";
    public static String LinkCreateExcel = "http://localhost:25346/sisbi_test/sampleFiles/reporteMuestras/";
    
    
    //Ruta para archivos ReportProjectController y UserRoleReportController   ***************************************************************
    
    //IMAGENES DE FIRMAS DE USUARIOS
    //RUTA PARA CARGAR IMAGEN DE FIRMA
    public static String DirectoryImageSing = "C:\\Users\\Omar\\personal_domain\\docroot\\sisbi_test\\reportProjects\\imgSign\\";
    public static String DirectoryTemplateReport = "C:\\Users\\Omar\\personal_domain\\docroot\\sisbi_test\\reportProjects\\templatesReports\\";
    public static String DirectoryreportDocuments = "C:\\Users\\Omar\\personal_domain\\docroot\\sisbi_test\\reportProjects\\reportDocuments\\";
    public static String DirectoryDownloadWords = "C:\\Users\\Omar\\personal_domain\\docroot\\sisbi_test\\reportProjects\\downloadWords\\";
    
    public static String LinkDirectoryImageSing = "http://localhost:25346/sisbi_test/reportProjects/imgSign/";
    
    */
    
    
    
    
    //RUTAS GNU/Linux Mantenimiento    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    /*
    public static String separatorPath = "/";
    
    //Rutas LINABAT
    
    //Ruta para archivos FileController   ***************************************************************
    public static String PathFileSample = "/home/leslie/GlassFish_Server/glassfish/domains/domain1/docroot/SISBI/sampleFiles";
    
    
    //Ruta para archivos SampleController   ***************************************************************
    public static String PathFileSampleReports  = "/home/leslie/GlassFish_Server/glassfish/domains/domain1/docroot/SISBI/reporteMuestras/";
    public static String LinkFileSampleReports = "http://localhost:8080/SISBI/sampleFiles/reporteMuestras/";
    
    
    //Ruta para archivos FilesProjectController   ***************************************************************
    
    //LINABAT path
    public static String PathFilesProject = "/home/leslie/GlassFish_Server/glassfish/domains/domain1/docroot/SISBI/projectFiles/";
    public static String LinkFilesProject = "http://localhost:8080/SISBI/projectFiles/";
    
    //Ruta para archivos LibraryController   ***************************************************************
    
    //ruta LINABAT
    //String pathSampleFiles = html_folder+"/sampleFiles/";
    public static String DirectorySampleFiles = "/home/leslie/GlassFish_Server/glassfish/domains/domain1/docroot/SISBI/sampleFiles/SampleSheetFiles/";
    //xochi path
    public static String LinkLibrarySampleFiles = "http://localhost:8080/SISBI/sampleFiles/SampleSheetFiles/";
    
    //Ruta para archivos CreateExcelController   ***************************************************************
    
    //LINABAT linux
    
    public static String LinkCreateExcel = "http://localhost:8080/SISBI/sampleFiles/reporteMuestras/";
    //ruta LINABAT
    public static String DirectoryCreateExcel = "/home/leslie/GlassFish_Server/glassfish/domains/domain1/docroot/SISBI/reporteMuestras/";
    
    //Ruta para archivos ReportProjectController y UserRoleReportController   ***************************************************************
    
    //IMAGENES DE FIRMAS DE USUARIOS
    //RUTA PARA CARGAR IMAGEN DE FIRMA html_folder+"/reportProjects/reportDocuments/";
    
    public static String DirectoryImageSing = "/home/leslie/GlassFish_Server/glassfish/domains/domain1/docroot/SISBI/reportProjects/imgSign/";
    public static String DirectoryTemplateReport = "/home/leslie/GlassFish_Server/glassfish/domains/domain1/docroot/SISBI/reportProjects/templatesReports/";
    public static String DirectoryreportDocuments = "/home/leslie/GlassFish_Server/glassfish/domains/domain1/docroot/SISBI/reportProjects/reportDocuments/";
    public static String DirectoryDownloadWords = "/home/leslie/GlassFish_Server/glassfish/domains/domain1/docroot/SISBI/reportProjects/downloadWords/";
    
    public static String LinkDirectoryImageSing = "http://localhost:8080/SISBI/reportProjects/imgSign/";
    
    */
    

}