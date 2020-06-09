package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.DataManagerSumBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

public class DataManagerSumAdapter extends BaseAdapter {
    private Context context;

    public List<DataManagerSumBean> dataList = new ArrayList<>();

    public DataManagerSumAdapter(Context context, List<DataManagerSumBean> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_store_data,null);
            holder = new ViewHolder();
            holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.tv_order_count = (TextView) convertView.findViewById(R.id.tv_order_count);
            holder.tv_refund_order = (TextView) convertView.findViewById(R.id.tv_refund_order);
            holder.tv_order_sum = (TextView) convertView.findViewById(R.id.tv_order_sum);
            holder.tv_refund_count = (TextView) convertView.findViewById(R.id.tv_refund_count);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        DataManagerSumBean bean = dataList.get(position);
        if(bean!=null){
            holder.tv_shop_name.setText(bean.ckName);
            holder.tv_order_count.setText(bean.orderCount);
            holder.tv_refund_order.setText("退款数:"+bean.refundCount);
            holder.tv_order_sum.setText(bean.orderSum);
            holder.tv_refund_count.setText("退款数:"+bean.refundSum);
        }

        return convertView;
    }


    public class ViewHolder{
        TextView tv_shop_name;
        TextView tv_order_count;
        TextView tv_refund_order;
        TextView tv_order_sum;
        TextView tv_refund_count;
    }
}
