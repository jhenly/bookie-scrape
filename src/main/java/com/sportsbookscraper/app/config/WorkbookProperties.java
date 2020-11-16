package com.sportsbookscraper.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Loads {@code config.properties} file and populates global configs and individual configs
 * Excel workbook
 * sheet specific configs.
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
 * 
 * @author Jonathan Henly
 *
 */
public final class WorkbookProperties {
	
	// don't subclass this class
	private WorkbookProperties() {}
	
	
	/* -- Static Properties Section -- */
	
	public static final String CONFIG_CLASS_PATH = "./config/config.properties";
	
	/*  - properties shared by all sheets -  */
	public static final String EXCEL_FILE_PATH = "excel.file.path";
	public static final String ALL_SHEETS = "all.sheets";
	public static final String SHEET_FONT = ALL_SHEETS + ".font";
	public static final String SHEET_FONT_SIZE = SHEET_FONT + ".size";
	public static final String COLS_SIZETOFIT = ALL_SHEETS + ".cols.sizetofit";
	public static final String ROWS_SIZETOFIT = ALL_SHEETS + ".rows.sizetofit";
	
	/*  - individual sheet properties -  */
	
	private static final String SCRAPE_URL = ".scrape.url";
	private static final String SHEET_TITLE = ".sheet.title";
	private static final String TITLE_ROW = SHEET_TITLE + ".row";
	private static final String TITLE_COL = SHEET_TITLE + ".row";
	private static final String SHEET_TABLE = ".sheet.table";
	private static final String TEAMS_COL = SHEET_TABLE + ".teams.col";
	private static final String OPENER = SHEET_TABLE + ".opener";
	private static final String OPENER_COL = SHEET_TABLE + ".opener.col";
	private static final String BOOKIE_COL = SHEET_TABLE + ".bookie.col";
	
	/*  - Begin static load config.properties file section -  */
	private static final Properties props;
	private static final String excelFilePath;
	private static final List<String> allSheets;
	
	// properties shared by all sheets
	private static final String font;
	private static final String DEF_FONT = "Calibri";
	private static final int fontSize;
	private static final int DEF_FONT_SIZE = 11;
	private static final boolean colSizeToFit;
	private static final boolean rowSizeToFit;
	private static final boolean DEF_SIZE_TO_FIT = true;
	
	static {
		props = loadProps(WorkbookProperties.CONFIG_CLASS_PATH);
		excelFilePath = props.getProperty(EXCEL_FILE_PATH);
		allSheets = initAllSheets();
		
		// init shared properties
		font = getStrPropOrDefault(SHEET_FONT, DEF_FONT);
		fontSize = getIntPropOrDefault(SHEET_FONT_SIZE, DEF_FONT_SIZE);
		colSizeToFit = getBoolPropOrDefault(COLS_SIZETOFIT, DEF_SIZE_TO_FIT);
		rowSizeToFit = getBoolPropOrDefault(ROWS_SIZETOFIT, DEF_SIZE_TO_FIT);
	}
	
	private static Properties loadProps(String filename) {
		Properties props = new Properties();

		try (InputStream input = WorkbookProperties.class.getClassLoader()
				.getResourceAsStream(filename))
		{
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
			}

			props.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return props;
	}
	
	private static List<String> initAllSheets() {
		return Collections.unmodifiableList(
				Arrays.asList(props.getProperty(ALL_SHEETS)));
	}
	
	private static int getIntPropOrDefault(String prop, int def) {
		String tmp = props.getProperty(prop);
		if(tmp != null) {
			int val;
			try {
				val = Integer.parseInt(tmp);
			} catch(NumberFormatException nfe) {
				return def;
			}
			
			return (val >= 0) ? val : def;
		}
		
		return def; 
	}
	
	private static String getStrPropOrDefault(String prop, String def) {
		String tmp = props.getProperty(prop);		
		return (tmp != null) ? tmp : def; 
	}
	
	private static boolean getBoolPropOrDefault(String prop, boolean def) {
		String tmp = props.getProperty(prop);
		return (tmp != null) ? Boolean.parseBoolean(tmp) : def; 
	}
	/*  - End static load config.properties file section -  */
	
	/*  - Static accessors section -  */
	public static String getExcelFilePath() {
		return excelFilePath;
	}

	public static List<String> getAllSheets() {
		return allSheets;
	}
	
	public static String getSheetFont() {
		return font;
	}
	
	public static int getSheetFontSize() {
		return fontSize;
	}
	
	public static boolean getColSizeToFit() {
		return colSizeToFit;
	}
	
	public static boolean getRowSizeToFit() {
		return colSizeToFit;
	}
	
	public static SheetProperties getSheetProperties(int index) {
		return null;
	}
	
	/*  - End static accessors section -  */
	
	
	/* -- End Static Properties Section -- */
	
	
	/* -- SheetProperties Section -- */
	
	/**
	 * Class that wraps individual sheet properties with getter methods.
	 * 
	 * @author Jonathan Henly
	 */
	private static final class SheetProperties {
		private String scrapeUrl;
		private String sheetTitle;
		private int titleRow;
		private int titleCol;
		private int teamsCol;
		private boolean opener;
		private int openerCol;
		private int bookieCol;
		
		private SheetProperties() {}

		/**
		 * @return the url to scrape
		 */
		public String getScrapeUrl() {
			return scrapeUrl;
		}

		/**
		 * @return the sheet's title
		 */
		public String getSheetTitle() {
			return sheetTitle;
		}

		/**
		 * @return the sheet's title row index
		 */
		public int getTitleRow() {
			return titleRow;
		}

		/**
		 * @return the sheet's title column index
		 */
		public int getTitleCol() {
			return titleCol;
		}

		/**
		 * @return the sheet's column index where team are listed
		 */
		public int getTeamsCol() {
			return teamsCol;
		}

		/**
		 * @return {@code true} if the sheet has an opener column, otherwise
		 * {@code false}
		 */
		public boolean hasOpener() {
			return opener;
		}

		/**
		 * @return the sheet's opener column index
		 */
		public int getOpenerCol() {
			return openerCol;
		}

		/**
		 * @return the sheet's column index where bookies start
		 */
		public int getBookieCol() {
			return bookieCol;
		}
		
	} // private static class SheetProperties
	
} // public final class WorkbookProperties
