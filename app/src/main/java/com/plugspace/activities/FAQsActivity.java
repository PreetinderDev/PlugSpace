package com.plugspace.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.FAQsAdapter;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class FAQsActivity extends BaseActivity {
    Activity activity;
    RecyclerView recyclerView;
    ArrayList<HeightModel> faQsList = new ArrayList<>();
    FAQsAdapter adapter;
    TextView tvTitleName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        activity = FAQsActivity.this;
        initFillData();
        initToolBar();
        initView();
    }

    private void initToolBar() {
        tvTitleName = findViewById(R.id.tvTitleName);
        tvTitleName.setText(R.string.faq_s);

        findViewById(R.id.ivShowImg).setVisibility(View.GONE);
        findViewById(R.id.ivImg).setVisibility(View.GONE);
        findViewById(R.id.ivBack).setVisibility(View.VISIBLE);
        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());
    }

    private void initFillData() {    // TODO: 29-11-2021 Pending: @Client give me dynamic value set API through.
        faQsList.add(new HeightModel("1. What is plugspace?"));
        faQsList.add(new HeightModel("2. How do I reset my digital plugspace?"));
        faQsList.add(new HeightModel("3. If use plugspace lore ipsum?"));
        faQsList.add(new HeightModel("4. Why choose plugspace?"));
        faQsList.add(new HeightModel("5. What are the different plugspace?"));
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FAQsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setItems(faQsList);
    }
}
