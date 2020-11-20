package com.sportsbookscraper.app.excel;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class CellRangeTest {
    
    /**
     * Runs once before any tests (Called once before anything else)
     */
    @BeforeClass
    public static void setUpOnceFirst() {
        
    }

    /**
     * Cleans up any other resources. (Called once after evything else)
     */
    @AfterClass
    public static void tearDownOnceLast() {
        
    }
    
    public void testForConstructorWithNegativeParamsThrowsIAException() {
        
    }

    List<Object> emptyList;
    
    // tests whether the right exception is thrown
    @Test(expected = IndexOutOfBoundsException.class)
    public void testForThrowsIndexOutOfBoundsException() {
        Object o = emptyList.get(0);
    }

    /**
     * Sets up the test fixture. (Called before every test case method.)
     */
    @Before
    public void setUp() {
        emptyList = Collections.emptyList();
    }
    
    
    /**
     * Tears down the test fixture. (Called after every test case method.)
     */
    @After
    public void tearDown() {
        emptyList = null;
    }
}
