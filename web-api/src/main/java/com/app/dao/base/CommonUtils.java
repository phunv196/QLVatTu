/*
 * Copyright (C) 2010 HRPlus. All rights reserved.
 * HRPLUS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.dao.base;

import com.app.dao.base.converter.*;
import com.app.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cac method dung chung cua he thong.
 *
 * @author HuyenNV
 * @ModifiedBy: HoangCH,PhucNH
 * @since 1.0
 * @version 1.0
 */
public class CommonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
    //    private static BaseHibernateDAO baseHibernateDAO = new BaseHibernateDAO();
    private static final String[] SIGNED_ARR = new String[]{
            "Ã ", "Ã¡", "áº¡", "áº£", "Ã£",
            "Ã¢", "áº§", "áº¥", "áº­", "áº©", "áº«",
            "Äƒ", "áº±", "áº¯", "áº·", "áº³", "áºµ",
            "Ã¨", "Ã©", "áº¹", "áº»", "áº½",
            "Ãª", "á»�", "áº¿", "á»‡", "á»ƒ", "á»…",
            "Ã¬", "Ã­", "á»‹", "á»‰", "Ä©",
            "Ã²", "Ã³", "á»�", "á»�", "Ãµ",
            "Ã´", "á»“", "á»‘", "á»™", "á»•", "á»—",
            "Æ¡", "á»�", "á»›", "á»£", "á»Ÿ", "á»¡",
            "Ã¹", "Ãº", "á»¥", "á»§", "Å©",
            "Æ°", "á»«", "á»©", "á»±", "á»­", "á»¯",
            "á»³", "Ã½", "á»µ", "á»·", "á»¹",
            "Ä‘",
            "Ã€", "Ã�", "áº ", "áº¢", "Ãƒ",
            "Ã‚", "áº¦", "áº¤", "áº¬", "áº¨", "áºª",
            "Ä‚", "áº°", "áº®", "áº¶", "áº²", "áº´",
            "Ãˆ", "Ã‰", "áº¸", "áºº", "áº¼",
            "ÃŠ", "á»€", "áº¾", "á»†", "á»‚", "á»„",
            "ÃŒ", "Ã�", "á»Š", "á»ˆ", "Ä¨",
            "Ã’", "Ã“", "á»Œ", "á»Ž", "Ã•",
            "Ã”", "á»’", "á»�", "á»˜", "á»”", "á»–",
            "Æ ", "á»œ", "á»š", "á»¢", "á»ž", "á» ",
            "Ã™", "Ãš", "á»¤", "á»¦", "Å¨",
            "Æ¯", "á»ª", "á»¨", "á»°", "á»¬", "á»®",
            "á»²", "Ã�", "á»´", "á»¶", "á»¸",
            "Ä�"
    };
    private static final String[] UNSIGNED_ARR = new String[]{
            "a", "a", "a", "a", "a",
            "a", "a", "a", "a", "a", "a",
            "a", "a", "a", "a", "a", "a",
            "e", "e", "e", "e", "e",
            "e", "e", "e", "e", "e", "e",
            "i", "i", "i", "i", "i",
            "o", "o", "o", "o", "o",
            "o", "o", "o", "o", "o", "o",
            "o", "o", "o", "o", "o", "o",
            "u", "u", "u", "u", "u",
            "u", "u", "u", "u", "u", "u",
            "y", "y", "y", "y", "y",
            "d",
            "A", "A", "A", "A", "A",
            "A", "A", "A", "A", "A", "A",
            "A", "A", "A", "A", "A", "A",
            "E", "E", "E", "E", "E",
            "E", "E", "E", "E", "E", "E",
            "I", "I", "I", "I", "I",
            "O", "O", "O", "O", "O",
            "O", "O", "O", "O", "O", "O",
            "O", "O", "O", "O", "O", "O",
            "U", "U", "U", "U", "U",
            "U", "U", "U", "U", "U", "U",
            "Y", "Y", "Y", "Y", "Y",
            "D"
    };

    /**
     * Lay gia tri tu file config.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getConfig(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        return rb.getString(key);
    }

    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.trim().isEmpty());
    }

    /**
     * Check list object is null.
     *
     * @return
     */
    public static boolean isNullOrEmpty(List data) {
        return (data == null || data.isEmpty());
    }

    /**
     * Lay gia tri tu file config.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getConfigValueByKey(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        return rb.getString(key);
    }

    /**
     * Lay gia tri tu file permissionCode.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getPermissionCodeByKey(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("permissionCode");
        return rb.getString(key);
    }

    /**
     * Lay xau gia tri tu file ApplicationResources.properties.
     *
     * @param key Khoa
     * @return Gia tri
     */
    public static String getApplicationResource(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("ApplicationResources");
        return rb.getString(key);
    }

    /**
     * Lay tham so config mail
     *
     * @param key Khoa
     * @return gia tri
     */
    public static String getMailConfigProperties(String key) {
        ResourceBundle rb = ResourceBundle.getBundle("mail");
        return rb.getString(key);
    }

    /**
     * Lay xau gia tri tu file ApplicationResources.properties.
     *
     * @author HoangCH
     * @param key Khoa
     * @param args
     * @return Gia tri
     */
    public static String getMessage(String key, Object... args) {
        ResourceBundle rb = ResourceBundle.getBundle("messages");
        try {
            return String.format(rb.getString(key), args);
        } catch (Exception e) {
            return key;
        }
    }

    public static java.sql.Date convertStringToSQLDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        } else {
            String pattern = Constants.COMMON.SQLDATE_FORMAT;
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setLenient(false);
            try {
                Date parsed = dateFormat.parse(date);
                return new java.sql.Date(parsed.getTime());
            } catch (Exception ex) {
                LOGGER.debug(ex.toString());
                return null;
            }
        }
    }

    /**
     * Chuyen doi tuong String thanh doi tuong Date.
     *
     * @param date Xau ngay, co dinh dang duoc quy trinh trong file Constants
     * @return Doi tuong Date
     * @throws Exception Exception
     */
    public static Date convertStringToDate(String date) throws Exception {
        if (date == null || date.trim().isEmpty()) {
            return null;
        } else {
            String pattern = Constants.COMMON.DATE_FORMAT;
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setLenient(false);
            try {
                return dateFormat.parse(date);
            } catch (Exception ex) {
                LOGGER.debug(ex.toString());
                return null;
            }
        }
    }

    /**
     * Chuyen doi tuong String thanh doi tuong Date.
     *
     * @param date Xau ngay, co dinh dang duoc quy trinh trong file Constants
     * @return Doi tuong Date
     * @throws Exception Exception
     */
    public static Date convertStringToDateOther(String date) throws Exception {
        if (date == null || date.trim().isEmpty()) {
            return null;
        } else {
            String pattern = Constants.COMMON.SQLDATE_FORMAT;
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setLenient(false);
            try {
                return dateFormat.parse(date);
            } catch (Exception ex) {
                LOGGER.debug(ex.toString());
                return null;
            }
        }
    }

    /**
     * kiem tra chuoi co kieu Date
     *
     * @param date Xau ngay, co dang dd/MM/yyyy
     * @return Doi tuong Date
     * @throws Exception Exception
     */
    public static Date isDateFormat(String date) throws Exception {
        if (date == null || date.trim().isEmpty()) {
            return null;
        } else {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.parse(date);
        }
    }

    /**
     * Chuyen doi tuong Date thanh doi tuong String.
     *
     * @param date Doi tuong Date
     * @return Xau ngay, co dang dd/MM/yyyy
     */
    public static String convertDateToString(Date date) {
        if (date == null) {
            return "";
        } else {
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        }
    }

    /**
     * Chuyen doi tuong Date thanh doi tuong String
     *
     * @param date Doi tuong Date
     * @param formatPattern Kieu format ngay thang
     * @return Xau ngay voi kieu format truyen vao
     */
    public static String convertDateToString(Date date, String formatPattern) {
        if (date == null) {
            return null;
        } else {
            formatPattern = formatPattern == null ? Constants.COMMON.DATE_FORMAT : formatPattern;
            SimpleDateFormat dateFormat = getSimpleDateFormat(formatPattern);
            return dateFormat.format(date);
        }
    }

    /**
     *
     * @param formatPattern
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(String formatPattern) {
        return new SimpleDateFormat(formatPattern);
    }

    /**
     *
     * @param date
     * @return
     */
    public static String convertDateTimeToString(Date date) {
        try {
            return convertDateToString(date, Constants.COMMON.DATE_TIME_FORMAT);
        } catch (Exception e) {
            LOGGER.debug("debug", e);
            return convertDateToString(date);
        }
    }

    /**
     * Chuyen doi tuong Date thanh doi tuong String
     *
     * @param date Doi tuong Date
     * @param formatPattern Kieu format ngay thang
     * @return Xau ngay voi kieu format truyen vao
     * @throws java.text.ParseException
     */
    public static Date convertStringToDate(String date, String formatPattern) throws ParseException {
        if (date == null || "".equals(date)) {
            return null;
        } else {
            String tg;
            tg = formatPattern == null ? Constants.COMMON.DATE_FORMAT : formatPattern;
            SimpleDateFormat dateFormat = new SimpleDateFormat(tg);
            return dateFormat.parse(date);
        }
    }

    /**
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static Date convertStringToDateTime(String date) throws Exception {
        try {
            return convertStringToDate(date, Constants.COMMON.DATE_TIME_FORMAT);
        } catch (ParseException e) {
            LOGGER.debug("debug", e);
            return convertStringToDate(date);
        }
    }

    /**
     * Tinh khoang thoi gian giua hai ngay.
     *
     * @param startDate Ngay bat dau
     * @param endDate Ngay ket thuc
     * @return Khoang thoi gian, dang "Con mm thang dd ngay"
     */
    public static String getDuration(Date startDate, Date endDate) {
        final int[] MONTH_DAYS = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int date1 = startDate.getDate();
        int month1 = startDate.getMonth();
        int year1 = startDate.getYear() + 1900;
        int date2 = endDate.getDate();
        int month2 = endDate.getMonth();
        int year2 = endDate.getYear() + 1900;

        String remainDuration;
        int diff = (year2 * 10000 + month2 * 100 + date2) - (year1 * 10000 + month1 * 100 + date1);
        if (diff <= 0) {
            remainDuration = "Háº¿t háº¡n há»£p Ä‘á»“ng";
        } else {
            int diffMonth = (year2 - year1) * 12 + (month2 - month1);
            int diffDate;
            if (date1 <= date2) {
                diffDate = date2 - date1;
            } else {
                diffMonth--;
                month2--;
                if (month2 < 0) {
                    month2 += 12;
                    year2--;
                }
                diffDate = date2 - date1 + MONTH_DAYS[month2];
                if (month2 == 2) {
                    if ((year2 % 400 == 0) || (year2 % 4 == 0 && year2 % 100 != 0)) {
                        diffDate++;
                    }
                }
            }
            remainDuration = "CÃ²n" + (diffMonth == 0 ? "" : " " + diffMonth + " thÃ¡ng") + (diffDate == 0 ? "" : " " + diffDate + " ngÃ y");
        }
        return remainDuration;
    }



    /**
     * Format so.
     *
     * @param d So
     * @return Xau
     */
    public static String formatNumber(Long d) {
        if (d == null) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat("###,###");
            return format.format(d);
        }
    }

    /**
     * Format so.
     *
     * @param d So
     * @param parten
     * @return Xau
     */
    public static String formatNumber(Object d, String parten) {
        if (d == null) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat(parten);
            return format.format(d);
        }
    }

    /**
     * Format so.
     *
     * @param d So
     * @return Xau
     */
    public static String formatNumber(Double d) {
        if (d == null) {
            return null;
        } else {
            DecimalFormat format = new DecimalFormat("###,###.#####");
            return format.format(d);
        }
    }

    /**
     * Format Double theo he thong kieu Phap
     *
     * @param itemValue
     * @return
     */
    public static String formatFrenchNumber(Double itemValue) {
        if (itemValue != null) {
            try {
                DecimalFormat df = new DecimalFormat();
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(',');
                symbols.setGroupingSeparator('.');
                df.setDecimalFormatSymbols(symbols);

                return df.format(itemValue);
            } catch (Exception ex) {
                LOGGER.debug("debug", ex);
                return String.valueOf(itemValue);
            }
        } else {
            return "";
        }
    }

    /**
     * Format Double theo he thong kieu Phap
     *
     * @param itemValue
     * @return
     */
    public static String formatFrenchNumber(Long itemValue) {
        if (itemValue != null) {
            try {
                DecimalFormat df = new DecimalFormat();
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setGroupingSeparator('.');
                df.setDecimalFormatSymbols(symbols);
                return df.format(itemValue);
            } catch (Exception ex) {
                LOGGER.debug("debug", ex);
                return String.valueOf(itemValue);
            }
        } else {
            return "";
        }
    }

    /**
     * Format so.
     *
     * @param d So
     * @return Xau
     */
    public static String formatCommaNumber(Long d) {
        if (d == null) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat("###,###");
            return format.format(d);
        }
    }

    /**
     *
     * @param d
     * @return
     */
    public static String formatCommaNumber(Double d) {
        if (d == null) {
            return "";
        } else {
            DecimalFormat format = new DecimalFormat("###,###.###");
            return format.format(d);
        }
    }

    /**
     * Loai cac ki tu /, \, null trong ten file Fix loi path traversal
     *
     * @param input : string
     * @return String
     */
    public static String getSafeFileName(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != '/' && c != '\\' && c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Luu file len server.
     *
     * @param uploadFile Doi tuong FormFile
     * @param fileName Ten file
     * @param uploadPath Duong dan thu muc
     * @throws Exception Exception
     */
//    public static void saveFile(FormFile uploadFile, String fileName, String uploadPath) throws Exception {
//        if (isAllowedType(uploadFile.getFileName())) {
//            File desDir = new File(uploadPath);
//            if (!desDir.exists()) {
//                desDir.mkdir();
//            }
//
//            String url = desDir.getAbsolutePath() + (desDir.getAbsolutePath().endsWith(File.separator) ? "" : File.separator) + getSafeFileName(fileName);
//            OutputStream outStream = new FileOutputStream(url);
//            InputStream inStream = uploadFile.getInputStream();
//            int bytesRead;
//            byte[] buffer = new byte[1024 * 8];
//            while ((bytesRead = inStream.read(buffer, 0, 1024 * 8)) != -1) {
//                outStream.write(buffer, 0, bytesRead);
//            }
//            inStream.close();
//            outStream.close();
//        } else {
//            throw new Exception("FILE TYPE NOT ALLOW");
//        }
//    }

    /**
     * Luu file len server.
     *
     * @param bytes Doi tuong FormFile
     * @param fileName Ten file
     * @param uploadPath Duong dan thu muc
     * @throws Exception Exception
     */
    public static void saveFile(byte[] bytes, String fileName, String uploadPath) throws Exception {
        if (isAllowedType(fileName)) {
            File desDir = new File(uploadPath);
            if (!desDir.exists()) {
                desDir.mkdir();
            }
            String url = desDir.getAbsolutePath() + File.separator + getSafeFileName(fileName);
            FileOutputStream outStream = new FileOutputStream(url);
            outStream.write(bytes);
            outStream.close();
        } else {
            throw new Exception("FILE TYPE NOT ALLOW");
        }
    }

    public static void saveFile(String base64, String fileName, String uploadPath) throws Exception {
        if (isAllowedType(fileName)) {
            File desDir = new File(uploadPath);
            if (!desDir.exists()) {
                desDir.mkdir();
            }
            String url = desDir.getAbsolutePath() + File.separator + getSafeFileName(fileName);
            FileOutputStream outStream = new FileOutputStream(url);
            String base64Image = base64.split(",")[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            outStream.write(imageBytes);
            outStream.close();
        } else {
            throw new Exception("FILE TYPE NOT ALLOW");
        }
    }

    /**
     * Luu file import Excel.
     *
     * @param uploadFile Doi tuong FormFile
     * @param fileName Ten file
     * @param uploadPath Duong dan thu muc
     * @return Doi tuong Workbook
     * @throws Exception Exception
     */
//    public static Workbook saveImportExcelFile(FormFile uploadFile, String fileName, String uploadPath) throws Exception {
//        fileName = CommonUtils.getSafeFileName(CommonUtils.removeSign(fileName));
//        if (isAllowedType(fileName)) {
//            WorkbookSettings ws = new WorkbookSettings();
//            ws.setEncoding("Cp1252"); // UTF-8
//            ws.setCellValidationDisabled(true);
//            Workbook workbook = Workbook.getWorkbook(uploadFile.getInputStream(), ws);
//            return workbook;
//        } else {
//            throw new Exception("FILE TYPE NOT ALLOW");
//        }
//    }

    /**
     *
     * @param fileName
     * @return
     */
    public static boolean isAllowedType(String fileName) {
        if (fileName != null && !"".equals(fileName.trim())) {
            String[] allowedType = {".jpg", ".jpeg", ".png", ".doc", ".docx", ".xls", ".xlsx", ".pdf", ".rar", ".zip", ".gif", ".txt", ".log", ".xml", ".7zip", ".svg"};
            String ext = extractFileExt(fileName);
            if (ext == null) {
                return true;
            }
            ext = ext.toLowerCase();
            for (String extendFile : allowedType) {
                if (extendFile.equals(ext)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String extractFileExt(String fileName) {
        int dotPos = fileName.lastIndexOf(".");
        if (dotPos != -1) {
            return fileName.substring(dotPos);
        }
        return null;
    }



    /**
     *
     * @param chkSource
     * @return
     * @throws JSONException
     */
    public static List<Long> getArrId(String chkSource) throws JSONException {
        chkSource = chkSource.replace("(", "").replace(")", "");
        JSONObject json = new JSONObject(chkSource);
        Iterator iterator = json.keys();
        List<Long> arrId = new ArrayList();
        while (iterator.hasNext()) {
            arrId.add(Long.parseLong(iterator.next().toString()));
        }
        return arrId;
    }

    /**
     *
     * @param array
     * @return
     */
//    public static List<ListBoxBean> convertArrayToList(String[] array) {
//        List<ListBoxBean> list = new ArrayList();
//        // Cac gia tri nay khong duoc bat dau tu 0
//        for (int i = 1; i < array.length; i++) {
//            list.add(new ListBoxBean(array[i], i));
//        }
//        return list;
//    }

    /**
     * copy properties tu form sang BO va nguoc lai
     *
     * @param dest
     * @param orig
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        BeanUtilsBean beanUtilsBean = getUtilsBean();
        beanUtilsBean.copyProperties(dest, orig);
    }

    /**
     *
     * @return
     */
    public static BeanUtilsBean getUtilsBean() {
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new LongConverter(), Long.class);
        convertUtilsBean.register(new DoubleConverter(), Double.class);
        convertUtilsBean.register(new StringToDateConverter(), Date.class);
        convertUtilsBean.register(new DateToStringConverter(), String.class);
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
        return beanUtilsBean;
    }

    /**
     *
     * @param <T>
     * @param list
     * @param beanUtilsBean
     * @return
     * @throws Exception
     */
    public static <T> List<Map> convertListToMap(List<T> list, BeanUtilsBean beanUtilsBean) throws Exception {
        List<Map> listMap = new ArrayList();
        for (T dbo : list) {
            Map map = beanUtilsBean.describe(dbo);
            for (Object key : map.keySet()) {
                try {
                    Date date = CommonUtils.isDateFormat(map.get(key).toString());
                    map.put(key, CommonUtils.convertDateToString(date));
                } catch (Exception ex) {
                    LOGGER.debug("debug", ex);
                }
            }
            listMap.add(map);
        }
        return listMap;
    }

    /**
     * chuyen danh sach doi tuong sang string, phan cach bang separator truyen
     * vao
     *
     * @param <T> : Lop doi tuong
     * @param list : danh sach truyen vao
     * @param separator : ky tu phan cach
     * @return String: xau ket qua
     */
    public static <T> String convertListToString(List<T> list, String separator) {
        if (list != null && !list.isEmpty()) {
            String result = "";
            for (T obj : list) {
                result += obj.toString() + separator;
            }
            return result.substring(0, result.length() - 1);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public static List getMonths() {
        List<ListBoxBean> lstMonths = new ArrayList();
        for (int i = 1; i < 13; i++) {
            ListBoxBean month = new ListBoxBean(CommonUtils.getApplicationResource("global.month") + " " + i, i);
            lstMonths.add(month);
        }
        return lstMonths;
    }

    /**
     *
     * @return
     */
    public static List getQuaters() {
        List<ListBoxBean> lstQuaters = new ArrayList();
        for (int i = 1; i < 5; i++) {
            ListBoxBean quater = new ListBoxBean(CommonUtils.getApplicationResource("global.quarter") + " " + convertDecimalToRoman(i), i);
            lstQuaters.add(quater);
        }
        return lstQuaters;
    }

    /**
     *
     * @return
     */
    public static List getYears() {
        List<ListBoxBean> lstYears = new ArrayList();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        //Nam truoc do
        ListBoxBean year = new ListBoxBean(String.valueOf(currentYear - 1), currentYear - 1);
        lstYears.add(year);

        //Nam hien tai
        year = new ListBoxBean(String.valueOf(currentYear), currentYear);
        lstYears.add(year);

        //Nam ke tiep
        year = new ListBoxBean(String.valueOf(currentYear + 1), currentYear + 1);
        lstYears.add(year);
        return lstYears;
    }

    /**
     * Ham lay danh sach so nam cho Form
     *
     * @author dev2
     * @return
     * @since 1.0
     * @version 1.0
     * @param lenght : so nam can truyen so voi nam hien tai
     */
    public static List getYearsWithLengh(int lenght) {
        List<ListBoxBean> lstYears = new ArrayList();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int temp = lenght;
        ListBoxBean year;
        while (temp > 0) {
            //Nam truoc do
            year = new ListBoxBean(String.valueOf(currentYear - temp), currentYear - temp);
            lstYears.add(year);
            temp--;
        }
        //Nam hien tai
        year = new ListBoxBean(String.valueOf(currentYear), currentYear);
        lstYears.add(year);
        temp = 1;
        while (temp <= lenght) {
            //Nam ke tiep
            year = new ListBoxBean(String.valueOf(currentYear + temp), currentYear + temp);
            lstYears.add(year);
            temp++;
        }

        return lstYears;
    }

    /**
     * Lay danh sach nam tu nam hien tai tro ve truoc
     *
     * @param lenght
     * @return
     */
    public static List getYearsBeforeCurrentWithLengh(int lenght, boolean isSortDesc) {
        List<ListBoxBean> lstYears = new ArrayList();
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        int temp = 1;
        ListBoxBean year;
        if (isSortDesc) {
            year = new ListBoxBean(String.valueOf(currentYear), currentYear);
            lstYears.add(year);
            while (temp < lenght) {
                //Nam truoc do
                year = new ListBoxBean(String.valueOf(currentYear - temp), currentYear - temp);
                lstYears.add(year);
                temp++;
            }
        } else {
            temp = lenght;
            while (temp > 0) {
                //Nam truoc do
                year = new ListBoxBean(String.valueOf(currentYear - temp), currentYear - temp);
                lstYears.add(year);
                temp--;
            }
            //Nam hien tai
            year = new ListBoxBean(String.valueOf(currentYear), currentYear);
            lstYears.add(year);
        }
        return lstYears;
    }

    /**
     * Loai bo cac dau, ten file chi chua cac ky tu ASCII.
     *
     * @param originalName
     * @return String : xau sau khi bo dau
     */
    public static String removeSign(String originalName) {
        if (originalName == null) {
            return "";
        }
        String result = originalName;
        for (int i = 0; i < SIGNED_ARR.length; i++) {
            result = result.replaceAll(SIGNED_ARR[i], UNSIGNED_ARR[i]);
        }
        return result;
    }

    /**
     * Lam tron so thuc // positive value only.
     *
     * @param value
     * @param decimalPlace
     * @return
     */
    public static Double round(double value, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     *
     * @param listId
     * @return
     */
    public static String listLong2String(List<Long> listId) {
        if (listId != null && listId.size() > 0) {
            String returnStr = "";

            for (Long listId1 : listId) {
                returnStr += ("".equals(returnStr) ? listId1.toString() : "," + listId1.toString());
            }

            return returnStr;
        } else {
            return "";
        }
    }

    /**
     * So sanh ngay
     *
     * @author PhucTH
     * @param earlierDate
     * @param laterDate
     * @param equal
     * @return
     */
    public static Boolean compareDate(Date earlierDate, Date laterDate, Boolean equal) {
        /*
         * equal = true: Bang cung hop le false: endDate < startDate
         */
        Calendar earlierDateCal = Calendar.getInstance();
        Calendar laterDateCal = Calendar.getInstance();

        earlierDateCal.setTime(earlierDate);
        laterDateCal.setTime(laterDate);

        if (equal) {
            //Bang cung hop le
            return laterDateCal.after(earlierDateCal) || laterDateCal.equals(earlierDateCal);

        } else {
            return laterDateCal.after(earlierDateCal);
        }
    }

    /**
     * Kiem tra 1 ngay nam trong 1 khoang ngay
     *
     * @param checkDate
     * @param fromDate
     * @param toDate
     * @param equal
     * @return
     */
    public static Boolean checkDateInTimePeriod(Date checkDate, Date fromDate, Date toDate, Boolean equal) {
        if (toDate == null) {
            return true;
        }
        return compareDate(fromDate, checkDate, equal) && compareDate(checkDate, toDate, equal);
    }

    /**
     * Tao listbox chon nam.
     *
     * @author DuongPV5
     * @return danh sach cac nam
     */
    public static List<ListBoxBean> createYearList() {
        List result = new ArrayList();
        int currentYear = new Date().getYear() + 1900;
        for (int i = 0; i <= 10; i++) {
            result.add(new ListBoxBean(String.valueOf(currentYear - i), currentYear - i));
        }
        return result;
    }

    /**
     * Tao listbox chon nam.
     *
     * @author DuongPV5
     * @param startYear
     * @param endYear
     * @return danh sach cac nam
     */
    public static List<ListBoxBean> createYearList(int startYear, int endYear) {
        List result = new ArrayList();
        for (int i = startYear; i <= endYear; i++) {
            result.add(new ListBoxBean(String.valueOf(i), i));
        }
        return result;
    }

    /**
     * Tao listbox chon quy.
     *
     * @author HuyenNV
     * @return danh sach cac nam
     */
    public static List<ListBoxBean> createQuarterList() {
        List result = new ArrayList();
        result.add(new ListBoxBean("QuÃ½ I", 1));
        result.add(new ListBoxBean("QuÃ½ II", 4));
        result.add(new ListBoxBean("QuÃ½ III", 7));
        result.add(new ListBoxBean("QuÃ½ IV", 10));
        return result;
    }

    /**
     * Tao list thang.
     *
     * @author DuongPV5
     * @return ds 12 thang
     */
    public static List<ListBoxBean> createMonthList() {
        List result = new ArrayList();
        for (int i = 1; i <= 12; i++) {
            result.add(new ListBoxBean(String.valueOf(i), i));
        }
        return result;
    }

    /**
     * Tinh so ngay nam giua 2 ngay
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * Cong valueToAdd ngay vao ngay inputDate.
     *
     * @param inputDate
     * @param valueToAdd
     * @return
     * @throws Exception
     */
    public static Date addDate(Date inputDate, int valueToAdd) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(inputDate);
        c.add(Calendar.DATE, valueToAdd);
        return c.getTime();
    }

    /**
     * So sanh 2 ngay. Cac ngay co dinh dang 'dd/mm/yyyy'.
     *
     * @param date1
     * @param date2
     * @return nho hon 0 neu date1 nho hon date2, 0 neu date1 bang date2, lon
     * hon 0 neu date1 lon hon date2
     */
    public static int compareDates(String date1, String date2) {
        String[] a1 = date1.split("/");
        String[] a2 = date2.split("/");

        int year1 = (int) Float.parseFloat(a1[2]);
        int year2 = (int) Float.parseFloat(a2[2]);
        int month1 = (int) Float.parseFloat(a1[1]);
        int month2 = (int) Float.parseFloat(a2[1]);
        int day1 = (int) Float.parseFloat(a1[0]);
        int day2 = (int) Float.parseFloat(a2[0]);

        if (year1 != year2) {
            return year1 - year2;
        } else if (month1 != month2) {
            return month1 - month2;
        } else {
            return day1 - day2;
        }
    }

    /**
     * So sanh 2 ngay. Cac ngay co dinh dang 'dd/mm/yyyy'.
     *
     * @param date1
     * @param date2
     * @return nho hon 0 neu date1 nho hon date2, 0 neu date1 bang date2, lon
     * hon 0 neu date1 lon hon date2
     */
    public static int compareDates(Date date1, Date date2) {
        String strDate1 = convertDateToString(date1);
        String strDate2 = convertDateToString(date2);
        return compareDates(strDate1, strDate2);
    }

    /**
     * Convert mot so sang dang so La Ma.
     *
     * @param decimal So binh thuong
     * @return So La Ma
     */
    public static String convertDecimalToRoman(int decimal) {
        final String[] ROMAN_CODE = {"M", "CM", "D", "CD", "C", "XC", "L",
                "XL", "X", "IX", "V", "IV", "I"};
        final int[] DECIMAL_VALUE = {1000, 900, 500, 400, 100, 90, 50,
                40, 10, 9, 5, 4, 1};
        if (decimal <= 0 || decimal >= 4000) {
            throw new NumberFormatException("Value outside roman numeral range.");
        }
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < ROMAN_CODE.length; i++) {
            while (decimal >= DECIMAL_VALUE[i]) {
                decimal -= DECIMAL_VALUE[i];
                roman.append(ROMAN_CODE[i]);
            }
        }
        return roman.toString();
    }

    /**
     * ham tra lai prefix dia chi email theo dinh dang chuan
     *
     * @param fullName ten nhap vao
     * @return phan prefix cua dia chi email Nguyen van bien --> result biennv
     */
    public static String getPrefixEmailByFullName(String fullName) {
        String emailResult = "";
        if (!CommonUtils.isNullOrEmpty(fullName)) {
            fullName = removeSign(fullName);
            String[] str = fullName.trim().split(" ");
            int strLen = str.length;
            emailResult = str[strLen - 1];
            for (int i = 0; i < strLen - 1; i++) {
                String subStr = str[i].trim();
                if (!"".equals(subStr)) {
                    emailResult += subStr.substring(0, 1);
                }
            }
        }
        return emailResult.toLowerCase();
    }

    /**
     * Convert Long => String. Them 0 vao dau neu so do nho hon 10
     *
     * @param inputLong
     * @return
     */
    public static String addZeroToString(Long inputLong) {
        return (inputLong < 10) ? "0" + inputLong : String.valueOf(inputLong);
    }

    /**
     * @author BienNv
     * @todo Thuc hien tao password ngau nhien
     * @return password
     */
    public static String generatePassword() {

        Random generator = new Random();
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String str = AB + AB.toLowerCase();
        int len = generator.nextInt(9);
        while (len < 8) {
            len = generator.nextInt(9);
        }
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(str.toCharArray()[generator.nextInt(str.length())]);
        }
        return "0" + sb.toString().toLowerCase() + "@F";
    }

    /**
     * So ngay trong thang.
     *
     * @param month Thang, thang chuan
     * @param year Nam, nam chuan
     * @return So ngay trong thang
     */
    public static int getDaysOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * So ngay lam viec trong thang (tru thu 7 va chu nhat)
     *
     * @param month
     * @param year
     * @return
     */
    public static int getTotalWorkingDayOfMonth(int month, int year, List<Date> holidayDateList) {
        int workingDayOfMonth = 0;
        int dayOfMonth = getDaysOfMonth(month, year);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < dayOfMonth; i++) {
            cal.set(Calendar.DATE, i + 1);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.YEAR, year);

            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                    && !checkHolidayDate(cal, holidayDateList)) {
                workingDayOfMonth++;
            }
        }
        return workingDayOfMonth;
    }

    public static boolean checkHolidayDate(Calendar cal, List<Date> holidayDateList) {
        if (cal != null && holidayDateList != null && !holidayDateList.isEmpty()) {
            for (Date date : holidayDateList) {
                if (compareDates(cal.getTime(), date) == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Tinh tong so thang cach nhau giua 2 ngay Neu so ngay le > 15 thi lam tron
     * len thanh 1 thang neu nho hon 15 ngay thi bo qua
     *
     * @param startDate ngay bat dau
     * @param endDate ngay cuoi
     * @return
     */
    public static int getTotalMonthBettween(Date startDate, Date endDate) {
        final int[] MONTH_DAYS = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        endDate = DateUtils.addDays(endDate, 1);
        int date1 = startDate.getDate();
        int month1 = startDate.getMonth();
        int year1 = startDate.getYear() + 1900;
        int date2 = endDate.getDate();
        int month2 = endDate.getMonth();
        int year2 = endDate.getYear() + 1900;

        int diffMonth = (year2 - year1) * 12 + (month2 - month1);
        int diffDate;
        if (date1 <= date2) {
            diffDate = date2 - date1;
        } else {
            diffMonth--;
            month2--;
            if (month2 < 0) {
                month2 += 12;
                year2--;
            }
            diffDate = date2 - date1 + MONTH_DAYS[month2];
            if (month2 == 2) {
                if ((year2 % 400 == 0) || (year2 % 4 == 0 && year2 % 100 != 0)) {
                    diffDate++;
                }
            }
        }
        diffDate++;
        return diffDate >= 15 ? diffMonth + 1 : diffMonth;
    }

    /**
     * Tinh so thang giua 2 date truyen vao tinh ca 2 dau mut
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getTotalMonthNotRoundBettween(Date startDate, Date endDate) {
        endDate = DateUtils.addDays(endDate, 1);
        int date1 = startDate.getDate();
        int month1 = startDate.getMonth();
        int year1 = startDate.getYear() + 1900;
        int date2 = endDate.getDate();
        int month2 = endDate.getMonth();
        int year2 = endDate.getYear() + 1900;
        int diffMonth = (year2 - year1) * 12 + (month2 - month1);
        if (date1 > date2) {
            diffMonth--;
            month2--;
            if (month2 < 0) {
                month2 += 12;
            }
        }

        return diffMonth;
    }

    /**
     * Tinh so ngay le giua hai date tinh ca 2 dau mut
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getOddDay(Date startDate, Date endDate) {
        final int[] MONTH_DAYS = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int date1 = startDate.getDate();
        int date2 = endDate.getDate();
        int month2 = endDate.getMonth();
        int year2 = endDate.getYear() + 1900;

        int diffDate;
        if (date1 <= date2) {
            diffDate = date2 - date1;
        } else {
            month2--;
            if (month2 < 0) {
                month2 += 12;
                year2--;
            }
            diffDate = date2 - date1 + MONTH_DAYS[month2];
            if (month2 == 2) {
                if ((year2 % 400 == 0) || (year2 % 4 == 0 && year2 % 100 != 0)) {
                    diffDate++;
                }
            }
        }
        diffDate++;
        return diffDate;
    }

    /**
     * Chuyen string -> List Long
     *
     * @param inpuString
     * @param separator
     * @return
     */
    public static List<Long> string2ListLong(String inpuString, String separator) {
        List<Long> outPutList = new ArrayList();

        if (inpuString != null && !"".equals(inpuString.trim()) && separator != null && !"".equals(separator.trim())) {
            String[] idArr = inpuString.split(separator);
            for (String idArr1 : idArr) {
                if (idArr1 != null && !"".equals(idArr1.trim())) {
                    outPutList.add(Long.parseLong(idArr1.trim()));
                }
            }
        }

        return outPutList;
    }

    /**
     *
     * @param <T>
     * @param list
     * @param size
     * @return
     */
    public static <T> List<List<T>> partition(List<T> list, int size) {
        return new Partition<>(list, size);
    }

    /**
     * Dao nghich chuoi (vd: input: abc - def - xyz --> xyz - def - abc)
     *
     * @param input : chuoi nhap vao
     * @param separator : ki tu phan cach
     * @param newSeparator
     * @return String
     */
    public static String reverseString(String input, String separator, String newSeparator) {
        if (input != null && !"".equals(input.trim()) && separator != null) {
            String[] elementLst = input.split(separator);
            if (elementLst.length > 0) {
                String output = elementLst[elementLst.length - 1];
                for (int i = elementLst.length - 2; i >= 0; i--) {
                    output += newSeparator + elementLst[i];
                }
                return output;
            }
        }
        return input;
    }

    /**
     * Ham chuyen List sang List kieu Long. Dung de cast List Id
     *
     * @param listInput
     * @return
     */
    public static List<Long> getCastListStringToLong(List listInput) {
        List<Long> lstReturn = new ArrayList();
        for (Object listInput1 : listInput) {
            lstReturn.add(Long.valueOf(listInput1.toString().trim()));
        }
        return lstReturn;
    }

    /**
     * Ham chan SQL INJECTION khi truyen vao 1 xau chua cac Id (Vd: ,,,a,1,2,3,
     * 4 , 5 ,a,,b,,c,,ab,c,a,,, => 1,2,3,4,5)
     *
     * @author phucth
     * @param inputListIds
     * @return
     */
    public static String fixListIdsStr(String inputListIds) {
        if (inputListIds != null && !"".equals(inputListIds.trim())) {

            //Buoc 1: Loai bo tat ca cac ky tu Khong phai la So hoac dau ,
            String patternStr = "[^\\d,]";
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(inputListIds);
            String outputListIds = matcher.replaceAll("");

            //Buoc 2: Thay dau ,, bang dau ,
            while (outputListIds.contains(",,")) {
                outputListIds = outputListIds.replaceAll(",,", ",");
            }

            //Buoc 3: Bo dau , neu co o Dau xau
            while (outputListIds.indexOf(",") == 0) {
                outputListIds = outputListIds.substring(1, outputListIds.length());
            }

            //Buoc 4: Bo dau , neu co o Cuoi xau
            while (outputListIds != null && !"".equals(outputListIds) && (outputListIds.lastIndexOf(",") == (outputListIds.length() - 1))) {
                outputListIds = outputListIds.substring(0, outputListIds.length() - 1);
            }

            return outputListIds;
        } else {
            return "";
        }
    }

    /**
     * Build cau query khi List co nhieu hon 1000 phan tu vd: fieldName =
     * v.organizationId listIdsStr = id1, id2, id3, id4, id5,....., id1000,
     * id1001, id1002 => v.organizationId IN ( id1, id2, id3, id4, id5,.....,
     * id999 ) OR v.organizationId IN ( id1000, id1001, id1002 )
     *
     * @param fieldName
     * @param listIdsStr
     * @return
     */
    public static String genQueryWithBigList(String fieldName, String listIdsStr) {
        String outputQuery;

        if (listIdsStr == null || "".equals(listIdsStr)) {
            return "";
        }
        //Buoc 1: Neu co chua cac ky tu la => Bo di het(Chi giu lai SO va DAU PHAY)
        listIdsStr = fixListIdsStr(listIdsStr);

        //Buoc 2: Tach nho ra thanh cac menh de OR neu Kich thuoc List lon hon 1000
        List<Long> listIds = string2ListLong(listIdsStr, ",");
        if (listIds != null && listIds.size() > 1000) {
            outputQuery = " ( 1=0 ";
            List<List<Long>> adjustedList = CommonUtils.partition(listIds, 999);
            for (List<Long> adjustedList1 : adjustedList) {
                outputQuery += " OR " + fieldName + " IN ( " + listLong2String(adjustedList1) + " ) ";
            }
            outputQuery += " ) ";
        } else {
            String tmpIdsStr = listLong2String(listIds);
            outputQuery = " " + fieldName + " IN ( " + ((tmpIdsStr != null && !"".equals(tmpIdsStr.trim())) ? tmpIdsStr : "-1") + " ) ";
        }

        return outputQuery;
    }

    /**
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static Long NVL(Long value, Long defaultValue) {

        return value == null ? defaultValue : value;
    }

    /**
     *
     * @param value
     * @return
     */
    public static Long NVL(Long value) {

        return NVL(value, 0L);
    }

    /**
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static Double NVL(Double value, Double defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     *
     * @param value
     * @return
     */
    public static Double NVL(Double value) {
        return NVL(value, 0d);
    }

    /**
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static String NVL(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     *
     * @param value
     * @return
     */
    public static String NVL(String value) {
        return NVL(value, "");
    }

    /**
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static Object NVL(Object value, Object defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static Integer NVL(Integer value, Integer defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * Trunc date.
     *
     * @param inputDate
     * @return
     */
    public static Date TRUNC(Date inputDate) {
        if (inputDate == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(inputDate);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR, 0);
            return cal.getTime();
        }
    }

    /**
     *
     * @param number
     * @return
     */
    public static String readNumber(int number) {

        int billion = number / 1000000000;
        number -= billion * 1000000000;

        int million = number / 1000000;
        number -= million * 1000000;

        int thousand = number / 1000;
        int unit = number - thousand * 1000;

        String s = "";
        if (billion != 0) {
            s = s + readTriple(billion) + " tá»·, ";
        }
        if (million != 0) {
            s = s + readTriple(million) + " triá»‡u, ";
        }
        if (thousand != 0) {
            s = s + readTriple(thousand) + " nghÃ¬n, ";
        }
        if (unit != 0) {
            s = s + readTriple(unit);
        }
        s = s.trim();
        if (s.endsWith(",")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private static String readTriple(int number) {
        final String[] HUNDRED_DIGITS = {"", "má»™t trÄƒm", "hai trÄƒm", "ba trÄƒm", "bá»‘n trÄƒm", "nÄƒm trÄƒm", "sÃ¡u trÄƒm", "báº£y trÄƒm", "tÃ¡m trÄƒm", "chÃ­n trÄƒm"};
        final String[] TEN_DIGITS = {" ", " mÆ°á»�i ", " hai mÆ°Æ¡i ", " ba mÆ°Æ¡i ", " bá»‘n mÆ°Æ¡i ", " nÄƒm mÆ°Æ¡i ", " sÃ¡u mÆ°Æ¡i ", " báº£y mÆ°Æ¡i ", " tÃ¡m mÆ°Æ¡i ", " chÃ­n mÆ°Æ¡i "};
        final String[] UNIT_DIGITS = {"", "má»™t", "hai", "ba", "bá»‘n", "nÄƒm", "sÃ¡u", "báº£y", "tÃ¡m", "chÃ­n"};
        final String[] UNIT_DIGITS_2 = {"", "má»‘t", "hai", "ba", "tÆ°", "lÄƒm", "sÃ¡u", "báº£y", "tÃ¡m", "chÃ­n"};

        int hundred = number / 100;
        number -= hundred * 100;
        int ten = number / 10;
        int unit = number - ten * 10;

        String s = HUNDRED_DIGITS[hundred] + TEN_DIGITS[ten] + (ten <= 1 ? UNIT_DIGITS[unit] : UNIT_DIGITS_2[unit]);
        return s.trim();
    }

    /**
     *
     * @param url
     * @return
     */
    public static boolean isAvailableUrlAddress(String url) {
        boolean available = false;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.connect();
            if (conn.getResponseCode() == 200) {
                available = true;
            }
        } catch (IOException e) {
            LOGGER.debug("debug", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return available;
    }

    /**
     *
     * @param month
     * @param year
     * @return
     * @throws Exception
     */
    public static Date getFirstDayOfMonth(int month, int year) throws Exception {
        if (month > 9) {
            return CommonUtils.convertStringToDate("01/" + month + "/" + year);
        } else {
            return CommonUtils.convertStringToDate("01/0" + month + "/" + year);
        }
    }

    /**
     *
     * @param month
     * @param year
     * @return
     * @throws Exception
     */
    public static Date getLastDayOfMonth(int month, int year) throws Exception {
        if (month > 9) {
            return CommonUtils.convertStringToDate(getDaysOfMonth(month, year) + "/" + month + "/" + year);
        } else {
            return CommonUtils.convertStringToDate(getDaysOfMonth(month, year) + "/0" + month + "/" + year);
        }
    }

    public static String getCurrentLanguage(HttpServletRequest req) {
        // TODO Auto-generated method stub
        String langCode = req.getHeader("Current-Language");
        return NVL(langCode, "vi");
    }

    /**
     *
     * @param request
     * @return
     */
    public static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-FORWARDED-FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     *
     * @param endDate
     * @param startDate
     * @return
     */
    public static double monthsBetween(Date endDate, Date startDate) {
        int temp = (endDate.getYear() * 12 + endDate.getMonth()) - (startDate.getYear() * 12 + startDate.getMonth());
        int month = temp - 1;
        for (; month <= temp; month++) {
            if (DateUtils.addMonths(startDate, month).after(endDate)) {
                break;
            }
        }
        long days = (endDate.getTime() - DateUtils.addMonths(startDate, month - 1).getTime()) / (24 * 60 * 60 * 1000) + 1;

        Date tempDate = DateUtils.addDays(endDate, 1);
        if (tempDate.getMonth() == 0) {
            return (month - 1d) + (days * 1d / (getDaysOfMonth(12, tempDate.getYear() + 1900 - 1)));
        }
        return (month - 1d) + (days * 1d / (getDaysOfMonth(tempDate.getMonth(), tempDate.getYear() + 1900)));
    }

    //#014 Start D2T Modify
    /**
     * check BETWEEN startDate AND endDate
     *
     * @param check
     * @param startDate
     * @param endDate
     * @return Boolean
     */
    public static Boolean betweenDate(Date check, Date startDate, Date endDate) {
        Long longCheck = check.getTime(),
                longStartDate = startDate.getTime(),
                longEndDate = endDate.getTime();
        return (longStartDate <= longCheck) && (longCheck <= longEndDate);
    }



    public static boolean validatePassword(String password) {
        String patternPassword = "^(?=.*[0-9])(?=.*[A-z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(patternPassword);
    }

    public static String getExcelColumnName(int number) {
        final StringBuilder sb = new StringBuilder();

        int num = number - 1;
        while (num >= 0) {
            int numChar = (num % 26) + 65;
            sb.append((char) numChar);
            num = (num / 26) - 1;
        }
        return sb.reverse().toString();
    }

    public static Long convertStringToLong(String inputLong) {
        if (StringUtils.isEmpty(inputLong)) {
            return null;
        }
        try {
            return Long.parseLong(inputLong);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String getRealPath(String filePath, HttpServletRequest req) {
        return req.getSession().getServletContext().getRealPath(filePath);
    }

    public static String validateKeySearch(String str) {
        return str.replaceAll("&", "&&").replaceAll("%", "&%").replaceAll("'", "''");
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getYearsBeforeCurrentWithLengh(5, true));
    }

    public static Date getLastDayOfMonth(Date dDate) throws Exception {
        return getLastDayOfMonth(dDate.getMonth() + 1, dDate.getYear() + 1900);
    }

    /**
     * Checks if is object empty.
     *
     * @param object the object
     * @return true, if is object empty
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof String) {
            if (((String) object).trim().length() == 0) {
                return true;
            }
        } else if (object instanceof Collection) {
            return isCollectionEmpty((Collection<?>) object);
        }
        return false;
    }

    /**
     * Checks if is collection empty.
     *
     * @param collection the collection
     * @return true, if is collection empty
     */
    private static boolean isCollectionEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param str         : xau
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(String str, StringBuilder queryString, List<Object> paramList, String field) {
        if ((str != null) && !"".equals(str.trim())) {
            queryString.append(" AND LOWER(").append(field).append(") LIKE ? ESCAPE '/'");
            str = str.replace("  ", " ");
            str = "%" + str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%") + "%";
            paramList.add(str);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param str         : xau
     * @param queryString
     * @param paramList
     * @param
     */
    public static void filterAndOrString(String str, StringBuilder queryString, List<Object> paramList, String field1, String field2) {
        if ((str != null) && !"".equals(str.trim())) {
            queryString.append(" AND ( LOWER(").append(field1).append(") LIKE ? ESCAPE '/'");
            queryString.append(" OR  LOWER(").append(field2).append(") LIKE ? ESCAPE '/'");
            queryString.append(" ) ");
            str = str.replace("  ", " ");
            str = "%" + str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%") + "%";
            paramList.add(str);
            paramList.add(str);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param str         : xau
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterEqual(String str, StringBuilder queryString, List<Object> paramList, String field) {
        if ((str != null) && !"".equals(str.trim())) {
            queryString.append(" AND LOWER(").append(field).append(") = ? ");
            str = str.replace("  ", " ");
            str = str.trim().toLowerCase();
            paramList.add(str);
        }
    }

    /**
     * kiem tra 1 so rong hay null khong
     *
     * @param n           So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Long n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n > 0L)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 so rong hay null khong
     *
     * @param n           So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterWithZero(Long n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n >= 0L)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 so rong hay null khong
     *
     * @param n           So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Double n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n > 0L)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 so rong hay null khong
     *
     * @param n           So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Boolean n, StringBuilder queryString, List<Object> paramList, String field) {
        if (n != null) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 Integer rong hay null khong
     *
     * @param n           So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filter(Integer n, StringBuilder queryString, List<Object> paramList, String field) {
        if ((n != null) && (n > 0)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(n);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param date        So
     * @param queryString
     * @param field
     * @param paramList
     */
    public static void filter(Date date, StringBuilder queryString, List<Object> paramList, String field) {
        if ((date != null)) {
            queryString.append(" AND ").append(field).append(" = ? ");
            paramList.add(date);
        }
    }

    /**
     * kiem tra 1 xau rong hay null khong
     *
     * @param arrIds
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterSelectInL(String arrIds, StringBuilder queryString, List<Object> paramList, String field) {
        if (!CommonUtils.isNullOrEmpty(arrIds)) {
            queryString.append(" AND ").append(field).append(" IN (-1 ");
            String[] ids = arrIds.split(",");
            for (String strId : ids) {
                queryString.append(", ?");
                paramList.add(Long.parseLong(strId.trim()));
            }
            queryString.append(" ) ");
        }
    }

    /**
     * kiem tra list id
     *
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterSelectInL(List<Long> ids, StringBuilder queryString, List<Object> paramList, String field) {
        if (!CommonUtils.isNullOrEmpty(ids)) {
            queryString.append(" AND ").append(field).append(" IN (-1 ");
            for (Long id : ids) {
                queryString.append(", ?");
                paramList.add(id);
            }
            queryString.append(" ) ");
        }
    }

    /**
     * Kiem tra lon hon hoac bang.
     *
     * @param obj         So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterGe(Object obj, StringBuilder queryString, List<Object> paramList, String field) {
        if (obj != null && !"".equals(obj)) {
            queryString.append(" AND ").append(field).append(" >= ? ");
            paramList.add(obj);
        }
    }

    /**
     * Kiem tra GIAO
     *     So
     * @param queryString
     * @param paramList
     */
    public static void filterBetweenDate(Object obj1, Object obj2, StringBuilder queryString, List<Object> paramList, String field1, String field2) {
        if ((obj1 != null && !"".equals(obj1)) && (obj2 == null || "".equals(obj2))) {
            queryString.append(" AND ").append(field1).append(" >= ? ");
            paramList.add(obj1);
        } else if ((obj1 == null || "".equals(obj1)) && (obj2 != null && !"".equals(obj2))){
            queryString.append(" AND ").append(field2).append(" <= ? ");
            paramList.add(obj2);
        } else if ((obj1 != null && !"".equals(obj1)) && (obj2 != null && !"".equals(obj2))) {
            queryString.append(" AND ( ").append(field2).append(" >= ? ");
            paramList.add(obj1);
            queryString.append(" OR ").append(field2).append(" IS NULL ) ");
            queryString.append(" AND ").append(field1).append(" <= ? ");
            paramList.add(obj2);
        }
    }

    /**
     * Kiem tra GIAO
     *
       So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterFromDateToDate(Object obj1, Object obj2, StringBuilder queryString, List<Object> paramList, String field) {
        if ((obj1 != null && !"".equals(obj1)) && (obj2 == null || "".equals(obj2))) {
            queryString.append(" AND ( ").append(field).append(" >= DATE(?) ");
            queryString.append(" OR ").append(field).append(" IS NULL ) ");
            paramList.add(obj1);
        } else if ((obj1 == null || "".equals(obj1)) && (obj2 != null && !"".equals(obj2))){
            queryString.append(" AND ( ").append(field).append(" <= DATE(?) ");
            queryString.append(" OR ").append(field).append(" IS NULL ) ");
            paramList.add(obj2);
        } else if ((obj1 != null && !"".equals(obj1)) && (obj2 != null && !"".equals(obj2))) {
            queryString.append(" AND ( ").append(field).append(" >= DATE(?) ");
            paramList.add(obj1);
            queryString.append(" AND  ").append(field).append(" <= DATE(?) ) ");
            paramList.add(obj2);
            queryString.append(" OR ").append(field).append(" IS NULL  ");
        }
    }

    /**
     * Kiem tra nho hon hoac bang.
     *
     * @param obj         So
     * @param queryString
     * @param paramList
     * @param field
     */
    public static void filterLe(Object obj, StringBuilder queryString, List<Object> paramList, String field) {
        if (obj != null && !"".equals(obj)) {
            queryString.append(" AND ").append(field).append(" <= ? ");
            paramList.add(obj);
        }
    }

    /**
     * filter for inserting preparedStatement
     *
     * @param value
     * @param index
     * @param preparedStatement
     * @throws Exception
     */
    public static void filter(String value, PreparedStatement preparedStatement, int index) throws Exception {

        if (value != null) {
            preparedStatement.setString(index, value.trim());
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     *
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(Double value, PreparedStatement preparedStatement, int index) throws Exception {

        if (value != null) {
            preparedStatement.setDouble(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     *
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(Long value, PreparedStatement preparedStatement, int index) throws Exception {

        if (value != null) {
            preparedStatement.setLong(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     *
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(Object value, PreparedStatement preparedStatement, int index) throws Exception {
        if (value != null) {
            if (value instanceof Date) {
                Date temp = (Date) value;
                preparedStatement.setObject(index, new java.sql.Timestamp(temp.getTime()));
            } else {
                preparedStatement.setObject(index, value);
            }

        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     *
     * @param value
     * @param preparedStatement
     * @param index
     * @throws Exception
     */
    public static void filter(java.sql.Date value, PreparedStatement preparedStatement, int index) throws Exception {

        if (value != null) {
            preparedStatement.setDate(index, value);
        } else {
            preparedStatement.setNull(index, java.sql.Types.NULL);
        }
    }

    /**
     * kiem tra mot chuoi co chua ky tu Unicode khong
     *
     * @param str
     * @return
     */
    public static boolean containUnicode(String str) {
        String signChars = "ăâđêôơưàảãạáằẳẵặắầẩẫậấèẻẽẹéềểễệếìỉĩịíòỏõọóồổỗộốờởỡợớùủũụúừửữựứỳỷỹỵý";
        if ("".equals(str)) {
            return false;
        } else {
            int count = 0;
            String subStr;
            while (count < str.length()) {
                subStr = str.substring(count, count + 1);
                if (signChars.contains(subStr)) {
                    return true;
                }
                count++;
            }
        }
        return false;
    }

    /**
     * kiem tra mot chuoi co chua ky tu Unicode khong
     *
     * @param str
     * @return
     */
    public static boolean containPhoneNumber(String str) {
        String signChars = "0123456789";
        if ("".equals(str)) {
            return false;
        } else {
            int count = 0;
            String subStr;
            while (count < str.length()) {
                subStr = str.substring(count, count + 1);
                if (!signChars.contains(subStr)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * replaceSpecialKeys
     *
     * @param str String
     * @return String
     */
    public static String replaceSpecialKeys(String str) {
        str = str.replace("  ", " ");
        str = "%" + str.trim().toLowerCase().replace("/", "//").replace("_", "/_").replace("%", "/%") + "%";
        return str;
    }
}
