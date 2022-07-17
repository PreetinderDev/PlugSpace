package com.plugspace.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Preferences;
import com.plugspace.common.Utils;
import com.plugspace.model.MediaModel;

import java.util.ArrayList;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {
    Activity activity;
    ArrayList<MediaModel> lstModel;
    MyListener myListener;
    private String userId;


    public PreviewAdapter(Activity activity, ArrayList<MediaModel> lstModel, MyListener myListener, String userId) {
        this.activity = activity;
        this.lstModel = lstModel;
        this.myListener = myListener;
        this.userId = userId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPreviewDesc;
        ImageView ivPreviewImg, ivMore;
        private VideoView videoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPreviewDesc = itemView.findViewById(R.id.tvPreviewDesc);
            ivMore = itemView.findViewById(R.id.ivMore);
            ivPreviewImg = itemView.findViewById(R.id.ivPreviewImg);
            videoView = itemView.findViewById(R.id.videoView);
        }
    }

    public interface MyListener {
        void showPowerMenuClicked(int position, ImageView ivMore, MediaModel mediaModel, String type);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_preview, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!userId.equals(Preferences.getStringName(Preferences.keyUserId))) {
            holder.ivMore.setVisibility(View.GONE);
        }


        MediaModel model = lstModel.get(position);
        if (model != null) {
            if (model.getMediaType().equals(Constants.MEDIA_TYPE_VIDEO)) {
                holder.ivPreviewImg.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.VISIBLE);

                String pathVideo = "";
                if (model.getType().equalsIgnoreCase(Constants.PROFILE)) {
                    pathVideo = model.getProfile();
                } else if (model.getType().equalsIgnoreCase(Constants.FEED)) {
                    pathVideo = model.getFeedImage();
                }

                holder.videoView.setVideoURI(Uri.parse(pathVideo));
                holder.videoView.start();

            } else {
                holder.ivPreviewImg.setVisibility(View.VISIBLE);
                holder.videoView.setVisibility(View.GONE);

                String image = "";

                if (model.getType().equalsIgnoreCase(Constants.PROFILE)) {
                    image = model.getProfile();
                } else if (model.getType().equalsIgnoreCase(Constants.FEED)) {
                    image = model.getFeedImage();
                }
                Utils.setImageBg(activity,image,holder.ivPreviewImg);
            }

            // : 8/2/22 Pending: above below.

//            holder.ivPreviewImg.setVisibility(View.VISIBLE);
//            holder.videoView.setVisibility(View.GONE);
//
//            String image = "";
//
//            if (model.getType().equalsIgnoreCase(Constants.PROFILE)) {
//                image = model.getProfile();
//            } else if (model.getType().equalsIgnoreCase(Constants.FEED)) {
//                image = model.getFeedImage();
//            }
//
//            Utils.setImageBg(activity,image,holder.ivPreviewImg);

            if (!Utils.isValidationEmpty(model.getDescription())
                    && model.getType() != null && model.getType().equalsIgnoreCase(Constants.FEED)) {
                holder.tvPreviewDesc.setVisibility(View.VISIBLE);
                holder.tvPreviewDesc.setText(model.getDescription());
            } else {
                holder.tvPreviewDesc.setVisibility(View.GONE);
            }

            holder.ivMore.setOnClickListener(view -> myListener.showPowerMenuClicked(position, holder.ivMore,
                    lstModel.get(position), model.getType()));
        }

    }

    @Override
    public int getItemCount() {
        return lstModel.size();
    }

}
