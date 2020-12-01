package com.bookiescrape.app.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bookiescrape.app.config.Settings.SheetSettings;
import com.bookiescrape.app.scrape.Bookie;
import com.bookiescrape.app.scrape.DateGroup;

// data store for sheets
class SheetData {
    private String sheetName;
    private Map<String, Bookie> existingBookies;
    private List<Bookie> scrapedBookies;
    private List<DateGroup> scrapedMatches;
    private SheetSettings sheetSettings;
    
    private SheetData(String sheetName) { this.sheetName = sheetName; }
    
    @Override
    public int hashCode() { return sheetName.hashCode(); }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        
        if (!(obj instanceof SheetData))
            return false;
        
        SheetData that = (SheetData) obj;
        return this.sheetName.equals(that.sheetName);
    }
    
    /**
     * 
     * @return
     */
    public Map<String, Bookie> getExistingBookies() {
        return existingBookies;
    }
    
    void setExistingBookies(Map<String, Bookie> map) { existingBookies = map; }
    
    /**
     * 
     * @return
     */
    public List<Bookie> getCurrentBookies() { return scrapedBookies; }
    
    void setCurrentBookies(List<Bookie> scraped) { scrapedBookies = scraped; }
    
    /**
     * 
     * @return
     */
    public List<DateGroup> getScrapedMatches() { return scrapedMatches; }
    
    void setScrapedMatches(List<DateGroup> matches) {
        scrapedMatches = matches;
    }
    
    /**
     * 
     * @return
     */
    public SheetSettings getSheetSettings() { return sheetSettings; }
    
    void setSheetSettings(SheetSettings settings) { sheetSettings = settings; }
    
    // map to keep each sheet's data
    private static Map<String, SheetData> sheetData;
    
    /**
     * 
     * @param sheetName
     */
    public static void createSheetData(String sheetName) {
        if (sheetData == null) {
            sheetData = new HashMap<>();
        }
        
        sheetData.put(sheetName, new SheetData(sheetName));
    }
    
    /**
     * 
     * @param sheetName
     * @return
     */
    public static SheetData getSheetData(String sheetName) {
        return sheetData.get(sheetName);
    }
    
} // class SheetData
