package com.plugspace.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Utils;
import com.plugspace.model.MediaModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class StatusAdapter extends PagerAdapter {
    ArrayList<MediaModel> statusList;
    Activity activity;
    LayoutInflater mLayoutInflater;
    //    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private Interfaces.OnNextStoryClickListener onNextStoryClickListener;

    public void setOnNextStoryClickListener(Interfaces.OnNextStoryClickListener onNextStoryClickListener) {
        this.onNextStoryClickListener = onNextStoryClickListener;
    }

    public StatusAdapter(Activity activity, ArrayList<MediaModel> statusList) {
        this.statusList = statusList;
        this.activity = activity;
        mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return statusList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.row_status_list, container, false);

        // referencing the image view from the item.xml file
        ImageView ivStatusImg = itemView.findViewById(R.id.ivStatusImg);
        VideoView videoView = itemView.findViewById(R.id.videoView);
//        SimpleExoPlayerView videoView = itemView.findViewById(R.id.videoView);
//        SimpleExoPlayer player;
//        DefaultTrackSelector trackSelector;
//        ComponentListener componentListener;

//        componentListener = new ComponentListener(new Interfaces.OnPlayback() {
//            @Override
//            public void onPlayback(int playback) {
//                String stateString = "";
//
////                switch (playback) {
////                    case ExoPlayer.STATE_IDLE:
////                        stateString = "ExoPlayer.STATE_IDLE      -";
////                        break;
////                    case ExoPlayer.STATE_BUFFERING:
////                        stateString = "ExoPlayer.STATE_BUFFERING -";
////                        isBuffer = true;
////                        progress.setVisibility(View.VISIBLE);
////                        break;
////                    case ExoPlayer.STATE_READY:
////
////
////
////                        if(isPlayFirstTime){
////                            isPlayFirstTime=false;
////                            stateString = "ExoPlayer.STATE_READY     -";
////                            isBuffer = false;
////                            setVideoTimeBack();
////
////                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
////                                @Override
////                                public void run() {
////                                    activity.runOnUiThread(new Runnable() {
////                                        @Override
////                                        public void run() {
////                                            progress.setVisibility(View.GONE);
////
////                                            if (isPlay && player != null) {
////                                                pausePlayer();
////                                                ivPlayPause.setImageResource(R.drawable.ic_play);
////                                                ivPlayPause.setVisibility(View.VISIBLE);
////                                            }
////                                        }
////                                    });
////                                }
////                            }, 500);
////                        }else {
////                            stateString = "ExoPlayer.STATE_READY     -";
////                            isBuffer = false;
////                            progress.setVisibility(View.GONE);
////
//////                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//////                retriever.setDataSource(model.getVideo());
//////                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
////
////                /*long timeInMilliSec = player.getDuration();
////                String hms = *//*(TimeUnit.MILLISECONDS.toDays(timeInMilliSec)) + "day "
////                    + (TimeUnit.MILLISECONDS.toHours(timeInMilliSec) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(timeInMilliSec)) + "hh ")*//*
////                        (TimeUnit.MILLISECONDS.toMinutes(timeInMilliSec) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMilliSec)) + "mm "
////                                + (TimeUnit.MILLISECONDS.toSeconds(timeInMilliSec) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMilliSec)) + "sec"));
////                tvTime.setText(hms);*/
//////            -30mm 50sec
////                            setVideoTimeBack();
////                        }
////                        break;
////                    case ExoPlayer.STATE_ENDED:
////                        stateString = "ExoPlayer.STATE_ENDED     -";
////
//////                 next to back
////                        onBackPressed();
////
//////                next to video
//////                pos++;
//////                currentProcess(pos);
////                        break;
////                    default:
////                        stateString = "UNKNOWN_STATE             -";
////                        break;
////                }
//                Logger.d("ExopLayer", "changed state to " + stateString);
//            }
//        });
//        trackSelector = new DefaultTrackSelector();


        MediaModel model = statusList.get(position);
        if (model != null) {
            if (model.getMediaType().equals(Constants.MEDIA_TYPE_VIDEO)) {
                ivStatusImg.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
//
                String path = model.getMedia();
//                path = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"; // : Test: remove this line.
                videoView.setVideoURI(Uri.parse(path));
                videoView.start();

                videoView.setOnCompletionListener(mp -> {
//                    if (statusList.size() - 1 > position)
                    if (onNextStoryClickListener != null) {
                        onNextStoryClickListener.nextStoryClick(position);
                    }
                });

//                player = ExoPlayerFactory.newSimpleInstance(activity, trackSelector);
//                player.addListener(componentListener);
//                Logger.e("AA_S_PATH", path + " -- ");
//                Uri uri = Uri.parse(path);
//                MediaSource mediaSource = buildMediaSource(uri);
//                player.prepare(mediaSource, true, false);
//                videoView.setPlayer(player);
            } else {

//                Handler mHandler = new Handler(Looper.getMainLooper());
//                mHandler.postDelayed(() -> {
//                    activity.runOnUiThread(() -> {
//
//                        if (onNextStoryClickListener != null) {
//                            onNextStoryClickListener.nextStoryClick(position);
//                        }
//                    });
////                }, Constants.STORY_LINE_TIME_DELAY_MILLIS);
//                }, 5000);

                ivStatusImg.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);

                //// setting the image in the imageView
//                Glide.with(activity)
//                        .asBitmap()
//                        .placeholder(R.drawable.bg_place_holder_photo)
//                        .error(R.drawable.bg_error_photo)
//                        .load(statusList.get(position).getMedia())
//                        .into(ivStatusImg);

                Utils.setImageBg(activity, statusList.get(position).getMedia(), ivStatusImg);
            }
        }


        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((LinearLayout) object);
    }

//    private DataSource.Factory buildDataSourceFactory() {
//        return new DefaultDataSourceFactory(activity, BANDWIDTH_METER, buildHttpDataSourceFactory());
//    }
//
//    private MediaSource buildMediaSource(Uri uri) {
//
//        //return new HlsMediaSource(uri, mediaDataSourceFactory, 1800000, new Handler(), null);
//
//        return new ExtractorMediaSource(uri, buildDataSourceFactory(), new DefaultExtractorsFactory(), new Handler(), null);
//
//    }
//
//    private HttpDataSource.Factory buildHttpDataSourceFactory() {
//
//        String userAgent = Util.getUserAgent(activity, activity.getResources().getString(R.string.app_name));
//
//        /* return new DefaultHttpDataSourceFactory(userAgent, BANDWIDTH_METER);*/
//
//        return new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
//                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
//
//    }
}