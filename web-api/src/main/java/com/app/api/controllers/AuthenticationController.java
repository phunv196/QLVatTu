package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.base.BaseHibernateDAO;
import com.app.model.BaseResponse;
import com.app.model.user.LoginModel;
import com.app.model.user.LoginModel.LoginResponse;
import com.app.model.user.UserModel;
import com.app.model.user.UserOutputModel;
import com.app.util.PlainTextPasswordEncoder;
import com.app.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "1st Authenticate Yourself")
@Path("/authenticate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    BaseHibernateDAO dao = new BaseHibernateDAO();

    @POST
    @PermitAll
    @Path("/user")
    @Operation(
      summary = "Authenticates user to access Application",
      responses = { @ApiResponse(content = @Content(schema = @Schema(implementation = LoginResponse.class)))}
    )
    public Response authenticateUser(LoginModel loginModel) {
        String uid = loginModel.getUsername();
        String pwd = loginModel.getPassword();

        BaseResponse resp = new BaseResponse();
        if (StringUtils.isAnyBlank(uid,pwd )  ) {
            resp.setErrorMessage("Missing Username or Password");
            resp.setTypeAndMessage(BaseResponse.MessageTypeEnum.AUTH_FAILED, "Missing Username or Password");
            return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
        }


        String hql = "FROM UserModel u WHERE u.loginName = :uid";
        Query q = dao.createQuery(hql);
        q.setParameter("uid", uid);
        UserModel user = (UserModel)q.uniqueResult();  // can throw org.hibernate.NonUniqueResultExceptio
        String pwdEncoder = PlainTextPasswordEncoder.encode(loginModel.getPassword(), user.getUserId().toString());
        if (user!=null && pwdEncoder.equals(user.getPassword())){
            String strToken = TokenUtil.createTokenForUser(user);
            UserOutputModel usrOutput = new UserOutputModel(
                user.getUserId(),
                user.getLoginName(),
                user.getRole(),
                user.getFullName(),
                user.getEmail(),
                user.getEmployeeId(),
                user.getCustomerId(),
                strToken
            );
            LoginResponse successResp = new LoginResponse(usrOutput);
            return Response.status(Response.Status.OK).entity(successResp).build();
        }

        resp.setTypeAndMessage(BaseResponse.MessageTypeEnum.AUTH_FAILED, "Incorrect username/password");
        return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
    }
}
