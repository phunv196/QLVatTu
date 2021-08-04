/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.dao.base.converter;

// #148 start
import java.awt.Color;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.app.dao.base.CommonUtils;
import com.app.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jxcell.CellException;
import com.jxcell.CellFormat;
import com.jxcell.View;
//import com.viettel.config.CorsFilter;
// #148 end

/**
 * Bao cao dong cot.
 *
 * @author HuyenNV
 * @since 1.0
 * @version 1.0
 */
public class DynamicExport {

    /**
     * Format cho header
     */
    public static final int HEADER_FORMAT = 0;
    /**
     * Format tao border
     */
    public static final int BORDER_FORMAT = 1;
    /**
     * Tieu de
     */
    public static final int TITLE = 2;
    /**
     * Format group cap 1
     */
    public static final int GROUP_LEVEL1_FORMAT = 3;
    /**
     * Format group cap 2
     */
    public static final int GROUP_LEVEL2_FORMAT = 4;
    /**
     * Format group cap 3
     */
    public static final int GROUP_LEVEL3_FORMAT = 5;
    /**
     * Format group cap 4
     */
    public static final int GROUP_LEVEL4_FORMAT = 6;
    /**
     * Format group cap 5
     */
    public static final int GROUP_LEVEL5_FORMAT = 7;
    /**
     * Format group cap 6
     */
    public static final int GROUP_LEVEL6_FORMAT = 8;
    /* Format number .*/
    /**
     *
     */
    public static final int ACCOUNTING_FORMAT = 9;
    /* Format number .*/
    /**
     *
     */
    public static final int NUMBER_FORMAT = 10;
    /* Format number double.*/
    /**
     *
     */
    public static final int NUMBER_FORMAT_D = 11;
    /* Format tao border voi vien Den.*/
    /**
     *
     */
    public static final int BLACK_BORDER_FORMAT = 12;
    // SonPN added on 2012-05-14
    /**
    *
    */
    public static final int BLACK_BORDER_FORMAT_NON_WRAP = 13;
    /**
     *
     */
    public static final int BOLD_WHITE = 20;
    /**
     *
     */
    public static final int BOLD_WHITE_CENTER = 21;
    /**
     *
     */
    public static final int NOMARL_WHITE_CENTER = 22;
    //khanhnq16 2012-05-14
    /**
     *
     */
    public static final int NORMAL_ITALIC = 23;
    /**
     *
     */
    public static final int GROUP_DATA_FORMAT = 24;
    /**
     *
     */
    public static final int PERCENT_FORMAT = 25;
    /**
     *
     */
    public static final int CENTER_FORMAT = 26;
    /**
     *
     */
    public static final int BOLD_CENTER_FORMAT = 27;
    /**
     *
     */
    public static final int BOLD_FORMAT = 28;
    // Align text to middle of cell
    /**
     *
     */
    public static final int CENTER_VERTICAL_FORMAT = 29;
    // Shrink to fit
    /**
     *
     */
    public static final int SHRINK_TO_FIT = 30;
    // Header orange
    /**
     *
     */
    public static final int HEADER_ORANGE = 31;
    // Header yellow
    /**
     *
     */
    public static final int HEADER_YELLOW = 32;
    /**
     *
     */
    public static final int BORDER_FORMAT_NON_WRAP = 33;
    // Left alignment
    /**
     *
     */
    public static final int ALIGN_LEFT = 34;
    // Cell color
    /**
     *
     */
    public static final int CELL_COLOR_YELLOW = 35;
    
  
    // Left alignment
    /**
     *
     */
    public static final int ALIGN_RIGHT = 36;
    // No wrap text
    /**
     *
     */
    public static final int NO_WRAP_TEXT = 37;
    /**
     *
     */
    public static final int BLACK_BORDER_NO_HORIZONTAL_NONE_WRAP = 38;
    /**
     *
     */
    public static final int BLACK_BORDER_NO_HORIZONTAL = 39;
    /**
     *
     */
    public static final int BORDER_FORMAT_NO_ROW_HEIGHT = 40;
    /**
     *
     */
    public static final int MERGE_CELL = 41;
    //#095 Start
    public static final int COLOR_RED = 42;
    public static final int NO_WORK_PROCESS = 43;
    public static final int PAST_LOCK = 44;
    public static final int ORG_LOCK = 45;
    public static final int CELL_COLOR_GREEN = 46;
    public static final int CELL_COLOR_BLUE = 47;
    public static final int CELL_COLOR_SAMPLE = 52;
    /**
    * In đậm nghiêng
    */
    public static final int BOLD_ITALIC_FORMAT = 48;
    /**
     * Căn trái
     */
    public static final int LEFT_FORMAT = 49;
    
    public static final int FORMAT_1 = 50;
    public static final int FORMAT_2 = 51;
    public static final int UNDERLINE_GREEN = 58;
    
    
    //#095 End
    
    // #44 xuat bao cao start
    public static final int ONLY_PERCENT_FORMAT = 53;
    public static final int FONT_TIMES_NEW_ROMAN = 54;
    public static final int ROW_HEIGHT_AUTO = 55;
    public static final int FONT_SIZE_13 = 56;
    // #44 xuat bao cao end
    public static final int BLUE_TEXT = 57;
    /**
     * Doi tuong de tuong tac voi file Excel
     */
    private final View view;
    /**
     * Cot dau tien cua du lieu
     */
    private int startColumn;
    /**
     * Dong dau tien cua header
     */
    private int startHeaderRow;
    /**
     * Dong dau tien cua du lieu
     */
    private int startDataRow;
    /**
     * Dong du lieu cuoi cung, moi nhat, hien tai
     */
    private int lastRow;
    private boolean isXLSX;
    public static final Logger LOGGER = LoggerFactory.getLogger(DynamicExport.class);

    /**
     * Khoi tao.
     *
     * @param templateFile Duong dan den file template
     * @param startColumn Cot dau tien cua du lieu
     * @param startHeaderRow Dong dau tien cua header
     * @param startDataRow Dong dau tien cua du lieu
     * @throws Exception Exception
     */
    public DynamicExport(String templateFile, int startColumn, int startHeaderRow, int startDataRow)
            throws Exception {
        this.startColumn = startColumn;
        this.startHeaderRow = startHeaderRow;
        this.startDataRow = startDataRow;
        this.lastRow = startDataRow - 1;
        view = new View();
        view.read(templateFile);
    }
    
