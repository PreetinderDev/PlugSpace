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

public class MrgRaceFragment extends BaseFragment implements View.OnClickListener, SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    RecyclerView rvOpenMarring;
    SlcHeightAdapter openMarringAdapter;
    ArrayList<HeightModel> openMarringList = new ArrayList<>();
    Button btnNext;
    int mrgRacePosition = 0;

    public MrgRaceFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_marring, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        initClick();
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getMarringRace())) {
            for (int i = 0; i < openMarringList.size(); i++) {
                openMarringList.get(i).setBooleanSelected(false);
            }
            openMarringList.get(mrgRacePosition).setBooleanSelected(true);
            Constants.loginDataModel.setMarringRace(Constants.loginDataModel.getMarringRace());
            openMarringAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcMrgRace - >", Constants.loginDataModel.getMarringRace());
        }
        return view;
    }

    private void initFillData() {
        openMarringList.clear();
        openMarringList.add(new HeightModel(getString(R.string.yes), false));
        openMarringList.add(new HeightModel(getString(R.string.no), false));
        openMarringList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setMarringRace(openMarringList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "mrgRace 2 - " + openMarringList.get(Constants.SelectedPosition).getString());
    }

    private void initClick() {
        btnNext.setOnClickListener(this);
    }

    private void initView(View view) {
        btnNext = view.findViewById(R.id.btnNext);

        rvOpenMarring = view.findViewById(R.id.rvOpenMarring);
        rvOpenMarring.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        openMarringAdapter = new SlcHeightAdapter(activity, openMarringList, MrgRaceFragment.this);
        rvOpenMarring.setAdapter(openMarringAdapter);
    }

    @Override
    public void mySlcHeightClicked(int position) {
        mrgRacePosition = position;
        for (int i = 0; i < openMarringList.size(); i++) {
            openMarringList.get(i).setBooleanSelected(false);
        }
        openMarringList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setMarringRace(openMarringList.get(position).getString());
        Logger.e("16/12", "mrgRace - " + openMarringList.get(position).getString());
        openMarringAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (backClickListener != null) {
                    backClickListener.gobackClick(13);
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
        TextView view = activity.findViewById(R.id.tvNumber3);
        view.setText(R.string._13);
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
