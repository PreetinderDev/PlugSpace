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

public class ChildrenFragment extends BaseFragment implements SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    RecyclerView rvSlcChildren;
    SlcHeightAdapter childrenAdapter;
    ArrayList<HeightModel> childrenList = new ArrayList<>();
    int chiPosition = 0;

    public ChildrenFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_children_how_many, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        initClick(view);
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getChildren())) {
            for (int i = 0; i < childrenList.size(); i++) {
                childrenList.get(i).setBooleanSelected(false);
            }
            childrenList.get(chiPosition).setBooleanSelected(true);
            Constants.loginDataModel.setChildren(Constants.loginDataModel.getChildren());
            childrenAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcChildren - >", Constants.loginDataModel.getChildren());
        }
        return view;
    }

    private void initFillData() {
        childrenList.clear();
        for (int i = 0; i <= 5; i++) {
            childrenList.add(new HeightModel(String.valueOf(i), false));
        }
        childrenList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setChildren(childrenList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "children 2 - " + childrenList.get(Constants.SelectedPosition).getString());

    }

    private void initClick(View view) {
        view.findViewById(R.id.btnNext).setOnClickListener(view1 -> {
            if (backClickListener != null) {
                backClickListener.gobackClick(11);
            }
        });
    }

    private void initView(View view) {
        rvSlcChildren = view.findViewById(R.id.rvSlcChildren);
        rvSlcChildren.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        childrenAdapter = new SlcHeightAdapter(activity, childrenList, ChildrenFragment.this);
        rvSlcChildren.setAdapter(childrenAdapter);
    }

    @Override
    public void mySlcHeightClicked(int position) {
        chiPosition = position;
        for (int i = 0; i < childrenList.size(); i++) {
            childrenList.get(i).setBooleanSelected(false);
        }
        childrenList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setChildren(childrenList.get(position).getString());
        Logger.e("16/12", "children - " + childrenList.get(position).getString());
        childrenAdapter.notifyDataSetChanged();
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
        TextView view = activity.findViewById(R.id.tvNumber1);
        view.setText(R.string._11);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg1), view);
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
