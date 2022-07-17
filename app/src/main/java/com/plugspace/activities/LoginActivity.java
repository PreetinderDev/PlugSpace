package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.plugspace.BuildConfig;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends BaseActivity implements View.OnClickListener/*, AuthenticationListener*/ {
    Activity activity;
    Button btnContinue;
    EditText etMobileNumber;
    //    TextView tvTitle/*, tvLoginSignUp*/;
    private TextView tvLoginSignUp;
    CountryCodePicker ccpCountryCode;
    String strCountryCode = "", isFrom = "";
    RelativeLayout rlInstagram;
//    private String token = null;
//    private AppPreferences appPreferences = null;
//    private DialogNew authenticationDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;
        getPreviousData();
        initView();
        intClick();

        if (isFrom.equals(Constants.IS_FROM_SIGN_UP)) {
            tvLoginSignUp.performClick();
        }
    }


    private void getPreviousData() {
        Bundle mBundle = getIntent().getExtras();

        if (mBundle != null) {
            if (mBundle.containsKey(Constants.INTENT_KEY_IS_FROM)) {
                isFrom = mBundle.getString(Constants.INTENT_KEY_IS_FROM);
            }
        }
    }

    private void intClick() {
        btnContinue.setOnClickListener(this);
//        tvLoginSignUp.setOnClickListener(this);
//        llLoginSignUp.setOnClickListener(this);
        rlInstagram.setOnClickListener(this);
    }

    private void initView() {
        rlInstagram = findViewById(R.id.rlInstagram);
//        llLoginSignUp = findViewById(R.id.llLoginSignUp);
        tvLoginSignUp = findViewById(R.id.tvLoginSignUp);
//        tvTitle = findViewById(R.id.tvTitle);
        btnContinue = findViewById(R.id.btnContinue);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        ccpCountryCode = findViewById(R.id.ccpCountryCode);

      /*  appPreferences = new AppPreferences(this);

        //check already have access token
        token = appPreferences.getString(AppPreferences.TOKEN);
        if (token != null) {
            getUserInfoByAccessToken(token);
        }*/

        tvLoginSignUp.setOnClickListener(v -> startActivity(new Intent(activity, SignUpActivity.class)));
    }

//    public void login() {
//        /*info.setVisibility(View.VISIBLE);
//        ImageView pic = findViewById(R.id.pic);
//        Picasso.with(this).load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(pic);
//        TextView id = findViewById(R.id.id);
//        id.setText(appPreferences.getString(AppPreferences.USER_ID));
//        TextView name = findViewById(R.id.name);
//        name.setText(appPreferences.getString(AppPreferences.USER_NAME));*/
//    }

    /* public void logout() {
         token = null;
         appPreferences.clear();
     }

     @Override
     public void onTokenReceived(String auth_token) {
         if (auth_token == null)
             return;
         appPreferences.putString(AppPreferences.TOKEN, auth_token);
         token = auth_token;
         Log.e("30/12 - token", token + "");
         getUserInfoByAccessToken(token);
     }
 */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinue:
                isValid();
                break;
            case R.id.rlInstagram:
                /*if(token!=null)
                {
                    logout();
                }
                else {
//            authenticationDialog = new AuthenticationDialog(this, this);
                    authenticationDialog = new DialogNew(this, this);
                    authenticationDialog.setCancelable(true);
                    authenticationDialog.show();
                }*/
                break;

//            case R.id.tvLoginSignUp:
//            case R.id.llLoginSignUp:
//
//                break;
        }
    }

    /*private void getUserInfoByAccessToken(String token) {
        new RequestInstagramAPI().execute();
    }

    private class RequestInstagramAPI extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_url) + token);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if (jsonData.has("id")) {
                        //сохранение данных пользователя
                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));

                        // сохранить еще данные
                        login();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"Ошибка входа!",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }*/

    private void isValid() {
        String mobileNo = etMobileNumber.getText().toString().trim();
        ccpCountryCode.registerCarrierNumberEditText(etMobileNumber);
        if (Utils.isValidationEmpty(mobileNo)) {
            Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                    activity.getResources().getString(R.string.lbl_phone_number));

        }
        /*else if (!Utils.isValidPhoneNumber(etMobileNumber.getText().toString().trim())) {
            Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                    activity.getResources().getString(R.string.lbl_valid_phone_number));

        }*/
        else if(!ccpCountryCode.isValidFullNumber()){
            Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                    activity.getResources().getString(R.string.lbl_valid_phone_number));
        } else if (Utils.isNetworkAvailable(activity, true, false)) {
            strCountryCode = ccpCountryCode.getSelectedCountryCodeWithPlus();

            Constants.loginScreenDataModel.setPhone(mobileNo);
            Constants.loginScreenDataModel.setCcode(strCountryCode);

            startActivity(new Intent(activity, VerifyOTPActivity.class));
            // TODO: 15/2/22 Confirm: above below.
//            sendVerificationCodeToPhone(mobileNo, strCountryCode);
        }

    }

//    private void sendVerificationCodeToPhone(String mobileNo, String countryCode) {
////        showProgressDialog(activity);
//
//        String phoneNo = countryCode + mobileNo;
//
//
//        // [START start_phone_auth]
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder()
//                        .setPhoneNumber(phoneNo)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(
//                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                                    @Override
//                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                        Logger.d("test_getSmsCode", phoneAuthCredential.getSmsCode());
//                                        hideProgressDialog(activity);
//                                        Utils.showAlert(activity, phoneAuthCredential.getSmsCode());
//                                    }
//
//                                    @Override
//                                    public void onVerificationFailed(@NonNull FirebaseException e) {
//                                        Logger.d("test_getSmsCode", e.getMessage());
//                                        hideProgressDialog(activity);
//                                        Utils.showAlert(activity, e.getMessage());
//                                    }
//                                }
//                        )          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//        // [END start_phone_auth]
//    }
}