    /**
     * Khoi tao.
     *
     * @param templateFile Duong dan den file template
     * @param startColumn Cot dau tien cua du lieu
     * @param startHeaderRow Dong dau tien cua header
     * @param startDataRow Dong dau tien cua du lieu
     * @param isXLSX
     * @throws Exception Exception
     */
    public DynamicExport(String templateFile, int startColumn, int startHeaderRow, int startDataRow, boolean isXLSX)
            throws Exception {
        this.startColumn = startColumn;
        this.startHeaderRow = startHeaderRow;
        this.startDataRow = startDataRow;
        this.lastRow = startDataRow - 1;
        view = new View();
        this.isXLSX = isXLSX;
        if (isXLSX) {
            view.readXLSX(templateFile);
        } else {
            view.read(templateFile);
        }
    }

    /**
     *
     * @param templateFile
     * @param startDataRow
     * @param isXLSX
     * @throws Exception
     */
    public DynamicExport(String templateFile, int startDataRow, boolean isXLSX)
            throws Exception {
        this.startDataRow = startDataRow;
        this.lastRow = startDataRow - 1;
        // #110 start
        this.templateFile = templateFile;
        // #110 end
        view = new View();
        this.isXLSX = isXLSX;
        if (isXLSX) {
            view.readXLSX(templateFile);
        } else {
            view.read(templateFile);
        }
    }

    public DynamicExport(InputStream templateFile, int startDataRow, boolean isXLSX)
            throws Exception {
        this.startDataRow = startDataRow;
        this.lastRow = startDataRow - 1;
        view = new View();
        this.isXLSX = isXLSX;
        if (isXLSX) {
            view.readXLSX(templateFile);
        } else {
            view.read(templateFile);
        }
    }

    /**
     *
     * @param startHeaderRow
     */
    public void setStartHeaderRow(int startHeaderRow) {
        this.startHeaderRow = startHeaderRow;
    }

    /**
     *
     * @param startDataRow
     */
    public void setStartDataRow(int startDataRow) {
        this.startDataRow = startDataRow;
    }

    /**
     * Ghi ra file Excel.
     *
     * @param exportFile File Excel xuat ra
     * @param lastColumn Cot cuoi cung (khong phai la so cot)
     * @param req
     * @throws Exception Exception
     */
    public File exportFile(String exportFile, int lastColumn, HttpServletRequest req)
            throws Exception {
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        exportFile = prefixOutPutFile + exportFile;
        this.setCellFormat(startHeaderRow, startColumn, startDataRow - 1, lastColumn, HEADER_FORMAT);
        if (lastRow > startDataRow) {
            this.setCellFormat(startDataRow, startColumn, lastRow, lastColumn, BORDER_FORMAT);
        }
        String fullPathFile = Constants.COMMON.EXPORT_FOLDER + exportFile;
        view.write(fullPathFile);
        return new File(fullPathFile);
    }

    /**
     * Ghi ra file Excel khong wraptext
     *
     * @param exportFile
     * @param lastColumn
     * @param req
     * @throws Exception
     */
    public void exportFileNonWrap(String exportFile, int lastColumn, HttpServletRequest req)
            throws Exception {
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        exportFile = prefixOutPutFile + exportFile;
        //this.setCellFormat(startHeaderRow, startColumn, startDataRow - 1, lastColumn, HEADER_FORMAT);
        if (lastRow > startDataRow) {
            this.setCellFormat(startDataRow, startColumn, lastRow, lastColumn, BORDER_FORMAT_NON_WRAP);
        }
        view.write(Constants.COMMON.EXPORT_FOLDER + exportFile);
        req.setAttribute("fileName", exportFile + ".xls");
        req.setAttribute("filePath", exportFile);
    }

    /**
     * Ghi ra file Excel.
     *
     * @param exportFile File Excel xuat ra
     * @param req
     * @return 
     * @throws Exception Exception
     */
    public String exportFile(String exportFile, jakarta.servlet.http.HttpServletRequest req) throws Exception {
        String fullPathFile;
        if (isXLSX) {
            fullPathFile = exportFile + ".xlsx";
            view.writeXLSX(fullPathFile);
        } else {
            fullPathFile = exportFile + ".xls";
            view.write(fullPathFile);
        }
        return fullPathFile;
    }
    /**
     * toResponse
     * @param fileExport
     * @return
     * @throws FileNotFoundException
     */
//    public ResponseEntity<InputStreamResource> toResponse(File fileExport) throws FileNotFoundException {
//        final InputStream targetStream = new DataInputStream(new FileInputStream(fileExport));
//        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
//        return ResponseEntity.ok()
//                // .contentLength( targetStream.length() )
//                .contentType(org.springframework.http.MediaType.parseMediaType(mimeTypesMap.getContentType(fileExport)))
//                .body(new InputStreamResource(targetStream));
//    }

    /**
     * Ghi ra file Excel.
     *
     * @param exportFile File Excel xuat ra
     * @throws Exception Exception
     */
    public void exportFile(String exportFile) throws Exception {
        if (isXLSX) {
            view.writeXLSX(Constants.COMMON.EXPORT_FOLDER + exportFile);
        } else {
            view.write(Constants.COMMON.EXPORT_FOLDER + exportFile);
        }
    }

    public void deleteSheets(int start, int numOfSheet) throws CellException {
        view.deleteSheets(start, numOfSheet);
    }

    /**
     * Xuat file pdf
     *
     * @param exportFile
     * @param req
     * @throws Exception
     */
    public void exportPdfFile(String exportFile, HttpServletRequest req)
            throws Exception {
        exportFile = Constants.COMMON.EXPORT_FOLDER + exportFile + ".pdf";
        view.setPrintScale(100);
        view.setPrintLeftMargin(0);
        view.setPrintRightMargin(0);
        view.setPrintScaleFitHPages(100);
        view.exportPDF(req.getRealPath(exportFile));
        req.setAttribute("downloadLinkPath", req.getContextPath() + exportFile);
    }
    //#103 Start
    /**
     * Xuat file pdf
     *
     * @param exportFile
     * @param req
     * @throws Exception
     */
    public void exportPdfFileFitPage(String exportFile, HttpServletRequest req)
            throws Exception {
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        exportFile = prefixOutPutFile + exportFile;
        view.setPrintLeftMargin(10);
        view.setPrintRightMargin(10);
        view.setPrintScaleFitHPages(100);
        view.setPrintScaleFitToPage(true);
        view.setPrintGridLines(false);
        view.exportPDF(Constants.COMMON.EXPORT_FOLDER + exportFile);
        req.setAttribute("fileName", exportFile + ".pdf");
        req.setAttribute("filePath", exportFile);
    }
    //#103 End
    /**
     * Them vao anh
     *
     * @param col1
     * @param row1
     * @param col2
     * @param row2
     * @param img
     * @throws CellException
     */
    public void addPicture(int col1, int row1, int col2, int row2, String img) throws CellException {
        view.addPicture(col1, row1, col2, row2, img);
    }

