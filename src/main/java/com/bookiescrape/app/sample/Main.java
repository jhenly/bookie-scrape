package com.bookiescrape.app.sample;

import dorkbox.util.OS;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

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
 * <li>This class does NOT extend the {@link Application} class on
 * purpose, so that those issues can be worked around</li>
 * <li>This class launches the application via the singleton {@link FXLauncher}
 * class in this same package</li>
 *
 * @author Jonathan Henly
 * @see FXLauncher
 * @see javafx.application.Application
 */
public final class Main {
    
    /** */
    private static class Application extends FXLauncher {
        Application() { super(); }
        
        @Override
        public void start(final Stage stage) throws Exception {
            super.start(stage);
        }
        
        @Override
        protected void onApplicationMinimize(ActionEvent action) {}
    }
    
    private Main() {
        application = FXLauncher.getSingletonFXLauncher();
    }
    
    /**
     * Entry point of the application.
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        if (OS.isMacOsX() && OS.javaVersion <= 7) {
            System.setProperty("javafx.macosx.embedded", "true");
            java.awt.Toolkit.getDefaultToolkit();
        }
        
        application = FXLauncher.getSingletonFXLauncher();
        
        // make sure JNA jar is on the classpath!
        application.launch(FXLauncher.class);
    }
    
    
}
