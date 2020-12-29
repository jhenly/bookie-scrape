package com.bookiescrape.app.sample;

import dorkbox.util.JavaFX;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private static BookieScrape bookieScrape;

    /** */
    private Main() {}
    
    /**
     * Entry point of the application.
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
// TASK following code is used with SystemTray which does not currently work
//      on Linux/Unix
//
//        if (OS.isMacOsX() && OS.javaVersion <= 7) {
//            System.setProperty("javafx.macosx.embedded", "true");
//            java.awt.Toolkit.getDefaultToolkit();
//        }
//
//        SystemTray.ENABLE_ROOT_CHECK = false;
//        SystemTray.ENABLE_SHUTDOWN_HOOK = true;
//        SystemTray.FORCE_GTK2 = true;
//        SystemTray.PREFER_GTK3 = false;
//        SystemTray.FORCE_TRAY_TYPE = SystemTray.TrayType.Swing;


        bookieScrape = new BookieScrape();
        
        // make sure JNA jar is on the classpath!
        bookieScrape.launch(BookieScrape.class);
    }
    
    // private ActionListener
    /** */
    public static class BookieScrape extends ApplicationHandler {
        private static SystemTrayController systemTrayController;

        public BookieScrape() {}

        @Override
        public void start(final Stage stage) throws Exception {
            super.start(stage);
        }

        @Override
        public void stop() throws Exception {
            super.stop();
        }
        
//        @Override
//        public void stop() { super.stop(); }

        @Override
        protected EventHandler<ActionEvent> getApplicationMinimizeHandler() {
            if (!systemTrayController.isSupported()) return action -> getPrimaryStage().setIconified(true);
            
            return null;
        }

        @Override
        protected EventHandler<ActionEvent> getApplicationCloseHandler() {
            if (!systemTrayController.isSupported()) return action -> { exit(); };

            return null;
        }
        
        private void exit() {
            if (!JavaFX.isEventThread())
                JavaFX.dispatch(() -> {
                    getPrimaryStage().hide(); // must do this BEFORE Platform.exit() otherwise odd errors show up
                    Platform.exit();  // necessary to close javaFx
                });
            else {
                getPrimaryStage().hide(); // must do this BEFORE Platform.exit() otherwise odd errors show up
                Platform.exit();  // necessary to close javaFx
            }
        }
        
    } // class BookieScrape


} // class Main
