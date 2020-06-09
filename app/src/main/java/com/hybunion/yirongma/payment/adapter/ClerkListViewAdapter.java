package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.QueryClerkListBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.R;

import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
public class ClerkListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public List<QueryClerkListBean.ObjBean> dataList;
    private Context mContext;

    public ClerkListViewAdapter(Context mContext, List<QueryClerkListBean.ObjBean> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
        inflater = LayoutInflater.from(mContext);
    }



    public void updateList(List<QueryClerkListBean.ObjBean> list, boolean isRefresh) {
        if (list == null) {
            return;
        }
//        if(isRefresh){
//            dataList.clear();
//        }
//        dataList.addAll(list);
        dataList= list;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        LogUtil.d("dataListSize--->" + dataList.size());
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.clerk_setting_item2,null);
            holder = new ViewHolder();
            holder.tv_clerk_name = (TextView) convertView.findViewById(R.id.tv_clerk_name);
            LogUtil.d(dataList.get(position).getEmployName()+"店员名称");
            holder.tv_clerk_name.setText(dataList.get(position).getEmployName());
            holder.img_activity = (ImageView) convertView.findViewById(R.id.img_activity);
            holder.tv_clerk_number = (TextView) convertView.findViewById(R.id.tv_clerk_number);
            holder.tv_clerk_number.setText(dataList.get(position).getEmployPhone());
            holder.iv_check_clerk = (ImageView) convertView.findViewById(R.id.iv_check_clerk);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(dataList.get(position).isClicked){
            holder.iv_check_clerk.setVisibility(View.VISIBLE);
        }else {
            holder.iv_check_clerk.setVisibility(View.GONE);
        }
        String position1 = dataList.get(position).getPosition();
        if ("店长".equals(position1)){//店长
            holder.img_activity.setBackgroundResource(R.drawable.img_shop_manager2);
        }else {
            holder.img_activity.setBackgroundResource(R.drawable.img_shop_worker);
        }
//        convertView.findViewById(R.id.ll_clerk).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {//bottomView点击事件
//                    MonButtonClickListener.onDeleteClerk(position,dataList.get(position));
//                    closeItem(position);
//                }
//            });
        return convertView;
    }

    class ViewHolder{
        TextView tv_clerk_name;
        TextView tv_clerk_number;
        ImageView img_activity;
        ImageView iv_check_clerk;
    }
}