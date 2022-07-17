package com.plugspace.model;

import com.plugspace.common.Utils;

public class ChatListModel {
    private String user_id="", name="",profile="";

    public ChatListModel() {
    }

    public ChatListModel(String user_id, String name, String profile) {
        this.user_id = user_id;
        this.name = name;
        this.profile = profile;
    }

    public String getUser_id() {
//        return user_id;
        return Utils.checkEmptyWithNull(user_id,"");
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
//        return name;
        return Utils.checkEmptyWithNull(name,"");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
//        return profile;
        return Utils.checkEmptyWithNull(profile,"");
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
