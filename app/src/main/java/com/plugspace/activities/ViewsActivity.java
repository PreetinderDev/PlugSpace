package com.plugspace.activities;

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
import com.plugspace.adapters.ViewsAdapter;
import com.plugspace.common.Constants;
import com.plugspace.model.LoginDataModel;

import java.util.ArrayList;

public class ViewsActivity extends BaseActivity {
    Activity activity;
    RecyclerView rvViews;
    ViewsAdapter viewsAdapter;
    ArrayList<LoginDataModel> viewsList = new ArrayList<>();
    String strViewCount = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_views);

        activity = this;
        GetIntentData();
        initView();
        initToolBar();
    }

    private void GetIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.ARRAY_LIST) && intent.hasExtra(Constants.VIEW_COUNT)) {
            viewsList = (ArrayList<LoginDataModel>) intent.getSerializableExtra(Constants.ARRAY_LIST);
            strViewCount = intent.getStringExtra(Constants.VIEW_COUNT);
        }
    }

    private void initToolBar() {
        TextView tvTitleName = findViewById(R.id.tvTitleName);
        String strTitle = strViewCount + " | " + getResources().getString(R.string.views);
        tvTitleName.setText(strTitle);
        findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
        findViewById(R.id.ivShowImg).setVisibility(View.GONE);
        findViewById(R.id.ivImg).setVisibility(View.GONE);
        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());
    }

    private void initView() {
        LinearLayout llEmptyList = findViewById(R.id.llEmptyList);
        TextView tvEmptyList = findViewById(R.id.tvEmptyList);
        tvEmptyList.setText(activity.getResources().getString(R.string.empty_list_story_view));

        rvViews = findViewById(R.id.rvViews);
        rvViews.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        if (viewsList != null && viewsList.size() > 0) {
            rvViews.setVisibility(View.VISIBLE);
            llEmptyList.setVisibility(View.GONE);
            viewsAdapter = new ViewsAdapter(activity, viewsList);
            rvViews.setAdapter(viewsAdapter);
        } else {
            llEmptyList.setVisibility(View.VISIBLE);
            rvViews.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