    /**
     * Chuyen den dong moi.
     */
    public void increaseRow() {
        lastRow++;
    }

    /**
     * Lay dong hien tai, dong cuoi cung.
     *
     * @return Dong cuoi cung
     */
    public int getLastRow() {
        return lastRow;
    }

    /**
     * Thiet lap chi so dong cuoi cung.
     *
     * @param row
     */
    public void setLastRow(int row) {
        lastRow = row;
    }

    /**
     *
     * @param r1
     * @param c1
     * @param r2
     * @param c2
     * @param value
     * @throws Exception
     */
    public void setRowHeight(int r1, int c1, int r2, int c2, Long value) throws Exception {
        if (value.equals(0L)) {
            value = 500L;
        }
        for (int i = r1; i <= r2; i++) {
            view.setRowHeight(i, value.intValue());
        }
    }

    /**
     * Set wraptext
     *
     * @param r1
     * @param c1
     * @param r2
     * @param c2
     * @throws Exception
     */
    public void setWrapText(int r1, int c1, int r2, int c2) throws Exception {
        for (int i = r1; i <= r2; i++) {
            CellFormat format = view.getCellFormat(i, c1, i, c2);
            format.setWordWrap(true);
        }
    }

    /**
     *
     * @param col
     * @param width in inch
     * @throws Exception
     */
    public void setColumnWidth(int col, int width) throws Exception {
        view.setColWidth(col, width * 254);
    }

    /**
     *
     * @param col1
     * @param col2
     * @param width in inch
     * @throws Exception
     */
    public void setColumnWidth(int col1, int col2, int width) throws Exception {
        view.setColWidth(col1, col2, width * 254, true);
    }

    /**
     *
     * @param row
     * @param height
     * @throws Exception
     */
    public void setRowHeight(int row, int height) throws Exception {
        view.setRowHeight(row, height);
    }

    /**
     *
     * @param row1
     * @param row2
     * @param height
     * @throws Exception
     */
    public void setRowHeight(int row1, int row2, int height) throws Exception {
        view.setColWidth(row1, row2, height, true);
    }

    /**
     *
     * @param r1
     * @param c1
     * @param r2
     * @param c2
     * @param cellColor
     * @throws CellException
     */
    public void setCellColor(int r1, int c1, int r2, int c2, Color cellColor) throws CellException {
        CellFormat format = view.getCellFormat(r1, c1, r2, c2);
        format.setPattern((short) 1);
        format.setPatternFG(cellColor);
        view.setCellFormat(format, r1, c1, r2, c2);
    }

    /**
     *
     * @param r1
     * @param c1
     * @param r2
     * @param c2
     * @param red
     * @param green
     * @param blue
     * @throws CellException
     */
    public void setCellColor(int r1, int c1, int r2, int c2, int red, int green, int blue) throws CellException {
        CellFormat format = view.getCellFormat(r1, c1, r2, c2);
        format.setPattern((short) 1);
        format.setPatternFG(new Color(red, green, blue));
        view.setCellFormat(format, r1, c1, r2, c2);
    }

    /**
     *
     * @param r1
     * @param c1
     * @param r2
     * @param c2
     * @param size
     * @throws CellException
     */
    public void setFontSize(int r1, int c1, int r2, int c2, Double size) throws CellException {
        CellFormat format = view.getCellFormat(r1, c1, r2, c2);
        format.setFontSize(size);
        view.setCellFormat(format, r1, c1, r2, c2);
    }

    /**
     * Format cell
     *
     * @param r1 Top
     * @param c1 Left
     * @param r2 Bottom
     * @param c2 Right
     * @param format
     * @throws com.jxcell.CellException
     */
    public void setCellFormat(int r1, int c1, int r2, int c2, CellFormat format)
            throws CellException {
        view.setCellFormat(format, r1, c1, r2, c2);
    }

    /**
     *
     * @param i1
     * @param i2
     * @param format
     * @throws CellException
     * @throws Exception
     */
    public void setTextFormat(int i1, int i2, CellFormat format)
            throws CellException, Exception {
        view.setTextSelection(i1, i2);
        view.setCellFormat(format);
    }

    /**
     *
     * @param r1
     * @param c1
     * @param i1
     * @param i2
     * @param formatType
     * @throws CellException
     * @throws Exception
     */
    public void setTextFormat(int r1, int c1, int i1, int i2, int formatType)
            throws CellException, Exception {
        view.setSelection(r1, c1, r1, c1);
        view.setTextSelection(i1, i2);
        CellFormat format = view.getCellFormat();
        if (formatType == BOLD_FORMAT) {
            format.setFontBold(true);
        }
        view.setCellFormat(format);
    }

