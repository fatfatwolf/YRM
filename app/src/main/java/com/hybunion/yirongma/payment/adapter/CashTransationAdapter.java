package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.bean.CashTransactionInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现记录适配器
 * Created by liuyujia on 2015/5/6.
 */
public class CashTransationAdapter extends BaseAdapter {
    private Context mContext;
    public List<CashTransactionInfoBean.ObjBean> data = new ArrayList<>();
    private LayoutInflater inflater;

    public CashTransationAdapter(Context mContext) {
        super();
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    public CashTransationAdapter(Context mContext, List<CashTransactionInfoBean.ObjBean> data) {
        super();
        this.mContext = mContext;
        this.data = data;
        this.inflater = LayoutInflater.from(mContext);
    }

    public void clearData(){
        data.clear();
    }
    public void addData(List<CashTransactionInfoBean.ObjBean> info) {
        if (info == null){
            return;
        }
        data.addAll(info);

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cash_transaction_item, null);
            holder = new ViewHolder();
            holder.tv_cashamt = (TextView) convertView.findViewById(R.id.tv_cashamt);
            holder.tv_cashfee = (TextView) convertView.findViewById(R.id.tv_cashfee);
            holder.tv_cashstatus = (TextView) convertView.findViewById(R.id.tv_cashstatus);
            holder.iv_cashstatus = (ImageView) convertView.findViewById(R.id.iv_cashstatus);
            holder.ll_cashstatus = (LinearLayout) convertView.findViewById(R.id.ll_cashstatus);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CashTransactionInfoBean.ObjBean info = data.get(position);
        String tv_cashamt = String.valueOf(info.getCASHAMT());
        String tv_cashfee = String.valueOf(info.getCASHFEE());
        String tv_cashstatus = info.getCASHSTATUS();
        String tv_time = info.getCASHDATE();
        holder.tv_cashamt.setText(tv_cashamt);
        holder.tv_cashfee.setText(tv_cashfee);
        holder.tv_time.setText(tv_time);
        if (tv_cashstatus != null) {
            if (tv_cashstatus.equals("1")) {
                holder.tv_cashstatus.setVisibility(View.VISIBLE);
                holder.ll_cashstatus.setVisibility(View.GONE);
                holder.tv_cashstatus.setText("审核中");
            } else if (tv_cashstatus.equals("2") || tv_cashstatus.equals("4")) {
                holder.tv_cashstatus.setVisibility(View.GONE);
                holder.ll_cashstatus.setVisibility(View.VISIBLE);
                holder.iv_cashstatus.setImageResource(R.drawable.withdraw_pass);
            } else if (tv_cashstatus.equals("3")) {
                holder.tv_cashstatus.setVisibility(View.GONE);
                holder.ll_cashstatus.setVisibility(View.VISIBLE);
                holder.iv_cashstatus.setImageResource(R.drawable.withdraw_forbid);
            } else {
                holder.tv_cashstatus.setVisibility(View.VISIBLE);
                holder.ll_cashstatus.setVisibility(View.GONE);
                holder.tv_cashstatus.setText("审核中");
            }
        } else {
            Toast.makeText(mContext, "没有提现记录！", Toast.LENGTH_SHORT).show();
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_cashamt;
        TextView tv_cashfee;
        TextView tv_cashstatus;
        TextView tv_time;
        ImageView iv_cashstatus;
        LinearLayout ll_cashstatus;
    }
}
