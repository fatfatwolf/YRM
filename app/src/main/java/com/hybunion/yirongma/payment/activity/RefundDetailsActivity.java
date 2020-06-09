package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BaseActivity;
import com.hybunion.yirongma.payment.model.BillDetailsBean;
import com.hybunion.yirongma.payment.view.MyBottomDialog;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class RefundDetailsActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.btn_back)
    LinearLayout btn_back;
    @Bind(R.id.tv_transAmount)
    TextView tv_transAmount;
    @Bind(R.id.tv_refund_status)
    TextView tv_refund_status;
    @Bind(R.id.tv_refund_time)
    TextView tv_refund_time;
    @Bind(R.id.tv_refund_no)
    TextView tv_refund_no;
    @Bind(R.id.tv_trader_time)
    TextView tv_trader_time;
    @Bind(R.id.tv_trader_no)
    TextView tv_trader_no;
    @Bind(R.id.tv_collect_code)
    TextView tv_collect_code;
    @Bind(R.id.tv_mode_pay)
    TextView tv_mode_pay;
    @Bind(R.id.ll_tid_code)
    LinearLayout ll_tid_code;
    @Bind(R.id.tv_refund)
    TextView mReturnMoney; // 退款按钮
    @Bind(R.id.tv_order_amount)
    TextView tv_order_amount;
    @Bind(R.id.refund_time_parent)
    LinearLayout mRefundTimeParent;
    @Bind(R.id.refund_no_parent)
    LinearLayout mRefundNoParent;
    @Bind(R.id.ll_remark)
    LinearLayout ll_remark;
    @Bind(R.id.tv_remark)
    TextView tv_remark;
    @Bind(R.id.youhuiParent_refund_details_activity)
    LinearLayout mYouHuiParent;
    @Bind(R.id.tv_youhui_amount)
    TextView mTvYouHui;
    @Bind(R.id.tv_actual_paid)
    TextView tv_actual_paid;
    @Bind(R.id.store_parent_details)
    LinearLayout mStoreParent;   // 和卡用
    @Bind(R.id.tv_store_detail)
    TextView mTvStore;  // 和卡用


    private String mStatus;
