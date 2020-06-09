package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.TerminalBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款码适配器
 * Created by lyf on 2017/5/22.
 */

public class TerminalManageAdapter extends BaseAdapter {
    private Context context;
    private List<TerminalBean.DataBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    public TerminalManageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void addAllList(List<TerminalBean.DataBean> list) {
        if (list == null) {
            return;
        }
        dataList = list;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public TerminalBean.DataBean getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_terminal_list, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(dataList.get(position).type.equals("0")) {//收款码
            holder.tv_equip_name.setText(dataList.get(position).tidName);
            holder.tv_equip_number.setText("设备编号: " + dataList.get(position).tid);
            holder.iv_table_card.setImageResource(R.drawable.img_table_card);
            if(TextUtils.isEmpty(dataList.get(position).limitAmt)){
                holder.tv_equip_pwd.setVisibility(View.GONE);
            }else {
                holder.tv_equip_pwd.setVisibility(View.VISIBLE);
                holder.tv_equip_pwd.setText("固定金额: "+dataList.get(position).limitAmt+" 元");
            }
            holder.iv_right_arrow.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    private class ViewHolder {
        private TextView tv_equip_name, tv_equip_number, tv_equip_pwd;
        private ImageView iv_table_card,iv_right_arrow;

        public ViewHolder(View view) {
            tv_equip_name = view.findViewById(R.id.tv_equip_name);
            tv_equip_number = view.findViewById(R.id.tv_equip_number);
            tv_equip_pwd = view.findViewById(R.id.tv_equip_pwd);
            iv_table_card = view.findViewById(R.id.iv_table_card);
            iv_right_arrow = view.findViewById(R.id.iv_right_arrow);
        }

    }
}
