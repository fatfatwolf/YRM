package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.MemberListBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.util.ArrayList;
import java.util.List;

public class MemberListAdapter extends BaseAdapter {
    private Context context;

    public List<MemberListBean.DataBean> dataList = new ArrayList<>();

    public MemberListAdapter(Context context, List<MemberListBean.DataBean> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    public void updateList(List<MemberListBean.DataBean> dataList){
        if(dataList == null){
            return;
        }

        this.dataList = dataList;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_member_list,null);
            holder = new ViewHolder();
            holder.tv_type_num = (TextView) convertView.findViewById(R.id.tv_type_num);
            holder.tv_pay_count = (TextView) convertView.findViewById(R.id.tv_pay_count);
            holder.tv_averg_price = (TextView) convertView.findViewById(R.id.tv_averg_price);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        MemberListBean.DataBean bean = dataList.get(position);
        if(bean!=null){
            holder.tv_type_num.setText(bean.rowIndex);
            holder.tv_averg_price.setText(bean.avgAmount);

            holder.tv_pay_count.setText(bean.trans_amount);

            holder.tv_phone.setText(YrmUtils.handlePhoneNum(bean.wx_phone));
            holder.tv_count.setText(bean.trans_num);
        }
        return convertView;
    }


    public class ViewHolder{
        TextView tv_type_num;
        TextView tv_pay_count;
        TextView tv_count;
        TextView tv_phone;
        TextView tv_averg_price;
    }
}
