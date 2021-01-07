package com.bookiescrape.app.launch;

import java.util.Locale;

/**
 * Enum specifying the operating system detected from
 * {@code System.getProperty("os.name")}.
 * <p>
 * This enum's static methods are guaranteed to not throw any exceptions.
 * 
 * @author Jonathan Henly
 * 
 * @see System#getProperty(String)
 */
public enum OSType {
    /** The Mac operating system. */
    MacOS("mac", "darwin"),
    /** The Windows operating system. */
    Windows("win"),
    /** The Ubuntu operating system. */
    Ubuntu("ubuntu"),
    /** The Unix operating system. */
    Unix("nix"),
    /** The Linux operating system. */
    Linux("nux"),
    /** An unrecognized operating system. */
    Other("generic");
    
    private static final String OS_NAME_PROPERTY = "os.name";
    
    private static OSType detectedOS;
    
    private final String[] keys;
    
    /** Constructs an OSType enum. */
    private OSType(String... keys) { this.keys = keys; }
    
    /**
     * Gets the operating system type detected from
     * {@code System.getProperty("os.name")}.
     * @return the operating system type detected from system propertie's
     *         {@code "os.name"}
     */
    public static OSType getDetectedOSType() {
        if (detectedOS == null) { detectedOS = getOperatingSystemType(); }
        return detectedOS;
    }
    
    /** Helper for {@code getDetectedOSType()}. */
    private static OSType getOperatingSystemType() {
        String osName = getOSNameProperty();
        
        // return Other if getOSNameProperty returns ""
        if (osName.isBlank()) { return Other; }
        
        // change osName to English lowercase
        osName = osName.toLowerCase(Locale.ENGLISH);
        
        // iterate over OS types and check if osName contains any key
        for (OSType osType : values()) {
            if (osNameContainsOSType(osName, osType)) { return osType; }
        }
        
        return Other;
    }
    
    /** 
     * Helper that iterates over an {@code OSType} instance's keys and checks if
     * {@code osName} contains a key.
     *  
     *  @param osName - the operating system name from
     *         {@code System.getProperty("os.name")}
     *  @param type - the {@code OSType} to check
     *  @return {@code true} if {@code osName} contains any of {@code type}'s
     *          keys, otherwise {@code false}
     */
    private static boolean osNameContainsOSType(String osName, OSType type) {
        for (String key : type.keys) {
            if (osName.indexOf(key) != -1) { return true; }
        }
        return false;
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
    private static String getOSNameProperty() {
        String osName = null;
        try {
            osName = System.getProperty(OS_NAME_PROPERTY);
        } catch (Exception e) {
            // TODO properly log exception from getting OS name
            System.err.println(e.getLocalizedMessage());
        }
        
        return osName == null ? "" : osName;
    }
    
    
}
