package com.bookiescrape.app.sample;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Sample unit test.
 */
public class AppTest {

    private java.util.List emptyList;
    
    
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
    
    
    @Test
    public void testSomeBehavior() {
        assertEquals("Empty list should have 0 elements", 0, emptyList.size());
    }


    @Test(expected = IndexOutOfBoundsException.class) // tests whether the right
    public void testForException() {                  // exception was thrown
        Object o = emptyList.get(0);
    }


}

