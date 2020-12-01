package com.bookiescrape.app.excel;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class ExcelTest {
    
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() { assertTrue(true); }
    
    @Test(expected = FileNotFoundException.class)
    public void
    callingExcelFactoryNewExcelFileReaderWithNonExistantRelativePathShouldThrowFNFException()
        throws FileNotFoundException, IOException
    {
        try (
        WorkbookReader efr = WorkbookFactory.newWorkbookReader("./_foo_"))
        {
            // constructing efr should throw FileNotFoundException
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void
    callingExcelFactoryNewExcelFileReaderWithExistingDirectoryRelativePathShouldThrowIAException()
        throws FileNotFoundException, IOException
    {
        try (
        WorkbookReader efr = WorkbookFactory.newWorkbookReader("src/test/java"))
        {
            // constructing efr should throw FileNotFoundException
        }
    }
    
}
