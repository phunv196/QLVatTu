package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.category.ReceiptDao;
import com.app.dao.category.ReceiptFlowDao;
import com.app.model.BaseResponse;
import com.app.model.category.ReceiptModel;
import com.app.model.category.ReceiptModel.ReceiptResponse;
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
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("receipts")
@Tag(name = "Receipts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceiptController extends BaseController {
    ReceiptDao receiptDao = new ReceiptDao();
    ReceiptFlowDao receiptFlowDao = new ReceiptFlowDao();

    @GET
    @RolesAllowed({"ADMIN"})
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
        try {
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
                    searchName,
                    searchEmployee,
                    searchWarehouse,
                    searchFormDate,
                    searchToDate);
            BigInteger total = receiptDao.getReceiptCount(receiptId, searchCode,
                    searchName,
                    searchEmployee,
                    searchWarehouse,
                    searchFormDate,
                    searchToDate);
            resp.setList(billModelList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(),pageSize, page,"");
            resp.setSuccessMessage("List of receipt and nested details " + (receiptId >0 ? "- Customer:" + receiptId:""));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Order - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN"})
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
            receiptDao.beginTransaction();
            receiptDao.save(receipt);
            receiptDao.commitTransaction();
            resp.setSuccessMessage(String.format("Receipt Added - New Receipt ID : %s ", receipt.getReceiptId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add Receipt - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
                receiptDao.beginTransaction();
                receiptDao.update(receipt);
                receiptDao.commitTransaction();
                resp.setSuccessMessage(String.format("Receipt Updated (getReceiptId:%s)", receipt.getReceiptId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - Receipt not found (getReceiptId:%s)", receipt.getReceiptId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update Receipt - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
                resp.setErrorMessage(String.format("Cannot delete Receipt - Customer do not exist (id:%s)", receiptId));
                return Response.ok(resp).build();
            } else {
                receiptDao.beginTransaction();
                receiptDao.delete(receiptId);
                receiptDao.commitTransaction();
                resp.setSuccessMessage(String.format("Receipt deleted (receiptId:%s)", receiptId));
                return Response.ok(resp).build();
            }
//            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Receipt - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
        return Response.ok(id == null ? 1 : id).build();
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
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get all deliveryBills",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = ReceiptResponse.class)))}
    )
    public Response getAll(
            @Parameter(description="DeliveryBill Id", example="601") @PathParam("suppliesId") Integer suppliesId
    ) {
        ReceiptResponse resp = new ReceiptResponse();
        try {
            suppliesId = suppliesId == null ? 0 : suppliesId;
            List<Integer> arrReceiptId = new ArrayList<>();
            arrReceiptId = receiptDao.getListBySuppliersId(suppliesId);
            Set<Integer> set = new HashSet<>(arrReceiptId);
            List<Integer> arrReceipt = new ArrayList<Integer>(set);
            List<ReceiptModel> modelList = receiptDao.getListBySupplies(arrReceipt);
            resp.setList(modelList);
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Order - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }
}
