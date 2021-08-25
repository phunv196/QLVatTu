/*
 * Copyright (C) 2010 VIETSENS. All rights reserved.
 * VIETSENS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.dao.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author vietsens_01
 */
public class ReadExelFile {

    public static List<String[]> readXLSFile(File file, int numOfColumn, int startDataRow) throws IOException {
        InputStream ExcelFileToRead = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;
        List result = new ArrayList();
        Iterator rows = sheet.rowIterator();
        int curDataRow = 0;
        while (rows.hasNext()) {
            row = (HSSFRow) rows.next();
            if (row.getRowNum() > curDataRow) {
                for (int i = curDataRow; i < row.getRowNum(); i++) {
                    result.add(new String[numOfColumn]);
                }
            }
            String[] a = new String[numOfColumn];
            for (int col = 0; col < numOfColumn; col++) {
                cell = (HSSFCell) row.getCell(col);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_STRING:
                            a[col] = cell.getRichStringCellValue().getString().trim();
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            a[col] = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                a[col] = CommonUtils.convertDateToString(cell.getDateCellValue());
                            } else {
                                a[col] = CommonUtils.formatNumber(cell.getNumericCellValue());
                            }
                            break;
                        case HSSFCell.CELL_TYPE_BLANK:
                            a[col] = "";
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA:
                            if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                a[col] = CommonUtils.formatNumber(cell.getNumericCellValue());
                            } else if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_STRING) {
                                a[col] = cell.getRichStringCellValue().getString().trim();
                            }
                            break;
                        default:
                            System.out.println(col + ":" + cell.getCellType());
                            a[col] = cell.getRichStringCellValue().getString();
                            break;
                    }
                }
            }
            result.add(a);
            curDataRow = row.getRowNum() + 1;
        }
        return result;
    }

    public static List<String[]> readXLSXFile(File file, int numOfColumn, int startRow) throws IOException {
        InputStream ExcelFileToRead = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;
        List result = new ArrayList();
        Iterator rows = sheet.rowIterator();
        int curDataRow = 0;
        while (rows.hasNext()) {
            row = (XSSFRow) rows.next();
            if (row.getRowNum() > curDataRow) {
                for (int i = curDataRow; i < row.getRowNum(); i++) {
                    result.add(new String[numOfColumn]);
                }
            }
            String[] a = new String[numOfColumn];
            for (int col = 0; col < numOfColumn; col++) {
                cell = (XSSFCell) row.getCell(col);
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_STRING:
                            a[col] = cell.getRichStringCellValue().getString().trim();
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            a[col] = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                a[col] = CommonUtils.convertDateToString(cell.getDateCellValue());
                            } else {
                                a[col] = CommonUtils.formatNumber(cell.getNumericCellValue());
                            }
                            break;
                        case HSSFCell.CELL_TYPE_BLANK:
                            a[col] = "";
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA:
                            if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_NUMERIC) {
                                a[col] = CommonUtils.formatNumber(cell.getNumericCellValue());
                            } else if (cell.getCachedFormulaResultType() == HSSFCell.CELL_TYPE_STRING) {
                                a[col] = cell.getRichStringCellValue().getString().trim();
                            }
                            break;
                        default:
                            System.out.println(col + ":" + cell.getCellType());
                            a[col] = cell.getRichStringCellValue().getString().trim();
                            break;
                    }
                }
            }
            result.add(a);
            curDataRow = row.getRowNum() + 1;
        }
        return result;
    }
}
