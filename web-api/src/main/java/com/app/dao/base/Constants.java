/*
 * Copyright (C) 2010 HRPlus. All rights reserved.
 * HRPLUS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.app.dao.base;

/**
 * Phan bo: - Cac trang tra ve global (PAGE_FORWARD) - Cac ma tra ve
 * (MESSAGE_CODE) - Cac hang so hay su dung (COMMON) - Chua cac hang tuong ung
 * voi cac bang (cac truong combobox, trang thai, mot so truong hop dac biet nhu
 * SYS_CAT_TYPE) Cac hang ma chi mot chuc nang su dung thi khong cho vao day.
 *
 * @author HuyenNV
 * @since 1.0
 * @version 1.0
 */
public interface Constants {

    /**
     * Cac trang tra ve chung.
     */
    public interface PAGE_FORWARD {

        //Trang chua link download
        String DOWNLOAD_LINK = "downloadLink";
        //Trang chua ket qua gui mail
        String SEND_MAIL_RESULT = "sendMailResult";
        //Trang xu ly ma tra ve
        String SAVE_RESULT = "saveResult";
        //Trang xu ly submit form iframe
        String SUBMIT_RESULT = "submitResult";
        //Trang thong bao loi het session
        String SESSION_TIME_OUT = "sessionTimeout";
        //Trang thong bao file khong con ton tai tren he thong
        String FILE_NOT_FOUND = "fileNotFound";
        //Trang loi
        String ERROR_PAGE = "error";
        String PERMISSION_INVALID = "permissionInvalid";
        String CSRF_TOKEN_PAGE = "CSRFToken";
        String IMPORT_ERROR_LIST = "importErrorList";
    }

    /**
     * Cac code tra ve.
     */
    public interface MESSAGE_CODE {

        //Thanh cong
        Long SUCCESS = 0L;
        //Loi
        Long ERROR = 1L;
        //Canh bao
        Long WARNING = 2L;
    }

    /**
     * Cac hang so dung chung.
     */
    public interface COMMON {

        //Dinh dang ngay
        String DATE_FORMAT = "MMM dd yyyy";
        String SQLDATE_FORMAT = "yyyy-MM-dd";
        String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
        //Thu muc chua file tam de import
        String IMPORT_CONFIG = "/share/importConfig/";
        String IMPORT_TEMPLATE = "/share/importTemplate/";
        //Thu muc xuat bao cao
        String TEMPLATE_FOLDER = "/share/exportTemplate/";

        String TEMP = "/share/temp/";
    }



}
