package com.plugspace.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.iamkdblue.videocompressor.VideoCompress;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mindorks.paracamera.Camera;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.customCamera.activity.VideoCameraActivity;
import com.plugspace.fragment.ChatFragment;
import com.plugspace.fragment.EditProfileFragment;
import com.plugspace.fragment.HomeFragment;
import com.plugspace.fragment.ScoreFragment;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;
import com.plugspace.model.ObjectResponseModel;
import com.plugspace.retrofitApi.ApiClient;
import com.plugspace.retrofitApi.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends BaseActivity implements View.OnClickListener {
    Activity activity;
    ImageView ivHome, ivScore, ivChat, ivProfile;
    LinearLayout llAddStory;
    private Camera camera;
    private Dialog storyFeedDialog = null;
    File imgFile;
    private Camera editProfileCamera;
    String strUserId = "";
    public static ArrayList<MediaModel> mediaDetailList = new ArrayList<>();
    public static ArrayList<MediaModel> mediaList = new ArrayList<>();
    private final int PICK_IMAGE_GALLERY = 300;
    private boolean isNotification = false;

    List<File> videoPathList;
    List<String> pathList;

    private final int VIDEO_CAPTURE_FROM_CAMREA = 2001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        videoPathList = new ArrayList<>();
        pathList = new ArrayList<>();
        activity = this;
        getPreviousData();
        Utils.setActiveUser(false);
        LoginDataModel dataModel = Preferences.GetLoginDetails();
        if (dataModel != null) {
            strUserId = dataModel.getUserId();
        }
        initView();
        initClick();

        EditProfileFragment.setOnEditProfileSaveClickListener(new Interfaces.OnEditProfileSaveClickListener() {
            @Override
            public void editProfileSaveClick() {
                Logger.d("test_edit_profile_save", "ok");
                ivProfile.performClick();
            }
        });


    }

    private void getPreviousData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(Constants.INTENT_KEY_IS_FROM)) {
                String isFrom = mBundle.getString(Constants.INTENT_KEY_IS_FROM);

                if (isFrom != null && isFrom.equals(Constants.IS_FROM_NOTIFICATION)) {
                    isNotification = true;
                }
            }
        }
    }

    private void initClick() {
        ivHome.setOnClickListener(this);
        ivScore.setOnClickListener(this);
        ivChat.setOnClickListener(this);
        ivProfile.setOnClickListener(this);
        llAddStory.setOnClickListener(this);
    }

    private void initView() {
        ivHome = findViewById(R.id.ivHome);
        ivScore = findViewById(R.id.ivScore);
        ivChat = findViewById(R.id.ivChat);
        ivProfile = findViewById(R.id.ivProfile);
        llAddStory = findViewById(R.id.llAddStory);

        slcOptions("1");
        LoadFragment(new HomeFragment(isNotification));
    }

    public void slcOptions(String position) {
//        FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_FOLDER_ACTIVE_USER).child(strUserId).setValue("0");

        ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_gray));
        ivScore.setImageDrawable(getResources().getDrawable(R.drawable.ic_score_gray));
        ivChat.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_gray));
        ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_gray));

        if (position.equalsIgnoreCase("1")) {
            ivHome.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
        } else if (position.equalsIgnoreCase("2")) {
            ivScore.setImageDrawable(getResources().getDrawable(R.drawable.ic_score));
        } else if (position.equalsIgnoreCase("3")) {
            ivChat.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat));
        } else if (position.equalsIgnoreCase("4")) {
            ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
        }
    }

    private void LoadFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flHomeContainer, fragment)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivHome:
                slcOptions("1");
                LoadFragment(new HomeFragment(false));
                break;

            case R.id.ivScore:
                slcOptions("2");
                LoadFragment(new ScoreFragment());
                break;

            case R.id.ivChat:
                slcOptions("3");
                LoadFragment(new ChatFragment());
                break;

            case R.id.ivProfile:
                slcOptions("4");
                LoadFragment(new EditProfileFragment());
                break;

            case R.id.llAddStory:
                checkStoragePermission();
                break;
        }
    }

    private void checkStoragePermission() {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            Dexter.withContext(activity)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                // do you work now
                           /* Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivity(intent);*/

//                            camera = new Camera.Builder()
//                                    .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
//                                    .setTakePhotoRequestCode(1)
//                                    .setImageFormat(Camera.IMAGE_JPEG)
//                                    .build(activity);
//                            try {
//                                camera.takePicture();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }

//                                OpenPhotoDialog();
                                openDialogSelectOption();
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
    }

