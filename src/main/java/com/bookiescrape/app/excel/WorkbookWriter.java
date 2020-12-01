package com.bookiescrape.app.excel;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IgnoredErrorType;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Wrapper class for Apache POI's {@link XSSFWorkbook} class.
 * <p>
 * The main purpose of this class is to alter an Excel workbook file. This class
 * also includes multiple helper functions.
 *
 * @author Jonathan Henly
 */
public class WorkbookWriter extends AbstractWorkbook {
    /* private members */
    private XSSFSheet currentSheet;
    
    /* opens an Excel workbook from the passed in file path */
    WorkbookWriter(String excelFilePath) throws IOException {
        super(excelFilePath);
    }
    
    /**
     * @throws IOException
     */
    public void saveChangesToWorkbook() throws IOException {
        saveChangesToWorkbook(pathToWorkbook);
    }
    
    /**
     * @param differentExcelFileName
     * @throws IOException
     */
    public void saveChangesToWorkbook(String fileName) throws IOException {
        String test = "src/test/resources/excel/Test.xlsx";
        
        BufferedOutputStream bos =
            new BufferedOutputStream(new FileOutputStream(test));
        
        workbook.write(bos);
        
        bos.flush();
        
        bos.close();
    }
    
    /**
     * Auto size a range of columns in the specified sheet.
     * <p>
     * <b>Note:</b> this method will not effect the size of columns after they
     * have been changed, for this reason you should call this method after
     * making your last change to the call, or better yet - right before your
     * save you this workbook.
     * <p>
     * <b>Note:</b> for large sheets this can take a long time, so it's best to
     * only call this method once.
     *
     * @param sheetName - the sheet to auto size columns in
     * @param lastColumnIndex - the last column to auto size
     */
    public void autoSizeColumns(String sheetName, int lastColumnIndex) {
        forRange(CellRange.rowRange(0, 0, lastColumnIndex)).in(sheetName)
            .autoSizeColumns();
    }
    
    /**
     * TODO document this method
     *
     * @param range
     * @return
     */
    public RangeWriterBuilder forRange(CellRange range) {
        if (currentSheet == null) {
            return new RangeWriterBuilder(this, range);
        } else {
            return new RangeWriterBuilder(this, currentSheet, range);
        }
    }
    
    protected enum DataType {
        STRING, INTEGER, DOUBLE;
    }
    
    /**
     * @author Jonathan Henly
     */
    protected class RangeWriterBuilder {
        private static final String DEF_DATA_FORMAT = "General";
        private WorkbookWriter ref;
        private CellRange range;
        private XSSFSheet sheet;
        private String nameOfSheet;
        
        private XSSFCellStyle cellStyle;
        private XSSFDataFormat dataFormat;
        private XSSFFont font;
        private DataType dtype;
        
        
        /**
         * @param ref - reference to this workbook writer
         * @param range - the range of cells to operate on
         */
        protected RangeWriterBuilder(WorkbookWriter ref, CellRange range) {
            this.ref = ref;
            this.range = range;
            
            cellStyle = ref.workbook.createCellStyle();
            dataFormat = ref.workbook.createDataFormat();
            cellStyle.setDataFormat(dataFormat.getFormat(DEF_DATA_FORMAT));
            
            font = createDefaultFont();
            dtype = DataType.STRING;
        }
        
        /**
         * @param ref - reference to this workbook writer
         * @param sheet - the sheet that the specified cell range pertains to
         * @param range - the range of cells to operate on
         */
        protected RangeWriterBuilder(WorkbookWriter ref, XSSFSheet sheet,
                                     CellRange range)
        {
            this(ref, range);
            
            this.sheet = sheet;
        }
        
        /* method used by constructor */
        private XSSFFont createDefaultFont() {
            XSSFFont f = ref.workbook.createFont();
            f.setFontName("Calibri");
            f.setFontHeightInPoints((short) 11);
            f.setColor(Font.COLOR_NORMAL);
            f.setBold(false);
            f.setItalic(false);
            f.setUnderline(Font.U_NONE);
            f.setTypeOffset(Font.SS_NONE);
            f.setStrikeout(false);
            
            return f;
        }
        
        /**
         * Signals that the data to be written is of type string.
         *
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder dataIsString() {
            dtype = DataType.STRING;
            return this;
        }
        
        /**
         * Signals that the data to be written is of type integer.
         *
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder dataIsInteger() {
            // stopExcelFromShowingNumberStoredAsTextWarning();
            
            dtype = DataType.INTEGER;
            return this;
        }
        
        /**
         * Signals that the data to be written is of type double.
         *
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder dataIsDouble() {
            // stopExcelFromShowingNumberStoredAsTextWarning();
            
            dtype = DataType.DOUBLE;
            return this;
        }
        
        /* */
        private void stopExcelFromShowingNumberStoredAsTextWarning() {
            CellRangeAddress region = new CellRangeAddress(range.rowStart(),
                range.rowEnd(), range.colStart(), range.colEnd());
            
            sheet.addIgnoredErrors(region,
                IgnoredErrorType.NUMBER_STORED_AS_TEXT);
        }
        
