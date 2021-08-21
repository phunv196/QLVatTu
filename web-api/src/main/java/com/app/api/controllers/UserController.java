package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.UserDao;
import com.app.dao.base.CommonUtils;
import com.app.dao.base.converter.DynamicExport;
import com.app.model.BaseResponse;
import com.app.model.ExportModel;
import com.app.model.ExportModel.ExportResponse;
import com.app.model.user.LoginModel.LoginResponse;
import com.app.model.user.UserModel;
import com.app.model.user.UserOutputModel;
import com.app.model.user.UserOutputModel.UserListResponse;
import com.app.model.user.UserOutputModel.UserResponse;
import com.app.util.Constants;
import com.app.util.PlainTextPasswordEncoder;
import com.app.util.TemplateResouces;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static com.app.util.Constants.COMMON.FOLDER_EXPORT;
import static com.app.util.Constants.COMMON.TEMPLATE_EXPORT_FOLDER;

@Path("users")
@Tag(name = "Users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController extends BaseController {
    static final String PASSWORD = "123456a@";
    UserDao userDao = new UserDao();

    @GET
    @RolesAllowed({"ADMIN"})
    @Operation(
            summary = "Get list of user ",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = UserListResponse.class)))}
    )
    public Response getUserList(
            @Parameter(description = "Order Id") @QueryParam("userId") Long userId,
            @Parameter(description = "Order Id") @QueryParam("searchLoginName") String searchLoginName,
            @Parameter(description = "Order Id") @QueryParam("searchRole") String searchRole,
            @Parameter(description = "Order Id") @QueryParam("searchFullName") String searchFullName,
            @Parameter(description = "Order Id") @QueryParam("searchEmail") String searchEmail,
            @Parameter(description = "Order Id") @QueryParam("searchPhone") String searchPhone,
            @Parameter(description = "Order Id") @QueryParam("searchEmployeeId") Integer searchEmployeeId,
            @Parameter(description = "Page No, Starts from 1 ", example = "1") @DefaultValue("1") @QueryParam("page") int page,
            @Parameter(description = "Items in each page", example = "20") @DefaultValue("10") @QueryParam("page-size") int pageSize
    ) {
        UserListResponse resp = new UserListResponse();
        try {
            if (searchEmployeeId == null) {
                searchEmployeeId = 0;
            }
            List<UserOutputModel> modelList = userDao.getList(page, pageSize, searchLoginName, searchRole, searchFullName,
                    searchEmail, searchPhone, searchEmployeeId);
            BigInteger total = userDao.getCount(searchLoginName, searchRole, searchFullName, searchEmail, searchPhone,
                    searchEmployeeId);
            resp.setList(modelList);
            resp.setTotal(total.intValue());
            resp.setPageStats(total.intValue(),pageSize, page,"");
            return Response.ok(resp).build();
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete User - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @GET
    @Path("/logged-user")
    @RolesAllowed({"CUSTOMER", "SUPPORT"})
    @Operation(
      summary = "Get Details of Logged in User",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = LoginResponse.class)))}
    )
    public Response getLoggedInUser() {
        UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();  // securityContext is defined in BaseController
        UserOutputModel userOutput = new UserOutputModel(userFromToken);

        LoginResponse resp = new LoginResponse();
        resp.setData(userOutput);
        resp.setSuccessMessage("Current Logged in User Details");
        return Response.ok(resp).build();
    }

    @GET
    @Path("{loginName}")
    //@RolesAllowed({"CUSTOMER", "SUPPORT"})
    @Operation(
      summary = "Get Details of a User by id",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = UserResponse.class)))}
    )
    public Response getUserDetailsById(@Parameter(description="User ID") @PathParam("loginName") String loginName) {
        UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();  // securityContext is defined in BaseController
        UserModel userView = userDao.getById(loginName);

        UserResponse resp = new UserResponse();
        if (userView!=null) {
            if (userFromToken.getRole().equalsIgnoreCase("ADMIN") || userFromToken.getLoginName().equals(loginName)) {
                //For Admins and Own-Id - Just remove the password
                userView.setPassword("");
            } else {
                // If not a ADMIN or not his own id then strip all the data and just send the id
                // others should be able to find out if the loginName exist or not
                userView.setEmployeeId(0);
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
    @Path("{loginName}")
    @RolesAllowed({"ADMIN"})
    @Operation(
      summary = "Delete a user by id",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response deleteUser(
        @Parameter(description="User ID", example="mdaniel") @PathParam("loginName") String loginName
    ) {
        BaseResponse resp = new BaseResponse();
        if (loginName.equals("admin") || loginName.equals("support") || loginName.equals("customer")){
            resp.setErrorMessage("Cannot delete reserved User IDs - (admin, support or customer)");
            return Response.ok(resp).build();
        }
        if (StringUtils.isBlank(loginName)){
            resp.setErrorMessage("Must provide a valid ID");
            return Response.ok(resp).build();
        }
        try {
            UserModel userInfo = userDao.getById(loginName);
            if (userInfo==null){
                resp.setErrorMessage("User not found");
                return Response.ok(resp).build();
            }
            userDao.beginTransaction();
            userDao.delete(userInfo);
            userDao.commitTransaction();
        } catch (ConstraintViolationException e) {
            resp.setErrorMessage("Cannot delete User - Database constraints are violated");
            return Response.ok(resp).build();
        }
        resp.setSuccessMessage("Deleted");
        return Response.ok(resp).build();
    }

    @POST
    @RolesAllowed({"ADMIN"})
    @Operation(
      summary = "Register as a New User",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response addUser(UserOutputModel addUser) throws Exception {
        BaseResponse resp = new BaseResponse();
        UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();  // securityContext is defined in BaseController
        //Customers can query their own cart only
        if (userFromToken==null || !userFromToken.getRole().equalsIgnoreCase(Constants.UserRoleConstants.ROLE_ADMIN)){
            if (addUser.getRole().equalsIgnoreCase(Constants.UserRoleConstants.ROLE_ADMIN)) {
                resp.setErrorMessage("Role cannot be ADMIN ");
                return Response.ok(resp).build();
            }
        }
        try {
            Long id = userDao.getSequence();
            if (id == null) {
                id = 0l;
            }
            UserModel model = new UserModel();
            CommonUtils.copyProperties(model, addUser);
            model.setPassword(PlainTextPasswordEncoder.encode(PASSWORD, id.toString()));
            if (CommonUtils.isNullOrEmpty(addUser.getRole())) {
                model.setRole(Constants.UserRoleConstants.ROLE_SUPPORT);
            }
            userDao.beginTransaction();
            userDao.save(model);
            userDao.commitTransaction();
            resp.setSuccessMessage("User Registered Successfully");
        } catch (HibernateException | ConstraintViolationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            resp.setErrorMessage("Cannot add User - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
        }
        return Response.ok(resp).build();
    }


    @PUT
    @RolesAllowed({"ADMIN", "SUPPORT"})
    @Operation(
            summary = "Update a User",
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response updateUser(UserOutputModel user) {
        BaseResponse resp = new BaseResponse();
        try {
            UserModel foundProd  = userDao.getById(user.getUserId());
            if (foundProd != null) {
                CommonUtils.copyProperties(foundProd, user);
                userDao.beginTransaction();
                userDao.update(foundProd);
                userDao.commitTransaction();
                resp.setSuccessMessage(String.format("User Updated (getUserId:%s)", user.getUserId()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Cannot Update - User not found (getUserId:%s)", user.getUserId()));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            resp.setErrorMessage("Cannot update User - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

    @POST
    @Path("byLoginName")
    @RolesAllowed({"ADMIN"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response byLoginName(
            UserOutputModel model
    ) {
        Criteria criteria = userDao.createCriteria(UserModel.class);
        if (model.getUserId() != null) {
            criteria.add(Restrictions.ne("userId", model.getUserId()));
        }
        if (!CommonUtils.isNullOrEmpty(model.getLoginName())){
            criteria.add(Restrictions.eq("loginName", model.getLoginName()).ignoreCase());
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());
        Long rowCount = (Long)criteria.uniqueResult();
        return Response.ok(rowCount > 0).build();
    }


    @POST
    @Path("changePassword")
    @RolesAllowed({"ADMIN"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response changePassword(
            UserModel model
    ) {
        UserModel userFromToken = (UserModel)securityContext.getUserPrincipal();
        Criteria criteria = userDao.createCriteria(UserModel.class);
        if (userFromToken != null){
            criteria.add(Restrictions.eq("userId", userFromToken.getUserId()));
        }
        UserModel userModel = (UserModel) criteria.uniqueResult();
        if (userModel == null) {
            return Response.ok(false).build();
        }
        String pass = PlainTextPasswordEncoder.encode(model.getPassword(), userFromToken.getUserId().toString());
        if(!pass.equals(userModel.getPassword())) {
            return Response.ok(false).build();
        } else {
            String newPass = PlainTextPasswordEncoder.encode(model.getNewPassword(), userFromToken.getUserId().toString());
            userModel.setPassword(newPass);
            userDao.beginTransaction();
            userDao.update(userModel);
            userDao.commitTransaction();
        }
        // Execute the Total-Count Query first ( if main query is executed first, it results in error for count-query)
        criteria.setProjection(Projections.rowCount());

        return Response.ok(true).build();
    }

    @GET
    @Path("resetPasswordUser/{userId}")
    @RolesAllowed({"ADMIN"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response resetPasswordUser(
            @Parameter(description="User ID", example="mdaniel") @PathParam("userId") Integer userId
    ) {
        UserModel model = userDao.getById(userId);
        BaseResponse resp = new BaseResponse();
        try {
            UserModel user  = userDao.getById(userId);
            if (user != null) {
                user.setPassword(PlainTextPasswordEncoder.encode(PASSWORD, userId.toString()));
                userDao.beginTransaction();
                userDao.update(user);
                userDao.commitTransaction();
                resp.setSuccessMessage(String.format("Reset password thành công với (Tên đăng nhập:%s)", user.getLoginName()));
                return Response.ok(resp).build();
            } else {
                resp.setErrorMessage(String.format("Không tìm thấy user với Id (getUserId:%s)", userId));
                return Response.ok(resp).build();
            }
        } catch (HibernateException | ConstraintViolationException e) {
            resp.setErrorMessage("Cannot update User - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }
    @POST
    @Path("export")
    @RolesAllowed({"ADMIN"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response export(
            UserModel model
    ) throws Exception {
        ExportResponse resp = new ExportResponse();
        String fileName = "danh_sach_user.xls";
        DynamicExport dynamicExport = new DynamicExport(TemplateResouces.getReportFile(TEMPLATE_EXPORT_FOLDER + fileName), 6, false);
        List<UserOutputModel> models = userDao.getListExport(model.getLoginName(), model.getRole(),
                model.getFullName(), model.getEmail(), model.getPhone(), model.getEmployeeId());
        int stt = 1;
        if(models != null && !models.isEmpty()) {
            for (int i = 0 ; i < models.size() ; i++){
                UserOutputModel user = models.get(i);
                int index = 0;
                dynamicExport.increaseRow();
                dynamicExport.setEntry(stt++, index++);
                dynamicExport.setText(user.getLoginName(), index++);
                dynamicExport.setText(user.getRole(), index++);
                dynamicExport.setText(user.getEmployeeCode(), index++);
                dynamicExport.setText(user.getFullName(), index++);
                dynamicExport.setText(user.getEmail(), index++);
                dynamicExport.setText(user.getPhone(), index++);
            }
        }
        // Set ten file xuat ra excel
        String prefixOutPutFile = new SimpleDateFormat("yyyyMMddHHmmss_").format(new Date()) + "_";
        String fileExport = FOLDER_EXPORT + prefixOutPutFile +  "danh_sach_user";
        String filePath = dynamicExport.exportFile(fileExport, req);
        File file = new File(filePath);
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        String[] fileNameNew = filePath.split("/");
        ExportModel exportModel = new ExportModel();
        exportModel.setFileName(fileNameNew[fileNameNew.length - 1]);
        exportModel.setData(encodedString);
        resp.setData(exportModel);
        return Response.ok(resp).build();
    }

    @GET
    @Path("byId/{userId}")
    @RolesAllowed({"ADMIN"})
    @Operation(
            responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = BaseResponse.class)))}
    )
    public Response byId(
            @Parameter(description="User ID", example="mdaniel") @PathParam("userId") Integer userId
    ) {
        BaseResponse resp = new BaseResponse();
        try {
            UserModel foundProd  = userDao.getById(userId);
            UserOutputModel model = new UserOutputModel();
            CommonUtils.copyProperties(model, foundProd);
            return Response.ok(model).build();
        } catch (HibernateException | ConstraintViolationException | NullPointerException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            resp.setErrorMessage("Cannot update User - " + e.getMessage() + ", " + (e.getCause()!=null? e.getCause().getMessage():""));
            return Response.ok(resp).build();
        }
    }

}
