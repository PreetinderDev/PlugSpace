package com.plugspace.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends BaseActivity implements View.OnClickListener {
    Activity activity;
    Button btnContinue;
    EditText etName, etEmail, etSubject, etTypeMsg;
    LoginDataModel loginDataModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        activity = ContactUsActivity.this;
        loginDataModel = Preferences.GetLoginDetails();
        initView();
        initToolBar();
        initClick();
    }

    private void initClick() {
        btnContinue.setOnClickListener(this);
    }

    private void initToolBar() {
        TextView tvTitleName = findViewById(R.id.tvTitleName);
        tvTitleName.setText(R.string.contact_us);
        findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
        findViewById(R.id.ivShowImg).setVisibility(View.GONE);
        findViewById(R.id.ivImg).setVisibility(View.GONE);
        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());

    }

    private void initView() {
        btnContinue = findViewById(R.id.btnContinue);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etSubject = findViewById(R.id.etSubject);
        etTypeMsg = findViewById(R.id.etTypeMsg);

        if (loginDataModel != null) {
            etName.setText(loginDataModel.getName());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnContinue) {
            isValid();
        }
    }

    public void isValid() {
        if (Utils.isValidationEmpty(etName.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.lbl_enter_name));

        } else if (Utils.isValidationEmpty(etEmail.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_email));

        } else if (!Utils.isValidEmail(etEmail.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_valid_email));

        } else if (Utils.isValidationEmpty(etSubject.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_subject));

        } else if (Utils.isValidationEmpty(etTypeMsg.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_msg));

        } else {
            if (Utils.isNetworkAvailable(activity, true, false)) {
                ContactUs();
            }
        }
    }

    private void ContactUs() {
        showProgressDialog(activity);
        ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

        Call<ObjectResponseModel> call = service.contactUs(etName.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etSubject.getText().toString().trim(),
                etTypeMsg.getText().toString().trim());
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
