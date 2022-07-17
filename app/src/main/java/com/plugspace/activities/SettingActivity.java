package com.plugspace.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.SettingAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends BaseActivity implements SettingAdapter.MyListener {
    Activity activity;
    TextView tvTitleName;
    RecyclerView rvSettings;
    SettingAdapter settingAdapter;
    ArrayList<HeightModel> settingsList = new ArrayList<>();
    String strUserId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        activity = SettingActivity.this;
        initFillData();
        initView();
        initToolBar();
    }

    private void initFillData() {
        settingsList.add(new HeightModel(getString(R.string.subscriptions)));
        settingsList.add(new HeightModel(getString(R.string.favourite)));
        settingsList.add(new HeightModel(getString(R.string.help)));
        settingsList.add(new HeightModel(getString(R.string.faq_s)));
        settingsList.add(new HeightModel(getString(R.string.contact_us)));
        settingsList.add(new HeightModel(getString(R.string.logout)));
    }

    private void initToolBar() {
        tvTitleName = findViewById(R.id.tvTitleName);
        tvTitleName.setText(R.string.settings);
        findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
        findViewById(R.id.ivShowImg).setVisibility(View.GONE);
        findViewById(R.id.ivImg).setVisibility(View.GONE);
        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());
    }

    private void initView() {
        rvSettings = findViewById(R.id.rvSettings);
        rvSettings.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        settingAdapter = new SettingAdapter(activity, settingsList, SettingActivity.this);
        rvSettings.setAdapter(settingAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void mySlcSettingOptionClicked(String identify, int position) {

        if (identify.equalsIgnoreCase(getString(R.string.subscriptions))) {
            startActivity(new Intent(activity, SubscriptionsActivity.class));

        } else if (identify.equalsIgnoreCase(getString(R.string.favourite))) {
//            startActivity(new Intent(activity, MusicFavCategoryActivity.class)
//                    .putExtra(Constants.categoryListFrom, Constants.EVEN_NUM));

            LoginDataModel loginDataModel=Preferences.GetLoginDetails();

            startActivity(new Intent(activity, MusicFavCategoryActivity.class)
                    .putExtra(Constants.INTENT_KEY_MODEL, loginDataModel)
                    .putExtra(Constants.categoryListFrom, "1"));

        } else if (identify.equalsIgnoreCase(getString(R.string.help))) {
            startActivity(new Intent(activity, HelpActivity.class));

        } else if (identify.equalsIgnoreCase(getString(R.string.faq_s))) {
            startActivity(new Intent(activity, FAQsActivity.class));

        } else if (identify.equalsIgnoreCase(getString(R.string.contact_us))) {
            startActivity(new Intent(activity, ContactUsActivity.class));

        } else if (identify.equalsIgnoreCase(getString(R.string.logout))) {
            Utils.showAlertLogoutConfirmDialog(activity, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LogOut();
                }
            });

        }

    }

    private void LogOut() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);

            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);
            LoginDataModel dataModel = Preferences.GetLoginDetails();
            if (dataModel != null) {
                strUserId = dataModel.getUserId();
            }

            Call<ObjectResponseModel> call = service.logOut(strUserId);

            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        ObjectResponseModel model = response.body();
                        if (model != null) {
                            if (model.getResponseCode() == 1) {
                                Preferences.SetLoginDetails(null);
                                startActivity(new Intent(activity, LoginActivity.class));
                                ActivityCompat.finishAffinity(activity);

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
}
