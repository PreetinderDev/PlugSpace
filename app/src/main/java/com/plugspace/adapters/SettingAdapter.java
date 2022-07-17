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
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {
    Activity activity;
    ArrayList<HeightModel> settingsList;
    MyListener myListener;

    public SettingAdapter(Activity activity, ArrayList<HeightModel> settingsList, MyListener myListener) {
        this.activity = activity;
        this.settingsList = settingsList;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSettingOptions;
        LinearLayout llSlcSettingOptions;
        private ImageView ivTick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSettingOptions = itemView.findViewById(R.id.tvSettingOptions);
            llSlcSettingOptions = itemView.findViewById(R.id.llSlcSettingOptions);
            ivTick = itemView.findViewById(R.id.ivTick);
        }
    }

    public interface MyListener {
        void mySlcSettingOptionClicked(String identify, int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_setting, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String strSettingList = settingsList.get(position).getString();

        if (strSettingList.equals(activity.getResources().getString(R.string.logout))) {
            holder.ivTick.setVisibility(View.GONE);
        } else {
            holder.ivTick.setVisibility(View.VISIBLE);
        }

        holder.tvSettingOptions.setText(strSettingList);

        holder.llSlcSettingOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.mySlcSettingOptionClicked(strSettingList, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

}
