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

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Logger;
import com.plugspace.model.HomeDetailsResponseModel;
import com.plugspace.model.LoginDataModel;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    Activity activity;
    ArrayList<HomeDetailsResponseModel.StoryDtl> storyList;
    MyListener myListener;
    String strUserId;

    public StoryAdapter(Activity activity, ArrayList<HomeDetailsResponseModel.StoryDtl> storyList, String strUserId, MyListener myListener) {
        this.activity = activity;
        this.storyList = storyList;
        this.myListener = myListener;
        this.strUserId = strUserId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RoundedImageView rivProfileStory;
        LinearLayout llStoryStatus;
//        ImageView ivAddPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            rivProfileStory = itemView.findViewById(R.id.rivProfileStory);
            llStoryStatus = itemView.findViewById(R.id.llStoryStatus);
//            ivAddPhoto = itemView.findViewById(R.id.ivAddPhoto);
        }
    }

    public interface MyListener {
        void statusClicked(int position, String strOtherUserId, HomeDetailsResponseModel.StoryDtl storyDataModel);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_story, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeDetailsResponseModel.StoryDtl storyDataModel = storyList.get(position);

        if (storyDataModel != null) {
            Logger.d("test_user_id", storyDataModel.getUserId());
            if (storyDataModel.getUserId().equalsIgnoreCase(strUserId)) {
                holder.tvName.setText(activity.getString(R.string.your_story));
//                holder.rivProfileStory.setBackground(activity.getResources().getDrawable(R.drawable.round_orange_border_width_2));
                holder.rivProfileStory.setBackground(activity.getResources().getDrawable(R.drawable.round_orange_border));
            } else {
                holder.tvName.setText(storyDataModel.getName());

                if (storyDataModel.getIsShowStory().equals(Constants.isStory_1)) {
                    holder.rivProfileStory.setBackground(activity.getResources().getDrawable(R.drawable.round_dark_gray_border));
                } else {
                    holder.rivProfileStory.setBackground(activity.getResources().getDrawable(R.drawable.round_orange_border));
                }
            }
            String strOtherUserId = storyDataModel.getUserId();

            Glide.with(activity)
                    .load(storyDataModel.getProfile())
                    .placeholder(R.drawable.bg_place_holder_photo)
                    .error(R.drawable.ic_profile_placeholder)
                    .into(holder.rivProfileStory);



//        if (storyDataModel.getUserId().equalsIgnoreCase(strUserId)&&!storyDataModel.getIsStory().equals(Constants.isStory_1)) {
//            holder.ivAddPhoto.setVisibility(View.VISIBLE);
//        } else {
//            holder.ivAddPhoto.setVisibility(View.GONE);
//        }

            holder.llStoryStatus.setOnClickListener(view -> myListener.statusClicked(position, strOtherUserId, storyDataModel));
        }
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

}
