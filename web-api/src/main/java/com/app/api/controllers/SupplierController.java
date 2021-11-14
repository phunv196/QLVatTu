package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.ImportFileExcell;
import com.app.dao.base.converter.DynamicExport;
import com.app.dao.SupplierDao;
import com.app.model.BaseResponse;
import com.app.model.ExportModel;
import com.app.model.supplier.SupplierModel;
import com.app.model.supplier.SupplierModel.SupplierResponse;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.app.util.Constants.COMMON.*;
import static com.app.util.Constants.COMMON.FOLDER_IMPORT;

@Path("supplier")
@Tag(name = "Supplier")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SupplierController extends BaseController {

    SupplierDao supplierDao = new SupplierDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SupplierResponse.class)))}
    )
    public Response getSupplierList(
            @Parameter(description="Supplier Id") @QueryParam("supplierId") int supplierId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="code", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="email", example="nikon%") @QueryParam("email") String email,
            @Parameter(description="phone", example="nikon%") @QueryParam("phone") String phone,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = supplierDao.createCriteria(SupplierModel.class);

        if (supplierId > 0){
            criteria.add(Restrictions.eq("supplierId",  supplierId ));
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

        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();

        // Execute the Main Query
        criteria.setProjection(null);
        criteria.setFirstResult( (int) (long)recordFrom);
        criteria.setMaxResults(  (int) (long)pageSize);
        List<SupplierModel> supplierList = criteria.list();

        SupplierResponse resp = new SupplierResponse();
        resp.setList(supplierList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of suppliers");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SupplierResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = supplierDao.createCriteria(SupplierModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<SupplierModel> supplierList = criteria.list();

        SupplierResponse resp = new SupplierResponse();
        resp.setList(supplierList);
        resp.setSuccessMessage("List of suppliers");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addSupplier(SupplierModel supplier) {
        BaseResponse resp = new BaseResponse();
        try {
            supplierDao.beginTransaction();
            supplierDao.save(supplier);
            supplierDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công code: %s ", supplier.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateSupplier(SupplierModel supplier) {
        BaseResponse resp = new BaseResponse();
        try {
            SupplierModel foundProd  = supplierDao.getById(supplier.getSupplierId());
            if (foundProd != null) {
                supplierDao.beginTransaction();
                supplierDao.update(supplier);
                supplierDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", supplier.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", supplier.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{supplierId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteSupplier(@Parameter(description="Supplier Id", example="601") @PathParam("supplierId") Long supplierId) {
        BaseResponse resp = new BaseResponse();
        try {
            SupplierModel foundProd  = supplierDao.getById(supplierId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", supplierId));
                return Response.ok(resp).build();
            } else {
                supplierDao.beginTransaction();
                supplierDao.delete(supplierId);
                supplierDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (code:%s)", foundProd.getCode()));
                return Response.ok(resp).build();
            }
//            }
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
            SupplierModel supplier
    ) {
        Criteria criteria = supplierDao.createCriteria(SupplierModel.class);
        if (supplier.getSupplierId() != null){
            criteria.add(Restrictions.ne("supplierId", supplier.getSupplierId()));
        }
        if (!CommonUtils.isNullOrEmpty(supplier.getCode())){
            criteria.add(Restrictions.eq("code", supplier.getCode()).ignoreCase());
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
            SupplierModel supplier
    ) throws Exception {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        String fileName = "danh_sach_nha_cung_cap.xls";
        Integer startDataRow = 6;
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_EXPORT_EXCELL + fileName), startDataRow, false);
        List<SupplierModel> models = supplierDao.getListExport(supplier.getCode(), supplier.getName(), supplier.getEmail(),
                supplier.getPhone());
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                SupplierModel model = models.get(i);
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
        String fileExport = FOLDER_EXPORT + prefixOutPutFile +  "danh_sach_nha_cung_cap";
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
        String fileName = "BM_Nhap_Moi_Nha_Cung_Cap.xls";
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
        List<SupplierModel> supplierModels = new ArrayList<>();
        List<SupplierModel> supplierModelList = supplierDao.getAll(SupplierModel.class, "supplierId");
        List<String> supplierCodes = new ArrayList<>();
        supplierModelList.forEach(element -> {
            supplierCodes.add(element.getCode());
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
                SupplierModel supplierModel = new SupplierModel();
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
                    if (supplierCodes.contains(code.toLowerCase())) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.DUOLICATE, (String) objects[column]));
                    } else {
                        supplierCodes.add(code.toLowerCase());
                        supplierModel.setCode(code);
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(name)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    supplierModel.setName(name);
                }
                column++;
                if (CommonUtils.isNullOrEmpty(email)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (CommonUtils.isValidEmailAddress(email)) {
                        supplierModel.setEmail(email);
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
                        supplierModel.setPhone(phone);
                    }
                }
                supplierModel.setAddress(address);
                supplierModel.setDescription(description);
                supplierModels.add(supplierModel);
            }
            if (errorList.size() > 0) {
                return Response.ok(errorList).build();
            }
            supplierDao.beginTransaction();
            supplierDao.saveOrUpdateAll(supplierModels);
            supplierDao.commitTransaction();
            resp.setSuccessMessage("Import thành công!");
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Import thất bại - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }
}
