package com.hybunion.yirongma.valuecard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.bean.ValueCardsListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/1/9.
 */

public class ValueCardsListAdapter extends BaseAdapter{
    private String cardNumber;
    private Context context;
    private LayoutInflater inflater;
    public List<ValueCardsListBean.DataBean> valueCardBean = new ArrayList<>();
    public ValueCardsListAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void clearData() {
        valueCardBean.clear();
    }
    @Override
    public int getCount() {
        return valueCardBean.size();
    }

    @Override
    public Object getItem(int i) {
        return valueCardBean.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView==null){
            inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.value_cards_list_item,null);
            holder=new ViewHolder();
            holder.ll_value_card = (LinearLayout) convertView.findViewById(R.id.ll_value_card);
            holder.tv_card_type= (TextView) convertView.findViewById(R.id.tv_card_type);
            holder.tv_card_money=(TextView) convertView.findViewById(R.id.tv_card_money);
            holder.tv_card_number=(TextView) convertView.findViewById(R.id.tv_card_number);
            holder.tv_card_valid=(TextView) convertView.findViewById(R.id.tv_card_valid);
            holder.tv_card_type_company = (TextView) convertView.findViewById(R.id.tv_card_type_company);
            holder.tv_discountRate = (TextView) convertView.findViewById(R.id.tv_discountRate);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
          holder.tv_card_type.setText(valueCardBean.get(i).getCardName());
          holder.tv_card_money.setText(valueCardBean.get(i).getBalance());
          formattingMethod(valueCardBean.get(i).getCardNo());
          holder.tv_card_number.setText(cardNumber);
          holder.tv_card_valid.setText("截止日期："+valueCardBean.get(i).getExpireDate());
          String cardType = valueCardBean.get(i).getCardType();//0：次卡；1：金额卡；2：充送卡；3：折扣卡；4：任意金额卡
          if ("0".equals(cardType)){
              holder.tv_discountRate.setVisibility(View.GONE);
              holder.tv_card_type_company.setText("次");
              holder.tv_card_type.setTextColor(context.getResources().getColor(R.color.ci_card));
              holder.ll_value_card.setBackgroundResource(R.drawable.img_silvery_card);
              holder.tv_card_type_company.setTextColor(context.getResources().getColor(R.color.ci_card_num));
              holder.tv_card_money.setTextColor(context.getResources().getColor(R.color.ci_card_num));
              holder.tv_card_number.setTextColor(context.getResources().getColor(R.color.ci_card_num));
              holder.tv_card_valid.setTextColor(context.getResources().getColor(R.color.ci_card_num));
          }else {
              holder.tv_card_type_company.setText("元");
              if ("1".equals(cardType)){
                  holder.tv_discountRate.setVisibility(View.GONE);
                  holder.tv_card_type.setTextColor(context.getResources().getColor(R.color.white));
                  holder.ll_value_card.setBackgroundResource(R.drawable.img_red_card);
                  holder.tv_card_type_company.setTextColor(context.getResources().getColor(R.color.white));
                  holder.tv_card_money.setTextColor(context.getResources().getColor(R.color.white));
                  holder.tv_card_number.setTextColor(context.getResources().getColor(R.color.white));
                  holder.tv_card_valid.setTextColor(context.getResources().getColor(R.color.white));
              }else if ("2".equals(cardType)){
                  holder.tv_discountRate.setVisibility(View.GONE);
                  holder.tv_card_type.setTextColor(context.getResources().getColor(R.color.white));
                  holder.ll_value_card.setBackgroundResource(R.drawable.img_black_card);
                  holder.tv_card_type_company.setTextColor(context.getResources().getColor(R.color.white));
                  holder.tv_card_money.setTextColor(context.getResources().getColor(R.color.white));
                  holder.tv_card_number.setTextColor(context.getResources().getColor(R.color.textColor_9));
                  holder.tv_card_valid.setTextColor(context.getResources().getColor(R.color.textColor_9));
              }else if ("3".equals(cardType)){
                  holder.tv_discountRate.setVisibility(View.VISIBLE);
                  holder.tv_discountRate.setText("("+valueCardBean.get(i).getDiscountRate()+"折)");
                  holder.tv_card_type.setTextColor(context.getResources().getColor(R.color.zhekou_card));
                  holder.ll_value_card.setBackgroundResource(R.drawable.img_golden_card);
                  holder.tv_card_type_company.setTextColor(context.getResources().getColor(R.color.zhekou_card_num));
                  holder.tv_card_money.setTextColor(context.getResources().getColor(R.color.zhekou_card_num));
                  holder.tv_card_number.setTextColor(context.getResources().getColor(R.color.zhekou_card_num));
                  holder.tv_card_valid.setTextColor(context.getResources().getColor(R.color.zhekou_card_num));
              }else if ("4".equals(cardType)){
                  holder.tv_discountRate.setVisibility(View.GONE);
                  holder.tv_card_type.setTextColor(context.getResources().getColor(R.color.white));
                  holder.ll_value_card.setBackgroundResource(R.drawable.img_red_card);
                  holder.tv_card_type_company.setTextColor(context.getResources().getColor(R.color.white));
                  holder.tv_card_money.setTextColor(context.getResources().getColor(R.color.white));
                  holder.tv_card_number.setTextColor(context.getResources().getColor(R.color.white));
                  holder.tv_card_valid.setTextColor(context.getResources().getColor(R.color.white));
              }
          }
        return convertView;
    }
    private void formattingMethod(String cardNo){
        char[] array = cardNo.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if((i + 1) % 4 == 0 && i != array.length - 1) {
                sb.append(" ");
            }
        }
        cardNumber = sb.toString();
    }
    static class ViewHolder{
        LinearLayout ll_value_card;
        TextView tv_card_type;
        TextView tv_card_money;
        TextView tv_card_number;
        TextView tv_card_valid;
        TextView tv_card_type_company;
        TextView tv_discountRate;
    }
}
