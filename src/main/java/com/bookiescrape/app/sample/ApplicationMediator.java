package com.bookiescrape.app.sample;

import com.bookiescrape.app.settings.Settings;
import com.bookiescrape.app.settings.SettingsKey;
import com.bookiescrape.app.settings.UserSettings;

/**
 * 
 * @author JonathanHenly
 */
public class ApplicationMediator {
    
    private Settings settings;
    
    public ApplicationMediator(Settings settings) {
        this.settings = settings;
    }
    
    public <T> void changeApplicationSetting(SettingsKey key, T value, Class<T> clazz) {
        UserSettings.changeApplicationSetting(key, value, clazz);
    }
    
}
