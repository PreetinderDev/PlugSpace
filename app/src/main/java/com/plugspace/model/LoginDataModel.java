package com.plugspace.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.plugspace.common.Constants;
import com.plugspace.common.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginDataModel implements Serializable {
    public LoginDataModel() {
    }

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("other_id")
    @Expose
    private String other_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ccode")
    @Expose
    private String ccode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("is_verified")
    @Expose
    private Integer isVerified;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("rank")
    @Expose
    private String rank;
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
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("media_detail")
    @Expose
    private ArrayList<MediaModel> mediaDetail = null;
    @SerializedName("noti_id")
    @Expose
    private String notiId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("story_id")
    @Expose
    private String storyId;
    @SerializedName("like_id")
    @Expose
    private String likeId;
    ;
    @SerializedName("like_user_id")
    @Expose
    private String likeUserId;
    @SerializedName("like_type")
    @Expose
    private String likeType;
    @SerializedName("view_story_id")
    @Expose
    private String viewStoryId;
    @SerializedName("view_user_id")
    @Expose
    private String viewUserId;
    @SerializedName("view_id")
    @Expose
    private String viewId;
    @SerializedName("type")
    @Expose
    private String type;
    String title;
    @SerializedName("story_media_detail")
    @Expose
    private ArrayList<MediaModel> storyMediaDetail = null;

    @SerializedName("story_media")
    @Expose
    private ArrayList<MediaModel> storyMedia;
    @SerializedName("is_private")
    @Expose
    private String isPrivate;
    @SerializedName("plugspace_rank")
    @Expose
    private String plugspaceRank;
    @SerializedName("characteristics")
    @Expose
    private List<CharacteristicsModel> lstCharacteristics;
    //    @SerializedName("is_story")
//    @Expose
//    private String is_story;
    @SerializedName("is_show_story")
    @Expose
    private String is_show_story;
    @SerializedName("read_count")
    @Expose
    private String read_count;
    @SerializedName("device_type")
    @Expose
    private String device_type;
    @SerializedName("device_token")
    @Expose
    private String device_token;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("message_status")
    @Expose
    private String message_status;
    @SerializedName("created_date")
    @Expose
    private String created_date;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("header_desc")
    @Expose
    private String header_desc;
    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("request_sent")
    @Expose
    private Integer requestSent;
    @SerializedName("message")

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getRequestStatus() {
        return requestSent;
    }

    public void setRequestStatus(Integer requestSent) {
        this.requestSent = requestSent;
    }

    public String getIsPrivate() {
//        return isPrivate;
        return Utils.checkEmptyWithNull(isPrivate, Constants.IS_PRIVATE_1);
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getPlugspaceRank() {
        return plugspaceRank;
    }

    public void setPlugspaceRank(String plugspaceRank) {
        this.plugspaceRank = plugspaceRank;
    }

//    public String getCharacteristics() {
//        return characteristics;
//    }
//
//    public void setCharacteristics(String characteristics) {
//        this.characteristics = characteristics;
//    }


    public List<CharacteristicsModel> getLstCharacteristics() {
        return lstCharacteristics;
    }

    public void setLstCharacteristics(List<CharacteristicsModel> lstCharacteristics) {
        this.lstCharacteristics = lstCharacteristics;
    }

//    public String getIsStory() {
////        return is_story;
//        return Utils.checkEmptyWithNull(is_story, Constants.isStory_0);
//    }
//
//    public void setIsStory(String is_story) {
//        this.is_story = is_story;
//    }


    public String getIs_show_story() {
//        return is_show_story;
        return Utils.checkEmptyWithNull(is_show_story, Constants.isStory_0);
    }

    public void setIs_show_story(String is_show_story) {
        this.is_show_story = is_show_story;
    }

    public String getRead_count() {
//        return read_count;
        return Utils.checkEmptyWithNull(read_count, Constants.readCount_0);
    }

    public void setRead_count(String read_count) {
        this.read_count = read_count;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getTime() {
//        return time;
        return Utils.checkEmptyWithNull(time, "");
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage_status() {
//        return message_status;
        return Utils.checkEmptyWithNull(message_status, "0");
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getCreated_date() {
//        return created_date;
        return Utils.checkEmptyWithNull(created_date, "");
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public ArrayList<MediaModel> getStoryMedia() {
        return storyMedia;
    }

    public void setStoryMedia(ArrayList<MediaModel> storyMedia) {
        this.storyMedia = storyMedia;
    }

    public ArrayList<MediaModel> getStoryMediaDetail() {
        return storyMediaDetail;
    }

    public void setStoryMediaDetail(ArrayList<MediaModel> storyMediaDetail) {
        this.storyMediaDetail = storyMediaDetail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLocation() {
//        return location;
        return Utils.checkEmptyWithNull(location, "");
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getViewStoryId() {
        return viewStoryId;
    }

    public void setViewStoryId(String viewStoryId) {
        this.viewStoryId = viewStoryId;
    }

    public String getViewUserId() {
        return viewUserId;
    }

    public void setViewUserId(String viewUserId) {
        this.viewUserId = viewUserId;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public String getLikeUserId() {
        return likeUserId;
    }

    public void setLikeUserId(String likeUserId) {
        this.likeUserId = likeUserId;
    }

    public String getLikeType() {
        return likeType;
    }

    public void setLikeType(String likeType) {
        this.likeType = likeType;
    }

    public String getAge() {
//        return age;
        return Utils.checkEmptyWithNull(age, "");
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNotiId() {
        return notiId;
    }

    public void setNotiId(String notiId) {
        this.notiId = notiId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getUserId() {
//        return userId;
        return Utils.checkEmptyWithNull(user_id, "");
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

//    public String getUser_id() {
////        return user_id;
//        return Utils.checkEmptyWithNull(user_id, "");
//    }


    public String getOther_id() {
//        return other_id;
        return Utils.checkEmptyWithNull(other_id, "");
    }

    public void setOther_id(String other_id) {
        this.other_id = other_id;
    }

    public String getName() {
//        return name;
        return Utils.checkEmptyWithNull(name, "");
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

    public Integer getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Integer isVerified) {
        this.isVerified = isVerified;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRank() {
//        return rank;
        return Utils.checkEmptyWithNull(rank, "");
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getIsGeoLocation() {
//        return isGeoLocation;
        return Utils.checkEmptyWithNull(isGeoLocation, Constants.IS_GEO_LOCATION_0);
    }

    public void setIsGeoLocation(String isGeoLocation) {
        this.isGeoLocation = isGeoLocation;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<MediaModel> getMediaDetail() {
//        return mediaDetail;
        return (ArrayList<MediaModel>) Utils.checkNull(mediaDetail, new ArrayList<>());
    }

    public void setMediaDetail(ArrayList<MediaModel> mediaDetail) {
        this.mediaDetail = mediaDetail;
    }

    @SerializedName("is_play")
    private boolean isPlay;

    @SerializedName("is_favorite")
    private String isFavorite;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("music_id")
    private String musicId;

    @SerializedName("language")
    private String language;

    @SerializedName("artists_name")
    private String artistsName;

    @SerializedName("music_other_id")
    private String musicOtherId;

    @SerializedName("media_url")
    private String mediaUrl;

    @SerializedName("is_like")
    private String is_like;

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getIsFavorite() {
//        return isFavorite;
        return Utils.checkEmptyWithNull(isFavorite, Constants.IS_FAVOURITE_0);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setArtistsName(String artistsName) {
        this.artistsName = artistsName;
    }

    public String getArtistsName() {
        return artistsName;
    }


    public void setMusicOtherId(String musicOtherId) {
        this.musicOtherId = musicOtherId;
    }

    public String getMusicOtherId() {
        return musicOtherId;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getIs_like() {
//        return is_like;
        return Utils.checkEmptyWithNull(is_like, Constants.isLike_2);
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public Integer getCount() {
//        return count;
        return Integer.parseInt(Utils.checkEmptyWithNull(count, "0"));
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSubtitle() {
//        return subtitle;
        return Utils.checkEmptyWithNull(subtitle, 0);
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getHeader_desc() {
//        return header_desc;
        return Utils.checkEmptyWithNull(header_desc, "");
    }

    public void setHeader_desc(String header_desc) {
        this.header_desc = header_desc;
    }
}
