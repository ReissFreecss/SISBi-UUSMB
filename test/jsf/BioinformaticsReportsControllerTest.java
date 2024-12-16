/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import jpa.entities.Project;
import jpa.entities.Sample;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.BioinformaticsReports;
import jpa.entities.BioinformaticAnalysisSampleLink;
import jpa.entities.BioinformaticAnalysisSampleLinkPK;
import jpa.session.BioinformaticAnalysisSampleLinkFacade;
import jpa.session.BioinformaticsReportsFacade;
import jpa.session.ReportProjectFacade;
import jpa.session.ProjectFacade;
import jpa.session.AbstractFacade;
import jsf.util.PaginationHelper;
import jsf.BioinformaticAnalysisController;
import jsf.BioinformaticAnalysisSampleLinkController;
import jsf.ReportProjectController;
import jsf.ProjectController;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.primefaces.event.FileUploadEvent;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ejb.embeddable.EJBContainer;
import junit.framework.TestCase;
import javax.persistence.EntityManager;
//import static org.mockito.Mockito.*;


/**
 *
 * @author laloa
 */
public class BioinformaticsReportsControllerTest extends TestCase {
    List<Sample> samples;
    List<Sample> samplesList;
    boolean expResult;
    SampleController instance;
    boolean result;
    private Context  ctx;
    private EJBContainer ejbContainer;

    public BioinformaticsReportsControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @BeforeClass
    public void setUp() throws Exception {
        System.out.println("-------Comienzo del test-------");
        ctx = new InitialContext();
        //ReportProjectController rp = new ReportProjectController();
        //BioinformaticsReportsFacade commentService = new BioinformaticsReportsFacade();

        //Object entityManager = mock(EntityManager.class);
        //commentService (entityManager); // inject our stubbed entity manager
        ejbContainer = EJBContainer.createEJBContainer();
        System.out.println("Opening the container" );
        //ctx = ejbContainer.getContext();
    }

    @AfterClass
    public void tearDown() {
        //ejbContainer.close();
        System.out.println("Closing the container" );
        System.out.println("-------Fin del test-------");
        System.out.println("");
    }

    @Test
    public void testCheckListSamplesListEmpty() throws Exception {
        ctx = new InitialContext();
        //FacesContext ctx = FacesContext.getCurrentInstance();
        System.out.println("testCheckListSamplesListEmpty: Comprobamos que la lista de muestras NO este vacia");
        //BioInformaticAnalysisController bac = new jsf.BioInformaticAnalysisController();
        //ReportProjectController rp = new ReportProjectController();
        //rfac.findProjectById("Project_HMejia_2023_06_19_14_06_42");
        AdderImplRemote remote;
        remote=(AdderImplRemote) ctx.lookup("st1");
        System.out.println(remote.add(32,32));
        BioinformaticsReportsFacade ja;
        Object pu;
        pu = ctx.lookup("SISBIPU");
        ja = (BioinformaticsReportsFacade) ctx.lookup("SISBIPU");
        ProjectController pc = new ProjectController();
        List<BioinformaticsReports> bio;
        BioinformaticsReportsFacade bf;
        bf = new BioinformaticsReportsFacade();
        //bf = pc.getBioInfoFac();
        bio = bf.findAll();
        //ProjectFacade rfac = new pc.;
        expResult = false;
        samples = null;
        instance = new SampleController();
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertFalse(result);
        assertEquals(expResult, result);
    }

}
