package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.ChooseDayBean;
import com.hybunion.yirongma.payment.bean.HuiListBean2;
import com.hybunion.yirongma.payment.bean.ModifyRulersShopBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.view.DiscountImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: xjz
 * @data: 2019/7/3.
 */

public class GridViewModifyAdapter extends BaseAdapter {

    private Context context;
    private List<HuiListBean2> list = new ArrayList<>();
    private LayoutInflater inflater;
    private boolean isChoose;
    private int clickStatus = -1;
    private int type = 1;

    public GridViewModifyAdapter(Context context, List<HuiListBean2> list) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void updataList(List<HuiListBean2>datalist){

        if(datalist == null){
            return;
        }
        this.list = datalist;
        notifyDataSetChanged();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_modify_gridview_layout, null);
            holder = new ViewHolder();
            holder.tv_all_amount = (TextView) convertView.findViewById(R.id.tv_all_amount);
            holder.tv_send_amount = convertView.findViewById(R.id.tv_send_amount);
            holder.tv_send_cores = convertView.findViewById(R.id.tv_send_cores);
            holder.tv_discount = convertView.findViewById(R.id.tv_discount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_all_amount.setText("充 "+list.get(position).actualAmount+" 元");
        holder.tv_send_amount.setText(list.get(position).givenAmount);
        holder.tv_discount.setText(list.get(position).discount+"折");




        return convertView;
    }

    class ViewHolder {
        TextView tv_all_amount;
        TextView tv_send_amount;
        TextView tv_send_cores;
        DiscountImageView tv_discount;
    }
}
