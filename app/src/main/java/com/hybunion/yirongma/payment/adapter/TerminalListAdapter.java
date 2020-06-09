package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.TerminalBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.TerminalManageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款码适配器
 * Created by lyf on 2017/5/22.
 */

public class TerminalListAdapter extends BaseAdapter {
    private TerminalManageActivity activity;
    private List<TerminalBean.DataBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;

    public TerminalListAdapter(Context context) {
        activity = (TerminalManageActivity) context;
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
        if(dataList.get(position).type.equals("0")){//收款码
            holder.tv_equip_name.setText(dataList.get(position).tidName);
            holder.tv_equip_number.setText("设备编号: "+dataList.get(position).tid);
            holder.tv_equip_pwd.setVisibility(View.GONE);
            holder.iv_right_arrow.setVisibility(View.VISIBLE);
            switch (dataList.get(position).snModel){
                case "QR65":
                    holder.iv_table_card.setImageResource(R.drawable.img_qr);
                    break;
                case "ME50":
                case "ME50C":
                    holder.iv_table_card.setImageResource(R.drawable.img_mec);
                    break;
                case "SL51":
                    holder.iv_table_card.setImageResource(R.drawable.img_sl);
                    break;
                case "QM50":
                    holder.iv_table_card.setImageResource(R.drawable.img_liandi);
                    break;
                case "3.0"://意锐
                case "PPB311":
                    holder.iv_table_card.setImageResource(R.drawable.img_wsy);
                    break;
                case "QM800":
                    holder.iv_table_card.setImageResource(R.drawable.img_qm800);
                    break;
                default:
                    holder.iv_table_card.setImageResource(R.drawable.img_table_card);
                    break;
            }
        }else if(dataList.get(position).type.equals("1")){//微收银
            holder.tv_equip_name.setText(dataList.get(position).tidName);
            holder.tv_equip_number.setText("款台编号: "+dataList.get(position).tid);
            holder.tv_equip_pwd.setText("款台密钥: "+dataList.get(position).secretKey);
            holder.iv_table_card.setImageResource(R.drawable.img_wsy);
            holder.iv_right_arrow.setVisibility(View.GONE);

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
