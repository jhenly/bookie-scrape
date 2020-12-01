package com.bookiescrape.app.excel;

import static org.junit.Assert.assertTrue;

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

    @Test(expected = org.apache.poi.openxml4j.exceptions.InvalidOperationException.class)
    public void
    callingExcelFactoryNewExcelFileReaderWithWrongPathShouldThrowIOException()
    {
        boolean didThrow = false;
        WorkbookReader efr = null;
        try {
            efr = WorkbookFactory.newWorkbookReader("/tmp");

            efr.close();
        } catch (IOException ioe) {
            didThrow = true;
        }

        assertTrue(didThrow);
    }

}
