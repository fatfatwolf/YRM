package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.ClerkWorkDetailBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

public class ClerkWorkDetailAdapter extends BaseAdapter {
    private Context context;
    List<ClerkWorkDetailBean.DataBean> list = new ArrayList<>();

    public ClerkWorkDetailAdapter(Context context, List<ClerkWorkDetailBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_clerk_detail,null);
            holder = new ViewHolder();
            holder.tv_check_name = (TextView) convertView.findViewById(R.id.tv_check);
            holder.tv_order_count = (TextView) convertView.findViewById(R.id.tv_order_count);
            holder.tv_order_sum = (TextView) convertView.findViewById(R.id.tv_order_sum);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClerkWorkDetailBean.DataBean bean = list.get(position);
        if(bean!=null){
            holder.tv_check_name.setText(bean.name);
            holder.tv_order_count.setText(bean.transCount);
            holder.tv_order_sum.setText(bean.transSum);
        }
        return convertView;
    }




    class ViewHolder{
        TextView tv_check_name;
        TextView tv_order_count;
        TextView tv_order_sum;
    }
}
