package com.sportsbookscraper.app.config;

import static com.sportsbookscraper.app.config.PropertyKey.ALL_SHEETS;
import static com.sportsbookscraper.app.config.PropertyKey.BOOKIE_COL;
import static com.sportsbookscraper.app.config.PropertyKey.COLS_SIZETOFIT;
import static com.sportsbookscraper.app.config.PropertyKey.EXCEL_FILE_PATH;
import static com.sportsbookscraper.app.config.PropertyKey.KEEP_ORDER;
import static com.sportsbookscraper.app.config.PropertyKey.LAUNCH_ON_START;
import static com.sportsbookscraper.app.config.PropertyKey.OPENER;
import static com.sportsbookscraper.app.config.PropertyKey.OPENER_COL;
import static com.sportsbookscraper.app.config.PropertyKey.ROWS_SIZETOFIT;
import static com.sportsbookscraper.app.config.PropertyKey.SCRAPE_INTERVAL;
import static com.sportsbookscraper.app.config.PropertyKey.SCRAPE_URL;
import static com.sportsbookscraper.app.config.PropertyKey.SHEET_FONT;
import static com.sportsbookscraper.app.config.PropertyKey.SHEET_FONT_SIZE;
import static com.sportsbookscraper.app.config.PropertyKey.SHEET_TABLE;
import static com.sportsbookscraper.app.config.PropertyKey.SHEET_TITLE;
import static com.sportsbookscraper.app.config.PropertyKey.TEAMS_COL;
import static com.sportsbookscraper.app.config.PropertyKey.TITLE_COL;
import static com.sportsbookscraper.app.config.PropertyKey.TITLE_ROW;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


/**
 * Loads {@code config.properties} file and populates global configs and
 * individual Excel workbook, sheet specific configs.
 * <p>
 * Example {@code config.properties} file:
 *
 * <pre>
 * # path to Excel file
 * excel.file.path=/absolute/or/relative/path/to/excel/file
 *
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
 * </pre>
 *
 * @author Jonathan Henly
 */
final class UserProperties extends AbstractSettings {
    // TODO remove CONFIG_CLASS_PATH <- it's just for debugging
    private final String CONFIG_CLASS_PATH = "./config/config.properties";
    
    private final Properties props;
    private final String excelFilePath;
    private final List<String> allSheets;
    // individual sheet properties holder
    private final List<SheetSettings> sheetProps;
    
    UserProperties(String propertiesFile)
        throws RequiredSettingNotFoundException, IOException {
        this(propertiesFile, null);
    }
    
