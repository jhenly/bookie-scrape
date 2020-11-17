package com.sportsbookscraper.app.sample;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sportsbookscraper.app.config.DSProperties;
import com.sportsbookscraper.app.config.PropertiesFactory;
import com.sportsbookscraper.app.config.RequiredPropertyNotFoundException;
import com.sportsbookscraper.app.excel.ExcelFactory;
import com.sportsbookscraper.app.excel.ExcelFileReader;
import com.sportsbookscraper.app.excel.ExcelFileWriter;
import com.sportsbookscraper.app.excel.SheetNotFoundException;
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
    private static final String DEF_EXCEL_FILE = "excel/Odds.xlsx";
    
    // private members
    private String excelFilePath;
    private Map<String, Bookie> existingBookies;
    private ExcelFileWriter writer;
    private Scraper scraper;
    private DSProperties props;
    
    /**
     * TODO DELETE THIS METHOD AFTER DEBBUGGING
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        Mediator mediator = new Mediator();
    }

    /**
     * TODO DELETE THIS DEBUGGIN CONSTRUCTOR
     * <p>
     * Constructor that uses default properties file and default Excel file.
     */
    public Mediator() {
        this(DEF_PROPS_FILE, DEF_EXCEL_FILE);
    }

    /**
     * Constructor that uses default properties file and default Excel file.
     *
     * @param propertiesPath -
     * @param excelFilePath -
     */
    public Mediator(String propertiesPath, String excelFilePath) {
        try {
            props = PropertiesFactory.loadProperties(propertiesPath,
                excelFilePath);
        } catch (RequiredPropertyNotFoundException | IOException e) {
            e.printStackTrace();
        }
        
        for (String name : props.getSheetNames()) {
            System.out.println("Reading from sheet: " + name);
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
     * @param sheetName - the name of the Excel sheet in the Excel workbook
     * @param bRow - the Excel sheet row index where bookies start
     * @param bCol - the Excel sheet column index where bookies start
     * @return a {@code Map<String, Bookie>} containing any bookies read from
     *         the Excel sheet, this method will return {@code null} if the
     *         Excel sheet does not exist
     * @throws IOException
     */
    private Map<String, Bookie> getBookiesFromSheet(String sheetName, int bRow,
                                                    int bCol)
    {
        // read bookies names from Excel sheet
        List<String> bNames = null;
        try (ExcelFileReader excelReader
            = ExcelFactory.newExcelFileReader(excelFilePath);)
        {
            bNames = excelReader.readStringsInRow(sheetName, 1, bRow, bCol);
        } catch (SheetNotFoundException e) {
            // TODO either throw SNFException or return Collections.emptyMap()
            
            bNames = Collections.emptyList();
            
            e.printStackTrace();
        } catch (IOException e1) { // only thrown on excelReader.close()
            // can't do anything about this so just swallow the exception
            // e1.printStackTrace();
        }
        
        // return map of bookies read from Excel
        return getMapFromBookieNames(bNames);
    }
    
    
    /* getBookiesFromSheet helper, will return an empty map if names is empty */
    private Map<String, Bookie> getMapFromBookieNames(List<String> names) {
        Map<String, Bookie> bookies = new HashMap<>(names.size());
        
        // put bookie with bookie's name and Excel column index into map
        for (int i = 0, n = names.size(); i < n; i++) {
            Bookie bookie = new Bookie(names.get(i), i);
            bookies.put(bookie.name(), bookie);
        }
        
        return bookies;
    }
    
    
}
