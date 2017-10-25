/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.petalidis.datamars.rsglibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class RsgTest {
    
    public RsgTest() {
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
     * Test of getIdentificationCode method, of class Rsg.
     */
    @Test
    public void testGetIdentificationCode() throws ParseException {
        System.out.println("getIdentificationCode");
        Rsg instance = new Rsg("A0000000964000000123456","26072017","124028");
        String expResult = "000000123456";
        String result = instance.getIdentificationCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCountryCode method, of class Rsg.
     */
    @Test
    public void testGetCountryCode() throws ParseException {
        System.out.println("getCountryCode");
        Rsg instance = new Rsg("A0000000964000000123456","26072017","124028");
        String expResult = "0964";
        String result = instance.getCountryCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDate method, of class Rsg.
     */
    @Test
    public void testGetDate() throws ParseException {
        System.out.println("getDate");
        Rsg instance = new Rsg("A0000000964000000123456","26072017","124028");
         SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy:HHmmss");
        Date expResult = format.parse("26072017:124028");
        Date result = instance.getDate();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Rsg.
     */
    @Test
    public void testToString() throws ParseException {
        System.out.println("toString");
        Rsg instance = new Rsg("A0000000964000000123456","26072017","124028");
        String expResult = "0964,000000123456,26/07/2017 124028";
        String result = instance.toString();
        assertEquals(expResult, result);
        
    }
    
}
