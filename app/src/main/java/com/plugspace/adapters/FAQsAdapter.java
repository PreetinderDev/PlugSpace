package com.plugspace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;
import com.plugspace.R;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public final class FAQsAdapter extends RecyclerView.Adapter<FAQsAdapter.RecyclerHolder> {

    private final ArrayList<HeightModel> list = new ArrayList<>();
    boolean isSlc = true;

    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

    public FAQsAdapter() {
        expansionsCollection.openOnlyOne(true);
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecyclerHolder.buildFor(parent);
    }

    public void setItems(ArrayList<HeightModel> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public final static class RecyclerHolder extends RecyclerView.ViewHolder {

        private static final int LAYOUT = R.layout.row_faqs_list;

        ExpansionLayout expansionLayout;
        TextView tvSubTile;
        ImageView ivPlusMinus;

        public static RecyclerHolder buildFor(ViewGroup viewGroup) {
            return new RecyclerHolder(LayoutInflater.from(viewGroup.getContext()).inflate(LAYOUT, viewGroup, false));
        }

        public RecyclerHolder(View itemView) {
            super(itemView);
            expansionLayout = itemView.findViewById(R.id.expansionLayout);
            tvSubTile = itemView.findViewById(R.id.tvSubTile);
            ivPlusMinus = itemView.findViewById(R.id.ivPlusMinus);
        }

        public void bind(Object object) {
            expansionLayout.collapse(false);
        }

        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.bind(list.get(position));

        expansionsCollection.add(holder.getExpansionLayout());
        holder.tvSubTile.setText(list.get(position).getString());

        holder.expansionLayout.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {
                if (isSlc) {
                    isSlc = false;
                    holder.ivPlusMinus.setImageResource(R.drawable.ic_minus2);
                } else {
                    isSlc = true;
                    holder.ivPlusMinus.setImageResource(R.drawable.ic_plus);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}