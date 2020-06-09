package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.CollectionCodeBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 款台适配器
 * Created by lyf on 2017/5/22.
 */

public class CollectionCodeListAdapter1 extends BaseAdapter {
    private Context context;
    private List<CollectionCodeBean.DataBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    public CollectionCodeListAdapter1(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addAllList(List<CollectionCodeBean.DataBean> list,boolean isRefresh) {
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
    public CollectionCodeBean.DataBean getItem(int position) {
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
            convertView = inflater.inflate(R.layout.item_collection_code_list1, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.i("xjz111",dataList.size()+"");
        holder.tvName.setText(dataList.get(position).tidName);
        holder.tvCode.setText("款台编号："+dataList.get(position).tid);
        holder.tvPwd.setText("款台密钥："+dataList.get(position).secretKey);


        return convertView;
    }

    private class ViewHolder {
       private TextView tvName, tvCode, tvPwd;
        public ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.name_kuantai_list);
            tvCode = (TextView) view.findViewById(R.id.code_kuantai_list);
            tvPwd = (TextView) view.findViewById(R.id.pwd_kuantai_list);

        }

    }
}
