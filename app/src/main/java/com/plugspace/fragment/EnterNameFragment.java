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

import com.plugspace.BuildConfig;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;

public class EnterNameFragment extends BaseFragment {
  Interfaces.OnGoBackClickListener backClickListener;
  Activity activity;
  Button btnNext;
  EditText etName;

  public EnterNameFragment(Interfaces.OnGoBackClickListener activity) {
    backClickListener = activity;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_enter_name, container, false);
    activity = getActivity();
    initViewData(view);
    initClick();
    if (!Utils.isValidationEmpty(Constants.loginDataModel.getName())) {
      etName.setText(Constants.loginDataModel.getName());
    }
    return view;
  }

  private void initClick() {
    btnNext.setOnClickListener(view -> isValid());
  }

  private void initViewData(View view) {
    btnNext = view.findViewById(R.id.btnNext);
    etName = view.findViewById(R.id.etName);
  }

  public void isValid() {
    if (Utils.isValidationEmpty(etName.getText().toString().trim())) {
      Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.lbl_enter_name));
    } else {
      Constants.loginDataModel.setName(etName.getText().toString().trim());

      if (backClickListener != null) {
        backClickListener.gobackClick(4);
      }
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
    TextView view = activity.findViewById(R.id.tvNumber4);
    view.setText(R.string._4);
    ChangeNumber(activity.findViewById(R.id.ivGrayBg4), view);
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
