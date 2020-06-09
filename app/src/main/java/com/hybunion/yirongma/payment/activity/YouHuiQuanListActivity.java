package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.Fragment.YouHuiQuanListFragment;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 优惠券列表
 */

public class YouHuiQuanListActivity extends BasicActivity {
    @Bind(R.id.tv_all_youhuiquan_activity)
    TextView mTvAll;
    @Bind(R.id.line_all_youhuiquan_activity)
    View mLineAll;
    @Bind(R.id.tv_onLine_youhuiquan_activity)
    TextView mTvOnLine;
    @Bind(R.id.line_onLine_youhuiquan_activity)
    View mLineOnLine;
    @Bind(R.id.tv_not_onLine_youhuiquan_activity)
    TextView mTvNotOnLine;
    @Bind(R.id.line_not_onLine_youhuiquan_activity)
    View mLineNotOnLine;
    @Bind(R.id.tv_offLine_youhuiquan_activity)
    TextView mTvOffLine;
    @Bind(R.id.line_offLine_youhuiquan_activity)
    View mLineOffLine;
    @Bind(R.id.tv_overTime_youhuiquan_activity)
    TextView mTvOverTime;
    @Bind(R.id.line_overTime_youhuiquan_activity)
    View mLineOverTime;
    @Bind(R.id.viewPager_youhuiquan_activity)
    ViewPager mVp;
    @Bind(R.id.titleBar)
    TitleBar titleBar;

    public static final String TYPE_ALL = "";   // 全部
    public static final String TYPE_ONLINE = "1";   // 已上线
    public static final String TYPE_NOT_ONLINE = "0";   // 未上线
    public static final String TYPE_OFFLINE = "2";    // 已下线
    public static final String TYPE_OVERTIME = "3";  // 已过期
    private List<YouHuiQuanListFragment> fragmentList = new ArrayList<>(5);
    YouHuiQuanListFragment allFragment;
    YouHuiQuanListFragment onlineFragment;
    YouHuiQuanListFragment notOnLineFragment;
    YouHuiQuanListFragment offlineFragment;
    YouHuiQuanListFragment overtimeFragment;
    public String loginType,couponAdmin;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_youhuiquan_list;
    }

    @Override
    public void initView() {
        super.initView();
        couponAdmin = SharedPreferencesUtil.getInstance(YouHuiQuanListActivity.this).getKey("couponAdmin");
        loginType = SharedPreferencesUtil.getInstance(YouHuiQuanListActivity.this).getKey("loginType");
        titleBar.setRightTextClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void rightClick() {
                if(loginType.equals("0") || (loginType.equals("1") && "1".equals(couponAdmin))){
                    Intent intent = new Intent(YouHuiQuanListActivity.this,MakeNoteActivity.class);
                    startActivityForResult(intent,0);
                }else {
                    ToastUtil.show("暂无权限");
                }

            }
        });
        allFragment = new YouHuiQuanListFragment();
        Bundle bundleAll = new Bundle();
        bundleAll.putString("type",TYPE_ALL);
        allFragment.setArguments(bundleAll);


        onlineFragment = new YouHuiQuanListFragment();
        Bundle bundleOnline = new Bundle();
        bundleOnline.putString("type",TYPE_ONLINE);
        onlineFragment.setArguments(bundleOnline);


        notOnLineFragment = new YouHuiQuanListFragment();
        Bundle bundleNotOnline = new Bundle();
        bundleNotOnline.putString("type",TYPE_NOT_ONLINE);
        notOnLineFragment.setArguments(bundleNotOnline);


        offlineFragment = new YouHuiQuanListFragment();
        Bundle bundleOffline = new Bundle();
        bundleOffline.putString("type",TYPE_OFFLINE);
        offlineFragment.setArguments(bundleOffline);


        overtimeFragment = new YouHuiQuanListFragment();
        Bundle bundleOvertime = new Bundle();
        bundleOvertime.putString("type",TYPE_OVERTIME);
        overtimeFragment.setArguments(bundleOvertime);


        fragmentList.add(allFragment);
        fragmentList.add(onlineFragment);
        fragmentList.add(notOnLineFragment);
        fragmentList.add(offlineFragment);
        fragmentList.add(overtimeFragment);

