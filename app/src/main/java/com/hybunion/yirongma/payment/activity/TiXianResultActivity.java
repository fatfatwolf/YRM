package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * HRT钱包提现结果界面
 */

public class TiXianResultActivity extends BasicActivity {
    @Bind(R.id.tv_tixianAmt_result_activity)
    TextView mTvTiXianAmt;
    @Bind(R.id.tvFuWuFei_tixian_result_activity)
    TextView mTvFuWuFei;
    @Bind(R.id.tv_tixianType_tixian_result_activity)
    TextView mTvTiXianType;
    @Bind(R.id.tv_bankName_tixian_result_activity)
    TextView mTvTiXianBankName;
    @Bind(R.id.tv_time_tixian_result_activity)
    TextView mTvTiXianTime;
    @Bind(R.id.ll_titlebar_back)
    LinearLayout mBackParent;


    private String mTiXianAmt, mFuWuFei, mTiXianType, mTiXianBank, mTiXianTime;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tixian_result_layout;
    }

    public static void start(Context from, String tiXianAmt, String fuWuFei, String tiXianType, String tiXianBank, String tiXianTime) {
        Intent intent = new Intent(from, TiXianResultActivity.class);
        intent.putExtra("tiXianAmt", tiXianAmt);
        intent.putExtra("fuWuFei", fuWuFei);
        intent.putExtra("tiXianType", tiXianType);
        intent.putExtra("tiXianBank", tiXianBank);
        intent.putExtra("tiXianTime", tiXianTime);
        from.startActivity(intent);
    }


    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        mTiXianAmt = intent.getStringExtra("tiXianAmt");
        mFuWuFei = intent.getStringExtra("fuWuFei");
        mTiXianType = intent.getStringExtra("tiXianType");
        mTiXianBank = intent.getStringExtra("tiXianBank");
        mTiXianTime = intent.getStringExtra("tiXianTime");

        mTvTiXianAmt.setText("¥ " + mTiXianAmt);
        mTvFuWuFei.setText(mFuWuFei + " 元");
        mTvTiXianType.setText(mTiXianType);
        mTvTiXianBankName.setText(mTiXianBank);
        mTvTiXianTime.setText(mTiXianTime);

    }

    // 返回按钮
    @OnClick(R.id.ll_titlebar_back)
    public void back() {
        handleBack();
        finish();
    }

    @OnClick(R.id.finish_tixian_result_activity)
    public void wancheng() {
        handleBack();
        finish();

    }


    @Override
    public void onBackPressed() {
        handleBack();
        super.onBackPressed();


    }

    private void handleBack(){
        HRTApplication.finishActivity(TiXianNewActivity.class);
        HRTApplication.finishActivity(MyNewWalletActivity.class);
        Intent intent = new Intent("tixian");
        this.sendBroadcast(intent);
    }

}