        /**
         * Sets the cell range formatting to align horizontally by a specified
         * value.
         *
         * @param hAlign - one of the {@code HorizontalAlignment} enums.
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder hAlign(HorizontalAlignment hAlign) {
            cellStyle.setAlignment(hAlign);
            return this;
        }
        
        /**
         * Sets the cell range formatting to align vertically by a specified
         * value.
         *
         * @param vAlign - one of the {@code VerticalAlignment} enums.
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder vAlign(VerticalAlignment vAlign) {
            cellStyle.setVerticalAlignment(vAlign);
            return this;
        }
        
        /**
         * Specifies the data format to use when writing the cell range.
         * <p>
         * For example: {@code "General"} (the default), {@code "0"},
         * {@code "0.00"}, {@code "#,##0"}, etc.
         *
         * @param format - the cell data format to use
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder withFormat(String format) {
            cellStyle.setDataFormat(dataFormat.getFormat(format));
            return this;
        }
        
        /**
         * Specifies the font name to use when writing the cell range.
         * <p>
         * For example: {@code "Calibri"} (the default), {@code "Arial"}, etc.
         *
         * @param fontName - the font name to use
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder setFontName(String fontName) {
            font.setFontName(fontName);
            return this;
        }
        
        /**
         * Specifies the font size to use when writing the cell range.
         * <p>
         * For example: {@code 11} (the default), {@code 12}, etc.
         *
         * @param fontName - the font size to use
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder setFontSize(int fontSize) {
            font.setFontHeightInPoints((short) fontSize);
            return this;
        }
        
        /**
         * Specifies whether or not to use a bold font when writing the cell
         * range.
         *
         * @param bold - {@code true} if the font should be bold, otherwise
         *        {@code false}
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder useBoldFont(boolean bold) {
            font.setBold(bold);
            return this;
        }
        
        /**
         * Specifies whether or not to use an italic font when writing the cell
         * range.
         *
         * @param italic - {@code true} if the font should be bold, otherwise
         *        {@code false}
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder useItalicFont(boolean italic) {
            font.setItalic(italic);
            return this;
        }
        
        /**
         * Specifies a sheet to be used in one of the build methods, i.e.
         * {@link #write(List)}, {@link #autoSizeColumns()}, etc.
         *
         * @param sheetName - the name of the sheet to be used by the
         *        {@code RangeWriter}
         * @return {@code this}, in order to chain builder calls
         */
        public RangeWriterBuilder in(String sheetName) {
            nameOfSheet = sheetName;
            XSSFSheet load = (XSSFSheet) ref.getSheetFromWorkbook(sheetName);
            sheet = load;
            return this;
        }
        
        /**
         *
         */
        public void autoSizeColumns() {
            throwNPEIfSheetOrNameOfSheetAreNull();
            
            (new RangeWriter(sheet, range)).autoSizeColumns();
        }
        
        /**
         * Writes values from a {@code List<String>} to a range of cells in a
         * specified sheet in this workbook.
         *
         * @param whatToWrite {@code List<String>} of the data to write to the
         *        specified range of cells.
         */
        public void write(List<String> whatToWrite) {
            throwNPEIfSheetOrNameOfSheetAreNull();
            
            cellStyle.setFont(font);
            
            (new RangeWriter(sheet, range, whatToWrite, cellStyle, dtype))
                .writeRange();
        }
        
        /* helper that checks for null in sheet or sheetName */
        private void throwNPEIfSheetOrNameOfSheetAreNull() {
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
        }
        
    }
    
    /**
     * @author Jonathan Henly
     */
    public class RangeWriter {
        private XSSFSheet sheet;
        private CellRange range;
        private List<String> out;
        private XSSFCellStyle cstyle;
        private DataType dtype;
        
        /**
         * Constructor used to write data to a given range of cells in a
         * specified sheet.
         *
         * @param sht - the sheet to perform actions on
         * @param rng - the range to perform actions on
         * @param toWrite
         */
        protected RangeWriter(XSSFSheet sht, CellRange rng,
                              List<String> toWrite, XSSFCellStyle style,
                              DataType dataType)
        {
            this(sht, rng);
            
            
            out = toWrite;
            cstyle = style;
            dtype = dataType;
        }
        
