package com.plugspace.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;
import com.plugspace.model.AgeCalculatorResponseModel;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class DateOfBirthFragment extends BaseFragment implements View.OnClickListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    ImageView ivCalender;
    LinearLayout llMyAge;
    Button btnNext;
    EditText etDOB;
    TextView tvMyAge;
    String strDOB = "";

    public DateOfBirthFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_of_birth, container, false);
        activity = getActivity();
        initView(view);
        initClick();

        if (!Utils.isValidationEmpty(Constants.loginDataModel.getDob())) {
            strDOB = Constants.loginDataModel.getDob();
            strDOB = Utils.customDateTimeFormat(strDOB, Constants.DATE_FORMAT_YYYY_MM_DD_DASH, Constants.DATE_FORMAT_YYYY_MM_DD_DASH);
            etDOB.setText(strDOB);
            AgeCalculator(strDOB);
        }
        return view;
    }

    private void initClick() {
        ivCalender.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    private void initView(View view) {
        tvMyAge = view.findViewById(R.id.tvMyAge);
        etDOB = view.findViewById(R.id.etDOB);
        ivCalender = view.findViewById(R.id.ivCalender);
        llMyAge = view.findViewById(R.id.llMyAge);
        btnNext = view.findViewById(R.id.btnNext);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCalender:

                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int year = c.get(Calendar.YEAR);

                String dob = etDOB.getText().toString().trim();
                if (!Utils.isValidationEmpty(dob)) {

                    year = Integer.parseInt(Utils.customDateTimeFormat(dob, Constants.DATE_FORMAT_YYYY_MM_DD_DASH, Constants.DATE_FORMAT_YYYY));
                    month = Integer.parseInt(Utils.customDateTimeFormat(dob, Constants.DATE_FORMAT_YYYY_MM_DD_DASH, Constants.DATE_FORMAT_MM));
                    day = Integer.parseInt(Utils.customDateTimeFormat(dob, Constants.DATE_FORMAT_YYYY_MM_DD_DASH, Constants.DATE_FORMAT_DD));
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, (view1, year1, month1, dayOfMonth) -> {
                    int months = month1 + 1;
//                    String strDOB = dayOfMonth+"-"+months+"-"+ year1;
                    strDOB = year1 + "-" + months + "-" + dayOfMonth;
                    strDOB = Utils.customDateTimeFormat(strDOB, Constants.DATE_FORMAT_YYYY_MM_DD_DASH, Constants.DATE_FORMAT_YYYY_MM_DD_DASH);
                    etDOB.setText(strDOB);
                    Constants.loginDataModel.setDob(strDOB);
                    AgeCalculator(strDOB);

                }, year, month, day);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -18);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

                datePickerDialog.show();
                break;

            case R.id.btnNext:
                isValid();
                break;
        }
    }

    public void isValid() {
        if (Utils.isValidationEmpty(etDOB.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.lbl_enter_dob));
        } else {
            if (backClickListener != null) {
                backClickListener.gobackClick(10);
            }
        }
    }

    private void AgeCalculator(String strDOB) {
//        if (Utils.isNetworkAvailable(activity, true, false)) {
//            showProgressDialog(activity);
//            ApiService service = ApiClient.UserApiClient(activity, null).create(ApiService.class);
//
//            Call<AgeCalculatorResponseModel> call = service.ageCalculator(strDOB);
//
//            call.enqueue(new Callback<AgeCalculatorResponseModel>() {
//                @Override
//                public void onResponse(@NonNull Call<AgeCalculatorResponseModel> call,
//                                       @NonNull ArrayResponseModel<AgeCalculatorResponseModel> response) {
//                    hideProgressDialog(activity);
//                    if (response.isSuccessful()) {
//                        AgeCalculatorResponseModel model = response.body();
//                        if (model != null) {
//                            if (model.getResponseCode() == 1) {
//
//                                llMyAge.setVisibility(View.VISIBLE);
//                                tvMyAge.setText(model.getData());
//                                String strAge = model.getData().replace(" years old", "");
//                                Constants.loginDataModel.setAge(strAge);
//                                Logger.e("16/12 -age -", strAge);
//
//                            } else {
//                                if (!model.getResponseMsg().isEmpty()) {
//                                    Utils.showAlert(activity, getString(R.string.app_name),
//                                            model.getResponseMsg());
//                                }
//                            }
//                        }
//                    } else {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<AgeCalculatorResponseModel> call, @NonNull Throwable t) {
//                    hideProgressDialog(activity);
//                    if (!Utils.isNetworkAvailable(activity, true, false)) {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
//                                activity.getResources().getString(R.string.error_network));
//                    } else {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
//                                activity.getResources().getString(R.string.technical_problem));
//                    }
//                    t.printStackTrace();
//                }
//            });
//        }

        if (!Utils.isValidationEmpty(strDOB)) {
            int selectedYear = Integer.parseInt(Utils.customDateTimeFormat(strDOB, Constants.DATE_FORMAT_YYYY_MM_DD_DASH, Constants.DATE_FORMAT_YYYY));
            int currentYear = Integer.parseInt(Utils.currentDateTimeFormat(Constants.DATE_FORMAT_YYYY));
            int oldYears = currentYear - selectedYear;

            llMyAge.setVisibility(View.VISIBLE);
            String age = activity.getResources().getString(R.string.age_years_old, oldYears);
            tvMyAge.setText(age);
            Constants.loginDataModel.setAge(age);
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
        TextView view = activity.findViewById(R.id.tvNumber10);
        view.setText(R.string._10);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg10), view);
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
