package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.ChooseDayBean;
import com.hybunion.yirongma.payment.bean.ChooseTimeBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.NoUseTimeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: xjz
 * @data: 2019/7/3.
 */

public class GridViewTimeAdapter extends BaseAdapter {

    private Context context;
    private List<ChooseTimeBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    private int type = 1;

    public GridViewTimeAdapter(Context context, List<ChooseTimeBean> list) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    public void updataList(List<ChooseTimeBean>dataList,int type){
        this.type = type;
        if(dataList == null){
            return;
        }
        this.list = dataList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_time_gridview_layout, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.ll_time = convertView.findViewById(R.id.ll_time);
            holder.iv_close = convertView.findViewById(R.id.iv_close);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(list.get(position).title);
        holder.tv_time.setText(list.get(position).time);
        if(position == list.size()-1){
            holder.tv_time.setVisibility(View.GONE);
        }else {
            holder.tv_time.setVisibility(View.VISIBLE);
        }

            if(type==1){//第一个
                if (list.get(position).isChoose) {
                    holder.ll_time.setBackgroundResource(R.drawable.radius4_red_button);
                    holder.tv_title.setTextColor(Color.parseColor("#f74948"));
                    holder.tv_time.setTextColor(Color.parseColor("#f74948"));
                }else {
                    holder.ll_time.setBackgroundResource(R.drawable.radius4_gray_button);
                    holder.tv_title.setTextColor(context.getResources().getColor(R.color.text_color2));
                    holder.tv_time.setTextColor(context.getResources().getColor(R.color.text_color2));
                }
                if(position>2 && position!=list.size()-1){
                    holder.iv_close.setVisibility(View.VISIBLE);
                    if(list.get(position).isVisible){
                        holder.iv_close.setBackgroundResource(R.drawable.img_time_close);
                    }else {
                        holder.iv_close.setBackgroundResource(R.drawable.img_close_gray);
                    }
                }else {
                    holder.iv_close.setVisibility(View.GONE);
                }


                holder.iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NoUseTimeActivity activity = (NoUseTimeActivity) context;
                        activity.setChangeCount(type);
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }else if(type == 2){//第二个
                if (list.get(position).isChoose) {
                    holder.ll_time.setBackgroundResource(R.drawable.radius4_red_button);
                    holder.tv_title.setTextColor(Color.parseColor("#f74948"));
                    holder.tv_time.setTextColor(Color.parseColor("#f74948"));
                }else {
                    holder.ll_time.setBackgroundResource(R.drawable.radius4_gray_button);
                    holder.tv_title.setTextColor(context.getResources().getColor(R.color.text_color2));
                    holder.tv_time.setTextColor(context.getResources().getColor(R.color.text_color2));
                }
                if(position>2 && position!=list.size()-1) {
                    holder.iv_close.setVisibility(View.VISIBLE);
                    if (list.get(position).isVisible) {
                        holder.iv_close.setBackgroundResource(R.drawable.img_time_close);
                    } else {
                        holder.iv_close.setBackgroundResource(R.drawable.img_close_gray);
                    }
                }else {
                    holder.iv_close.setVisibility(View.GONE);
                }
                holder.iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NoUseTimeActivity activity = (NoUseTimeActivity) context;
                        activity.setChangeCount(type);
                        list.remove(position);
                        notifyDataSetChanged();

                    }
                });
            }





        return convertView;
    }

    class ViewHolder {
        LinearLayout ll_time;
        TextView tv_title;
        TextView tv_time;
        ImageView iv_close;
    }
}
