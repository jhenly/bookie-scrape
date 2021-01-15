package com.bookiescrape.app.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bookiescrape.app.settings.Settings;
import com.bookiescrape.app.settings.SettingsKey;
import com.bookiescrape.app.settings.UserSettings;

/**
 * A mediator that facilitates communication between the application's font-end
 * and back-end.
 * 
 * @author Jonathan Henly
 */
public class ApplicationMediator {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationMediator.class);
    
    private Settings settings;
    
    /**************************************************************************
     *                                                                        *
     * Constructor(s)                                                         *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Constructs an application mediator with the specified user settings.
     * @param settings - the user's settings
     */
    public ApplicationMediator(Settings settings) {
        this.settings = settings;
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Public API                                                             *
     *                                                                        *
     *************************************************************************/
    
    /**
     * Gets the user's settings that this applicaiton mediator was constructed
     * with.
     * @return the user's settings
     */
    public Settings getUserSettings() { return settings; }
    
    /**
     * 
     * @param <T>
     * @param key
     * @param value
     * @param clazz
     */
    @Deprecated
    public <T> void changeApplicationSetting(SettingsKey key, T value, Class<T> clazz) {
        UserSettings.changeApplicationSetting(key, value, clazz);
    }
    
    
    /**************************************************************************
     *                                                                        *
     * Private API                                                            *
     *                                                                        *
     *************************************************************************/
    
    
}
