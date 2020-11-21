package com.sportsbookscraper.app.config;

import java.util.Collections;
import java.util.List;


/**
 * Interface with methods to retrieve properties from a data store.
 *
 * @author Jonathan Henly
 */
public interface DSProperties {
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
    SheetDataStore getSheetProperties(int index);
    
    /**
     * @param name
     *             - the name of the sheet's properties to retrieve
     * @return the sheet properties associated with sheet name supplied
     */
    public SheetDataStore getSheetProperties(String name);
    
    /**
     * Class that wraps individual sheet properties with accessor methods.
     *
     * @author Jonathan Henly
     */
    interface SheetDataStore {
        /**
         * @return the sheet's name
         */
        String getSheetName();
        
        /**
         * @return the sheet's url to scrape
         */
        String getScrapeUrl();
        
        /**
         * @return the sheet's title
         */
        String getSheetTitle();
        
        /**
         * @return the sheet's title row index
         */
        int getTitleRow();
        
        /**
         * @return the sheet's title column index
         */
        int getTitleCol();
        
        /**
         * @return the sheet's table row index
         */
        int getTableRow();
        
        /**
         * @return the sheet's column index where team are listed
         */
        int getTeamsCol();
        
        /**
         * @return {@code true} if the sheet has an opener column, otherwise
         *         {@code false}
         */
        boolean hasOpener();
        
        /**
         * @return the sheet's opener column index
         */
        int getOpenerCol();
        
        /**
         * @return the sheet's column index where bookies start
         */
        int getBookieCol();
    }
    
}
