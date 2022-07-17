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

import java.util.ArrayList;

public class ViewsAdapter extends RecyclerView.Adapter<ViewsAdapter.ViewHolder> {
    Activity activity;
    ArrayList<LoginDataModel> viewsList;

    public ViewsAdapter(Activity activity, ArrayList<LoginDataModel> viewsList) {
        this.activity = activity;
        this.viewsList = viewsList;
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
        View chatView = inflater.inflate(R.layout.row_saved_profile, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoginDataModel model = viewsList.get(position);
        if (model != null) {
            holder.tvDes.setText(model.getJobTitle());
            holder.tvName.setText(model.getName());
            holder.tvTimes.setText(model.getDateTime());

//            Glide.with(activity)
//                    .asBitmap()
//                    .placeholder(R.drawable.ic_profile_placeholder)
//                    .load(model.getProfile())
//                    .into(holder.rivViewsImg);

            Utils.setImageProfile(activity,model.getProfile(),holder.rivViewsImg);

            holder.rivViewsImg.setOnClickListener(v -> activity.startActivity(new Intent(activity, PreviewActivity.class).putExtra(Constants.INTENT_KEY_USER_ID, model.getViewUserId())));
        }
    }

    @Override
    public int getItemCount() {
        return viewsList.size();
    }

}
