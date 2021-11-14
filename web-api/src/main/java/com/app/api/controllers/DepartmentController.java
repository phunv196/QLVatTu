package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.DepartmentDao;
import com.app.model.BaseResponse;
import com.app.model.department.DepartmentModel;
import com.app.model.department.DepartmentModel.DepartmentResponse;
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

@Path("department")
@Tag(name = "Department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentController extends BaseController {

    DepartmentDao departmentDao = new DepartmentDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of Departments",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = DepartmentResponse.class)))}
    )
    public Response getDepartmentList(
            @Parameter(description="Department Id") @QueryParam("departmentId") int departmentId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="code", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="email", example="nikon%") @QueryParam("email") String email,
            @Parameter(description="phone", example="nikon%") @QueryParam("phone") String phone,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = departmentDao.createCriteria(DepartmentModel.class);

        if (departmentId > 0){
            criteria.add(Restrictions.eq("departmentId",  departmentId ));
        }
        if (StringUtils.isNotBlank(name)){
            criteria.add(Restrictions.like("name", "%"+name+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(code)){
            criteria.add(Restrictions.like("code", "%"+code+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(email)){
            criteria.add(Restrictions.like("email", "%"+email+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(phone)){
            criteria.add(Restrictions.like("phone", "%"+phone+"%" ).ignoreCase());
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
        List<DepartmentModel> departmentList = criteria.list();

        DepartmentResponse resp = new DepartmentResponse();
        resp.setList(departmentList);
        resp.setPageStats(rowCount == null ? 0 : rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of departments");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all departments",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = DepartmentResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = departmentDao.createCriteria(DepartmentModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<DepartmentModel> departmentList = criteria.list();
        DepartmentResponse resp = new DepartmentResponse();
        resp.setList(departmentList);
        resp.setSuccessMessage("List of departments");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Department",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addDepartment(DepartmentModel dep) {
        BaseResponse resp = new BaseResponse();
        try {
            if(isNullOrEmpty(dep.getName())) {
                resp.setErrorMessage(String.format("Tên chất lượng chưa nhập (dep.getName():%s)", dep.getName()));
                return Response.ok(resp).build();
            }
            if(isNullOrEmpty(dep.getCode())) {
                resp.setErrorMessage("Mã chất lượng chưa nhập");
                return Response.ok(resp).build();
            }
            departmentDao.beginTransaction();
            departmentDao.save(dep);
            departmentDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản chi thành công code: %s ", dep.getCode()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Department",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateDepartment(DepartmentModel dep) {
        BaseResponse resp = new BaseResponse();
        try {
            DepartmentModel foundProd  = departmentDao.getById(dep.getDepartmentId());
            if (foundProd != null) {
                departmentDao.beginTransaction();
                departmentDao.update(dep);
                departmentDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (code:%s)", dep.getCode()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (coe:%s)", dep.getCode()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{departmentId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Department",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteDepartment(@Parameter(description="Department Id", example="601") @PathParam("departmentId") Long departmentId) {
        BaseResponse resp = new BaseResponse();
        try {
            DepartmentModel foundProd  = departmentDao.getById(departmentId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", departmentId));
                return Response.ok(resp).build();
            } else {
                departmentDao.beginTransaction();
                departmentDao.delete(departmentId);
                departmentDao.commitTransaction();
                resp.setSuccessMessage(String.format("xóa bản ghi thành công (code:%s)", foundProd.getCode()));
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
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getByCode(
            DepartmentModel model
    ) {
        Criteria criteria = departmentDao.createCriteria(DepartmentModel.class);
        if (model.getDepartmentId() != null){
            criteria.add(Restrictions.ne("departmentId", model.getDepartmentId()));
        }
        if (!CommonUtils.isNullOrEmpty(model.getCode())){
            criteria.add(Restrictions.eq("code", model.getCode()).ignoreCase());
        }
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount > 0).build();
    }
}
