package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.cache.ImageLoader;
import com.hybunion.yirongma.payment.view.BranchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SunBingbing
 * @date 2017/8/14
 * @email freemars@yeah.net
 * @description
 */

public class BranchAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    public List<BranchBean.Data> dataList = new ArrayList<>();
    ImageLoader imageLoader;
    int right;
    public BranchAdapter(Context context) {
        this.context = context;
        imageLoader = ImageLoader.getInstance(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addData(List<BranchBean.Data> list) {
        if (list == null) {
            return;
        }
        dataList.addAll(list);
        this.notifyDataSetChanged();

    }
    public void right(int flag){
         right=flag;
    }
    public void clearData() {
        dataList.clear();

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
        BranchAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.branch_item, null);
            holder = new BranchAdapter.ViewHolder();
            holder.tv_branch = (TextView) convertView.findViewById(R.id.tv_branch);
            holder.img_right = (ImageView) convertView.findViewById(R.id.img_right);
            convertView.setTag(holder);
        } else {
            holder = (BranchAdapter.ViewHolder) convertView.getTag();
        }
        final BranchBean.Data item = dataList.get(position);
        holder.tv_branch.setText(item.getMerName());
        if (right==position){
            holder.img_right.setVisibility(View.VISIBLE);
        }else {
            holder.img_right.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_branch;
        ImageView img_right;
    }
}
