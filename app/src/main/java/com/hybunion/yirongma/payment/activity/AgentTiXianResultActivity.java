package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.TitleBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * HRT钱包提现结果界面
 */

public class AgentTiXianResultActivity extends BasicActivity {
    @Bind(R.id.titleBar_tixian_result_activity)
    TitleBar mTitleBar;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_agent_tixian_result_layout;
    }

    @Override
    public void initView() {
        super.initView();
        mTitleBar.setTitleBarBackVisible(false);  // 设置 TitleBar 不显示左边的返回箭头

    }

    @OnClick(R.id.finish_tixian_result_activity)
    public void wancheng(){
        HRTApplication.finishActivity(TiXianNewActivity.class);
        HRTApplication.finishActivity(MyNewWalletActivity.class);
        finish();

    }

}
