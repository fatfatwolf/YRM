package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
import com.hybunion.yirongma.payment.adapter.ValueCardBillingDetailAP;
import com.hybunion.yirongma.payment.model.BillingDABean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author lyj
 * @date 2017/8/28
 * @email freemars@yeah.net
 * @description
 */

public class ValueCardBillingDetailsAt extends BaseActivity implements View.OnClickListener {
    private MySwipe mySwipe;
    private ExpandableListView mExpandableListView;
    private String merchantID, count, countAll;
    private TextView tv_noData, tv_summay, tv_sum, tv_search;
    private int page = 0;
    private ValueCardBillingDetailAP mValueCardBillingDetailAP;
    private boolean hasData; // 是否有数据
    private static final int SUCCESS = 0x1001; // 网络回调成功
    private static final int ERROR = 0x1002; // 网络回调失败
    private Gson mGson;
    private EditText et_search;
    BillingDABean bean = null;
    private LinearLayout ib_back;
    private boolean mIsNeedLoad = true; // 是否展示 LoadingDialog
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    hideProgressDialog();
                    JSONObject response = (JSONObject) msg.obj;
                    LogUtils.d("返回的数据是：" + response.toString());
                    try {
                        mGson = new Gson();
                        bean = mGson.fromJson(response.toString(), BillingDABean.class);
                        hasData = bean.isHasNextPage();
                        count = bean.getTotalCount();
                        countAll = bean.getCount();
                        tv_sum.setText(count + "笔");
                        tv_summay.setText("¥" + bean.getTotalAmount());
                        String message = bean.getMessage();
                        String status = bean.getStatus();
                        if ("0".equals(status)) {
                            if ("0".equals(countAll)) {
                                mySwipe.setVisibility(View.GONE);
                                mExpandableListView.setVisibility(View.GONE);
                                tv_noData.setVisibility(View.VISIBLE);
                            } else {
                                List<BillingDABean.DataBeanX> dataBeanXes = bean.getData();
                                LogUtils.d(dataBeanXes.size() + "====dataBeanXes");
                                if (dataBeanXes != null && dataBeanXes.size() > 0) {
                                    mySwipe.setVisibility(View.VISIBLE);
                                    tv_noData.setVisibility(View.GONE);
                                    if (!hasData) {
                                        mySwipe.loadAllData();
                                    } else {
                                        mySwipe.resetText();
                                    }
                                    if (page == 0) {
                                        mValueCardBillingDetailAP.group.clear();
                                    }
                                    mValueCardBillingDetailAP.addDataSources(dataBeanXes);
                                    for (int i = 0; i < mValueCardBillingDetailAP.getGroupCount(); i++) {
                                        mExpandableListView.expandGroup(i);
                                    }
                                    mySwipe.setRefreshing(false);
                                    mySwipe.setLoading(false);
                                }
                            }
                        } else {
                            ToastUtil.show(message);
                            mySwipe.setVisibility(View.GONE);
                            mExpandableListView.setVisibility(View.GONE);
                            tv_noData.setVisibility(View.VISIBLE);
                            if (mValueCardBillingDetailAP != null) {
                                mValueCardBillingDetailAP.group.clear();
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
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
        setContentView(R.layout.activity_czk_billing_da);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        merchantID = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        mySwipe = (MySwipe) findViewById(R.id.lv_query_billing_data);
        mExpandableListView = (ExpandableListView) findViewById(R.id.el_billing_da);
        mValueCardBillingDetailAP = new ValueCardBillingDetailAP(ValueCardBillingDetailsAt.this);
        mExpandableListView.setAdapter(mValueCardBillingDetailAP);
        tv_noData = (TextView) findViewById(R.id.tv_billing_record_nodata);
        tv_summay = (TextView) findViewById(R.id.tv_summay);
        tv_sum = (TextView) findViewById(R.id.tv_num);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.getBackground().setAlpha(100);
        tv_search = (TextView) findViewById(R.id.tv_search);
        ib_back = (LinearLayout) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        handleList();
    }

    /**
     * 加载刷新监听
     */
    private void handleList() {
        mySwipe.setChildView(mExpandableListView);
        mySwipe.addFooterView();
        mySwipe.setOnLoadListener(mySwipe.new MyLoad(this) {
            public void onLoad() {
                super.onLoad();
                mIsNeedLoad = true;
                if (hasData) {
                    page++;
                    // 请求数据
                    queryBillingData(page);
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
                mIsNeedLoad = false;
                page = 0;
                queryBillingData(page);
            }
        });
    }

    private void queryBillingData(int page) {
        if (mIsNeedLoad)
            showProgressDialog("");
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtils.d("LYJ-----response" + response.toString());
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
            JSONObject dataParam = new JSONObject();
            jsonRequest = new JSONObject();
            if ("".equals(et_search.getText().toString().trim())) {
                dataParam.put("lastFourCardNo", "");
            } else {
                dataParam.put("lastFourCardNo", et_search.getText().toString().trim());
            }
            dataParam.put("merId", merchantID);
            dataParam.put("page", page);
            dataParam.put("rowsPerPage", Constant.PAGE_SIZE);
            jsonRequest.put("body", dataParam);
            LogUtils.d(jsonRequest.toString() + "----jsonRequest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleySingleton.getInstance(this).addJsonObjectRequest(listener, errorlistener, jsonRequest, NetUrl.QUERY_CZK_BILLING_DA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_search:
                page = 0;
                mIsNeedLoad = true;
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                queryBillingData(page);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsNeedLoad = true;
        queryBillingData(page);
    }
}
