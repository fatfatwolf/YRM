package com.hybunion.yirongma.payment.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.KuanTaiBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.ClerkSettingActivity1;
import com.hybunion.yirongma.payment.activity.KuanTaiActivity;
import com.hybunion.yirongma.payment.activity.TerminalManageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款码适配器
 * Created by lyf on 2017/5/22.
 */

public class KuanTaiAdapter extends BaseAdapter {
    private KuanTaiActivity activity;
    public List<KuanTaiBean.DataBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;
    private String mStoreId,storeName;

    public KuanTaiAdapter(KuanTaiActivity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    public void addAllList(List<KuanTaiBean.DataBean> list,boolean isRefresh) {
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
            convertView = inflater.inflate(R.layout.item_kuantai_manager, null);
            holder = new ViewHolder();
            holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.tv_shop_time = (TextView) convertView.findViewById(R.id.tv_shop_time);
            holder.tv_bind_worker = (TextView) convertView.findViewById(R.id.tv_bind_worker);
            holder.tv_manage = (TextView) convertView.findViewById(R.id.tv_manage);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        KuanTaiBean.DataBean data = dataList.get(position);
        mStoreId = activity.getStoreId();
        if(data!=null){
            holder.tv_shop_name.setText(data.getStoreName());
            holder.tv_shop_time.setText(data.getCreateDate());
            holder.tv_bind_worker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     Intent intent = new Intent(activity,ClerkSettingActivity1.class);
                     intent.putExtra("type","2");
                     intent.putExtra("mStoreId",mStoreId);
                     intent.putExtra("storeId",dataList.get(position).getStoreId());
                     intent.putExtra("storeName",dataList.get(position).getStoreName());
                    activity.startActivity(intent);
                }
            });

            holder.tv_manage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity,TerminalManageActivity.class);
                    intent.putExtra("storeId",dataList.get(position).getStoreId());
                    intent.putExtra("storeName",dataList.get(position).getStoreName());
                    activity.startActivity(intent);
                }
            });

        }


        return convertView;
    }

    class ViewHolder {
        TextView tv_shop_name;
        TextView tv_shop_time;
        TextView tv_bind_worker;
        TextView tv_manage;
    }
}
