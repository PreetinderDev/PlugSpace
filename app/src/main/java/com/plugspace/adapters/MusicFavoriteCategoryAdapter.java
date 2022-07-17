package com.plugspace.adapters;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.plugspace.R;
import com.plugspace.model.PreviewModel;

import java.util.ArrayList;

public class MusicFavoriteCategoryAdapter extends RecyclerView.Adapter<MusicFavoriteCategoryAdapter.ViewHolder> {
    Activity activity;
    ArrayList<PreviewModel> favoriteCategoryList;
    MyListener myListener;

    public MusicFavoriteCategoryAdapter(Activity activity, ArrayList<PreviewModel> favoriteCategoryList, MyListener myListener) {
        this.activity = activity;
        this.favoriteCategoryList = favoriteCategoryList;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMusicCategoryName;
        CardView cvCategory;
        ImageView ivMusicCategory;
        LinearLayout llCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusicCategoryName = itemView.findViewById(R.id.tvMusicCategoryName);
            cvCategory = itemView.findViewById(R.id.cvCategory);
            ivMusicCategory = itemView.findViewById(R.id.ivMusicCategory);
            llCategory = itemView.findViewById(R.id.llCategory);
        }
    }

    public interface MyListener {
        void myFavoriteCategory(int position, String identify);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_music_favorite, parent,false);
        return  new ViewHolder(chatView);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PreviewModel previewModel = favoriteCategoryList.get(position);
        String  strCategoryName = previewModel.getNotiDescriptions();
        holder.tvMusicCategoryName.setText(strCategoryName);
        Glide.with(activity)
                .asBitmap()
                .load(previewModel.getPreviewImg())
                .into(holder.ivMusicCategory);

        if (favoriteCategoryList.get(position).getSlcCategory()) {
            holder.tvMusicCategoryName.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            holder.llCategory.setBackground(activity.getResources().getDrawable(R.drawable.white_bg_10sdp_with_orange_border));
            holder.cvCategory.setOutlineSpotShadowColor(activity.getResources().getColor(R.color.colorOrange));
            slcCategory(strCategoryName, holder);
        } else {
            holder.tvMusicCategoryName.setTextColor(activity.getResources().getColor(R.color.colorDarkGray));
            holder.llCategory.setBackground(activity.getResources().getDrawable(R.drawable.white_bg_10sdp_with_gray_border));
            holder.cvCategory.setOutlineSpotShadowColor(activity.getResources().getColor(R.color.colorDarkGray));
        }

        holder.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.myFavoriteCategory(position, strCategoryName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteCategoryList.size();
    }

    public void slcCategory(String strCategoryName, ViewHolder holder) {
        if (strCategoryName.equalsIgnoreCase(activity.getString(R.string.exercise_songs))) {
            Glide.with(activity)
                    .asBitmap()
                    .load(R.drawable.ic_exercise_selected)
                    .into(holder.ivMusicCategory);

        } else if (strCategoryName.equalsIgnoreCase(activity.getString(R.string.relax_songs))) {
            Glide.with(activity)
                    .asBitmap()
                    .load(R.drawable.ic_relax_selected)
                    .into(holder.ivMusicCategory);

        } else if (strCategoryName.equalsIgnoreCase(activity.getString(R.string.cars_songs))) {
            Glide.with(activity)
                    .asBitmap()
                    .load(R.drawable.ic_cars_selected)
                    .into(holder.ivMusicCategory);

        } else if (strCategoryName.equalsIgnoreCase(activity.getString(R.string.wedding_songs))) {
            Glide.with(activity)
                    .asBitmap()
                    .load(R.drawable.ic_wedding_selected)
                    .into(holder.ivMusicCategory);

        } else if (strCategoryName.equalsIgnoreCase(activity.getString(R.string.regions_songs))) {
            Glide.with(activity)
                    .asBitmap()
                    .load(R.drawable.ic_regions_selected)
                    .into(holder.ivMusicCategory);
        }
    }

}
