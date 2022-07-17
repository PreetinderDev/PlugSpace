package com.plugspace.retrofitApi;

import com.plugspace.model.ArrayResponseModel;
import com.plugspace.model.AutoChatModel;
import com.plugspace.model.HomeDetailsResponseModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.model.PreDefinedMessageModel;
import com.plugspace.model.RankYourSelfResponse;
import com.plugspace.model.SavedProfileModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("signUp")
    Call<ObjectResponseModel> signUp(@Part("name") RequestBody name,
                                     @Part("ccode") RequestBody ccode,
                                     @Part("phone") RequestBody phone,
                                     @Part("is_verified") RequestBody is_verified,
                                     @Part("gender") RequestBody gender,
                                     @Part("rank") RequestBody rank,
                                     @Part("is_geo_location") RequestBody is_geo_location,
                                     @Part("is_apple") RequestBody is_apple,
                                     @Part("apple_id") RequestBody apple_id,
                                     @Part("is_insta") RequestBody is_insta,
                                     @Part("insta_id") RequestBody insta_id,
                                     @Part("is_manual_email") RequestBody is_manual_email,
                                     @Part("height") RequestBody height,
                                     @Part("weight") RequestBody weight,
                                     @Part("education_status") RequestBody education_status,
                                     @Part("dob") RequestBody dob, // YYYY-MM-DD
                                     @Part("children") RequestBody children,
                                     @Part("want_childrens") RequestBody want_childrens,
                                    /* @Part("marring_race") RequestBody marring_race,
                                     @Part("relationship_status") RequestBody relationship_status,
                                     @Part("ethinicity") RequestBody ethinicity,
                                     @Part("company_name") RequestBody company_name,
                                     @Part("job_title") RequestBody job_title,
                                     @Part("make_over") RequestBody make_over,
                                     @Part("dress_size") RequestBody dress_size,
                                     @Part("signiat_bills") RequestBody signiat_bills,
                                     @Part("times_of_engaged") RequestBody times_of_engaged,
                                     @Part("your_body_tatto") RequestBody your_body_tatto,
                                     @Part("age_range_marriage") RequestBody age_range_marriage,
                                     @Part("my_self_men") RequestBody my_self_men,
                                     @Part("about_you") RequestBody about_you,
                                     @Part("nice_meet") RequestBody nice_meet,*/
                                     @Part("device_type") RequestBody device_type,// android
                                     @Part("device_token") RequestBody device_token,//device nu token get karavu
                                     @Part MultipartBody.Part[] profile,
                                     @Part("location") RequestBody location);

    @FormUrlEncoded
    @POST("sendOTP")
    Call<ObjectResponseModel> sendOTP(@Field("phone") String phone,
                                      @Field("ccode") String ccode,
                                      @Field("is_signup") String is_signup

    );

    @FormUrlEncoded
    @POST("verifyOTP")
    Call<ObjectResponseModel> verifyOTP(@Field("phone") String phone,
                                        @Field("ccode") String ccode,
                                        @Field("otpcode") String otpcode,
                                        @Field("device_type") String device_type,
                                        @Field("device_token") String device_token
    );

//    @FormUrlEncoded
//    @POST("ageCalculator")
//    Call<AgeCalculatorResponseModel> ageCalculator(@Field("dob") String dob);

    @FormUrlEncoded
    @POST("getRankPerson")
    Call<RankYourSelfResponse> getRankPerson(@Field("rank") String rank,
                                             @Field("gender") String gender);

//    @FormUrlEncoded
//    @POST("getUserProfile")
//    Call<RankYourSelfResponse> getUserProfile(@Field("user_id") String user_id,
//                                              @Field("to_user_id") String to_user_id);

