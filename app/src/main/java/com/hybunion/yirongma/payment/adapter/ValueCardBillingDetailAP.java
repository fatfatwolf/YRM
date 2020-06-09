package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.model.BillingDABean;
import com.hybunion.yirongma.valuecard.activity.VcBillingDetailsAt;

import java.util.ArrayList;
import java.util.List;

import static com.hybunion.yirongma.R.id.img_czk_type;

/**
 * @author lyj
 * @date 2017/8/28
 * @email freemars@yeah.net
 * @description
 */

public class ValueCardBillingDetailAP extends BaseExpandableListAdapter {
    private Context mContext;
    public List<BillingDABean.DataBeanX> group = new ArrayList<>();
    private LayoutInflater inflater;

    public ValueCardBillingDetailAP(Context mContext) {
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addDataSources(List<BillingDABean.DataBeanX> list) {
        if (group.size() > 0 && list != null && list.size() > 0) {
            BillingDABean.DataBeanX lastBean = group.get(group.size() - 1);  //取出最后一条记录
            BillingDABean.DataBeanX newFirstBean = list.get(0);  //取出最新拿到的数据的第一条记录
            if (lastBean.getDayDate().equals(newFirstBean.getDayDate())) {
                lastBean.getDetails().addAll(newFirstBean.getDetails());
                list.remove(newFirstBean);
            }
        }
        if (list != null) {
            group.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return group == null ? 0 : group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return group.get(groupPosition).getDetails().size();
    }

    @Override
    public BillingDABean.DataBeanX getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public BillingDABean.DataBeanX.DataBean getChild(int groupPosition, int childPosition) {
        return group.get(groupPosition).getDetails().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ValueCardBillingDetailAP.ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.value_card_billing_da_item, null);
            holder = new ValueCardBillingDetailAP.ViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        } else {
            holder = (ValueCardBillingDetailAP.ViewHolder) convertView.getTag();
        }
        BillingDABean.DataBeanX data = group.get(groupPosition);
        holder.tv_time.setText(data.getDayDate());
        holder.tv_count.setText("充售卡总计: ¥" + data.getDayAmount() + " | " + data.getDayCount() + "笔");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ValueCardBillingDetailAP.ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.value_card_billing_da_child_item, null);
            holder = new ValueCardBillingDetailAP.ChildViewHolder();
            holder.tv_czk_type = (TextView) convertView.findViewById(R.id.tv_czk_type);
            holder.tv_czk_data = (TextView) convertView.findViewById(R.id.tv_czk_data);
            holder.tv_czk_money = (TextView) convertView.findViewById(R.id.tv_czk_money);
            holder.tv_vc_num = (TextView) convertView.findViewById(R.id.tv_vc_num);
            holder.img_czk_type = (ImageView) convertView.findViewById(img_czk_type);
            holder.tv_successful_refund = (TextView) convertView.findViewById(R.id.tv_successful_refund);
            convertView.setTag(holder);
        } else {
            holder = (ValueCardBillingDetailAP.ChildViewHolder) convertView.getTag();
        }
        final BillingDABean.DataBeanX.DataBean bean = getChild(groupPosition, childPosition);
        holder.tv_czk_type.setText(bean.getShowStatus());
        holder.tv_czk_data.setText(bean.getSimpleDate());
        holder.tv_vc_num.setText(bean.getCardNo());
        String cardType = bean.getCardType();
        if ("0".equals(cardType)) {
            holder.img_czk_type.setImageResource(R.drawable.img_lmf_cicard);
        } else if ("1".equals(cardType)) {
            holder.img_czk_type.setImageResource(R.drawable.img_lmf_any_card);
        } else if ("2".equals(cardType)) {
            holder.img_czk_type.setImageResource(R.drawable.img_lmf_charging_card);
        } else if ("3".equals(cardType)) {
            holder.img_czk_type.setImageResource(R.drawable.img_lmf_discount_card);
        } else {
            holder.img_czk_type.setImageResource(R.drawable.img_lmf_any_card);
        }
        String transType = bean.getTransType();//0 消费成功 1充值成功 4 购卡成功 2 消费撤销 5 购卡撤销 8 充值撤销
        /**
         * 消费成功，减。充值撤销，减，购卡撤销，减
         * 充值成功，加，购卡成功，加， 消费撤销，加，
         * */
        if ("0".equals(transType) || "5".equals(transType) || "8".equals(transType)) {
            if (bean.getTransAmount() != null) {
                holder.tv_czk_money.setText("-" + bean.getTransAmount());
            } else {
                holder.tv_czk_money.setText("-");
            }
        } else if ("1".equals(transType) || "4".equals(transType) || "2".equals(transType)) {
            if (bean.getTransAmount() != null) {
                holder.tv_czk_money.setText("+" + bean.getTransAmount());
            }else {
                holder.tv_czk_money.setText("+");
            }
        }
        if ("0".equals(transType) || "1".equals(transType) || "4".equals(transType)) {
            holder.tv_czk_type.setTextColor(Color.GRAY);
        } else if ("2".equals(transType) || "5".equals(transType) || "8".equals(transType)) {
            holder.tv_czk_type.setTextColor(Color.RED);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, VcBillingDetailsAt.class);
                detailIntent.putExtra("transAmount", bean.getTransAmount());
                detailIntent.putExtra("orderNo", bean.getOrderNo());
                detailIntent.putExtra("cardNo", bean.getCardNo());
                detailIntent.putExtra("transType", bean.getTransType());
                detailIntent.putExtra("simpleDate", bean.getSimpleDate());
                detailIntent.putExtra("dateSimple", bean.getDateSimple());
                mContext.startActivity(detailIntent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        TextView tv_time, tv_count;
    }

    class ChildViewHolder {
        TextView tv_czk_type, tv_czk_data, tv_czk_money, tv_vc_num, tv_successful_refund;
        ImageView img_czk_type;
    }
}
