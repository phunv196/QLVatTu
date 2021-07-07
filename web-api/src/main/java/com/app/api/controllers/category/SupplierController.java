package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.category.SupplierDao;
import com.app.model.BaseResponse;
import com.app.model.category.SupplierModel;
import com.app.model.category.SupplierModel.SupplierResponse;
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

@Path("supplier")
@Tag(name = "Supplier")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SupplierController extends BaseController {

    SupplierDao supplierDao = new SupplierDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SupplierResponse.class)))}
    )
    public Response getSupplierList(
            @Parameter(description="Supplier Id") @QueryParam("supplierId") int supplierId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="code", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="email", example="nikon%") @QueryParam("email") String email,
            @Parameter(description="phone", example="nikon%") @QueryParam("phone") String phone,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = supplierDao.createCriteria(SupplierModel.class);

        if (supplierId > 0){
            criteria.add(Restrictions.eq("supplierId",  supplierId ));
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
        List<SupplierModel> supplierList = criteria.list();

        SupplierResponse resp = new SupplierResponse();
        resp.setList(supplierList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of suppliers");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get all Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = SupplierResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = supplierDao.createCriteria(SupplierModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<SupplierModel> supplierList = criteria.list();

        SupplierResponse resp = new SupplierResponse();
        resp.setList(supplierList);
        resp.setSuccessMessage("List of suppliers");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addSupplier(SupplierModel supplier) {
        BaseResponse resp = new BaseResponse();
        try {
            supplierDao.beginTransaction();
            supplierDao.save(supplier);
            supplierDao.commitTransaction();
            resp.setSuccessMessage(String.format("Supplier Added - New Supplier ID : %s ", supplier.getSupplierId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add Supplier - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateSupplier(SupplierModel supplier) {
        BaseResponse resp = new BaseResponse();
        try {
            SupplierModel foundProd  = supplierDao.getById(supplier.getSupplierId());
            if (foundProd != null) {
                supplierDao.beginTransaction();
                supplierDao.update(supplier);
                supplierDao.commitTransaction();
                resp.setSuccessMessage(String.format("Supplier Updated (getSupplierId:%s)", supplier.getSupplierId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - Supplier not found (getSupplierId:%s)", supplier.getSupplierId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update Supplier - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{supplierId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Supplier",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteSupplier(@Parameter(description="Supplier Id", example="601") @PathParam("supplierId") Long supplierId) {
        BaseResponse resp = new BaseResponse();
        try {
            SupplierModel foundProd  = supplierDao.getById(supplierId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Cannot delete Supplier - Customer do not exist (id:%s)", supplierId));
                return Response.ok(resp).build();
            } else {
                supplierDao.beginTransaction();
                supplierDao.delete(supplierId);
                supplierDao.commitTransaction();
                resp.setSuccessMessage(String.format("Supplier deleted (supplierId:%s)", supplierId));
                return Response.ok(resp).build();
            }
//            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Supplier - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }
}
