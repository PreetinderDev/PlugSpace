package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.iamkdblue.videocompressor.VideoCompress;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.fragment.HomeFragment;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDescriptionActivity extends BaseActivity {
    Activity activity;
    EditText etAddDes;
    Button btnDone;
    public static ArrayList<MediaModel> mediaDetailList = new ArrayList<>();
    String strUserId = "", mimeType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);
        activity = AddDescriptionActivity.this;
        LoginDataModel dataModel = Preferences.GetLoginDetails();
        if (dataModel != null) {
            strUserId = dataModel.getUserId();
        }
        GetIntentData();
        initView();
        initClick();
    }

    private void GetIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.ARRAY_LIST)) {
            mediaDetailList = (ArrayList<MediaModel>) intent.getSerializableExtra(Constants.ARRAY_LIST);
        }
        if (intent.hasExtra(Constants.INTENT_KEY_MIME_TYPE)) {
            mimeType = intent.getStringExtra(Constants.INTENT_KEY_MIME_TYPE);
        }
    }

    private void initClick() {
        btnDone.setOnClickListener(view -> {
            isValid();
        });
    }

    private void initView() {
        etAddDes = findViewById(R.id.etAddDes);
        btnDone = findViewById(R.id.btnDone);

    }

    public void isValid() {
//        if (Utils.isValidationEmpty(etAddDes.getText().toString().trim())) {
//            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_write_msg)); //  check : API time
//        } else {
//            if (Utils.isNetworkAvailable(activity, true, false)) {
//                CreateFeed(etAddDes.getText().toString().trim());
//            }
//        }


        CreateFeed(etAddDes.getText().toString().trim());

    }

    private void CreateFeed(String strDescription) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

//        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[]{};
//        if (mediaDetailList != null && mediaDetailList.size() > 0) {
//
//            surveyImagesParts = new MultipartBody.Part[mediaDetailList
//                    .size()];
//            for (int i = 0; i < mediaDetailList.size(); i++) {
////                if (mediaDetailList.get(i) != null && mediaDetailList.get(i).getFeedImage() != null && !mediaDetailList.get(i).getFeedImage().startsWith("http")) {
//                if (mediaDetailList.get(i) != null && mediaDetailList.get(i).getFeedImage() != null) {
//
//                    File file = new File(mediaDetailList.get(i).getFeedImage());
//                    if (file.exists()) {
//                        if (Utils.isValidationEmpty(mimeType)) {
//                            mimeType = Constants.MIME_TYPE_IMAGE;
//                        }
//
//                        RequestBody surveyBody = RequestBody.create(file, MediaType.parse(mimeType));
//                        surveyImagesParts[i] = MultipartBody.Part.createFormData("feed_image",
//                                file.getName(),
//                                surveyBody);
//                    }
//                }
//            }
//
//        }

            File mFile = null;
            if (mediaDetailList != null && mediaDetailList.size() > 0) {
                mFile = new File(mediaDetailList.get(0).getMedia());
            }

            int random = new Random().nextInt(100000) + 20;
            String destPath;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                destPath = getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + random + ".mp4";
            } else {
                destPath = Environment.getExternalStorageDirectory().toString() + "/" + random + ".mp4";
            }

            MultipartBody.Part filePart = MultipartBody.Part.createFormData("feed_image",
                    mFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), mFile));


            Call<ObjectResponseModel> call = service.createFeed(
                    Utils.convertValueToRequestBody(strUserId),
                    Utils.convertValueToRequestBody(strDescription),
//                    storyFileList.get(0));
//                Utils.getMultipartBody(activity, mFile, "feed_image[]"));
                    filePart);

            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
                    Logger.d("test_onResponse_createFeed_call", "ok");
                    Logger.d("test_onResponse_createFeed_response", response.toString());
                    hideProgressDialog(activity);

                    if (response.isSuccessful()) {
                        ObjectResponseModel model = response.body();
                        if (model != null) {
                            if (model.getResponseCode() == 1) {
//                            startActivity(new Intent(activity, PreviewActivity.class));
//                            finish();
                                Utils.showToast(activity, model.getResponseMsg());
                                onBackPressed();

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
                    Logger.d("test_onFailure_createFeed_full", new Gson().toJson(t));
                    Logger.d("test_onFailure_createFeed_message", t.getMessage());

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
//            List<MultipartBody.Part> storyFileList = new ArrayList<>();
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
//            storyFileList.add(MultipartBody.Part.createFormData("feed_image",
//                    mFile.getName(), requestBody));

        }
    }

}
