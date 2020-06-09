package com.hybunion.yirongma.payment.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.TextBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.BannerViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 Banner 图
 */

public class MyBannerView extends RelativeLayout {
    private Context mContext;
    private ViewPager mVp;
    private LinearLayout mRectParent;
    private int mPosition;  // 记录上一次 ViewPager 显示的图片位置。
    private int mPositionCurrent;  // 当前 ViewPager 的位置。
    private int mTime = 4000; // 轮播图轮播延迟时间
    private List<TextView> mTvList; // 放图片下面的黄色小块
    private boolean mIsFirst = true;
    private BannerViewPagerAdapter mAdapter;
    private BannerViewPagerAdapter.OnBannerItemListener mBannerItemListener;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPositionCurrent++;
            mVp.setCurrentItem(mPositionCurrent);
            mHandler.sendEmptyMessageDelayed(1, mTime);
        }
    };

    public MyBannerView(Context context) {
        super(context);
    }

    public MyBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_banner_house_first_fragment, null);
        this.addView(view);
        initView(view);

    }
    public MyBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(View view) {
        mVp = (ViewPager) view.findViewById(R.id.viewPager_banner_house_first_fragment);
        mVp.setPageMargin(24);
        mVp.setOffscreenPageLimit(3);
        mRectParent = (LinearLayout) view.findViewById(R.id.dotParent_banner_house_first_fragment);

    }

    public void setData(List<TextBean.DataBean> data) {
        initData(data);
    }

    // 钱包用，这里将轮播时间改成了 5s
    public void setData(List<TextBean.DataBean> data, BannerViewPagerAdapter.OnBannerItemListener listener) {
        mBannerItemListener = listener;
        initData(data);
        mTime = 5000;
    }



    private void initData(final List<TextBean.DataBean> data){
        mAdapter = new BannerViewPagerAdapter(mContext, data, mBannerItemListener);
        mVp.setAdapter(mAdapter);
        if (mRectParent != null)
            mRectParent.removeAllViews();
        if (data.size() == 1) return;   // 如果只有一条，就不用轮播了。
        mPosition = 0;  // 重置，将此值置0。
        mPositionCurrent = data.size() * 100 / 2;
        mVp.setCurrentItem(mPositionCurrent);
        if (mTvList == null)
            mTvList = new ArrayList<>();
        mTvList.clear();
        for (int i = 0; i < data.size(); i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10,10);
            TextView tv = new TextView(mContext);
            if (i == 0) {
                tv.setBackgroundResource(R.drawable.shape_banner_selected);
                layoutParams.width = 30;
            } else {
                tv.setBackgroundResource(R.drawable.shape_banner_unselected);
            }

            layoutParams.setMargins(20, 0, 0, 20);


            mTvList.add(tv);
            mRectParent.addView(tv, layoutParams);
        }
        mVp.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        // 当参数为null时，handler将移除所有的回调和消息
                        mHandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_CANCEL:// 事件取消
                        // 给handler发一条消息即可让它自动继续轮播
                        mHandler.sendEmptyMessageDelayed(0, mTime);
                        break;
                    case MotionEvent.ACTION_UP:// 抬起
                        mHandler.sendEmptyMessageDelayed(0, mTime);
                        break;

                    default:
                        break;
                }

                return false;
            }
        });
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override

            public void onPageSelected(int position) {
                TextView tvLast = mTvList.get(mPosition % data.size());
                TextView tvNow = mTvList.get(position % data.size());
                LinearLayout.LayoutParams layoutParamsLast = new LinearLayout.LayoutParams(10,10);
                layoutParamsLast.setMargins(20, 0, 0, 20);
                LinearLayout.LayoutParams layoutParamsNow = new LinearLayout.LayoutParams(30,10);
                layoutParamsNow.setMargins(20, 0, 0, 20);
                tvLast.setLayoutParams(layoutParamsLast);
                tvLast.setBackgroundResource(R.drawable.shape_banner_unselected);
                tvNow.setLayoutParams(layoutParamsNow);
                tvNow.setBackgroundResource(R.drawable.shape_banner_selected);
//
//                mTvList.get(mPosition % data.size()).setBackgroundResource(R.drawable.shape_banner_unselected);
//                mTvList.get(position % data.size()).setBackgroundResource(R.drawable.shape_banner_selected);
                mPositionCurrent = position;
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (mIsFirst) {
            mIsFirst = false;
            mHandler.sendEmptyMessageDelayed(1, mTime);
        }


    }




}
