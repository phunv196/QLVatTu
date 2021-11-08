package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.*;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.ImportFileExcell;
import com.app.dao.base.converter.DynamicExport;
import com.app.model.BaseResponse;
import com.app.model.ExportModel;
import com.app.model.species.SpeciesModel;
import com.app.model.supplier.SupplierModel;
import com.app.model.supplies.SuppliesModel;
import com.app.model.supplies.SuppliesModel.SuppliesResponse;
import com.app.model.quality.QualityModel;
import com.app.model.unit.UnitModel;
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
import static com.app.util.Constants.COMMON.FOLDER_IMPORT;

@Path("supplies")
@Tag(name = "Supplies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SuppliesController extends BaseController {

    SuppliesDao suppliesDao = new SuppliesDao();
    SpeciesDao speciesDao = new SpeciesDao();
    QualityDao qualityDao = new QualityDao();
    SupplierDao supplierDao = new SupplierDao();
    UnitDao unitDao = new UnitDao();

    @GET
    @Path("{suppliesId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getSupplierById(
            @Parameter(description="Supplies Id", example="601") @PathParam("suppliesId") Long suppliesId
    ) {
        SuppliesModel model = suppliesDao.getById(suppliesId);
        return Response.ok(model).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SuppliesResponse.class)))}
    )
    public Response getSuppliesList(
            @Parameter(description = "Order Id") @QueryParam("suppliesId") Long suppliesId,
            @Parameter(description = "Order Id") @QueryParam("searchCode") String searchCode,
            @Parameter(description = "Order Id") @QueryParam("searchName") String searchName,
            @Parameter(description = "Order Id") @QueryParam("searchSupplier") Long searchSupplier,
            @Parameter(description = "Order Id") @QueryParam("searchSpecies") Long searchSpecies,
            @Parameter(description = "Order Id") @QueryParam("searchFormPrice") Long searchFormPrice,
            @Parameter(description = "Order Id") @QueryParam("searchToPrice") Long searchToPrice,
            @Parameter(description = "Order Id") @QueryParam("searchQuality") Long searchQuality,
            @Parameter(description = "Order Id") @QueryParam("searchUnit") Long searchUnit,
            @Parameter(description = "Page No, Starts from 1 ", example = "1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description = "Items in each page", example = "20") @DefaultValue("10") @QueryParam("page-size") int pageSize
    ) {

        SuppliesModel.SuppliesResponse resp = new SuppliesModel.SuppliesResponse();
        if (suppliesId == null) {
            suppliesId = 0l;
        }
        if (searchSupplier == null) {
            searchSupplier = 0l;
        }
        if (searchSpecies == null) {
            searchSpecies = 0l;
        }
        if (searchQuality == null) {
            searchQuality = 0l;
        }
        if (searchFormPrice == null) {
            searchFormPrice = 0l;
        }
        if (searchToPrice == null) {
            searchToPrice = 0l;
        }
        if (searchUnit == null) {
            searchUnit = 0l;
        }
        List<SuppliesModel> modelList = suppliesDao.getList(page, pageSize, suppliesId, searchCode, searchName,
                searchSupplier, searchSpecies, searchFormPrice, searchToPrice, searchQuality, searchUnit);
        BigInteger total = suppliesDao.getSuppliesCount(suppliesId, searchCode, searchName, searchSupplier, searchSpecies,
                searchFormPrice, searchToPrice, searchQuality, searchUnit);
        resp.setList(modelList);
        resp.setTotal(total.intValue());
        resp.setPageStats(total.intValue(),pageSize, page,"");
        resp.setSuccessMessage("List of Supplies and nested details " + (suppliesId>0 ? "- Customer:"+suppliesId:""));
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SuppliesResponse.class)))}
    )
    public Response getAll(

    ) {
        Criteria criteria = suppliesDao.createCriteria(SuppliesModel.class);
        criteria.setProjection(null);
        List<SuppliesModel> suppliesList = criteria.list();
        SuppliesResponse resp = new SuppliesResponse();
        resp.setList(suppliesList);
        resp.setSuccessMessage("List of suppliess");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addSupplies(SuppliesModel supplies) {
        BaseResponse resp = new BaseResponse();
        try {
            suppliesDao.beginTransaction();
            suppliesDao.save(supplies);
            suppliesDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công code: %s ", supplies.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateSupplies(SuppliesModel supplies) {
        BaseResponse resp = new BaseResponse();
        try {
            SuppliesModel foundProd  = suppliesDao.getById(supplies.getSuppliesId());
            if (foundProd != null) {
                suppliesDao.beginTransaction();
                suppliesDao.update(supplies);
                suppliesDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", supplies.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", supplies.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{suppliesId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteSupplies(@Parameter(description="Supplies Id", example="601") @PathParam("suppliesId") Long suppliesId) {
        BaseResponse resp = new BaseResponse();
        try {
            SuppliesModel foundProd  = suppliesDao.getById(suppliesId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", suppliesId));
                return Response.ok(resp).build();
            } else {
                suppliesDao.beginTransaction();
                suppliesDao.delete(suppliesId);
                suppliesDao.commitTransaction();
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
            SuppliesModel supplies
    ) {
        Criteria criteria = suppliesDao.createCriteria(SuppliesModel.class);
        if (supplies.getSuppliesId() != null){
            criteria.add(Restrictions.ne("suppliesId", supplies.getSuppliesId()));
        }
        if (!CommonUtils.isNullOrEmpty(supplies.getCode())){
            criteria.add(Restrictions.eq("code", supplies.getCode()).ignoreCase());
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
            SuppliesModel supplies
    ) throws Exception {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        String fileName = "danh_sach_vat_tu.xls";
        Integer startDataRow = 6;
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_EXPORT_EXCELL + fileName), startDataRow, false);
        List<SuppliesModel> models = suppliesDao.getListExport(supplies.getCode(), supplies.getName(), supplies.getSupplierId(),
                supplies.getFormPrice(), supplies.getToPrice(), supplies.getQualityId(), supplies.getUnitId());
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                SuppliesModel model = models.get(i);
                int index = 0;
                dynamicExport.increaseRow();
                dynamicExport.setEntry(stt++, index++);
                dynamicExport.setText(model.getCode(), index++);
                dynamicExport.setText(model.getName(), index++);
                dynamicExport.setText(model.getSpeciesName(), index++);
                dynamicExport.setText(model.getSupplierName(), index++);
                dynamicExport.setText(model.getQualityName(), index++);
                dynamicExport.setText(model.getUnitName(), index++);
                dynamicExport.setText(model.getPrice() == null ? "" : model.getPrice().toString(), index++);
            }
        }
        dynamicExport.setCellFormat(startDataRow, 0, dynamicExport.getLastRow(), 7, DynamicExport.BORDER_FORMAT);
        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = FOLDER_EXPORT + prefixOutPutFile +  "danh_sach_vat_tu";
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
        String fileName = "BM_Nhap_Moi_Vat_Tu.xls";
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_IMPORT_EXCELL + fileName), 6, false);
        dynamicExport.setActiveSheet(1);
        List<SpeciesModel> listSpeciesModel = speciesDao.getAll(SpeciesModel.class, "speciesId");
        int rows = 2;
        for (SpeciesModel model : listSpeciesModel) {
            dynamicExport.setEntry(String.valueOf(rows-1), 0, rows);
            dynamicExport.setText(model.getCode(), 1, rows);
            dynamicExport.setText(model.getName(), 2, rows);
            rows++;
        }
        dynamicExport.setCellFormat(0, 0, rows-1, 2, DynamicExport.BORDER_FORMAT);

        List<QualityModel> listQualityModel = qualityDao.getAll(QualityModel.class, "qualityId");
        rows = 2;
        for (QualityModel model : listQualityModel) {
            dynamicExport.setEntry(String.valueOf(rows-1), 4, rows);
            dynamicExport.setText(model.getCode(), 5, rows);
            dynamicExport.setText(model.getName(), 6, rows);
            rows++;
        }
        dynamicExport.setCellFormat(0, 4, rows-1, 6, DynamicExport.BORDER_FORMAT);

        List<SupplierModel> listSupplierModel = supplierDao.getAll(SupplierModel.class, "supplierId");
        rows = 2;
        for (SupplierModel model : listSupplierModel) {
            dynamicExport.setEntry(String.valueOf(rows-1), 8, rows);
            dynamicExport.setText(model.getCode(), 9, rows);
            dynamicExport.setText(model.getName(), 10, rows);
            rows++;
        }
        dynamicExport.setCellFormat(0, 8, rows-1, 10, DynamicExport.BORDER_FORMAT);

        List<UnitModel> listUnitModel = unitDao.getAll(UnitModel.class, "unitId");
        rows = 2;
        for (UnitModel model : listUnitModel) {
            dynamicExport.setEntry(String.valueOf(rows-1), 12, rows);
            dynamicExport.setText(model.getCode(), 13, rows);
            dynamicExport.setText(model.getName(), 14, rows);
            rows++;
        }
        dynamicExport.setCellFormat(0, 12, rows-1, 14, DynamicExport.BORDER_FORMAT);

        String fileExport = FOLDER_EXPORT_TEMPLATE + "BM_Nhap_Moi_Vat_Tu";
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
        List<SuppliesModel> suppliesModels = new ArrayList<>();
        List<SuppliesModel> suppliesModelList = suppliesDao.getAll(SuppliesModel.class, "suppliesId");
        List<String> suppliesCodes = new ArrayList<>();
        suppliesModelList.forEach(element -> {
            suppliesCodes.add(element.getCode());
        });
        List<SpeciesModel> listSpeciesModel = speciesDao.getAll(SpeciesModel.class, "speciesId");
        Map<String, Long> mapSpecies = new HashMap<>();
        listSpeciesModel.forEach(element -> {
            if(!CommonUtils.isNullOrEmpty(element.getCode())){
                mapSpecies.put(element.getCode().toLowerCase(), element.getSpeciesId());
            }
        });
        List<QualityModel> listQualityModel = qualityDao.getAll(QualityModel.class, "qualityId");
        Map<String, Long> mapQuality = new HashMap<>();
        listQualityModel.forEach(element -> {
            if(!CommonUtils.isNullOrEmpty(element.getCode())){
                mapQuality.put(element.getCode().toLowerCase(), element.getQualityId());
            }
        });
        List<SupplierModel> listSupplierModel = supplierDao.getAll(SupplierModel.class, "supplierId");
        Map<String, Long> mapSupplier = new HashMap<>();
        listSupplierModel.forEach(element -> {
            if(!CommonUtils.isNullOrEmpty(element.getCode())){
                mapSupplier.put(element.getCode().toLowerCase(), element.getSupplierId());
            }
        });

        List<UnitModel> listUnitModel = supplierDao.getAll(UnitModel.class, "unitId");
        Map<String, Long> mapUnit = new HashMap<>();
        listUnitModel.forEach(element -> {
            mapUnit.put(element.getCode().toLowerCase(), element.getUnitId());
            if(!CommonUtils.isNullOrEmpty(element.getCode())){
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
                SuppliesModel suppliesModel = new SuppliesModel();
                int column = 1;
                String code = (String) objects[column++];
                String name = (String) objects[column++];
                String speciesCode = (String) objects[column++];
                String qualityCode = (String) objects[column++];
                String supplierCode = (String) objects[column++];
                String unitCode = (String) objects[column++];
                String price = (String) objects[column++];
                String description = (String) objects[column++];
                column = 1;
                if (CommonUtils.isNullOrEmpty(code)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if (suppliesCodes.contains(code.toLowerCase())) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.DUOLICATE, (String) objects[column]));
                    } else {
                        suppliesCodes.add(code.toLowerCase());
                        suppliesModel.setCode(code);
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(name)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    suppliesModel.setName(name);
                }
                column++;
                if(!mapSpecies.containsKey(speciesCode.toLowerCase())) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, "Mã chủng loại phải nhập theo dữ liệu cho trước!", (String) objects[column]));
                } else {
                    suppliesModel.setSpeciesId(mapSpecies.get(speciesCode.toLowerCase()));
                }
                column++;
                if(!mapQuality.containsKey(qualityCode.toLowerCase())) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, "Mã chất lượng phải nhập theo dữ liệu cho trước!", (String) objects[column]));
                } else {
                    suppliesModel.setQualityId(mapQuality.get(qualityCode.toLowerCase()));
                }
                column++;
                if (CommonUtils.isNullOrEmpty(supplierCode)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if(!mapSupplier.containsKey(supplierCode.toLowerCase())) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, "Mã nhà cung cấp phải nhập theo dữ liệu cho trước!", (String) objects[column]));
                    } else {
                        suppliesModel.setSupplierId(mapSupplier.get(supplierCode.toLowerCase()));
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(unitCode)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if(!mapUnit.containsKey(unitCode.toLowerCase())) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, "Đơn vị tính phải nhập theo dữ liệu cho trước!", (String) objects[column]));
                    } else {
                        suppliesModel.setUnitId(mapUnit.get(unitCode.toLowerCase()));
                    }
                }
                column++;
                if (CommonUtils.isNullOrEmpty(price)) {
                    errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, Constants.Error.NULL_OR_ENITY, (String) objects[column]));
                } else {
                    if(CommonUtils.convertStringToLong(price) == null) {
                        errorList.add(new ImportFileExcell.ImportErrorBean(importBean.getRows().get(row), column, "Đơn giá phải nhập dạng số nguyên!", (String) objects[column]));
                    } else {
                        suppliesModel.setPrice(CommonUtils.convertStringToLong(price));
                    }
                }
                suppliesModel.setDescription(description);
                suppliesModels.add(suppliesModel);
            }
            if (errorList.size() > 0) {
                return Response.ok(errorList).build();
            }
            suppliesDao.beginTransaction();
            suppliesDao.saveOrUpdateAll(suppliesModels);
            suppliesDao.commitTransaction();
            resp.setSuccessMessage("Import thành công!");
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Import thất bại - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }
}
