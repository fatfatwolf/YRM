package com.hybunion.yirongma.valuecard.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.util.GetResourceUtil;
import com.hybunion.yirongma.common.view.MySwipe;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.valuecard.adapter.LMFValueCardBalanceDaAP;
import com.hybunion.yirongma.valuecard.model.VcBalanceDetailBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author lyj
 * @date 2017/8/30
 * @email freemars@yeah.net
 * @description
 */

public class LMFValueCardBalanceDaAT extends BaseActivity implements View.OnClickListener{
    private static final int SUCCESS = 0x1001; // 网络回调成功
    private static final int ERROR = 0x1002; // 网络回调失败
    private Gson mGson;
    private int page = 0;
    private LinearLayout ll_back;
    private MySwipe mySwipe;
    private ListView listView;
    private TextView tv_noData,tv_head;
    private String merchantID,cardNo;
    private boolean hasData; // 是否有数据
    private LMFValueCardBalanceDaAP mLMFValueCardBalanceAP;
    ArrayList<VcBalanceDetailBean> body;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    JSONObject response = (JSONObject) msg.obj;
                    LogUtils.d("返回的数据是：" + response.toString());
                    String status = response.optString("status");
                    if ("1".equals(status)) {
                        hasData = response.optBoolean("hasData");
                        Gson gson=new Gson();
                        try {
                             body = gson.fromJson(response.getString("body"),
                                    new TypeToken<ArrayList<VcBalanceDetailBean>>() {
                                    }.getType());
                            if (body != null &&body.size() > 0) {
                                mySwipe.setVisibility(View.VISIBLE);
                                tv_noData.setVisibility(View.INVISIBLE);
                                if (!hasData) {
                                    mySwipe.loadAllData();
                                } else {
                                    mySwipe.resetText();
                                }
                                if (page==0){
                                    mLMFValueCardBalanceAP.mValueCardBeen.clear();
                                }
                                mLMFValueCardBalanceAP.mValueCardBeen.addAll(body);
                                mLMFValueCardBalanceAP.notifyDataSetChanged();
                                mySwipe.setRefreshing(false);
                                mySwipe.setLoading(false);
                            } else {
                                mySwipe.setVisibility(View.INVISIBLE);
                                tv_noData.setVisibility(View.VISIBLE);
                                mLMFValueCardBalanceAP.clearData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        mySwipe.setVisibility(View.INVISIBLE);
                        tv_noData.setVisibility(View.VISIBLE);
                        String message = response.optString("message");
                        ToastUtil.show(message);
                    }
                    break;
                case ERROR:
                    ToastUtil.show(GetResourceUtil.getString(R.string.network_err_info));
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void initView() {
        setContentView(R.layout.activity_value_card_balance_datial);
        Intent intent=getIntent();
        cardNo = intent.getStringExtra("cardNo");
        merchantID= SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        tv_noData = (TextView) findViewById(R.id.tv_consume_record_nodata);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setVisibility(View.VISIBLE);
        ll_back.setOnClickListener(this);
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_head.setText("消费明细");
        mySwipe = (MySwipe) findViewById(R.id.lv_query_vc_balance_data);
        listView = (ListView) findViewById(R.id.lv_va_balance_flow);
        mLMFValueCardBalanceAP = new LMFValueCardBalanceDaAP(LMFValueCardBalanceDaAT.this);
        listView.setAdapter( mLMFValueCardBalanceAP);
    }

    @Override
    protected void initData() {
        handleList();
        getValueCardBalanceData(page);
    }
    /**
     * 加载刷新监听
     */
    private void handleList() {
        mySwipe.setChildView(listView);
        mySwipe.addFooterView();
        mySwipe.setOnLoadListener(mySwipe.new MyLoad(this) {
            public void onLoad() {
                super.onLoad();
                if (hasData) {
                    page++;
                    // 请求数据
                    getValueCardBalanceData(page);
                } else {
                    mySwipe.loadAllData();
                    mySwipe.setLoading(false);
                }
            }

            @Override
            public void onLoadEnd() {
                super.onLoadEnd();
                mySwipe.clearFootAnimation();
            }
        });
        mySwipe.startOnRefresh(new MySwipe.MyOnRefresh() {
            @Override
            public void onRefresh() {
                page = 0;
                getValueCardBalanceData(page);
            }
        });
    }
    private void  getValueCardBalanceData(final int page) {
        showProgressDialog("");
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.d("lyf-----response" + response.toString());
                // 请求成功
                hideProgressDialog();
                Message msg = Message.obtain();
                msg.obj = response;
                msg.what = SUCCESS;
                mHandler.sendMessage(msg);
            }
        };
        Response.ErrorListener errorlistener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 请求失败
                hideProgressDialog();
                Message msg = Message.obtain();
                msg.obj = error.getMessage();
                msg.what = ERROR;
                mHandler.sendMessage(msg);
            }
        };
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            JSONObject header = new JSONObject();
            header.put("channel", "android");
            header.put("agent_id", Constant.AGENT_ID);
            header.put("version_no", HRTApplication.versionName);
            JSONObject dataParam = new JSONObject();
            dataParam.put("cardNo",  cardNo);
            dataParam.put("page", page);
            dataParam.put("rowsPerPage", Constant.PAGE_SIZE);
            jsonRequest.put("body", dataParam);
            jsonRequest.put("header",header);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleySingleton.getInstance(this).addJsonObjectRequest(listener, errorlistener, jsonRequest, NetUrl.TEABS_DETAIL);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
