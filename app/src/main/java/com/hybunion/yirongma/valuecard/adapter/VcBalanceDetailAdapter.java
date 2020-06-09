package com.hybunion.yirongma.valuecard.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.valuecard.model.VcBalanceDetailBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/12.
 */
public class VcBalanceDetailAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<VcBalanceDetailBean> vcBalanceDetailBeans=new ArrayList<>();
    private LayoutInflater layoutInflater;

    public VcBalanceDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return vcBalanceDetailBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return vcBalanceDetailBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
              layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              convertView=layoutInflater.inflate(R.layout.layout_balance_detail_item,null);
              holder=new ViewHolder();
              holder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
              holder.tv_type=(TextView) convertView.findViewById(R.id.tv_type);
              holder.tv_object=(TextView) convertView.findViewById(R.id.tv_object);
              holder.tv_employee=(TextView) convertView.findViewById(R.id.tv_employee);
              holder.tv_money_num=(TextView) convertView.findViewById(R.id.tv_money_num);

              convertView.setTag(holder);
        }else {
              holder= (ViewHolder) convertView.getTag();
        }
        String tvTime=vcBalanceDetailBeans.get(position).getTransTime();
        String tvType=vcBalanceDetailBeans.get(position).getTransType();
        String tvObject=vcBalanceDetailBeans.get(position).getItemName();
        String tvEmployee=vcBalanceDetailBeans.get(position).getEmpName();
        String tvMoneyNum=vcBalanceDetailBeans.get(position).getTransAmount();

        holder.tv_time.setText(tvTime);
        holder.tv_type.setText(tvType);

        if(TextUtils.isEmpty(tvObject)){
            holder.tv_object.setText("无");
            holder.tv_object.setTextColor(0xFFDCDCDC);
        }else {
            holder.tv_object.setText(tvObject);
        }
        if(TextUtils.isEmpty(tvEmployee)){
            holder.tv_employee.setText("无");
            holder.tv_employee.setTextColor(0xFFDCDCDC);
        }else {
            holder.tv_employee.setText(tvEmployee);
        }
        if(TextUtils.isEmpty(tvMoneyNum)){
            holder.tv_money_num.setText("无");
            holder.tv_employee.setTextColor(0xFFDCDCDC);
        }else {
            holder.tv_money_num.setText(tvMoneyNum);
        }

        return convertView;
    }

        static class ViewHolder{
            TextView tv_time;
            TextView tv_type;
            TextView tv_object;
            TextView tv_employee;
            TextView tv_money_num;
     }
}
