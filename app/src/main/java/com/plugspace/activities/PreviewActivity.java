package com.plugspace.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mindorks.paracamera.Camera;
import com.plugspace.R;
import com.plugspace.adapters.PreviewAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviewActivity extends BaseActivity implements PreviewAdapter.MyListener {
    private Activity activity;
    private TextView tvLocation;
    private RecyclerView rvModel;
    private PreviewAdapter mAdapter;
    private LinearLayout llMyMusicChoice, llMakeOver/*, llLocation*/;
    //    private LoginDataModel loginDataModel = null;
    private ImageView ivFirstImg;
    private Camera camera;
    private final int PICK_IMAGE_GALLERY = 300;
    private String strUserId = "", strRemoveMediaID = "", strEditMediaID = "", strType = "",strSaveUserId;
    private ArrayList<MediaModel> lstModel = new ArrayList<>();
    private TextView tvNameAge, tvEducation, tvJobTitle, tvConsiderMySelf, tvRank, tvAboutMe, tvHeight,
            tvWeight, tvChildren, tvRelationStatus, tvDescribe, tvMakeOver, tvDressSize, tvEngagement, tvTattoo;
    private int positions = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        activity = this;
        getPreviousData();
        initToolBar();
        initView();
        initClick();
        doAPIPreview();
    }

    private void getPreviousData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(Constants.INTENT_KEY_USER_ID)) {
                strSaveUserId = mBundle.getString(Constants.INTENT_KEY_USER_ID);
                Log.e("INTENT_KEY_USER_ID==>", "" + strSaveUserId);

            }

        }
