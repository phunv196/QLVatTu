/*
 * subject to license terms.
 * Copyright (C) 2018 Viettel Telecom. All rights reserved. VIETTEL PROPRIETARY/CONFIDENTIAL. Use is
 */
package com.app.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.dao.base.CommonUtils;
import com.app.dao.base.converter.DynamicExport;
import com.fasterxml.jackson.annotation.JsonFormat;



/**
 * @author d2tsoftware
 * @since Nov 20, 2018
 * @version 1.0
 */
public class Constants {
    public static final String URL_PRODUCTION = "http://localhost:8081/";

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

        String FOLDER_EXPORT = "web-api/src/main/resources/file/folder-export-excel/";
        String TEMPLATE_EXPORT_EXCELL = "web-api/src/main/resources/file/template-export-excel/";
        String FOLDER_IMPORT = "web-api/src/main/resources/file/folder-file-import/";
        String TEMPLATE_IMPORT_EXCELL = "web-api/src/main/resources/file/template-import-excell/";
        String FOLDER_EXPORT_TEMPLATE = "web-api/src/main/resources/file/folder-export-template/";
        String TEMPLATE_EXPORT_DOCX = "web-api/src/main/resources/file/template-export-docx/";
        String FOLDER_EXPORT_DOCX = "web-api/src/main/resources/file/folder-export-docx/";

        String EXPORT_FOLDER = CommonUtils.getConfig("exportFolder");

    }
    
    /**
     * 
     * @author DATDC key cho xuat bao cao hoac trinh ky
     *
     */
    public static class DATE_TIME {
        public static final String MONTH = "tháng";
        public static final String DAY = "ngày";
        public static final String YEAR = "năm";
    }
}
