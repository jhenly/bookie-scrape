package com.bookiescrape.app.sample;

import java.io.IOException;
import java.net.URL;

import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Separator;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.Desktop;


/**
 *
 * @author Jonathan Henly
 */
public class SystemTrayController {
    private static final URL ICON_TRAY_WHITE_32 = Main.class.getResource("icon-tray-white-32.png");
    private static final String BOOKIE_SCRAPE_URL = "https://github.com/jhenly/bookie-scrape";

    private SystemTray systemTray;
    private boolean supported;
    private MenuItem about;
    private MenuItem runScraper;
    private MenuItem dashboard;
    private MenuItem settings;
    private MenuItem logs;
    private MenuItem quit;
    
    /**
     * Creates a system tray controller.
     * @throws UnsupportedOperationException if the system tray is not supported
     */
    public SystemTrayController() throws UnsupportedOperationException {
        SystemTray.DEBUG = true;
        supported = true;
        systemTray = SystemTray.get();
        if (systemTray == null) {
            supported = false;
            throw new UnsupportedOperationException("No tray available!");
        }
        
        systemTray.setTooltip(Main.APP_NAME);
        systemTray.setImage(ICON_TRAY_WHITE_32);
        // sysTray.setStatus("idle");
        
        
        about = createAboutMenuItem();
        runScraper = createRunScraperMenuItem();
        dashboard = createDashboardMenuItem();
        settings = createSettingsMenuItem();
        logs = createLogsMenuItem();
        quit = createQuitMenuItem();
        
        createSystemTrayMenu(systemTray.getMenu());
    }
    
    /**
     * Shuts down the system tray if it's supported, otherwise calling this
     * method is a no-op.
     */
    public void shutdown() { if (isSupported()) systemTray.shutdown(); }
    
    /**
     * Gets whether the system tray is supported or not.
     * @return {@code true} if the system tray is supported, otherwise
     *         {@code false}
     */
    public boolean isSupported() { return supported; }
    
    private void createSystemTrayMenu(Menu menu) {
        menu.add(about);
        menu.add(runScraper);
        menu.add(new Separator());
        menu.add(dashboard);
        menu.add(settings);
        menu.add(logs);
        menu.add(new Separator());
        menu.add(quit);
    }

    private MenuItem createAboutMenuItem() {
        return new MenuItem("About", e -> {
            System.out.println("About was selected");
            try {
                Desktop.browseURL(BOOKIE_SCRAPE_URL);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
    
    private MenuItem createRunScraperMenuItem() {
        return new MenuItem("Run Scraper", e -> {
            System.out.println("Run Scraper was selected");
        });
    }
    
    private MenuItem createDashboardMenuItem() {
        return new MenuItem("Dashboard", e -> {
            System.out.println("Dashboard was selected");
        });
    }
    
    private MenuItem createSettingsMenuItem() {
        return new MenuItem("Settings", e -> {
            System.out.println("Settings was selected");
        });
    }
    
    private MenuItem createLogsMenuItem() {
        return new MenuItem("View Logs", e -> {
            System.out.println("View Logs was selected");
        });
    }
    
    private MenuItem createQuitMenuItem() {
        return new MenuItem("Quit", e -> {
            System.out.println("Quit was selected");
            systemTray.shutdown();
        });
    }
    
}
