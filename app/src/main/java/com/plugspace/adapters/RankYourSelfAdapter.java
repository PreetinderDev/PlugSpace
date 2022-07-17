package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;

import java.util.ArrayList;
import java.util.List;

public class RankYourSelfAdapter extends RecyclerView.Adapter<RankYourSelfAdapter.ViewHolder> {
    Activity activity;
    List<String> rankNameList;

    public RankYourSelfAdapter(Activity activity, List<String> rankNameList) {
        this.activity = activity;
        this.rankNameList = rankNameList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRankName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRankName = itemView.findViewById(R.id.tvRankName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_rank_list, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String strName = (position + 1) + ". " + rankNameList.get(position);
        holder.tvRankName.setText(strName);
    }

    @Override
    public int getItemCount() {
        return rankNameList.size();
    }

}
