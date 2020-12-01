package com.bookiescrape.app.excel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Wrapper class for Apache POI's {@link XSSFWorkbook} class.
 * <p>
 * The main purpose of this class is to prevent the accidental altering of an
 * Excel file. This class also includes multiple helper functions.
 *
 * @author Jonathan Henly
 */
public class WorkbookReader extends AbstractWorkbook implements AutoCloseable {
    private Sheet currentSheet;
    
    /* opens an Excel workbook from the passed in file path */
    WorkbookReader(String excelFilePath) throws IOException {
        super(excelFilePath);
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
    public List<String>
    readStringsInRow(String sheetName, int row, int start, int end)
        throws SheetNotFoundException
    {
        // do closed check, arg count/value check, sheet exists check
        Sheet sheet = doPreChecksThenGetSheet(sheetName, row, start, end);
        currentSheet = sheet;
        
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
        currentSheet = sheet;
        
        Row rrow = sheet.getRow(row);
        
        return readStringsInRowFromStartToEnd(rrow, start,
            rrow.getLastCellNum());
    }
    
    /* readStringsInRow* helper method, reads string cells from start to end */
    private List<String>
    readStringsInRowFromStartToEnd(Row row, int start, int end)
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
     * Used to tell the reader when to stop reading.
     *
     * @author Jonathan Henly
     */
    public enum StopOn {
        /** stop reading when a blank cell is encountered */
        BLANK,
        /** stop reading when a null cell is encountered */
        NULL,
        /** stop reading when a numeric cell is encountered */
        NUMBER,
        /** stop reading when a string cell is encountered */
        STRING;
    }
    
    public class RangeReaderBuilder {
        private WorkbookReader ref;
        private CellRange range;
        private Sheet sheet;
        private String nameOfSheet;
        private StopOn stopOn;
        
        protected RangeReaderBuilder(WorkbookReader ref, CellRange range) {
            this.ref = ref;
            this.range = range;
        }
        
        protected RangeReaderBuilder(WorkbookReader ref, Sheet sheet,
                                     CellRange range)
        {
            this(ref, range);
            this.sheet = sheet;
        }
        
        /**
         * @param sheetName
         * @return
         * @throws SheetNotFoundException
         */
        public RangeReaderBuilder in(String sheetName)
            throws SheetNotFoundException
        {
            
            sheet = ref.getSheetFromWorkbook(sheetName);
            nameOfSheet = sheetName;
            return this;
        }
        
        
        /**
         * Reads the values from a range of cells in a specified sheet in this
         * workbook.
         *
         * @return a {@code List<String>} of the cell values read from the range
         */
        public List<String> read() {
            if (sheet == null) {
                if (nameOfSheet == null) {
                    throw new NullPointerException(
                        "sheet is null, you probably forgot to call "
                            + "'in(sheetName)'");
                } else {
                    throw new NullPointerException(
                        "workbook does not have a sheet named '" + nameOfSheet
                            + "'");
                }
            }
            return (new RangeReader(this)).readRange();
        }
        
    }
    
    /**
     * @author Jonathan Henly
     */
    public class RangeReader {
        private Sheet sheet;
        private CellRange range;
        private StopOn stopOn;
        
        protected RangeReader(RangeReaderBuilder builder) {
            sheet = builder.sheet;
            range = builder.range;
            stopOn = builder.stopOn;
        }
        
        protected List<String> readRange() {
            
            switch (range.type()) {
                case CELL:
                    return readCellRange();
                
                case ROW:
                    return readRowRange();
                
                case OPEN_ROW:
                    return readOpenRowRange();
                
                case COL:
                    return readColRange();
                
                case OPEN_COL:
                    return readOpenColRange();
                
                case ROW_COL:
                    return readRowColRange();
                
                default: // we should never get here
                    return null; // make compiler happy
            }
        }
        
        /* returns a list containing the value of a single cell */
        private List<String> readCellRange() {
            List<String> list = new ArrayList<>(1);
            Row rs = sheet.getRow(range.rowStart());
            Cell cell = rs.getCell(range.colStart());
            
            list.add(getCellValueAsString(cell));
            
            return list;
        }
        
        /* returns a list containing the values of cells in a row range */
        private List<String> readRowRange() {
            List<String> list = new ArrayList<>();
            
            Row rs = sheet.getRow(range.rowStart());
            for (int c = range.colStart(), n = range.colEnd(); c <= n; c++) {
                Cell cell = rs.getCell(c);
                list.add(getCellValueAsString(cell));
            }
            
            return list;
        }
        
        /* returns a list containing the values of cells in a open row range */
        private List<String> readOpenRowRange() {
            List<String> list = new ArrayList<>();
            
            Row rs = sheet.getRow(range.rowStart());
            int end = rs.getLastCellNum();
            
            for (int c = range.colStart(); c <= end; c++) {
                Cell cell = rs.getCell(c);
                list.add(getCellValueAsString(cell));
            }
            
            return list;
        }
        
        /* returns a list containing the values of cells in a col range */
        private List<String> readColRange() {
            List<String> list = new ArrayList<>();
            
            int column = range.colStart();
            for (int r = range.rowStart(), n = range.rowEnd(); r <= n; r++) {
                Row curRow = sheet.getRow(r);
                Cell cell = curRow.getCell(column);
                list.add(getCellValueAsString(cell));
            }
            
            return list;
        }
        
        /* returns a list containing the values of cells in an open col range */
        private List<String> readOpenColRange() {
            List<String> list = new ArrayList<>();
            
            int column = range.colStart();
            Iterator<Row> ri = sheet.iterator();
            // iterate to starting row
            for (int count = 0; count < range.rowStart(); count++) {
                ri.next();
            }
            // read column from each row, until no more rows
            while (ri.hasNext()) {
                Row cur = ri.next();
                list.add(cur.getCell(column).getStringCellValue());
            }
            
            return list;
        }
        
        /* returns a list containing the values of cells in a row_col range */
        private List<String> readRowColRange() {
            List<String> list = new ArrayList<>();
            
            for (int r = range.rowStart(), rn = range.rowEnd(); r <= rn; r++) {
                Row curRow = sheet.getRow(r);
                
                int cn = range.colEnd();
                for (int c = range.colStart(); c <= cn; c++) {
                    Cell cell = curRow.getCell(c);
                    list.add(getCellValueAsString(cell));
                }
            }
            
            return list;
        }
        
        /* helper method for read* methods */
        private String getCellValueAsString(Cell cell) {
            if (cell == null) { return ""; }
            
            switch (cell.getCellType()) {
                case BLANK:
                    return "";
                
                case STRING:
                    return cell.getStringCellValue();
                
                case BOOLEAN:
                    boolean b = cell.getBooleanCellValue();
                    return Boolean.toString(b);
                
                case NUMERIC:
                    double d = cell.getNumericCellValue();
                    return Double.toString(d);
                
                case FORMULA:
                    return cell.getCellFormula();
                
                case ERROR:
                    byte error = cell.getErrorCellValue();
                    return "Error Cell: " + error;
                    
                // only time we reach here is when cell is _NONE, which is only
                // used internally by POI
                default:
                    return "";
            }
            
        }
    }
    
    /**
     * TODO document this method
     *
     * @param range
     * @return
     */
    public RangeReaderBuilder forRange(CellRange range) {
        if (currentSheet == null) {
            return new RangeReaderBuilder(this, range);
        } else {
            return new RangeReaderBuilder(this, currentSheet, range);
        }
    }
    
    public RangeReaderBuilder forOpenRowRange(int row, int start) {
        return forRange(CellRange.openRowRange(row, start));
    }
    
    public RangeReaderBuilder forOpenColRange(int row, int start) {
        return forRange(CellRange.openRowRange(row, start));
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
    public List<String>
    readStringsInCol(String sheetName, int col, int start, int end)
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
    
    
    /* helper method for read* methods, does close/arg/sheet exists checks */
    private Sheet
    doPreChecksThenGetSheet(String sheetName, int index, int start, int end)
        throws SheetNotFoundException
    {
        throwIfClosed(); // throw NPE if close() has been called
        checkArgs(index, start, end); // throws IllegalArgumentException
        
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) { throw new SheetNotFoundException(sheetName); }
        
        return sheet;
    }
    
    
    /* convenience overloaded helper, just calls doPreChecksThenGetSheet */
    private Sheet
    doPreChecksThenGetSheet(String sheetName, int index, int start)
        throws SheetNotFoundException
    {
        // pass 'start' twice so check args doesn't throw when 'start < end'
        return doPreChecksThenGetSheet(sheetName, index, start, start);
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
