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
    @Column(name = "user_id") private String userId;
    private String password;

    @Schema(allowableValues =  {"ADMIN", "SUPPORT", "CUSTOMER"}, example="ADMIN")
    private String role;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "customer_id")
    private Integer customerId;

    //Constructors
    public UserModel(){}

    public UserModel(String userId, String password, String role, Integer employeeId, Integer customerId){
        this.setUserId(userId);
        this.setPassword(password);
        this.setRole(role);
        if (employeeId != null){
            this.setEmployeeId(employeeId);
        } else {
            this.setCustomerId(customerId);
        }
    }

    //Getters and Setters
    @JsonIgnore // This getter is duplicate of getId but is must for all classes that implements java.security.Principal
    public String getName() {return userId;}

    public String getUserId() {return userId;}
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) {this.password = password; }

    public String getRole() {return role; }
    public void setRole(String role) {this.role = role; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
}
