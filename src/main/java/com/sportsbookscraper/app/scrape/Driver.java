package com.sportsbookscraper.app.scrape;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Driver {
    
    private static final String MONEY_LINE = "https://classic.sportsbookreview.com/betting-odds/nfl-football/money-line/";
    
    public static void main(String[] args) throws IOException {
        Scraper scraper = new Scraper();
        scraper.scrape(MONEY_LINE);
        
        System.out.println("*** Showing all Bookies list ***\n");
        
        /* for (Bookie bookie : scraper.getBookies()) {
         * System.out.println(bookie.getName() + " " + bookie.getBookieIndex());
         * } */
        
    }
    
    public static void writeToFile(String filename, String content)
        throws IOException {
        BufferedWriter bw = Files
            .newBufferedWriter(new File(filename).toPath());
        
        bw.write(content);
        bw.close();
    }
}
