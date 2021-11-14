package com.app.model.receipt;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "receipt_flow")
public class ReceiptFlowModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_flow_id", unique = true, nullable = false) private Long receiptFlowId;

    @Schema(example="601")
    @Column(name = "receipt_id")       private Long  receiptId;
    @Column(name = "supplies_id")      private Long  suppliesId;
    @Column(name = "amount")       private Long  amount;
    @Column(name = "received")       private Long  received;
    @Column(name = "description")       private String  description;

    //nhà cung cấp
    @Transient
    private String supplierName;
    @Transient
    private String supplierCode;

    //vật tư
    @Transient
    private String suppliesCode;
    @Transient
    private String suppliesName;
    @Transient
    private String suppliesPrice;
    @Transient
    private String suppliesUnit;
    @Transient
    private String speciesName;
    @Transient
    private String calculatePrice;
    @Transient
    private Long missing;
    //Getters and Setters


    public Long getReceiptFlowId() {
        return receiptFlowId;
    }

    public void setReceiptFlowId(Long receiptFlowId) {
        this.receiptFlowId = receiptFlowId;
    }

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public Long getSuppliesId() {
        return suppliesId;
    }

    public void setSuppliesId(Long suppliesId) {
        this.suppliesId = suppliesId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getReceived() {
        return received;
    }

    public void setReceived(Long received) {
        this.received = received;
    }

    public Long getMissing() { return missing; }

    public void setMissing(Long missing) { this.missing = missing; }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
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

    public String getSuppliesPrice() {
        return suppliesPrice;
    }

    public void setSuppliesPrice(String suppliesPrice) {
        this.suppliesPrice = suppliesPrice;
    }

    public String getSuppliesUnit() {
        return suppliesUnit;
    }

    public void setSuppliesUnit(String suppliesUnit) {
        this.suppliesUnit = suppliesUnit;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getCalculatePrice() {
        return calculatePrice;
    }

    public void setCalculatePrice(String calculatePrice) {
        this.calculatePrice = calculatePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class ReceiptFlowResponse extends PageResponse {
        private List<ReceiptFlowModel> list;

        public List<ReceiptFlowModel> getList() {return list; }
        public void setList(List<ReceiptFlowModel> list) { this.list = list; }
    }
}
