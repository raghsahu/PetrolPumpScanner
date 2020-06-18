package com.reward.scanner.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuccessModel {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("qrcode_id")
    @Expose
    private Integer qrcode_id;

    public Integer getQrcode_id() {
        return qrcode_id;
    }

    public void setQrcode_id(Integer qrcode_id) {
        this.qrcode_id = qrcode_id;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
