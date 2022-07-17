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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.SlcHeightAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.SpacesItemDecoration;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class MySelfConsideredFragment extends BaseFragment implements View.OnClickListener, SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    Button btnNext;
    RecyclerView rvMySelfConsider;
    SlcHeightAdapter mySelfConsiderAdapter;
    ArrayList<HeightModel> mySelfConsiderList = new ArrayList<>();
    int bioPosition = 0;

    public MySelfConsideredFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_self_considered, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        initClick(view);
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getMySelfMen())) {
            for (int i = 0; i < mySelfConsiderList.size(); i++) {
                mySelfConsiderList.get(i).setBooleanSelected(false);
            }
            mySelfConsiderList.get(bioPosition).setBooleanSelected(true);
            Constants.loginDataModel.setMySelfMen(Constants.loginDataModel.getMySelfMen());
            mySelfConsiderAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcMySelf - >", Constants.loginDataModel.getMySelfMen());
        }
        return view;
    }

    private void initFillData() {
        mySelfConsiderList.clear();
        mySelfConsiderList.add(new HeightModel(getString(R.string.feminine), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.beta), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.professional), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.gamma), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.sigma), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.regular_guy), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.delta), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.alpha), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.standard_guy), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.celebrity), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.middle_class), false));
        mySelfConsiderList.add(new HeightModel(getString(R.string.others), false));
        mySelfConsiderList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setMySelfMen(mySelfConsiderList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "myself 2 - " + mySelfConsiderList.get(Constants.SelectedPosition).getString());
    }

    private void initClick(View view) {
        btnNext.setOnClickListener(this);
    }

    private void initView(View view) {
        btnNext = view.findViewById(R.id.btnNext);
        rvMySelfConsider = view.findViewById(R.id.rvMySelfConsider);

        LinearLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
        rvMySelfConsider.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvMySelfConsider.setLayoutManager(layoutManager);
        mySelfConsiderAdapter = new SlcHeightAdapter(activity, mySelfConsiderList, MySelfConsideredFragment.this);
        rvMySelfConsider.setAdapter(mySelfConsiderAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnNext) {
            if (backClickListener != null) {
                backClickListener.gobackClick(24);
            }
        }
    }

    @Override
    public void mySlcHeightClicked(int position) {
        bioPosition = position;
        for (int i = 0; i < mySelfConsiderList.size(); i++) {
            mySelfConsiderList.get(i).setBooleanSelected(false);
        }
        mySelfConsiderList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setMySelfMen(mySelfConsiderList.get(position).getString());
        Logger.e("16/12", "myself - " + mySelfConsiderList.get(position).getString());
        mySelfConsiderAdapter.notifyDataSetChanged();
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
        view.setText(R.string._24);
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
