package com.plugspace.model;

public class AddPhotoModel {
    private String photoPath = "";

    public AddPhotoModel(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
