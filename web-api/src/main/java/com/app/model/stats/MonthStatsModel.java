package com.app.model.stats;

import com.app.model.BaseResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

public class MonthStatsModel {
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private String month;
    private Long sumReceipt;
    private Long sumDeliveryBill;
    private Long sumInventory;

    //Constructor

    public MonthStatsModel(){}

    public MonthStatsModel(String month, Long sumReceipt, Long sumDeliveryBill, Long sumInventory) {
        this.month = month;
        this.sumReceipt = sumReceipt;
        this.sumDeliveryBill = sumDeliveryBill;
        this.sumInventory = sumInventory;
    }
//Getters and Setters

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public Long getSumInventory() {
        return sumInventory;
    }

    public void setSumInventory(Long sumInventory) {
        this.sumInventory = sumInventory;
    }

    //Response Class
    public static class MonthStatsResponse  extends BaseResponse {
        private List<MonthStatsModel> list;

        public List<MonthStatsModel> getList() {return list; }
        public void setList(List<MonthStatsModel> list) { this.list = list; }
    }
}
