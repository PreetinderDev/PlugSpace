package com.plugspace.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.plugspace.R;
import com.plugspace.adapters.StatusAdapter;
import com.plugspace.adapters.VerticalViewPager;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.HomeDetailsResponseModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusCopyActivity  extends BaseActivity {
    private Activity activity;
    private ArrayList<LoginDataModel> lstStatus = new ArrayList<>();
    private ArrayList<MediaModel> lstStory = new ArrayList<>();
    private LoginDataModel modelStory = null;
    private MediaModel modelStoryMediaDetail = null;
    //    private ArrayList<LoginDataModel> lstView = new ArrayList<>();
    private StatusAdapter statusAdapter;
    private VerticalViewPager statusViewPager;
    private RoundedImageView rivProfileImage;
    private TextView tvName, tvTime, tvViewCount;
    private ImageView ivBack, ivDelete, ivReport;
    private LinearLayout llPreview, llComment;
    //    private LoginDataModel storyDataModel;
    //private    LoginDataModel dataModelLogin;
    private String /*userIdLogin,*/ /*strName = "",*/ /*strProfileImg = "",*/ /*strTime = "",*/ strUserId = ""/*, strStoryUserId = ""*//*, strFrom = ""*/;
    private EditText etComment;
    private boolean isUpdate = false;
    private int selectedPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_copy);

        activity = this;
        getPreviousData();
        initView();
        initClick();
        processNextUserStoryView();
