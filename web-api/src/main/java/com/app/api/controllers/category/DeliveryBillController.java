package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.converter.DynamicExport;
import com.app.dao.category.DeliveryBillDao;
import com.app.dao.category.DeliveryBillFlowDao;
import com.app.dao.category.WarehouseDao;
import com.app.model.BaseResponse;
import com.app.model.ExportModel;
import com.app.model.category.DeliveryBillFlowModel;
import com.app.model.category.DeliveryBillModel;
import com.app.model.category.DeliveryBillModel.DeliveryBillResponse;
import com.app.model.category.WarehouseModel;
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

@Path("delivery_bills")
@Tag(name = "deliveryBills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryBillController extends BaseController {
    DeliveryBillDao deliveryBillDao = new DeliveryBillDao();
    DeliveryBillFlowDao deliveryBillFlowDao = new DeliveryBillFlowDao();
    WarehouseDao warehouseDao = new WarehouseDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of deliveryBills",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = DeliveryBillResponse.class)))}
    )
    public Response getList(@Parameter(description = "DeliveryBill Id") @QueryParam("deliveryBillId") Long deliveryBillId,
                            @Parameter(description = "Order Id") @QueryParam("searchCode") String searchCode,
                            @Parameter(description = "Order Id") @QueryParam("searchName") String searchName,
                            @Parameter(description = "Order Id") @QueryParam("searchEmployee") Long searchEmployee,
                            @Parameter(description = "Order Id") @QueryParam("searchWarehouse") Long searchWarehouse,
                            @Parameter(description = "Order Id") @QueryParam("searchFormDate") String searchFormDate,
                            @Parameter(description = "Order Id") @QueryParam("searchToDate") String searchToDate,
                            @Parameter(description = "Order Id") @QueryParam("searchFactory") Long searchFactory,
                            @Parameter(description = "Page No, Starts from 1 ", example = "1") @DefaultValue("1") @QueryParam("page") int page,
                            @Parameter(description = "Items in each page", example = "20") @DefaultValue("10") @QueryParam("page-size") int pageSize
    ) throws Exception {
        DeliveryBillResponse resp = new DeliveryBillResponse();
        try {
            if (deliveryBillId == null) {
                deliveryBillId = 0l;
            }

            if (searchEmployee == null) {
                searchEmployee = 0l;
            }

            if (searchWarehouse == null) {
                searchWarehouse = 0l;
            }

            if (searchFactory == null) {
                searchFactory = 0l;
            }
            List<DeliveryBillModel> billModelList = deliveryBillDao.getList(page, pageSize, deliveryBillId, searchCode,
                    searchName,
                    searchEmployee,
                    searchWarehouse,
                    searchFormDate,
                    searchToDate,
                    searchFactory);
            BigInteger total = deliveryBillDao.getDeliveryBillCount(deliveryBillId, searchCode,
                    searchName,
                    searchEmployee,
                    searchWarehouse,
                    searchFormDate,
                    searchToDate,
                    searchFactory);
            resp.setList(billModelList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(), pageSize, page, "");
            resp.setSuccessMessage("  " + (deliveryBillId > 0 ? "- Customer:" + deliveryBillId : ""));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Lỗi xảy ra - " + e.getMessage() + ", " + (e.getCause() != null ? e.getCause().getMessage() : ""));
            return Response.ok(resp).build();
        }
    }


    @GET
    @Path("all/{suppliesId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all deliveryBills",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = DeliveryBillResponse.class)))}
    )
    public Response getAll(
            @Parameter(description = "DeliveryBill Id", example = "601") @PathParam("suppliesId") Integer suppliesId
    ) {
        DeliveryBillResponse resp = new DeliveryBillResponse();
        try {
            suppliesId = suppliesId == null ? 0 : suppliesId;
            List<Integer> arrDeliveryBillId = new ArrayList<>();
            arrDeliveryBillId = deliveryBillDao.getListBySuppliersId(suppliesId);
            Set<Integer> set = new HashSet<>(arrDeliveryBillId);
            List<Integer> arrDeliveryBill = new ArrayList<Integer>(set);
            List<DeliveryBillModel> billModelList = deliveryBillDao.getListBySupplies(arrDeliveryBill);
            resp.setList(billModelList);
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Lỗi xảy ra - " + e.getMessage() + ", " + (e.getCause() != null ? e.getCause().getMessage() : ""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a deliveryBill",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addDeliveryBill(DeliveryBillModel deliveryBill) {
        BaseResponse resp = new BaseResponse();
        try {
            UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();
            deliveryBill.setEmployeeId(Long.valueOf(userFromToken.getEmployeeId()));
            deliveryBillDao.beginTransaction();
            deliveryBillDao.save(deliveryBill);
            deliveryBillDao.commitTransaction();
            resp.setSuccessMessage(String.format("DeliveryBill Added - New DeliveryBill ID : %s ", deliveryBill.getDeliveryBillId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Lỗi xảy ra - " + e.getMessage() + ", " + (e.getCause() != null ? e.getCause().getMessage() : ""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a DeliveryBill",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateDeliveryBill(DeliveryBillModel deliveryBill) {
        BaseResponse resp = new BaseResponse();
        try {
            DeliveryBillModel foundProd = deliveryBillDao.getById(deliveryBill.getDeliveryBillId());
            if (foundProd != null) {
                UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();
                deliveryBill.setEmployeeId(Long.valueOf(userFromToken.getEmployeeId()));
                deliveryBillDao.beginTransaction();
                deliveryBillDao.update(deliveryBill);
                deliveryBillDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", deliveryBill.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Không thể sửa bản ghi (code:%s)", deliveryBill.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Lỗi xảy ra - " + e.getMessage() + ", " + (e.getCause() != null ? e.getCause().getMessage() : ""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{deliveryBillId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a deliveryBill",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteDeliveryBill(@Parameter(description = "DeliveryBill Id", example = "601") @PathParam("deliveryBillId") Long deliveryBillId) {
        BaseResponse resp = new BaseResponse();
        try {
            DeliveryBillModel foundProd = deliveryBillDao.getById(deliveryBillId);
            if (foundProd == null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại: (id:%s)", deliveryBillId));
                return Response.ok(resp).build();
            } else {
                deliveryBillDao.beginTransaction();
                deliveryBillDao.delete(deliveryBillId);
                deliveryBillDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (code:%s)", foundProd.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể xóa bản ghi - " + e.getMessage() + ", " + (e.getCause() != null ? e.getCause().getMessage() : ""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("sequence")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "get sequenceId a deliveryBill",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getSequence() throws Exception {
        Long id = deliveryBillDao.getSequence();
        return Response.ok(id == null ? 1 : id).build();
    }

    @GET
    @Path("equal/{deliveryBillId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "get sequenceId a deliveryBill",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getEqualId(@Parameter(description = "DeliveryBill Id", example = "601") @PathParam("deliveryBillId") Long deliveryBillId) {
        boolean check = false;
        Criteria criteria = deliveryBillDao.getSession().createCriteria(DeliveryBillModel.class);
        // Execute the Main Query
        if (deliveryBillId > 0) {
            criteria.add(Restrictions.eq("deliveryBillId", deliveryBillId));
        }
        criteria.setProjection(null);
        List<DeliveryBillModel> deliveryBillList = criteria.list();
        if (deliveryBillList.size() > 0) {
            check = true;
        }
        return Response.ok(check).build();
    }


    @DELETE
    @Path("delete_by_id/{deliveryBillId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    public void deleteByRreceiptId(@Parameter(description = "Receipt Id", example = "601") @PathParam("deliveryBillId") Long deliveryBillId) {
        deliveryBillId = deliveryBillId == null ? 0 : deliveryBillId;
        deliveryBillFlowDao.beginTransaction();
        deliveryBillFlowDao.deleteByDeliveryBillId(deliveryBillId);
        deliveryBillFlowDao.commitTransaction();
    }

    @POST
    @Path("byCode")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getByCode(
            DeliveryBillModel model
    ) {
        int recordFrom = 0;
        Criteria criteria = deliveryBillDao.createCriteria(DeliveryBillModel.class);
        if (model.getDeliveryBillId() != null){
            criteria.add(Restrictions.ne("deliveryBillId", model.getDeliveryBillId()));
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
            DeliveryBillModel deliveryBill
    ) throws Exception {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        String fileName = "danh_sach_phieu_xuat.xls";
        Integer startDataRow = 6;
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_EXPORT_EXCELL + fileName), startDataRow, false);
        List<DeliveryBillModel> models = deliveryBillDao.getListExport(deliveryBill.getCode(), deliveryBill.getName(), deliveryBill.getEmployeeId(),
                deliveryBill.getWarehouseId(), deliveryBill.getFormDate(), deliveryBill.getToDate(), deliveryBill.getFactoryId());
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                DeliveryBillModel model = models.get(i);
                int index = 0;
                dynamicExport.increaseRow();
                dynamicExport.setEntry(stt++, index++);
                dynamicExport.setText(model.getCode(), index++);
                dynamicExport.setText(model.getName(), index++);
                dynamicExport.setText(CommonUtils.convertDateToString(model.getDateDeliveryBill()), index++);
                dynamicExport.setText(model.getFullName(), index++);
                dynamicExport.setText(model.getWarehouseCode(), index++);
                dynamicExport.setText(model.getWarehouseName(), index++);
                dynamicExport.setText(model.getFactoryCode(), index++);
                dynamicExport.setText(model.getFactoryName(), index++);
                dynamicExport.setText(model.getSumMoney() == null ? "" : model.getSumMoney().toString(), index++);
            }
        }
        dynamicExport.setCellFormat(startDataRow, 0, dynamicExport.getLastRow(), 9, DynamicExport.BORDER_FORMAT);
        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = FOLDER_EXPORT + prefixOutPutFile +  "danh_sach_phieu_xuat";
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

    @DELETE
    @Path("downloadFileDocx/{deliveryBillId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a deliveryBill",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response downloadFileDocx(@Parameter(description = "DeliveryBill Id", example = "601") @PathParam("deliveryBillId") Long deliveryBillId) throws IOException {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        List<DeliveryBillFlowModel> models = deliveryBillFlowDao.getByDeliveryBillId(deliveryBillId);
        DeliveryBillModel deliveryBillModel = deliveryBillDao.getById(deliveryBillId);
        WarehouseModel warehouseModel = warehouseDao.getById(deliveryBillModel.getWarehouseId());
        Date date = new Date();
        String strDate = CommonUtils.convertDateToString(date);
        String[] arrDate = strDate.split("/");
        File fileDocx = new File(TEMPLATE_EXPORT_DOCX + "BM_Phieu_Xuat_Kho.docx");
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_";
        try (InputStream is = new FileInputStream(fileDocx);
             XWPFDocument doc = new XWPFDocument(is)) {
            Iterator<IBodyElement> docElementsIterator = doc.getBodyElementsIterator();
            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();
            for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
                for (XWPFRun xwpfRun : xwpfParagraph.getRuns()) {
                    String docText = xwpfRun.getText(0);
                    //replacement and setting position
                    if (docText != null) {
                        docText = docText.replace("day", arrDate[0]);
                        docText = docText.replace("month", arrDate[1]);
                        docText = docText.replace("year", arrDate[2]);
                        docText = docText.replace("warehouse", warehouseModel.getName());
                        xwpfRun.setText(docText, 0);
                    }
                }
                IBodyElement docElement = docElementsIterator.next();
                if ("TABLE".equalsIgnoreCase(docElement.getElementType().name())) {
                    //Get List of table and iterate it
                    List<XWPFTable> xwpfTableList = docElement.getBody().getTables();
                    for (XWPFTable xwpfTable : xwpfTableList) {
                        System.out.println("Total Rows : " + xwpfTable.getNumberOfRows());
                        int index = 1;
                        int row = 1;
                        Long sum = 0L;
                        for (int i = 0; i < models.size(); i++) {
                            DeliveryBillFlowModel model = models.get(i);
                            int col = 0;
                            xwpfTable.createRow();
                            xwpfTable.getRow(row).getCell(col++).setText(String.valueOf(index++));
                            xwpfTable.getRow(row).getCell(col++).setText(model.getSpeciesName());
                            xwpfTable.getRow(row).getCell(col++).setText(model.getSuppliesUnit());
                            xwpfTable.getRow(row).getCell(col++).setText(String.valueOf(model.getAmount()));
                            xwpfTable.getRow(row).getCell(col++).setText(model.getSuppliesPrice());
                            xwpfTable.getRow(row).getCell(col++).setText(model.getCalculatePrice());
                            row++;
                            sum += Long.parseLong(model.getCalculatePrice());
                        }
                        xwpfTable.createRow();
                        xwpfTable.getRow(row).getCell(1).setText("Tổng cộng:");
                        xwpfTable.getRow(row).getCell(5).setText(String.valueOf(sum));
                    }
                }
            }
            try (FileOutputStream out = new FileOutputStream(FOLDER_EXPORT_DOCX + prefixOutPutFile + "Phieu_Xuat_Kho.docx")) {
                doc.write(out);
            }
        }
        File file = new File(FOLDER_EXPORT_DOCX + prefixOutPutFile + "Phieu_Xuat_Kho.docx");
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        ExportModel exportModel = new ExportModel();
        exportModel.setFileName(prefixOutPutFile + "test2.docx");
        exportModel.setData(encodedString);
        resp.setData(exportModel);
        return Response.ok(resp).build();
    }
}
