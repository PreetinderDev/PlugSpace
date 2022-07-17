package com.plugspace.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
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
import com.plugspace.common.Utils;
import com.plugspace.model.LoginDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<LoginDataModel> notificationList;

    public NotificationsAdapter(Activity activity, ArrayList<LoginDataModel> notificationList) {
        this.activity = activity;
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDateTime, tvTime, tvNotificationText;
        RoundedImageView rivNotificationImg;
        private LinearLayout llMainClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNotificationText = itemView.findViewById(R.id.tvNotificationText);
            rivNotificationImg = itemView.findViewById(R.id.rivNotificationImg);
            llMainClick = itemView.findViewById(R.id.llMainClick);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View chatView = inflater.inflate(R.layout.row_notifications, parent, false);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoginDataModel model = notificationList.get(position);

        if (model != null) {
            String first = "<font color='#FA5A20'>" + "@" + model.getName() + "</font>";
            String next = "<font color='#000000'>" + model.getMessage() + "</font>";
            String sourceString = "<b>" + first + "</b> " + next;
            holder.tvNotificationText.setText(Html.fromHtml(sourceString));


            // : 7/2/22 Pending: below 3 line process manage start.

//            String strTime = model.getDateTime() + " ago";
//            holder.tvTime.setText(strTime);
//            holder.tvTime.setText(model.getAge());
            // : 7/2/22 Pending: above 3 line process manage end.


            setDayStartDate(notificationList, model, position, holder);


//            Glide.with(activity)
//                    .asBitmap()
//                    .placeholder(R.drawable.ic_profile_placeholder)
//                    .load(model.getProfile())
//                    .into(holder.rivNotificationImg);

            Utils.setImageProfile(activity,model.getProfile(),holder.rivNotificationImg);

            holder.llMainClick.setOnClickListener(v -> activity.startActivity(new Intent(activity, PreviewActivity.class).putExtra(Constants.INTENT_KEY_USER_ID, model.getOther_id())));
        }

    }

//    private void setCreatedTime(ArrayList<LoginDataModel> lstModel, LoginDataModel model, int position, ViewHolder holder) {
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd");
//        String dateToStr = formatter.format(new Date());
//
//        long msgDays;
//        if (model.getCreated_date() != null && !Utils.isValidationEmpty(model.getCreated_date())) {
//            if (model.getCreated_date() != null) {
//                long time = 0;
//                try {
//                    time = Utils.customDateTimeFormat(model.getCreated_date(), Constants.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
//                    model.setCreated_date(String.valueOf(time));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                String date = Utils.getDate(time);
//                msgDays = Long.parseLong(date);
//            } else {
//                msgDays = Long.parseLong(dateToStr);
//            }
//
//            Long currentDate = Long.valueOf(dateToStr);
//
//            if (position == 0) {
////                holder.tvTime.setVisibility(View.VISIBLE);
//
//                if (currentDate.equals(msgDays)) {
//                    holder.tvTime.setText(activity.getResources().getString(R.string.hint_date_time_today));
//                } else if (currentDate - 1 == msgDays) {
//                    holder.tvTime.setText(activity.getResources().getString(R.string.hint_date_time_yesterday));
//                } else {
//                    holder.tvTime.setText(Utils.getDateChat(Long.parseLong(model.getCreated_date())));
//                }
//
//            } else {
//                Long previous_time = null, current_time = null;
//
//                if (lstModel.get(position - 1).getCreated_date() != null) {
//                    previous_time = Long.valueOf(Utils.getDate(Long.parseLong(lstModel.get(position - 1).getCreated_date())));
//                }
//
//                if (lstModel.get(position).getCreated_date() != null) {
//                    current_time = Long.valueOf(Utils.getDate(Long.parseLong(lstModel.get(position).getCreated_date())));
//                }
//
//
//                if (Objects.equals(previous_time, current_time)) {
////                    holder.tvTime.setVisibility(View.GONE);
//                } else {
//                    if (currentDate.equals(msgDays)) {
////                        holder.tvTime.setVisibility(View.VISIBLE);
//                        holder.tvTime.setText(activity.getResources().getString(R.string.hint_date_time_today));
//
//                    } else if (currentDate - 1 == msgDays) {
////                        holder.tvTime.setVisibility(View.VISIBLE);
//                        holder.tvTime.setText(activity.getResources().getString(R.string.hint_date_time_yesterday));
//
//                    } else {
////                        holder.tvTime.setVisibility(View.VISIBLE);
//
//                        if (model.getCreated_date() != null) {
//                            String date = Utils.getDateChat(Long.parseLong(model.getCreated_date()));
//                            holder.tvTime.setText(date);
//                        }
//                    }
//
//                }
//            }
//
//        } else {
////            holder.tvTime.setVisibility(View.GONE);
//        }
//    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    private void setDayStartDate(ArrayList<LoginDataModel> lstModel, LoginDataModel model, int position, ViewHolder holder) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String dateToStr = formatter.format(new Date());

        long msgDays;
        if (model.getTime() != null && !Utils.isValidationEmpty(model.getTime())) {
            if (model.getTime() != null) {
//                long time = 0;
//                try {
//                    time = Utils.customDateTimeFormat(model.getCreated_date(), Constants.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);
//                    model.setCreated_date(String.valueOf(time));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                msgDays = Long.parseLong(Utils.getDate(time));

                long time = Long.parseLong(model.getTime());
                String date = Utils.getDate(time);
                msgDays = Long.parseLong(date);
            } else {
                msgDays = Long.parseLong(dateToStr);
            }

            Long currentDate = Long.valueOf(dateToStr);
            String createdDate = Utils.milliSecToDateTimeFormat(model.getTime(), Constants.DATE_FORMAT_HH_MM_AA);

            if (position == 0) {
                holder.tvDateTime.setVisibility(View.VISIBLE);

                if (currentDate.equals(msgDays)) {
                    holder.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_today));

                    createdDate = activity.getResources().getString(R.string.hint_time_ago, model.getDateTime());
                } else if (currentDate - 1 == msgDays) {
                    holder.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_yesterday));
                } else {
                    holder.tvDateTime.setText(Utils.getDateChat(Long.parseLong(model.getTime())));
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
                    holder.tvDateTime.setVisibility(View.GONE);

                    if (currentDate.equals(msgDays)) {

                        createdDate = activity.getResources().getString(R.string.hint_time_ago, model.getDateTime());

                    }
                } else {
                    if (currentDate.equals(msgDays)) {
                        holder.tvDateTime.setVisibility(View.VISIBLE);
                        holder.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_today));

                        createdDate = activity.getResources().getString(R.string.hint_time_ago, model.getDateTime());

                    } else if (currentDate - 1 == msgDays) {
                        holder.tvDateTime.setVisibility(View.VISIBLE);
                        holder.tvDateTime.setText(activity.getResources().getString(R.string.hint_date_time_yesterday));

                    } else {
                        holder.tvDateTime.setVisibility(View.VISIBLE);

                        if (model.getTime() != null) {
                            String date = Utils.getDateChat(Long.parseLong(model.getTime()));
                            holder.tvDateTime.setText(date);
                        }
                    }

                }
            }

            holder.tvTime.setText(createdDate);

        } else {
            holder.tvDateTime.setVisibility(View.GONE);
        }
    }

}
