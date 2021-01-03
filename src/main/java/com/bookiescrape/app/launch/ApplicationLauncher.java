package com.bookiescrape.app.launch;

import com.bookiescrape.app.sample.ApplicationHandler;

/**
 * A Bookie Scrape application launcher.
 * 
 * @author Jonathan Henly
 */
public abstract class ApplicationLauncher extends ApplicationHandler {
    
    /**************************************************************************
     *                                                                        *
     * Static Launch Functionality                                            *
     *                                                                        *
     *************************************************************************/
    
    private static final String OS_NAME_PROPERTY = "os.name";
    
    private static final String WINDOWS = "windows";
    
    private static boolean hasLaunched;
    
    /**
     * Launches the application based on the system's {@code "os.name"}
     * property value.
     * @throws UnsupportedOperationException if this method is invoked again
     *         after being invoked
     * @see System#getProperty(String)
     */
    public final static void launchApplication() {
        if (hasLaunched) { throw new UnsupportedOperationException("application has already been launched."); }
        hasLaunched = true;
        
        final String osName = getOperatingSystemName();
        if (osIsWindows(osName)) {
            // if OS is windows then launch with system tray support
            (new WindowsLauncher()).launch();
        } else {
            // if OS isn't windows then launch with no system tray support
            (new DefaultLauncher()).launch();
        }
        
    }
    
    /** 
     * Helper to check if a specified operating system name is associated with
     * the Windows operating system.
     * <p>
     * If the specified operating system name is {@code null} then this method
     * will return {@code false}.
     * 
     * @param osName - the operating system name
     * @return {@code true} if the specified operating system name is
     *         associated with the Windows operating system, otherwise {@code false}
     */
    private static boolean osIsWindows(String osName) {
        return osName != null && osName.toLowerCase().startsWith(WINDOWS);
    }
    
    /** 
     * Helper to get the operating system name from
     * {@link System#getProperty(String)}.
     * <p>
     * If querying the system properties for {@code "os.name"} throws an
     * exception or results in {@code null}, then the empty string ({@code ""})
     * will be returned.
     * <p>
     * Any thrown exceptions will be caught by this method and logged.
     * 
     * @return the system properties' operating system name, or the empty string
     *         ({@code ""})
     */
    private static String getOperatingSystemName() {
        String osName = null;
        try {
            osName = System.getProperty(OS_NAME_PROPERTY);
        } catch (Exception e) {
            // TODO properly log exception from getting OS name
            System.err.println(e.getLocalizedMessage());
        }
        
        return osName == null ? "" : osName;
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
    
}
