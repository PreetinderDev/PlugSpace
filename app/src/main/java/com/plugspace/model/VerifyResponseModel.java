package com.plugspace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plugspace.common.Utils;

public class VerifyResponseModel {
    @SerializedName("data")
    @Expose
    private String data;
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


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
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


}
