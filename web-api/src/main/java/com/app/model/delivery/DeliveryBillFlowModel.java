package com.app.model.delivery;

import com.app.model.PageResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "delivery_bill_flow")
public class DeliveryBillFlowModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_bill_flow_id", unique = true, nullable = false) private Long deliveryBillFlowId;

    @Schema(example="601")
    @Column(name = "delivery_bill_id", unique = true, nullable = false) private Long deliveryBillId;
    @Column(name = "supplies_id")      private Long  suppliesId;
    @Column(name = "amount")       private Long  amount;
    @Column(name = "description")       private String  description;

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

    //Getters and Setters


    public Long getDeliveryBillFlowId() {
        return deliveryBillFlowId;
    }

    public void setDeliveryBillFlowId(Long deliveryBillFlowId) {
        this.deliveryBillFlowId = deliveryBillFlowId;
    }

    public Long getDeliveryBillId() {
        return deliveryBillId;
    }

    public void setDeliveryBillId(Long deliveryBillId) {
        this.deliveryBillId = deliveryBillId;
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

    public static class DeliveryBillFlowResponse extends PageResponse {
        private List<DeliveryBillFlowModel> list;

        public List<DeliveryBillFlowModel> getList() {return list; }
        public void setList(List<DeliveryBillFlowModel> list) { this.list = list; }
    }
}
