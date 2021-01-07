package com.bookiescrape.app.sample;

import java.net.URL;


/**
 * Bookie Scrape's main overarching application class.
 * <p>
 * Main does not only contain the entry point method of the application, it also
 * contains the application to system-tray delegating logic, which is somewhat
 * the backbone of the Bookie Scrape application.
 * <p>
 * <b>Notes:</b>
 * <ul>
 * <li>The Bookie Scrape application needs JavaFX to run</li>
 * <li>JavaFX on Mac (Java7) has many bugs when also used with AWT</li>
 * <li>This class does NOT extend the {@link BookieScrape} class on
 * purpose, so that those issues can be worked around</li>
 * <li>This class launches the application via the singleton {@link ApplicationHandler}
 * class in this same package</li>
 *
 * @author Jonathan Henly
 * @see ApplicationHandler
 * @see javafx.application.Application
 */
public final class Main {
    /** The name of the application. */
    public static final String APP_NAME = "Bookie Scrape";
    public static final String APP_DIR_NAME = "BookieScrape";
    public static final String BOOKIE_SCRAPE_URL = "https://github.com/jhenly/bookie-scrape#bookie-scrape";
    
    public static final URL ICON_TRAY_WHITE_32 = Main.class.getResource("icon-tray-white-32.png");
    private static final String ICON_APP_BLUE_NAME = "icon-app-blue-";
    public static final String[] ICON_APP_BLUE = createIconAppBlueArray();
    
    /** Helper that creates ICON_APP_BLUE resource strings array. */
    private static String[] createIconAppBlueArray() {
        String i256 = createIconAppBlueString("256");
        String i128 = createIconAppBlueString("128");
        String i64 = createIconAppBlueString("64");
        String i32 = createIconAppBlueString("32");
        String i16 = createIconAppBlueString("16");
        
        String[] ret = { i256, i128, i64, i32, i16 };
        
        return ret;
    }
    
    /** Helper that creates ICON_APP_BLUE resource strings. */
    private static String createIconAppBlueString(String dimension) {
        return ICON_APP_BLUE_NAME + dimension + ".png";
    }
    
    /** */
    private Main() {}
    
    
    /**
     * Entry point of the application.
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        ApplicationBooter.boot();
    }
    
} // class Main
