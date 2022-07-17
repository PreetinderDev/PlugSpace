package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ScoreFragment extends BaseFragment {
    private Activity activity;
    private SwitchButton swScore;
    //    String /*strPrivate = "",*/ strUserId = "";
    private TextView tvPlugSpaceRank, tvMySelfScore/*, tvCharaMsg*/;
    //    private String strUserId = ""/*, strPluSpaceRank = ""*//*, strCharaMsg = ""*/;
    private CharacteristicsAdapter mAdapter;
    private List<CharacteristicsModel> lstModel = new ArrayList<>();
    private RecyclerView rvModel;
    private LinearLayout llCharacteristics;
    //    private String isPrivate = "";
    private boolean isManuallySwitch = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score, container, false);

        activity = getActivity();
        initToolBar(view);
        initView(view);
        initClick();
        setDataToList();
        setDefaultData();
        setKeepMyScorePrivate(Constants.IS_PRIVATE_1);
        doAPIGetMyScore();


//        if (Utils.isNetworkAvailable(activity, true, false)) {
//            GetMyScore();
//        }
        return view;
    }

    private void setKeepMyScorePrivate(String isPrivate) {
//        isManuallySwitch = false;
        if (isPrivate.equals(Constants.IS_PRIVATE_0)) {
//            swScore.setChecked(false);
            setIsMyRankSwitch(false);
        } else {
//            swScore.setChecked(true);
            setIsMyRankSwitch(true);
        }
    }

    private void setDefaultData() {
        setPlugSpaceRank("");
        setMySelfScore("");
    }

    private void setPlugSpaceRank(String strPlugSPaceRank) {
        if (Utils.isValidaEmptyWithZero(strPlugSPaceRank)) {
            strPlugSPaceRank = "0";
        }
        tvPlugSpaceRank.setText(strPlugSPaceRank);
    }

    private void setMySelfScore(String strRank) {
        if (Utils.isValidaEmptyWithZero(strRank)) {
            strRank = "0";
        }
        tvMySelfScore.setText(strRank);
    }

    private void setDataToList() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new CharacteristicsAdapter(activity, lstModel, false);
            rvModel.setAdapter(mAdapter);
        }

        if (lstModel.size() > 0) {
            llCharacteristics.setVisibility(View.VISIBLE);
        } else {
            llCharacteristics.setVisibility(View.GONE);
        }
    }

    private void initClick() {

        swScore.setOnCheckedChangeListener((view, isChecked) -> {
//            if (isChecked) {
//                swScore.setChecked(true);
////                setKeepMyScorePrivate(Constants.IS_PRIVATE_1);
////                isPrivate = Constants.IS_PRIVATE_1;
////                strPrivate = "1";
////                Preferences.SetStringName(Constants.PRIVATE, strPrivate);
//
//
//                IsPrivateScore();
//
//            } else {
//                swScore.setChecked(false);
////                isPrivate = Constants.IS_PRIVATE_0;
////                setKeepMyScorePrivate(Constants.IS_PRIVATE_0);
////                strPrivate = "0";
////                Preferences.SetStringName(Constants.UN_PRIVATE, strPrivate);
//            }
            if (isManuallySwitch) {
                doAPIIsPrivateScore();
            } else {
                isManuallySwitch = true;
//                new Handler(Looper.getMainLooper()).postDelayed(() -> isManuallySwitch = true, 500);
            }
        });
    }

    private void initToolBar(View view) {
        TextView tvTitleName = view.findViewById(R.id.tvTitleName);
        tvTitleName.setText(R.string.my_score);
    }

    private void initView(View view) {
//        LoginDataModel dataModel = Preferences.GetLoginDetails();
//        if (dataModel != null) {
//            strUserId = dataModel.getUserId();
//        }

        swScore = view.findViewById(R.id.swScore);
//        tvCharaMsg = view.findViewById(R.id.tvCharaMsg);
        tvPlugSpaceRank = view.findViewById(R.id.tvPlugSpaceRank);
        tvMySelfScore = view.findViewById(R.id.tvMySelfScore);
        rvModel = view.findViewById(R.id.rvModel);
        llCharacteristics = view.findViewById(R.id.llCharacteristics);

        rvModel.setLayoutManager(new LinearLayoutManager(activity));
    }


    private void doAPIIsPrivateScore() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            String isPrivate = swScore.isChecked() ? Constants.IS_PRIVATE_1 : Constants.IS_PRIVATE_0;
            Logger.d("test_isPrivate", isPrivate);

            Call<ObjectResponseModel> call = service.apiIsPrivateScore(Preferences.getStringName(Preferences.keyUserId), isPrivate);
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        ObjectResponseModel model = response.body();
                        if (model != null) {
                            if (model.getResponseCode() == 1) {

//                            if (Utils.isNetworkAvailable(activity, true, false)) {
//                                GetMyScore();
//                            }
//                                Utils.showToast(activity, model.getResponseMsg());

                            } else {
//                                isManuallySwitch = false;
//                                swScore.setChecked(!swScore.isChecked());
                                setIsMyRankSwitch(!swScore.isChecked());

                                if (!model.getResponseMsg().isEmpty()) {
                                    Utils.showAlert(activity, getString(R.string.app_name), model.getResponseMsg());
                                }
                            }
                        }
                    } else {
//                        isManuallySwitch = false;
//                        swScore.setChecked(!swScore.isChecked());
                        setIsMyRankSwitch(!swScore.isChecked());
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
//                    swScore.setChecked(!swScore.isChecked());
                    setIsMyRankSwitch(!swScore.isChecked());

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

    private void doAPIGetMyScore() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiGetMyScore(Preferences.getStringName(Preferences.keyUserId));
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

                                if (dataModel != null) {
//                                    strPluSpaceRank = dataModel.getPlugspaceRank();
                                    setPlugSpaceRank(dataModel.getPlugspaceRank());
                                    setMySelfScore(dataModel.getRank());
//                                    Preferences.SetStringName(Constants.PLUG_SPACE_RANK, strPluSpaceRank);

                                    String isPrivate = dataModel.getIsPrivate().equals(Constants.IS_PRIVATE_0) ? Constants.IS_PRIVATE_0 : Constants.IS_PRIVATE_1;
                                    Logger.d("test_isPrivate", isPrivate);
                                    setKeepMyScorePrivate(isPrivate);


                                    if (lstModel == null) {
                                        lstModel = new ArrayList<>();
                                    }
                                    lstModel.clear();
                                    lstModel.addAll(dataModel.getLstCharacteristics());
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

    private void setIsMyRankSwitch(boolean isMyRankSwitch) {
        isManuallySwitch = false;
        swScore.setChecked(isMyRankSwitch);

    }
}
