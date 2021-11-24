package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.DeliveryBillDao;
import com.app.dao.DeliveryBillFlowDao;
import com.app.model.BaseResponse;
import com.app.model.delivery.DeliveryBillFlowModel;
import com.app.model.delivery.DeliveryBillFlowModel.DeliveryBillFlowResponse;
import com.app.model.receipt.ReceiptFlowModel;
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
import java.util.List;

@Path("delivery_bill_flows")
@Tag(name = "deliveryBillFlows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryBillFlowController extends BaseController {

    DeliveryBillFlowDao deliveryBillFlowDao = new DeliveryBillFlowDao();
    DeliveryBillDao deliveryBillDao = new DeliveryBillDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of deliveryBillFlows",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = DeliveryBillFlowResponse.class)))}
    )
    public Response getDeliveryBillFlowList(
            @Parameter(description="DeliveryBill Id") @QueryParam("deliveryBillId") Long deliveryBillId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="5") @DefaultValue("5") @QueryParam("page-size") int pageSize
    ) {
        DeliveryBillFlowResponse resp = new DeliveryBillFlowResponse();
        try {
            if (deliveryBillId == null) {
                deliveryBillId = 0l;
            }
            List<DeliveryBillFlowModel> billModelList = deliveryBillFlowDao.getList( page, pageSize, deliveryBillId);
            BigInteger total = deliveryBillFlowDao.getDeliveryBillFlowCount(deliveryBillId);
            resp.setList(billModelList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(),pageSize, page,"");
            resp.setSuccessMessage("List of DeliveryBillFlowModel and nested details " + (deliveryBillId>0 ? "- Customer:"+deliveryBillId:""));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Lỗi xảy ra - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a deliveryBillFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addDeliveryBillFlow(DeliveryBillFlowModel deliveryBillFlow) {
        BaseResponse resp = new BaseResponse();
        try {
            deliveryBillFlowDao.beginTransaction();
            deliveryBillFlowDao.save(deliveryBillFlow);
            deliveryBillFlowDao.commitTransaction();
            resp.setSuccessMessage(String.format("DeliveryBillFlow Added - New DeliveryBillFlow ID : %s ", deliveryBillFlow.getDeliveryBillFlowId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a DeliveryBillFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateDeliveryBillFlow(DeliveryBillFlowModel deliveryBillFlow) {
        BaseResponse resp = new BaseResponse();
        try {
            DeliveryBillFlowModel foundProd  = deliveryBillFlowDao.getById(deliveryBillFlow.getDeliveryBillFlowId());
            if (foundProd != null) {
                deliveryBillFlowDao.beginTransaction();
                deliveryBillFlowDao.update(deliveryBillFlow);
                deliveryBillFlowDao.commitTransaction();
                resp.setSuccessMessage(String.format("DeliveryBillFlow Updated (getDeliveryBillFlowId:%s)", deliveryBillFlow.getDeliveryBillFlowId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Sửa bản ghi thất bại (id:%s)", deliveryBillFlow.getDeliveryBillFlowId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{deliveryBillFlowId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a deliveryBillFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteDeliveryBillFlow(@Parameter(description="DeliveryBillFlow Id", example="601") @PathParam("deliveryBillFlowId") Long deliveryBillFlowId) {
        BaseResponse resp = new BaseResponse();
        try {
            DeliveryBillFlowModel foundProd  = deliveryBillFlowDao.getById(deliveryBillFlowId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", deliveryBillFlowId));
                return Response.ok(resp).build();
            } else {
                deliveryBillFlowDao.beginTransaction();
                deliveryBillFlowDao.delete(deliveryBillFlowId);
                deliveryBillFlowDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (id:%s)", deliveryBillFlowId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể xóa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("delivery_bill_flow")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of deliveryBillFlows",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = DeliveryBillFlowResponse.class)))}
    )
    public Response getDeliveryBillFlow(
            @Parameter(description="DeliveryBill Id") @QueryParam("deliveryBillId") Long deliveryBillId,
            @Parameter(description="supplies Id") @QueryParam("suppliesId") Long suppliesId
    ) {
        Criteria criteria = deliveryBillFlowDao.getSession().createCriteria(DeliveryBillFlowModel.class);
        // Execute the Main Query
        if (deliveryBillId != null){
            criteria.add(Restrictions.eq("deliveryBillId",  deliveryBillId ));
        }
        if (suppliesId != null){
            criteria.add(Restrictions.eq("suppliesId",  suppliesId ));
        }
        criteria.setProjection(null);
        DeliveryBillFlowModel deliveryBillFlow = (DeliveryBillFlowModel) criteria.uniqueResult();

        return Response.ok(deliveryBillFlow).build();
    }

    @GET
    @Path("check_delivery_bill_flow")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of receipt_flows",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = ReceiptFlowModel.ReceiptFlowResponse.class)))}
    )
    public Response checkReceiptFlow(@Parameter(description="deliveryBill Id") @QueryParam("deliveryBillId") Long deliveryBillId,
                                     @Parameter(description="receipt flow Id") @QueryParam("deliveryBillFlowId") Long deliveryBillFlowId,
                                     @Parameter(description="supplies Id") @QueryParam("suppliesId") Long suppliesId
    ) {
        Criteria criteria = deliveryBillFlowDao.createCriteria(DeliveryBillFlowModel.class);
        if (deliveryBillId != null){
            criteria.add(Restrictions.eq("deliveryBillId",  deliveryBillId ));
        }
        if (deliveryBillFlowId != null){
            criteria.add(Restrictions.ne("deliveryBillFlowId",  deliveryBillFlowId ));
        }
        if (suppliesId != null){
            criteria.add(Restrictions.eq("suppliesId",  suppliesId ));
        }
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount > 0).build();
    }
}
