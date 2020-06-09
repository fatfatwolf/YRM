package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;

/**
 * @description:
 * @author: luyafeng
 * @data: 2017/6/15.
 */

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    //    private String[] types = {"微信", "支付宝", "百度钱包", "京东支付", "QQ钱包", "银联支付","众维码","快捷支付"};
    private String[] types = {"微信", "支付宝", "云闪付", "和卡"};
    //    private int[] normalIcons = {R.drawable.wechat_normal_icon, R.drawable.alipay_normal_icon,
//            R.drawable.baidu_wallet_normal_icon, R.drawable.jd_normal_icon,
//            R.drawable.qq_normal_icon, R.drawable.union_normal_icon,R.drawable.yrm_clerk,R.drawable.quick_normal_pay};
//    private int[] whiteIcons = {R.drawable.wechat_white_icon, R.drawable.alipay_white_icon,
//            R.drawable.baidu_wallet_white_icon, R.drawable.jd_white_icon,
//            R.drawable.qq_white_icon, R.drawable.union_white_icon,R.drawable.yrm_clerk_light2,R.drawable.quick_pay_while};
    private int[] normalIcons = {R.drawable.wechat_normal_icon, R.drawable.alipay_normal_icon,
            R.drawable.yinlianka,R.drawable.img_heka};
    private int[] whiteIcons = {R.drawable.wechat_white_icon, R.drawable.alipay_white_icon};

    private LayoutInflater inflater;
    private int clickStatus = -1;

    public GridViewAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setSelection(int position) {
        clickStatus = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return types.length;
    }

    @Override
    public Object getItem(int position) {
        return types[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview_layout, null);
            holder = new ViewHolder();
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.ll_item_bg = (LinearLayout) convertView.findViewById(R.id.ll_item_bg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(types[position]);
        holder.iv_icon.setImageResource(normalIcons[position]);
        if (clickStatus == position) {
            holder.ll_item_bg.setBackgroundResource(R.drawable.shape_kuang_rid);
//                holder.iv_icon.setImageResource(whiteIcons[position]);
            holder.tv_type.setTextColor(Color.parseColor("#f74948"));
        } else {
            holder.ll_item_bg.setBackgroundResource(R.drawable.shape_gray_bg);
//                holder.iv_icon.setImageResource(normalIcons[position]);
            holder.tv_type.setTextColor(context.getResources().getColor(R.color.app_black));
        }


//        if (clickStatus == -1) {
//            holder.ll_item_bg.setBackgroundResource(R.drawable.shape_gray_bg);
//            holder.tv_type.setTextColor(context.getResources().getColor(R.color.app_black));
//        } else {
//
//        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout ll_item_bg;
        ImageView iv_icon;
        TextView tv_type;
    }
}
