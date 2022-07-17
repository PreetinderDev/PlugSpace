package com.plugspace.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.plugspace.R;
import com.plugspace.adapters.ChatAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.HomeDetailsResponseModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.Message;
import com.plugspace.model.SavedProfileModel;
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatFragment extends BaseFragment /*implements View.OnClickListener*/ {
    private Activity activity;
    //    TabLayout tabLayout;
    private int totalTabs = 4;
    private int selectedPosition = 1;
    private ViewPager viewPager;
    private ChatAdapter mAdapter;
    //    private EditText etSearch;
//    ImageView ivSearch, ivSearchName;
//    ViewGroup rlSearchTransitions;
    //    ArrayList<LoginDataModel> chatsList = new ArrayList<>();
    private ArrayList<LoginDataModel> lstLike = new ArrayList<>();
    private ArrayList<LoginDataModel> lstLikeMain = new ArrayList<>();
    private ArrayList<LoginDataModel> lstViewProfile = new ArrayList<>();
    private ArrayList<LoginDataModel> lstViewProfileMain = new ArrayList<>();


    private ArrayList<SavedProfileModel> lstSavedProfile = new ArrayList<>();
    private ArrayList<SavedProfileModel> lstSavedProfileMain = new ArrayList<>();

    private ArrayList<Message> savedProfileList = new ArrayList<>();
    //    private String strUserId = ""/*, strSearchUserName = ""*/;
    private LinearLayout llHideSearch, llLiked, llViews, llMatchChat, llSaveProfile;
    private TextView tvLiked, tvViews, tvMatchChat, tvSaveProfile;
    private View viewLiked, viewViews, viewMatchChat, viewSaveProfile;
    private RelativeLayout rlShowSearch;
    private EditText etSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        activity = getActivity();
        initView(view);
        initClick();
        processCustomTab(true, 0);
        searchViewProcess(false, true);
        doAPIGetChatList();
        return view;
    }

    private void searchViewProcess(boolean isShowSearch, boolean isCallAPI) {

        if (isShowSearch) {
            rlShowSearch.setVisibility(View.VISIBLE);
            llHideSearch.setVisibility(View.GONE);
            etSearch.requestFocus();
            Utils.showKeyBoard(activity, etSearch);
        } else {
            etSearch.setText("");
            Utils.hideKeyBoard(activity, etSearch);
            rlShowSearch.setVisibility(View.GONE);
            llHideSearch.setVisibility(View.VISIBLE);

//            if (isCallAPI) {
//                doAPIGetChatList();
//            }
        }
    }

    private void processCustomTab(boolean isSetCurrentItem, int position) {
        selectedPosition = position;

        if (isSetCurrentItem && viewPager != null && viewPager.getChildCount() >= position) {
            viewPager.setCurrentItem(position);
        }

        tvLiked.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        tvViews.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        tvMatchChat.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        tvSaveProfile.setTextColor(activity.getResources().getColor(R.color.colorBlack));

        viewLiked.setBackgroundColor(0);
        viewViews.setBackgroundColor(0);
        viewMatchChat.setBackgroundColor(0);
        viewSaveProfile.setBackgroundColor(0);


        if (position == 0) {
            tvLiked.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            viewLiked.setBackgroundColor(activity.getResources().getColor(R.color.colorOrange));
        } else if (position == 1) {
            tvViews.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            viewViews.setBackgroundColor(activity.getResources().getColor(R.color.colorOrange));
        } else if (position == 2) {
            tvMatchChat.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            viewMatchChat.setBackgroundColor(activity.getResources().getColor(R.color.colorOrange));
        } else if (position == 3) {
            tvSaveProfile.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            viewSaveProfile.setBackgroundColor(activity.getResources().getColor(R.color.colorOrange));
        }
    }

    private void doAPIGetChatList() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Utils.hideKeyBoard(activity, etSearch);

            setTabLayoutPositionWise(0, "");
            setTabLayoutPositionWise(1, "");
            setTabLayoutPositionWise(2, "");
            setTabLayoutPositionWise(3, "");


            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);
