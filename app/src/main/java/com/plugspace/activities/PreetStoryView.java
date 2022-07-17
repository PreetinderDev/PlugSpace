package com.plugspace.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.plugspace.R;
import com.plugspace.adapters.StatusAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.HomeDetailsResponseModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import omari.hamza.storyview.callback.StoryClickListeners;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xute.storyview.StoryModel;
import xute.storyview.StoryView;

public class PreetStoryView extends BaseActivity implements StoriesProgressView.StoriesListener {

    private Activity activity;
    private ImageView image;
    private int counter = 0;
    private long[] durations;
    private ImageView ivBack;
    private EditText editText;
    private ImageView ivDelete;
    private VideoView mVideoView;
    private LinearLayout llPreview;
    private LinearLayout llComment;
    private int selectedPosition = 0;
    private  View viewLeft, viewRight;
    private boolean goNextBack = false;
    private RoundedImageView rivProfileImage;
    private TextView tvName, tvTime, tvViewCount;
    private StoriesProgressView storiesProgressView;
    private ArrayList<String> resources = new ArrayList<>();
    //    private ArrayList<String> durationList = new ArrayList<>();
    private ArrayList<HomeDetailsResponseModel.StoryDtl> lstStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preet_story_view);

        activity = this;
        rivProfileImage = findViewById(R.id.rivProfileImage);
        storiesProgressView = findViewById(R.id.stories);
        tvViewCount = findViewById(R.id.tvViewCount);
        mVideoView = findViewById(R.id.videoView);
        llPreview = findViewById(R.id.llPreview);
        llComment = findViewById(R.id.llComment);
        editText = findViewById(R.id.etComment);
        ivDelete = findViewById(R.id.ivDelete);
        viewLeft = findViewById(R.id.reverse);
        viewRight = findViewById(R.id.skip);
        ivBack = findViewById(R.id.ivBack);
        tvTime = findViewById(R.id.tvTime);
        tvName = findViewById(R.id.tvName);
        image = findViewById(R.id.image);


        counter = 0;
        storiesProgressView.setStoriesListener(PreetStoryView.this);
