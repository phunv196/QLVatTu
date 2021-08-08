package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.category.WarehouseCardDao;
import com.app.dao.category.WarehouseCardFlowDao;
import com.app.model.BaseResponse;
import com.app.model.category.ReceiptModel;
import com.app.model.category.WarehouseCardModel;
import com.app.model.category.WarehouseCardModel.WarehouseCardResponse;
import com.app.model.user.UserModel;
import com.app.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.catalina.User;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

@Path("warehouse_cards")
@Tag(name = "WarehouseCards")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarehouseCardController extends BaseController {
    WarehouseCardDao warehouseCardDao = new WarehouseCardDao();
    WarehouseCardFlowDao warehouseCardFlowDao = new WarehouseCardFlowDao();
    @Context
    HttpServletRequest request;
    @GET
    @RolesAllowed({"ADMIN"})
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
        try {
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
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Order - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
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
            resp.setSuccessMessage(String.format("WarehouseCard Added - New WarehouseCard ID : %s ", warehouseCard.getWarehouseCardId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add WarehouseCard - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
                resp.setSuccessMessage(String.format("WarehouseCard Updated (getWarehouseCardId:%s)", warehouseCard.getWarehouseCardId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - WarehouseCard not found (getWarehouseCardId:%s)", warehouseCard.getWarehouseCardId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update WarehouseCard - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
                resp.setErrorMessage(String.format("Cannot delete WarehouseCard - Customer do not exist (id:%s)", warehouseCardId));
                return Response.ok(resp).build();
            } else {
                warehouseCardDao.beginTransaction();
                warehouseCardDao.delete(warehouseCardId);
                warehouseCardDao.commitTransaction();
                resp.setSuccessMessage(String.format("WarehouseCard deleted (warehouseCardId:%s)", warehouseCardId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete WarehouseCard - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
    @RolesAllowed({"ADMIN"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseCardModel.class)))}
    )
    public Response getByCode(
            WarehouseCardModel model
    ) {
        int recordFrom = 0;
        Criteria criteria = warehouseCardDao.createCriteria(WarehouseCardModel.class);
        if (model.getWarehouseCardId() != null){
            criteria.add(Restrictions.ne("warehouseCardId", model.getWarehouseCardId()));
        }
        if (!CommonUtils.isNullOrEmpty(model.getCode())){
            criteria.add(Restrictions.eq("code", model.getCode()).ignoreCase());
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount > 0).build();
    }
}
