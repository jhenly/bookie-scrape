package com.sportsbookscraper.app.config;

import static com.sportsbookscraper.app.config.SettingsKey.EXCEL_FILE_PATH;
import static com.sportsbookscraper.app.config.SettingsKey.SETTINGS_LAST_UPDATE;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Static factory class to serve concrete implementations of {@link Settings}.
 *
 * @author Jonathan Henly
 */
public final class UserSettings {
    // used to check if backing preferences data store is available
    private static final String BACKING_STORE_AVAIL = "BackingStoreAvail";
    // apps root directory in the backing preferences data store
    private static final String PREF_NODE_NAME = "/com/sportsbookscraper/app";
    // preferences node to check for existing user preferences
    private static final String PREF_CHECK_FOR_EXISTING_NODE = "excel";
    
    // this class' singleton preference instance
    private static Preferences userPrefs;
    // this class' singleton properties instance
    private static Properties userProps;
    // this class' current properties file, if properties are being used
    private static String curUserPropsFilePath;
    // whether we're using user preferences or properties
    private static boolean usingPreferences;
    
    /* gets singleton Preferences instance: userPrefs */
    private static Preferences getUserPreferences() {
        if (userPrefs == null) {
            userPrefs = Preferences.userRoot().node(PREF_NODE_NAME);
        }
        
        return userPrefs;
    }
    
    /* gets singleton Properties instance: userProps */
    private static Properties getUserProperties() { return userProps; }
    
    /* gets the current user properties file path, or null */
    private static String getCurrentPropertiesFilePath() {
        return curUserPropsFilePath;
    }
    
    /**
     * Convenience method to check for existing user preferences, do not call
     * this method before checking if the backing store is available via
     * {@linkplain UserSettings#backingStoreIsAvailable()}.
     * 
     * @return {@code true} if user preferences exist, otherwise {@code false}
     */
    private static boolean userPreferencesExist() {
        boolean exists = false;
        try {
            exists = getUserPreferences()
                .nodeExists(PREF_CHECK_FOR_EXISTING_NODE);
        } catch (BackingStoreException bse) {
            /* swallow BackingStoreException, this method should only be called
             * after checking if the backing store is available */
            return false;
        }
        
        return exists;
    }
    
