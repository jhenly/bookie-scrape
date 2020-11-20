package com.sportsbookscraper.app.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ExcelDriver {
    
    private static final String ODDS_PATH = "./in/Odds.xlsx";
    
    public static void main(String[] args) throws IOException {
        /* ExcelFileReader oddsFile =
         * ExcelFactory.newExcelFileReader(ODDS_PATH);
         * 
         * Properties props = loadProps(Config.CONFIG_CLASS_PATH); String
         * allSheets = props.getProperty(Config.ALL_SHEETS); String nfl =
         * allSheets.split(",")[0]; */
        /* int bookieRow = Integer.parseInt(props.getProperty(nfl +
         * Config.SHEET_BOOKIE_ROW)); int bookieCol =
         * Integer.parseInt(props.getProperty(nfl + Config.SHEET_BOOKIE_COL));
         * 
         * Map<String, Bookie> sheetBookies = oddsFile.getBookiesFromSheet(nfl,
         * bookieRow, bookieCol);
         * 
         * for(Map.Entry<String, Bookie> entry : sheetBookies.entrySet()) {
         * System.out.println(entry.getValue().index() + " " + entry.getKey());
         * } */
        
        System.out.println("IT WORKED!");
    }
    
    public static Properties loadProps(String filename) {
        Properties props = new Properties();
        
        try (InputStream input
            = ExcelDriver.class.getClassLoader().getResourceAsStream(filename))
        {
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
            }
            
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return props;
    }
}
