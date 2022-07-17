package com.plugspace.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plugspace.common.Utils;

import java.io.File;
import java.io.Serializable;

public class MediaModel implements Serializable {

    @SerializedName("media_id")
    @Expose
    private String mediaId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("media_type")
    @Expose
    private String mediaType;
    @SerializedName("story_media_id")
    @Expose
    private String storyMediaId;
    @SerializedName("story_id")
    @Expose
    private String storyId;
    @SerializedName("media")
    @Expose
    private String media;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("feed_id")
    @Expose
    private String feedId;
    @SerializedName("feed_image")
    @Expose
    private String feedImage;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
//        return type;
        return Utils.checkEmptyWithNull(type, "");
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getFeedImage() {
        return feedImage;
    }

    public void setFeedImage(String feedImage) {
        this.feedImage = feedImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    File imgFile = null;
    Uri imageData = null;

    public Uri getImageData() {
        return imageData;
    }

    public void setImageData(Uri imageData) {
        this.imageData = imageData;
    }

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


    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfile() {
//        return profile;
        return Utils.checkEmptyWithNull(profile, "");
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public File getImgFile() {
        return imgFile;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }
}
