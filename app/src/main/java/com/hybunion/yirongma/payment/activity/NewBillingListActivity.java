package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.QueryTransBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BasicFragment;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.Fragment.BillingListFragment;
import com.hybunion.yirongma.payment.Fragment.HuiChuZhiListFragment;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Jairus on 2019/7/26.
 */

public class NewBillingListActivity extends BasicActivity {
    @Bind(R.id.tv_shoukuanma_str_new_billing)
    TextView mTvShouKuanMaStr;
    @Bind(R.id.line1_new_billing)
    View mLine1;
    @Bind(R.id.tv_huichongzhi_str_new_billing)
    TextView mTvHuiChuZhiStr;
    @Bind(R.id.line2_new_billing)
    View mLine2;
    @Bind(R.id.vp_new_billing)
    ViewPager mVp;
    @Bind(R.id.tv_today_new_billing)
    TextView mTvToday;
    @Bind(R.id.titlaBar_new_billing)
    TitleBar mTitleBar;
    @Bind(R.id.tab_parent_new_billing)
    RelativeLayout mTabParent;

    private List<BasicFragment> mFragmentList = new ArrayList<>();
    private BillingListFragment mBillingFragment;
    private HuiChuZhiListFragment mHuiChuZhiFragment;
    private int mChooseType; // 当前展示的类型。   0---收款码    1---惠储值
    String isCoupon;
    // type=1---订单明细    type=2---班结汇总进来
    public static void start(Context from){
        Intent intent = new Intent(from, NewBillingListActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_new_billing_list;
    }

    @Override
    public void initView() {
        super.initView();
        // 筛选按钮监听
        mTitleBar.setRightTextClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void rightClick() {
                switch (mChooseType){
                    case 0:  // 选择了 收款码
                        Intent intent = new Intent(NewBillingListActivity.this, ScreeningActivity.class);
                        startActivity(intent);
                        break;
                    case 1:  // 选择了 惠储值
                        Intent intent1 = new Intent(NewBillingListActivity.this, HuiChuZhiScreeningActivity.class);
                        startActivity(intent1);
                        break;

                }
            }
        });
        // 判断是否开通储值卡
        String vcSale = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.VC_SALE);
        isCoupon = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.IS_COUPON);
        mBillingFragment = new BillingListFragment();
        mFragmentList.add(mBillingFragment);
        if("0".equals(isCoupon)){
            if ("2".equals(vcSale)){}
            else {  // 开通储值卡，才展示 惠储值 （只有老板展示）
                String loginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
                if ("0".equals(loginType)){
                    mHuiChuZhiFragment = new HuiChuZhiListFragment();
                    mFragmentList.add(mHuiChuZhiFragment);
                    mTabParent.setVisibility(View.VISIBLE);
                }
            }
        }

        FragmentManager manager = getSupportFragmentManager();
        ViewPagerAdapter vpa = new ViewPagerAdapter(manager);
        mVp.setAdapter(vpa);
        mVp.setOffscreenPageLimit(mFragmentList.size());
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTvToday.setText(YrmUtils.getNowDay("yyyy-MM-dd")+"（今天）");


    }

    @OnClick({R.id.shoukuanma_parent_new_billing, R.id.huichuzhi_parent_new_billing})
    public void topclick(RelativeLayout godfather){
        switch (godfather.getId()){
            case R.id.shoukuanma_parent_new_billing:   // 收款码
                setTabs(0);
                break;

            case R.id.huichuzhi_parent_new_billing:    // 惠储值
                setTabs(1);
                break;
        }
    }

    private void setTabs(int position){
        mTvShouKuanMaStr.setTextSize(13);
        mLine1.setVisibility(View.GONE);
        mTvHuiChuZhiStr.setTextSize(13);
        mLine2.setVisibility(View.GONE);
        mChooseType = position;
        switch (position){
            case 0:
                mTvShouKuanMaStr.setTextSize(16);
                mLine1.setVisibility(View.VISIBLE);
                mVp.setCurrentItem(0);
                break;
            case 1:
                mTvHuiChuZhiStr.setTextSize(16);
                mLine2.setVisibility(View.VISIBLE);
                mVp.setCurrentItem(1);
                break;
        }

    }

    // 推送更新数据用
    // type=1----极光     type=2----华为
    public void handlePush(String type, QueryTransBean.DataBean bean){
        if (bean.isHuiChuZhi){   // 惠储值推送数据
            switch (type){
                case "1":
                    mHuiChuZhiFragment.handlePushData(bean);
                    break;
                case "2":
                    mHuiChuZhiFragment.huaWeiPushData(bean);
                    break;
            }

        }else{                 // 收款码数据列表推送数据
            switch (type){
                case "1":
                    mBillingFragment.handlePush(bean);
                    break;
                case "2":
                    mBillingFragment.handleHuaWeiPush(bean);
                    break;

            }

        }


    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BasicFragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }


}
