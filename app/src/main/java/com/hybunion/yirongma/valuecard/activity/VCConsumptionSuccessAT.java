package com.hybunion.yirongma.valuecard.activity;

import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/1/11.
 */

public class VCConsumptionSuccessAT extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.tv_titlebar_back_title)
    TextView title;
    @Bind(R.id.tv_orderNo)
    TextView tv_orderNo;
    @Bind(R.id.tv_order_date)
    TextView tv_order_date;
    @Bind(R.id.tv_payable_amount)
    TextView tv_payable_amount;
    @Bind(R.id.tv_payable_discountRate)
    TextView tv_payable_discountRate;
    @Bind(R.id.ll_discountRate)
    LinearLayout ll_discountRate;
    @Bind(R.id.tv_paid_amount)
    TextView tv_paid_amount;
    @Bind(R.id.btn_back)
    Button btn_back;
    @Bind(R.id.ll_amount)
    LinearLayout ll_amount;
    @Bind(R.id.tv_order_no)
    TextView tv_order_no;
    @Bind(R.id.tv_mer_name)
    TextView tv_mer_name;
    public String cardNo, cardName, orderNo, cardType, exchangeTim, putMoney, phone, amountPayment,discountRate;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.value_card_consumption_success;
    }

    @Override
    public void initView() {
        phone = getIntent().getStringExtra("phone");
        orderNo = getIntent().getStringExtra("orderNo");
        exchangeTim = getIntent().getStringExtra("exchangeTim");
        cardType = getIntent().getStringExtra("cardType");
        cardNo = getIntent().getStringExtra("cardNo");
        putMoney = getIntent().getStringExtra("putMoney");
        discountRate = getIntent().getStringExtra("discountRate");
        amountPayment = getIntent().getStringExtra("amountPayment"); //cardType = 3时 显示
        tv_mer_name.setText(SharedPreferencesUtil.getInstance(VCConsumptionSuccessAT.this).getKey("merchantName"));
        title.setText(phone);
        Spanned spanned = Html.fromHtml(orderNo.substring(0, orderNo.length() - 4) + "<font color=\"#FF5614\"  >" + "<big>" + orderNo.substring(orderNo.length() - 4, orderNo.length()) + "</big>" + "</font>");
        tv_orderNo.setText(spanned);
        tv_order_date.setText(exchangeTim);
        tv_order_no.setText(cardNo);
        if ("3".equals(cardType)){
            ll_discountRate.setVisibility(View.VISIBLE);
            ll_amount.setVisibility(View.VISIBLE);
            tv_payable_discountRate.setText(discountRate+"折");
            tv_payable_amount.setText(putMoney+"元");
            tv_paid_amount.setText(amountPayment+"元");
        }else {
            if ("0".equals(cardType)){
                tv_payable_amount.setText(putMoney+"次");
            }else {
                tv_payable_amount.setText(putMoney+"元");
            }
            ll_discountRate.setVisibility(View.GONE);
            tv_paid_amount.setVisibility(View.GONE);
        }
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @OnClick(R.id.ll_titlebar_back)
    public void goBack() {
        finish();
    }
}
