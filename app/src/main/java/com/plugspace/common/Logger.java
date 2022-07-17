package com.plugspace.common;

import android.util.Log;

import com.plugspace.BuildConfig;
import com.plugspace.PlugSpaceApplication;
import com.plugspace.R;

public class Logger {
    public static final boolean DEBUGGING_BUILD = BuildConfig.DEBUG;

    public static final String TAG = PlugSpaceApplication.getAppContext().getResources().getString(R.string.app_name);
    public static final String LOG_TAG = PlugSpaceApplication.getAppContext().getResources().getString(R.string.log_empty_tag);
    public static final String LOG_MESSAGE = PlugSpaceApplication.getAppContext().getResources().getString(R.string.log_empty_msg);
    public static final String LOG_SYMBOL = PlugSpaceApplication.getAppContext().getResources().getString(R.string.log_symbol);

    public static void d(String mTag, Object mMsg) {
        if (DEBUGGING_BUILD) {
            Log.d(TAG + LOG_SYMBOL + Utils.checkEmptyWithNull(mTag, LOG_TAG), LOG_SYMBOL + Utils.checkEmptyWithNull(mMsg, LOG_MESSAGE));
        }
    }

    public static void i(String mTag, String mMsg) {
        if (DEBUGGING_BUILD) {
            Log.i(TAG + LOG_SYMBOL + Utils.checkEmptyWithNull(mTag, LOG_TAG), LOG_SYMBOL + Utils.checkEmptyWithNull(mMsg, LOG_MESSAGE));
        }
    }

    public static void e(String mTag, Object mMsg) {
        if (DEBUGGING_BUILD) {
            Log.e(TAG + LOG_SYMBOL + Utils.checkEmptyWithNull(mTag, LOG_TAG), LOG_SYMBOL + Utils.checkEmptyWithNull(mMsg, LOG_MESSAGE));
        }
    }
}
