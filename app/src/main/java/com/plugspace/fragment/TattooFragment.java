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
import com.plugspace.adapters.SlcHeightAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class TattooFragment extends BaseFragment implements SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    RecyclerView rvHowManyTattoo;
    SlcHeightAdapter tattooAdapter;
    ArrayList<HeightModel> tattooList = new ArrayList<>();
    int tattooPosition = 0;

    public TattooFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tattoo_how_many, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        initClick(view);
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getYourBodyTatto())) {
            for (int i = 0; i < tattooList.size(); i++) {
                tattooList.get(i).setBooleanSelected(false);
            }
            tattooList.get(tattooPosition).setBooleanSelected(true);
            Constants.loginDataModel.setYourBodyTatto(Constants.loginDataModel.getYourBodyTatto());
            tattooAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcTattoo - >", Constants.loginDataModel.getYourBodyTatto());
        }
        return view;
    }

    private void initFillData() {
        tattooList.clear();
        for (int i = 0; i <= 5; i++) {
            tattooList.add(new HeightModel(String.valueOf(i), false));
        }
        tattooList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setYourBodyTatto(tattooList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "tattoo 2 - " + tattooList.get(Constants.SelectedPosition).getString());
    }

    private void initClick(View view) {
        view.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backClickListener != null) {
                    backClickListener.gobackClick(22);
                }
            }
        });
    }

    private void initView(View view) {
        rvHowManyTattoo = view.findViewById(R.id.rvHowManyTattoo);
        rvHowManyTattoo.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        tattooAdapter = new SlcHeightAdapter(activity, tattooList, TattooFragment.this);
        rvHowManyTattoo.setAdapter(tattooAdapter);
    }

    @Override
    public void mySlcHeightClicked(int position) {
        tattooPosition = position;
        for (int i = 0; i < tattooList.size(); i++) {
            tattooList.get(i).setBooleanSelected(false);
        }
        tattooList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setYourBodyTatto(tattooList.get(position).getString());
        Logger.e("16/12", "tattoo - " + tattooList.get(position).getString());
        tattooAdapter.notifyDataSetChanged();
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
        TextView view = activity.findViewById(R.id.tvNumber2);
        view.setText(R.string._22);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg2), view);
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