    /**
     * Method that determines whether the backing preferences data store is
     * available by attempting to modify a preference value and flush the result
     * to the backing store.
     * 
     * @return {@code true} if the backing store is available, otherwise
     *         {@code false}
     */
    private static boolean backingStoreIsAvailable() {
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
    
    
    // don't subclass this class
    private UserSettings() {}
    
    /**
     * 
     * @author Jonathan Henly
     */
    private enum ActionToTake {
        
    }
    
    /**
     * Loads user settings from an OS specific data store.
     * <p>
     * If the data store does not exist, then this method will throw a
     * {@linplain RequiredSettingNotFoundException}.
     *
     * @return a concrete implementation of {@link Settings} which can be used
     *         to retrieve specific properties
     * @throws RequiredSettingNotFoundException
     *                                          thrown if no preferences exist,
     *                                          or a required setting is missing
     *                                          from the preferences
     * @throws BackingStoreException
     *                                          thrown if the the backing
     *                                          preferences data store is not
     *                                          available
     * @throws IOException
     *                                          if an unforeseen I/O error
     *                                          occurs
     */
    public static Settings loadSettings()
        throws RequiredSettingNotFoundException, BackingStoreException,
        IOException {
        // signal to loadSettings(String, String) to load preferences
        return loadUserSettings(null, null);
    }
    
    
    /**
     * Loads user settings, from a passed in properties file path, into a
     * concrete {@link Settings} implementation instance.
     *
     * @param propertiesFile
     *                       - a path to an external file
     * @return a concrete implementation of {@link Settings} which can be used
     *         to retrieve specific properties
     * @throws RequiredSettingNotFoundException
     *                                          thrown if the properties file is
     *                                          missing any required properties
     * @throws IOException
     *                                          if an I/O error occurs while
     *                                          trying to read or close the
     *                                          supplied properties file
     * @throws BackingStoreException
     */
    public static Settings loadSettings(String propertiesFile)
        throws RequiredSettingNotFoundException, IOException,
        BackingStoreException {
        return loadUserSettings(propertiesFile, null);
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
     * @throws BackingStoreException
     *                                          thrown if the the backing
     *                                          preferences data store is not
     *                                          available
     */
    public static Settings loadUserSettings(String propertiesFile,
        String pathToExcelFile) throws RequiredSettingNotFoundException,
        IOException, BackingStoreException {
        
        Settings settings = null;
        boolean available = backingStoreIsAvailable();
        boolean exist = userPreferencesExist();
        
        if (propertiesFile == null && pathToExcelFile == null) {
            // only one option - load preferences
            
            throwIfBSNotAvailableOrUserPrefsDoNotExist(available, exist);
            
            /* if we get here then user preferences must exist, but might still
             * throw RequiredSettingNotFoundException */
            settings = new UserPreferences(getUserPreferences());
            
            // if we get this far then signal that we're using user prefs
            usingPreferences = true;
            return settings;
            
        } else if (propertiesFile == null) {
            // if properties file is null then load preferences with Excel file
            
            throwIfBSNotAvailableOrUserPrefsDoNotExist(available, exist);
            
            // update preferences Excel file path before returning settings
            Preferences prefs = getUserPreferences();
            prefs.put(EXCEL_FILE_PATH.key(), pathToExcelFile);
            // write changes to backing data store
            prefs.flush();
            
            
            settings = new UserPreferences(prefs);
            
            // if we get this far then signal that we're using user prefs
            usingPreferences = true;
            return settings;
            
        } else if (pathToExcelFile == null) {
            // set the static properties file path
            curUserPropsFilePath = propertiesFile;
            
            // try to create user properties from passed in properties file
            UserProperties props = new UserProperties(propertiesFile);
            
            // if props creation succeeds then try to create preferences from it
            if (available) {
                boolean copySucceeded = true;
                
                try {
                    copyPropertiesToPreferences(props);
                    copySucceeded = true;
                } catch (BackingStoreException bse) {
                    // swallow exception and continue on with properties
                }
                
                if (copySucceeded) {
                    boolean userPrefsCreationSucceeded = false;
                    
                    try {
                        settings = new UserPreferences(getUserPreferences());
                        userPrefsCreationSucceeded = true;
                    } catch (RequiredSettingNotFoundException rsnfe) {
                        // swallow exception and continue on with properties
                    }
                    
                    if (userPrefsCreationSucceeded) {
                        // get this far then signal that we're using user prefs
                        usingPreferences = true;
                        return settings;
                    }
                }
            }
            
            /* could not create settings from preferences for some reason so we
             * carry on with properties */
            
            // set static userProps
            userProps = props.getProperties();
            // signal that we're using properties rather than preferences
            usingPreferences = false;
            
            settings = props;
        } else {
            // we received a properties file and an Excel file
        }
        
        return settings;
    }
    
    
    /* helper method that throws */
    private static void throwIfBSNotAvailableOrUserPrefsDoNotExist(
        boolean available, boolean exist)
        throws BackingStoreException, RequiredSettingNotFoundException {
        
        // throw if we can't access the preferences data store
        if (!available) {
            throw new BackingStoreException(
                "The backing preferences data store is not available.");
        }
        
        if (!exist) {
            // signal preferences don't exist with no arg exception
            throw new RequiredSettingNotFoundException();
        }
        
    }
    
    
    /* */
    private static Settings tryLoadingUserPreferences() throws IOException,
        BackingStoreException, RequiredSettingNotFoundException {
        Preferences prefs = getUserPreferences();
        Settings settings = null;
        
        if (userPreferencesExist()) {
            settings = new UserPreferences(prefs);
        }
        
        return settings;
    }
    
    /* copy key-value-s from java.util.Properties to java.util.Preferences */
    private static void copyPropertiesToPreferences(UserProperties userProps)
        throws BackingStoreException {
        Properties props = userProps.getProperties();
        Preferences prefs = getUserPreferences();
        
        // get iterators from properties key and value enumerations
        Iterator<Object> keys = props.keys().asIterator();
        Iterator<Object> vals = props.elements().asIterator();
        
        // iterate over properties key-value pairs and put them in preferences
        while (keys.hasNext() && vals.hasNext()) {
            Object okey = keys.next();
            Object val = vals.next();
            
            // if properties key is null then we can't store it in preferences
            if (okey == null) {
                continue;
            }
            
            // all property keys should be strings, but just in case
            if (okey instanceof String) {
                String key = (String) okey;
                
                /* java.util.Properties stores keys and values as Object, so we
                 * have to check what type of Object val is before we cast it
                 * and put it in preferences */
                if (val == null) {
                    /* java.util.Preferences does not allow values to be null,
                     * but java.util.Properties does, in this situation the
                     * empty string will be used if a null value is retrieved
                     * from properties */
                    prefs.put(key, "");
                } else if (val instanceof String) {
                    prefs.put(key, (String) val);
                } else if (val instanceof Integer) {
                    prefs.putInt(key, (Integer) val);
                } else if (val instanceof Long) {
                    prefs.putLong(key, (Long) val);
                } else if (val instanceof Float) {
                    prefs.putFloat(key, (Float) val);
                } else if (val instanceof Double) {
                    prefs.putDouble(key, (Double) val);
                } else {
                    // other data types not currently supported
                    continue;
                }
                
            } else {
                // if key == null
                continue;
            }
        }
        
        // save preferences to backing data store
        prefs.flush();
    }
    
    
    /**
     * Queues up an application settings change, to be applied when
     * {@link UserSettings#applySettings()} is invoked.
     * 
     * @param <T>
     *              - value's type
     * @param key
     *              - the application setting's key
     * @param value
     *              - the value to change the application setting to
     * @param clazz
     *              - values class type
     */
    public static <T> void changeApplicationSetting(SettingsKey key, T value,
        Class<T> clazz) {
        StagedSettingChange.putStagedSettingChange(key, value, clazz);
    }
    
    /**
     * Queues up a sheet settings change, to be applied when
     * {@link UserSettings#applySettings()} is invoked.
     * 
     * @param <T>
     *              - value's type
     * @param key
     *              - the sheet setting's key
     * @param value
     *              - the value to change the sheet setting to
     * @param clazz
     *              - values class type
     */
    public static <T> void changeSheetSetting(String sheet, SettingsKey key,
        T value, Class<T> clazz) {
        StagedSettingChange.putStagedSettingChange(sheet, key, value, clazz);
    }
    
    /**
     * 
     * @param key
     * @param value
     */
    public static void changeSetting(SettingsKey key, String value) {
        getUserPreferences().put(key.key(), value);
    }
    
    /**
     * 
     * @param key
     * @param value
     */
    public static void changeSetting(SettingsKey key, int value) {
        getUserPreferences().putInt(key.key(), value);
    }
    
    /**
     * 
     * @param key
     * @param value
     */
    public static void changeSetting(SettingsKey key, long value) {
        getUserPreferences().putLong(key.key(), value);
    }
    
    /**
     * 
     * @param key
     * @param value
     */
    public static void changeSetting(SettingsKey key, double value) {
        getUserPreferences().putDouble(key.key(), value);
    }
    
    /**
     * 
     * @param key
     * @param value
     */
    public static void changeSetting(SettingsKey key, boolean value) {
        getUserPreferences().putBoolean(key.key(), value);
    }
    
    /**
     * Applies all queued settings changes.
     * <p>
     * <b>Note:</b> this static method does nothing if no changes have been
     * queued, but the operations contained in this method are expensive and
     * should be avoided when unnecessary.
     * 
     * @throws BackingStoreException
     *                                          if an error occurs while
     *                                          applying any changes.
     * @throws RequiredSettingNotFoundException
     *                                          thrown if trying to apply user
     *                                          setting changes with a missing
     *                                          required user setting
     * @throws FileNotFoundException
     *                                          when trying to write to a
     *                                          non-existent properties file, if
     *                                          settings backing is a properties
     *                                          file
     * @throws IOException
     *                                          thrown when an I/O error occurs
     *
     * @see UserSettings#changeApplicationSetting(SettingsKey, Object, Class)
     * @see UserSettings#changeSheetSetting(String, SettingsKey, Object, Class)
     * @see UserSettings#clearQueuedSettingsChanges()
     */
    public static Settings applySettingsChanges() throws BackingStoreException,
        RequiredSettingNotFoundException, FileNotFoundException, IOException {
        
        // get map of staged settings changes
        HashMap<String, StagedSettingChange<?>> staged = StagedSettingChange
            .getStagedChanges();
        
        if (staged == null || staged.isEmpty()) {
            if (usingPreferences) {
                // expensive operation but onus is on caller
                return new UserPreferences(getUserPreferences());
            } else {
                // again, expensive operation but onus is on caller
                return new UserProperties(getUserProperties(), null, null);
            }
        }
        
        if (usingPreferences) {
            return applySettingsChanges(getUserPreferences(), staged);
        } else {
            return applySettingsChanges(getUserProperties(), staged);
        }
    }
    
    /* helper preferences method for applySettingsChanges() */
    private static Settings applySettingsChanges(Preferences prefs,
        HashMap<String, StagedSettingChange<?>> staged)
        throws BackingStoreException, RequiredSettingNotFoundException {
        
        // iterate over queued settings changes and update preferences
        for (Entry<String, StagedSettingChange<?>> entry : staged.entrySet()) {
            applyStagedChangeToPrefs(entry.getValue(), prefs);
        }
        
        
        // clear staged settings changes
        clearQueuedSettingsChanges();
        
        // update last-updated time stamp
        prefs.putLong(SETTINGS_LAST_UPDATE.key(), System.currentTimeMillis());
        
        // write new preferences to backing preferences data store
        prefs.flush();
        
        // return newly applied settings
        return new UserPreferences(prefs);
    }
    
    /* */
    private static void applyStagedChangeToPrefs(StagedSettingChange<?> ssc,
        Preferences prefs) {
        // get StagedSettingChange class type
        Class<?> c = ssc.clazz();
        
        if (c.isAssignableFrom(String.class)) {
            prefs.put(ssc.key(), (String) ssc.value());
        } else if (c.isAssignableFrom(Integer.class)) {
            prefs.putInt(ssc.key(), (Integer) ssc.value());
        } else if (c.isAssignableFrom(Boolean.class)) {
            prefs.putBoolean(ssc.key(), (Boolean) ssc.value());
        } else if (c.isAssignableFrom(Long.class)) {
            prefs.putLong(ssc.key(), (Long) ssc.value());
        } else if (c.isAssignableFrom(Float.class)) {
            prefs.putFloat(ssc.key(), (Float) ssc.value());
        } else if (c.isAssignableFrom(Double.class)) {
            prefs.putDouble(ssc.key(), (Double) ssc.value());
        }
    }
    
    
    /* helper properties method for applySettingsChanges() */
    private static Settings applySettingsChanges(Properties props,
        HashMap<String, StagedSettingChange<?>> staged)
        throws RequiredSettingNotFoundException, IOException {
        
        // iterate over queued settings changes and update preferences
        for (Entry<String, StagedSettingChange<?>> entry : staged.entrySet()) {
            StagedSettingChange<?> ssc = entry.getValue();
            
            // properties is easy, its put method takes (Object, Object)
            props.put(ssc.key(), ssc.value().toString());
        }
        
        // clear staged settings changes
        clearQueuedSettingsChanges();
        
        // update-last updated time stamp
        props.put(SETTINGS_LAST_UPDATE,
            ((Long) System.currentTimeMillis()).toString());
        
        String curPropsFile = getCurrentPropertiesFilePath();
        // write properties to current properties file path, if it applies
        if (curPropsFile != null
            && curPropsFile != UserProperties.DEFAULT_PROPERTIES_FILE) {
            try (FileWriter writer = new FileWriter(curPropsFile)) {
                props.store(writer, "Created by an automated properties "
                    + "writer, only edit if you know what you're doing.");
            }
        }
        
        // return newly applied settings
        return new UserProperties(props, curPropsFile);
    }
    
    /**
     * Clears all queued settings changes, calling
     * {@linkplain UserSettings#applySettingsChanges()} directly after this
     * method will apply nothing.
     * 
     * @see UserSettings#changeApplicationSetting(SettingsKey, Object, Class)
     * @see UserSettings#changeSheetSetting(String, SettingsKey, Object, Class)
     * @see UserSettings#applySettingsChanges()
     */
    public static void clearQueuedSettingsChanges() {
        StagedSettingChange.clearStagedSettingChanges();
    }
    
    /**
     * Abstract class mainly representing an application setting being staged
     * for change, or a sheet setting being staged for change.
     * 
     * @author Jonathan Henly
     *
     * @param <T>
     *            - the class type of the setting being staged for change.
     */
    private static abstract class StagedSettingChange<T> {
        // map of staged settings changes
        private static HashMap<String, StagedSettingChange<?>> stagedChanges;
        
        /**
         * Internal static method used by this class, don't use this static
         * method.
         */
        private static <T> HashMap<String, StagedSettingChange<?>> createStagedChanges() {
            return new HashMap<>();
        }
        
        /**
         * Internal static method, do not use this static method.
         */
        private static <T> HashMap<String, StagedSettingChange<?>> getStagedChanges() {
            return stagedChanges;
        }
        
        /**
         * Used by subclasses to instantiate the backing static staged changes
         * map.
         */
        protected StagedSettingChange() {
            if (stagedChanges == null) {
                stagedChanges = createStagedChanges();
            }
        }
        
        /**
         * Gets the setting's key associated with the staged change.
         * 
         * @return the key associated with this staged settings change
         */
        protected abstract String key();
        
        /**
         * Gets the setting's value associated with the staged change.
         * 
         * @return the value associated with this staged settings change
         */
        protected abstract T value();
        
        /**
         * Gets the type of the value returned by {@linkplain #value()}.
         * 
         * @return the type of {@code value()}
         */
        protected abstract Class<?> clazz();
        
        /**
         * Puts the specified {@code StagedSettingsChange} instance in the map
         * of staged settings changes.
         * <p>
         * If a {@code StagedSettingsChange} instance is already associated with
         * the key of the parameter instance, then the existing staged settings
         * change will be overwritten by the parameter instance.
         * 
         * @param ssc
         *            - the staged settings change to add
         */
        private static <T> void putStagedSettingChange(
            StagedSettingChange<T> ssc) {
            stagedChanges.put(ssc.key(), ssc);
        }
        
        /**
         * Static convenience method to create and put a staged application
         * settings change in the map of staged settings changes.
         * 
         * @param key
         *              - the application setting's key
         * @param value
         *              - the value to change the application setting to
         * @param clazz
         *              - values class type
         * @param <T>
         *              - value's type
         */
        private static <T> void putStagedSettingChange(SettingsKey key, T value,
            Class<T> clazz) {
            putStagedSettingChange(
                new StagedApplicationSettingChange<T>(key, value, clazz));
        }
        
        /**
         * Static convenience method to create and put a staged sheet settings
         * change in the map of staged settings changes.
         * 
         * @param sheet
         *              - the name of the sheet to change settings in
         * @param key
         *              - the sheet setting's key
         * @param value
         *              - the value to change the sheet setting to
         * @param clazz
         *              - values class type
         * @param <T>
         *              - value's type
         */
        private static <T> void putStagedSettingChange(String sheet,
            SettingsKey key, T value, Class<T> clazz) {
            putStagedSettingChange(
                new StagedSheetSettingChange<T>(sheet, key, value, clazz));
        }
        
        /**
         * Removes a staged settings change from the map of staged settings
         * changes.
         * <p>
         * Useful for if a user cancels out of a settings window or form other
         * than the main window or form, in which case
         * {@linkplain #clearStagedSettingChanges()} should be used.
         * 
         * @param key
         *            the settings key of the staged settings change to remove
         */
        private static void removeStagedSettingChange(String key) {
            // don't do anything if stagedChanges hasn't been created
            if (stagedChanges != null) {
                stagedChanges.remove(key);
            }
        }
        
        /**
         * Used to clear the staged settings changes back to zero.
         * <p>
         * Useful for if a user clicks 'Cancel' before 'Apply Changes', or after
         * all of the changes have been applied.
         */
        private static void clearStagedSettingChanges() {
            // do nothing if staged changes has not been created
            if (stagedChanges == null) { return; }
            
            stagedChanges.clear();
        }
    }
    
    /**
     * Class that holds a staged user settings change.
     * <p>
     * This class wraps a {@link SettingsKey} and a value.
     * 
     * @author Jonathan Henly
     */
    private static class StagedApplicationSettingChange<T>
        extends StagedSettingChange<T> {
        
        private String key;
        private T value;
        private Class<T> clazz; // ugly type erasure work around
        
        /**
         * Constructs a staged application settings change.
         * 
         * @param key
         *              - the key of the application setting
         * @param value
         *              - the value to change the application setting to
         * @param clazz
         *              - values class type
         */
        private StagedApplicationSettingChange(SettingsKey key, T value,
            Class<T> clazz) {
            // make a call to super to initialize map if it needs to be
            super();
            
            this.key = key.key();
            this.value = value;
            this.clazz = clazz;
        }
        
        @Override
        protected String key() { return key; }
        
        @Override
        protected T value() { return value; }
        
        @Override
        protected Class<T> clazz() { return clazz; }
        
    }
    
    /**
     * Class that holds a staged sheet settings change.
     * 
     * @author Jonathan Henly
     */
    private static class StagedSheetSettingChange<T>
        extends StagedSettingChange<T> {
        
        private String key;
        private T value;
        private Class<T> clazz; // ugly type erasure work around
        
        /**
         * Constructs a staged sheet settings change.
         * 
         * @param sheet
         *              - the name of the sheet to change settings in
         * @param key
         *              - the key of the sheet setting
         * @param value
         *              - the value to change the sheet setting to
         * @param clazz
         *              - values class type
         */
        private StagedSheetSettingChange(String sheet, SettingsKey key, T value,
            Class<T> clazz) {
            // make a call to super to initialize map if it needs to be
            super();
            
            this.key = sheet + key.key();
            this.value = value;
            this.clazz = clazz;
        }
        
        @Override
        protected String key() { return key; }
        
        @Override
        protected T value() { return value; }
        
        @Override
        protected Class<T> clazz() { return clazz; }
        
    }
    
}
