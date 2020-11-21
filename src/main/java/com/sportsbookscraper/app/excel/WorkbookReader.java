package com.sportsbookscraper.app.excel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
public class ExcelFileReader implements AutoCloseable {

    /* private members */
    private Workbook workbook;


    /* opens an Excel workbook from the passed in file path */
    ExcelFileReader(String excelFilePath) throws IOException {
        try {
            workbook = new XSSFWorkbook(excelFilePath);
        } catch (IllegalArgumentException iae) {
            throw new IOException(iae);
        }
    }


    /**
     * Reads strings from a specified row index and specified range of column
     * indexes.
     *
     * @param sheetName - the name of the Excel workbook sheet
     * @param row - the row index in the sheet
     * @param start - the index of the row to read from
     * @param end - the last cell's column index in the range
     * @return a list of strings from the specified range of cells in a row
     * @throws SheetNotFoundException - if this workbook does not contain a
     *         sheet named {@code sheetName}
     */
    public
    List<String> readStringsInRow(String sheetName, int row, int start, int end)
        throws SheetNotFoundException
    {
        // do closed check, arg count/value check, sheet exists check
        Sheet sheet = doPreChecksThenGetSheet(sheetName, row, start, end);
        Row rrow = sheet.getRow(row);

        return readStringsInRowFromStartToEnd(rrow, start, end);
    }


    /**
     * Reads strings from a specified row index, starting from a specified
     * column index.
     * <p>
     * This method will read all of the cells in a row after, and including, the
     * the starting column index. Meaning, any blank cells between the starting
     * column index cell and the last non-blank cell, will be empty strings in
     * the returned list.
     *
     * @param sheetName - the name of the Excel workbook sheet
     * @param row - the index of the row to read from
     * @param start - the column index to start reading from
     * @return a list of strings from the specified range of column indexes in a
     *         row
     * @throws SheetNotFoundException - if this workbook does not contain a
     *         sheet named {@code sheetName}
     */
    public List<String> readStringsInRow(String sheetName, int row, int start)
        throws SheetNotFoundException
    {
        // do closed check, arg count/value check, sheet exists check
        Sheet sheet = doPreChecksThenGetSheet(sheetName, row, start);
        Row rrow = sheet.getRow(row);

        return readStringsInRowFromStartToEnd(rrow, start,
            rrow.getLastCellNum());
    }

    /* readStringsInRow* helper method, reads string cells from start to end */
    private
    List<String> readStringsInRowFromStartToEnd(Row row, int start, int end)
        throws SheetNotFoundException
    {
        List<String> strings = new ArrayList<>(end - start);
        for (int col = start; col < end; col++) {
            Cell curCell = row.getCell(col);
            // if cell is empty then add empty string to list
            if (curCell != null && curCell.getCellType() == CellType.STRING) {
                strings.add(col, curCell.getStringCellValue());
            } else {
                strings.add("");
            }
        }

        return strings;
    }


    /**
     * Reads strings from a specified column index and specified range of row
     * indexes.
     *
     * @param sheetName - the name of the Excel workbook sheet
     * @param col - the col index in the sheet
     * @param start - the first cell's row index in the range
     * @param end - the last cell's row index in the range
     * @return a list of strings from the specified range of cells in a column
     * @throws SheetNotFoundException - if this workbook does not contain a
     *         sheet named {@code sheetName}
     */
    public
    List<String> readStringsInCol(String sheetName, int col, int start, int end)
        throws SheetNotFoundException
    {
        // do closed check, arg count/value check, sheet exists check
        Sheet sheet = doPreChecksThenGetSheet(sheetName, col, start, end);

        StringBuilder builder = new StringBuilder(); // holds comma separated
                                                     // cell values
        Row row;
        int curRow = start;
        // iterate over rows and get value from specified column index
        while ((row = sheet.getRow(curRow)) != null && curRow < end) {
            Cell curCell = row.getCell(col);

            if (curCell != null && curCell.getCellType() == CellType.STRING) {
                builder.append(curCell.getStringCellValue());
            } else {
                // if the cell is empty or not a string then append empty string
                builder.append("");
            }
            builder.append(',');

            curRow += 1;
        }


        // split cell values into string array and return new list from that
        return new ArrayList<>(Arrays.asList(builder.toString().split(",")));
    }


    /* helper to get sheet from workbook, or throw if it's closed */
    protected Sheet getSheetFromWorkbook(String sheetName) {
        throwIfClosed(); // throw NPE if close() has been called

        return workbook.getSheet(sheetName);
    }
    
    
    /**
     * @return the number of sheets in the underlying Excel workbook.
     */
    public int numSheets() {
        throwIfClosed(); // throw NPE if close() has been called

        return workbook.getNumberOfSheets();
    }


    /**
     * Checks if the Excel file's workbook, associated with this
     * {@code ExcelFileReader}, contains a sheet name that equals a passed in
     * sheet name.
     *
     * @param sheetName the name of the sheet to query
     * @return {@code true} if the Excel workbook contains a sheet named
     *         {@code sheetName}, otherwise {@code false}
     */
    public boolean hasSheet(String sheetName) {
        throwIfClosed(); // throw NPE if close() has been called

        return workbook.getSheet(sheetName) != null;
    }


    /**
     * Closes this {@code ExcelFileReader}.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        workbook.close();

        // null out workbook to ensure isOpen and throwIfWorkbookIsNull work
        workbook = null;
    }


    /**
     * @return {@code true} if this {@code ExcelFileReader} has not been closed,
     *         otherwise {@code false}
     */
    public boolean isOpen() { return workbook != null; }


    /* helper method for read* methods, does close/arg/sheet exists checks */
    private Sheet doPreChecksThenGetSheet(String sheetName, int index,
                                          int start, int end)
        throws SheetNotFoundException
    {
        throwIfClosed(); // throw NPE if close() has been called
        checkArgs(index, start, end); // throws IllegalArgumentException

        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) { throw new SheetNotFoundException(sheetName); }

        return sheet;
    }


    /* convenience overloaded helper, just calls doPreChecksThenGetSheet */
    private
    Sheet doPreChecksThenGetSheet(String sheetName, int index, int start)
        throws SheetNotFoundException
    {
        // pass 'start' twice so check args doesn't throw when 'start < end'
        return doPreChecksThenGetSheet(sheetName, index, start, start);
    }

    /* helper to throw a *helpful* NullPointerException if workbook is null */
    private void throwIfClosed() throws NullPointerException {
        if (workbook != null) {
            throw new NullPointerException("workbook is null, you may be"
                + " trying to use an ExcelFileReader instance method after"
                + " calling its 'close()' method.");
        }
    }
    
    /* helper to throw if int method arguments are negative or invalid range */
    private void checkArgs(int index, int start, int end)
        throws IllegalArgumentException
    {
        if ((index | start | end) < 0) {
            throw new IllegalArgumentException(
                "method arguments must be positive.");
        }
        if (start > end) {
            throw new IllegalArgumentException(
                "start index is greater than end index.");
        }
    }


}
