package com.bookiescrape.app.config;

/**
 * Enum representing the user setting keys.
 * <p>
 * These keys are used when storing or loading settings from a properties file
 * or preferences data store.
 *
 * @author Jonathan Henly
 */
enum SettingsKey {
    /* application settings */
    SETTINGS_LAST_UPDATE("last_settings_update", 0L), // def 0 means need update
    LAUNCH_ON_START("launch_on_start", false),
    SCRAPE_INTERVAL("auto_scrape_interval", 0), // def 0 means no autoscrape
    LAST_SCRAPE("last_scrape", 0L), // def 0 means scrape now
    EXCEL_FILE_PATH("excel/file_path"), // no default
    ALL_SHEETS("excel/all_sheets"), // no default, throw exception if not found
    
    /* - keys of settings shared by all sheets - */
    SHEET_FONT("excel/sheets/font", "Calibri"),
    SHEET_FONT_SIZE("excel/sheets/size", 11),
    COLS_SIZETOFIT("excel/sheets/cols_size_to_fit", true),
    ROWS_SIZETOFIT("excel/sheets/rows_size_to_fit", false),
    
    /* - individual sheet settings keys - */
    SCRAPE_URL("/scrape_url"), // no default, throw exception if not found
    SHEET_TITLE("/sheet/title"), // no default
    TITLE_ROW("/sheet/title_row", 0), //
    TITLE_COL("/sheet/title_col", 0), //
    SHEET_TABLE("/sheet/table/row", 1), //
    TEAMS_COL("/sheet/table/teams_col", 0), //
    OPENER("/sheet/table/opener", true),
    OPENER_COL("/sheet/table/opener_col", 1),
    BOOKIE_COL("/sheet/table/bookie_col", 2),
    KEEP_ORDER("/sheet/table/bookie_keep_order", true);
    
    /* enum members */
    private String key; // the key in the properties file/preference data store
    private String def; // the setting's string default
    private int idef; // the setting's int default
    private long ldef; // the setting's long default
    private double ddef; // the setting's double default
    private boolean bdef; // the setting's boolean default
    
    /** key - the setting's key in the properties file */
    private SettingsKey(String key) {
        this.key = key;
    }
    
    /* sdef - this setting's string default, if it has one */
    private SettingsKey(String key, String def) {
        this(key);
        this.def = def;
    }
    
    /* idef - this setting's int default, if it has one */
    private SettingsKey(String key, int idef) {
        this(key);
        this.idef = idef;
    }
    
    /* ldef - this setting's long default, if it has one */
    private SettingsKey(String key, long ldef) {
        this(key);
        this.ldef = ldef;
    }
    
    /* ddef - this setting's double default, if it has one */
    private SettingsKey(String key, double ddef) {
        this(key);
        this.ddef = ddef;
    }
    
    /* bdef - this setting's boolean default, if it has one */
    private SettingsKey(String key, boolean bdef) {
        this(key);
        this.bdef = bdef;
    }
    
    /** @return this setting's key as a string */
    String key() { return key; }
    
    /** @return this setting's string default, if it has one */
    String sdef() {
        return def;
    }
    
    /** @return this setting's int default, if it has one */
    int idef() {
        return idef;
    }
    
    /** @return this setting's int default, if it has one */
    long ldef() {
        return ldef;
    }
    
    /** @return this setting's double default, if it has one */
    double ddef() {
        return ddef;
    }
    
    /** @return this setting's boolean default, if it has one */
    boolean bdef() {
        return bdef;
    }
}
