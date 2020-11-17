package com.sportsbookscraper.app.config;

import java.io.IOException;


/**
 * Static factory class to serve concrete implementations of
 * {@link DSProperties}.
 *
 * @author Jonathan Henly
 */
public final class PropertiesFactory {

    // don't subclass this class
    private PropertiesFactory() {}

    /**
     * Loads the properties from an OS specific data store rather than a file
     * based data store.
     *
     * @return a concrete implementation of {@link DSProperties} which can be
     *         used to retrieve specific properties
     */
    public static DSProperties getProperties() {
        return null;
    }
    
    
    /**
     * Loads the properties from a passed in properties file path into a
     * concrete {@link DSProperties} implementation instance.
     *
     * @param propertiesFile - either a resource in the class path or a path to
     *        a file
     * @return a concrete implementation of {@link DSProperties} which can be
     *         used to retrieve specific properties
     * @throws RequiredPropertyNotFoundException thrown if the properties file
     *         is missing any required properties
     * @throws IOException if an I/O error occurs while trying to read or close
     *         the supplied properties file
     */
    public static DSProperties loadProperties(String propertiesFile)
        throws RequiredPropertyNotFoundException, IOException
    {
        return new WorkbookProperties(propertiesFile);
    }


    /**
     * Loads the properties from a passed in properties file path into a
     * concrete {@link DSProperties} implementation instance. Also tells the
     * data store to use the supplied Excel file path, rather than the path in
     * the properties.
     *
     * @param propertiesFile - either a resource in the class path or a path to
     *        a file
     * @param pathToExcelFile - the path to Excel file to be used, rather than
     *        the path loaded from properties
     * @return a concrete implementation of {@link DSProperties} which can be
     *         used to retrieve specific properties
     * @throws RequiredPropertyNotFoundException thrown if the properties file
     *         is missing any required properties
     * @throws IOException if an I/O error occurs while trying to read or close
     *         the supplied properties file, or if the Excel file does not exist
     */
    public static
    DSProperties loadProperties(String propertiesFile, String pathToExcelFile)
        throws RequiredPropertyNotFoundException, IOException
    {
        return new WorkbookProperties(propertiesFile, pathToExcelFile);
    }
}
