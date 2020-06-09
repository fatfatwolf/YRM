package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.bean.ChangeRecordBean;

import java.util.List;

/**
 * Created by lyf on 2017/5/21.
 */

public class ChangeRecordListAdapter extends BaseAdapter {

    private Context context;
    private List<ChangeRecordBean.ObjEntity.RowsEntity> list;
    private LayoutInflater inflater;

    public ChangeRecordListAdapter(Context context, List<ChangeRecordBean.ObjEntity.RowsEntity> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_approve_list, null);
            holder.tv_approve_date = (TextView) convertView.findViewById(R.id.tv_approve_date);
            holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
            holder.tv_approve_status = (TextView) convertView.findViewById(R.id.tv_approve_status);
            holder.tv_bankCardNumber = (TextView) convertView.findViewById(R.id.tv_bankCardNumber);
            holder.tv_sendBack_reason = (TextView) convertView.findViewById(R.id.tv_sendBack_reason);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChangeRecordBean.ObjEntity.RowsEntity dataList = list.get(position);
        String approveStatus = dataList.getAPPROVESTATUS();
        String approveContent = dataList.getPROCESSCONTEXT();
        holder.tv_approve_date.setText("时间：" + dataList.getMAINTAINDATE());
        holder.tv_userName.setText("姓名：" + dataList.getBANKACCNAME());
        holder.tv_bankCardNumber.setText("卡号：" + dataList.getBANKACCNO());

        if (TextUtils.isEmpty(approveContent) || "".equals(approveContent)) {
            holder.tv_sendBack_reason.setVisibility(View.GONE);
        } else {
            holder.tv_sendBack_reason.setVisibility(View.VISIBLE);
            holder.tv_sendBack_reason.setText("受理描述：" + dataList.getPROCESSCONTEXT());
        }
        if ("Y".equals(approveStatus)) {
            holder.tv_approve_status.setText(Html.fromHtml("受理状态：" + "<font color='#00BAFF'>" + "已成功" + "</font>"));
        } else if ("K".equals(approveStatus)) {
            holder.tv_approve_status.setText(Html.fromHtml("受理状态：" + "<font color='#F24642'>" + "退回" + "</font>"));
        } else if ("Z".equals(approveStatus)) {
            holder.tv_approve_status.setText(Html.fromHtml("受理状态：" + "<font color='#F98F81'>" + "待审批" + "</font>"));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_approve_date; //时间
        TextView tv_userName; //姓名
        TextView tv_bankCardNumber; //卡号
        TextView tv_sendBack_reason; //受理描述
        TextView tv_approve_status;//受理状态
    }
}
