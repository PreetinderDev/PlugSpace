package com.plugspace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plugspace.adapters.UserInfoModel;

import java.util.ArrayList;

public class HomeResponseModel {

    @SerializedName("data")
    @Expose
    private ArrayList<UserInfoModel> data = null;
    @SerializedName("storyDtl")
    @Expose
    private ArrayList<LoginDataModel> storyDtl = null;
    @SerializedName("notificationDtl")
    @Expose
    private ArrayList<LoginDataModel> notificationDtl = null;
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

    @SerializedName("like_details")
    @Expose
    private ArrayList<LoginDataModel> likeDetails = null;

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("view_profile")
    @Expose
    private ArrayList<LoginDataModel> viewProfile = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<LoginDataModel> getViewProfile() {
        return viewProfile;
    }

    public void setViewProfile(ArrayList<LoginDataModel> viewProfile) {
        this.viewProfile = viewProfile;
    }

    public ArrayList<LoginDataModel> getLikeDetails() {
        return likeDetails;
    }

    public void setLikeDetails(ArrayList<LoginDataModel> likeDetails) {
        this.likeDetails = likeDetails;
    }

    public ArrayList<UserInfoModel> getData() {
        return data;
    }

    public void setData(ArrayList<UserInfoModel> data) {
        this.data = data;
    }

    public ArrayList<LoginDataModel> getStoryDtl() {
        return storyDtl;
    }

    public void setStoryDtl(ArrayList<LoginDataModel> storyDtl) {
        this.storyDtl = storyDtl;
    }

    public ArrayList<LoginDataModel> getNotificationDtl() {
        return notificationDtl;
    }

    public void setNotificationDtl(ArrayList<LoginDataModel> notificationDtl) {
        this.notificationDtl = notificationDtl;
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
