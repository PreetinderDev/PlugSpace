package com.plugspace.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.plugspace.R;

public class HelpActivity extends BaseActivity {
    Activity activity;
    Button btnContinue;
    TextView tvTitleName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        activity = HelpActivity.this;
        initView();
        initToolBar();
    }


    private void initToolBar() {
        tvTitleName = findViewById(R.id.tvTitleName);
        tvTitleName.setText(R.string.help);
        findViewById(R.id.ivShowImg).setVisibility(View.GONE);
        findViewById(R.id.ivImg).setVisibility(View.GONE);
        findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());
    }

    private void initView() {
        btnContinue = findViewById(R.id.btnContinue);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
