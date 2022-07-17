package com.plugspace.model;

import com.plugspace.common.Utils;

import java.io.Serializable;

public class ChattingMsgModel implements Serializable {
    String textMsg;
    String time;
    String type;
    String userId;
    String name;
    String image;
    String key;
    String msgStatus;

    public ChattingMsgModel(String textMsg, String time, String type) {
        this.textMsg = textMsg;
        this.time = time;
        this.type = type;
    }

    public ChattingMsgModel() {
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTextMsg() {
        return textMsg;
    }

    public void setTextMsg(String textMsg) {
        this.textMsg = textMsg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        if (Utils.isValidaEmptyWithZero(type)) {
            return "0";
        }

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
