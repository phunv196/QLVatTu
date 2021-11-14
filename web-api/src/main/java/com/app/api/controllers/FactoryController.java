package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.EmployeeDao;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.ImportFileExcell;
import com.app.dao.base.converter.DynamicExport;
import com.app.dao.FactoryDao;
import com.app.model.BaseResponse;
import com.app.model.ExportModel;
import com.app.model.factory.FactoryModel;
import com.app.model.factory.FactoryModel.FactoryResponse;
import com.app.model.employee.EmployeeModel;
import com.app.util.Constants;
import com.app.util.TemplateResouces;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.app.util.Constants.COMMON.*;
import static com.app.util.Constants.COMMON.FOLDER_EXPORT_TEMPLATE;

@Path("factorys")
@Tag(name = "Factorys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FactoryController extends BaseController {

    FactoryDao factoryDao = new FactoryDao();
    EmployeeDao employeeDao = new EmployeeDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of factorys",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = FactoryResponse.class)))}
    )
    public Response getFactoryList(
            @Parameter(description = "Order Id") @QueryParam("factoryId") Long factoryId,
            @Parameter(description = "Order Id") @QueryParam("searchCode") String searchCode,
            @Parameter(description = "Order Id") @QueryParam("searchName") String searchName,
            @Parameter(description = "Order Id") @QueryParam("searchEmail") String searchEmail,
            @Parameter(description = "Order Id") @QueryParam("searchEmployee") Long searchEmployee,
            @Parameter(description = "Order Id") @QueryParam("searchFormDate") String searchFormDate,
            @Parameter(description = "Order Id") @QueryParam("searchToDate") String searchToDate,
            @Parameter(description = "Order Id") @QueryParam("searchFormSuccessDate") String searchFormSuccessDate,
            @Parameter(description = "Order Id") @QueryParam("searchToSuccessDate") String searchToSuccessDate,
            @Parameter(description = "Page No, Starts from 1 ", example = "1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description = "Items in each page", example = "20") @DefaultValue("1000") @QueryParam("page-size") int pageSize
    ) {
        FactoryResponse resp = new FactoryResponse();
        try {
            if (factoryId == null) {
                factoryId = 0l;
            }
            if (searchEmployee == null) {
                searchEmployee = 0l;
            }
            java.sql.Date formDateSQL = CommonUtils.convertStringToSQLDate(searchFormDate);
            java.sql.Date toDateSQL = CommonUtils.convertStringToSQLDate(searchToDate);
            java.sql.Date formSuccessDateSQL = CommonUtils.convertStringToSQLDate(searchFormSuccessDate);
            java.sql.Date toSuccessDateSQL = CommonUtils.convertStringToSQLDate(searchToSuccessDate);

            List<FactoryModel> modelList = factoryDao.getList(page, pageSize, factoryId, searchCode, searchName,
                    searchEmail, searchEmployee, formDateSQL, toDateSQL, formSuccessDateSQL, toSuccessDateSQL);
            BigInteger total = factoryDao.getFactoryCount(factoryId, searchCode, searchName,
                    searchEmail, searchEmployee, formDateSQL, toDateSQL, formSuccessDateSQL, toSuccessDateSQL);
            resp.setList(modelList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(),pageSize, page,"");
            resp.setSuccessMessage("Danh sách phân xưởng " + (factoryId>0 ? "- factory:"+factoryId:""));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Lỗi sảy ra - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of factorys",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = FactoryResponse.class)))}
    )
    public Response getAll(
            @Parameter(description="Factory Id") @QueryParam("factoryId") int factoryId
    ) {
        Criteria criteria = factoryDao.createCriteria(FactoryModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<FactoryModel> factoryList = criteria.list();
        FactoryResponse resp = new FactoryResponse();
        resp.setList(factoryList);
        resp.setSuccessMessage("List of factorys");
        return Response.ok(resp).build();
    }

    @GET
    @Path("{factoryId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Factory",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getById(@Parameter(description="Factory Id", example="601") @PathParam("factoryId") Long factoryId) {
        BaseResponse resp = new BaseResponse();
        try {
            FactoryModel foundProd  = factoryDao.getById(factoryId);
            return Response.ok(foundProd).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Lỗi xảy ra - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Factory",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addFactory(FactoryModel factory) {
        BaseResponse resp = new BaseResponse();
        try {
            factoryDao.beginTransaction();
            factoryDao.save(factory);
            factoryDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thê mới bản ghi thành công code: %s ", factory.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Factory",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateFactory(FactoryModel factory) {
        BaseResponse resp = new BaseResponse();
        try {
            FactoryModel foundProd  = factoryDao.getById(factory.getFactoryId());
            if (foundProd != null) {
                factoryDao.beginTransaction();
                factoryDao.update(factory);
                factoryDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", factory.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", factory.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Sửa bản ghi thất bại - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{factoryId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Factory",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteFactory(@Parameter(description="Factory Id", example="601") @PathParam("factoryId") Long factoryId) {
        BaseResponse resp = new BaseResponse();
        try {
            FactoryModel foundProd  = factoryDao.getById(factoryId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", factoryId));
                return Response.ok(resp).build();
            } else {
                factoryDao.beginTransaction();
                factoryDao.delete(factoryId);
                factoryDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (code:%s)", foundProd.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể xóa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @Path("byCode")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getByCode(
            FactoryModel model
    ) {
        Criteria criteria = factoryDao.createCriteria(FactoryModel.class);
        if (model.getFactoryId() != null){
            criteria.add(Restrictions.ne("factoryId", model.getFactoryId()));
        }
        if (!CommonUtils.isNullOrEmpty(model.getCode())){
            criteria.add(Restrictions.eq("code", model.getCode()).ignoreCase());
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount > 0).build();
    }

    @POST
    @Path("export")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response export(
            FactoryModel factory
    ) throws Exception {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        String fileName = "danh_sach_phan_xuong.xls";
        Integer startDataRow = 6;
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_EXPORT_EXCELL + fileName), startDataRow, false);
        List<FactoryModel> models = factoryDao.getListExport(factory.getCode(), factory.getName(), factory.getEmail(),
                factory.getEmployeeId(), factory.getFormDate(), factory.getToDate(), factory.getFormSuccessDate(), factory.getToSuccessDate());
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                FactoryModel model = models.get(i);
                int index = 0;
                dynamicExport.increaseRow();
                dynamicExport.setEntry(stt++, index++);
                dynamicExport.setText(model.getCode(), index++);
                dynamicExport.setText(model.getName(), index++);
                dynamicExport.setText(model.getEmail(), index++);
                dynamicExport.setText(model.getAddress(), index++);
                dynamicExport.setText(model.getEmployeeName(), index++);
                dynamicExport.setText(CommonUtils.convertDateToString(model.getDateConstruction()), index++);
                dynamicExport.setText(CommonUtils.convertDateToString(model.getDateFinish()), index++);
            }
        }
        dynamicExport.setCellFormat(startDataRow, 0, dynamicExport.getLastRow(), 7, DynamicExport.BORDER_FORMAT);
        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = FOLDER_EXPORT + prefixOutPutFile +  "danh_sach_phan_xuong";
        String filePath = dynamicExport.exportFile(fileExport, req);
        File file = new File(filePath);
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        String[] fileNameNew = filePath.split("/");
        ExportModel exportModel = new ExportModel();
        exportModel.setFileName(fileNameNew[fileNameNew.length - 1]);
        exportModel.setData(encodedString);
        resp.setData(exportModel);
        return Response.ok(resp).build();
    }

    @GET
    @Path("downloadTemplate")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response dowloadTemplate() throws Exception {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        String fileName = "BM_Nhap_Moi_Phan_Xuong.xls";
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_IMPORT_EXCELL + fileName), 6, false);
        dynamicExport.setActiveSheet(1);
        List<EmployeeModel> listEmployeeModel = employeeDao.getAll(EmployeeModel.class, "employeeId");
        int rows = 2;
        for (EmployeeModel model : listEmployeeModel) {
            dynamicExport.setEntry(String.valueOf(rows-1), 0, rows);
            dynamicExport.setText(model.getCode(), 1, rows);
            dynamicExport.setText(model.getFullName(), 2, rows);
            rows++;
        }
        dynamicExport.setCellFormat(0, 0, rows-1, 2, DynamicExport.BORDER_FORMAT);
        String fileExport = FOLDER_EXPORT_TEMPLATE + "BM_Nhap_Moi_Phan_Xuong";
        String filePath = dynamicExport.exportFile(fileExport, req);
        File file = new File(filePath);
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        String[] fileNameNew = filePath.split("/");
        ExportModel exportModel = new ExportModel();
        exportModel.setFileName(fileNameNew[fileNameNew.length - 1]);
        exportModel.setData(encodedString);
        resp.setData(exportModel);
        return Response.ok(resp).build();
    }

    @POST
    @Path("uploadFile")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add an employee",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response uploadFile(ExportModel model) throws Exception {
        ImportFileExcell importFileExcell = new ImportFileExcell();
        BaseResponse resp = new BaseResponse();
        List<FactoryModel> factoryModels = new ArrayList<>();
        List<FactoryModel> factoryModelList = factoryDao.getAll(FactoryModel.class, "factoryId");
        List<String> factoryCodes = new ArrayList<>();
        factoryModelList.forEach(element -> {
            factoryCodes.add(element.getCode());
        });
        List<EmployeeModel> listEmployeeModel = employeeDao.getAll(EmployeeModel.class, "employeeId");
        Map<String, Long> mapEmployee = new HashMap<>();
        listEmployeeModel.forEach(element -> {
            if(!CommonUtils.isNullOrEmpty(element.getCode())){
                mapEmployee.put(element.getCode().toLowerCase(), element.getEmployeeId());
            }
        });
        List<ImportFileExcell.ImportErrorBean> errorList = new ArrayList<>();
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(model.getData());
            FileUtils.writeByteArrayToFile(new File(FOLDER_IMPORT + model.getFileName()), decodedBytes);
            int startDataRow = 6;
            int columnConfig = 10;
            ImportFileExcell.ImportBean importBean = importFileExcell.getListImport(FOLDER_IMPORT + model.getFileName(), startDataRow, columnConfig);
            for (int row = 0 ; row < importBean.getRows().size(); row++) {
                Object[] objects = importBean.getDataList().get(row);
                FactoryModel factoryModel = new FactoryModel();
                int column = 1;
                String code = (String) objects[column++];
                String name = (String) objects[column++];
                String email = (String) objects[column++];
                String employeeCode = (String) objects[column++];
                String address = (String) objects[column++];
                String dateConstruction = (String) objects[column++];
                String dateFinish = (String) objects[column++];
                String description = (String) objects[column++];
                column = 1;
                if (CommonUtils.isNullOrEmpty(code)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (factoryCodes.contains(code.toLowerCase())) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.DUOLICATE, (String) objects[column]));
                    } else {
                        factoryCodes.add(code.toLowerCase());
                        factoryModel.setCode(code);
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(name)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    factoryModel.setName(name);
                }
                column++;
                if (CommonUtils.isNullOrEmpty(email)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (CommonUtils.isValidEmailAddress(email)) {
                        factoryModel.setEmail(email);
                    } else {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.FOMAT, (String) objects[column]));
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(employeeCode)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if(!mapEmployee.containsKey(employeeCode.toLowerCase())) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, "Mã nhân viên phải nhập theo dữ liệu cho trước!", (String) objects[column]));
                    } else {
                        factoryModel.setEmployeeId(mapEmployee.get(employeeCode.toLowerCase()));
                    }
                }
                column++;
                factoryModel.setAddress(address);
                column++;
                if (CommonUtils.isNullOrEmpty(dateConstruction)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (CommonUtils.convertStringToDateBasic(dateConstruction) == null) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.DATE, (String) objects[column]));
                    } else {
                        factoryModel.setDateConstruction(CommonUtils.convertStringToDateBasic(dateConstruction));
                    }
                }

                column++;
                if (CommonUtils.isNullOrEmpty(dateFinish)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (CommonUtils.convertStringToDateBasic(dateFinish) == null) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.DATE, (String) objects[column]));
                    } else {
                        factoryModel.setDateFinish(CommonUtils.convertStringToDateBasic(dateFinish));
                    }
                }
                factoryModel.setDescription(description);
                factoryModels.add(factoryModel);
            }
            if (errorList.size() > 0) {
                return Response.ok(errorList).build();
            }
            factoryDao.beginTransaction();
            factoryDao.saveOrUpdateAll(factoryModels);
            factoryDao.commitTransaction();
            resp.setSuccessMessage("Import thành công!");
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Import thất bại - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }
}
