package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.CategoryDao;
import com.app.dao.base.CommonUtils;
import com.app.model.BaseResponse;
import com.app.model.category.CategoryModel;
import com.app.model.category.CategoryModel.CategoryResponse;
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

import java.util.ArrayList;
import java.util.List;

@Path("categorys")
@Tag(name = "Categorys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryController extends BaseController {

    CategoryDao categoryDao = new CategoryDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get list of categorys",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = CategoryResponse.class)))}
    )
    public Response getCategoryList(
            @Parameter(description="Category Id") @QueryParam("categoryId") int categoryId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Name", example="nikon%") @QueryParam("code") String code,
            @Parameter(description="parentCode", example="nikon%") @QueryParam("parentCode") String parentCode,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize
    ) {
        int recordFrom = 0;
        Criteria criteria = categoryDao.createCriteria(CategoryModel.class);

        if (categoryId > 0){
            criteria.add(Restrictions.eq("categoryId",  categoryId ));
        }
        if (StringUtils.isNotBlank(name)){
            criteria.add(Restrictions.like("name", "%"+name+"%" ).ignoreCase());
        }
        if (StringUtils.isNotBlank(parentCode)){
            criteria.add(Restrictions.eq("parentCode", parentCode));
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
        List<CategoryModel> categoryList = criteria.list();

        CategoryResponse resp = new CategoryResponse();
        resp.setList(categoryList);
        resp.setPageStats(rowCount.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of categorys");
        return Response.ok(resp).build();
    }

    @GET
    @Path("all")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Get all categorys",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = CategoryResponse.class)))}
    )
    public Response getAll(
    ) {
        Criteria criteria = categoryDao.createCriteria(CategoryModel.class);
        // Execute the Main Query
        criteria.setProjection(null);
        List<CategoryModel> categoryList = criteria.list();
        CategoryResponse resp = new CategoryResponse();
        resp.setList(categoryList);
        resp.setSuccessMessage("List of categorys");
        return Response.ok(resp).build();
    }

    @GET
    @Path("getListByParentCode/{parentCode}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "getListIsNotParentCode",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = CategoryResponse.class)))}
    )
    public Response getListByParentCode(
            @Parameter(description="parentCode", example="601") @PathParam("parentCode") String parentCode
    ) {
        Criteria criteria = categoryDao.createCriteria(CategoryModel.class);
        criteria.add(Restrictions.eq("parentCode", parentCode));
        // Execute the Main Query
        criteria.setProjection(null);
        List<CategoryModel> categoryList = criteria.list();
        CategoryResponse resp = new CategoryResponse();
        resp.setList(categoryList);
        resp.setSuccessMessage("List of categorys");
        return Response.ok(resp).build();
    }

    @GET
    @Path("getListIsNotParentCode")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "getListIsNotParentCode",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = CategoryResponse.class)))}
    )
    public Response getListIsNotParentCode(
            @Parameter(description="Name", example="nikon%") @QueryParam("code") String code
    ) {
        Criteria criteria = categoryDao.createCriteria(CategoryModel.class);
        criteria.add(Restrictions.isNull("parentCode"));
        if (StringUtils.isNotBlank(code)){
            criteria.add(Restrictions.ne("code", code ).ignoreCase());

            Criteria criteriaCheck = categoryDao.createCriteria(CategoryModel.class);
            criteriaCheck.add(Restrictions.eq("parentCode", code));
            // Execute the Main Query
            criteriaCheck.setProjection(null);
            List<CategoryModel> categoryCheckList = criteriaCheck.list();
            if (categoryCheckList != null && categoryCheckList.size() > 0 ) {
                return Response.ok(new ArrayList<>()).build();
            }
        }

        // Execute the Main Query
        criteria.setProjection(null);
        List<CategoryModel> categoryList = criteria.list();
        CategoryResponse resp = new CategoryResponse();
        resp.setList(categoryList);
        resp.setSuccessMessage("List of categorys");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a Category",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addQuatlity(CategoryModel qual) {
        BaseResponse resp = new BaseResponse();
        try {
            categoryDao.beginTransaction();
            categoryDao.save(qual);
            categoryDao.commitTransaction();
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
            summary = "Update a Category",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateCategory(CategoryModel qual) {
        BaseResponse resp = new BaseResponse();
        try {
            CategoryModel foundProd  = categoryDao.getById(qual.getCategoryId());
            if (foundProd != null) {
                categoryDao.beginTransaction();
                categoryDao.update(qual);
                categoryDao.commitTransaction();
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
    @Path("{categoryId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a Category",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteCategory(@Parameter(description="Category Id", example="601") @PathParam("categoryId") Long categoryId) {
        BaseResponse resp = new BaseResponse();
        try {
            CategoryModel foundProd  = categoryDao.getById(categoryId);

            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", categoryId));
                return Response.ok(resp).build();
            } else {
                Criteria criteria = categoryDao.createCriteria(CategoryModel.class);
                criteria.add(Restrictions.eq("parentCode", foundProd.getParentCode()));
                // Execute the Main Query
                criteria.setProjection(null);
                List<CategoryModel> categoryList = criteria.list();
                if (categoryList != null) {
                    resp.setErrorMessage(String.format("Bản ghi đã được sử dụng không thể xóa (id:%s)", categoryId));
                    return Response.ok(resp).build();
                }
                categoryDao.beginTransaction();
                categoryDao.delete(categoryId);
                categoryDao.commitTransaction();
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
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getByCode(
            CategoryModel model
    ) {
        Criteria criteria = categoryDao.createCriteria(CategoryModel.class);
        if (model.getCategoryId() != null){
            criteria.add(Restrictions.ne("categoryId", model.getCategoryId()));
        }
        if (!CommonUtils.isNullOrEmpty(model.getCode())){
            criteria.add(Restrictions.eq("code", model.getCode()).ignoreCase());
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount != null && rowCount > 0).build();
    }
}
