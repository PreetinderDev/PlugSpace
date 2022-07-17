package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class SlcBioAdapter extends RecyclerView.Adapter<SlcBioAdapter.ViewHolder> {
    Activity activity;
    ArrayList<HeightModel> slcHeightList;
    MyListener myListener;

    public SlcBioAdapter(Activity activity, ArrayList<HeightModel> slcHeightList, MyListener myListener) {
        this.activity = activity;
        this.slcHeightList = slcHeightList;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlcBio;
        LinearLayout llSlcBio;
        ImageView ivTick;
        RelativeLayout rlSlcBio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlcBio = itemView.findViewById(R.id.tvSlcBio);
            llSlcBio = itemView.findViewById(R.id.llSlcBio);
            ivTick = itemView.findViewById(R.id.ivTick);
            rlSlcBio = itemView.findViewById(R.id.rlSlcBio);
        }
    }

    public interface MyListener {
        void mySlcBioClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_slc_bio, parent,false);
        return  new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSlcBio.setText(slcHeightList.get(position).getString());

        holder.llSlcBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.mySlcBioClicked(position);
            }
        });

        if (slcHeightList.get(position).getBooleanSelected()) {
            holder.ivTick.setVisibility(View.VISIBLE);
            holder.tvSlcBio.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            holder.rlSlcBio.setBackground(activity.getResources().getDrawable(R.drawable.white_bg_10sdp_with_orange_border));
        } else {
            holder.ivTick.setVisibility(View.GONE);
            holder.tvSlcBio.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            holder.rlSlcBio.setBackground(activity.getResources().getDrawable(R.drawable.white_10sdp));
        }
    }

    @Override
    public int getItemCount() {
        return slcHeightList.size();
    }

}
