package com.app.model.category;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "warehouse_card_flow")
public class WarehouseCardFlowModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_card_flow_id", unique = true, nullable = false) private Long warehouseCardFlowId;

    @Schema(example="601")
    @Column(name = "warehouse_card_id") private Long warehouseCardId;
    @Column(name = "delivery_bill_id")      private Long  deliveryBillId;
    @Column(name = "receipt_id")      private Long  receiptId;
    @Column(name = "amount")      private Long amount;
    @Column(name = "description")       private String  description;
    @Column(name = "type")       private Long  type;

    @Transient
    private String employeeName;
    @Transient
    private String employeeCode;
    @Transient
    private String receiptCode;
    @Transient
    private String deliveryBillCode;

    //Getters and Setters


    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getWarehouseCardFlowId() {
        return warehouseCardFlowId;
    }

    public void setWarehouseCardFlowId(Long warehouseCardFlowId) {
        this.warehouseCardFlowId = warehouseCardFlowId;
    }

    public Long getWarehouseCardId() {
        return warehouseCardId;
    }

    public void setWarehouseCardId(Long warehouseCardId) {
        this.warehouseCardId = warehouseCardId;
    }

    public Long getDeliveryBillId() {
        return deliveryBillId;
    }

    public void setDeliveryBillId(Long deliveryBillId) {
        this.deliveryBillId = deliveryBillId;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    public String getDeliveryBillCode() {
        return deliveryBillCode;
    }

    public void setDeliveryBillCode(String deliveryBillCode) {
        this.deliveryBillCode = deliveryBillCode;
    }

    public static class WarehouseCardFlowResponse extends PageResponse {
        private List<WarehouseCardFlowModel> list;

        public List<WarehouseCardFlowModel> getList() {return list; }
        public void setList(List<WarehouseCardFlowModel> list) { this.list = list; }
    }
}
