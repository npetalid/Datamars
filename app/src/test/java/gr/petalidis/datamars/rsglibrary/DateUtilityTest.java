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

import static org.junit.Assert.assertEquals;

/**
 *
 * @author npetalid
 */
public class DateUtilityTest {
    
    public DateUtilityTest() {
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
     * Test of isValidYear method, of class RsgFileUtility.
     */
    @Test
    public void testIsValidYear() {
        System.out.println("isValidYear2009");
        String year = "2009";
        boolean expResult = true;
        boolean result = RsgFileUtility.isValidYear(year);
        assertEquals(expResult, result);
       
    }

     /**
     * Test of isValidYear method, of class RsgFileUtility.
     */
    @Test
    public void testIsInvalidYear() {
        System.out.println("testIsInvalidYear20a09");
        String year = "20a09";
        boolean expResult = false;
        boolean result = RsgFileUtility.isValidYear(year);
        assertEquals(expResult, result);
       
    }
    /**
     * Test of isValidMonth method, of class RsgFileUtility.
     */
    @Test
    public void testIsValidMonth() {
        System.out.println("isValidMonth");
        String month = "09";
        boolean expResult = true;
        boolean result = RsgFileUtility.isValidMonth(month);
        assertEquals(expResult, result);
    }

    /**
     * Test of isInvalidMonth method, of class RsgFileUtility.
     */
    @Test
    public void testIsinvalidMonth() {
        System.out.println("testIsinvalidMontha09");
        String month = "a09";
        boolean expResult = false;
        boolean result = RsgFileUtility.isValidMonth(month);
        assertEquals(expResult, result);
    }

     /**
     * Test of isInvalidMonth method, of class RsgFileUtility.
     */
    @Test
    public void testIsinvalidMonthLargerThan12() {
        System.out.println("testIsinvalidMonthLargerThan12");
        String month = "13";
        boolean expResult = false;
        boolean result = RsgFileUtility.isValidMonth(month);
        assertEquals(expResult, result);
    }
    /**
     * Test of isValidDay method, of class RsgFileUtility.
     */
    @Test
    public void testIsValidDay() {
        System.out.println("isValidDay");
        String day = "09";
        boolean expResult = true;
        boolean result = RsgFileUtility.isValidDay(day);
        assertEquals(expResult, result);
    }
    
     /**
     * Test of isValidDay method, of class RsgFileUtility.
     */
    @Test
    public void testIsinvalidDay() {
        System.out.println("isInvalidDay-v09");
        String day = "v09";
        boolean expResult = false;
        boolean result = RsgFileUtility.isValidDay(day);
        assertEquals(expResult, result);
    }
    
     /**
     * Test of isValidDay method, of class RsgFileUtility.
     */
    @Test
    public void testIsinvalidDayGreaterThan31() {
        System.out.println("isInvalidDay-v09");
        String day = "32";
        boolean expResult = false;
        boolean result = RsgFileUtility.isValidDay(day);
        assertEquals(expResult, result);
    }
}
