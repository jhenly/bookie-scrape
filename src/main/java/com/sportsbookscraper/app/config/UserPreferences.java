package com.sportsbookscraper.app.config;

import static com.sportsbookscraper.app.config.SettingsKey.ALL_SHEETS;
import static com.sportsbookscraper.app.config.SettingsKey.BOOKIE_COL;
import static com.sportsbookscraper.app.config.SettingsKey.COLS_SIZETOFIT;
import static com.sportsbookscraper.app.config.SettingsKey.EXCEL_FILE_PATH;
import static com.sportsbookscraper.app.config.SettingsKey.KEEP_ORDER;
import static com.sportsbookscraper.app.config.SettingsKey.LAUNCH_ON_START;
import static com.sportsbookscraper.app.config.SettingsKey.OPENER;
import static com.sportsbookscraper.app.config.SettingsKey.OPENER_COL;
import static com.sportsbookscraper.app.config.SettingsKey.ROWS_SIZETOFIT;
import static com.sportsbookscraper.app.config.SettingsKey.SCRAPE_INTERVAL;
import static com.sportsbookscraper.app.config.SettingsKey.SCRAPE_URL;
import static com.sportsbookscraper.app.config.SettingsKey.SHEET_FONT;
import static com.sportsbookscraper.app.config.SettingsKey.SHEET_FONT_SIZE;
import static com.sportsbookscraper.app.config.SettingsKey.SHEET_TABLE;
import static com.sportsbookscraper.app.config.SettingsKey.SHEET_TITLE;
import static com.sportsbookscraper.app.config.SettingsKey.TEAMS_COL;
import static com.sportsbookscraper.app.config.SettingsKey.TITLE_COL;
import static com.sportsbookscraper.app.config.SettingsKey.TITLE_ROW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * This class is used to load user settings from a backing data store, which is
 * operating system dependent.
 * <p>
 * This class contains methods to retrieve application specific settings and
 * individual settings for sheets in an Excel workbook.
 * <p>
 * <b>Note:</b> if no user settings can be found via Java's preferences API,
 * then the {@linkplain UserProperties} class will be used to load settings from
 * {@code config.properties}. After which, the application will try to use the
 * Java preferences API to save the loaded settings to an OS dependent backing
 * data store.
 * 
 * @author Jonathan Henly
 */
class UserPreferences extends AbstractSettings {
    private final List<String> allSheets;
    // individual sheet properties holder
    private final List<SheetSettings> sheetPrefs;
    private final Preferences prefs;
    
    UserPreferences(Preferences userPreferences)
        throws IOException, RequiredSettingNotFoundException {
        this(userPreferences, null);
    }
    
    UserPreferences(Preferences userPreferences, String pathToExcelFile)
        throws IOException, RequiredSettingNotFoundException {
        prefs = userPreferences;
        
        if (pathToExcelFile != null) {
            excelFilePath = getRequiredPreferenceOrThrow(EXCEL_FILE_PATH);
        } else {
            excelFilePath = pathToExcelFile;
        }
        
        // initialize application specific properties, i.e. launchOnStart, etc.
        initApplicationPreferences();
        // create unmodifiable list of sheet names
        allSheets = initAllSheets();
        // initialize properties shared across all sheets, i.e. font, etc.
        initSharedPreferences();
        // create list of all individual sheet properties
        sheetPrefs = loadSheetPreferences(allSheets);
    }
    
    /* initialize application specific preferences */
    private void initApplicationPreferences() {
        launchOnStart = getBoolPreference(LAUNCH_ON_START);
        scrapeInterval = getIntPreference(SCRAPE_INTERVAL);
        lastScrape = getLongPreference(SettingsKey.LAST_SCRAPE);
    }
    
    /* splits comma separated sheet names to an unmodifiable list, or throws */
    private List<String> initAllSheets()
        throws RequiredSettingNotFoundException {
        // will throw if ALL_SHEETS property is not in config.properties
        String tmpAllSheets = getRequiredPreferenceOrThrow(ALL_SHEETS);
        
        String[] sheetNames = tmpAllSheets.split(",");
        List<String> nonEmptySheetNames = new ArrayList<>(sheetNames.length);
        
        // iterate over sheetNames, remove trail/leading whitespace and add to
        // list if not an empty string
        for (int i = 0; i < sheetNames.length; i++) {
            String tmp = sheetNames[i].strip();
            if (!tmp.isEmpty()) {
                nonEmptySheetNames.add(tmp);
            }
        }
        
        // return unmodifiable list, it should not be changed
        return Collections.unmodifiableList(nonEmptySheetNames);
    }
    
    /* initialize preferences shared across all sheets */
    private void initSharedPreferences() {
        font = getStrPreference(SHEET_FONT);
        fontSize = getIntPreference(SHEET_FONT_SIZE);
        colSizeToFit = getBoolPreference(COLS_SIZETOFIT);
        rowSizeToFit = getBoolPreference(ROWS_SIZETOFIT);
    }
    
