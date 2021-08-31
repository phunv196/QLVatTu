package com.app.model.stats;

import com.app.model.BaseResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public class SuppliesStatsModel {
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private String suppliesName;
    private Long sumReceipt;
    private Long sumDeliveryBill;

    //Constructor

    public SuppliesStatsModel(){}

    public SuppliesStatsModel(String suppliesName, Long sumReceipt, Long sumDeliveryBill) {
        this.suppliesName = suppliesName;
        this.sumReceipt = sumReceipt;
        this.sumDeliveryBill = sumDeliveryBill;
    }
//Getters and Setters


    public String getSuppliesName() {
        return suppliesName;
    }

    public void setSuppliesName(String suppliesName) {
        this.suppliesName = suppliesName;
    }

    public Long getSumReceipt() {
        return sumReceipt;
    }

    public void setSumReceipt(Long sumReceipt) {
        this.sumReceipt = sumReceipt;
    }

    public Long getSumDeliveryBill() {
        return sumDeliveryBill;
    }

    public void setSumDeliveryBill(Long sumDeliveryBill) {
        this.sumDeliveryBill = sumDeliveryBill;
    }

    //Response Class
    public static class SuppliesStatsResponse  extends BaseResponse {
        private List<SuppliesStatsModel> list;

        public List<SuppliesStatsModel> getList() {return list; }
        public void setList(List<SuppliesStatsModel> list) { this.list = list; }
    }
}
