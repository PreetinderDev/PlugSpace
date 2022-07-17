package com.plugspace.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.NotificationsAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class NotificationsActivity extends BaseActivity {
    private Activity activity;
    private TextView tvTitleName, tvEmptyList;
    private RecyclerView rvSubscriptions;
    private NotificationsAdapter notificationAdapter;
    public static ArrayList<LoginDataModel> lstModel = new ArrayList<>();
    private LinearLayout llEmptyList;
//    TextView tvNoDataFound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        activity = NotificationsActivity.this;
        GetIntentData();
        initView();
        initToolBar();
    }

    private void GetIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.ARRAY_LIST)) {
            lstModel = (ArrayList<LoginDataModel>) intent.getSerializableExtra(Constants.ARRAY_LIST);
        }
    }

    private void initToolBar() {
        tvTitleName = findViewById(R.id.tvTitleName);
        tvTitleName.setText(R.string.notification);

        findViewById(R.id.ivShowImg).setVisibility(View.GONE);
        findViewById(R.id.ivImg).setVisibility(View.GONE);
        findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());

    }

    private void initView() {
        llEmptyList = findViewById(R.id.llEmptyList);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        tvEmptyList.setText(activity.getResources().getString(R.string.empty_list_notification));

        rvSubscriptions = findViewById(R.id.rvSubscriptions);
        rvSubscriptions.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        if (lstModel != null && lstModel.size() > 0) {
            rvSubscriptions.setVisibility(View.VISIBLE);
            llEmptyList.setVisibility(View.GONE);

//            for (int i = 0; i < lstModel.size(); i++) {
//                getCreatedDateToSetMillis(lstModel.get(i), i);
//            }

            notificationAdapter = new NotificationsAdapter(activity, lstModel);
            rvSubscriptions.setAdapter(notificationAdapter);
        } else {
            llEmptyList.setVisibility(View.VISIBLE);
            rvSubscriptions.setVisibility(View.GONE);
        }
    }

//    private void getCreatedDateToSetMillis(LoginDataModel model, int position) {
////        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd");
////        String dateToStr = formatter.format(new Date());
//
//        if (model.getCreated_date() != null && !Utils.isValidationEmpty(model.getCreated_date())) {
//            if (model.getCreated_date() != null) {
//                long time;
//                try {
//                    time = Utils.customDateTimeFormat(model.getCreated_date(), Constants.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
//                    model.setTime(String.valueOf(time));
//                    lstModel.set(position, model);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
////
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();

        Utils.readNotification();
    }
}
