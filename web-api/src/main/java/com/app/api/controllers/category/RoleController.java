package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.category.RoleDao;
import com.app.model.BaseResponse;
import com.app.model.category.QualityModel;
import com.app.model.category.ReceiptModel;
import com.app.model.category.RoleModel;
import com.app.model.category.RoleModel.RolesResponse;
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

@Path("roles")
@Tag(name = "Roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleController extends BaseController {

    RoleDao roleDao = new RoleDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of roles",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = RoleModel.RolesResponse.class)))}
    )
    public Response getRoleList(
            @Parameter(description="Role Id") @QueryParam("roleId") int roleId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Code", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = roleDao.createCriteria(RoleModel.class);

        if (roleId > 0){
            criteria.add(Restrictions.eq("roleId",  roleId ));
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
        List<RoleModel> roleList = criteria.list();

        RolesResponse resp = new RolesResponse();
        resp.setList(roleList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of roles");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all roles",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = RoleModel.RolesResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = roleDao.createCriteria(RoleModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<RoleModel> roleList = criteria.list();
        RolesResponse resp = new RolesResponse();
        resp.setList(roleList);
        resp.setSuccessMessage("List of roles");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Role",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addRole(RoleModel role) {
        BaseResponse resp = new BaseResponse();
        try {
            roleDao.beginTransaction();
            roleDao.save(role);
            roleDao.commitTransaction();
            resp.setSuccessMessage(String.format("Role Added - New Role ID : %s ", role.getRoleId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add Role - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Role",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateRole(RoleModel role) {
        BaseResponse resp = new BaseResponse();
        try {
            RoleModel foundProd  = roleDao.getById(role.getRoleId());
            if (foundProd != null) {
                roleDao.beginTransaction();
                roleDao.update(role);
                roleDao.commitTransaction();
                resp.setSuccessMessage(String.format("Role Updated (getRoleId:%s)", role.getRoleId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - Role not found (getRoleId:%s)", role.getRoleId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update Role - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{roleId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Role",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteRole(@Parameter(description="Role Id", example="601") @PathParam("roleId") Long roleId) {
        BaseResponse resp = new BaseResponse();
        try {
            RoleModel foundProd  = roleDao.getById(roleId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Cannot delete Role - Customer do not exist (id:%s)", roleId));
                return Response.ok(resp).build();
            } else {
                roleDao.beginTransaction();
                roleDao.delete(roleId);
                roleDao.commitTransaction();
                resp.setSuccessMessage(String.format("Role deleted (id:%s)", roleId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Role - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
            RoleModel model
    ) {
        int recordFrom = 0;
        Criteria criteria = roleDao.createCriteria(RoleModel.class);
        if (model.getRoleId() != null){
            criteria.add(Restrictions.ne("roleId", model.getRoleId()));
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
