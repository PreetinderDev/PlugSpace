package com.plugspace.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hbb20.CountryCodePicker;
import com.plugspace.BuildConfig;
import com.plugspace.R;
import com.plugspace.activities.LoginActivity;
import com.plugspace.common.AppPreferences;
import com.plugspace.common.Constants;
import com.plugspace.common.DialogNew;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;

public class VerifyMobileNumberFragment extends BaseFragment implements View.OnClickListener/*, AuthenticationListener*/ {
    Interfaces.OnGoBackClickListener goBackClickListener;
    Activity activity;
    Button btnContinue;
    TextView tvLoginSignUp, tvMsg;
    //    LinearLayout llLoginSignUp;
    EditText etMobileNumber;
    RelativeLayout rlInstagram;
    String strCountryCode = "";
    CountryCodePicker ccpCountryCode;
    private String token = null;
    private AppPreferences appPreferences = null;
    private DialogNew authenticationDialog = null;
    private View viewLogin;

    public VerifyMobileNumberFragment(Interfaces.OnGoBackClickListener activity, View view) {
        goBackClickListener = activity;
        this.viewLogin = view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        activity = getActivity();
        initViewData(view);
        initClick();

        return view;
    }

    private void initClick() {
        tvLoginSignUp.setOnClickListener(v -> viewLogin.performClick());
        btnContinue.setOnClickListener(this);
//        llLoginSignUp.setOnClickListener(this);
        rlInstagram.setOnClickListener(this);
    }

    private void initViewData(View view) {
        tvMsg = view.findViewById(R.id.tvMsg);
        rlInstagram = view.findViewById(R.id.rlInstagram);
        etMobileNumber = view.findViewById(R.id.etMobileNumber);
        btnContinue = view.findViewById(R.id.btnContinue);
        tvLoginSignUp = view.findViewById(R.id.tvLoginSignUp);
//        llLoginSignUp = view.findViewById(R.id.llLoginSignUp);
        ccpCountryCode = view.findViewById(R.id.ccpCountryCode);

        tvLoginSignUp.setText(getString(R.string.login2));
        tvMsg.setText(getString(R.string.already_have_an_account));
        view.findViewById(R.id.rlToToolBar).setVisibility(View.GONE);

        /*  appPreferences = new AppPreferences(this);

        //check already have access token
        token = appPreferences.getString(AppPreferences.TOKEN);
        if (token != null) {
            getUserInfoByAccessToken(token);
        }*/
    }


    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }
        TextView view = activity.findViewById(R.id.tvNumber1);
        view.setText(R.string._1);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg1), view);
//    Toast.makeText(activity, "isVisibleToUser", Toast.LENGTH_SHORT).show();
        //INSERT CUSTOM CODE HERE
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
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tvLoginSignUp:
//                viewLogin.performClick();
//                break;
//            case R.id.llLoginSignUp:
//                startActivity(new Intent(activity, LoginActivity.class));
//                activity.finish();
//                break;

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
        }
    }

    private void isValid() {
        ccpCountryCode.registerCarrierNumberEditText(etMobileNumber);
        if (Utils.isValidationEmpty(etMobileNumber.getText().toString().trim())) {
            Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                    activity.getResources().getString(R.string.lbl_phone_number));

        } else if (!ccpCountryCode.isValidFullNumber()) {
            Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                    activity.getResources().getString(R.string.lbl_valid_phone_number));
        } else if (Utils.isNetworkAvailable(activity, true, false)) {
            strCountryCode = ccpCountryCode.getSelectedCountryCodeWithPlus();
            String strNumber = etMobileNumber.getText().toString().trim();
            Constants.loginDataModel.setPhone(strNumber);
            Constants.loginDataModel.setCcode(strCountryCode);

            Logger.e("16/12", "phone - " + strNumber);
            Logger.e("16/12", "ccode - " + strCountryCode);
            if (goBackClickListener != null) {
                goBackClickListener.gobackClick(1);
            }
        }
    }

    public void login() {
        /*info.setVisibility(View.VISIBLE);
        ImageView pic = findViewById(R.id.pic);
        Picasso.with(this).load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(pic);
        TextView id = findViewById(R.id.id);
        id.setText(appPreferences.getString(AppPreferences.USER_ID));
        TextView name = findViewById(R.id.name);
        name.setText(appPreferences.getString(AppPreferences.USER_NAME));*/
    }

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
}
