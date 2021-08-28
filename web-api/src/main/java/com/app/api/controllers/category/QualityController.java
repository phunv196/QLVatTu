package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.category.QualityDao;
import com.app.model.BaseResponse;
import com.app.model.category.PositionModel;
import com.app.model.category.QualityModel;
import com.app.model.category.QualityModel.QualityResponse;
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

import static com.app.dao.base.CommonUtils.isNullOrEmpty;

@Path("qualitys")
@Tag(name = "Qualitys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QualityController extends BaseController {

    QualityDao qualityDao = new QualityDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of qualitys",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = QualityResponse.class)))}
    )
    public Response getQualityList(
            @Parameter(description="Quality Id") @QueryParam("qualityId") int qualityId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Name", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = qualityDao.createCriteria(QualityModel.class);

        if (qualityId > 0){
            criteria.add(Restrictions.eq("qualityId",  qualityId ));
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
        List<QualityModel> qualityList = criteria.list();

        QualityResponse resp = new QualityResponse();
        resp.setList(qualityList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of qualitys");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get all qualitys",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = QualityResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = qualityDao.createCriteria(QualityModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<QualityModel> qualityList = criteria.list();
        QualityResponse resp = new QualityResponse();
        resp.setList(qualityList);
        resp.setSuccessMessage("List of qualitys");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Quality",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addQuatlity(QualityModel qual) {
        BaseResponse resp = new BaseResponse();
        try {
            qualityDao.beginTransaction();
            qualityDao.save(qual);
            qualityDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công code: %s ", qual.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Quality",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateQuality(QualityModel qual) {
        BaseResponse resp = new BaseResponse();
        try {
            QualityModel foundProd  = qualityDao.getById(qual.getQualityId());
            if (foundProd != null) {
                qualityDao.beginTransaction();
                qualityDao.update(qual);
                qualityDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", qual.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (code:%s)", qual.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{qualityId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Quality",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteQuality(@Parameter(description="Quality Id", example="601") @PathParam("qualityId") Long qualityId) {
        BaseResponse resp = new BaseResponse();
        try {
            QualityModel foundProd  = qualityDao.getById(qualityId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", qualityId));
                return Response.ok(resp).build();
            } else {
                qualityDao.beginTransaction();
                qualityDao.delete(qualityId);
                qualityDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (code:%s)", foundProd.getCode()));
                return Response.ok(resp).build();
            }
//            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể xóa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
            QualityModel model
    ) {
        int recordFrom = 0;
        Criteria criteria = qualityDao.createCriteria(QualityModel.class);
        if (model.getQualityId() != null){
            criteria.add(Restrictions.ne("qualityId", model.getQualityId()));
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