//        storiesProgressView.startStories(counter);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storiesProgressView.pause();
                mVideoView.pause();
                dialogConfirmDeleteStory();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((keyCode == EditorInfo.IME_ACTION_SEARCH
                        || keyCode == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                    // Perform action on key press
//                    Toast.makeText(MainActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {


                String comment = editText.getText().toString().trim();
                if (Utils.isValidationEmpty(comment)) {
                    Utils.showAlert(activity, activity.getResources().getString(R.string.valid_empty_comment));
                } else {
                    storiesProgressView.pause();
                    mVideoView.pause();
                    doStoryComment();
                }
            }
            return false;
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    storiesProgressView.pause();
//                    Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                } else {
                    storiesProgressView.resume();
//                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });

        showProgressDialog(activity);
        getPreviousData();
        nextBackMove();
    }

    private void nextBackMove() {
        viewLeft.setOnClickListener(view -> {
            Log.e("===>", counter + " REVERSE CLICK , " + (counter == 0));
            if (counter == 0) {
                return;
            }
            storiesProgressView.reverse();
        });
        viewRight.setOnClickListener(view -> {
            Log.e("===>", counter + " SKIP CLICK:" + selectedPosition + " , " + (counter == lstStatus.get(selectedPosition).getMedia().size()));
            if (counter == lstStatus.get(selectedPosition).getMedia().size()){
                return;
            }
            storiesProgressView.skip();
        });
    }

    private void doStoryComment() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Utils.hideKeyBoard(activity, editText);

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiStoryComment(Preferences.getStringName(Preferences.keyUserId),
                    lstStatus.get(selectedPosition).getUserId(),
                    editText.getText().toString().trim(),
                    Constants.deviceType,
                    Constants.token);
            call.enqueue(new Callback<ObjectResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ObjectResponseModel> call,
                                       @NonNull Response<ObjectResponseModel> response) {
                    mVideoView.resume();
                    hideProgressDialog(activity);
                    storiesProgressView.resume();
                    if (response.isSuccessful() && response.body() != null) {
                        ObjectResponseModel model = response.body();

                        if (model.getResponseCode() == 1) {
                            editText.setText("");
                            Utils.showToast(activity, model.getResponseMsg());

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
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
                    mVideoView.resume();
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

    private void dialogConfirmDeleteStory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getResources().getString(R.string.msg_confirm_delete_story));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            dialogInterface.dismiss();
//            Log.e(">>>", "" + storiesProgressView.getBaselineAlignedChildIndex());
            doAPIDeleteStory();
        });
        builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
            storiesProgressView.resume();
            mVideoView.resume();
            dialogInterface.dismiss();
        });
        builder.show();
    }

    private void doAPIDeleteStory() {
        if (Utils.isNetworkAvailable(activity, true, false)) {

            showProgressDialog(activity);
            ApiService service = ApiClient.UserApiClient(activity, Constants.token).create(ApiService.class);

            Call<ObjectResponseModel> call = service.apiDeleteStory(
                    Preferences.getStringName(Preferences.keyUserId),
                    lstStatus.get(selectedPosition).getStoryId(),
                    lstStatus.get(selectedPosition).getMedia().get(counter).getStoryMediaId(),
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
                            hideProgressDialog(activity);
                            Utils.showToast(activity, model.getResponseMsg());
                            Intent data = new Intent();
                            data.putExtra(Constants.INTENT_KEY_IS_UPDATE, true);
                            setResult(RESULT_OK, data);
                            finish();

                        } else {
                            storiesProgressView.resume();
                            mVideoView.resume();
                            hideProgressDialog(activity);
                            if (!model.getResponseMsg().isEmpty()) {
                                Utils.showAlert(activity, getString(R.string.app_name),
                                        model.getResponseMsg());
                            }
                        }

                    } else {
                        storiesProgressView.resume();
                        mVideoView.resume();
                        hideProgressDialog(activity);
                        Utils.showAlert(activity, activity.getResources().getString(R.string.app_name), activity.getResources().getString(R.string.something_went_wrong));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ObjectResponseModel> call, @NonNull Throwable t) {
                    storiesProgressView.resume();
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

    void videoSet(String url, int type) {
        Log.e("PK===>", counter + " , " + type);
        goNextBack = false;
        storiesProgressView.pause();
        storiesProgressView.setVisibility(View.GONE);

        showProgressDialog(activity);

        if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
            image.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            Picasso.get().load(url).into(image, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    hideProgressDialog(activity);
//                        progressBar.setVisibility(VideoView.INVISIBLE);
                    goNextBack = true;
                    // starts the video
                    storiesProgressView.setVisibility(View.VISIBLE);
                    storiesProgressView.startStories(counter);
                    Log.e("PK===>", "PLAY");
                }

                @Override
                public void onError(Exception e) {
                    hideProgressDialog(activity);
                }
            });

        } else {
            image.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);

            Uri uri = Uri.parse(url);
            mVideoView.setVideoURI(uri);

            mVideoView.setOnPreparedListener(
                    new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            hideProgressDialog(activity);
//                        progressBar.setVisibility(VideoView.INVISIBLE);
                            goNextBack = true;
                            // starts the video
                            storiesProgressView.setVisibility(View.VISIBLE);
                            mVideoView.start();
                            storiesProgressView.startStories(counter);
                            Log.e("PK===>", "PLAY");
                        }
                    });

            mVideoView.setOnCompletionListener(
                    new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {

                            // Return the video position to the start.
                            mVideoView.seekTo(0);
                        }
                    });
        }

    }

    @Override
    public void onNext() {
//        image.setImageResource(resources[++counter]);
        storiesProgressView.pause();
        storiesProgressView.setVisibility(View.GONE);

        if (goNextBack) {
            videoSet(resources.get(++counter), 1);
        }
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;
//        image.setImageResource(resources[--counter]);
        storiesProgressView.pause();
        storiesProgressView.setVisibility(View.GONE);
//        Picasso.get().load(resources[--counter]).into(image, new Callback() {
//            @Override
//            public void onSuccess() {
//                storiesProgressView.startStories(counter);
//                Log.e("PK 2===>", " SUCCESS");
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e("PK 2===>", " " + e.getLocalizedMessage() + ",," + e.getMessage());
//            }
//        });

        if (goNextBack) {
            videoSet(resources.get(--counter), 2);
        }
    }

    @Override
    public void onComplete() {
        Log.e("PK 9===>", "onComplete: " + lstStatus.size() + "-" + selectedPosition);
        if (tvName.getText().toString().equals(getString(R.string.your_story))) {
            finish();
        } else {
            selectedPosition++;
            if (selectedPosition < lstStatus.size()) {
                counter = 0;
                setStatusProfileData();
                
                showProgressDialog(activity);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getVideoDurations();
                    }
                }).start();

            } else {
                finish();
            }
        }

    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }


    private void getPreviousData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(Constants.INTENT_KEY_SELECTED_POSITION)) {
                selectedPosition = mBundle.getInt(Constants.INTENT_KEY_SELECTED_POSITION);
            }
            if (mBundle.containsKey(Constants.INTENT_KEY_LIST)) {
                lstStatus = (ArrayList<HomeDetailsResponseModel.StoryDtl>) mBundle.getSerializable(Constants.INTENT_KEY_LIST);
            }
            setStatusProfileData();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    getVideoDurations();
                }
            }).start();
        }
