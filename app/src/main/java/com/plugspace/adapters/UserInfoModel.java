package com.plugspace.adapters;

import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfoModel implements Serializable {
    LoginDataModel model;
    String title;
    ArrayList<MediaModel> mediaDetail = null;

    public LoginDataModel getModel() {
        return model;
    }

    public void setModel(LoginDataModel model) {
        this.model = model;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<MediaModel> getMediaDetail() {
        return mediaDetail;
    }

    public void setMediaDetail(ArrayList<MediaModel> mediaDetail) {
        this.mediaDetail = mediaDetail;
    }
}