//        if (Utils.isValidationEmpty(strUserId) && loginDataModel == null) {
//            loginDataModel = Preferences.GetLoginDetails();
//
//            if (loginDataModel != null) {
//                strUserId = loginDataModel.getUserId();
//                lstModel = loginDataModel.getMediaDetail();
//
//                for (int i = 0; i < lstModel.size(); i++) {
//                    strType = lstModel.get(i).getType();
//                }
//            }
//        }
        if (Utils.isValidationEmpty(strUserId)) {
            strUserId = Preferences.getStringName(Preferences.keyUserId);
        }


    }

    private void doAPIPreview() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiCallPreview(
                    /*strSaveUserId*/strUserId,
                    strType);
            Log.e("SaveUserId's==>", strUserId + " , " + strSaveUserId);
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        ObjectResponseModel model = response.body();
                        if (model != null) {
                            if (model.getResponseCode() == 1) {
                                LoginDataModel loginDataModel = model.getData();
                                if (loginDataModel != null) {
//                                    Preferences.SetLoginDetails(dataModel);
//                                    Logger.e("5/1 - strUserId - ", strUserId + "");

//                                    LoginDataModel loginDataModel=dataModel;

                                    String strNameAge = loginDataModel.getName() + ", " + loginDataModel.getAge();
                                    tvNameAge.setText(strNameAge);
                                    tvEducation.setText(loginDataModel.getEducationStatus());
                                    tvJobTitle.setText(loginDataModel.getJobTitle());
                                    tvConsiderMySelf.setText(loginDataModel.getMySelfMen());
                                    tvRank.setText(loginDataModel.getRank());
                                    tvAboutMe.setText(loginDataModel.getAboutYou());
                                    tvHeight.setText(loginDataModel.getHeight());
                                    tvWeight.setText(loginDataModel.getWeight());
                                    tvChildren.setText(loginDataModel.getChildren());
                                    tvRelationStatus.setText(loginDataModel.getRelationshipStatus());
                                    tvDescribe.setText(loginDataModel.getEthinicity());
                                    tvMakeOver.setText(loginDataModel.getMakeOver());
                                    tvDressSize.setText(loginDataModel.getDressSize());
                                    tvEngagement.setText(loginDataModel.getTimesOfEngaged());
                                    tvTattoo.setText(loginDataModel.getYourBodyTatto());

                                    if (!Utils.isValidationEmpty(loginDataModel.getLocation())) {
                                        String location = loginDataModel.getLocation().trim();
                                        if (location.equals(",")) {
                                            location = "";
                                        }
                                        tvLocation.setText(location);
//                                        llLocation.setVisibility(View.VISIBLE);
                                    } else {
                                        tvLocation.setText("");
//                                        llLocation.setVisibility(View.GONE);
                                    }


                                    if (Utils.isValidationEmpty(loginDataModel.getMakeOver())) {
                                        llMakeOver.setVisibility(View.GONE);
                                    }
                                    lstModel = loginDataModel.getMediaDetail();
                                    if (lstModel != null && lstModel.size() > 0) {
                                        MediaModel modelFirst = lstModel.get(0);
                                        Glide.with(activity)
                                                .asBitmap()
                                                .placeholder(R.drawable.bg_place_holder_photo)
                                                .error(R.drawable.bg_error_photo)
                                                .load(modelFirst.getProfile())
                                                .into(ivFirstImg);
                                        lstModel.remove(0);
                                    }

                                    if (lstModel != null && lstModel.size() > 0) {
                                        mAdapter = new PreviewAdapter(activity, lstModel, PreviewActivity.this, strUserId);
                                        rvModel.setAdapter(mAdapter);
                                    }
                                } else {
                                    Utils.showAlert(activity, activity.getResources().getString(R.string.empty_data), (dialog, which) -> onBackPressed());
                                }
                            } else {
                                if (!model.getResponseMsg().isEmpty()) {
                                    Utils.showAlert(activity,
                                            model.getResponseMsg(), (dialog, which) -> onBackPressed());
                                }
                            }
                        }
                    } else {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.something_went_wrong), (dialog, which) -> onBackPressed());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
                    hideProgressDialog(activity);
                    if (!Utils.isNetworkAvailable(activity, true, false)) {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                                activity.getResources().getString(R.string.error_network));
                    } else {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                                activity.getResources().getString(R.string.technical_problem));
                    }
                    t.printStackTrace();
                }
            });
        }
    }

    private void initClick() {
        llMyMusicChoice.setOnClickListener(view -> {

            if (strUserId.equals(Preferences.getStringName(Preferences.keyUserId))) {
                startActivity(
                        new Intent(activity, MusicListActivity.class)
                                .putExtra(Constants.from, "2")
                                .putExtra(Constants.CategoryName, activity.getResources().getString(R.string.my_music_choice)));
            } else {
                LoginDataModel model = new LoginDataModel();
                model.setUserId(strUserId);

                activity.startActivity(new Intent(activity, MusicFavCategoryActivity.class)
                        .putExtra(Constants.INTENT_KEY_MODEL, model)
                        .putExtra(Constants.categoryListFrom, "1"));
            }
        });


    }

    private void initToolBar() {
        TextView tvTitleName = findViewById(R.id.tvTitleName);
        tvTitleName.setText(R.string.preview);
        findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
        findViewById(R.id.ivShowImg).setVisibility(View.GONE);
        findViewById(R.id.ivImg).setVisibility(View.GONE);
        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());
    }

    private void initView() {
        ivFirstImg = findViewById(R.id.ivFirstImg);
        llMakeOver = findViewById(R.id.llMakeOver);
//        llLocation = findViewById(R.id.llLocation);
        tvTattoo = findViewById(R.id.tvTattoo);
        tvEngagement = findViewById(R.id.tvEngagement);
        tvDressSize = findViewById(R.id.tvDressSize);
        tvMakeOver = findViewById(R.id.tvMakeOver);
        tvDescribe = findViewById(R.id.tvDescribe);
        tvRelationStatus = findViewById(R.id.tvRelationStatus);
        tvChildren = findViewById(R.id.tvChildren);
        tvWeight = findViewById(R.id.tvWeight);
        tvHeight = findViewById(R.id.tvHeight);
        tvAboutMe = findViewById(R.id.tvAboutMe);
        tvRank = findViewById(R.id.tvRank);
        tvConsiderMySelf = findViewById(R.id.tvConsiderMySelf);
        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvEducation = findViewById(R.id.tvEducation);
        tvNameAge = findViewById(R.id.tvNameAge);
        tvLocation = findViewById(R.id.tvLocation);
        llMyMusicChoice = findViewById(R.id.llMyMusicChoice);
        rvModel = findViewById(R.id.rvModel);
        rvModel.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

//        llLocation.setVisibility(View.GONE);


        if (!strUserId.equals(Preferences.getStringName(Preferences.keyUserId))) {
            ImageView ivBlock = findViewById(R.id.ivBlock);
            ivBlock.setVisibility(View.VISIBLE);
            ivBlock.setOnClickListener(v -> {
                dialogConfirmBlockUser(strUserId);
            });
        }

    }

