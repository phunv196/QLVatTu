package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.ReceiptFlowDao;
import com.app.dao.WarehouseCardDao;
import com.app.dao.WarehouseCardFlowDao;
import com.app.model.BaseResponse;
import com.app.model.receipt.ReceiptFlowModel;
import com.app.model.supplier.SupplierModel;
import com.app.model.warehouseCard.WarehouseCardFlowModel;
import com.app.model.warehouseCard.WarehouseCardFlowModel.WarehouseCardFlowResponse;
import com.app.model.warehouseCard.WarehouseCardModel;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Path("warehouse_card_flows")
@Tag(name = "WarehouseCardFlows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarehouseCardFlowController extends BaseController {

    WarehouseCardDao warehouseCardDao = new WarehouseCardDao();
    WarehouseCardFlowDao warehouseCardFlowDao = new WarehouseCardFlowDao();
    ReceiptFlowDao receiptFlowDao = new ReceiptFlowDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of warehouseCardFlows",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseCardFlowResponse.class)))}
    )
    public Response getWarehouseCardFlowList(
            @Parameter(description="WarehouseCard Id") @QueryParam("warehouseCardId") Long warehouseCardId,
            @Parameter(description="supplies Id") @QueryParam("suppliesId") Long suppliesId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="5") @DefaultValue("5") @QueryParam("page-size") int pageSize
    ) {
        WarehouseCardFlowResponse resp = new WarehouseCardFlowResponse();
        if (warehouseCardId == null) {
            warehouseCardId = 0l;
        }
        if (suppliesId == null) {
            suppliesId = 0l;
        }
        List<WarehouseCardFlowModel> cardFlowModels = warehouseCardFlowDao.getList(page, pageSize, warehouseCardId, suppliesId);
        BigInteger total = warehouseCardFlowDao.getWarehouseCardFlowCount(warehouseCardId, suppliesId);
        resp.setList(cardFlowModels);
        resp.setTotal(total.intValue());
        resp.setPageStats(total.intValue(),pageSize, page,"");
        resp.setSuccessMessage("List of receipt and nested details " + (warehouseCardId >0 ? "- Customer:" + warehouseCardId:""));
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a warehouseCardFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addWarehouseCardFlow(WarehouseCardFlowModel warehouseCardFlow) {
        BaseResponse resp = new BaseResponse();
        try {
            warehouseCardFlowDao.beginTransaction();
            warehouseCardFlow.setCreateAt(new Date());
            warehouseCardFlowDao.save(warehouseCardFlow);
            warehouseCardFlowDao.commitTransaction();
            if( check(warehouseCardFlow.getWarehouseCardId()) && warehouseCardFlow.getReceiptId() != null) {
                updateRecepit(warehouseCardFlow);
            }
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công id: %s ", warehouseCardFlow.getWarehouseCardFlowId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    public void updateRecepit(WarehouseCardFlowModel warehouseCardFlow) {
        WarehouseCardModel cardModel = warehouseCardDao.getById(warehouseCardFlow.getWarehouseCardId());
        ReceiptFlowModel model = receiptFlowDao.getModel(warehouseCardFlow.getReceiptId(), cardModel.getSuppliesId());
        model.setReceived(model.getReceived() == null ? warehouseCardFlow.getAmount() : model.getReceived() + warehouseCardFlow.getAmount());
        receiptFlowDao.beginTransaction();
        receiptFlowDao.save(model);
        receiptFlowDao.commitTransaction();
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a WarehouseCardFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateWarehouseCardFlow(WarehouseCardFlowModel warehouseCardFlow) {
        BaseResponse resp = new BaseResponse();
        try {
            WarehouseCardFlowModel foundProd  = warehouseCardFlowDao.getById(warehouseCardFlow.getWarehouseCardFlowId());
            if (foundProd != null) {
                warehouseCardFlowDao.beginTransaction();
                warehouseCardFlowDao.update(warehouseCardFlow);
                warehouseCardFlowDao.commitTransaction();
                if (check(warehouseCardFlow.getWarehouseCardId()) && warehouseCardFlow.getReceiptId() != null) {
                    updateRecepit(warehouseCardFlow);
                }
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (id:%s)", warehouseCardFlow.getWarehouseCardFlowId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", warehouseCardFlow.getWarehouseCardFlowId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{warehouseCardFlowId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a WarehouseCardFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteWarehouseCardFlow(@Parameter(description="WarehouseCardFlow Id", example="601") @PathParam("warehouseCardFlowId") Long warehouseCardFlowId) {
        BaseResponse resp = new BaseResponse();
        try {

            WarehouseCardFlowModel foundProd  = warehouseCardFlowDao.getById(warehouseCardFlowId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", warehouseCardFlowId));
                return Response.ok(resp).build();
            } else {
                warehouseCardFlowDao.beginTransaction();
                warehouseCardFlowDao.delete(warehouseCardFlowId);
                warehouseCardFlowDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (id:%s)", warehouseCardFlowId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể xóa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    boolean check(Long id) {
        Criteria criteria = warehouseCardDao.createCriteria(WarehouseCardModel.class);
        criteria.add(Restrictions.eq("warehouseCardId",  id ));
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return rowCount > 0;
    }
}
