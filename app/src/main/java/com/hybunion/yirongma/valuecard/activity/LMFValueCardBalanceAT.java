package com.hybunion.yirongma.valuecard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.hybunion.yirongma.valuecard.adapter.LMFValueCardBalanceAP;
import com.hybunion.yirongma.valuecard.model.LMFValueCardBalanceBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author lyj
 * @date 2017/8/29
 * @email freemars@yeah.net
 * @description
 */

public class LMFValueCardBalanceAT extends BaseActivity implements View.OnClickListener {
    private static final int SUCCESS = 0x1001; // 网络回调成功
    private static final int ERROR = 0x1002; // 网络回调失败
    private Gson mGson;
    private int page = 0;
    private LinearLayout ib_back;
    private MySwipe mySwipe;
    private ListView listView;
    private TextView tv_noData, tv_search;
    private String merchantID;
    private EditText et_search;
    private boolean hasData; // 是否有数据
    private LMFValueCardBalanceAP mLMFValueCardBalanceAP;
    private boolean mIsNeedLoad = true; // 是否需要 loadingDialog
    private List<LMFValueCardBalanceBean.ValueCardBean> mValueCardBeen;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    JSONObject response = (JSONObject) msg.obj;
                    LogUtils.d("返回的数据是：" + response.toString());
                    String status = response.optString("status");
                    if ("1".equals(status)) {
                        JSONArray body = response.optJSONArray("data");
                        hasData = response.optBoolean("hasNextPage");
                        mGson = new Gson();
                        mValueCardBeen = mGson.fromJson(body.toString(), new
                                TypeToken<List<LMFValueCardBalanceBean.ValueCardBean>>() {
                                }.getType());
                        if (mValueCardBeen != null && mValueCardBeen.size() > 0) {
                            mySwipe.setVisibility(View.VISIBLE);
                            tv_noData.setVisibility(View.INVISIBLE);
                            if (!hasData) {
                                mySwipe.loadAllData();
                            } else {
                                mySwipe.resetText();
                            }
                            if (page == 0) {
                                mLMFValueCardBalanceAP.mValueCardBeen.clear();
                            }
                            mLMFValueCardBalanceAP.mValueCardBeen.addAll(mValueCardBeen);
                            mLMFValueCardBalanceAP.notifyDataSetChanged();
                            mySwipe.setRefreshing(false);
                            mySwipe.setLoading(false);
                        } else {
                            if (page == 0) {
                                mySwipe.setVisibility(View.INVISIBLE);
                                tv_noData.setVisibility(View.VISIBLE);
                                mLMFValueCardBalanceAP.clearData();
                            }
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
        setContentView(R.layout.activity_lmf_vc_balance);
        merchantID = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        tv_noData = (TextView) findViewById(R.id.tv_consume_record_nodata);
        ib_back = (LinearLayout) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.getBackground().setAlpha(100);
        mySwipe = (MySwipe) findViewById(R.id.lv_query_vc_balance_data);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_search.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.lv_va_balance_flow);
        mLMFValueCardBalanceAP = new LMFValueCardBalanceAP(LMFValueCardBalanceAT.this);
        listView.setAdapter(mLMFValueCardBalanceAP);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= listView.getCount() - 1)
                    return;

                Intent intent = new Intent(LMFValueCardBalanceAT.this, LMFValueCardBalanceDaAT.class);
                intent.putExtra("cardNo", mLMFValueCardBalanceAP.mValueCardBeen.get(position).getCardNo());
                startActivity(intent);
            }
        });
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
                mIsNeedLoad = true;
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
                mIsNeedLoad = false;
                getValueCardBalanceData(page);
            }
        });
    }

    private void getValueCardBalanceData(final int page) {
        String url = "https://10.51.130.153:8080/JHAdminConsole/member/balance/getMemberBalanceNew.do";
        if (mIsNeedLoad)
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
            if ("".equals(et_search.getText().toString().trim())) {
                jsonRequest.put("code", "");
            } else {
                jsonRequest.put("code", et_search.getText().toString().trim());
            }
            jsonRequest.put("merId", merchantID);
            jsonRequest.put("page", page);
            jsonRequest.put("rowsPerPage", Constant.PAGE_SIZE);
            LogUtils.d(jsonRequest.toString() + "----jsonRequest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleySingleton.getInstance(this).addJsonObjectRequest(listener, errorlistener, jsonRequest, NetUrl.MEMBER_BALANCE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_search:
                page = 0;
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                getValueCardBalanceData(page);
                break;
        }
    }
}
