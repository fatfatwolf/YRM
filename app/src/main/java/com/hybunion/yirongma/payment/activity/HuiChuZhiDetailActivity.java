package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.HuiChuZhiDetailBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.Bind;

/**
 * 惠储值列表详情页
 */

public class HuiChuZhiDetailActivity extends BasicActivity {
    @Bind(R.id.tv_amt_huichuzhi_detail)
    TextView mTvAmtTop;
    @Bind(R.id.tv_amt1_huichuzhi_detail)
    TextView mTvAmtBottom;
    @Bind(R.id.tv_order_status_huichuzhi_detail)
    TextView mTvOrderStatusTop;
    @Bind(R.id.tv_order_status1_huichuzhi_detail)
    TextView mTvOrderStatusBottom;
    @Bind(R.id.tv_trans_time_huichuzhi_detail)
    TextView mTvTransTime;
    @Bind(R.id.tv_pay_channel_huichuzhi_detail)
    TextView mTvPayChannel;
    @Bind(R.id.tv_order_num_huichuzhi_detail)
    TextView mTvOrderNum;

    private String mOrderNum;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_huichuzhi_detail;
    }

    public static void start(Context from, String orderNum){
        Intent intent = new Intent(from, HuiChuZhiDetailActivity.class);
        intent.putExtra("orderNum",orderNum);
        from.startActivity(intent);
    }

    @Override
    public void initView() {
        super.initView();
        mOrderNum = getIntent().getStringExtra("orderNum");
    }

    @Override
    protected void load() {
        super.load();
        showLoading();
        getData(mOrderNum);
    }

    public void getData(String orderNum){
        String url = NetUrl.HUICHUZHI_DETAIL;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderNo",orderNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(HuiChuZhiDetailActivity.this, url, jsonObject, new MyOkCallback<HuiChuZhiDetailBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(HuiChuZhiDetailBean bean) {
                mTvAmtTop.setText(bean.transAmount);
                mTvAmtBottom.setText(bean.transAmount);
                mTvTransTime.setText(bean.transDate);
                mTvPayChannel.setText(bean.payChannel);
                mTvOrderStatusTop.setText(bean.orderStatus);
                mTvOrderStatusBottom.setText(bean.orderStatus);
                mTvOrderNum.setText(bean.orderNo);
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
                return HuiChuZhiDetailBean.class;
            }
        });



    }

}
