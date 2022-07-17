package com.plugspace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.plugspace.R;
import com.plugspace.model.HeightModel;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<HeightModel> {

    private final LayoutInflater mInflater;
    private final Activity mActivity;
    private List<HeightModel> arrayList;
    private final int mResource;

    public CustomArrayAdapter(@NonNull Activity activity, @LayoutRes int resource, @NonNull List<HeightModel> arrayLists) {
        super(activity, resource, 0, arrayLists);
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        mResource = resource;
        arrayList = arrayLists;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
//        View view = super.getDropDownView(position, convertView, parent);
//        TextView tv = (TextView) view;
//        if (position == 0) {
//            // Set the hint text color gray
//            tv.setTextColor(mContext.getColor(R.color.text_color));
//        } else {
//            tv.setTextColor(mContext.getColor(R.color.colorPrimary));
//        }
//        return view;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HeightModel rowItem = getItem(position);

        View rowview = mInflater.inflate(R.layout.row_spinner,null,true);

        TextView tvName = (TextView) convertView.findViewById(R.id.tvSpinner);
//        tvName.setText(arrayList.get(position).getStrHeight());


        return rowview;
    }
    private View createItemView(int position, View convertView, ViewGroup parent) {
//        final View view = mInflater.inflate(mResource, parent, false);
//        TextView tvName = (TextView) view.findViewById(R.id.tvSpinner);
//        tvName.setText(arrayList.get(position).getStrHeight());


//        tvName.setCompoundDrawablePadding(10);
//        tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_spinner);


        return convertView;
    }

}