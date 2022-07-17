package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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

public class DescribeYouEthnicityFragment extends BaseFragment implements View.OnClickListener, SlcHeightAdapter.MyListener {
    Interfaces.OnGoBackClickListener backClickListener;
    Activity activity;
    Button btnNext;
    RecyclerView rvDescribeYouEthnicity;
    SlcHeightAdapter describeAdapter;
    ArrayList<HeightModel> describeList = new ArrayList<>();
    TextView tvOthers;
    CardView cvOthers;
    LinearLayout llOthers;
    int desPosition = 0, position = 10;

    public DescribeYouEthnicityFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_describe_you_ethnicity, container, false);
        activity = getActivity();
        initFillData();
        initView(view);
        initClick(view);
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getEducationStatus()) &&
                Constants.DESCRIBE_POSITION == 8){
            for (int i = 0; i < describeList.size(); i++) {
                describeList.get(i).setBooleanSelected(false);
            }
            slcDescribeYouEthnicity(tvOthers, llOthers);
            Constants.loginDataModel.setEthinicity(Constants.loginDataModel.getEthinicity());
            describeAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcEthnicity 2 - >", Constants.loginDataModel.getEthinicity());
        } else if (!Utils.isValidationEmpty(Constants.loginDataModel.getEthinicity())) {
            for (int i = 0; i < describeList.size(); i++) {
                describeList.get(i).setBooleanSelected(false);
            }
            describeList.get(desPosition).setBooleanSelected(true);
            Constants.loginDataModel.setEthinicity(Constants.loginDataModel.getEthinicity());
            describeAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcEthnicity - >", Constants.loginDataModel.getEthinicity());
        }
        return view;
    }

    private void initFillData() {
        describeList.clear();
        describeList.add(new HeightModel(getString(R.string.india), false));
        describeList.add(new HeightModel(getString(R.string.hispanic_latin), false));
        describeList.add(new HeightModel(getString(R.string.pacific_islander), false));
        describeList.add(new HeightModel(getString(R.string.black), false));
        describeList.add(new HeightModel(getString(R.string.asian), false));
        describeList.add(new HeightModel(getString(R.string.middle_eastern), false));
        describeList.add(new HeightModel(getString(R.string.native_american), false));
        describeList.add(new HeightModel(getString(R.string.caucasian), false));
        describeList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setEthinicity(describeList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "Ethnicity 2 - " + describeList.get(Constants.SelectedPosition).getString());
    }

    private void initClick(View view) {
        btnNext.setOnClickListener(this);
        cvOthers.setOnClickListener(this);

    }

    private void initView(View view) {
        btnNext = view.findViewById(R.id.btnNext);
        rvDescribeYouEthnicity = view.findViewById(R.id.rvDescribeYouEthnicity);

        cvOthers = view.findViewById(R.id.cvOthers);
        llOthers = view.findViewById(R.id.llOthers);
        tvOthers = view.findViewById(R.id.tvOthers);

        LinearLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
        rvDescribeYouEthnicity.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvDescribeYouEthnicity.setLayoutManager(layoutManager);
        describeAdapter = new SlcHeightAdapter(activity, describeList, DescribeYouEthnicityFragment.this);
        rvDescribeYouEthnicity.setAdapter(describeAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                if (backClickListener != null) {
                    backClickListener.gobackClick(15);
                }
                break;

            case R.id.cvOthers:
                for (int i = 0; i < describeList.size(); i++) {
                    describeList.get(i).setBooleanSelected(false);
                }
                Constants.DESCRIBE_POSITION  = 8;
                slcDescribeYouEthnicity(tvOthers, llOthers);
                Constants.loginDataModel.setEthinicity(tvOthers.getText().toString().trim());
                describeAdapter.notifyDataSetChanged();
                Logger.e("16/12", "Ethnicity 3 - " + Constants.loginDataModel.getEducationStatus());
                break;
        }
    }

    public void slcDescribeYouEthnicity(TextView view, LinearLayout linearLayout) {
        tvOthers.setTextColor(getResources().getColor(R.color.colorBlack));
        llOthers.setBackground(getResources().getDrawable(R.drawable.white_10sdp));

        view.setTextColor(getResources().getColor(R.color.colorOrange));
        linearLayout.setBackground(getResources().getDrawable(R.drawable.white_bg_10sdp_with_orange_border));


    }

    @Override
    public void mySlcHeightClicked(int position) {
        Constants.DESCRIBE_POSITION = position;
        desPosition = position;
        for (int i = 0; i < describeList.size(); i++) {
            describeList.get(i).setBooleanSelected(false);
            slcDescribeYouEthnicity(tvOthers, llOthers);
        }
        describeList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setEthinicity(describeList.get(position).getString());
        Logger.e("16/12", "Ethnicity - " + describeList.get(position).getString());
        tvOthers.setTextColor(getResources().getColor(R.color.colorBlack));
        llOthers.setBackground(getResources().getDrawable(R.drawable.white_10sdp));
        describeAdapter.notifyDataSetChanged();
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
        TextView view = activity.findViewById(R.id.tvNumber5);
        view.setText(R.string._15);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg5), view);
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