    /**
     *
     * @param c1
     * @param i1
     * @param i2
     * @param formatType
     * @throws CellException
     * @throws Exception
     */
    public void setTextFormat(int c1, int i1, int i2, int formatType)
            throws CellException, Exception {
        setTextFormat(lastRow, c1, i1, i2, formatType);
    }

    
    //#044 d2t Start
    /**
     *
     * @param r1
     * @param c1
     * @param r2
     * @param c2
     * @param formatType
     * @throws CellException
     */
    public void setCellFormat(int r1, int c1, int r2, int c2, int formatType)
            throws CellException {
        if(r1 <= r2 && c1 <= c2){
            CellFormat format = view.getCellFormat(r1, c1, r2, c2);
            if (formatType == HEADER_FORMAT) {
                //<editor-fold defaultstate="collapsed" desc="Header cua bang">
                short border = CellFormat.BorderThin;
                format.setLeftBorder(border);
                format.setRightBorder(border);
                format.setTopBorder(border);
                format.setBottomBorder(border);
                format.setHorizontalInsideBorder(border);
                format.setVerticalInsideBorder(border);
    
                Color borderColor = Color.GREEN.darker();
                format.setLeftBorderColor(borderColor);
                format.setRightBorderColor(borderColor);
                format.setTopBorderColor(borderColor);
                format.setBottomBorderColor(borderColor);
    
                format.setFontBold(true);
                format.setPattern((short) 1);
                format.setPatternFG(new Color(254, 252, 172));
                format.setWordWrap(true);
    
                format.setVerticalAlignment(CellFormat.VerticalAlignmentCenter);
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
                //</editor-fold>
            } else if (formatType == BORDER_FORMAT) {
                //<editor-fold defaultstate="collapsed" desc="Border cho du lieu binh thuong">            
                short border = CellFormat.BorderThin;
                format.setLeftBorder(border);
                format.setRightBorder(border);
                format.setTopBorder(border);
                format.setBottomBorder(border);
                format.setHorizontalInsideBorder(border);
                format.setVerticalInsideBorder(border);
    
                Color borderColor = Color.GREEN.darker();
                format.setLeftBorderColor(borderColor);
                format.setRightBorderColor(borderColor);
                format.setTopBorderColor(borderColor);
                format.setBottomBorderColor(borderColor);
    
                //format.setFontBold(false);
                view.setRowHeightAuto(r1, c1, r2, c2, true);
                //view.setRowHeight(r1, r2, 200, true, true);
                format.setWordWrap(true);
                //</editor-fold>
            } else if (formatType == BORDER_FORMAT_NON_WRAP) {
                //<editor-fold defaultstate="collapsed" desc="Border cho du lieu binh thuong">            
                short border = CellFormat.BorderThin;
                format.setLeftBorder(border);
                format.setRightBorder(border);
                format.setTopBorder(border);
                format.setBottomBorder(border);
                format.setHorizontalInsideBorder(border);
                format.setVerticalInsideBorder(border);
    
                Color borderColor = Color.GREEN.darker();
                format.setLeftBorderColor(borderColor);
                format.setRightBorderColor(borderColor);
                format.setTopBorderColor(borderColor);
                format.setBottomBorderColor(borderColor);
    
                //format.setFontBold(false);
                format.setPattern((short) 1);
                //view.setRowHeightAuto(r1, c1, r2, c2, true);
                view.setRowHeight(r1, r2, 200, true, true);
                format.setWordWrap(false);
                //</editor-fold>
            } else if (formatType == BLACK_BORDER_FORMAT) {
                //<editor-fold defaultstate="collapsed" desc="Border cho du lieu binh thuong">
                short border = CellFormat.BorderThin;
                format.setLeftBorder(border);
                format.setRightBorder(border);
                format.setTopBorder(border);
                format.setBottomBorder(border);
                format.setHorizontalInsideBorder(border);
                format.setVerticalInsideBorder(border);
    
                Color borderColor = Color.BLACK.darker();
                format.setLeftBorderColor(borderColor);
                format.setRightBorderColor(borderColor);
                format.setTopBorderColor(borderColor);
                format.setBottomBorderColor(borderColor);
    
                //format.setFontBold(false);
                format.setWordWrap(true);
                //</editor-fold>
            } else if (formatType == BLACK_BORDER_FORMAT_NON_WRAP) {
                //<editor-fold defaultstate="collapsed" desc="Border cho du lieu binh thuong">
                short border = CellFormat.BorderThin;
                format.setLeftBorder(border);
                format.setRightBorder(border);
                format.setTopBorder(border);
                format.setBottomBorder(border);
                format.setHorizontalInsideBorder(border);
                format.setVerticalInsideBorder(border);
    
                Color borderColor = Color.BLACK.darker();
                format.setLeftBorderColor(borderColor);
                format.setRightBorderColor(borderColor);
                format.setTopBorderColor(borderColor);
                format.setBottomBorderColor(borderColor);
    
                //format.setFontBold(false);
                format.setWordWrap(false);
                //</editor-fold>
            } else if (formatType == TITLE) {
                format.setFontBold(true);
                format.setPattern((short) 1);
                format.setPatternFG(Color.GREEN);
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
            } else if (formatType == GROUP_LEVEL1_FORMAT) {
                format.setFontBold(true);
                format.setPattern((short) 1);
                format.setPatternFG(Color.ORANGE);
            } else if (formatType == GROUP_LEVEL2_FORMAT) {
                format.setFontBold(true);
                format.setFontItalic(true);
                format.setPattern((short) 1);
                format.setPatternFG(new Color(135, 206, 250));
            } else if (formatType == GROUP_LEVEL3_FORMAT) {
                format.setFontBold(true);
                format.setFontItalic(true);
                format.setPattern((short) 1);
                format.setPatternFG(new Color(10, 175, 255));
            } else if (formatType == GROUP_LEVEL4_FORMAT) {
                format.setFontBold(true);
                format.setFontItalic(true);
                format.setPattern((short) 1);
                format.setPatternFG(new Color(209, 232, 170));
            } else if (formatType == GROUP_LEVEL5_FORMAT) {
                format.setFontBold(true);
                format.setFontItalic(true);
                format.setPattern((short) 1);
                format.setPatternFG(new Color(250, 250, 210));
            } else if (formatType == GROUP_LEVEL6_FORMAT) {
                format.setFontBold(true);
                format.setFontItalic(true);
                format.setPattern((short) 1);
                format.setPatternFG(new Color(209, 238, 238));
            } else if (formatType == ACCOUNTING_FORMAT) {
                String numberFormat = "_(* #,##0_);_(* (#,##0);_(* \"-\"_);_(@_)";
                format.setCustomFormat(numberFormat);
            } else if (formatType == NUMBER_FORMAT) {
                String numberFormat = "#,##0";
                format.setCustomFormat(numberFormat);
            } else if (formatType == NUMBER_FORMAT_D) {
                String numberFormat = "#,##0.00";
                format.setCustomFormat(numberFormat);
            } else if (formatType == BOLD_WHITE) { // SonPN
                format.setFontBold(true);
                format.setPattern((short) 1);
                format.setPatternFG(Color.WHITE);
                //format.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
            } else if (formatType == CENTER_FORMAT) {
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
            } else if (formatType == LEFT_FORMAT) {
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
            } else if (formatType == BOLD_CENTER_FORMAT) {
                format.setFontBold(true);
                format.setWordWrap(true);
                format.setVerticalAlignment((short) 1);
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
            } else if (formatType == BOLD_FORMAT) {
                format.setFontBold(true);
            } else if (formatType == BOLD_ITALIC_FORMAT) {
                format.setFontBold(true);
                format.setFontItalic(true);
            } else if (formatType == BOLD_WHITE_CENTER) { // SonPN
                format.setFontBold(true);
                format.setPattern((short) 1);
    //            format.setPatternFG(Color.WHITE);
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
            } else if (formatType == NOMARL_WHITE_CENTER) { // SonPN
                format.setFontBold(false);
                format.setPattern((short) 1);
                format.setPatternFG(Color.WHITE);
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
            } else if (formatType == NORMAL_ITALIC) {
                format.setFontItalic(true);
            } else if (formatType == GROUP_DATA_FORMAT) {
                format.setPattern((short) 1);
                format.setPatternFG(Color.YELLOW);
            } else if (formatType == PERCENT_FORMAT) {
                format.setFontBold(true);
    //            format.setFontItalic(true);
                format.setPattern((short) 1);
    //            format.setPatternFG(new Color(209, 232, 170));
                String percentFormat = "#,##" + "%";
                format.setCustomFormat(percentFormat);
            } else if (formatType == CENTER_VERTICAL_FORMAT) {
                format.setVerticalAlignment(CellFormat.VerticalAlignmentCenter);
            } else if (formatType == SHRINK_TO_FIT) {
                format.setShrinkToFit(true);
            } else if (formatType == HEADER_ORANGE) {
                format.setFontBold(true);
//                format.setVerticalAlignment(CellFormat.VerticalAlignmentCenter);
//                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
                format.setPattern((short) 1);
                format.setPatternFG(Color.ORANGE);
            } else if (formatType == HEADER_YELLOW) {
                format.setFontBold(true);
                format.setVerticalAlignment(CellFormat.VerticalAlignmentCenter);
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
                format.setPattern((short) 1);
                format.setPatternFG(Color.YELLOW);
            } else if (formatType == ALIGN_LEFT) {
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
            } else if (formatType == ALIGN_RIGHT) {
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
            } else if (formatType == CELL_COLOR_YELLOW) {
                format.setPattern((short) 1);
                format.setPatternFG(Color.YELLOW);
            }else if (formatType == CELL_COLOR_GREEN) {
                    format.setPattern((short) 1);
                    format.setPatternFG(Color.GREEN);
            }else if (formatType == CELL_COLOR_BLUE) {
                format.setPattern((short) 1);
                format.setPatternFG(Color.BLUE);
            }else if (formatType == CELL_COLOR_SAMPLE) {
                format.setPattern((short) 1);
                format.setPatternFG(new Color(153, 204, 255));
            }else if (formatType == NO_WRAP_TEXT) {
                format.setWordWrap(false);
            } else if (formatType == BLACK_BORDER_NO_HORIZONTAL_NONE_WRAP) {
                short border = CellFormat.BorderThin;
                format.setLeftBorder(border);
                format.setRightBorder(border);
                format.setTopBorder(border);
                format.setBottomBorder(border);
                format.setVerticalInsideBorder(border);
                Color borderColor = Color.BLACK.darker();
                format.setLeftBorderColor(borderColor);
                format.setRightBorderColor(borderColor);
                format.setTopBorderColor(borderColor);
                format.setBottomBorderColor(borderColor);
                format.setWordWrap(false);
            } else if (formatType == BLACK_BORDER_NO_HORIZONTAL) {
                short border = CellFormat.BorderThin;
                format.setLeftBorder(border);
                format.setRightBorder(border);
                format.setTopBorder(border);
                format.setBottomBorder(border);
                format.setVerticalInsideBorder(border);
                Color borderColor = Color.BLACK.darker();
                format.setLeftBorderColor(borderColor);
                format.setRightBorderColor(borderColor);
                format.setTopBorderColor(borderColor);
                format.setBottomBorderColor(borderColor);
                format.setWordWrap(true);
            } else if (formatType == BORDER_FORMAT_NO_ROW_HEIGHT) {
                short border = CellFormat.BorderThin;
                format.setLeftBorder(border);
                format.setRightBorder(border);
                format.setTopBorder(border);
                format.setBottomBorder(border);
                format.setHorizontalInsideBorder(border);
                format.setVerticalInsideBorder(border);
                Color borderColor = Color.GREEN.darker();
                format.setLeftBorderColor(borderColor);
                format.setRightBorderColor(borderColor);
                format.setTopBorderColor(borderColor);
                format.setBottomBorderColor(borderColor);
                format.setWordWrap(true);
    //            view.setRowHeightAuto(r1, c1, r2, c2, true);
            //#095 Start
            } else if (formatType == MERGE_CELL) {
                format.setMergeCells(true);
            } else if (formatType == COLOR_RED) {
                format.setFontColor(Color.RED);
            } else if (formatType == NO_WORK_PROCESS) {
                format.setPattern((short) 1);
                format.setPatternFG(Color.PINK);
            } else if (formatType == PAST_LOCK) {
                format.setPattern((short) 1);
                format.setPatternFG(Color.GRAY);
            } else if (formatType == ORG_LOCK) {
                format.setPattern((short) 1);
                format.setPatternFG(Color.lightGray);
            } else if (formatType == FORMAT_1) {
                format.setFontSize(13);
                format.setFontBold(true);
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
            } else if (formatType == FORMAT_2) {
                format.setFontSize(12);
                format.setFontBold(true);
                format.setFontItalic(true);
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
            } else if (formatType == UNDERLINE_GREEN) {
                format.setFontSize(12);
                Color borderColor = Color.GREEN.brighter();
                format.setPatternFG(borderColor);
                format.setFontBold(true);
                format.setFontUnderline((short) 1);
                format.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
            }
            //#095 End
            // #44 xuat bao cao start
            else if (formatType == ONLY_PERCENT_FORMAT) {
                String percentFormat = "#,##" + "%";
                format.setCustomFormat(percentFormat);
            } else if (formatType == FONT_TIMES_NEW_ROMAN) {
                format.setFontName("Times New Roman");
            } else if (formatType == ROW_HEIGHT_AUTO) {
                view.setRowHeightAuto(r1, c1, r2, c2, true);
            } else if (formatType == FONT_SIZE_13) {
                format.setFontSize(13);
            } else if (formatType == BLUE_TEXT) {
                format.setFontColor(Color.BLUE);
            }
            // #44 xuat bao cao end
            view.setCellFormat(format, r1, c1, r2, c2);
        }
        //#042 End
    }
    //044 d2t END


