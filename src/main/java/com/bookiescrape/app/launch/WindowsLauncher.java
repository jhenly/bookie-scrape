package com.bookiescrape.app.launch;

import com.bookiescrape.app.fx.control.ControllerMediator;
import com.bookiescrape.app.sample.Main;
import com.bookiescrape.app.sample.SystemTrayController;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * The Windows operating system application launcher.
 * <p>
 * This launcher supports the system tray, but checking for system tray support
 * before use is wise.
 * 
 * @author Jonathan Henly
 */
public class WindowsLauncher extends ApplicationLauncher {
    private static SystemTrayController trayController;
    private static boolean supportsTray = true;
    
    /**
     * Do not create instances of this class!
     * <p>
     * This constructor is needed by and only to be used by JavaFX.
     */
    public WindowsLauncher() {}
    
    @Override
    public boolean systemTrayIsSupported() { return supportsTray; }
    
    @Override
    public SystemTrayController getSystemTrayController() { return trayController; }
    
    @Override
    protected void implLaunch() {
        WindowsLauncher.launch(WindowsLauncher.class);
    }
    
    @Override
    protected void setUpSystemTray() {
        if (trayController != null) { return; } // this should never happen
        
        // run on Swing's EDT, not on JavaFX's EDT
        javax.swing.SwingUtilities.invokeLater(() -> {
            
            try {
                trayController = new SystemTrayController(Main.ICON_TRAY_WHITE_32);
            } catch (UnsupportedOperationException uoe) {
                supportsTray = false;
                
                // TODO log this exception and launch app without system tray
                uoe.printStackTrace();
            }
            
            // instruct JavaFX not to exit when last application window closes
            
            trayController.setToolTip(Main.APP_NAME);
            trayController.setAboutBrowserUrl(Main.BOOKIE_SCRAPE_URL);
            
            Platform.runLater(() -> {
                Platform.setImplicitExit(false);
                
                addSystemTrayMenuItemListeners(getControllerMediator());
            });
        });
        
    }
    
    /** Helper that sets system tray's menu item's action listeners. */
    private void addSystemTrayMenuItemListeners(ControllerMediator cmed) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            
            trayController.setOnDashboardSelected(action -> {
                Platform.runLater(() -> cmed.requestShowDashboardView());
            });
            trayController.setOnSettingsSelected(action -> {
                Platform.runLater(() -> cmed.requestShowSettingsView());
            });
            trayController.setOnViewLogsSelected(action -> {
                Platform.runLater(() -> cmed.requestShowViewLogsView());
            });
            
        });
    }
    
    @Override
    public void stop() throws Exception {
        // if (trayController != null) {
        // trayController.shutdown();
        // trayController = null;
        // }
    }
    
    @Override
    protected EventHandler<ActionEvent> getApplicationCloseButtonHandler() {
        return event -> { System.out.println("WindowsLauncher: close button event called"); getPrimaryStage().hide(); };
    }
    
}
