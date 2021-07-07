package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.category.FactoryDao;
import com.app.model.BaseResponse;
import com.app.model.category.FactoryModel;
import com.app.model.category.FactoryModel.FactoryResponse;
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

import java.math.BigInteger;
import java.util.List;

@Path("factorys")
@Tag(name = "Factorys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FactoryController extends BaseController {

    FactoryDao factoryDao = new FactoryDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of factorys",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = FactoryResponse.class)))}
    )
    public Response getFactoryList(
            @Parameter(description = "Order Id") @QueryParam("factoryId") Long factoryId,
            @Parameter(description = "Page No, Starts from 1 ", example = "1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description = "Items in each page", example = "20") @DefaultValue("1000") @QueryParam("page-size") int pageSize
    ) {
        FactoryResponse resp = new FactoryResponse();
        try {
            if (factoryId == null) {
                factoryId = 0l;
            }
            List<FactoryModel> modelList = factoryDao.getList(page, pageSize, factoryId);
            BigInteger total = factoryDao.getFactoryCount(factoryId);
            resp.setList(modelList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(),pageSize, page,"");
            resp.setSuccessMessage("List of factory and nested details " + (factoryId>0 ? "- factory:"+factoryId:""));
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
            summary = "Get list of factorys",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = FactoryResponse.class)))}
    )
    public Response getAll(
            @Parameter(description="Factory Id") @QueryParam("factoryId") int factoryId
    ) {
        Criteria criteria = factoryDao.createCriteria(FactoryModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<FactoryModel> factoryList = criteria.list();
        FactoryResponse resp = new FactoryResponse();
        resp.setList(factoryList);
        resp.setSuccessMessage("List of factorys");
        return Response.ok(resp).build();
    }

    @GET
    @Path("{factoryId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Factory",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getById(@Parameter(description="Factory Id", example="601") @PathParam("factoryId") Long factoryId) {
        BaseResponse resp = new BaseResponse();
        try {
            FactoryModel foundProd  = factoryDao.getById(factoryId);
            return Response.ok(foundProd).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Factory - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Factory",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addFactory(FactoryModel factory) {
        BaseResponse resp = new BaseResponse();
        try {
            factoryDao.beginTransaction();
            factoryDao.save(factory);
            factoryDao.commitTransaction();
            resp.setSuccessMessage(String.format("Factory Added - New Factory ID : %s ", factory.getFactoryId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add Factory - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Factory",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateFactory(FactoryModel factory) {
        BaseResponse resp = new BaseResponse();
        try {
            FactoryModel foundProd  = factoryDao.getById(factory.getFactoryId());
            if (foundProd != null) {
                factoryDao.beginTransaction();
                factoryDao.update(factory);
                factoryDao.commitTransaction();
                resp.setSuccessMessage(String.format("Factory Updated (getFactoryId:%s)", factory.getFactoryId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - Factory not found (getFactoryId:%s)", factory.getFactoryId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update Factory - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{factoryId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Factory",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteFactory(@Parameter(description="Factory Id", example="601") @PathParam("factoryId") Long factoryId) {
        BaseResponse resp = new BaseResponse();
        try {
            FactoryModel foundProd  = factoryDao.getById(factoryId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Cannot delete Factory - Customer do not exist (id:%s)", factoryId));
                return Response.ok(resp).build();
            } else {
                factoryDao.beginTransaction();
                factoryDao.delete(factoryId);
                factoryDao.commitTransaction();
                resp.setSuccessMessage(String.format("Factory deleted (factoryId:%s)", factoryId));
                return Response.ok(resp).build();
            }
//            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Factory - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }
}
