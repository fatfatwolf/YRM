package com.hybunion.yirongma.payment.activity;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.model.BillDetailsBean;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;

public class RefundItemActivity extends BasicActivity {
    @Bind(R.id.tv_transAmount)
    TextView tv_transAmount;
    @Bind(R.id.tv_refund_status)
    TextView tv_refund_status;
    @Bind(R.id.tv_trader_time)
    TextView tv_trader_time;
    @Bind(R.id.tv_collect_code)
    TextView tv_collect_code;
    @Bind(R.id.tv_trader_no)
    TextView tv_trader_no;
    @Bind(R.id.tv_mode_pay)
    TextView tv_mode_pay;
    @Bind(R.id.tv_order_amount)
    TextView tv_order_amount;
    @Bind(R.id.tv_refund_date)
    TextView tv_refund_date;
    @Bind(R.id.tv_refund_no)
    TextView tv_refund_no;
    @Bind(R.id.ll_tid_code)
    LinearLayout ll_tid_code;
    private BillDetailsBean mDataBean;
    private String UUID;
    private List<BillDetailsBean.DataBean> mDataList;
    private String orderNo;
    private String mStatus;
    private String amount;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_item;
    }

    @Override
    public void initView() {
        super.initView();
        UUID = getIntent().getStringExtra("UUID");
        amount = getIntent().getStringExtra("amount");
        if (!TextUtils.isEmpty(amount)) {
            tv_order_amount.setText(YrmUtils.decimalTwoPoints(amount));
            tv_transAmount.setText(YrmUtils.decimalTwoPoints(amount));
        }

    }

    @Override
    public void initData() {
        super.initData();
        queryBillingDetails(UUID);
    }

    private void queryBillingDetails(String UUID) {
        JSONObject dataParam = new JSONObject();
        try {
            dataParam.put("UUID", UUID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(this, NetUrl.BILL_DETAIL, dataParam, new MyOkCallback<BillDetailsBean>() {
            @Override
            public void onStart() {
                showLoading();
            }
            @Override
            public void onSuccess(BillDetailsBean bean) {
                mDataBean = bean;
                if (mDataBean != null) {
                    String status = mDataBean.getStatus();
                    String message = mDataBean.getMessage();
                    if (!TextUtils.isEmpty(status) && "0".equals(status)) {
                        mDataList = mDataBean.getData();
                        orderNo = mDataList.get(0).orderNo;

                        if (mDataList != null && mDataList.size() != 0) {
                            mStatus = mDataList.get(0).status;
                            if (TextUtils.isEmpty(orderNo)) {
                                tv_trader_no.setText("");
                            } else {
                                tv_trader_no.setText(Html.fromHtml(splitOrderNo(orderNo)));
                            }
                            tv_mode_pay.setText(mDataList.get(0).payChannel);
                            tv_trader_time.setText(mDataList.get(0).transDate);
                            tv_refund_date.setText(mDataList.get(0).refundDate);
                            if (TextUtils.isEmpty(mDataList.get(0).refundOrderNo)) {
                                tv_refund_no.setText("");
                            } else {
                                tv_refund_no.setText(Html.fromHtml(splitOrderNo(mDataList.get(0).refundOrderNo)));
                            }
                            if (TextUtils.isEmpty(mDataList.get(0).tid)) {
                                ll_tid_code.setVisibility(View.GONE);
                            } else {
                                tv_collect_code.setText(mDataList.get(0).tid);
                            }
                            tv_refund_status.setText(mStatus);
                            switch (mStatus) {
                                case "退款中":
                                    tv_refund_status.setTextColor(getResources().getColor(R.color.blue_color2));
                                    break;
                                case "退款失败":
                                    tv_refund_status.setTextColor(getResources().getColor(R.color.lmf_main_color));
                                    break;
                            }

                        }
                    }else{
                        if (!TextUtils.isEmpty(message))
                            ToastUtil.showShortToast(message);
                        else
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

    public String splitOrderNo(String orderNo) {
        if (orderNo.length() > 4) {
            String orderNo1 = orderNo.substring(orderNo.length() - 4, orderNo.length());
            String orderNo2 = orderNo.substring(0, orderNo.length() - 4);
            String orderNo3 = orderNo2 + " <b>" + orderNo1 + "</b>";
            return orderNo3;
        }
        return orderNo;
    }
}