        /**
         * Constructor used to call methods that don't write anything, like
         * {@link #autoSizeColumns()}.
         *
         * @param sht - the sheet to perform actions on
         * @param rng - the range to perform actions on
         */
        protected RangeWriter(XSSFSheet sht, CellRange rng) {
            sheet = sht;
            range = rng;
        }
        
        /**
         * Auto sizes a range of columns in sheet.
         */
        protected void autoSizeColumns() {
            for (int c = range.colStart(), cn = range.colEnd(); c <= cn; c++) {
                sheet.autoSizeColumn(c);
            }
        }
        
        /**
         *
         */
        protected void writeRange() {
            switch (range.type()) {
                case CELL:
                    writeCellRange();
                    break;
                
                case ROW:
                    writeRowRange();
                    break;
                
                case COL:
                    writeColRange();
                    break;
                
                case ROW_COL:
                    writeRowColRange();
                    break;
                
                default:// we should never get here
                    break;  // make compiler happy
            }
        }
        
        /* writes a list containing a single value to a cell */
        private void writeCellRange() {
            XSSFRow rs =
                getRowOrReturnNewRowIfRowIsNull(sheet, range.rowStart());
            XSSFCell cell = getCellOrCreateNewIfNull(rs, range.colStart());
            
            setValueAndFormatCell(cell, out.get(0));
        }
        
        /* writes a list containing values to a range of cells in a row */
        private void writeRowRange() {
            XSSFRow rs =
                getRowOrReturnNewRowIfRowIsNull(sheet, range.rowStart());
            
            int colStart = range.colStart();
            for (int c = colStart, n = range.colEnd(); c <= n; c++) {
                XSSFCell cell = getCellOrCreateNewIfNull(rs, c);
                
                setValueAndFormatCell(cell, out.get(c - colStart));
            }
            
        }
        
        /* writes a list containing values to a range of cells in a col */
        private void writeColRange() {
            int column = range.colStart();
            int rowStart = range.rowStart();
            
            for (int r = rowStart, n = range.rowEnd(); r <= n; r++) {
                XSSFRow curRow = getRowOrReturnNewRowIfRowIsNull(sheet, r);
                XSSFCell cell = getCellOrCreateNewIfNull(curRow, column);
                
                setValueAndFormatCell(cell, out.get(r - rowStart));
            }
            
        }
        
        /* writes a list containing values to a range of cells in a row_col */
        private void writeRowColRange() {
            int rowStart = range.rowStart();
            int rowEnd = range.rowEnd();
            int colStart = range.colStart();
            int colEnd = range.colEnd();
            
            for (int r = rowStart, rn = rowEnd; r <= rn; r++) {
                XSSFRow curRow = getRowOrReturnNewRowIfRowIsNull(sheet, r);
                
                for (int c = colStart; c <= colEnd; c++) {
                    XSSFCell cell = getCellOrCreateNewIfNull(curRow, c);
                    
                    int outIndex = ((r - rowStart) * (colEnd - colStart + 1))
                                   + (c - colStart);
                    
                    setValueAndFormatCell(cell, out.get(outIndex));
                }
            }
            
        }
        
        /* */
        private void setValueAndFormatCell(XSSFCell cell, String value) {
            if (!value.equals("")) {
                switch (dtype) {
                    case INTEGER:
                        cell.setCellValue(Integer.parseInt(value));
                        cell.setCellType(CellType.NUMERIC);
                        break;
                    
                    case DOUBLE:
                        cell.setCellValue(Double.parseDouble(value));
                        cell.setCellType(CellType.NUMERIC);
                        break;
                    
                    default: // STRING
                        cell.setCellValue(value);
                }
            }
            cell.setCellStyle(cstyle);
        }
        
        /* */
        private XSSFRow
        getRowOrReturnNewRowIfRowIsNull(XSSFSheet sheet, int rowIndex)
        {
            XSSFRow row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            return row;
        }
        
        /* */
        private XSSFCell getCellOrCreateNewIfNull(XSSFRow row, int cellIndex) {
            XSSFCell cell = row.getCell(cellIndex);
            
            if (row.getCell(cellIndex) == null) {
                switch (dtype) {
                    case STRING:
                        cell = row.createCell(cellIndex, CellType.BLANK);
                        break;
                    
                    default:
                        cell = row.createCell(cellIndex, CellType.NUMERIC);
                        break;
                }
            }
            
            return cell;
        }
        
    }
    
    
    // Use the following to create a hyperlink in Excel via Apache POI
    // wb.getCreationHelper().createHyperlink(HyperlinkType.URL).;
}
