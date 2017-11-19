/*
     Copyright 2017 Nikolaos Petalidis
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
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
    public void testScanDirectory() throws Exception {
        System.out.println("scanDirectory");
        String root = folder.getRoot().getAbsolutePath();
        RsgSessionScanner instance = new RsgSessionScanner();
        HashMap<String, String> expResult = new HashMap<>();
        expResult.put("2010-09-04", root+"2010"+File.separator+"09"+File.separator+"session_04092010.rsg");
        RsgSessionFiles result = RsgSessionScanner.scanDirectory(root);
        assertEquals(expResult.size(), result.getSessions().size());
    }

}
