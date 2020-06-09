package com.hybunion.yirongma.valuecard.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;

import butterknife.Bind;

/**
 * Created by admin on 2018/1/15.
 */

public class ReFundSuccessActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.tv_titlebar_back_title)
    TextView title;
    @Bind(R.id.ll_titlebar_back)
    LinearLayout ll_titlebar_back;
    @Bind(R.id.tv_result)
    TextView tv_result;
    @Bind(R.id.bt_button)
    Button bt_button;
    String msg;
    String orderStatus;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.refund_success_activity;
    }

    @Override
    public void initView() {
        super.initView();
        msg = getIntent().getStringExtra("msg");
        orderStatus = getIntent().getStringExtra("orderStatus");
        if("5".equals(orderStatus)){
            title.setText("退款失败");
        }else if("3".equals(orderStatus)){
            title.setText("已退款");
        }else if("4".equals(orderStatus)){
            title.setText("退款中");
        }else {
            title.setText("退款成功");
        }

        tv_result.setText(msg);
        bt_button.setOnClickListener(this);
        ll_titlebar_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_button:
                finish();
                break;
            case R.id.ll_titlebar_back:
                finish();
                break;
        }
    }
}
