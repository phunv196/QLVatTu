package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.EmployeeDao;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.converter.DynamicExport;
import com.app.dao.category.*;
import com.app.model.BaseResponse;
import com.app.model.ExportModel;
import com.app.model.category.*;
import com.app.model.category.WarehouseCardModel.WarehouseCardResponse;
import com.app.model.employee.EmployeeModel;
import com.app.model.user.UserModel;
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
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.app.util.Constants.COMMON.*;
import static com.app.util.Constants.COMMON.FOLDER_EXPORT_DOCX;

@Path("warehouse_cards")
@Tag(name = "WarehouseCards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarehouseCardController extends BaseController {
    WarehouseCardDao warehouseCardDao = new WarehouseCardDao();
    WarehouseCardFlowDao warehouseCardFlowDao = new WarehouseCardFlowDao();
    SuppliesDao suppliesDao = new SuppliesDao();
    DepartmentDao departmentDao = new DepartmentDao();
    EmployeeDao employeeDao = new EmployeeDao();
    UnitDao unitDao = new UnitDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of warehouseCards",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseCardResponse.class)))}
    )
    public Response getWarehouseCardList(
            @Parameter(description="WarehouseCard Id") @QueryParam("warehouseCardId") Long warehouseCardId,
            @Parameter(description = "Order Id") @QueryParam("searchCode") String searchCode,
            @Parameter(description = "Order Id") @QueryParam("searchName") String searchName,
            @Parameter(description = "Order Id") @QueryParam("searchEmployee") Long searchEmployee,
            @Parameter(description = "Order Id") @QueryParam("searchWarehouse") Long searchWarehouse,
            @Parameter(description = "Order Id") @QueryParam("searchFormDate") String searchFormDate,
            @Parameter(description = "Order Id") @QueryParam("searchToDate") String searchToDate,
            @Parameter(description = "Order Id") @QueryParam("searchSupplies") Long searchSupplies,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        WarehouseCardResponse resp = new WarehouseCardResponse();
        if (warehouseCardId == null) {
            warehouseCardId = 0l;
        }
        if (searchEmployee == null) {
            searchEmployee = 0l;
        }

        if (searchWarehouse == null) {
            searchWarehouse = 0l;
        }
        if (searchSupplies == null) {
            searchSupplies = 0l;
        }

        List<WarehouseCardModel> cardModels = warehouseCardDao.getList(page, pageSize, warehouseCardId, searchCode,
                searchName,
                searchEmployee,
                searchWarehouse,
                searchFormDate,
                searchToDate, searchSupplies);
        BigInteger total = warehouseCardDao.getWarehouseCardCount(warehouseCardId,  searchCode,
                searchName,
                searchEmployee,
                searchWarehouse,
                searchFormDate,
                searchToDate, searchSupplies);
        resp.setList(cardModels);
        resp.setTotal(total.intValue());
        resp.setPageStats(total.intValue(),pageSize, page,"");
        resp.setSuccessMessage("List of receipt and nested details " + (warehouseCardId >0 ? "- Customer:" + warehouseCardId:""));
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a warehouseCard",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addWarehouseCard(WarehouseCardModel warehouseCard) {
        BaseResponse resp = new BaseResponse();
        try {
            UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();
            warehouseCard.setEmployeeId(Long.valueOf(userFromToken.getEmployeeId()));
            warehouseCardDao.beginTransaction();
            warehouseCardDao.save(warehouseCard);
            warehouseCardDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công code: %s ", warehouseCard.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a WarehouseCard",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateWarehouseCard(WarehouseCardModel warehouseCard) {
        BaseResponse resp = new BaseResponse();
        try {
            WarehouseCardModel foundProd  = warehouseCardDao.getById(warehouseCard.getWarehouseCardId());
            if (foundProd != null) {
                UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();
                warehouseCard.setEmployeeId(Long.valueOf(userFromToken.getEmployeeId()));
                warehouseCardDao.beginTransaction();
                warehouseCardDao.update(warehouseCard);
                warehouseCardDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", warehouseCard.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", warehouseCard.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{warehouseCardId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a WarehouseCard",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteWarehouseCard(@Parameter(description="WarehouseCard Id", example="601") @PathParam("warehouseCardId") Long warehouseCardId) {
        BaseResponse resp = new BaseResponse();
        try {
            WarehouseCardModel foundProd  = warehouseCardDao.getById(warehouseCardId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", warehouseCardId));
                return Response.ok(resp).build();
            } else {
                warehouseCardDao.beginTransaction();
                warehouseCardDao.delete(warehouseCardId);
                warehouseCardDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (code:%s)", foundProd.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể xóa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("sequence")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "get sequenceId a WarehouseCard",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getSequence() throws Exception {
        Long id = warehouseCardDao.getSequence();
        return Response.ok(id == null ? 1 : id).build();
    }

    @GET
    @Path("equal/{warehouseCardId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Check receiptId a receipt",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getEqualId(@Parameter(description="Receipt Id", example="601") @PathParam("warehouseCardId") Long warehouseCardId) {
        boolean check = false;
        Criteria criteria = warehouseCardDao.createCriteria(WarehouseCardModel.class);
        // Execute the Main Query
        if (warehouseCardId > 0){
            criteria.add(Restrictions.eq("warehouseCardId",  warehouseCardId ));
        }
        criteria.setProjection(null);
        List<ReceiptModel> deliveryBillList = criteria.list();
        if (deliveryBillList.size() > 0) {
            check = true;
        }
        return Response.ok(check).build();
    }

    @DELETE
    @Path("delete_by_id/{warehouseCardId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    public void deleteByWarehouseCardId(@Parameter(description="warehouseCard Id", example="601") @PathParam("warehouseCardId") Long warehouseCardId) {
        warehouseCardId = warehouseCardId == null ? 0 : warehouseCardId;
        warehouseCardFlowDao.beginTransaction();
        warehouseCardFlowDao.deleteByWarehouseCardId(warehouseCardId);
        warehouseCardFlowDao.commitTransaction();
    }

    @POST
    @Path("byCode")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseCardModel.class)))}
    )
    public Response getByCode(
            WarehouseCardModel model
    ) {
        Criteria criteria = warehouseCardDao.createCriteria(WarehouseCardModel.class);
        if (model.getWarehouseCardId() != null){
            criteria.add(Restrictions.ne("warehouseCardId", model.getWarehouseCardId()));
        }
        if (!CommonUtils.isNullOrEmpty(model.getCode())){
            criteria.add(Restrictions.eq("code", model.getCode()).ignoreCase());
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
            WarehouseCardModel warehouseCard
    ) throws Exception {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        String fileName = "danh_sach_the_kho.xls";
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_EXPORT_EXCELL + fileName), 6, false);
        List<WarehouseCardModel> models = warehouseCardDao.getListExport(warehouseCard.getCode(), warehouseCard.getName(), warehouseCard.getEmployeeId(),
                warehouseCard.getWarehouseId(), warehouseCard.getSuppliesId(), warehouseCard.getFormDate(), warehouseCard.getToDate());
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                WarehouseCardModel model = models.get(i);
                int index = 0;
                dynamicExport.increaseRow();
                dynamicExport.setEntry(stt++, index++);
                dynamicExport.setText(model.getCode(), index++);
                dynamicExport.setText(model.getName(), index++);
                dynamicExport.setText(model.getFullName(), index++);
                dynamicExport.setText(CommonUtils.convertDateToString(model.getDateCreated()), index++);
                dynamicExport.setText(model.getSuppliesCode(), index++);
                dynamicExport.setText(model.getSuppliesName(), index++);
                dynamicExport.setText(model.getWarehouseCode(), index++);
                dynamicExport.setText(model.getWarehouseName(), index++);
                dynamicExport.setText(model.getCountDeliveryBill() == null ? "" : model.getCountDeliveryBill().toString(), index++);
                dynamicExport.setText(model.getCountReceipt() == null ? "" : model.getCountReceipt().toString(), index++);
                dynamicExport.setText(model.getAmountDeliveryBill() == null ? "" : model.getAmountDeliveryBill().toString(), index++);
                dynamicExport.setText(model.getAmountReceipt() == null ? "" : model.getAmountReceipt().toString(), index++);
                dynamicExport.setText(model.getAmountInventory() == null ? "" : model.getAmountInventory().toString(), index++);
            }
        }
        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = FOLDER_EXPORT + prefixOutPutFile +  "danh_sach_the_kho";
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
    @Path("downloadFileDocx/{warehouseCardId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a deliveryBill",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response downloadFileDocx(@Parameter(description = "DeliveryBill Id", example = "601") @PathParam("warehouseCardId") Long warehouseCardId) throws IOException {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        //lay ra user dang dang nhap -> employeeId -> department
        UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();
        EmployeeModel employeeModel = employeeDao.getById(Long.valueOf(userFromToken.getEmployeeId()));
        DepartmentModel departmentModel = departmentDao.getById(employeeModel.getDepartmentId());

        WarehouseCardModel warehouseCardModel = warehouseCardDao.getById(warehouseCardId);
        SuppliesModel suppliesModel = suppliesDao.getById(warehouseCardModel.getSuppliesId());
        UnitModel unitModel = unitDao.getById(suppliesModel.getUnitId());
        List<WarehouseCardFlowModel> models = warehouseCardFlowDao.getByWarehouseCardId(warehouseCardId);
        Date date = new Date();
        String strDate = CommonUtils.convertDateToString(date);
        String[] arrDate = strDate.split("/");
        File fileDocx = new File(TEMPLATE_EXPORT_DOCX + "BM_The_Kho.docx");
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_";

        Map<String, String> map = new HashMap<>();
        map.put("departmentName", departmentModel == null ? " " : departmentModel.getName());
        map.put("departmentAddress", departmentModel == null ? " " : departmentModel.getAddress());
        map.put("day", arrDate[0]);
        map.put("month", arrDate[1]);
        map.put("year", arrDate[2]);
        map.put("strDate", CommonUtils.convertDateToString(warehouseCardModel.getDateCreated()));
        map.put("supplies", suppliesModel.getCode() + " - " + suppliesModel.getName());
        map.put("unitName", unitModel == null ? " " : unitModel.getName());
        map.put("warehouseCardCode", warehouseCardModel.getCode());

        try (InputStream is = new FileInputStream(fileDocx);
             XWPFDocument doc = new XWPFDocument(is)) {
            Iterator<IBodyElement> docElementsIterator = doc.getBodyElementsIterator();
            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();
            for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
                CommonUtils.replaceParagraph(xwpfParagraph, map);
                IBodyElement docElement = docElementsIterator.next();
                if ("TABLE".equalsIgnoreCase(docElement.getElementType().name())) {
                    //Get List of table and iterate it
                    List<XWPFTable> xwpfTableList = docElement.getBody().getTables();
                    for (XWPFTable xwpfTable : xwpfTableList) {
                        int index = 1;
                        int row = 2;
                        Long sumReceipt = 0L;
                        Long sumDeliveryBill = 0L;
                        Long inventory = 0L;
                        for (int i = 0; i < models.size(); i++) {
                            WarehouseCardFlowModel model = models.get(i);
                            int col = 0;
                            xwpfTable.createRow();
                            xwpfTable.getRow(row).getCell(col++).setText(String.valueOf(index++));
                            xwpfTable.getRow(row).getCell(col++).setText(CommonUtils.convertDateToString(model.getCreateAt()));
                            xwpfTable.getRow(row).getCell(col++).setText(model.getReceiptCode());
                            xwpfTable.getRow(row).addNewTableCell();
                            xwpfTable.getRow(row).getCell(col++).setText(model.getDeliveryBillCode());
                            xwpfTable.getRow(row).getCell(col++).setText(model.getDescription());
                            xwpfTable.getRow(row).getCell(col++).setText(CommonUtils.convertDateToString(model.getDate()));
                            xwpfTable.getRow(row).getCell(col++).setText( model.getReceiptId() != null ? String.valueOf(model.getAmount()) : "");
                            xwpfTable.getRow(row).addNewTableCell();
                            xwpfTable.getRow(row).getCell(col++).setText( model.getDeliveryBillId() != null ? String.valueOf(model.getAmount()) : "");
                            xwpfTable.getRow(row).addNewTableCell();
                            if (model.getReceiptId() != null) {
                                inventory += model.getAmount();
                                sumReceipt += model.getAmount();
                            } else {
                                inventory -= model.getAmount();
                                sumDeliveryBill += model.getAmount();
                            }
                            xwpfTable.getRow(row).getCell(col++).setText(String.valueOf(inventory));
                            row++;
                        }
                        xwpfTable.createRow();
                        xwpfTable.getRow(row).getCell(1).setText("Tổng cộng:");
                        xwpfTable.getRow(row).addNewTableCell();
                        xwpfTable.getRow(row).addNewTableCell();
                        xwpfTable.getRow(row).addNewTableCell();
                        xwpfTable.getRow(row).getCell(6).setText(String.valueOf(sumReceipt));
                        xwpfTable.getRow(row).getCell(7).setText(String.valueOf(sumDeliveryBill));
                        xwpfTable.getRow(row).getCell(8).setText(String.valueOf(sumReceipt - sumDeliveryBill));
                    }
                }
            }
            try (FileOutputStream out = new FileOutputStream(FOLDER_EXPORT_DOCX + prefixOutPutFile + "The_Kho.docx")) {
                doc.write(out);
            }
        }
        File file = new File(FOLDER_EXPORT_DOCX + prefixOutPutFile + "The_Kho.docx");
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        ExportModel exportModel = new ExportModel();
        exportModel.setFileName(prefixOutPutFile + "The_Kho.docx");
        exportModel.setData(encodedString);
        resp.setData(exportModel);
        return Response.ok(resp).build();
    }
}
