package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.SlcHeightAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class PayBillsFragment extends BaseFragment implements View.OnClickListener, SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    TextView tvSkipPayBills;
    Button btnNext;
    RecyclerView rvPayBills;
    SlcHeightAdapter payBillsAdapter;
    ArrayList<HeightModel> payBillsList = new ArrayList<>();
    int bioPosition = 0;

    public PayBillsFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_bills, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        initClick();
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getSigniatBills())) {
            for (int i = 0; i < payBillsList.size(); i++) {
                payBillsList.get(i).setBooleanSelected(false);
            }
            payBillsList.get(bioPosition).setBooleanSelected(true);
            Constants.loginDataModel.setSigniatBills(Constants.loginDataModel.getSigniatBills());
            payBillsAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcBill - >", Constants.loginDataModel.getSigniatBills());
        }
        return view;
    }

    private void initFillData() {
        payBillsList.clear();
        payBillsList.add(new HeightModel(getString(R.string.yes), false));
        payBillsList.add(new HeightModel(getString(R.string.no), false));
        payBillsList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setSigniatBills(payBillsList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "bill 2 - " + payBillsList.get(Constants.SelectedPosition).getString());
    }

    private void initClick() {
        btnNext.setOnClickListener(this);
        tvSkipPayBills.setOnClickListener(this);
    }

    private void initView(View view) {
        tvSkipPayBills = view.findViewById(R.id.tvSkipPayBills);
        btnNext = view.findViewById(R.id.btnNext);

        rvPayBills = view.findViewById(R.id.rvPayBills);
        rvPayBills.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        payBillsAdapter = new SlcHeightAdapter(activity, payBillsList, PayBillsFragment.this);
        rvPayBills.setAdapter(payBillsAdapter);
    }


    @Override
    public void mySlcHeightClicked(int position) {
        bioPosition = position;
        for (int i = 0; i < payBillsList.size(); i++) {
            payBillsList.get(i).setBooleanSelected(false);
        }
        payBillsList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setSigniatBills(payBillsList.get(position).getString());
        Logger.e("16/12", "bill - " + payBillsList.get(position).getString());
        payBillsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (backClickListener != null) {
                    backClickListener.gobackClick(20);
                }
                break;
            case R.id.tvSkipPayBills:
                Constants.loginDataModel.setSigniatBills("");
                if (backClickListener != null) {
                    backClickListener.gobackClick(20);
                }
                break;

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
        view.setText(R.string._20);
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
