package com.hybunion.yirongma.payment.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用适配器
 */
public abstract class CommonAdapter1<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;//上下文
    public List<T> mDatas;//数据源
    protected final int mItemLayoutId;

    public CommonAdapter1(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    public void updateList(List<T> data){
        mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return (T) (mDatas == null ? 0 : mDatas.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getViewHolder(mContext, convertView,
                parent, mItemLayoutId, position);
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T item, int position);
}