    /* helper method used by methods retrieving required preferences */
    private String getRequiredPreferenceOrThrow(SettingsKey key)
        throws RequiredSettingNotFoundException {
        String prefValue = prefs.get(key.key(), null);
        
        if (prefValue == null) {
            throw new RequiredSettingNotFoundException(key);
        }
        
        return prefValue;
    }
    
    /* helper method to return a string preference or a key's default string */
    private String getStrPreference(SettingsKey key) {
        return prefs.get(key.key(), key.sdef());
    }
    
    /* helper method to return an int preference or a key's default int */
    private int getIntPreference(SettingsKey key) {
        return prefs.getInt(key.key(), key.idef());
    }
    
    /* helper method to return a long preference or a key's default long */
    private long getLongPreference(SettingsKey key) {
        return prefs.getLong(key.key(), key.ldef());
    }
    
    /* helper method to return a boolean preference or a key's def boolean */
    private boolean getBoolPreference(SettingsKey key) {
        return prefs.getBoolean(key.key(), key.bdef());
    }
    
    /* helper method to return a string preference or a key's default string */
    private String getStrSheetPreference(String sheet, SettingsKey key) {
        return prefs.get(sheet + key.key(), key.sdef());
    }
    
    /* helper method to return an int preference or a key's default int */
    private int getIntSheetPreference(String sheet, SettingsKey key) {
        return prefs.getInt(key.key(), key.idef());
    }
    
    /* helper method to return a boolean preference or a key's def boolean */
    private boolean getBoolSheetPreference(String sheet, SettingsKey key) {
        return prefs.getBoolean(sheet + key.key(), key.bdef());
    }
    
    /* --- Interface Methods Not Covered By Abstract Settings --- */
    
    @Override
    public List<String> getSheetNames() { return allSheets; }
    
    @Override
    public SheetSettings getSheetSettings(int index) {
        return sheetPrefs.get(index);
    }
    
    @Override
    public SheetSettings getSheetSettings(String name) {
        for (SheetSettings sds : sheetPrefs) {
            if (sds.getSheetName().equals(name)) { return sds; }
        }
        
        return null;
    }
    
    /* -- Sheet Preferences Section -- */
    
    /* iterates over sheet names and creates sheet properties */
    private List<SheetSettings> loadSheetPreferences(List<String> sheets) {
        List<SheetSettings> sprops = new ArrayList<>(sheets.size());
        
        // build sheet properties for each sheet
        for (String sheetName : sheets) {
            sprops.add(buildSheetProperties(sheetName));
        }
        
        return Collections.unmodifiableList(sprops);
    }
    
    /* builds sheet properties using the supplied sheet name */
    private SheetSettings buildSheetProperties(String sheet) {
        AbstractSheetSettings sp = new AbstractSheetSettings();
        
        // set this sheet's sheet name
        sp.sheetName = sheet;
        
        // if no url then default, will be handled later in mediator
        sp.scrapeUrl = getStrSheetPreference(sheet, SCRAPE_URL);
        
        sp.sheetTitle = getStrSheetPreference(sheet, SHEET_TITLE);
        sp.titleRow = getIntSheetPreference(sheet, TITLE_ROW);
        sp.titleCol = getIntSheetPreference(sheet, TITLE_COL);
        sp.opener = getBoolSheetPreference(sheet, OPENER);
        sp.teamsCol = getIntSheetPreference(sheet, TEAMS_COL);
        sp.keepExisting = getBoolSheetPreference(sheet, KEEP_ORDER);
        
        // now we have to range check any row or column index, for instance
        // tableRow should be greater than titleRow
        int tmp;
        
        // get tableRow prop, set it to titleRow + 1 if it's less than titleRow
        tmp = getIntSheetPreference(sheet, SHEET_TABLE);
        sp.tableRow = (tmp > sp.titleRow) ? tmp : sp.titleRow + 1;
        
        // get openerCol prop, set it to teamsCol + 1 if it's less than teamsCol
        tmp = getIntSheetPreference(sheet, OPENER_COL);
        sp.openerCol = (tmp > sp.teamsCol) ? tmp : sp.teamsCol + 1;
        
        // get openerCol prop, set it to teamsCol + 1 if it's less than teamsCol
        tmp = getIntSheetPreference(sheet, BOOKIE_COL);
        // account for a sheet not having an opener column
        if (sp.hasOpener()) {
            sp.bookieCol = (tmp > sp.openerCol) ? tmp : sp.openerCol + 1;
        } else {
            sp.bookieCol = (tmp > sp.teamsCol) ? tmp : sp.teamsCol + 1;
        }
        
        return sp;
    }
}
