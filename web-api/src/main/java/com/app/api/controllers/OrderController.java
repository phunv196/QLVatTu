package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.OrderDao;
import com.app.model.BaseResponse;
import com.app.model.order.OrderItemModel;
import com.app.model.order.OrderModel;
import com.app.model.order.OrderWithNestedDetailModel;
import com.app.model.order.OrderWithNestedDetailResponse;
import com.app.model.user.UserModel;
import com.app.util.Constants;
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
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Path("")
@Tag(name = "Orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    OrderDao orderDao = new OrderDao();

    @GET
    @Path("orders")
    @RolesAllowed({"ADMIN", "CUSTOMER", "SUPPORT"})
    @Operation(
      summary = "Get Details of an order with nested order-lines",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = OrderWithNestedDetailResponse.class)))}
    )
    public Response getOrderDetail(
        @Parameter(description = "Order Id") @QueryParam("order-id") int orderId,
        @Parameter(description = "Customer Id") @QueryParam("customer-id") int customerId,
        @Parameter(description = "Payment Type", schema = @Schema(allowableValues = {"Check","Cash","Card"})) @QueryParam("payment-type") String paymentType,
        @Parameter(description = "Order Status", schema = @Schema(allowableValues = {"On Hold","Shipped","Complete", "New"})) @QueryParam("order-status") String orderStatus,
        @Parameter(description = "Page No, Starts from 1 ", example = "1") @DefaultValue("1") @QueryParam("page") int page,
        @Parameter(description = "Items in each page", example = "20") @DefaultValue("1000") @QueryParam("page-size") int pageSize
    ) {

        OrderWithNestedDetailResponse resp = new OrderWithNestedDetailResponse();

        UserModel userFromToken = (UserModel) securityContext.getUserPrincipal();  // securityContext is defined in BaseController
        //Customers can query their own cart only
        if (userFromToken.getRole().equalsIgnoreCase(Constants.UserRoleConstants.ROLE_CUSTOMER)){
        }
        try {
            List<OrderWithNestedDetailModel> orderWithOrderLinesList = orderDao.getWithOrderLines(page, pageSize, orderId, customerId, paymentType, orderStatus);
            BigInteger total = orderDao.getOrderCount(orderId, customerId, paymentType, orderStatus);

            resp.setList(orderWithOrderLinesList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(),pageSize, page,"");
            resp.setSuccessMessage("List of Orders and nested details " + (customerId>0 ? "- Customer:"+customerId:""));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Order - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("orders/{orderId}")
    @RolesAllowed({"USER"})
    @Operation(
      summary = "Delete an order and all its line-items",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteOrder(@Parameter(description = "Order Id", example="4002") @PathParam("orderId") Integer orderId) {
        BaseResponse resp = new BaseResponse();
        if (orderId <= 0 ) {
            resp.setErrorMessage("Must provide a valid Order-id ");
            return Response.ok(resp).build();
        }
        try {
            OrderModel foundOrder  = orderDao.getById(orderId);
            if (foundOrder==null){
                resp.setErrorMessage(String.format("Cannot delete order - Order do not exist (id:%s)", orderId));
                return Response.ok(resp).build();
            } else {
                orderDao.beginTransaction();
                orderDao.delete(orderId);
                orderDao.commitTransaction();
                resp.setSuccessMessage(String.format("Order deleted (id:%s)", orderId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Order - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("/order-item/{orderId}/{productId}")
    @RolesAllowed({"USER"})
    @Operation(
      summary = "Delete an order line-item",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteOrderItem(
        @Parameter(description = "Order Id") @PathParam("orderId") int orderId,
        @Parameter(description = "Order-Item Id") @PathParam("productId") int productId
    ) {
        BaseResponse resp = new BaseResponse();

        if (orderId <= 0 || productId <= 0) {
            resp.setErrorMessage("Must provide a valid Order-id and Product-id");
            return Response.ok(resp).build();
        }
        try {
            orderDao.beginTransaction();
            orderDao.deleteOrderLine(orderId, productId);
            orderDao.commitTransaction();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Order-Item - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
        resp.setSuccessMessage("Deleted");
        return Response.ok(resp).build();
    }

    @DELETE
    @Path("/order-item/{orderId}")
    @RolesAllowed({"USER"})
    @Operation(
      summary = "Delete all order line-item by product-id",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteOrderItems(
      @Parameter(description = "Order Id") @PathParam("orderId") int orderId,
      @Parameter(description = "Comma separated list of product ids") @QueryParam("product-ids") String commaSeparatedIds
    ) {
        BaseResponse resp = new BaseResponse();
        String[] productIdArr = commaSeparatedIds.split(",");
        ArrayList<Integer> productIdList = new ArrayList<Integer>();
        for (String productIdStr : productIdArr) {
            productIdList.add(Integer.parseInt(productIdStr.trim()));
        }
        try {
            orderDao.beginTransaction();
            orderDao.deleteOrderLines(orderId, productIdList);
            orderDao.commitTransaction();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete Order-Item - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
        resp.setSuccessMessage("Deleted all order lines");
        return Response.ok(resp).build();
    }

    @POST
    @Path("/order-item")
    @RolesAllowed({"ADMIN", "CUSTOMER", "SUPPORT"})
    @Operation(
      summary = "Add an order line-item",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addOrderItem(OrderItemModel orderItem) {
        BaseResponse resp = new BaseResponse();
        try {
            orderDao.beginTransaction();
            orderDao.save(orderItem);
            orderDao.commitTransaction();
        } catch (HibernateException | ConstraintViolationException  e) {
            resp.setErrorMessage("Cannot add Order-Item - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }
}
