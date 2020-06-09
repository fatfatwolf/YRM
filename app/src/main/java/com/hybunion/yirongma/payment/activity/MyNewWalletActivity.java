package com.hybunion.yirongma.payment.activity;


import android.app.Dialog;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MyWalletBalanceBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 钱包界面
 */

public class MyNewWalletActivity extends BasicActivity {
    @Bind(R.id.tixian_button_my_wallet_fragment_new)
    TextView mTvTiXian;
    @Bind(R.id.notice_button_my_wallet_fragment_new)
    TextView mTvNotice;
    //    @Bind(R.id.titleBar_my_wallet_fragment_new)
//    TitleBar mTitleBar;
    @Bind(R.id.balance_my_wallet_fragment_new)
    TextView mTvBalance;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;

    private double mBalance; // 钱包余额
    private double mServiceCharge = 10;
    ; // 手续费，当钱包余额低于手续费时，不能提现。
    private boolean mCanTiXian; // 是否能提现  true-能  false-不能

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void initView() {
        super.initView();
        // mServiceCharge 默认为 10 元
        mTvNotice.setText("可提现金额低于 " + mServiceCharge + " 元，暂不支持提现");
        mTvNotice.setVisibility(View.VISIBLE);
        smartRefresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                queryBalance(false);
            }
        });


    }

    private boolean mIsFirstIn = true;

    @Override
    protected void onResume() {
        super.onResume();
            queryBalance(mIsFirstIn);
            mIsFirstIn = false;

    }

    @Override
    protected int getContentView() {
        return R.layout.new_fragment_my_wallet_layout;
    }

    private void queryBalance(final boolean isShowLoading){
        JSONObject jb = new JSONObject();
        try {
            jb.put("mid",SharedPreferencesUtil.getInstance(this).getKey("mid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(this, NetUrl.WALLET_NEW_BALANCE, jb, new MyOkCallback<MyWalletBalanceBean>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishRefresh();
                if (isShowLoading)
                    showLoading();
            }

            @Override
            public void onSuccess(MyWalletBalanceBean result) {

                if (result != null) {
                    String msg = result.getMessage();
                    String status = result.getStatus();
                    if ("0".equals(status)) {
                        mBalance = result.balance;  // 余额
                        mMoney = result.money;  // 可提现金额
                        String cashStatus = result.cashstatus;  // 实时到账 审核状态

                        // 保存 实时到账 开通状态。
                        SharedPreferencesUtil.getInstance(MyNewWalletActivity.this).putKey(SharedPConstant.IS_SHISHI, cashStatus);
                        DecimalFormat df = new DecimalFormat("0.00");
                        mTvBalance.setText(df.format(mBalance));
                        if (mBalance < mServiceCharge) {   // 当余额低于10时，不能提现。
                            mCanTiXian = false;
                            mTvTiXian.setBackgroundResource(R.drawable.shape_tixian_button_gray);
                            mTvNotice.setText("钱包余额低于 " + mServiceCharge + " 元，暂不支持提现");

                        } else {
                            mCanTiXian = true;
                            mTvTiXian.setBackgroundResource(R.drawable.shape_tixian_button_red);
                            mTvNotice.setText("提现金额上限50w");
                        }

                    } else {
                        if (!TextUtils.isEmpty(msg))
                            ToastUtil.show(msg);
                    }
                } else {
                    ToastUtil.show("网络连接不佳");
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return MyWalletBalanceBean.class;
            }
        });
    }


    @OnClick({R.id.iv_question, R.id.title_image})
    public void titleRight(ImageView layout) {
        switch (layout.getId()) {
            case R.id.title_image:    // 提现记录
                TiXianNewRecordActivity.start(MyNewWalletActivity.this);
                break;
            case R.id.iv_question:   // 问号
                showMyDialog();
                break;
        }
    }

    @OnClick(R.id.ll_titlebar_back)
    public void back() {
        finish();
    }


    @OnClick(R.id.tixian_button_my_wallet_fragment_new)
    public void tiXian() {   // 提现按钮
        if (mCanTiXian)
            TiXianNewActivity.start(this, mMoney, mBalance);
    }

    private double mMoney; // 可提现金额

    private Dialog mMyDialog;

    private void showMyDialog() {
        if (mMyDialog == null) {
            mMyDialog = new Dialog(this);
            mMyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mMyDialog.setContentView(R.layout.dialog_my_wallet_fragment);
            TextView tvAccount = (TextView) mMyDialog.findViewById(R.id.tv_account_my_wallet_dialog);
            TextView tvContent = (TextView) mMyDialog.findViewById(R.id.tv_content_my_wallet_dialog);
            TextView tvOk = mMyDialog.findViewById(R.id.ok_button_my_wallet_dialog);
            tvAccount.setText("钱包余额");
            tvContent.setText("钱包余额为交易金额扣除交易手续费后所得金额。");
            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMyDialog != null)
                        mMyDialog.dismiss();
                }
            });

        }
        mMyDialog.show();
    }


}
