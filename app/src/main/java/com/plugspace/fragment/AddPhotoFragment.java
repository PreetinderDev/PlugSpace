package com.plugspace.fragment;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.erikagtierrez.multiple_media_picker.Gallery;
//import com.erikagtierrez.multiple_media_picker.Gallery;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.models.Image;
import com.google.gson.Gson;
//import com.iceteck.silicompressorr.SiliCompressor;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mindorks.paracamera.Camera;
import com.plugspace.R;
import com.plugspace.adapters.AddPhotosAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.SpacesItemDecoration;
import com.plugspace.common.Utils;
import com.plugspace.model.AddPhotoModel;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.MediaModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class AddPhotoFragment extends BaseFragment implements AddPhotosAdapter.MyListener/*, View.OnClickListener */ {
    Interfaces.OnGoBackClickListener backClickListener;
    private Activity activity;
    //    Button btnNext;
//    private Camera camera;
//    private File imgFile;
//    private final int PICK_IMAGE_GALLERY = 300;
    private List<AddPhotoModel> lstModel = new ArrayList<>();
    private AddPhotosAdapter mAdapter;
    private RecyclerView rvModel;
    //    CardView cvPhoto1, cvPhoto2;
//    ImageView ivAddPhoto1, ivAddPhoto2;
//    ImageView ivPhoto1, ivPhoto2;
//    String strImg = "";
//    int slcImgPosition = 0;
//    boolean ISFromRecycleView = false;
    private ScrollView svPhotos;
    //    private ArrayList<MediaModel> mediaDetailList = new ArrayList<>();
//    private final int OPEN_MEDIA_PICKER = 1;  // Request code

    public AddPhotoFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }
//    private TextView tvNoteMsg1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_photo, container, false);
        activity = getActivity();
//        initFillData();
        initView(view);
        initClick(view);
//        
        if (Constants.loginDataModel.getMediaDetail() != null &&
                Constants.loginDataModel.getMediaDetail().size() > 0) {

//            Constants.loginDataModel.setMediaDetail(mediaDetailList);
//            Logger.e("16/12 - slcPhotos - >", Constants.loginDataModel.getMediaDetail());

            if (lstModel == null) {
                lstModel = new ArrayList<>();
            }
            lstModel.clear();

            for (int i = 0; i < Constants.loginDataModel.getMediaDetail().size(); i++) {
                lstModel.add(new AddPhotoModel(Constants.loginDataModel.getMediaDetail().get(i).getImgFile().getPath()));
            }
        }
        setSelectedImageToList();
        return view;
    }

