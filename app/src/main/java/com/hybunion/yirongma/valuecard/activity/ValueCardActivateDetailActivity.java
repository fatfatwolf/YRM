package com.hybunion.yirongma.valuecard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;

/**
 *已激活储值卡详情界面
 * Created by lyf on 2016/9/21.
 */

public class ValueCardActivateDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_cardNo, tv_cardNumber, tv_cardType, tv_cardMoney, tv_cardBatch, tv_cardTime, tv_cardState, tv_title;
    private LinearLayout btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuecard_activate_detail);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        tv_cardNo = (TextView) findViewById(R.id.tv_cardNo);
        tv_cardNumber = (TextView) findViewById(R.id.tv_cardNumber);
        tv_cardType = (TextView) findViewById(R.id.tv_cardType);
        tv_cardMoney = (TextView) findViewById(R.id.tv_cardMoney);
        tv_cardBatch = (TextView) findViewById(R.id.tv_cardBatch);
        tv_cardTime = (TextView) findViewById(R.id.tv_cardTime);
        tv_cardState = (TextView) findViewById(R.id.tv_cardState);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("储值卡激活");
        Intent intent = getIntent();
//          卡序号
        tv_cardNo.setText(intent.getStringExtra("cardSeq"));
//        卡号
        tv_cardNumber.setText(intent.getStringExtra("cardNo"));
//        卡类型
        tv_cardType.setText(intent.getStringExtra("cardType"));
//        卡余额
        tv_cardMoney.setText(intent.getStringExtra("balance"));
//        激活批次号
        tv_cardBatch.setText(intent.getStringExtra("activateBatchId"));
//        激活时间
        tv_cardTime.setText(intent.getStringExtra("activateDate"));
//        卡状态
        tv_cardState.setText(intent.getStringExtra("cardStatus"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
            break;
        }
    }
}
