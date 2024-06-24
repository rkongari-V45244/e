package com.aflac.core.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BrochureDisclosureDataGeneration {

	public static XSSFWorkbook workbook;
	public static void main(String args[]) throws IOException {
		List<BrochureDisclosureExcelRow> list = new ArrayList<>();
		 	
		try {
			workbook = new XSSFWorkbook(new File("D:\\testBrochureData.xlsx"));
			XSSFSheet sheet = workbook.getSheet("Sheet1");
			System.out.println("Total Rows: " + sheet.getPhysicalNumberOfRows());
			for(Row row : sheet) {
				BrochureDisclosureExcelRow r = new BrochureDisclosureExcelRow();
				for(Cell cell : row) {
					switch (cell.getColumnIndex()) {
					case 1:
						r.fileName = cell.getStringCellValue();
						break;
					case 2:
						r.docName = cell.getStringCellValue();
						break;
					case 3:
						r.docType = cell.getStringCellValue();
						break;
					case 4:
						r.product = cell.getStringCellValue();
						break;
					case 5:
						r.series = cell.getStringCellValue();
						break;
					case 6:
						r.state = cell.getStringCellValue();
						break;
					case 7:
						r.formNo = cell.getStringCellValue();
						break;
					case 8:
						r.description = cell.getStringCellValue();
						break;
					default:
						//System.out.println("Data not found");
						break;
					}
				}
				list.add(r);
			}
		} catch (InvalidFormatException | IOException e) {
			System.out.println("Exception: " + e.getMessage());
		} finally {
			workbook.close();
		}
	}
}
