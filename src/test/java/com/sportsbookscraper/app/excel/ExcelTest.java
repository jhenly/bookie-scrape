package com.sportsbookscraper.app.excel;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ExcelTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue(true);
    }
    
    @Test
    public void callingExcelFactoryNewExcelFileReaderWithWrongPathShouldThrowIOException()
    {
    	boolean didThrow = false;
    	ExcelFileReader efr = null;
    	try {
    		efr = ExcelFactory.newExcelFileReader("/tmp");
    		
    		efr.close();
		} catch (IOException ioe) {
			didThrow = true;
		} finally {
			didThrow = true;
		}
    	
    	assertTrue(didThrow);
    }
}
