package com.plugspace.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Utils;
import com.plugspace.model.AddPhotoModel;

import java.util.ArrayList;
import java.util.List;

public class AddPhotosAdapter extends RecyclerView.Adapter<AddPhotosAdapter.ViewHolder> {
    private Activity activity;
    private List<AddPhotoModel> lstModel;
    MyListener myListener;

    public AddPhotosAdapter(Activity activity, List<AddPhotoModel> lstModel, MyListener myListener) {
        this.activity = activity;
        this.lstModel = lstModel;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvPhoto1;
//        TextView tvNoteMsg2;
        ImageView /*ivAddPhoto,*/ ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvPhoto1 = itemView.findViewById(R.id.cvPhoto1);
//            tvNoteMsg2 = itemView.findViewById(R.id.tvNoteMsg2);
//            ivAddPhoto = itemView.findViewById(R.id.ivAddPhoto);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }
    }

    public interface MyListener {
        void mySlcHeightClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView0 = inflater.inflate(R.layout.row_add_photos, parent, false);
        return new AddPhotosAdapter.ViewHolder(chatView0);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddPhotoModel model = lstModel.get(position);

        if (model != null) {

            String path="file://"+model.getPhotoPath();
            Utils.setImageProfile(activity,path,holder.ivPhoto);

//            if (Utils.isValidationEmpty(model.getString())) {
//                holder.tvNoteMsg2.setVisibility(View.GONE);
//                holder.cvPhoto1.setVisibility(View.VISIBLE);
//            } else {
//                holder.tvNoteMsg2.setText(model.getString());
//                holder.tvNoteMsg2.setVisibility(View.VISIBLE);
//                holder.cvPhoto1.setVisibility(View.GONE);
//            }
//
//            if (model.getImageView() == null) {
//                holder.ivAddPhoto.setVisibility(View.VISIBLE);
//                holder.ivPhoto.setVisibility(View.GONE);
//            } else {
//                holder.ivAddPhoto.setVisibility(View.GONE);
//                holder.ivPhoto.setVisibility(View.VISIBLE);
//                Glide.with(activity)
//                        .asBitmap()
//                        .load(model.getImageView())
//                        .into(new CustomTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                holder.ivPhoto.setImageBitmap(resource);
//
//                            }
//
//                            @Override
//                            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                            }
//                        });
//            }


            holder.cvPhoto1.setOnClickListener(view -> {
//            if (lstModel != null && !Utils.isValidationEmpty(model.getImageView().toString())) {
//            } else {
//                myListener.mySlcHeightClicked(model.getId());
//
//            }

//                if (model.getId() >= 0) {
                    myListener.mySlcHeightClicked(position);
//                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lstModel.size();
    }

}
