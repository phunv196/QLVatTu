package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.UserDao;
import com.app.model.BaseResponse;
import com.app.model.user.LoginModel.LoginResponse;
import com.app.model.user.UserOutputModel;
import com.app.model.user.UserOutputModel.UserListResponse;
import com.app.model.user.UserOutputModel.UserResponse;
import com.app.model.user.UserRegistrationModel;
import com.app.model.user.UserViewModel;
import com.app.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;

import java.util.ArrayList;
import java.util.List;

@Path("users")
@Tag(name = "Users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController extends BaseController {
    UserDao userDao = new UserDao();

    @GET
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
      summary = "Get list of users",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = UserListResponse.class)))}
    )
    public Response getUserList(
      @Parameter (description="Page No, Starts from 1 ", example="1") @DefaultValue("1")  @QueryParam("page") int page,
      @Parameter(description="Items in each page", example="20") @DefaultValue("20") @QueryParam("page-size") int pageSize,
      @Parameter(description="User Id") @QueryParam("user-id") String userId,
      @Parameter(description="Role", schema=@Schema(allowableValues={"ADMIN", "SUPPORT", "CUSTOMER"})) @QueryParam("role") String role
    ) {
        // Fill hibernate search by example user (Hibernate Query-by-example way of searching )
        int recordFrom=0;
        UserViewModel searchUser = new UserViewModel();
        if (StringUtils.isNotBlank(userId)){ searchUser.setUserId(userId); }
        if (StringUtils.isNotBlank(role)){ searchUser.setRole(role); }
        //if (StringUtils.isNotBlank(firstName)){ searchUser.setFirstName(firstName); }
        if (page<=0){
            page = 1;
        }
        if (pageSize<=0 || pageSize > 1000){
            pageSize =20;
        }
        recordFrom = (page-1) * pageSize;

        Example userExample = Example.create(searchUser);
        Criteria criteria = userDao.createCriteria(UserViewModel.class);
        criteria.add(userExample);

        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long totalRows = (Long)criteria.uniqueResult();

        // Execute the Main Query
        criteria.setProjection(null);
        criteria.setFirstResult( (int) (long)recordFrom);
        criteria.setMaxResults(  (int) (long)pageSize);
        List<UserViewModel> userList = criteria.list();

        // Fill the result into userOutput list
        criteria.setProjection(null);
        List<UserOutputModel> userFoundList = new ArrayList<>();
        for (UserViewModel tmpUser : userList) {
            UserOutputModel usrOutput = new UserOutputModel(tmpUser);
            userFoundList.add(usrOutput);
        }

        UserListResponse resp = new UserListResponse();
        resp.setList(userFoundList);
        resp.setPageStats(totalRows.intValue(), pageSize, page,"");
        resp.setSuccessMessage("List of users");
        return Response.ok(resp).build();
    }

    @GET
    @Path("/logged-user")
    @RolesAllowed({"CUSTOMER", "SUPPORT"})
    @Operation(
      summary = "Get Details of Logged in User",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = LoginResponse.class)))}
    )
    public Response getLoggedInUser() {
        UserViewModel userFromToken = (UserViewModel)securityContext.getUserPrincipal();  // securityContext is defined in BaseController
        UserOutputModel userOutput = new UserOutputModel(userFromToken);

        LoginResponse resp = new LoginResponse();
        resp.setData(userOutput);
        resp.setSuccessMessage("Current Logged in User Details");
        return Response.ok(resp).build();
    }

    @GET
    @Path("{userId}")
    @RolesAllowed({"CUSTOMER", "SUPPORT"})
    @Operation(
      summary = "Get Details of a User by id",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = UserResponse.class)))}
    )
    public Response getUserDetailsById(@Parameter(description="User ID") @PathParam("userId") String userId) {
        UserViewModel userFromToken = (UserViewModel)securityContext.getUserPrincipal();  // securityContext is defined in BaseController
        UserViewModel userView = userDao.getById(userId);

        UserResponse resp = new UserResponse();
        if (userView!=null) {
            if (userFromToken.getRole().equalsIgnoreCase("ADMIN") || userFromToken.getUserId().equals(userId)) {
                //For Admins and Own-Id - Just remove the password
                userView.setPassword("");
            } else {
                // If not a ADMIN or not his own id then strip all the data and just send the id
                // others should be able to find out if the userId exist or not
                userView.setEmployeeId(0);
                userView.setCustomerId(0);
                userView.setPassword("");
                userView.setRole("");
                userView.setEmail("");
                userView.setFullName("");
            }
            UserOutputModel userOutput = new UserOutputModel(userView);
            resp.setData(userOutput);
            resp.setSuccessMessage("User Details");
        } else {
            resp.setErrorMessage("User Not Found");
        }
        return Response.ok(resp).build();
    }

    @DELETE
    @Path("{userId}")
    @RolesAllowed({"ADMIN"})
    @Operation(
      summary = "Delete a user by id",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteUser(
        @Parameter(description="User ID", example="mdaniel") @PathParam("userId") String userId,
        @Parameter(description="Delete associated orders, customer/employee data also", example="true") @DefaultValue("true")  @QueryParam("delete-related-data") boolean deleteRelatedData
    ) {
        BaseResponse resp = new BaseResponse();
        if (userId.equals("admin") || userId.equals("support") || userId.equals("customer")){
            resp.setErrorMessage("Cannot delete reserved User IDs - (admin, support or customer)");
            return Response.ok(resp).build();
        }

        if (StringUtils.isBlank(userId)){
            resp.setErrorMessage("Must provide a valid ID");
            return Response.ok(resp).build();
        }

        try {
            UserViewModel userInfo = userDao.getById(userId);
            if (userInfo==null){
                resp.setErrorMessage("User not found");
                return Response.ok(resp).build();
            }
            userDao.beginTransaction();
            userDao.delete(userInfo,deleteRelatedData);
            userDao.commitTransaction();
        } catch (ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete User - Database constraints are violated");
            return Response.ok(resp).build();
        }
        resp.setSuccessMessage("Deleted");
        return Response.ok(resp).build();
    }

    @POST
    @PermitAll
    @Operation(
      summary = "Register as a New User",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addUser(UserRegistrationModel registerObj) {
        BaseResponse resp = new BaseResponse();
        UserViewModel userFromToken = (UserViewModel)securityContext.getUserPrincipal();  // securityContext is defined in BaseController
        //Customers can query their own cart only
        if (userFromToken==null || !userFromToken.getRole().equalsIgnoreCase(Constants.UserRoleConstants.ROLE_ADMIN)){
            if (registerObj.getRole().equalsIgnoreCase(Constants.UserRoleConstants.ROLE_ADMIN)) {
                resp.setErrorMessage("Role cannot be ADMIN ");
                return Response.ok(resp).build();
            }
        }
        try {
            userDao.beginTransaction();
            userDao.save(registerObj);
            userDao.commitTransaction();
            resp.setSuccessMessage("User Registered Successfully");
        } catch (HibernateException | ConstraintViolationException  e) {
            resp.setErrorMessage("Cannot add User - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }
}
