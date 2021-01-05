package com.bookiescrape.app.fx.control;

import java.awt.TrayIcon.MessageType;

import com.bookiescrape.app.tray.SystemTrayController;

import javafx.stage.Stage;

/**
 * The notifier controller controls regular dialog and, if supported, system
 * tray notifications.
 * <p>
 * <b>Note:</b> system tray notification support is platform dependent, this
 * class makes an effort to allow checking for system tray notification
 * support. However, the only guarantee any of this class' methods can make is
 * that system tray notifications are not supported. Any method returning
 * {@code true} should be questioned.
 * 
 * @author Jonathan Henly
 */
public class NotifierController extends MediatableController {
    
    private Stage primaryStage;
    private SystemTrayController sysTray;
    
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s)                                                         *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Constructs a {@code NotifierController} instance that supports regular
     * dialog notifications.
     * 
     * @param primaryStage - reference to the application's primary stage
     */
    public NotifierController(Stage primaryStage) { this(primaryStage, null); }
    
    /**
     * Constructs a {@code NotifierController} instance that supports regular
     * dialog notifications and system tray notifications.
     * <p>
     * <b>Note:</b> system tray notification support is platform dependent,
     * using this constructor does not guarantee system tray notification
     * support.
     * 
     * @param primaryStage - reference to the application's primary stage
     * @param trayController - system tray controller reference, or {@code null}
     *        if system tray is not supported
     */
    public NotifierController(Stage primaryStage, SystemTrayController trayController) {
        this.primaryStage = primaryStage;
        sysTray = trayController;
        
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Public API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Gets whether or not system tray notifications are supported.
     * <p>
     * <b>Note:</b> system tray notification support is platform dependent,
     * this method can only guarantee that system tray notifications are not
     * supported.
     * 
     * @return {@code true} if system tray notifications are supported,
     *         otherwise {@code false}
     */
    public boolean systemTrayNotificationsSupported() { return sysTray != null; }
    
    /**
     * Notifies the user with an informative message.
     * @param caption - the notification caption
     * @param message - the notification info message
     */
    public void infoNotify(String caption, String message) { notify(caption, message, MessageType.INFO); }
    
    /**
     * Notifies the user with a warning message.
     * @param caption - the notification caption
     * @param message - the notification warning message
     */
    public void warnNotify(String caption, String message) { notify(caption, message, MessageType.WARNING); }
    
    /**
     * Notifies the user with an error message.
     * @param caption - the notification caption
     * @param message - the notification error message
     */
    public void errorNotify(String caption, String message) { notify(caption, message, MessageType.ERROR); }
    
    
    public void showDialog() {}
    
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     *************************************************************************/
    
    private void notify(String caption, String message, MessageType type) {
        if (systemTrayNotificationsSupported()) {
            sysTray.notifyUser(caption, message, type);
        }
    }
    
    
}
