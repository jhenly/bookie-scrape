package com.sportsbookscraper.app.config;

import java.io.File;
import java.nio.file.Path;

/**
 * Signals that a required property was not found after loading properties from
 * a {@code .properties} file.
 * 
 * @author Jonathan Henly
 */
public class RequiredSettingNotFoundException extends Exception {
    private static final long serialVersionUID = -1207032861834691318L;
    private String propFilePath;
    private String settingKey;
    private boolean inPropertiesFile;
    private boolean inUserPreferences;
    
    /**
     * Returns whether or not the required setting was not found in a properties
     * file.
     * 
     * @return {@code true} if the required setting was not found in a
     *         properties file, otherwise {@code false}
     */
    public boolean notFoundInPropertiesFile() { return inPropertiesFile; }
    
    /**
     * Returns whether or not the required setting was not found in the user's
     * backing preferences data store.
     * 
     * @return {@code true} if the required setting was not found in the user's
     *         backing preferences data store, otherwise {@code false}
     */
    public boolean notFoundInUserPreferences() { return inUserPreferences; }
    
    /**
     * Returns a string representing the properties file path used, or
     * {@code null} if this exception is not related to a setting not being
     * found in a properties file.
     * 
     * @return the properties file path where the setting was not found, or
     *         {@code null}
     */
    public String propertiesFilePath() { return propFilePath; }
    
    /**
     * Returns the setting's key that was not found in either a properties file
     * or a user's backing preferences data store.
     * 
     * @return the setting's key that was not found.
     */
    public String requiredSettingKey() { return settingKey; }
    
    /**
     * Constructs a {@code RequiredPropertyNotFoundException}, combining the
     * specified property and the specified properties file path into a detail
     * message.
     *
     * @param property
     *                      the property that was not found
     * @param propsFilePath
     *                      the {@code .properties} file path
     */
    public RequiredSettingNotFoundException(SettingsKey key) {
        super(combineIntoPreferenceMessage(key));
        inUserPreferences = true;
    }
    
    /* helper method to combine strings in constructor call to super() */
    private static String combineIntoPreferenceMessage(SettingsKey key) {
        // TODO provide where the user's preferences data store is
        final String frmtstr = "the required preference '%s' was not found in"
            + " the user's preferences data store";
        return String.format(frmtstr, key.key());
    }
    
    /**
     * Constructs a {@code RequiredPropertyNotFoundException}, combining the
     * specified property and the specified properties file path into a detail
     * message.
     *
     * @param property
     *                      the property that was not found
     * @param propsFilePath
     *                      the {@code .properties} file path
     */
    public RequiredSettingNotFoundException(String property,
        String propsFilePath) {
        // call Exception(String) with detailed message
        super(combineIntoPropertyMessage(property, propsFilePath));
        
        settingKey = property;
        inPropertiesFile = true;
    }
    
    /* helper method to combine strings in constructor call to super() */
    private static String combineIntoPropertyMessage(String prop,
        String filepath) {
        final String frmtstr = "the required property '%s' was not found in the"
            + " properties file '%s'";
        return String.format(frmtstr, prop, filepath);
    }
    
    /**
     * Constructs a {@code RequiredPropertyNotFoundException} from an exception
     * thrown when a required property was not found in the user's preferences.
     * <p>
     * This exception signals a fatal error, that a required property could not
     * be found in the user's backing preferences store or a specified
     * properties file.
     * 
     * @param propertiesFilePath
     *                           the path to the properties file
     * @param rsnfe
     *                           the exception that was thrown when a required
     *                           setting was not found in the user's backing
     *                           preferences store
     */
    public RequiredSettingNotFoundException(SettingsKey key,
        String propertiesFilePath, RequiredSettingNotFoundException rsnfe) {
        // call Exception(String) with detail message
        super(getCombinedNotFoundMsg(key, propertiesFilePath));
        super.initCause(rsnfe);
        
        settingKey = settingKey;
        propFilePath = propertiesFilePath;
        inUserPreferences = true;
        inPropertiesFile = true;
    }
    
    
    /* helper to get a detailed msg when setting can't be found in properties
     * file or user's backing preferences store */
    private static String getCombinedNotFoundMsg(SettingsKey key,
        String propFilePath) {
        final String frmtstr = "the required user setting '%s' was not found in"
            + " the user's preferences or the properties file '%s'";
        return String.format(frmtstr, key.key(), propFilePath);
    }
    
    
    /**
     * Convenience constructor that takes in the property string that was not
     * found, as well as the {@code .properties} file in which it was not found.
     * <p>
     * This constructor simply calls {@code this(property,
     * propertiesFile.getAbsolutePath())}
     *
     * @param property
     *                       the property that was not found
     * @param propertiesFile
     *                       the {@code .properties} file
     * 
     * @see File#getAbsolutePath()
     * @see {@linkplain #RequiredPropertyNotFound(String, String) this(String,
     *      String)}
     */
    public RequiredSettingNotFoundException(String property,
        File propertiesFile) {
        this(property, propertiesFile.getAbsolutePath());
    }
    
    
    /**
     * Convenience constructor that takes in the property string that was not
     * found, as well as a path representing the {@code .properties} file in
     * which it was not found.
     * <p>
     * This constructor simply calls {@code this(property,
     * propertiesPath.toFile())}
     *
     * @param property
     *                       the property that was not found
     * @param propertiesPath
     *                       the {@code .properties} file
     * 
     * @see Path#toFile()
     * @see {@linkplain #RequiredPropertyNotFoundException(String, String)
     *      this(String, String)}
     */
    public RequiredSettingNotFoundException(String property,
        Path propertiesPath) {
        this(property, propertiesPath.toFile());
    }
    
    
}
