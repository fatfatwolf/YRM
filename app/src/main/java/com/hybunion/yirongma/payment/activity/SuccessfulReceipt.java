package com.hybunion.yirongma.payment.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.zxing.activity.CaptureActivity2;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;


/**
 * @author liuyujia
 * @date 2017/6/14
 * @email freemars@yeah.net
 * @description
 */

public class SuccessfulReceipt extends BasicActivity implements View.OnClickListener {
    private Constants.PAY_WAY type;
    @Bind(R.id.tv_amount)
    TextView TvAmounnt;
    @Bind(R.id.tv_merName)
    TextView TvmerchandName;
    @Bind(R.id.tv_current_status)
    TextView TvCurrentStatus;
    @Bind(R.id.tv_trade_time)
    TextView TvTradeTime;
    @Bind(R.id.tv_pay_way)
    TextView TvPayWay;
    @Bind(R.id.tv_order_num)
    TextView TvOrderNum;
    @Bind(R.id.tv_titlebar_back_title)
    TextView tv_title;
    @Bind(R.id.ll_titlebar_back)
    LinearLayout ll_titlebar_back;
    @Bind(R.id.bt_button)
    Button BtButton;
    @Bind(R.id.tv_result)
    TextView TvResult;
    @Bind(R.id.img_scan)
    ImageView imgScan;
    String Status,orderNumber,tvAmounnt,payName,orderStatus="",message,city,address;
    private double longitude,latitude;
    public static String ACTION_INTENT_RECEIVER = "com.hybunion.yirongma.common.util.jpush";
    private MessageReceiver mMessageReceiver;
    public static boolean jpushMsg;
    protected BasePresenter getPresenter() {
        return null;
    }
    @Override
    protected int getContentView() {
        return R.layout.successful_receipt;
    }
    @Override
    public void initView() {
        super.initView();
        tv_title.setText("收款结果");
        ll_titlebar_back.setOnClickListener(this);
        BtButton.setOnClickListener(this);
        registerMessageReceiver();
        jpushMsg = true;
    }
    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        tvAmounnt=intent.getStringExtra("payableAmount");
        city = intent.getStringExtra("city");
        address = intent.getStringExtra("address");
        longitude = intent.getExtras().getDouble("longitude");
        latitude = intent.getExtras().getDouble("latitude");
        TvAmounnt.setText(tvAmounnt);
        TvmerchandName.setText(intent.getStringExtra("merName"));
        if ("0".equals(intent.getStringExtra("tradeStatus"))) {
            TvCurrentStatus.setText("待付款");
        } else if ("1".equals(intent.getStringExtra("tradeStatus"))) {
            TvCurrentStatus.setText("已付款");
        } else {
            TvCurrentStatus.setText("订单已取消");
        }
        TvTradeTime.setText(intent.getStringExtra("orderDate"));
        payName=intent.getStringExtra("payName");
        TvPayWay.setText(payName);
        orderNumber=intent.getStringExtra("orderNo");
        TvOrderNum.setText(orderNumber);
        Status = intent.getStringExtra("status");
        if ("0".equals(Status)) {
            BtButton.setText("返回首页");
            TvResult.setText("收款成功");
            imgScan.setImageResource(R.drawable.union_success);
            TvResult.setTextColor(getResources().getColor(R.color.main_body_color));
        } else if ("1".equals(Status)) {
            message = intent.getStringExtra("message");
            if ("未开通被扫支付，不允许交易！".equals(message)){
                BtButton.setVisibility(View.GONE);
                TvResult.setText(message);
                imgScan.setImageResource(R.drawable.union_fail);
                TvResult.setTextColor(getResources().getColor(R.color.text_black));
            }else {
                BtButton.setText("重新扫码");
                TvResult.setText("收款失败，请重新扫码");
                imgScan.setImageResource(R.drawable.union_fail);
                TvResult.setTextColor(getResources().getColor(R.color.text_black));
            }
        } else {
            BtButton.setText("查询订单状态");
            TvResult.setText("收款处理中，请稍等....");
            imgScan.setImageResource(R.drawable.union_paying);
            TvResult.setTextColor(getResources().getColor(R.color.main_body_color));
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_titlebar_back:
                this.finish();
                break;
            case R.id.bt_button:
                if ("0".equals(Status)) {//成功
                    finish();
                } else if ("1".equals(Status)) {//失败
                    Intent intent = new Intent(SuccessfulReceipt.this, CaptureActivity2.class);
                    intent.putExtra("flag",1);
                    intent.putExtra("amt",tvAmounnt);
                    intent.putExtra("city", city);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("address", address);
                    intent.putExtra("finalFlag",10);
                    startActivity(intent);
                    this.finish();
                    LogUtil.e(orderStatus+"-----orderStatus");
                }else if ("".equals(orderStatus) || "0".equals(orderStatus)){
                    showLoading();
                    startScanPaying(orderNumber);
            }else {
                finish();
            }
                break;
        }
    }

    private void startScanPaying(final String orderNumber) {

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("orderNo",orderNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().postNoHeader(this, NetUrl.QUERY_INFOR_URL, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject response = new JSONObject(o);
                    String status=response.getString("status");
                    String msg = response.getString("msg");
                    orderStatus=response.optString("orderStatus");
                    if ("0".equals(status)||"2".equals(status)){
                        if ("0".equals(orderStatus)){
                            ToastUtil.showShortToast("用户正在付款中，请稍候");
                            hideLoading();
                            TvCurrentStatus.setText("待付款");
                        }else if("1".equals(orderStatus)){
                            hideLoading();
                            TvResult.setText("收款成功");
                            TvCurrentStatus.setText("已付款");
                            BtButton.setText("返回首页");
                            imgScan.setImageResource(R.drawable.union_success);
                        }else if ("2".equals(orderStatus)){
                            hideLoading();
                            TvCurrentStatus.setText("订单被取消");
                            BtButton.setText("返回首页");
                            imgScan.setImageResource(R.drawable.union_fail);
                        }
                    }else {
                        ToastUtil.showShortToast(msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
                return String.class;
            }
        });


    }

    private void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER);
        registerReceiver(mMessageReceiver,filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("lyf---成功页面接收到了推送");
            startScanPaying(orderNumber);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }
}
