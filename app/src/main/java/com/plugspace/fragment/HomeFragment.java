package com.plugspace.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.plugspace.R;
import com.plugspace.activities.HomeActivity;
import com.plugspace.activities.NotificationsActivity;
import com.plugspace.activities.PlugspaceRankActivity;
import com.plugspace.activities.PreetStoryView;
import com.plugspace.activities.SignUpActivity;
import com.plugspace.activities.SubscriptionsActivity;
import com.plugspace.adapters.PostListAdapter;
import com.plugspace.adapters.PreDefinedMessagesAdapter;
import com.plugspace.adapters.StoryAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.HomeDetailsResponseModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.model.PreDefinedMessageModel;
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends BaseFragment implements /*View.OnClickListener,*/ StoryAdapter.MyListener, PostListAdapter.MyListener {
    private Activity activity;
    private RecyclerView rvStory;
    private StoryAdapter mAdapterStory;
    private ArrayList<HomeDetailsResponseModel.StoryDtl> lstStory = new ArrayList<>();
    private LinearLayout llEmptyList, llEmptyListPost;
    private ImageView ivNotification/*, ivSearch*//*, ivSearchUser*/;
    public static ArrayList<LoginDataModel> notificationList = new ArrayList<>();
    public static ArrayList<LoginDataModel> lstPost = new ArrayList<>();
    public static ArrayList<MediaModel> mediaList = new ArrayList<>();
    public static ArrayList<LoginDataModel> storyDetailList = new ArrayList<>();
    public static ArrayList<MediaModel> storyStatusList = new ArrayList<>();
    private PostListAdapter mAdapterPostList;
    private RecyclerView rvPostList;
    private String strOtherUserId = "", strUserId = ""/*, strLikeUserId = ""*/, strStatusUserId = ""/*, strSearchUerName = ""*/;
    private LoginDataModel loginDataModel;
    private RelativeLayout rlShowSearch;
    private EditText etSearch;
    //    ViewGroup rlSearchTransitions;
    private boolean isNotification;
    private int selectedPosition;
    private List<PreDefinedMessageModel.PreDefinedMessageList> messagesList;


    public HomeFragment(boolean isNotification) {
        this.isNotification = isNotification;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        activity = getActivity();
        getPreviousData();
        initView(view);
        initClick(view);
        setDataToListStory();
        setDataToListPost();
        searchViewProcess(false);
//        doAPIGetHomeDetails();
//        initToolBar(view);
    }

    private void setDataToListPost() {


        if (lstPost != null && lstPost.size() > 0) {
            rvPostList.setVisibility(View.VISIBLE);
            llEmptyListPost.setVisibility(View.GONE);

            if (mediaList == null) {
                mediaList = new ArrayList<>();
            }
            mediaList.clear();

            for (int i = 0; i < lstPost.size(); i++) {
                LoginDataModel model = lstPost.get(i);
                if (model != null) {
                    loginDataModel = model;
                    if (model.getMediaDetail() != null) {
                        mediaList.addAll(model.getMediaDetail());
                    }
                }
            }

            if (mediaList != null) {
                LoginDataModel userInfoModel = new LoginDataModel();
                userInfoModel.setTitle(Constants.MEDIA_LIST);
                userInfoModel.setMediaDetail(mediaList);
                lstPost.add(userInfoModel);
            }

            if (mAdapterPostList == null) {
                mAdapterPostList = new PostListAdapter(activity, lstPost, loginDataModel, mediaList, HomeFragment.this);
                rvPostList.setAdapter(mAdapterPostList);

                mAdapterPostList.setOnAdapterClickListener((position, isFrom) -> {
                    LoginDataModel model = lstPost.get(position);
                    if (isFrom.equals(Constants.IS_FROM_BLOCK)) {
                        // dialogConfirmBlockUser(model.getUserId());
                        showMenuDialog(model.getUserId(),"","");
                    }
                });

            } else {
                mAdapterPostList.notifyDataSetChanged();
            }
        } else {
            rvPostList.setVisibility(View.GONE);
            llEmptyListPost.setVisibility(View.VISIBLE);
        }
    }

    private void dialogConfirmBlockUser(String userId) {
        Utils.hideKeyBoard(activity, etSearch);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.msg_confirm_block_user));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            doAPIBlockUser(userId);
        });
        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    private void doAPIBlockUser(String userId) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Utils.hideKeyBoard(activity, etSearch);

            Logger.d("test_block_user_id", userId);

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiBlockUser(
                    Preferences.getStringName(Preferences.keyUserId),
                    userId,
                    Constants.deviceType,
                    Constants.token
            );
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
//                    hideProgressDialog(activity);

                    if (response.isSuccessful() && response.body() != null) {
                        ObjectResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
//                            hideProgressDialog(activity);
                            Utils.showToast(activity, model.getResponseMsg());
                            doAPIGetHomeDetails();
                        } else {
                            hideProgressDialog(activity);
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }

                    } else {
                        hideProgressDialog(activity);
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
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

    private void getPreviousData() {
        LoginDataModel dataModel = Preferences.GetLoginDetails();
        if (dataModel != null) {
            strUserId = dataModel.getUserId();

        }
    }

    private void setDataToListStory() {
        if (mAdapterStory == null) {
            mAdapterStory = new StoryAdapter(activity, lstStory, strUserId, HomeFragment.this);
            rvStory.setAdapter(mAdapterStory);
        } else {
            mAdapterStory.notifyDataSetChanged();
        }

        if (lstStory != null && lstStory.size() > 0) {
            rvStory.setVisibility(View.VISIBLE);
            llEmptyList.setVisibility(View.GONE);
        } else {
            rvStory.setVisibility(View.GONE);
            llEmptyList.setVisibility(View.VISIBLE);
        }
    }

    private void doAPIGetHomeDetails() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Utils.hideKeyBoard(activity, etSearch);

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

//            String searchText = etSearch.getText().toString().trim();
//        if (!Utils.isValidationEmpty(etSearch.getText().toString().trim())) {
//            strSearchUerName = etSearch.getText().toString().trim();
//        }
            Call<HomeDetailsResponseModel> call = service.apiCallGetHomeDetails(
                    strUserId,
                    etSearch.getText().toString().trim());

            call.enqueue(new Callback<HomeDetailsResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<HomeDetailsResponseModel> call,
                                       @NonNull Response<HomeDetailsResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        HomeDetailsResponseModel model = response.body();
//                        Log.e("PPP-->", new Gson().toJson(response.body()));
//                        try {
//                            JSONObject jsonObj = new JSONObject(new Gson().toJson(response.body()));
//                            Log.e("PR", "===> " + jsonObj.getJSONArray("storyDtl"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        if (model != null) {
                            if (model.getResponseCode() == 1) {

                                if (lstPost == null) {
                                    lstPost = new ArrayList<>();
                                }
                                lstPost.clear();
                                lstPost.addAll(model.getData());

                                setDataToListPost();

//                                if (lstPost != null && lstPost.size() > 0) {
//                                    mAdapterPostList = new PostListAdapter(activity, lstPost, loginDataModel, mediaList, HomeFragment.this);
//                                    rvPostList.setAdapter(mAdapterPostList);
//                                }

//                                if (lstPost != null && lstPost.size() > 0) {
//                                    for (int i = 0; i < lstPost.size(); i++) {
//                                        mediaList = lstPost.get(i).getMediaDetail();
//                                        loginDataModel = lstPost.get(i);
//                                    }
//                                }
//
//                                if (mediaList != null) {
//                                    LoginDataModel userInfoModel = new LoginDataModel();
//                                    userInfoModel.setTitle(Constants.MEDIA_LIST);
//                                    userInfoModel.setMediaDetail(mediaList);
//                                    lstPost.add(userInfoModel);
//                                }


                                //                            lstStory = model.getStoryDtl();

//                            if (lstStory != null && lstStory.size() > 0) {
//                                mAdapterStory = new StoryAdapter(activity, lstStory, strUserId, HomeFragment.this);
//                                rvStory.setAdapter(mAdapterStory);
//                            }

                                if (lstStory == null) {
                                    lstStory = new ArrayList<>();
                                }
                                lstStory.clear();
                                lstStory.addAll(model.getStoryDtl());
                                setDataToListStory();

//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        getVideoDurations();
//                                    }
//                                }).start();

                                notificationList = model.getNotificationDtl();

                                if (isNotification) {
                                    isNotification = false;
                                    ivNotification.performClick();
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

    private void initClick(View view) {
        rvPostList.setOnTouchListener((v, event) -> {
            Utils.hideKeyBoard(activity, etSearch);
            return false;
        });
        rvStory.setOnTouchListener((v, event) -> {
            Utils.hideKeyBoard(activity, etSearch);
            return false;
        });

        ivNotification.setOnClickListener(v -> {
            Utils.hideKeyBoard(activity, etSearch);

            startActivity(new Intent(activity, NotificationsActivity.class)
                    .putExtra(Constants.ARRAY_LIST, notificationList));
        });


        view.findViewById(R.id.ivSearch).setOnClickListener(v -> {
            if (rlShowSearch.getVisibility() == View.VISIBLE) {
                searchViewProcess(false);
            } else {
                searchViewProcess(true);
            }
        });
        view.findViewById(R.id.ivClose).setOnClickListener(v -> searchViewProcess(false));


//        ivSearch.setOnClickListener(this);
//        etSearch.setOnClickListener(this);
//        ivSearchUser.setOnClickListener(this);

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
////                if (s.length() != 0) {
////                    Glide.with(activity)
////                            .asBitmap()
////                            .load(R.drawable.ic_search_orange)
////                            .into(ivSearchUser);
////                } else {
////                    Glide.with(activity)
////                            .asBitmap()
////                            .load(R.drawable.ic_search_gray)
////                            .into(ivSearchUser);
////                }
//            }
//        });
    }

    private void searchViewProcess(boolean isShowSearch) {

        if (isShowSearch) {
            rlShowSearch.setVisibility(View.VISIBLE);
            etSearch.requestFocus();
            Utils.showKeyBoard(activity, etSearch);
        } else {
            etSearch.setText("");
            Utils.hideKeyBoard(activity, etSearch);
            rlShowSearch.setVisibility(View.GONE);

            doAPIGetHomeDetails();
        }
    }

    private void initView(View view) {
        ivNotification = view.findViewById(R.id.ivNotification);
//        LinearLayout llMyStory = view.findViewById(R.id.llMyStory);
//        TextView tvMyName = view.findViewById(R.id.tvMyName);
        etSearch = view.findViewById(R.id.etSearch);
//        ivSearch = view.findViewById(R.id.ivSearch);
//        ivSearchUser = view.findViewById(R.id.ivSearchUser);
        rlShowSearch = view.findViewById(R.id.rlShowSearch);
//        rlSearchTransitions = view.findViewById(R.id.rlSearchTransitions);


        llEmptyList = view.findViewById(R.id.llEmptyList);
        TextView tvEmptyList = view.findViewById(R.id.tvEmptyList);
        tvEmptyList.setText(activity.getResources().getString(R.string.empty_list_story));

        llEmptyListPost = view.findViewById(R.id.llEmptyListPost);
        TextView tvEmptyListPost = view.findViewById(R.id.tvEmptyListPost);
        tvEmptyListPost.setText(activity.getResources().getString(R.string.empty_list_profile));

        rvStory = view.findViewById(R.id.rvStory);
        rvStory.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));

        rvPostList = view.findViewById(R.id.rvPostList);
        rvPostList.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));


        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doAPIGetHomeDetails();
                return true;
            }
            return false;
        });
    }

