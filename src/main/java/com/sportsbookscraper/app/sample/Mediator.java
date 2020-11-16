package com.sportsbookscraper.app.sample;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sportsbookscraper.app.config.WorkbookProperties;
import com.sportsbookscraper.app.excel.ExcelFactory;
import com.sportsbookscraper.app.excel.ExcelFileReader;
import com.sportsbookscraper.app.excel.ExcelFileWriter;
import com.sportsbookscraper.app.excel.SheetNotFoundException;
import com.sportsbookscraper.app.scrape.Bookie;
import com.sportsbookscraper.app.scrape.Scraper;

class Mediator {
	private String excelFilePath;
	private Map<String, Bookie> existingBookies;
	private ExcelFileWriter writer;
	private Scraper scraper;
	private WorkbookProperties config;
	
	
	Mediator(String excelFilePath, String sheetUrl) {
		this.excelFilePath = excelFilePath;
		scraper = new Scraper();
	}
	
	
	/**
	 * Retrieves any bookies that are already listed in the Excel sheet, as well
	 * as their column index. This method is useful for retaining the order of
	 * the bookies already listed in the excel sheet.
	 * <p>
	 * <b>Note:</b> rows and columns are not zero-indexed in Excel, but this
	 * method treats them as if they are. Thus, {@code bRow} and
	 * {@code bCol} should be set with this in mind.
	 * <p>
	 * <b>Note:</b> if the Excel workbook does not have a sheet with a name that
	 * equals {@code sheetName}, then this method will return {@code null}.
	 * 
	 * @param sheetName - the name of the Excel sheet in the Excel workbook
	 * @param bRow - the Excel sheet row index where bookies start
	 * @param bCol - the Excel sheet column index where bookies start
	 * 
	 * @return a {@code Map<String, Bookie>} containing any bookies read from
	 * the Excel sheet, this method will return {@code null} if the Excel sheet
	 * does not exist
	 * 
	 * @throws IOException 
	 */
	private Map<String, Bookie>
	getBookiesFromSheet(String sheetName, int bRow, int bCol)
	{
		// read bookies names from Excel sheet
		List<String> bNames = null;
		try(ExcelFileReader excelReader = ExcelFactory.newExcelFileReader(excelFilePath);) {
			bNames = excelReader.readStringsInRow(sheetName, 1, bRow, bCol);
		} catch (SheetNotFoundException e) {
			// TODO either throw SNFException or return Collections.emptyMap()
			
			bNames = Collections.emptyList();
			
			e.printStackTrace();
		} catch (IOException e1) { // only thrown on excelReader.close()
			// can't do anything about this so just swallow the exception
			// e1.printStackTrace();
		}
		
		// return map of bookies read from Excel
		return getMapFromBookieNames(bNames);
	}
	
	
	/* getBookiesFromSheet helper, will return an empty map if names is empty */
	private Map<String, Bookie> getMapFromBookieNames(List<String> names) {
		Map<String, Bookie> bookies = new HashMap<String, Bookie>(names.size());
		
		// put bookie with bookie's name and Excel column index into map
		for(int i = 0, n = names.size(); i < n; i++) {
			Bookie bookie = new Bookie(names.get(i), i);
			bookies.put(bookie.name(), bookie);
		}
		
		return bookies;		
	}
	
	
}
