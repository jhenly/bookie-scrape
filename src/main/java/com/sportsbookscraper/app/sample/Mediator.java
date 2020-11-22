package com.sportsbookscraper.app.sample;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sportsbookscraper.app.config.DSProperties;
import com.sportsbookscraper.app.config.DSProperties.SheetDataStore;
import com.sportsbookscraper.app.config.PropertiesFactory;
import com.sportsbookscraper.app.config.RequiredPropertyNotFoundException;
import com.sportsbookscraper.app.excel.CellRange;
import com.sportsbookscraper.app.excel.SheetNotFoundException;
import com.sportsbookscraper.app.excel.WorkbookFactory;
import com.sportsbookscraper.app.excel.WorkbookReader;
import com.sportsbookscraper.app.excel.WorkbookWriter;
import com.sportsbookscraper.app.scrape.Bookie;
import com.sportsbookscraper.app.scrape.Scraper;


/**
 * A class that mediates between all of the resources needed to perform site
 * scraping, Excel operations and JavaFX functionality.
 *
 * @author Jonathan Henly
 */
public class Mediator {
    
    // data store for sheets
    private static class SheetData {
        private String sheetName;
        private Map<String, Bookie> existingBookies;
        private List<Bookie> scrapedBookies;
        
        SheetData(String sheetName) { this.sheetName = sheetName; }
        
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
        
        public Map<String, Bookie> getExistingBookies() {
            return existingBookies;
        }
        
        public void setExistingBookies(Map<String, Bookie> map) {
            existingBookies = map;
        }
        
        public void setScrapedBookies(List<Bookie> scraped) {
            scrapedBookies = scraped;
        }
        
        public List<Bookie> getScrapedBookies() { return scrapedBookies; }
        
    } // class SheetData
    
    // private constants
    private static final String DEF_PROPS_FILE = "config.properties";
    private static final String DEF_EXCEL_FILE = "src/test/resources/excel/test.xlsx";
    
    // private members
    private String excelFilePath;
    private Map<String, SheetData> sheetData;
    private List<String> sheetNames;
    private WorkbookWriter writer;
    private Scraper scraper;
    private DSProperties props;
    
    /**
     * TODO DELETE THIS METHOD AFTER DEBBUGGING
     *
     * @param args
     *             - command line arguments
     */
    public static void main(String[] args) {
        Mediator mediator = new Mediator();
    }
    
    /**
     * TODO DELETE THIS DEBUGGIN CONSTRUCTOR
     * <p>
     * Constructor that uses default properties file and default Excel file.
     */
    public Mediator() { this(DEF_PROPS_FILE, DEF_EXCEL_FILE); }
    
