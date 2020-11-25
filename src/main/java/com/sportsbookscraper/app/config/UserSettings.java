package com.sportsbookscraper.app.config;

import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


/**
 * Static factory class to serve concrete implementations of {@link Settings}.
 *
 * @author Jonathan Henly
 */
public final class UserSettings {
    private static final String PREF_NODE_NAME = "/com/sportsbookscraper/app";
    private static Preferences userPrefs;
    
    /* gets singleton Preferences instance userPrefs */
    private static Preferences getUserPreferences() {
        if (userPrefs == null) {
            userPrefs = Preferences.userRoot().node(PREF_NODE_NAME);
        }
        
        return userPrefs;
    }
    
    // don't subclass this class
    private UserSettings() {}
    
    /**
     * Loads user settings from an OS specific data store rather than a file
     * based data store.
     *
     * @return a concrete implementation of {@link Settings} which can be used
     *         to retrieve specific properties
     */
    public static Settings loadSettings() { return null; }
    
    
    /**
     * Loads user settings, from a passed in properties file path, into a
     * concrete {@link Settings} implementation instance.
     *
     * @param propertiesFile
     *                       - either a resource in the class path or a path to
     *                       a file
     * @return a concrete implementation of {@link Settings} which can be used
     *         to retrieve specific properties
     * @throws RequiredSettingNotFoundException
     *                                          thrown if the properties file is
     *                                          missing any required properties
     * @throws IOException
     *                                          if an I/O error occurs while
     *                                          trying to read or close the
     *                                          supplied properties file
     */
    public static Settings loadSettings(String propertiesFile)
        throws RequiredSettingNotFoundException, IOException {
        return new UserProperties(propertiesFile);
    }
    
    
    /**
     * Loads user settings, from a passed in properties file path, into a
     * concrete {@link Settings} implementation instance. Also tells the data
     * store to use the supplied Excel file path, rather than the path in the
     * properties file.
     *
     * @param propertiesFile
     *                        - either a resource in the class path or a path to
     *                        a file
     * @param pathToExcelFile
     *                        - the path to Excel file to be used, rather than
     *                        the path loaded from the properties file
     * @return a concrete implementation of {@link Settings} which can be used
     *         to retrieve specific properties
     * @throws RequiredSettingNotFoundException
     *                                          thrown if the properties file is
     *                                          missing any required properties
     * @throws IOException
     *                                          if an I/O error occurs while
     *                                          trying to read or close the
     *                                          supplied properties file, or if
     *                                          the Excel file does not exist
     */
    public static Settings loadSettings(String propertiesFile,
        String pathToExcelFile)
        throws RequiredSettingNotFoundException, IOException {
        return new UserProperties(propertiesFile, pathToExcelFile);
    }
    
    /* -- Private Static Methods -- */
    
    private static final String BACKING_STORE_AVAIL = "BackingStoreAvail";
    
    /**
     * Method that determines whether the backing store is available by
     * attempting to modify a preference value and flush the result to the
     * backing store.
     * 
     * @return {@code true} if the backing store is available, otherwise
     *         {@code false}
     */
    private static boolean backingStoreAvailable() {
        Preferences prefs = Preferences.userRoot().node("<temporary>");
        try {
            boolean oldValue = prefs.getBoolean(BACKING_STORE_AVAIL, false);
            prefs.putBoolean(BACKING_STORE_AVAIL, !oldValue);
            prefs.flush();
        } catch (BackingStoreException e) {
            return false;
        }
        return true;
    }
}
