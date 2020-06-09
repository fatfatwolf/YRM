package com.hybunion.yirongma.valuecard.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.valuecard.model.LMFValueCardBalanceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SunBingbing
 * @date 2017/8/30
 * @email freemars@yeah.net
 * @description
 */

public class LMFValueCardBalanceAP  extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    public List<LMFValueCardBalanceBean.ValueCardBean> mValueCardBeen = new ArrayList<>();

    public LMFValueCardBalanceAP(Context context) {
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
        LMFValueCardBalanceAP.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_lmf_vc_balance_item, null);
            holder = new LMFValueCardBalanceAP.ViewHolder();
            holder.tv_value_card_type = (TextView) convertView.findViewById(R.id.tv_value_card_type);
            holder.tv_card_number = (TextView) convertView.findViewById(R.id.tv_card_number);
            holder.tv_member = (TextView) convertView.findViewById(R.id.tv_member);
            holder.tv_contact_information = (TextView) convertView.findViewById(R.id.tv_contact_information);
            holder.tv_account = (TextView) convertView.findViewById(R.id.tv_account);
            convertView.setTag(holder);
        } else {
            holder = (LMFValueCardBalanceAP.ViewHolder) convertView.getTag();
        }
        final LMFValueCardBalanceBean.ValueCardBean data = mValueCardBeen.get(position);
            holder.tv_value_card_type.setText("卡名称："+data.getTypeName());
            holder.tv_card_number.setText("卡号："+data.getCardNo());
            holder.tv_member.setText(data.getMemCode());
            if(!TextUtils.isEmpty(data.getPhoneNum())){
                holder.tv_contact_information.setVisibility(View.VISIBLE);
                holder.tv_contact_information.setText("联系方式："+data.getPhoneNum());
            }else {
                holder.tv_contact_information.setVisibility(View.GONE);
            }
            holder.tv_account.setText("¥"+data.getBalance());
        return convertView;
    }
    class ViewHolder {
        TextView tv_value_card_type,tv_card_number,tv_member,tv_contact_information,tv_account;
    }
}
