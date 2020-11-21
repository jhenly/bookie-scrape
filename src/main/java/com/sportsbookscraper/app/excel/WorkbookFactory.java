package com.sportsbookscraper.app.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;


/**
 * @author Jonathan Henly
 */
public final class ExcelFactory {
    
    private ExcelFactory() {}
    
    /**
     * Opens an Excel workbook reader with the specified the file path.
     *
     * @param excelFilePath - path to the Excel file
     * @return an new Excel workbook reader instance
     * @throws IOException if an I/O error occurs
     */
    public static ExcelFileReader newExcelFileReader(String excelFilePath)
        throws IOException
    {
        return new ExcelFileReader(excelFilePath);
    }
    
    /**
     * Opens an Excel workbook writer with the specified the file path.
     *
     * @param excelFilePath - path to the Excel file
     * @return a new Excel workbook writer instance
     * @throws IOException if an I/O error occurs
     */
    public static ExcelFileWriter newExcelFileWriter(String excelFilePath)
        throws IOException
    {
        return new ExcelFileWriter(excelFilePath);
    }
    
    /**
     * Creates a new Excel file at the specified file path and opens an Excel
     * workbook writer with the newly created workbook.
     *
     * @param newExcelFilePath - path to create the new Excel file.
     * @return an Excel workbook writer instance opened with the newly created
     *         Excel file
     * @throws IOException if an I/O error occurs
     */
    public static ExcelFileWriter createNewExcelFile(String newExcelFilePath)
        throws IOException
    {
        doCreateFileChecks(newExcelFilePath); // throws IOException
        createWriteAndCloseNewExcelFile(newExcelFilePath);
        
        return new ExcelFileWriter(newExcelFilePath, true);
    }
    
    /* checks new Excel file path and throws exceptions accordingly */
    private static String doCreateFileChecks(String newExcelFilePath)
        throws IOException
    {
        Objects.requireNonNull(newExcelFilePath);

        if (newExcelFilePath.isBlank()) {
            throw new IllegalArgumentException(
                "cannot create a new Excel file with an empty string");
        }

        Path newFilePath = (new File(newExcelFilePath)).toPath().normalize();

        if (Files.exists(newFilePath)) {
            /* new file path points to an existing file, so throw */
            throw new FileAlreadyExistsException(newExcelFilePath);
        }

        if (!Files.isDirectory(newFilePath.getParent().normalize())) {
            /* new file's parent is not a directory, so throw */
            throw new IOException(String.format(
                "cannot create new Excel file, the parent directory in '%s' is"
                    + "not a directory",
                newExcelFilePath));
        }

        return newExcelFilePath;
    }

    /* function that creates, writes and closes a new Excel file */
    private static void createWriteAndCloseNewExcelFile(String newExcelFilePath)
        throws IOException
    {
        // create new workbook and file output stream
        Workbook wb = XSSFWorkbookFactory.createWorkbook();
        FileOutputStream out = new FileOutputStream(newExcelFilePath);
        
        // write new workbook to file
        wb.write(out);
        
        // close file output stream and workbook
        out.close();
        wb.close();
    }

}
