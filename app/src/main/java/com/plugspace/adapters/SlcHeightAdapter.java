package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class SlcHeightAdapter extends RecyclerView.Adapter<SlcHeightAdapter.ViewHolder> {
    Activity activity;
    ArrayList<HeightModel> slcHeightList;
    MyListener myListener;

    public SlcHeightAdapter(Activity activity, ArrayList<HeightModel> slcHeightList, MyListener myListener) {
        this.activity = activity;
        this.slcHeightList = slcHeightList;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeight;
        CardView cvHeight;
        LinearLayout llSlcHeight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeight = itemView.findViewById(R.id.tvHeight);
            cvHeight = itemView.findViewById(R.id.cvHeight);
            llSlcHeight = itemView.findViewById(R.id.llSlcHeight);
        }
    }

    public interface MyListener {
        void mySlcHeightClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_height, parent,false);
        return  new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvHeight.setText(slcHeightList.get(position).getString());

        holder.cvHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.mySlcHeightClicked(position);
            }
        });

        if (slcHeightList.get(position).getBooleanSelected()) {
            holder.tvHeight.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            holder.llSlcHeight.setBackground(activity.getResources().getDrawable(R.drawable.white_bg_10sdp_with_orange_border));
        } else {
            holder.tvHeight.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            holder.llSlcHeight.setBackground(activity.getResources().getDrawable(R.drawable.white_10sdp));
        }
    }

    @Override
    public int getItemCount() {
        return slcHeightList.size();
    }

}
