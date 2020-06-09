package com.hybunion.yirongma.payment.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.net.utils.LogUtil;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.Fragment.SettlementFragment;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.hybunion.yirongma.payment.view.DataPopupWindow;
import com.hybunion.yirongma.payment.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

// 结算对账界面
public class SettlementActivity extends BasicActivity {
    @Bind(R.id.titleBar_settlement_activity)
    TitleBar mTitleBar;
    @Bind(R.id.tab_parent_settlement_activity)
    RelativeLayout mTabParent;
    @Bind(R.id.tv_pay_code_str_settlement_activity)
    TextView mTvPayCodeStr;
    @Bind(R.id.line1_settlement_activity)
    View mLine1;
    @Bind(R.id.tv_jingFu_card_str_settlement_activity)
    TextView mTvJingFuStr;
    @Bind(R.id.line2_settlement_activity)
    View mLine2;
    @Bind(R.id.fragment_parent_settlement_activity)
    FrameLayout mFragmentParent;

    private SettlementFragment mPayCodeFragment, mJingFuCardFragment;
    private FragmentManager mFragmentManager;
    private String mStartTime, mEndTime; // 筛选用的开始，结束时间
    private String mMid, mMerId;
    private DataPopupWindow mDataPopupWindow;
    private int mChooseType;  //0-当前展示的是收款码   1-当前展示的是和卡  默认为0


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settlement_layout;
    }

    @Override
    public void initView() {
        super.initView();
//        fragment_parent_settlement_activity
        mMid = SharedPreferencesUtil.getInstance(this).getKey("mid");
        mMerId = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.MERCHANT_ID);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_popupwindow_data_activity, null);
        mDataPopupWindow = new DataPopupWindow(this, view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDataPopupWindow.isPickTime(false);

        mStartTime = YrmUtils.getNowDay("yyyy-MM-dd HH:mm:ss");  // 不筛选时分秒，但是要传到控件中作为初始值，控件中会再截取
        mEndTime = YrmUtils.getNowDay("yyyy-MM-dd HH:mm:ss");

        mPayCodeFragment = new SettlementFragment();
        String url = NetUrl.SETTLEMENT_URL + mMid;
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        mPayCodeFragment.setArguments(bundle);
        queryMerInfo();

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.fragment_parent_settlement_activity, mJingFuCardFragment, "1");
        fragmentTransaction.add(R.id.fragment_parent_settlement_activity, mPayCodeFragment, "1");
        fragmentTransaction.commit();

        mTitleBar.setRightTextClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void rightClick() {
                showSelectedDialog();
            }
        });
    }

    // 商户入驻资料查询（是否开通和卡）
    private void queryMerInfo(){
        JSONObject jb = new JSONObject();
        try {
            jb.put("merId", mMerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().postEncryp(this, NetUrl.QUERY_MER_INFO, jb, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String str) {
                try {
                    JSONObject response = new JSONObject(str);
                    String status = response.getString("status");
                    String msg = response.getString("message");
                    if ("S".equals(status)){
                        JSONObject jbData = response.getJSONObject("data");
                        String generalCardStatus = jbData.getString("generalCardStatus");  // 通用卡商户状态
                        // 2-审核通过  3-已冻结   4-关闭
                        if ("2".equals(generalCardStatus) || "3".equals(generalCardStatus) || "4".equals(generalCardStatus)){
                            addJingFuCardFragment();
                        }else{ // else 中包括：0-待审核  1-已驳回 5-未开通
                            mTabParent.setVisibility(View.GONE);
                        }
                    }else{
                        if (!TextUtils.isEmpty(msg)){
                            ToastUtil.showShortToast(msg);
                        }else{
                            ToastUtil.showShortToast("查询商户信息失败");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("查询失败，网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });


    }

    private void addJingFuCardFragment(){
        mJingFuCardFragment = new SettlementFragment();
        String url1 = NetUrl.SETTLEMENTJ_URL + mMid;
        Bundle bundle1 = new Bundle();
        bundle1.putString("url", url1);
        mJingFuCardFragment.setArguments(bundle1);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_parent_settlement_activity, mJingFuCardFragment, "1");
        fragmentTransaction.hide(mJingFuCardFragment);
        fragmentTransaction.commit();
    }



    @OnClick({R.id.pay_code_parent_settlement_activity, R.id.jingFu_card_parent_settlement_activity})
    public void tabClick(View view) {
        switch (view.getId()) {
            case R.id.pay_code_parent_settlement_activity:    // 收款码
                setTabs(0);
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.show(mPayCodeFragment);
                fragmentTransaction.hide(mJingFuCardFragment);
                fragmentTransaction.commit();
                break;

            case R.id.jingFu_card_parent_settlement_activity:   // 和卡
                setTabs(1);
                FragmentTransaction fragmentTransaction1 = mFragmentManager.beginTransaction();
                fragmentTransaction1.show(mJingFuCardFragment);
                fragmentTransaction1.hide(mPayCodeFragment);
                fragmentTransaction1.commit();
                break;
        }

    }

    private void setTabs(int position) {
        mTvPayCodeStr.setTextSize(13);
        mLine1.setVisibility(View.GONE);
        mTvJingFuStr.setTextSize(13);
        mLine2.setVisibility(View.GONE);
        mChooseType = position;
        switch (position) {
            case 0:
                mTvPayCodeStr.setTextSize(16);
                mLine1.setVisibility(View.VISIBLE);
                break;
            case 1:
                mTvJingFuStr.setTextSize(16);
                mLine2.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showSelectedDialog() {
        if (mDataPopupWindow != null) {
            mDataPopupWindow.setSettlementVisible(mChooseType == 0?View.GONE:View.VISIBLE);
            mDataPopupWindow.showThisPopWindow(mTitleBar, mStartTime, mEndTime, new DataPopupWindow.OnDataPopWindowListener() {
                @Override
                public void onPickFinish(String startPickedTime, String endPickedTime, String nameStr) {
                    if (startPickedTime != null) {
                        mStartTime = startPickedTime.substring(0, 10);
                    }
                    if (endPickedTime != null) {
                        mEndTime = endPickedTime.substring(0, 10);

                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date startDate = sdf.parse(mStartTime);
                        Date endDate = sdf.parse(mEndTime);
                        long startMillis = startDate.getTime();
                        long endMillis = endDate.getTime();
                        // endMillis 应该 >= startMillis
                        if (endMillis < startMillis) {
                            ToastUtil.showShortToast("开始时间不能晚于结束时间");
                            return;
                        }
                        long day31 = 31L * 24 * 60 * 60 * 1000;
                        if ((endMillis - startMillis) > day31) {
                            ToastUtil.showShortToast("查询区间不能大于31天");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (mChooseType == 1) {  // 筛选的是和卡
                        String nameS = "";
                        if (!TextUtils.isEmpty(nameStr)){
                            nameS = "&status="+nameStr;
                        }else{
                            nameS = "&status=";
                        }
                        String url = NetUrl.SETTLEMENTJ_URL + mMid + "&beginDate="+mStartTime+"&endDate="+mEndTime+nameS;
                        mJingFuCardFragment.loadNewUrl(url);
                    }else{
                        String url = NetUrl.SETTLEMENT_URL + mMid + "&startTime="+mStartTime+"&endTime="+mEndTime;
                        mPayCodeFragment.loadNewUrl(url);
                    }
                    mDataPopupWindow.dismiss();

                }

                @Override
                public void onPickReset() {
                    mDataPopupWindow.dismiss();
                    mStartTime = YrmUtils.getNowDay("yyyy-MM-dd");
                    mEndTime = YrmUtils.getNowDay("yyyy-MM-dd");

                    if (mChooseType == 1) {  // 筛选的是和卡
                        String url = NetUrl.SETTLEMENTJ_URL + mMid + "&&beginDate="+mStartTime+"&endDate="+mEndTime;
                        mJingFuCardFragment.loadNewUrl(url);
                    }else{
                        String url = NetUrl.SETTLEMENT_URL + mMid + "&&beginDate="+mStartTime+"&endDate="+mEndTime;
                        mPayCodeFragment.loadNewUrl(url);
                    }

                }
            });
        }

    }


}
