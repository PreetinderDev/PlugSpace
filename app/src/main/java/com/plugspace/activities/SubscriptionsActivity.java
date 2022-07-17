package com.plugspace.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.SubscriptionsAdapter;
import com.plugspace.common.Constants;
import com.plugspace.model.SubscriptionsModel;

import java.util.ArrayList;

public class SubscriptionsActivity extends BaseActivity implements SubscriptionsAdapter.MyListener {
    Activity activity;
    TextView tvTitleName;
    RecyclerView rvSubscriptions;
    SubscriptionsAdapter subscriptionsAdapter;
    ArrayList<SubscriptionsModel> subscriptionsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);
        activity = SubscriptionsActivity.this;
        initFillData();
        initView();
        initToolBar();
        initClick();
    }

    private void initFillData() {
        subscriptionsList.add(new SubscriptionsModel(getString(R.string.gold), getString(R.string._39_99), getString(R.string._6_months_subscriptions), false));
        subscriptionsList.add(new SubscriptionsModel(getString(R.string.plus), getString(R.string._19_99), getString(R.string._3_months_subscriptions), false));
        subscriptionsList.add(new SubscriptionsModel(getString(R.string.basic), getString(R.string._9_99), getString(R.string._1_months_subscriptions), false));
        subscriptionsList.get(Constants.SelectedPosition).setBooleanSelected(true);
    }

    private void initClick() {
        findViewById(R.id.btnContinue).setOnClickListener(view -> onBackPressed());
    }

    private void initToolBar() {
        tvTitleName = findViewById(R.id.tvTitleName);

        tvTitleName.setText(R.string.subscriptions);
        findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
        findViewById(R.id.ivShowImg).setVisibility(View.GONE);
        findViewById(R.id.ivImg).setVisibility(View.GONE);
        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());
    }

    private void initView() {
        rvSubscriptions = findViewById(R.id.rvSubscriptions);
        rvSubscriptions.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        subscriptionsAdapter = new SubscriptionsAdapter(activity, subscriptionsList, SubscriptionsActivity.this);
        rvSubscriptions.setAdapter(subscriptionsAdapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void mySubscriptionClicked(int position) {
        for (int i = 0; i < subscriptionsList.size(); i++) {
            subscriptionsList.get(i).setBooleanSelected(false);
        }
        subscriptionsList.get(position).setBooleanSelected(true);
        subscriptionsAdapter.notifyDataSetChanged();
    }
}
