package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.hybunion.yirongma.payment.bean.QueryClerkListBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.inteface.OnButtonClickListener;

import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */
public class FavoriteStationListViewAdapter extends BaseSwipeAdapter {
    private LayoutInflater inflater;
    public List<QueryClerkListBean.ObjBean> dataList;
    private Context mContext;
    private SwipeLayout mSwipeLayout;
    private OnButtonClickListener MonButtonClickListener;
    public FavoriteStationListViewAdapter(Context mContext, List<QueryClerkListBean.ObjBean> dataList, OnButtonClickListener onButtonClickListener) {
        this.mContext = mContext;
        this.dataList = dataList;
        inflater = LayoutInflater.from(mContext);
        this.MonButtonClickListener = onButtonClickListener;
    }
    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
    /**
     * 此方法中一定不能绑定监听器和填充数据
     * never bind listeners or fill values, just genertate view here !!
     *
     * @param position
     * @param parent
     * @return
     */
    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.clerk_setting_item, null);
        return v;
    }

    public void updateList(List<QueryClerkListBean.ObjBean> list, boolean isRefresh) {
        if (list == null) {
            return;
        }
//        if(isRefresh){
//            dataList.clear();
//        }
//        dataList.addAll(list);
        dataList= list;
        notifyDataSetChanged();

    }


    @Override
    public void fillValues(final int position, View convertView) {
        TextView tv_clerk_name = (TextView) convertView.findViewById(R.id.tv_clerk_name);
        LogUtil.d(dataList.get(position).getEmployName()+"店员名称");
        tv_clerk_name.setText(dataList.get(position).getEmployName());
        ImageView img_activity = (ImageView) convertView.findViewById(R.id.img_activity);
        TextView tv_clerk_number = (TextView) convertView.findViewById(R.id.tv_clerk_number);
        tv_clerk_number.setText(dataList.get(position).getEmployPhone());
//        LinearLayout ll_agin_send = (LinearLayout) convertView.findViewById(R.id.ll_agin_send);
            String position1 = dataList.get(position).getPosition();
            if ("店长".equals(position1)){//店长
                img_activity.setBackgroundResource(R.drawable.img_shop_manager2);
//                ll_agin_send.setVisibility(View.GONE);
            }else {
                img_activity.setBackgroundResource(R.drawable.img_shop_worker);

//                ll_agin_send.setVisibility(View.VISIBLE);
        }
        mSwipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        //绑定监听事件
        mSwipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.tv_cancel_favorite));
            }
        });
        /**
         * 用getSurfaceView()可以防止滑回与点击事件冲突
         */
        mSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转到充电站详情
                closeAllItems();
            }
        });
        //删除
        convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//bottomView点击事件
                MonButtonClickListener.onDeleteClerk(position,dataList.get(position));
                closeItem(position);
            }
        });
        //发送二维码
//        ll_agin_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {//bottomView点击事件
//                MonButtonClickListener.onDialog(position,dataList.get(position));
//               // closeItem(position);
//            }
//        });
    }
    @Override
    public int getCount() {
        LogUtil.d("dataListSize--->" + dataList.size());
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
}