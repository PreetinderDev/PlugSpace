package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.WeightAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class WeightFragment extends BaseFragment implements WeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    RecyclerView rvSlcHeight;
    WeightAdapter slcWeightAdapter;
    ArrayList<HeightModel> slcWeightList = new ArrayList<>();
    int bioPosition = 0;

    public WeightFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getWeight())) {
            for (int i = 0; i < slcWeightList.size(); i++) {
                slcWeightList.get(i).setBooleanSelected(false);
            }
            slcWeightList.get(bioPosition).setBooleanSelected(true);
            Constants.loginDataModel.setWeight(Constants.loginDataModel.getWeight());
            slcWeightAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcWeight - >", Constants.loginDataModel.getWeight());
        }
        return view;
    }

    private void initView(View view) {
        rvSlcHeight = view.findViewById(R.id.rvSlcHeight);
        rvSlcHeight.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        slcWeightAdapter = new WeightAdapter(activity, slcWeightList,WeightFragment.this);
        rvSlcHeight.setAdapter(slcWeightAdapter);
    }

    @Override
    public void mySlcHeightClicked(int position) {
        bioPosition = position;
        for (int i = 0; i < slcWeightList.size(); i++) {
            slcWeightList.get(i).setBooleanSelected(false);
        }
        slcWeightList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setWeight(slcWeightList.get(position).getString() + " lbs");
        Logger.e("16/12", "weight - " + Constants.loginDataModel.getWeight());
        slcWeightAdapter.notifyDataSetChanged();
        if (backClickListener != null) {
            backClickListener.gobackClick(8);
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
        TextView view = activity.findViewById(R.id.tvNumber8);
        view.setText(R.string._8);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg8), view);
    }

    private void initFillData() {
        slcWeightList.clear();
        for (int i = 60; i <= 400; i++) {
            slcWeightList.add(new HeightModel(String.valueOf(i), false));
        }
        slcWeightList.get(Constants.SelectedPosition).setBooleanSelected(true);
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