//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.ivNotification) {
////            if (notificationList != null && notificationList.size() > 0) {
//            startActivity(new Intent(activity, NotificationsActivity.class)
//                    .putExtra(Constants.ARRAY_LIST, notificationList));
////            }
//
//        }
////        else if (view.getId() == R.id.ivSearch) {
//////            TransitionSet set = new TransitionSet()
//////                    .addTransition(new Fade())
//////                    .setInterpolator(new FastOutLinearInInterpolator());
////
//////            TransitionManager.beginDelayedTransition(rlSearchTransitions, set);
////
////            etSearch.setText("");
////            ivSearch.setVisibility(View.GONE);
////            rlShowSearch.setVisibility(View.VISIBLE);
////            etSearch.setVisibility(View.VISIBLE);
////
//////        } else if (view.getId() == R.id.etSearch) {
//////            TransitionSet set = new TransitionSet()
//////                    .addTransition(new Fade())
//////                    .setInterpolator(new FastOutLinearInInterpolator());
////
//////            TransitionManager.beginDelayedTransition(rlSearchTransitions, set);
////
////        }
////        else if (view.getId() == R.id.ivSearchUser) {
////            // : 9/2/22 Pending: search -> action done click.
////            rlShowSearch.setVisibility(View.GONE);
////            ivSearch.setVisibility(View.VISIBLE);
////            if (etSearch.getText().toString().trim().length() != 0) {
////                if (Utils.isNetworkAvailable(activity, true, false)) {
////                    slideUp(rvPostList);
////                    GetHomeDetails();
////                }
////            }
////        }
//    }

    @Override
    public void statusClicked(int position, String strOtherUserId, HomeDetailsResponseModel.StoryDtl storyDataModel) {
        selectedPosition = position;
//        doAPIGetStoryDetails(strOtherUserId, storyDataModel);
// : 14/2/22 Confirm: above below.

        if (lstStory != null && lstStory.size() > 0 && lstStory.size() > selectedPosition) {
//            startActivityForResult(new Intent(activity, StatusActivity.class)
            startActivityForResult(new Intent(activity, PreetStoryView.class)
//            startActivityForResult(new Intent(activity, Kaler_stpry.class)
                            .putExtra(Constants.INTENT_KEY_LIST, lstStory)
//                            .putExtra(Constants.INTENT_KEY_LIST_2, durationList)
                            .putExtra(Constants.INTENT_KEY_SELECTED_POSITION, selectedPosition),
                    Constants.REQUEST_CODE_VIEW_STORY);

            // TODO: 21/2/22 Confirm: above below.

//            startActivityForResult(new Intent(activity, StatusCopyActivity.class)
//                            .putExtra(Constants.INTENT_KEY_LIST, lstStory)
//                            .putExtra(Constants.INTENT_KEY_SELECTED_POSITION, selectedPosition),
//                    Constants.REQUEST_CODE_VIEW_STORY);
        }

    }

