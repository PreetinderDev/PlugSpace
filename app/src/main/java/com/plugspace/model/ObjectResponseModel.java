package com.plugspace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plugspace.common.Utils;

import java.io.Serializable;

public class ObjectResponseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private LoginDataModel data;
    @SerializedName("ResponseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("ResponseMsg")
    @Expose
    private String responseMsg;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ServerTime")
    @Expose
    private String serverTime;
    @SerializedName("otpcode")
    @Expose
    private String otpcode;
    @SerializedName("is_login")
    @Expose
    private String is_login;


    public String getOtpcode() {
        return otpcode;
    }

    public void setOtpcode(String otpcode) {
        this.otpcode = otpcode;
    }

    public LoginDataModel getData() {
//        return data;
        return (LoginDataModel) Utils.checkNull(data, new LoginDataModel());
    }

    public void setData(LoginDataModel data) {
        this.data = data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
//        return responseMsg;
        return Utils.checkEmptyWithNull(responseMsg, "");
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getIs_login() {
//        return is_login;
        return Utils.checkEmptyWithNull(is_login, "0");
    }

    public void setIs_login(String is_login) {
        this.is_login = is_login;
    }
}