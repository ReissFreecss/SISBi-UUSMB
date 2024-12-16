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
import javax.faces.model.DataModel;
import jpa.entities.Project;
import jpa.entities.Sample;
import jpa.session.BioinformaticAnalysisSampleLinkFacade;
import jsf.util.PaginationHelper;
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

/**
 *
 * @author laloa
 */
public class SampleControllerTest {
    List<Sample> samples;
    List<Sample> samplesList;
    boolean expResult;
    SampleController instance;
     boolean result;

    public SampleControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        System.out.println("-------Comienzo del test-------");
    }

    @After
    public void tearDown() {
        System.out.println("-------Fin del test-------");
        System.out.println("");
    }

    @Test
    public void testCheckListSamplesListEmpty() {
        System.out.println("testCheckListSamplesListEmpty: Comprobamos que la lista de muestras este vacia");
        expResult = false;
        samples = null;
        instance = new SampleController();
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertFalse(result);
        assertEquals(expResult, result);
    }

    @Test
    public void testCheckListSamplesListWithOneData() {
        System.out.println("testCheckListSamplesListWithOneData: Comprobamos que la lista tenga un registro\n"
                + "Con los campos de plataforma, tamaño de lectura y tipo de muestra");
        expResult = true;
        samples = new ArrayList<>();

        samples.add(new Sample(17390, "No", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", "2x75", "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", null, null, null, null));

        instance = new SampleController();
        // instance.setSampleTable(samplesList);
        //instance.sampleTable=samplesList;
        instance.setSampleTable_test(samples);
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertTrue(result);
        assertEquals(expResult, result);
    }
    
     @Test
    public void testCheckListSamplesListWithOneDataNull() {
        System.out.println("testCheckListSamplesListWithOneDataNull: Comprobamos que la lista tenga un registro\n"
                + "Sin los campos de plataforma, tamaño de lectura y tipo de muestra");
        expResult = false;
        samples = new ArrayList<>();

        samples.add(new Sample(17390, "No", null, "", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", "", "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null,null , null, "2.48", "0.05", null, null, null, null));
    
        instance = new SampleController();
        // instance.setSampleTable(samplesList);
        //instance.sampleTable=samplesList;
        instance.setSampleTable_test(samples);
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertFalse(result);
        assertEquals(expResult, result);
    }
    
     @Test
    public void testCheckListSamplesListWithData() {
        System.out.println("testCheckListSamplesListWithData: Agregamos a la lista mas de un registro\n"
                + "y comprobamos que todos los registros tengan la misma plataforma, el mismo tamaño de lectura y el "
                + "mismo tipo de muestra");
        expResult = true;
        samples = new ArrayList<>();

        samples.add(new Sample(17390, "No", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", "2x75", "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", null, null, null, null));
        samples.add(new Sample(5433, "Si", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un proyecto muy prometedor", "2x75", "MD004", "Hielo seco", "", null, null, ""
                + "BLagunas_2203416_MD007","Buena", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", 
                null, null, null, null));  

        instance = new SampleController();
        // instance.setSampleTable(samplesList);
        //instance.sampleTable=samplesList;
        instance.setSampleTable_test(samples);
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertTrue(result);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCheckListSamplesListWithDataDistinct() {
        System.out.println("ttestCheckListSamplesListWithDataDistinct: Agregamos a la lista mas de un registro\n"
                + "y comprobamos que todos los registros tengan distinta plataforma, distinto tamaño de lectura"
                + "y distinto tipo de muestra");
        expResult = false;
        samples = new ArrayList<>();

        samples.add(new Sample(17390, "No", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", "2x75", "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", null, null, null, null));
        samples.add(new Sample(5433, "Si", null, "NextSeq100", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un proyecto muy prometedor", "1x75", "MD004", "Hielo seco", "", null, null, ""
                + "BLagunas_2203416_MD007","Buena", "19.7", "Registrada", null, "DNA Genómico", null, "2.48", "0.05", 
                null, null, null, null));
       
        instance = new SampleController();
        // instance.setSampleTable(samplesList);
        //instance.sampleTable=samplesList;
        instance.setSampleTable_test(samples);
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertFalse(result);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCheckListSamplesListWithDataNullPlataform() {
        System.out.println("testCheckListSamplesListWithDataNullPlataform: Agregamos a la lista mas de un registro\n"
                + "y en uno de los registros dejamos como nulo el campo de plataforma");
        expResult = false;
        samples = new ArrayList<>();

        samples.add(new Sample(17390, "No", null, null, "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", "2x75", "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", null, null, null, null));
        samples.add(new Sample(5433, "Si", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un proyecto muy prometedor", "2x75", "MD004", "Hielo seco", "", null, null, ""
                + "BLagunas_2203416_MD007","Buena", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", 
                null, null, null, null));  

        instance = new SampleController();
        // instance.setSampleTable(samplesList);
        //instance.sampleTable=samplesList;
        instance.setSampleTable_test(samples);
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertFalse(result);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCheckListSamplesListWithDataNullSizeLec() {
        System.out.println("testCheckListSamplesListWithDataNullSizeLec: Agregamos a la lista mas de un registro\n"
                + "y en uno de los registros dejamos como nulo el campo de tamaño de lectura");
        expResult = false;
        samples = new ArrayList<>();

        samples.add(new Sample(17390, "No", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", null, "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", null, null, null, null));
        samples.add(new Sample(5433, "Si", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un proyecto muy prometedor", "2x75", "MD004", "Hielo seco", "", null, null, ""
                + "BLagunas_2203416_MD007","Buena", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", 
                null, null, null, null));  
        
        instance = new SampleController();
        // instance.setSampleTable(samplesList);
        //instance.sampleTable=samplesList;
        instance.setSampleTable_test(samples);
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertFalse(result);
        assertEquals(expResult, result);
    }
    
   @Test
    public void testCheckListSamplesListWithDataNullType() {
        System.out.println("testCheckListSamplesListWithDataNullType: Agregamos a la lista mas de un registro\n"
                + "y en uno de los registros dejamos como nulo el campo de tipo de muestra");
        expResult = false;
        samples = new ArrayList<>();

        samples.add(new Sample(17390, "No", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", "2x75", "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", null, null, null, null));
        samples.add(new Sample(5433, "Si", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un proyecto muy prometedor", "2x75", "MD004", "Hielo seco", "", null, null, ""
                + "BLagunas_2203416_MD007","Buena", "19.7", "Registrada", null, null, null, "2.48", "0.05", 
                null, null, null, null));  

        instance = new SampleController();
        instance.setSampleTable_test(samples);
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertFalse(result);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCheckListSamplesListWithDataRejected() {
        System.out.println("testCheckListSamplesListWithDataRejected: Agregamos a la lista mas de un registro\n"
                + "y en uno de los registros tendra de estatus Rechazada (Forsake)");
        expResult = false;
        samples = new ArrayList<>();

        samples.add(new Sample(17390, "No", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", "2x75", "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", null, null, null, null));
        samples.add(new Sample(5433, "Si", null, "NextSeq500", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un proyecto muy prometedor", "2x75", "MD004", "Hielo seco", "", null, null, ""
                + "BLagunas_2203416_MD007","Buena", "19.7", "Rechazada (Forsake)", null, "RNA", null, "2.48", "0.05", 
                null, null, null, null));     
        
        instance = new SampleController();
        instance.setSampleTable_test(samples);
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertFalse(result);
        assertEquals(expResult, result);
    }
    
    
    @Test
    public void testCheckListSamplesListWithDataNull() {
        System.out.println("testCheckListSamplesListWithDataNull: Agregamos a la lista mas de un registro\n"
                + "y comprobamos que la plataforma, el tamaño de lectura y el tipo de muestra esten vacios");
        expResult = false;
        samples = new ArrayList<>();

        samples.add(new Sample(17390, "No", null, "", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", "", "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", null, null, null, null));
        samples.add(new Sample(5433, "Si", null, "", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un proyecto muy prometedor", "", "MD004", "Hielo seco", "", null, null, ""
                + "BLagunas_2203416_MD007","Buena", "19.7", "Registrada", null, null, null, "2.48", "0.05", 
                null, null, null, null));
        
        instance = new SampleController();
        // instance.setSampleTable(samplesList);
        //instance.sampleTable=samplesList;
        instance.setSampleTable_test(samples);
        result = instance.checkListSamples(samples);
        assertNotNull(result);
        assertFalse(result);
        assertEquals(expResult, result);
    }
    
    
    
   /*  @Test
    public void testRedirectLibrary() {
        System.out.println("redirectLibrary");
        SampleController instance = new SampleController();
        samples = new ArrayList<>();
        samples.add(new Sample(17390, "No", null, "", "Sinorhizobium meliloti", "Sinorhizobium meliloti",
                null, "15", "Un buen proyecto", "", "MD001", "Hielo seco", "", null, null, "VChavez_220516_MD001",
                "Aún no determinada", "19.7", "Registrada", null, "RNA", null, "2.48", "0.05", null, null, null, null));
        instance.setSamplesRedirect(samples);
        instance.redirectLibrary();
        //fail("The test case is a prototype.");
    }*/

    /* 
    @Test
    public void testGetErrorSampleList() {
        System.out.println("getErrorSampleList");
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getErrorSampleList();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetErrorSampleList() {
        System.out.println("setErrorSampleList");
        List<Sample> errorSampleList = null;
        SampleController instance = new SampleController();
        instance.setErrorSampleList(errorSampleList);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetListSample() {
        System.out.println("getListSample");
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getListSample();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetListSample() {
        System.out.println("setListSample");
        List<Sample> listSample = null;
        SampleController instance = new SampleController();
        instance.setListSample(listSample);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRequireInput() {
        System.out.println("getRequireInput");
        SampleController instance = new SampleController();
        Boolean expResult = null;
        Boolean result = instance.getRequireInput();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRequireInput() {
        System.out.println("setRequireInput");
        Boolean requireInput = null;
        SampleController instance = new SampleController();
        instance.setRequireInput(requireInput);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAnalysisCost() {
        System.out.println("getAnalysisCost");
        SampleController instance = new SampleController();
        List<String> expResult = null;
        List<String> result = instance.getAnalysisCost();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAnalysisCost() {
        System.out.println("setAnalysisCost");
        List<String> analysisCost = null;
        SampleController instance = new SampleController();
        instance.setAnalysisCost(analysisCost);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsHaveBioAn() {
        System.out.println("isHaveBioAn");
        SampleController instance = new SampleController();
        boolean expResult = false;
        boolean result = instance.isHaveBioAn();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetHaveBioAn() {
        System.out.println("setHaveBioAn");
        boolean haveBioAn = false;
        SampleController instance = new SampleController();
        instance.setHaveBioAn(haveBioAn);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetTableDialog() {
        System.out.println("getTableDialog");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getTableDialog();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetTableDialog() {
        System.out.println("setTableDialog");
        String tableDialog = "";
        SampleController instance = new SampleController();
        instance.setTableDialog(tableDialog);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamplesDiferent() {
        System.out.println("getSamplesDiferent");
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getSamplesDiferent();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSamplesDiferent() {
        System.out.println("setSamplesDiferent");
        List<Sample> samplesDiferent = null;
        SampleController instance = new SampleController();
        instance.setSamplesDiferent(samplesDiferent);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetMessageDialog() {
        System.out.println("getMessageDialog");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getMessageDialog();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetMessageDialog() {
        System.out.println("setMessageDialog");
        String messageDialog = "";
        SampleController instance = new SampleController();
        instance.setMessageDialog(messageDialog);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetProfundity() {
        System.out.println("getProfundity");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getProfundity();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetProfundity() {
        System.out.println("setProfundity");
        String profundity = "";
        SampleController instance = new SampleController();
        instance.setProfundity(profundity);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPrevUrL() {
        System.out.println("getPrevUrL");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getPrevUrL();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetPrevUrL() {
        System.out.println("setPrevUrL");
        String prevUrL = "";
        SampleController instance = new SampleController();
        instance.setPrevUrL(prevUrL);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEdit() {
        System.out.println("getEdit");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getEdit();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEdit() {
        System.out.println("setEdit");
        String edit = "";
        SampleController instance = new SampleController();
        instance.setEdit(edit);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetBAFac() {
        System.out.println("getBAFac");
        SampleController instance = new SampleController();
        BioinformaticAnalysisSampleLinkFacade expResult = null;
        BioinformaticAnalysisSampleLinkFacade result = instance.getBAFac();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetBAFac() {
        System.out.println("setBAFac");
        BioinformaticAnalysisSampleLinkFacade BAFac = null;
        SampleController instance = new SampleController();
        instance.setBAFac(BAFac);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSeq() {
        System.out.println("getSeq");
        SampleController instance = new SampleController();
        Boolean expResult = null;
        Boolean result = instance.getSeq();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSeq() {
        System.out.println("setSeq");
        Boolean seq = null;
        SampleController instance = new SampleController();
        instance.setSeq(seq);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAbs260_280_usmb() {
        System.out.println("getAbs260_280_usmb");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getAbs260_280_usmb();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAbs260_280_usmb() {
        System.out.println("setAbs260_280_usmb");
        String Abs260_280_usmb = "";
        SampleController instance = new SampleController();
        instance.setAbs260_280_usmb(Abs260_280_usmb);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAbs260_230_usmb() {
        System.out.println("getAbs260_230_usmb");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getAbs260_230_usmb();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAbs260_230_usmb() {
        System.out.println("setAbs260_230_usmb");
        String Abs260_230_usmb = "";
        SampleController instance = new SampleController();
        instance.setAbs260_230_usmb(Abs260_230_usmb);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetFolio() {
        System.out.println("getFolio");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getFolio();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetFolio() {
        System.out.println("setFolio");
        String folio = "";
        SampleController instance = new SampleController();
        instance.setFolio(folio);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetUser() {
        System.out.println("getUser");
        SampleController instance = new SampleController();
        Boolean expResult = null;
        Boolean result = instance.getUser();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetUser() {
        System.out.println("setUser");
        Boolean user = null;
        SampleController instance = new SampleController();
        instance.setUser(user);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAbs260_280() {
        System.out.println("getAbs260_280");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getAbs260_280();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAbs260_280() {
        System.out.println("setAbs260_280");
        String Abs260_280 = "";
        SampleController instance = new SampleController();
        instance.setAbs260_280(Abs260_280);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAbs260_230() {
        System.out.println("getAbs260_230");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getAbs260_230();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAbs260_230() {
        System.out.println("setAbs260_230");
        String Abs260_230 = "";
        SampleController instance = new SampleController();
        instance.setAbs260_230(Abs260_230);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRequireInputUnidades() {
        System.out.println("getRequireInputUnidades");
        SampleController instance = new SampleController();
        Boolean expResult = null;
        Boolean result = instance.getRequireInputUnidades();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRequireInputUnidades() {
        System.out.println("setRequireInputUnidades");
        Boolean requireInputUnidades = null;
        SampleController instance = new SampleController();
        instance.setRequireInputUnidades(requireInputUnidades);
        fail("The test case is a prototype.");
    }

    @Test
    public void testInit() {
        System.out.println("init");
        SampleController instance = new SampleController();
        instance.init();
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsSendMail() {
        System.out.println("isSendMail");
        SampleController instance = new SampleController();
        boolean expResult = false;
        boolean result = instance.isSendMail();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSendMail() {
        System.out.println("setSendMail");
        boolean sendMail = false;
        SampleController instance = new SampleController();
        instance.setSendMail(sendMail);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamConcent() {
        System.out.println("getSamConcent");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getSamConcent();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSamConcent() {
        System.out.println("setSamConcent");
        String samConcent = "";
        SampleController instance = new SampleController();
        instance.setSamConcent(samConcent);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLabVolume() {
        System.out.println("getLabVolume");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getLabVolume();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetLabVolume() {
        System.out.println("setLabVolume");
        String labVolume = "";
        SampleController instance = new SampleController();
        instance.setLabVolume(labVolume);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLabConcent() {
        System.out.println("getLabConcent");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getLabConcent();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetLabConcent() {
        System.out.println("setLabConcent");
        String labConcent = "";
        SampleController instance = new SampleController();
        instance.setLabConcent(labConcent);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSampleList() {
        System.out.println("getSampleList");
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getSampleList();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSampleList() {
        System.out.println("setSampleList");
        List<Sample> sampleList = null;
        SampleController instance = new SampleController();
        instance.setSampleList(sampleList);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetComment() {
        System.out.println("getComment");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getComment();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetComment() {
        System.out.println("setComment");
        String comment = "";
        SampleController instance = new SampleController();
        instance.setComment(comment);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetUnidades() {
        System.out.println("getUnidades");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getUnidades();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAcept() {
        System.out.println("getAcept");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getAcept();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAcept() {
        System.out.println("setAcept");
        String acept = "";
        SampleController instance = new SampleController();
        instance.setAcept(acept);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetGenExtra() {
        System.out.println("getGenExtra");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getGenExtra();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetGenExtra() {
        System.out.println("setGenExtra");
        String genExtra = "";
        SampleController instance = new SampleController();
        instance.setGenExtra(genExtra);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetOrgName() {
        System.out.println("getOrgName");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getOrgName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetOrgName() {
        System.out.println("setOrgName");
        String orgName = "";
        SampleController instance = new SampleController();
        instance.setOrgName(orgName);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRefName() {
        System.out.println("getRefName");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getRefName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRefName() {
        System.out.println("setRefName");
        String refName = "";
        SampleController instance = new SampleController();
        instance.setRefName(refName);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetContName() {
        System.out.println("getContName");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getContName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetContName() {
        System.out.println("setContName");
        String contName = "";
        SampleController instance = new SampleController();
        instance.setContName(contName);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamVolume() {
        System.out.println("getSamVolume");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getSamVolume();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSamVolume() {
        System.out.println("setSamVolume");
        String samVolume = "";
        SampleController instance = new SampleController();
        instance.setSamVolume(samVolume);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetDes() {
        System.out.println("getDes");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getDes();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetDes() {
        System.out.println("setDes");
        String des = "";
        SampleController instance = new SampleController();
        instance.setDes(des);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetInSize() {
        System.out.println("getInSize");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getInSize();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetInSize() {
        System.out.println("setInSize");
        String inSize = "";
        SampleController instance = new SampleController();
        instance.setInSize(inSize);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetReadSize() {
        System.out.println("getReadSize");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getReadSize();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetReadSize() {
        System.out.println("setReadSize");
        String readSize = "";
        SampleController instance = new SampleController();
        instance.setReadSize(readSize);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetTube() {
        System.out.println("getTube");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getTube();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetTube() {
        System.out.println("setTube");
        String tube = "";
        SampleController instance = new SampleController();
        instance.setTube(tube);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetExpPerfo() {
        System.out.println("getExpPerfo");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getExpPerfo();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetExpPerfo() {
        System.out.println("setExpPerfo");
        String expPerfo = "";
        SampleController instance = new SampleController();
        instance.setExpPerfo(expPerfo);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRealPerfo() {
        System.out.println("getRealPerfo");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getRealPerfo();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRealPerfo() {
        System.out.println("setRealPerfo");
        String realPerfo = "";
        SampleController instance = new SampleController();
        instance.setRealPerfo(realPerfo);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamName() {
        System.out.println("getSamName");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getSamName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSamName() {
        System.out.println("setSamName");
        String samName = "";
        SampleController instance = new SampleController();
        instance.setSamName(samName);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamQuality() {
        System.out.println("getSamQuality");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getSamQuality();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSamQuality() {
        System.out.println("setSamQuality");
        String samQuality = "";
        SampleController instance = new SampleController();
        instance.setSamQuality(samQuality);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamConcen() {
        System.out.println("getSamConcen");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getSamConcen();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSamConcen() {
        System.out.println("setSamConcen");
        String samQuantity = "";
        SampleController instance = new SampleController();
        instance.setSamConcen(samQuantity);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetComments() {
        System.out.println("getComments");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getComments();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetComments() {
        System.out.println("setComments");
        String comments = "";
        SampleController instance = new SampleController();
        instance.setComments(comments);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetDelDate() {
        System.out.println("getDelDate");
        SampleController instance = new SampleController();
        Date expResult = null;
        Date result = instance.getDelDate();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetDelDate() {
        System.out.println("setDelDate");
        Date delDate = null;
        SampleController instance = new SampleController();
        instance.setDelDate(delDate);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetType() {
        System.out.println("getType");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getType();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetType() {
        System.out.println("setType");
        String type = "";
        SampleController instance = new SampleController();
        instance.setType(type);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRecepDate() {
        System.out.println("getRecepDate");
        SampleController instance = new SampleController();
        Date expResult = null;
        Date result = instance.getRecepDate();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRecepDate() {
        System.out.println("setRecepDate");
        Date recepDate = null;
        SampleController instance = new SampleController();
        instance.setRecepDate(recepDate);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetUnidades() {
        System.out.println("setUnidades");
        String unidades = "";
        SampleController instance = new SampleController();
        instance.setUnidades(unidades);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPlataform() {
        System.out.println("getPlataform");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getPlataform();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetPlataform() {
        System.out.println("setPlataform");
        String plataform = "";
        SampleController instance = new SampleController();
        instance.setPlataform(plataform);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetDelivery() {
        System.out.println("getDelivery");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getDelivery();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetDelivery() {
        System.out.println("setDelivery");
        String delivery = "";
        SampleController instance = new SampleController();
        instance.setDelivery(delivery);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSelected() {
        System.out.println("getSelected");
        SampleController instance = new SampleController();
        Sample expResult = null;
        Sample result = instance.getSelected();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSampleTable() {
        System.out.println("getSampleTable");
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getSampleTable();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSampleTable() {
        System.out.println("setSampleTable");
        List<Sample> sampleTable = null;
        SampleController instance = new SampleController();
        instance.setSampleTable(sampleTable);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamStat() {
        System.out.println("getSamStat");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getSamStat();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSamStat() {
        System.out.println("setSamStat");
        String samStat = "";
        SampleController instance = new SampleController();
        instance.setSamStat(samStat);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSelectedSample() {
        System.out.println("getSelectedSample");
        SampleController instance = new SampleController();
        Sample expResult = null;
        Sample result = instance.getSelectedSample();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSelectedSample() {
        System.out.println("setSelectedSample");
        Sample selectedSample = null;
        SampleController instance = new SampleController();
        instance.setSelectedSample(selectedSample);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPagination() {
        System.out.println("getPagination");
        SampleController instance = new SampleController();
        PaginationHelper expResult = null;
        PaginationHelper result = instance.getPagination();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetExpPerfoOxford() {
        System.out.println("getExpPerfoOxford");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getExpPerfoOxford();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetExpPerfoOxford() {
        System.out.println("setExpPerfoOxford");
        String expPerfoOxford = "";
        SampleController instance = new SampleController();
        instance.setExpPerfoOxford(expPerfoOxford);
        fail("The test case is a prototype.");
    }

    @Test
    public void testPrepareList() {
        System.out.println("prepareList");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.prepareList();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testPrepareView() {
        System.out.println("prepareView");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.prepareView();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testPrepareCreate() {
        System.out.println("prepareCreate");
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.prepareCreate();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckUser() {
        System.out.println("checkUser");
        SampleController instance = new SampleController();
        boolean expResult = false;
        boolean result = instance.checkUser();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testRedirectProject() {
        System.out.println("redirectProject");
        SampleController instance = new SampleController();
        instance.redirectProject();
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckListSamples() {
        System.out.println("checkListSamples");
        List<Sample> samples = null;
        SampleController instance = new SampleController();
        boolean expResult = false;
        boolean result = instance.checkListSamples(samples);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testRedirectLibrary() {
        System.out.println("redirectLibrary");
        SampleController instance = new SampleController();
        instance.redirectLibrary();
        fail("The test case is a prototype.");
    }

    @Test
    public void testColorCell() {
        System.out.println("colorCell");
        XSSFWorkbook workbook = null;
        SampleController instance = new SampleController();
        CellStyle expResult = null;
        CellStyle result = instance.colorCell(workbook);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testEstiloSubtitulos() {
        System.out.println("EstiloSubtitulos");
        XSSFWorkbook workbook = null;
        SampleController instance = new SampleController();
        CellStyle expResult = null;
        CellStyle result = instance.EstiloSubtitulos(workbook);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testEstiloEncabezado() {
        System.out.println("EstiloEncabezado");
        XSSFWorkbook workbook = null;
        SampleController instance = new SampleController();
        CellStyle expResult = null;
        CellStyle result = instance.EstiloEncabezado(workbook);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testEstiloSubEncabezado() {
        System.out.println("EstiloSubEncabezado");
        XSSFWorkbook workbook = null;
        SampleController instance = new SampleController();
        CellStyle expResult = null;
        CellStyle result = instance.EstiloSubEncabezado(workbook);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testEstiloCeldas() {
        System.out.println("EstiloCeldas");
        XSSFWorkbook workbook = null;
        SampleController instance = new SampleController();
        CellStyle expResult = null;
        CellStyle result = instance.EstiloCeldas(workbook);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testEstiloLibreriaEnc() {
        System.out.println("EstiloLibreriaEnc");
        XSSFWorkbook workbook = null;
        SampleController instance = new SampleController();
        CellStyle expResult = null;
        CellStyle result = instance.EstiloLibreriaEnc(workbook);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testEstiloCerrado() {
        System.out.println("EstiloCerrado");
        XSSFWorkbook workbook = null;
        SampleController instance = new SampleController();
        CellStyle expResult = null;
        CellStyle result = instance.EstiloCerrado(workbook);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSizeCell() {
        System.out.println("sizeCell");
        XSSFWorkbook book = null;
        String SheetName = "";
        SampleController instance = new SampleController();
        XSSFSheet expResult = null;
        XSSFSheet result = instance.sizeCell(book, SheetName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testVersion() {
        System.out.println("version");
        XSSFWorkbook book = null;
        String SheetName = "";
        SampleController instance = new SampleController();
        XSSFSheet expResult = null;
        XSSFSheet result = instance.version(book, SheetName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCreateExcel() {
        System.out.println("createExcel");
        SampleController instance = new SampleController();
        instance.createExcel();
        fail("The test case is a prototype.");
    }

    @Test
    public void testCreateExcelRNA() throws Exception {
        System.out.println("createExcelRNA");
        SampleController instance = new SampleController();
        instance.createExcelRNA();
        fail("The test case is a prototype.");
    }

    @Test
    public void testReports() throws Exception {
        System.out.println("Reports");
        FileUploadEvent event = null;
        SampleController instance = new SampleController();
        instance.Reports(event);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCreate() {
        System.out.println("create");
        SampleController instance = new SampleController();
        instance.create();
        fail("The test case is a prototype.");
    }

    @Test
    public void testPrepareEdit() {
        System.out.println("prepareEdit");
        SampleController instance = new SampleController();
        instance.prepareEdit();
        fail("The test case is a prototype.");
    }

    @Test
    public void testClearVars() {
        System.out.println("clearVars");
        SampleController instance = new SampleController();
        instance.clearVars();
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateManySamples() {
        System.out.println("updateManySamples");
        SampleController instance = new SampleController();
        instance.updateManySamples();
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateManySamplesView() {
        System.out.println("updateManySamplesView");
        SampleController instance = new SampleController();
        instance.updateManySamplesView();
        fail("The test case is a prototype.");
    }

    @Test
    public void testAreMoreThanOne() {
        System.out.println("areMoreThanOne");
        SampleController instance = new SampleController();
        boolean expResult = false;
        boolean result = instance.areMoreThanOne();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        SampleController instance = new SampleController();
        instance.update();
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateSample() {
        System.out.println("updateSample");
        SampleController instance = new SampleController();
        instance.updateSample();
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateAllSample() {
        System.out.println("updateAllSample");
        SampleController instance = new SampleController();
        instance.updateAllSample();
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNumberSamplesProj() {
        System.out.println("getNumberSamplesProj");
        Project proj = null;
        SampleController instance = new SampleController();
        int expResult = 0;
        int result = instance.getNumberSamplesProj(proj);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNumberSamplesBioAn() {
        System.out.println("getNumberSamplesBioAn");
        Project proj = null;
        SampleController instance = new SampleController();
        int expResult = 0;
        int result = instance.getNumberSamplesBioAn(proj);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNumberSamplesBioAnCon() {
        System.out.println("getNumberSamplesBioAnCon");
        Project proj = null;
        SampleController instance = new SampleController();
        String expResult = "";
        String result = instance.getNumberSamplesBioAnCon(proj);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamplesProj() {
        System.out.println("getSamplesProj");
        Project proj = null;
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getSamplesProj(proj);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAnalysisbySample() {
        System.out.println("getAnalysisbySample");
        Sample id = null;
        SampleController instance = new SampleController();
        List<String> expResult = null;
        List<String> result = instance.getAnalysisbySample(id);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateView() {
        System.out.println("updateView");
        SampleController instance = new SampleController();
        instance.updateView();
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateViewList() {
        System.out.println("updateViewList");
        SampleController instance = new SampleController();
        instance.updateViewList();
        fail("The test case is a prototype.");
    }

    @Test
    public void testCanEdit() {
        System.out.println("canEdit");
        SampleController instance = new SampleController();
        boolean expResult = false;
        boolean result = instance.canEdit();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetItems() {
        System.out.println("getItems");
        SampleController instance = new SampleController();
        DataModel expResult = null;
        DataModel result = instance.getItems();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetItemsProj() {
        System.out.println("getItemsProj");
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getItemsProj();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetItemsProjSam() {
        System.out.println("getItemsProjSam");
        Sample sm = null;
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getItemsProjSam(sm);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetItemsProjSelected() {
        System.out.println("getItemsProjSelected");
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getItemsProjSelected();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAllSamples() {
        System.out.println("getAllSamples");
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getAllSamples();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamples() {
        System.out.println("getSamples");
        SampleController instance = new SampleController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getSamples();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testUploadFileSam() {
        System.out.println("uploadFileSam");
        FileUploadEvent event = null;
        SampleController instance = new SampleController();
        instance.uploadFileSam(event);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckEditSamples() {
        System.out.println("checkEditSamples");
        String[] listSamples = null;
        SampleController instance = new SampleController();
        boolean expResult = false;
        boolean result = instance.checkEditSamples(listSamples);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteSample() {
        System.out.println("DeleteSample");
        int idSample = 0;
        SampleController instance = new SampleController();
        instance.DeleteSample(idSample);
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateSampleByRange() {
        System.out.println("updateSampleByRange");
        SampleController instance = new SampleController();
        instance.updateSampleByRange();
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPrevURL() {
        System.out.println("getPrevURL");
        SampleController instance = new SampleController();
        instance.getPrevURL();
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetURLPreviousPage() throws Exception {
        System.out.println("getURLPreviousPage");
        SampleController instance = new SampleController();
        instance.getURLPreviousPage();
        fail("The test case is a prototype.");
    }

    @Test
    public void testContinueCreateUser() throws Exception {
        System.out.println("continueCreateUser");
        SampleController instance = new SampleController();
        instance.continueCreateUser();
        fail("The test case is a prototype.");
    }

    @Test
    public void testContinueCreateProject() throws Exception {
        System.out.println("continueCreateProject");
        SampleController instance = new SampleController();
        instance.continueCreateProject();
        fail("The test case is a prototype.");
    }

    @Test
    public void testResetSelectionSampleTable() {
        System.out.println("resetSelectionSampleTable");
        SampleController instance = new SampleController();
        instance.resetSelectionSampleTable();
        fail("The test case is a prototype.");
    }

    @Test
    public void testAsignarValorCampos() {
        System.out.println("asignarValorCampos");
        SampleController instance = new SampleController();
        instance.asignarValorCampos();
        fail("The test case is a prototype.");
    }

    @Test
    public void testCleanFormSample() {
        System.out.println("cleanFormSample");
        SampleController instance = new SampleController();
        instance.cleanFormSample();
        fail("The test case is a prototype.");
    }

    @Test
    public void testRedirectCreateSample() {
        System.out.println("redirectCreateSample");
        SampleController instance = new SampleController();
        instance.redirectCreateSample();
        fail("The test case is a prototype.");
    }

    @Test
    public void testAddRequireInput() {
        System.out.println("addRequireInput");
        SampleController instance = new SampleController();
        instance.addRequireInput();
        fail("The test case is a prototype.");
    }

    @Test
    public void testRequireUnidades() {
        System.out.println("requireUnidades");
        SampleController instance = new SampleController();
        instance.requireUnidades();
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateListSample() {
        System.out.println("updateListSample");
        SampleController instance = new SampleController();
        instance.updateListSample();
        fail("The test case is a prototype.");
    }

    @Test
    public void testCancelBioinformaticAnalysis() {
        System.out.println("cancelBioinformaticAnalysis");
        SampleController instance = new SampleController();
        instance.cancelBioinformaticAnalysis();
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsIllumina() {
        System.out.println("isIllumina");
        String selectPlataform = "";
        SampleController instance = new SampleController();
        boolean expResult = false;
        boolean result = instance.isIllumina(selectPlataform);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsOxford() {
        System.out.println("isOxford");
        String selectPlataform = "";
        SampleController instance = new SampleController();
        boolean expResult = false;
        boolean result = instance.isOxford(selectPlataform);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetAllPlataforms() {
        System.out.println("getAllPlataforms");
        SampleController instance = new SampleController();
        List<String> expResult = null;
        List<String> result = instance.getAllPlataforms();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }*/
}
