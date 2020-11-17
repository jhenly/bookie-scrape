package com.sportsbookscraper.app.config;

/**
 * Enums representing the keys in a properties file.
 *
 * @author Jonathan Henly
 */
enum PropertyKey {
    
    /* - keys of properties shared by all sheets - */
    EXCEL_FILE_PATH("excel.file.path"),
    ALL_SHEETS("all.sheets"),
    SHEET_FONT(ALL_SHEETS + ".font"),
    SHEET_FONT_SIZE(SHEET_FONT + ".size"),
    COLS_SIZETOFIT(ALL_SHEETS + ".cols.sizetofit"),
    ROWS_SIZETOFIT(ALL_SHEETS + ".rows.sizetofit"),
    
    /* - individual sheet property keys - */
    SCRAPE_URL(".scrape.url"),
    SHEET_TITLE(".sheet.title"),
    TITLE_ROW(SHEET_TITLE + ".row"),
    TITLE_COL(SHEET_TITLE + ".row"),
    SHEET_TABLE(".sheet.table"),
    TEAMS_COL(SHEET_TABLE + ".teams.col"),
    OPENER(SHEET_TABLE + ".opener"),
    OPENER_COL(SHEET_TABLE + ".opener.col"),
    BOOKIE_COL(SHEET_TABLE + ".bookie.col");
    
    
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
    String getsdef() {
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
