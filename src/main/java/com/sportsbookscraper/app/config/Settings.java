package com.sportsbookscraper.app.config;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;


/**
 * Interface that represents user settings.
 *
 * @author Jonathan Henly
 */
public interface Settings {
    
    /**
     * Gets the last time these settings were updated in milliseconds since the
     * Unix epoch.
     * 
     * @return the last time these settings were updated
     */
    long getLastSettingsUpdatedTime();
    
    /**
     * Gets the path to the Excel file as a string.
     * 
     * @return the Excel file path
     */
    String getExcelFilePath();
    
    /**
     * Gets the auto scrape time interval in minutes.
     * <p>
     * <b>Note:</b> a time interval of {@code 0} minutes can be taken to mean
     * multiple things:
     * <ul>
     * <li>do not auto scrape</li>
     * <li>never stop scraping</li>
     * </ul>
     * <p>
     * It is not up to implementations of this interface to distinguish between
     * the two.
     * 
     * @return the number of minutes to wait before scraping again
     */
    int getAutoScrapeInterval();
    
    /**
     * Gets the last scrape time in milliseconds.
     * 
     * @return the time since last scrape in milliseconds
     */
    long getLastScrapeTime();
    
    /**
     * Gets whether the application should launch when the computer starts or
     * not.
     * 
     * @return {@code true} if the application should launch when the computer
     *         starts, otherwise {@code false}
     */
    boolean launchOnStart();
    
    /**
     * Gets an unmodifiable list containing the sheet names loaded from the
     * user's settings.
     * 
     * @return returns a {@linkplain Collections#unmodifiableList(List)
     *         unmodifiable list} containing the Excel workbook's sheet names.
     */
    List<String> getSheetNames();
    
    /**
     * Gets the font name used by all sheets.
     * 
     * @return the name of the font
     */
    String getSheetFont();
    
    /**
     * Gets the font size used by all sheets.
     * 
     * @return the font size
     */
    int getSheetFontSize();
    
    /**
     * Gets whether Excel sheet columns should be sized to fit their content.
     * 
     * @return {@code true} if Excel column widths should fit their content,
     *         {@code false}
     */
    boolean colsAreSizedToFit();
    
    /**
     * Gets whether Excel sheet rows should be sized to fit their content.
     * 
     * @return {@code true} if Excel row heights should fit their content,
     *         otherwise {@code false}
     */
    boolean rowsAreSizedToFit();
    
    /**
     * @param index
     *              - the index of the sheet for which settings are to be
     *              retrieved
     * @return the sheet settings associated with the sheet index supplied
     */
    SheetSettings getSheetSettings(int index);
    
    /**
     * @param name
     *             - the name of the sheet for which settings are to be
     *             retrieved
     * @return the sheet settings associated with the sheet name supplied
     */
    public SheetSettings getSheetSettings(String name);
    
    /** TODO DEBUG METHOD -- DELETE THIS */
    public void listSettings(PrintStream out);
    
    /**
     * Class that wraps individual sheet properties with accessor methods.
     *
     * @author Jonathan Henly
     */
    interface SheetSettings {
        /**
         * Gets the sheet's name.
         * 
         * @return the sheet's name
         */
        String getSheetName();
        
        /**
         * Gets the sheet's url to scrape.
         * 
         * @return the sheet's url to scrape
         */
        String getScrapeUrl();
        
        /**
         * Gets the sheet's title.
         * 
         * @return the sheet's title
         */
        String getSheetTitle();
        
        /**
         * Gets the sheet's title row index.
         * 
         * @return the sheet's title row index
         */
        int getTitleRow();
        
        /**
         * Gets the sheet's title column index.
         * 
         * @return the sheet's title column index
         */
        int getTitleCol();
        
        /**
         * Gets the sheet's table row index.
         * 
         * @return the sheet's table row index
         */
        int getTableRow();
        
        /**
         * Get the sheet's column index where teams are listed.
         * 
         * @return the sheet's column index where teams are listed
         */
        int getTeamsCol();
        
        /**
         * Gets whether the sheet has an opener column or not.
         * 
         * @return {@code true} if the sheet has an opener column, otherwise
         *         {@code false}
         */
        boolean hasOpener();
        
        /**
         * Gets the sheet's opener column index, if it has one.
         * 
         * @return the sheet's opener column index
         */
        int getOpenerCol();
        
        /**
         * Gets the sheet's column index where bookies start.
         * 
         * @return the sheet's column index where bookies start
         */
        int getBookieCol();
        
        /**
         * Gets whether the existing order of bookies should be kept.
         * 
         * @return {@code true} if the existing order of bookies should be kept,
         *         otherwise {@code false}
         */
        boolean keepOrder();
    }
    
}