//    @FormUrlEncoded
//    @POST("login")
//    Call<ObjectResponseModel> login(@Field("phone") String phone,
//                              @Field("ccode") String ccode);

    @FormUrlEncoded
    @POST("logOut")
    Call<ObjectResponseModel> logOut(@Field("user_id") String user_id);

    @GET("getMessages")
    Call<PreDefinedMessageModel> apiCallGetPreDefinedMessage();

    @FormUrlEncoded
    @POST("getHomeDetails")
    Call<HomeDetailsResponseModel> apiCallGetHomeDetails(@Field("user_id") String user_id,
                                                         @Field("search_text") String search_text);

    @FormUrlEncoded
    @POST("profileLikeDislike")
    Call<ObjectResponseModel> profileLikeDislike(@Field("user_id") String user_id,
                                                 @Field("like_user_id") String like_user_id,
                                                 @Field("like_type") String like_type,
                                                 @Field("comment") String comment,
                                                 @Field("message") String message);

    @FormUrlEncoded
    @POST("getChatList")
    Call<HomeDetailsResponseModel> apiGetChatList(@Field("user_id") String user_id,
                                                  @Field("search_text") String search_text);

    @Multipart
    @POST("createStory")
    Call<ObjectResponseModel> createStory(@Part("user_id") RequestBody user_id,
                                          @Part List<MultipartBody.Part> media);

    @Multipart
    @POST("createFeed")
    Call<ObjectResponseModel> createFeed(@Part("user_id") RequestBody user_id,
                                         @Part("description") RequestBody description,
                                         @Part MultipartBody.Part media);

