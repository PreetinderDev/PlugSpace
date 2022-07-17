package com.plugspace.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.RangeSlider;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mindorks.paracamera.Camera;
import com.plugspace.R;
import com.plugspace.activities.PreviewActivity;
import com.plugspace.activities.SettingActivity;
import com.plugspace.adapters.AddNewPhotoAdapter;
import com.plugspace.adapters.SpinnerListAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.SpacesItemDecoration;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiService;
import com.plugspace.retrofitApi.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends BaseFragment implements View.OnClickListener,
        AddNewPhotoAdapter.MyListener {

    Activity activity;
    TextView tvTitleName;
    ImageView ivImg, ivShowImg, ivCalender;
    RecyclerView rvAddNewPhoto;
    AddNewPhotoAdapter addNewPhotoAdapter;
    ArrayList<HeightModel> genderList = new ArrayList<>();
    ArrayList<HeightModel> heightList = new ArrayList<>();
    ArrayList<HeightModel> slcWeightList = new ArrayList<>();
    ArrayList<HeightModel> educationStatusList = new ArrayList<>();
    ArrayList<HeightModel> childrenList = new ArrayList<>();
    ArrayList<HeightModel> relationStatusList = new ArrayList<>();
    ArrayList<HeightModel> describeList = new ArrayList<>();
    ArrayList<HeightModel> dressSizeList = new ArrayList<>();
    ArrayList<HeightModel> engagedList = new ArrayList<>();
    ArrayList<HeightModel> tattooList = new ArrayList<>();
    ArrayList<HeightModel> mrgRaceList = new ArrayList<>();
    ArrayList<HeightModel> meetGenderList = new ArrayList<>();
    ArrayList<HeightModel> mySelfConsiderList = new ArrayList<>();
    ArrayList<MediaModel> mediaList = new ArrayList<>();
    ArrayList<String> removeMediaIDList = new ArrayList<>();
    Spinner spGender, spHeight, spWeight, spEducationStatus, spHowManyChildren, spOpenMarring, spRelationStatus,
            spDescribeEthnicity, spDressSize, spEngaged, spTattoo, spMeetGender, spMySelfConsider;
    EditText etName, etDOB, etMrgRangeAge, /*etHeight,*/
    /*etWeight,*/ etCompanyName, etJobTitle, etYourSalary, etAddTitleAbout;
    LoginDataModel loginDataModel;
    Button btnSave;
    String strGender = "", strHeight = "", strWeight = "", strEducation = "", strChildren = "", strMrgRace = "", strRelation = "", strDescribe = "",
            strDress = "", strEngaged = "", strTattoo = "", strMySelf = "", strMeetGender = "",
            strMediaID = "", strRemoveMediaID = "";
    private Camera editProfileCamera;
    File imgFile;
    private final int PICK_IMAGE_GALLERY = 300;
    int position = 0;
    public static Interfaces.OnEditProfileSaveClickListener onEditProfileSaveClickListener;
    private String ageRangeMarriage = "";

    public static void setOnEditProfileSaveClickListener(Interfaces.OnEditProfileSaveClickListener onEditProfileSaveClickListener) {
        EditProfileFragment.onEditProfileSaveClickListener = onEditProfileSaveClickListener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        activity = getActivity();
        loginDataModel = Preferences.GetLoginDetails();
        initToolBar(view);
        initFillData();
        initView(view);
        GetIntentData();
        initClick();
        return view;
    }

    private void GetIntentData() {
        if (loginDataModel != null) {
            etName.setText(loginDataModel.getName());
            etDOB.setText(loginDataModel.getDob());
            if (!Utils.isValidationEmpty(loginDataModel.getAgeRangeMarriage())) {
                etMrgRangeAge.setText(loginDataModel.getAgeRangeMarriage());
            } else {
                etMrgRangeAge.setText("18-26");
            }
//            etHeight.setText(loginDataModel.getHeight());
//            etWeight.setText(loginDataModel.getWeight());
            etCompanyName.setText(loginDataModel.getCompanyName());
            etJobTitle.setText(loginDataModel.getJobTitle());
            etYourSalary.setText(loginDataModel.getMakeOver());
            etAddTitleAbout.setText(loginDataModel.getAboutYou());
//            Logger.e("5/1 - ID - ", loginDataModel.getUserId());

            for (int i = 0; i < genderList.size(); i++) {
                String gender = genderList.get(i).getString();
                if (gender.equalsIgnoreCase(loginDataModel.getGender())) {
                    spGender.setSelection(i);
                }
            }

            Logger.d("test_strHeight_1", loginDataModel.getHeight());
            for (int i = 0; i < heightList.size(); i++) {
                String height = heightList.get(i).getString().trim();
                if (height.equalsIgnoreCase(loginDataModel.getHeight())) {
                    spHeight.setSelection(i);
                    Logger.d("test_strHeight_2", height);
                }
            }

            for (int i = 0; i < slcWeightList.size(); i++) {
                String weight = slcWeightList.get(i).getString().trim();
                if (weight.equalsIgnoreCase(loginDataModel.getWeight())) {
                    spWeight.setSelection(i);
                }
            }

            for (int i = 0; i < educationStatusList.size(); i++) {
                String education = educationStatusList.get(i).getString();
                if (education.equalsIgnoreCase(loginDataModel.getEducationStatus())) {
                    spEducationStatus.setSelection(i);
                }
            }

            for (int i = 0; i < childrenList.size(); i++) {
                String children = childrenList.get(i).getString();
                if (children.equalsIgnoreCase(loginDataModel.getChildren())) {
                    spHowManyChildren.setSelection(i);
                }
            }

            for (int i = 0; i < mrgRaceList.size(); i++) {
                String mrgRace = mrgRaceList.get(i).getString();
                if (mrgRace.equalsIgnoreCase(loginDataModel.getMarringRace())) {
                    spOpenMarring.setSelection(i);
                }
            }

            for (int i = 0; i < relationStatusList.size(); i++) {
                String relation = relationStatusList.get(i).getString();
                if (relation.equalsIgnoreCase(loginDataModel.getRelationshipStatus())) {
                    spRelationStatus.setSelection(i);
                }
            }

            for (int i = 0; i < describeList.size(); i++) {
                String ethnicity = describeList.get(i).getString();
                if (ethnicity.equalsIgnoreCase(loginDataModel.getEthinicity())) {
                    spDescribeEthnicity.setSelection(i);
                }
            }

            for (int i = 0; i < dressSizeList.size(); i++) {
                String dressSize = dressSizeList.get(i).getString();
                if (dressSize.equalsIgnoreCase(loginDataModel.getDressSize())) {
                    spDressSize.setSelection(i);
                }
            }

            for (int i = 0; i < engagedList.size(); i++) {
                String engaged = engagedList.get(i).getString();
                if (engaged.equalsIgnoreCase(loginDataModel.getTimesOfEngaged())) {
                    spEngaged.setSelection(i);
                }
            }

            for (int i = 0; i < tattooList.size(); i++) {
                String tattoo = tattooList.get(i).getString();
                if (tattoo.equalsIgnoreCase(loginDataModel.getYourBodyTatto())) {
                    spTattoo.setSelection(i);
                }
            }

            for (int i = 0; i < mySelfConsiderList.size(); i++) {
                String mySelfConsider = mySelfConsiderList.get(i).getString();
                if (mySelfConsider.equalsIgnoreCase(loginDataModel.getMySelfMen())) {
                    spMySelfConsider.setSelection(i);
                }
            }

            for (int i = 0; i < meetGenderList.size(); i++) {
                String meetGender = meetGenderList.get(i).getString();
                if (meetGender.equalsIgnoreCase(loginDataModel.getNiceMeet())) {
                    spMeetGender.setSelection(i);
                }
            }


            mediaList = new ArrayList<>();
            mediaList.add(new MediaModel());
            mediaList.add(new MediaModel());
            mediaList.add(new MediaModel());
            mediaList.add(new MediaModel());
            mediaList.add(new MediaModel());
            mediaList.add(new MediaModel());
            mediaList.add(new MediaModel());

            if (loginDataModel.getMediaDetail() != null && loginDataModel.getMediaDetail().size() > 0) {
//                if (loginDataModel.getMediaDetail().size() > 5) {
                if (loginDataModel.getMediaDetail().size() > 7) {
//                    for (int i = 0; i < 5; i++) {
                    for (int i = 0; i < 7; i++) {
                        if (loginDataModel.getMediaDetail().get(i).getType() != null
                                && loginDataModel.getMediaDetail().get(i).getType().equalsIgnoreCase(Constants.PROFILE)) {
                            MediaModel model = new MediaModel();
                            model.setProfile(loginDataModel.getMediaDetail().get(i).getProfile());
                            model.setMediaId(loginDataModel.getMediaDetail().get(i).getMediaId());
                            mediaList.set(i, model);
                        }

                    }
                } else {
                    for (int i = 0; i < loginDataModel.getMediaDetail().size(); i++) {
                        if (loginDataModel.getMediaDetail().get(i).getType() != null
                                && loginDataModel.getMediaDetail().get(i).getType().equalsIgnoreCase(Constants.PROFILE)) {
                            MediaModel model = new MediaModel();
                            model.setProfile(loginDataModel.getMediaDetail().get(i).getProfile());
                            model.setMediaId(loginDataModel.getMediaDetail().get(i).getMediaId());
                            mediaList.set(i, model);
                        }

                    }
                }

            }
            addNewPhotoAdapter = new AddNewPhotoAdapter(activity, mediaList, EditProfileFragment.this);

            rvAddNewPhoto.setAdapter(addNewPhotoAdapter);
        }

    }

    private void initClick() {
        ivImg.setOnClickListener(this);
        ivShowImg.setOnClickListener(this);
        ivCalender.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strGender = genderList.get(i).getString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strHeight = heightList.get(i).getString().trim();
                Logger.d("test_strHeight", strHeight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strWeight = slcWeightList.get(i).getString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spEducationStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strEducation = educationStatusList.get(i).getString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spHowManyChildren.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strChildren = childrenList.get(i).getString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spOpenMarring.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strMrgRace = mrgRaceList.get(i).getString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spRelationStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strRelation = relationStatusList.get(i).getString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDescribeEthnicity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strDescribe = describeList.get(i).getString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spDressSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strDress = dressSizeList.get(i).getString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spEngaged.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strEngaged = engagedList.get(i).getString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTattoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strTattoo = tattooList.get(i).getString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spMySelfConsider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strMySelf = mySelfConsiderList.get(i).getString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spMeetGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strMeetGender = meetGenderList.get(i).getString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etMrgRangeAge.setOnClickListener(v -> dialogMrgAgeRange());
    }

    private void initFillData() {

        genderList.add(new HeightModel(getString(R.string.biologically_female)));
        genderList.add(new HeightModel(getString(R.string.biologically_male)));
        genderList.add(new HeightModel(getString(R.string.trans_male)));
        genderList.add(new HeightModel(getString(R.string.trans_female)));
        genderList.add(new HeightModel(getString(R.string.others)));


        heightList.clear();
        heightList.add(new HeightModel(getString(R.string._3_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_8_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_9_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_10_ft), false));
        heightList.add(new HeightModel(getString(R.string._3_11_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_8_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_9_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_10_ft), false));
        heightList.add(new HeightModel(getString(R.string._4_11_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_8_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_9_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_10_ft), false));
        heightList.add(new HeightModel(getString(R.string._5_11_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_8_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_9_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_10_ft), false));
        heightList.add(new HeightModel(getString(R.string._6_11_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_0_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_1_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_2_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_3_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_4_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_5_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_6_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_7_ft), false));
        heightList.add(new HeightModel(getString(R.string._7_8_ft), false));
        heightList.get(Constants.SelectedPosition).setBooleanSelected(true);

        slcWeightList.clear();
        for (int i = 60; i <= 400; i++) {
            slcWeightList.add(new HeightModel(String.valueOf(i), false));
        }
        slcWeightList.get(Constants.SelectedPosition).setBooleanSelected(true);

        educationStatusList.add(new HeightModel(getString(R.string.high_school)));
        educationStatusList.add(new HeightModel(getString(R.string.bachelor)));
        educationStatusList.add(new HeightModel(getString(R.string.vocational_school)));
        educationStatusList.add(new HeightModel(getString(R.string.phd)));
        educationStatusList.add(new HeightModel(getString(R.string.some_collage)));
        educationStatusList.add(new HeightModel(getString(R.string.advanced_degree)));
        educationStatusList.add(new HeightModel(getString(R.string.associate_degree)));
        educationStatusList.add(new HeightModel(getString(R.string.bma)));

        for (int i = 0; i <= 5; i++) {
            childrenList.add(new HeightModel(String.valueOf(i)));
        }

        mrgRaceList.add(new HeightModel(getString(R.string.yes)));
        mrgRaceList.add(new HeightModel(getString(R.string.no)));

        relationStatusList.add(new HeightModel(getString(R.string.definitely_single)));
        relationStatusList.add(new HeightModel(getString(R.string.separated)));
        relationStatusList.add(new HeightModel(getString(R.string.divorced)));
        relationStatusList.add(new HeightModel(getString(R.string.windowed)));

        describeList.add(new HeightModel(getString(R.string.india)));
        describeList.add(new HeightModel(getString(R.string.hispanic_latin)));
        describeList.add(new HeightModel(getString(R.string.pacific_islander)));
        describeList.add(new HeightModel(getString(R.string.black)));
        describeList.add(new HeightModel(getString(R.string.asian)));
        describeList.add(new HeightModel(getString(R.string.middle_eastern)));
        describeList.add(new HeightModel(getString(R.string.native_american)));
        describeList.add(new HeightModel(getString(R.string.caucasian)));

        for (int i = 1; i <= 26; i++) {
            dressSizeList.add(new HeightModel(String.valueOf(i)));
        }

        for (int i = 0; i <= 5; i++) {
            engagedList.add(new HeightModel(String.valueOf(i)));
        }

        for (int i = 0; i <= 5; i++) {
            tattooList.add(new HeightModel(String.valueOf(i)));
        }

        meetGenderList.add(new HeightModel(getString(R.string.woman)));
        meetGenderList.add(new HeightModel(getString(R.string.man)));
        meetGenderList.add(new HeightModel(getString(R.string.all)));

        mySelfConsiderList.add(new HeightModel(getString(R.string.feminine)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.beta)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.professional)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.gamma)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.sigma)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.regular_guy)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.delta)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.alpha)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.standard_guy)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.celebrity)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.middle_class)));
        mySelfConsiderList.add(new HeightModel(getString(R.string.others)));
    }

    private void initToolBar(View view) {
        tvTitleName = view.findViewById(R.id.tvTitleName);
        ivImg = view.findViewById(R.id.ivImg);
        ivShowImg = view.findViewById(R.id.ivShowImg);

        tvTitleName.setText(R.string.edit_profile);
        ivShowImg.setVisibility(View.VISIBLE);
        ivImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings_with_bg));
    }

    private void initView(View view) {
        btnSave = view.findViewById(R.id.btnSave);
        etName = view.findViewById(R.id.etName);
        etDOB = view.findViewById(R.id.etDOB);
        etMrgRangeAge = view.findViewById(R.id.etMrgRangeAge);
//        etHeight = view.findViewById(R.id.etHeight);
//        etWeight = view.findViewById(R.id.etWeight);
        etCompanyName = view.findViewById(R.id.etCompanyName);
        etJobTitle = view.findViewById(R.id.etJobTitle);
        etYourSalary = view.findViewById(R.id.etYourSalary);
        etAddTitleAbout = view.findViewById(R.id.etAddTitleAbout);
        ivCalender = view.findViewById(R.id.ivCalender);

        rvAddNewPhoto = view.findViewById(R.id.rvAddNewPhoto);
        LinearLayoutManager layoutManager = new GridLayoutManager(activity, 3);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._1sdp);
        rvAddNewPhoto.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvAddNewPhoto.setLayoutManager(layoutManager);

        spGender = view.findViewById(R.id.spGender);
        SpinnerListAdapter aa = new SpinnerListAdapter(activity, genderList);
        spGender.setAdapter(aa);

        spHeight = view.findViewById(R.id.spHeight);
        SpinnerListAdapter mAdapterHeight = new SpinnerListAdapter(activity, heightList);
        spHeight.setAdapter(mAdapterHeight);

        spWeight = view.findViewById(R.id.spWeight);
        SpinnerListAdapter mAdapterWeight = new SpinnerListAdapter(activity, slcWeightList);
        spWeight.setAdapter(mAdapterWeight);

        spEducationStatus = view.findViewById(R.id.spEducationStatus);
        SpinnerListAdapter educationStatusAdapter = new SpinnerListAdapter(activity, educationStatusList);
        spEducationStatus.setAdapter(educationStatusAdapter);

        spHowManyChildren = view.findViewById(R.id.spHowManyChildren);
        SpinnerListAdapter howManyChildrenAdapter = new SpinnerListAdapter(activity, childrenList);
        spHowManyChildren.setAdapter(howManyChildrenAdapter);

        spOpenMarring = view.findViewById(R.id.spOpenMarring);
        SpinnerListAdapter openMarringAdapter = new SpinnerListAdapter(activity, mrgRaceList);
        spOpenMarring.setAdapter(openMarringAdapter);

        spRelationStatus = view.findViewById(R.id.spRelationStatus);
        SpinnerListAdapter relationStatusAdapter = new SpinnerListAdapter(activity, relationStatusList);
        spRelationStatus.setAdapter(relationStatusAdapter);

        spDescribeEthnicity = view.findViewById(R.id.spDescribeEthnicity);
        SpinnerListAdapter describeEthnicityAdapter = new SpinnerListAdapter(activity, describeList);
        spDescribeEthnicity.setAdapter(describeEthnicityAdapter);

        spDressSize = view.findViewById(R.id.spDressSize);
        SpinnerListAdapter dressSizeAdapter = new SpinnerListAdapter(activity, dressSizeList);
        spDressSize.setAdapter(dressSizeAdapter);

        spEngaged = view.findViewById(R.id.spEngaged);
        SpinnerListAdapter engagedAdapter = new SpinnerListAdapter(activity, engagedList);
        spEngaged.setAdapter(engagedAdapter);

        spTattoo = view.findViewById(R.id.spTattoo);
        SpinnerListAdapter tattooAdapter = new SpinnerListAdapter(activity, tattooList);
        spTattoo.setAdapter(tattooAdapter);

        spMeetGender = view.findViewById(R.id.spMeetGender);
        SpinnerListAdapter meetGenderAdapter = new SpinnerListAdapter(activity, meetGenderList);
        spMeetGender.setAdapter(meetGenderAdapter);

        spMySelfConsider = view.findViewById(R.id.spMySelfConsider);
        SpinnerListAdapter mySelfConsider = new SpinnerListAdapter(activity, mySelfConsiderList);
        spMySelfConsider.setAdapter(mySelfConsider);
    }

    public boolean isValid() {

        boolean isEmptyPhoto = true;
        for (int i = 0; i < mediaList.size(); i++) {
            MediaModel mediaModel = mediaList.get(i);
            if (mediaModel != null && !Utils.isValidationEmpty(mediaModel.getProfile())) {
                isEmptyPhoto = false;
                break;
            }
        }

        if (Utils.isValidationEmpty(etName.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.lbl_enter_name));
            return false;

        } else if (isEmptyPhoto) {
            Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                    activity.getResources().getString(R.string.lbl_select_photo));
            return false;

        } else if (Utils.isValidationEmpty(spGender.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_gender));
            return false;

        } else if (Utils.isValidationEmpty(strHeight)) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_height));
            return false;

