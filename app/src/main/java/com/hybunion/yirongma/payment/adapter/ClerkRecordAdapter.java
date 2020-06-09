package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.ClerkRecordBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

public class ClerkRecordAdapter extends BaseAdapter{

    private Context context;
    List<ClerkRecordBean.DataBean> list = new ArrayList<>();
    String startDate;
    String endDate;

    public ClerkRecordAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void addAll(List<ClerkRecordBean.DataBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_time_clerk_detail,null);
            holder = new ViewHolder();
            holder.tv_check = (TextView) convertView.findViewById(R.id.tv_check);
            holder.tv_order_count = (TextView) convertView.findViewById(R.id.tv_order_count);
            holder.tv_order_sum = (TextView) convertView.findViewById(R.id.tv_order_sum);
            holder.tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
            holder.tv_end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClerkRecordBean.DataBean bean = list.get(position);
        if(bean!=null){
            if(bean.startDate!=null){
                startDate = bean.startDate.substring(5);
                holder.tv_start_time.setText(startDate);
            }
            if(bean.endDate!=null){
                endDate = bean.endDate.substring(5);
                holder.tv_end_time.setText(endDate);
            }
            if(bean.cashierName!=null){
                holder.tv_check.setText(bean.cashierName);
            }
            if(bean.transCount!=null){
                holder.tv_order_count.setText(bean.transCount);
            }
            if(bean.transAmount!=null){
                holder.tv_order_sum.setText(bean.transAmount);
            }

        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_check;
        TextView tv_order_count;
        TextView tv_order_sum;
        TextView tv_start_time;
        TextView tv_end_time;
    }







}
