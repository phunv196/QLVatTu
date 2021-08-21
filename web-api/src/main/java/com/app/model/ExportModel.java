package com.app.model;

import com.app.model.user.UserOutputModel;

public class ExportModel {
    private String fileName;
    private String data;

    public ExportModel(String fileName, String data) {
        this.fileName = fileName;
        this.data = data;
    }

    public ExportModel() {

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class ExportResponse extends BaseResponse {
        private ExportModel data = null;

        // ===== Getters & Setters =====
        public ExportModel getData() {return data;}
        public void setData(ExportModel data) {this.data = data;}
    }
}
