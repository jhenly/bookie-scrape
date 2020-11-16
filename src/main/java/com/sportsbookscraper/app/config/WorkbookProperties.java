package com.sportsbookscraper.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Loads {@code config.properties} file and populates global configs and
 * individual Excel workbook sheet specific configs.
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
	
	// individual sheet properties holder
	private static final List<SheetProperties> sheetProps;
	
	// NOTE: do not the change the order of calls in the following static block,
	//       some calls depend on other calls happening prior 
	static {
		props = loadProps(WorkbookProperties.CONFIG_CLASS_PATH);
		excelFilePath = props.getProperty(EXCEL_FILE_PATH);
		allSheets = initAllSheets();
		
		// init shared properties
		font = getStrPropOrDefault(SHEET_FONT, DEF_FONT);
		fontSize = getIntPropOrDefault(SHEET_FONT_SIZE, DEF_FONT_SIZE);
		colSizeToFit = getBoolPropOrDefault(COLS_SIZETOFIT, DEF_SIZE_TO_FIT);
		rowSizeToFit = getBoolPropOrDefault(ROWS_SIZETOFIT, DEF_SIZE_TO_FIT);
		
		// init all individual sheet properties
		sheetProps = initSheetProperties(allSheets);
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
	
	/**
	 * @return the path to the Excel file, if set in {@code config.properties}
	 */
	public static String getExcelFilePath() {
		return excelFilePath;
	}

	/**
	 * @return returns an {@linkplain Collections#unmodifiableList(List)
	 * unmodifiable list} containing the Excel workbook's sheet names.
	 */
	public static List<String> getAllSheets() {
		return allSheets;
	}
	
	/**
	 * @return font name used by all sheets
	 */
	public static String getSheetFont() {
		return font;
	}
	
	/**
	 * @return font size used by all sheets
	 */
	public static int getSheetFontSize() {
		return fontSize;
	}
	
	/**
	 * @return {@code true} if Excel column widths should fit their content,
	 * {@code false}
	 */
	public static boolean getColSizeToFit() {
		return colSizeToFit;
	}
	
	/**
	 * @return {@code true} if Excel row heights should fit their content,
	 * otherwise {@code false}
	 */
	public static boolean getRowSizeToFit() {
		return rowSizeToFit;
	}
	
	/*  - End static accessors section -  */
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public static SheetProperties getSheetProperties(int index) {
		if(index < 0 || index > sheetProps.size()) {
			throw new IndexOutOfBoundsException(index);
		}
		
		return sheetProps.get(index);
	}
	
	/* -- End Static Properties Section -- */
	
	
	/* -- SheetProperties Section -- */

	private static List<SheetProperties> initSheetProperties(List<String> sheets) {
		List<SheetProperties> sprops = new ArrayList<SheetProperties>(sheets.size());
		
		// build sheet properties for each sheet
		for(String sheetName : sheets) {
			sprops.add(buildSheetProperties(sheetName));
		}
		
		return Collections.unmodifiableList(sprops);
	}
	
	// sheet property defaults
	private static final int DEF_TITLE_ROW_COL = 0;
	private static final int DEF_TABLE_ROW = 1;
	private static final int DEF_TEAMS_COL = 0;
	private static final boolean DEF_HAS_OPENER = true;
	private static final int DEF_OPENER_COL = 1;
	private static final int DEF_BOOKIE_COL = 2;

	private static SheetProperties buildSheetProperties(String sheet) {
		SheetProperties sp = new SheetProperties();
		
		// if no url then default, will be handled later in mediator
		sp.scrapeUrl = getStrPropOrDefault(sheet + SCRAPE_URL, "");
		
		// if no sheet title specified, then set title to sheet name
		sp.sheetTitle = getStrPropOrDefault(sheet + SHEET_TITLE, sheet);
		// if no title row specified then use default 
		sp.titleRow = getIntPropOrDefault(sheet + TITLE_ROW, DEF_TITLE_ROW_COL);
		// if no title col specified then use default
		sp.titleCol = getIntPropOrDefault(sheet + TITLE_COL, DEF_TITLE_ROW_COL);
		// if no opener specified then use default
		sp.opener = getBoolPropOrDefault(sheet + OPENER, DEF_HAS_OPENER);
		// if not teams col specified then use default
		sp.teamsCol = getIntPropOrDefault(sheet + TEAMS_COL, DEF_TEAMS_COL);
		
		// now we have to range check any row or column index, for instance
		// tableRow should be greater than titleRow
		int tmp;
		
		// get tableRow prop, set it to titleRow + 1 if it's less than titleRow
		tmp = getIntPropOrDefault(sheet + SHEET_TABLE, DEF_TABLE_ROW);
		sp.tableRow = (tmp > sp.titleRow) ? tmp : sp.titleRow + 1;
		
		// get tableRow prop, set it to titleRow + 1 if it's less than titleRow
		tmp = getIntPropOrDefault(sheet + SHEET_TABLE, DEF_TABLE_ROW);
		sp.tableRow = (tmp > sp.titleRow) ? tmp : sp.titleRow + 1;
		
		// get openerCol prop, set it to teamsCol + 1 if it's less than teamsCol
		tmp = getIntPropOrDefault(sheet + OPENER_COL, DEF_OPENER_COL);
		sp.openerCol = (tmp > sp.teamsCol) ? tmp : sp.teamsCol + 1;
		
		// get openerCol prop, set it to teamsCol + 1 if it's less than teamsCol
		tmp = getIntPropOrDefault(sheet + BOOKIE_COL, DEF_BOOKIE_COL);
		// account for the sheet not having an opener column
		if(sp.hasOpener()) {
			sp.bookieCol = (tmp > sp.openerCol) ? tmp : sp.openerCol + 1;
		} else {
			sp.bookieCol = (tmp > sp.teamsCol) ? tmp : sp.teamsCol + 1;
		}
		
		
		return sp;
	}
	
	/**
	 * Class that wraps individual sheet properties with accessor methods.
	 * 
	 * @author Jonathan Henly
	 */
	private static final class SheetProperties {	
		private String scrapeUrl;
		private String sheetTitle;
		private int titleRow;
		private int titleCol;
		private int tableRow;
		private int teamsCol;
		private boolean opener;
		private int openerCol;
		private int bookieCol;
		
		// don't subclass this class
		private SheetProperties() {}

		/**
		 * @return the sheet's url to scrape
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
		 * @return the sheet's table row index
		 */
		public int getTableRow() {
			return tableRow;
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
