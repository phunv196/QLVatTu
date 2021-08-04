package com.app.exceldownload.util;

import com.app.exceldownload.model.Customer;
import com.app.model.employee.EmployeeModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelGenerator {
	
	public static ByteArrayInputStream customersToExcel(List<EmployeeModel> customers) throws IOException {
		String[] COLUMNs = {"Id", "Name", "Address", "Age"};
		try(
				Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
		){
			CreationHelper createHelper = workbook.getCreationHelper();
	 
			Sheet sheet = workbook.createSheet("Customers");
	 
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.BLUE.getIndex());
	 
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
	 
			// Row for Header
			Row headerRow = sheet.createRow(0);
	 
			// Header
			for (int col = 0; col < COLUMNs.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(COLUMNs[col]);
				cell.setCellStyle(headerCellStyle);
			}
	 
			// CellStyle for Age
			CellStyle ageCellStyle = workbook.createCellStyle();
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("#"));
	 
			int rowIdx = 1;
			for (EmployeeModel customer : customers) {
				Row row = sheet.createRow(rowIdx++);
	 
				row.createCell(0).setCellValue(customer.getEmployeeId());
				row.createCell(1).setCellValue(customer.getFullName());
				row.createCell(2).setCellValue(customer.getAddress());
	 
				Cell ageCell = row.createCell(3);
				ageCell.setCellValue(customer.getCode());
				ageCell.setCellStyle(ageCellStyle);
			}
	 
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
}