package com.sportsbookscraper.app.config;

import java.util.Collections;
import java.util.List;


/**
 * Interface that represents user settings.
 *
 * @author Jonathan Henly
 */
public interface Settings {
    /**
     * @return the path to the Excel file
     */
    String getExcelFilePath();
    
    /**
     * @return returns an {@linkplain Collections#unmodifiableList(List)
     *         unmodifiable list} containing the Excel workbook's sheet names.
     */
    List<String> getSheetNames();
    
    
    /**
     * @return font name used by all sheets
     */
    String getSheetFont();
    
    /**
     * @return font size used by all sheets
     */
    int getSheetFontSize();
    
    /**
     * @return {@code true} if Excel column widths should fit their content,
     *         {@code false}
     */
    boolean getColSizeToFit();
    
    /**
     * @return {@code true} if Excel row heights should fit their content,
     *         otherwise {@code false}
     */
    boolean getRowSizeToFit();
    
    /**
     * @param index
     *              - which sheet's properties to retrieve
     * @return the sheet properties associated with sheet index supplied
     */
    SheetSettings getSheetProperties(int index);
    
    /**
     * @param name
     *             - the name of the sheet's properties to retrieve
     * @return the sheet properties associated with sheet name supplied
     */
    public SheetSettings getSheetProperties(String name);
    
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
