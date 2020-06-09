package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.MainMassageBean;
import com.hybunion.yirongma.R;

import java.util.List;

/**
 * Created by admin on 2018/2/27.
 */

public class MainMassageAdapter extends BaseAdapter{
    public Context mContext;
    public List<MainMassageBean.DataBean> mainMassageBeen;
    private LayoutInflater inflater;

    public MainMassageAdapter(Context mContext, List<MainMassageBean.DataBean> mainMassageBeen) {
        super();
        this.mContext = mContext;
        this.mainMassageBeen = mainMassageBeen;
        this.inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mainMassageBeen.size();
    }

    @Override
    public Object getItem(int i) {
        return mainMassageBeen.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.main_massage_item,null );
            holder=new ViewHolder();
            holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            holder.tv_title=(TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_createdate=(TextView) convertView.findViewById(R.id.tv_createdate);
            holder.tv_content_message = (TextView) convertView.findViewById(R.id.tv_content_message);
            holder.tvDot = (TextView) convertView.findViewById(R.id.tv_dian);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        String title = mainMassageBeen.get(position).getTitle();
        if(title == null){
            title = "";
        }
        holder.tv_title.setText(title);
        String createDate = mainMassageBeen.get(position).getCreateDate();
        if(createDate == null){
            createDate = "";
        }
        holder.tv_createdate.setText(createDate);
        String viewStatus = mainMassageBeen.get(position).getViewStatus();
        String content = mainMassageBeen.get(position).getMessage();
        holder.tv_content_message.setText(Html.fromHtml(content));
        if ("1".equals(viewStatus)){
            holder.tvDot.setVisibility(View.VISIBLE);
//            holder.img_icon.setBackgroundResource(R.drawable.unread_msg);
        }else {
            holder.tvDot.setVisibility(View.GONE);
//            holder.img_icon.setBackgroundResource(R.drawable.read_msg);
        }

        return convertView;
    }
    private static class ViewHolder{
        ImageView img_icon;
        TextView tv_title;
        TextView tv_createdate;
        TextView tv_content_message;
        TextView tvDot;
    }
}
