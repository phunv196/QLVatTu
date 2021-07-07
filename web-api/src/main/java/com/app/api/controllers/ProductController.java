package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.ProductDao;
import com.app.model.BaseResponse;
import com.app.model.product.ProductModel;
import com.app.model.product.ProductModel.ProductResponse;
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

import java.math.BigDecimal;
import java.util.List;

@Path("products")
@Tag(name = "Products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController extends BaseController {
    ProductDao productDao = new ProductDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
      summary = "Get list of products",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = ProductResponse.class)))}
    )
    public Response getProductList(
        @Parameter(description="Product Id") @QueryParam("id") int id,
        @Parameter(description="Category", schema= @Schema(allowableValues = {"Camera", "Laptop", "Tablet", "Phone"})) @QueryParam("category") String category,
        @Parameter(description="Name", example="nikon%") @QueryParam("name") String productName,
        @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
        @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = productDao.createCriteria(ProductModel.class);

        if (id > 0){
            criteria.add(Restrictions.eq("id",  id ));
        }
        if (StringUtils.isNotBlank(productName)){
            criteria.add(Restrictions.like("productName, Use % for wildcard ",  productName ).ignoreCase());
        }
        if (StringUtils.isNotBlank(category)){
            criteria.add(Restrictions.like("category, Use % for wildcard ",  category ).ignoreCase());
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
        List<ProductModel> productList = criteria.list();

        ProductResponse resp = new ProductResponse();
        resp.setList(productList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of products");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Add a Product",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addProduct(ProductModel prod) {
        BaseResponse resp = new BaseResponse();
        try {
            productDao.beginTransaction();
            productDao.save(prod);
            productDao.commitTransaction();
            resp.setSuccessMessage(String.format("Product Added - New Product ID : %s ", prod.getId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add Product - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Update a Product",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateProduct(ProductModel prod) {
        BaseResponse resp = new BaseResponse();
        try {
            ProductModel foundProd  = productDao.getById(prod.getId());
            if (foundProd != null) {
                productDao.beginTransaction();
                productDao.update(prod);
                productDao.commitTransaction();
                resp.setSuccessMessage(String.format("Product Updated (id:%s)", prod.getId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - Product not found (id:%s)", prod.getId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update Product - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{productId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Delete a Product",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteProduct(@Parameter(description="Product Id", example="601") @PathParam("productId") Integer productId) {
        BaseResponse resp = new BaseResponse();
        try {
            BigDecimal referenceCount = productDao.getReferenceCount(productId);
            if (referenceCount.intValue() > 0) {
                resp.setErrorMessage("Cannot remove product as this product is still being referred in orders");
                return Response.ok(resp).build();
            } else {
                ProductModel foundProd  = productDao.getById(productId);
                if (foundProd==null) {
                    resp.setErrorMessage(String.format("Cannot delete product - Customer do not exist (id:%s)", productId));
                    return Response.ok(resp).build();
                } else {
                    productDao.beginTransaction();
                    productDao.delete(productId);
                    productDao.commitTransaction();
                    resp.setSuccessMessage(String.format("Product deleted (id:%s)", productId));
                    return Response.ok(resp).build();
                }
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete product - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }
}
