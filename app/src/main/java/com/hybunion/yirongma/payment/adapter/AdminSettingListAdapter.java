package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.hybunion.yirongma.payment.bean.AdminSettingBean;
import com.hybunion.yirongma.R;

import java.util.List;

/**
 * 管理员设置列表用
 */

public class AdminSettingListAdapter extends BaseSwipeAdapter {
    private List<AdminSettingBean.DataBean> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnDeleteClickListener mDeleteListener;

    public AdminSettingListAdapter(Context context, List<AdminSettingBean.DataBean> dataList, OnDeleteClickListener listener){
        mContext = context;
        mDataList = dataList;
        mDeleteListener = listener;
        mInflater = LayoutInflater.from(context);
    }

    public void updateList(List<AdminSettingBean.DataBean> dataList){
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe_item_admin_setting;
    }

    @Override
    public View generateView(int i, ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.item_admin_setting,null);
        return view;
    }

    @Override
    public void fillValues(final int i, View view) {
        TextView tvName = view.findViewById(R.id.tv_name_item_admin_setting);
        TextView tvPhone = view.findViewById(R.id.tv_phone_item_admin_setting);
        TextView tvStoreName = view.findViewById(R.id.tv_store_name_item_admin_setting);
        tvName.setText(mDataList.get(i).userName);
        tvPhone.setText(mDataList.get(i).phone);
        tvStoreName.setText(mDataList.get(i).storeName);

        SwipeLayout mSl = view.findViewById(getSwipeLayoutResourceId(i));
        mSl.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转到充电站详情
                closeAllItems();
            }
        });

        view.findViewById(R.id.delete_parent_item_admin_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDeleteListener!=null)
                    mDeleteListener.onDeletClick(i,mDataList.get(i));

                closeItem(i);
            }
        });

    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public interface OnDeleteClickListener{
        void onDeletClick(int position, AdminSettingBean.DataBean dataBean);
    }


}
