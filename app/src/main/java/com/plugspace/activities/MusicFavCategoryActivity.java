package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.adapters.MusicFavoriteCategoryAdapter;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.common.SpacesItemDecoration;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.PreviewModel;

import java.util.ArrayList;

public class MusicFavCategoryActivity extends BaseActivity implements MusicFavoriteCategoryAdapter.MyListener {
    Activity activity;
    RecyclerView rvFavorite;
    ArrayList<PreviewModel> favoriteCategoryList = new ArrayList<>();
    MusicFavoriteCategoryAdapter favoriteCategoryAdapter;
    String strCategoryFrom = "";
    private LoginDataModel modelOtherPost = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_fav_category);

        activity = this;
        getIntentData();
        initFillData();
        initToolBar();
        initView();

    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.categoryListFrom)) {
            strCategoryFrom = intent.getStringExtra(Constants.categoryListFrom);
        }

        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(Constants.INTENT_KEY_MODEL)) {
                modelOtherPost = (LoginDataModel) mBundle.get(Constants.INTENT_KEY_MODEL);

                if (modelOtherPost != null) {
                    Logger.d("test_otherUserId_category",modelOtherPost.getUserId());
                }
            }
        }

    }

    private void initFillData() {
        favoriteCategoryList.clear();
        favoriteCategoryList.add(new PreviewModel(getString(R.string.exercise_songs), R.drawable.ic_exercise, false, Constants.MUSIC_TYPE_EXERCISE));
        favoriteCategoryList.add(new PreviewModel(getString(R.string.relax_songs), R.drawable.ic_relax, false, Constants.MUSIC_TYPE_RELAX));
        favoriteCategoryList.add(new PreviewModel(getString(R.string.cars_songs), R.drawable.ic_cars, false, Constants.MUSIC_TYPE_CARS));
        favoriteCategoryList.add(new PreviewModel(getString(R.string.wedding_songs), R.drawable.ic_wedding, false, Constants.MUSIC_TYPE_WEDDING));
        favoriteCategoryList.add(new PreviewModel(getString(R.string.regions_songs), R.drawable.ic_regions, false, Constants.MUSIC_TYPE_REGIONS));
        favoriteCategoryList.get(Constants.SelectedPosition).setSlcCategory(true);
    }

    private void initToolBar() {
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivShowImg = findViewById(R.id.ivShowImg);
        ImageView ivImg = findViewById(R.id.ivImg);
        TextView tvTitleName = findViewById(R.id.tvTitleName);

        tvTitleName.setText(R.string.favourite);
        ivBack.setVisibility(View.VISIBLE);
        ivShowImg.setVisibility(View.GONE);
        ivImg.setVisibility(View.GONE);

        ivBack.setOnClickListener(view -> onBackPressed());
    }

    private void initView() {
        rvFavorite = findViewById(R.id.rvFavorite);

        LinearLayoutManager layoutManager = new GridLayoutManager(activity, 2);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._5sdp);
        rvFavorite.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvFavorite.setLayoutManager(layoutManager);
        favoriteCategoryAdapter = new MusicFavoriteCategoryAdapter(activity, favoriteCategoryList, MusicFavCategoryActivity.this);
        rvFavorite.setAdapter(favoriteCategoryAdapter);
    }

    @Override
    public void myFavoriteCategory(int position, String identify) {
        for (int i = 0; i < favoriteCategoryList.size(); i++) {
            favoriteCategoryList.get(i).setSlcCategory(false);
        }
        favoriteCategoryList.get(position).setSlcCategory(true);

        favoriteCategoryAdapter.notifyDataSetChanged();

        new Handler(Looper.getMainLooper()).postDelayed(() -> startActivity(new Intent(activity, MusicListActivity.class)
                .putExtra(Constants.INTENT_KEY_MODEL, favoriteCategoryList.get(position))
                .putExtra(Constants.INTENT_KEY_MODEL_OTHER_POST, modelOtherPost)
        ), 200);

    }
}
