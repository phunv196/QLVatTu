package com.app.api.controllers;

import com.app.api.BaseController;
import com.app.dao.base.BaseHibernateDAO;
import com.app.model.BaseResponse;
import com.app.model.user.LoginModel;
import com.app.model.user.LoginModel.LoginResponse;
import com.app.model.user.UserOutputModel;
import com.app.model.user.UserViewModel;
import com.app.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpSession;
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


        String hql = "FROM UserViewModel u WHERE u.userId = :uid and u.password = :pwd";
        Query q = dao.createQuery(hql);
        q.setParameter("uid", uid);
        q.setParameter("pwd", pwd);

        UserViewModel userView = (UserViewModel)q.uniqueResult();  // can throw org.hibernate.NonUniqueResultExceptio
        if (userView!=null){
            String strToken = TokenUtil.createTokenForUser(userView);
            UserOutputModel usrOutput = new UserOutputModel(
                userView.getUserId(),
                userView.getRole(),
                userView.getFullName(),
                userView.getEmail(),
                userView.getEmployeeId(),
                userView.getCustomerId(),
                strToken
            );
            LoginResponse successResp = new LoginResponse(usrOutput);
            return Response.status(Response.Status.OK).entity(successResp).build();
        }

        resp.setTypeAndMessage(BaseResponse.MessageTypeEnum.AUTH_FAILED, "Incorrect username/password");
        return Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
    }
}
