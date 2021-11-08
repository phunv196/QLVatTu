package com.app.model.factory;

import com.app.model.PageResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "factory")
public class FactoryModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factory_id", unique = true, nullable = false) private Long factoryId;

    @Schema(example="601")
    @Column(name = "code")       private String  code;
    @Column(name = "name")      private String  name;
    @Column(name = "email")      private String  email;
    @Column(name = "address")      private String  address;
    @Column(name = "description")       private String  description;
    @Column(name = "employee_id")      private Long  employeeId;
    @Column(name = "date_construction")      private Date dateConstruction;
    @Column(name = "date_finish")      private Date  dateFinish;

    @Transient
    private String formDate;
    @Transient
    private String toDate;
    @Transient
    private String formSuccessDate;
    @Transient
    private String toSuccessDate;
    @Transient
    private String employeeName;

    //Getters and Setters


    public String getFormDate() {
        return formDate;
    }

    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFormSuccessDate() {
        return formSuccessDate;
    }

    public void setFormSuccessDate(String formSuccessDate) {
        this.formSuccessDate = formSuccessDate;
    }

    public String getToSuccessDate() {
        return toSuccessDate;
    }

    public void setToSuccessDate(String toSuccessDate) {
        this.toSuccessDate = toSuccessDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDateConstruction() {
        return dateConstruction;
    }

    public void setDateConstruction(Date dateConstruction) {
        this.dateConstruction = dateConstruction;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public static class FactoryResponse extends PageResponse {
        private List<FactoryModel> list;

        public List<FactoryModel> getList() {return list; }
        public void setList(List<FactoryModel> list) { this.list = list; }
    }
}
