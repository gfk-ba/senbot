package com.gfk.senbot.framework.data;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class CaseinsensitiveMapTest {


    @Test
    public void testPut() throws Exception {
        // covered by testGet()
    }

    @Test
    public void testGet() throws Exception {
        Map<String, String> theMap = new CaseinsensitiveMap<String>();

        theMap.put("UPPER", "UPPER");
        theMap.put("lower", "lower");

        assertEquals("UPPER", theMap.get("upper"));
        assertEquals("UPPER", theMap.get("UPPER"));
        
        assertEquals("lower", theMap.get("lower"));
        assertEquals("lower", theMap.get("LOWER"));
    }

}
