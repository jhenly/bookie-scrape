package com.bookiescrape.app.settings;

import static com.bookiescrape.app.settings.SettingsKey.ALL_SHEETS;
import static com.bookiescrape.app.settings.SettingsKey.BOOKIE_COL;
import static com.bookiescrape.app.settings.SettingsKey.COLS_SIZETOFIT;
import static com.bookiescrape.app.settings.SettingsKey.EXCEL_FILE_PATH;
import static com.bookiescrape.app.settings.SettingsKey.KEEP_ORDER;
import static com.bookiescrape.app.settings.SettingsKey.LAST_SCRAPE;
import static com.bookiescrape.app.settings.SettingsKey.LAUNCH_ON_START;
import static com.bookiescrape.app.settings.SettingsKey.OPENER;
import static com.bookiescrape.app.settings.SettingsKey.OPENER_COL;
import static com.bookiescrape.app.settings.SettingsKey.ROWS_SIZETOFIT;
import static com.bookiescrape.app.settings.SettingsKey.SCRAPE_INTERVAL;
import static com.bookiescrape.app.settings.SettingsKey.SCRAPE_URL;
import static com.bookiescrape.app.settings.SettingsKey.SETTINGS_LAST_UPDATE;
import static com.bookiescrape.app.settings.SettingsKey.SHEET_FONT;
import static com.bookiescrape.app.settings.SettingsKey.SHEET_FONT_SIZE;
import static com.bookiescrape.app.settings.SettingsKey.SHEET_TABLE;
import static com.bookiescrape.app.settings.SettingsKey.SHEET_TITLE;
import static com.bookiescrape.app.settings.SettingsKey.TEAMS_COL;
import static com.bookiescrape.app.settings.SettingsKey.TITLE_COL;
import static com.bookiescrape.app.settings.SettingsKey.TITLE_ROW;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.bookiescrape.app.config.UserSettings;
import com.bookiescrape.app.util.FileUtils;


/**
 * This class is used to load user settings from a {@code .properties}
 * file.
 *
 * @author Jonathan Henly
 */
final class UserProperties extends AbstractSettings {
    
    /**
     * TODO DELETE THIS DEBUGGING METHOD
     */
    @Override
    public void listSettings(PrintStream out) { props.list(out); }
    
    private final Properties props;
    private final List<String> allSheets;
    
    UserProperties(Path propsPath) throws IOException {
        this(FileUtils.loadPropertiesFile(propsPath));
    }
    
    UserProperties(Properties properties) {
        props = properties;
        
        // initialize application specific properties, i.e. launchOnStart, etc.
        initApplicationProperties();
        // create unmodifiable list of sheet names
        allSheets = initAllSheets();
        // initialize properties shared across all sheets, i.e. font, etc.
        initSharedProperties();
        // create list of all individual sheet properties
        sheetSettings = loadSheetProperties(allSheets);
    }
    
    /* initialize application specific properties */
    private void initApplicationProperties() {
        lastUpdatedTime = getLongPropOrDefault(SETTINGS_LAST_UPDATE);
        launchOnStart = getBoolPropOrDefault(LAUNCH_ON_START);
        scrapeInterval = getIntPropOrDefault(SCRAPE_INTERVAL);
        lastScrape = getLongPropOrDefault(LAST_SCRAPE); // should be 0L
        excelFilePath = getStrPropOrDefault(EXCEL_FILE_PATH);
    }
    
    /* splits comma separated sheet names to an unmodifiable list, or throws */
    private List<String> initAllSheets() {
        String allSheetsStr = getStrPropOrDefault(ALL_SHEETS);
        
        if (allSheetsStr == null) { return Collections.emptyList(); }
        
        // convert non-empty comma delimited sheet names to List<String>
        List<String> nonEmptySheetNames = Utils.commaDelimitedStringToList(allSheetsStr);
        
        
        // return unmodifiable list, it should not be changed
        return Collections.unmodifiableList(nonEmptySheetNames);
    }
    
    /* initialize properties shared across all sheets */
    private void initSharedProperties() {
        font = getStrPropOrDefault(SHEET_FONT);
        fontSize = getIntPropOrDefault(SHEET_FONT_SIZE);
        colSizeToFit = getBoolPropOrDefault(COLS_SIZETOFIT);
        rowSizeToFit = getBoolPropOrDefault(ROWS_SIZETOFIT);
    }
    
    /* helper int property getter */
    private int getIntPropOrDefault(String prop, int def) {
        String tmp = props.getProperty(prop);
        
        if (tmp != null) {
            int val;
            try {
                val = Integer.parseInt(tmp);
            } catch (NumberFormatException nfe) {
                return def;
            }
            
            return (val >= 0) ? val : def;
        }
        
        return def;
    }
    
    /* helper int property getter */
    private int getIntPropOrDefault(SettingsKey sk) {
        return getIntPropOrDefault(sk.key(), sk.idef());
    }
    
    /* helper int property getter */
    private int getIntPropOrDefault(String sheet, SettingsKey sk) {
        return getIntPropOrDefault(sheet + sk.key(), sk.idef());
    }
    
