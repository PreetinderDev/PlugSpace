package com.plugspace.model;

import android.net.Uri;

import java.io.Serializable;

public class HeightModel implements Serializable {
    String string;
    int genderImg;
    boolean BooleanSelected;
    Uri imageView;
    int id;

    public HeightModel(String string, boolean BooleanSelected) {
        this.string = string;
        this.BooleanSelected = BooleanSelected;
    }

    public HeightModel(String string, int genderImg, boolean BooleanSelected) {
        this.string = string;
        this.genderImg = genderImg;
        this.BooleanSelected = BooleanSelected;
    }

    public HeightModel(String string) {
        this.string = string;
    }

    public HeightModel(int id, String string, Uri imageView) {
        this.id = id;
        this.string = string;
        this.imageView = imageView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Uri getImageView() {
        return imageView;
    }

    public void setImageView(Uri imageView) {
        this.imageView = imageView;
    }

    public int getGenderImg() {
        return genderImg;
    }

    public void setGenderImg(int genderImg) {
        this.genderImg = genderImg;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public boolean getBooleanSelected() {
        return BooleanSelected;
    }

    public void setBooleanSelected(boolean booleanSelected) {
        BooleanSelected = booleanSelected;
    }
}