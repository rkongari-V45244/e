package com.aflac.core.util;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jetty.util.log.Log;

public class ExcelSheetStyling {
	
	public static Font getFont(XSSFWorkbook workbook, short color, short hight, boolean isBold, boolean isItalic, byte underline) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints(hight);
		font.setFontName("Calibri");
		font.setBold(isBold);
		font.setItalic(isItalic);
		font.setUnderline(underline);
		font.setColor(color);
		
		return font;
	}
	
	public static XSSFCellStyle getCellStyle(XSSFWorkbook workbook, Font font, XSSFColor color, HorizontalAlignment horizontal, VerticalAlignment vertical, boolean wrapText) {
		XSSFCellStyle xssfStyle = workbook.createCellStyle();
		xssfStyle.setFont(font);
		xssfStyle.setWrapText(wrapText);
		if(horizontal != null)
			xssfStyle.setAlignment(horizontal);
		if(vertical != null)
			xssfStyle.setVerticalAlignment(vertical);
		if(color != null) {
			xssfStyle.setFillForegroundColor(color);
			xssfStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
			
		return xssfStyle;
	}
	
	public static XSSFCellStyle getHeaderLableStyle(XSSFWorkbook workbook) {
		Font font = getFont(workbook, IndexedColors.BLACK.getIndex(), (short)14, false, true, Font.U_NONE);
		XSSFColor color = new XSSFColor(new Color(222,235,247), null);
		return getCellStyle(workbook, font, color, null, null, false);
	}
	
	public static XSSFCellStyle getfieldLableStyle(XSSFWorkbook workbook, XSSFSheet sheet, boolean isBorder) {
		Font font = getFont(workbook, IndexedColors.BLACK.getIndex(), (short)8, false, false, Font.U_NONE);
		XSSFColor color = new XSSFColor(new Color(231,230,230), null);
		if(sheet.getSheetName().equalsIgnoreCase(Products.ACCOUNT_INFORMATION.getValue()))
			return getCellStyle(workbook, font, color, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true);
		else {
			XSSFCellStyle style =getCellStyle(workbook, font, color, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, true);
			if(isBorder) {
				style =getCellStyle(workbook, font, color, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true);
				style.setBorderTop(BorderStyle.THIN);      style.setTopBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderBottom(BorderStyle.THIN);   style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderRight(BorderStyle.THIN);    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderLeft(BorderStyle.THIN);     style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			}
			return style;
		}
	}
	
	public static XSSFCellStyle getHelperTextStyle(XSSFWorkbook workbook) {
		Font font = getFont(workbook, IndexedColors.ORANGE.getIndex(), (short) 9, false, true, Font.U_NONE);
		XSSFCellStyle style = getCellStyle(workbook, font, null, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, true);
		style.setBorderTop(BorderStyle.THICK);      style.setTopBorderColor(IndexedColors.SKY_BLUE.getIndex());
		style.setBorderBottom(BorderStyle.THICK);   style.setBottomBorderColor(IndexedColors.SKY_BLUE.getIndex());
		style.setBorderRight(BorderStyle.THICK);    style.setRightBorderColor(IndexedColors.SKY_BLUE.getIndex());
		style.setBorderLeft(BorderStyle.THICK);     style.setLeftBorderColor(IndexedColors.SKY_BLUE.getIndex());
		return style;
	}
	
	public static void getAflacFreezPane(XSSFWorkbook workbook, XSSFSheet sheet) throws IOException {
		
		Row row1, row2; 
		Cell cell1, cell12;
		Font font;
		sheet.createFreezePane(0, 3);
		row1 = sheet.createRow(0);
	    row2 = sheet.createRow(1);
	    cell1 = row1.createCell(0);
	    cell12 = row2.createCell(0);
	    	    		
		font = getFont(workbook, IndexedColors.DARK_BLUE.getIndex(), (short)16, true, false, Font.U_NONE);
	    cell1.setCellStyle(getCellStyle(workbook, font, null, null, null, false));
	    cell1.setCellValue("Electronic Enrollment Setup");
	    
	    cell1 = row1.createCell(3);
		font = getFont(workbook, IndexedColors.DARK_BLUE.getIndex(), (short)11, true, false, Font.U_NONE);
	    cell1.setCellStyle(getCellStyle(workbook, font, null, null, null, false));
	    cell1.setCellValue("Today's Date");
	    	    
//	    font = getFont(workbook, IndexedColors.DARK_BLUE.getIndex(), (short)12, true, true, Font.U_NONE);
//	    cell12.setCellStyle(getCellStyle(workbook, font, null, null, null, false));
//	    cell12.setCellValue("It's never been easier…");
	    
	    font = getFont(workbook, IndexedColors.DARK_BLUE.getIndex(), (short)11, true, false, Font.U_NONE);
	    cell12.setCellStyle(getCellStyle(workbook, font, null, null, null, false));
	    cell12 = row2.createCell(3);
	    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate localDate = LocalDate.now();
	    cell12.setCellValue(dtf.format(localDate));
	    	   
	    if(!sheet.getSheetName().equalsIgnoreCase(Products.ACCOUNT_INFORMATION.getValue()))
	    	sheet.autoSizeColumn(6);
	    else
	    	sheet.autoSizeColumn(3);
	    	    
		InputStream is;
		is = ExcelSheetStyling.class.getClassLoader().getResourceAsStream("logo/aflac.png");
		
	    byte[] bytes = IOUtils.toByteArray(is);
	    int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
	    is.close();
	    
	    CreationHelper helper = workbook.getCreationHelper();
	  
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setRow1(0);
		anchor.setRow2(3);
		anchor.setCol1(6);
		anchor.setCol2(7);
		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize(2.5, 1.0);
	}
}
