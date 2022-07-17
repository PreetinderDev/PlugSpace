package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.plugspace.activities.BaseActivity;
import com.plugspace.adapters.SlcHeightAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class HeightFragment extends BaseFragment implements SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    RecyclerView rvSlcHeight;
    SlcHeightAdapter heightAdapter;
    ArrayList<HeightModel> heightList = new ArrayList<>();
    int heightPosition = 0;

    public HeightFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_height, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getHeight())) {
            for (int i = 0; i < heightList.size(); i++) {
                heightList.get(i).setBooleanSelected(false);
            }
            heightList.get(heightPosition).setBooleanSelected(true);
            Constants.loginDataModel.setHeight(Constants.loginDataModel.getHeight());
            heightAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcHeight - >", Constants.loginDataModel.getHeight());
        }
        return view;
    }

    private void initFillData() {
        heightList.clear();
        heightList.add(new HeightModel(getString(R.string._3_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_8_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_9_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_10_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_11_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_8_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_9_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_10_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_11_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_8_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_9_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_10_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_11_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_8_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_9_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_10_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_11_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_8_ft), false));
        heightList.get(Constants.SelectedPosition).setBooleanSelected(true);
    }

    private void initView(View view) {
        rvSlcHeight = view.findViewById(R.id.rvSlcHeight);
        rvSlcHeight.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        heightAdapter = new SlcHeightAdapter(activity, heightList, HeightFragment.this);
        rvSlcHeight.setAdapter(heightAdapter);
    }

    @Override
    public void mySlcHeightClicked(int position) {

        heightPosition = position;
        for (int i = 0; i < heightList.size(); i++) {
            heightList.get(i).setBooleanSelected(false);
        }
        heightList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setHeight(heightList.get(position).getString());
        heightAdapter.notifyDataSetChanged();

        if (backClickListener != null) {
            backClickListener.gobackClick(7);
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
        TextView view = activity.findViewById(R.id.tvNumber7);
        view.setText(R.string._7);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg7), view);
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
