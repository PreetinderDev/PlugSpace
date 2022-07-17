package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.fragment.AboutYouFragment;
import com.plugspace.fragment.AddPhotoFragment;
import com.plugspace.fragment.ChildrenFragment;
import com.plugspace.fragment.CompanyNameFragment;
import com.plugspace.fragment.DateOfBirthFragment;
import com.plugspace.fragment.DescribeYouEthnicityFragment;
import com.plugspace.fragment.DressSizeFragment;
import com.plugspace.fragment.EducationStatusFragment;
import com.plugspace.fragment.EngagedFragment;
import com.plugspace.fragment.EnterNameFragment;
import com.plugspace.fragment.HeightFragment;
import com.plugspace.fragment.JobTitleFragment;
import com.plugspace.fragment.MrgRangeAgeFragment;
import com.plugspace.fragment.MySelfConsideredFragment;
import com.plugspace.fragment.MrgRaceFragment;
import com.plugspace.fragment.PayBillsFragment;
import com.plugspace.fragment.RankYourSelfFragment;
import com.plugspace.fragment.RelationshipStatusFragment;
import com.plugspace.fragment.SlcBioFragment;
import com.plugspace.fragment.TattooFragment;
import com.plugspace.fragment.VerifyMobileNumberFragment;
import com.plugspace.fragment.VerifyOTPFragment;
import com.plugspace.fragment.WeightFragment;
import com.plugspace.fragment.WantChildrenFragment;
import com.plugspace.fragment.MakeOverFragment;
import com.plugspace.fragment.MeetGenderFragment;
import com.plugspace.model.LoginDataModel;