    /* helper long property getter */
    private long getLongPropOrDefault(String prop, long def) {
        String tmp = props.getProperty(prop);
        if (tmp != null) {
            long val;
            try {
                val = Long.parseLong(tmp);
            } catch (NumberFormatException nfe) {
                return def;
            }
            
            return (val >= 0L) ? val : def;
        }
        
        return def;
    }
    
    /* helper long property getter */
    private long getLongPropOrDefault(SettingsKey sk) {
        return getLongPropOrDefault(sk.key(), sk.ldef());
    }
    
    /* helper lomg property getter */
    private long getLongPropOrDefault(String sheet, SettingsKey sk) {
        return getLongPropOrDefault(sheet + sk.key(), sk.ldef());
    }
    
    /* helper string property getter */
    private String getStrPropOrDefault(String prop, String def) {
        String tmp = props.getProperty(prop);
        return (tmp != null) ? tmp : def;
    }
    
    /* helper string property getter */
    private String getStrPropOrDefault(SettingsKey sk) {
        return getStrPropOrDefault(sk.key(), sk.sdef());
    }
    
    /* helper string property getter */
    private String getStrPropOrDefault(String sheet, SettingsKey sk) {
        return getStrPropOrDefault(sheet + sk.key(), sk.sdef());
    }
    
    /* helper boolean property getter */
    private boolean getBoolPropOrDefault(String prop, boolean def) {
        String tmp = props.getProperty(prop);
        return (tmp != null) ? Boolean.parseBoolean(tmp) : def;
    }
    
    /* helper boolean property getter */
    private boolean getBoolPropOrDefault(SettingsKey sk) {
        return getBoolPropOrDefault(sk.key(), sk.bdef());
    }
    
    /* helper boolean property getter */
    private boolean getBoolPropOrDefault(String sheet, SettingsKey sk) {
        return getBoolPropOrDefault(sheet + sk.key(), sk.bdef());
    }
    
    /* --- Interface Methods Not Covered By Abstract Settings --- */
    
    @Override
    public List<String> getSheetNames() { return allSheets; }
    
    /* -- SheetProperties Section -- */
    
    /* iterates over sheet names and creates sheet properties */
    private List<SheetSettings> loadSheetProperties(List<String> sheets) {
        List<SheetSettings> sprops = new ArrayList<>(sheets.size());
        
        // build sheet properties for each sheet
        for (String sheetName : sheets) {
            sprops.add(buildSheetSettings(sheetName));
        }
        
        return Collections.unmodifiableList(sprops);
    }
    
    /* builds sheet properties using the supplied sheet name */
    @Override
    protected SheetSettings buildSheetSettings(String sheetName) {
        AbstractSheetSettings sp = new AbstractSheetSettings();
        
        // set this sheet's sheet name
        sp.sheetName = sheetName;
        
        // if no url then default, will be handled later in mediator
        sp.scrapeUrl = getStrPropOrDefault(sheetName, SCRAPE_URL);
        
        sp.sheetTitle = getStrPropOrDefault(sheetName, SHEET_TITLE);
        sp.titleRow = getIntPropOrDefault(sheetName, TITLE_ROW);
        sp.titleCol = getIntPropOrDefault(sheetName, TITLE_COL);
        sp.opener = getBoolPropOrDefault(sheetName, OPENER);
        sp.teamsCol = getIntPropOrDefault(sheetName, TEAMS_COL);
        sp.keepExisting = getBoolPropOrDefault(sheetName, KEEP_ORDER);
        
        // now we have to range check any row or column index, for instance
        // tableRow should be greater than titleRow
        int tmp;
        
        // get tableRow prop, set it to titleRow + 1 if it's less than titleRow
        tmp = getIntPropOrDefault(sheetName, SHEET_TABLE);
        sp.tableRow = (tmp > sp.titleRow) ? tmp : sp.titleRow + 1;
        
        // get openerCol prop, set it to teamsCol + 1 if it's less than teamsCol
        tmp = getIntPropOrDefault(sheetName, OPENER_COL);
        sp.openerCol = (tmp > sp.teamsCol) ? tmp : sp.teamsCol + 1;
        
        // get openerCol prop, set it to teamsCol + 1 if it's less than teamsCol
        tmp = getIntPropOrDefault(sheetName, BOOKIE_COL);
        // account for a sheet not having an opener column
        if (sp.hasOpener()) {
            sp.bookieCol = (tmp > sp.openerCol) ? tmp : sp.openerCol + 1;
        } else {
            sp.bookieCol = (tmp > sp.teamsCol) ? tmp : sp.teamsCol + 1;
        }
        
        return sp;
    }
    
    /**
     * Used by {@link UserSettings} to create preferences from a loaded
     * properties file.
     * <p>
     * This method is <i>usually</i> only needed when a user first uses the
     * program, or when the preferences in the user's backing store have been
     * corrupted and need resetting.
     * 
     * @return user settings loaded from a properties file.
     */
    Properties getProperties() { return props; }
    
} // public final class WorkbookProperties
