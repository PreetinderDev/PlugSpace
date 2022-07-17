package com.plugspace.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.plugspace.PlugSpaceApplication;
import com.plugspace.R;
import com.plugspace.adapters.PreviewAdapter;
import com.plugspace.firebase.MyFirebaseMessagingService;
import com.skydoves.powermenu.CircularEffect;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnDismissedListener;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utils {

    public static void showAlert(final Activity activity, String message) {
        showAlert(activity, activity.getResources().getString(R.string.app_name), message);
    }

    public static void showAlert(final Activity activity, String title, String message) {
        if (activity != null && !activity.isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAppCompatAlertDialogStyle);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setMessage(message);
//            builder.setPositiveButton(Html.fromHtml("<font color='#FA5A20'> " + "OK" + "</font>"), null);
            builder.setPositiveButton(activity.getResources().getString(R.string.ok), null);
            builder.show();
        }
    }

    public static void showAlert(final Activity activity, String message, DialogInterface.OnClickListener onClickListener) {
        if (activity != null && !activity.isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAppCompatAlertDialogStyle);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(activity.getResources().getString(R.string.app_name));
            builder.setMessage(message);
//            builder.setPositiveButton(Html.fromHtml("<font color='#FA5A20'> " + "OK" + "</font>"), onClickListener);
            builder.setPositiveButton(activity.getResources().getString(R.string.ok), onClickListener);
            builder.setCancelable(false);
            builder.show();
        }
    }

    public static void showAlertTwoButtons(final Activity activity, String message,
                                           DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener) {
        if (activity != null && !activity.isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAppCompatAlertDialogStyle);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(activity.getResources().getString(R.string.app_name));
            builder.setMessage(message);
//            builder.setPositiveButton(Html.fromHtml("<font color='#FA5A20'> " + "OK" + "</font>"), onClickListener);
            builder.setPositiveButton(activity.getResources().getString(R.string.ok), positiveClickListener);
            builder.setNegativeButton(activity.getResources().getString(R.string.subscribe), negativeClickListener);
            builder.setCancelable(false);
            builder.show();
        }
    }

    public static boolean isValidPhoneNumber(String number) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", number)) {
            if (number.length() < 6 || number.length() > 13) {
                if (number.length() != 6) {
                    check = false;
                }

            } else {
                check = android.util.Patterns.PHONE.matcher(number).matches();
            }
        } else {
            check = false;
        }
        return check;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showCustomDialog(Activity activity, String title, String message, String
            positiveButton, DialogInterface.OnClickListener listenerPositive, String
                                                negativeButton, DialogInterface
                                                .OnClickListener listenerNegative,
                                        boolean isCancel, boolean isNegative) {

        if (activity != null && !((Activity) activity).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setCancelable(isCancel);

            builder.setMessage(message).setPositiveButton(positiveButton, listenerPositive);
            if (isNegative) {
                builder.setNegativeButton(negativeButton, listenerNegative);
            }

            AlertDialog mAlertDialog = builder.create();
            if (!mAlertDialog.isShowing()) {
                mAlertDialog.show();
            }
            Button pbutton = mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button nbutton = mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            pbutton.setBackgroundColor(Color.WHITE);
            pbutton.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            nbutton.setBackgroundColor(Color.WHITE);
            nbutton.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        }
    }

    public static Boolean isValidationEmpty(String value) {
        return value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null") || value.trim().equalsIgnoreCase("") || value.trim().length() == 0;
    }

    public static PowerMenu showDeleteEditMenu(
            Context context,
            PreviewAdapter lifecycleOwner,
            OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener,
            OnDismissedListener onDismissedListener) {

        return new PowerMenu.Builder(context)
                .addItem(new PowerMenuItem(context.getString(R.string.edit), false))
                .addItem(new PowerMenuItem(context.getString(R.string.delete), false))
                .setAutoDismiss(true)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setCircularEffect(CircularEffect.BODY)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
                .setTextSize(14)
                .setTextGravity(Gravity.LEFT)
                .setTextTypeface(Typeface.createFromAsset(context.getAssets(), "font/" + context.getString(R.string.font_times_regular)))
                .setSelectedTextColor(Color.BLACK)
                .setMenuColor(ContextCompat.getColor(context, R.color.colorWhite))
                .setSelectedMenuColor(ContextCompat.getColor(context, R.color.colorWhite))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setOnDismissListener(onDismissedListener)
                .setPreferenceName("HamburgerPowerMenu")
                .setInitializeRule(Lifecycle.Event.ON_CREATE, 0)
                .build();
    }

    public static Boolean isValidaEmptyWithZero(String value) {
//        return value == null || value.isEmpty() || value.equalsIgnoreCase("null") || value.equalsIgnoreCase("0");
        return value == null || value.isEmpty() || value.equalsIgnoreCase("null") || Integer.parseInt(value) <= 0;
    }

    public static boolean isNetworkAvailable(final Context context, boolean canError,
                                             final boolean isFinish) {
        boolean isNetAvailable = false;

        if (context != null) {
            final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mConnectivityManager != null) {
                boolean mobileNetwork = false;
                boolean wifiNetwork = false;
                boolean mobileNetworkConnecetd = false;
                boolean wifiNetworkConnecetd = false;

                final NetworkInfo mobileInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                final NetworkInfo wifiInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mobileInfo != null) {
                    mobileNetwork = mobileInfo.isAvailable();
                }

                if (wifiInfo != null) {
                    wifiNetwork = wifiInfo.isAvailable();
                }

                if (wifiNetwork || mobileNetwork) {
                    if (mobileInfo != null)
                        mobileNetworkConnecetd = mobileInfo
                                .isConnectedOrConnecting();
                    wifiNetworkConnecetd = wifiInfo.isConnectedOrConnecting();
                }

                isNetAvailable = (mobileNetworkConnecetd || wifiNetworkConnecetd);
            }
            context.setTheme(R.style.AppTheme);
            if (!isNetAvailable && canError) {
                Logger.d("TAG", "context : " + context.toString());
                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showAlertWithFinishDialog((Activity) context, context.getString(R.string.app_name),
                                    context.getString(R.string.error_network),
                                    isFinish);
                        }
                    });
                }
            }
        }
        return isNetAvailable;
    }

    public static void showAlertWithFinishDialog(final Activity activity, String title, String message, final boolean isFinish) {
        if (activity != null && !((Activity) activity).isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAppCompatAlertDialogStyle);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle(title);
            builder.setCancelable(false);
            builder.setMessage(message);
            builder.setPositiveButton(activity.getString(R.string.ok), (dialog, which) -> {
                if (isFinish) {
                    if (!activity.isFinishing()) {
                        dialog.dismiss();
                        activity.finish();
                    }
                } else {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();

            try {
                alert.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showAlertLogoutConfirmDialog(Activity activity, DialogInterface.OnClickListener positiveClickListener) {
        if (activity != null && !((Activity) activity).isFinishing()) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
            builder.setTitle(activity.getResources().getString(R.string.app_name));
            builder.setMessage(activity.getResources().getString(R.string.msg_logout_confirm));
            builder.setPositiveButton(activity.getResources().getString(R.string.yes), positiveClickListener);
            builder.setNegativeButton(activity.getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();
        }
    }

    public static RequestBody convertValueToRequestBody(String value) {
        if (value == null || Utils.isValidationEmpty(value)) {
            value = "";
        }
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public static String TimeStamp() {
        Date date = new Date();
        //This method returns the time in millis
        long timeMilli = date.getTime();
        System.out.println("Time in milliseconds using Date class: " + timeMilli);
        return String.valueOf(timeMilli);
    }

    public static String CurrentChatTime(String time) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm a");

        return mdformat.format(calendar.getTime());
    }

    public static long currentTimeInMilliSeconds() {
        long currentTime = System.currentTimeMillis();
        // TODO: 21/1/22 Pending: UTC convert
//        return String.valueOf(currentTime);
        return currentTime;
    }

    public static String checkEmptyWithNull(Object value, Object defaultValue) {
        if (value != null && !Utils.isValidationEmpty(String.valueOf(value)))
            return String.valueOf(value);
        else
            return String.valueOf(defaultValue);
    }

    public static Object checkNull(Object value, Object defaultValue) {
        if (value != null)
            return value;
        else
            return defaultValue;
    }

    public static void hideKeyBoard(Activity mActivity, View view) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyBoard(Activity mActivity, View v) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    public static void showToast(Activity activity, String msgToast) {
        if (activity != null && !activity.isFinishing()) {
            if (Utils.isValidationEmpty(msgToast)) {
                msgToast = activity.getResources().getString(R.string.log_empty_msg);
            }
            Toast.makeText(activity, msgToast, Toast.LENGTH_SHORT).show();
        }
    }

    public static String milliSecToDateTimeFormat(String milliSeconds, String outputDateFormat) {

        if (!Utils.isValidationEmpty(String.valueOf(milliSeconds))) {
            long ms = Long.parseLong(milliSeconds);
//            @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat(inputDateFormat);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat(outputDateFormat);
            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ms);
            return output.format(calendar.getTime());
        } else {
            return "";
        }

    }

    public static void setActiveUser(boolean isActiveUser) {
        String activeUser = Constants.FIREBASE_VALUE_ACTIVE_USER_0;
        if (isActiveUser) {
            activeUser = Constants.FIREBASE_VALUE_ACTIVE_USER_1;
        }
        try {
            FirebaseDatabase.getInstance().getReference()
                    .child(Constants.FIREBASE_FOLDER_ACTIVE_USER)
                    .child(Preferences.getStringName(Preferences.keyUserId))
                    .setValue(activeUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageBg(Activity activity, String path, ImageView imageView) {
        Utils.setImageFull(activity, path, imageView, R.drawable.bg_place_holder_photo, R.drawable.bg_error_photo);
    }
    public static void setImageProfile(Activity activity, String path, ImageView imageView) {
        Utils.setImageFull(activity, path, imageView, R.drawable.bg_place_holder_photo, R.drawable.ic_profile_placeholder);
    }

    public static void setImageFull(Activity activity, String path, ImageView imageView, @DrawableRes int icLoading, @DrawableRes int icError) {
        Glide.with(activity).load(path).placeholder(icLoading).error(icError).into(imageView);
    }

    public static String getDateHH_MM_AA(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("hh:mm aa", cal).toString();
    }

    public static String getDateMessage(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format(Constants.DATE_FORMAT_SLASH_MM_DD_YY, cal).toString();
    }

    public static String getDateChat(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format(Constants.DATE_FORMAT_MMM_SPACE_DD_COMMA_SPACE_YYYY, cal).toString();
    }

//    public static String getTimeStampChat(long time) {
//        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//        cal.setTimeInMillis(time);
//        return DateFormat.format(Constants.DATE_FORMAT_M_SLASH_D_SLASH_YY_COMMA_HH_COLUMN_MM_SPACE_AA, cal).toString();
//    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("d", cal).toString();
    }

    public static String customDateTimeFormat(String dateTime, String inputDateFormat, String outputDateFormat) {

        if (!Utils.isValidationEmpty(dateTime)) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat(inputDateFormat);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat output = new SimpleDateFormat(outputDateFormat);
            try {
                Date date = input.parse(dateTime);                 // parse input
                return output.format(Objects.requireNonNull(date));    // format output
            } catch (ParseException e) {
                e.printStackTrace();
                return dateTime;
            }
        } else {
            return "";
        }
    }

    public static long customDateTimeFormat(String dateTime, String inputDateFormat) throws ParseException {

        if (!Utils.isValidationEmpty(dateTime)) {
            SimpleDateFormat formatter = new SimpleDateFormat(inputDateFormat); //   "created_date": "2022-02-05 02:14:39"
            formatter.setLenient(false);

            Date oldDate = formatter.parse(dateTime);
            if (oldDate != null) {
                return oldDate.getTime();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static String currentDateTimeFormat(String outputDateFormat) {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat(outputDateFormat, Locale.getDefault());
        return df.format(c);
    }

//    // UPDATED! (select video from gallery)
//    public static String getPath(Activity activity, Uri uri) {
//        String[] projection = {MediaStore.Video.Media.DATA};
//        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
//        if (cursor != null) {
//            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else
//            return null;
//    }

    // capture video
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = true;

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                //  handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static void setFirebaseTokenOnPreferences() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
//                        Loggers.d(TAG, "Fetching FCM registration token failed: " + task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    Utils.setFirebaseTokenOnPreferences(token);
                });
    }

    public static void setFirebaseTokenOnPreferences(String token) {
        Preferences.SetStringName(Preferences.keyFirebaseToken, token);
        Logger.d("test_firebase_token: ", token);
    }

    public static MultipartBody.Part getMultipartBody(Activity activity, File file, String paramName) {

        if (file != null && file.exists()) {
            String mediaType = Utils.returnMediaTypeUsingFileForPassApi(activity, file);
            Logger.d("test_media_type", mediaType);
            RequestBody requestFile = RequestBody.create(MediaType.parse(mediaType), file);

            try {
                return MultipartBody.Part.createFormData(paramName,
                        URLEncoder.encode(file.getName(), "utf-8"), requestFile);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return MultipartBody.Part.createFormData(paramName,
                        file.getName(), requestFile);
            }
        } else {
            return null;
        }
    }

    public static String returnMediaTypeUsingFileForPassApi(Activity activity, File file) {
        String mimeType = "";
        if (file != null && file.exists()) {
            mimeType = Utils.getMimeTypeFromFile(file, activity);
        }

        if (!Utils.isValidationEmpty(mimeType) && mimeType.contains(Constants.MIME_TYPE_VIDEO_CONTAINS)) {
            return Constants.MIME_TYPE_VIDEO;
        } else if (!Utils.isValidationEmpty(mimeType) && mimeType.contains(Constants.MIME_TYPE_IMAGE_CONTAINS) || !Utils.isValidationEmpty(mimeType) && mimeType.contains(Constants.MIME_TYPE_IMAGES_CONTAINS)) {
            return Constants.MIME_TYPE_IMAGE;
        } else {
            return Constants.MIME_TYPE_ALL;
        }

    }

    public static String getMimeTypeFromFile(File file, Activity activity) {
        if (file != null && file.exists()) {

            Uri uri = FileUtils.getUri(file);
            String mimeType = null;
            if (Objects.equals(uri.getScheme(), ContentResolver.SCHEME_CONTENT)) {
                ContentResolver cr = PlugSpaceApplication.getAppContext().getContentResolver();
                mimeType = cr.getType(uri);
            }
            if (Utils.isValidationEmpty(mimeType)) {
                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                        .toString());
                mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                        fileExtension.toLowerCase());
            }

            if (Utils.isValidationEmpty(mimeType)) {
                mimeType = PlugSpaceApplication.getAppContext().getContentResolver().getType(uri);
            }
            if (Utils.isValidationEmpty(mimeType)) {
                mimeType = URLConnection.guessContentTypeFromName(file.getPath());
            }

            if (!Utils.isValidationEmpty(uri.getPath()) && Objects.requireNonNull(uri.getPath()).contains(Constants.MIME_TYPE_VIDEO_CONTAINS)) {
                mimeType = Constants.MIME_TYPE_VIDEO;
            } else if (!Utils.isValidationEmpty(uri.getPath()) && Objects.requireNonNull(uri.getPath()).contains(Constants.MIME_TYPE_IMAGE_CONTAINS) || !Utils.isValidationEmpty(uri.getPath()) && Objects.requireNonNull(uri.getPath()).contains(Constants.MIME_TYPE_IMAGES_CONTAINS)) {
                mimeType = Constants.MIME_TYPE_IMAGE;
            }
            if (Utils.isValidationEmpty(mimeType)) {
                mimeType = "";
            }
            Logger.d("test_getMimeTypeFromFile mimeType =-=-=-=-=-=-> ", mimeType + "");
            return mimeType;

        }

        return "";
    }

    public static void readNotification() {
        if (MyFirebaseMessagingService.notificationManager != null) {
            MyFirebaseMessagingService.notificationManager.cancelAll();
        }
    }
    public static void readNotification(int notificationId) {
        if (MyFirebaseMessagingService.notificationManager != null) {
            MyFirebaseMessagingService.notificationManager.cancel(notificationId);
        }
    }
}
