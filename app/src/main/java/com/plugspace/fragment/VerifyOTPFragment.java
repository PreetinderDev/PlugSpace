package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.plugspace.BuildConfig;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.farhanfarooqui.pinview.PinView;


public class VerifyOTPFragment extends BaseFragment {
    Activity activity;
    Interfaces.OnGoBackClickListener goBackClickListener;
    Button btnVerifyCode;
    PinView pvLoginSignUpVerify;
    TextView llResend;
//    private String otpCode = "";

    public VerifyOTPFragment(Interfaces.OnGoBackClickListener activity) {
        goBackClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_verify_otp, container, false);
        activity = getActivity();
        initViewData(view);
        initClick();
        return view;
    }

    private void initClick() {
        btnVerifyCode.setOnClickListener(view -> isValid());

        llResend.setOnClickListener(view -> {
            pvLoginSignUpVerify.setPin("");
            SendOPT();
        });
    }

    private void initViewData(View view) {
        llResend = view.findViewById(R.id.llResend);
        btnVerifyCode = view.findViewById(R.id.btnVerifyCode);
        pvLoginSignUpVerify = view.findViewById(R.id.pvLoginSignUpVerify);
        /*final Typeface regular = Typeface.createFromAsset(activity.getAssets(),
                "font/font_regular_times.ttf");
        pvSignVerify.setTypeface(regular);*/
        view.findViewById(R.id.rlTopToolbar).setVisibility(View.GONE);

        SendOPT();

    }

    private void SendOPT() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, null).create(ApiService.class);

            Call<ObjectResponseModel> call = service.sendOTP(
                    Constants.loginDataModel.getPhone(),
                    Constants.loginDataModel.getCcode(),
                    "1"
            );

            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        ObjectResponseModel model = response.body();
                        if (model != null) {
                            if (model.getResponseCode() == 1) {
                                Utils.showToast(activity, model.getResponseMsg());

                                // TODO: 17/1/22 Test: below code remove start.
                                if (BuildConfig.DEBUG) {
                                    String otpCode = model.getOtpcode();
//                                    pvLoginSignUpVerify.setPin(otpCode);
                                    Logger.d("test_otp_code", otpCode);
                                }
                                // TODO: 17/1/22 Test: below code remove end.
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
                    t.printStackTrace();
                }
            });
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
        TextView view = activity.findViewById(R.id.tvNumber2);
        view.setText(R.string._2);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg2), view);
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

    private void isValid() {

        String otpCode = pvLoginSignUpVerify.getPin();

        if (otpCode == null || Utils.isValidationEmpty(otpCode)) {
            Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                    activity.getResources().getString(R.string.lbl_otp));

        } else {
            VerifyOTP();
        }

    }

    private void VerifyOTP() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, null).create(ApiService.class);

            Call<ObjectResponseModel> call = service.verifyOTP(
                    Constants.loginDataModel.getPhone(),
                    Constants.loginDataModel.getCcode(),
                    pvLoginSignUpVerify.getPin(),
                    Constants.deviceType,
                    Preferences.getStringName(Preferences.keyFirebaseToken));

            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        ObjectResponseModel model = response.body();
                        if (model != null) {

                            if (model.getIs_login().equals("1") && model.getData() != null) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            } else if (goBackClickListener != null) {
                                Constants.loginDataModel.setIsVerified(1);
                                goBackClickListener.gobackClick(2);

                            } else {
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
}
