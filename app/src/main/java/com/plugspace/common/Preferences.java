package com.plugspace.common;

import android.content.Context;

import com.google.gson.Gson;
import com.plugspace.PlugSpaceApplication;
import com.plugspace.R;
import com.plugspace.model.LoginDataModel;


public class Preferences {
    public static final String keyLoginData = "keyLoginData";
    public static final String keyUserId = "keyUserId";
    public static final String keyFirebaseToken = "keyFirebaseToken";
//    public static final String keyIsShowWelcomeScreen = "keyIsShowWelcomeScreen";


//    public static void setAdInteger(String Key, int clickval) {
//        get().edit().putInt(String.valueOf(Key), clickval).apply();
//    }
//
//    public static int getAdInteger(String Key) {
//        return get().getInt(Key, 1);
//    }

    private static android.content.SharedPreferences get() {
        return PlugSpaceApplication.getAppContext().getSharedPreferences(PlugSpaceApplication.getAppContext().getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static String getStringName(String Key) {
        return get().getString(Key, "");
    }

    public static void SetStringName(String Key, String Value) {
        get().edit().putString(Key, Value).apply();
    }

//    public static Float getFloatName(String Key) {
//        return get().getFloat(Key, 0);
//    }
//
//    public static void seFloatName(String Key, Float Value) {
//        get().edit().putFloat(Key, Value).apply();
//    }
//
    public static boolean getBooleanValue(String Key) {
        return get().getBoolean(String.valueOf(Key), false);
    }

    public static void setBooleanValue(String Key, boolean TrueOrFalse) {
        get().edit().putBoolean(String.valueOf(Key), TrueOrFalse).apply();
    }
//
//    public static int getInteger(String Key) {
//        return get().getInt(String.valueOf(Key), 0);
//    }
//
//    public static void setInteger(String Key, int value) {
//        get().edit().putInt(String.valueOf(Key), value).apply();
//    }
//
//    public static String getRadius(String Key) {
//        return get().getString(Key, "25");
//    }
//
//    public static void setRadius(String key, String values) {
//        get().edit().putString(key, values).apply();
//    }
//
//
//    public static void SETSAVEADSID(String Key, String Value) {
//        get().edit().putString(Key, Value).apply();
//    }
//
//    public static String GETDENAME(String Key) {
//        return get().getString(Key, "");
//    }
//
//    public static void SETDEVNAME(String Key, String Value) {
//        get().edit().putString(Key, Value).apply();
//    }
//
//    public static String GETSAVEADSID(String Key) {
//        return get().getString(Key, "");
//    }

    public static LoginDataModel GetLoginDetails() {
        try {
            Gson gson = new Gson();
            String json = get().getString(Preferences.keyLoginData, "");
            return gson.fromJson(json, LoginDataModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void SetLoginDetails(LoginDataModel loginDataModel) {
        if (loginDataModel != null) {
            Preferences.SetStringName(Preferences.keyUserId, loginDataModel.getUserId());

            if (Utils.isValidationEmpty(loginDataModel.getProfile()) && loginDataModel.getMediaDetail() != null && loginDataModel.getMediaDetail().size() > 0) {
                if (loginDataModel.getMediaDetail().get(0).getProfile() != null) {
                    String profile = loginDataModel.getMediaDetail().get(0).getProfile();
                    if (!Utils.isValidationEmpty(profile)) {
                        loginDataModel.setProfile(profile);
                    }
                }
            }
            Preferences.SetStringName(Preferences.keyLoginData, new Gson().toJson(loginDataModel));

        } else {
            Utils.readNotification();

            Preferences.SetStringName(Preferences.keyUserId, "");
            Preferences.SetStringName(Preferences.keyLoginData, "");
        }
    }
}