    /**
     * Format cell
     *
     * @param c1 Left
     * @param c2 Right
     * @param formatType Loai cell
     * @throws com.jxcell.CellException
     */
    public void setCellFormat(int c1, int c2, int formatType) throws CellException {
        setCellFormat(lastRow, c1, lastRow, c2, formatType);
    }

    /**
     *
     * @param c1
     * @param c2
     * @param formatType
     * @throws CellException
     */
    public void setCellFormat(int c1, int c2, CellFormat formatType) throws CellException {
        setCellFormat(lastRow, c1, lastRow, c2, formatType);
    }

    /**
     *
     * @param r1
     * @param c1
     * @param r2
     * @param c2
     * @return
     * @throws CellException
     */
    public CellFormat getCellFormat(int r1, int c1, int r2, int c2) throws CellException {
        return view.getCellFormat(r1, c1, r2, c2);
    }

    /**
     *
     * @param r1
     * @param c1
     * @param r2
     * @param c2
     * @param r3
     * @param c3
     * @param r4
     * @param c4
     * @throws CellException
     */
    public void copyRange(int r1, int c1, int r2, int c2, int r3, int c3, int r4, int c4) throws CellException {
        view.copyRange(r1, c1, r2, c2, view, r3, c3, r4, c4);
    }

    /**
     * Merge cell.
     *
     * @param r1 Top
     * @param c1 Left
     * @param r2 Bottom
     * @param c2 Right
     * @throws CellException CellException
     */
    public void mergeCell(int r1, int c1, int r2, int c2)
            throws CellException {
        try {
            setCellFormat(r1, c1, r2, c2, MERGE_CELL);
        } catch (CellException e) {
            LOGGER.debug("debug", e);
            view.setSelection(r1, c1, r2, c2);
            CellFormat format = view.getCellFormat();
            format.setMergeCells(true);
            view.setCellFormat(format);
        }
    }

