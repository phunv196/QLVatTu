package com.app.model.delivery;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "delivery_bill")
public class DeliveryBillModel {
    @Id
    @Column(name = "delivery_bill_id", unique = true, nullable = false) private Long deliveryBillId;

    @Schema(example="601")
    @Column(name = "code")       private String  code;
    @Column(name = "name")      private String  name;
    @Column(name = "date_delivery_bill")      private Date  dateDeliveryBill;
    @Column(name = "factory_id")       private Long  factoryId;
    @Column(name = "warehouse_id")       private Long  warehouseId;
    @Column(name = "employee_id")       private Long  employeeId;
    @Column(name = "description")       private String  description;

    @Transient
    private String  factoryCode;
    @Transient
    private String  factoryName;
    @Transient
    private String  warehouseCode;
    @Transient
    private String  warehouseName;
    @Transient
    private Long sumMoney;
    @Transient
    private String fullName;
    @Transient
    private String formDate;
    @Transient
    private String toDate;

    //Getters and Setters


    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

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

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Long getDeliveryBillId() {
        return deliveryBillId;
    }

    public void setDeliveryBillId(Long deliveryBillId) {
        this.deliveryBillId = deliveryBillId;
    }

    public Date getDateDeliveryBill() {
        return dateDeliveryBill;
    }

    public void setDateDeliveryBill(Date dateDeliveryBill) {
        this.dateDeliveryBill = dateDeliveryBill;
    }

    public Long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(Long factoryId) {
        this.factoryId = factoryId;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(Long sumMoney) {
        this.sumMoney = sumMoney;
    }

    public static class DeliveryBillResponse extends PageResponse {
        private List<DeliveryBillModel> list;

        public List<DeliveryBillModel> getList() {return list; }
        public void setList(List<DeliveryBillModel> list) { this.list = list; }
    }

}
