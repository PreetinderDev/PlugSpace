package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;

import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;

public class SplashActivity extends Activity {
    Activity activity;
    String strAndroidId = "";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Window mWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mWindow = getWindow();
        mWindow.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        activity = this;

        strAndroidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences mPrefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("androidId", strAndroidId);
        prefsEditor.apply();

        // TODO: 21/2/22 Confirm: device country wise get country name code.
//        String locale = activity.getResources().getConfiguration().locale.getCountry();
//        Logger.d("test_locale", locale);

        Constants.loginDataModel = new LoginDataModel();
        Constants.loginScreenDataModel = new LoginDataModel();

        initView();
    }

    private void initView() {

//// this is old flow.
//        boolean isShowWelcomeScreen = Preferences.getBooleanValue(Preferences.keyIsShowWelcomeScreen);
//        if (isShowWelcomeScreen) {
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                LoginDataModel dataModel = Preferences.GetLoginDetails();
//                if (dataModel != null) {
//                    startActivity(new Intent(activity, HomeActivity.class));
//                    ActivityCompat.finishAffinity(activity);
//                } else {
////                startActivity(new Intent(activity, WelcomeActivity.class)); // : Confirm: now remove this class with other file.
//
////                    boolean isShowWelcomeScreen = Preferences.getBooleanValue(Preferences.keyIsShowWelcomeScreen);
////                    if (isShowWelcomeScreen) {
////                        startActivity(new Intent(activity, LoginActivity.class));
////                    } else {
////                        startActivity(new Intent(activity, WelcomeVideoActivity.class));
////                    }
//                    startActivity(new Intent(activity, LoginActivity.class));
//                    finish();
//                }
//            }, 2000);
//        }else {
//            startActivity(new Intent(activity, WelcomeVideoActivity.class));
//            finish();
//        }

//// this is new flow.
        String userId = Preferences.getStringName(Preferences.keyUserId);
        LoginDataModel dataModel = Preferences.GetLoginDetails();

        if (!Utils.isValidaEmptyWithZero(userId) && dataModel != null) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(activity, HomeActivity.class));
                finish();
            }, Constants.delayMillis);
        } else {
            startActivity(new Intent(activity, WelcomeVideoActivity.class));
            finish();
        }
    }
}
