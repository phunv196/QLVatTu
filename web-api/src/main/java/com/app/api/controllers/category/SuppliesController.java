package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.category.SuppliesDao;
import com.app.model.BaseResponse;
import com.app.model.category.SuppliesModel;
import com.app.model.category.SuppliesModel.SuppliesResponse;
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

@Path("supplies")
@Tag(name = "Supplies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SuppliesController extends BaseController {

    SuppliesDao suppliesDao = new SuppliesDao();

    @GET
    @Path("{suppliesId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getSupplierById(
            @Parameter(description="Supplies Id", example="601") @PathParam("suppliesId") Long suppliesId
    ) {
        SuppliesModel model = suppliesDao.getById(suppliesId);
        //model.setSuccessMessage("List of species");
        return Response.ok(model).build();
    }

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SuppliesResponse.class)))}
    )
    public Response getSuppliesList(
            @Parameter(description = "Order Id") @QueryParam("suppliesId") Long suppliesId,
            @Parameter(description = "Order Id") @QueryParam("searchCode") String searchCode,
            @Parameter(description = "Order Id") @QueryParam("searchName") String searchName,
            @Parameter(description = "Order Id") @QueryParam("searchSupplier") Long searchSupplier,
            @Parameter(description = "Order Id") @QueryParam("searchSpecies") Long searchSpecies,
            @Parameter(description = "Order Id") @QueryParam("searchFormPrice") Long searchFormPrice,
            @Parameter(description = "Order Id") @QueryParam("searchToPrice") Long searchToPrice,
            @Parameter(description = "Order Id") @QueryParam("searchQuality") Long searchQuality,
            @Parameter(description = "Order Id") @QueryParam("searchUnit") String searchUnit,
            @Parameter(description = "Page No, Starts from 1 ", example = "1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description = "Items in each page", example = "20") @DefaultValue("1000") @QueryParam("page-size") int pageSize
    ) {

        SuppliesModel.SuppliesResponse resp = new SuppliesModel.SuppliesResponse();
        try {
            if (suppliesId == null) {
                suppliesId = 0l;
            }
            if (searchSupplier == null) {
                searchSupplier = 0l;
            }
            if (searchSpecies == null) {
                searchSpecies = 0l;
            }
            if (searchQuality == null) {
                searchQuality = 0l;
            }
            if (searchFormPrice == null) {
                searchFormPrice = 0l;
            }
            if (searchToPrice == null) {
                searchToPrice = 0l;
            }
            List<SuppliesModel> modelList = suppliesDao.getList(page, pageSize, suppliesId, searchCode, searchName,
                    searchSupplier, searchSpecies, searchFormPrice, searchToPrice, searchQuality, searchUnit);
            BigInteger total = suppliesDao.getSuppliesCount(suppliesId, searchCode, searchName, searchSupplier, searchSpecies,
                    searchFormPrice, searchToPrice, searchQuality, searchUnit);
            resp.setList(modelList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(),pageSize, page,"");
            resp.setSuccessMessage("List of Supplies and nested details " + (suppliesId>0 ? "- Customer:"+suppliesId:""));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Supplies - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get all Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SuppliesResponse.class)))}
    )
    public Response getAll(

    ) {
        int recordFrom = 0;
        Criteria criteria = suppliesDao.createCriteria(SuppliesModel.class);

        // Execute the Main Query
        criteria.setProjection(null);
        List<SuppliesModel> suppliesList = criteria.list();
        SuppliesResponse resp = new SuppliesResponse();
        resp.setList(suppliesList);
        resp.setSuccessMessage("List of suppliess");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addSupplies(SuppliesModel supplies) {
        BaseResponse resp = new BaseResponse();
        try {
            suppliesDao.beginTransaction();
            suppliesDao.save(supplies);
            suppliesDao.commitTransaction();
            resp.setSuccessMessage(String.format("Supplies Added - New Supplies ID : %s ", supplies.getSuppliesId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add Supplies - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateSupplies(SuppliesModel supplies) {
        BaseResponse resp = new BaseResponse();
        try {
            SuppliesModel foundProd  = suppliesDao.getById(supplies.getSuppliesId());
            if (foundProd != null) {
                suppliesDao.beginTransaction();
                suppliesDao.update(supplies);
                suppliesDao.commitTransaction();
                resp.setSuccessMessage(String.format("Supplies Updated (getSuppliesId:%s)", supplies.getSuppliesId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - Supplies not found (getSuppliesId:%s)", supplies.getSuppliesId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update Supplies - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{suppliesId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Supplies",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteSupplies(@Parameter(description="Supplies Id", example="601") @PathParam("suppliesId") Long suppliesId) {
        BaseResponse resp = new BaseResponse();
        try {
            SuppliesModel foundProd  = suppliesDao.getById(suppliesId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Cannot delete Supplies - Customer do not exist (id:%s)", suppliesId));
                return Response.ok(resp).build();
            } else {
                suppliesDao.beginTransaction();
                suppliesDao.delete(suppliesId);
                suppliesDao.commitTransaction();
                resp.setSuccessMessage(String.format("Supplies deleted (suppliesId:%s)", suppliesId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Supplies - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }
}
