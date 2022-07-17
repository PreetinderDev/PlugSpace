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

public class RelationshipStatusFragment extends BaseFragment implements View.OnClickListener, SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    Button btnNext;
    RecyclerView rvRelationshipStatus;
    SlcHeightAdapter relaStatusAdapter;
    ArrayList<HeightModel> relaStatusList = new ArrayList<>();
    int relationPosition = 0;

    public RelationshipStatusFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_relationship_status, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        initClick(view);
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getRelationshipStatus())) {
            for (int i = 0; i < relaStatusList.size(); i++) {
                relaStatusList.get(i).setBooleanSelected(false);
            }
            relaStatusList.get(relationPosition).setBooleanSelected(true);
            Constants.loginDataModel.setRelationshipStatus(Constants.loginDataModel.getRelationshipStatus());
            relaStatusAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcBIO - >", Constants.loginDataModel.getRelationshipStatus());
        }
        return view;
    }

    private void initFillData() {
        relaStatusList.clear();
        relaStatusList.add(new HeightModel(getString(R.string.definitely_single), false));
        relaStatusList.add(new HeightModel(getString(R.string.separated), false));
        relaStatusList.add(new HeightModel(getString(R.string.divorced), false));
        relaStatusList.add(new HeightModel(getString(R.string.windowed), false));
        relaStatusList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setRelationshipStatus(relaStatusList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "relation 2 - " + relaStatusList.get(Constants.SelectedPosition).getString());
    }

    private void initClick(View view) {
        btnNext.setOnClickListener(this);
    }

    private void initView(View view) {
        btnNext = view.findViewById(R.id.btnNext);

        rvRelationshipStatus = view.findViewById(R.id.rvRelationshipStatus);
        rvRelationshipStatus.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        relaStatusAdapter = new SlcHeightAdapter(activity, relaStatusList, RelationshipStatusFragment.this);
        rvRelationshipStatus.setAdapter(relaStatusAdapter);
    }

    @Override
    public void mySlcHeightClicked(int position) {
        relationPosition = position;
        for (int i = 0; i < relaStatusList.size(); i++) {
            relaStatusList.get(i).setBooleanSelected(false);
        }
        relaStatusList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setRelationshipStatus(relaStatusList.get(position).getString());
        Logger.e("16/12", "relation - " + relaStatusList.get(position).getString());
        relaStatusAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnNext) {
            if (backClickListener != null) {
                backClickListener.gobackClick(14);
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
        view.setText(R.string._14);
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