    /**
     * Merge cell.
     *
     * @param c1 Left
     * @param c2 Right
     * @throws CellException CellException
     */
    public void mergeCell(int c1, int c2)
            throws CellException {
        try {
            mergeCell(lastRow, c1, lastRow, c2);
        } catch (CellException e) {
            LOGGER.debug("debug", e);
            view.setSelection(lastRow, c1, lastRow, c2);
            CellFormat format = view.getCellFormat();
            format.setMergeCells(true);
            view.setCellFormat(format);
        }
    }

    /**
     * Merge header (cho bao cao dong).
     *
     * @param row Dong
     * @param exportHeaderBean ExportHeaderBean
     * @param groupHeader Ten group header
     * @throws CellException CellException
     */
    public void mergeHeaders(int row, ExportHeaderBean exportHeaderBean, String groupHeader)
            throws CellException {
        if (exportHeaderBean.getLastColumn() > exportHeaderBean.getFirstColumn()) {
            view.setTextAsValue(row, exportHeaderBean.getFirstColumn(), groupHeader);
            view.setSelection(row, exportHeaderBean.getFirstColumn(), row, exportHeaderBean.getLastColumn() - 1);
            CellFormat format = view.getCellFormat();
            format.setMergeCells(true);
            view.setCellFormat(format);
        }
    }

    /**
     * Thiet lap gia tri cho bao cao dong.
     *
     * @param stat Du lieu thong ke dong
     * @param entityId ID doi tuong
     * @param groupHeader ExportHeaderBean
     * @throws Exception Exception
     */
    public void setEntryForDynamicColumns(List<DynamicCellBean> stat, Long entityId, ExportHeaderBean groupHeader)
            throws Exception {
        Integer col;
        for (DynamicCellBean e : stat) {
            if ((e.getRowId() != null) && e.getRowId().equals(entityId)) {
                col = groupHeader.getColumn(e.getColumnId());
                if (col != null && col > 0) {
                    view.setTextAsValue(lastRow, col, e.getText());
                }
            }
        }
    }

    /**
     * Thiet lap gia tri cho bao cao dong.
     *
     * @param stat Du lieu thong ke dong
     * @param entityId ID doi tuong
     * @param groupHeader ExportHeaderBean
     * @param offset
     * @throws Exception Exception
     */
    public void setEntryForDynamicColumns(List<DynamicCellBean> stat, Long entityId, ExportHeaderBean groupHeader, int offset)
            throws Exception {
        Integer col;
        for (DynamicCellBean e : stat) {
            if ((e.getRowId() != null) && e.getRowId().equals(entityId)) {
                col = groupHeader.getColumn(e.getColumnId());
                if (col != null && col > 0) {
                    view.setTextAsValue(lastRow, col + offset, e.getText());
                }
            }
        }
    }

    /**
     *
     * @param stat
     * @param entityId
     * @param col
     * @throws Exception
     */
    public void setEntryForDynamicColumns(List<DynamicCellBean> stat, Long entityId, int col)
            throws Exception {
        for (DynamicCellBean e : stat) {
            if ((e.getRowId() != null) && e.getRowId().equals(entityId)) {
                if (col > 0) {
                    view.setTextAsValue(lastRow, col, e.getText());
                }
            }
        }
    }

    /**
     * Thiet lap gia tri cho cell o dong hien tai.
     *
     * @param text Gia tri
     * @param column Cot
     * @throws CellException CellException
     */
    public void setEntry(String text, int column)
            throws CellException {
        if (!CommonUtils.isNullOrEmpty(text)) {
            view.setTextAsValue(lastRow, column, text);
        }
    }

    /**
     * Thiet lap gia tri cho cell o dong hien tai.
     *
     * @param obj
     * @param column Cot
     * @throws CellException CellException
     */
    public void setEntry(Object obj, int column)
            throws CellException {
        if (obj != null) {
            view.setTextAsValue(lastRow, column, obj.toString());
        }
    }

    /**
     * Thiet lap gia tri cho cell o dong row.
     *
     * @param text Gia tri
     * @param column Cot
     * @param row Dong
     * @throws CellException CellException
     */
    public void setEntry(String text, int column, int row)
            throws CellException {
        if (!CommonUtils.isNullOrEmpty(text)) {
            view.setTextAsValue(row, column, text);
        }
    }

    /**
     * Thiet lap gia tri cho cell o dong row.
     *
     * @param text Gia tri
     * @param column Cot
     * @throws CellException CellException
     */
    public void setCenterAlignmentEntry(String text, int column)
            throws CellException {
        view.setTextAsValue(this.getLastRow(), column, text);
        CellFormat format = view.getCellFormat(this.getLastRow(), column, this.getLastRow(), column);
        format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
        view.setCellFormat(format, this.getLastRow(), column, this.getLastRow(), column);
    }

