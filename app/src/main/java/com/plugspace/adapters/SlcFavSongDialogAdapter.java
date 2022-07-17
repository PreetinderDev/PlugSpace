package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.model.PreviewModel;

import java.util.ArrayList;

public class SlcFavSongDialogAdapter extends RecyclerView.Adapter<SlcFavSongDialogAdapter.ViewHolder> {
    Activity activity;
    ArrayList<PreviewModel> favCategoryList;
    MyListener myListener;

    public SlcFavSongDialogAdapter(Activity activity, ArrayList<PreviewModel> favCategoryList, MyListener myListener) {
        this.activity = activity;
        this.favCategoryList = favCategoryList;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        LinearLayout llSlcCategory;
        ImageView ivCheck;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            llSlcCategory = itemView.findViewById(R.id.llSlcCategory);
            ivCheck = itemView.findViewById(R.id.ivCheck);
        }
    }

    public interface MyListener {
        void mySlcFavSongCategory(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_fav_song_slc_category, parent,false);
        return  new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvCategoryName.setText(favCategoryList.get(position).getSubTitle());

        if (favCategoryList.get(position).getSlcCategory()) {
            holder.tvCategoryName.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.tvCategoryName.setTextColor(activity.getResources().getColor(R.color.colorDarkGray));
            holder.ivCheck.setVisibility(View.INVISIBLE);
        }

        holder.llSlcCategory.setOnClickListener(view -> myListener.mySlcFavSongCategory(position));
    }

    @Override
    public int getItemCount() {
        return favCategoryList.size();
    }

}
