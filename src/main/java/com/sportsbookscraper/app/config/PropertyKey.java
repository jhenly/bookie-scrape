package com.sportsbookscraper.app.config;

/**
 * Enum representing the keys in a properties or preference file.
 *
 * @author Jonathan Henly
 */

enum PropertyKey {

    /* - keys of properties shared by all sheets - */
    EXCEL_FILE_PATH("excel.file.path"), // no default
    ALL_SHEETS("all.sheets"), // no default, throw exception if not found
    SHEET_FONT(ALL_SHEETS + ".font", "Calibri"),
    SHEET_FONT_SIZE(SHEET_FONT + ".size", 11),
    COLS_SIZETOFIT(ALL_SHEETS + ".cols.sizetofit", true),
    ROWS_SIZETOFIT(ALL_SHEETS + ".rows.sizetofit", false),

    /* - individual sheet property keys - */
    SCRAPE_URL(".scrape.url"), // no default, throw exception if not found
    SHEET_TITLE(".sheet.title"), // no default
    TITLE_ROW(SHEET_TITLE + ".row", 1),
    TITLE_COL(SHEET_TITLE + ".col", 0),
    SHEET_TABLE(".sheet.table", 1),
    TEAMS_COL(SHEET_TABLE + ".teams.col", 0),
    OPENER(SHEET_TABLE + ".opener", true),
    OPENER_COL(SHEET_TABLE + ".opener.col", 1),
    BOOKIE_COL(SHEET_TABLE + ".bookie.col", 2);

    /* enum members */
    private String key; // the key in the properties file
    private String def; // the key's string default
    private int idef; // the key's int default
    private boolean bdef; // the key's boolean default
    
    /** key - the property's key in the properties file */
    private PropertyKey(String key) {
        this.key = key;
    }
    
    /* sdef - this properties string default, if it has one */
    private PropertyKey(String key, String def) { this(key); this.def = def; }
    
    /* idef - this property's int default, if it has one */
    PropertyKey(String key, int idef) { this(key); this.idef = idef; }
    
    /* bdef - this property's boolean default, if it has one */
    PropertyKey(String key, boolean bdef) { this(key); this.bdef = bdef; }

    /** @return this property's key as a string */
    String key() { return key; }
    
    /** @return this property's string default, if it has one */
    String sdef() {
        return def;
    }
    
    /** @return this property's int default, if it has one */
    int idef() {
        return idef;
    }
    
    /** @return this property's boolean default, if it has one */
    boolean bdef() {
        return bdef;
    }
}