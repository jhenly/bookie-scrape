package com.bookiescrape.app.sample;

import java.net.URL;

import com.bookiescrape.app.launch.ApplicationLauncher;


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
    public static final String BOOKIE_SCRAPE_URL = "https://github.com/jhenly/bookie-scrape#bookie-scrape";
    
    public static final URL ICON_TRAY_WHITE_32 = Main.class.getResource("icon-tray-white-32.png");
    
    
    /** */
    private Main() {}
    
    /**
     * Entry point of the application.
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        ApplicationLauncher.launchApplication();
    }
    
} // class Main