//        GetMyViewStory(false); // : 15/2/22 Pending: API call or not confirm.
    }

    private void dialogConfirmDeleteStory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.msg_confirm_delete_story));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            doAPIDeleteStory();
        });
        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void dialogReport() {

        Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_report);
        dialog.setTitle("");

        EditText etReason = dialog.findViewById(R.id.etReason);

        dialog.findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            String reason = etReason.getText().toString().trim();
            if (Utils.isValidationEmpty(reason)) {
                Utils.showAlert(activity, activity.getResources().getString(R.string.valid_empty_reason));
            } else {
                Utils.hideKeyBoard(activity,etReason);
//                dialog.dismiss(); // TODO: Confirm: loader show to comment this line
                doAPIProfileReportByUser(reason, dialog);
            }

        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(view -> dialog.dismiss());


        dialog.show();

    }

    private void initClick() {
        ivBack.setOnClickListener(view -> onBackPressed());

        ivDelete.setOnClickListener(v -> {
            dialogConfirmDeleteStory();
        });

        ivReport.setOnClickListener(v -> {
            dialogReport();
        });

        llPreview.setOnClickListener(view -> GetMyViewStory(true));

//        if (lstStory != null && lstStory.size() > 0) {
//            strTime = lstStory.get(0).getDateTime();
//            tvTime.setText(strTime);
//        }

        statusViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Logger.d("test_statusViewPager_onPageSelected", position);

                if (lstStory != null && lstStory.size() > 0 && lstStory.size() - 1 != position) {
                    modelStoryMediaDetail = lstStory.get(position);
                    tvTime.setText(modelStoryMediaDetail.getDateTime());
                    statusAdapter.notifyDataSetChanged();

                } else {
//                    // TODO: 15/2/22 Confirm:
                    selectedPosition++;
                    processNextUserStoryView();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        etComment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {


                String comment = etComment.getText().toString().trim();
                if (Utils.isValidationEmpty(comment)) {
                    Utils.showAlert(activity, activity.getResources().getString(R.string.valid_empty_comment));
                } else {
                    doStoryComment();
                }
            }
            return false;
        });


    }

    private void doStoryComment() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Utils.hideKeyBoard(activity, etComment);

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiStoryComment(Preferences.getStringName(Preferences.keyUserId),
                    strUserId,
                    etComment.getText().toString().trim(),
                    Constants.deviceType,
                    Constants.token);
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful() && response.body() != null) {
                        ObjectResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
                            etComment.setText("");
                            Utils.showToast(activity, model.getResponseMsg());

                        } else {
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
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
                    t.printStackTrace();
                }
            });
        }
    }

    private void GetMyViewStory(boolean isOpenViews) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);


            Call<HomeDetailsResponseModel> call = service.getMyViewStory(Preferences.getStringName(Preferences.keyUserId));
            call.enqueue(new Callback<HomeDetailsResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<HomeDetailsResponseModel> call,
                                       @NonNull Response<HomeDetailsResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        HomeDetailsResponseModel model = response.body();
                        if (model != null) {
                            if (model.getResponseCode() == Constants.RESPONSE_CODE_1) {
                                isUpdate = true;
//                                lstView = model.getData();
                                int viewCount = model.getCount();
                                tvViewCount.setText(String.valueOf(viewCount));

//                                if (isOpenViews && lstView != null && lstView.size() > 0) {
                                if (isOpenViews /*&& lstView != null && lstView.size() > 0*/) {
                                    startActivity(new Intent(activity, ViewsActivity.class)
                                            .putExtra(Constants.VIEW_COUNT, String.valueOf(viewCount))
                                            .putExtra(Constants.ARRAY_LIST, model.getData()));
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
                public void onFailure(@NonNull Call<HomeDetailsResponseModel> call, @NonNull Throwable t) {
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

    private void getPreviousData() {

//        Intent intent = getIntent();
//        if (intent.hasExtra(Constants.ARRAY_LIST)
//                && intent.hasExtra(Constants.USER_ID)
//                && intent.hasExtra(Constants.DATA_MODEL)) {
//
//            lstStory = (ArrayList<MediaModel>) intent.getSerializableExtra(Constants.ARRAY_LIST);
//            strUserId = intent.getStringExtra(Constants.USER_ID);
//            storyDataModel = (LoginDataModel) intent.getSerializableExtra(Constants.DATA_MODEL);
//
//            if (storyDataModel != null) {
//                strUserId = storyDataModel.getUserId();
//                strName = storyDataModel.getName();
//                strProfileImg = storyDataModel.getProfile();
//            }
//        }

//        Intent intent = getIntent();
//        if (intent.hasExtra(Constants.ARRAY_LIST)) {
//            lstStatus = (ArrayList<LoginDataModel>) intent.getSerializableExtra(Constants.INTENT_KEY_LIST);
//        }
//        Logger.d("test_lstStatus_size", lstStatus.size());

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(Constants.INTENT_KEY_SELECTED_POSITION)) {
                selectedPosition = mBundle.getInt(Constants.INTENT_KEY_SELECTED_POSITION);
            }
            if (mBundle.containsKey(Constants.INTENT_KEY_LIST)) {
                lstStatus = (ArrayList<LoginDataModel>) mBundle.getSerializable(Constants.INTENT_KEY_LIST);
            }

        }
//        Logger.d("test_lstStatus_size", lstStatus.size());

    }

    private void processNextUserStoryView() {
//        if (lstStatus != null && lstStatus.size() > 0 && lstStatus.size() > selectedPosition ) { // This condition check Login user and other all users next process
        if (lstStatus != null && lstStatus.size() > 0 && lstStatus.size() > selectedPosition && !Preferences.getStringName(Preferences.keyUserId).equals(strUserId)) {  // This condition check without Login users next process

            modelStory = lstStatus.get(selectedPosition);

            if (modelStory != null) {

                strUserId = modelStory.getUserId();
                Utils.setImageProfile(activity, modelStory.getProfile(), rivProfileImage);

                String strName = modelStory.getName();
                if (Preferences.getStringName(Preferences.keyUserId).equals(strUserId)) {
                    llPreview.setVisibility(View.VISIBLE);
                    ivDelete.setVisibility(View.VISIBLE);
                    ivReport.setVisibility(View.GONE);
                    tvName.setText(getString(R.string.your_story));
                    llComment.setVisibility(View.GONE);
                } else {
                    llPreview.setVisibility(View.GONE);
                    ivDelete.setVisibility(View.GONE);
                    ivReport.setVisibility(View.VISIBLE);
                    tvName.setText(strName);
                    llComment.setVisibility(View.VISIBLE);
                }

                doAPIGetStoryDetails(modelStory);
            } else {
//                // TODO: 15/2/22 Confirm:
                selectedPosition++;
                processNextUserStoryView();
            }

        } else {
            hideProgressDialog(activity);

            onBackPressed();
        }
    }

    private void initView() {
        rivProfileImage = findViewById(R.id.rivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvTime = findViewById(R.id.tvTime);
        ivBack = findViewById(R.id.ivBack);
        ivDelete = findViewById(R.id.ivDelete);
        ivReport = findViewById(R.id.ivReport);
        llPreview = findViewById(R.id.llPreview);
        llComment = findViewById(R.id.llComment);
        tvViewCount = findViewById(R.id.tvViewCount);
        etComment = findViewById(R.id.etComment);

        statusViewPager = findViewById(R.id.statusViewPager);
//        if (lstStory != null && lstStory.size() > 0) {
//            statusAdapter = new StatusAdapter(activity, lstStory);
//            statusViewPager.setAdapter(statusAdapter);
//        }
//
////        Glide.with(activity)
////                .asBitmap()
////                .placeholder(R.drawable.ic_profile_placeholder)
////                .load(strProfileImg)
////                .into(rivProfileImage);
//
//        Utils.setImageProfile(activity, strProfileImg, rivProfileImage);
//
//        if (Preferences.getStringName(Preferences.keyUserId).equalsIgnoreCase(storyDataModel.getUserId())) {
//            llPreview.setVisibility(View.VISIBLE);
//            tvName.setText(getString(R.string.your_story));
//        } else {
//            llPreview.setVisibility(View.GONE);
//            tvName.setText(strName);
//        }

    }


//    @Override
//    public void onBackPressed() {
//        finish();
//    }


    @Override
    public void onBackPressed() {
        if (isUpdate) {
            Intent intent = new Intent();
            intent.putExtra(Constants.INTENT_KEY_IS_UPDATE, isUpdate);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            super.onBackPressed();
        }
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void doAPIGetStoryDetails(LoginDataModel loginDataModel) {
        if (Utils.isNetworkAvailable(activity, true, false)) {

            showProgressDialog(activity);

            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<HomeDetailsResponseModel> call = service.apiCallGetStoryDetails(Preferences.getStringName(Preferences.keyUserId), loginDataModel.getUserId());
            call.enqueue(new Callback<HomeDetailsResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<HomeDetailsResponseModel> call,
                                       @NonNull Response<HomeDetailsResponseModel> response) {
//                    hideProgressDialog(activity);
                    Logger.d("test_onResponse_getStoryDetails", new Gson().toJson(response.body()));

                    if (response.isSuccessful() && response.body() != null) {
                        HomeDetailsResponseModel model = response.body();

                        if (model.getResponseCode() == Constants.RESPONSE_CODE_1) {
                            isUpdate = true;

                            if (lstStory == null) {
                                lstStory = new ArrayList<>();
                            }
                            lstStory.clear();


                            ArrayList<LoginDataModel> lstData = model.getData();
                            if (lstData != null && lstData.size() > 0) {
                                LoginDataModel dataModel = lstData.get(0);
                                if (dataModel != null) {
                                    lstStory.addAll(dataModel.getStoryMediaDetail());

                                    int viewCount = dataModel.getCount();
                                    tvViewCount.setText(String.valueOf(viewCount));
                                }
                            }

                            if (lstStory != null && lstStory.size() > 0) {

                                // TODO: 15/2/22 Confirm: below condition.
                                lstStory.add(lstStory.get(lstStory.size() - 1));


                                statusAdapter = new StatusAdapter(activity, lstStory);
                                statusViewPager.setAdapter(statusAdapter);

                                modelStoryMediaDetail = lstStory.get(0);
                                tvTime.setText(modelStoryMediaDetail.getDateTime());
                                statusViewPager.setCurrentItem(0);

                                statusAdapter.setOnNextStoryClickListener(position -> {
                                    if (lstStory != null && lstStory.size() > position) {
                                        int nextItem = position + 1;
                                        statusViewPager.setCurrentItem(nextItem);
                                    } else {
//                onBackPressed();

//                                        // TODO: 15/2/22 Confirm:
                                        selectedPosition++;
                                        processNextUserStoryView();
                                    }
                                });

                                hideProgressDialog(activity);
                            } else {
//                                // TODO: 15/2/22 Confirm:
                                selectedPosition++;
                                processNextUserStoryView();
                            }

                        } else {
//                            if (!model.getResponseMsg().isEmpty()) {
//                                Utils.showAlert(activity, getString(R.string.app_name),
//                                        model.getResponseMsg());
//                            }

//                            // TODO: 15/2/22 Confirm:
                            selectedPosition++;
                            processNextUserStoryView();
                        }
                    } else {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
//                        // TODO: 15/2/22 Confirm:
                        selectedPosition++;
                        processNextUserStoryView();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<HomeDetailsResponseModel> call, @NonNull Throwable t) {
                    hideProgressDialog(activity);

//                    if (!Utils.isNetworkAvailable(activity, true, false)) {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
//                                activity.getResources().getString(R.string.error_network));
//                    } else {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
//                                activity.getResources().getString(R.string.technical_problem));
//                    }
//                    t.printStackTrace();

//                    // TODO: 15/2/22 Confirm:
                    selectedPosition++;
                    processNextUserStoryView();
                }
            });
        }
    }

    private void doAPIDeleteStory() {
        if (Utils.isNetworkAvailable(activity, true, false)) {

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiDeleteStory(
                    Preferences.getStringName(Preferences.keyUserId),
                    modelStory.getStoryId(),
                    modelStoryMediaDetail.getStoryMediaId(),
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
                            hideProgressDialog(activity);
                            Utils.showToast(activity, model.getResponseMsg());

                            // TODO: 15/2/22 Confirm:
                            selectedPosition++;
                            processNextUserStoryView();

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

    private void doAPIProfileReportByUser(String reason, Dialog dialog) {
        if (Utils.isNetworkAvailable(activity, true, false)) {

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiProfileReportByUser(
                    Preferences.getStringName(Preferences.keyUserId),
                    modelStory.getUserId(),
                    modelStoryMediaDetail.getStoryMediaId(),
                    reason,
                    Constants.REPORT_TYPE_STORY,
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
                            hideProgressDialog(activity);
                            Utils.showToast(activity, model.getResponseMsg());

                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

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
}
