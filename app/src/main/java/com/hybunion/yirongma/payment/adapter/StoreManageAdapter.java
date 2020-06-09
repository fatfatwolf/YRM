package com.hybunion.yirongma.payment.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.ClerkSettingActivity;
import com.hybunion.yirongma.payment.activity.KuanTaiActivity;
import com.hybunion.yirongma.payment.activity.StoreManageActivity2;
import com.hybunion.yirongma.payment.activity.TerminalManageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款码适配器
 * Created by lyf on 2017/5/22.
 */

public class StoreManageAdapter extends BaseAdapter {
    private StoreManageActivity2 activity;
    public List<StoreManageBean.ObjBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    public StoreManageAdapter(StoreManageActivity2 context) {
        this.activity = context;
        inflater = LayoutInflater.from(context);
    }

    public void addAllList(List<StoreManageBean.ObjBean> list,boolean isRefresh) {
        if (list == null) {
            return;
        }
//        if(isRefresh){
//            dataList.clear();
//        }
//        dataList.clear();
        dataList=list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_store_manager, null);
            holder = new ViewHolder();
            holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.tv_shop_address = (TextView) convertView.findViewById(R.id.tv_shop_address);
            holder.tv_shop_phone = (TextView) convertView.findViewById(R.id.tv_shop_phone);
            holder.tv_shop_time = (TextView) convertView.findViewById(R.id.tv_shop_time);
            holder.tv_bind_worker = (TextView) convertView.findViewById(R.id.tv_bind_worker);
            holder.tv_kuantai_manage = (TextView) convertView.findViewById(R.id.tv_kuantai_manage);
            holder.tv_terminal_manage = (TextView) convertView.findViewById(R.id.tv_terminal_manage);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StoreManageBean.ObjBean data = dataList.get(position);
        if(data!=null){
            holder.tv_shop_name.setText(data.getStoreName());
            holder.tv_shop_address.setText(data.getStoreAddr());
            holder.tv_shop_phone.setText(data.getStorePhone());
            holder.tv_shop_time.setText(data.createDate);
            holder.tv_bind_worker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     Intent intent = new Intent(activity,ClerkSettingActivity.class);
                     intent.putExtra("type","1");
                     intent.putExtra("storeId",dataList.get(position).getStoreId());
                     intent.putExtra("storeName",dataList.get(position).getStoreName());
                    activity.startActivity(intent);
                }
            });

            holder.tv_kuantai_manage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity,KuanTaiActivity.class);
                    intent.putExtra("storeId",dataList.get(position).getStoreId());
                    intent.putExtra("storeName",dataList.get(position).getStoreName());
                    activity.startActivity(intent);
                }
            });
        }

        holder.tv_terminal_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,TerminalManageActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("storeId",dataList.get(position).getStoreId());
                intent.putExtra("storeName",dataList.get(position).getStoreName());
                activity.startActivity(intent);
            }
        });


        return convertView;
    }

    class ViewHolder {
        TextView tv_shop_name;
        TextView tv_shop_address;
        TextView tv_shop_phone;
        TextView tv_shop_time;
        TextView tv_bind_worker;
        TextView tv_kuantai_manage;
        TextView tv_terminal_manage;
    }
}
