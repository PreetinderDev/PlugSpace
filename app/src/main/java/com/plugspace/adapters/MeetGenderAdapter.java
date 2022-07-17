package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class MeetGenderAdapter extends RecyclerView.Adapter<MeetGenderAdapter.ViewHolder> {
    Activity activity;
    ArrayList<HeightModel> slcHeightList;
    MyListener myListener;

    public MeetGenderAdapter(Activity activity, ArrayList<HeightModel> slcHeightList, MyListener myListener) {
        this.activity = activity;
        this.slcHeightList = slcHeightList;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenderName;
        CardView cvGender;
        LinearLayout llGender;
        ImageView ivGenderTick, ivGender;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenderName = itemView.findViewById(R.id.tvGenderName);
            cvGender = itemView.findViewById(R.id.cvGender);
            llGender = itemView.findViewById(R.id.llGender);
            ivGenderTick = itemView.findViewById(R.id.ivGenderTick);
            ivGender = itemView.findViewById(R.id.ivGender);
        }
    }

    public interface MyListener {
        void meetGenderClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_meet_gender, parent,false);
        return  new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvGenderName.setText(slcHeightList.get(position).getString());
        holder.ivGender.setImageResource(slcHeightList.get(position).getGenderImg());

        holder.cvGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.meetGenderClicked(position);
            }
        });

        if (slcHeightList.get(position).getBooleanSelected()) {
            holder.tvGenderName.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            holder.llGender.setBackground(activity.getResources().getDrawable(R.drawable.white_bg_10sdp_with_orange_border));
            holder.ivGenderTick.setVisibility(View.VISIBLE);
        } else {
            holder.tvGenderName.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            holder.llGender.setBackground(activity.getResources().getDrawable(R.drawable.white_10sdp));
            holder.ivGenderTick.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return slcHeightList.size();
    }

}
