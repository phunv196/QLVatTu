package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.CartDao;
import com.app.model.BaseResponse;
import com.app.model.cart.CartModel;
import com.app.model.cart.CartViewModel;
import com.app.model.cart.CartViewModel.CartViewResponse;
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

import java.util.List;


@Path("cart")
@Tag(name = "Shopping Cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    CartDao cartDao = new CartDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT", "CUSTOMER"})
    @Operation(
      summary = "Get cart Items of an User",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = CartViewResponse.class)))}
    )
    public Response getCartItemsByUser( @Parameter(description="User Id", example="customer") @QueryParam("login-name") String loginName) {
        CartViewResponse resp = new CartViewResponse();
        UserModel userFromToken = (UserModel) securityContext.getUserPrincipal();  // securityContext is defined in BaseController
        //Customers can query their own cart only
        if (userFromToken.getRole().equalsIgnoreCase(Constants.UserRoleConstants.ROLE_CUSTOMER)){
            loginName = userFromToken.getLoginName();
        }

        List<CartViewModel> cartItemList =  cartDao.listCartItemsOfUser(loginName);
        resp.setList(cartItemList);
        resp.setTotal(cartItemList.size());

        resp.setSuccessMessage("List of Cart Items for user :" + loginName);
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN", "SUPPORT", "CUSTOMER"})
    @Operation(
      summary = "Add a new product to cart",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response getCartItemsByUser(
        @Parameter(description="User Id", example="customer") @QueryParam("login-name") String loginName,
        @Parameter(description="Product Id", example="610") @QueryParam("product-id") Integer productId,
        @Parameter(description="Quantity"  , example="2") @QueryParam("quantity") Long quantity
    ) {
        BaseResponse resp = new BaseResponse();
        UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();  // securityContext is defined in BaseController

        //Customers can query their own cart only
        if (userFromToken.getRole().equalsIgnoreCase(Constants.UserRoleConstants.ROLE_CUSTOMER)){
            loginName = userFromToken.getLoginName();
        }
        CartModel cart = new CartModel(loginName,productId,quantity);
        cartDao.beginTransaction();
        cartDao.save(cart);
        cartDao.commitTransaction();
        resp.setSuccessMessage(String.format("New product(%s) added to cart of user (%s)", productId, loginName));
        return Response.ok(resp).build();
    }


    @DELETE
    @Path("{loginName}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Delete all cart Items of an User",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteCartItemsByUser(@Parameter(description="User ID", example="customer") @PathParam("loginName") String loginName) {
        BaseResponse resp = new BaseResponse();
        try {
            cartDao.beginTransaction();
            int result = cartDao.removeFromCart(loginName, null);
            cartDao.commitTransaction();
            resp.setSuccessMessage(String.format("All the Items from cart are removed for user:%s (Items Removed:%s)", loginName, result));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update cart item - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @DELETE
    @Path("{loginName}/{productId}")
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Removes a product from cart of a user",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response removeProductFromCartOfUser(
       @Parameter(description = "User ID", example="customer") @PathParam("loginName") String loginName,
       @Parameter(description = "Product ID", example="601") @PathParam("productId") int productId
    ) {
        BaseResponse resp = new BaseResponse();
        try {
            cartDao.beginTransaction();
            int result = cartDao.removeFromCart(loginName, productId);
            cartDao.commitTransaction();
            resp.setSuccessMessage(String.format("Product:%s from cart is removed for user:%s (Items Removed:%s)", productId, loginName, result));
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update cart item - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @PUT
    @Path("{loginName}/{productId}/quantity")
    @RolesAllowed({"ADMIN"})
    @Operation(
      summary = "Modify cart of a user (by adding, removing or updating) product quantities",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addCartItemsForAnUser(
        @Parameter(description = "User ID", example="customer") @PathParam("loginName") String loginName,
        @Parameter(description="Product Id", example="603") @PathParam("productId") int productId,
        @Parameter(description="Quantity", example="2") @QueryParam("quantity") Long quantity,
        @Parameter(example="add", schema = @Schema(allowableValues =  {"add","remove","update"})) @QueryParam("action") String action
    ) {
        BaseResponse resp = new BaseResponse();
        int resultCount;
        if (quantity.intValue() < 0){
            resp.setErrorMessage("Quantity must be positive value");
            return Response.ok(resp).build();
        }
        //TODO: Check if quantity is available for that product


        //First Check if the product is already available in the cart, then just increase the quantity
        try {
            CartModel cartItem = cartDao.getProductsInCart(loginName, productId);

            if (action.equalsIgnoreCase("add")){
                String msg="";
                cartDao.beginTransaction();
                if (cartItem == null) {
                    cartItem = new CartModel(loginName, productId, quantity);
                    cartDao.save(cartItem);
                    msg = "Product Added with specified quantities";
                } else {
                    Long existingQuantity = cartItem.getQuantity();
                    Long newQuantity = existingQuantity + quantity;
                    cartDao.beginTransaction();
                    resultCount = cartDao.updateProductQuantityInCart(loginName, productId, newQuantity);
                    cartDao.commitTransaction();
                    msg = "Quantities updated for a product that already exist in cart";
                }
                cartDao.commitTransaction();
                resp.setSuccessMessage("Product added to Cart " );
                return Response.ok(resp).build();

            } else if (action.equalsIgnoreCase("remove")){
                String msg="";
                if (cartItem == null) {
                    resp.setErrorMessage("Cannot Remove - Product dont exist in the cart");
                    return Response.ok(resp).build();
                } else {
                    Long existingQuantity = cartItem.getQuantity();
                    Long newQuantity = existingQuantity- quantity;
                    cartDao.beginTransaction();
                    if (newQuantity.intValue() <= 0){
                        resultCount = cartDao.removeFromCart(loginName, productId);
                        msg = "Product completely removed from cart";
                    } else {
                        resultCount = cartDao.updateProductQuantityInCart(loginName, productId, newQuantity);
                        msg = "Product quantity updated after removal";
                    }
                    cartDao.commitTransaction();
                    resp.setSuccessMessage(msg);
                    return Response.ok(resp).build();
                }
            } else if (action.equalsIgnoreCase("update")){
                if (cartItem == null) {
                    resp.setErrorMessage("Cannot update - Product dont exist in the cart");
                } else {
                    cartDao.beginTransaction();
                    resultCount = cartDao.updateProductQuantityInCart(loginName, productId, quantity);
                    cartDao.commitTransaction();
                    resp.setSuccessMessage("Product Quantity updated " );
                }
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage("Invalid action  - only add, remove and update are allowed");
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot add cart item - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }
}
