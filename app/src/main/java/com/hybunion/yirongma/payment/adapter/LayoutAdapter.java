/*
 * Copyright (C) 2014 Lucas Rocha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.bean.TextBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.activity.LMFRedRainActivity;

import java.util.ArrayList;
import java.util.List;


public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.SimpleViewHolder> {
    private static final int DEFAULT_ITEM_COUNT = 5;

    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private final List<TextBean.DataBean> mItems = new ArrayList();
    private int mCurrentItemId = 0;

    public LayoutAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
    }

    /**
     * 获取数据源
     * @return
     */
    public List<TextBean.DataBean> getDataSource() {
        return mItems;
    }

    /**
     * 设置适配器的数据
     * @param dataList
     */
    public void setDataSource(List<TextBean.DataBean> dataList) {
        mItems.clear();
        if (dataList != null) {
            mItems.addAll(dataList);
        }
        notifyDataSetChanged();
    }

  /*  public void addItem(int position, TextBean t) {
        final int id = mCurrentItemId++;
        mItems.add(position, t);
        notifyItemInserted(position);
    }*/

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new SimpleViewHolder(view);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final ImageView title;

        public SimpleViewHolder(View view) {
            super(view);
            title = (ImageView) view.findViewById(R.id.title);  //这里去findViewById
        }
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        LogUtil.d(mItems.get(position).getImgUrl()+"图片地址");
        Glide.with(mContext)
                .load(mItems.get(position).getImgUrl())
                .into(holder.title);
        final String url = mItems.get(position).getLink();
        //这里去加载图片
          final View itemView = holder.itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LMFRedRainActivity.class);
                intent.putExtra("webViewUrl", "1");
                intent.putExtra("url",url);
                mContext.startActivity(intent);
            }
        });
        final TextBean.DataBean itemId = mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
