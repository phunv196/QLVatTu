package com.app.model.stats;

import com.app.model.BaseResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class QuarterModel {
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Long quarter;
    private Long sumAmount;

    //Constructor

    public QuarterModel(){}
    public QuarterModel(Long quarter, Long sumAmount) {
        this.quarter = quarter;
        this.sumAmount = sumAmount;
    }

    //Getters and Setters


    public Long getQuarter() {
        return quarter;
    }

    public void setQuarter(Long quarter) {
        this.quarter = quarter;
    }

    public Long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Long sumAmount) {
        this.sumAmount = sumAmount;
    }

    //Response Class
    public static class QuarterResponse  extends BaseResponse {
        private List<QuarterModel> list;

        public List<QuarterModel> getList() {return list; }
        public void setList(List<QuarterModel> list) { this.list = list; }
    }
}
