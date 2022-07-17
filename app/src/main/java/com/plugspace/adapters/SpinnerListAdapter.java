package com.plugspace.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.plugspace.R;
import com.plugspace.model.HeightModel;

import java.util.ArrayList;

public class SpinnerListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<HeightModel> arrayList;
    private LayoutInflater inflter;

    public SpinnerListAdapter(Context applicationContext, ArrayList<HeightModel> arrayList) {
        this.context = applicationContext;
        this.arrayList = arrayList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row_spinner, null);
        TextView tvCountryTitle = view.findViewById(R.id.tvSpinner);

        tvCountryTitle.setText(arrayList.get(i).getString());


        int paddingDp = 10;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);
        view.setPadding(0, 0, paddingPixel, 0);
        return view;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.row_spinner, null);
        TextView tvCountryTitle = convertView.findViewById(R.id.tvSpinner);
        int paddingDp = 10;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);
        convertView.setPadding(paddingPixel, 0, 0, 0);
        tvCountryTitle.setText(arrayList.get(position).getString());
        return convertView;
    }
}
