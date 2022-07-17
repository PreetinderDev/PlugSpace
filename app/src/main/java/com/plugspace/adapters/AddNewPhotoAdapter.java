package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Utils;
import com.plugspace.model.MediaModel;

import java.util.ArrayList;

public class AddNewPhotoAdapter extends RecyclerView.Adapter<AddNewPhotoAdapter.ViewHolder> {
    Activity activity;
    ArrayList<MediaModel> addNewPickList;
    MyListener myListener;
    String strMediaID = "";

    public AddNewPhotoAdapter(Activity activity, ArrayList<MediaModel> addNewPickList, MyListener myListener) {
        this.activity = activity;
        this.addNewPickList = addNewPickList;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfileImage, ivAddPhoto, ivRemoveImg;
        RelativeLayout rlAddPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ivAddPhoto = itemView.findViewById(R.id.ivAddPhoto);
            ivRemoveImg = itemView.findViewById(R.id.ivRemoveImg);
            rlAddPhoto = itemView.findViewById(R.id.rlAddPhoto);
        }
    }

    public interface MyListener {
        void addPhotoClicked(int position, String addPhoto, String mediaId);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_add_new_photos, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaModel pickModel = addNewPickList.get(position);

        if (pickModel != null /*&& pickModel.getType() != null &&
                pickModel.getType().equalsIgnoreCase(Constants.PROFILE)*/ &&
                !Utils.isValidationEmpty(pickModel.getProfile())) {
            if (pickModel.getProfile() != null && !Utils.isValidationEmpty(pickModel.getProfile())) {
//                Glide.with(activity)
//                        .asBitmap()
//                        .load(pickModel.getProfile())
//                        .into(holder.ivProfileImage);

                Utils.setImageProfile(activity,pickModel.getProfile(),holder.ivProfileImage);

                holder.ivRemoveImg.setVisibility(View.VISIBLE);
                holder.ivAddPhoto.setVisibility(View.GONE);
            } else {
                holder.ivRemoveImg.setVisibility(View.GONE);
                holder.ivAddPhoto.setVisibility(View.VISIBLE);
            }
        }else {
            holder.ivRemoveImg.setVisibility(View.GONE);
            holder.ivAddPhoto.setVisibility(View.VISIBLE);
        }

        if (pickModel != null && pickModel.getProfile() != null &&
                pickModel.getProfile().startsWith("http")) {
            strMediaID = pickModel.getMediaId();
        }

        holder.rlAddPhoto.setOnClickListener(view -> {
            if (pickModel != null && !Utils.isValidationEmpty(pickModel.getProfile())) {
            } else {
                myListener.addPhotoClicked(position, Constants.ADD_PHOTO, strMediaID);
            }
//            myListener.addPhotoClicked(position, Constants.ADD_PHOTO, strMediaID);

        });

        holder.ivRemoveImg.setOnClickListener(view -> myListener.addPhotoClicked(position, Constants.REMOVE_PHOTO, strMediaID));

    }

    @Override
    public int getItemCount() {
        return addNewPickList.size();
    }

}
