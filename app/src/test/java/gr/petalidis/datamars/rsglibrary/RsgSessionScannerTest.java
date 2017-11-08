/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.petalidis.datamars.rsglibrary;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author npetalid
 */
public class RsgSessionScannerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public RsgSessionScannerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        File yearFolder = folder.newFolder("2010");
        File monthPath = new File(yearFolder.getAbsolutePath(), "09");
        File filePath = new File(monthPath.getAbsoluteFile(), "session_04092010.rsg");

        filePath.mkdirs();
        yearFolder = folder.newFolder("a1232");
        monthPath = new File(yearFolder.getAbsolutePath(), "09");
        filePath = new File(monthPath.getAbsoluteFile(), "session_04092010");
        filePath.mkdirs();


        yearFolder = folder.newFolder("2011");
        monthPath = new File(yearFolder.getAbsolutePath(), "09as");
        filePath = new File(monthPath.getAbsoluteFile(), "session_04092010");
        filePath.mkdirs();


        yearFolder = folder.newFolder("2012");
        monthPath = new File(yearFolder.getAbsolutePath(), "09");
        filePath = new File(monthPath.getAbsoluteFile(), "session_04092010");
        filePath.mkdirs();


    }

    @After
    public void tearDown() {
        folder.delete();
    }

    /**
     * Test of scanDirectory method, of class RsgSessionScanner.
     */
    @Test
    public void testScanDirectory() {
        System.out.println("scanDirectory");
        String root = folder.getRoot().getAbsolutePath();
        RsgSessionScanner instance = new RsgSessionScanner();
        HashMap<String, String> expResult = new HashMap<>();
        expResult.put("2010-09-04", root+"2010"+File.separator+"09"+File.separator+"session_04092010.rsg");
        HashMap<String, String> result = instance.scanDirectory(root);
        assertEquals(expResult.size(), result.size());
        assertEquals(expResult.keySet(),result.keySet());
    }

}
