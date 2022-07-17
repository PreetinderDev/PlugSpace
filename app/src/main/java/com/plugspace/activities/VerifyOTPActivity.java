package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plugspace.BuildConfig;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.fragment.AboutYouFragment;
import com.plugspace.fragment.AddPhotoFragment;
import com.plugspace.fragment.ChildrenFragment;
import com.plugspace.fragment.CompanyNameFragment;
import com.plugspace.fragment.DateOfBirthFragment;
import com.plugspace.fragment.DescribeYouEthnicityFragment;
import com.plugspace.fragment.DressSizeFragment;
import com.plugspace.fragment.EducationStatusFragment;
import com.plugspace.fragment.EngagedFragment;
import com.plugspace.fragment.EnterNameFragment;
import com.plugspace.fragment.HeightFragment;
import com.plugspace.fragment.JobTitleFragment;
import com.plugspace.fragment.MakeOverFragment;
import com.plugspace.fragment.MeetGenderFragment;
import com.plugspace.fragment.MrgRaceFragment;
import com.plugspace.fragment.MrgRangeAgeFragment;
import com.plugspace.fragment.MySelfConsideredFragment;
import com.plugspace.fragment.PayBillsFragment;
import com.plugspace.fragment.RankYourSelfFragment;
import com.plugspace.fragment.RelationshipStatusFragment;
import com.plugspace.fragment.SlcBioFragment;
import com.plugspace.fragment.TattooFragment;
import com.plugspace.fragment.VerifyOTPFragment;
import com.plugspace.fragment.WantChildrenFragment;
import com.plugspace.fragment.WeightFragment;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.farhanfarooqui.pinview.PinView;

public class VerifyOTPActivity extends BaseActivity  implements Interfaces.OnGoBackClickListener {
    Activity activity;
    PinView pvLoginSignUpVerify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        activity = VerifyOTPActivity.this;
        initView();
        initClick();
    }

    private void initClick() {
        findViewById(R.id.btnVerifyCode).setOnClickListener(view -> isValid());

        findViewById(R.id.llResend).setOnClickListener(view -> {
            pvLoginSignUpVerify.setPin("");
            SendOPT();
        });
    }

    private void initView() {
        pvLoginSignUpVerify = findViewById(R.id.pvLoginSignUpVerify);

        SendOPT();

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

    private void SendOPT() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, null).create(ApiService.class);

            Call<ObjectResponseModel> call = service.sendOTP(
                    Constants.loginScreenDataModel.getPhone(),
                    Constants.loginScreenDataModel.getCcode(),
                    "0"
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

                                //// TODO: 17/1/22 Test: below code remove start.
                                if (BuildConfig.DEBUG) {
                                    String otpCode = model.getOtpcode();
//                                    pvLoginSignUpVerify.setPin(otpCode);
                                    Logger.d("test_otp_code", otpCode);
                                }
                                //// TODO: 17/1/22 Test: below code remove end.


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

    private void VerifyOTP() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);

            Utils.setFirebaseTokenOnPreferences();

            ApiService service = ApiClient.UserApiClient(activity, null).create(ApiService.class);

