package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;

public class MakeOverFragment extends BaseFragment {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    Button btnNext;
    TextView tvSkipYourSalary;
    EditText etYourSalary;

    public MakeOverFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_salary, container, false);
        activity = getActivity();
        initViewData(view);
        initClick();
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getMakeOver())) {
            etYourSalary.setText(Constants.loginDataModel.getMakeOver());
            Logger.e("16/12 - slcMakeOver - >", Constants.loginDataModel.getMakeOver());
        }
        return view;
    }

    private void initClick() {
        btnNext.setOnClickListener(view -> {
            if (!Utils.isValidationEmpty(etYourSalary.getText().toString().trim())) {
                Logger.e("16/12", "salary salary - " + etYourSalary.getText().toString().trim());
                Constants.loginDataModel.setMakeOver(etYourSalary.getText().toString().trim());

            } else {
                Constants.loginDataModel.setMakeOver("");
                Logger.e("16/12", "salary - " + etYourSalary.getText().toString().trim());
            }
            if (backClickListener != null) {
                backClickListener.gobackClick(18);
            }
        });

        tvSkipYourSalary.setOnClickListener(view -> {
            Constants.loginDataModel.setMakeOver(""); //  check: Temporary
            Logger.e("16/12", "salary 2 - " + etYourSalary.getText().toString().trim());

            if (backClickListener != null) {
                backClickListener.gobackClick(18);
            }
        });
    }

    private void initViewData(View view) {
        btnNext = view.findViewById(R.id.btnNext);
        tvSkipYourSalary = view.findViewById(R.id.tvSkipYourSalary);
        etYourSalary = view.findViewById(R.id.etYourSalary);
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
        view.setText(R.string._18);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg8), view);
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
