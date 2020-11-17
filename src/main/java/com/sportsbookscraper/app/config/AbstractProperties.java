package com.sportsbookscraper.app.config;

import java.util.Collections;
import java.util.List;


/**
 * Skeleton base class for accessing properties specific to this project.
 *
 * @author Jonathan Henly
 */
public abstract class AbstractProperties implements DSProperties {

    /* - keys of properties shared by all sheets - */
    protected static final String EXCEL_FILE_PATH = "excel.file.path";
    protected static final String ALL_SHEETS = "all.sheets";
    protected static final String SHEET_FONT = ALL_SHEETS + ".font";
    protected static final String SHEET_FONT_SIZE = SHEET_FONT + ".size";
    protected static final String COLS_SIZETOFIT
        = ALL_SHEETS + ".cols.sizetofit";
    protected static final String ROWS_SIZETOFIT
        = ALL_SHEETS + ".rows.sizetofit";
    
    /* - individual sheet property keys - */
    protected static final String SCRAPE_URL = ".scrape.url";
    protected static final String SHEET_TITLE = ".sheet.title";
    protected static final String TITLE_ROW = SHEET_TITLE + ".row";
    protected static final String TITLE_COL = SHEET_TITLE + ".row";
    protected static final String SHEET_TABLE = ".sheet.table";
    protected static final String TEAMS_COL = SHEET_TABLE + ".teams.col";
    protected static final String OPENER = SHEET_TABLE + ".opener";
    protected static final String OPENER_COL = SHEET_TABLE + ".opener.col";
    protected static final String BOOKIE_COL = SHEET_TABLE + ".bookie.col";
    
    /* - default values of properties shared by all sheets - */
    protected static final String DEF_FONT = "Calibri";
    protected static final int DEF_FONT_SIZE = 11;
    protected static final boolean DEF_SIZE_TO_FIT = true;
    
    /* - default values of individual sheet properties - */
    protected static final int DEF_TITLE_ROW_COL = 0;
    protected static final int DEF_TABLE_ROW = 1;
    protected static final int DEF_TEAMS_COL = 0;
    protected static final boolean DEF_HAS_OPENER = true;
    protected static final int DEF_OPENER_COL = 1;
    protected static final int DEF_BOOKIE_COL = 2;


    /* end of constants */

    protected String font;
    protected int fontSize;
    protected boolean colSizeToFit;
    protected boolean rowSizeToFit;

    protected AbstractProperties() {}

    /**
     * @return the path to the Excel file
     */
    @Override
    public abstract String getExcelFilePath();

    /**
     * @return returns an {@linkplain Collections#unmodifiableList(List)
     *         unmodifiable list} containing the Excel workbook's sheet names.
     */
    @Override
    public abstract List<String> getSheetNames();
    
    /**
     * @return font name used by all sheets
     */
    @Override
    public abstract String getSheetFont();
    
    /**
     * @return font size used by all sheets
     */
    @Override
    public abstract int getSheetFontSize();
    
    /**
     * @return {@code true} if Excel column widths should fit their content,
     *         {@code false}
     */
    @Override
    public abstract boolean getColSizeToFit();
    
    /**
     * @return {@code true} if Excel row heights should fit their content,
     *         otherwise {@code false}
     */
    @Override
    public abstract boolean getRowSizeToFit();
    
    /**
     * @param index - which sheet's properties to retrieve
     * @return the sheet properties associated with sheet index supplied
     */
    @Override
    public abstract SheetDataStore getSheetProperties(int index);


    /**
     * Class that wraps individual sheet properties with accessor methods.
     *
     * @author Jonathan Henly
     */
    protected static class SheetProperties
    implements DSProperties.SheetDataStore
    {
        /** sheet's url to scrape */
        protected String scrapeUrl;
        /** the sheet's title */
        protected String sheetTitle;
        /** the sheet's row index to put the sheet's title */
        protected int titleRow;
        /** the sheet's column index to put the sheet's title */
        protected int titleCol;
        /** the sheet's row index to put the table headers */
        protected int tableRow;
        /** the sheet's column index to put team names in */
        protected int teamsCol;
        /** whether or not this sheet has an opener */
        protected boolean opener;
        /** the sheet's column index to put opener data, if opener is true */
        protected int openerCol;
        /** the sheet's column index to start listing bookie names */
        protected int bookieCol;
        
        // sole purpose is for use by sub classes
        protected SheetProperties() {}
        
        /**
         * @return the sheet's url to scrape
         */
        @Override
        public String getScrapeUrl() {
            return scrapeUrl;
        }
        
        /**
         * @return the sheet's title
         */
        @Override
        public String getSheetTitle() {
            return sheetTitle;
        }
        
        /**
         * @return the sheet's title row index
         */
        @Override
        public int getTitleRow() {
            return titleRow;
        }
        
        /**
         * @return the sheet's title column index
         */
        @Override
        public int getTitleCol() {
            return titleCol;
        }
        
        /**
         * @return the sheet's table row index
         */
        @Override
        public int getTableRow() {
            return tableRow;
        }
        
        /**
         * @return the sheet's column index where team are listed
         */
        @Override
        public int getTeamsCol() {
            return teamsCol;
        }
        
        /**
         * @return {@code true} if the sheet has an opener column, otherwise
         *         {@code false}
         */
        @Override
        public boolean hasOpener() {
            return opener;
        }
        
        /**
         * @return the sheet's opener column index
         */
        @Override
        public int getOpenerCol() {
            return openerCol;
        }
        
        /**
         * @return the sheet's column index where bookies start
         */
        @Override
        public int getBookieCol() {
            return bookieCol;
        }
        
    } // private static class SheetProperties

}
