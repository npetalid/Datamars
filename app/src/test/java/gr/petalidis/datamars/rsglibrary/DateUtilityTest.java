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
