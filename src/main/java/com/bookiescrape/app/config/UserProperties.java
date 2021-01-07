package com.bookiescrape.app.config;

import static com.bookiescrape.app.config.SettingsKey.ALL_SHEETS;
import static com.bookiescrape.app.config.SettingsKey.BOOKIE_COL;
import static com.bookiescrape.app.config.SettingsKey.COLS_SIZETOFIT;
import static com.bookiescrape.app.config.SettingsKey.EXCEL_FILE_PATH;
import static com.bookiescrape.app.config.SettingsKey.KEEP_ORDER;
import static com.bookiescrape.app.config.SettingsKey.LAST_SCRAPE;
import static com.bookiescrape.app.config.SettingsKey.LAUNCH_ON_START;
import static com.bookiescrape.app.config.SettingsKey.OPENER;
import static com.bookiescrape.app.config.SettingsKey.OPENER_COL;
import static com.bookiescrape.app.config.SettingsKey.ROWS_SIZETOFIT;
import static com.bookiescrape.app.config.SettingsKey.SCRAPE_INTERVAL;
import static com.bookiescrape.app.config.SettingsKey.SCRAPE_URL;
import static com.bookiescrape.app.config.SettingsKey.SETTINGS_LAST_UPDATE;
import static com.bookiescrape.app.config.SettingsKey.SHEET_FONT;
import static com.bookiescrape.app.config.SettingsKey.SHEET_FONT_SIZE;
import static com.bookiescrape.app.config.SettingsKey.SHEET_TABLE;
import static com.bookiescrape.app.config.SettingsKey.SHEET_TITLE;
import static com.bookiescrape.app.config.SettingsKey.TEAMS_COL;
import static com.bookiescrape.app.config.SettingsKey.TITLE_COL;
import static com.bookiescrape.app.config.SettingsKey.TITLE_ROW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


/**
 * This class is used to load user settings from the {@code config.properties}
 * file, by default.
 * <p>
 * This class has a constructor that takes a path to a properties file, if a
 * properties file other than {@code config.properties} is to be used. This
 * class also contains methods to retrieve application specific settings and
 * individual settings for sheets in an Excel workbook.
 * <p>
 * An example of the {@code config.properties} file follows:
 *
 * <pre>
 * # application settings
 *
 * # launch application on computer start (default is false)
 * app.launch.on.start=false
 * # auto scrape time interval, in minutes (default is 0, meaning no auto scrape)
 * app.auto.scrape.interval=0
 *
 * # path to Excel file
 * excel.file.path=
 * # list of all sheets
 * all.sheets=NFL,NCAAF
 *
 * # properties shared by all sheets
 *
 * all.sheets.font=Calibri
 * all.sheets.font.size=11
 * all.sheets.cols.sizetofit=true
 * all.sheets.rows.sizetofit=false
 *
 * # individual sheet properties
 *
 * # NFL sheet
 * NFL.scrape.url=https\://classic.sportsbookreview.com/betting-odds/nfl-football/money-line/
 * NFL.sheet.title=NFL Football
 * NFL.sheet.title.row=0
 * NFL.sheet.title.col=0
 * NFL.sheet.table.row=1
 * NFL.sheet.table.teams.col=0
 * NFL.sheet.table.opener=true
 * NFL.sheet.table.opener.col=1
 * NFL.sheet.table.bookie.col=2
 * NFL.sheet.table.bookie.keep.order=true
 * 
 * # NCAAF sheet
 * NCAAF.scrape.url=https\://classic.sportsbookreview.com/betting-odds/college-football/money-line/
 * NCAAF.sheet.title=College Football
 * NCAAF.sheet.title.row=0
 * NCAAF.sheet.title.col=0
 * NCAAF.sheet.table.row=1
 * NCAAF.sheet.table.teams.col=0
 * NCAAF.sheet.table.opener=true
 * NCAAF.sheet.table.opener.col=1
 * NCAAF.sheet.table.bookie.col=2
 * NCAAF.sheet.table.bookie.keep.order=true
 * </pre>
 *
 * @author Jonathan Henly
 */
final class UserProperties extends AbstractSettings {
    
    /**
     * TODO DELETE THIS DEBUGGING METHOD
     */
    @Override
    public void listSettings(PrintStream out) { props.list(out); }
    
    // class path to the default properties file
    static final String DEFAULT_PROPERTIES_FILE = "./config/config.properties";
    
    private final Properties props;
    private final List<String> allSheets;
    private String propsFilePath;
    
    /**
     * This constructor is used when the user's backing preference's data store
     * cannot be reached or is corrupted, and a user has supplied a non-default
     * properties file.
     * 
     * @param propertiesFile
     *                       - path to a specified properties file
     */
    UserProperties(String propertiesFile) throws RequiredSettingNotFoundException, IOException {
        this(propertiesFile, null);
    }
    
    /**
     * This constructor is used when the user's backing preference's data store
     * cannot be reached or is corrupted, and a user has supplied a non-default
     * properties file as well as a path to an Excel file.
     * <p>
     * If the specified path to the Excel file is {@code null} then this
     * constructor will use the Excel file path in the specified properties
     * file.
     * 
     * @param propertiesFile
     *                        - path to a specified properties file
     * @param pathToExcelFile
     *                        - if not {@code null}, a property overriding path
     *                        to an Excel file
     */
    UserProperties(String propertiesFilePath, String pathToExcelFile)
        throws IOException, RequiredSettingNotFoundException {
        this(null, propertiesFilePath, pathToExcelFile);
    }
    
