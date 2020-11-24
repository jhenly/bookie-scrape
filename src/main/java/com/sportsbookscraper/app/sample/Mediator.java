package com.sportsbookscraper.app.sample;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sportsbookscraper.app.config.RequiredSettingNotFoundException;
import com.sportsbookscraper.app.config.Settings;
import com.sportsbookscraper.app.config.Settings.SheetSettings;
import com.sportsbookscraper.app.config.SettingsFactory;
import com.sportsbookscraper.app.excel.SheetNotFoundException;
import com.sportsbookscraper.app.excel.WorkbookFactory;
import com.sportsbookscraper.app.excel.WorkbookReader;
import com.sportsbookscraper.app.scrape.Bookie;
import com.sportsbookscraper.app.scrape.Scraper;


/**
 * A class that mediates between all of the resources needed to perform site
 * scraping, Excel operations and JavaFX functionality.
 *
 * @author Jonathan Henly
 */
public class Mediator {
    
    
    // private constants
    private static final String DEF_PROPS_FILE = "config.properties";
    private static final String DEF_EXCEL_FILE = "src/test/resources/excel/test.xlsx";
    
    // private members
    private String excelFilePath;
    private List<String> sheetNames;
    private Scraper scraper;
    private Settings settings;
    
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
    
    public Mediator(Settings settings) {
        excelFilePath = settings.getExcelFilePath();
        
    }
    
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
            settings = SettingsFactory.loadSettings(propertiesPath,
                excelFilePath);
        } catch (RequiredSettingNotFoundException | IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("ExcelFilePath: " + excelFilePath);
        
        sheetNames = settings.getSheetNames();
        
        // create data store for each sheet
        createSheetDataForEachSheet();
        // add sheet settings to sheet data stores
        addSheetSettingsToEachSheetData();
        // add existing bookies to sheet data if keep order is true
        addExistingBookiesFromSheetToSheetData();
        
        scraper = new Scraper();
        for (String sheet : sheetNames) {
            
            // TODO remove this debugging conditional
            if (!sheet.equals("NFL")) {
                continue;
            }
            
            SheetData sd = SheetData.getSheetData(sheet);
            SheetSettings ss = sd.getSheetSettings();
            
            System.out.println("Scraping bookies for sheet: " + sheet);
            sd.setCurrentBookies(
                getCurrentBookies(scraper, ss.getScrapeUrl(), sheet));
        }
        
        // outputBookiesFromSheets(sheetData);
    }
    
    private void addExistingBookiesFromSheetToSheetData() {
        for (String sheetName : sheetNames) {
            SheetData sd = SheetData.getSheetData(sheetName);
            SheetSettings ss = sd.getSheetSettings();
            
            // if keep existing bookie order is false then continue
            if (!ss.keepOrder()) {
                continue;
            }
            
            int brow = ss.getTableRow();
            int bcol = ss.getBookieCol();
            
            try (WorkbookReader reader = WorkbookFactory
                .newWorkbookReader(excelFilePath)) {
                sd.setExistingBookies(
                    getBookiesFromSheet(reader, sheetName, brow, bcol));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            
            System.out.println("Added existing bookies for sheet " + sheetName);
        }
    }
    
    /* creates a sheet data store for each sheet in settings */
    private void createSheetDataForEachSheet() {
        for (String sheetName : sheetNames) {
            SheetData.createSheetData(sheetName);
        }
    }
    
    private void addSheetSettingsToEachSheetData() {
        for (String sheetName : sheetNames) {
            SheetData sd = SheetData.getSheetData(sheetName);
            sd.setSheetSettings(settings.getSheetSettings(sheetName));
        }
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
            
            for (Bookie bookie : cur.getCurrentBookies()) {
                System.out.print(bookie + "   ");
            }
        }
        
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
            bNames = reader.forOpenRowRange(bRow, bCol).in(sheetName).read();
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
