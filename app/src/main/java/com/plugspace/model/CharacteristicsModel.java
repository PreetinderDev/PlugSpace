package com.plugspace.model;

import com.google.gson.annotations.SerializedName;
import com.plugspace.common.Utils;

public class CharacteristicsModel {

    @SerializedName("name")
    private String name;

    @SerializedName("rank")
    private String rank;

    @SerializedName("id")
    private int id;

    @SerializedName("text")
    private String text;

    public String getName() {
//		return name;
        return Utils.checkEmptyWithNull(name, "");
    }

    public String getRank() {
        return rank;
    }

    public int getId() {
        return id;
    }

    public String getText() {
//		return text;
        return Utils.checkEmptyWithNull(text, "");
    }
}