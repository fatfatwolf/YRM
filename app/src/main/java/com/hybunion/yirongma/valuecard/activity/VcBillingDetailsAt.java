package com.hybunion.yirongma.valuecard.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.VcBillingDetailsBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.LogUtil;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2017/9/20.
 */

public class VcBillingDetailsAt extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.tv_count)
    TextView tv_count;
    @Bind(R.id.tv_transaction_number)
    TextView tv_transaction_number;
    @Bind(R.id.tv_stored_vc_number)
    TextView tv_stored_vc_number;
    @Bind(R.id.tv_transaction_type)
    TextView tv_transaction_type;
    @Bind(R.id.tv_transaction_time)
    TextView tv_transaction_time;
    @Bind(R.id.tv_head)
    TextView tv_head;
    @Bind(R.id.rl_refund)
    Button rl_refund;
    String transType, status, description, orderNo,dateSimple;
    public static VcBillingDetailsAt instance = null;
    @Override
    protected int getContentView() {
        return R.layout.activity_vc_billing_details;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public void initView() {
        instance = this;
        tv_head.setText("账单详情");
        rl_refund.setOnClickListener(this);
        Intent intent = getIntent();
        String amount = intent.getStringExtra("transAmount");
        transType = intent.getStringExtra("transType");
        LogUtil.d(amount + "消费金额");
        tv_count.setText("¥" + amount);
        orderNo = intent.getStringExtra("orderNo");
        tv_transaction_number.setText(orderNo);
        tv_stored_vc_number.setText(intent.getStringExtra("cardNo"));
        if ("0".equals(transType)) {//数字 0 消费成功 1充值成功 4 购卡成功 2 消费撤销 5 购卡撤销 8 充值撤销
            tv_transaction_type.setText("消费成功");
            rl_refund.setVisibility(View.VISIBLE);
        }
        if ("1".equals(transType)) {
            tv_transaction_type.setText("充值成功");
            rl_refund.setVisibility(View.VISIBLE);
        }
        if ("2".equals(transType)) {
            tv_transaction_type.setText("消费撤销");
        }
        if ("4".equals(transType)) {
            tv_transaction_type.setText("购卡成功");
            rl_refund.setVisibility(View.VISIBLE);
        }
        if ("5".equals(transType)) {
            tv_transaction_type.setText("购卡撤销");
        }
        if ("8".equals(transType)) {
            tv_transaction_type.setText("充值撤销");
        }
        dateSimple = intent.getStringExtra("dateSimple");
        tv_transaction_time.setText(dateSimple + " " + intent.getStringExtra("simpleDate"));
    }
    @Override
    public void initData() {

    }
    @Override
    protected void loadData() {
    }
    @Override
    public void showInfo(Map map) {
        super.showInfo(map);

    }

    @OnClick(R.id.ll_back)
    public void llback() {
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            status = data.getStringExtra("status");
            description = data.getStringExtra("reason");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_refund:
                SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd");
                String end = df.format(new Date());
                try {
                    long timeStart = df.parse(dateSimple).getTime();//交易日期
                    long timeEnd = df.parse(end).getTime();//当前时间
                    // 两个日期想减得到天数
                    long dayCount = (timeEnd - timeStart) / (24 * 3600 * 1000);
                    if (dayCount==0){
                        if ("0".equals(transType)) {
                            getRefund();
                        }
                    }else {
                        ToastUtil.showShortToast("非当天交易");
                    }
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                break;
        }
    }

    private void getRefund(){
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("merid", SharedPreferencesUtil.getInstance(this).getKey(Constants.MERCHANTID));
            jsonRequest.put("oriOrderId",orderNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().postNoHeader(this, NetUrl.GETREFUND, jsonRequest, new MyOkCallback<VcBillingDetailsBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(VcBillingDetailsBean vcBillingDetailsBean) {
                String msg = vcBillingDetailsBean.getMsg();
                String status = vcBillingDetailsBean.getStatus();
                if ("0".equals(status)) {
                    Intent intent = new Intent(VcBillingDetailsAt.this, ReFundSuccessActivity.class);
                    intent.putExtra("msg", msg);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.showShortToast(msg);
                }

            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return VcBillingDetailsBean.class;
            }
        });

    }



}
