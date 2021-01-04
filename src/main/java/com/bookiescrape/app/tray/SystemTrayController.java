package com.bookiescrape.app.tray;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Taskbar;
import java.awt.Taskbar.Feature;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.desktop.AppForegroundEvent;
import java.awt.desktop.AppForegroundListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import com.bookiescrape.app.sample.Main;

import javafx.application.Platform;


/**
 * Handles the creation and layout of the system tray icon and system tray
 * application popup menu.
 * 
 * @author Jonathan Henly
 * @see SystemTray
 */
public class SystemTrayController {
    
    private static URI aboutUri;
    private static SystemTray tray;
    private static TrayIcon trayIcon;
    
    private Image defIcon;
    
    private TrayIconPopupMenu<?, ?> popupMenu;
    
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s)                                                         *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Creates a system tray controller with the application's system tray icon
     * image set to the default system tray icon image.
     * 
     * @throws UnsupportedOperationException if the system tray is not supported
     */
    public SystemTrayController() throws UnsupportedOperationException {
        this(null);
    }
    
    /**
     * Creates a system tray controller and sets the application's system tray
     * icon image to the image loaded from the specified image url.
     * 
     * @param iconImageUrl - the url to load the tray icon's image from, or
     *        {@code null} if the default system tray icon image should be used
     * @throws UnsupportedOperationException if the system tray is not supported
     */
    public SystemTrayController(URL iconImageUrl) throws UnsupportedOperationException {
        // ensure awt toolkit is initialized.
        java.awt.Toolkit.getDefaultToolkit();
        
        // assign system tray or throw if not supported
        tray = getSystemTrayOrThrowIfNotSupported();
        
        // listen for app raised to foreground/moved to background events
        Desktop.getDesktop().addAppEventListener(new AppFocusListener());
        
        // load specified icon image or use the default toolkit icon image
        defIcon = loadIconImageOrUseDefault(iconImageUrl);
        // create tray icon and add it to the system tray
        initTrayIcon();
        addAppTrayIconToSystemTray();
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Public API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Displays a system tray notification to the user.
     * @param caption - the notification caption
     * @param text - the notification detail text
     * @param type - the type of notification
     * @see MessageType
     */
    public void notifyUser(String caption, String text, MessageType type) {
        runLater(() -> trayIcon.displayMessage(caption, text, type));
    }
    
    /**
     * Shuts down the system tray if it's supported, otherwise calling this
     * method is a no-op.
     */
    public void shutdown() {
        if (trayIcon == null) { return; }
        
        System.out.println("SystemTrayController: closing");
        
        runLater(() -> {
            Platform.exit();
            tray.remove(trayIcon);
        });
    }
    
    /**
     * Specifies the system tray icon's tool-tip message.
     * @param tip - the icon's tool-tip message to display
     */
    public void setToolTip(String tip) {
        if (trayIcon == null) { return; }
        
        runLater(() -> trayIcon.setToolTip(tip));
    }
    
    /**
     * Sets a status string at the first position in the context menu.
     * <p>
     * This status string appears as a disabled menu entry.
     * @param status - the string to display at the first position in the
     *        context menu
     * @see SystemTray#setStatus(String)
     */
    public void setStatus(String status) {
        if (trayIcon == null) { return; }
        
        // javax.swing.SwingUtilities.invokeLater(() -> trayIcon.get().);
    }
    
    /**
     * Specifies the image to use for the system tray icon.
     * @param imageUrl - image to use for the system tray icon
     * @see SystemTray#setImage(URL)
     */
    public void setIconImage(URL imageUrl) {
        runLater(() -> {
            if (trayIcon == null) { return; }
            
            defIcon = loadIconImageOrUseDefault(imageUrl);
            trayIcon.setImage(defIcon);
        });
    }
    
    /**
     * Specifies the URL of the website to open in a browser when the 'About'
     * menu item is selected.
     * @param url - the website URL to open when the 'About' menu item is
     *        selected
     */
    public void setAboutBrowserUrl(String url) {
        runLater(() -> {
            try {
                aboutUri = URI.create(url);
            } catch (Exception e) {
                // TODO log create URI exception rather than printing stack trace
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Specifies the enabled state of the 'Run Scraper' menu item.
     * @param enabled - the enabled state of the 'Run Scraper' menu item
     */
    public void setRunScraperEnabled(boolean enabled) {
        runLater(() -> popupMenu.setRunScraperEnabled(enabled));
    }
    
    /**
     * Sets the about tray icon menu item selected action listener.
     * @param listener - the about selected listener
     */
    public void setOnAboutSelected(ActionListener listener) {
        runLater(() -> popupMenu.setOnAboutSelected(listener));
    }
    
    /**
     * Sets the run scraper tray icon menu item selected action listener.
     * @param listener - the run scraper selected listener
     */
    public void setOnRunScraperSelected(ActionListener listener) {
        runLater(() -> popupMenu.setOnRunScraperSelected(listener));
    }
    
    /**
     * Sets the dashboard tray icon menu item selected action listener.
     * @param listener - the dashboard selected listener
     */
    public void setOnDashboardSelected(ActionListener listener) {
        runLater(() -> popupMenu.setOnDashboardSelected(listener));
    }
    
    /**
     * Sets the settings tray icon menu item selected action listener.
     * @param listener - the settings selected listener
     */
    public void setOnSettingsSelected(ActionListener listener) {
        runLater(() -> popupMenu.setOnSettingsSelected(listener));
    }
    
    /**
     * Sets the view logs tray icon menu item selected action listener.
     * @param listener - the view logs selected listener
     */
    public void setOnViewLogsSelected(ActionListener listener) {
        runLater(() -> popupMenu.setOnViewLogsSelected(listener));
    }
    
    /**
     * Sets the quit tray icon menu item selected action listener.
     * <p>
     * The quit tray icon menu item has a preset action listener that handles
     * exiting of the application, therefore changing the action listener
     * should rarely, if ever, be needed.
     * @param listener - the quit selected listener
     */
    public void setOnQuitSelected(ActionListener listener) {
        runLater(() -> popupMenu.setOnQuitSelected(listener));
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     *************************************************************************/
    
    /** Gets the system tray or throws an UnsupportedOperationException if the
     *  system tray is not supported.
     */
    private SystemTray getSystemTrayOrThrowIfNotSupported() {
        UnsupportedOperationException uoe = new UnsupportedOperationException("system tray not supported.");
        
        if (!SystemTray.isSupported()) { throw uoe; }
        
        SystemTray tmp = null;
        try {
            tmp = SystemTray.getSystemTray();
        } catch (Exception e) {
            throw uoe;
        }
        
        return tmp;
    }
    
    /** Initializes the tray icon. */
    private void initTrayIcon() {
        trayIcon = new TrayIcon(defIcon, Main.APP_NAME);
        
        // allow the system to auto-size the tray icon
        trayIcon.setImageAutoSize(true);
        
        // add notification click listener to tray icon
        trayIcon.setActionCommand("notification-clicked");
        trayIcon.addActionListener(this::onNotificationClicked);
        
        // create and set tray icon's popup menu
        useSwingPopupMenu();
        // useAWTPopupMenu();
        
        // set about and quit initial menu item action handlers
        setInitialAboutActionHandler();
        setInitialQuitActionHandler();
    }
    
    /**  */
    private void useSwingPopupMenu() { popupMenu = new SwingPopupMenu(trayIcon); }
    
    /**  */
    private void useAWTPopupMenu() {
        popupMenu = new AWTPopupMenu();
        trayIcon.setPopupMenu((PopupMenu) popupMenu.getPopupMenu());
    }
    
    /** Initial about handler opens aboutUri in a browser. */
    private void setInitialAboutActionHandler() {
        popupMenu.setOnAboutSelected(action -> {
            try {
                Desktop.getDesktop().browse(aboutUri);
            } catch (IOException e) {
                // TODO log browser exception rather than print stack trace
                e.printStackTrace();
            }
        });
    }
    
    /** Initial quit handler exits JavaFX and removes icon from system tray. */
    private void setInitialQuitActionHandler() {
        popupMenu.setOnQuitSelected(action -> {
            System.out.println("Quit was selected");
            
            // exit JavaFX and remove system tray icon
            Platform.exit();
            tray.remove(trayIcon);
            System.exit(0);
        });
    }
    
    /**
     *  Helper that loads the specified icon image url or uses the default
     *  toolkit icon image.
     */
    private Image loadIconImageOrUseDefault(URL iconImageUrl) {
        Image icon = null;
        
        if (iconImageUrl != null) {
            try {
                icon = Toolkit.getDefaultToolkit().createImage(iconImageUrl);
            } catch (Exception e) {
                // TODO log if an exception occurred while loading the icon image
            }
        }
        
        // if something went wrong then try to set icon to OS default
        if (icon == null) {
            if (Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Feature.ICON_IMAGE)) {
                icon = Taskbar.getTaskbar().getIconImage();
            }
        }
        
        return icon;
    }
    
    /** Adds application tray icon to system tray. */
    private void addAppTrayIconToSystemTray() {
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            // TODO log tray add exception rather than printing stack trace
            e.printStackTrace();
        }
        
    }
    
    /**
     * Handles clicking actions on notifications.
     * @param action - the clicking action to handle
     */
    private void onNotificationClicked(ActionEvent action) {
        System.out.println(action.getActionCommand());
        System.out.println("Notification Clicked");
    }
    
    /** Listeners for app raised to foreground/moved to background events. */
    private class AppFocusListener implements AppForegroundListener {
        
        @Override
        public void appRaisedToForeground(AppForegroundEvent e) {
            System.out.println("SystemTrayController: app moved to foreground");
        }
        
        @Override
        public void appMovedToBackground(AppForegroundEvent e) {
            System.out.println("SystemTrayController: app moved to background");
        }
        
    } // class AppFocusListener
    
    /** Convenience helper that invokes {@code SwingUtils.invokeLater(toRun)}. */
    private void runLater(final Runnable toRun) {
        javax.swing.SwingUtilities.invokeLater(toRun);
    }
    
    
}
