package com.app.model.user;

import com.app.model.BaseResponse;
import com.app.model.PageResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;

public class UserOutputModel implements Serializable, Principal {
    private Integer userId;
    private String loginName;

    @Schema(allowableValues =  {"ADMIN", "SUPPORT", "CUSTOMER"}, example="ADMIN")
    private String role;

    private String fullName;
    private String email;
    private String phone;
    private Integer employeeId;
    private String employeeCode;
    private String token;

    //Constructors
    public UserOutputModel(){}

    public UserOutputModel(UserModel user){
        this.setUserId(user.getUserId());
        this.setLoginName(user.getLoginName());
        this.setRole(user.getRole());
        this.setFullName(user.getFullName());
        this.setEmail(user.getEmail());
        this.setEmployeeId(user.getEmployeeId());
        this.setToken("");
    }

    public UserOutputModel(Integer userId, String loginName, String role, String fullName, String email, Integer empId, String token){
        this.setUserId(userId);
        this.setLoginName(loginName);
        this.setRole(role);
        this.setFullName(fullName);
        this.setEmail(email);
        this.setEmployeeId(empId);
        this.setToken(token);
    }

    //Getters & Setters
    @JsonIgnore // This getter is duplicate of getId but is must for all classes that implements java.security.Principal
    public String getName() {return loginName;}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginName() {return loginName;}
    public void setLoginName(String loginName) { this.loginName = loginName; }

    public String getRole() {return role; }
    public void setRole(String role) {this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getToken() {return token; }
    public void setToken(String token) {this.token = token; }

    //User Response Classes
    public static class UserListResponse extends PageResponse {
        private List<UserOutputModel> list = null;

        // ===== Getters & Setters =====
        public List<UserOutputModel> getList() {return list;}
        public void setList(List<UserOutputModel> list) {this.list = list;}
    }

    public static class UserResponse extends BaseResponse {
        private UserOutputModel data = null;

        // ===== Getters & Setters =====
        public UserOutputModel getData() {return data;}
        public void setData(UserOutputModel data) {this.data = data;}
    }
}
