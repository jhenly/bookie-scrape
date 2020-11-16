package com.sportsbookscraper.app.excel;

import java.io.IOException;

public final class ExcelFactory {
	
	private ExcelFactory() {}
	
	public static ExcelFileReader newExcelFileReader(String excelFilePath) throws IOException {
		return new ExcelFileReader(excelFilePath);
	}
	
	public static ExcelFileWriter newExcelFileWriter(String excelFilePath) throws IOException {
		return new ExcelFileWriter(excelFilePath);
	}
}
