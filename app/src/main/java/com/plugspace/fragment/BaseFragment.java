package com.plugspace.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.Window;

import androidx.fragment.app.Fragment;

import com.plugspace.R;

import static com.plugspace.activities.BaseActivity.dialog;
//import static com.plugspace.activities.BaseActivity.progressDialog;

public class BaseFragment extends Fragment {

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
}