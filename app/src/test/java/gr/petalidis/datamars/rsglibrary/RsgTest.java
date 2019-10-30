/*
 * Copyright 2017-2019 Nikolaos Petalidis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gr.petalidis.datamars.rsglibrary;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

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
