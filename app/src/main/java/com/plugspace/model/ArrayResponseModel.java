package com.plugspace.model;

import com.google.gson.annotations.SerializedName;
import com.plugspace.common.Utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayResponseModel {

    @SerializedName("ResponseCode")
    private int responseCode;

    @SerializedName("data")
    private List<LoginDataModel> data;

    @SerializedName("ResponseMsg")
    private String responseMsg;

    @SerializedName("ServerTime")
    private String serverTime;

    @SerializedName("Result")
    private String result;

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setData(List<LoginDataModel> data) {
        this.data = data;
    }

    public List<LoginDataModel> getData() {
//		return data;
//        return Utils.checkNull(data, new List<LoginDataModel>());
        return (ArrayList<LoginDataModel>) Utils.checkNull(data, new ArrayList<>());
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}