package com.plugspace.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.plugspace.R;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Utils;
import com.plugspace.firebase.MyFirebaseMessagingService;
import com.plugspace.model.LoginDataModel;

public class BaseActivity extends AppCompatActivity {
    //    public static ProgressDialog progressDialog = null;
    public static Dialog dialog = null;
    public static Activity activity;

    public void setDarkPurpleStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

//    public static void showProgressDialog(Activity activity) {
//
//        if (activity != null && !activity.isDestroyed()) {
//            if (progressDialog == null || !progressDialog.isShowing()) {
//                try {
//                    progressDialog = new ProgressDialog(activity);
//                    progressDialog.setTitle(activity.getResources().getString(R.string.app_name));
//                    progressDialog.setMessage(activity.getResources().getString(R.string.msg_loading_please_wait));
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void hideProgressDialog(Activity activity) {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//    }


    public static void showProgressDialog(Activity activity) {

        // TODO: 19/2/22 Confirm: @Client side confirm loader show now uncomment below code.

        if (activity != null && !activity.isDestroyed()) {
            if (dialog == null || !dialog.isShowing()) {
                dialog = new Dialog(activity, R.style.MyDialogStyle);

                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                }
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_loader);
                dialog.setTitle("");

                dialog.show();
            }
        }
    }

    public static void hideProgressDialog(Activity activity) {
        // TODO: 19/2/22 Confirm: @Client side confirm loader show now uncomment below code.

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        activity = this;
        setDarkPurpleStatusBar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    protected void onStart() {
        super.onStart();

        MyFirebaseMessagingService.setOnMatchProfileClickListener(model -> {
            if (model != null) {
                dialogMatchProfile(activity, model);
            }
        });
    }

    public static void dialogMatchProfile(Activity activity, LoginDataModel model) {

        if (activity != null && !activity.isDestroyed()) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> activity.runOnUiThread(() -> {
                if (!activity.isDestroyed()) {

                    Dialog dialogMatchProfile = new Dialog(activity, R.style.MyDialogStyle);

                    if (dialogMatchProfile.getWindow() != null) {
                        dialogMatchProfile.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    }
                    dialogMatchProfile.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialogMatchProfile.setCanceledOnTouchOutside(false);
                    dialogMatchProfile.setCancelable(false);
                    dialogMatchProfile.setContentView(R.layout.dialog_match_profile);
                    dialogMatchProfile.setTitle("");

                    if (model != null) {
                        if (!Utils.isValidationEmpty(model.getNotiId())) {
                            Utils.readNotification(Integer.parseInt(model.getNotiId()));
                        }

                        if (!Utils.isValidationEmpty(model.getName())) {
                            String message = activity.getResources().getString(R.string.msg_you_and_s_have_liked_each_other, model.getName());

                            TextView tvMessage = dialogMatchProfile.findViewById(R.id.tvMessage);
                            tvMessage.setText(message);
                        }
                    }

                    dialogMatchProfile.findViewById(R.id.btnCancel).setOnClickListener(v -> dialogMatchProfile.dismiss());
                    dialogMatchProfile.findViewById(R.id.btnMessage).setOnClickListener(v -> dialogMatchProfile.dismiss());

                    dialogMatchProfile.show();


                }
            }), 1000);

        }
    }
}