    /**
     * Thiet lap gia tri cho cell o dong row.
     *
     * @param text Gia tri
     * @param column Cot
     * @param row Dong
     * @param alignHorizontal: can giua theo chieu rong 0 : can trai 1 : can
     * giua 2 : can phai
     * @param alignVertical : can giua theo chieu cao 0 : can tren 1 : can giua
     * 2 : can duoi
     * @throws CellException CellException
     */
    public void setAlignmentEntry(String text, int column, int row, int alignHorizontal, int alignVertical)
            throws CellException {
        view.setText(row, column, text);
        CellFormat format = view.getCellFormat(row, column, row, column);
        if (alignHorizontal == 0) {
            format.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
        } else if (alignHorizontal == 1) {
            format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
        } else if (alignHorizontal == 2) {
            format.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
        }

        if (alignVertical == 0) {
            format.setVerticalAlignment(CellFormat.VerticalAlignmentTop);
        } else if (alignVertical == 1) {
            format.setVerticalAlignment(CellFormat.VerticalAlignmentCenter);
        } else if (alignVertical == 2) {
            format.setVerticalAlignment(CellFormat.VerticalAlignmentBottom);
        }
        view.setCellFormat(format, row, column, row, column);
    }

    /**
     * @param text Gia tri
     * @param column Cot
     * @param alignHorizontal: can giua theo chieu rong 0 : can trai 1 : can
     * giua 2 : can phai
     * @param alignVertical : can giua theo chieu cao 0 : can tren 1 : can giua
     * 2 : can duoi
     * @throws CellException CellException
     */
    public void setAlignmentEntry(String text, int column, int alignHorizontal, int alignVertical)
            throws CellException {
        int row = this.getLastRow();
        view.setText(row, column, text);
        CellFormat format = view.getCellFormat(row, column, row, column);
        if (alignHorizontal == 0) {
            format.setHorizontalAlignment(CellFormat.HorizontalAlignmentLeft);
        } else if (alignHorizontal == 1) {
            format.setHorizontalAlignment(CellFormat.HorizontalAlignmentCenter);
        } else if (alignHorizontal == 2) {
            format.setHorizontalAlignment(CellFormat.HorizontalAlignmentRight);
        }

        if (alignVertical == 0) {
            format.setVerticalAlignment(CellFormat.VerticalAlignmentTop);
        } else if (alignVertical == 1) {
            format.setVerticalAlignment(CellFormat.VerticalAlignmentCenter);
        } else if (alignVertical == 2) {
            format.setVerticalAlignment(CellFormat.VerticalAlignmentBottom);
        }
        view.setCellFormat(format, row, column, row, column);
    }

    /**
     * Thiet lap gia tri cho cell o dong row.
     *
     * @param text Gia tri
     * @param column Cot
     * @param row Dong
     * @param horizontalAlignment
     * @throws CellException CellException
     */
    public void setHorizontalAlignmentEntry(String text, int column, int row, short horizontalAlignment)
            throws CellException {
        view.setTextAsValue(row, column, text);
        CellFormat format = view.getCellFormat(row, column, row, column);
        format.setHorizontalAlignment(horizontalAlignment);
        view.setCellFormat(format, row, column, row, column);
    }

    /**
     * Thiet lap gia tri cho cell o dong hien tai.
     *
     * @param text Gia tri
     * @param column Cot
     * @throws CellException CellException
     */
    public void setText(String text, int column)
            throws CellException {
        if (!CommonUtils.isNullOrEmpty(text)) {
            view.setText(lastRow, column, text);
        }
    }

    /**
     * Thiet lap gia tri cho cell o dong row.
     *
     * @param text Gia tri
     * @param column Cot
     * @param row Dong
     * @throws CellException CellException
     */
    public void setText(String text, int column, int row)
            throws CellException {
        if (!CommonUtils.isNullOrEmpty(text)) {
            view.setText(row, column, text);
        }
    }
    
    //#148 Start
    public void setTextNotCondition(String text, int column, int row)  
            throws CellException {
        view.setText(row, column, text);
    }
    
    // #148 End

    /**
     * Thiet lap gia tri so cho cell o dong row, cot column
     *
     * @param num: so
     * @param column
     * @throws com.jxcell.CellException
     */
    public void setNumber(Double num, int column) throws CellException {
        if (num == null) {
            num = 0D;
        }
        CellFormat format = view.getCellFormat(lastRow, column, lastRow, column);
        format.setCustomFormat("#,###");
        view.setNumber(lastRow, column, num);
        view.setCellFormat(format, lastRow, column, lastRow, column);
    }

    /**
     *
     * @param num
     * @param column
     * @throws CellException
     */
    public void setNumberNoFormat(Double num, int column) throws CellException {
        if (num == null) {
            num = 0D;
        }
        view.setNumber(lastRow, column, num);
    }

    /**
     * Thiet lap gia tri so cho cell o dong row, cot column
     *
     * @param num: so
     * @param column
     * @param row
     * @throws com.jxcell.CellException
     */
    public void setNumber(Double num, int column, int row) throws CellException {
        if (num == null) {
            num = 0D;
        }
        CellFormat format = view.getCellFormat(row, column, row, column);
        format.setCustomFormat("#,##0");
        view.setNumber(row, column, num);
        view.setCellFormat(format, row, column, row, column);
    }

    /**
     *
     * @param num
     * @param fmt
     * @param column
     * @param row
     * @throws CellException
     */
    public void setNumberFormat(Double num, String fmt, int column, int row) throws CellException {
        if (num == null) {
            num = 0D;
        }
        CellFormat format = view.getCellFormat(row, column, row, column);
        format.setCustomFormat(fmt);
        view.setNumber(row, column, num);
        view.setCellFormat(format, row, column, row, column);
    }

    /**
     * Thiet lap gia tri so cho cell o cot column
     *
     * @param num: so
     * @param column
     * @throws com.jxcell.CellException
     */
    public void setNumberD(Double num, int column) throws CellException {
        if (num == null) {
            num = 0D;
        }
        CellFormat format = view.getCellFormat(lastRow, column, lastRow, column);
        format.setCustomFormat("#,##0.00");
        view.setNumber(lastRow, column, num);
        view.setCellFormat(format, lastRow, column, lastRow, column);
    }

    /**
     * Thiet lap gia tri so cho cell o dong row, cot column
     *
     * @param num: so
     * @param column
     * @param row
     * @throws com.jxcell.CellException
     */
    public void setNumberD(Double num, int column, int row) throws CellException {
        if (num == null) {
            num = 0D;
        }
        CellFormat format = view.getCellFormat(row, column, row, column);
        format.setCustomFormat("#,##0.00");
        view.setNumber(row, column, num);
        view.setCellFormat(format, row, column, row, column);
    }

