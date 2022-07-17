package com.plugspace.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.plugspace.R;
import com.plugspace.activities.PreviewActivity;
import com.plugspace.common.Constants;
import com.plugspace.common.Interfaces;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;
import com.plugspace.model.PreviewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyChatListAdapter extends RecyclerView.Adapter<MyChatListAdapter.ViewHolder> {
    Activity activity;
    ArrayList<LoginDataModel> myChaList;
    MyListener myListener;

    public MyChatListAdapter(Activity activity, ArrayList<LoginDataModel> myChaList, MyListener myListener) {
        this.activity = activity;
        this.myChaList = myChaList;
        this.myListener = myListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvChatMsg, tvTimes, tvHowManyMsg;
        RoundedImageView rivChatProfileImg;
        LinearLayout llMyChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvChatMsg = itemView.findViewById(R.id.tvChatMsg);
            tvTimes = itemView.findViewById(R.id.tvTimes);
            tvHowManyMsg = itemView.findViewById(R.id.tvHowManyMsg);
            rivChatProfileImg = itemView.findViewById(R.id.rivChatProfileImg);
            llMyChat = itemView.findViewById(R.id.llMyChat);

        }
    }

    public interface MyListener {
        void myChatClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_my_chat_list, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoginDataModel myChatModel = myChaList.get(position);
        holder.tvName.setText(myChatModel.getName());
        holder.tvChatMsg.setText(myChatModel.getMessage());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String dateToStr = formatter.format(new Date());

        long msgDays;
        if (myChatModel.getTime() != null && !Utils.isValidationEmpty(myChatModel.getTime())) {
            if (myChatModel.getTime() != null) {
                long time = Long.parseLong(myChatModel.getTime());
                String date = Utils.getDate(time);
                msgDays = Long.parseLong(date);
            } else {
                msgDays = Long.parseLong(dateToStr);
            }
            Long currentDate = Long.valueOf(dateToStr);
//        Logger.debugOnLog("current_date_match", currentDate + " MsgDays " + msgDays);

            if (currentDate.equals(msgDays)) {
                holder.tvTimes.setText(Utils.getDateHH_MM_AA(Long.parseLong(myChatModel.getTime())));
            } else if (currentDate - 1 == msgDays) {
                holder.tvTimes.setText(activity.getResources().getString(R.string.hint_date_time_yesterday));
            } else {
//                String date = Utils.getDateMessage(Long.parseLong(myChatModel.getTime()));
                String date = Utils.getDateChat(Long.parseLong(myChatModel.getTime()));
//            Logger.debugOnLog("chat_day_date", date);
                holder.tvTimes.setText(date);
            }

//        holder.tvTimes.setText(Utils.milliSecToDateTimeFormat(myChatModel.getTime(), Constants.DATE_FORMAT_HH_MM_AA));
        }

        if (Utils.isValidationEmpty(myChatModel.getRead_count()) || myChatModel.getRead_count().equals(Constants.readCount_0)) {
            holder.tvHowManyMsg.setVisibility(View.INVISIBLE);
        } else {
            holder.tvHowManyMsg.setVisibility(View.VISIBLE);
            holder.tvHowManyMsg.setText(myChatModel.getRead_count());
        }
//        Glide.with(activity)
//                .asBitmap()
//                .placeholder(R.drawable.ic_profile_placeholder)
//                .load(myChatModel.getProfile())
//                .into(holder.rivChatProfileImg);

        Utils.setImageProfile(activity, myChatModel.getProfile(), holder.rivChatProfileImg);

//        activity.startActivity(new Intent(activity, PreviewActivity.class).putExtra(Constants.INTENT_KEY_USER_ID, userId));

        holder.llMyChat.setOnClickListener(view -> myListener.myChatClicked(position));

        holder.rivChatProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, PreviewActivity.class).putExtra(Constants.INTENT_KEY_USER_ID, myChatModel.getUserId()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return myChaList.size();
    }

}