//    @Override
//    public void onBackPressed() {
//        finish();
//    }

    private void dialogConfirmBlockUser(String userId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.msg_confirm_block_user));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            doAPIBlockUser(userId);
        });
        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void doAPIBlockUser(String userId) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Logger.d("test_block_user_id", userId);

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiBlockUser(
                    Preferences.getStringName(Preferences.keyUserId),
                    userId,
                    Constants.deviceType,
                    Constants.token
            );
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
//                    hideProgressDialog(activity);

                    if (response.isSuccessful() && response.body() != null) {
                        ObjectResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
//                            hideProgressDialog(activity);
                            Utils.showToast(activity, model.getResponseMsg());
                            onBackPressed();
                        } else {
                            hideProgressDialog(activity);
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }

                    } else {
                        hideProgressDialog(activity);
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
                    hideProgressDialog(activity);
                    if (!Utils.isNetworkAvailable(activity, true, false)) {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                                activity.getResources().getString(R.string.error_network));
                    } else {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                                activity.getResources().getString(R.string.technical_problem));
                    }
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void showPowerMenuClicked(int position, ImageView ivMore, MediaModel mediaModel, String type) {
        positions = position;

        Context myContext = new ContextThemeWrapper(activity, R.style.menuStyle);
        PopupMenu popupMenu = new PopupMenu(myContext, ivMore);
        popupMenu.getMenuInflater()
                .inflate(R.menu.preview_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
//            Toast.makeText(activity, item.getTitle(), Toast.LENGTH_SHORT).show();

            if (item.getTitle().equals(myContext.getResources().getString(R.string.edit))) {
                checkStoragePermission();

//                String strProfile = "";
                if (mediaModel.getType() != null && mediaModel.getType().equalsIgnoreCase(Constants.PROFILE)) {
//                    strProfile = lstModel.get(position).getProfile();
                    strEditMediaID = mediaModel.getMediaId();
                } else if (mediaModel.getType() != null && mediaModel.getType().equalsIgnoreCase(Constants.FEED)) {
                    strEditMediaID = mediaModel.getFeedId();
                    strEditMediaID = lstModel.get(position).getFeedId();
                }
                strType = lstModel.get(position).getType();
//                PreviewUpdatePro(strProfile, false);
            } else if (item.getTitle().equals(myContext.getResources().getString(R.string.delete))) {
                dialogConfirmDeleteFeed(mediaModel, position);
            }
            return true;
        });
        popupMenu.show();

    }

    private void dialogConfirmDeleteFeed(MediaModel mediaModel, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.msg_confirm_delete_feed));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            dialogInterface.dismiss();

            if (mediaModel.getType() != null && mediaModel.getType().equalsIgnoreCase(Constants.PROFILE)) {
                strRemoveMediaID = mediaModel.getMediaId();
            } else if (mediaModel.getType() != null && mediaModel.getType().equalsIgnoreCase(Constants.FEED)) {
                strRemoveMediaID = mediaModel.getFeedId();
            }


            if (Utils.isNetworkAvailable(activity, true, false)) {
                String strProfile = "";
                if (mediaModel.getType() != null && mediaModel.getType().equalsIgnoreCase(Constants.PROFILE)) {
                    strProfile = lstModel.get(position).getProfile();
                    strEditMediaID = mediaModel.getMediaId();
                } else if (mediaModel.getType() != null && mediaModel.getType().equalsIgnoreCase(Constants.FEED)) {
                    strProfile = lstModel.get(position).getFeedImage();
                    strEditMediaID = lstModel.get(position).getFeedId();
                }
                strType = lstModel.get(position).getType();
                PreviewUpdatePro(strProfile, true);
            }
        });
        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void checkStoragePermission() {
        Dexter.withContext(activity)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            OpenPhotoDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void OpenPhotoDialog() {
        Utils.showCustomDialog(activity,
                activity.getResources().getString(R.string.app_name),
                activity.getString(R.string.msg_select_select_option),
                activity.getString(R.string.gallery),
                (dialog, which) -> {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activity.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                }, activity.getString(R.string.camera),
                (dialog, which) -> {
                    camera = new Camera.Builder()
                            .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                            .setTakePhotoRequestCode(1)
                            .setImageFormat(Camera.IMAGE_JPEG)
                            .build(activity);
                    try {
                        camera.takePicture();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, true, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            File imgFile;
            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = camera.getCameraBitmap();
                if (bitmap != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + activity.getResources().getString(R.string.app_name) + "/" + activity.getResources().getString(R.string.folder_name_camera);
                    File dir = new File(file_path);
                    if (!dir.exists())
                        dir.mkdirs();
                    String format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
                    File file = new File(dir, format + ".png");
                    FileOutputStream fo;
                    try {
                        fo = new FileOutputStream(file);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imgFile = file;

                    String string = imgFile.getPath();
                    if (lstModel.get(positions).getType() != null && lstModel.get(positions).getType().equalsIgnoreCase(Constants.PROFILE)) {
                        lstModel.get(positions).setProfile(string);
                        strEditMediaID = lstModel.get(positions).getMediaId();
                    } else if (lstModel.get(positions).getType() != null && lstModel.get(positions).getType().equalsIgnoreCase(Constants.FEED)) {
                        lstModel.get(positions).setFeedImage(string);
                        strEditMediaID = lstModel.get(positions).getFeedId();
                    }
                    strType = lstModel.get(positions).getType();

                    if (Utils.isNetworkAvailable(activity, true, false)) {
                        PreviewUpdatePro(string, false);
                    }


                } else {
                    Toast.makeText(activity, "Picture not taken!", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PICK_IMAGE_GALLERY) {
                Uri selectedImage = data.getData();
                try {
                    imgFile = FileUtils.getFile(activity, selectedImage);
                    if (imgFile != null) {
                        String string = imgFile.getPath();
                        if (lstModel.get(positions).getType() != null && lstModel.get(positions).getType().equalsIgnoreCase(Constants.PROFILE)) {
                            lstModel.get(positions).setProfile(string);
                            strEditMediaID = lstModel.get(positions).getMediaId();
                        } else if (lstModel.get(positions).getType() != null && lstModel.get(positions).getType().equalsIgnoreCase(Constants.FEED)) {
                            lstModel.get(positions).setFeedImage(string);
                            strEditMediaID = lstModel.get(positions).getFeedId();
                        }
                        strType = lstModel.get(positions).getType();

                        if (Utils.isNetworkAvailable(activity, true, false)) {
                            PreviewUpdatePro(string, false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void PreviewUpdatePro(String imgFile, boolean isRemove) {
        showProgressDialog(activity);
        ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

        File file = new File(imgFile);
        Logger.e("5/1 - file - ", file.getPath());
        Logger.e("5/1 - strEditMediaID - ", strEditMediaID);

        // create RequestBody instance from file

        RequestBody requestFile;
        MultipartBody.Part body = null;

        if (file.exists()) {
            requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));

            body =
                    MultipartBody.Part.createFormData("feed_image", file.getName(), requestFile);

        }

        Logger.d("test_previewUpdatePro_user_id", strUserId);
        Logger.d("test_previewUpdatePro_remove_media_id", strRemoveMediaID);
        Logger.d("test_previewUpdatePro_feed_id", strEditMediaID);
        Logger.d("test_previewUpdatePro_type", strType);

        Call<ObjectResponseModel> call = service.previewUpdatePro(
                Utils.convertValueToRequestBody(strUserId),
                Utils.convertValueToRequestBody(strRemoveMediaID),
                Utils.convertValueToRequestBody(strEditMediaID),
                Utils.convertValueToRequestBody(strType),
                body);

        call.enqueue(new Callback<ObjectResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                   @NonNull Response<ObjectResponseModel> response) {
                hideProgressDialog(activity);
                if (response.isSuccessful()) {
                    ObjectResponseModel model = response.body();
                    if (model != null) {
                        if (model.getResponseCode() == 1) {
                            LoginDataModel dataModel = model.getData();
                            Logger.d("5/1 - dataModel - ", new Gson().toJson(dataModel));
                            if (dataModel != null) {
                                if (isRemove)
                                    lstModel.remove(positions);
                                Preferences.SetLoginDetails(dataModel);
                                mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }
                    }
                } else {
                    Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
                hideProgressDialog(activity);
                if (!Utils.isNetworkAvailable(activity, true, false)) {
                    Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                            activity.getResources().getString(R.string.error_network));
                } else {
                    Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                            activity.getResources().getString(R.string.technical_problem));
                }
                Logger.e("5/1 - onFailure", t.getMessage() + " - " + t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }

}
