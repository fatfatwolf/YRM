package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.DataSummaryBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.util.ArrayList;
import java.util.List;

public class DataSummaryAdapter extends BaseAdapter {
    private Context context;

    public List<DataSummaryBean.DataBean> dataList = new ArrayList<>();

    public DataSummaryAdapter(Context context, List<DataSummaryBean.DataBean> dataList) {
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
        DataSummaryBean.DataBean bean = dataList.get(position);
        if(bean!=null){
            holder.tv_shop_name.setText(bean.storeName);
            holder.tv_order_count.setText(bean.transCount);
            if(bean.refundSum.equals("0")){
                holder.tv_refund_order.setVisibility(View.GONE);
            }else {
                holder.tv_refund_order.setVisibility(View.VISIBLE);
                holder.tv_refund_order.setText("含退款:"+bean.refundSum);
            }
            holder.tv_order_sum.setText(YrmUtils.decimalTwoPoints(bean.transSum));
            if(bean.refundCount.equals("0")){
                holder.tv_refund_count.setVisibility(View.GONE);
            }else {
                holder.tv_refund_count.setVisibility(View.VISIBLE);
                holder.tv_refund_count.setText("含退款:"+bean.refundCount);
            }
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