//    private void OpenPhotoDialog() {
//        Constants.IS_HOME = true;
//
//        Utils.showCustomDialog(activity,
//                activity.getResources().getString(R.string.app_name),
//                activity.getString(R.string.msg_select_option),
//                activity.getString(R.string.gallery),
//                (dialog, which) -> {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    activity.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
//                }, activity.getString(R.string.camera),
//                (dialog, which) -> {
//                    editProfileCamera = new Camera.Builder()
//                            .resetToCorrectOrientation(true)
//                            .setTakePhotoRequestCode(1)
//                            .setImageFormat(Camera.IMAGE_JPEG)
//                            .build(activity);
//                    try {
//                        editProfileCamera.takePicture();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }, true, true);
//
//    }

    private void openDialogSelectOption() {
        Constants.IS_HOME = true;

        final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_select_option);
        dialog.setTitle("");

        dialog.findViewById(R.id.tvCapturePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

//                takePhotoFile = Utils.takePhotoIntent(activity);

                editProfileCamera = new Camera.Builder()
                        .resetToCorrectOrientation(true)
                        .setTakePhotoRequestCode(1)
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .build(activity);
                try {
                    editProfileCamera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.findViewById(R.id.tvSelectPhotoGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

//                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pickPhoto, Constants.REQUEST_CODE_PICK_IMAGE_GALLERY);

                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
            }
        });

        dialog.findViewById(R.id.tvCaptureVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

//                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, Constants.EXTRA_VIDEO_DURATION_LIMIT);
////                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory().getPath()+"videocapture_example.mp4");
//
//                startActivityForResult(takeVideoIntent, Constants.REQUEST_CODE_CAPTURE_VIDEO);

                Intent intent = new Intent(HomeActivity.this, VideoCameraActivity.class);
                openCameraResult.launch(intent);
            }
        });

        TextView tvSelectVideoGallery = dialog.findViewById(R.id.tvSelectVideoGallery);
        tvSelectVideoGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent();
                intent.setType(Constants.MIME_TYPE_VIDEO);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, tvSelectVideoGallery.getText().toString().trim()), Constants.REQUEST_CODE_SELECT_VIDEO_GALLERY);
            }
        });

        dialog.findViewById(R.id.tvCancel).setOnClickListener(view -> dialog.dismiss());


        dialog.show();

    }


    ActivityResultLauncher<Intent> openCameraResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        List<String> videoPath = data.getStringArrayListExtra("path");

                        if (videoPath != null) {
                            pathList = videoPath;
                            if (videoPath != null && !Utils.isValidationEmpty(videoPath.get(0))) {
                                imgFile = new File(videoPath.get(0));

                                for (String file : videoPath) {
                                    videoPathList.add(new File(file));
                                }

                                Logger.i("test_videoPath_capture", String.valueOf(videoPathList.size()));
                                if (imgFile.exists()) {
                                    checkPermission(imgFile, Constants.MIME_TYPE_VIDEO);
                                    Logger.d("test_videoPath_capture", imgFile.getPath());
                                }
                            }
                        }
                    }
                }
            });

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!Constants.IS_HOME) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
//                Bitmap bitmap = camera.getCameraBitmap();
//                if (bitmap != null) {
//                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                    String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/Camera";
//                    File dir = new File(file_path);
//                    if (!dir.exists())
//                        dir.mkdirs();
//                    String format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
//                    File file = new File(dir, format + ".png");
//                    FileOutputStream fo;
//                    try {
//                        fo = new FileOutputStream(file);
//                        fo.write(bytes.toByteArray());
//                        fo.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    imgFile = file;
//                    slcOptionDialog(imgFile);
//                } else {
//                    Toast.makeText(activity, "Picture not taken!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }

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
                            checkPermission(imgFile, Constants.MIME_TYPE_IMAGE);
                        }

                    } else {
                        Toast.makeText(activity, "Picture not taken!", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == PICK_IMAGE_GALLERY) {
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            imgFile = FileUtils.getFile(activity, selectedImage);

                            if (imgFile != null && imgFile.exists()) {
                                checkPermission(imgFile, Constants.MIME_TYPE_IMAGE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == Constants.REQUEST_CODE_CAPTURE_VIDEO) {
                    if (data != null) {
                        Uri uri = data.getData();
                        String videoPath = Utils.getPathFromUri(activity, uri);
                        if (videoPath != null && !Utils.isValidationEmpty(videoPath)) {
                            imgFile = new File(videoPath);

                            if (imgFile.exists()) {
                                checkPermission(imgFile, Constants.MIME_TYPE_VIDEO);
                                Logger.d("test_videoPath_capture", imgFile.getPath());
                            }
                        }
                    }
                } else if (requestCode == Constants.REQUEST_CODE_SELECT_VIDEO_GALLERY) {
                    if (data != null) {
                        Uri videoUri = data.getData();
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(activity, videoUri);
                        long duration = Long.parseLong(Objects.requireNonNull(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
                        retriever.release();
//                        Loggers.d("Duration", duration / 1000 + "");
                        if (duration / 1000 > Constants.EXTRA_VIDEO_DURATION_LIMIT) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(getString(R.string.app_name));
                            builder.setMessage(activity.getResources().getString(R.string.msg_select_max_duration_video_gallery, Constants.EXTRA_VIDEO_DURATION_LIMIT, Constants.EXTRA_VIDEO_DURATION_LIMIT));
                            builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> dialog.dismiss());
                            builder.show();
                        } else {
                            String videoPath = Utils.getPathFromUri(activity, videoUri);
                            if (videoPath != null && !Utils.isValidationEmpty(videoPath)) {
                                imgFile = new File(videoPath);

                                if (imgFile.exists()) {
                                    checkPermission(imgFile, Constants.MIME_TYPE_VIDEO);
                                    Logger.d("test_videoPath_gallery", imgFile.getPath());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkPermission(File fileImg, String mimeType) {
//        if (fileImg != null && fileImg.exists()) {
//            Dexter.withContext(activity)
//                    .withPermissions(
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    .withListener(new MultiplePermissionsListener() {
//                        @Override
//                        public void onPermissionsChecked(MultiplePermissionsReport report) {
//                            // check if all permissions are granted
//                            if (report.areAllPermissionsGranted()) {
//                                // do you work now
//
////                                showProgressDialog(activity);
//
//                                File fileDestination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + activity.getResources().getString(R.string.app_name) + "/" + activity.getResources().getString(R.string.add));
//                                if (!fileDestination.exists()) {
//                                    fileDestination.mkdirs();
//                                }
//
////                                if (lstModel == null) {
////                                    lstModel = new ArrayList<>();
////                                }
////                                lstModel.clear();
////
////                                for (int i = 0; i < images.size(); i++) {
//
////                                    //// Compress an image and return the file path of the new image
////                                    String filePath = SiliCompressor.with(activity).compress(images.get(i).path, fileDestination);
////                                    Logger.d("test_image_path", filePath);
////                                    lstModel.add(new AddPhotoModel(filePath));
//
////                                    File compressedImage = new Compressor.Builder(activity)
//////                                            .setMaxWidth(640)
//////                                            .setMaxHeight(480)
////                                            .setQuality(75)
////                                            .setCompressFormat(Bitmap.CompressFormat.PNG)
////                                            .setDestinationDirectoryPath(fileDestination.getPath())
////                                            .build()
////                                            .compressToFile(new File(images.get(i).path));
//
//
//                                File compressedImage = Compressor.getDefault(activity).compressToFile(fileImg);
//
//                                Logger.d("test_image_path", compressedImage.getPath());
////                                    lstModel.add(new AddPhotoModel(compressedImage.getPath()));
//
////                                }
////                                setSelectedImageToList();
//
//                                activity.runOnUiThread(() -> hideProgressDialog(activity));
//                                slcOptionDialog(compressedImage, mimeType);
//
//                            } else {
//                                hideProgressDialog(activity);
//                            }
//
////                        // check for permanent denial of any permission
////                        if (report.isAnyPermissionPermanentlyDenied()) {
////                            // permission is denied permanently, navigate user to app settings
////                        }
//                        }
//
//                        @Override
//                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                            token.continuePermissionRequest();
//                            hideProgressDialog(activity);
//                        }
//                    })
//                    .onSameThread()
//                    .check();
//        } else {
//            hideProgressDialog(activity);
//        }

        slcOptionDialog(fileImg, mimeType);
    }

    private void slcOptionDialog(File fileImg, String mimeType) {
        try {
            storyFeedDialog = new Dialog(activity, R.style.DialogTheme);
            storyFeedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(storyFeedDialog.getWindow()).setBackgroundDrawable(null);
            storyFeedDialog.setContentView(R.layout.dialog_story_feed);
            storyFeedDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

            final LinearLayout llStory = storyFeedDialog.findViewById(R.id.llStory);
            final LinearLayout llFeed = storyFeedDialog.findViewById(R.id.llFeed);
            mediaDetailList = new ArrayList<>();

            ImageView ivPreviewImg = storyFeedDialog.findViewById(R.id.ivPreviewImg);
            Glide.with(activity).load(fileImg.getPath()).placeholder(R.drawable.bg_place_holder_photo).error(R.drawable.bg_error_photo).into(ivPreviewImg);
//            Glide.with(activity).load(R.drawable.ic_profile).placeholder(R.drawable.bg_place_holder_photo).error(R.drawable.bg_error_photo).into(ivPreviewImg);

            llStory.setOnClickListener(v -> {
//                if (storyFeedDialog != null && storyFeedDialog.isShowing()) {
//                    storyFeedDialog.dismiss();
//                    storyFeedDialog = null;

//                MediaModel model = new MediaModel();
//                model.setMedia(fileImg.getPath());
//                mediaDetailList.add(model);
                mediaDetailList.clear();
                for (File file : videoPathList) {
                    MediaModel model = new MediaModel();
                    model.setFeedImage(file.getPath());
                    mediaDetailList.add(model);
                }

                CreateStory(fileImg, mimeType);

//                }
            });

            llFeed.setOnClickListener(v -> {
                if (storyFeedDialog != null && storyFeedDialog.isShowing()) {
                    storyFeedDialog.dismiss();
                    storyFeedDialog = null;


                    MediaModel model = new MediaModel();
                    if (pathList.isEmpty())
                        model.setMedia(fileImg.getPath());
                    else
                        model.setMedia(appendTwoVideos());
                    mediaDetailList.add(model);


                    startActivity(new Intent(activity, AddDescriptionActivity.class)
                            .putExtra(Constants.ARRAY_LIST, mediaDetailList)
                            .putExtra(Constants.INTENT_KEY_MIME_TYPE, mimeType)
                    );
                }
            });

            storyFeedDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String appendTwoVideos() {
        try {
            Movie[] inMovies = new Movie[videoPathList.size()];

            for (int i = 0; i < videoPathList.size(); i++) {
                inMovies[i] = MovieCreator.build(String.valueOf(pathList.get(i)));
            }

            List<Track> videoTracks = new LinkedList<>();
            List<Track> audioTracks = new LinkedList<>();

            for (Movie m : inMovies) {
                for (Track t : m.getTracks()) {
                    if (t.getHandler().equals("soun")) {
                        audioTracks.add(t);
                    }
                    if (t.getHandler().equals("vide")) {
                        videoTracks.add(t);
                    }
                }
            }

            Movie result = new Movie();

            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack(audioTracks
                        .toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack(videoTracks
                        .toArray(new Track[videoTracks.size()])));
            }

            BasicContainer out = (BasicContainer) new DefaultMp4Builder().build(result);

            @SuppressWarnings("resource")
            FileChannel fc = new RandomAccessFile(Environment.getExternalStorageDirectory() + "/wishbyvideo.mp4", "rw").getChannel();
            out.writeContainer(fc);
            fc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/wishbyvideo.mp4";
        return mFileName;
    }


    private void CreateStory(File mFile, String mimeType) {
        if (Utils.isNetworkAvailable(activity, true, false)) {
            showProgressDialog(activity);
            Log.e("TYPE CHECK ===>", mFile.getPath() + " , " + mFile.getAbsolutePath() + " , " + mFile.getName());

            if (mFile.getName().contains(".jpg") || mFile.getName().contains(".jpeg") || mFile.getName().contains(".png")) {
                List<MultipartBody.Part> storyFileList = new ArrayList<>();
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("media[]",
                        mFile.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), mFile));
                storyFileList.add(filePart);

                ApiService service = ApiClient.UserApiClient(activity, Constants.token)
                        .create(ApiService.class);
                Call<ObjectResponseModel> call = service.createStory(
                        Utils.convertValueToRequestBody(strUserId),
                        storyFileList);

                call.enqueue(new Callback<ObjectResponseModel>() {
                    @Override
                    public void onResponse(Call<ObjectResponseModel> call, Response<ObjectResponseModel> response) {
                        Log.e("inj", "ok");
                        Log.e("inj", response.toString());
                        hideProgressDialog(activity);

                        if (response.isSuccessful()) {
                            ObjectResponseModel model = response.body();
                            if (model != null) {
                                if (model.getResponseCode() == 1) {
                                    LoginDataModel loginDataModel = model.getData();

                                    if (loginDataModel != null) {
                                        mediaList = loginDataModel.getStoryMedia();
                                    }

                                    if (storyFeedDialog != null && storyFeedDialog.isShowing()) {
                                        storyFeedDialog.dismiss();
                                        storyFeedDialog = null;
                                    }

                                    Utils.showToast(activity, model.getResponseMsg());
                                    LoadFragment(new HomeFragment(false));


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
                    public void onFailure(Call<ObjectResponseModel> call, Throwable t) {
                        Log.e("Story Img Error", new Gson().toJson(t));
                        Log.e("Story Img Error:-", t.getMessage());

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

            } else {
                for (File path : videoPathList) {
                    int random = new Random().nextInt(100000) + 20;
                    String destPath;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                        destPath = getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + random + ".mp4";
                    } else {
                        destPath = Environment.getExternalStorageDirectory().toString() + "/" + random + ".mp4";
                    }

                    VideoCompress.compressVideoLow(path.getAbsolutePath(), destPath,
                            new VideoCompress.CompressListener() {
                                @Override
                                public void onStart() {

                                    //Util.writeFile(MainActivity.this, "Start at: " + new SimpleDateFormat("HH:mm:ss", getLocale()).format(new Date()) + "\n");
                                }

                                @Override
                                public void onSuccess(String compressVideoPath) {

                                    newFileList.add(new File(compressVideoPath));
                                    if (newFileList.size() == videoPathList.size()) {
                                        ApiService service = ApiClient.UserApiClient(activity, Constants.token)
                                                .create(ApiService.class);

                                        List<MultipartBody.Part> storyFileList = new ArrayList<>();

                                        if (newFileList.size() > 0) {
                                            for (File file : newFileList) {
                                                MultipartBody.Part filePart = MultipartBody.Part.createFormData("media[]",
                                                        file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                                                storyFileList.add(filePart);
//                    storyFileList.add(MultipartBody.Part.createFormData("media[]", file.getName(), requestBody));
                                            }
                                        }
                                        //            List<MultipartBody.Part> storyFileList = new ArrayList<>();
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFile);
//            storyFileList.add(MultipartBody.Part.createFormData("media[]",
//                    mFile.getName(), requestBody));

//            MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[]{};
//            surveyImagesParts[0] = Utils.getMultipartBody(activity, mFile, "media[]");

//            if (mediaDetailList != null && mediaDetailList.size() > 0) {
//
//                surveyImagesParts = new MultipartBody.Part[mediaDetailList
//                        .size()];
//
//                for (int i = 0; i < mediaDetailList.size(); i++) {
//
////                if (mediaDetailList.get(i) != null && mediaDetailList.get(i).getMedia() != null && !mediaDetailList.get(i).getMedia().startsWith("http")) {
//                    if (mediaDetailList.get(i) != null && mediaDetailList.get(i).getMedia() != null) {
//
//                        File file = new File(mediaDetailList.get(i).getMedia());
//
//                        if (file.exists()) {
//                            if (Utils.isValidationEmpty(mimeType)) {
//                                mimeType = Constants.MIME_TYPE_IMAGE;
//                            }
//                            RequestBody surveyBody = RequestBody.create(file, MediaType.parse(mimeType));
//                            surveyImagesParts[i] = MultipartBody.Part.createFormData("media[]",
//                                    file.getName(),
//                                    surveyBody);
//                        }
//                    }
//                }
//
//            }
                                        Call<ObjectResponseModel> call = service.createStory(
                                                Utils.convertValueToRequestBody(strUserId),
                                                storyFileList);

                                        call.enqueue(new Callback<ObjectResponseModel>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ObjectResponseModel> call, @NonNull Response<ObjectResponseModel> response) {
                                                Log.d("inj", "ok");
                                                Log.d("inj", response.toString());
                                                hideProgressDialog(activity);

                                                if (response.isSuccessful()) {
                                                    ObjectResponseModel model = response.body();
                                                    if (model != null) {
                                                        if (model.getResponseCode() == 1) {
                                                            LoginDataModel loginDataModel = model.getData();

                                                            if (loginDataModel != null) {
                                                                mediaList = loginDataModel.getStoryMedia();
                                                            }

                           /* if (mediaList != null && mediaList.size() > 0 || loginDataModel != null) {

                                startActivity(new Intent(activity, StatusActivity.class)
                                        .putExtra(Constants.ARRAY_LIST, mediaList)
                                        .putExtra(Constants.USER_ID, strUserId)
                                        .putExtra(Constants.FROM, "2")
                                        .putExtra(Constants.DATA_MODEL, loginDataModel));


                            }*/

                                                            if (storyFeedDialog != null && storyFeedDialog.isShowing()) {
                                                                storyFeedDialog.dismiss();
                                                                storyFeedDialog = null;
                                                            }

                                                            Utils.showToast(activity, model.getResponseMsg());
                                                            LoadFragment(new HomeFragment(false));


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
                                                Log.d("inj", new Gson().toJson(t));
                                                Log.d("inj", t.getMessage());

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

                                @Override
                                public void onFail() {
                                    Toast.makeText(HomeActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onProgress(float percent) {
                                }
                            });

                }
            }
        }
    }

    List<File> newFileList = new ArrayList<>();

    private class CallApiAsync extends AsyncTask<Void, Integer, List<File>> {

        String TAG = getClass().getSimpleName();

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected List<File> doInBackground(Void... arg0) {

            return newFileList;
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);
            Log.d(TAG + " onProgressUpdate", "You are in progress update ... " + a[0]);
        }

        protected void onPostExecute(List<File> newFileList) {

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Utils.setActiveUser(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.setActiveUser(false);
    }

//    private void compressVideo(File mFile, String mimeType) {
//        if (Utils.isNetworkAvailable(activity, true, false)) {
//            showProgressDialog(activity);
//
//
////            String destinationDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/" + activity.getResources().getString(R.string.folder_name_compress_video);
////
////            File f = new File(destinationDirectory);
////            if (!f.exists()) {
////                f.mkdirs();
////            }
////
////            try {
////                String filePath = SiliCompressor.with(activity).compressVideo(fileImg.getPath(), destinationDirectory);
////                File mFile = new File(filePath);
////                if (mFile.exists()) {
////                    CreateStory(mFile, mimeType);
////                } else {
////                    CreateStory(fileImg, mimeType);
////                }
////            } catch (URISyntaxException e) {
////                e.printStackTrace();
////                CreateStory(fileImg, mimeType);
////            }
//
////// FFMPEG start
//
//            String outputPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/" + activity.getResources().getString(R.string.folder_name_compress_video);
//
//            File destinationFile = new File(outputPath);
//            if (!destinationFile.exists()) {
//                destinationFile.mkdirs();
//            }
//
//
//            String[] cmd = {"-y", "-i", mFile.getPath(), "-s", "720x480", "-r", "25",
//                    "-vcodec", "mpeg4", "-b:v", "300k", "-b:a", "48000", "-ac", "2", "-ar", "22050", outputPath};
//
//            RxFFmpegInvoke.getInstance().runCommandRxJava(cmd).subscribe(new RxFFmpegSubscriber() {
//                @Override
//                public void onFinish() {
//                    Logger.d("test_compressVideo_onFinish", destinationFile.getPath());
////                    CreateStory(destinationFile, mimeType);
//                }
//
//                @Override
//                public void onProgress(int progress, long progressTime) {
//
//                }
//
//                @Override
//                public void onCancel() {
//                    Logger.d("test_compressVideo_onCancel", "onCancel()");
////                    CreateStory(mFile, mimeType);
//                }
//
//                @Override
//                public void onError(String message) {
//                    Logger.d("test_compressVideo_onError", message);
////                    CreateStory(mFile, mimeType);
//                }
//            });
//
////            FFmpeg.getInstance(activity).execute(cmd, new FFcommandExecuteResponseHandler() {
////                @Override
////                public void onSuccess(String message) {
////                    Logger.d("test_compressVideo_onSuccess", message);
////                    Logger.d("test_compressVideo_onSuccess", destinationFile.getPath());
////                    CreateStory(destinationFile, mimeType);
////                }
////
////                @Override
////                public void onProgress(String message) {
////
////                }
////
////                @Override
////                public void onFailure(String message) {
////                    Logger.d("test_compressVideo_onError", message);
////                    CreateStory(mFile, mimeType);
////                }
////
////                @Override
////                public void onStart() {
////
////                }
////
////                @Override
////                public void onFinish() {
////                    Logger.d("test_compressVideo_onFinish", destinationFile.getPath());
////                    CreateStory(destinationFile, mimeType);
////                }
////            });
//
//
//            //// FFMPEG end
//        }
//    }


}
