package com.aflac.core.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BrochureDataGenerationScript {

	public static XSSFWorkbook workbook;
	public static void main(String args[]) throws IOException {
		List<ExcelRow> list = new ArrayList<>();
		 	
		try {
			workbook = new XSSFWorkbook(new File("D:\\testBrochureData.xlsx"));
			XSSFSheet sheet = workbook.getSheet("Sheet1");
			System.out.println("Total Rows: " + sheet.getPhysicalNumberOfRows());
			for(Row row : sheet) {
				ExcelRow r = new ExcelRow();
				for(Cell cell : row) {
					switch (cell.getColumnIndex()) {
					case 1:
						r.brochureId = cell.getStringCellValue();
						break;
					case 2:
						r.productName = cell.getStringCellValue();
						break;
					case 3:
						r.series = cell.getStringCellValue();
						break;
					case 4:
						r.state = cell.getStringCellValue();
						break;
					case 5:
						r.language = cell.getStringCellValue();
						break;
					case 6:
						r.coverageName = cell.getStringCellValue();
						break;
					case 7:
						r.coverageLevel = cell.getStringCellValue();
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
				
		Brochure br = new Brochure();
		System.out.println("List size: " + list.size());
		for(ExcelRow row : list) {
			if(br.lob == null)
				br.lob = row.productName;
			
			if(br.series == null) {
				br.series = new ArrayList<>();
				Series series = new Series();
				series.name = row.series;
								
				List<DataSC> data = new ArrayList<>(); 
				List<Coverage> coverages = new ArrayList<>();
				Coverage cov = new Coverage();
				cov.name = row.coverageName; cov.language = row.language;
				List<Level> levels = new ArrayList<>();	Level level = new Level();
				level.brochureId = row.brochureId; level.level = row.coverageLevel;
				levels.add(level);
				cov.levels = levels;
				
				coverages.add(cov);
				List<String> states = Arrays.asList(row.state.replace(";#", ",").split(","));
				DataSC dt = new DataSC();
				dt.situs = states; dt.coverages = coverages;
				data.add(dt);
				
				series.data = data;
				br.series.add(series);
			} else {
				List<String> situs = Arrays.asList(row.state.replace(";#", ",").split(","));
				List<Series> series = br.series.stream().filter(s -> s.name.equalsIgnoreCase(row.series)).collect(Collectors.toList());
				
				if(series.isEmpty()) {
					Series ser = new Series();
					ser.name = row.series;
					 
					List<Coverage> coverages = new ArrayList<>();
					Coverage cov = new Coverage();
					cov.name = row.coverageName; cov.language = row.language;
					List<Level> levels = new ArrayList<>();	Level level = new Level();
					level.brochureId = row.brochureId; level.level = row.coverageLevel;
					levels.add(level);
					cov.levels = levels;
					
					coverages.add(cov);
					DataSC dt = new DataSC();
					dt.situs = situs; 
					dt.coverages = coverages;
					ser.data = new ArrayList<>();
					ser.data.add(dt);
					br.series.add(ser);
					
				}
				else {
					List<DataSC> dataList = series.get(0).data.stream().filter(d -> (d.situs.size() == situs.size()) && d.situs.containsAll(situs)).collect(Collectors.toList());
					
					if(dataList.isEmpty()) {
						List<Coverage> coverages = new ArrayList<>();
						Coverage cov = new Coverage();
						cov.name = row.coverageName; cov.language = row.language;
						List<Level> levels = new ArrayList<>();	Level level = new Level();
						level.brochureId = row.brochureId; level.level = row.coverageLevel;
						levels.add(level);
						cov.levels = levels;
						
						coverages.add(cov);
						DataSC dt = new DataSC();
						dt.situs = situs; 
						dt.coverages = coverages;
						br.series.forEach(s -> {
							if(s.name.equalsIgnoreCase(row.series)) {
								s.data.add(dt);
							}
						});
					} 
					else {
						DataSC datasc = dataList.get(0);
												
						List<Coverage> coverages = datasc.coverages.stream().filter(c -> c.name.equalsIgnoreCase(row.coverageName) && c.language.equalsIgnoreCase(row.language)).collect(Collectors.toList());
						Level level = new Level();
						level.brochureId = row.brochureId;
						level.level = row.coverageLevel;
												
						if(coverages.isEmpty()) {
							Coverage cov = new Coverage();
							cov.name = row.coverageName; cov.language = row.language;
							cov.levels = new ArrayList<>();
							cov.levels.add(level);
							dataList.get(0).coverages.add(cov);
						}
						else
							coverages.get(0).levels.add(level);
					}
				}
			}
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String outPut = objectMapper.writeValueAsString(br);
		
//		System.out.println(outPut);
		
		Path path = Paths.get("D:\\outPut.txt");
		Files.write(path, outPut.getBytes());
	}
}
