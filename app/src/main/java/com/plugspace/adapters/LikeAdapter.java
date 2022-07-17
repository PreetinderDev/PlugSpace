package com.plugspace.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.plugspace.R;
import com.plugspace.activities.PreviewActivity;
import com.plugspace.common.Constants;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.OtherProfile;
import com.plugspace.model.SavedProfileModel;

import java.util.ArrayList;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.ViewHolder> {
    Activity activity;
    ArrayList<LoginDataModel> likeList;
    private ArrayList<SavedProfileModel> lstSavedProfile;
    private int positionTab;

    public LikeAdapter(Activity activity, ArrayList<LoginDataModel> likeList, ArrayList<SavedProfileModel> lstSavedProfile, int positionTab) {
        this.activity = activity;
        this.likeList = likeList;
        this.lstSavedProfile = lstSavedProfile;
        this.positionTab = positionTab;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDes, tvTimes;
        RoundedImageView rivViewsImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDes = itemView.findViewById(R.id.tvDes);
            tvTimes = itemView.findViewById(R.id.tvTimes);
            rivViewsImg = itemView.findViewById(R.id.rivViewsImg);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_views, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(positionTab == 3){
            saveDataSet(lstSavedProfile.get(position).getOtherProfile(), holder);
        } else{

            likeDataSet(likeList.get(position), holder);
        }

    }

    private void likeDataSet(LoginDataModel model, ViewHolder holder) {
        if (model != null) {
            String strNameAge = model.getName() + ", " + model.getAge();

            holder.tvDes.setText(model.getJobTitle());
            if (!Utils.isValidationEmpty(strNameAge)) {
                holder.tvName.setText(strNameAge);
            }
            holder.tvTimes.setText(model.getDateTime());

            Glide.with(activity)
                    .asBitmap()
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .load(model.getProfile())
                    .into(holder.rivViewsImg);

            holder.rivViewsImg.setOnClickListener(v -> {
                String userId = "";
//                if (!Utils.isValidationEmpty(model.getLikeUserId())) {
                if (positionTab == 0) {
                    userId = model.getLikeUserId();
//                } else if (!Utils.isValidationEmpty(model.getViewUserId())) {
                } else if (positionTab == 1) {
                    userId = model.getViewUserId();

                } /*else if (positionTab == 3) {
                    userId = model.getViewUserId();

                }*/
                if (!Utils.isValidationEmpty(userId)) {
                    activity.startActivity(new Intent(activity, PreviewActivity.class).putExtra(Constants.INTENT_KEY_USER_ID, userId));
                }
            });
        }
    }

    private void saveDataSet(OtherProfile model, ViewHolder holder) {
        if (model != null) {
            String strNameAge = model.getName() + ", " + model.getAge();

            holder.tvDes.setText(model.getJobTitle());
            if (!Utils.isValidationEmpty(strNameAge)) {
                holder.tvName.setText(strNameAge);
            }
//            holder.tvTimes.setText(model.getDateTime());

            Glide.with(activity)
                    .asBitmap()
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .load(model.getProfile())
                    .into(holder.rivViewsImg);

            holder.rivViewsImg.setOnClickListener(v -> {
                String userId = "";
//                if (!Utils.isValidationEmpty(model.getLikeUserId())) {
              /*  if (positionTab == 0) {
                    userId = model.getLikeUserId();
//                } else if (!Utils.isValidationEmpty(model.getViewUserId())) {
                } else if (positionTab == 1) {
                    userId = model.getViewUserId();

                } */ if (positionTab == 3) {
                    userId = model.getUserId();

                }
                if (!Utils.isValidationEmpty(userId)) {
                    activity.startActivity(new Intent(activity, PreviewActivity.class).putExtra(Constants.INTENT_KEY_USER_ID, userId));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(positionTab == 3){
            return lstSavedProfile.size();

        } else {
            return likeList.size();
        }
    }

}
