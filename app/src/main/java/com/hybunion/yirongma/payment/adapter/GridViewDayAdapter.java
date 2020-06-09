package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.ChooseDayBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: xjz
 * @data: 2019/7/3.
 */

public class GridViewDayAdapter extends BaseAdapter {

    private Context context;
    private List<ChooseDayBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    private boolean isChoose;
    private int clickStatus = -1;
    private int type = 1;

    public GridViewDayAdapter(Context context,List<ChooseDayBean> list) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setSelection(int position,boolean isChoose) {
        clickStatus = position;
        this.isChoose = isChoose;
        notifyDataSetChanged();
    }

    public void updataList(List<ChooseDayBean>datalist,int type){
        this.type = type;
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
            convertView = inflater.inflate(R.layout.item_day_gridview_layout, null);
            holder = new ViewHolder();
            holder.tv_day = (TextView) convertView.findViewById(R.id.tv_day);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_day.setText(list.get(position).day);
        if(type == 1){//第一个选中的处理
//控制第一个点击
            if(list.get(position).isClickable2){
                holder.tv_day.setBackgroundResource(R.drawable.radius15_gray_button);
                holder.tv_day.setTextColor(context.getResources().getColor(R.color.viewfinder_mask));
            }else{
                if (list.get(position).isChoose) {
                    holder.tv_day.setBackgroundResource(R.drawable.radius15_red_button);
                    holder.tv_day.setTextColor(Color.parseColor("#f74948"));
                } else {
                    holder.tv_day.setBackgroundResource(R.drawable.radius15_gray_button);
                    holder.tv_day.setTextColor(context.getResources().getColor(R.color.text_color2));
                }
            }
        }else if(type == 2){
            //控制第二个点击
            if(list.get(position).isClickable){
                holder.tv_day.setBackgroundResource(R.drawable.radius15_gray_button);
                holder.tv_day.setTextColor(context.getResources().getColor(R.color.viewfinder_mask));
            }else {
                if (list.get(position).isChoose2) {
                    holder.tv_day.setBackgroundResource(R.drawable.radius15_red_button);
                    holder.tv_day.setTextColor(Color.parseColor("#f74948"));
                } else {
                    holder.tv_day.setBackgroundResource(R.drawable.radius15_gray_button);
                    holder.tv_day.setTextColor(context.getResources().getColor(R.color.text_color2));
                }
            }
        }


        return convertView;
    }

    class ViewHolder {
        TextView tv_day;
    }
}
