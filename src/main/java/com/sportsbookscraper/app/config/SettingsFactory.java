package com.sportsbookscraper.app.config;

import java.io.IOException;


/**
 * Static factory class to serve concrete implementations of {@link Settings}.
 *
 * @author Jonathan Henly
 */
public final class SettingsFactory {
    
    // don't subclass this class
    private SettingsFactory() {}
    
    /**
     * Loads user settings from an OS specific data store rather than a file
     * based data store.
     *
     * @return a concrete implementation of {@link Settings} which can be used
     *         to retrieve specific properties
     */
    public static Settings loadSettings() { return null; }
    
    
    /**
     * Loads user settings from a passed in properties file path into a concrete
     * {@link Settings} implementation instance.
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
        return new WorkbookProperties(propertiesFile);
    }
    
    
    /**
     * Loads user settings from a passed in properties file path into a concrete
     * {@link Settings} implementation instance. Also tells the data store to
     * use the supplied Excel file path, rather than the path in the properties.
     *
     * @param propertiesFile
     *                        - either a resource in the class path or a path to
     *                        a file
     * @param pathToExcelFile
     *                        - the path to Excel file to be used, rather than
     *                        the path loaded from properties
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
        return new WorkbookProperties(propertiesFile, pathToExcelFile);
    }
}