//        Logger.d("test_lstStatus_size", lstStatus.size());
    }

    private void setStatusProfileData() {
        Utils.setImageProfile(activity, lstStatus.get(selectedPosition).getProfile(), rivProfileImage);
        if (Preferences.getStringName(Preferences.keyUserId).equals(lstStatus.get(selectedPosition).getUserId())) {
            tvName.setText(getString(R.string.your_story));
            llPreview.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
            llComment.setVisibility(View.GONE);
        } else {
            ivDelete.setVisibility(View.GONE);
            llPreview.setVisibility(View.GONE);
            llComment.setVisibility(View.VISIBLE);
            tvName.setText(lstStatus.get(selectedPosition).getName());
        }
    }

    private void getVideoDurations() {
        if ((lstStatus != null && lstStatus.size() > 0) && (lstStatus.get(selectedPosition).getMedia() != null)) {
            resources = new ArrayList<>();
            durations = new long[lstStatus.get(selectedPosition).getMedia().size()];

            for (int j = 0; j < lstStatus.get(selectedPosition).getMedia().size(); j++) {
                if (lstStatus.get(selectedPosition).getMedia().get(j).getMedia().contains(".jpg") ||
                        lstStatus.get(selectedPosition).getMedia().get(j).getMedia().contains(".jpeg") ||
                        lstStatus.get(selectedPosition).getMedia().get(j).getMedia().contains(".png")) {
                    durations[j] = 5000L;
                } else {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(lstStatus.get(selectedPosition).getMedia().get(j).getMedia(), new HashMap<String, String>());
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInMillisec = Long.parseLong(time);
                    retriever.release();
                    long duration = convertMillieToHMmSs(timeInMillisec);
                    Log.e("PK===>" + duration, " " +
                            lstStatus.get(selectedPosition).getMedia().get(j).getMedia() + " , " + j);

                    if (duration == 1) {
                        durations[j] = 1000L;
                    } else if (duration == 2) {
                        durations[j] = 2000L;
                    } else if (duration == 3) {
                        durations[j] = 3000L;
                    } else if (duration == 4) {
                        durations[j] = 4000L;
                    } else if (duration == 5) {
                        durations[j] = 5000L;
                    } else if (duration == 6) {
                        durations[j] = 6000L;
                    } else if (duration == 7) {
                        durations[j] = 7000L;
                    } else if (duration == 8) {
                        durations[j] = 8000L;
                    } else if (duration == 9) {
                        durations[j] = 9000L;
                    } else if (duration == 10) {
                        durations[j] = 10000L;
                    } else {
                        durations[j] = 5000L;
                    }

                }


                resources.add(lstStatus.get(selectedPosition).getMedia().get(j).getMedia());
            }

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog(activity);

//                storiesProgressView.setStoriesCount(lstStatus.get(selectedPosition).getMedia().size());
                storiesProgressView.setStoriesCountWithDurations(durations);

                Log.e("KK: ",  resources.size() + "-" + counter);
                videoSet(resources.get(counter), 0);
            }
        });
    }

    private static long convertMillieToHMmSs(long millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        if (hour > 0) {
//            return String.format("%02d:%02d:%02d", hour, minute, second);
            return second;
        } else {
//            return String.format("%02d:%02d", minute, second);
            return second;
        }
    }

}