//        } else if (Utils.isValidationEmpty(etHeight.getText().toString().trim())) {
//            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_height));
//            return false;
        } else if (Utils.isValidationEmpty(strWeight)) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_weight));
            return false;
//        } else if (Utils.isValidationEmpty(etWeight.getText().toString().trim())) {
//            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_weight));
//            return false;

        } else if (Utils.isValidationEmpty(spEducationStatus.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_education));
            return false;

        } else if (Utils.isValidationEmpty(etDOB.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_dob));
            return false;

        } else if (Utils.isValidationEmpty(spHowManyChildren.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_children));
            return false;

        } else if (Utils.isValidationEmpty(spOpenMarring.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_marrying_race));
            return false;

        } else if (Utils.isValidationEmpty(spRelationStatus.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_relation));
            return false;

        } else if (Utils.isValidationEmpty(spDescribeEthnicity.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_ethnicity));
            return false;

//        } else if (Utils.isValidationEmpty(etCompanyName.getText().toString().trim())) {
//            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_company_name));
//            return false;

      /*  } else if (Utils.isValidationEmpty(etJobTitle.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_job_title));
            return false;*/

//        } else if (Utils.isValidationEmpty(etYourSalary.getText().toString().trim())) {
//            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_make_over));
//            return false;

        /*} else if (Utils.isValidationEmpty(spDressSize.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_dress));
            return false;

        } else if (Utils.isValidationEmpty(spEngaged.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_engaged));//  EDIT: Ask Engaged
            return false;

        } else if (Utils.isValidationEmpty(spTattoo.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_tattoo));//  EDIT: Ask Tattoo
            return false;

        } else if (Utils.isValidationEmpty(etMrgRangeAge.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_enter_mrg_age));//  EDIT: Ask MrgAge
            return false;

        } else if (Utils.isValidationEmpty(spMySelfConsider.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_consider_myself));//  EDIT: Ask considerSelf
            return false;

        } else if (Utils.isValidationEmpty(spMeetGender.toString())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.alert_select_meet_gender));//  EDIT: Ask Gender
            return false;

        } else if (Utils.isValidationEmpty(etAddTitleAbout.getText().toString().trim())) {
            Utils.showAlert(activity, getString(R.string.app_name), getString(R.string.lbl_enter_about_us)); //  EDIT: Ask about us
            return false;*/

        } else {
            if (Utils.isNetworkAvailable(activity, true, false)) {
                UpdateProfile();
            }
        }
        return true;
    }

    private void UpdateProfile() {
        showProgressDialog(activity);
        ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);
        MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[]{};
        if (mediaList != null && mediaList.size() > 0) {

            surveyImagesParts = new MultipartBody.Part[mediaList.size()];

            for (int i = 0; i < mediaList.size(); i++) {
                if (mediaList.get(i) != null && mediaList.get(i).getProfile() != null) {

                    File file = new File(mediaList.get(i).getProfile());
                    if (file.exists()) {
                        RequestBody surveyBody = RequestBody.create(file, MediaType.parse("image/*"));
                        surveyImagesParts[i] = MultipartBody.Part.createFormData("profile[]",
                                file.getName(),
                                surveyBody);
                    }
                }
            }
        }

        Call<ObjectResponseModel> call = service.updateProfile(
                Utils.convertValueToRequestBody(loginDataModel.getUserId()),
                Utils.convertValueToRequestBody(etName.getText().toString().trim()),
                Utils.convertValueToRequestBody(loginDataModel.getCcode()),
                Utils.convertValueToRequestBody(loginDataModel.getPhone()),
                Utils.convertValueToRequestBody(strGender),
                Utils.convertValueToRequestBody(loginDataModel.getRank()),
                Utils.convertValueToRequestBody(loginDataModel.getIsGeoLocation()),
//                Utils.convertValueToRequestBody(etHeight.getText().toString().trim()),
                Utils.convertValueToRequestBody(strHeight),
//                Utils.convertValueToRequestBody(etWeight.getText().toString().trim()),
                Utils.convertValueToRequestBody(strWeight),
                Utils.convertValueToRequestBody(strEducation),
                Utils.convertValueToRequestBody(etDOB.getText().toString().trim()),
                Utils.convertValueToRequestBody(strChildren),
                Utils.convertValueToRequestBody(loginDataModel.getWantChildrens()),
                Utils.convertValueToRequestBody(strMrgRace),
                Utils.convertValueToRequestBody(strRelation),
                Utils.convertValueToRequestBody(strDescribe),
                Utils.convertValueToRequestBody(etCompanyName.getText().toString().trim()),
                Utils.convertValueToRequestBody(etJobTitle.getText().toString().trim()),
                Utils.convertValueToRequestBody(etYourSalary.getText().toString().trim()),
                Utils.convertValueToRequestBody(strDress),
                Utils.convertValueToRequestBody(loginDataModel.getSigniatBills()),
                Utils.convertValueToRequestBody(strEngaged),
                Utils.convertValueToRequestBody(strTattoo),
                Utils.convertValueToRequestBody(etMrgRangeAge.getText().toString().trim()),
                Utils.convertValueToRequestBody(strMySelf),
                Utils.convertValueToRequestBody(etAddTitleAbout.getText().toString().trim()),
                Utils.convertValueToRequestBody(strMeetGender),
                Utils.convertValueToRequestBody(Constants.deviceType),
                Utils.convertValueToRequestBody(loginDataModel.getToken()),
                Utils.convertValueToRequestBody(loginDataModel.getLocation()),
                Utils.convertValueToRequestBody(strRemoveMediaID),
                surveyImagesParts);

        call.enqueue(new Callback<ObjectResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                   @NonNull Response<ObjectResponseModel> response) {
                hideProgressDialog(activity);

                if (response.isSuccessful()) {
                    ObjectResponseModel model = response.body();
                    if (model != null) {
                        if (model.getResponseCode() == 1) {
                            LoginDataModel dataModel = model.getData();
                            if (dataModel != null) {
                                Preferences.SetLoginDetails(dataModel);


                                if (onEditProfileSaveClickListener != null) {
                                    onEditProfileSaveClickListener.editProfileSaveClick();
                                }

                                Utils.showToast(activity, model.getResponseMsg());
                            }
                        } else {
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }
                    }
                } else {
                    Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
                Logger.d("test_onFailure_updateProfile", t.getMessage());
                hideProgressDialog(activity);
                if (!Utils.isNetworkAvailable(activity, true, false)) {
                    Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                            activity.getResources().getString(R.string.error_network));
                } else {
                    Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                            activity.getResources().getString(R.string.technical_problem));
                }
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivImg:
                startActivity(new Intent(activity, SettingActivity.class));
                break;

            case R.id.ivShowImg:
                startActivity(new Intent(activity, PreviewActivity.class));
                break;

            case R.id.ivCalender:
                Calendar c = Calendar.getInstance();
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                int year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, (view1, year1, month1, dayOfMonth) -> {
                    int months = month1 + 1;
                    String strDMY = year1 + "-" + months + "-" + dayOfMonth;
                    etDOB.setText(strDMY);
                }, year, month, day);
                datePickerDialog.show();
                break;

            case R.id.btnSave:
                isValid();
                break;
        }
    }

    @Override
    public void addPhotoClicked(int positions, String identify, String mediaId) {
        if (identify.equalsIgnoreCase(Constants.ADD_PHOTO)) {
//            position = 0;
            position = positions;
            checkStoragePermission();
        } else if (identify.equalsIgnoreCase(Constants.REMOVE_PHOTO)) {

            mediaList.remove(positions);
            strMediaID = mediaId;
            removeMediaIDList.add(strMediaID);

            for (String name : removeMediaIDList) {
                if (strRemoveMediaID.length() == 0) {
                    strRemoveMediaID = name;
                } else {
                    strRemoveMediaID = strRemoveMediaID + "," + name;
                }
            }
            mediaList.add(new MediaModel());
            addNewPhotoAdapter.notifyDataSetChanged();
        }

    }

    private void checkStoragePermission() {
        Dexter.withContext(activity)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            OpenPhotoDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void OpenPhotoDialog() {
        Constants.IS_HOME = false;
        Utils.showCustomDialog(activity,
                activity.getResources().getString(R.string.app_name),
                activity.getString(R.string.msg_select_select_option),
                activity.getString(R.string.gallery),
                (dialog, which) -> {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activity.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                }, activity.getString(R.string.camera),
                (dialog, which) -> {
                    editProfileCamera = new Camera.Builder()
                            .resetToCorrectOrientation(true)
                            .setTakePhotoRequestCode(1)
                            .setImageFormat(Camera.IMAGE_JPEG)
                            .build(EditProfileFragment.this);
                    try {
                        editProfileCamera.takePicture();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, true, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Camera.REQUEST_TAKE_PHOTO && editProfileCamera != null) {
                Bitmap bitmap = editProfileCamera.getCameraBitmap();
                if (bitmap != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/Camera";
                    File dir = new File(file_path);
                    if (!dir.exists())
                        dir.mkdirs();
                    String format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
                    File file = new File(dir, format + ".png");
                    FileOutputStream fo;
                    try {
                        fo = new FileOutputStream(file);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imgFile = file;

                    if (imgFile.exists()) {
                        if (mediaList != null && mediaList.get(position) != null && mediaList.get(position).getProfile() != null) {
                            mediaList.get(position).setProfile(imgFile.getPath());
                        } else {
                            MediaModel model = new MediaModel();
                            model.setProfile(imgFile.getPath());
                            mediaList.set(position, model);
                        }
                    }
                    addNewPhotoAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(activity, "Picture not taken!", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PICK_IMAGE_GALLERY) {
                Uri selectedImage = data.getData();
                try {
                    imgFile = FileUtils.getFile(activity, selectedImage);

                    if (imgFile != null && imgFile.exists()) {
                        if (mediaList != null && mediaList.get(position) != null && mediaList.get(position).getProfile() != null) {
                            mediaList.get(position).setProfile(imgFile.getPath());
                        } else {
                            MediaModel model = new MediaModel();
                            model.setProfile(imgFile.getPath());
                            mediaList.set(position, model);
                        }

//                        MediaModel model = new MediaModel();
//                        if (mediaList != null && mediaList.size() > position) {
//                            model = mediaList.get(position);
//                            model.setProfile(imgFile.getPath());
//                            mediaList.set(position, model);
//                        } else {
//                            model.setProfile(imgFile.getPath());
//                            mediaList.add(model);
//                        }
                        addNewPhotoAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void dialogMrgAgeRange() {

        Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_mrg_rage_age);
        dialog.setTitle("");


        RangeSlider rsRangeMrgAge = dialog.findViewById(R.id.rsRangeMrgAge);
        rsRangeMrgAge.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            int valueFrom = Math.round(values.get(0));
            int valueTo = Math.round(values.get(1));

            ageRangeMarriage = valueFrom + "-" + valueTo;

        });

        ageRangeMarriage = etMrgRangeAge.getText().toString().trim();
        if (!Utils.isValidationEmpty(ageRangeMarriage)) {
            String[] lstMrgRangeAge = ageRangeMarriage.split("-");
            if (lstMrgRangeAge.length > 0) {
                float valueFrom = 0, valueTo = 0;
                String strValueFrom = lstMrgRangeAge[0];
                if (!Utils.isValidationEmpty(strValueFrom)) {
                    valueFrom = Float.parseFloat(lstMrgRangeAge[0]);
                }
                if (lstMrgRangeAge.length > 1) {
                    String strValueTo = lstMrgRangeAge[1];
                    if (!Utils.isValidationEmpty(strValueTo)) {
                        valueTo = Float.parseFloat(lstMrgRangeAge[1]);
                    }
                }
                rsRangeMrgAge.setValues(valueFrom, valueTo);
            } else {
                rsRangeMrgAge.setValues(18f, 26f);
            }

        } else {
            rsRangeMrgAge.setValues(18f, 26f);
        }

        dialog.findViewById(R.id.btnDone).setOnClickListener(v -> {
            etMrgRangeAge.setText(ageRangeMarriage);
            dialog.dismiss();
        });

        dialog.show();
    }
}
