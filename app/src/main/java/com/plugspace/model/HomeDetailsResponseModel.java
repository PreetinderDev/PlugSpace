package com.plugspace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plugspace.common.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeDetailsResponseModel implements Serializable {

    @SerializedName("data")
    @Expose
    private ArrayList<LoginDataModel> data = null;

    @SerializedName("storyDtl")
    @Expose
    private ArrayList<StoryDtl> storyDtl = null;

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

    @SerializedName("saved_profile")
    @Expose
    private ArrayList<SavedProfileModel> savedProfile = null;

    public Integer getCount() {
//        return count;
        return Integer.parseInt(Utils.checkEmptyWithNull(count, "0"));
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
    public ArrayList<SavedProfileModel> getSavedProfile() {
        return savedProfile;
    }

    public void setSavedProfile(ArrayList<SavedProfileModel> savedProfile) {
        this.savedProfile = savedProfile;
    }


    //
    public ArrayList<SavedProfileModel> getSaveProfile() {
        return savedProfile;
    }

    public void setSaveProfile(ArrayList<SavedProfileModel> savedProfile) {
        this.savedProfile = savedProfile;
    }
    public ArrayList<LoginDataModel> getLikeDetails() {
        return likeDetails;
    }

    public void setLikeDetails(ArrayList<LoginDataModel> likeDetails) {
        this.likeDetails = likeDetails;
    }

    public ArrayList<LoginDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<LoginDataModel> data) {
        this.data = data;
    }

    public ArrayList<StoryDtl> getStoryDtl() {
        return storyDtl;
    }

    public void setStoryDtl(ArrayList<StoryDtl> storyDtl) {
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


    public class StoryDtl implements Serializable {

        @SerializedName("story_id")
        @Expose
        private String storyId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("story_view_count")
        @Expose
        private Integer storyViewCount;
        @SerializedName("media")
        @Expose
        private List<Medium> media = null;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("is_show_story")
        @Expose
        private String isShowStory;
        @SerializedName("profile")
        @Expose
        private String profile;
        @SerializedName("is_story")
        @Expose
        private String isStory;

        public String getStoryId() {
            return storyId;
        }

        public void setStoryId(String storyId) {
            this.storyId = storyId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getStoryViewCount() {
            return storyViewCount;
        }

        public void setStoryViewCount(Integer storyViewCount) {
            this.storyViewCount = storyViewCount;
        }

        public List<Medium> getMedia() {
            return media;
        }

        public void setMedia(List<Medium> media) {
            this.media = media;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIsShowStory() {
            return isShowStory;
        }

        public void setIsShowStory(String isShowStory) {
            this.isShowStory = isShowStory;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getIsStory() {
            return isStory;
        }

        public void setIsStory(String isStory) {
            this.isStory = isStory;
        }


    }

    public class Medium implements Serializable {

        @SerializedName("story_media_id")
        @Expose
        private String storyMediaId;
        @SerializedName("story_id")
        @Expose
        private String storyId;
        @SerializedName("media")
        @Expose
        private String media;
        @SerializedName("media_type")
        @Expose
        private String mediaType;

        public String getStoryMediaId() {
            return storyMediaId;
        }

        public void setStoryMediaId(String storyMediaId) {
            this.storyMediaId = storyMediaId;
        }

        public String getStoryId() {
            return storyId;
        }

        public void setStoryId(String storyId) {
            this.storyId = storyId;
        }

        public String getMedia() {
            return media;
        }

        public void setMedia(String media) {
            this.media = media;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

    }



}