package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.RefundRecordBean;
import com.hybunion.yirongma.R;

import java.util.List;

public class RefundRecordAdapter extends BaseAdapter {
    private Context context;
    private List<RefundRecordBean.DataBean> dataList;
    private LayoutInflater inflater;


    public RefundRecordAdapter(Context context) {
        this.context = context;
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

    public void addAllList(List<RefundRecordBean.DataBean> list) {
        if (list == null) {
            return;
        }
        dataList=list;
        notifyDataSetChanged();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_refund_record_list,null);
            holder = new ViewHolder();
            holder.tv_refund_status = convertView.findViewById(R.id.tv_refund_status);
            holder.tv_refund_time = convertView.findViewById(R.id.tv_refund_time);
            holder.tv_amount = convertView.findViewById(R.id.tv_amount);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        RefundRecordBean.DataBean bean = dataList.get(position);
        if(bean!=null){
            if(null!=bean.status)
                holder.tv_refund_status.setText(bean.status);

            if(null!=bean.crateDate)
                holder.tv_refund_time.setText(bean.crateDate);

            if(null!=bean.amount)
                holder.tv_amount.setText(bean.amount);
        }

        return convertView;
    }

    public class ViewHolder{
        TextView tv_refund_status;
        TextView tv_refund_time;
        TextView tv_amount;
    }
}
