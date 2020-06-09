package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;

import java.text.DecimalFormat;

import butterknife.Bind;

/**
 *  和融通钱包 提现记录详情页。
 */

public class TiXianRecordDetailsActivity extends BasicActivity {
    @Bind(R.id.tv_tixianAmt_record_details_activity)
    TextView mTvTiXianAmt;
    @Bind(R.id.tvState_tixian_record_details_activity)
    TextView mTvTiXianState;
    @Bind(R.id.tvFuWuFei_tixian_record_details_activity)
    TextView mTvFuWuFei;
    @Bind(R.id.tv_time_tixian_record_details_activity)
    TextView mTvTiXianTime;


    // tixianState-提现状态 用于修改提示语
    // tixianState-1 审核中  提示语：提现审核中，请注意查看提现状态
    // tixianState-5 处理中  提示语：提现处理中，请注意查看提现状态
    // tixianState-2、4 提现成功  提示语：提现申请成功，具体到账时间以开户行为准
    public static void start(Context from, String tixianState, String tiXianAmt, double fuWuFei, String tixianTime){
        Intent intent = new Intent(from, TiXianRecordDetailsActivity.class);
        intent.putExtra("tixianState",tixianState);
        intent.putExtra("tixianAmt",tiXianAmt);
        intent.putExtra("fuWuFei",fuWuFei);
        intent.putExtra("tiXianTime",tixianTime);
        from.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tixian_record_details_layout;
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        String tixianState = intent.getStringExtra("tixianState");
        String tixianAmt = intent.getStringExtra("tixianAmt");
        double fuWuFei = intent.getDoubleExtra("fuWuFei",0);
        String tiXianTime = intent.getStringExtra("tiXianTime");

        mTvTiXianAmt.setText("¥ "+tixianAmt);
        String state="";
        switch (tixianState){
            case "1":
                state = "提现审核中，请注意查看提现状态";
                break;
            case "2":
            case "4":

                break;
            default:
                state = "提现处理中，请注意查看提现状态";
        }
        mTvTiXianState.setText(state);
        DecimalFormat format = new DecimalFormat("0.00");
        mTvFuWuFei.setText(format.format(fuWuFei)+" 元");
        mTvTiXianTime.setText(tiXianTime);
    }
}