//            LoginDataModel dataModel = Preferences.GetLoginDetails();
//            if (dataModel != null) {
//                strUserId = dataModel.getUserId();
//            }

//        if (!Utils.isValidationEmpty(etSearch.getText().toString().trim())) {
//            strSearchUserName = etSearch.getText().toString().trim();
//        }

//            Call<HomeDetailsResponseModel> call = service.apiGetChatList(strUserId, etSearch.getText().toString().trim());
            Call<HomeDetailsResponseModel> call = service.apiGetChatList(Preferences.getStringName(Preferences.keyUserId), etSearch.getText().toString().trim());
            call.enqueue(new Callback<HomeDetailsResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<HomeDetailsResponseModel> call,
                                       @NonNull Response<HomeDetailsResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        HomeDetailsResponseModel model = response.body();
                        if (model != null) {
                            if (model.getResponseCode() == 1) {
//                            chatsList = model.getData();

                                //                                lstLike = model.getLikeDetails();
                                if (lstLikeMain == null) {
                                    lstLikeMain = new ArrayList<>();
                                }
                                lstLikeMain.clear();
                                lstLikeMain.addAll(model.getLikeDetails());

                                if (lstLike == null) {
                                    lstLike = new ArrayList<>();
                                }
                                lstLike.clear();
                                lstLike.addAll(lstLikeMain);


//                                lstViewProfile = model.getViewProfile();
                                if (lstViewProfileMain == null) {
                                    lstViewProfileMain = new ArrayList<>();
                                }
                                lstViewProfileMain.clear();
                                lstViewProfileMain.addAll(model.getViewProfile());

                                if (lstViewProfile == null) {
                                    lstViewProfile = new ArrayList<>();
                                }
                                lstViewProfile.clear();
                                lstViewProfile.addAll(lstViewProfileMain);


                                ///
                                if (lstSavedProfileMain == null) {
                                    lstSavedProfileMain = new ArrayList<>();
                                }
                                lstSavedProfileMain.clear();
                                lstSavedProfileMain.addAll(model.getSavedProfile());

                                if (lstSavedProfile == null) {
                                    lstSavedProfile = new ArrayList<>();
                                }
                                lstSavedProfile.clear();
                                lstSavedProfile.addAll(lstSavedProfileMain);



                                if (lstLike != null || lstViewProfile != null|| lstSavedProfile != null
                                    /*|| chatsList != null*/) {
                                    mAdapter = new ChatAdapter(getFragmentManager(), /*tabLayout.getTabCount()*/totalTabs, lstLike, lstViewProfile,lstSavedProfile);
                                    viewPager.setAdapter(mAdapter);

                                    if (lstLike.size() > 0) {
//                                    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string._50_liked, String.valueOf(lstLike.size()))), 0);
                                        setTabLayoutPositionWise(0, lstLike.size() + "");
                                    }
                                    if (lstViewProfile.size() > 0) {
//                                    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string._120_views, String.valueOf(lstViewProfile.size()))), 1);
                                        setTabLayoutPositionWise(1, lstViewProfile.size() + "");
                                    }
                                    if (lstSavedProfile.size() > 0) {
//                                    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string._120_views, String.valueOf(lstViewProfile.size()))), 1);
                                        setTabLayoutPositionWise(3, lstSavedProfile.size() + "");
                                    }

                                    processCustomTab(true, selectedPosition);

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
                public void onFailure(@NonNull Call<HomeDetailsResponseModel> call, @NonNull Throwable t) {
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
    }


    private void initClick() {
//        etSearch.setOnClickListener(this);
//        ivSearch.setOnClickListener(this);
//        ivSearchName.setOnClickListener(this);

//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() != 0) {
////                    Glide.with(activity)
////                            .asBitmap()
////                            .load(R.drawable.ic_search_orange)
////                            .into(ivSearchName);
//                } else {
////                    Glide.with(activity)
////                            .asBitmap()
////                            .load(R.drawable.ic_search_gray)
////                            .into(ivSearchName);
//                }
//            }
//        });

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });

        llLiked.setOnClickListener(v -> processCustomTab(true, 0));
        llViews.setOnClickListener(v -> processCustomTab(true, 1));
        llMatchChat.setOnClickListener(v -> processCustomTab(true, 2));
        llSaveProfile.setOnClickListener(v -> processCustomTab(true, 3));

    }

    private void initView(View view) {
        etSearch = view.findViewById(R.id.etSearch);
        rlShowSearch = view.findViewById(R.id.rlShowSearch);
        llHideSearch = view.findViewById(R.id.llHideSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setFilterProcess(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                doAPIGetChatList();
                Utils.hideKeyBoard(activity, etSearch);
                return true;
            }
            return false;
        });

        view.findViewById(R.id.ivSearch).setOnClickListener(v -> {
            if (rlShowSearch.getVisibility() == View.VISIBLE) {
                searchViewProcess(false, true);
            } else {
                searchViewProcess(true, true);
            }
        });
        view.findViewById(R.id.ivClose).setOnClickListener(v -> searchViewProcess(false, false));


        llLiked = view.findViewById(R.id.llLiked);
        llViews = view.findViewById(R.id.llViews);
        llMatchChat = view.findViewById(R.id.llMatchChat);
        llSaveProfile = view.findViewById(R.id.llSaveProfile);

        tvLiked = view.findViewById(R.id.tvLiked);
        tvViews = view.findViewById(R.id.tvViews);
        tvMatchChat = view.findViewById(R.id.tvMatchChat);
        tvSaveProfile = view.findViewById(R.id.tvSaveProfile);

        viewLiked = view.findViewById(R.id.viewLiked);
        viewViews = view.findViewById(R.id.viewViews);
        viewMatchChat = view.findViewById(R.id.viewMatchChat);
        viewSaveProfile = view.findViewById(R.id.viewSaveProfile);

//        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string._50_liked, "")));
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string._120_views, "")));
//        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string._5_match_and_chat, "")));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                searchViewProcess(false,false);
                processCustomTab(false, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        tabLayout.setupWithViewPager(viewPager);

//        etSearch = view.findViewById(R.id.etSearch);
//        ivSearch = view.findViewById(R.id.ivSearch);
//        ivSearchName = view.findViewById(R.id.ivSearchName);
//        rlSearchTransitions = view.findViewById(R.id.rlSearchTransitions);


        ChatListFragment.setOnGetReadCountClickListener((readCount, totalUserCount) -> {

//                ChatFragment.this.setTabLayoutPositionWise(2, readCount + "");
            ChatFragment.this.setTabLayoutPositionWise(2, totalUserCount + "");
        });
    }

    //    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.etSearch: {