//    @Multipart
//    @POST("createFeed")
//    Call<ObjectResponseModel> createFeed(@Part("user_id") RequestBody user_id,
//                                         @Part("description") RequestBody description,
//                                         @Part List<MultipartBody.Part> media);

    @FormUrlEncoded
    @POST("getMyViewStory")
    Call<HomeDetailsResponseModel> getMyViewStory(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("getStoryDetails")
    Call<HomeDetailsResponseModel> apiCallGetStoryDetails(@Field("user_id") String user_id,
                                                          @Field("view_user_id") String view_user_id);

//    @FormUrlEncoded
//    @POST("isRegister")
//    Call<HomeDetailsResponseModel> isRegister(@Field("is_insta") String is_insta,
//                                              @Field("insta_id") String insta_id,
//                                              @Field("is_apple") String is_apple,
//                                              @Field("apple_id") String apple_id,
//                                              @Field("phone") String phone);

    @FormUrlEncoded
    @POST("isPrivateScore")
    Call<ObjectResponseModel> apiIsPrivateScore(@Field("user_id") String user_id,
                                                @Field("is_private") String is_private);

    @FormUrlEncoded
    @POST("getFriendsScore")
    Call<ObjectResponseModel> apiGetFriendsScore(@Field("user_id") String user_id,
                                                 @Field("friend_user_id") String friend_user_id);

    @FormUrlEncoded
    @POST("getMyScore")
    Call<ObjectResponseModel> apiGetMyScore(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("contactUs")
    Call<ObjectResponseModel> contactUs(@Field("email") String email,
                                        @Field("name") String name,
                                        @Field("subject") String subject,
                                        @Field("message") String message);

    @Multipart
    @POST("previewUpdatePro")
    Call<ObjectResponseModel> previewUpdatePro(@Part("user_id") RequestBody user_id,
                                               @Part("remove_media_id") RequestBody remove_media_id,
                                               @Part("feed_id") RequestBody feed_id,
                                               @Part("type") RequestBody type,
                                               @Part MultipartBody.Part feed_image);

    @FormUrlEncoded
    @POST("previewUpdatePro")
    Call<ObjectResponseModel> apiCallPreview(@Field("user_id") String user_id,
                                             @Field("type") String type);

    @Multipart
    @POST("updateProfile")
    Call<ObjectResponseModel> updateProfile(@Part("user_id") RequestBody user_id,
                                            @Part("name") RequestBody name,
                                            @Part("ccode") RequestBody ccode,
                                            @Part("phone") RequestBody phone,
                                            @Part("gender") RequestBody gender,
                                            @Part("rank") RequestBody rank,
                                            @Part("is_geo_location") RequestBody is_geo_location,
                                            @Part("height") RequestBody height,
                                            @Part("weight") RequestBody weight,
                                            @Part("education_status") RequestBody education_status,
                                            @Part("dob") RequestBody dob,
                                            @Part("children") RequestBody children,
                                            @Part("want_childrens") RequestBody want_childrens,
                                            @Part("marring_race") RequestBody marring_race,
                                            @Part("relationship_status") RequestBody relationship_status,
                                            @Part("ethinicity") RequestBody ethinicity,
                                            @Part("company_name") RequestBody company_name,
                                            @Part("job_title") RequestBody job_title,
                                            @Part("make_over") RequestBody make_over,
                                            @Part("dress_size") RequestBody dress_size,
                                            @Part("signiat_bills") RequestBody signiat_bills,
                                            @Part("times_of_engaged") RequestBody times_of_engaged,
                                            @Part("your_body_tatto") RequestBody your_body_tatto,
                                            @Part("age_range_marriage") RequestBody age_range_marriage,
                                            @Part("my_self_men") RequestBody my_self_men,
                                            @Part("about_you") RequestBody about_you,
                                            @Part("nice_meet") RequestBody nice_meet,
                                            @Part("device_type") RequestBody device_type,
                                            @Part("device_token") RequestBody device_token,
                                            @Part("location") RequestBody location,
                                            @Part("remove_media_id") RequestBody remove_media_id,
                                            @Part MultipartBody.Part[] profile);

    @FormUrlEncoded
    @POST("storyComment")
    Call<ObjectResponseModel> apiStoryComment(@Field("user_id") String user_id,
                                              @Field("friend_id") String friend_id,
                                              @Field("comment") String comment,
                                              @Field("device_type") String device_type,
                                              @Field("device_token") String device_token
    );

    @FormUrlEncoded
    @POST("getMusicList")
    Call<ArrayResponseModel> apiGetMusicList(@Field("user_id") String user_id,
                                             @Field("music_type") String music_type,
                                             @Field("search_text") String search_text

    );

    @FormUrlEncoded
    @POST("getFavoriteMusic")
    Call<ArrayResponseModel> apiGetFavoriteMusic(@Field("user_id") String user_id,
                                                 @Field("music_type") String music_type,
                                                 @Field("search_text") String search_text

    );

    @FormUrlEncoded
    @POST("musicLikeDislike")
    Call<ObjectResponseModel> apiMusicLikeDislike(@Field("user_id") String user_id,
                                                  @Field("music_id") String music_id,
                                                  @Field("music_type") String music_type

    );

    @FormUrlEncoded
    @POST("deleteStory")
    Call<ObjectResponseModel> apiDeleteStory(@Field("user_id") String user_id,
                                             @Field("story_id") String story_id,
                                             @Field("story_media_id") String story_media_id,
                                             @Field("device_type") String device_type,
                                             @Field("device_token") String device_token

    );

    @FormUrlEncoded
    @POST("profileReportByUser")
    Call<ObjectResponseModel> apiProfileReportByUser(@Field("user_id") String user_id,
                                                     @Field("friend_id") String friend_id,
                                                     @Field("extra_id") String extra_id,
                                                     @Field("message") String message,
                                                     @Field("type") String type,
                                                     @Field("device_type") String device_type,
                                                     @Field("device_token") String device_token

    );

    @FormUrlEncoded
    @POST("blockUser")
    Call<ObjectResponseModel> apiBlockUser(@Field("user_id") String user_id,
                                           @Field("block_user_id") String block_user_id,
                                           @Field("device_type") String device_type,
                                           @Field("device_token") String device_token

    );

    @GET("getMessages")
    Call<AutoChatModel> autoChat();


    @FormUrlEncoded
    @POST("save/profile")
    Call<ObjectResponseModel> apiSaveProfile(@Field("user_id") String user_id,
                                             @Field("saved_user_id") String saved_user_id

    );

    @FormUrlEncoded
    @POST("get/saved/profile")
    Call<SavedProfileModel> getSavedProfiles(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("profileReportByUser")
    Call<ObjectResponseModel> apiReport(@Field("user_id") String user_id,
                                        @Field("friend_id") String friend_id,
                                        @Field("message") String message,
                                        @Field("type") String type);


    @FormUrlEncoded
    @POST("block-user")
    Call<ObjectResponseModel> apiRemoveUser(@Field("user_id") String user_id,
                                           @Field("blocked_id") String block_user_id

    );

}
