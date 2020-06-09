package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款码适配器
 * Created by lyf on 2017/5/22.
 */

public class StoreListAdapter extends BaseAdapter {
    private Context context;
    public List<StoreManageBean.ObjBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;
    private int selectedPosition = 0;// 选中的位置
    public StoreListAdapter(Context context) {
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

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_store_list, null);
            holder = new ViewHolder();
            holder.tv_list_name = (TextView) convertView.findViewById(R.id.tv_list_name);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoreManageBean.ObjBean data = dataList.get(position);
        Log.i("xjz","走到了"+dataList.size());
        if(data!=null){
            holder.tv_list_name.setText(data.getStoreName());
        }

        if (position == selectedPosition) {
            convertView.setBackgroundColor(context.getResources().getColor(
                    R.color.address_child));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(
                    R.color.white));
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_list_name;

    }
}
