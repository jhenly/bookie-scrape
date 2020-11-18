package com.sportsbookscraper.app.config;

import java.util.Collections;
import java.util.List;


/**
 * Skeleton base class for accessing properties specific to this project.
 *
 * @author Jonathan Henly
 */
public abstract class AbstractProperties implements DSProperties {
    
    /* end of constants */
    
    protected String font;
    protected int fontSize;
    protected boolean colSizeToFit;
    protected boolean rowSizeToFit;


    // To Allow this class to be subclassed.
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
        public String getScrapeUrl() { return scrapeUrl; }

        /**
         * @return the sheet's title
         */
        @Override
        public String getSheetTitle() { return sheetTitle; }

        /**
         * @return the sheet's title row index
         */
        @Override
        public int getTitleRow() { return titleRow; }

        /**
         * @return the sheet's title column index
         */
        @Override
        public int getTitleCol() { return titleCol; }

        /**
         * @return the sheet's table row index
         */
        @Override
        public int getTableRow() { return tableRow; }

        /**
         * @return the sheet's column index where team are listed
         */
        @Override
        public int getTeamsCol() { return teamsCol; }

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
        public int getOpenerCol() { return openerCol; }

        /**
         * @return the sheet's column index where bookies start
         */
        @Override
        public int getBookieCol() { return bookieCol; }

    } // private static class SheetProperties
    
}
