package com.hybunion.yirongma.payment.activity;

import android.content.Intent;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;

import butterknife.OnClick;

/**
 * 商户管理密码缺省页
 */

public class NoMerchantManageActivity extends BasicActivity {
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_no_merchant_manage_layout;
    }

    // 设置密码
    @OnClick(R.id.setup_pwd_no_merchant_manage_activity)
    public void setUpPwd(){
        startActivity(new Intent(this,VerificationCodeActivity.class));
        finish();
    }

}
