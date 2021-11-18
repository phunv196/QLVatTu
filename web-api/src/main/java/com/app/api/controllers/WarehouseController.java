package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.ImportFileExcell;
import com.app.dao.base.converter.DynamicExport;
import com.app.dao.WarehouseDao;
import com.app.model.BaseResponse;
import com.app.model.ExportModel;
import com.app.model.warehouse.WarehouseModel;
import com.app.model.warehouse.WarehouseModel.WarehouseResponse;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static com.app.util.Constants.COMMON.*;
import static com.app.util.Constants.COMMON.FOLDER_IMPORT;

@Path("warehouses")
@Tag(name = "Warehouses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarehouseController extends BaseController {

    WarehouseDao warehouseDao = new  WarehouseDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of warehouses",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseResponse.class)))}
    )
    public Response getWarehouseList(
            @Parameter(description="Warehouse Id") @QueryParam("warehouseId") int warehouseId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="code", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="email", example="nikon%") @QueryParam("email") String email,
            @Parameter(description="phone", example="nikon%") @QueryParam("phone") String phone,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = warehouseDao.createCriteria(WarehouseModel.class);

        if (warehouseId > 0){
            criteria.add(Restrictions.eq("warehouseId",  warehouseId ));
        }
        if (StringUtils.isNotBlank(name)){
            criteria.add(Restrictions.like("name", "%"+name+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(code)){
            criteria.add(Restrictions.like("code", "%"+code+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(email)){
            criteria.add(Restrictions.like("email", "%"+email+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(phone)){
            criteria.add(Restrictions.like("phone", "%"+phone+"%" ).ignoreCase());
        }
        if (page<=0){ page = 1; }
        if (pageSize <= 0 || pageSize > 1000){ pageSize = 20; }
        recordFrom = (page-1) * pageSize;

        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();

        criteria.setProjection(null);
        criteria.setFirstResult( (int) (long)recordFrom);
        criteria.setMaxResults(  (int) (long)pageSize);
        List<WarehouseModel> warehouseList = criteria.list();
        WarehouseResponse resp = new WarehouseResponse();
        resp.setList(warehouseList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of warehouse");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all warehouses",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseResponse.class)))}
    )
    public Response getWarehouseList(
    ) {
        Criteria criteria = warehouseDao.createCriteria(WarehouseModel.class);
        criteria.setProjection(null);
        List<WarehouseModel> warehouseList = criteria.list();
        WarehouseResponse resp = new WarehouseResponse();
        resp.setList(warehouseList);
        resp.setSuccessMessage("List of warehouse");
        return Response.ok(resp).build();
    }

    @GET
    @Path("by-receipt")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all warehouses",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseResponse.class)))}
    )
    public Response getByRecepit(
    ) {
        List<WarehouseModel> warehouseList = warehouseDao.getByRecepit();
        WarehouseResponse resp = new WarehouseResponse();
        resp.setList(warehouseList);
        resp.setSuccessMessage("List of warehouse");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a warehouse",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addWarehouse(WarehouseModel warehouse) {
        BaseResponse resp = new BaseResponse();
        try {
            warehouseDao.beginTransaction();
            warehouseDao.save(warehouse);
            warehouseDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công code: %s ", warehouse.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Warehouse",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateWarehouse(WarehouseModel warehouse) {
        BaseResponse resp = new BaseResponse();
        try {
            WarehouseModel foundProd  = warehouseDao.getById(warehouse.getWarehouseId());
            if (foundProd != null) {
                warehouseDao.beginTransaction();
                warehouseDao.update(warehouse);
                warehouseDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", warehouse.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", warehouse.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{warehouseId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Warehouse",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteWarehouse(@Parameter(description="Warehouse Id", example="601") @PathParam("warehouseId") Long warehouseId) {
        BaseResponse resp = new BaseResponse();
        try {
            WarehouseModel foundProd  = warehouseDao.getById(warehouseId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", warehouseId));
                return Response.ok(resp).build();
            } else {
                warehouseDao.beginTransaction();
                warehouseDao.delete(warehouseId);
                warehouseDao.commitTransaction();
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
            WarehouseModel warehouseModel
    ) {
        Criteria criteria = warehouseDao.createCriteria(WarehouseModel.class);
        if (warehouseModel.getWarehouseId() != null){
            criteria.add(Restrictions.ne("warehouseId", warehouseModel.getWarehouseId()));
        }
        if (!CommonUtils.isNullOrEmpty(warehouseModel.getCode())){
            criteria.add(Restrictions.eq("code", warehouseModel.getCode()).ignoreCase());
        }
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
            WarehouseModel warehouse
    ) throws Exception {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        String fileName = "danh_sach_kho.xls";
        Integer startDataRow = 6;
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_EXPORT_EXCELL + fileName), startDataRow, false);
        List<WarehouseModel> models = warehouseDao.getListExport(warehouse.getCode(), warehouse.getName(), warehouse.getEmail(),
                warehouse.getPhone());
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                WarehouseModel model = models.get(i);
                int index = 0;
                dynamicExport.increaseRow();
                dynamicExport.setEntry(stt++, index++);
                dynamicExport.setText(model.getCode(), index++);
                dynamicExport.setText(model.getName(), index++);
                dynamicExport.setText(model.getEmail(), index++);
                dynamicExport.setText(model.getPhone(), index++);
                dynamicExport.setText(model.getAddress(), index++);
            }
        }
        dynamicExport.setCellFormat(startDataRow, 0, dynamicExport.getLastRow(), 5, DynamicExport.BORDER_FORMAT);
        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = FOLDER_EXPORT + prefixOutPutFile +  "danh_sach_kho";
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
        String fileName = "BM_Nhap_Moi_Kho_Hang.xls";
        File file = new File(TEMPLATE_IMPORT_EXCELL + fileName);
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        ExportModel exportModel = new ExportModel();
        exportModel.setFileName(fileName);
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
        List<WarehouseModel> warehouseModels = new ArrayList<>();
        List<WarehouseModel> warehouseModelList = warehouseDao.getAll(WarehouseModel.class, "warehouseId");
        List<String> warehouseCodes = new ArrayList<>();
        warehouseModelList.forEach(element -> {
            warehouseCodes.add(element.getCode());
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
                WarehouseModel warehouseModel = new WarehouseModel();
                int column = 1;
                String code = (String) objects[column++];
                String name = (String) objects[column++];
                String email = (String) objects[column++];
                String phone = (String) objects[column++];
                String address = (String) objects[column++];
                String description = (String) objects[column++];
                column = 1;
                if (CommonUtils.isNullOrEmpty(code)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (warehouseCodes.contains(code.toLowerCase())) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.DUOLICATE, (String) objects[column]));
                    } else {
                        warehouseCodes.add(code.toLowerCase());
                        warehouseModel.setCode(code);
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(name)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    warehouseModel.setName(name);
                }
                column++;
                if (CommonUtils.isNullOrEmpty(email)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (CommonUtils.isValidEmailAddress(email)) {
                        warehouseModel.setEmail(email);
                    } else {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.FOMAT, (String) objects[column]));
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(phone)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (!CommonUtils.containPhoneNumber(phone)) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.FOMAT, (String) objects[column]));
                    } else {
                        warehouseModel.setPhone(phone);
                    }
                }
                warehouseModel.setAddress(address);
                warehouseModel.setDescription(description);
                warehouseModels.add(warehouseModel);
            }
            if (errorList.size() > 0) {
                return Response.ok(errorList).build();
            }
            warehouseDao.beginTransaction();
            warehouseDao.saveOrUpdateAll(warehouseModels);
            warehouseDao.commitTransaction();
            resp.setSuccessMessage("Import thành công!");
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Import thất bại - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }
}
