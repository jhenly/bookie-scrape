package com.bookiescrape.app.util;

import java.util.Locale;

/**
 * Utility class with operating system related methods.
 * <p>
 * The main functionality of this utility class is to make a best guess at the
 * currently running operating system through the use of
 * {@code System.getProperty("os.name")}.
 * 
 * @author Jonathan Henly
 * @see #getDetectedOS()
 * @see OperatingSystem
 * @see System#getProperty(String)
 */
public final class OperatingSystemUtils {
    
    /**
     * Enum representing some popular operating systems.
     * 
     * @see OperatingSystemUtils
     * @see OperatingSystemUtils#getDetectedOS()
     */
    public enum OperatingSystem {
        /** Mac operating system. */
        MAC_OS("mac", "darwin"),
        /** Windows operating system. */
        WINDOWS("win"),
        /** Ubuntu operating system. */
        UBUNTU("ubuntu"),
        /** Unix operating system. */
        UNIX("nix"),
        /** Linux operating system. */
        LINUX("nux"),
        /** Unrecognized operating system. */
        OTHER("generic");
        
        
        private final String[] keys;
        
        /** Constructs an OSType enum. */
        private OperatingSystem(String... keys) { this.keys = keys; }
        
    } // enum OperatingSystem
    
    
    /** Utility class, don't subclass this class. */
    private OperatingSystemUtils() {}
    
    
    /**************************************************************************
     *                                                                        *
     * Static API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /** The operating system name system property key. */
    private static final String OS_NAME_KEY = "os.name";
    
    private static OperatingSystem detectedOS;
    
    /**
     * Gets the operating system detected from
     * {@code System.getProperty("os.name")}.
     * 
     * @return the operating system detected from
     *         {@code System.getProperty("os.name")}
     * @see OperatingSystem
     * @see System#getProperty(String)
     */
    public static OperatingSystem getDetectedOS() {
        if (detectedOS == null) { detectedOS = getOperatingSystemType(); }
        return detectedOS;
    }
    
    /** Helper for {@code getDetectedOSType()}. */
    private static OperatingSystem getOperatingSystemType() {
        String osName = getOSNameProperty();
        
        // return Other if getOSNameProperty() returns a blank string
        if (osName.isBlank()) { return OperatingSystem.OTHER; }
        
        // change osName to English lowercase
        osName = osName.toLowerCase(Locale.ENGLISH);
        
        // iterate over OS enums and check if osName contains any OS keys
        for (OperatingSystem os : OperatingSystem.values()) {
            if (osNameContainsAnOSKey(osName, os)) { return os; }
        }
        
        return OperatingSystem.OTHER;
    }
    
    /** 
     * Helper that iterates over an {@code OperatingSystem} enum's keys and
     * checks if {@code osName} contains a key.
     *  
     *  @param osName - the operating system name from
     *         {@code System.getProperty("os.name")}
     *  @param os - the {@code OperatingSystem} enum to check
     *  @return {@code true} if {@code osName} contains any of {@code os}'s
     *          keys, otherwise {@code false}
     */
    private static boolean osNameContainsAnOSKey(String osName, OperatingSystem os) {
        for (String key : os.keys) {
            if (osName.indexOf(key) != -1) { return true; }
        }
        return false;
    }
    
    /** 
     * Helper to get the operating system name from
     * {@code System#getProperty("os.name")}.
     * <p>
     * If querying the system properties for {@code "os.name"} throws an
     * exception or results in {@code null}, then the empty string ({@code ""})
     * will be returned.
     * <p>
     * <b>Note:</b>Any thrown exceptions will be caught by this method and, if
     * enabled, logged.
     * 
     * @return the operating system name retrieved from
     *         {@code System#getProperty("os.name")}, or the empty string
     *         ({@code ""})
     * @see System#getProperty(String)
     */
    private static String getOSNameProperty() {
        String osName = null;
        
        try {
            osName = System.getProperty(OS_NAME_KEY);
        } catch (Exception e) {
            // TODO properly log exception from getting OS name
            System.err.println(e.getLocalizedMessage());
        }
        
        return osName == null ? "" : osName;
    }
    
    
} // class OSUtils
