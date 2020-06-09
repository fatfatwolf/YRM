package com.hybunion.yirongma.valuecard.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.util.DateFormatUtil;
import com.hybunion.yirongma.common.util.DateSetUtil;
import com.hybunion.yirongma.common.util.DateTimeGetUtil;
import com.hybunion.yirongma.common.util.GetApplicationInfoUtil;
import com.hybunion.yirongma.common.util.GetResourceUtil;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author SunBingbing
 * @date 2017/4/18
 * @email freemars@yeah.net
 * @description 储值卡汇总报表
 */

public class ValueCardSummaryActivity extends BaseActivity {

    private Context mContext; // 本类对象
    private TextView tv_start_date,tv_end_date; // 开始时间和结束时间
    private TextView tv_buy_consume_amount,tv_buy_consume_count; // 购卡、充值的交易金额和交易数量
    // 储值卡消费模块：应付金额、实付金额、交易数量、所有储值卡余额总计、剩余次数总计、交易次数
    private TextView tv_buy_consume_amount_2,tv_buy_consume_amount_3,
            tv_buy_consume_count_2,tv_buy_consume_count_3,
            tv_buy_consume_count_4,tv_buy_consume_count_5;
    private static final int SUCCESS = 0x1001; // 网络回调成功
    private static final int ERROR = 0x1002; // 网络回调失败
    // 可能暂时性导致内存泄漏（在延时结束后自动释放泄漏的内存）
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS: // 网络回调成功
                    JSONObject response = (JSONObject) msg.obj;
                    String status = response.optString("status");
                    String message = response.optString("message");
                    JSONObject body = response.optJSONObject("body");

                    if ("0".equals(status)){
                        tv_buy_consume_amount.setText(body.optString("moneyTotalAmount"));
                        tv_buy_consume_count.setText(body.optString("moneyTotalNum"));
                        tv_buy_consume_amount_2.setText(body.optString("tranTotalPayAmount"));
                        tv_buy_consume_amount_3.setText(body.optString("tranTotalPaidAmount"));
                        tv_buy_consume_count_2.setText(body.optString("tranTotalNum"));
                        tv_buy_consume_count_5.setText(body.optString("tranTotalTime"));
                        tv_buy_consume_count_3.setText(body.optString("balanceAmount"));
                        tv_buy_consume_count_4.setText(body.optString("balanceNum"));
                    }else {
                        ToastUtil.show(message);
                    }

                    break;
                case ERROR:
                    ToastUtil.show(GetResourceUtil.getString(R.string.poor_network));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_value_card_summary);
        mContext = this;
        // 返回
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishSelf();
            }
        });

        // 标题
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("汇总报表");
        tv_start_date = (TextView) findViewById(R.id.start_date);
        tv_end_date = (TextView) findViewById(R.id.end_date);
        tv_buy_consume_amount = (TextView) findViewById(R.id.buy_consume_amount);
        tv_buy_consume_count = (TextView) findViewById(R.id.buy_consume_count);
        tv_buy_consume_amount_2 = (TextView) findViewById(R.id.buy_consume_amount_2);
        tv_buy_consume_amount_3 = (TextView) findViewById(R.id.buy_consume_amount_3);
        tv_buy_consume_count_2 = (TextView) findViewById(R.id.buy_consume_count_2);
        tv_buy_consume_count_3 = (TextView) findViewById(R.id.buy_consume_count_3);
        tv_buy_consume_count_4 = (TextView) findViewById(R.id.buy_consume_count_4);
        tv_buy_consume_count_5 = (TextView) findViewById(R.id.buy_consume_count_5);

        // 获取当前日期
        String currentDate = DateFormatUtil.formatDate(DateTimeGetUtil.getYear()
                ,DateTimeGetUtil.getMonth(),DateTimeGetUtil.getDay());
        tv_start_date.setText(currentDate);
        tv_end_date.setText(currentDate);
        // 选择开始时间
        tv_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateSetUtil.setDate(mContext, tv_start_date, tv_end_date, 1, new DateSetUtil.DatePickerCallback() {
                    @Override
                    public void loadData() {
                        getSummaryData();
                    }

                    @Override
                    public void showErrorMessage() {
                        showDateErrorMessage();
                    }
                });

            }
        });
        // 选择结束时间
        tv_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateSetUtil.setDate(mContext, tv_end_date, tv_start_date, 0, new DateSetUtil.DatePickerCallback() {
                    @Override
                    public void loadData() {
                        getSummaryData();
                    }

                    @Override
                    public void showErrorMessage() {
                        showDateErrorMessage();
                    }
                });
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        getSummaryData();
    }

    /**
     * 时间选择非法提示（开始时间不能晚于结束时间）
     */
    private void showDateErrorMessage(){
        ToastUtil.show(GetResourceUtil.getString(R.string.dateError));
    }

    private void finishSelf(){
        finish();
        mContext = null;
    }

    /**
     * 获取汇总数据
     */
    private void getSummaryData(){
        showProgressDialog("");
        // 请求参数
        JSONObject jsonParams = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            String startDate = tv_start_date.getText().toString().trim();
            jsonParams.put("startDate",startDate);
            String endDate = tv_end_date.getText().toString().trim();
            jsonParams.put("endDate",endDate);
            jsonParams.put("merId", GetApplicationInfoUtil.getMerchantId());
            body.put("body",jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 回调
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                // 请求成功
                hideProgressDialog();
                Message msg = Message.obtain();
                msg.obj = response;
                msg.what = SUCCESS;
                mHandler.sendMessage(msg);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                // 请求失败
                hideProgressDialog();
                Message msg = Message.obtain();
                msg.obj = error.getMessage();
                msg.what = ERROR;
                mHandler.sendMessage(msg);
            }
        };

        // 请求数据
        VolleySingleton.getInstance(this).addJsonObjectRequest(listener, errorListener, body, NetUrl.VALUE_CARD_SUMMARY);


    }
}
