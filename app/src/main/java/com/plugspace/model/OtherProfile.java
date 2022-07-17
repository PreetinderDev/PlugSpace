
package com.plugspace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtherProfile implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ccode")
    @Expose
    private String ccode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("plugspace_rank")
    @Expose
    private String plugspaceRank;
    @SerializedName("is_geo_location")
    @Expose
    private String isGeoLocation;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("is_apple")
    @Expose
    private String isApple;
    @SerializedName("apple_id")
    @Expose
    private String appleId;
    @SerializedName("is_insta")
    @Expose
    private String isInsta;
    @SerializedName("insta_id")
    @Expose
    private String instaId;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("education_status")
    @Expose
    private String educationStatus;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("children")
    @Expose
    private String children;
    @SerializedName("want_childrens")
    @Expose
    private String wantChildrens;
    @SerializedName("marring_race")
    @Expose
    private String marringRace;
    @SerializedName("relationship_status")
    @Expose
    private String relationshipStatus;
    @SerializedName("ethinicity")
    @Expose
    private String ethinicity;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("job_title")
    @Expose
    private String jobTitle;
    @SerializedName("make_over")
    @Expose
    private String makeOver;
    @SerializedName("dress_size")
    @Expose
    private String dressSize;
    @SerializedName("signiat_bills")
    @Expose
    private String signiatBills;
    @SerializedName("times_of_engaged")
    @Expose
    private String timesOfEngaged;
    @SerializedName("your_body_tatto")
    @Expose
    private String yourBodyTatto;
    @SerializedName("age_range_marriage")
    @Expose
    private String ageRangeMarriage;
    @SerializedName("my_self_men")
    @Expose
    private String mySelfMen;
    @SerializedName("about_you")
    @Expose
    private String aboutYou;
    @SerializedName("nice_meet")
    @Expose
    private String niceMeet;
    @SerializedName("is_subscribe")
    @Expose
    private Integer isSubscribe;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("is_private")
    @Expose
    private String isPrivate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("profile")
    @Expose
    private String profile;

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

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPlugspaceRank() {
        return plugspaceRank;
    }

    public void setPlugspaceRank(String plugspaceRank) {
        this.plugspaceRank = plugspaceRank;
    }

    public String getIsGeoLocation() {
        return isGeoLocation;
    }

    public void setIsGeoLocation(String isGeoLocation) {
        this.isGeoLocation = isGeoLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsApple() {
        return isApple;
    }

    public void setIsApple(String isApple) {
        this.isApple = isApple;
    }

    public String getAppleId() {
        return appleId;
    }

    public void setAppleId(String appleId) {
        this.appleId = appleId;
    }

    public String getIsInsta() {
        return isInsta;
    }

    public void setIsInsta(String isInsta) {
        this.isInsta = isInsta;
    }

    public String getInstaId() {
        return instaId;
    }

    public void setInstaId(String instaId) {
        this.instaId = instaId;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEducationStatus() {
        return educationStatus;
    }

    public void setEducationStatus(String educationStatus) {
        this.educationStatus = educationStatus;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getWantChildrens() {
        return wantChildrens;
    }

    public void setWantChildrens(String wantChildrens) {
        this.wantChildrens = wantChildrens;
    }

    public String getMarringRace() {
        return marringRace;
    }

    public void setMarringRace(String marringRace) {
        this.marringRace = marringRace;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getEthinicity() {
        return ethinicity;
    }

    public void setEthinicity(String ethinicity) {
        this.ethinicity = ethinicity;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getMakeOver() {
        return makeOver;
    }

    public void setMakeOver(String makeOver) {
        this.makeOver = makeOver;
    }

    public String getDressSize() {
        return dressSize;
    }

    public void setDressSize(String dressSize) {
        this.dressSize = dressSize;
    }

    public String getSigniatBills() {
        return signiatBills;
    }

    public void setSigniatBills(String signiatBills) {
        this.signiatBills = signiatBills;
    }

    public String getTimesOfEngaged() {
        return timesOfEngaged;
    }

    public void setTimesOfEngaged(String timesOfEngaged) {
        this.timesOfEngaged = timesOfEngaged;
    }

    public String getYourBodyTatto() {
        return yourBodyTatto;
    }

    public void setYourBodyTatto(String yourBodyTatto) {
        this.yourBodyTatto = yourBodyTatto;
    }

    public String getAgeRangeMarriage() {
        return ageRangeMarriage;
    }

    public void setAgeRangeMarriage(String ageRangeMarriage) {
        this.ageRangeMarriage = ageRangeMarriage;
    }

    public String getMySelfMen() {
        return mySelfMen;
    }

    public void setMySelfMen(String mySelfMen) {
        this.mySelfMen = mySelfMen;
    }

    public String getAboutYou() {
        return aboutYou;
    }

    public void setAboutYou(String aboutYou) {
        this.aboutYou = aboutYou;
    }

    public String getNiceMeet() {
        return niceMeet;
    }

    public void setNiceMeet(String niceMeet) {
        this.niceMeet = niceMeet;
    }

    public Integer getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(Integer isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

}
