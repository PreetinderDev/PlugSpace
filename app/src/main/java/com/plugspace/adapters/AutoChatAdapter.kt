package com.plugspace.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.plugspace.R
import com.plugspace.model.AutoChatModel

class AutoChatAdapter(var itemList: ArrayList<AutoChatModel.Message>) :
    RecyclerView.Adapter<AutoChatAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val chatView: View = inflater.inflate(R.layout.auto_chat_adapter, parent, false)
        return AutoChatAdapter.ViewHolder(chatView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pojo = itemList[position]
        holder.tvMsg.text = pojo.message

    }

    override fun getItemCount(): Int {
        return itemList.size;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMsg = itemView.findViewById<TextView>(R.id.autoMsgs)
    }

}