package com.sportsbookscraper.app.excel;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class CellRangeTest {


    @Test(expected = IndexOutOfBoundsException.class) // tests whether the right
    public void testForConstructorWithNegativeParamsThrowsIAException() {
        Object o = emptyList.get(0);
    }


    /**
     * Runs once before any tests (Called once before anything else)
     */
    @BeforeClass
    public void setUpOnceFirst() {

    }
    
    /**
     * Cleans up any other resources. (Called once after evything else)
     */
    @AfterClass
    public void tearDownOnceLast() {

    }
    
    
    /**
     * Sets up the test fixture. (Called before every test case method.)
     */
    @Before
    public void setUp() {
        emptyList = new java.util.ArrayList();
    }


    /**
     * Tears down the test fixture. (Called after every test case method.)
     */
    @After
    public void tearDown() {
        emptyList = null;
    }
}
