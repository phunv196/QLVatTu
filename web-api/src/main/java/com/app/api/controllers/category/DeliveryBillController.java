package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.category.DeliveryBillDao;
import com.app.dao.category.DeliveryBillFlowDao;
import com.app.model.BaseResponse;
import com.app.model.category.DeliveryBillModel;
import com.app.model.category.DeliveryBillModel.DeliveryBillResponse;
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

@Path("delivery_bills")
@Tag(name = "deliveryBills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeliveryBillController extends BaseController {
    DeliveryBillDao deliveryBillDao = new DeliveryBillDao();
    DeliveryBillFlowDao deliveryBillFlowDao = new DeliveryBillFlowDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of deliveryBills",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = DeliveryBillResponse.class)))}
    )
    public Response getList(@Parameter(description="DeliveryBill Id") @QueryParam("deliveryBillId") Long deliveryBillId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Name", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="Name", example="nikon%") @QueryParam("formDate") String formDate, @Parameter(description="Name", example="nikon%") @QueryParam("toDate") String toDate,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) throws Exception {
        DeliveryBillResponse resp = new DeliveryBillResponse();
        try {
            if (deliveryBillId == null) {
                deliveryBillId = 0l;
            }
            java.sql.Date formDateSQL = CommonUtils.convertStringToSQLDate(formDate);
            java.sql.Date toDateSQL = CommonUtils.convertStringToSQLDate(toDate);

            List<DeliveryBillModel> billModelList = deliveryBillDao.getList( page, pageSize, deliveryBillId, name, code , formDateSQL, toDateSQL);
            BigInteger total = deliveryBillDao.getDeliveryBillCount(deliveryBillId, name, code , formDateSQL, toDateSQL);
            resp.setList(billModelList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(),pageSize, page,"");
            resp.setSuccessMessage("List of Orders and nested details " + (deliveryBillId>0 ? "- Customer:"+deliveryBillId:""));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Order - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }


    @GET
    @Path("all/{suppliesId}")
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get all deliveryBills",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = DeliveryBillResponse.class)))}
    )
    public Response getAll(
            @Parameter(description="DeliveryBill Id", example="601") @PathParam("suppliesId") Integer suppliesId
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
            resp.setErrorMessage("Cannot delete Order - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a deliveryBill",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addDeliveryBill(DeliveryBillModel deliveryBill) {
        BaseResponse resp = new BaseResponse();
        try {
            deliveryBillDao.beginTransaction();
            deliveryBillDao.save(deliveryBill);
            deliveryBillDao.commitTransaction();
            resp.setSuccessMessage(String.format("DeliveryBill Added - New DeliveryBill ID : %s ", deliveryBill.getDeliveryBillId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add DeliveryBill - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a DeliveryBill",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateDeliveryBill(DeliveryBillModel deliveryBill) {
        BaseResponse resp = new BaseResponse();
        try {
            DeliveryBillModel foundProd  = deliveryBillDao.getById( deliveryBill.getDeliveryBillId());
            if (foundProd != null) {
                deliveryBillDao.beginTransaction();
                deliveryBillDao.update(deliveryBill);
                deliveryBillDao.commitTransaction();
                resp.setSuccessMessage(String.format("DeliveryBill Updated (getDeliveryBillId:%s)", deliveryBill.getDeliveryBillId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - DeliveryBill not found (getDeliveryBillId:%s)", deliveryBill.getDeliveryBillId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update DeliveryBill - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{deliveryBillId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a deliveryBill",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteDeliveryBill(@Parameter(description="DeliveryBill Id", example="601") @PathParam("deliveryBillId") Long deliveryBillId) {
        BaseResponse resp = new BaseResponse();
        try {
            DeliveryBillModel foundProd  = deliveryBillDao.getById(deliveryBillId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Cannot delete DeliveryBill - Customer do not exist (id:%s)", deliveryBillId));
                return Response.ok(resp).build();
            } else {
                deliveryBillDao.beginTransaction();
                deliveryBillDao.delete(deliveryBillId);
                deliveryBillDao.commitTransaction();
                resp.setSuccessMessage(String.format("DeliveryBill deleted (deliveryBillId:%s)", deliveryBillId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete DeliveryBill - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("sequence")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "get sequenceId a deliveryBill",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
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
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getEqualId(@Parameter(description="DeliveryBill Id", example="601") @PathParam("deliveryBillId") Long deliveryBillId) {
        boolean check = false;
        Criteria criteria = deliveryBillDao.getSession().createCriteria(DeliveryBillModel.class);
        // Execute the Main Query
        if (deliveryBillId > 0){
            criteria.add(Restrictions.eq("deliveryBillId",  deliveryBillId ));
        }
        criteria.setProjection(null);
        List<DeliveryBillModel> deliveryBillList = criteria.list();
        if (deliveryBillList.size() > 0) {
            check = true;
        }
        return Response.ok(check).build();
    }

//    @GET
//    @Path("{suppliesId}")
//    @RolesAllowed({"ADMIN", "SUPPORT"})
//    @Operation(
//            summary = "get sequenceId a deliveryBill",
//            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
//    )
//    public Response getListSuppliersId(@Parameter(description="DeliveryBill Id", example="601") @PathParam("suppliesId") Long suppliesId) {
//
//        List<Long> arrSuppliersId = new ArrayList<>();
//        arrSuppliersId = deliveryBillDao.getListBySuppliersId(suppliesId);
//        Set<Long> set = new HashSet<>(arrSuppliersId);
//        List<Long> arrSuppliers = new ArrayList<Long>(set);
//        return Response.ok(arrSuppliers).build();
//    }

    @DELETE
    @Path("delete_by_id/{deliveryBillId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    public void deleteByRreceiptId(@Parameter(description="Receipt Id", example="601") @PathParam("deliveryBillId") Long deliveryBillId) {
        deliveryBillId = deliveryBillId == null ? 0 : deliveryBillId;
        deliveryBillFlowDao.beginTransaction();
        deliveryBillFlowDao.deleteByDeliveryBillId(deliveryBillId);
        deliveryBillFlowDao.commitTransaction();
    }
}