//        String strOPT = pvLoginSignUpVerify.getPin();
            Call<ObjectResponseModel> call = service.verifyOTP(
                    Constants.loginScreenDataModel.getPhone(),
                    Constants.loginScreenDataModel.getCcode(),
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
                            if (model.getResponseCode() == 1) {
                                Constants.loginDataModel.setIsVerified(1);


                                LoginDataModel dataModel = model.getData();
                                if (model.getIs_login().equals("1") && dataModel != null) {
                                    Preferences.SetLoginDetails(dataModel);

                                    startActivity(new Intent(activity, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();

                                } else {
                                    Constants.loginDataModel.setIsVerified(1);
                                    Constants.loginDataModel.setPhone(Constants.loginScreenDataModel.getPhone());
                                    Constants.loginDataModel.setCcode(Constants.loginScreenDataModel.getCcode());
                                    startActivity(new Intent(activity,SignUpActivity.class).putExtra("from","login").putExtra("phn",Constants.loginScreenDataModel.getPhone())
                                            .putExtra("code",Constants.loginScreenDataModel.getCcode()));

//                                    Utils.showAlert(activity, getString(R.string.app_name),
//                                            model.getResponseMsg());
                                }

//                                Login();


                            /*if (Utils.isNetworkAvailable(activity, true, false)) {
                                Login();
                            }*/

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

//    private void Login() {
//        if (Utils.isNetworkAvailable(activity, true, false)) {
//            showProgressDialog(activity);
//            ApiService service = ApiClient.UserApiClient(activity, null).create(ApiService.class);
//
//            Call<ObjectResponseModel> call = service.login(
//                    Constants.loginScreenDataModel.getPhone(),
//                    Constants.loginScreenDataModel.getCcode());
//
//            call.enqueue(new Callback<ObjectResponseModel>() {
//                @Override
//                public void onResponse(@NonNull Call<ObjectResponseModel> call,
//                                       @NonNull ArrayResponseModel<ObjectResponseModel> response) {
//                    hideProgressDialog(activity);
//                    if (response.isSuccessful()) {
//                        ObjectResponseModel model = response.body();
//                        if (model != null) {
//                            if (model.getResponseCode() == 1) {
//                                LoginDataModel dataModel = model.getData();
//                                Preferences.SetLoginDetails(dataModel);
//                                if (dataModel != null) {
//                                    startActivity(new Intent(activity, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                                    finish();
//                                }
//
//                            } else {
//                                if (!model.getResponseMsg().isEmpty()) {
//                                    Utils.showAlert(activity, getString(R.string.app_name),
//                                            model.getResponseMsg());
//                                }
//                            }
//                        }
//                    } else {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
//                    hideProgressDialog(activity);
//                    if (!Utils.isNetworkAvailable(activity, true, false)) {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
//                                activity.getResources().getString(R.string.error_network));
//                    } else {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
//                                activity.getResources().getString(R.string.technical_problem));
//                    }
//                    t.printStackTrace();
//                }
//            });
//        }
//    }

    private void LoadFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frmnt_Container, fragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void gobackClick(int position) {
//        BaseActivity.showProgressDialog(activity);
//
//        new Handler(Looper.getMainLooper()).postDelayed(() -> activity.runOnUiThread(() -> {
//            BaseActivity.hideProgressDialog(activity);
//        }), 1000);

        switch (position) {
            case 1:
//                ChangeNumber(ivGrayBg2, tvNumber2);
//                tvNumber2.setText(R.string._2);
                LoadFragment(new VerifyOTPFragment(VerifyOTPActivity.this));
                break;

            case 2:
//                ChangeNumber(ivGrayBg3, tvNumber3);
//                tvNumber3.setText(R.string._3);
                LoadFragment(new SlcBioFragment(VerifyOTPActivity.this));
                break;

            case 3:
//                ChangeNumber(ivGrayBg4, tvNumber4);
//                tvNumber4.setText(R.string._4);
                LoadFragment(new EnterNameFragment(VerifyOTPActivity.this));
                break;

            case 4:
//                ChangeNumber(ivGrayBg5, tvNumber5);
//                tvNumber5.setText(R.string._5);
                LoadFragment(new RankYourSelfFragment(VerifyOTPActivity.this));
                break;

            case 5:
//                ChangeNumber(ivGrayBg6, tvNumber6);
//                tvNumber6.setText(R.string._6);
                LoadFragment(new AddPhotoFragment(VerifyOTPActivity.this));

                break;

            case 6:
//                ChangeNumber(ivGrayBg7, tvNumber7);
//                tvNumber7.setText(R.string._7);
                LoadFragment(new HeightFragment(VerifyOTPActivity.this));
                break;

            case 7:
//                ChangeNumber(ivGrayBg8, tvNumber8);
//                tvNumber8.setText(R.string._8);
                LoadFragment(new WeightFragment(VerifyOTPActivity.this));
                break;

            case 8:
//                ChangeNumber(ivGrayBg9, tvNumber9);
//                tvNumber9.setText(R.string._9);
                LoadFragment(new EducationStatusFragment(VerifyOTPActivity.this));
                break;

            case 9:
//                ChangeNumber(ivGrayBg10, tvNumber10);
//                tvNumber10.setText(R.string._10);
                LoadFragment(new DateOfBirthFragment(VerifyOTPActivity.this));
                break;

            case 10:
//                ChangeNumber(ivGrayBg1, tvNumber1);
//                tvNumber1.setText(R.string._11);
                LoadFragment(new ChildrenFragment(VerifyOTPActivity.this));
                break;

            case 11:
//                ChangeNumber(ivGrayBg2, tvNumber2);
//                tvNumber2.setText(R.string._12);
                LoadFragment(new WantChildrenFragment(VerifyOTPActivity.this));
                break;

            case 12:
//                ChangeNumber(ivGrayBg3, tvNumber3);
//                tvNumber3.setText(R.string._13);
                LoadFragment(new MrgRaceFragment(VerifyOTPActivity.this));
                break;

            case 13:
//                ChangeNumber(ivGrayBg4, tvNumber4);
//                tvNumber4.setText(R.string._14);
                LoadFragment(new RelationshipStatusFragment(VerifyOTPActivity.this));
                break;

            case 14:
//                ChangeNumber(ivGrayBg5, tvNumber5);
//                tvNumber5.setText(R.string._15);
                LoadFragment(new DescribeYouEthnicityFragment(VerifyOTPActivity.this));
                break;

            case 15:
//                ChangeNumber(ivGrayBg6, tvNumber6);
//                tvNumber6.setText(R.string._16);
                LoadFragment(new CompanyNameFragment(VerifyOTPActivity.this));
                break;

            case 16:
//                ChangeNumber(ivGrayBg7, tvNumber7);
//                tvNumber7.setText(R.string._17);
                LoadFragment(new JobTitleFragment(VerifyOTPActivity.this));
                break;

            case 17:
//                ChangeNumber(ivGrayBg8, tvNumber8);
//                tvNumber8.setText(R.string._18);
                LoadFragment(new MakeOverFragment(VerifyOTPActivity.this));
                break;

            case 18:
//                ChangeNumber(ivGrayBg9, tvNumber9);
//                tvNumber9.setText(R.string._19);
                LoadFragment(new DressSizeFragment(VerifyOTPActivity.this));
                break;

            case 19:
//                ChangeNumber(ivGrayBg10, tvNumber10);
//                tvNumber10.setText(R.string._20);
                LoadFragment(new PayBillsFragment(VerifyOTPActivity.this));
                break;

            case 20:
//                ChangeNumber(ivGrayBg1, tvNumber1);
//                tvNumber1.setText(R.string._21);
                LoadFragment(new EngagedFragment(VerifyOTPActivity.this));
                break;

            case 21:
//                ChangeNumber(ivGrayBg2, tvNumber2);
//                tvNumber2.setText(R.string._22);
                LoadFragment(new TattooFragment(VerifyOTPActivity.this));
                break;

            case 22:
//                ChangeNumber(ivGrayBg3, tvNumber3);
//                tvNumber3.setText(R.string._23);
                LoadFragment(new MrgRangeAgeFragment(VerifyOTPActivity.this));
                break;

            case 23:
//                ChangeNumber(ivGrayBg4, tvNumber4);
//                tvNumber4.setText(R.string._24);
                LoadFragment(new MySelfConsideredFragment(VerifyOTPActivity.this));
                break;

            case 24:
//                ChangeNumber(ivGrayBg5, tvNumber5);
//                tvNumber5.setText(R.string._25);
                LoadFragment(new AboutYouFragment(VerifyOTPActivity.this));
                break;

            case 25:
//                ChangeNumber(ivGrayBg6, tvNumber6);
//                tvNumber6.setText(R.string._26);
                LoadFragment(new MeetGenderFragment(VerifyOTPActivity.this));
                break;
        }
    }
    
}
