package com.app.model.category;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "warehouse_card")
public class WarehouseCardModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_card_id", unique = true, nullable = false) private Long warehouseCardId;

    @Schema(example="601")
    @Column(name = "warehouse_id") private Long warehouseId;
    @Column(name = "code")       private String  code;
    @Column(name = "name")      private String  name;
    @Column(name = "date_created")      private Date dateCreated;
    @Column(name = "supplies_id")      private Long  suppliesId;
    @Column(name = "description")       private String  description;
    @Column(name = "employee_id")       private Long  employeeId;

    //Getters and Setters
    @Transient
    private String employeeName;

    @Transient
    private String suppliesCode;

    @Transient
    private String suppliesName;

    @Transient
    private Long suppliesPrice;

    @Transient
    private String warehouseCode;

    @Transient
    private Long countDeliveryBill;

    @Transient
    private Long countReceipt;

    @Transient
    private Long amountDeliveryBill;

    @Transient
    private Long amountReceipt;

    public Long getWarehouseCardId() {
        return warehouseCardId;
    }

    public void setWarehouseCardId(Long warehouseCardId) {
        this.warehouseCardId = warehouseCardId;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getSuppliesId() {
        return suppliesId;
    }

    public void setSuppliesId(Long suppliesId) {
        this.suppliesId = suppliesId;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getSuppliesCode() {
        return suppliesCode;
    }

    public void setSuppliesCode(String suppliesCode) {
        this.suppliesCode = suppliesCode;
    }

    public String getSuppliesName() {
        return suppliesName;
    }

    public void setSuppliesName(String suppliesName) {
        this.suppliesName = suppliesName;
    }

    public Long getSuppliesPrice() {
        return suppliesPrice;
    }

    public void setSuppliesPrice(Long suppliesPrice) {
        this.suppliesPrice = suppliesPrice;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Long getCountDeliveryBill() {
        return countDeliveryBill;
    }

    public void setCountDeliveryBill(Long countDeliveryBill) {
        this.countDeliveryBill = countDeliveryBill;
    }

    public Long getCountReceipt() {
        return countReceipt;
    }

    public void setCountReceipt(Long countReceipt) {
        this.countReceipt = countReceipt;
    }

    public Long getAmountDeliveryBill() {
        return amountDeliveryBill;
    }

    public void setAmountDeliveryBill(Long amountDeliveryBill) {
        this.amountDeliveryBill = amountDeliveryBill;
    }

    public Long getAmountReceipt() {
        return amountReceipt;
    }

    public void setAmountReceipt(Long amountReceipt) {
        this.amountReceipt = amountReceipt;
    }

    public static class WarehouseCardResponse extends PageResponse {
        private List<WarehouseCardModel> list;

        public List<WarehouseCardModel> getList() {return list; }
        public void setList(List<WarehouseCardModel> list) { this.list = list; }
    }
}
