package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款码适配器
 * Created by lyf on 2017/5/22.
 */

public class ManagerStoreAdapter extends BaseAdapter {
    private Context context;
    public List<StoreManageBean.ObjBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    public ManagerStoreAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void updateList(List<StoreManageBean.ObjBean> list, boolean isRefresh) {
        if (list == null) {
            return;
        }
//        if(isRefresh){
//            dataList.clear();
//        }
//        dataList.addAll(list);
        dataList = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_manager_store, null);
            holder = new ViewHolder();
            holder.ll_shop_name = (LinearLayout) convertView.findViewById(R.id.ll_shop_name);
            holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoreManageBean.ObjBean data = dataList.get(position);
        if(data!=null){
            holder.tv_shop_name.setText(data.getStoreName());
        }


        return convertView;
    }

    class ViewHolder {
        TextView tv_shop_name;
        LinearLayout ll_shop_name;
    }
}
