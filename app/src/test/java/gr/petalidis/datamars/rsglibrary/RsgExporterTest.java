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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author npetalid
 */
public class RsgExporterTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public RsgExporterTest() {
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
        folder.delete();
    }

    /**
     * Test of export method, of class RsgExporter.
     */
    @Test
    public void testExport() throws Exception {
        System.out.println("export");
         List<Rsg> rsgList = new ArrayList<>();
        rsgList.add(new Rsg("A0040000300055000011420", "26072017", "123917"));
        rsgList.add(new Rsg("A0040000300055000011420", "26072017", "124028"));

        RsgExporter instance = new RsgExporter();
        String expectedFilename = folder.getRoot().getAbsolutePath()+ File.separator + rsgList.get(0).getName()+".csv";
        instance.export(rsgList, folder.getRoot().getAbsolutePath());
        File file = new File(expectedFilename);
        assertTrue(file.exists());
        

    }
    
}
