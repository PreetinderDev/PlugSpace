package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.plugspace.R;
import com.plugspace.adapters.RankYourSelfAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;
import com.plugspace.model.RankYourSelfResponse;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class RankYourSelfFragment extends BaseFragment {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    IndicatorSeekBar isbRankYourSelf;
    Button btnNext;
    //    LinearLayout llRankYourSelfDes;
    private RecyclerView rvModel;
    private RankYourSelfAdapter mAdapter;
    //    private LinearLayout llEmptyList;
//    private TextView tvEmptyList;
    int strRank;
    private List<String> lstModel = new ArrayList<>();
    TextView tvRankMsg;
    String strGender = "";

    public RankYourSelfFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_your_self, container, false);

        activity = getActivity();
        strGender = Constants.loginDataModel.getGender();

        initView(view);
        initClick();
        mAdapter = null;
        setDataToList();
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getRank())) {
            Logger.e("16/12 - slcRank - ", String.valueOf(strRank));
            isbRankYourSelf.setProgress(Float.parseFloat(Constants.loginDataModel.getRank()));

            GetRankPerson(Integer.parseInt(Constants.loginDataModel.getRank()));

        }else {
            strRank=1;
            isbRankYourSelf.setProgress(strRank);
            GetRankPerson(strRank);
        }
        return view;
    }

    private void setDataToList() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new RankYourSelfAdapter(activity, lstModel);
            rvModel.setAdapter(mAdapter);
        }

        if (lstModel != null && lstModel.size() > 0) {
            rvModel.setVisibility(View.VISIBLE);
//            llEmptyList.setVisibility(View.GONE);
        } else {
            rvModel.setVisibility(View.GONE);
//            llEmptyList.setVisibility(View.VISIBLE);
//            tvEmptyList.setText(activity.getResources().getString(R.string.empty_list_rank));
        }
    }

    private void initClick() {
        isbRankYourSelf.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                strRank = seekBar.getProgress();
                Constants.loginDataModel.setRank(String.valueOf(strRank));
                Logger.e("16/12", "rank - " + String.valueOf(strRank));
                GetRankPerson(strRank);
            }
        });

        btnNext.setOnClickListener(view -> {
            if (backClickListener != null) {
                backClickListener.gobackClick(5);
            }
        });
    }

    private void clearModelList(){
        if (lstModel == null) {
            lstModel = new ArrayList<>();
        }
        lstModel.clear();
    }

    private void GetRankPerson(int strRank) {
//        if (strRank > 0 && Utils.isNetworkAvailable(activity, true, false)) {
        if ( Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, null).create(ApiService.class);

//            String strValue = "";
//            if (strGender.contains(getString(R.string.biologically_male))
//                    || strGender.contains(getString(R.string.trans_male))) {
//                strValue = "Male";
//            } else if (strGender.contains(getString(R.string.biologically_female))
//                    || strGender.contains(getString(R.string.trans_female))) {
//                strValue = "Female";
//            } else {
//                strValue = "Others";
//            }

//            Logger.d("test_rank", "Rank: " + strRank + ", Gender:" + strValue);
//            Call<RankYourSelfResponse> call = service.getRankPerson(String.valueOf(strRank),
//                    strValue);

            Logger.d("test_rank", "Rank: " + strRank + ", Gender:" + strGender);
            Call<RankYourSelfResponse> call = service.getRankPerson(String.valueOf(strRank),
                    strGender);

            call.enqueue(new Callback<RankYourSelfResponse>() {
                @Override
                public void onResponse(@NonNull Call<RankYourSelfResponse> call,
                                       @NonNull Response<RankYourSelfResponse> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        RankYourSelfResponse model = response.body();
                        if (model != null) {
                            if (model.getResponseCode() == 1) {
                                Logger.d("test_rank", new Gson().toJson(model));

                                clearModelList();
                                lstModel.addAll(model.getData());

//                            if (lstModel != null) {
//                                mAdapter = new RankYourSelfAdapter(activity, lstModel);
//                                rvModel.setAdapter(mAdapter);
//                            }

                                setDataToList();
//                            btnNext.setVisibility(View.VISIBLE);

                            } else {
                                clearModelList();
                                setDataToList();
                                if (!model.getResponseMsg().isEmpty()) {
                                    Utils.showAlert(activity, getString(R.string.app_name),
                                            model.getResponseMsg());
                                }
                            }
                        }else {
                            clearModelList();
                            setDataToList();
                        }
                    } else {
                        clearModelList();
                        setDataToList();
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RankYourSelfResponse> call, @NonNull Throwable t) {
                    clearModelList();
                    setDataToList();
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
//        }else {
//            clearModelList();
//            setDataToList();
        }
    }

    private void initView(View view) {
        tvRankMsg = view.findViewById(R.id.tvRankMsg);
        isbRankYourSelf = view.findViewById(R.id.isbRankYourSelf);
        btnNext = view.findViewById(R.id.btnNext);
//        llRankYourSelfDes = view.findViewById(R.id.llRankYourSelfDes);

//        llEmptyList = view.findViewById(R.id.llEmptyList);
//        tvEmptyList = view.findViewById(R.id.tvEmptyList);
        rvModel = view.findViewById(R.id.rvRankYourSelf);
        rvModel.setLayoutManager(new LinearLayoutManager(activity));

        if (strGender.contains(getString(R.string.biologically_male))
                || strGender.contains(getString(R.string.trans_male))) {
            tvRankMsg.setText(getString(R.string.no_filter_or_makeup_male));
        } else if (strGender.contains(getString(R.string.biologically_female))
                || strGender.contains(getString(R.string.trans_female))) {
            tvRankMsg.setText(getString(R.string.no_filter_or_makeup_));

        } else {
//            tvRankMsg.setText(getString(R.string.no_filter_or_makeup_));
            tvRankMsg.setText(getString(R.string.no_filter_or_makeup_male));

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
        TextView view = activity.findViewById(R.id.tvNumber5);
        view.setText(R.string._5);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg5), view);
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
}
