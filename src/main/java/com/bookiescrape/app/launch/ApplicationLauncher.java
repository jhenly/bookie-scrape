package com.bookiescrape.app.launch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.sample.ApplicationHandler;
import com.bookiescrape.app.sample.ApplicationMediator;
import com.bookiescrape.app.util.OperatingSystemUtils;
import com.bookiescrape.app.util.OperatingSystemUtils.OperatingSystem;

/**
 * Bookie Scrape application launcher.
 * 
 * @author Jonathan Henly
 */
public abstract class ApplicationLauncher extends ApplicationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationHandler.class);
    
    /**************************************************************************
     *                                                                        *
     * Static Launch Functionality                                            *
     *                                                                        *
     *************************************************************************/
    
    private static boolean hasLaunched;
    private static ApplicationMediator applicationMediator;
    
    /**
     * Launches the application based on the detected operating system.
     * @throws RuntimeException if this method is invoked again after being
     *         invoked
     */
    public final static void launchApplication(ApplicationMediator appMediator) {
        if (hasLaunched) { logAndThrow(new RuntimeException("application has already been launched")); }
        hasLaunched = true;
        
        applicationMediator = appMediator;
        
        LOG.info("beginning application launch");
        
        OperatingSystem os = OperatingSystemUtils.getDetectedOS();
        LOG.info("{} operating system detected", os);
        
        switch (os) {
            case WINDOWS:
                LOG.info("launching application with the Windows launcher");
                
                // if OS is windows then launch with system tray support
                (new WindowsLauncher()).launch();
                break;
            default:
                LOG.info("launching application with the default launcher");
                
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
    
    /** Convenience helper that logs and throws an exception. */
    private static final void logAndThrow(RuntimeException e) {
        LOG.error("{}", e);
        throw e;
    }
    
}
