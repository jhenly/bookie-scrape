package com.bookiescrape.app.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.HorizontalAlignment;


public class ExcelDriver {
    
    private static final String ODDS_PATH = "src/test/resources/excel/Odds.xlsx";
    
    public static void main(String[] args) throws IOException {
        File file = new File("./");
        System.out.println(file.getAbsolutePath());
        
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
        
        
        WorkbookReader reader = WorkbookFactory.newWorkbookReader(ODDS_PATH);
        CellRange t = CellRange.cell(0, 0);
        List<String> title = reader.forRange(t).in("NFL").read();
        System.out.println("TITLE:  ");
        for (String cell : title) {
            System.out.print(cell + " ");
        }
        
        CellRange r = CellRange.rowRange(1, 0, 11);
        List<String> bookies = reader.forRange(r).in("NFL").read();
        System.out.print("HEADERS:  ");
        for (String cell : bookies) {
            System.out.print(cell + " ");
        }
        
        CellRange c = CellRange.colRange(0, 2, 25);
        List<String> teams = reader.forRange(c).in("NFL").read();
        System.out.print("\nTEAMS:  ");
        for (String cell : teams) {
            System.out.print(cell + " ");
        }
        
        CellRange d = CellRange.range(2, 25, 1, 11);
        List<String> data = reader.forRange(d).in("NFL").read();
        System.out.print("\nDATA:\n");
        for (int i = 0; i < data.size(); i++) {
            if (i % 11 == 0) {
                System.out.println();
            }
            System.out.printf("%-5s", data.get(i));
            
        }
        reader.close();
        
        WorkbookWriter writer = WorkbookFactory.newWorkbookWriter(ODDS_PATH);
        
        System.out.println("\n\nWriting TITLE: ");
        writer.forRange(t).in("Test").write(title);
        
        System.out.println("Writing HEADERS: ");
        writer.forRange(r).in("Test").write(bookies);
        
        System.out.println("Writing TEAMS: ");
        writer.forRange(c).in("Test").write(teams);
        
        System.out.println("Writing DATA: ");
        writer.forRange(d).in("Test").hAlign(HorizontalAlignment.RIGHT)
            .withFormat("0.00").dataIsDouble().write(data);
        
        writer.autoSizeColumns("Test", 11);
        
        writer.saveChangesToWorkbook();
        
        writer.close();
        System.out.println("\nIT WORKED!");
    }
    
    public static Properties loadProps(String filename) {
        Properties props = new Properties();
        
        try (InputStream input = ExcelDriver.class.getClassLoader()
            .getResourceAsStream(filename)) {
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
