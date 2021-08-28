package com.app.api.controllers.category;

import com.app.api.BaseController;
import com.app.dao.category.ReceiptFlowDao;
import com.app.model.BaseResponse;
import com.app.model.category.ReceiptFlowModel;
import com.app.model.category.ReceiptFlowModel.ReceiptFlowResponse;
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
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.math.BigInteger;
import java.util.List;

@Path("receipt_flows")
@Tag(name = "ReceiptFlows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReceiptFlowController extends BaseController {
    ReceiptFlowDao receiptFlowDao = new ReceiptFlowDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of ReceiptFlows",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = ReceiptFlowResponse.class)))}
    )
    public Response getDeliveryBillFlowList(
            @Parameter(description="receipt Id") @QueryParam("receiptId") Long receiptId,
            @Parameter(description="Name", example="nikon%") @QueryParam("name") String name,
            @Parameter(description="Page No, Starts from 1 ", example="1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description="Items in each page", example="5") @DefaultValue("5") @QueryParam("page-size") int pageSize
    ) {
        ReceiptFlowResponse resp = new ReceiptFlowResponse();
        if (receiptId == null) {
            receiptId = 0l;
        }
        List<ReceiptFlowModel> modelList = receiptFlowDao.getList(page, pageSize, receiptId);
        BigInteger total = receiptFlowDao.getReceiptFlowCount(receiptId);
        resp.setList(modelList);
        resp.setTotal(total.intValue());
        resp.setPageStats(total.intValue(),pageSize, page,"");
        resp.setSuccessMessage("List of DeliveryBillFlowModel and nested details " + (receiptId>0 ? "- Customer:"+receiptId:""));
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Add a receiptFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addReceiptFlow(ReceiptFlowModel receiptFlow) {
        BaseResponse resp = new BaseResponse();
        try {
            receiptFlowDao.beginTransaction();
            receiptFlowDao.save(receiptFlow);
            receiptFlowDao.commitTransaction();
            resp.setSuccessMessage(String.format("Thêm mới bản ghi thành công id: %s ", receiptFlow.getReceiptFlowId()));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể thêm mới bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a ReceiptFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateReceiptFlow(ReceiptFlowModel receiptFlow) {
        BaseResponse resp = new BaseResponse();
        try {
            ReceiptFlowModel foundProd  = receiptFlowDao.getById(receiptFlow.getReceiptFlowId());
            if (foundProd != null) {
                receiptFlowDao.beginTransaction();
                receiptFlowDao.update(receiptFlow);
                receiptFlowDao.commitTransaction();
                resp.setSuccessMessage(String.format("Sửa bản ghi thành công (id:%s)", receiptFlow.getReceiptFlowId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", receiptFlow.getReceiptFlowId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể sửa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{receiptFlowId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Delete a receiptFlow",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteReceiptFlow(@Parameter(description="ReceiptFlow Id", example="601") @PathParam("receiptFlowId") Long receiptFlowId) {
        BaseResponse resp = new BaseResponse();
        try {
            ReceiptFlowModel foundProd  = receiptFlowDao.getById(receiptFlowId);
            if (foundProd==null) {
                resp.setErrorMessage(String.format("Bản ghi không tồn tại (id:%s)", receiptFlowId));
                return Response.ok(resp).build();
            } else {
                receiptFlowDao.beginTransaction();
                receiptFlowDao.delete(receiptFlowId);
                receiptFlowDao.commitTransaction();
                resp.setSuccessMessage(String.format("Xóa bản ghi thành công (id:%s)", receiptFlowId));
                return Response.ok(resp).build();
            }
//            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Không thể xóa bản ghi - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("receipt_flow")
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of receipt_flows",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = ReceiptFlowResponse.class)))}
    )
    public Response getReceiptFlow(
            @Parameter(description="receipt Id") @QueryParam("receiptId") Long receiptId,
            @Parameter(description="supplies Id") @QueryParam("suppliesId") Long suppliesId
    ) {
        Criteria criteria = receiptFlowDao.getSession().createCriteria(ReceiptFlowModel.class);
        // Execute the Main Query
        if (receiptId != null){
            criteria.add(Restrictions.eq("receiptId",  receiptId ));
        }
        if (suppliesId != null){
            criteria.add(Restrictions.eq("suppliesId",  suppliesId ));
        }
        criteria.setProjection(null);
        List<ReceiptFlowModel> receiptFlowList = criteria.list();

        return Response.ok(receiptFlowList).build();
    }
}
