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

    public static class UserRoleConstants {
        public final static String ROLE_ADMIN     = "ADMIN";
        public final static String ROLE_CUSTOMER  = "CUSTOMER";
        public final static String ROLE_SUPPORT   = "SUPPORT";
    }
    /**
     * SCHEMA
     * @author d2tsoftware
     * @since Nov 27, 2018
     * @version 1.0
     */
    public static class SCHEMA {
        public static final String SYSTEM = "vhcm_system";
    }
    /**
     * LIMIT
     * @author TanPTN
     * @since Sep 26, 2019
     * @version cú lừa
     */
    public static class POLITICAL_API {
        public static final String LIMIT =  "5";
    }
    /**
     * RESPONSE_TYPE
     * @author d2tsoftware
     * @since Nov 27, 2018
     * @version 1.0
     */
    public static class RESPONSE_TYPE {
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";
        public static final String WARNING = "WARNING";
        public static final String CONFIRM = "CONFIRM";
        public static final String invalidPermission = "invalidPermission";
    }
    /**
     * RESPONSE_TYPE
     * @author d2tsoftware
     * @since Nov 27, 2018
     * @version 1.0
     */
    public static class RESPONSE_CODE {
        public static final String SUCCESS = "success";
        public static final String DELETE_SUCCESS = "deleteSuccess";
        public static final String ERROR = "error";
        public static final String WARNING = "warning";
        public static final String RECORD_DELETED = "record.deleted";
        public static final String RECORD_INUSED = "record.inUsed";
        public static final String RECORD_APPROVAL = "record.approval";
        public static final String INVALID_MOVE = "invalidMove";
        public static final String ERROR_EMPTY_DATA = "dataEmpty";
        public static final String EDIT_PENDING = "editIsPending";
        public static final String CATEGORY_TYPE_USED = "categoryTypeUsed";
        public static final String NOT_LEAF_PARTY_ORG = "partyOrgnization.notLeafPartyOrg";
        public static final String AMOUNT_NOT_FOUND = "brief.amountNotFound";
        public static final String NOT_PERMISION_DELETE = "permision.delete";
        
        //minhnq them error
        public static final String ERROR_BRIEF_CODE_NOT_EXIST = "notExist.brief.code.file";
        public static final String ERROR_PACKAGE_CODE_NOT_EXIST = "notExist.package.code.file";
        public static final String ERROR_VER_FILE = "version.error";
        public static final String APP_NOTIFICATION_IS_HAVE_KPI = "app.notification.is.have.kpi";
		//duybd
        public static final String BEP_KPI_ORG_IS_NULL = "listOrgForKpi";
        public static final String BEP_LIST_BRIEF_EVALUATION_PROCESS_IS_NULL = "listBriefEvaluationProcess";
        public static final String BEP_CANCEL_BRIEF_EVALUATION = "briefEvaluationCancel";
        public static final String BEP_RESTORE_BRIEF_EVALUATION = "briefEvaluationRestore";
        public static final String BEP_REQUEST_CREATE_FILE_SIGN_FAIL = "bepRequestCreateFileSignFail";
        public static final String BEP_REJECT_EVALUATION_FAIL = "bepRejectEvalutionFail";
        
        
        public static final String BEP_PROCESS_NOT_EXIST = "bepProcessNotExist";
        //Datdc
        public static final String NAME_INUSED = "name.inUsed";
		public static final String CODE_INUSED = "code.inUsed";
        //LongDV;
        public static final String BRIEF_TYPE_NAME_INUSED = "compendiumName.exists";
        public static final String BEP_PROCESS_MANAGER_NAME_IS_NULL = "managerEvaluationIdNull";
        public static final String INVALID_OPERATION = "invalidOperation";
        public static final String BEP_KPI_ORGANIZATION_EXIST = "organizationExitsKpi";
        public static final String PARAMETER_USED = "parameterUsed";
        public static final String ELEMENT_INUSED = "element.inUsed";
        public static final String ELEMENT_DELETED = "element.deleted";
        public static final String ELEMENT_EXISTED = "element.existed";



        public static final String PROJECT_HAS_PACKAGE = "projectHasPackage";
        public static final String TAX_NUMBER_EXISTED = "taxNumber.existed";
        //tunglt
//        public static final String ERROR_RECORD_DELETED = "recordHasBeenDelete";
        //binhnx
        public static final String INPUT_METHOD_INUSED = "inputMethod.inUsed";
        public static final String INPUT_METHOD_INUSED_EDIT = "inputMethod.edit.inUsed";
        public static final String VERSION_MANAGER_ERROR = "version.manager.exist";

        public static final String TEMPLATE_USED_EDIT = "template.edit.inUsed";
        public static final String TEMPLATE_USED_DELETE = "template.delete.inUsed";

        public static final String CODE_INUSED_CRITERIA = "code.inUsed.criteria";
        public static final String CODE_INUSED_CRITERIA_NOT_DELETE = "code.inUsed.criteria.not.deleted";
        public static final String CODE_INUSED_CRITERIA_NOT_EDIT = "code.inUsed.criteria.not.edit";
        // schedule
        public static final String CONFLICT_SCHEDULE = "code.schedule.conflicProcess";
        //binhnx: report checklist
        public static final String REPORT_CHECKLIST_INUSED = "report.checklist.inused";
        public static final String REPORT_CHECKLIST_INUSED_EDIT = "report.checklist.inused.edit";
        public static final String CREATE_DUPLICATE_METER = "create.duplicate.meter";
        
        // hauvd: electric building
        public static final String ELECTRIC_BUILDING_EXIST = "electricBuilding.existed";
        public static final String ELECTRIC_METER_CONFIGURATION_EDIT = "electricMeter.configuration.edit";
        public static final String ELECTRIC_METER_CONFIGURATION_DELETE = "electricMeter.configuration.delete";
        
        // sontd: criteria
        public static final String INUSED_CRITERIA_CHILDREN = "inused.criteria.children";
        
        //hailv: notificationSystem
        public static final String NOTIFICATION_SYSTEM_EDIT_SENDED = "record.edit.sended";
        public static final String NOTIFICATION_SYSTEM_DELETE_SENDED = "record.delete.sended";
        
        //manhntL: versionManagement activated
        public static final String VERSION_MANAGER_ACTIVATED_ERROR = "version.manager.delete.inActivated";
        public static final String VERSION_MANAGER_ACTIVATED_EDIT_ERROR = "version.manager.edit.inActivated";
		
		//hauvd: holiday
        public static final String HOLIDAY_CONFLICT_TIME = "holiday.conflict.time";
		//hailv docbrief
        public static final String ORDER_NUMBER_DUPLICATE = "doc.brief.orderNumber.duplicate";
        public static final String DELETE_BRIEF_IN_DOCUMENT = "delete.brief.in.document";
        
        public static final String PACKAGE_BRIEF_EXIST = "packageBrief.exist";
        public static final String BRIEF_PAGE_TYPE_INVALID = "brief.pageType.invalid";

    }

    /**
     * SYS_CAT_TYPE
     * @author d2tsoftware
     * @since Nov 20, 2018
     * @version 1.0
     */
    public static class SYS_CAT_TYPE {
        public static final Long CURRENCY = 1L;
        public static final Long LANGUAGE = 8L;
        public static final Long MENU = 26L;
        public static final Long TABLE = 27L;
        public static final Long RELIGION = 11L;
    }
    
    /** Khoa danh muc dung chung
     * CATEGORY_TYPE=
     */
    public interface CATEGORY_TYPE {
        String NHOM_CHUC_DANH = "NHOM_CHUC_DANH";
        String LOAI_HINH_TO_CHUC_DANG = "LOAI_HINH_TO_CHUC_DANG";
        String LY_DO_CHAM_DUT_DANG = "LY_DO_CHAM_DUT_DANG";
        String PARTY_ORG_TYPE = "PARTY_ORG_TYPE";
        String OFFICER_TYPE = "OFFICER_TYPE";
        String CONFIDENTIALITY = "CONFIDENTIALITY";
        String TEXT_FORM = "TEXT_FORM";
        String URGENT_LEVEL_TYPE = "URGENT_LEVEL_TYPE";
        String DOCUMENT_TYPE = "DOCUMENT_TYPE";
        String ORG_UNION = "TO_CHUC_CONG_DOAN";
        String ORG_WOMEN = "TO_CHUC_PHU_NU";
        String ORG_YOUTH = "TO_CHUC_THANH_NIEN";
    }

    /**
     * APP_PARAMS_PAR_TYPE=
     */
    public interface APP_PARAMS_PAR_TYPE {
        String PARTY_REPORT_MANAGEMENT_TYPE = "PARTY_REPORT_MANAGEMENT_TYPE";
        String PARTY_STRUCTURE_REPORT_TYPE_WAIT_RETIRE = "PARTY_STRUCTURE_REPORT_WAIT_RETIRE";
        String PARTY_EXPIRED_REPORT = "PARTY_EXPIRED_REPORT";
        String PARTY_EXPIRED_REPORT_EMP_TYPE = "PARTY_EXPIRED_REPORT_EMP_TYPE";
        String NONE_STAFF_AREA_EMP_TYPE_ID = "NONE_STAFF_AREA_EMP_TYPE_ID";
        String ID_BAO_CAO_TU_TUAT = "ID_BAO_CAO_TU_TUAT";
        String ID_GROUP_COMPANY = "GROUP_COMPANY";
        String LEAVE_PROCESS_TYPE = "LEAVE_PROCESS_TYPE";
        String LABOUR_CONTRACT_TYPE_REGULAR = "LABOUR_CONTRACT_TYPE_REGULAR";      
    }

    public static class APP_PARAMS {
    	public static final String GROUP_BRIEF = "GROUP_BRIEF";
    }
    
    /**
     * TABLE_COLUMN
     * @author huynq73
     * @since Dec 5, 2018
     * @version 1.0
     */    
    public static class DATABASE{
        public static final Map<String,List<String>> TABLE_COLUMN = Collections.unmodifiableMap(
                new HashMap<String, List<String>>() {
                    private static final long serialVersionUID = 4115698355756535169L;

                {
                    put("property", new ArrayList<>(Arrays.asList("property_id","code", "name", "start_date", "end_date", "created_by", "menuId", "table_name", "column_name")));
                    put("property_details", new ArrayList<>(Arrays.asList("property_detail_id", "property_id", "nation_id", "is_hide")));
                    put("sys_cat_type", new ArrayList<>(Arrays.asList("sys_cat_type_id", "nation_id", "code", "name")));
                    put("sys_cat", new ArrayList<>(Arrays.asList("sys_cat_type_id", "sys_cat_id", "code", "name")));
                }});
    }

    /**
     * Trang thai position
     * @author d2tsoftware
     * @since Dec 6, 2018
     * @version 1.0
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum PositionStatus {
        INIT(0L, "common.status.init"),
        APPROVED(1L, "common.status.approved"),
        REFUSED(2L, "common.status.refused");

        private Long key;
        private String code;
        /**
         * @param key
         * @param code
         */
        private PositionStatus(Long key, String code) {
            this.key = key;
            this.code = code;
        }
        
        /**
         * @return the key
         */
        public Long getKey() {
            return key;
        }
        
        /**
         * @return the code
         */
        public String getCode() {
            return code;
        }
    }

    public enum ProcessType {
        INTERIOR(1L, "Interior"),
        EXTERIOR(2L, "Exterior"),
        //Qua trinh nha'p
        TYPE_DRAFT(3L, "Draft"),
        //la qua trinh hien tại
        STATUS_CURRENT(1L, "Current"),
        //ko phai qua trinh hien tai
        STATUS_NOT_CURRENT(null, "NotCurrent");
        private Long key;
        private String code;

        private ProcessType(Long key, String code) {
            this.key = key;
            this.code = code;
        }

        public Long getKey() {
            return key;
        }

        public String getCode() {
            return code;
        }
    }

    public static class POSITION_REQ_TYPE {
        public static final int EDUCATION = 1;
        public static final int LANGUAGE = 2;
        public static final int OTHER_REQ = 3;
        public static final int EXPERIENCE = 4;
    }

    public static class POSITION_INTERACTION_TYPE {
        public static final int INTERNAL = 1;
        public static final int EXTERNAL = 2;
    }
    // api
    public interface API_METHOD {

        public static final String POST = "POST";
        public static final String GET = "GET";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
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
        //Thu muc chua file tam de import
        String IMPORT_CONFIG = "/share/importConfig/";
        String IMPORT_TEMPLATE = "/share/importTemplate/";
        //Thu muc xuat bao cao
        String TEMPLATE_FOLDER = "/share/exportTemplate/";
        /**
         * So loi toi da
         */
        Integer MAX_ERROR_NUM = 1000;
        String FOLDER_EXPORT = "web-api/src/main/resources/file/folder-export-excel/";
        String TEMPLATE_EXPORT_FOLDER = "web-api/src/main/resources/file/template-export-excel/";
        String FOLDER_IMPORT = "web-api/src/main/resources/file/folder-file-import/";
        String TEMPLATE_IMPORT_EXCELL = "web-api/src/main/resources/file/template-import-excell/";
        String FOLDER_EXPORT_TEMPLATE = "web-api/src/main/resources/file/folder-export-template/";

        String TEMP = "/share/temp/";
        String FONT_FOLDER = CommonUtils.getConfig("fontFolder");
        String FONT_TIMES_NEW_ROMAN = "Times-New-Roman.ttf";
        String MARKET_COMPANY_ID = "MARKET_COMPANY_ID";
        String EXPORT_FOLDER = CommonUtils.getConfig("exportFolder");
        //Thu muc chua file tam de import
        String IMPORT_FOLDER = "/share/import/";
        String BRIEF_TYPE_FILE_UPLOAD = CommonUtils.getConfig("briefTypeFileUpload");
        String BRIEF_FOLDER = CommonUtils.getConfig("briefFolderUpload");
        String MANAGER_CODES = CommonUtils.getConfig("managerCodes");
        String CRITERIA_PATH = CommonUtils.getConfig("criteriaFolder");
        Long TYPE_CRITERIA_ATTACHMENT_FILE = 30L;
        Long TYPE_CRITERIA_ICON_ATTACHMENT_FILE = 31L;
        String BEP_BRIEF_EVALUATION_REQUEST_FILE_TYPE = CommonUtils.getConfig("bepBriefEvaluationRequestTypeFile");
        String BEP_BRIEF_EVALUATION_REQUEST_FOLDER = CommonUtils.getConfig("bepBriefEvaluationRequestFolder");
        String INCIDENT_IMAGE_FOLDER = CommonUtils.getConfig("incidentImageFolder");
    }
    public static class FILE_TYPE {
        public static final Long PDF = 1L;
        public static final Long DWG = 2L;
        public static final Long XLS = 3L;
        public static final Long PNG = 4L;
        public static final Long XLSX = 5L;
        public static final Long DOCX = 6L;
        public static final Long DOC = 7L;
        public static final Long JPG = 8L;
        public static final Long JPEG = 9L;
        public static final Long ZIP = 10L;
        public static final Long AVI = 11L;
        public static final Long FLV = 12L;
        public static final Long WMV = 13L;
        public static final Long MOV = 14L;
        public static final Long MP4 = 15L;
        public static final Long SVG = 16L;
        public static final Long RAR = 17L;
        public static Long getFileTypeId (String extention) {
            for(Long id : listExtention.keySet()) {
                if(listExtention.get(id).contains(extention)) {
                    return id;
                }
            }
            return 0L;
        }
        public static Map<Long, String> listExtention = new HashMap<Long, String>();
        static {
            listExtention.put(1L, ".pdf");
            listExtention.put(2L, ".dwg");
            listExtention.put(3L, ".xls");
            listExtention.put(4L, ".png");
            listExtention.put(5L, ".xlsx");
            listExtention.put(6L, ".docx");
            listExtention.put(7L, ".doc");
            listExtention.put(8L, ".jpg");
            listExtention.put(9L, ".jpeg");
            listExtention.put(10L, ".zip");
            listExtention.put(17L, ".rar");
        }
        // them file type 
        public static Long TEMPLATE = 22L;
        public static Long CHECKLIST = 23L;
    }
    
    public interface SYNC_TABLE_NAME {
        public static final String NATION   = "nation";
        public static final String SYS_CAT   = "sys_cat";
        public static final String SYS_CAT_TYPE   = "sys_cat_type";
        public static final String PROVINCE  = "province";
        public static final String EMP_TYPE  = "emp_type";
        public static final String POSITION   = "position";
        public static final String ORGANIZATION = "organization";
        public static final String EMPLOYEE = "employee";
        public static final String ATTACHMENT_FILE = "attachment_file";
        public static final String EMP_TYPE_PROCESS   = "emp_type_process";
        public static final String WORK_PROCESS   = "work_process";
        public static final String OTHER_PARTY   = "other_party";
        public static final String LONG_LEAVE   = "long_leave";
        public static final String EMPLOYEE_T63_INFORMATION   = "employee_t63_information";
        // Datdc table phong ban
        public static final String BUILDING = "building";
    }
    
    public interface ORG_ACTION_LOCK{
        /** Chuc nang khoa
         *  -- 1: Qua trinh dien doi tuong,
        */
        Long EMP_TYPE_PROCESS = 1L;

        //Thong tin khoa
        Long LOCK = 1L;
        Long UNLOCK = 0L;

    }
    
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum LabourContractType {
        HDCTV("HDCTV", "app.contractProcess.labourCTV"),
        HDDV("HDDV", "app.contractProcess.labourService"),
        HDTV("HDTV", "app.contractProcess.labourTV");

        private String key;
        private String code;

        private LabourContractType(String key, String code) {
            this.key = key;
            this.code = code;
        }

        public String getKey() {
            return key;
        }

        public String getCode() {
            return code;
        }
    }
    
    public interface ORG_COMPLEMENT_LOCK {
        //Thong tin Chuc nang khoa
        Long WORK_PROCESS = 1L;
        Long EMP_TYPE_PROCESS = 2L;
        Long LONG_LEAVE = 3L;
        Long SALARY_PROCESS = 4l;
        Long EMPLOYEE_KI_PROCESS = 5l;
        //Thong tin khoa
        Long LOCK = 1L;
        Long UNLOCK = 2L;
        Long STAFF_AREA_WORK_PROCESS = 6L;
        Long STAFF_AREA_CONTRACT = 7L;
        Long ALLWANCE_PROCESS = 8L;
        //Thong tin cap don vi
    }

    public interface EMPLOYEE_SOLDIER {
        Long SOLDIER = 1L;//Thuong binh
        Long SICK_SOLDIER = 2L;//Benh binh
        Long DIOXIN = 3L;//Nhiem chat doc dioxin
    }

     /**
      * Service path of employee assessment API
      */
    public enum EmployeeAssessmentServicePath {
        FIND_ASSESSMENT_BY_EMP("/service/get-assessment-by-emp");

        private String path;

        private EmployeeAssessmentServicePath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
    
    public static class Type {

        public static final Long PDF = 1L;
        public static final Long DWG = 2L;
        public static final Long EXCEL_OLD = 3L;
        public static final Long IMG = 4L;
        public static final Long EXCEL_NEW = 5L;
        public static final Long DOCX = 6L;
    }
    
    public static class TYPE_FROM_TABLE {
        public static final Long BRIEF = 1L;
        public static final Long BEP_BRIEF_EVALUATION_REQUEST = 2L; // ho so de nghi tham dinh
        public static final Long BEP_BRIEF_EVALUATION_REQUEST_HISTORY = 3L; // lich su ho so de nghi tham dinh
        public static final Long BEP_BRIEF_EVALUATION_PROCESS_HISTORY = 4L; // lich su ho so dang tham dinh
        public static final Long BEP_BRIEF_EVALUATION_REQUEST_DETAIL = 5L; // lich su ho so dang tham dinh
        public static final Long INCIDENT_IMAGE_RECORD = 11L;
        public static final Long INCIDENT_IMAGE_RECORD_COMPLATE = 12L;
    }
	
	public static class BorrowStatus {
        public static final Long TYPE_EDIT = 0L;
        public static final Long TYPE_SEND_BACK = 1L;
        public static final Long TYPE_ANALYSIS = 2L;
        public static final Long TYPE_OTHER = 3L;

        public static final Long FORM_ORIGINAL = 0L;
        public static final Long FORM_DOWLOAD = 1L;
        public static final Long FORM_VIEW = 2L;

        public static final Long STATUS_NEW = 0L;
        public static final Long STATUS_SIGNING = 1L;
        public static final Long STATUS_ACCEPTED = 2L;
        public static final Long STATUS_SENT = 3L;
        public static final Long STATUS_RECEIVED = 4L;
        public static final Long STATUS_SENDING_BACK = 5L;
        public static final Long STATUS_REJECT_BORROW = 6L;
        public static final Long STATUS_REJECT_GET = 7L;
        public static final Long STATUS_DONE = 8L;

        public static final Long MOVE_ACCEPT = 0L;
    }
    public static class BORROW_DETAIL_STATUS {
        public static final Long STATUS_ACCEPT = 1L;
        public static final Long STATUS_REJECT = 0L;// minhnq sua thanh 0 theo giai phap
    }
    public static class BriefVersion{
        public static final Long BRIEF_ORIGIN_VERSION = 0L;
        public static final Long BRIEF_EDIT_VERSION = 1L;
    }
    public static class HANHDOVER_WINDOW{
        public static final Long TYPE_HANDOVER_LIST = 1L; // Danh sach phieu muon
        public static final Long TYPE_HANDOVER_LIST_IS_PENDING = 2L; // Danh sach phieu muon cho duyet
    }
    public static class CALLSLIP_WINDOW{
        public static final Long TYPE_BORROW_LIST = 1L; // Danh sach phieu ban giao
        public static final Long TYPE_BORROW_LIST_IS_PENDING = 2L; // Danh sach phieu ban giao cho duyet
    }

    public static class BRIEF_WINDOW{
        public static final Long BRIEF_LIST = 1L; // Danh sach ho so
        public static final Long BRIEF_BORROWED = 2L; // Danh sach ho so muon
        public static final Long BRIEF_EDIT_PENDING = 3L; // Danh sach ho so yeu cau sua
    }
    public static class SEARCH_FOR_HANDOVERballot {
        public static final Long HANDOVER_ISSUED_ORIGIN = 1L; // Ho so ban giao ban cung
        public static final Long HANDOVER_ISSUED_SOFT = 2L; // Ho so ban giao ban mem
    }
    public static class HandoverType{
        public static final Long HANDOVER_ISSUED_ORIGIN = 0L;
        public static final Long HANDOVER_ISSUED_SOFT = 1L;

        public static final Long HANDOVER_STATUS_NEW = 0L;
        public static final Long HANDOVER_STATUS_ACCEPT = 1L;
        public static final Long HANDOVER_STATUS_REJECT = 2L;

        public static final String VI_HANDOVER_STATUS_NEW = "Chưa bàn giao";
        public static final String VI_HANDOVER_STATUS_ACCEPT = "Đã bàn giao";
        public static final String VI_HANDOVER_STATUS_REJECT = "Từ chối";

        public static final String EN_HANDOVER_STATUS_NEW = "New";
        public static final String EN_HANDOVER_STATUS_ACCEPT = "Issued";
        public static final String EN_HANDOVER_STATUS_REJECT = "Reject";

        public static final Long HANDOVER_DETAIL_STATUS_PENDING = 0L;
        public static final Long HANDOVER_DETAIL_STATUS_RECEIVED = 1L;

        public static final Long ACTION_ACCEPT = 1L;
        public static final Long ACTION_REJECT = 2L;
    }

    public static class STORAGE{
        public static final Long STORAGE_ROOT_ID_1 = 1L;
        public static final Long STORAGE_ROOT_ID_2 = 2L;
    }

    public static class Callslip{
        public static final Long TYPE_EDIT = 0L;
        public static final Long TYPE_SEND_BACK = 1L;
        public static final Long TYPE_ANALYSIS = 2L;
        public static final Long TYPE_OTHER = 3L;

        public static final Long FORM_ORIGINAL = 0L;
        public static final Long FORM_DOWLOAD = 1L;
        public static final Long FORM_VIEW = 2L;

        public static final Long STATUS_NEW = 0L;
        public static final Long STATUS_SIGNING = 1L;
        public static final Long STATUS_ACCEPTED = 2L;
        public static final Long STATUS_SENT = 3L;
        public static final Long STATUS_RECEIVED = 4L;
        public static final Long STATUS_SENDING_BACK = 5L;
        public static final Long STATUS_REJECT_BORROW = 6L;
        public static final Long STATUS_REJECT_GET = 7L;
        public static final Long STATUS_DONE = 8L;
        public static final Long STATUS_SIGNING_REJECT = 9L;

        public static final Long MOVE_RECEIVED = 1L;
        public static final Long MOVE_SIGNING = 2L;
        public static final Long MOVE_SENT = 3L;
        public static final Long MOVE_REJECT = 4L;
        public static final Long MOVE_SENDING_BACK = 5L;
        public static final Long MOVE_RECEIVED_BRIEF = 6L;

        public static final String VI_STATUS_NEW = "Mới";
        public static final String VI_STATUS_SIGNING = "Đã trình ký";
        public static final String VI_STATUS_ACCEPTED = "Đã ký duyệt";
        public static final String VI_STATUS_SENT = "Đã bàn giao";
        public static final String VI_STATUS_RECEIVED = "Đã nhận";
        public static final String VI_STATUS_SENDING_BACK = "Đang hoàn trả";
        public static final String VI_STATUS_REJECT_BORROW = "Từ chối mượn";
        public static final String VI_STATUS_REJECT_GET = "Từ chối hoàn trả";
        public static final String VI_STATUS_DONE = "Đã trả";
        public static final String VI_STATUS_SIGNING_REJECT = "Từ chối duyệt";

        public static final String VI_TYPE_EDIT = "Sửa hồ sơ";
        public static final String VI_TYPE_SEND_BACK = "Bàn giao";
        public static final String VI_TYPE_ANALYSIS = "Giải quyết công việc chuyên môn";
        public static final String VI_TYPE_OTHER = "Khác";

        public static final String VI_FORM_ORIGINAL = "Mượn bản cứng";
        public static final String VI_FORM_DOWLOAD = "Download hồ sơ bản mềm";
        public static final String VI_FORM_VIEW = "Xem hồ sơ bản mềm";
        public static final String VI_STORAGE_ROOT_ID_1 = "Kho 1";
        public static final String VI_STORAGE_ROOT_ID_2 = "Kho 2";

    }

    public static class isActive{
        public static final Long AVAILABLE = 1L;
        public static final Long DELETE = 0L;
    }



    public static class BriefStatus{
        public static final String VI_STATUS_NEW = "Mới";
        public static final String VI_STATUS_ISSUED_SORF = "Bàn giao bản mềm";
        public static final String VI_STATUS_ISSUED_ORIGIN = "Bàn giao bản cứng";
        public static final String EN_STATUS_NEW = "New";
        public static final String EN_STATUS_ISSUED_SOFT = "Resolved scan version";
        public static final String EN_STATUS_ISSUED_ORIGIN = "Resolved origin version";

        public static final Long STATUS_NEW = 0L; //Van ban o trang thai moi
        public static final Long STATUS_ISSUED_SOFT = 1L; //Van ban o trang thai ban giao ban mem
        public static final Long STATUS_ISSUED_ORIGIN = 2L; //Van ban o trang thai ban giao ban cung

        public static final Long EDIT_ACCEPT = 0L; // Yeu cau chinh sua duoc chap nhan
        public static final Long EDIT_REJECT = 1L; // Yeu cau chinh sua bi tu choi

        public static final Long BRIEF_ORIGIN_VERSION = 0L; // Van ban goc
        public static final Long BRIEF_DRAF_VERSION = 1L; // Van ban nhap

        public static final Long BRIEF_AVAILABLE = 1L; // Van ban chua bi xoa
        public static final Long BRIEF_DELETE = 0L; // Van ban da bi xoa

        public static final Long STATUS_POSSIBILITY = 1L; // Van ban con co the muon
        public static final Long STATUS_IMPOSSIBILITY = 0L; // Van ban da het so luong de cho muon

        public static final Integer HIDE_DELETE = 1; // an nut xoa
        public static final Integer SHOW_DELETE = 0; // VB moi + co file: check nguoi upload/ TP/ CBLT/ BGD thi dc xoa
    }

    public static class BRIEF_ACTION_LOG{
    	public static final String ACTION_FIELD_BRIEF_ROOT_NUMBER = "SLVB gốc";
    	public static final String ACTION_FIELD_BRIEF_MAIN_NUMBER = "SLVB chính";
    	public static final String ACTION_FIELD_BRIEF_OTHER_NUMBER = "SLVB khác";
    	public static final String ACTION_FIELD_BRIEF_PAGE_NUMBER = "Số trang";
    	public static final String ACTION_FIELD_BRIEF_PAGE_TYPE = "Loại trang";
    	public static final String PAGE_TYPE_SHEET = "Tờ";
    	public static final String PAGE_TYPE_BOOK = "Quyển";
        public static final String ACTION_FIELD_BRIEF_NAME = "Tên văn bản";
        public static final String ACTION_FIELD_PROJECT_NAME = "Dự án";
        public static final String ACTION_FIELD_PACKAGE_NAME = "Gói thầu ";
        public static final String ACTION_FIELD_BRIEF_TYPE = "Hình thức văn bản";
        public static final String ACTION_FIELD_ISSUING_UNIT = "Đơn vị ban hành";
        public static final String ACTION_FIELD_VALUE = "Giá tiền";
        public static final String ACTION_FIELD_BRIEF_NUMBER = "Số văn bản";
        public static final String ACTION_FIELD_SIGNER = "Người ký";
        public static final String ACTION_FIELD_RELEASE_DATE = "Ngày ban hành";
        public static final String ACTION_FIELD_NOTE = "Ghi chú";
        public static final String ACTION_FIELD_DELETE = "DELETE";
        public static final String ACTION_FIELD_PACKAGE_PHASE = "Hồ sơ giai đoạn";

        public static final Long ACTION_TYPE_CREATE = 0L; // Ghi log khi tao moi van ban
        public static final Long ACTION_TYPE_EDIT = 1L; // Ghi log khi chinh sua van ban
        public static final Long ACTION_TYPE_DELETE = 2L; // Ghi log khi xoa van ban
    }
    public static class PACKAGE_STATUS{
        public static final Long PACKAGE_STATUS_DEFAULT = 0L;
        public static final Long PACKAGE_STATUS_APPROVAL = 1L;

        public static final String VI_STATUS_NEW = "Mới";
        public static final String VI_STATUS_APPROVAL = "Đã phê duyệt";

        public static final String EN_STATUS_NEW = "New";
        public static final String EN_STATUS_APPROVAL = "Approval";
    }

    /**
     * file so sanh voi nhau theo ver
     * @author DATDC
     *
     */
    public static class COMPARE_VER {
        public static final Long LESS = 1L;
        public static final Long EQUAL = 2L;
        public static final Long GREATER = 2L;
    }
    
    public static class VERSION_FILE {
        public static final Long IS_NEW = 1L;
    }

    public static class GENERATE_CODE {
        public static class MODULE {
            public static final Long QLHS = 1L; // quan ly ho so
            public static final Long QTTD = 2L; // quy trinh tham dinh (BEP)
            public static final Long CHECK_LIST_CRITERIA = 3L; // sinh mã tiêu chí
        }

        public static class MENU {
            public static final Long BEP_BRIEF_EVALUATION_REQUEST = 223L; // ho so de nghi tham dinh 
            public static final Long QLSH_LIST_PACKAGE = 628L; // Danh sach goi thau
            public static final Long QLSH_LIST_HANDOVERBALLOT = 6213L; // Danh sach phieu ban giao
            public static final Long QLSH_LIST_CALLSLIP = 6215L; // Danh sach phieu ban giao
            public static final Long QLSH_LIST_PROJECT = 624L; // Danh sach phieu ban giao
            public static final Long QLSH_LIST_BRIEF = 629L; // Danh sach van ban
            public static final Long CHECK_LIST_CRITERIA = 294L; // Danh sach van ban
        }

        public static class CODE_BEGIN {
            public static final String CODE_ORIGINAL = "0002"; // bat dau hang thang bang 0002
            public static final String CODE_ORIGINAL_BRIEF = "000002"; // bat dau hang thang bang 000002
        }
        
        public static final Long ABBREVIATION = 209L; // check voi truong hop ko co ten viet tat org
    }
    
    public static class BEP_BRIEF_EVALUATION {
        public static class STATUS_REQUEST {
            public static final Long CREATE_NEW = 0L; // tạo mới
            public static final Long CANCEL = 1L; // hủy
            public static final Long REVIEW = 2L; // dang xet duyet
            public static final Long REJECT_SIGN = 3L; // tu choi ky
            public static final Long PROCESSING = 4L; // dang xu ly
            public static final Long EVALUATION = 5L; // da tham dinh
            public static final Long DECLINED_EVALUATION = 6L; // tu choi tham dinh
            public static final Long RE_EVALUATION = 7L; // trinh tham dinh lai
        }
        public static class STATUS_PROCESS {
            public static final Long CREATE = 0L; // tao moi
            public static final Long CANCEL = 1L; // huy bo
            public static final Long REVIEW = 2L; // chờ duyệt voffice
            public static final Long REJECT_SIGN = 3L; // từ chối ký voffice
            public static final Long WAIT_ASSIGN = 4L; // chờ giao việc
            public static final Long PROCESSING_TESTING = 5L; // đang kiểm tra
            public static final Long DECLINED_EVALUATION = 6L ; // từ chối nhận
            public static final Long INVALID = 7L; // chưa hợp lệ
            public static final Long ADDITIONAL = 8L; // đang bổ sung
            public static final Long RE_TESTING = 9L; // kiểm tra lại
            public static final Long EVALUATING = 10L; // đang thẩm định
            public static final Long EVALUATED = 11L; // đã thẩm định
            public static final Long REJECT_EVALUATION = 12L; // kết quả thẩm định fail
            public static final Long PENDING = 13L; // chờ tiếp nhận (trạng thái ảo) chỉ dùng để hiển thị lịch sử thẩm định
            public static final Long UPDATE_ACCEPTOR = 14L; // cập nhật (trạng thái ảo) chỉ dùng để hiển thị lịch sử thẩm định
            public static final Long LACK_BRIEF_ATTACH = 15L; // cập nhật (trạng thái ảo) thiếu hồ sơ đính kèm
            public static final Long ENOUGH_BRIEF_ATTACH = 16L; // cập nhật (trạng thái ảo) đủ hồ sơ đính kèm
            public static final Long CHANGE_KPI = 17L; // cập nhật (trạng thái ảo) thay đổi thời gian hoàn thành
        }
        public static class PROCESS_WAIT_RECEIVE {
            public static final Long NON_PENDING = 0L; // 
            public static final Long PENDING = 1L; // chờ tiếp nhận
        }
        public static class ACTION_PROCESS {
            public static final Long SIGNED = 4L; // da ky vofffice
            public static final Long ASSIGNED = 5L; // da giao viec
            public static final Long REJECT_RECEIVED = 6L; // da tu choi tiep nhan
            public static final Long SUGGESTED = 7L; // da yeu cau bo sung ho so
            public static final Long ASSIGN_ADDITIONAL = 8L; // da giao viec bo sung cho nhan vien
            public static final Long COMPLETE_ADDITIONAL = 9L; // da hoan thanh bo sung bo so
            public static final Long RECEIVED = 10L; // da tiep nhan
            public static final Long EVALUATED_OK = 11L; // da tham dinh ok
            public static final Long EVALUATED_NON = 12L; // da tham dinh non ok
        }
        public static class TYPE_REQUEST_HISTORY {
            public static final Long ASSIGN = 1L; // y kien chi dao
            public static final Long IMPLEMENT = 2L; // nhan vien phan hoi hoan thanh bo sung ho so
            public static final Long CANCEL = 3L;
        }
        public static class TIME_FORMAT {
            public static final Long KPI_TYPE = 0L; // tao moi
            public static final Long FIXED_DATE_TYPE = 1L; // huy bo
            public static final Long DESIRED_DATE_TYPE = 2L; // chờ duyệt voffice
        }
        public static final class FLAG_ACTIVE {
            public static final Long IS_ACTIVE = 1L; // hoat dong
            public static final Long IS__NONE_ACTIVE = 0L; // bi xóa
        }
        public static class FLAG_IS_LACK_BRIEF_ATTACH {
            public static final Long LACK = 1L; // thiếu
            public static final Long ENOUGH = 0L; // đủ
        }
    }
    
    public static class SIGN_VOFFICE {
        public static final Long FILE_SIGN_MAIN = 1L;
        public static final Long FILE_SIGN_ATTACTMENT = 2L;
        
        public static enum SIGN_STATUS {

            NOT_YET(0L, "Chưa trình ký"),
            SIGNING(1L, "Đang trình ký"),
            DECLINE(2L, "Từ chối phê duyệt"),
            APPROVED(3L, "Đã phê duyệt");

            private Long value;
            private String label;
            
            private SIGN_STATUS(Long value, String label) {
                this.value = value;
                this.label = label;
            }

            public Long getValue() {
                return value;
            }
            public String getLabel() {
                return label;
            }
        }
        
        public static class SIGN_TYPE {
            // Tham dinh
            public static final String BEP_BRIEF_EVALUATION_TYPE = "bep-brief-evaluation";
            // Loai trinh ky
            public static final String CALLSLIP_TYPE = "callslip-type";
        }
        
        public static final class TEMPLATE {
            public static final class BEP_BRIEF_EVALUATION_REQUEST {
                public static final String BEP_BRIEF_EVALUATION_REQUEST_FOLDER = "bepBriefEvaluation/phieu_yeu_cau_kiem_ban_giao.docx";
                public static final String BEP_BRIEF_EVALUATION_REQUEST_TEMPLATE_DOCX = "phieu_yeu_cau_kiem_ban_giao.docx";
                public static final String BEP_BRIEF_EVALUATION_REQUEST_TEMPLATE_PDF = "phieu_yeu_cau_kiem_ban_giao.pdf";
            }
            public static final class CALLSLIP {
                public static final String CALLSLIP_SIGN_REQUEST = "callslip/callslip.docx";
                public static final String CALLSLIP_FILE_NAME = "callslip.docx";
                public static final String CALLSLIP_FILE_NAME_PDF = "phieu_su_dung_tai_lieu.pdf";
            }
        }
    }
    
    public static class WAREHOUSE {
        //xuat bao cao
        public static final Long EXPORT = 1L;
        // Nut goc 2 kho
        public static final Long ROOT_WAREHOUSE= 3L;
        public static final Long ROOT_WAREHOUSE_1 = 1L;
        public static final Long ROOT_WAREHOUSE_2 = 2L;
        // Trang thai hop
        public static final Long NEW = 0L;
        public static final Long FULL = 1L;
        public static final Long ARCHIVE = 2L;
        // Hop
        public static final Long BOX = 5L;
        public static final Long FLOOR = 4L;
        // Ti le % tren kho
        public static final Long FULL_PERCENT = 100L;
        // constant key label show cay kho
        public static final String FULL_LABEL = "warehousefull";
        public static final String ACTIVE_LABEL = "warehouseactive";
        public static final String NEW_LABEL = "warehousenew";
    }
    
    public static class BA_CRITERIA {
        //xuat bao cao
        public static final Long EXPORT = 1L;
        // Nut goc 2 kho
        public static final Long BA_CRITERIA_1 = 1L;
        public static final Long BA_CRITERIA_9 = 9L;
    }

    public static class DASHBOARD{
        public static final String HANDOVER_IS_PENDING = "amountBriefHandoverApproval";
        public static final String CALLSLIP_IS_PENDING = "amountBriefBorrowApproval";
        public static final String BRIEF_NOT_HAVE_ORIGIN = "amountBriefNotHaveHard";
        public static final String CALLSLIP_IN_EXPIRATIONING = "amountBriefBorrowAboutExpire";
        public static final String BRIEF_WAITING_FOR_EDIT = "amountBriefReqEdit";
        public static final String BRIEF_IS_BORROWING = "amountBriefBorrowing";

        public static final String CALLSLIP_IN_EXPIRATIONING_OWNER = "amountBriefBorrowAboutExpireOwner";
        public static final String CALLSLIP_IN_RECEIVE_PENDING = "amountCallslipInReceivePending";
        public static final String BRIEF_IS_BORROWING_OWNER = "amountBriefBorrowingOwner";

        public static final String BRIEF_SOFT_HANDOVER_DAILY = "briefSoftHandoverDaily";
        public static final String BRIEF_ORIGIN_HANDOVER_DAILY = "briefOriginHandoverDaily";
        public static final String BRIEF_BORROW_HANDOVER_DAILY = "briefBorrowHandoverDaily";

        public static final String WAREHOUSE_ID_1 = "warehouse1";
        public static final String WAREHOUSE_ID_2 = "warehouse2";

    }
    
    /**
     * Danh sach cac mau message
     */
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public static enum Branch {
        BRIEF_REQUEST(1L, "Hồ sơ - Yêu cầu sửa"),
        BRIEF_APPOROVE(2L, "Hồ sơ - Đồng ý sửa"),
        BRIEF_REJECT(3L, "Hồ sơ - Từ chối sửa"),
        HANDOVER_BALLOT_NOT_HANDED(4L, "Phiếu bàn giao - Chưa bàn giao"),
        HANDOVER_BALLOT_HANDED(5L, "Phiếu bàn giao - Đã bàn giao"),
        HANDOVER_BALLOT_REJECT(6L, "Phiếu bàn giao - Từ chối"),
        CALLSLIP_HANDED(7L, "Phiếu mượn - Đã bàn giao"),
        CALLSLIP_RECEIVED(8L, "Phiếu mượn - Đã nhận"),
        CALLSLIP_REFUNDED(9L, "Phiếu mượn - Đang hoàn trả"),
        CALLSLIP_REFUSE_TO_BORROW(10L, "Phiếu mượn - Từ chối mượn"),
        CALLSLIP_REFUSE_TO_REFUND(11L, "Phiếu mượn - Từ chối hoàn trả"),
        CALLSLIP_PAID(12L, "Phiếu mượn - Đã trả"),
        CALLSLIP_COMING_SOON(13L, "Phiếu mượn - Cảnh báo sắp tới hạn trả"),
        CALLSLIP_OUT_OF_DATE(14L, "Phiếu mượn - Cảnh báo quá hạn trả"),
        EVALUATION_REQ_PRO_PROCESSING_WAIT_ASSIGN_EMP(15L, "Đang xử lý - Chờ giao việc"),
        EVALUATION_REQ_PRO_PROCESSING_WAIT_ASSIGN_MNG(16L, "Đang xử lý - Chờ giao việc"),
        EVALUATION_REQ_PRO_REJECT_DECLINE(17L, "Từ chối thẩm định - Từ chối nhận"),
        EVALUATION_REQ_PRO_PROCESSING_TESTING(18L, "Đang xử lý - Đang kiểm tra"),
        EVALUATION_REQ_PRO_PROCESSING_INVALID(19L, "Đang xử lý - Chưa hợp lệ"),
        EVALUATION_REQ_PRO_PROCESSING_ADDING(20L, "Đang xử lý - Đang bổ sung"),
        EVALUATION_REQ_PRO_PROCESSING_RETESTING_ACCEPTOR(21L, "Đang xử lý - Đang kiểm tra lại"),
        EVALUATION_REQ_PRO_PROCESSING_RETESTING_EMP(22L, "Đang xử lý - Đang kiểm tra lại"),
        EVALUATION_REQ_PRO_EVALUATED_EVALUATED(23L, "Đã thẩm định - Đã thẩm định"),
        EVALUATION_REQ_PRO_REJECT_REJECT(24L, "Từ chối duyệt - Từ chối duyệt"),
        EVALUATION_REQ_PRO_PROCESSING_EVALUATING(25L, "Đang xử lý - Đang thẩm định"),
        EVALUATION_REQ_PRO_PROCESSING_TESTING_REQUEST(26L, "Đang xử lý - Đang kiểm tra"),
        EVALUATION_REQ_PRO_PROCESSING_INVALID_REQUEST(27L, "Đang xử lý - Chưa hợp lệ"),
        EVALUATION_REQ_PRO_PROCESSING_UPDATE_ACCEPTOR(28L, "Thay đổi người thụ lý - Đang xử lý - Đang kiểm tra"),
        EVALUATION_REQ_PRO_RETESTING_UPDATE_ACCEPTOR(29L, "Thay đổi người thụ lý - Đang xử lý - Đang kiểm tra"),
        EVALUATION_REQ_PRO_RETESTING_CANCEL(30L, "Hủy thẩm định - Đang xử lý - Chưa hợp lệ"),
        EVALUATION_REQ_PRO_INVALID_CANCEL(31L, "Hủy thẩm định - Đang xử lý - Chưa hợp lệ"),
        EVALUATION_REQ_LACK_ATTACH_BRIEF(32L, "Thiếu hồ sơ"),
		EVALUATION_REQ_UPDATE_LACK_BRIEF(33L, "Bổ sung hồ sơ thiếu"),
		CHANGE_ACCEPTOR(34L, "Đổi người thụ lý - gửi người thụ lý cũ"),
        SEND_SUPPORTER(35L, "Gửi người hỗ trợ"),
    	CHANGE_KPI(36L, "Thay đổi thời gian hoàn thành thẩm định");

        

        private Long value;
        private String label;

        private Branch(Long value, String label) {
            this.value = value;
            this.label = label;
        }

        public static Branch findByValue(Long value) {
            for (Branch branch : Branch.values()) {
                if (branch.getValue().equals(value)) {
                    return branch;
                }
            }
            return null;
        }

        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
    
    /**
     * 
     * @author DATDC
     * Loai thong bao
     *
     */
    public static enum TypeNotify {
        SMS(1L, "SMS"), 
        EMAIL(2L, "EMAIL"),
        NOTIFY(3L, "NOTIFY");

        private Long value;
        private String label;

        private TypeNotify(Long value, String label) {
            this.label = label;
            this.value = value;
        }

        public static TypeNotify findByValue(Long value) {
            for (TypeNotify type : TypeNotify.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
    
    public static class TYPE_CHECK_NOTIFY {
        public static final Long TYPE_CODE_USER = 1L;
        public static final Long TYPE_CODE_USER_STATUS = 2L;
        public static final Long TYPE_CODE_DATE_LINK = 3L;
    }
    
    /**
     * Loai gui thong bao: SMS - gửi tin nhắn, MAIL - gửi email, FCM - Gửi ứng dụng
     */
    public static enum ChannelRequestType {
        SMS("sms"),
        MAIL("mail"),
        FCM("fcm");

        private String type;

        private ChannelRequestType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
    
    public static class TYPE_NOTIFY {
        public static final int SMS = 1;
        public static final int MAIL = 2;
        public static final int NOTIFY = 3;
    }
    
    public static class NOTIFY {
        public static final String ALIAS = "VDMS";
        // public static final String ALIAS = "NOTIFY";
        public static final String BREAK_LINE_HTML = "<br/>";
        public static final String BREAK_LINE_WINDOWS = "\r\n";
        public static final String BREAK_LINE_LINUX = "\n";
        // Cac tham so dung tra ve cho front end start
        public static final String TYPE = "type";
        public static final String TAB = "tab";
        // Tab chinh
        public static final String TAB_MAIN = "1";
        // Tab muon/ban giao
        public static final String TAB_BORROW = "2";
        // Tab yeu cau
        public static final String TAB_REQUEST = "3";
        public static final String FORM_BRIEF = "brief";
        public static final String FORM_CALLSLIP = "callslip";
        public static final String FORM_HANDOVERBALLOT = "hANDOVERBALLOT";
        public static final String ID = "id";
        // Cac tham so dung tra ve cho front end end
        public static class CHANEL {
            public static final String BEP_BRIEF_EVALUATION_REQUEST = "bepBriefEvaluationRequest";
            public static final String BEP_BRIEF_EVALUATION_PROCESS = "bepBriefEvaluationProcess";
            public static final String BRIEF_MANAGER = "briefManager";
            public static final String CALLSLIP = "callslip";
            public static final String HANDOVER_BALLOT = "handoverBallot";
        }
    }
    
    /**
     * Constants 3 tab ho so
     * @author DATDC
     *
     */
    public static class TAB_BRIEF {
        public static final Long TAB_BRIEF_LIST = 1L;
        public static final Long TAB_BRIEF_BORROW_LIST = 2L;
        public static final Long TAB_BRIEF_REQUEST_EDIT_LIST = 3L;
    }
    
    /**
     * Constants 2 tab Phieu ban giao
     * @author DATDC
     *
     */
    public static class TAB_HANDOVERBALLOT {
        public static final Long TAB_HANDOVERBALLOT_LIST = 1L;
        public static final Long TAB_HANDOVERBALLOT_REQUEST_LIST = 2L;
    }
    
    /**
     * Constants 2 tab Phieu muon
     * @author DATDC
     *
     */
    public static class TAB_CALLSLIP {
        public static final Long TAB_CALLSLIP_LIST = 1L;
        public static final Long TAB_CALLSLIP_REQUEST_LIST = 2L;
        // Ten file trich yeu noi dung van ban khi trinh voffice
        public static final String NAME_FILE = "Trình ký phiếu sự dụng tài liệu";
    }
    
    // Ma vai tro
    public static class POSITON_CODE {
        public static final String WARHOUSE_POSITON = "1036";
        public static final String MANAGER_POSITON = "949";
    }

    /**
     * Constants from tab
     * @author MINHNQ
     *
     */
    public static class FROM_TAB {
        public static final Long TAB_BRIEF = 1L;
        public static final Long TAB_HANDOVER_BALLOT = 2L;
        public static final Long TAB_CALLSLIP = 3L;
    }
    
    /**
     * LINK_NOTIFY
     * @author d2tsoftware
     * @since Nov 27, 2018
     * @version 1.0
     */
    public static class LINK_NOTIFY {
        public static final String brief = URL_PRODUCTION + "brief-manager/brief-manager-child/list-brief";
        public static final String handover_ballot = URL_PRODUCTION + "brief-manager/brief-manager-child/handover-ballot-detail";
        public static final String callslip = URL_PRODUCTION + "brief-manager/brief-manager-child/callslip-detail";
        public static final String process = URL_PRODUCTION + "bep-evaluation-process/bep-brief-processing/bep-brief-process-detail/";
        public static final String request = URL_PRODUCTION + "bep-evaluation-process/bep-brief-request/bep-brief-request-detail/";
    }
    
    /**
     * 
     * @author DATDC Loai muc dich muon
     *
     */
    public static enum TypeBorrow {
        EDIT(0L, "Sửa đổi, bổ sung văn bản"), HANDLE(1L, "Bàn giao cho các bên"),
        SOLVE(2L, "Giải quyết công việc chuyên môn"), OTHER(3L, "Mục đích khác");

        private Long value;
        private String label;

        private TypeBorrow(Long value, String label) {
            this.label = label;
            this.value = value;
        }

        public static TypeBorrow findByValue(Long value) {
            for (TypeBorrow type : TypeBorrow.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }

    
    /**
     * 
     * @author DATDC Loai hinh thuc muon
     *
     */
    public static enum FormBorrow {
        HARD(0L, "Mượn bản cứng"), DOWNLOAD(1L, "Mượn bản mềm để download"), VIEW(2L, "Mượn bản mềm để xem");

        private Long value;
        private String label;

        private FormBorrow(Long value, String label) {
            this.label = label;
            this.value = value;
        }

        public static FormBorrow findByValue(Long value) {
            for (FormBorrow type : FormBorrow.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }

        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
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
    
    public static final String OPEN_TABLE = "<table border='0' cellspacing='0' cellpadding='0' style='width:100%;"
            + " font-family: auto; font-size: 14px; color: #3b3b3b; font-weight: 400; line-height: 30px;'>";
    public static final String CLOSE_TABLE = "</table>";
    // tag tr
    public static final String OPEN_TR_HEADER_RED = "<tr style='background: #ea6153;'>";
    public static final String OPEN_TR_HEADER_GREEN = "<tr style='background: #27ae60;'>";
    public static final String OPEN_TR_HEADER_BLUE = "<tr style='background: #2980b9;'>";
    public static final String OPEN_TR_HEADER_TOMATO = "<tr style='background: #FF6347;'>";
    public static final String OPEN_TR_HEADER_YELLOW_GREEN = "<tr style='background: #9ACD32;'>";
    public static final String OPEN_TR_EVEN = "<tr style='background: #f6f6f6;'>";
    public static final String OPEN_TR_ODD = "<tr style='background: #e9e9e9;'>";
    public static final String CLOSE_TR = "</tr>";
    // tag th
    public static final String OPEN_TH = "<th style='text-align: left; font-weight: 900; color: #ffffff; padding-left: 10px;'>";
    public static final String OPEN_TH_BORDER = "<th style='text-align: left; font-weight: 900; color: #ffffff;"
            + " padding-left: 1px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER = "<th style='text-align: center; width: 1%; font-weight: 900; color: #ffffff;"
            + " padding-left: 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER_1 = "<th style='text-align: center; width: 5%;"
            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER_7 = "<th style='text-align: center; width: 100px;"
            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER_8 = "<th style='text-align: center; width: 130px;"
            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
//    public static final String OPEN_TH_BORDER_CENTER_1 = "<th style='text-align: center; width: 2%; "
//            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER_3 = "<th style='text-align: center; width: 15%; "
            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER_5 = "<th style='text-align: center; width: 30%; "
            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER_10 = "<th style='text-align: center; width: 10%; font-weight: 900; "
            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER_9 = "<th style='text-align: center; width: 9%; font-weight: 900; "
            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER_40 = "<th style='text-align: center; width: 40%; font-weight: 900; "
            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_BORDER_CENTER_50 = "<th style='text-align: center; width: 50%; font-weight: 900; "
            + "color: #ffffff; padding: 0 10px 0 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TH_50 = "<th style='width: 50%; text-align: left; font-weight: 900; color: #ffffff; padding-left: 10px;'>";
    public static final String CLOSE_TH = "</th>";
    // tag td
    public static final String OPEN_TD = "<td style='padding: 5px 5px 5px 10px;'>";
    public static final String OPEN_TD_BORDER = "<td style='padding: 5px 5px 5px 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TD_BORDER_CENTER = "<td style='text-align: center; padding: 5px 5px 5px 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TD_BORDER_LEFT = "<td style='text-align: left; padding: 5px 5px 5px 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TD_BORDER_RIGHT = "<td style='text-align: right; padding: 5px 5px 5px 10px; border-right: 1px solid #d4d4d4;'>";
    public static final String OPEN_TD_ROWSPAN = "<td style='padding: 5px 5px 5px 10px;'>";
    public static final String OPEN_TD_BORDER_ROWSPAN = "<td style='padding: 5px 5px 5px 10px; border-right: 1px solid #d4d4d4;' ";
    public static final String OPEN_TD_BORDER_CENTER_ROWSPAN = "<td style='text-align: center; padding: 5px 5px 5px 10px; border-right: 1px solid #d4d4d4;' ";
    public static final String OPEN_TD_BORDER_LEFT_ROWSPAN = "<td style='text-align: left; padding: 5px 5px 5px 10px; border-right: 1px solid #d4d4d4;' ";
    public static final String CLOSE = ">";
    public static final String CLOSE_TD = "</td>";
    // tag br
    public static final String BR_TAG = "<br>";
    // tag hr
    public static final String HR_TAG = "<hr>";
    // tag strong
    public static final String OPEN_STRONG = "<strong>";
    public static final String CLOSE_STRONG = "</strong>";
    // tag p
    public static final String OPEN_P = "<p>";
    public static final String CLOSE_P = "</p>";

    public static final String OPEN_PARENTHESES = "(";
    public static final String CLOSE_PARENTHESES = ")";
    public static final String PLUS = "+ ";

    // tag div
    public static final String OPEN_DIV = "<div style='width: 100%; float: left; font-family: Helvetica Neue, Helvetica, Arial; font-size: 14px;'>";
    public static final String OPEN_DIV_WIDTH = "<div style='width: {width}; float: left; font-family: Helvetica Neue, Helvetica, Arial; font-size: 14px;'>";
    public static final String CLOSE_DIV = "</div>";
	
	/**
     * 
     * @author DATDC Check Van ban moi hay ko khi upload file goi thau
     */
    public static class CHECK_BRIEF_NEW {
        public static final Long BRIEF_NEW = 1L;
        public static final Long BRIEF_OLD = 0L;
    }
    
    public static enum StatusRequest {
        CREATE(0L, "Tạo mới"),
        CANCEL(1L, "Hủy thẩm định"),
        REVIEW(2L, "Đang xét duyệt"),
        REJECT_SIGN(3L, "Từ chối ký"),
        PROCESSING(4L, "Đang xử lý"),
        EVALUATION(5L, "Đã thẩm định"),
        DECLINED_EVALUATION(6L, "Từ chối thẩm định"),
        RE_EVALUATION(7L, "Đã đề nghị thẩm định lại");
        
        private Long value;
        private String label;

        private StatusRequest(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public static StatusRequest findByValue(Long value) {
            for (StatusRequest type : StatusRequest.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }
        
        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
    
    public static enum TypePriority {
        NORMAL(1L, "Bình thường"),
        EXPRESS(2L, "Hỏa tốc"),
        EMERGENCY(3L, "Khẩn cấp"),
        HIGH_URGENCY(4L, "Thượng khẩn");
        private Long value;
        private String label;

        private TypePriority(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public static TypePriority findByValue(Long value) {
            for (TypePriority type : TypePriority.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }
        
        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
    
    public static enum StatusProcess {
        NONE(0L, "N/A"),
        NONE1(1L, "Hủy thẩm định"),
        NONE2(2L, "N/A"),
        NONE3(3L, "N/A"),
        WAIT_ASSIGN(4L, "Chờ giao việc"),
        PROCESSING_TESTING(5L, "Đang kiểm tra"),
        DECLINED_EVALUATION(6L, "Từ chối nhận"),
        INVALID(7L, "Chưa hợp lệ"),
        ADDITIONAL(8L, "Đang bổ sung"),
        RE_TESTING(9L, "Đang kiểm tra lại"),
        EVALUATING(10L, "Đang thẩm định"),
        EVALUATED(11L, "Đã thẩm định"),
        REJECT_EVALUATION(12L, "Từ chối duyệt");
        
        private Long value;
        private String label;

        private StatusProcess(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public static StatusProcess findByValue(Long value) {
            for (StatusProcess type : StatusProcess.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }
        
        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }

    public static enum BriefFormatName {
        ORIGINAL(1L, "Bản gốc"),
        SOTF(2L, "Bản mềm"),
        MAIN(3L, "Bản chính"),
        CERTIFIED_TRUE_COPY(4L, "Bản sao y bản chính"),
        NOTARIZED(5L, "Bản sao công chứng"),
        PHOTO(6L, "Bản photo");

        private Long value;
        private String label;

        private BriefFormatName(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public static BriefFormatName findByValue(Long value) {
            for (BriefFormatName type : BriefFormatName.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }
        
        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
    /**
     * 
     * @author MINHNQ
     *
     */
    public static class TIME_FORMAT {
        public static final Long NUMBER_DAY = 1L;
        public static final Long ESTIMATE = 2L;
        public static final Long DESIRED = 3L;
    }
    
    public interface SYSTEM_PARAMETER {
    	
    }
    
    /**
     * buildingAssessment start
     */
    public static class BUILDING_ASSESSMENT  {
        public enum ReportType { // mau bao cao
            KTDGCL(1L,"Kiểm tra đánh giá chất lượng"),
            TTCHTMDK(2L,"Thực tập cứu hộ thang máy định kỳ" ),
            KTKT(3L, "Kiểm tra khách thuê"),
            THPCCC(4L, "Tổng hợp PCCC"),
            KTCLDV(5L, "Kiểm tra chất lượng dịch vụ ( ĐUV)"),
            TDKTD(6L, "Theo dõi kiểm tra đèn"),
            TDNL(7L, "Theo dõi năng lượng, XLNT"),
            CDDG(8L,"Chấm điểm đánh giá"),
            KTLDPCCC(9L, "Kiểm tra liên động PCCC"),
            TKTCLDV(10L, "Tổng kiểm tra chất lượng dịch vụ"),
            BCCVT(11L, "Báo cáo công việc tuần"),
            KTDGHSE(12L, "Kiểm tra đánh giá HSE"),
            KTVHHTKT(13L, "Kiểm tra vận hành hệ thống kỹ thuật 285 & D26"),
            KTHTD(14L, "Kiểm tra hệ thống điện D26"),
            KTBCC(15L, "Kiểm tra bình chữa cháy xách tay – xe đẩy 285"),
        	KTCSDKH(16L, "Kiểm tra chỉ số điện khách hàng");

            
            private Long value;
            private String label;
            
            private ReportType(Long value, String label) {
                this.label = label;
                this.value = value;
            }
            public Long getValue() {
                return value;
            }

            public String getLabel() {
                return label;
            }
        }
        
        
        public enum ObjectType { // doi tuong su dung
            BGD(1L,"Ban giám đốc"),
            PKDVH(2L,"Phòng kinh doanh vận hành" ),
            BQLTN(3L, "Ban quản lý tòa nhà"),
            NVKT(4L, "Nhân viên kỹ thuật");
            
            private Long value;
            private String label;
            
            private ObjectType(Long value, String label) {
                this.label = label;
                this.value = value;
            }
            public Long getValue() {
                return value;
            }

            public String getLabel() {
                return label;
            }
        }
        
        public static Map<Long,ReportType> MAP_REPORT_TYPE = new HashMap<>();
        static {
            ReportType[] listReportType = ReportType.values();
            for(ReportType item : listReportType  ) {
                MAP_REPORT_TYPE.put(item.getValue(), item);
            }
        }
    }
    
    public static class IS_DELETE{
        public static final Integer AVAILABLE = 0;
        public static final Integer DELETE = 1;

	}
	
    public static class INPUT_METHOD_TYPE {
    	public static final int  CHOOSE_AN_OPTION = 1;
    	public static final int TEXT = 2;
    	public static final int NUMBER = 3;
    	public static final int UPLOAD_FILE = 4;
    }
    
    public static class ORG_NAME_CALSIP {
        public static final String ORG_NAME_CALSIP = "TĐ & KSCL";
    }
	public static class STATUS_CRITERIA{
        public static final Long DELETED = 1L;
        public static final Long NOT_DELETED = 0L;
    }
	public static class STATUS_ELECTRIC_CONSUME{
        public static final Long DELETED = 1L;
        public static final Long NOT_DELETED = 0L;
    }
    public static class RECORD_SEARCH{
        public static final int RESULT_15 = 15;

	}
	// Level tieu chi fix cung
    public static final Long CRITERIA_MAX_LEVEL = 10L;
    public static final Long CHECK_IS_ACTIVE = 0L;
    public static final Long CHECK_IS_ACTIVE_THAN_1 = 1L;
    
    public static final Long ANDROID = 2L;
    public static final Long IOS = 1L;
    
    // is_active version_manager
    public static class IS_ACTIVE_VERSION{
        public static final Long CREATE = 0L;
        public static final Long ACTIVE = 1L;
        public static final Long DEACTIVE = 2L;
    }
    
    public static class STATUS_VERSION_MANAGER{
        public static final Long DELETED = 1L;
        public static final Long NOT_DELETED = 0L;
        public static final Long ACTIVATED = 1L;
        public static final Long NOT_ACTIVATED = 0L;

    }
    


    public static final int INSERT_FETCH_SIZE = 999;


    // buidding-assessment
    public static class FODEL {
        public static final String FODEL_TEMPLATE = "file_type/template/";
    }

   
    public static final Long LEVEL_CRITERIA = 1L;
    
    /**
     * buildingAssessment start
     */
    public static class BUILDING_ASSESSMENT_CHECKLIST_TYPE  {
        public enum BuildingType { // mau bao cao
            NT(1L,"Nhà tỉnh"),
            TS(2L,"Trụ sở TĐ, TCT,Cty" ),
            NTVH(3L, "Nhà trạm vu hồi"),
            NHSHVT(4L, "Nhà huyện sở hữu Viettel"),
            NKNCV(5L, "Nhà khách, nhà công vụ"),
            NTLQ(7L, "Nhà trạm lặp quang"),
            TDLPM(8L, "TT dữ liệu, phòng máy"),
            TKK(9L,"Tổng kho, kho "),
            NMXSX(11L, "Nhà máy, xưởng sản xuất"),
            NHTN(12L, "Nhà huyện thuê ngoài"),
            K(13L, "Khác"),
        	X(14L, "Xưởng");
            
            private Long value;
            private String label;
            
            private BuildingType(Long value, String label) {
                this.label = label;
                this.value = value;
            }
            public Long getValue() {
                return value;
            }

            public String getLabel() {
                return label;
            }
        }

    }
    
    // ba_schedule start
    public static class SCHEDULE_STATUS {
        public static final Long WAITING_APPROVE = 1L;
        public static final Long APPROVED = 2L;
        public static final Long UNAPPROVED = 3L;
    }
    
    public static class SCHEDULE_CYCLE_TYPE {
        public static final Long CA_1 = 1L;
        public static final Long CA_2 = 2L;
        public static final Long CA_3 = 3L;
        public static final Long HANG_NGAY = 4l;
        public static final Long HANG_TUAN = 5L;
        public static final Long HANG_THANG = 6L;
        public static final Long HANG_QUY = 7L;
        public static final Long KY_6_THANG = 8L;
        
        public enum ScheduleCycleType { // mau bao cao
            CA_1(1L,"Ca 1"),
            CA_2(2L,"Ca 2" ),
            CA_3(3L, "Ca 3"),
            HANG_NGAY(4L, "Hàng ngày"),
            HANG_TUAN(5L, "Hàng tuần"),
            HANG_THANG(6L, "Hàng tháng"),
            HANG_QUY(7L, "Hàng quý"),
            KY_6_THANG(8L, "6 tháng"),
            BAT_NGO(0L,"Đột xuất");
            
            private Long value;
            private String label;
            
            private ScheduleCycleType(Long value, String label) {
                this.label = label;
                this.value = value;
            }
            public Long getValue() {
                return value;
            }

            public String getLabel() {
                return label;
            }
            
            public static String findByValue(Long value) {
                for (ScheduleCycleType scheduleCycleType : ScheduleCycleType.values()) {
                    if (scheduleCycleType.getValue().equals(value)) {
                        return scheduleCycleType.getLabel();
                    }
                }
                return null;
            }
        }
    }
    public static class TASK_STATUS {
        public static final Long WAITING_APPROVE = 1L;
    }
    public static final int SCHEDULE_PROCESS_INTERVAL = 3;
    
    public static class ELEMENT_TYPE {
        public static final Integer CHOOSE_CRITERIA = 1;
        public static final Integer DESCRIPTION = 2;
        public static final Integer FILE_IMG = 3;
        public static final Integer CHOOSE_DATA_TYPE = 4;
    }
    
    public static class REPORT_CONFIG_TEMPLATE  {
        public enum ReportConfigTemplate { // cau hinh bieu mau bao cao 
            KTDGCL(1L,"Template kiểm tra đánh giá.xlsx", 3, true, "STT"),
            TTCHTMDK(2L,"Template thực tập cứu hộ thang máy định kỳ.xlsx", 5 , true, "TT"),
            KTKT(3L, "Template kiểm soát khách thuê.xlsx", 4, true, "STT"),
            THPCCCC(4L, "Template tổng hợp PCCC.xlsx", 4, true, "STT"),
            KTCLDV(5L, "Template kiểm tra chất lượng dịch vụ.xlsx", 5, true, "STT"),
            TDKTD(6L, "Template theo dõi kiểm tra đèn.xlsx", 4, true, "STT"),
            KTHTD(14L, "Template kiểm tra hệ thống điện.xlsx", 4, true, "STT"),
            COMMON(20L, "Template chung.xlsx", 3, true, "STT"),
            KTVHHTKT(13L, "Template Kiểm tra vận hành hệ thống kỹ thuật 285.xlsx", 2, true, "STT");
            
            
            private Long reportType;
            private String fileName;
            private int startRow;
            private boolean isXlsx;
            private String idxHeader;
            
            private ReportConfigTemplate(Long reportType, String fileName, int startRow, boolean isXlsx, String idxHeader) {
                this.reportType = reportType;
                this.fileName = fileName;
                this.startRow = startRow;
                this.isXlsx = isXlsx;
                this.idxHeader = idxHeader;
            }
            public Long getReportType() {
                return reportType;
            }

            public String getFileName() {
                return fileName;
            }
            
            public int getStartRow() {
                return startRow;
            }
            
            public boolean isXlsx() {
                return isXlsx;
            }
            
            public String getIdxHeader() {
                return idxHeader;
            }
        }

    }
    
    public static class ELEMENT_KTCLDV_FIX {
        public static final String DAT = "Đạt";
        public static final String KHONG_DAT = "Không đạt";
        public static final String KHONG_AP_DUNG = "Không áp dụng";
        public static final String GHI_CHU = "Ghi chú";
    }

    // ba_schedule end
	
	public static enum TypeStatusIncident {
        NO_PROCESS(1L, "Chưa xử lý"),
        PROCESSING_OUT(2L, "Đang xử lý"),
        OUT_PROCESSED(3L, "Quá hạn xử lý"),
        PROCESSED(4L, "Đã xử lý"),
		PROCESSED_NO_PAYMENT(1070L, "Đã xử lý");
        private Long value;
        private String label;

        private TypeStatusIncident(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public static TypeStatusIncident findByValue(Long value) {
            for (TypeStatusIncident type : TypeStatusIncident.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }
        
        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
    
    public static enum TypeLevelIncident {
        LOW(3L, "Thấp"),
        NORMAL(2L, "Bình thường"),
        HIGH(1L, "Cao"),
        EMERGENCY(4L, "Khẩn cấp");
        private Long value;
        private String label;
        
        private TypeLevelIncident(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public static TypeLevelIncident findByValue(Long value) {
            for (TypeLevelIncident type : TypeLevelIncident.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }
        
        public Long getValue() {
            return value;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    public static enum TypeIncident {
        TYPE_INCEDENT_1(9L, "Việc thường xuyên"),
        TYPE_INCEDENT_2(3L, "Sự cố nhỏ lẻ"),
        TYPE_INCEDENT_3(7L, "Sự cố lớn"),
        TYPE_INCEDENT_4(2L, "Yêu cầu mua sắm"),
        TYPE_INCEDENT_5(8L, "Bảo trì thường xuyên");
        private Long value;
        private String label;
        
        private TypeIncident(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public static TypeIncident findByValue(Long value) {
            for (TypeIncident type : TypeIncident.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }
        
        public Long getValue() {
            return value;
        }
        
        public String getLabel() {
            return label;
        }
    }

    public static enum TypeResourceReceive {
        DIRECT(4L, "Trực tiếp"),
        INDIRECT(1024L, "Gián tiếp");
        private Long value;
        private String label;
        
        private TypeResourceReceive(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public static TypeResourceReceive findByValue(Long value) {
            for (TypeResourceReceive type : TypeResourceReceive.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return null;
        }
        
        public Long getValue() {
            return value;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    public static class IS_RESOLVE{
        public static final Long RESOLVE = 0L;
        public static final Long IS_RESOLVE = 1L;
    }

    
    public static class STATUS_ELECTRIC_METER{
        public static final Long DELETED = 1L;
        public static final Long NOT_DELETED = 0L;
    }
    
    // duong dan export
    public static class CONSUME_EXPORT {
        public static final String CONSUME_EXPORT = "electricManage/bien_ban_xac_nhan_tieu_thu_dien.docx";
    }
    
    public enum IncidentStatus { // trang thai su co
        CHUA_XU_LY(1L,"Chưa xử lý"),
        DANG_XU_LY(2L,"Đang xử lý" ),
        DA_XU_LY(3L, "Đã xử lý"),
        QUA_HAN_XU_LY(4L, "Quá hạn xử lý"),
    	CHUA_THANH_TOAN(1070L, "Đã xử lý, chưa thanh toán");
        
        private Long value;
        private String label;
        
        private IncidentStatus(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public Long getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }
    }
    
    public enum PositionAlignInputMethod { // vị trí hiển thị của input method
        CENTER(1, DynamicExport.CENTER_FORMAT),
        LEFT(2, DynamicExport.ALIGN_LEFT),
        RIGHT(3, DynamicExport.ALIGN_RIGHT);
        
        private Integer key;
        private int value;
        
        private PositionAlignInputMethod(Integer key, int value) {
            this.key = key;
            this.value = value;
        }
        
        public Integer getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }
    }
    
    public static class TYPE_PUBLIC_INCIDENT{
        public static final Long TYPE_INCIDENT = 11L;
        public static final Long TYPE_PERFECT = 12L;
    }
    
    public static class TYPE_SENDER_NOTIFICATION{
        public static final Long SENDED = 2L;
        public static final Long UNSEND = 1L;
    }
    
    public static final String NOTIFY_ALIAS = "DGTN";

    public static final String VIEW_DETAIL = "Xem chi tiết";
    public static class TYPE_VIEW_NOTIFICATION{
        public static final Integer VIEW = 1;
        public static final Integer UNVIEW = 0;
    }
    
    public static class INCIDENT_STATUS_RESOLVE{
        public static final Long NO_PROCESS = 1L;
        public static final Long PROCESSING = 2L;
        public static final Long OUT_OF_DATE = 3L;
        public static final Long PROCESSED = 4L;
        public static final Long PROCESSED_NO_PAYMENT = 1070L;
    }
    
    public static final int LIST_LONG_SIZE_MAX = 1000;
	
		public static enum Status_Brief{
    	STATUS_BRIEF_0(0L, "Mới"),
    	STATUS_BRIEF_1(1L, "Bàn giao bản mềm"),
    	STATUS_BRIEF_2(2L, "Bàn giao bản cứng");
        private Long value;
        private String label;
        
        private Status_Brief(Long value, String label) {
            this.label = label;
            this.value = value;
        }
        public static Status_Brief findByValue(Long value) {
            for (Status_Brief status : Status_Brief.values()) {
                if (status.getValue().equals(value)) {
                    return status;
                }
            }
            return null;
        }
        
        public Long getValue() {
            return value;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    public static final String STATUS_YES = "Có";
    public static final String STATUS_NO = "Không";
    public static final Long BOOLEAN_TRUE = 1L;
    public static final Long SELECTED = 1L;
	public static class CONTRACTORS_ISACTIVE{
    	public static final Long CREATED = 0L;
    	public static final Long USED = 1L;
    	public static final Long DELETED = 2L;
    }

	public static final String TAX_NUMBER_EXISTED = "contractors.taxNumber.existed";
	 public static class LIST_TYPE_GRID_WAREHOUE {
    	public static final String TYPE_DOCUMENT = "1";
    	public static final String  TYPE_PROJECT = "2";
    	public static final String TYPE_WAREHOUSE_BOX = "3";
    }
	// duong dan print handoverballot
	public static class HANDOVER_BALLOT_PRINT {
	   public static final String HANDOVER_BALLOT_PRINT = "handoverBallot/print/danh_sach_tai_lieu_ban_giao.docx";
	}
	public static class HAS_FILE{
        public static final Integer YES = 1;
        public static final Integer NO = 2;
	}
    // brief: 1: to, 2: quyen
    public static class PAGE_TYPE{
        public static final Integer SHEET = 1;
        public static final Integer BOOK = 2;
	}
    
    public static class ACTION_TYPE{
        public static final Integer ADD = 1;
        public static final Integer EDIT = 2;
        public static final Integer DELETE = 3;
        public static final Integer TRANSFER = 4;
	}
    
    public static class CHECK_ROOT{
    	public static final Integer IS_ROOT = 1;
        public static final Integer NOT_ROOT = 2;
    }
	public static class HOLIDAY{
    	public static final String START_DATE_NULL = "Ngày bắt đầu không được rỗng";
    	public static final String END_DATE_NULL = "Ngày kết thúc không được rỗng";
    	public static final String DATE_FORMAT = "Ngày phải có dạng ngày dd/mm/yyyy";
    	public static final String START_DATE_BEFORE_END_DATE = "Ngày bắt đầu không được lớn hơn ngày kết thúc";
    	public static final String TIME_EXIST = "Thời gian đã bị trùng";
    	public static final String CONTENT_NULL = "Nội dung không được rỗng";
    	public static final String CONTENT_MAX_LENGTH = "Nội dung không được lớn hơn 200 ký tự";
    	public static final String CONTENT_EXCEPT_SYMBOL = "Nội dung không được có ký tự đặc biệt";
    }
    public static class SCHEDULE{
        public static final String START_DATE_NULL = "Ngày bắt đầu không được rỗng";
        public static final String END_DATE_NULL = "Ngày kết thúc không được rỗng";
        public static final String DATE_FORMAT = "Ngày phải có dạng ngày dd/mm/yyyy";
        public static final String START_DATE_BEFORE_END_DATE = "Ngày bắt đầu không được lớn hơn ngày kết thúc";
        public static final String CONTENT_NULL = "Nội dung không được rỗng";
    }
    public static class HISTORY_ACTION_TYPE {
	   public static final Long CREATED = 1L;
	   public static final Long MODIFIED = 2L;
	   public static final Long DELETED = 3L;
   }
   
   public static enum DOC_TYPE {
   	STATUS_DOC_1(1, "Hồ sơ lưu"),
   	STATUS_DOC_2(2, "Hồ sơ dự phòng 1"),
   	STATUS_DOC_3(3, "Hồ sơ dự phòng 2");
	   
       private Integer value;
       private String label;
       
       private DOC_TYPE (Integer value, String label) {
           this.label = label;
           this.value = value;
       }
       public static DOC_TYPE findByValue(Integer value) {
    	   if(CommonUtils.isEmpty(value)) {
    		   return null;
    	   }
           for (DOC_TYPE status : DOC_TYPE.values()) {
               if (status.getValue().equals(value)) {
                   return status;
               }
           }
           return null;
       }
       
       public Integer getValue() {
           return value;
       }
       
       public String getLabel() {
           return label;
       }
   }
   
   public static enum DURATION_PRESERVATION {
	   	TYPE_1(1, "10 năm"),
	   	TYPE_2(2, "30 năm"),
	   	TYPE_3(3, "Theo tuổi thọ công trình"),
	   	TYPE_4(4, "Vĩnh viễn");
	       private Integer value;
	       private String label;
	       
	       private DURATION_PRESERVATION (Integer value, String label) {
	           this.label = label;
	           this.value = value;
	       }
	       public static DURATION_PRESERVATION findByValue(Integer value) {
	    	   if(CommonUtils.isEmpty(value)) {
	    		   return null;
	    	   }
	           for (DURATION_PRESERVATION status : DURATION_PRESERVATION.values()) {
	               if (status.getValue().equals(value)) {
	                   return status;
	               }
	           }
	           return null;
	       }
	       
	       public Integer getValue() {
	           return value;
	       }
	       
	       public String getLabel() {
	           return label;
	       }
	   }
   public static enum TIME_FORMAT_TYPE {
	   	NUMBER_DAY(1L, "KPI"),
	   	ESTIMATE(2L, "Ấn định ngày"),
	   	DESIRED(3L, "Theo ngày mong muốn");
	       private Long value;
	       private String label;
	       
	       private TIME_FORMAT_TYPE (Long value, String label) {
	           this.label = label;
	           this.value = value;
	       }
	       public static TIME_FORMAT_TYPE findByValue(Long value) {
	    	   if(CommonUtils.isEmpty(value)) {
	    		   return null;
	    	   }
	           for (TIME_FORMAT_TYPE status : TIME_FORMAT_TYPE.values()) {
	               if (status.getValue().equals(value)) {
	                   return status;
	               }
	           }
	           return null;
	       }
	       
	       public static String findLabelByValue(Long value) {
	    	   if(CommonUtils.isEmpty(value)) {
	    		   return "";
	    	   }
	           for (TIME_FORMAT_TYPE status : TIME_FORMAT_TYPE.values()) {
	               if (status.getValue().equals(value)) {
	                   return status.label;
	               }
	           }
	           return "";
	       }
	       
	       
	       public Long getValue() {
	           return value;
	       }
	       
	       public String getLabel() {
	           return label;
	       }
	   }
	   public static class LOCATION_ERROR_GROUP_DOC{
       public static final Long MAIN = 1L;
       public static final Long ROOT = 2L;
       public static final Long ORTHER = 3L;
	}
}
