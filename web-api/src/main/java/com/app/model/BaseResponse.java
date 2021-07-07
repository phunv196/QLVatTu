package com.app.model;

import io.swagger.v3.oas.annotations.media.Schema;

public class BaseResponse {
    public enum MessageTypeEnum {SUCCESS, ERROR, WARNING, AUTH_FAILED, NO_ACCESS, BAD_TOKEN};

    protected MessageTypeEnum  msgType;
    protected String  msg;

    @Schema(description = "Can be either SUCCESS, ERROR", example = "SUCCESS", required = true)
    public MessageTypeEnum getMsgType() {return msgType;}
    public void setMsgType(MessageTypeEnum msgType) {this.msgType = msgType;}

    @Schema(description = "Overall message for the operation", example = "Operation was success", required = true)
    public String getMsg() {return msg;}
    public void setMsg(String msg) {this.msg = msg;}

    public void setSuccessMessage(String msg){
        this.msgType = MessageTypeEnum.SUCCESS;
        this.msg = msg;
    }

    public void setErrorMessage(String msg){
        this.msgType = MessageTypeEnum.ERROR;
        this.msg = msg;
    }

    public void setWarningMessage(String msg){
        this.msgType = MessageTypeEnum.WARNING;
        this.msg = msg;
    }

    public void setTypeAndMessage(MessageTypeEnum msgType, String msg){
        this.msgType = msgType;
        this.msg = msg;
    }
    public void setTypeAndMessage(MessageTypeEnum msgType, String msg, String usrMsg){
        this.msgType = msgType;
        this.msg = msg;
    }
}