    // NOTE: do not change the order of calls in the following constructor, some
    // calls depend on other calls happening prior
    UserProperties(String propertiesFile, String pathToExcelFile)
        throws IOException, RequiredSettingNotFoundException {
        // load user settings from config.properties file
        props = loadProps(propertiesFile);
        
        if (pathToExcelFile != null) {
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
        sheetProps = loadSheetProperties(allSheets);
    }
    
    /* loads properties from a passed in file, or throws IOException */
    private Properties loadProps(String filename) throws IOException {
        Properties props = new Properties();
        
        try (InputStream input = UserProperties.class.getClassLoader()
            .getResourceAsStream(filename)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
            }
            
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return props;
    }
    
    /* initialize application specific properties */
    private void initApplicationProperties() {
        launchOnStart = getBoolPropOrDefault(LAUNCH_ON_START);
        scrapeInterval = getIntPropOrDefault(SCRAPE_INTERVAL);
    }
    
    /* splits comma separated sheet names to an unmodifiable list, or throws */
    private List<String> initAllSheets()
        throws RequiredSettingNotFoundException {
        // will throw if ALL_SHEETS property is not in config.properties
        String tmpAllSheets = getRequiredPropertyOrThrow(ALL_SHEETS);
        
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
    
    /* initialize properties shared across all sheets */
    private void initSharedProperties() {
        font = getStrPropOrDefault(SHEET_FONT);
        fontSize = getIntPropOrDefault(SHEET_FONT_SIZE);
        colSizeToFit = getBoolPropOrDefault(COLS_SIZETOFIT);
        rowSizeToFit = getBoolPropOrDefault(ROWS_SIZETOFIT);
    }
    
    /* helper method used by methods retrieving required properties */
    private String getRequiredPropertyOrThrow(String property)
        throws RequiredSettingNotFoundException {
        String propValue = props.getProperty(property);
        if (propValue == null) {
            throw new RequiredSettingNotFoundException(property,
                CONFIG_CLASS_PATH);
        }
        return propValue;
    }
    
    /* overloaded helper method */
    private String getRequiredPropertyOrThrow(PropertyKey pk)
        throws RequiredSettingNotFoundException {
        return getRequiredPropertyOrThrow(pk.key());
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
    private int getIntPropOrDefault(PropertyKey pk) {
        return getIntPropOrDefault(pk.key(), pk.idef());
    }
    
    /* helper int property getter */
    private int getIntPropOrDefault(String sheet, PropertyKey pk) {
        return getIntPropOrDefault(sheet + pk.key(), pk.idef());
    }
    
    /* helper string property getter */
    private String getStrPropOrDefault(String prop, String def) {
        String tmp = props.getProperty(prop);
        return (tmp != null) ? tmp : def;
    }
    
    /* helper string property getter */
    private String getStrPropOrDefault(PropertyKey pk) {
        return getStrPropOrDefault(pk.key(), pk.sdef());
    }
    
    /* helper string property getter */
    private String getStrPropOrDefault(String sheet, PropertyKey pk) {
        return getStrPropOrDefault(sheet + pk.key(), pk.sdef());
    }
    
    /* helper boolean property getter */
    private boolean getBoolPropOrDefault(String prop, boolean def) {
        String tmp = props.getProperty(prop);
        return (tmp != null) ? Boolean.parseBoolean(tmp) : def;
    }
    
    /* helper boolean property getter */
    private boolean getBoolPropOrDefault(PropertyKey pk) {
        return getBoolPropOrDefault(pk.key(), pk.bdef());
    }
    
    /* helper boolean property getter */
    private boolean getBoolPropOrDefault(String sheet, PropertyKey pk) {
        return getBoolPropOrDefault(sheet + pk.key(), pk.bdef());
    }
    
    /* --- Public Accessors Section --- */
    
    @Override
    public int getAutoScrapeInterval() { return scrapeInterval; }
    
    @Override
    public boolean launchOnStart() { return launchOnStart; }
    
    /**
     * @return the path to the Excel file, if set in {@code config.properties}
     */
    @Override
    public String getExcelFilePath() { return excelFilePath; }
    
    @Override
    public List<String> getSheetNames() { return allSheets; }
    
    @Override
    public String getSheetFont() { return font; }
    
    @Override
    public int getSheetFontSize() { return fontSize; }
    
    @Override
    public boolean colsAreSizedToFit() { return colSizeToFit; }
    
    @Override
    public boolean rowsAreSizedToFit() { return rowSizeToFit; }
    
    @Override
    public SheetSettings getSheetSettings(int index) {
        return sheetProps.get(index);
    }
    
    @Override
    public SheetSettings getSheetSettings(String name) {
        for (SheetSettings sds : sheetProps) {
            if (sds.getSheetName().equals(name)) { return sds; }
        }
        
        return null;
    }
    
    /* -- SheetProperties Section -- */
    
    /* iterates over sheet names and creates sheet properties */
    private List<SheetSettings> loadSheetProperties(List<String> sheets) {
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
        sp.scrapeUrl = getStrPropOrDefault(sheet, SCRAPE_URL);
        
        sp.sheetTitle = getStrPropOrDefault(sheet, SHEET_TITLE);
        sp.titleRow = getIntPropOrDefault(sheet, TITLE_ROW);
        sp.titleCol = getIntPropOrDefault(sheet, TITLE_COL);
        sp.opener = getBoolPropOrDefault(sheet, OPENER);
        sp.teamsCol = getIntPropOrDefault(sheet, TEAMS_COL);
        sp.keepExisting = getBoolPropOrDefault(sheet, KEEP_ORDER);
        
        // now we have to range check any row or column index, for instance
        // tableRow should be greater than titleRow
        int tmp;
        
        // get tableRow prop, set it to titleRow + 1 if it's less than titleRow
        tmp = getIntPropOrDefault(sheet, SHEET_TABLE);
        sp.tableRow = (tmp > sp.titleRow) ? tmp : sp.titleRow + 1;
        
        // get openerCol prop, set it to teamsCol + 1 if it's less than teamsCol
        tmp = getIntPropOrDefault(sheet, OPENER_COL);
        sp.openerCol = (tmp > sp.teamsCol) ? tmp : sp.teamsCol + 1;
        
        // get openerCol prop, set it to teamsCol + 1 if it's less than teamsCol
        tmp = getIntPropOrDefault(sheet, BOOKIE_COL);
        // account for a sheet not having an opener column
        if (sp.hasOpener()) {
            sp.bookieCol = (tmp > sp.openerCol) ? tmp : sp.openerCol + 1;
        } else {
            sp.bookieCol = (tmp > sp.teamsCol) ? tmp : sp.teamsCol + 1;
        }
        
        return sp;
    }
    
    
} // public final class WorkbookProperties
