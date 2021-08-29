package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.EmployeeDao;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.ImportFileExcell;
import com.app.dao.base.ImportFileExcell.ImportBean;
import com.app.dao.base.ImportFileExcell.ImportErrorBean;
import com.app.dao.base.converter.DynamicExport;
import com.app.dao.category.DepartmentDao;
import com.app.dao.category.PositionDao;
import com.app.model.BaseResponse;
import com.app.model.ExportModel;
import com.app.model.ExportModel.ExportResponse;
import com.app.model.category.DepartmentModel;
import com.app.model.category.PositionModel;
import com.app.model.employee.EmployeeModel;
import com.app.model.employee.EmployeeModel.EmployeeResponse;
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
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.app.util.Constants.COMMON.*;

@Path("employees")
@Tag(name = "Employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin
public class EmployeeController extends BaseController {
    EmployeeDao employeeDao = new EmployeeDao();
    DepartmentDao departmentDao = new DepartmentDao();
    PositionDao positionDao = new PositionDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Get list of employees",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = EmployeeResponse.class)))}
    )
    public Response getEmployeeList(
        @Parameter(description="Employee Id", example="202") @QueryParam("employee-id") Long employeeId,
        @Parameter(description = "Order Id") @QueryParam("searchCode") String searchCode,
        @Parameter(description = "Order Id") @QueryParam("searchName") String searchName,
        @Parameter(description = "Order Id") @QueryParam("searchEmail") String searchEmail,
        @Parameter(description = "Order Id") @QueryParam("searchPhone") String searchPhone,
        @Parameter(description = "Order Id") @QueryParam("searchDepartment") Long searchDepartment,
        @Parameter(description = "Order Id") @QueryParam("searchPosition") Long searchPosition,
        @Parameter(description="Search by name or email - Use % for wildcard like '%ra%'", example="%ra%") @QueryParam("search") String search,
        @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1")  @QueryParam("page")  int page,
        @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        EmployeeResponse resp = new EmployeeResponse();
        if (employeeId == null) {
            employeeId = 0l;
        }

        if (searchDepartment == null) {
            searchDepartment = 0l;
        }

        if (searchPosition == null) {
            searchPosition = 0l;
        }

        List<EmployeeModel> employeeList = employeeDao.getList(page, pageSize, employeeId, searchCode,
                searchName, searchEmail, searchPhone, searchDepartment, searchPosition);
        BigInteger total = employeeDao.getEmployeeCount(employeeId, searchCode,
                searchName, searchEmail, searchPhone, searchDepartment, searchPosition);
        resp.setList(employeeList);
        resp.setTotal(total.intValue());
        resp.setPageStats(total.intValue(), pageSize, page, "");
        resp.setSuccessMessage("List of Orders and nested details " + (employeeId > 0 ? "- Customer:" + employeeId : ""));
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Add an employee",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addEmployee(EmployeeModel emp) {
        BaseResponse resp = new BaseResponse();
        try {
            employeeDao.beginTransaction();
            employeeDao.save(emp);
            employeeDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công code: %s ", emp.getCode()));
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Update an Employee",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateEmployee(EmployeeModel emp) {

        BaseResponse resp = new BaseResponse();
        try {
            EmployeeModel foundEmp  = employeeDao.getById(emp.getEmployeeId());
            if (foundEmp != null){
                employeeDao.beginTransaction();
                employeeDao.update(emp);
                employeeDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", emp.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", emp.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{employeeId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Delete an Employee",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteEmployee(@Parameter(description="Employee Id", example="1") @PathParam("employeeId") Long employeeId) {
        BaseResponse resp = new BaseResponse();
        try {
            if (employeeId == null) {
                employeeId = 0l;
            }
            EmployeeModel foundEmp  = employeeDao.getById(employeeId);
            if (foundEmp==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", employeeId));
                return Response.ok(resp).build();
            } else {
                employeeDao.beginTransaction();
                employeeDao.delete(employeeId);
                employeeDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (id:%s)", employeeId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể xóa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }


    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of employees",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = EmployeeResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = employeeDao.createCriteria(EmployeeModel.class);
        criteria.setProjection(null);
        List<EmployeeModel> empUserList = criteria.list();
        EmployeeResponse resp = new EmployeeResponse();
        resp.setList(empUserList);
        resp.setSuccessMessage("List of employees");
        return Response.ok(resp).build();
    }

    @GET
    @Path("downloadTemplate")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response dowloadTemplate() throws Exception {
        ExportResponse resp = new ExportResponse();
        String fileName = "BM_Nhap_Moi_Nhan_Vien.xls";
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_IMPORT_EXCELL + fileName), 6, false);
        dynamicExport.setActiveSheet(1);
        List<DepartmentModel> departmentList = departmentDao.getAll(DepartmentModel.class, "departmentId");
        int rows = 2;
        for (DepartmentModel model : departmentList) {
            dynamicExport.setEntry(String.valueOf(rows-1), 0, rows);
            dynamicExport.setText(model.getCode(), 1, rows);
            dynamicExport.setText(model.getName(), 2, rows);
            rows++;
        }
        dynamicExport.setCellFormat(0, 0, rows-1, 2, DynamicExport.BORDER_FORMAT);
        rows = 2;
        List<PositionModel> positionList = positionDao.getAll(PositionModel.class, "positionId");
        for (PositionModel model : positionList) {
            dynamicExport.setEntry(String.valueOf(rows-1), 4, rows);
            dynamicExport.setText(model.getCode(), 5, rows);
            dynamicExport.setText(model.getName(), 6, rows);
            rows++;
        }
        dynamicExport.setCellFormat(0, 4, rows-1, 6, DynamicExport.BORDER_FORMAT);

        String fileExport = FOLDER_EXPORT_TEMPLATE + "BM_Nhap_Moi_Nhan_Vien";
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
    @Path("export")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response export(
            EmployeeModel employee
    ) throws Exception {
        ExportResponse resp = new ExportResponse();
        String fileName = "danh_sach_nhan_vien.xls";
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_EXPORT_EXCELL + fileName), 6, false);
        List<EmployeeModel> models = employeeDao.getListExport(employee.getCode(), employee.getFullName(), employee.getEmail(),
                employee.getPhone(), employee.getDepartmentId(), employee.getPositionId());
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                EmployeeModel model = models.get(i);
                int index = 0;
                dynamicExport.increaseRow();
                dynamicExport.setEntry(stt++, index++);
                dynamicExport.setText(model.getCode(), index++);
                dynamicExport.setText(model.getFullName(), index++);
                dynamicExport.setText(CommonUtils.convertDateToString(model.getBirth()), index++);
                dynamicExport.setText(model.getSexString(), index++);
                dynamicExport.setText(model.getPhone(), index++);
                dynamicExport.setText(model.getEmail(), index++);
                dynamicExport.setText(model.getDepartmentName(), index++);
                dynamicExport.setText(model.getPositionName(), index++);
            }
        }
        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = FOLDER_EXPORT + prefixOutPutFile +  "danh_sach_nhan_vien";
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
    @Path("byCode")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getByCode(
            EmployeeModel model
    ) {
        Criteria criteria = employeeDao.createCriteria(EmployeeModel.class);
        if (model.getEmployeeId() != null){
            criteria.add(Restrictions.ne("employeeId", model.getEmployeeId()));
        }
        if (!CommonUtils.isNullOrEmpty(model.getCode())){
            criteria.add(Restrictions.eq("code", model.getCode()).ignoreCase());
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount > 0).build();
    }


    @GET
    @Path("getEmployeeById/{employeeId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getEmployeeById(
            @Parameter(description="Employee Id", example="1") @PathParam("employeeId") Long employeeId
    ) {
        Criteria criteria = employeeDao.createCriteria(EmployeeModel.class);
        if (employeeId != null){
            criteria.add(Restrictions.eq("employeeId", employeeId));
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        EmployeeModel model = (EmployeeModel) criteria.uniqueResult();
        return Response.ok(model).build();
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
        List<DepartmentModel> departmentList = departmentDao.getAll(DepartmentModel.class, "departmentId");
        Map<String, Long> department = new HashMap<>();
        departmentList.forEach(element ->{
            department.put(element.getCode().toLowerCase(), element.getDepartmentId());
        });
        List<PositionModel> positionList = positionDao.getAll(PositionModel.class, "positionId");
        Map<String, Long> position = new HashMap<>();
        positionList.forEach(element ->{
            position.put(element.getCode().toLowerCase(), element.getPositionId());
        });
        List<EmployeeModel> listEmployeeModel = employeeDao.getAll(EmployeeModel.class, "employeeId");
        List<EmployeeModel> employeeModels = new ArrayList<>();
        List<ImportErrorBean> errorList = new ArrayList<>();
        List<String> employeeCode = new ArrayList<>();
        listEmployeeModel.forEach(element -> {
            if(!CommonUtils.isNullOrEmpty(element.getCode())){
                employeeCode.add(element.getCode().toLowerCase());
            }
        });
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(model.getData());
            FileUtils.writeByteArrayToFile(new File(FOLDER_IMPORT + model.getFileName()), decodedBytes);
            int startDataRow = 6;
            int columnConfig = 10;
            ImportBean importBean = importFileExcell.getListImport(FOLDER_IMPORT + model.getFileName(), startDataRow, columnConfig);
            for (int row = 0 ; row < importBean.getRows().size(); row++) {
                Object[] objects = importBean.getDataList().get(row);
                EmployeeModel employeeModel = new EmployeeModel();
                int column = 1;
                String code = (String) objects[column++];
                String firstName = (String) objects[column++];
                String lastName = (String) objects[column++];
                String birth = (String) objects[column++];
                String sex = (String) objects[column++];
                String email = (String) objects[column++];
                String phone = (String) objects[column++];
                String departmentCode = (String) objects[column++];
                String positionCode = (String) objects[column++];
                String address = (String) objects[column++];
                column = 1;
                if (CommonUtils.isNullOrEmpty(code)) {
                    errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (employeeCode.contains(code.toLowerCase())) {
                        errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.DUOLICATE, (String) objects[column]));
                    } else {
                        employeeCode.add(code.toLowerCase());
                        employeeModel.setCode(code);
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(firstName)) {
                    errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    employeeModel.setFirstName(firstName);
                }
                column++;
                if (CommonUtils.isNullOrEmpty(lastName)) {
                    errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    employeeModel.setLastName(lastName);
                }
                column++;
                if (CommonUtils.isNullOrEmpty(birth)) {
                    errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (CommonUtils.convertStringToDateBasic(birth) == null) {
                        errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.DATE, (String) objects[column]));
                    } else {
                        employeeModel.setBirth(CommonUtils.convertStringToDateBasic(birth));
                    }
                }

                column++;
                if (CommonUtils.isNullOrEmpty(sex)) {
                    errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (!sex.toLowerCase().equals("nam") && !sex.toLowerCase().equals("nu")) {
                        errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.FOMAT, (String) objects[column]));
                    } else {
                        employeeModel.setSex(sex.toLowerCase().equals("nam") ? 1L : 2L);
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(email)) {
                    errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (CommonUtils.isValidEmailAddress(email)) {
                        employeeModel.setEmail(email);
                    } else {
                        errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.FOMAT, (String) objects[column]));
                    }
                }
                column++;
                if (!CommonUtils.containPhoneNumber(phone) && !CommonUtils.isNullOrEmpty(phone)) {
                    errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.FOMAT, (String) objects[column]));
                } else {
                    employeeModel.setPhone(phone);
                }

                column++;
                if (CommonUtils.isNullOrEmpty(departmentCode)) {
                    errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if(!department.containsKey(departmentCode.toLowerCase())) {
                        errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, "Mã phòng ban phải nhập theo dữ liệu cho trước!", (String) objects[column]));
                    } else {
                        employeeModel.setDepartmentId(department.get(departmentCode.toLowerCase()));
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(positionCode)) {
                    errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if(!position.containsKey(positionCode.toLowerCase())) {
                        errorList.add(new ImportErrorBean(importBean.getRows().get(row), column, "Mã chức vụ phải nhập theo dữ liệu cho trước!", (String) objects[column]));
                    } else {
                        employeeModel.setPositionId(position.get(positionCode.toLowerCase()));
                    }
                }
                employeeModel.setFullName(firstName + " " + lastName);
                employeeModel.setAddress(address);
                employeeModels.add(employeeModel);
            }
            if (errorList.size() > 0) {
                return Response.ok(errorList).build();
            }
            employeeDao.beginTransaction();
            employeeDao.saveOrUpdateAll(employeeModels);
            employeeDao.commitTransaction();
            resp.setSuccessMessage("Import thành công!");
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Import thất bại - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }
}
