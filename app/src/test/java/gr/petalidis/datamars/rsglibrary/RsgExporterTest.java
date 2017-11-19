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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

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
        RsgExporter.export(rsgList, folder.getRoot().getAbsolutePath(),rsgList.get(0).getName()+".csv");
        File file = new File(expectedFilename);
        assertTrue(file.exists());
        

    }
    
}