//    private void initFillData() {
//        lstModel.add(new AddPhotoModel(0, "", null));
//        lstModel.add(new AddPhotoModel(1, "", null));
//        lstModel.add(new AddPhotoModel(2, getString(R.string.note_add_full_size_body_pictures), null));
//        lstModel.add(new AddPhotoModel(3, getString(R.string.note_add_multiple_fully), null));
//        lstModel.add(new AddPhotoModel(4, "", null));
//        lstModel.add(new AddPhotoModel(5, "", null));
//        lstModel.add(new AddPhotoModel(6, "", null));
//    }

    private void initClick(View view) {
        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            if (lstModel != null && lstModel.size() > 0) {
                Constants.loginDataModel.setMediaDetail(null);

                ArrayList<MediaModel> mediaModels = new ArrayList<>();
                for (int i = 0; i < lstModel.size(); i++) {
                    MediaModel mediaModel = new MediaModel();
                    mediaModel.setImgFile(new File(lstModel.get(i).getPhotoPath()));
                    mediaModels.add(mediaModel);
                }

                Constants.loginDataModel.setMediaDetail(mediaModels);
                mAdapter = null;

                if (backClickListener != null) {
                    backClickListener.gobackClick(6);
                }
            } else {
                Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
                        activity.getResources().getString(R.string.lbl_select_photo));
            }
        });

        view.findViewById(R.id.cvPhoto1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        view.findViewById(R.id.cvPhoto2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        view.findViewById(R.id.cvPhoto3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        view.findViewById(R.id.cvPhoto4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        view.findViewById(R.id.cvPhoto5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        view.findViewById(R.id.cvPhoto6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        view.findViewById(R.id.cvPhoto7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

//        cvPhoto1.setOnClickListener(this);
//        cvPhoto2.setOnClickListener(this);

    }

    private void initView(View view) {
//        cvPhoto1 = view.findViewById(R.id.cvPhoto1);
//        ivAddPhoto1 = view.findViewById(R.id.ivAddPhoto1);
//        ivPhoto1 = view.findViewById(R.id.ivPhoto1);

//        cvPhoto2 = view.findViewById(R.id.cvPhoto2);
//        ivAddPhoto2 = view.findViewById(R.id.ivAddPhoto2);
//        ivPhoto2 = view.findViewById(R.id.ivPhoto2);

        svPhotos = view.findViewById(R.id.svPhotos);
        rvModel = view.findViewById(R.id.rvModel);

//        LinearLayoutManager layoutManager = new GridLayoutManager(activity, 2);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
//        rvModel.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//        rvModel.setLayoutManager(layoutManager);

        rvModel.setLayoutManager(new GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false));
//        mAdapter = new AddPhotosAdapter(activity, lstModel, AddPhotoFragment.this);
//        rvModel.setAdapter(mAdapter);

//        btnNext = view.findViewById(R.id.btnNext);
//        tvNoteMsg1 = view.findViewById(R.id.tvNoteMsg1);
    }

    private void setSelectedImageToList() {
        if (mAdapter == null) {
            mAdapter = new AddPhotosAdapter(activity, lstModel, AddPhotoFragment.this);
            rvModel.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        if (lstModel != null && lstModel.size() > 0) {
            svPhotos.setVisibility(View.GONE);
            rvModel.setVisibility(View.VISIBLE);
        } else {
            svPhotos.setVisibility(View.VISIBLE);
            rvModel.setVisibility(View.GONE);
        }
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
        TextView view = activity.findViewById(R.id.tvNumber6);
        view.setText(R.string._6);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg6), view);
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

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.cvPhoto1:
//                ISFromRecycleView = false;
//                strImg = Constants.ODD_NUM;
//                checkStoragePermission();
//                break;
//
//            case R.id.cvPhoto2:
//                strImg = Constants.EVEN_NUM;
//                ISFromRecycleView = false;
//                checkStoragePermission();
//                break;
//
//            case R.id.btnNext:
//                if (mediaDetailList != null && mediaDetailList.size() > 0) {
//                    if (backClickListener != null) {
//                        backClickListener.gobackClick(6);
//                    }
//                } else {
//                    Utils.showAlert(activity, activity.getResources().getString(R.string.app_name),
//                            activity.getResources().getString(R.string.lbl_select_photo));
//                }
//                break;
//        }
//    }

    @Override
    public void mySlcHeightClicked(int id) {
//        slcImgPosition = 0;
//        slcImgPosition = id;
//        ISFromRecycleView = true;
        checkStoragePermission();
    }

    private void checkStoragePermission() {
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
                            dialogSelectionOption();
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

    private void dialogSelectionOption() {
//        Utils.showCustomDialog(activity,
//                activity.getResources().getString(R.string.app_name),
//                activity.getString(R.string.msg_select_select_option),
//                activity.getString(R.string.gallery),
//                (dialog, which) -> {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    activity.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
//
////                    Intent intent = new Intent();
////                    intent.setType("image/*");
////                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
////                    intent.setAction(Intent.ACTION_GET_CONTENT);
////                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
//
//                }, activity.getString(R.string.camera),
//                (dialog, which) -> {
//                    camera = new Camera.Builder()
//                            .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
//                            .setTakePhotoRequestCode(1)
//                            .setImageFormat(Camera.IMAGE_JPEG)
//                            .build(activity);
//                    try {
//                        camera.takePicture();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }, true, true);


//        Intent intent = new Intent(activity, Gallery.class);
//        // Set the title
//        intent.putExtra("title", "Select media");
//        // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
//        intent.putExtra("mode", 2);
//        intent.putExtra("maxSelection", 7); // Optional
//        startActivityForResult(intent, OPEN_MEDIA_PICKER);


//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.requestCodeMultiplePhotoChooser);

        Intent intent = new Intent(activity, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, Constants.EXTRA_LIMIT_IMAGES);
        startActivityForResult(intent, Constants.REQUEST_CODE_MULTIPLE_PHOTO_CHOOSER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_MULTIPLE_PHOTO_CHOOSER && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

//            if (images != null && images.size() > 0) {
//                if (lstModel == null) {
//                    lstModel = new ArrayList<>();
//                }
//                lstModel.clear();
//
//                for (int i = 0; i < images.size(); i++) {
//                    lstModel.add(new AddPhotoModel(images.get(i).path));
//                }
//                setSelectedImageToList();
//
//            }
            showProgressDialog(activity);
            new Handler(Looper.getMainLooper()).postDelayed(() -> activity.runOnUiThread(() -> checkPermission(images)), 500);
        }
    }

    private void checkPermission(ArrayList<Image> images) {
        if (images != null && images.size() > 0) {
            Dexter.withContext(activity)
                    .withPermissions(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                // do you work now

//                                showProgressDialog(activity);

                                File fileDestination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + activity.getResources().getString(R.string.app_name) + "/" + activity.getResources().getString(R.string.add_photos));
                                if (!fileDestination.exists()) {
                                    fileDestination.mkdirs();
                                }

                                if (lstModel == null) {
                                    lstModel = new ArrayList<>();
                                }
                                lstModel.clear();

                                for (int i = 0; i < images.size(); i++) {

//                                    //// Compress an image and return the file path of the new image
//                                    String filePath = SiliCompressor.with(activity).compress(images.get(i).path, fileDestination);
//                                    Logger.d("test_image_path", filePath);
//                                    lstModel.add(new AddPhotoModel(filePath));

//                                    File compressedImage = new Compressor.Builder(activity)
////                                            .setMaxWidth(640)
////                                            .setMaxHeight(480)
//                                            .setQuality(75)
//                                            .setCompressFormat(Bitmap.CompressFormat.PNG)
//                                            .setDestinationDirectoryPath(fileDestination.getPath())
//                                            .build()
//                                            .compressToFile(new File(images.get(i).path));

                                    File compressedImage = Compressor.getDefault(activity).compressToFile(new File(images.get(i).path));

                                    Logger.d("test_image_path", compressedImage.getPath());
                                    lstModel.add(new AddPhotoModel(compressedImage.getPath()));

                                }
                                setSelectedImageToList();

                                activity.runOnUiThread(() -> hideProgressDialog(activity));

                            }else {
                                hideProgressDialog(activity);
                            }

//                        // check for permanent denial of any permission
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            // permission is denied permanently, navigate user to app settings
//                        }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                            hideProgressDialog(activity);
                        }
                    })
                    .onSameThread()
                    .check();
        }else {
            hideProgressDialog(activity);
        }
    }
}
