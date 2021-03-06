package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.category.WarehouseCardFlowDao;
import com.app.model.BaseResponse;
import com.app.model.category.WarehouseCardFlowModel;
import com.app.model.category.WarehouseCardFlowModel.WarehouseCardFlowResponse;
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
import org.hibernate.HibernateException;

import java.math.BigInteger;
import java.util.List;

@Path("warehouse_card_flows")
@Tag(name = "WarehouseCardFlows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarehouseCardFlowController extends BaseController {

    WarehouseCardFlowDao warehouseCardFlowDao = new WarehouseCardFlowDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of warehouseCardFlows",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseCardFlowResponse.class)))}
    )
    public Response getWarehouseCardFlowList(
            @Parameter(description="WarehouseCard Id") @QueryParam("warehouseCardId") Long warehouseCardId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="5") @DefaultValue("5") @QueryParam("page-size") int pageSize
    ) {
        WarehouseCardFlowResponse resp = new WarehouseCardFlowResponse();
        try {
            if (warehouseCardId == null) {
                warehouseCardId = 0l;
            }
            List<WarehouseCardFlowModel> cardFlowModels = warehouseCardFlowDao.getList(page, pageSize, warehouseCardId);
            BigInteger total = warehouseCardFlowDao.getWarehouseCardFlowCount(warehouseCardId);
            resp.setList(cardFlowModels);
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
            summary = "Add a warehouseCardFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addWarehouseCardFlow(WarehouseCardFlowModel warehouseCardFlow) {
        BaseResponse resp = new BaseResponse();
        try {
            warehouseCardFlowDao.beginTransaction();
            warehouseCardFlowDao.save(warehouseCardFlow);
            warehouseCardFlowDao.commitTransaction();
            resp.setSuccessMessage(String.format("WarehouseCardFlow Added - New WarehouseCardFlow ID : %s ", warehouseCardFlow.getWarehouseCardFlowId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add WarehouseCardFlow - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
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
                resp.setSuccessMessage(String.format("WarehouseCardFlow Updated (getWarehouseCardFlowId:%s)", warehouseCardFlow.getWarehouseCardFlowId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - WarehouseCardFlow not found (getWarehouseCardFlowId:%s)", warehouseCardFlow.getWarehouseCardFlowId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update WarehouseCardFlow - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
                resp.setErrorMessage(String.format("Cannot delete WarehouseCardFlow - Customer do not exist (id:%s)", warehouseCardFlowId));
                return Response.ok(resp).build();
            } else {
                warehouseCardFlowDao.beginTransaction();
                warehouseCardFlowDao.delete(warehouseCardFlowId);
                warehouseCardFlowDao.commitTransaction();
                resp.setSuccessMessage(String.format("WarehouseCardFlow deleted (warehouseCardFlowId:%s)", warehouseCardFlowId));
                return Response.ok(resp).build();
            }
//            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete WarehouseCardFlow - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }
}
