package com.plugspace.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.common.Constants;
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChattingMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<LoginDataModel> chatting;
    Activity activity;
    String strSenderId;

    public ChattingMsgAdapter(Activity activity, ArrayList<LoginDataModel> chatting, String strSenderId) {
        this.chatting = chatting;
        this.activity = activity;
        this.strSenderId = strSenderId;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateTime, tvTextMsg, tvTime;
        ImageView ivSeenUnSeenMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvTextMsg = itemView.findViewById(R.id.tvTextMsg);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivSeenUnSeenMsg = itemView.findViewById(R.id.ivSeenUnSeenMsg);
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView tvDateTime, tvSubTextMsg, tvSubTime;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvSubTextMsg = itemView.findViewById(R.id.tvSubTextMsg);
            tvSubTime = itemView.findViewById(R.id.tvSubTime);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (chatting.get(position).getUserId() != null) {
            if (!chatting.get(position).getUserId().equals(strSenderId)) {
                return 1;
            }
        }

        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(activity);
//        switch (viewType) {
//            case 0:
//                View chatView = inflater.inflate(R.layout.row_msg_send, parent, false);
//                return new ViewHolder(chatView);
//            case 1:
//                View chatView1 = inflater.inflate(R.layout.row_msg_reply, parent, false);
//                return new ViewHolder1(chatView1);
//        }
//        View chatView0 = inflater.inflate(R.layout.row_msg_send, parent, false);
//        return new ViewHolder(chatView0);

        LayoutInflater inflater = LayoutInflater.from(activity);
        if (viewType == 1) {
            View chatView1 = inflater.inflate(R.layout.row_msg_reply, parent, false);
            return new ViewHolder1(chatView1);
        } else {
            View chatView = inflater.inflate(R.layout.row_msg_send, parent, false);
            return new ViewHolder(chatView);
        }
//        switch (viewType) {
//            case 0:
//
//            case 1:
//
//        }
//        View chatView0 = inflater.inflate(R.layout.row_msg_send, parent, false);
//        return new ViewHolder(chatView0);

//        int resource = R.layout.row_msg_send;
//        if (viewType == 1) {
//            resource = R.layout.row_msg_reply;
//        }
//        return new ViewHolder(LayoutInflater.from(activity).inflate(resource, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        LoginDataModel model = chatting.get(position);

        switch (holder.getItemViewType()) {
            case 0:
                final ViewHolder viewHolder = (ViewHolder) holder;

//                viewHolder.tvTextMsg.setText(model.getTextMsg());
                viewHolder.tvTextMsg.setText(model.getMessage());
//                viewHolder.tvTime.setText(model.getTime());
                viewHolder.tvTime.setText(Utils.milliSecToDateTimeFormat(model.getTime(), Constants.DATE_FORMAT_HH_MM_AA));
                String messageStatus = model.getMessage_status();

                if (!Utils.isValidationEmpty(messageStatus) && messageStatus.equals(Constants.FIREBASE_VALUE_MESSAGE_STATUS_2)) {
                    viewHolder.ivSeenUnSeenMsg.setImageResource(R.drawable.ic_seen_msg);
                } else {
                    viewHolder.ivSeenUnSeenMsg.setImageResource(R.drawable.ic_unseen_msg);
                }
//                if (string != null && string.equalsIgnoreCase("1")) {
//                    Glide.with(activity)
//                            .asBitmap()
//                            .load(R.drawable.ic_unseen_msg)
//                            .into(viewHolder.ivSeenUnSeenMsg);
//                } else if (string != null){
//                    Glide.with(activity)
//                            .asBitmap()
//                            .load(R.drawable.ic_seen_msg)
//                            .into(viewHolder.ivSeenUnSeenMsg);
//                }

                /*Glide.with(activity)
                        .asBitmap()
                        .load(R.drawable.ic_seen_msg)
                        .into(viewHolder.ivSeenUnSeenMsg);*/
//                setChatStartTiming(model);
                setChatStartTiming(chatting, model, position, viewHolder);

                break;

            case 1:
                final ViewHolder1 viewHolder1 = (ViewHolder1) holder;
//                ChattingMsgModel chattingModel = chatting.get(position);
                viewHolder1.tvSubTextMsg.setText(model.getMessage());
//                viewHolder1.tvSubTime.setText(chattingModel.getTime());
                viewHolder1.tvSubTime.setText(Utils.milliSecToDateTimeFormat(model.getTime(), Constants.DATE_FORMAT_HH_MM_AA));
                setChatStartTiming1(chatting, model, position, viewHolder1);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return chatting.size();
    }

    private void setChatStartTiming(List<LoginDataModel> lstModel, LoginDataModel model, int position, ViewHolder viewHolder) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String dateToStr = formatter.format(new Date());

        long msgDays;
        if (model.getTime() != null && !Utils.isValidationEmpty(model.getTime())) {
            if (model.getTime() != null) {
                long time = Long.parseLong(model.getTime());
                String date = Utils.getDate(time);
                msgDays = Long.parseLong(date);
            } else {
                msgDays = Long.parseLong(dateToStr);
            }

            Long currentDate = Long.valueOf(dateToStr);

            if (position == 0) {
                viewHolder.tvDateTime.setVisibility(View.VISIBLE);
//            viewHolder.tvDateTime.setVisibility(View.GONE);

                if (currentDate.equals(msgDays)) {
                    viewHolder.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_today));
                } else if (currentDate - 1 == msgDays) {
                    viewHolder.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_yesterday));
                } else {
                    viewHolder.tvDateTime.setText(Utils.getDateChat(Long.parseLong(model.getTime())));
                }

            } else {
                Long previous_time = null, current_time = null;

                if (lstModel.get(position - 1).getTime() != null) {
                    previous_time = Long.valueOf(Utils.getDate(Long.parseLong(lstModel.get(position - 1).getTime())));
                }

                if (lstModel.get(position).getTime() != null) {
                    current_time = Long.valueOf(Utils.getDate(Long.parseLong(lstModel.get(position).getTime())));
                }


                if (Objects.equals(previous_time, current_time)) {
                    viewHolder.tvDateTime.setVisibility(View.GONE);
//                viewHolder1.tvMyTimeStamp.setVisibility(View.VISIBLE);
//                viewHolder1.tvMyTimeStamp.setText(Utils.getDateChat(model.getTime()));
                } else {
                    if (currentDate.equals(msgDays)) {
                        viewHolder.tvDateTime.setVisibility(View.VISIBLE);
//                    viewHolder.tvDateTime.setVisibility(View.GONE);
                        viewHolder.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_today));

                    } else if (currentDate - 1 == msgDays) {
                        viewHolder.tvDateTime.setVisibility(View.VISIBLE);
//                    viewHolder.tvDateTime.setVisibility(View.GONE);
                        viewHolder.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_yesterday));

                    } else {
                        viewHolder.tvDateTime.setVisibility(View.VISIBLE);
//                    viewHolder.tvDateTime.setVisibility(View.GONE);

                        if (model.getTime() != null) {
                            String date = Utils.getDateChat(Long.parseLong(model.getTime()));
                            viewHolder.tvDateTime.setText(date);
                        }
                    }

                }
            }

        }else {
            viewHolder.tvDateTime.setVisibility(View.GONE);
        }
    }

    private void setChatStartTiming1(List<LoginDataModel> lstModel, LoginDataModel model, int position, ViewHolder1 viewHolder1) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String dateToStr = formatter.format(new Date());

        long msgDays;
        if (model.getTime() != null && !Utils.isValidationEmpty(model.getTime())) {
            if (model.getTime() != null) {
                long time = Long.parseLong(model.getTime());
                String date = Utils.getDate(time);
                msgDays = Long.parseLong(date);
            } else {
                msgDays = Long.parseLong(dateToStr);
            }

            Long currentDate = Long.valueOf(dateToStr);

            if (position == 0) {
                viewHolder1.tvDateTime.setVisibility(View.VISIBLE);
//            viewHolder1.tvDateTime.setVisibility(View.GONE);

                if (currentDate.equals(msgDays)) {
                    viewHolder1.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_today));
                } else if (currentDate - 1 == msgDays) {
                    viewHolder1.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_yesterday));
                } else {
                    viewHolder1.tvDateTime.setText(Utils.getDateChat(Long.parseLong(model.getTime())));
                }

            } else {
                Long previous_time = null, current_time = null;

                if (lstModel.get(position - 1).getTime() != null) {
                    previous_time = Long.valueOf(Utils.getDate(Long.parseLong(lstModel.get(position - 1).getTime())));
                }

                if (lstModel.get(position).getTime() != null) {
                    current_time = Long.valueOf(Utils.getDate(Long.parseLong(lstModel.get(position).getTime())));
                }


                if (Objects.equals(previous_time, current_time)) {
                    viewHolder1.tvDateTime.setVisibility(View.GONE);
//                viewHolder1.tvMyTimeStamp.setVisibility(View.VISIBLE);
//                viewHolder1.tvMyTimeStamp.setText(Utils.getDateChat(model.getTime()));
                } else {
                    if (currentDate.equals(msgDays)) {
                        viewHolder1.tvDateTime.setVisibility(View.VISIBLE);
//                    viewHolder1.tvDateTime.setVisibility(View.GONE);
                        viewHolder1.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_today));

                    } else if (currentDate - 1 == msgDays) {
                        viewHolder1.tvDateTime.setVisibility(View.VISIBLE);
//                    viewHolder1.tvDateTime.setVisibility(View.GONE);
                        viewHolder1.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_yesterday));

                    } else {
                        viewHolder1.tvDateTime.setVisibility(View.VISIBLE);
//                    viewHolder1.tvDateTime.setVisibility(View.GONE);

                        if (model.getTime() != null) {
                            String date = Utils.getDateChat(Long.parseLong(model.getTime()));
                            viewHolder1.tvDateTime.setText(date);
                        }
                    }

                }

            }
        }else {
            viewHolder1.tvDateTime.setVisibility(View.GONE);
        }
    }
}
