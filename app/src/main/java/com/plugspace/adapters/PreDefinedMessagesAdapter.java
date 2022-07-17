package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.plugspace.R;
import com.plugspace.model.PreDefinedMessageModel;

import java.util.List;

public class PreDefinedMessagesAdapter extends RecyclerView.Adapter<PreDefinedMessagesAdapter.ViewHolder> {
    Activity activity;
    List<PreDefinedMessageModel.PreDefinedMessageList> messagesList;
    MyListener myListener;

    public PreDefinedMessagesAdapter(Activity activity, List<PreDefinedMessageModel.PreDefinedMessageList> messagesList, MyListener myListener) {
        this.activity = activity;
        this.messagesList = messagesList;
        this.myListener = myListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.pre_defined_messages_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(messagesList.get(position).getMessage());
        final int index = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListener.msgClick(messagesList.get(index).getId(), messagesList.get(index).getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.pre_defined_msg_tv);
        }
    }

    public interface MyListener {
        void msgClick(int id, String msg);
    }

}
