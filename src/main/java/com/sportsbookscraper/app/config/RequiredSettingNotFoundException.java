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
    
    // private members
    private String propFilePath;
    private String settingKey;
    private boolean inPropertiesFile;
    private boolean inUserPreferences;
    private boolean isFatal;
    
    /**
     * Signals that this exception is a fatal, non-recoverable exception.
     * <p>
     * This method returns {@code true} if a required setting could not be found
     * in any of the following:
     * <ul>
     * <li>a user's backing preferences data store</li>
     * <li>a specified properties file</li>
     * <li>the default {@code config.properties} file</li>
     * </ul>
     * 
     * @return {@code true} if this exception is non-recoverable, {@code false}
     *         otherwise
     */
    public boolean isFatal() { return isFatal; }
    
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
    public String requiredSettingsKey() { return settingKey; }
    
    /**
     * Used internally to signal that no user preferences exist.
     */
    RequiredSettingNotFoundException() {}
    
    /**
     * Constructs a {@code RequiredPropertyNotFoundException} with a detail
     * message informing that the specified required setting's key was not found
     * in the user's backing preferences data store.
     *
     * @param SettingsKey
     *                    - the settings key that was not found in the user's
     *                    backing preferences data store
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
     * specified property and the user specified properties file path into a
     * detail message.
     *
     * @param property
     *                  - the property that was not found
     * @param propsFile
     *                  - the user specified properties file path
     */
    public RequiredSettingNotFoundException(String property, String propsFile) {
        // call Exception(String) with detailed message
        super(combineIntoPropertyMessage(property, propsFile));
        
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
     * Constructs a {@code RequiredPropertyNotFoundException} from a settings
     * key and a specified properties file path.
     * <p>
     * This exception signals that a required property could not be found in the
     * user's backing preferences store as well as a specified properties file.
     * 
     * @param key
     *                  - the required setting key that could not be found
     * 
     * @param propsFile
     *                  - the path to the properties file
     */
    public RequiredSettingNotFoundException(SettingsKey key, String propsFile) {
        // call Exception(String) with detail message
        super(getCombinedNotFoundMsg(key.key(), propsFile));
        
        // set exception members
        settingKey = key.key();
        propFilePath = propsFile;
        inUserPreferences = true;
        inPropertiesFile = true;
    }
    
    /* helper to get a detailed message when a settings key can't be found in a
     * properties file and user's backing preferences store */
    private static String getCombinedNotFoundMsg(String key, String propsFile) {
        final String frmtstr = "the required user setting '%s' was not found in"
            + " the user's preferences or the properties file '%s'";
        return String.format(frmtstr, key, propsFile);
    }
    
    /**
     * Constructs a fatal {@code RequiredSettingNotFoundException} that signals
     * that this exception is a non-recoverable exception.
     * <p>
     * The calling code that causes this exception must check if this exception
     * is fatal, via {@link #isFatal()}, in order to act.
     * <p>
     * The only time this constructor should be used is when a required setting
     * could not be found in any of the following:
     * <ul>
     * <li>a user's backing preferences data store</li>
     * <li>a specified properties file</li>
     * <li>the default {@code config.properties} file</li>
     * </ul>
     * 
     * @param key
     *                         - the required setting's key that could not be
     *                         found
     * @param propsFile
     *                         - the path to the specified properties file, or
     *                         {@code null} if not specified
     * @param defPropsFilePath
     *                         - the path to the default properties file
     */
    public RequiredSettingNotFoundException(SettingsKey key, String propsFile,
        String defPropsFilePath) {
        
        // call Exception(String) with detail message
        super(formatFatalMsg(key.key(), propsFile, defPropsFilePath));
        
        // set exception members
        settingKey = key.key();
        propFilePath = propsFile;
        
        inUserPreferences = true;
        inPropertiesFile = (propsFile != null);
        isFatal = true;
    }
    
    /* helper to get a fatal detailed message */
    private static String formatFatalMsg(String key, String propsFile,
        String defaultPropFile) {
        String ret = "";
        
        if (propsFile != null) {
            String frmtstr = "the required user setting '%s' was not found in"
                + " the user's preferences, the specified properties file '%s'"
                + ", or the default properties file '%s'";
            ret = String.format(frmtstr, key, propsFile, defaultPropFile);
        } else {
            String frmtstr = "the required user setting '%s' was not found in"
                + " the user's preferences or the default properties file '%s'";
            ret = String.format(frmtstr, key, defaultPropFile);
        }
        
        return ret;
    }
    
    /**
     * Convenience constructor that takes in the property string that was not
     * found, as well as the {@code .properties} file in which it was not found.
     * <p>
     * This constructor simply calls {@code this(property,
     * propertiesFile.getAbsolutePath())}
     *
     * @param property
     *                       - the property that was not found
     * @param propertiesFile
     *                       - the {@code .properties} file
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
     *                       - the property that was not found
     * @param propertiesPath
     *                       - the {@code .properties} file
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
