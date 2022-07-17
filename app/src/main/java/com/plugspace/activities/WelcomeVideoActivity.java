package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import com.plugspace.R;
import com.plugspace.common.Constants;

public class WelcomeVideoActivity extends BaseActivity {
    private Activity activity;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_welcome_video);


        activity = this;
        initComponents();
        initClick();
    }

    private void initClick() {
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
//            Preferences.setBooleanValue(Preferences.keyIsShowWelcomeScreen, true); // : 2/2/22 Test: uncomment this line

            if (videoView != null && videoView.isPlaying()) {
                videoView.pause();
                videoView = null;
            }

            startActivity(new Intent(activity, LoginActivity.class));
            finish();
        });

        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
//            Preferences.setBooleanValue(Preferences.keyIsShowWelcomeScreen, true); // : 2/2/22 Test: uncomment this line

            if (videoView != null && videoView.isPlaying()) {
                videoView.pause();
                videoView = null;
            }

            startActivity(new Intent(activity, LoginActivity.class).putExtra(Constants.INTENT_KEY_IS_FROM, Constants.IS_FROM_SIGN_UP));
            finish();
        });
    }

    private void initComponents() {
        videoView = findViewById(R.id.videoView);

//        videoView.setVideoPath(Constants.welcomeScreenVideo);
//        videoView.start();

        String path = "android.resource://" + getPackageName() + "/" + R.raw.welcome_video;
        videoView.setVideoURI(Uri.parse(path));
        videoView.start();

        videoView.setOnCompletionListener(mp -> {

//            Preferences.setBooleanValue(Preferences.keyIsShowWelcomeScreen, true); // : 2/2/22 Test: uncomment this line

//            startActivity(new Intent(activity, LoginActivity.class));
//            finish();

            if (videoView != null) {
                videoView.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
            videoView = null;
        }
    }
}