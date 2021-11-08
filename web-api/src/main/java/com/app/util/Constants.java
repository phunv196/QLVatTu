/*
 * subject to license terms.
 * Copyright (C) 2018 Viettel Telecom. All rights reserved. VIETTEL PROPRIETARY/CONFIDENTIAL. Use is
 */
package com.app.util;
/**
 * @author phunv
 * @since Nov 20, 2018
 * @version 1.0
 */
public class Constants {

    public static class UserRoleConstants {
        public final static String ROLE_ADMIN     = "ADMIN";
        public final static String ROLE_CUSTOMER  = "CUSTOMER";
        public final static String ROLE_SUPPORT   = "SUPPORT";
    }

    public interface Error {
        String NULL_OR_ENITY = "Trường dữ liệu không được để trống!";
        String DUOLICATE = "Trường dữ liệu bị trùng!";
        String DATE = "Trường ngày tháng phải đúng định dạng (dd-mm-yyyy)!";
        String FOMAT = "Trường dữ liệu phải nhập đúng định dạng!";
    }

    /**
     * Cac hang so dung chung.
     */
    public interface COMMON {
        //Dinh dang ngay
        String DATE_FORMAT = "MMM dd yyyy";
        String DATE_FORMAT_BASIC = "dd-MM-yyyy";
        String SQLDATE_FORMAT = "yyyy-MM-dd";
        String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

        String FOLDER_EXPORT = "D:/file/folder-export-excel/";
        String TEMPLATE_EXPORT_EXCELL = "D:/file/template-export-excel/";
        String FOLDER_IMPORT = "D:/file/folder-file-import/";
        String TEMPLATE_IMPORT_EXCELL = "D:/file/template-import-excell/";
        String FOLDER_EXPORT_TEMPLATE = "D:/file/folder-export-template/";
        String TEMPLATE_EXPORT_DOCX = "D:/file/template-export-docx/";
        String FOLDER_EXPORT_DOCX = "D:/file/folder-export-docx/";
        String EXPORT_FOLDER = "D:/file";
    }

}
