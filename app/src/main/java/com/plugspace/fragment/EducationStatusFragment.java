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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.SlcHeightAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.SpacesItemDecoration;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class EducationStatusFragment extends BaseFragment implements View.OnClickListener, SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    Button btnNext;
    RecyclerView rvEducationStatus;
    SlcHeightAdapter eduStatusAdapter;
    ArrayList<HeightModel> eduStatusList = new ArrayList<>();
    TextView tvOthers;
    CardView cvOthers;
    LinearLayout llOthers;
    int eduPosition = 0, position = 8;

    public EducationStatusFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education_status, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        initClick();
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getEducationStatus()) &&
                Constants.POSITION == 8) {
            for (int i = 0; i < eduStatusList.size(); i++) {
                eduStatusList.get(i).setBooleanSelected(false);
            }
            slcEducationStatus(tvOthers, llOthers);
            Constants.loginDataModel.setEducationStatus(Constants.loginDataModel.getEducationStatus());
            eduStatusAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcEDU 2 - >", Constants.loginDataModel.getEducationStatus());
        } else if (!Utils.isValidationEmpty(Constants.loginDataModel.getEducationStatus())) {
            for (int i = 0; i < eduStatusList.size(); i++) {
                eduStatusList.get(i).setBooleanSelected(false);
            }
            eduStatusList.get(eduPosition).setBooleanSelected(true);
            Constants.loginDataModel.setEducationStatus(Constants.loginDataModel.getEducationStatus());
            eduStatusAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcEDU - >", Constants.loginDataModel.getEducationStatus());
        }
        return view;
    }

    private void initFillData() {
        eduStatusList.clear();
        eduStatusList.add(new HeightModel(getString(R.string.high_school), false));
        eduStatusList.add(new HeightModel(getString(R.string.bachelor), false));
        eduStatusList.add(new HeightModel(getString(R.string.vocational_school), false));
        eduStatusList.add(new HeightModel(getString(R.string.phd), false));
        eduStatusList.add(new HeightModel(getString(R.string.some_collage), false));
        eduStatusList.add(new HeightModel(getString(R.string.advanced_degree), false));
        eduStatusList.add(new HeightModel(getString(R.string.associate_degree), false));
        eduStatusList.add(new HeightModel(getString(R.string.bma), false));
        eduStatusList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setEducationStatus(eduStatusList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "education 2 - " + eduStatusList.get(Constants.SelectedPosition).getString());
    }

    private void initClick() {
        btnNext.setOnClickListener(this);
        cvOthers.setOnClickListener(this);

    }

    private void initView(View view) {
        btnNext = view.findViewById(R.id.btnNext);
        rvEducationStatus = view.findViewById(R.id.rvEducationStatus);
        cvOthers = view.findViewById(R.id.cvOthers);
        tvOthers = view.findViewById(R.id.tvOthers);
        llOthers = view.findViewById(R.id.llOthers);

        LinearLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
        rvEducationStatus.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvEducationStatus.setLayoutManager(layoutManager);
        eduStatusAdapter = new SlcHeightAdapter(activity, eduStatusList, EducationStatusFragment.this);
        rvEducationStatus.setAdapter(eduStatusAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (backClickListener != null) {
                    backClickListener.gobackClick(9);
                }
                break;

            case R.id.cvOthers:
                for (int i = 0; i < eduStatusList.size(); i++) {
                    eduStatusList.get(i).setBooleanSelected(false);
                }
                Constants.POSITION  = 8;
                slcEducationStatus(tvOthers, llOthers);
                Constants.loginDataModel.setEducationStatus(tvOthers.getText().toString().trim());
                Logger.e("16/12", "education 3 - " + Constants.loginDataModel.getEducationStatus());
                eduStatusAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void slcEducationStatus(TextView view, LinearLayout linearLayout) {
        tvOthers.setTextColor(getResources().getColor(R.color.colorBlack));
        llOthers.setBackground(getResources().getDrawable(R.drawable.white_10sdp));

        view.setTextColor(getResources().getColor(R.color.colorOrange));
        linearLayout.setBackground(getResources().getDrawable(R.drawable.white_bg_10sdp_with_orange_border));
    }

    @Override
    public void mySlcHeightClicked(int position) {
        eduPosition = position;
        Constants.POSITION = position;
        for (int i = 0; i < eduStatusList.size(); i++) {
            eduStatusList.get(i).setBooleanSelected(false);
            slcEducationStatus(tvOthers, llOthers);
        }
        eduStatusList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setEducationStatus(eduStatusList.get(position).getString());
        Logger.e("16/12", "education - " + eduStatusList.get(position).getString());
        tvOthers.setTextColor(getResources().getColor(R.color.colorBlack));
        llOthers.setBackground(getResources().getDrawable(R.drawable.white_10sdp));
        eduStatusAdapter.notifyDataSetChanged();
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
        TextView view = activity.findViewById(R.id.tvNumber9);
        view.setText(R.string._9);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg9), view);
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
