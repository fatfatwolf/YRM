package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.MemberListBean;
import com.hybunion.yirongma.payment.bean.MessageHistoryBean;
import com.hybunion.yirongma.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryMsgListAdapter extends BaseAdapter {
    private Context context;

    public List<MessageHistoryBean.DataBean> dataList = new ArrayList<>();

    public HistoryMsgListAdapter(Context context, List<MessageHistoryBean.DataBean> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    public void updateList(List<MessageHistoryBean.DataBean> dataList){
        if(dataList == null)
                return;

        this.dataList = dataList;
        notifyDataSetChanged();

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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_msg_history,null);
            holder = new ViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_content = (TextView) convertView.findViewById(
                    R.id.tv_content);
            holder.tv_toMember = (TextView) convertView.findViewById(R.id.tv_toMember);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(dataList !=null && dataList.size()>0){
            MessageHistoryBean.DataBean bean = dataList.get(position);
            if(bean!=null){

                holder.tv_content.setText(bean.message);

                holder.tv_toMember.setText("推送给"+bean.memberType+": 总计"+bean.memberTotal+"人");
                holder.tv_type.setText(bean.dateType);
                holder.tv_time.setText(bean.pushDate);
            }
        }
        return convertView;
    }


    public class ViewHolder{
        TextView tv_time;
        TextView tv_content;
        TextView tv_toMember;
        TextView tv_type;
    }
}
