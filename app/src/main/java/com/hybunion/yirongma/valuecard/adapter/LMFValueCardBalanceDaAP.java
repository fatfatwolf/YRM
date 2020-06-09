package com.hybunion.yirongma.valuecard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.valuecard.model.VcBalanceDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SunBingbing
 * @date 2017/8/30
 * @email freemars@yeah.net
 * @description
 */

public class LMFValueCardBalanceDaAP extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    public List<VcBalanceDetailBean> mValueCardBeen = new ArrayList<>();

    public LMFValueCardBalanceDaAP(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void clearData() {
        mValueCardBeen.clear();
    }
    @Override
    public int getCount() {
        return mValueCardBeen.size();
    }


    @Override
    public Object getItem(int position) {
        return mValueCardBeen.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LMFValueCardBalanceDaAP.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_lmf_value_card_balance_datial_item, null);
            holder = new LMFValueCardBalanceDaAP.ViewHolder();
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            convertView.setTag(holder);
        } else {
            holder = (LMFValueCardBalanceDaAP.ViewHolder) convertView.getTag();
        }
        final VcBalanceDetailBean data = mValueCardBeen.get(position);
        holder.tv_type.setText(data.getTransType());
        holder.tv_time.setText(data.getTransTime());
        holder.tv_money.setText("¥"+data.getTransAmount());
        return convertView;
    }
    class ViewHolder {
        TextView tv_type,tv_time,tv_money;
    }
}
