package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.bean.AllBankBean;
import com.hybunion.yirongma.payment.view.cache.ImageLoader;

import java.util.List;

/**
 * Created by liujia on 2016/1/6.
 */
public class QueryAllBankNameAdapter extends BaseAdapter {
    private Context mContext;
    public List<AllBankBean.BankInfo> mBankList;
    private LayoutInflater inflater;
    private ImageLoader mImageLoader;
    public QueryAllBankNameAdapter(Context mContext) {
        this.mContext = mContext;
        mImageLoader = ImageLoader.getInstance(mContext);
        inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addAllList(List<AllBankBean.BankInfo> mBankList){
        this.mBankList = mBankList;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mBankList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBankList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_query_all_bank_item, null);
            holder=new ViewHolder();
            holder.bank_img= (ImageView) convertView.findViewById(R.id.bank_img);
            holder.bank_name_tv= (TextView) convertView.findViewById(R.id.bank_name_tv);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
        AllBankBean.BankInfo mBankBean=mBankList.get(position);
        Glide.with(mContext).load(mBankBean.getPaymentBankImg())
                .error(R.drawable.bank_card_failed)
                .into(holder.bank_img);

        holder.bank_name_tv.setText(mBankBean.getPaymentBank());

        return convertView;
    }

    static class ViewHolder{
        ImageView bank_img;
        TextView bank_name_tv;
    }
}
