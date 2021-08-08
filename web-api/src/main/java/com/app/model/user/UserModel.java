package com.app.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.security.Principal;

@Entity
@Table(name = "users")
public class UserModel implements Serializable, Principal {
    @Id
    @Column(name = "user_id")  private Integer userId;
    @Column(name = "login_name") private String loginName;
    private String password;

    @Schema(allowableValues =  {"ADMIN", "SUPPORT", "CUSTOMER"}, example="ADMIN")
    private String role;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    //Constructors
    public UserModel(){}

    public UserModel(Integer userId, String loginName, String password, String role, Integer employeeId, Integer customerId,
                String fullName, String email, String phone){
        this.setUserId(userId);
        this.setLoginName(loginName);
        this.setPassword(password);
        this.setRole(role);
        if (employeeId != null){
            this.setEmployeeId(employeeId);
        } else {
            this.setCustomerId(customerId);
        }
        this.setFullName(fullName);
        this.setEmail(email);
        this.setPhone(phone);
    }

    public UserModel(Integer userId, String loginName, String role, String name, String email, Integer empId, String phone) {
        this.setUserId(userId);
        this.setLoginName(loginName);
        this.setRole(role);
        this.setFullName(name);
        this.setEmail(email);
        this.setEmployeeId(empId);
        this.setPhone(phone);
    }

    //Getters and Setters
    @JsonIgnore // This getter is duplicate of getId but is must for all classes that implements java.security.Principal
    public String getName() {return loginName;}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginName() {return loginName;}
    public void setLoginName(String loginName) { this.loginName = loginName; }

    public String getPassword() { return password; }
    public void setPassword(String password) {this.password = password; }

    public String getRole() {return role; }
    public void setRole(String role) {this.role = role; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
