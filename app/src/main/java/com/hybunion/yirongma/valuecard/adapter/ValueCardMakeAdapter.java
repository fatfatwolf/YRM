package com.hybunion.yirongma.valuecard.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.valuecard.model.CardRuleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */
public class ValueCardMakeAdapter extends BaseAdapter {

    private Context mContext;
    private List<CardRuleBean> dataList;
    private int type;
    public static final int SONG = 1;
    public static final int DE = 2;
    public static final int XIANG = 3;
    public static final int ANY = 4;
    private onMinusClickLisener mListener; //用于将点击事件回调给activity


    public ValueCardMakeAdapter(Context context, onMinusClickLisener lisener) {
        this.mContext = context;
        this.mListener = lisener;
    }

    @Override
    public int getCount() {
        return null == dataList ? 0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null == dataList ? null : dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setList(List<CardRuleBean> list) {
        if (list == null) {
            return;
        }
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.clear();
        dataList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addList(List<CardRuleBean> list) {
        if (list == null) {
            return;
        }
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void removeList(int position) {
        if (null != dataList && dataList.size() > 0) {
            dataList.remove(position);
        }
        this.notifyDataSetChanged();
    }

    public List<CardRuleBean> getDataList() {
        return dataList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        CardRuleBean dataitem = dataList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_cardrule, null);
            viewHolder.tv_song = (TextView) convertView.findViewById(R.id.tv_song);
            viewHolder.tv_rechargeMoney2 = (TextView) convertView.findViewById(R.id.tv_rechargeMoney2);
            viewHolder.tv_getMoney2 = (TextView) convertView.findViewById(R.id.tv_getMoney2);
            viewHolder.imgv_minus = (ImageView) convertView.findViewById(R.id.imgv_minus);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (type == SONG) {
            viewHolder.tv_song.setText("送（元）");
        } else if (type == DE) {
            viewHolder.tv_song.setText("得（次）");
        } else if (type == XIANG) {
            viewHolder.tv_song.setText("享（折）");
        } else {
            viewHolder.tv_song.setText("送（元）");
        }
        viewHolder.tv_rechargeMoney2.setText(dataitem.getChong());
        viewHolder.tv_getMoney2.setText(dataitem.getSong());
        viewHolder.imgv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onMinusClick(position);
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tv_song;
        TextView tv_rechargeMoney2;
        TextView tv_getMoney2;
        ImageView imgv_minus;
    }

    public interface onMinusClickLisener {
        void onMinusClick(int position);
    }

}