//        mVp.setOffscreenPageLimit(0);
        FragmentManager manager = getSupportFragmentManager();
        ViewPagerAdapter vpa = new ViewPagerAdapter(manager);
        mVp.setAdapter(vpa);
        mVp.setOffscreenPageLimit(fragmentList.size());

//        allFragment.getData(TYPE_ALL);
//        onlineFragment.getData(TYPE_ONLINE);
//        notOnLineFragment.getData(TYPE_NOT_ONLINE);
//        offlineFragment.getData(TYPE_OFFLINE);
//        overtimeFragment.getData(TYPE_OVERTIME);

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

    @OnClick({R.id.not_onLine_parent, R.id.all_parent, R.id.onLine_parent, R.id.offLine_parent, R.id.overTime_parent})
    public void click(RelativeLayout layout){
        switch (layout.getId()){
            case R.id.all_parent:   // 全部
                setTab(0);
                mVp.setCurrentItem(0);
                break;
            case R.id.onLine_parent:   // 已上线
                setTab(1);
                mVp.setCurrentItem(1);
                break;
            case R.id.not_onLine_parent:   // 未上线
                setTab(2);
                mVp.setCurrentItem(2);
                break;
            case R.id.offLine_parent:    // 已下线
                setTab(3);
                mVp.setCurrentItem(3);
                break;
            case R.id.overTime_parent:    // 已过期
                setTab(4);
                mVp.setCurrentItem(4);
                break;
        }

    }


    private void setTab(int type){
        mTvAll.setTextColor(Color.parseColor("#252e44"));
        mTvAll.setTextSize(12);
        mLineAll.setVisibility(View.GONE);
        mTvOnLine.setTextColor(Color.parseColor("#252e44"));
        mTvOnLine.setTextSize(12);
        mLineOnLine.setVisibility(View.GONE);
        mTvNotOnLine.setTextColor(Color.parseColor("#252e44"));
        mTvNotOnLine.setTextSize(12);
        mLineNotOnLine.setVisibility(View.GONE);
        mTvOffLine.setTextColor(Color.parseColor("#252e44"));
        mTvOffLine.setTextSize(12);
        mLineOffLine.setVisibility(View.GONE);
        mTvOverTime.setTextColor(Color.parseColor("#252e44"));
        mTvOverTime.setTextSize(12);
        mLineOverTime.setVisibility(View.GONE);

        switch (type){
            case 0:   // 全部
                mTvAll.setTextColor(Color.parseColor("#f74948"));
                mTvAll.setTextSize(15);
                mLineAll.setVisibility(View.VISIBLE);
                break;
            case 1:   // 已上线
                mTvOnLine.setTextColor(Color.parseColor("#f74948"));
                mTvOnLine.setTextSize(15);
                mLineOnLine.setVisibility(View.VISIBLE);
                break;
            case 2:   // 未上线
                mTvNotOnLine.setTextColor(Color.parseColor("#f74948"));
                mTvNotOnLine.setTextSize(15);
                mLineNotOnLine.setVisibility(View.VISIBLE);
                break;
            case 3:    // 已下线
                mTvOffLine.setTextColor(Color.parseColor("#f74948"));
                mTvOffLine.setTextSize(15);
                mLineOffLine.setVisibility(View.VISIBLE);
                break;
            case 4:    // 已过期
                mTvOverTime.setTextColor(Color.parseColor("#f74948"));
                mTvOverTime.setTextSize(15);
                mLineOverTime.setVisibility(View.VISIBLE);
                break;
        }
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public YouHuiQuanListFragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 0:
                allFragment.loadData(TYPE_ALL);
                onlineFragment.loadData(TYPE_ONLINE);
                notOnLineFragment.loadData(TYPE_NOT_ONLINE);
                offlineFragment.loadData(TYPE_OFFLINE);
                overtimeFragment.loadData(TYPE_OVERTIME);
                break;
        }
    }

}