//    private void doAPIGetStoryDetails(String strOtherUserId, LoginDataModel storyDataModel) {
//        if (Utils.isNetworkAvailable(activity, true, false)) {
//            Utils.hideKeyBoard(activity, etSearch);
//
//            showProgressDialog(activity);
//            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);
//            LoginDataModel dataModel = Preferences.GetLoginDetails();
//            if (dataModel != null) {
//                strStatusUserId = dataModel.getUserId();
//            }
//
//            Call<HomeDetailsResponseModel> call = service.apiCallGetStoryDetails(strStatusUserId, strOtherUserId);
//            call.enqueue(new Callback<HomeDetailsResponseModel>() {
//                @Override
//                public void onResponse(@NonNull Call<HomeDetailsResponseModel> call,
//                                       @NonNull Response<HomeDetailsResponseModel> response) {
//                    hideProgressDialog(activity);
//                    if (response.isSuccessful()) {
//                        HomeDetailsResponseModel model = response.body();
//                        if (model != null) {
//                            if (model.getResponseCode() == 1) {
//                                storyDetailList = model.getData();
//                                if (storyDetailList != null) {
//                                    for (int i = 0; i < storyDetailList.size(); i++) {
//                                        storyStatusList = storyDetailList.get(i).getStoryMediaDetail();
//
//                                        // TODO: 14/2/22 Confirm: below condition.
//                                        if (storyDetailList.size() - 1 == i) {
//                                            storyStatusList = storyDetailList.get(i).getStoryMediaDetail();
//                                        }
//                                    }
//                                }
//
//                                if (storyStatusList != null && storyStatusList.size() > 0) {
//                                    startActivityForResult(new Intent(activity, StatusActivity.class)
//                                            .putExtra(Constants.ARRAY_LIST, storyStatusList)
//                                            .putExtra(Constants.USER_ID, strStatusUserId)
//                                            .putExtra(Constants.FROM, "1")
//                                            .putExtra(Constants.DATA_MODEL, storyDataModel), Constants.REQUEST_CODE_VIEW_STORY);
//                                }
//                            } else {
//                                if (!model.getResponseMsg().isEmpty()) {
//                                    Utils.showAlert(activity, getString(R.string.app_name),
//                                            model.getResponseMsg());
//                                }
//                            }
//                        }
//                    } else {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<HomeDetailsResponseModel> call, @NonNull Throwable t) {
//                    hideProgressDialog(activity);
//                    if (!Utils.isNetworkAvailable(activity, true, false)) {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
//                                activity.getResources().getString(R.string.error_network));
//                    } else {
//                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
//                                activity.getResources().getString(R.string.technical_problem));
//                    }
//                    t.printStackTrace();
//                }
//            });
//        }
//    }

    @Override
    public void myClicked(String identify, int position) {
        if (identify.equalsIgnoreCase(Constants.LIKE)) {
            showDialog(position);

        } else if (identify.equalsIgnoreCase(Constants.DISLIKE)) {
            ProfileLikeDislike(Constants.isLike_2, "", "");

        } else if (identify.equalsIgnoreCase(Constants.RANK)) {
            LoginDataModel dataModel = lstPost.get(position);
            startActivity(new Intent(activity, PlugspaceRankActivity.class)
                    .putExtra(Constants.DATA_MODEL, dataModel));

        }
    }

    private void ProfileLikeDislike(String strType, String comment, String message) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            if (loginDataModel != null) {
                strOtherUserId = loginDataModel.getUserId();
            }

