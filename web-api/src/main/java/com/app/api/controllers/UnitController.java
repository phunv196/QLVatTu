package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.UnitDao;
import com.app.model.BaseResponse;
import com.app.model.unit.UnitModel;
import com.app.model.unit.UnitModel.UnitResponse;
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

@Path("units")
@Tag(name = "Units")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UnitController extends BaseController {

    UnitDao unitDao = new UnitDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of units",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = UnitResponse.class)))}
    )
    public Response getUnitList(
            @Parameter(description="Unit Id") @QueryParam("unitId") int unitId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Code", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = unitDao.createCriteria(UnitModel.class);

        if (unitId > 0){
            criteria.add(Restrictions.eq("unitId",  unitId ));
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
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        criteria.setProjection(null);
        criteria.setFirstResult( (int) (long)recordFrom);
        criteria.setMaxResults(  (int) (long)pageSize);
        List<UnitModel> unitList = criteria.list();
        UnitResponse resp = new UnitResponse();
        resp.setList(unitList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of units");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all units",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = UnitResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = unitDao.createCriteria(UnitModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<UnitModel> unitList = criteria.list();
        UnitResponse resp = new UnitResponse();
        resp.setList(unitList);
        resp.setSuccessMessage("List of units");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Unit",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addUnit(UnitModel unit) {
        BaseResponse resp = new BaseResponse();
        try {
            unitDao.beginTransaction();
            unitDao.save(unit);
            unitDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công code: %s ", unit.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Unit",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateUnit(UnitModel unit) {
        BaseResponse resp = new BaseResponse();
        try {
            UnitModel foundProd  = unitDao.getById(unit.getUnitId());
            if (foundProd != null) {
                unitDao.beginTransaction();
                unitDao.update(unit);
                unitDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bả ghi thành công (code:%s)", unit.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", unit.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{unitId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Unit",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteUnit(@Parameter(description="Unit Id", example="601") @PathParam("unitId") Long unitId) {
        BaseResponse resp = new BaseResponse();
        try {
            UnitModel foundProd  = unitDao.getById(unitId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", unitId));
                return Response.ok(resp).build();
            } else {
                unitDao.beginTransaction();
                unitDao.delete(unitId);
                unitDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (code:%s)", foundProd.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể xóa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @Path("byCode")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getByCode(
            UnitModel model
    ) {
        Criteria criteria = unitDao.createCriteria(UnitModel.class);
        if (model.getUnitId() != null){
            criteria.add(Restrictions.ne("unitId", model.getUnitId()));
        }
        if (!CommonUtils.isNullOrEmpty(model.getCode())){
            criteria.add(Restrictions.eq("code", model.getCode()).ignoreCase());
        }
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount > 0).build();
    }
}
