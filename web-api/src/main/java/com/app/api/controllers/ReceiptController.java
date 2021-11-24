package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.EmployeeDao;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.converter.DynamicExport;
import com.app.dao.DepartmentDao;
import com.app.dao.ReceiptDao;
import com.app.dao.ReceiptFlowDao;
import com.app.dao.WarehouseDao;
import com.app.model.BaseResponse;
import com.app.model.ExportModel;
import com.app.model.receipt.ReceiptFlowModel;
import com.app.model.receipt.ReceiptModel;
import com.app.model.receipt.ReceiptModel.ReceiptResponse;
import com.app.model.department.DepartmentModel;
import com.app.model.employee.EmployeeModel;
import com.app.model.user.UserModel;
import com.app.model.warehouse.WarehouseModel;
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
import static com.app.util.Constants.COMMON.FOLDER_EXPORT_DOCX;

@Path("receipts")
@Tag(name = "Receipts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceiptController extends BaseController {
    ReceiptDao receiptDao = new ReceiptDao();
    ReceiptFlowDao receiptFlowDao = new ReceiptFlowDao();
    WarehouseDao warehouseDao = new WarehouseDao();
    EmployeeDao employeeDao = new EmployeeDao();
    DepartmentDao departmentDao = new DepartmentDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of receipts",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = ReceiptResponse.class)))}
    )
    public Response getReceiptList(
            @Parameter(description="Receipt Id") @QueryParam("receiptId") Long receiptId,
            @Parameter(description = "Order Id") @QueryParam("searchCode") String searchCode,
            @Parameter(description = "Order Id") @QueryParam("searchName") String searchName,
            @Parameter(description = "Order Id") @QueryParam("searchEmployee") Long searchEmployee,
            @Parameter(description = "Order Id") @QueryParam("searchWarehouse") Long searchWarehouse,
            @Parameter(description = "Order Id") @QueryParam("searchFormDate") String searchFormDate,
            @Parameter(description = "Order Id") @QueryParam("searchToDate") String searchToDate,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        ReceiptResponse resp = new ReceiptResponse();;
            if (receiptId == null) {
                receiptId = 0l;
            }
            if (searchEmployee == null) {
                searchEmployee = 0l;
            }

            if (searchWarehouse == null) {
                searchWarehouse = 0l;
            }
            List<ReceiptModel> billModelList = receiptDao.getList(page, pageSize, receiptId,searchCode,
                    searchName, searchEmployee, searchWarehouse, searchFormDate, searchToDate);
            BigInteger total = receiptDao.getReceiptCount(receiptId, searchCode,
                    searchName, searchEmployee, searchWarehouse, searchFormDate, searchToDate);
            resp.setList(billModelList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(),pageSize, page,"");
            resp.setSuccessMessage("List of receipt and nested details " + (receiptId >0 ? "- Customer:" + receiptId:""));
            return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all receipts",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = ReceiptResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = receiptDao.createCriteria(ReceiptModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<ReceiptModel> receiptList = criteria.list();
        ReceiptResponse resp = new ReceiptResponse();
        resp.setList(receiptList);
        resp.setSuccessMessage("List of receipt");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a receipt",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addReceipt(ReceiptModel receipt) {
        BaseResponse resp = new BaseResponse();
        try {
            UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();
            receipt.setEmployeeId(Long.valueOf(userFromToken.getEmployeeId()));
            receiptDao.beginTransaction();
            receiptDao.save(receipt);
            receiptDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công code: %s ", receipt.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Receipt",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateReceipt(ReceiptModel receipt) {
        BaseResponse resp = new BaseResponse();
        try {
            ReceiptModel foundProd  = receiptDao.getById(receipt.getReceiptId());
            if (foundProd != null) {
                UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();
                receipt.setEmployeeId(Long.valueOf(userFromToken.getEmployeeId()));
                receiptDao.beginTransaction();
                receiptDao.update(receipt);
                receiptDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", receipt.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", receipt.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{receiptId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a receipt",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteReceipt(@Parameter(description="Receipt Id", example="601") @PathParam("receiptId") Long receiptId) {
        BaseResponse resp = new BaseResponse();
        try {
            ReceiptModel foundProd  = receiptDao.getById(receiptId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", receiptId));
                return Response.ok(resp).build();
            } else {
                receiptDao.beginTransaction();
                receiptDao.delete(receiptId);
                receiptDao.commitTransaction();
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
            summary = "get sequenceId a Receipt",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getSequence() throws Exception {
        Long id = receiptDao.getSequence();
        return Response.ok(id == null ? 1 : id -1 ).build();
    }

    @GET
    @Path("equal/{receiptId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Check receiptId a receipt",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getEqualId(@Parameter(description="Receipt Id", example="601") @PathParam("receiptId") Long receiptId) {
        boolean check = false;
        Criteria criteria = receiptDao.createCriteria(ReceiptModel.class);
        // Execute the Main Query
        if (receiptId > 0){
            criteria.add(Restrictions.eq("receiptId",  receiptId ));
        }
        criteria.setProjection(null);
        List<ReceiptModel> deliveryBillList = criteria.list();
        if (deliveryBillList.size() > 0) {
            check = true;
        }
        return Response.ok(check).build();
    }

    @DELETE
    @Path("delete_by_id/{receiptId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    public void deleteByRreceiptId(@Parameter(description="Receipt Id", example="601") @PathParam("receiptId") Long receiptId) {
        receiptId = receiptId == null ? 0 : receiptId;
        receiptFlowDao.beginTransaction();
        receiptFlowDao.deleteByRreceiptId(receiptId);
        receiptFlowDao.commitTransaction();
    }

    @GET
    @Path("all/{suppliesId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all deliveryBills",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = ReceiptResponse.class)))}
    )
    public Response getAll(
            @Parameter(description="DeliveryBill Id", example="601") @PathParam("suppliesId") Integer suppliesId
    ) {
        ReceiptResponse resp = new ReceiptResponse();
        suppliesId = suppliesId == null ? 0 : suppliesId;
        List<Integer> arrReceiptId = new ArrayList<>();
        arrReceiptId = receiptDao.getListBySuppliersId(suppliesId);
        if (arrReceiptId.size() > 0) {
            Set<Integer> set = new HashSet<>(arrReceiptId);
            List<Integer> arrReceipt = new ArrayList<Integer>(set);
            List<ReceiptModel> modelList = receiptDao.getListBySupplies(arrReceipt);
            resp.setList(modelList);
        }
        return Response.ok(resp).build();
    }

    @POST
    @Path("byCode")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getByCode(
            ReceiptModel model
    ) {
        Criteria criteria = receiptDao.createCriteria(ReceiptModel.class);
        if (model.getReceiptId() != null){
            criteria.add(Restrictions.ne("receiptId", model.getReceiptId()));
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
            ReceiptModel receipt
    ) throws Exception {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        String fileName = "danh_sach_phieu_nhap.xls";
        Integer startDataRow = 6;
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_EXPORT_EXCELL + fileName), startDataRow, false);
        List<ReceiptModel> models = receiptDao.getListExport(receipt.getCode(), receipt.getName(), receipt.getEmployeeId(),
                receipt.getWarehouseId(), receipt.getFormDate(), receipt.getToDate());
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                ReceiptModel model = models.get(i);
                int index = 0;
                dynamicExport.increaseRow();
                dynamicExport.setEntry(stt++, index++);
                dynamicExport.setText(model.getCode(), index++);
                dynamicExport.setText(model.getName(), index++);
                dynamicExport.setText(CommonUtils.convertDateToString(model.getDateWarehousing()), index++);
                dynamicExport.setText(model.getFullName(), index++);
                dynamicExport.setText(model.getWarehouseCode(), index++);
                dynamicExport.setText(model.getWarehouseName(), index++);
                dynamicExport.setText(model.getSumMoney() == null ? "" : model.getSumMoney().toString(), index++);
            }
        }
        dynamicExport.setCellFormat(startDataRow, 0, dynamicExport.getLastRow(), 7, DynamicExport.BORDER_FORMAT);
        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = FOLDER_EXPORT + prefixOutPutFile +  "danh_sach_phieu_nhap";
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
    @Path("downloadFileDocx/{receiptId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "receipt",
            responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response downloadFileDocx(@Parameter(description = "DeliveryBill Id", example = "601") @PathParam("receiptId") Long receiptId) throws IOException {
        ExportModel.ExportResponse resp = new ExportModel.ExportResponse();
        List<ReceiptFlowModel> models = receiptFlowDao.getByReceiptId(receiptId);
        ReceiptModel receiptModel = receiptDao.getById(receiptId);
        EmployeeModel employeeModel = employeeDao.getById(receiptModel.getEmployeeId());
        DepartmentModel departmentModel = departmentDao.getById(employeeModel.getDepartmentId());
        WarehouseModel warehouseModel = warehouseDao.getById(receiptModel.getWarehouseId());
        Date date = new Date();
        String strDate = CommonUtils.convertDateToString(date);
        String[] arrDate = strDate.split("/");
        File fileDocx = new File(TEMPLATE_EXPORT_DOCX + "BM_Phieu_Nhap_Kho.docx");
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_";
        Map<String, String> map = new HashMap<>();
        map.put("departmentName", departmentModel == null ? " " : departmentModel.getName());
        map.put("day", arrDate[0]);
        map.put("month", arrDate[1]);
        map.put("year", arrDate[2]);
        map.put("employeeName", employeeModel == null ? " " : employeeModel.getFullName());
        map.put("warehouseName", warehouseModel == null ? " " : warehouseModel.getName());
        map.put("warehouseAddress", warehouseModel == null ? " " : warehouseModel.getAddress());
        try (InputStream is = new FileInputStream(fileDocx);
             XWPFDocument doc = new XWPFDocument(is)) {
            Iterator<IBodyElement> docElementsIterator = doc.getBodyElementsIterator();
            List<XWPFParagraph> xwpfParagraphList = doc.getParagraphs();
            for (XWPFParagraph xwpfParagraph : xwpfParagraphList) {
                IBodyElement docElement = docElementsIterator.next();
                if ("TABLE".equalsIgnoreCase(docElement.getElementType().name())) {
                    List<XWPFTable> xwpfTableList = docElement.getBody().getTables();
                    for (XWPFTable xwpfTable : xwpfTableList) {
                        int index = 1;
                        int row = 2;
                        Long sum = 0L;
                        Long sumAmount = 0L;
                        for (int i = 0; i < models.size(); i++) {
                            ReceiptFlowModel model = models.get(i);
                            int col = 0;
                            xwpfTable.createRow();
                            xwpfTable.getRow(row).getCell(col++).setText(String.valueOf(index++));
                            xwpfTable.getRow(row).getCell(col++).setText(model.getSuppliesName());
                            xwpfTable.getRow(row).getCell(col++).setText(model.getSuppliesCode());
                            xwpfTable.getRow(row).getCell(col++).setText(model.getSuppliesUnit());
                            xwpfTable.getRow(row).getCell(col++).setText(String.valueOf(model.getAmount()));
                            xwpfTable.getRow(row).getCell(col++).setText(String.valueOf(model.getReceived() == null ? 0 : model.getReceived()));
                            xwpfTable.getRow(row).getCell(col++).setText(String.valueOf(model.getMissing()));
                            xwpfTable.getRow(row).addNewTableCell().setText(model.getSuppliesPrice());
                            xwpfTable.getRow(row).addNewTableCell().setText(model.getCalculatePrice());
                            row++;
                            sumAmount += model.getAmount() == null ? 0L : model.getAmount();
                            sum += Long.parseLong(CommonUtils.isNullOrEmpty(model.getCalculatePrice()) ? "0" : model.getCalculatePrice());
                        }
                        xwpfTable.createRow();
                        xwpfTable.getRow(row).getCell(1).setText("Tổng cộng:");
                        xwpfTable.getRow(row).addNewTableCell();
                        xwpfTable.getRow(row).getCell(4).setText(String.valueOf(sumAmount));
                        xwpfTable.getRow(row).addNewTableCell();
                        xwpfTable.getRow(row).getCell(8).setText(String.valueOf(sum));
                        map.put("strMoney", CommonUtils.numberToString(sum));
                    }
                }
                CommonUtils.replaceParagraph(xwpfParagraph, map);
            }
            try (FileOutputStream out = new FileOutputStream(FOLDER_EXPORT_DOCX + prefixOutPutFile + "Phieu_Nhap_Kho.docx")) {
                doc.write(out);
            }
        }
        File file = new File(FOLDER_EXPORT_DOCX + prefixOutPutFile + "Phieu_Nhap_Kho.docx");
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        ExportModel exportModel = new ExportModel();
        exportModel.setFileName(prefixOutPutFile + "Phieu_Nhap_Kho.docx");
        exportModel.setData(encodedString);
        resp.setData(exportModel);
        return Response.ok(resp).build();
    }
}
