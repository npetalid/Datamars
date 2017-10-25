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
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author npetalid
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({gr.petalidis.datamars.rsglibrary.RsgExporterTest.class, gr.petalidis.datamars.rsglibrary.DateUtilityTest.class, gr.petalidis.datamars.rsglibrary.RsgTest.class, gr.petalidis.datamars.rsglibrary.DateScannerTest.class, gr.petalidis.datamars.rsglibrary.RsgParserTest.class})
public class RsglibrarySuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}
