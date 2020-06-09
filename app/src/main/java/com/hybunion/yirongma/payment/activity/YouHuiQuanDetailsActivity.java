package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.Fragment.YouHuiQuanDetailsDataFragment;
import com.hybunion.yirongma.payment.Fragment.YouHuiQuanInfoFragment;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 优惠券详情页
 */

public class YouHuiQuanDetailsActivity extends BasicActivity {
    @Bind(R.id.viewPager_youhuiquan_details_activity)
    ViewPager mVp;
    @Bind(R.id.tv_data_youhuiquan_details_activity)
    TextView mTvDataStr;
    @Bind(R.id.line_data_youhuiquan_details_activity)
    View mLineData;
    @Bind(R.id.tv_info_youhuiquan_details_activity)
    TextView mTvInfoStr;
    @Bind(R.id.line_info_youhuiquan_details_activity)
    View mLineInfo;
    @Bind(R.id.titleBar)
    public TitleBar titleBar;

    private YouHuiQuanDetailsDataFragment mDataFragment;
    private YouHuiQuanInfoFragment mInfoFragment;
    private List<Fragment> mFragmentList = new ArrayList<>(2);
    boolean isVisible = false;


    public static void start(Context from, String couponId,String couponStatus){
        Intent intent = new Intent(from, YouHuiQuanDetailsActivity.class);
        intent.putExtra("couponId",couponId);
        intent.putExtra("couponStatus",couponStatus);
        from.startActivity(intent);
    }


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_youhuiquan_details;
    }

    @Override
    public void initView() {
        super.initView();

        String couponId = getIntent().getStringExtra("couponId");
        String couponStatus = getIntent().getStringExtra("couponStatus");
        if("0".equals(couponStatus)){
            isVisible = true;
            titleBar.setRightTexViewVisible(false);
        }else {
            isVisible = false;
            titleBar.setRightTexViewVisible(false);
        }
        Bundle bundle = new Bundle();
        bundle.putString("couponId",couponId);

        mDataFragment = new YouHuiQuanDetailsDataFragment();
        mDataFragment.setArguments(bundle);
        mFragmentList.add(mDataFragment);

        mInfoFragment = new YouHuiQuanInfoFragment();
        mInfoFragment.setArguments(bundle);
        mFragmentList.add(mInfoFragment);


        mVp.setAdapter(new MyAdapter(getSupportFragmentManager()));

        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                setTab(i);

            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @OnClick({R.id.data_parent_youhuiquan_details_activity, R.id.info_parent_youhuiquan_details_activity})
    public void parentClick(RelativeLayout layout){
        switch (layout.getId()){
            case R.id.data_parent_youhuiquan_details_activity:
                titleBar.setRightTexViewVisible(false);
                setTab(0);
                mVp.setCurrentItem(0);
                break;
            case R.id.info_parent_youhuiquan_details_activity:
                titleBar.setRightTexViewVisible(isVisible);
                setTab(1);
                mVp.setCurrentItem(1);
                break;
        }
    }

    private void setTab(int index){
        mTvDataStr.setTextColor(Color.parseColor("#252e44"));
        mTvDataStr.setTextSize(15);
        mLineData.setVisibility(View.GONE);
        mTvInfoStr.setTextColor(Color.parseColor("#252e44"));
        mTvInfoStr.setTextSize(15);
        mLineInfo.setVisibility(View.GONE);

        switch (index){
            case 0:
                titleBar.setRightTexViewVisible(false);
                mTvDataStr.setTextColor(Color.parseColor("#F74948"));
                mTvDataStr.setTextSize(17);
                mLineData.setVisibility(View.VISIBLE);
                break;
            case 1:
                titleBar.setRightTexViewVisible(isVisible);
                mTvInfoStr.setTextColor(Color.parseColor("#F74948"));
                mTvInfoStr.setTextSize(17);
                mLineInfo.setVisibility(View.VISIBLE);

                break;
        }


    }



    private class MyAdapter extends FragmentPagerAdapter{


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


}
