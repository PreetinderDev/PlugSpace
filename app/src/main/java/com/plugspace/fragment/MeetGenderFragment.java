package com.plugspace.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.plugspace.R;
import com.plugspace.activities.BaseActivity;
import com.plugspace.activities.HomeActivity;
import com.plugspace.adapters.MeetGenderAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.SpacesItemDecoration;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetGenderFragment extends BaseFragment implements View.OnClickListener, MeetGenderAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    Button btnDone;
    private Dialog loaderDialog = null;
    private Dialog successDialog = null;
    RecyclerView rvMeetGender;
    ArrayList<HeightModel> meetGenderList = new ArrayList<>();
    ArrayList<MediaModel> mediaList = new ArrayList<>();
    MeetGenderAdapter meetGenderAdapter;
    int meetPosition = 0;

    public MeetGenderFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meet_gender, container, false);

        activity = getActivity();
        initFillData();
        initView(view);
        initClick();

        if (!Utils.isValidationEmpty(Constants.loginDataModel.getNiceMeet())) {
            for (int i = 0; i < meetGenderList.size(); i++) {
                meetGenderList.get(i).setBooleanSelected(false);
            }
            meetGenderList.get(meetPosition).setBooleanSelected(true);
            Constants.loginDataModel.setNiceMeet(Constants.loginDataModel.getNiceMeet());
            meetGenderAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcGender - >", Constants.loginDataModel.getNiceMeet());
        }
        return view;
    }

    private void initFillData() {
        meetGenderList.clear();
        meetGenderList.add(new HeightModel(getString(R.string.woman), R.drawable.ic_woman, false));
        meetGenderList.add(new HeightModel(getString(R.string.man), R.drawable.ic_man, false));
        meetGenderList.add(new HeightModel(getString(R.string.all), R.drawable.ic_all, false));
        meetGenderList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setNiceMeet(meetGenderList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "meet 2 - " + meetGenderList.get(Constants.SelectedPosition).getString());
    }

    private void initClick() {
        btnDone.setOnClickListener(this);
    }

    private void initView(View view) {
        btnDone = view.findViewById(R.id.btnDone);
        rvMeetGender = view.findViewById(R.id.rvMeetGender);
        LinearLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
        rvMeetGender.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvMeetGender.setLayoutManager(layoutManager);
        meetGenderAdapter = new MeetGenderAdapter(activity, meetGenderList, MeetGenderFragment.this);
        rvMeetGender.setAdapter(meetGenderAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnDone) {
           /* if (Utils.isNetworkAvailable(activity, true, false)) {
                RegisterAPI();
            }*/
        }
    }

  /*  private void RegisterAPI() {
        showLoaderDialog();

        Utils.setFirebaseTokenOnPreferences();

        ApiService service = ApiClient.UserApiClient(activity, null).create(ApiService.class);
        if (mediaList == null) {
            mediaList = new ArrayList<>();
        }
        mediaList.clear();
        mediaList.addAll(Constants.loginDataModel.getMediaDetail());
        Logger.d("test_imgFile_full", new Gson().toJson(mediaList));

        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[]{};
        if (mediaList != null && mediaList.size() > 0) {

            surveyImagesParts = new MultipartBody.Part[mediaList
                    .size()];


            for (int i = 0; i < mediaList.size(); i++) {
                if (mediaList.get(i).getImgFile() != null && mediaList.get(i).getImgFile().exists()) {

                    Logger.d("test_imgFile_path_" + i, mediaList.get(i).getImgFile().getPath());

                    RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"),
                            mediaList.get(i).getImgFile());
                    surveyImagesParts[i] = MultipartBody.Part.createFormData("profile[]",
                            mediaList.get(i).getImgFile().getName(),
                            surveyBody);
                }
            }
        }
        Logger.d("test_imgFile_length", surveyImagesParts.length);

        // : 4/2/22 Test: below code uncomment start.

        Call<ObjectResponseModel> call = service.signUp(
                Utils.convertValueToRequestBody(Constants.loginDataModel.getName()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getCcode()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getPhone()),
                Utils.convertValueToRequestBody(String.valueOf(Constants.loginDataModel.getIsVerified())),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getGender()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getRank()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getIsGeoLocation()),
                Utils.convertValueToRequestBody("0"),
                Utils.convertValueToRequestBody("0"),
                Utils.convertValueToRequestBody("0"),
                Utils.convertValueToRequestBody("0"),
                Utils.convertValueToRequestBody(Constants.isManualEmail_0),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getHeight()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getWeight()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getEducationStatus()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getDob()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getChildren()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getWantChildrens()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getMarringRace()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getRelationshipStatus()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getEthinicity()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getCompanyName()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getJobTitle()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getMakeOver()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getDressSize()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getSigniatBills()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getTimesOfEngaged()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getYourBodyTatto()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getAgeRangeMarriage()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getMySelfMen()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getAboutYou()),
                Utils.convertValueToRequestBody(Constants.loginDataModel.getNiceMeet()),
                Utils.convertValueToRequestBody(Constants.deviceType),
//                Utils.convertValueToRequestBody(Preferences.getStringName("androidId")),
                Utils.convertValueToRequestBody(Preferences.getStringName(Preferences.keyFirebaseToken)),
                surveyImagesParts,
                Utils.convertValueToRequestBody(Constants.loginDataModel.getLocation()));

        call.enqueue(new Callback<ObjectResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                   @NonNull Response<ObjectResponseModel> response) {
//                if (loaderDialog != null && loaderDialog.isShowing()) {
//                    loaderDialog.dismiss();
//                    loaderDialog = null;
//                }
                hideLoaderDialog();

                if (response.isSuccessful()) {
                    ObjectResponseModel model = response.body();
                    if (model != null) {
                        if (model.getResponseCode() == 1) {
                            LoginDataModel dataModel = model.getData();
                            Preferences.SetLoginDetails(dataModel);
                            if (dataModel != null) {
                                showSuccessDialog();
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
//                if (loaderDialog != null && loaderDialog.isShowing()) {
//                    loaderDialog.dismiss();
//                    loaderDialog = null;
//                }
                Logger.d("test_onFailure_signUp", new Gson().toJson(t));
                Logger.d("test_onFailure_message_signUp", t.getMessage());
                hideLoaderDialog();

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

        // : 4/2/22 Test: above code uncomment end.

//        // : 4/2/22 Test: below code remove start.
//        hideLoaderDialog();
//        Utils.showAlert(activity, "Ok");
//        // : 4/2/22 Test: below code remove end.
    }*/

    private void showSuccessDialog() {
        try {
            successDialog = new Dialog(activity, R.style.DialogTheme);
            successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(successDialog.getWindow()).setBackgroundDrawable(null);
            successDialog.setContentView(R.layout.dialog_show_success);
            successDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//            final Button btnOk = successDialog.findViewById(R.id.btnOk);

            successDialog.findViewById(R.id.btnOk).setOnClickListener(v -> {
                if (successDialog != null && successDialog.isShowing()) {
                    successDialog.dismiss();
                    successDialog = null;

                    Constants.loginDataModel = new LoginDataModel(); // clear sign up all data.

                    startActivity(new Intent(activity, HomeActivity.class));
                    ActivityCompat.finishAffinity(activity);
                }
            });

            successDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoaderDialog() {
        try {
            loaderDialog = new Dialog(activity, R.style.DialogTheme);
            loaderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(loaderDialog.getWindow()).setBackgroundDrawable(null);
            loaderDialog.setContentView(R.layout.dialog_show_loader);
            loaderDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

            TextView tvMsg = loaderDialog.findViewById(R.id.tvMsg);
            tvMsg.setText(activity.getResources().getString(R.string.msg_load_sign_up_api_call, activity.getResources().getString(R.string.app_name)));

            loaderDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideLoaderDialog() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
            loaderDialog = null;
        }
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
        TextView view = activity.findViewById(R.id.tvNumber6);
        view.setText(R.string._26);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg6), view);
    }

    public void ChangeNumber(ImageView imgGray, TextView tvTextNumber) {
        activity.findViewById(R.id.ivGrayBg1).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ivGrayBg2).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ivGrayBg3).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ivGrayBg4).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ivGrayBg5).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ivGrayBg6).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ivGrayBg7).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ivGrayBg8).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ivGrayBg9).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ivGrayBg10).setVisibility(View.VISIBLE);

        activity.findViewById(R.id.tvNumber1).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNumber2).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNumber3).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNumber4).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNumber5).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNumber6).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNumber7).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNumber8).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNumber9).setVisibility(View.GONE);
        activity.findViewById(R.id.tvNumber10).setVisibility(View.GONE);

        imgGray.setVisibility(View.GONE);
        tvTextNumber.setVisibility(View.VISIBLE);
    }

    @Override
    public void meetGenderClicked(int position) {
        meetPosition = position;
        for (int i = 0; i < meetGenderList.size(); i++) {
            meetGenderList.get(i).setBooleanSelected(false);
        }
        meetGenderList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setNiceMeet(meetGenderList.get(position).getString());
        Logger.e("16/12", "meet - " + meetGenderList.get(position).getString());
        meetGenderAdapter.notifyDataSetChanged();
    }
}