public class SignUpActivity extends BaseActivity implements Interfaces.OnGoBackClickListener {
    Activity activity;
    ImageView ivGrayBg1, ivGrayBg2, ivGrayBg3, ivGrayBg4, ivGrayBg5, ivGrayBg6, ivGrayBg7, ivGrayBg8, ivGrayBg9, ivGrayBg10;
    TextView tvNumber1, tvNumber2, tvNumber3, tvNumber4, tvNumber5, tvNumber6, tvNumber7, tvNumber8, tvNumber9, tvNumber10;
    ImageView ivBackArrow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        activity = SignUpActivity.this;
        initView();
        initClick();
    }

    private void initClick() {
        ivBackArrow.setOnClickListener(view -> onBackPressed());
    }

    private void initView() {
        ivBackArrow = findViewById(R.id.ivBackArrow);
        tvNumber1 = findViewById(R.id.tvNumber1);
        ivGrayBg1 = findViewById(R.id.ivGrayBg1);

        tvNumber2 = findViewById(R.id.tvNumber2);
        ivGrayBg2 = findViewById(R.id.ivGrayBg2);

        tvNumber3 = findViewById(R.id.tvNumber3);
        ivGrayBg3 = findViewById(R.id.ivGrayBg3);

        tvNumber4 = findViewById(R.id.tvNumber4);
        ivGrayBg4 = findViewById(R.id.ivGrayBg4);

        tvNumber5 = findViewById(R.id.tvNumber5);
        ivGrayBg5 = findViewById(R.id.ivGrayBg5);

        tvNumber6 = findViewById(R.id.tvNumber6);
        ivGrayBg6 = findViewById(R.id.ivGrayBg6);

        tvNumber7 = findViewById(R.id.tvNumber7);
        ivGrayBg7 = findViewById(R.id.ivGrayBg7);

        tvNumber8 = findViewById(R.id.tvNumber8);
        ivGrayBg8 = findViewById(R.id.ivGrayBg8);

        tvNumber9 = findViewById(R.id.tvNumber9);
        ivGrayBg9 = findViewById(R.id.ivGrayBg9);

        tvNumber10 = findViewById(R.id.tvNumber10);
        ivGrayBg10 = findViewById(R.id.ivGrayBg10);
        View view = new View(activity);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Constants.loginDataModel = new LoginDataModel(); // clear sign up all data.

              if(getIntent()!=null && getIntent().getStringExtra("from") != null){
    if(getIntent().getStringExtra("from").equalsIgnoreCase("login")){
       tvNumber3.setText((R.string._3));
       tvNumber2.setText((R.string._2));
        tvNumber1.setText(R.string._1);
        ChangeNumber(ivGrayBg1, tvNumber1);
        ChangeNumber(ivGrayBg2, tvNumber2);
        ChangeNumber(ivGrayBg3, tvNumber3);
        Constants.loginDataModel.setPhone(getIntent().getStringExtra("phn") );
        Constants.loginDataModel.setCcode(getIntent().getStringExtra("code") );
        Constants.loginDataModel.setIsVerified(1);

//        LoadFragment(new SlcBioFragment(SignUpActivity.this));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frmnt_Container, new SlcBioFragment(SignUpActivity.this))
                .commit();
    }
}else {
    ChangeNumber(ivGrayBg1, tvNumber1);
    tvNumber1.setText(R.string._1);
    getSupportFragmentManager().beginTransaction()
            .replace(R.id.frmnt_Container, new VerifyMobileNumberFragment(SignUpActivity.this, view))
            .commit();
}



    }

    @Override
    public void gobackClick(int position) {
//        BaseActivity.showProgressDialog(activity);
//
//        new Handler(Looper.getMainLooper()).postDelayed(() -> activity.runOnUiThread(() -> {
//            BaseActivity.hideProgressDialog(activity);
//        }), 1000);

        switch (position) {
            case 1:
                ChangeNumber(ivGrayBg2, tvNumber2);
                tvNumber2.setText(R.string._2);
                LoadFragment(new VerifyOTPFragment(SignUpActivity.this));
                break;

            case 2:
                ChangeNumber(ivGrayBg3, tvNumber3);
                tvNumber3.setText(R.string._3);
                LoadFragment(new SlcBioFragment(SignUpActivity.this));
                break;

            case 3:
                ChangeNumber(ivGrayBg4, tvNumber4);
                tvNumber4.setText(R.string._4);
                LoadFragment(new EnterNameFragment(SignUpActivity.this));
                break;

            case 4:
                ChangeNumber(ivGrayBg5, tvNumber5);
                tvNumber5.setText(R.string._5);
                LoadFragment(new RankYourSelfFragment(SignUpActivity.this));
                break;

            case 5:
                ChangeNumber(ivGrayBg6, tvNumber6);
                tvNumber6.setText(R.string._6);
                LoadFragment(new AddPhotoFragment(SignUpActivity.this));

                break;

            case 6:
                ChangeNumber(ivGrayBg7, tvNumber7);
                tvNumber7.setText(R.string._7);
                LoadFragment(new HeightFragment(SignUpActivity.this));
                break;

            case 7:
                ChangeNumber(ivGrayBg8, tvNumber8);
                tvNumber8.setText(R.string._8);
                LoadFragment(new WeightFragment(SignUpActivity.this));
                break;

            case 8:
                ChangeNumber(ivGrayBg9, tvNumber9);
                tvNumber9.setText(R.string._9);
                LoadFragment(new EducationStatusFragment(SignUpActivity.this));
                break;

            case 9:
                ChangeNumber(ivGrayBg10, tvNumber10);
                tvNumber10.setText(R.string._10);
                LoadFragment(new DateOfBirthFragment(SignUpActivity.this));
                break;

            case 10:
                ChangeNumber(ivGrayBg1, tvNumber1);
                tvNumber1.setText(R.string._11);
                LoadFragment(new ChildrenFragment(SignUpActivity.this));
                break;

            case 11:
                ChangeNumber(ivGrayBg2, tvNumber2);
                tvNumber2.setText(R.string._12);
                LoadFragment(new WantChildrenFragment(SignUpActivity.this));
                break;

            case 12:
                ChangeNumber(ivGrayBg3, tvNumber3);
                tvNumber3.setText(R.string._13);
                LoadFragment(new MrgRaceFragment(SignUpActivity.this));
                break;

            case 13:
                ChangeNumber(ivGrayBg4, tvNumber4);
                tvNumber4.setText(R.string._14);
                LoadFragment(new RelationshipStatusFragment(SignUpActivity.this));
                break;

            case 14:
                ChangeNumber(ivGrayBg5, tvNumber5);
                tvNumber5.setText(R.string._15);
                LoadFragment(new DescribeYouEthnicityFragment(SignUpActivity.this));
                break;

            case 15:
                ChangeNumber(ivGrayBg6, tvNumber6);
                tvNumber6.setText(R.string._16);
                LoadFragment(new CompanyNameFragment(SignUpActivity.this));
                break;

            case 16:
                ChangeNumber(ivGrayBg7, tvNumber7);
                tvNumber7.setText(R.string._17);
                LoadFragment(new JobTitleFragment(SignUpActivity.this));
                break;

            case 17:
                ChangeNumber(ivGrayBg8, tvNumber8);
                tvNumber8.setText(R.string._18);
                LoadFragment(new MakeOverFragment(SignUpActivity.this));
                break;

            case 18:
                ChangeNumber(ivGrayBg9, tvNumber9);
                tvNumber9.setText(R.string._19);
                LoadFragment(new DressSizeFragment(SignUpActivity.this));
                break;

            case 19:
                ChangeNumber(ivGrayBg10, tvNumber10);
                tvNumber10.setText(R.string._20);
                LoadFragment(new PayBillsFragment(SignUpActivity.this));
                break;

            case 20:
                ChangeNumber(ivGrayBg1, tvNumber1);
                tvNumber1.setText(R.string._21);
                LoadFragment(new EngagedFragment(SignUpActivity.this));
                break;

            case 21:
                ChangeNumber(ivGrayBg2, tvNumber2);
                tvNumber2.setText(R.string._22);
                LoadFragment(new TattooFragment(SignUpActivity.this));
                break;

            case 22:
                ChangeNumber(ivGrayBg3, tvNumber3);
                tvNumber3.setText(R.string._23);
                LoadFragment(new MrgRangeAgeFragment(SignUpActivity.this));
                break;

            case 23:
                ChangeNumber(ivGrayBg4, tvNumber4);
                tvNumber4.setText(R.string._24);
                LoadFragment(new MySelfConsideredFragment(SignUpActivity.this));
                break;

            case 24:
                ChangeNumber(ivGrayBg5, tvNumber5);
                tvNumber5.setText(R.string._25);
                LoadFragment(new AboutYouFragment(SignUpActivity.this));
                break;

            case 25:
                ChangeNumber(ivGrayBg6, tvNumber6);
                tvNumber6.setText(R.string._26);
                LoadFragment(new MeetGenderFragment(SignUpActivity.this));
                break;
        }
    }

    public void ChangeNumber(ImageView imgGray, TextView tvTextNumber) {
        ivGrayBg1.setVisibility(View.VISIBLE);
        ivGrayBg2.setVisibility(View.VISIBLE);
        ivGrayBg3.setVisibility(View.VISIBLE);
        ivGrayBg4.setVisibility(View.VISIBLE);
        ivGrayBg5.setVisibility(View.VISIBLE);
        ivGrayBg6.setVisibility(View.VISIBLE);
        ivGrayBg7.setVisibility(View.VISIBLE);
        ivGrayBg8.setVisibility(View.VISIBLE);
        ivGrayBg9.setVisibility(View.VISIBLE);
        ivGrayBg10.setVisibility(View.VISIBLE);

        tvNumber1.setVisibility(View.GONE);
        tvNumber2.setVisibility(View.GONE);
        tvNumber3.setVisibility(View.GONE);
        tvNumber4.setVisibility(View.GONE);
        tvNumber5.setVisibility(View.GONE);
        tvNumber6.setVisibility(View.GONE);
        tvNumber7.setVisibility(View.GONE);
        tvNumber8.setVisibility(View.GONE);
        tvNumber9.setVisibility(View.GONE);
        tvNumber10.setVisibility(View.GONE);

        imgGray.setVisibility(View.GONE);
        tvTextNumber.setVisibility(View.VISIBLE);
    }

    private void LoadFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frmnt_Container, fragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
