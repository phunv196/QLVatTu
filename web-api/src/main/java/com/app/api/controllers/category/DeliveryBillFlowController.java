package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.category.DeliveryBillDao;
import com.app.dao.category.DeliveryBillFlowDao;
import com.app.model.BaseResponse;
import com.app.model.category.DeliveryBillFlowModel;
import com.app.model.category.DeliveryBillFlowModel.DeliveryBillFlowResponse;
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
import java.util.List;

@Path("delivery_bill_flows")
@Tag(name = "deliveryBillFlows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryBillFlowController extends BaseController {

    DeliveryBillFlowDao deliveryBillFlowDao = new DeliveryBillFlowDao();
    DeliveryBillDao deliveryBillDao = new DeliveryBillDao();

    @GET
    @RolesAllowed({"ADMIN"})
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
            resp.setErrorMessage("Cannot delete Order - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
            resp.setErrorMessage("Cannot add DeliveryBillFlow - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
                resp.setErrorMessage(String.format("Cannot Update - DeliveryBillFlow not found (getDeliveryBillFlowId:%s)", deliveryBillFlow.getDeliveryBillFlowId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update DeliveryBillFlow - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
                resp.setErrorMessage(String.format("Cannot delete DeliveryBillFlow - Customer do not exist (id:%s)", deliveryBillFlowId));
                return Response.ok(resp).build();
            } else {
                deliveryBillFlowDao.beginTransaction();
                deliveryBillFlowDao.delete(deliveryBillFlowId);
                deliveryBillFlowDao.commitTransaction();
                resp.setSuccessMessage(String.format("DeliveryBillFlow deleted (deliveryBillFlowId:%s)", deliveryBillFlowId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete DeliveryBillFlow - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("delivery_bill_flow")
    @RolesAllowed({"ADMIN"})
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
        List<DeliveryBillFlowModel> deliveryBillFlowList = criteria.list();

        return Response.ok(deliveryBillFlowList).build();
    }

}
