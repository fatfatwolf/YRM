package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款码适配器
 * Created by lyf on 2017/5/22.
 */

public class KuanTaiListAdapter2 extends BaseAdapter {
    private Context context;
    public List<StoreManageBean.ObjBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;
    private String mStoreId,storeName;
    private int selectedPosition = -1;// 选中的位置

    public KuanTaiListAdapter2(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addAllList(List<StoreManageBean.ObjBean> list) {
        if (list == null) {
            return;
        }
//        if(isRefresh){
//            dataList.clear();
//        }
//        dataList.clear();
        dataList=list;
        selectedPosition = -1;
        notifyDataSetChanged();

    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
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
            convertView = inflater.inflate(R.layout.item_store_list2, null);
            holder = new ViewHolder();
            holder.tv_kuantai_name = (TextView) convertView.findViewById(R.id.tv_list_name  );
            holder.iv_right = convertView.findViewById(R.id.iv_right);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoreManageBean.ObjBean data = dataList.get(position);
        storeName = data.getStoreName();
        holder.tv_kuantai_name.setText(storeName);


        if (position == selectedPosition) {
            holder.iv_right.setVisibility(View.VISIBLE);
        } else {
            holder.iv_right.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_kuantai_name;
        ImageView iv_right;
    }
}
