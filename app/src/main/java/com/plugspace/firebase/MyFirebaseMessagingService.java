package com.plugspace.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.plugspace.PlugSpaceApplication;
import com.plugspace.R;
import com.plugspace.activities.HomeActivity;
import com.plugspace.activities.LoginActivity;
import com.plugspace.activities.WelcomeVideoActivity;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;

import java.util.Objects;
import java.util.Random;

/**
 * Created by comp on 2019-03-29.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    NotificationCompat.Builder notificationBuilder = null;
    public static NotificationManager notificationManager = null;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static Interfaces.OnNotificationRefreshClickListener onNotificationRefreshClickListener;

    public static void setOnNotificationRefreshClickListener(Interfaces.OnNotificationRefreshClickListener onNotificationRefreshClickListener) {
        MyFirebaseMessagingService.onNotificationRefreshClickListener = onNotificationRefreshClickListener;
    }

    public static Interfaces.OnMatchProfileClickListener onMatchProfileClickListener;

    public static void setOnMatchProfileClickListener(Interfaces.OnMatchProfileClickListener onMatchProfileClickListener) {
        MyFirebaseMessagingService.onMatchProfileClickListener = onMatchProfileClickListener;
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Utils.setFirebaseTokenOnPreferences(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = PlugSpaceApplication.getAppContext().getResources().getString(R.string.app_name);
        if (remoteMessage.getData().size() > 0) {
            Logger.d("response_notification", new Gson().toJson(remoteMessage.getData()));


            if (remoteMessage.getData().get("title") != null && !Utils.isValidationEmpty(remoteMessage.getData().get("title"))) {
                title = remoteMessage.getData().get("title");
            }

            String message = "";
            if (remoteMessage.getData().get("message") != null && !Utils.isValidationEmpty(remoteMessage.getData().get("message"))) {
                message = remoteMessage.getData().get("message");
            }
            if (Utils.isValidationEmpty(message)) {
                if (remoteMessage.getData().get("text") != null && !Utils.isValidationEmpty(remoteMessage.getData().get("text"))) {
                    message = remoteMessage.getData().get("text");
                }
            }
            String isMatch = Constants.IS_MATCH_0;
            if (remoteMessage.getData().get("is_match") != null && !Utils.isValidationEmpty(remoteMessage.getData().get("is_match"))) {
                isMatch = remoteMessage.getData().get("is_match");
            }

            if (!Utils.isValidaEmptyWithZero(Preferences.getStringName(Preferences.keyUserId))) {
                SetNotification(title, message, isMatch);
            }
        } else {
            String message = "";
            String isMatch = Constants.IS_MATCH_0;
            try {
                Logger.d("response_notification", new Gson().toJson(Objects.requireNonNull(remoteMessage).getNotification()));

                if (remoteMessage.getNotification() != null && remoteMessage.getNotification().getBody() != null) {
                    message = remoteMessage.getNotification().getBody();
                }


                SetNotification(title, message, isMatch);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void SetNotification(String title, String message, String isMatch) {


        String CHANNEL_ID = "my_channel_01";
        CharSequence name = getString(R.string.app_name);

        int importance = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_DEFAULT;
        }
        int notificationId = new Random().nextInt(60000);

        Intent intent;
        String userId = Preferences.getStringName(Preferences.keyUserId);
        LoginDataModel dataModel = Preferences.GetLoginDetails();

        if (!Utils.isValidaEmptyWithZero(userId) && dataModel != null) {
            intent = new Intent(this, HomeActivity.class);
            intent.putExtra(Constants.INTENT_KEY_IS_FROM, Constants.IS_FROM_NOTIFICATION);
        } else {
            intent = new Intent(this, WelcomeVideoActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);


        notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_app_logo);
            notificationBuilder.setColor(getResources().getColor(R.color.colorAccent));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }

        notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setSmallIcon(R.drawable.ic_app_logo);
            notificationBuilder.setColor(getResources().getColor(R.color.colorAccent));

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.enableLights(true);
            mChannel.setShowBadge(true);
            mChannel.setLightColor(getColor(R.color.colorAccent));

            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder.setChannelId(CHANNEL_ID);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance_high = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance_high);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            assert notificationManager != null;
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        assert notificationManager != null;
        notificationManager.notify(notificationId, notificationBuilder.build());


        if (onNotificationRefreshClickListener != null) {
            onNotificationRefreshClickListener.notificationRefreshClick();
        }

        if (!Utils.isValidaEmptyWithZero(userId) && dataModel != null && isMatch.equals(Constants.IS_MATCH_1) && onMatchProfileClickListener != null) {
            LoginDataModel model = new LoginDataModel();
            model.setName(title);
            model.setMessage(message);
            model.setNotiId(notificationId+"");
            onMatchProfileClickListener.matchProfileClick(model);
        }

    }

}
