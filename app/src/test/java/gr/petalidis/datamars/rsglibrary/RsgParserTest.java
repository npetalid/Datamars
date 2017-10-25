/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.petalidis.datamars.rsglibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author npetalid
 */
public class RsgParserTest {

    public RsgParserTest() {
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

    /**
     * Test of parseRsgStrings method, of class RsgParser.
     */
    @Test
    public void testParseRsgStrings() throws Exception {
        System.out.println("parseRsgStrings");

        List<String> rsgStrings = Arrays.asList("[A0040000300055000011420|26072017|123917]",
                "[A0040000300055000011420|26072017|124028]");

        List<Rsg> expResult = new ArrayList<>();
        expResult.add(new Rsg("A0040000300055000011420", "26072017", "123917"));
        expResult.add(new Rsg("A0040000300055000011420", "26072017", "124028"));

        RsgParser instance = new RsgParser();
        List<Rsg> result = instance.parseRsgStrings(rsgStrings);
        assertEquals(expResult.size(),result.size());
        assertTrue(expResult.containsAll(result));
        assertTrue(result.containsAll(expResult));
    }

}
