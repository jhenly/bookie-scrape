package com.sportsbookscraper.app.config;

import java.util.List;

/**
 * Skeleton base class for accessing properties specific to this project.
 *
 * @author Jonathan Henly
 */
public abstract class AbstractSettings implements Settings {
    
    /* end of constants */
    protected long lastUpdatedTime;
    protected String excelFilePath;
    protected int scrapeInterval;
    protected boolean launchOnStart;
    protected long lastScrape;
    protected String font;
    protected int fontSize;
    protected boolean colSizeToFit;
    protected boolean rowSizeToFit;
    
    // individual sheet settings holder
    protected List<SheetSettings> sheetSettings;
    
    // to allow this class to be subclassed.
    protected AbstractSettings() {}
    
    protected AbstractSettings(AbstractSettings that) {
        this.lastUpdatedTime = that.lastUpdatedTime;
        this.excelFilePath = that.excelFilePath;
        this.scrapeInterval = that.scrapeInterval;
        this.launchOnStart = that.launchOnStart;
        this.lastScrape = that.lastScrape;
        this.font = that.font;
        this.fontSize = that.fontSize;
        this.colSizeToFit = that.colSizeToFit;
        this.rowSizeToFit = that.rowSizeToFit;
        this.sheetSettings = that.sheetSettings;
    }
    
    @Override
    public long getLastSettingsUpdatedTime() { return lastUpdatedTime; }
    
    @Override
    public boolean launchOnStart() { return launchOnStart; }
    
    @Override
    public int getAutoScrapeInterval() { return scrapeInterval; }
    
    @Override
    public long getLastScrapeTime() { return lastScrape; }
    
    /**
     * @return the path to the Excel file, if set in {@code config.properties}
     */
    @Override
    public String getExcelFilePath() { return excelFilePath; }
    
    @Override
    public String getSheetFont() { return font; }
    
    @Override
    public int getSheetFontSize() { return fontSize; }
    
    @Override
    public boolean colsAreSizedToFit() { return colSizeToFit; }
    
    @Override
    public boolean rowsAreSizedToFit() { return rowSizeToFit; }
    
    @Override
    public SheetSettings getSheetSettings(int index) {
        return sheetSettings.get(index);
    }
    
    @Override
    public SheetSettings getSheetSettings(String sheetName) {
        for (SheetSettings ss : sheetSettings) {
            if (ss.getSheetName().equals(sheetName)) { return ss; }
        }
        
        return null;
    }
    
    /**
     * Subclasses must implement this method to build each sheet's
     * 
     * @param sheetName
     * @return
     */
    protected abstract SheetSettings buildSheetSettings(String sheetName);
    
    /**
     * Class that wraps individual sheet properties with accessor methods.
     *
     * @author Jonathan Henly
     */
    protected static class AbstractSheetSettings
        implements Settings.SheetSettings {
        /** sheet's name */
        protected String sheetName;
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
        /** whether or not the existing order of bookies should be kept */
        protected boolean keepExisting;
        
        
        // for use by sub classes
        protected AbstractSheetSettings() {}
        
        /**
         * @return the sheet's name
         */
        @Override
        public String getSheetName() { return sheetName; }
        
        
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
        public boolean hasOpener() { return opener; }
        
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
        
        /**
         * Gets whether the existing order of bookies should be kept.
         * 
         * @return {@code true} if the existing order of bookies should be kept,
         *         otherwise {@code false}
         */
        @Override
        public boolean keepOrder() { return keepExisting; }
        
    } // private static class SheetProperties
    
}
