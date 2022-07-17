package com.plugspace.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PreviewModel implements Serializable {
    String subTitle;
    String descriptions;
    String musicType;
    String notiDescriptions;
    String time;
    int previewImg;
    int storyImg;
    boolean slcCategory;

//    public PreviewModel(String subTitle, String descriptions, int previewImg, int storyImg) {
//        this.subTitle = subTitle;
//        this.descriptions = descriptions;
//        this.previewImg = previewImg;
//        this.storyImg = storyImg;
//    }
//
//    public PreviewModel(String subTitle) {
//        this.subTitle = subTitle;
//    }
//
//    public PreviewModel(String subTitle, String descriptions, int previewImg) {
//        this.subTitle = subTitle;
//        this.descriptions = descriptions;
//        this.previewImg = previewImg;
//    }
//
//    public PreviewModel(String subTitle, String descriptions, String notiDescriptions, int previewImg) {
//        this.subTitle = subTitle;
//        this.descriptions = descriptions;
//        this.previewImg = previewImg;
//        this.notiDescriptions = notiDescriptions;
//    }
//
//    public PreviewModel(String subTitle, String descriptions, String time, String notiDescriptions, int previewImg) {
//        this.subTitle = subTitle;
//        this.descriptions = descriptions;
//        this.notiDescriptions = notiDescriptions;
//        this.time = time;
//        this.previewImg = previewImg;
//    }
//
//    public PreviewModel(String name, int previewImg) {
//        this.subTitle = name;
//        this.previewImg = previewImg;
//    }

    public PreviewModel(String name, boolean slcCategory, String musicType) {
        this.subTitle = name;
        this.slcCategory = slcCategory;
        this.musicType = musicType;
    }

    public PreviewModel(String name, int previewImg, boolean slcCategory, String musicType) {
        this.notiDescriptions = name;
        this.previewImg = previewImg;
        this.slcCategory = slcCategory;
        this.musicType = musicType;
    }

    public boolean getSlcCategory() {
        return slcCategory;
    }

    public void setSlcCategory(boolean slcCategory) {
        this.slcCategory = slcCategory;
    }

    public int getStoryImg() {
        return storyImg;
    }

    public void setStoryImg(int storyImg) {
        this.storyImg = storyImg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotiDescriptions() {
        return notiDescriptions;
    }

    public void setNotiDescriptions(String notiDescriptions) {
        this.notiDescriptions = notiDescriptions;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }


    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public int getPreviewImg() {
        return previewImg;
    }

    public void setPreviewImg(int previewImg) {
        this.previewImg = previewImg;
    }
}
