package com.plugspace.activities;

import android.app.Activity;
import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.plugspace.R;
import com.plugspace.adapters.SlcFavSongDialogAdapter;
import com.plugspace.adapters.MusicListAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.ArrayResponseModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.model.PreviewModel;
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicListActivity extends BaseActivity implements /*MusicListAdapter.MyListener,*/ SlcFavSongDialogAdapter.MyListener {
    private Activity activity;
    //    String strCategoryName = ""/*, strFrom = "", strMusicFavFrom = ""*/;
    //    ArrayList<PreviewModel> songList = new ArrayList<>();
    ArrayList<PreviewModel> favCategoryList = new ArrayList<>();
    private List<LoginDataModel> lstModel = new ArrayList<>();
    private List<LoginDataModel> lstModelMain = new ArrayList<>();
    private RecyclerView rvModel;
    private MusicListAdapter mAdapter;
    private Dialog favCategoryDialog = null;
    private SlcFavSongDialogAdapter songDialogAdapter;
    private RelativeLayout rlShowSearch;
    private EditText etSearch;
    private LinearLayout llEmptyList, llHideSearch;
    private boolean isPlay = false;
    private int playPosition;
    private String title = "", musicType = "", otherUserId = "";
    private MediaPlayer mPlayer = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        activity = this;
        getIntentData();
        initFillData();
        initToolBar();
        initView();
        setDataToList();
        doAPIGetMusicList();
    }

    private void playMusicProcess(boolean isPlay) {
        if (isPlay && lstModel != null && lstModel.size() > playPosition && !Utils.isValidationEmpty(lstModel.get(playPosition).getMediaUrl())) {

            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.pause();
            }
            mPlayer = null;

            mPlayer = new MediaPlayer();
            String url = lstModel.get(playPosition).getMediaUrl();
            Logger.d("test_music_url", url);

            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mPlayer.setOnCompletionListener(mp -> playMusicProcess(false));

            mPlayer.setOnSeekCompleteListener(mp -> playMusicProcess(false));

            try {
                mPlayer.setDataSource(url);
                mPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mPlayer.start();

            setLiveMusicProcess(true);

        } else if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayer = null;
            setLiveMusicProcess(false);
        } else {
            mPlayer = null;
            setLiveMusicProcess(false);
        }
    }

    private void setLiveMusicProcess(boolean isPlay) {
        if (lstModel != null && lstModel.size() > playPosition) {
            LoginDataModel model = lstModel.get(playPosition);
            if (isPlay && mPlayer != null && mPlayer.isPlaying()) {
                model.setPlay(true);
            } else {
                model.setPlay(false);
            }
            lstModel.set(playPosition, model);
            setDataToList();
        }

    }

    private void setDataToList() {
        if (mAdapter == null) {
            mAdapter = new MusicListAdapter(activity, lstModel, otherUserId);
            rvModel.setAdapter(mAdapter);

            mAdapter.setOnPlayMusicClickListener(position -> {
                playPosition = position;

                for (int i = 0; i < lstModel.size(); i++) {
                    LoginDataModel model = lstModel.get(i);

                    if (model != null) {
                        if (position == i && !model.isPlay()) {
                            model.setPlay(true);
                            isPlay = true;

                        } else {
                            if (position == i) {
                                isPlay = false;
                            }
                            model.setPlay(false);

                        }
                        lstModel.set(i, model);
                    }
                }
                setDataToList();
                playMusicProcess(isPlay);
            });

            mAdapter.setOnLikeClickListener(position -> {
                LoginDataModel model = lstModel.get(position);
                if (model != null) {

                    if (Preferences.getStringName(Preferences.keyUserId).equals(otherUserId) || model.getIsFavorite().equals(Constants.IS_FAVOURITE_1)) {
                        String musicType = "";
                        doAPIMusicLikeDislike(position, model, musicType);
                    } else {
                        showFavSongDialog(position, model);
                    }
                }
            });
        } else {
            mAdapter.notifyDataSetChanged();
        }

        if (lstModel != null && lstModel.size() > 0) {
            rvModel.setVisibility(View.VISIBLE);
            llEmptyList.setVisibility(View.GONE);
        } else {
            rvModel.setVisibility(View.GONE);
            llEmptyList.setVisibility(View.VISIBLE);
        }

    }

    private void getIntentData() {
//        Intent intent = getIntent().getExtras();
//
//        if (intent.hasExtra(Constants.CategoryName) && intent.hasExtra(Constants.from) && intent.hasExtra(Constants.musicFavFrom)) {
//            strCategoryName = intent.getStringExtra(Constants.CategoryName);
////            strFrom = intent.getStringExtra(Constants.from);
////            strMusicFavFrom = intent.getStringExtra(Constants.musicFavFrom);
//        }

        // default title set
        title = activity.getResources().getString(R.string.my_music_choice);

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(Constants.INTENT_KEY_MODEL)) {
                PreviewModel previewModel = (PreviewModel) mBundle.get(Constants.INTENT_KEY_MODEL);

                if (previewModel != null) {
                    musicType = previewModel.getMusicType();

                    if (!Utils.isValidationEmpty(previewModel.getNotiDescriptions())) {
                        title = previewModel.getNotiDescriptions();
                    }
                }
            }
            if (mBundle.containsKey(Constants.INTENT_KEY_MODEL_OTHER_POST)) {
                LoginDataModel modelOtherPost = (LoginDataModel) mBundle.get(Constants.INTENT_KEY_MODEL_OTHER_POST);

                if (modelOtherPost != null) {
                    otherUserId = modelOtherPost.getUserId();
                    Logger.d("test_otherUserId_song", otherUserId);
                }
            }
        }
    }

    private void initFillData() {
//        if (strFrom.equalsIgnoreCase(Constants.ODD_NUM)) {
//            songList.add(new PreviewModel("IK Mulaqaat", "Palak Muachal", R.drawable.ic_song1));
//            songList.add(new PreviewModel("Old town road", "Lil Nas X", R.drawable.ic_song2));
//            songList.add(new PreviewModel("Pal Pal Dil Ke Pass", "Arijit Singh, Parampara", R.drawable.ic_song3));
//        } else {
//            songList.add(new PreviewModel("IK Mulaqaat", "Palak Muachal", R.drawable.ic_song1));
//            songList.add(new PreviewModel("Old town road", "Lil Nas X", R.drawable.ic_song2));
//            songList.add(new PreviewModel("Pal Pal Dil Ke Pass", "Arijit Singh, Parampara", R.drawable.ic_song3));
//            songList.add(new PreviewModel("Bad guy", "Billie Eilish", R.drawable.ic_song4));
//            songList.add(new PreviewModel("Happy B'Day", "Sachin Sangvi", R.drawable.ic_song5));
//            songList.add(new PreviewModel("Khairiyat", "Arijit Singh", R.drawable.ic_song6));
//            songList.add(new PreviewModel("GF BF", "Sooraj Pancholi", R.drawable.ic_song7));
//        }

        favCategoryList.clear();
        favCategoryList.add(new PreviewModel(getString(R.string.exercise_songs), false, Constants.MUSIC_TYPE_EXERCISE));
        favCategoryList.add(new PreviewModel(getString(R.string.relax_songs), false, Constants.MUSIC_TYPE_RELAX));
        favCategoryList.add(new PreviewModel(getString(R.string.cars_songs), false, Constants.MUSIC_TYPE_CARS));
        favCategoryList.add(new PreviewModel(getString(R.string.wedding_songs), false, Constants.MUSIC_TYPE_WEDDING));
        favCategoryList.add(new PreviewModel(getString(R.string.regions_songs), false, Constants.MUSIC_TYPE_REGIONS));
        favCategoryList.get(Constants.SelectedPosition).setSlcCategory(true);

    }

    private void initToolBar() {
        ImageView ivBack = findViewById(R.id.ivBack);
//        ImageView ivSearch = findViewById(R.id.ivSearch);
        TextView tvTitleName = findViewById(R.id.tvTitleName);
        etSearch = findViewById(R.id.etSearch);
        rlShowSearch = findViewById(R.id.rlShowSearch);
        llHideSearch = findViewById(R.id.llHideSearch);

        tvTitleName.setText(title);
        rlShowSearch.setVisibility(View.GONE);
        llHideSearch.setVisibility(View.VISIBLE);

        findViewById(R.id.ivSearch).setOnClickListener(view -> {
            TransitionSet set = new TransitionSet()
                    .addTransition(new Fade())
                    .setInterpolator(new FastOutLinearInInterpolator());

            TransitionManager.beginDelayedTransition(rlShowSearch, set);

//            ivSearch.setVisibility(View.GONE);
//            etSearch.setVisibility(View.VISIBLE);

            llHideSearch.setVisibility(View.GONE);
            rlShowSearch.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.ivClose).setOnClickListener(v -> {
            Utils.hideKeyBoard(activity, etSearch);
            etSearch.setText("");
            llHideSearch.setVisibility(View.VISIBLE);
            rlShowSearch.setVisibility(View.GONE);
        });

        etSearch.setOnClickListener(view -> {
            TransitionSet set = new TransitionSet()
                    .addTransition(new Fade())
                    .setInterpolator(new FastOutLinearInInterpolator());

            TransitionManager.beginDelayedTransition(rlShowSearch, set);
        });


        ivBack.setOnClickListener(view -> onBackPressed());
    }


    private void initView() {
        llEmptyList = findViewById(R.id.llEmptyList);
        TextView tvEmptyList = findViewById(R.id.tvEmptyList);
        tvEmptyList.setText(activity.getResources().getString(R.string.empty_list_music));
        rvModel = findViewById(R.id.rvModel);
        rvModel.setLayoutManager(new LinearLayoutManager(activity));

//        songListAdapter = new MusicListAdapter(activity, songList, strFrom, strMusicFavFrom, MusicListActivity.this);
//        rvSongList.setAdapter(songListAdapter);

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
                Utils.hideKeyBoard(activity, etSearch);
                setFilterProcess(etSearch.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void setFilterProcess(String search) {
        search = search.toLowerCase().trim();

        if (isPlay) {
            isPlay = false;
            if (lstModel != null && lstModel.size() > playPosition) {
                LoginDataModel model = lstModel.get(playPosition);
                model.setPlay(false);
                lstModel.set(playPosition, model);
                setDataToList();
            }
            playMusicProcess(isPlay);
        }

        if (lstModel == null) {
            lstModel = new ArrayList<>();
        }
        lstModel.clear();

        if (Utils.isValidationEmpty(search)) {
            lstModel.addAll(lstModelMain);
        } else {
            for (int i = 0; i < lstModelMain.size(); i++) {
                LoginDataModel model = lstModelMain.get(i);

                if (model != null) {
//                    if (model.getTitle().toLowerCase().trim().contains(search) || model.getArtistsName().toLowerCase().trim().contains(search)) {
//                    if (model.getSubtitle().toLowerCase().trim().contains(search) || model.getHeader_desc().toLowerCase().trim().contains(search)) {
                    if (model.getSubtitle().toLowerCase().trim().contains(search)) {
                        lstModel.add(model);
                    }
                }
            }

        }
        setDataToList();

    }

//    @Override
//    public void myFavSong(int position, ImageView imageView) {
//        if (strFrom.equalsIgnoreCase(Constants.ODD_NUM)) {
//
//        } else {
//            imageView.setImageResource(R.drawable.ic_heart_fill);
//            showFavSongDialog();
//        }
//    }

    private void showFavSongDialog(int position, LoginDataModel model) {
        try {
            favCategoryDialog = new Dialog(activity, R.style.DialogTheme);
            favCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(favCategoryDialog.getWindow()).setBackgroundDrawable(null);
            favCategoryDialog.setContentView(R.layout.dialog_fav_song_slc_category);
            favCategoryDialog.setCancelable(true);
            favCategoryDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            final Button btnOk = favCategoryDialog.findViewById(R.id.btnOk);
            final RecyclerView rvSlcFavCategory = favCategoryDialog.findViewById(R.id.rvSlcFavCategory);

            rvSlcFavCategory.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
            songDialogAdapter = new SlcFavSongDialogAdapter(activity, favCategoryList, MusicListActivity.this);
            rvSlcFavCategory.setAdapter(songDialogAdapter);

            btnOk.setOnClickListener(v -> {
                String musicType = "";
                for (int i = 0; i < favCategoryList.size(); i++) {
                    PreviewModel previewModel = favCategoryList.get(i);
                    if (previewModel.getSlcCategory()) {
                        musicType = previewModel.getMusicType();
                        break;
                    }
                }

                if (favCategoryDialog != null && favCategoryDialog.isShowing()) {
                    favCategoryDialog.dismiss();
                    favCategoryDialog = null;
                }

                doAPIMusicLikeDislike(position, model, musicType);

            });
            favCategoryDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mySlcFavSongCategory(int position) {
        for (int i = 0; i < favCategoryList.size(); i++) {
            favCategoryList.get(i).setSlcCategory(false);
        }
        favCategoryList.get(position).setSlcCategory(true);
        songDialogAdapter.notifyDataSetChanged();
    }

    private void doAPIGetMusicList() {
        if (Utils.isNetworkAvailable(activity, true, false)) {

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            String userId = Preferences.getStringName(Preferences.keyUserId);
            if (!Utils.isValidationEmpty(otherUserId)) {
                userId = otherUserId;
            }

            Call<ArrayResponseModel> call = service.apiGetMusicList(
                    userId,
                    musicType,
                    etSearch.getText().toString().trim());

            if (!Utils.isValidationEmpty(musicType)) {
                call = service.apiGetFavoriteMusic(
                        userId,
                        musicType,
                        etSearch.getText().toString().trim());
            }


            call.enqueue(new Callback<ArrayResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ArrayResponseModel> call,
                                       @NonNull Response<ArrayResponseModel> response) {
                    hideProgressDialog(activity);

                    if (response.isSuccessful() && response.body() != null) {
                        ArrayResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
                            if (lstModelMain == null) {
                                lstModelMain = new ArrayList<>();
                            }
                            lstModelMain.clear();
                            lstModelMain.addAll(model.getData());

                            if (lstModel == null) {
                                lstModel = new ArrayList<>();
                            }
                            lstModel.clear();
                            lstModel.addAll(lstModelMain);
                            setFilterProcess(etSearch.getText().toString());
                        } else {
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }

                    } else {
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayResponseModel> call, @NonNull Throwable t) {
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

    private void doAPIMusicLikeDislike(int position, LoginDataModel dataModel, String musicType) {
        if (Utils.isNetworkAvailable(activity, true, false)) {

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiMusicLikeDislike(
                    Preferences.getStringName(Preferences.keyUserId),
                    dataModel.getMusicId(),
                    musicType
            );
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
//                    hideProgressDialog(activity);

                    if (response.isSuccessful() && response.body() != null) {
                        ObjectResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
                            Utils.showToast(activity, model.getResponseMsg());

                            if (Preferences.getStringName(Preferences.keyUserId).equals(otherUserId)) {
                                removeFavouriteProcess(position, dataModel);
                            } else {
                                updateFavouriteProcess(position, dataModel);
                            }

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

    private void removeFavouriteProcess(int position, LoginDataModel dataModel) {
        for (int i = 0; i < lstModelMain.size(); i++) {
            LoginDataModel dataModelMain = lstModelMain.get(i);
            if (dataModelMain != null) {
                if (dataModelMain.getMusicId().equals(dataModel.getMusicId())) {
                    lstModelMain.remove(i);
                    break;
                }
            }
        }
        lstModel.remove(position);

        setFilterProcess(etSearch.getText().toString());
        hideProgressDialog(activity);
    }

    private void updateFavouriteProcess(int position, LoginDataModel dataModel) {
        if (dataModel.getIsFavorite().equals(Constants.IS_FAVOURITE_1)) {
            dataModel.setIsFavorite(Constants.IS_FAVOURITE_0);
        } else {
            dataModel.setIsFavorite(Constants.IS_FAVOURITE_1);
        }
        lstModel.set(position, dataModel);

        for (int i = 0; i < lstModelMain.size(); i++) {
            LoginDataModel dataModelMain = lstModelMain.get(i);
            if (dataModelMain != null) {
                if (dataModelMain.getMusicId().equals(dataModel.getMusicId())) {
                    lstModelMain.set(i, dataModel);
                    break;
                }
            }
        }

        setFilterProcess(etSearch.getText().toString());
        hideProgressDialog(activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        playMusicProcess(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playMusicProcess(false);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
