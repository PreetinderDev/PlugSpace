package com.plugspace.customCamera.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.plugspace.R;
import com.plugspace.customCamera.fragment.VideoFragment;

public class VideoCameraActivity extends AppCompatActivity {

    private static final String TAG = "rustAppMainAct";
    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_camera);

        if (hasPermission(getApplicationContext(), VIDEO_PERMISSIONS)) {
            if (null == savedInstanceState) {
                Log.d(TAG, "onCreate: load fragment");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, VideoFragment.newInstance()).commit();
            }
        } else {
            Log.d(TAG, "onCreate: requestPermissions");
            ActivityCompat.requestPermissions(this, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_VIDEO_PERMISSIONS && permissions.length == VIDEO_PERMISSIONS.length) {
            boolean gotPermission = true;
            for (int resCode : grantResults) {
                gotPermission = gotPermission && (PackageManager.PERMISSION_GRANTED == resCode);
            }
            if (gotPermission) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, VideoFragment.newInstance()).commitAllowingStateLoss();
            } else {
                Toast.makeText(getApplicationContext(), "Please grant the permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean hasPermission(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED !=
                    ActivityCompat.checkSelfPermission(context, permission)) {
                Log.e(TAG, "no permission: " + permission);
                return false;
            }
        }
        return true;
    }
}