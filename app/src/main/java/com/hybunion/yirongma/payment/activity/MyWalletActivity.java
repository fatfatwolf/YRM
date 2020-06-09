package com.hybunion.yirongma.payment.activity;


import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MyWalletBalanceBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.MyBottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 代理“商”计划
 */

public class MyWalletActivity extends BasicActivity {
    @Bind(R.id.tixian_button_my_wallet_fragment)
    TextView mTvTiXian;
    @Bind(R.id.notice_button_my_wallet_fragment)
    TextView mTvNotice;
    @Bind(R.id.balance_my_wallet_fragment)
    TextView mTvBalance;

    private Dialog mBalanceDialog;
    private String mLoginType;
    private String mMerId;
    private double mBalance; // 钱包余额
    private String mMinCash, mTotalCash;
    private double mServiceCharge; // 手续费，当钱包余额低于手续费时，不能提现。
    private boolean mCanTiXian; // 是否能提现  true-能  false-不能

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void initView() {
        super.initView();

        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if ("0".equals(mLoginType)) {  // 老板
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        } else {
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getBalance();

    }

    // 获取钱包余额
    private void getBalance(){
        JSONObject jb = new JSONObject();
        try {
            jb.put("merId", mMerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.WALLET_BALANCE, jb, new MyOkCallback<MyWalletBalanceBean>() {
            @Override
            public void onStart() {
                showLoading();
            }
            @Override
            public void onSuccess(MyWalletBalanceBean myWalletBalanceBean) {
                if ("0".equals(myWalletBalanceBean.getStatus())){
                    mBalance = myWalletBalanceBean.balance;
                    mMinCash = myWalletBalanceBean.minCash;
                    mTotalCash = myWalletBalanceBean.totalCash;
                    DecimalFormat df = new DecimalFormat("0.00");
                    mTvBalance.setText(df.format(mBalance));
//                    mTvJiangJin.setText("累计奖金：" + mTotalCash);
                    if (!TextUtils.isEmpty(mMinCash) && (mMinCash.matches("[0-9]+[.][0-9]+") || mMinCash.matches("[0-9]+"))) {
                        double minCashD = Double.parseDouble(mMinCash);
                        mServiceCharge = minCashD;
                        mTvNotice.setText("账户余额低于 " + mServiceCharge + " 元，暂不支持提现");
                        if (mBalance < mServiceCharge) {   // 当钱包余额低于手续费时，不能提现。
                            mCanTiXian = false;
                            mTvTiXian.setBackgroundResource(R.drawable.shape_tixian_button_gray);
                            mTvNotice.setVisibility(View.VISIBLE);
                        } else {
                            mCanTiXian = true;
                            mTvTiXian.setBackgroundResource(R.drawable.shape_tixian_button_red);
                            mTvNotice.setVisibility(View.GONE);
                        }
                    } else {
                        mCanTiXian = false;
                        mTvTiXian.setBackgroundResource(R.drawable.shape_tixian_button_gray);
                    }
                }else{
                    String msg = myWalletBalanceBean.getMessage();
                    if (!TextUtils.isEmpty(msg)){
                        ToastUtil.showShortToast(msg);
                    }else{
                        ToastUtil.showShortToast("网络连接不佳");
                    }
                    mTvTiXian.setBackgroundResource(R.drawable.shape_tixian_button_gray);
                }

            }
            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
                mTvTiXian.setBackgroundResource(R.drawable.shape_tixian_button_gray);
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

    @Override
    protected int getContentView() {
        return R.layout.fragment_my_wallet_layout;
    }

    // 提现按钮
    @OnClick(R.id.tixian_button_my_wallet_fragment)
    public void tiXian() {
        if (mCanTiXian)
            TiXianActivity.start(this, mBalance);
    }

    @OnClick(R.id.titlebar_back_my_wallet_layout)
    public void titleBack() {
        this.finish();
    }
    // 点击问号
    @OnClick(R.id.img_wenhao_my_wallet_layout)
    public void wenhao() {
        showBalanceDialog();  // Dialog 内容是定死的。
    }

    // 菜单
    @OnClick(R.id.img_menu_my_wallet_layout)
    public void menu() {
        showDilaog();
    }

    private MyBottomDialog mDialog;
    public void showDilaog() {
        final List<String> strList = new ArrayList<>();
        strList.add("奖励详情");
        strList.add("提现记录");
        if (mDialog == null) {
            mDialog = new MyBottomDialog(this);
        }
        mDialog.showThisDialog("代理“商”计划", strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                switch (position) {
                    case 0:        //  奖励详情 列表
                        startActivity(new Intent(MyWalletActivity.this,RewardListActivity.class));
                        break;
                    case 1:        // 提现记录 列表
                        TiXianRecordActivity.start(MyWalletActivity.this);
                        break;
                }
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });
    }
    private void showBalanceDialog() {
        mBalanceDialog = new Dialog(this);
        mBalanceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBalanceDialog.setContentView(R.layout.dialog_my_wallet_fragment);
        TextView tvAccount = (TextView) mBalanceDialog.findViewById(R.id.tv_account_my_wallet_dialog);
        TextView tvContent = (TextView) mBalanceDialog.findViewById(R.id.tv_content_my_wallet_dialog);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        TextView okButton = (TextView) mBalanceDialog.findViewById(R.id.ok_button_my_wallet_dialog);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBalanceDialog != null)
                    mBalanceDialog.dismiss();
            }
        });
        tvAccount.setText("代理“商”计划收益");
        tvContent.setText("1.支付完成页的广告，按市场价每个有效广告3分，您将获得其中30%收益\n" +
                "2.支付默认关注，按市场价每个有效粉丝1元，您将获得其中20%收益\n" +
                "3.支付分润，朋友圈广告等，您将获得其中10%收益");
        mBalanceDialog.show();

    }
}
