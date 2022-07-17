package com.plugspace.customCamera.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.customCamera.CameraPreview;
import com.plugspace.customCamera.animation.ProgressBarAnimation;
import com.plugspace.customCamera.fragment.adapter.ThumbsAdapter;
import com.plugspace.customCamera.listener.SimpleTouchListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class VideoFragment extends Fragment {
    private static final String TAG = "rustAppVideoFrag";

    private View mRoot;

    private CameraPreview mCameraPreview;

    RecyclerView recyclerView;
    ThumbsAdapter thumbsAdapter;
    List<String> thumbList;

    private Camera mCamera;

    boolean recording = false;

    List<String> pathList;

    CountDownTimer timer;
    int progressSeconds = 0;
    ProgressBar progressBar;

    FrameLayout preview;
    CardView switchCamera;

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "frag onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "frag onCreateView");
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "frag onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mRoot = view;
        pathList = new ArrayList<>();
        thumbList = new ArrayList<>();


        recyclerView = view.findViewById(R.id.recycler_video);

        initAdapter();

        initCameraPreview();

        ImageView recordButton = view.findViewById(R.id.iv_thumb);
        progressBar = view.findViewById(R.id.progress_circular);
        switchCamera = view.findViewById(R.id.switch_camera);

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraPreview.switchCamera();
            }
        });


        recordButton.setOnTouchListener(new SimpleTouchListener() {

            @Override
            public void onDownTouchAction() {
                switchCamera.setVisibility(View.GONE);

                Log.i("Touch", "down");
                updateProgress();
                mCameraPreview.startRecording(getContext());
                startrecording();
            }

            @Override
            public void onUpTouchAction() {
                Log.i("Touch", "up");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopRecording();
                    }
                },500);

                // do something when the down touch is released on the View
            }

            @Override
            public void onCancelTouchAction() {
                Log.i("Touch", "cancel");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopRecording();
                    }
                },500);
                // do something when the down touch is canceled
                // (e.g. because the down touch moved outside the bounds of the View
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: 销毁预览");
        mCameraPreview = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 回到前台");
        if (null == mCameraPreview) {
            initCameraPreview();
        }
    }

    private void initCameraPreview() {
        mCameraPreview = new CameraPreview(getContext());
        preview = mRoot.findViewById(R.id.framelayout);
        preview.addView(mCameraPreview);
    }

    private void stopRecording() {
        // Let's initRecorder so we can record again
        if (mCameraPreview != null)
            mCameraPreview.stopRecording();
        timer.cancel();

        Intent intent = new Intent();
        if (pathList.size() <= 0) {
            pathList.add(mCameraPreview.path);
        }
        if (isVisible()) {
            intent.putExtra("path", (ArrayList<String>) pathList);
//        intent.putExtra("path",mCameraPreview.path);
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
//        Intent i = new Intent(getActivity(), VideoPlayerActivty.class);
//
////        if (pathList.size() > 0) {
////            i.putExtra("path", appendTwoVideos());
////        } else {
//        i.putExtra("path", mCameraPreview.path);
////        }
//
//        startActivity(i);
    }

    private void startrecording() {
        try {

            timer = new CountDownTimer(21000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                    Log.i("second", String.valueOf(seconds));

                    if (seconds == 5 || seconds == 10 || seconds == 15 || seconds == 0) {
                        if (seconds != 0) {
                            updateProgress();
                            mCameraPreview.stopRecording();
                            pathList.add(mCameraPreview.path);
                            mCameraPreview.startRecording(getContext());
                        }

                        Log.i("second", String.valueOf(seconds));
                        takeScreenshot();
                    }

                }

                @Override
                public void onFinish() {
                    stopRecording();
                }
            }.start();


        } catch (Exception e) {
            String message = e.getMessage();
            Log.i(null, "Problem Start" + message);
        }
    }

    private void updateProgress() {
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 0, 100);
        anim.setDuration(5000);
        progressBar.startAnimation(anim);
    }

    private void initAdapter() {
        thumbsAdapter = new ThumbsAdapter(getActivity(), thumbList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(thumbsAdapter);
    }


    private void takeScreenshot() {
        Date now = new Date();
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath;
            int random = new Random().nextInt(100000) + 20;


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mPath = getActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + random + ".jpg";
            } else {
                mPath = Environment.getExternalStorageDirectory().toString() + "/" + random + ".jpg";
            }
            // create bitmap screen capture
            View v1 = mCameraPreview;
            v1.setDrawingCacheEnabled(true);
            Bitmap tempBmp = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            PixelCopy.OnPixelCopyFinishedListener listener = copyResult -> {
                // success/failure callback
            };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                PixelCopy.request((SurfaceView) v1, tempBmp, listener, v1.getHandler());
            }
            // visualize the retrieved bitmap on your imageview

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            tempBmp.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            thumbList.add(imageFile.getPath());
            thumbsAdapter.notifyDataSetChanged();
        } catch (Throwable e) {
            Log.i("error", e.getMessage());
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}

