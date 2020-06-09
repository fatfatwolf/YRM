package com.hybunion.yirongma.payment.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hybunion.yirongma.payment.bean.TextBean;
import com.hybunion.yirongma.R;

import java.util.List;

/**
 * 首页 Banner 中 ViewPager 的 adapter
 */

public class BannerViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<TextBean.DataBean> mList;
    private LayoutInflater mInflater;
    private OnBannerItemListener mListener;

    public BannerViewPagerAdapter(Context mContext, List<TextBean.DataBean> mList, OnBannerItemListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
        mListener = listener;
    }


    @Override
    public int getCount() {
        if (mList.size() > 1) {
            return mList.size() * 100;
        } else {
            return mList.size();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mInflater.inflate(R.layout.banner_viewpager, container, false);
        ImageView imageView = view.findViewById(R.id.img_house_details_viewpager);
        Glide.with(mContext).load(mList.get(position % mList.size()).getImgUrl()).error(R.drawable.img_zhanwei).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.bannerItemListener(position);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public interface OnBannerItemListener {
        void bannerItemListener(int position);
    }


}
