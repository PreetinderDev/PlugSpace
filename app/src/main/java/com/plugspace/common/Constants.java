package com.plugspace.common;

import com.plugspace.model.LoginDataModel;

public class Constants {
            public static long delayMillis=2000;

    public static int SelectedPosition = 0;
    public static int POSITION = 0;
    public static int DESCRIBE_POSITION = 0;
    public static String CategoryName = "CategoryName";
    public static String from = "FROM";
    public static String categoryListFrom = "categoryList";
    public static String musicFavFrom = "musicFavFrom";
    public static LoginDataModel loginDataModel;
    public static LoginDataModel loginScreenDataModel;
    public static String deviceType = "android";
    public static String token = "Rokk7uW0jrwQqMWteTDCqP25TxWqSR8o";
    public static String MEDIA_LIST = "Media";
    public static String RANK = "Rank";
    public static String LIKE = "Like";
    public static String DISLIKE = "DisLike";
    //    public static String ODD_NUM = "1";
//    public static String EVEN_NUM = "2";
    public static String ARRAY_LIST = "arrayList";

    public static String INTENT_KEY_MIME_TYPE = "INTENT_KEY_MIME_TYPE";
    public static String INTENT_KEY_MODEL = "INTENT_KEY_MODEL";
    public static String INTENT_KEY_LIST = "INTENT_KEY_LIST";
    public static String INTENT_KEY_SAVE_LIST = "INTENT_KEY_SAVE_LIST";
    public static String INTENT_KEY_LIST_2 = "INTENT_KEY_LIST_2";
    public static String INTENT_KEY_SELECTED_POSITION = "INTENT_KEY_SELECTED_POSITION";
    public static String INTENT_KEY_MODEL_OTHER_POST = "INTENT_KEY_MODEL_OTHER_POST";
    public static String INTENT_KEY_IS_FROM = "INTENT_KEY_IS_FROM";
    public static String INTENT_KEY_USER_ID = "INTENT_KEY_USER_ID";
    public static String INTENT_KEY_POSITION_TAB = "INTENT_KEY_POSITION_TAB";
    public static String INTENT_KEY_IS_UPDATE = "INTENT_KEY_IS_UPDATE";

    public static String DATA_MODEL = "dataModel";
    public static String USER_ID = "userId";
    public static String VIEW_COUNT = "viewCount";
    public static String FROM = "from";
    public static String REMOVE_PHOTO = "REMOVE_PHOTO";
    public static String ADD_PHOTO = "ADD_PHOTO";
    //    public static String PRIVATE = "PRIVATE";
//    public static String UN_PRIVATE = "UN_PRIVATE";
//    public static String PLUG_SPACE_RANK = "PLUG_SPACE_RANK";
//    public static String PLUG_CHARA_MSG = "PLUG_CHARA_MSG";
    public static String PROFILE = "profile";
    public static String FEED = "feed";

//    // firebase node
//    Chat Functionality(Firebase Process)
//
//* Chatting :- (CHATTING)
//            2_6 (small sender user_id underscore big receiver user_id) or (small receiver user_id underscore big sender user_id)
//1642736706454 (unique key)
//
//    message_status (1 for send, 2 for read)
//    message (sender any message send to receiver)
//    name  (sender name)
//    time (UTC time (milli seconds))
//    user_id (sender user_id)
//    device_token (sender device_token)
//    device_type (sender device_type)
//
//
//* chat_list :- (CHAT_LIST)
//            2 (sender user_id)
//            6 (receiver user_id)
//
//    read_count (0, 4, 5, 20, and etc…)
//    message (sender or receiver any last message send)
//    name  (receiver name)
//    time (UTC time (milli seconds))
//    user_id (receiver user_id)
//    profile (receiver profile)
//    device_token (receiver device_token)
//    device_type (receiver device_type)
//
//
//* active_user :- (ACTIVE_USER)
//            1:0 (user_id : 1 for online and 0 for offline)
//            5:1 (user_id : 1 for online and 0 for offline)

    //        public static String FIREBASE_FOLDER_ACTIVE_USER = "active_user";
    public static String FIREBASE_FOLDER_ACTIVE_USER = "ACTIVE_USER";
    //    public static String FIREBASE_FOLDER_CHATTING = "Chatting";
    public static String FIREBASE_FOLDER_CHATTING = "CHATTING";
    //    public static String FIREBASE_FOLDER_CHAT_LIST = "chat_list";
    public static String FIREBASE_FOLDER_CHAT_LIST = "CHAT_LIST";

    public static String SENDER_TIME = "Time";
    public static String SENDER_MSG = "Msg";
    public static String SENDER_USER_ID = "UserId";
    public static String SENDER_NAME = "Name";
    public static String SENDER_PROFILE = "Image";
    public static String SENDER_MESSAGE_STATUS = "Message_Status";
    public static String position = "position";

    public static String FIREBASE_KEY_MESSAGE_STATUS = "message_status";
    public static String FIREBASE_KEY_MESSAGE = "message";
    public static String FIREBASE_KEY_NAME = "name";
    public static String FIREBASE_KEY_TIME = "time";
    public static String FIREBASE_KEY_USER_ID = "user_id";
    public static String FIREBASE_KEY_DEVICE_TOKEN = "device_token";
    public static String FIREBASE_KEY_DEVICE_TYPE = "device_type";
    public static String FIREBASE_KEY_READ_COUNT = "read_count";
    public static String FIREBASE_KEY_PROFILE = "profile";

    //    active_user
    public static String FIREBASE_VALUE_ACTIVE_USER_0 = "0"; // 0 for offline
    public static String FIREBASE_VALUE_ACTIVE_USER_1 = "1"; // 1 for online

