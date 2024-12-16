/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.util.Date;
import java.util.List;
import javax.faces.event.ActionEvent;
import jpa.entities.Barcodes;
import jpa.entities.Library;
import jpa.entities.Project;
import jpa.entities.QualityReports;
import jpa.entities.Run;
import jpa.entities.Sample;
import jpa.entities.Users;
import jpa.session.BioinformaticAnalysisSampleLinkFacade;
import jpa.session.CommentsFacade;
import jpa.session.FilesFacade;
import jpa.session.LibraryFacade;
import jpa.session.LibraryRunLinkFacade;
import jpa.session.ProjectFacade;
import jpa.session.RunFacade;
import jpa.session.SampleLibraryLinkFacade;
import jpa.session.UsersFacade;
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
public class LibraryControllerTest {
    
    public LibraryControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetEditLibrariesName() {
        System.out.println("getEditLibrariesName");
        LibraryController instance = new LibraryController();
        Boolean expResult = null;
        Boolean result = instance.getEditLibrariesName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEditLibrariesName() {
        System.out.println("setEditLibrariesName");
        Boolean EditLibrariesName = null;
        LibraryController instance = new LibraryController();
        instance.setEditLibrariesName(EditLibrariesName);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetDisabledBtn() {
        System.out.println("getDisabledBtn");
        LibraryController instance = new LibraryController();
        Boolean expResult = null;
        Boolean result = instance.getDisabledBtn();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetDisabledBtn() {
        System.out.println("setDisabledBtn");
        Boolean disabledBtn = null;
        LibraryController instance = new LibraryController();
        instance.setDisabledBtn(disabledBtn);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRunNameSelected() {
        System.out.println("getRunNameSelected");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getRunNameSelected();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRunNameSelected() {
        System.out.println("setRunNameSelected");
        String runNameSelected = "";
        LibraryController instance = new LibraryController();
        instance.setRunNameSelected(runNameSelected);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetListBarcodesi5() {
        System.out.println("setListBarcodesi5");
        List<Barcodes> listBarcodesi5 = null;
        LibraryController instance = new LibraryController();
        instance.setListBarcodesi5(listBarcodesi5);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetListBarcodesi7() {
        System.out.println("getListBarcodesi7");
        LibraryController instance = new LibraryController();
        List<Barcodes> expResult = null;
        List<Barcodes> result = instance.getListBarcodesi7();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetListBarcodesi5() {
        System.out.println("getListBarcodesi5");
        LibraryController instance = new LibraryController();
        List<Barcodes> expResult = null;
        List<Barcodes> result = instance.getListBarcodesi5();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetIndexBasei5Create() {
        System.out.println("getIndexBasei5Create");
        Barcodes itemBarcode = null;
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getIndexBasei5Create(itemBarcode);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetIndexBasei5View() {
        System.out.println("getIndexBasei5View");
        Barcodes itemBarcode = null;
        String itemPlataform = "";
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getIndexBasei5View(itemBarcode, itemPlataform);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetListBarcodesi7() {
        System.out.println("setListBarcodesi7");
        List<Barcodes> listBarcodesi7 = null;
        LibraryController instance = new LibraryController();
        instance.setListBarcodesi7(listBarcodesi7);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetListSampleLibraries() {
        System.out.println("getListSampleLibraries");
        LibraryController instance = new LibraryController();
        List<TemplateLibrary> expResult = null;
        List<TemplateLibrary> result = instance.getListSampleLibraries();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetListSampleLibraries() {
        System.out.println("setListSampleLibraries");
        List<TemplateLibrary> listSampleLibraries = null;
        LibraryController instance = new LibraryController();
        instance.setListSampleLibraries(listSampleLibraries);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSampleTable() {
        System.out.println("getSampleTable");
        LibraryController instance = new LibraryController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getSampleTable();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSampleTable() {
        System.out.println("setSampleTable");
        List<Sample> sampleTable = null;
        LibraryController instance = new LibraryController();
        instance.setSampleTable(sampleTable);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRowSampleExcel() {
        System.out.println("getRowSampleExcel");
        LibraryController instance = new LibraryController();
        List<String> expResult = null;
        List<String> result = instance.getRowSampleExcel();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRowSampleExcel() {
        System.out.println("setRowSampleExcel");
        List<String> rowSampleExcel = null;
        LibraryController instance = new LibraryController();
        instance.setRowSampleExcel(rowSampleExcel);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEjbFacadeRun() {
        System.out.println("getEjbFacadeRun");
        LibraryController instance = new LibraryController();
        RunFacade expResult = null;
        RunFacade result = instance.getEjbFacadeRun();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEjbFacadeRun() {
        System.out.println("setEjbFacadeRun");
        RunFacade ejbFacadeRun = null;
        LibraryController instance = new LibraryController();
        instance.setEjbFacadeRun(ejbFacadeRun);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetFileProjectPath() {
        System.out.println("getFileProjectPath");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getFileProjectPath();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetFileProjectPath() {
        System.out.println("setFileProjectPath");
        String fileProjectPath = "";
        LibraryController instance = new LibraryController();
        instance.setFileProjectPath(fileProjectPath);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetMessageDialog() {
        System.out.println("getMessageDialog");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getMessageDialog();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetMessageDialog() {
        System.out.println("setMessageDialog");
        String messageDialog = "";
        LibraryController instance = new LibraryController();
        instance.setMessageDialog(messageDialog);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetBAFac() {
        System.out.println("getBAFac");
        LibraryController instance = new LibraryController();
        BioinformaticAnalysisSampleLinkFacade expResult = null;
        BioinformaticAnalysisSampleLinkFacade result = instance.getBAFac();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetBAFac() {
        System.out.println("setBAFac");
        BioinformaticAnalysisSampleLinkFacade BAFac = null;
        LibraryController instance = new LibraryController();
        instance.setBAFac(BAFac);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEjbFiles() {
        System.out.println("getEjbFiles");
        LibraryController instance = new LibraryController();
        FilesFacade expResult = null;
        FilesFacade result = instance.getEjbFiles();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEjbFiles() {
        System.out.println("setEjbFiles");
        FilesFacade ejbFiles = null;
        LibraryController instance = new LibraryController();
        instance.setEjbFiles(ejbFiles);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEjbSLLF() {
        System.out.println("getEjbSLLF");
        LibraryController instance = new LibraryController();
        SampleLibraryLinkFacade expResult = null;
        SampleLibraryLinkFacade result = instance.getEjbSLLF();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEjbSLLF() {
        System.out.println("setEjbSLLF");
        SampleLibraryLinkFacade ejbSLLF = null;
        LibraryController instance = new LibraryController();
        instance.setEjbSLLF(ejbSLLF);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEjbProject() {
        System.out.println("getEjbProject");
        LibraryController instance = new LibraryController();
        ProjectFacade expResult = null;
        ProjectFacade result = instance.getEjbProject();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEjbProject() {
        System.out.println("setEjbProject");
        ProjectFacade ejbProject = null;
        LibraryController instance = new LibraryController();
        instance.setEjbProject(ejbProject);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetCommentFac() {
        System.out.println("getCommentFac");
        LibraryController instance = new LibraryController();
        CommentsFacade expResult = null;
        CommentsFacade result = instance.getCommentFac();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetCommentFac() {
        System.out.println("setCommentFac");
        CommentsFacade commentFac = null;
        LibraryController instance = new LibraryController();
        instance.setCommentFac(commentFac);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetUsersList() {
        System.out.println("getUsersList");
        LibraryController instance = new LibraryController();
        List<String> expResult = null;
        List<String> result = instance.getUsersList();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetUsersList() {
        System.out.println("setUsersList");
        List<String> usersList = null;
        LibraryController instance = new LibraryController();
        instance.setUsersList(usersList);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetUsuario() {
        System.out.println("getUsuario");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getUsuario();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetUsuario() {
        System.out.println("setUsuario");
        String usuario = "";
        LibraryController instance = new LibraryController();
        instance.setUsuario(usuario);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetProjs() {
        System.out.println("getProjs");
        LibraryController instance = new LibraryController();
        List<String> expResult = null;
        List<String> result = instance.getProjs();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetProjs() {
        System.out.println("setProjs");
        List<String> projs = null;
        LibraryController instance = new LibraryController();
        instance.setProjs(projs);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetProject() {
        System.out.println("getProject");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getProject();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetProject() {
        System.out.println("setProject");
        String project = "";
        LibraryController instance = new LibraryController();
        instance.setProject(project);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRange() {
        System.out.println("getRange");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getRange();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRange() {
        System.out.println("setRange");
        String range = "";
        LibraryController instance = new LibraryController();
        instance.setRange(range);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLibraryTable() {
        System.out.println("getLibraryTable");
        LibraryController instance = new LibraryController();
        List<Library> expResult = null;
        List<Library> result = instance.getLibraryTable();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetLibraryTable() {
        System.out.println("setLibraryTable");
        List<Library> LibraryTable = null;
        LibraryController instance = new LibraryController();
        instance.setLibraryTable(LibraryTable);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEjbUser() {
        System.out.println("getEjbUser");
        LibraryController instance = new LibraryController();
        UsersFacade expResult = null;
        UsersFacade result = instance.getEjbUser();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEjbUser() {
        System.out.println("setEjbUser");
        UsersFacade ejbUser = null;
        LibraryController instance = new LibraryController();
        instance.setEjbUser(ejbUser);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsCycles() {
        System.out.println("isCycles");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.isCycles();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetCycles() {
        System.out.println("setCycles");
        boolean cycles = false;
        LibraryController instance = new LibraryController();
        instance.setCycles(cycles);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEjbRun() {
        System.out.println("getEjbRun");
        LibraryController instance = new LibraryController();
        RunFacade expResult = null;
        RunFacade result = instance.getEjbRun();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEjbRun() {
        System.out.println("setEjbRun");
        RunFacade ejbRun = null;
        LibraryController instance = new LibraryController();
        instance.setEjbRun(ejbRun);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEjbLibraryRun() {
        System.out.println("getEjbLibraryRun");
        LibraryController instance = new LibraryController();
        LibraryRunLinkFacade expResult = null;
        LibraryRunLinkFacade result = instance.getEjbLibraryRun();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEjbLibraryRun() {
        System.out.println("setEjbLibraryRun");
        LibraryRunLinkFacade ejbLibraryRun = null;
        LibraryController instance = new LibraryController();
        instance.setEjbLibraryRun(ejbLibraryRun);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetInvestigator() {
        System.out.println("getInvestigator");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getInvestigator();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetInvestigator() {
        System.out.println("setInvestigator");
        String Investigator = "";
        LibraryController instance = new LibraryController();
        instance.setInvestigator(Investigator);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRunName_0args() {
        System.out.println("getRunName");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getRunName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRunName() {
        System.out.println("setRunName");
        LibraryController instance = new LibraryController();
        instance.setRunName();
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetDescription() {
        System.out.println("setDescription");
        String Description = "";
        LibraryController instance = new LibraryController();
        instance.setDescription(Description);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNm1() {
        System.out.println("getNm1");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getNm1();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetNm1() {
        System.out.println("setNm1");
        String nm1 = "";
        LibraryController instance = new LibraryController();
        instance.setNm1(nm1);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNm2() {
        System.out.println("getNm2");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getNm2();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetNm2() {
        System.out.println("setNm2");
        String nm2 = "";
        LibraryController instance = new LibraryController();
        instance.setNm2(nm2);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRun1() {
        System.out.println("getRun1");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getRun1();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRun1() {
        System.out.println("setRun1");
        String run1 = "";
        LibraryController instance = new LibraryController();
        instance.setRun1(run1);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRun2() {
        System.out.println("getRun2");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getRun2();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRun2() {
        System.out.println("setRun2");
        String run2 = "";
        LibraryController instance = new LibraryController();
        instance.setRun2(run2);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRunDate() {
        System.out.println("getRunDate");
        LibraryController instance = new LibraryController();
        Date expResult = null;
        Date result = instance.getRunDate();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRunDate() {
        System.out.println("setRunDate");
        Date runDate = null;
        LibraryController instance = new LibraryController();
        instance.setRunDate(runDate);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetReadType() {
        System.out.println("getReadType");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getReadType();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetReadType() {
        System.out.println("setReadType");
        String readType = "";
        LibraryController instance = new LibraryController();
        instance.setReadType(readType);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetKitPerformance() {
        System.out.println("getKitPerformance");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getKitPerformance();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetKitPerformance() {
        System.out.println("setKitPerformance");
        String kitPerformance = "";
        LibraryController instance = new LibraryController();
        instance.setKitPerformance(kitPerformance);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNameRun() {
        System.out.println("getNameRun");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getNameRun();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetNameRun() {
        System.out.println("setNameRun");
        String NameRun = "";
        LibraryController instance = new LibraryController();
        instance.setNameRun(NameRun);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetExperimentName() {
        System.out.println("getExperimentName");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getExperimentName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetExperimentName() {
        System.out.println("setExperimentName");
        String ExperimentName = "";
        LibraryController instance = new LibraryController();
        instance.setExperimentName(ExperimentName);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLibRun() {
        System.out.println("getLibRun");
        LibraryController instance = new LibraryController();
        List<Library> expResult = null;
        List<Library> result = instance.getLibRun();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetLibRun() {
        System.out.println("setLibRun");
        List<Library> libRun = null;
        LibraryController instance = new LibraryController();
        instance.setLibRun(libRun);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEdit() {
        System.out.println("getEdit");
        LibraryController instance = new LibraryController();
        List<Run> expResult = null;
        List<Run> result = instance.getEdit();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEdit() {
        System.out.println("setEdit");
        List<Run> edit = null;
        LibraryController instance = new LibraryController();
        instance.setEdit(edit);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetScode() {
        System.out.println("getScode");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getScode();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetScode() {
        System.out.println("setScode");
        String scode = "";
        LibraryController instance = new LibraryController();
        instance.setScode(scode);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetCorrida() {
        System.out.println("getCorrida");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getCorrida();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetCorrida() {
        System.out.println("setCorrida");
        String corrida = "";
        LibraryController instance = new LibraryController();
        instance.setCorrida(corrida);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetFlowcell() {
        System.out.println("getFlowcell");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getFlowcell();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetFlowcell() {
        System.out.println("setFlowcell");
        String flowcell = "";
        LibraryController instance = new LibraryController();
        instance.setFlowcell(flowcell);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetUser() {
        System.out.println("getUser");
        LibraryController instance = new LibraryController();
        Users expResult = null;
        Users result = instance.getUser();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetUser() {
        System.out.println("setUser");
        Users user = null;
        LibraryController instance = new LibraryController();
        instance.setUser(user);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSelectedLibraries() {
        System.out.println("getSelectedLibraries");
        LibraryController instance = new LibraryController();
        List<Library> expResult = null;
        List<Library> result = instance.getSelectedLibraries();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSelectedLibraries() {
        System.out.println("setSelectedLibraries");
        List<Library> selectedLibraries = null;
        LibraryController instance = new LibraryController();
        instance.setSelectedLibraries(selectedLibraries);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetIndex() {
        System.out.println("getIndex");
        LibraryController instance = new LibraryController();
        int expResult = 0;
        int result = instance.getIndex();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetIndex() {
        System.out.println("setIndex");
        int index = 0;
        LibraryController instance = new LibraryController();
        instance.setIndex(index);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsAmpRender() {
        System.out.println("isAmpRender");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.isAmpRender();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetAmpRender() {
        System.out.println("setAmpRender");
        boolean ampRender = false;
        LibraryController instance = new LibraryController();
        instance.setAmpRender(ampRender);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEjbFacade() {
        System.out.println("getEjbFacade");
        LibraryController instance = new LibraryController();
        LibraryFacade expResult = null;
        LibraryFacade result = instance.getEjbFacade();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetDnaRender() {
        System.out.println("setDnaRender");
        boolean dnaRender = false;
        LibraryController instance = new LibraryController();
        instance.setDnaRender(dnaRender);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsRnaRender() {
        System.out.println("isRnaRender");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.isRnaRender();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetRnaRender() {
        System.out.println("setRnaRender");
        boolean rnaRender = false;
        LibraryController instance = new LibraryController();
        instance.setRnaRender(rnaRender);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsDnaRender() {
        System.out.println("isDnaRender");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.isDnaRender();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsSmallRender() {
        System.out.println("isSmallRender");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.isSmallRender();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSmallRender() {
        System.out.println("setSmallRender");
        boolean smallRender = false;
        LibraryController instance = new LibraryController();
        instance.setSmallRender(smallRender);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamples() {
        System.out.println("getSamples");
        LibraryController instance = new LibraryController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getSamples();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSamples() {
        System.out.println("setSamples");
        List<Sample> samples = null;
        LibraryController instance = new LibraryController();
        instance.setSamples(samples);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamTypes() {
        System.out.println("getSamTypes");
        LibraryController instance = new LibraryController();
        List<String> expResult = null;
        List<String> result = instance.getSamTypes();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetSamTypes() {
        System.out.println("setSamTypes");
        List<String> samTypes = null;
        LibraryController instance = new LibraryController();
        instance.setSamTypes(samTypes);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNumLib() {
        System.out.println("getNumLib");
        LibraryController instance = new LibraryController();
        int expResult = 0;
        int result = instance.getNumLib();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetNumLib() {
        System.out.println("setNumLib");
        int numLib = 0;
        LibraryController instance = new LibraryController();
        instance.setNumLib(numLib);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLibraryType() {
        System.out.println("getLibraryType");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getLibraryType();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetLibraryType() {
        System.out.println("setLibraryType");
        String libraryType = "";
        LibraryController instance = new LibraryController();
        instance.setLibraryType(libraryType);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetKit() {
        System.out.println("getKit");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getKit();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetKit() {
        System.out.println("setKit");
        String kit = "";
        LibraryController instance = new LibraryController();
        instance.setKit(kit);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPlatafrom() {
        System.out.println("getPlatafrom");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getPlatafrom();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetPlatafrom() {
        System.out.println("setPlatafrom");
        String platafrom = "";
        LibraryController instance = new LibraryController();
        instance.setPlatafrom(platafrom);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSelected() {
        System.out.println("getSelected");
        LibraryController instance = new LibraryController();
        Library expResult = null;
        Library result = instance.getSelected();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetBibliotecas() {
        System.out.println("getBibliotecas");
        LibraryController instance = new LibraryController();
        List<Library> expResult = null;
        List<Library> result = instance.getBibliotecas();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetBibliotecas() {
        System.out.println("setBibliotecas");
        List<Library> bibliotecas = null;
        LibraryController instance = new LibraryController();
        instance.setBibliotecas(bibliotecas);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLibrariesByKit() {
        System.out.println("getLibrariesByKit");
        LibraryController instance = new LibraryController();
        List<Library> expResult = null;
        List<Library> result = instance.getLibrariesByKit();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetFechaPrep() {
        System.out.println("getFechaPrep");
        LibraryController instance = new LibraryController();
        Date expResult = null;
        Date result = instance.getFechaPrep();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetFechaPrep() {
        System.out.println("setFechaPrep");
        Date fechaPrep = null;
        LibraryController instance = new LibraryController();
        instance.setFechaPrep(fechaPrep);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetUsersSamples() {
        System.out.println("getUsersSamples");
        LibraryController instance = new LibraryController();
        List<Users> expResult = null;
        List<Users> result = instance.getUsersSamples();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetProjects() {
        System.out.println("getProjects");
        LibraryController instance = new LibraryController();
        List<String> expResult = null;
        List<String> result = instance.getProjects();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetProjectLibrary() {
        System.out.println("getProjectLibrary");
        Library id = null;
        LibraryController instance = new LibraryController();
        List<String> expResult = null;
        List<String> result = instance.getProjectLibrary(id);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetIdSample() {
        System.out.println("getIdSample");
        Library id = null;
        LibraryController instance = new LibraryController();
        List<Integer> expResult = null;
        List<Integer> result = instance.getIdSample(id);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLibsRun() {
        System.out.println("getLibsRun");
        LibraryController instance = new LibraryController();
        List<Library> expResult = null;
        List<Library> result = instance.getLibsRun();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testReset() {
        System.out.println("reset");
        LibraryController instance = new LibraryController();
        instance.reset();
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckSelectedRunLibraries() {
        System.out.println("checkSelectedRunLibraries");
        LibraryController instance = new LibraryController();
        instance.checkSelectedRunLibraries();
        fail("The test case is a prototype.");
    }

    @Test
    public void testValidatePlataform() {
        System.out.println("validatePlataform");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.validatePlataform();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testIsIllumina() {
        System.out.println("isIllumina");
        String platform = "";
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.isIllumina(platform);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckTags() {
        System.out.println("checkTags");
        LibraryController instance = new LibraryController();
        instance.checkTags();
        fail("The test case is a prototype.");
    }

    @Test
    public void testValidateFormGenerarCorrida() {
        System.out.println("validateFormGenerarCorrida");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.validateFormGenerarCorrida();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCreateRun() {
        System.out.println("createRun");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.createRun();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testValidateFormCreateSampleSheet() {
        System.out.println("validateFormCreateSampleSheet");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.validateFormCreateSampleSheet();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteUserFromNameSample() {
        System.out.println("deleteUserFromNameSample");
        String nameSample = "";
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.deleteUserFromNameSample(nameSample);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCreateSampleSheet() throws Exception {
        System.out.println("createSampleSheet");
        int caseOrigin = 0;
        int idRun = 0;
        Run runCurrent = null;
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.createSampleSheet(caseOrigin, idRun, runCurrent);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCleanFormCreateRun() {
        System.out.println("cleanFormCreateRun");
        LibraryController instance = new LibraryController();
        instance.cleanFormCreateRun();
        fail("The test case is a prototype.");
    }

    @Test
    public void testResetValues() {
        System.out.println("resetValues");
        LibraryController instance = new LibraryController();
        instance.resetValues();
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetOptPlataform() {
        System.out.println("getOptPlataform");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getOptPlataform();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPlataformSamplesL() {
        System.out.println("getPlataformSamplesL");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getPlataformSamplesL();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetOptPlataform() {
        System.out.println("setOptPlataform");
        String optPlataform = "";
        LibraryController instance = new LibraryController();
        instance.setOptPlataform(optPlataform);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPropNumLib() {
        System.out.println("getPropNumLib");
        LibraryController instance = new LibraryController();
        int expResult = 0;
        int result = instance.getPropNumLib();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetPropNumLib() {
        System.out.println("setPropNumLib");
        int propNumLib = 0;
        LibraryController instance = new LibraryController();
        instance.setPropNumLib(propNumLib);
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdate() {
        System.out.println("update");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.update();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLstLibrary_Project() {
        System.out.println("getLstLibrary_Project");
        LibraryController instance = new LibraryController();
        List<Library> expResult = null;
        List<Library> result = instance.getLstLibrary_Project();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetLstLibrary_Project() {
        System.out.println("setLstLibrary_Project");
        List<Library> lstLibrary_Project = null;
        LibraryController instance = new LibraryController();
        instance.setLstLibrary_Project(lstLibrary_Project);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLib() {
        System.out.println("getLib");
        LibraryController instance = new LibraryController();
        instance.getLib();
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetListaMuestras() {
        System.out.println("getListaMuestras");
        List<Sample> list = null;
        LibraryController instance = new LibraryController();
        List<List<String>> expResult = null;
        List<List<String>> result = instance.getListaMuestras(list);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testInit() {
        System.out.println("init");
        LibraryController instance = new LibraryController();
        instance.init();
        fail("The test case is a prototype.");
    }

    @Test
    public void testCanEditListSampleLibrary() {
        System.out.println("canEditListSampleLibrary");
        LibraryController instance = new LibraryController();
        Boolean expResult = null;
        Boolean result = instance.canEditListSampleLibrary();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateListSampleLibrary() {
        System.out.println("updateListSampleLibrary");
        TemplateLibrary item = null;
        String id_tag1 = "";
        String id_tag2 = "";
        String nameLibrary = "";
        LibraryController instance = new LibraryController();
        instance.updateListSampleLibrary(item, id_tag1, id_tag2, nameLibrary);
        fail("The test case is a prototype.");
    }

    @Test
    public void testUpdateListSampleLibraryKits() {
        System.out.println("updateListSampleLibraryKits");
        TemplateLibrary item = null;
        String id_tag1 = "";
        String id_tag2 = "";
        String nameLibrary = "";
        String itemKit = "";
        LibraryController instance = new LibraryController();
        instance.updateListSampleLibraryKits(item, id_tag1, id_tag2, nameLibrary, itemKit);
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteItemListSampleLibrary() {
        System.out.println("deleteItemListSampleLibrary");
        TemplateLibrary item = null;
        LibraryController instance = new LibraryController();
        instance.deleteItemListSampleLibrary(item);
        fail("The test case is a prototype.");
    }

    @Test
    public void testChangeAKit() {
        System.out.println("changeAKit");
        LibraryController instance = new LibraryController();
        instance.changeAKit();
        fail("The test case is a prototype.");
    }

    @Test
    public void testChangeDiferentsKits() {
        System.out.println("changeDiferentsKits");
        LibraryController instance = new LibraryController();
        instance.changeDiferentsKits();
        fail("The test case is a prototype.");
    }

    @Test
    public void testViewDiferentKit() {
        System.out.println("viewDiferentKit");
        LibraryController instance = new LibraryController();
        instance.viewDiferentKit();
        fail("The test case is a prototype.");
    }

    @Test
    public void testViewSampleList() {
        System.out.println("viewSampleList");
        LibraryController instance = new LibraryController();
        instance.viewSampleList();
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetKits() {
        System.out.println("getKits");
        LibraryController instance = new LibraryController();
        List<String> expResult = null;
        List<String> result = instance.getKits();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetDiferentsKits() {
        System.out.println("getDiferentsKits");
        String itemPlataform = "";
        LibraryController instance = new LibraryController();
        List<String> expResult = null;
        List<String> result = instance.getDiferentsKits(itemPlataform);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetPlataforms() {
        System.out.println("getPlataforms");
        LibraryController instance = new LibraryController();
        List<String> expResult = null;
        List<String> result = instance.getPlataforms();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testPutName() {
        System.out.println("putName");
        List<String> sams = null;
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.putName(sams);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSampleIds() {
        System.out.println("getSampleIds");
        List<String> samNames = null;
        LibraryController instance = new LibraryController();
        List<Sample> expResult = null;
        List<Sample> result = instance.getSampleIds(samNames);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetNameRepeted() {
        System.out.println("getNameRepeted");
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getNameRepeted();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetNameRepeted() {
        System.out.println("setNameRepeted");
        String nameRepeted = "";
        LibraryController instance = new LibraryController();
        instance.setNameRepeted(nameRepeted);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCreateLibraryL() {
        System.out.println("createLibraryL");
        ActionEvent actionEvent = null;
        LibraryController instance = new LibraryController();
        instance.createLibraryL(actionEvent);
        fail("The test case is a prototype.");
    }

    @Test
    public void testShowDialog() {
        System.out.println("showDialog");
        int i = 0;
        LibraryController instance = new LibraryController();
        instance.showDialog(i);
        fail("The test case is a prototype.");
    }

    @Test
    public void testLoadFieldGenerateRun() {
        System.out.println("loadFieldGenerateRun");
        Run runCurrent = null;
        LibraryController instance = new LibraryController();
        instance.loadFieldGenerateRun(runCurrent);
        fail("The test case is a prototype.");
    }

    @Test
    public void testKeyUpInputRun1() {
        System.out.println("keyUpInputRun1");
        LibraryController instance = new LibraryController();
        instance.keyUpInputRun1();
        fail("The test case is a prototype.");
    }

    @Test
    public void testLimpiaLista() {
        System.out.println("limpiaLista");
        LibraryController instance = new LibraryController();
        instance.limpiaLista();
        fail("The test case is a prototype.");
    }

    @Test
    public void testRender() {
        System.out.println("render");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.render();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetCountLibraries() {
        System.out.println("getCountLibraries");
        LibraryController instance = new LibraryController();
        int expResult = 0;
        int result = instance.getCountLibraries();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetCountLibraries() {
        System.out.println("setCountLibraries");
        int countLibraries = 0;
        LibraryController instance = new LibraryController();
        instance.setCountLibraries(countLibraries);
        fail("The test case is a prototype.");
    }

    @Test
    public void testProcesFile() throws Exception {
        System.out.println("procesFile");
        FileUploadEvent event = null;
        LibraryController instance = new LibraryController();
        instance.procesFile(event);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCloseStatusDialogUploadFile() {
        System.out.println("closeStatusDialogUploadFile");
        LibraryController instance = new LibraryController();
        instance.closeStatusDialogUploadFile();
        fail("The test case is a prototype.");
    }

    @Test
    public void testUploadFileLib() {
        System.out.println("uploadFileLib");
        FileUploadEvent event = null;
        LibraryController instance = new LibraryController();
        instance.uploadFileLib(event);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckEditLibraries() {
        System.out.println("checkEditLibraries");
        String[] listLibraries = null;
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.checkEditLibraries(listLibraries);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testRederLibraryUpdate() {
        System.out.println("rederLibraryUpdate");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.rederLibraryUpdate();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testRedirectToProj() {
        System.out.println("redirectToProj");
        LibraryController instance = new LibraryController();
        instance.redirectToProj();
        fail("The test case is a prototype.");
    }

    @Test
    public void testNewsBarcodes() throws Exception {
        System.out.println("NewsBarcodes");
        FileUploadEvent event = null;
        LibraryController instance = new LibraryController();
        instance.NewsBarcodes(event);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetRunName_Run() {
        System.out.println("getRunName");
        Run name = null;
        LibraryController instance = new LibraryController();
        instance.getRunName(name);
        fail("The test case is a prototype.");
    }

    @Test
    public void testDeleteLibraries() {
        System.out.println("DeleteLibraries");
        LibraryController instance = new LibraryController();
        instance.DeleteLibraries();
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckSelectedLibraries() {
        System.out.println("checkSelectedLibraries");
        LibraryController instance = new LibraryController();
        instance.checkSelectedLibraries();
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckPlataform() {
        System.out.println("checkPlataform");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.checkPlataform();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckQuatationProj() {
        System.out.println("checkQuatationProj");
        String pj = "";
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.checkQuatationProj(pj);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCheckPaymentProj() {
        System.out.println("checkPaymentProj");
        String pj = "";
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.checkPaymentProj(pj);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetSamplesProj() {
        System.out.println("getSamplesProj");
        Project proj = null;
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getSamplesProj(proj);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testLinkFileSampleSheet() {
        System.out.println("linkFileSampleSheet");
        String nameFile = "";
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.linkFileSampleSheet(nameFile);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCanDiferentKits() {
        System.out.println("canDiferentKits");
        LibraryController instance = new LibraryController();
        boolean expResult = false;
        boolean result = instance.canDiferentKits();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetBarcode1ByIdLibrary() {
        System.out.println("getBarcode1ByIdLibrary");
        int idLibrary = 0;
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getBarcode1ByIdLibrary(idLibrary);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetBarcode2ByIdLibrary() {
        System.out.println("getBarcode2ByIdLibrary");
        int idLibrary = 0;
        LibraryController instance = new LibraryController();
        String expResult = "";
        String result = instance.getBarcode2ByIdLibrary(idLibrary);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetLibsByProject() {
        System.out.println("getLibsByProject");
        LibraryController instance = new LibraryController();
        List<Library> expResult = null;
        List<Library> result = instance.getLibsByProject();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testResetListLibRun() {
        System.out.println("resetListLibRun");
        LibraryController instance = new LibraryController();
        instance.resetListLibRun();
        fail("The test case is a prototype.");
    }

    @Test
    public void testLibrariesByRunName() {
        System.out.println("librariesByRunName");
        LibraryController instance = new LibraryController();
        List<Library> expResult = null;
        List<Library> result = instance.librariesByRunName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testQualityReportByRunName() {
        System.out.println("qualityReportByRunName");
        LibraryController instance = new LibraryController();
        List<QualityReports> expResult = null;
        List<QualityReports> result = instance.qualityReportByRunName();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testCleanListSelectionLibrary() {
        System.out.println("cleanListSelectionLibrary");
        LibraryController instance = new LibraryController();
        instance.cleanListSelectionLibrary();
        fail("The test case is a prototype.");
    }
    
}
