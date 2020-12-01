package com.bookiescrape.app.excel;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bookiescrape.app.excel.CellRange;
import com.bookiescrape.app.excel.CellRange.RangeType;


public class CellRangeTest {
    CellRange validSing;
    
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
        
    }
    
    /**
     * Tears down the test fixture. (Called after every test case method.)
     */
    @After
    public void tearDown() {
        
    }
    
    
    @Test
    public void testCreateCellRangeWithTwoParamsReturnsCellRangeTypeCell() {
        assertTrue(CellRange.cell(10, 10).type() == RangeType.CELL);
    }
    
    @Test
    public void testCellRangeRowRangeWithDifferentColsReturnsRangeTypeRow() {
        assertTrue(CellRange.rowRange(4, 10, 12).type() == RangeType.ROW);
    }
    
    @Test
    public void testCellRangeRowRangeWithSameColsReturnsRangeTypeCell() {
        assertTrue(CellRange.rowRange(4, 10, 10).type() == RangeType.CELL);
    }
    
    @Test
    public void testCellRangeColRangeWithDifferentRowsReturnsRangeTypeCol() {
        assertTrue(CellRange.colRange(4, 10, 12).type() == RangeType.COL);
    }
    
    @Test
    public void testCellRangeColRangeWithSameRowsReturnsRangeTypeCell() {
        assertTrue(CellRange.colRange(4, 10, 10).type() == RangeType.CELL);
    }
    
    @Test
    public void testCellRangeRangeWithSameRowsAndColsReturnsRangeTypeCell() {
        assertTrue(CellRange.range(10, 10, 12, 12).type() == RangeType.CELL);
    }
    
    @Test
    public void testCellRangeRangeWithSameColsReturnsRangeTypeCol() {
        assertTrue(CellRange.range(10, 20, 30, 30).type() == RangeType.COL);
    }
    
    @Test
    public void testCellRangeRangeWithSameRowsReturnsRangeTypeRow() {
        assertTrue(CellRange.range(10, 10, 20, 30).type() == RangeType.ROW);
    }
    
    @Test
    public void testCellRangeRangeWithAllDifferentReturnsRangeTypeRowCol() {
        assertTrue(CellRange.range(10, 20, 30, 40).type() == RangeType.ROW_COL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCellRangeRangeWithNegativeRowStartThrowsIllegalArgumentException() {
        CellRange negRowStart = CellRange.range(-10, 10, 10, 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCellRangeRangeWithNegativeRowEndThrowsIllegalArgumentException() {
        CellRange negRowEnd = CellRange.range(10, -10, 10, 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCellRangeRangeWithNegativeColStartThrowsIllegalArgumentException() {
        CellRange negColStart = CellRange.range(10, 10, -10, 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCellRangeRangeWithNegativeColEndThrowsIllegalArgumentException() {
        CellRange negColEnd = CellRange.range(10, 10, 10, -10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCellRangeRangeWithAllNegativeParamsThrowsIllegalArgumentException() {
        CellRange negAllParams = CellRange.range(-10, -10, -10, -10);
    }
    
    @Test
    public void testCellRangeRangeNumCellsOfSingleCellReturnsOne() {
        CellRange range = CellRange.range(0, 0, 0, 0);
        int expected = 1;
        assertTrue(range.numCells() == expected);
    }
    
    @Test
    public void testCellRangeRangeNumCellsReturnsActualNumberOfCellsInRange() {
        CellRange range = CellRange.range(0, 2, 0, 2);
        int expected = 9;
        assertTrue(range.numCells() == expected);
    }
    
    @Test
    public void testCellRangeRangeNumCellsNonZeroStartingRangeReturnsActualNumberOfCellsInRange() {
        CellRange range = CellRange.range(1, 3, 1, 3);
        int expected = 9;
        assertTrue(range.numCells() == expected);
    }
    
    @Test
    public void testCellRangeRangeNumCellsJaggedReturnsActualNumberOfCellsInRange() {
        CellRange range = CellRange.range(0, 2, 0, 4);
        int expected = 15;
        assertTrue(range.numCells() == expected);
    }
    
    @Test
    public void testCellRangeRangeNumRowsReturnsActualNumberOfRowsInRange() {
        CellRange range = CellRange.range(0, 2, 0, 4);
        int expected = 3;
        assertTrue(range.numRows() == expected);
    }
    
    @Test
    public void testCellRangeRangeNumColsReturnsActualNumberOfColsInRange() {
        CellRange range = CellRange.range(0, 2, 0, 4);
        int expected = 5;
        assertTrue(range.numCols() == expected);
    }
    
    @Test
    public void testCellRangeRowRangeNumColsReturnsActualNumberOfRowsInRange() {
        CellRange range = CellRange.rowRange(4, 1, 6);
        int expected = 1;
        assertTrue(range.numRows() == expected);
    }
    
    @Test
    public void testCellRangeRowRangeNumColsReturnsActualNumberOfColsInRange() {
        CellRange range = CellRange.rowRange(4, 1, 6);
        int expected = 6;
        
        System.out.println(range.numCols());
        assertTrue(range.numCols() == expected);
    }
    
    @Test
    public void testCellRangeColRangeNumColsReturnsActualNumberOfColsInRange() {
        CellRange range = CellRange.colRange(4, 1, 6);
        int expected = 1;
        assertTrue(range.numCols() == expected);
    }
    
    @Test
    public void testCellRangeColRangeNumRowsReturnsActualNumberOfRowsInRange() {
        CellRange range = CellRange.colRange(4, 1, 6);
        int expected = 6;
        
        assertTrue(range.numRows() == expected);
    }
    
    @Test
    public void testCellRangeRangeStartGreaterThanEndNumCellsReturnsActualNumberOfCellsInRange() {
        CellRange range = CellRange.range(2, 0, 2, 0);
        int expected = 9;
        assertTrue(range.numCells() == expected);
    }
    
    @Test
    public void testCellRangeRangeRowStartGreaterThanRowEndNumCellsReturnsActualNumberOfCellsInRange() {
        CellRange range = CellRange.range(0, 2, 2, 0);
        int expected = 9;
        
        assertTrue(range.numCells() == expected);
    }
    
    @Test
    public void testCellRangeRangeColStartGreaterThanColEndNumCellsReturnsActualNumberOfCellsInRange() {
        CellRange range = CellRange.range(2, 0, 2, 0);
        int expected = 9;
        assertTrue(range.numCells() == expected);
    }
    
    
}
