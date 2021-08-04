package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.base.CommonUtils;
import com.app.dao.category.WarehouseDao;
import com.app.model.BaseResponse;
import com.app.model.category.QualityModel;
import com.app.model.category.SuppliesModel;
import com.app.model.category.WarehouseModel;
import com.app.model.category.WarehouseModel.WarehouseResponse;
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

@Path("warehouses")
@Tag(name = "Warehouses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WarehouseController extends BaseController {

    WarehouseDao warehouseDao = new  WarehouseDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of warehouses",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseResponse.class)))}
    )
    public Response getWarehouseList(
            @Parameter(description="Warehouse Id") @QueryParam("warehouseId") int warehouseId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="code", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="email", example="nikon%") @QueryParam("email") String email,
            @Parameter(description="phone", example="nikon%") @QueryParam("phone") String phone,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = warehouseDao.createCriteria(WarehouseModel.class);

        if (warehouseId > 0){
            criteria.add(Restrictions.eq("warehouseId",  warehouseId ));
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
        List<WarehouseModel> warehouseList = criteria.list();

        WarehouseResponse resp = new WarehouseResponse();
        resp.setList(warehouseList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of warehouse");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get all warehouses",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = WarehouseResponse.class)))}
    )
    public Response getWarehouseList(
    ) {
        Criteria criteria = warehouseDao.createCriteria(WarehouseModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<WarehouseModel> warehouseList = criteria.list();
        WarehouseResponse resp = new WarehouseResponse();
        resp.setList(warehouseList);
        resp.setSuccessMessage("List of warehouse");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a warehouse",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addWarehouse(WarehouseModel warehouse) {
        BaseResponse resp = new BaseResponse();
        try {
            warehouseDao.beginTransaction();
            warehouseDao.save(warehouse);
            warehouseDao.commitTransaction();
            resp.setSuccessMessage(String.format("Warehouse Added - New Warehouse ID : %s ", warehouse.getWarehouseId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add Warehouse - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Warehouse",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateWarehouse(WarehouseModel warehouse) {
        BaseResponse resp = new BaseResponse();
        try {
            WarehouseModel foundProd  = warehouseDao.getById(warehouse.getWarehouseId());
            if (foundProd != null) {
                warehouseDao.beginTransaction();
                warehouseDao.update(warehouse);
                warehouseDao.commitTransaction();
                resp.setSuccessMessage(String.format("Warehouse Updated (getWarehouseId:%s)", warehouse.getWarehouseId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - Warehouse not found (getWarehouseId:%s)", warehouse.getWarehouseId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update Warehouse - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{warehouseId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Warehouse",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteWarehouse(@Parameter(description="Warehouse Id", example="601") @PathParam("warehouseId") Long warehouseId) {
        BaseResponse resp = new BaseResponse();
        try {
            WarehouseModel foundProd  = warehouseDao.getById(warehouseId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Cannot delete Warehouse - Customer do not exist (id:%s)", warehouseId));
                return Response.ok(resp).build();
            } else {
                warehouseDao.beginTransaction();
                warehouseDao.delete(warehouseId);
                warehouseDao.commitTransaction();
                resp.setSuccessMessage(String.format("Warehouse deleted (warehouseId:%s)", warehouseId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Warehouse - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
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
            WarehouseModel warehouseModel
    ) {
        int recordFrom = 0;
        Criteria criteria = warehouseDao.createCriteria(WarehouseModel.class);
        if (warehouseModel.getWarehouseId() != null){
            criteria.add(Restrictions.ne("warehouseId", warehouseModel.getWarehouseId()));
        }
        if (!CommonUtils.isNullOrEmpty(warehouseModel.getCode())){
            criteria.add(Restrictions.eq("code", warehouseModel.getCode()).ignoreCase());
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount > 0).build();
    }
}
