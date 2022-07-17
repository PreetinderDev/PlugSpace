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
import androidx.core.widget.NestedScrollView;
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

public class DressSizeFragment extends BaseFragment implements SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    RecyclerView rvSlcDressSize;
    SlcHeightAdapter dressSizeAdapter;
    ArrayList<HeightModel> dressSizeList = new ArrayList<>();
//    NestedScrollView nsvDressSize;
//    ImageView ivScrollDownArrow;
    int dressPosition = 0;

    public DressSizeFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dress_size, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        initClick(view);
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getDressSize())) {
            for (int i = 0; i < dressSizeList.size(); i++) {
                dressSizeList.get(i).setBooleanSelected(false);
            }
            dressSizeList.get(dressPosition).setBooleanSelected(true);
            Constants.loginDataModel.setDressSize(Constants.loginDataModel.getDressSize());
            dressSizeAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcDress - >", Constants.loginDataModel.getDressSize());
        }
        return view;
    }

    private void initFillData() {
        dressSizeList.clear();
        for (int i = 1; i <= 26; i++) {
            dressSizeList.add(new HeightModel(String.valueOf(i), false));
        }
        dressSizeList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setDressSize(dressSizeList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "drees 2 - " + dressSizeList.get(Constants.SelectedPosition).getString());
    }

    private void initClick(View view) {
        view.findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (backClickListener != null) {
                    backClickListener.gobackClick(19);
                }
            }
        });
    }

    private void initView(View view) {
//        ivScrollDownArrow = view.findViewById(R.id.ivScrollDownArrow);
//        nsvDressSize = view.findViewById(R.id.nsvDressSize);
        rvSlcDressSize = view.findViewById(R.id.rvSlcDressSize);
        rvSlcDressSize.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        dressSizeAdapter = new SlcHeightAdapter(activity, dressSizeList, DressSizeFragment.this);
        rvSlcDressSize.setAdapter(dressSizeAdapter);

//        nsvDressSize.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                int listSize = dressSizeList.size();
//                int lastPosition = rvSlcDressSize.getChildCount();
////                if (scrollY > oldScrollY && listSize == lastPosition) {
////                    ivScrollDownArrow.setVisibility(View.GONE);
////                }
////                if (scrollY < oldScrollY) {
////                    ivScrollDownArrow.setVisibility(View.VISIBLE);
////                }
//            }
//
//        });
    }

    @Override
    public void mySlcHeightClicked(int position) {
        dressPosition = position;
        for (int i = 0; i < dressSizeList.size(); i++) {
            dressSizeList.get(i).setBooleanSelected(false);
        }
        dressSizeList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setDressSize(dressSizeList.get(position).getString());
        Logger.e("16/12", "drees - " + dressSizeList.get(position).getString());
        dressSizeAdapter.notifyDataSetChanged();
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
        TextView view = activity.findViewById(R.id.tvNumber9);
        view.setText(R.string._19);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg9), view);
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
