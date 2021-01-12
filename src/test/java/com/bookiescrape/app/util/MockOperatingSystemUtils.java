package com.bookiescrape.app.util;

import java.util.Locale;

import com.bookiescrape.app.util.OperatingSystemUtils.OperatingSystem;

final class MockOperatingSystemUtils {
    
    /**
         * Enum representing some popular operating systems.
         * 
         * @see OperatingSystemUtils
         * @see OperatingSystemUtils#getDetectedOS()
         */
    enum MockOperatingSystem {
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
        private MockOperatingSystem(String... keys) { this.keys = keys; }
        
        public OperatingSystem getOperatingSystem() {
            switch (this) {
                case MAC_OS:
                    return OperatingSystem.MAC_OS;
                case WINDOWS:
                    return OperatingSystem.WINDOWS;
                case UBUNTU:
                    return OperatingSystem.UBUNTU;
                case UNIX:
                    return OperatingSystem.UNIX;
                case LINUX:
                    return OperatingSystem.LINUX;
                
                case OTHER:
                default:
                    return OperatingSystem.OTHER;
            }
            
        }
        
    } // enum OperatingSystem
    
    
    public MockOperatingSystemUtils() {}
    
    
    /** The operating system name system property key. */
    private static final String OS_NAME_KEY = "os.name";
    
    /**
     * Gets the operating system detected from
     * {@code System.getProperty("os.name")}.
     * 
     * @return the operating system detected from
     *         {@code System.getProperty("os.name")}
     * @see MockOperatingSystem
     * @see System#getProperty(String)
     */
    public static MockOperatingSystem getDetectedOS() { return getOperatingSystemType(); }
    
    /** Helper for {@code getDetectedOSType()}. */
    private static MockOperatingSystem getOperatingSystemType() {
        String osName = getOSNameProperty();
        
        // return Other if getOSNameProperty() returns a blank string
        if (osName.isBlank()) { return MockOperatingSystem.OTHER; }
        
        // change osName to English lowercase
        osName = osName.toLowerCase(Locale.ENGLISH);
        
        // iterate over OS enums and check if osName contains any OS keys
        for (MockOperatingSystem os : MockOperatingSystem.values()) {
            if (osNameContainsAnOSKey(osName, os)) { return os; }
        }
        
        return MockOperatingSystem.OTHER;
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
    private static boolean osNameContainsAnOSKey(String osName, MockOperatingSystem os) {
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
    
    
}
