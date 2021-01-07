package com.bookiescrape.app.launch;

import com.bookiescrape.app.sample.ApplicationHandler;
import com.bookiescrape.app.sample.ApplicationMediator;

/**
 * Bookie Scrape application launcher.
 * 
 * @author Jonathan Henly
 */
public abstract class ApplicationLauncher extends ApplicationHandler {
    
    /**************************************************************************
     *                                                                        *
     * Static Launch Functionality                                            *
     *                                                                        *
     *************************************************************************/
    
    private static boolean hasLaunched;
    private static ApplicationMediator applicationMediator;
    
    /**
     * Launches the application based on the detected operating system.
     * @throws UnsupportedOperationException if this method is invoked again
     *         after being invoked
     */
    public final static void launchApplication(ApplicationMediator appMediator) {
        if (hasLaunched) { throw new UnsupportedOperationException("application has already been launched."); }
        hasLaunched = true;
        
        applicationMediator = appMediator;
        
        OSType os = OSType.getDetectedOSType();
        
        switch (os) {
            case Windows:
                // if OS is windows then launch with system tray support
                (new WindowsLauncher()).launch();
                break;
            default:
                // if OS isn't windows then launch with no system tray support
                (new DefaultLauncher()).launch();
                break;
        }
        
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Abstract API                                                           *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Launches the application.
     */
    public final void launch() { implLaunch(); }
    
    /**
     * Used by {@link ApplicationLauncher#launch()} to launch implementing classes.
     * <p>
     * Sub classes must implement this method. A simple implementation follows:
     * 
     * <pre>
     * public class ImplLauncher extends ApplicationLauncher {
     *     // other code ...
     * 
     *     &#064;Override
     *     protected void implLaunch() {
     *         ImplLauncher.launch(ImplLauncher.class);
     *     }
     * 
     * }
     * </pre>
     */
    protected abstract void implLaunch();
    
    @Override
    protected final ApplicationMediator getApplicationMediator() { return applicationMediator; }
}
