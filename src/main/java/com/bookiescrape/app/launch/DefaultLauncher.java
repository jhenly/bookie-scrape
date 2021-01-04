package com.bookiescrape.app.launch;

import com.bookiescrape.app.tray.SystemTrayController;

/**
 * Default application launcher.
 * <p>
 * This launcher does not support the system tray and simply launches the
 * application.
 * 
 * @author Jonathan Henly
 */
public class DefaultLauncher extends ApplicationLauncher {
    
    /**
     * This constructor is needed by, and only to be used by, JavaFX.
     * <p>
     * Do not create instances of this class!
     */
    public DefaultLauncher() {}
    
    
    @Override
    protected void implLaunch() { DefaultLauncher.launch(DefaultLauncher.class); }
    
    /**
     * {@link DefaultLauncher} does not support the system tray, therefore this
     * method returns {@code false}.
     * @return {@code false}
     */
    @Override
    public boolean systemTrayIsSupported() { return false; }
    
    /**
     * {@link DefaultLauncher} does not support the system tray, therefore this
     * method returns {@code null}.
     * @return {@code null}
     */
    @Override
    public SystemTrayController getSystemTrayController() { return null; }
    
    /**
     * {@link DefaultLauncher} does not support the system tray, therefore this
     * method does nothing.
     */
    @Override
    protected void setUpSystemTray() {}
    
}
