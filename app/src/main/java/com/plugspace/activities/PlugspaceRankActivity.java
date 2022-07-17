package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.CharacteristicsAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.CharacteristicsModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlugspaceRankActivity extends BaseActivity {
    private Activity activity;
    private TextView tvPlugSpaceRank, tvMySelfScore/*, tvCharaMsg*/;
    //    String strRank = "";
    private LoginDataModel dataModel;
    private String strFriendUserId = "", strUserId = ""/*, *//*strPlugSPaceRank = "0",*//* strCharaMsg = ""*/;
    private CharacteristicsAdapter mAdapter;
    private List<CharacteristicsModel> lstModel = new ArrayList<>();
    private RecyclerView rvModel;
    private LinearLayout llCharacteristics;
    private String isPrivate = "";
    private CardView cvWoman,cvGay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugspace_rank);

        activity = this;
        GetIntentData();
        initToolBar();
        initView();
        setDefaultData();
        setDataToList();
        doAPIGetFriendsScore();

    }

    private void setDataToList() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new CharacteristicsAdapter(activity, lstModel, true);
            rvModel.setAdapter(mAdapter);
        }

        if (lstModel.size() > 0) {
            llCharacteristics.setVisibility(View.VISIBLE);
        } else {
            llCharacteristics.setVisibility(View.GONE);
        }
    }

    private void setDefaultData() {
        isPrivate = Constants.IS_PRIVATE_1;
        setPlugSpaceRank("");
        setMySelfScore("");
    }

    private void GetIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.DATA_MODEL)) {
            dataModel = (LoginDataModel) intent.getSerializableExtra(Constants.DATA_MODEL);
            if (dataModel != null) {
                strFriendUserId = dataModel.getUserId();
            }
        }
    }

    private void initToolBar() {
        TextView tvTitleName = findViewById(R.id.tvTitleName);
        tvTitleName.setText(R.string.plugspace_rank);
        findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
        findViewById(R.id.ivShowImg).setVisibility(View.GONE);
        findViewById(R.id.ivImg).setVisibility(View.GONE);
        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());
    }

    private void initView() {
        tvPlugSpaceRank = findViewById(R.id.tvPlugSpaceRank);
//        tvCharaMsg = findViewById(R.id.tvCharaMsg);
        tvMySelfScore = findViewById(R.id.tvMySelfScore);
        cvWoman = findViewById(R.id.cvWoman);
        cvGay = findViewById(R.id.cvGay);
        rvModel = findViewById(R.id.rvModel);
        llCharacteristics = findViewById(R.id.llCharacteristics);

        rvModel.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void doAPIGetFriendsScore() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);
            LoginDataModel dataModel = Preferences.GetLoginDetails();
            if (dataModel != null) {
                strUserId = dataModel.getUserId();
            }

            Call<ObjectResponseModel> call = service.apiGetFriendsScore(strUserId, strFriendUserId);
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
//                                strPlugSPaceRank = loginDataModel.getPlugspaceRank();
//                                tvPlugSpaceRank.setText(strPlugSPaceRank);

                                    isPrivate = loginDataModel.getIsPrivate();
                                    Logger.d("test_isPrivate", isPrivate);
                                    setPlugSpaceRank(loginDataModel.getPlugspaceRank());
                                    setMySelfScore(loginDataModel.getRank());

                                    if (lstModel == null) {
                                        lstModel = new ArrayList<>();
                                    }
                                    lstModel.clear();
                                    lstModel.addAll(loginDataModel.getLstCharacteristics());
                                    setDataToList();

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
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setPlugSpaceRank(String strPlugSPaceRank) {

        if (isPrivate.equals(Constants.IS_PRIVATE_1)) {
//            cvWoman.setVisibility(View.GONE);
            cvGay.setVisibility(View.GONE);
        } else {
            if (Utils.isValidaEmptyWithZero(strPlugSPaceRank)) {
                strPlugSPaceRank = "0";
            }
            tvPlugSpaceRank.setText(strPlugSPaceRank);
//            cvWoman.setVisibility(View.VISIBLE);
            cvGay.setVisibility(View.VISIBLE);
        }
    }

    private void setMySelfScore(String strRank) {
        if (Utils.isValidaEmptyWithZero(strRank)) {
            strRank = "0";
        }
        tvMySelfScore.setText(strRank);
    }
}