    /**
     * Special constructor used to signal that the default properties file
     * should be used to load user settings.
     * <p>
     * This constructor should only be used if loading user settings from
     * preferences or from a specified properties file have failed.
     * <p>
     * If this constructor throws a {@code RequiredSheetNotFoundException}, then
     * nothing more can be done to load user settings. The caller of this method
     * should catch the exception and throw a new exception using the following:
     * <blockquote>
     * 
     * <pre>
     * throw new RequiredSheetNotFoundException(SettingsKey, String, String)
     * </pre>
     * 
     * </blockquote>
     * 
     * 
     * @param pathToExcelFile
     * @param cannonFodder
     * @throws RequiredSettingNotFoundException
     * @throws IOException
     * @see RequiredSettingNotFoundException#RequiredSettingNotFoundException(
     *      SettingsKey, String, String) RequiredSettingNotFoundException(
     *      SettingsKey, String, String)
     */
    UserProperties(String pathToExcelFile, boolean cannonFodder) throws IOException, RequiredSettingNotFoundException {
        this(null, DEFAULT_PROPERTIES_FILE, pathToExcelFile);
    }
    
    /* convenience constructor used by UserSettings */
    UserProperties(Properties props, String propertiesFilePath) throws RequiredSettingNotFoundException, IOException {
        this(props, propertiesFilePath, null);
    }
    
    /* main constructor that handles calls from all other constructors */
    UserProperties(Properties newProps, String propertiesFilePath, String pathToExcelFile)
        throws RequiredSettingNotFoundException, IOException {
        // assign propsFilePath in case it's needed to throw exceptions
        propsFilePath = propertiesFilePath;
        
        /* DO NOT CHANGE THE ORDER OF CALLS IN THIS CONSTRUCTOR some calls depend on
         * other calls happening prior */
        
        if (newProps == null) {
            // load properties from file if newProps is null
            props = loadPropertiesFromFile(propertiesFilePath);
        } else {
            // use newProps instead of loading properties from file
            props = newProps;
        }
        
        if (pathToExcelFile == null) {
            excelFilePath = getRequiredPropertyOrThrow(EXCEL_FILE_PATH);
        } else {
            excelFilePath = pathToExcelFile;
        }
        
        // initialize application specific properties, i.e. launchOnStart, etc.
        initApplicationProperties();
        // create unmodifiable list of sheet names
        allSheets = initAllSheets();
        // initialize properties shared across all sheets, i.e. font, etc.
        initSharedProperties();
        // create list of all individual sheet properties
        sheetSettings = loadSheetProperties(allSheets);
    }
    
    
    /* loads properties from a passed in file */
    private Properties loadPropertiesFromFile(String filePath) throws IOException, FileNotFoundException {
        
        Properties props = null;
        
        if (filePath.equals(DEFAULT_PROPERTIES_FILE)) {
            props = loadDefaultProperties();
        } else {
            props = loadPropertiesFromExternalFile(filePath);
        }
        return props;
    }
    
    /* */
    private Properties loadDefaultProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = UserProperties.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE)) {
            
            if (input == null) {
                // if input is null then a fatal error has occurred
                throw new FileNotFoundException(
                    "Internal properties file '" + DEFAULT_PROPERTIES_FILE + "' does not exist.");
            }
            
            props.load(input);
        } // don't need a catch statement, this method throws an IOException
        
        return props;
    }
    
    /* if IOException (other than FileNotFoundException) is thrown, then it's from
     * closing reader, which should very rarely (if ever) throw an IOException */
    private Properties loadPropertiesFromExternalFile(String filePath) throws FileNotFoundException, IOException {
        Properties props = new Properties();
        
        File propertiesFile = new File(filePath);
        
        try (FileReader reader = new FileReader(propertiesFile)) {
            props.load(reader);
        }
        
        return props;
    }
    
    /* initialize application specific properties */
    private void initApplicationProperties() {
        lastUpdatedTime = getLongPropOrDefault(SETTINGS_LAST_UPDATE);
        launchOnStart = getBoolPropOrDefault(LAUNCH_ON_START);
        scrapeInterval = getIntPropOrDefault(SCRAPE_INTERVAL);
        lastScrape = getLongPropOrDefault(LAST_SCRAPE); // should be 0L
    }
    
    /* splits comma separated sheet names to an unmodifiable list, or throws */
    private List<String> initAllSheets() {
        String allSheetsStr = getStrPropOrDefault(ALL_SHEETS);
        
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
    
    /* overloaded helper method */
    private String getRequiredPropertyOrThrow(SettingsKey pk) throws RequiredSettingNotFoundException {
        return getRequiredPropertyOrThrow(pk.key());
    }
    
    /* helper method used by methods retrieving required properties */
    private String getRequiredPropertyOrThrow(String property) throws RequiredSettingNotFoundException {
        // try to get the required property
        String propValue = props.getProperty(property);
        
        // throw if required property is null
        if (propValue == null) { throw new RequiredSettingNotFoundException(property, propsFilePath); }
        
        return propValue;
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
