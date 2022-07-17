package com.plugspace.adapters;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.model.SubscriptionsModel;

import java.util.ArrayList;

public class SubscriptionsAdapter extends RecyclerView.Adapter<SubscriptionsAdapter.ViewHolder> {
    Activity activity;
    ArrayList<SubscriptionsModel> subscriptionsList;
    MyListener myListener;

    public SubscriptionsAdapter(Activity activity, ArrayList<SubscriptionsModel> subscriptionsList, MyListener myListener) {
        this.activity = activity;
        this.subscriptionsList = subscriptionsList;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubName, tvDollarPrice, tvMonthlySub;
        CardView cvSubscription;
        LinearLayout llSubscriptions;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubName = itemView.findViewById(R.id.tvSubName);
            tvDollarPrice = itemView.findViewById(R.id.tvDollarPrice);
            tvMonthlySub = itemView.findViewById(R.id.tvMonthlySub);
            cvSubscription = itemView.findViewById(R.id.cvSubscription);
            llSubscriptions = itemView.findViewById(R.id.llSubscriptions);
        }
    }

    public interface MyListener {
        void mySubscriptionClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_subscriptions, parent,false);
        return  new ViewHolder(chatView);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvSubName.setText(subscriptionsList.get(position).getStrSubName());
        holder.tvDollarPrice.setText(subscriptionsList.get(position).getStrDollarPrice());
        holder.tvMonthlySub.setText(subscriptionsList.get(position).getStrMonthlySub());

        holder.cvSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.mySubscriptionClicked(position);
            }
        });

        if (subscriptionsList.get(position).getBooleanSelected()) {
            holder.tvSubName.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            holder.tvDollarPrice.setTextColor(activity.getResources().getColor(R.color.colorOrange));
            holder.tvMonthlySub.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            holder.cvSubscription.setOutlineSpotShadowColor(activity.getResources().getColor(R.color.colorOrange));
            holder.llSubscriptions.setBackground(activity.getResources().getDrawable(R.drawable.white_bg_10sdp_with_orange_border));
        } else {
            holder.tvSubName.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            holder.tvDollarPrice.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            holder.tvMonthlySub.setTextColor(activity.getResources().getColor(R.color.colorDarkGray));
            holder.cvSubscription.setOutlineSpotShadowColor(activity.getResources().getColor(R.color.colorDarkGray));
            holder.llSubscriptions.setBackground(activity.getResources().getDrawable(R.drawable.white_bg_10sdp_with_gray_border));
        }
    }

    @Override
    public int getItemCount() {
        return subscriptionsList.size();
    }

}
