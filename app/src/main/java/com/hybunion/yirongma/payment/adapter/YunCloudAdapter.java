package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.YunCloudBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 款台适配器
 * Created by lyf on 2017/5/22.
 */

public class YunCloudAdapter extends BaseAdapter {
    private Context context;
    private List<YunCloudBean.DataBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    public YunCloudAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addAllList(List<YunCloudBean.DataBean> list) {
        if (list == null) {
            return;
        }
        dataList = list;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public YunCloudBean.DataBean getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_yun_cloud, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(dataList.get(position).type.equals("0")){
            holder.tv_cloud_name.setText("智联博众");
            holder.iv_yunCloud_type.setImageResource(R.drawable.img_yun_cloud);
        }else if(dataList.get(position).type.equals("1")){
            holder.tv_cloud_name.setText("波普");
            holder.iv_yunCloud_type.setImageResource(R.drawable.img_yun_bopu);
        }else if(dataList.get(position).type.equals("2")){
            holder.tv_cloud_name.setText("新联付");
            holder.iv_yunCloud_type.setImageResource(R.drawable.img_yun_xlf);
        }else {
            holder.tv_cloud_name.setText("华智融");
            holder.iv_yunCloud_type.setImageResource(R.drawable.img_yun_hzr);
        }

        holder.tv_cloud_number.setText("设备编号："+dataList.get(position).yun_id);


        return convertView;
    }

    private class ViewHolder {
       private TextView tv_cloud_name, tv_cloud_number;
       private ImageView iv_yunCloud_type;
        public ViewHolder(View view) {
            tv_cloud_name = (TextView) view.findViewById(R.id.tv_cloud_name);
            tv_cloud_number = (TextView) view.findViewById(R.id.tv_cloud_number);
            iv_yunCloud_type = view.findViewById(R.id.iv_yunCloud_type);

        }

    }
}
