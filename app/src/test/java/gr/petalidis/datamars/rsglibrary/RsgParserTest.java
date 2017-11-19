/**
 *    Copyright 2017 Nikolaos Petalidis
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *        you may not use this file except in compliance with the License.
 *        You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing, software
 *        distributed under the License is distributed on an "AS IS" BASIS,
 *        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *        See the License for the specific language governing permissions and
 *        limitations under the License.
 */


package gr.petalidis.datamars.rsglibrary;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        List<Rsg> result = RsgParser.parseRsgStrings(rsgStrings);
        assertEquals(expResult.size(),result.size());
        assertTrue(expResult.containsAll(result));
        assertTrue(result.containsAll(expResult));
    }

}