    //    message_status
    public static String FIREBASE_VALUE_MESSAGE_STATUS_1 = "1"; // 1 for send
    public static String FIREBASE_VALUE_MESSAGE_STATUS_2 = "2"; // 2 for read

    //    read_count
    public static String FIREBASE_VALUE_READ_COUNT_0 = "0";

    //// is manual email
    public static String isManualEmail_0 = "0"; //// TODO: 17/1/22 Confirm: Client side confirm whole app email not used.
    public static String isManualEmail_1 = "1";

    //  is_story
    public static String isStory_0 = "0";
    public static String isStory_1 = "1";

    //  is_like
    public static String isLike_1 = "1";
    public static String isLike_2 = "2";

    //    read_count
    public static String readCount_0 = "0";

    //    public static String DATE_FORMAT_SLASH_DD_MM_YYYY_HH_MM_AA = "dd/MM/yyyy hh:mm aa";
//    public static String DATE_FORMAT_HH_MM_AA = "hh:mm aa";
    public static String DATE_FORMAT_HH_MM_AA = "hh:mm aa";
    public static String DATE_FORMAT_SLASH_MM_DD_YY = "MM/dd/yy";
    //    public static String DATE_FORMAT_SLASH_YYYY_MM_DD = "yyyy/MM/dd";
    public static String DATE_FORMAT_YYYY_MM_DD_DASH = "yyyy-MM-dd";
    public static String DATE_FORMAT_YYYY = "yyyy";
    public static String DATE_FORMAT_MM = "MM";
    public static String DATE_FORMAT_DD = "dd";
    public static String DATE_FORMAT_MMM_SPACE_DD_COMMA_SPACE_YYYY = "MMM dd, yyyy";
    //    public static String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-mm-dd hh:mm:ss";
    public static String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-mm-dd HH:mm:ss";

    //    public static String DATE_FORMAT_HH_COLUMN_MM_SPACE_AA = "hh:mm aa";
//    public static String DATE_FORMAT_M_SLASH_D_SLASH_YY_COMMA_HH_COLUMN_MM_SPACE_AA = "M/d/yy, hh:mm aa";


//    public static String welcomeScreenVideo = "https://appkiduniya.in/Plugspace/public/plugspace_video.mp4"; // : Confirm: now remove this url.


    //    request code
    public static final int REQUEST_CODE_MULTIPLE_PHOTO_CHOOSER = 1;
    public static final int REQUEST_CODE_CAPTURE_VIDEO = 2;
    public static final int REQUEST_CODE_SELECT_VIDEO_GALLERY = 3;
    public static final int REQUEST_CODE_VIEW_STORY = 4;

    public static final String INTENT_EXTRA_IMAGES = "images";
    public static final String INTENT_EXTRA_LIMIT = "limit";
    public static final int EXTRA_LIMIT_IMAGES = 7;
    //    public static final int EXTRA_VIDEO_DURATION_LIMIT = 40;
    public static final int EXTRA_VIDEO_DURATION_LIMIT = 20;
    public static final int STORY_LINE_TIME_DELAY_MILLIS = EXTRA_VIDEO_DURATION_LIMIT+1000;

    public static boolean IS_HOME = true;
    public static String IS_FROM_SIGN_UP = "IS_FROM_SIGN_UP";
    public static String IS_FROM_NOTIFICATION = "IS_FROM_NOTIFICATION";
    public static String IS_FROM_BLOCK = "IS_FROM_BLOCK";

//    public static final String MIME_TYPE_VIDEO = "video/*";
//    public static final String MIME_TYPE_IMAGE = "image/*";

    public static String MIME_TYPE_VIDEO_CONTAINS = "video";
    public static String MIME_TYPE_IMAGE_CONTAINS = "image";
    public static String MIME_TYPE_IMAGES_CONTAINS = "images";
    public static String MIME_TYPE_TEXT = "text/plain";
    public static String MIME_TYPE_IMAGE = "image/*";
    public static String MIME_TYPE_VIDEO = "video/*";
    public static String MIME_TYPE_ALL = " */*";

    //    media_type
    public static final String MEDIA_TYPE_IMAGE = "image";
    public static final String MEDIA_TYPE_VIDEO = "video";

    //    is_favorite
    public static final String IS_FAVOURITE_0 = "0";
    public static final String IS_FAVOURITE_1 = "1";

    //    music_type
    public static final String MUSIC_TYPE_EXERCISE = "exercise";
    public static final String MUSIC_TYPE_RELAX = "relax";
    public static final String MUSIC_TYPE_CARS = "cars";
    public static final String MUSIC_TYPE_WEDDING = "wedding";
    public static final String MUSIC_TYPE_REGIONS = "regions";

    //    isGeoLocation
    public static final String IS_GEO_LOCATION_1 = "1";
    public static final String IS_GEO_LOCATION_0 = "0";

    //    isPrivate
    public static final String IS_PRIVATE_0 = "0";
    public static final String IS_PRIVATE_1 = "1";


    //    response code
    public static final int RESPONSE_CODE_1 = 1; // success
    public static final int RESPONSE_CODE_2 = 2;
    public static final int RESPONSE_CODE_5 = 5; // de activated account by admin

    //    is_match
    public static final String IS_MATCH_0 = "0";
    public static final String IS_MATCH_1 = "1";

    //    report type (Param type)
    public static final String REPORT_TYPE_STORY = "story";
    public static final String REPORT_TYPE_FEED = "feed";
    public static final String REPORT_TYPE_PROFILE = "profile";
}
