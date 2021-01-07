package com.bookiescrape.app.settings;

import static com.bookiescrape.app.settings.SettingsKey.SETTINGS_LAST_UPDATE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import com.bookiescrape.app.util.FileUtils;

/**
 * Static factory class to serve concrete implementations of {@link Settings}.
 *
 * @author Jonathan Henly
 */
public final class UserSettings {
    private static Settings userSettings;
    
    // this class' singleton properties instance
    private static Properties userProps;
    // this class' current properties file, if properties are being used
    private static Path curUserPropsFilePath;
    
    
    /* gets singleton Properties instance: userProps */
    private static Properties getUserProperties() { return userProps; }
    
    /* gets the current user properties file path, or null */
    private static Path getCurrentPropertiesFilePath() { return curUserPropsFilePath; }
    
    private static Settings getUserSettings() { return userSettings; }
    
    // don't subclass this class
    private UserSettings() {}
    
    public static void listSettings(PrintStream out) {
        userSettings.listSettings(out);
    }
    
    public static Settings loadSettings(Path propsFilePath) throws IOException {
        UserProperties props = new UserProperties(propsFilePath);
        userProps = props.getProperties();
        curUserPropsFilePath = propsFilePath;
        
        userSettings = props;
        
        return userSettings;
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
    public static <T> void changeApplicationSetting(SettingsKey key, T value, Class<T> clazz) {
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
    public static <T> void changeSheetSetting(String sheet, SettingsKey key, T value, Class<T> clazz) {
        StagedSettingChange.putStagedSettingChange(sheet, key, value, clazz);
    }
    
    /**
     * Applies all queued settings changes.
     * <p>
     * <b>Note:</b> this static method does nothing if no changes have been
     * queued, but the operations contained in this method are expensive and
     * should be avoided when unnecessary.
     * 
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
    public static Settings applySettingsChanges() throws FileNotFoundException, IOException {
        
        // get map of staged settings changes
        HashMap<String, StagedSettingChange<?>> staged = StagedSettingChange.getStagedChanges();
        
        if (staged == null || staged.isEmpty()) {
            // expensive operation but onus is on caller
            return getUserSettings();
        }
        
        
        return applySettingsChanges(getUserProperties(), staged);
        
    }
    
    /* helper properties method for applySettingsChanges() */
    private static Settings applySettingsChanges(Properties props, HashMap<String, StagedSettingChange<?>> staged)
        throws IOException {
        
        // iterate over queued settings changes and update preferences
        for (Entry<String, StagedSettingChange<?>> entry : staged.entrySet()) {
            StagedSettingChange<?> ssc = entry.getValue();
            
            // properties is easy, its put method takes (Object, Object)
            props.put(ssc.key(), ssc.value().toString());
        }
        
        // clear staged settings changes
        clearQueuedSettingsChanges();
        
        // update last-updated time stamp
        props.put(SETTINGS_LAST_UPDATE.key(), ((Long) System.currentTimeMillis()).toString());
        
        Path curPropsFile = getCurrentPropertiesFilePath();
        // write properties to current properties file path, if it applies
        if (curPropsFile != null) {
            FileUtils.writePropertiesToFile(props, curPropsFile);
        }
        
        userSettings = new UserProperties(props);
        
        // return newly applied settings
        return userSettings;
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
        private static <T> HashMap<String, StagedSettingChange<?>> getStagedChanges() { return stagedChanges; }
        
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
        private static <T> void putStagedSettingChange(StagedSettingChange<T> ssc) {
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
        private static <T> void putStagedSettingChange(SettingsKey key, T value, Class<T> clazz) {
            
            putStagedSettingChange(new StagedApplicationSettingChange<T>(key, value, clazz));
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
        private static <T> void putStagedSettingChange(String sheet, SettingsKey key, T value, Class<T> clazz) {
            putStagedSettingChange(new StagedSheetSettingChange<T>(sheet, key, value, clazz));
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
    private static class StagedApplicationSettingChange<T> extends StagedSettingChange<T> {
        
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
        private StagedApplicationSettingChange(SettingsKey key, T value, Class<T> clazz) {
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
    private static class StagedSheetSettingChange<T> extends StagedSettingChange<T> {
        
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
        private StagedSheetSettingChange(String sheet, SettingsKey key, T value, Class<T> clazz) {
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