////                TransitionSet set = new TransitionSet()
////                        .addTransition(new Fade())
////                        .setInterpolator(new FastOutLinearInInterpolator());
////
////                TransitionManager.beginDelayedTransition(rlSearchTransitions, set);
//            }
//            break;
//
////            case R.id.ivSearch:
//////                TransitionSet set = new TransitionSet()
//////                        .addTransition(new Fade())
//////                        .setInterpolator(new FastOutLinearInInterpolator());
////
//////                TransitionManager.beginDelayedTransition(rlSearchTransitions, set);
//////                etSearch.setText("");
//////                ivSearch.setVisibility(View.GONE);
//////                etSearch.setVisibility(View.VISIBLE);
//////                ivSearchName.setVisibility(View.VISIBLE);
////                break;
////
////            case R.id.ivSearchName:
////
//////                ivSearch.setVisibility(View.VISIBLE);
////                etSearch.setVisibility(View.GONE);
//////                ivSearchName.setVisibility(View.GONE);
////                if (etSearch.getText().toString().trim().length() != 0) {
////                    if (Utils.isNetworkAvailable(activity, true, false)) {
////                        GetChatList();
////                    }
////                }
////                break;
//        }
//    }
    private void setFilterProcess(String search) {
        search = search.toLowerCase().trim();

        Logger.d("test_selectedPosition", selectedPosition);

        if (selectedPosition == 0) {
            if (lstLike == null) {
                lstLike = new ArrayList<>();
            }
            lstLike.clear();

            if (Utils.isValidationEmpty(search)) {
                lstLike.addAll(lstLikeMain);
            } else {
                for (int i = 0; i < lstLikeMain.size(); i++) {
                    LoginDataModel model = lstLikeMain.get(i);

                    if (model != null) {
                        if (model.getName().toLowerCase().trim().contains(search)) {
                            lstLike.add(model);
                        }
                    }
                }

            }

            Logger.d("test_size_lstLike", lstLike.size());

//            mAdapter = new ChatAdapter(getFragmentManager(), totalTabs, lstLike, lstViewProfile);
//            viewPager.setAdapter(mAdapter);
        } else if (selectedPosition == 1) {
            if (lstViewProfile == null) {
                lstViewProfile = new ArrayList<>();
            }
            lstViewProfile.clear();

            if (Utils.isValidationEmpty(search)) {
                lstViewProfile.addAll(lstViewProfileMain);
            } else {
                for (int i = 0; i < lstViewProfileMain.size(); i++) {
                    LoginDataModel model = lstViewProfileMain.get(i);

                    if (model != null) {
                        if (model.getName().toLowerCase().trim().contains(search)) {
                            lstViewProfile.add(model);
                        }
                    }
                }

            }
            Logger.d("test_size_lstViewProfile", lstViewProfile.size());

//            mAdapter = new ChatAdapter(getFragmentManager(), /*tabLayout.getTabCount()*/totalTabs, lstLike, lstViewProfile/*, chatsList*/);
//            viewPager.setAdapter(mAdapter);
        }

        mAdapter = new ChatAdapter(getFragmentManager(), /*tabLayout.getTabCount()*/totalTabs, lstLike, lstViewProfile/*, chatsList*/, lstSavedProfile);
        viewPager.setAdapter(mAdapter);

    }

    private void setTabLayoutPositionWise(int position, String count) {
//        if (tabLayout != null) {
//            // old tab remove.
//            tabLayout.removeTabAt(position);
//
//            if (Utils.isValidaEmptyWithZero(count)) {
//                count = "";
//            }
//
//            String title = "";
//            if (position == 0) {
//                title = activity.getResources().getString(R.string._50_liked, count);
//                tvLiked.setText(title);
//            } else if (position == 1) {
//                title = activity.getResources().getString(R.string._120_views, count);
//                tvViews.setText(title);
//            } else if (position == 2) {
//                title = activity.getResources().getString(R.string._5_match_and_chat, count);
//                tvMatchChat.setText(title);
//            }
//
//            // now new tab add to old position.
//            TabLayout.Tab firstTab = tabLayout.newTab(); // Create a new Tab names "title"
//            firstTab.setText(title); // set the Text for the title
//            tabLayout.addTab(firstTab, position); // add  the tab in the TabLayout at specific position
//        }

        if (Utils.isValidaEmptyWithZero(count)) {
            count = "";
        }

        String title;
        if (position == 0) {
            title = activity.getResources().getString(R.string._50_liked, count);
            tvLiked.setText(title);
        } else if (position == 1) {
            title = activity.getResources().getString(R.string._120_views, count);
            tvViews.setText(title);
        } else if (position == 2) {
            title = activity.getResources().getString(R.string._5_match_and_chat, count);
            tvMatchChat.setText(title);
        } else if (position == 3) {
            title = activity.getResources().getString(R.string._5_save_profile, count);
            tvSaveProfile.setText(title);
        }
    }
}
