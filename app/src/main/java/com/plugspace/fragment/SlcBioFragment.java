package com.plugspace.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.plugspace.R;
import com.plugspace.adapters.SlcBioAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.GPSTracker;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Logger;
import com.plugspace.common.Utils;
import com.plugspace.model.HeightModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SlcBioFragment extends BaseFragment implements SlcBioAdapter.MyListener {
    Activity activity;
    Interfaces.OnGoBackClickListener backClickListener;
    ArrayList<HeightModel> slcBioList = new ArrayList<>();
    RecyclerView rvSlcBoi;
    SlcBioAdapter slcBioAdapter;
    Button btnNext;
    private Dialog locationDialog = null;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    GPSTracker gps;
    double latitude, longitude;
    int bioPosition = 0;

    public SlcBioFragment(Interfaces.OnGoBackClickListener activity) {
        backClickListener = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slc_bio, container, false);
        activity = getActivity();

        int hasPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            showLocationDialog();
        } else {
            CheckGPS();
        }
        initFillData();
        initViewData(view);
        initClick();
        if (!Utils.isValidationEmpty(Constants.loginDataModel.getGender())) {
            for (int i = 0; i < slcBioList.size(); i++) {
                slcBioList.get(i).setBooleanSelected(false);
            }
            slcBioList.get(bioPosition).setBooleanSelected(true);
            Constants.loginDataModel.setGender(Constants.loginDataModel.getGender());
            slcBioAdapter.notifyDataSetChanged();
            Logger.e("16/12 - slcBIO - >", Constants.loginDataModel.getGender());
        }
        return view;
    }

    private void CheckGPS() {
        if (Utils.isNetworkAvailable(activity, false, false)) {
            gps = new GPSTracker(activity);
            if (gps.isgpsenabled() && gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                getCityStateCountryZipFromLatLng();
            }
        }
    }

    private void getCityStateCountryZipFromLatLng() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 5); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Logger.d("test_addresses_response_full_address ", new Gson().toJson(addresses.toString()));

            String address = "";
            String city = "";
            String country = "";
            String postalCode = "";
            String knownName = "";
            String mThoroughfare = "";

            for (int i = 0; i < addresses.size(); i++) {
                if (!Utils.isValidationEmpty(addresses.get(i).getAddressLine(0)) && Utils.isValidationEmpty(address)) {
                    address = addresses.get(i).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                }

                if (!Utils.isValidationEmpty(addresses.get(i).getLocality()) && Utils.isValidationEmpty(city)) {
                    city = addresses.get(i).getLocality();
                }
                if (!Utils.isValidationEmpty(addresses.get(i).getCountryName()) && Utils.isValidationEmpty(country)) {
                    country = addresses.get(i).getCountryName();
                }
                if (!Utils.isValidationEmpty(addresses.get(i).getPostalCode()) && Utils.isValidationEmpty(postalCode)) {
                    postalCode = addresses.get(i).getPostalCode();
                }
                if (!Utils.isValidationEmpty(addresses.get(i).getFeatureName()) && Utils.isValidationEmpty(knownName)) {
                    knownName = addresses.get(i).getFeatureName(); // Only if available else return NULL
                }
                if (!Utils.isValidationEmpty(addresses.get(i).getThoroughfare()) && Utils.isValidationEmpty(mThoroughfare)) {
                    mThoroughfare = addresses.get(i).getThoroughfare(); // Only if available else return NULL
                }
            }

            String location = "";

            if (!Utils.isValidationEmpty(city) && !Utils.isValidationEmpty(country)) {
                location = city + ", " + country;
            } else if (!Utils.isValidationEmpty(city)) {
                location = city;
            } else if (!Utils.isValidationEmpty(country)) {
                location = country;
            }

            Logger.d("test_addresses_location", location);
            Constants.loginDataModel.setLocation(location);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void locationCheckPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                    showLocationDialog();
                    displayLocationSettingsRequest(activity);
                }, 400);
            }
        } else {
            if (!isLocationEnabled()) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                    showLocationDialog();
                    displayLocationSettingsRequest(activity);
                }, 400);
            } else {
                Constants.loginDataModel.setIsGeoLocation(Constants.IS_GEO_LOCATION_1);
                Logger.e("16/12", "location ENABLE - " + "1");
            }
        }
    }

    protected boolean isLocationEnabled() {
        String le = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) activity.getSystemService(le);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showLocationDialog() {
        try {
            locationDialog = new Dialog(activity, R.style.DialogTheme);
            locationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(locationDialog.getWindow()).setBackgroundDrawable(null);
            locationDialog.setContentView(R.layout.dialog_enable_location);
            locationDialog.setCancelable(false);
            locationDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            final Button btnEnable = locationDialog.findViewById(R.id.btnEnable);

            btnEnable.setOnClickListener(v -> {

                if (locationDialog != null && locationDialog.isShowing()) {
                    locationDialog.dismiss();
                    locationDialog = null;
                }
//                displayLocationSettingsRequest(activity);

                locationCheckPermissions();
            });
            locationDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        CheckGPS();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                    try {
                        status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Constants.loginDataModel.setIsGeoLocation(Constants.IS_GEO_LOCATION_1);
                        Logger.e("16/12", "location - " + "1");
                        break;
                    case Activity.RESULT_CANCELED:
                        Constants.loginDataModel.setIsGeoLocation(Constants.IS_GEO_LOCATION_0);
                        Logger.e("16/12", "location 2 - " + "0");
//                        displayLocationSettingsRequest(activity);
                        break;
                }
                break;
        }
    }

    private void initClick() {
        btnNext.setOnClickListener(view -> {
            int hasPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                CheckGPS();

                if (backClickListener != null) {
                    backClickListener.gobackClick(3);
                }
            } else {
                showLocationDialog();
            }

        });
    }

    private void initFillData() {
        slcBioList.clear();
        slcBioList.add(new HeightModel(getString(R.string.biologically_female), false));
        slcBioList.add(new HeightModel(getString(R.string.biologically_male), false));
        slcBioList.add(new HeightModel(getString(R.string.trans_male), false));
        slcBioList.add(new HeightModel(getString(R.string.trans_female), false));
        slcBioList.add(new HeightModel(getString(R.string.others), false));


        slcBioList.get(Constants.SelectedPosition).setBooleanSelected(true);
        Constants.loginDataModel.setGender(slcBioList.get(Constants.SelectedPosition).getString());
        Logger.e("16/12", "gender 2 - " + slcBioList.get(Constants.SelectedPosition).getString());

    }

    private void initViewData(View view) {
        btnNext = view.findViewById(R.id.btnNext);
        rvSlcBoi = view.findViewById(R.id.rvSlcBoi);
        rvSlcBoi.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        slcBioAdapter = new SlcBioAdapter(activity, slcBioList, SlcBioFragment.this);
        rvSlcBoi.setAdapter(slcBioAdapter);
    }

    @Override
    public void mySlcBioClicked(int position) {
        bioPosition = position;
        for (int i = 0; i < slcBioList.size(); i++) {
            slcBioList.get(i).setBooleanSelected(false);
        }
        slcBioList.get(position).setBooleanSelected(true);
        Constants.loginDataModel.setGender(slcBioList.get(position).getString());
        Logger.e("16/12", "gender - " + Constants.loginDataModel.getGender());
        slcBioAdapter.notifyDataSetChanged();
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
        TextView view = activity.findViewById(R.id.tvNumber3);
        view.setText(R.string._3);
        ChangeNumber(activity.findViewById(R.id.ivGrayBg3), view);
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
}