    /**
     * Constructor that uses default properties file and default Excel file.
     *
     * @param propertiesPath
     *                       -
     * @param excelFilePath
     *                       -
     */
    public Mediator(String propertiesPath, String excelFilePath) {
        try {
            props = PropertiesFactory.loadProperties(propertiesPath,
                excelFilePath);
        } catch (RequiredPropertyNotFoundException | IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("ExcelFilePath: " + excelFilePath);
        
        sheetNames = props.getSheetNames();
        
        try (WorkbookReader reader = WorkbookFactory
            .newWorkbookReader(excelFilePath)) {
            sheetData = initSheetDataMap(reader, sheetNames);
            System.out.println("Loaded existing bookies");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        scraper = new Scraper();
        for (String sheet : sheetNames) {
            
            // TODO remove this debugging conditional
            if (!sheet.equals("NFL")) {
                continue;
            }
            
            SheetDataStore sds = props.getSheetProperties(sheet);
            
            System.out.println("Scraping bookies for sheet: " + sheet);
            SheetData curSheetData = sheetData.get(sheet);
            curSheetData.setScrapedBookies(
                getCurrentBookies(scraper, sds.getScrapeUrl(), sheet));
        }
        
        // outputBookiesFromSheets(sheetData);
    }
    
    
    private List<Bookie> getCurrentBookies(Scraper scraper, String url,
        String sheetName) {
        // TODO change this from debugging
        scraper.debugScrape(url, sheetName);
        return scraper.getBookies();
    }
    
    private void outputBookiesFromSheets(Map<String, SheetData> map) {
        for (String sheet : sheetNames) {
            SheetData cur = map.get(sheet);
            System.out
                .println("Printing existing bookies from sheet: " + sheet);
            
            Map<String, Bookie> bookies = cur.getExistingBookies();
            for (Entry<String, Bookie> bookie : bookies.entrySet()) {
                Bookie b = bookie.getValue();
                
                System.out.print(b + "   ");
            }
            
            System.out.println("Printing current bookies from sheet: " + sheet);
            
            for (Bookie bookie : cur.getScrapedBookies()) {
                System.out.print(bookie + "   ");
            }
        }
        
    }
    
    /* build sheet data map for later retrieval of existing bookies */
    private Map<String, SheetData> initSheetDataMap(WorkbookReader reader,
        List<String> sheetNames) {
        Map<String, SheetData> tmpSheetData = new HashMap<String, SheetData>(
            sheetNames.size());
        
        for (String curSheet : sheetNames) {
            // System.out.println("Reading from sheet: " + curSheet);
            
            SheetDataStore curProps = props.getSheetProperties(curSheet);
            
            int brow = curProps.getTableRow();
            int bcol = curProps.getBookieCol();
            Map<String, Bookie> existingBookies = getBookiesFromSheet(reader,
                curSheet, brow, bcol);
            
            // create sheet data for this sheet and add existing bookies
            SheetData newData = new SheetData(curSheet);
            newData.setExistingBookies(existingBookies);
            
            // load existing bookies into sheet data
            tmpSheetData.put(curSheet, newData);
        }
        
        return tmpSheetData;
    }
    
    /**
     * Retrieves any bookies that are already listed in the Excel sheet, as well
     * as their column index. This method is useful for retaining the order of
     * the bookies already listed in the excel sheet.
     * <p>
     * <b>Note:</b> rows and columns are not zero-indexed in Excel, but this
     * method treats them as if they are. Thus, {@code bRow} and {@code bCol}
     * should be set with this in mind.
     * <p>
     * <b>Note:</b> if the Excel workbook does not have a sheet with a name that
     * equals {@code sheetName}, then this method will return {@code null}.
     *
     * @param sheetName
     *                  - the name of the Excel sheet in the Excel workbook
     * @param bRow
     *                  - the Excel sheet row index where bookies start
     * @param bCol
     *                  - the Excel sheet column index where bookies start
     * @return a {@code Map<String, Bookie>} containing any bookies read from
     *         the Excel sheet, this method will return {@code null} if the
     *         Excel sheet does not exist
     * @throws IOException
     */
    private Map<String, Bookie> getBookiesFromSheet(WorkbookReader reader,
        String sheetName, int bRow, int bCol) {
        // read bookies names from Excel sheet
        List<String> bNames = null;
        try {
            bNames = reader.forRange(CellRange.rowRange(bRow, bCol, 11))
                .in(sheetName).read();
        } catch (SheetNotFoundException e) {
            // TODO either throw SNFException or return Collections.emptyMap()
            
            bNames = Collections.emptyList();
            
            e.printStackTrace();
        }
        
        // return map of bookies read from Excel
        return getMapFromBookieNames(bNames);
    }
    
    
    /* getBookiesFromSheet helper, will return an empty map if names is empty */
    private Map<String, Bookie> getMapFromBookieNames(List<String> names) {
        Map<String, Bookie> bookies = new HashMap<>(names.size());
        
        // put bookie with bookie's name and Excel column index into map
        for (int i = 0, n = names.size(); i < n; i++) {
            Bookie bookie = new Bookie(names.get(i).strip(), i);
            bookies.put(bookie.name(), bookie);
        }
        
        return bookies;
    }
    
    
}
