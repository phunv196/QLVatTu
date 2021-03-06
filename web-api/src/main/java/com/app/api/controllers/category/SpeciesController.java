package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.category.SpeciesDao;
import com.app.model.BaseResponse;
import com.app.model.category.QualityModel;
import com.app.model.category.RoleModel;
import com.app.model.category.SpeciesModel;
import com.app.model.category.SpeciesModel.SpeciesResponse;
import com.app.model.category.SuppliesModel;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

@Path("species")
@Tag(name = "Species")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SpeciesController extends BaseController {

    SpeciesDao speciesDao = new SpeciesDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of species",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SpeciesResponse.class)))}
    )
    public Response getSpeciesList(
            @Parameter(description="Species Id") @QueryParam("speciesId") int speciesId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Code", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = speciesDao.createCriteria(SpeciesModel.class);

        if (speciesId > 0){
            criteria.add(Restrictions.eq("speciesId",  speciesId ));
        }
        if (StringUtils.isNotBlank(name)){
            criteria.add(Restrictions.like("name", "%"+name+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(code)){
            criteria.add(Restrictions.like("code", "%"+code+"%" ).ignoreCase());
        }
        if (page<=0){ page = 1; }
        if (pageSize <= 0 || pageSize > 1000){ pageSize = 20; }
        recordFrom = (page-1) * pageSize;

        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();

        // Execute the Main Query
        criteria.setProjection(null);
        criteria.setFirstResult( (int) (long)recordFrom);
        criteria.setMaxResults(  (int) (long)pageSize);
        List<SpeciesModel> specieList = criteria.list();

        SpeciesResponse resp = new SpeciesResponse();
        resp.setList(specieList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of species");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get all species",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SpeciesResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = speciesDao.createCriteria(SpeciesModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<SpeciesModel> specieList = criteria.list();
        SpeciesResponse resp = new SpeciesResponse();
        resp.setList(specieList);
        resp.setSuccessMessage("List of species");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Species",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addSpecies(SpeciesModel species) {
        BaseResponse resp = new BaseResponse();
        try {
            speciesDao.beginTransaction();
            speciesDao.save(species);
            speciesDao.commitTransaction();
            resp.setSuccessMessage(String.format("Species Added - New Species ID : %s ", species.getSpeciesId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add Species - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Species",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateSpecies(SpeciesModel species) {
        BaseResponse resp = new BaseResponse();
        try {
            SpeciesModel foundProd  = speciesDao.getById(species.getSpeciesId());
            if (foundProd != null) {
                speciesDao.beginTransaction();
                speciesDao.update(species);
                speciesDao.commitTransaction();
                resp.setSuccessMessage(String.format("Species Updated (getSpeciesId:%s)", species.getSpeciesId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - Species not found (getSpeciesId:%s)", species.getSpeciesId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update Species - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{speciesId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Species",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteSpecies(@Parameter(description="Species Id", example="601") @PathParam("speciesId") Long speciesId) {
        BaseResponse resp = new BaseResponse();
        try {
            SpeciesModel foundProd  = speciesDao.getById(speciesId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Cannot delete Species - Customer do not exist (id:%s)", speciesId));
                return Response.ok(resp).build();
            } else {
                speciesDao.beginTransaction();
                speciesDao.delete(speciesId);
                speciesDao.commitTransaction();
                resp.setSuccessMessage(String.format("Species deleted (speciesId:%s)", speciesId));
                return Response.ok(resp).build();
            }
//            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Species - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @Path("byCode")
    @RolesAllowed({"ADMIN"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getByCode(
            SpeciesModel model
    ) {
        int recordFrom = 0;
        Criteria criteria = speciesDao.createCriteria(SpeciesModel.class);
        if (model.getSpeciesId() != null){
            criteria.add(Restrictions.ne("speciesId", model.getSpeciesId()));
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
