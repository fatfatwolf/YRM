package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hybunion.yirongma.payment.bean.QueryTransBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.util.GetApplicationInfoUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.cache.ImageLoader;
import com.hybunion.yirongma.payment.activity.RefundDetailsActivity;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.text.DecimalFormat;
import java.util.List;


public class LMFBillDataDetailAdapter extends BaseAdapter {
    private Context context;
    private List<QueryTransBean.DataBean> list;
    private LayoutInflater inflater;
    ImageLoader imageLoader;
    private String loginType;

    public LMFBillDataDetailAdapter(Context context, List<QueryTransBean.DataBean> list) {
        this.context = context;
        this.imageLoader = ImageLoader.getInstance(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void addAllList(List<QueryTransBean.DataBean> list) {
        if (list == null) {
            return;
        }
//        if(isRefresh){
//            dataList.clear();
//        }
//        dataList.clear();
        this.list = list;
        notifyDataSetChanged();

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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.value_card_billing_da_child_item1, null);
            holder = new ViewHolder();
            holder.tv_czk_type = (TextView) convertView.findViewById(R.id.tv_czk_type);
            holder.tv_czk_data = (TextView) convertView.findViewById(R.id.tv_czk_data);
            holder.tv_czk_money = (TextView) convertView.findViewById(R.id.tv_czk_money);
            holder.tv_vc_num = (TextView) convertView.findViewById(R.id.tv_vc_num);
            holder.img_czk_type = (ImageView) convertView.findViewById(R.id.img_czk_type);
            holder.tv_order_no = convertView.findViewById(R.id.tv_order_no);
            holder.mTvYouHui = convertView.findViewById(R.id.tv_youhui);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final QueryTransBean.DataBean bean = list.get(position);
        if (!TextUtils.isEmpty(bean.tidName))
            holder.tv_vc_num.setText(bean.tidName);
        else
            holder.tv_vc_num.setText("");

        if (bean.payChannel != null)
            holder.tv_czk_type.setText(bean.payChannel);

        if (bean.transTime != null)
            holder.tv_czk_data.setText(bean.transTime);
        else
            holder.tv_czk_data.setText("");

        if (bean.orderNo != null && bean.orderNo.length() > 4)
            holder.tv_order_no.setText(bean.orderNo.substring(bean.orderNo.length() - 4, bean.orderNo.length()));

        DecimalFormat myformat = new java.text.DecimalFormat("0.00");
        String str = "0.00";
        double transAmtDouble = 0;
        if (TextUtils.isEmpty(bean.transAmount)) {

        } else {
            transAmtDouble = Double.valueOf(bean.transAmount);
            str = myformat.format(transAmtDouble);
        }
        if (TextUtils.isEmpty(list.get(position).UUID)) {  // 如果 UUID 没有，则是推送过来的数据，交易金额颜色改变
            holder.tv_czk_money.setTextColor(context.getResources().getColor(R.color.text_color));
        } else {
            holder.tv_czk_money.setTextColor(context.getResources().getColor(R.color.text_color2));
        }
        holder.tv_czk_money.setText(str);
        holder.mTvYouHui.setVisibility(View.GONE);
        if ("退款中".equals(bean.payChannel)) {
            holder.tv_czk_type.setTextColor(context.getResources().getColor(R.color.text_color2));
            holder.tv_czk_money.setText(str);
            holder.tv_czk_type.setTextColor(context.getResources().getColor(R.color.yellow_text));
        } else if ("退款失败".equals(bean.payChannel)) {
            holder.tv_czk_type.setTextColor(context.getResources().getColor(R.color.btn_back_color1));
            holder.tv_czk_money.setText(str);
        } else if ("退款成功".equals(bean.payChannel)) {
            holder.tv_czk_type.setTextColor(context.getResources().getColor(R.color.main_color2));
            holder.tv_czk_money.setText("— " + str);
            holder.tv_czk_money.setTextColor(context.getResources().getColor(R.color.main_color2));
        } else {
            holder.tv_czk_type.setTextColor(context.getResources().getColor(R.color.text_color2));
            holder.tv_czk_money.setText(str);
            if (!TextUtils.isEmpty(bean.payableAmount)) {
                double payableAmountD = YrmUtils.stringToDouble(bean.payableAmount);
                if (transAmtDouble != 0 && payableAmountD != 0 && transAmtDouble > payableAmountD) {
                    double youhuiD = transAmtDouble - payableAmountD;
                    String youhuiStr = myformat.format(youhuiD);
                    holder.mTvYouHui.setVisibility(View.VISIBLE);
                    holder.mTvYouHui.setText("— " + youhuiStr);
                }
            }
//            holder

        }
//        bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/Alipay.png";
        if (TextUtils.isEmpty(bean.iconUrl)) {
            holder.img_czk_type.setVisibility(View.INVISIBLE);
        } else {
            holder.img_czk_type.setVisibility(View.VISIBLE);
//            imageLoader.DisplayImage(bean.iconUrl, holder.img_czk_type, false);
            Glide.with(context).load(bean.iconUrl).into(holder.img_czk_type);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent detailIntent = new Intent(context, RefundDetailsActivity.class);
                detailIntent.putExtra("transAmount", bean.transAmount);
                detailIntent.putExtra("orderNo", bean.orderNo);
                detailIntent.putExtra("payChannel", bean.payChannel);
                detailIntent.putExtra("transDate", bean.transDate);
                detailIntent.putExtra("payStyle", bean.payStyle);
                detailIntent.putExtra("payableAmount", bean.payableAmount);
                loginType = SharedPreferencesUtil.getInstance(context).getKey("loginType");
                if (("0").equals(loginType)) {
                    detailIntent.putExtra("merId", GetApplicationInfoUtil.getMerchantId());
                } else {
                    detailIntent.putExtra("merId", SharedPreferencesUtil.getInstance(context).getKey("shopId"));
                }
                detailIntent.putExtra("UUID", bean.UUID);
                context.startActivity(detailIntent);
            }

        });
        return convertView;
    }


    class ViewHolder {
        ImageView img_czk_type;
        TextView tv_czk_type;
        TextView tv_czk_data;
        TextView tv_vc_num;
        TextView tv_czk_money;
        TextView tv_order_no;
        TextView mTvYouHui;
    }
}
