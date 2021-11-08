package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.PositionDao;
import com.app.model.BaseResponse;
import com.app.model.position.PositionModel;
import com.app.model.position.PositionModel.PositionResponse;
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

@Path("positions")
@Tag(name = "Positions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PositionController extends BaseController {

    PositionDao positionDao = new PositionDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of positions",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = PositionResponse.class)))}
    )
    public Response getPositionList(
            @Parameter(description="Position Id") @QueryParam("positionId") int positionId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Name", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = positionDao.createCriteria(PositionModel.class);

        if (positionId > 0){
            criteria.add(Restrictions.eq("positionId",  positionId ));
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
        List<PositionModel> positionList = criteria.list();

        PositionResponse resp = new PositionResponse();
        resp.setList(positionList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of positions");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all positions",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = PositionResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = positionDao.createCriteria(PositionModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<PositionModel> positionList = criteria.list();
        PositionResponse resp = new PositionResponse();
        resp.setList(positionList);
        resp.setSuccessMessage("List of positions");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Position",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addPosition(PositionModel position) {
        BaseResponse resp = new BaseResponse();
        try {
            positionDao.beginTransaction();
            positionDao.save(position);
            positionDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công code:%s ", position.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Position",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updatePosition(PositionModel position) {
        BaseResponse resp = new BaseResponse();
        try {
            PositionModel foundProd  = positionDao.getById(position.getPositionId());
            if (foundProd != null) {
                positionDao.beginTransaction();
                positionDao.update(position);
                positionDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", position.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", position.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            e.printStackTrace();
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{positionId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Position",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deletePosition(@Parameter(description="Position Id", example="601") @PathParam("positionId") Long positionId) {
        BaseResponse resp = new BaseResponse();
        try {

            PositionModel foundProd  = positionDao.getById(positionId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", positionId));
                return Response.ok(resp).build();
            } else {
                positionDao.beginTransaction();
                positionDao.delete(positionId);
                positionDao.commitTransaction();
                resp.setSuccessMessage(String.format("Bản ghi thành công (code:%s)", foundProd.getCode()));
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
            PositionModel model
    ) {
        int recordFrom = 0;
        Criteria criteria = positionDao.createCriteria(PositionModel.class);
        if (model.getPositionId() != null){
            criteria.add(Restrictions.ne("positionId", model.getPositionId()));
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
