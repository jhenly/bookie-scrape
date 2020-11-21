package com.sportsbookscraper.app.excel;

import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Abstract class that {@link WorkbookReader} and {@link WorkbookWriter} extend.
 * 
 * @author Jonathan Henly
 */
public abstract class AbstractWorkbook implements AutoCloseable {
    
    /* subclass members */
    protected XSSFWorkbook workbook;
    protected String pathToWorkbook;
    
    protected AbstractWorkbook(String excelFilePath) throws IOException {
        try {
            workbook = new XSSFWorkbook(excelFilePath);
            pathToWorkbook = excelFilePath;
        } catch (IllegalArgumentException iae) {
            throw new IOException(iae);
        }
    }
    
    /**
     *
     */
    protected abstract class RangeIterator<E> implements Iterator<E> {
        
        
        @Override
        public abstract boolean hasNext();
        
        @Override
        public abstract E next();
        
    }
    
    
    /**
     * @return the number of sheets in the underlying Excel workbook.
     * 
     * @throws NullPointerException
     *                              if {@link #close()} has been called on this
     *                              workbook
     */
    public int numSheets() {
        throwIfClosed(); // throw NPE if close() has been called
        
        return workbook.getNumberOfSheets();
    }
    
    /**
     * Checks if the Excel file's workbook, associated with this workbook
     * instance, contains a sheet name that equals a passed in sheet name.
     *
     * @param sheetName
     *                  the name of the sheet to query
     * @return {@code true} if the Excel workbook contains a sheet named
     *         {@code sheetName}, otherwise {@code false}
     * @throws NullPointerException
     *                              if {@link #close()} has been called on this
     *                              workbook
     */
    public boolean hasSheet(String sheetName) {
        throwIfClosed(); // throw NPE if close() has been called
        
        return workbook.getSheet(sheetName) != null;
    }
    
    /**
     * Convenience method for subclasses to get a sheet from workbook, or throw
     * if it's closed.
     * 
     * @param sheetName
     *                  - the name of the workbook sheet to retrieve
     * @return the specified sheet from the workbook if it exists, otherwise
     *         {@code null}
     * @throws NullPointerException
     *                              if {@link #close()} has been called on this
     *                              workbook
     */
    protected Sheet getSheetFromWorkbook(String sheetName) {
        throwIfClosed(); // throw NPE if close() has been called
        
        return workbook.getSheet(sheetName);
    }
    
    
    /**
     * Convenience method for subclasses to throw a <i>helpful</i>
     * {@code NullPointerException} if {@code workbook} is {@code null}.
     * 
     * @throws NullPointerException
     *                              if {@link #close()} has been called on this
     *                              workbook
     */
    protected void throwIfClosed() throws NullPointerException {
        if (!isOpen()) {
            throw new NullPointerException("workbook is null, you may be"
                + " trying to use an " + this.getClass()
                + " instance method after calling its 'close()' method.");
        }
    }
    
    
    /**
     * Closes this {@code AbstractWorkbook} instance.
     *
     * @throws IOException
     *                     if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        workbook.close();
        
        // null out workbook to ensure isOpen and throwIfClosed work
        workbook = null;
    }
    
    /**
     * @return {@code true} if this {@code ExcelFileReader} has not been closed,
     *         otherwise {@code false}
     */
    public boolean isOpen() { return workbook != null; }
    
}
