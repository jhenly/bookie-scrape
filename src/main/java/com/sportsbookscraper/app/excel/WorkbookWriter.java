package com.sportsbookscraper.app.excel;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Wrapper class for Apache POI's {@link XSSFWorkbook} class.
 * <p>
 * The main purpose of this class is to prevent the accidental altering of an
 * Excel file. This class also includes multiple helper functions.
 *
 * @author Jonathan Henly
 */
public class ExcelFileWriter {
    /* private members */
    private final Workbook workbook;

    /* opens an Excel workbook from the passed in file path */
    ExcelFileWriter(String excelFilePath) throws IOException {
        this(excelFilePath, false);
    }
    
    ExcelFileWriter(String excelFilePath, boolean createNew)
    throws IOException
    {
        workbook = new XSSFWorkbook(excelFilePath);
    }
    
    
    // Use the following to create a hyperlink in Excel via Apache POI
    // wb.getCreationHelper().createHyperlink(HyperlinkType.URL).;
}
