package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.util.Log;
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
 * 门店列表多选
 * Created by xjz on 2019/8/5.
 */

public class StoreTwiceListAdapter extends BaseAdapter {
    private Context context;
    public List<StoreManageBean.ObjBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;
    private int selectedPosition = -1;// 选中的位置
    private int multiple;//判断choose为true时候是否可取消
    public StoreTwiceListAdapter(Context context, List<StoreManageBean.ObjBean> list,int multiple) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.dataList = list;
        this.multiple = multiple;
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
            convertView = inflater.inflate(R.layout.item_store_list2, null);
            holder = new ViewHolder();
            holder.tv_list_name = (TextView) convertView.findViewById(R.id.tv_list_name);
            holder.iv_right = convertView.findViewById(R.id.iv_right);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StoreManageBean.ObjBean data = dataList.get(position);
        Log.i("xjz","走到了"+dataList.size());
        if(data!=null){
            holder.tv_list_name.setText(data.getStoreName());
        }
            if(multiple == 0){
                if(data.isStoreChoose){
                    holder.iv_right.setVisibility(View.VISIBLE);
                }else {
                    holder.iv_right.setVisibility(View.GONE);
                }
            }else {
                if(data.isStoreChoose){
                    holder.iv_right.setVisibility(View.VISIBLE);
                }else if(data.isStoreChoose2){
                    holder.iv_right.setVisibility(View.VISIBLE);
                }else {
                    holder.iv_right.setVisibility(View.GONE);
                }

            }



        return convertView;
    }

    class ViewHolder {
        TextView tv_list_name;
        ImageView iv_right;

    }
}
