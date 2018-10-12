package gr.petalidis.datamars.inspections.utilities;

import org.junit.Test;

import static org.junit.Assert.*;

public class TagFormatterTest {

    @Test
    public void format() {

        assertEquals("0620-2284-0083",TagFormatter.format("062022840083"));
    }
}