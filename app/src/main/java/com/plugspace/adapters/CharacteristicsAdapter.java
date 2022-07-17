package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.model.CharacteristicsModel;

import java.util.List;

public class CharacteristicsAdapter extends RecyclerView.Adapter<CharacteristicsAdapter.ViewHolder> {
    private Activity activity;
    private List<CharacteristicsModel> lstModel;
    private boolean isFriendsScore;

    public CharacteristicsAdapter(Activity activity, List<CharacteristicsModel> lstModel, boolean isFriendsScore) {
        this.activity = activity;
        this.lstModel = lstModel;
        this.isFriendsScore = isFriendsScore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.row_characteristics_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setVisibility(View.GONE);

        CharacteristicsModel model = lstModel.get(position);
        if (model != null) {
            holder.tvDesc.setText(model.getText());

            if (!isFriendsScore) {
                holder.tvTitle.setText(model.getName());
                holder.tvTitle.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return lstModel.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
        }
    }
}
