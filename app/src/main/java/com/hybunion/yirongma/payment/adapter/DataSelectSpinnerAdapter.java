package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.R;


public class DataSelectSpinnerAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private String[] mStringArray;

    public DataSelectSpinnerAdapter(Context context, String[] objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
        mContext = context;
        mStringArray = objects;

    }

    public void updateData(String[] data){
        mStringArray = data;
        notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_screening, parent, false);
        }
        Log.i("xjz---length",mStringArray.length+"");
        if (mStringArray.length>position){
            TextView tv = (TextView) convertView.findViewById(R.id.tvName_item_screening);
            tv.setText(mStringArray[position]);
        }

        return convertView;


    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        if (mStringArray.length>position){
            //此处text1是Spinner默认的用来显示文字的TextView
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(mStringArray[position]);
            tv.setTextSize(12f);
            tv.setTextColor(Color.parseColor("#252e44"));
        }

        return convertView;


    }
}
