package com.plugspace.fragment;

import android.animation.ValueAnimator;
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

import com.google.android.material.slider.RangeSlider;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;
import java.util.List;

public class MrgRangeAgeFragment extends BaseFragment {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    Button btnNext;
    RangeSlider rsRangeMrgAge;

    public MrgRangeAgeFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mrg_range_age, container, false);
        activity = getActivity();
        initView(view);
        initClick();
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getAgeRangeMarriage())) {
            Constants.loginDataModel.setAgeRangeMarriage(Constants.loginDataModel.getAgeRangeMarriage());
            Logger.e("16/12", "mrgRange - " + Constants.loginDataModel.getAgeRangeMarriage());
        }
        return view;
    }

    private void initClick() {
        btnNext.setOnClickListener(view -> {
            if (backClickListener != null) {
                backClickListener.gobackClick(23);
            }
        });

        rsRangeMrgAge.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            int valueFrom = Math.round(values.get(0));
            int valueTo = Math.round(values.get(1));

            String ageRangeMarriage = valueFrom + "-" + valueTo;
            Constants.loginDataModel.setAgeRangeMarriage(ageRangeMarriage);
            Logger.d("test_ageRangeMarriage", ageRangeMarriage);

        });

        //// first time default value set from = 18 and to = 26.
        rsRangeMrgAge.setValues(18f, 26f);
    }

    private void initView(View view) {
        btnNext = view.findViewById(R.id.btnNext);
        rsRangeMrgAge = view.findViewById(R.id.rsRangeMrgAge);
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
        TextView view = activity.findViewById(R.id.tvNumber3);
        view.setText(R.string._23);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg3), view);
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