//        LoginDataModel dataModel = Preferences.GetLoginDetails();
//        if (dataModel != null) {
//            strLikeUserId = dataModel.getUserId();
//        }
            Logger.d("test_profileLikeDislike_strType", strType);

//        Call<ObjectResponseModel> call = service.profileLikeDislike(strLikeUserId, strOtherUserId, strType);
            Call<ObjectResponseModel> call = service.profileLikeDislike(Preferences.getStringName(Preferences.keyUserId),
                    strOtherUserId, strType, comment, message);

            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        ObjectResponseModel model = response.body();
                        if (model != null) {
                            if (model.getResponseCode() == 1) {
                                lstPost.remove(0);
                                mAdapterPostList.notifyDataSetChanged();

                                slideUp(rvPostList);
                                doAPIGetHomeDetails();

                            } else {
                                if (!model.getResponseMsg().isEmpty()) {
                                    Utils.showAlertTwoButtons(activity,
                                            model.getResponseMsg(), (dialog, which) -> {
                                                dialog.dismiss();
                                            }, (dialog, which) -> {
                                                dialog.dismiss();
                                                startActivity(new Intent(activity, SubscriptionsActivity.class));
                                            });
                                }
                            }
                        }
                    } else {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
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

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Bundle mBundle = data.getExtras();
            if (mBundle != null) {
                if (mBundle.containsKey(Constants.INTENT_KEY_IS_UPDATE)) {

                    if (requestCode == Constants.REQUEST_CODE_VIEW_STORY) {

                        boolean isUpdate = mBundle.getBoolean(Constants.INTENT_KEY_IS_UPDATE);
                        if (isUpdate && lstStory != null && lstStory.size() > 0 && lstStory.size() > selectedPosition) {
//                            LoginDataModel model = lstStory.get(selectedPosition);
//
//                            if (!model.getIs_show_story().equals(Constants.isStory_1)) {
//                                model.setIs_show_story(Constants.isStory_1);
//                                lstStory.remove(selectedPosition);
//                                lstStory.add(model);
//
//                                setDataToListStory();
//                            }
                            doAPIGetHomeDetails();
                        }
                    }
                }
            }

        }
    }

    public void showDialog(int position) {

        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<PreDefinedMessageModel> call = service.apiCallGetPreDefinedMessage();

            call.enqueue(new Callback<PreDefinedMessageModel>() {
                @Override
                public void onResponse(@NonNull Call<PreDefinedMessageModel> call,
                                       @NonNull Response<PreDefinedMessageModel> response) {
                    hideProgressDialog(activity);
                    if (response.isSuccessful()) {
                        PreDefinedMessageModel model = response.body();
                        if (model != null) {
                            messagesList = model.getMessage();
                        }
                    } else {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PreDefinedMessageModel> call, @NonNull Throwable t) {
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

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_love_option);

        Window window = dialog.getWindow();

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button sendMsg = dialog.findViewById(R.id.send_msg);
        Button tvMatch = dialog.findViewById(R.id.tvMatch);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        if (lstPost.get(position).getRequestStatus() == 0) {
            tvMatch.setText(R.string.like_only);
        } else {
            tvMatch.setText(R.string.go_to_match_screen);
        }

        if (lstPost.get(position).getRequestStatus() == 0) {
            sendMsg.setText(R.string.add_a_comment);
        } else {
            sendMsg.setText(R.string.send_a_message);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showSendLikeMessageDialog(position);
            }
        });

        tvMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (lstPost != null && lstPost.size() > position) {
                    String likeType = "";
                    if (lstPost.get(position).getIs_like().equals(Constants.isLike_1)) {
                        likeType = Constants.isLike_2;
                    } else {
                        likeType = Constants.isLike_1;
                    }
                    ProfileLikeDislike(likeType, "", "");
                }
            }
        });

        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void showSendLikeMessageDialog(int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.send_like_message_dialog);

        Window window = dialog.getWindow();

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView sendLikeMsgTV = dialog.findViewById(R.id.send_like_msg_tv);
        RecyclerView recyclerView = dialog.findViewById(R.id.pre_defined_msg_rv);
        Button sendMsg = dialog.findViewById(R.id.send_like_msg_btn);
        EditText msgET = dialog.findViewById(R.id.send_like_msg_edit);
        Button btnCancel = dialog.findViewById(R.id.send_like_msg_cancel_btn);

        if (lstPost.get(position).getRequestStatus() == 0) {
            sendLikeMsgTV.setText(R.string.add_a_comment);
            sendMsg.setText(R.string.add_a_comment);
            recyclerView.setVisibility(View.GONE);
        } else {
            sendLikeMsgTV.setText(R.string.send_a_message);
            sendMsg.setText(R.string.send_a_message);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (messagesList != null && messagesList.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(new PreDefinedMessagesAdapter(activity, messagesList, new PreDefinedMessagesAdapter.MyListener() {
                @Override
                public void msgClick(int id, String msg) {
                    dialog.dismiss();
                    if (lstPost.get(position).getRequestStatus() != null) {
                        likeWithCommentMessage(position, msg);
                    }
                }
            }));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.e(position + "==>", lstPost.get(position).getRequestStatus() + " " + msgET.getText().toString().trim());
                if (lstPost.get(position).getRequestStatus() != null) {
                    likeWithCommentMessage(position, msgET.getText().toString().trim());
                }
            }
        });

        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    void likeWithCommentMessage(int position, String text) {
        if (lstPost != null && lstPost.size() > position) {
            String likeType = "";
            if (lstPost.get(position).getIs_like().equals(Constants.isLike_1)) {
                likeType = Constants.isLike_2;
            } else {
                likeType = Constants.isLike_1;
            }

            if (lstPost.get(position).getRequestStatus() == 0) {
                ProfileLikeDislike(likeType, text, "");
            } else {
                ProfileLikeDislike(likeType, "", text);
            }

        }
    }

    public void showMenuDialog(String saveUserID,String message, String type) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.modal_bottomsheet);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnReport = dialog.findViewById(R.id.btn_report);
        Button btn_remove = dialog.findViewById(R.id.btn_remove);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAPISaveProfile(saveUserID);
                dialog.dismiss();
            }
        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportDialog(saveUserID,"","");
                dialog.dismiss();
            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAPIRemoveProfile(saveUserID);
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void doAPISaveProfile(String savedUserId) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Utils.hideKeyBoard(activity, etSearch);

            Logger.d("test_Save_user_id", savedUserId);

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiSaveProfile(
                    Preferences.getStringName(Preferences.keyUserId),
                    savedUserId

            );
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
//                    hideProgressDialog(activity);

                    if (response.isSuccessful() && response.body() != null) {
                        ObjectResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
//                            hideProgressDialog(activity);
                            Utils.showToast(activity, model.getResponseMsg());
                            doAPIGetHomeDetails();
                        } else {
                            hideProgressDialog(activity);
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }

                    } else {
                        hideProgressDialog(activity);
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
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

    public void showReportDialog(String friendId, String message, String type) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.report_dialog);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button btnCancel = dialog.findViewById(R.id.btn_report_cancel);
        Button btnSave = dialog.findViewById(R.id.btn_report_submit);
        EditText et_report_message = dialog.findViewById(R.id.et_report_message);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAPIReport(friendId,et_report_message.getText().toString(),"1");
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void doAPIReport(String friendId, String message, String type) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Utils.hideKeyBoard(activity, etSearch);


            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiReport(
                    Preferences.getStringName(Preferences.keyUserId),
                    friendId, message, type

            );
            Log.e("friendId",""+friendId);
            Log.e("message",""+message);
            Log.e("type",""+type);
            Log.e("userId",""+Preferences.getStringName(Preferences.keyUserId));
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
//                    hideProgressDialog(activity);

                    if (response.isSuccessful() && response.body() != null) {
                        ObjectResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
//                            hideProgressDialog(activity);
                            Utils.showToast(activity, model.getResponseMsg());
                            doAPIGetHomeDetails();
                        } else {
                            hideProgressDialog(activity);
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }

                    } else {
                        hideProgressDialog(activity);
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
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

    private void doAPIRemoveProfile(String blockedId) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Utils.hideKeyBoard(activity, etSearch);

            Logger.d("test_Save_user_id", blockedId);

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiRemoveUser(
                    Preferences.getStringName(Preferences.keyUserId),
                    blockedId

            );
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
//                    hideProgressDialog(activity);

                    if (response.isSuccessful() && response.body() != null) {
                        ObjectResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
//                            hideProgressDialog(activity);
                            Utils.showToast(activity, model.getResponseMsg());
                            doAPIGetHomeDetails();
                        } else {
                            hideProgressDialog(activity);
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }

                    } else {
                        hideProgressDialog(activity);
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
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


}