//    private BillDetailsBean mDataBean;
    private List<BillDetailsBean.DataBean> mDataList;

    private String payChannel, merId, tid, transAmount, orderNo, payStyle, transDate, sevenDate, description, refundSuccessDate, cycle,
            isScanPay,periodType;
    private String UUID; // 从上个界面传过来。
    private String loginType;
    private String payableAmount;   // 实际支付金额


    @Override
    protected int getContentView() {
        return R.layout.activity_refund_details;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        transAmount = intent.getStringExtra("transAmount");
        orderNo = intent.getStringExtra("orderNo");
        payStyle = intent.getStringExtra("payStyle");
        transDate = intent.getStringExtra("transDate");
        tid = intent.getStringExtra("tid");
        periodType = intent.getStringExtra("periodType");
        description = intent.getStringExtra("description");
        refundSuccessDate = intent.getStringExtra("refundSuccessDate");
        UUID = intent.getStringExtra("UUID");
        merId = intent.getStringExtra("merId");
        isScanPay = intent.getStringExtra("isScanPay");
        payChannel = intent.getStringExtra("payChannel");
        payableAmount = intent.getStringExtra("payableAmount");//实际支付金额
        tv_title.setText("交易详情");
        tv_trader_time.setText(transDate);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
    }

    @Override
    public void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        queryBillingDetails(orderNo);
    }
    private DecimalFormat df = new DecimalFormat("0.00");
    private void queryBillingDetails(String orderNo1) {
        JSONObject dataParam = new JSONObject();
        try {
            dataParam.put("orderNo", orderNo1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.BILLING_DETAILS, dataParam, new MyOkCallback<BillDetailsBean>() {
            @Override
            public void onStart() {
                showLoading();
            }
            @Override
            public void onSuccess(BillDetailsBean dataBean) {
                String status = dataBean.getStatus();
                String message = dataBean.getMessage();
                if ("0".equals(status)) {
                    mDataList = dataBean.getData();
                    if (mDataList != null && mDataList.size() != 0) {
                        orderNo = mDataList.get(0).orderNo;
                        sevenDate = mDataList.get(0).sevenDate;
                        mStatus = mDataList.get(0).status;
                        Double transAmt = 0d, payAmt = 0d;
                        if(!TextUtils.isEmpty(mDataList.get(0).transAmount)){
                            transAmt = YrmUtils.stringToDouble(mDataList.get(0).transAmount);
                            tv_transAmount.setText(df.format(transAmt));
                            tv_order_amount.setText(YrmUtils.decimalTwoPoints(mDataList.get(0).transAmount));
                        }
                        if (!TextUtils.isEmpty(mDataList.get(0).payableAmount)){
//                                BigDecimal bd = new BigDecimal(Double.valueOf(mDataList.get(0).payableAmount).toString());
//
                            payAmt = YrmUtils.stringToDouble(mDataList.get(0).payableAmount);
                        }

                        switch(mStatus){
                            case "退款中":
                                mReturnMoney.setVisibility(View.GONE);
                                tv_refund_status.setTextColor(getResources().getColor(R.color.blue_color2));
                                break;
                            case "退款失败":
                                mReturnMoney.setVisibility(View.GONE);
                                tv_refund_status.setTextColor(getResources().getColor(R.color.lmf_main_color));
                                break;
                            case "支付成功":
                                if (TextUtils.isEmpty(payableAmount)){
                                    mReturnMoney.setVisibility(View.GONE);
                                }else {
                                    if("福利卡".equals(mDataList.get(0).payChannel)){
                                        mReturnMoney.setVisibility(View.GONE);
                                    }else if ("和卡".equals(mDataList.get(0).payChannel)){
                                        mReturnMoney.setVisibility(View.GONE);  // 退款暂时没做
                                        mStoreParent.setVisibility(View.VISIBLE);
                                        mTvStore.setText(mDataList.get(0).storeName);
                                    } else {
                                        mReturnMoney.setVisibility(View.VISIBLE);
                                    }
                                }
                                break;
                        }
                        if (transAmt!=0 && payAmt!=0 && transAmt>payAmt){
                            double youhuid = transAmt - payAmt;
                            tv_actual_paid.setText("实际支付金额");
                            tv_order_amount.setText(df.format(payAmt));
                            mYouHuiParent.setVisibility(View.VISIBLE);
                            mReturnMoney.setVisibility(View.GONE);
                            mTvYouHui.setText("- "+df.format(youhuid));
                        }
                        if(TextUtils.isEmpty(orderNo)){
                            tv_trader_no.setText("");
                        }else {
                            tv_trader_no.setText(Html.fromHtml(splitOrderNo(orderNo)));
                        }
                        tv_mode_pay.setText(mDataList.get(0).payChannel);
                        tv_trader_time.setText(mDataList.get(0).transDate);
                        tv_refund_time.setText(mDataList.get(0).refundDate);
                        if(TextUtils.isEmpty(mDataList.get(0).refundOrderNo)){
                            tv_refund_no.setText("");
                        }else {
                            tv_refund_no.setText(Html.fromHtml(splitOrderNo(mDataList.get(0).refundOrderNo)));
                        }
                        if (TextUtils.isEmpty(mDataList.get(0).tid)) {
                            ll_tid_code.setVisibility(View.GONE);
                        } else {
                            tv_collect_code.setText(mDataList.get(0).tid);
                        }

                        if(TextUtils.isEmpty(mDataList.get(0).remark)){
                            ll_remark.setVisibility(View.GONE);
                        }else {
                            ll_remark.setVisibility(View.VISIBLE);
                            tv_remark.setText(mDataList.get(0).remark);
                        }
                        tv_refund_status.setText(mStatus);

                        if (!"支付成功".equals(mStatus)){
                            mRefundNoParent.setVisibility(View.VISIBLE);
                            mRefundTimeParent.setVisibility(View.VISIBLE);
//                                mTvRefund.setVisibility(View.GONE);
                        }else{
                            mRefundNoParent.setVisibility(View.GONE);
                            mRefundTimeParent.setVisibility(View.GONE);
//                                mTvRefund.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    if (!TextUtils.isEmpty(message)){
                        ToastUtil.showShortToast(message);
                    }else{
                        ToastUtil.showShortToast("网络连接不佳");
                    }

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
                return BillDetailsBean.class;
            }
        });
    }

    public String splitOrderNo(String orderNo){
        if(orderNo.length()>4){
            String orderNo1 = orderNo.substring(orderNo.length()-4,orderNo.length());
            String orderNo2 = orderNo.substring(0,orderNo.length()-4);
            String orderNo3 = orderNo2+" <b>"+orderNo1+"</b>";
            return orderNo3;
        }
        return orderNo;
    }


    private MyBottomDialog mDialog;
    //退款
    @OnClick(R.id.tv_refund)
    public void showMyDialog() {
            if ("1".equals(periodType)) {
                ToastUtil.showShortToast("30天内的交易可以退款，该交易已超过退款期限");
                return;
            }

        List<String> strList = new ArrayList<>();
        strList.add("立即退款");
        strList.add("退款记录");
        if (mDialog == null) {
            mDialog = new MyBottomDialog(this);
        }
        mDialog.showThisDialog("退款", strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                if (position == 0) {

                    Intent intent = new Intent(RefundDetailsActivity.this,RefundSecondActivity.class);
                    intent.putExtra("transAmount",transAmount);
                    intent.putExtra("merId",merId);
                    intent.putExtra("sevenDate",sevenDate);
                    intent.putExtra("orderNo",orderNo);
                    startActivity(intent);
                }else if(position == 1){
                    Intent intent = new Intent(RefundDetailsActivity.this,RefundRecordActivity.class);
                    intent.putExtra("orderNo",orderNo);
                    startActivity(intent);
                }
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });

    }





}