    /**
     * Thiet lap cong thuc cho cell.
     *
     * @param text Gia tri
     * @param column Cot
     * @param row Dong
     * @throws CellException CellException
     */
    public void setFormula(String text, int column, int row)
            throws CellException {
        view.setFormula(row, column, text);
        //view.recalc();
    }

    /**
     *
     * @param text
     * @param column
     * @throws CellException
     */
    public void setFormula(String text, int column)
            throws CellException {
        view.setFormula(lastRow, column, text);
        //view.recalc();
    }

    /**
     * Chuyen chi so cot (0, 1, 2, 3,..) thanh nhan (A, B, C,...).
     *
     * @param column Chi so cot
     * @return Nhan tuong ung
     */
    public String convertColumnIndexToLabel(int column) {
        final int ALPHABET_NUMBER = 26; // so chu cai
        if (column < ALPHABET_NUMBER) {
            return String.valueOf((char) ('A' + column));
        } else {
            int temp = column / ALPHABET_NUMBER;
            column -= ALPHABET_NUMBER * temp;
            String result = String.valueOf((char) ('A' + temp - 1));
            return result + ((char) ('A' + column));
        }
    }

    /**
     *
     * @param list
     * @param entityId
     * @return
     */
    public String getEmployeeNumber(List<Object[]> list, Long entityId) {
        for (Object[] a : list) {
            if (entityId.equals((Long) a[0])) {
                return ((Long) a[1]).toString();
            }
        }
        return "";
    }

    /**
     *
     * @param sheetIndex
     * @throws Exception
     */
    public void setActiveSheet(int sheetIndex) throws Exception {
        view.setSheet(sheetIndex);
        view.setSheetSelected(sheetIndex, true);
    }

    public void setSheetName(String sheetName) throws Exception {
        view.setSheetName(view.getSheet(), sheetName);
    }

    /**
     *
     * @param sheetIndex
     * @throws Exception
     */
    public void setInactiveSheet(int sheetIndex) throws Exception {
        view.setSheet(sheetIndex);
        view.setSheetSelected(sheetIndex, false);
    }

    /**
     * set cot an
     *
     * @param col
     * @throws Exception
     */
    public void hideColumn(int col) throws Exception {
        view.setColHidden(col, true);

    }

    /**
     * an dong
     *
     * @param row
     * @throws Exception
     */
    public void hideRow(int row) throws Exception {
        view.setRowHidden(row, true);

    }

    public void addText(String str, int row, int col) throws CellException {
        String text = view.getText(row, col);
        view.setText(row, col, CommonUtils.NVL(text) + CommonUtils.NVL(str));
    }
    //#042 Start
    /**
     * clearCell
     * 
     * @param row
     * @param col
     * @throws CellException
     */
    public void clearCell(int row, int col) throws CellException {
      view.setText(row, col, "");
    }

    /**
     * getCellText
     * 
     * @param row
     * @param col
     * @return
     * @throws CellException
     */
    public String getCellText(int row, int col) throws CellException {
      return view.getEntry(row, col);
    }
    //#042 #End
    // #110 start

    /**
     * Nhân bản một sheet và đặt tên cho sheet mới. Sau khi nhân bản sheet thành
     * công, activeSheet sẽ là sheet mới được nhân bản thêm vào.
     *
     * @param srcSheetIndex Sheet nguồn là mẫu để nhân bản.
     * @param destSheetIndex Vị trí index của sheet mới nhân bản tạo ra.
     * @param sheetName Tên của sheet mới nhân bản.
     * @throws Exception
     */
    public void cloneSheets(int srcSheetIndex, int destSheetIndex, String sheetName) throws Exception {
        View view2 = new View();
        if (isXLSX) {
            view2.readXLSX(templateFile);
        } else {
            view2.read(templateFile);
        }
        view2.setSheetName(srcSheetIndex, sheetName);
        view.insertSheets(destSheetIndex, 1);
        view.CopySheetFromBook(view2, srcSheetIndex, destSheetIndex);
        setActiveSheet(destSheetIndex);
    }
    
    /**
     * Đường dẫn trỏ đến file template
     */
    private String templateFile;
    
    
    public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

    // #110 end

//#148 start
    /**
     * Ghi ra file Excel.
     *
     * @param exportFile File Excel xuat ra
     * @param lastColumn Cot cuoi cung (khong phai la so cot)
     * @param saveToPath Duong dan luu file
     * @throws Exception Exception
     */
    public void saveFile(String exportFile, int lastColumn, String saveToPath, HttpServletRequest req) throws Exception {
        if (lastRow > startDataRow) {
            this.setCellFormat(startDataRow, startColumn, lastRow, lastColumn, BORDER_FORMAT);
        }

        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        exportFile = prefixOutPutFile + exportFile;
        if (isXLSX) {
            File folder = new File(saveToPath);
            if (!folder.exists()) {
                if( folder.mkdirs() || folder.mkdir() ) {
                    exportFile = exportFile + ".xlsx";
                    view.writeXLSX(saveToPath + exportFile);
                }
            } else {
                exportFile = exportFile + ".xlsx";
                view.writeXLSX(saveToPath + exportFile);
            }
        } else {
            exportFile = exportFile + ".xls";
            view.write(saveToPath + exportFile);
        }
    }
    
    /**
     * lay gia tri cu
     * @param row
     * @param column
     * @return
     * @throws CellException
     */
    public String getText(int row, int column)  throws CellException{
        return view.getText(row, column);
    }
    
    public void saveFile(String saveToPath) throws Exception {
        view.writeXLSX(saveToPath);
    }
    
    
//#148 end
    
    /**
     * insertRowAfter
     *
     * @param row
     * @param numberOfColumn
     * @throws CellException
     */
    public void insertRow(int row, int numberOfColumn) throws CellException {
        view.insertRange(row, 0, row, 100, (short) 3);
    }
    

    public View getView() {
        return view;
    }

    /**
     * Thiet lap gia tri cho cell o dong hien tai.
     *
     * @param text Gia tri
     * @param column Cot
     * @throws CellException CellException
     */
    public void setEntry2(String text, int column)
            throws CellException {
        view.setTextAsValue(lastRow, column, CommonUtils.NVL(text));
    }

    public void setEntry2(Object obj, int column)
            throws CellException {
        if (obj != null) {
            view.setTextAsValue(lastRow, column, obj.toString());
        } else {
            view.setTextAsValue(lastRow, column, "");
        }
    }
    
    public void setEntry(Object obj, int column, int row)
            throws CellException {
        if (obj != null) {
            view.setTextAsValue(row, column, obj.toString());
        }
    }
}
