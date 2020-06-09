package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.R;

public class IndustryAdapter extends BaseAdapter {
    private Context mContext;
    public String[] chart;
    private LayoutInflater inflater;

    public IndustryAdapter(Context context, String[] chart){
        this.mContext=context;
        this.chart =chart;
    }
    @Override
    public int getCount() {
        return chart.length;
    }

    @Override
    public Object getItem(int position) {
        return chart[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.industry_text,null);
            holder=new ViewHolder();
            holder.tv_industry= (TextView) convertView.findViewById(R.id.tv_industry);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_industry.setText(chart[position]);
        return convertView;
    }
    class ViewHolder{
        TextView tv_industry;
    }
}