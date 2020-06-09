package com.hybunion.yirongma.valuecard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.view.MySwipe;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.valuecard.adapter.VcBalanceDetailAdapter;
import com.hybunion.yirongma.valuecard.model.VcBalanceDetailBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/10/12.
 * 储值卡-报表-会员余额报表-点击条目-储值卡消费明细
 */
public class BalanceDetailsActivity extends BaseActivity implements View.OnClickListener {
    private Intent intent;
    private String cardNo;
    private ListView listView;
    private int currentPage = 0;
    private LinearLayout ib_back;
    private VcBalanceDetailAdapter vcBalanceDetailAdapter;
    private MySwipe mySwipe;
    private TextView tv_query_nodata;
    private boolean isflag = true, hasNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_balance_details);
        intent = getIntent();
        cardNo = intent.getStringExtra("cardNo");
        init();
        initListener();
        getBalanceDetailList(currentPage, Constant.PAGE_SIZE);
    }

    private void init() {
        ib_back = (LinearLayout) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        mySwipe = (MySwipe) findViewById(R.id.lv_member_balance_data);
        listView = (ListView) findViewById(R.id.lv_balance_statement_detail);
        tv_query_nodata = (TextView) findViewById(R.id.tv_query_nodata);
    }

    private void initListener() {
        mySwipe.setChildView(listView);
        mySwipe.addFooterView();
        //上拉时操作
        mySwipe.setOnLoadListener(mySwipe.new MyLoad(this) {
            @Override
            public void onLoad() {
                super.onLoad();
                if (hasNext == true) {
                    currentPage++;
                    getBalanceDetailList(currentPage, Constant.PAGE_SIZE);
                } else {
                    mySwipe.loadAllData();
                    mySwipe.setLoading(false);
                    return;
                }
            }
            @Override
            public void onLoadEnd() {
                super.onLoadEnd();
                mySwipe.clearFootAnimation();
            }
        });
        //刷新时操作
        mySwipe.startOnRefresh(new MySwipe.MyOnRefresh() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                getBalanceDetailList(currentPage, Constant.PAGE_SIZE);
            }
        });
        vcBalanceDetailAdapter = new VcBalanceDetailAdapter(this);
        listView.setAdapter(vcBalanceDetailAdapter);
    }

    private void getBalanceDetailList(int page, int size) {
        if (isflag) {
            showProgressDialog(null);
            isflag = false;
        }
        RequestQueue requestQueue = VolleySingleton.getInstance(BalanceDetailsActivity.this).getRequestQueue();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.dlyj(jsonObject + "返回数据");
                hideProgressDialog();
                try {
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        Gson gson = new Gson();
                        ArrayList<VcBalanceDetailBean> body = gson.fromJson(
                                jsonObject.getString("body"),
                                new TypeToken<ArrayList<VcBalanceDetailBean>>() {
                                }.getType());
                        if (currentPage == 0) {
                            if (body.size() == 0) {
                                mySwipe.setVisibility(View.GONE);
                                tv_query_nodata.setVisibility(View.VISIBLE);
                                return;
                            } else {
                                mySwipe.setVisibility(View.VISIBLE);
                                tv_query_nodata.setVisibility(View.GONE);
                            }
                            vcBalanceDetailAdapter.vcBalanceDetailBeans.clear();
                        }
                        hasNext = jsonObject.getBoolean("hasData");
                        if (hasNext) {
                            mySwipe.setLoading(false);
                            mySwipe.setRefreshing(false);
                            mySwipe.resetText();
                        } else {
                            mySwipe.setRefreshing(false);
                            mySwipe.setLoading(false);
                            mySwipe.loadAllData();
                        }
                        vcBalanceDetailAdapter.vcBalanceDetailBeans.addAll(body);
                        vcBalanceDetailAdapter.notifyDataSetChanged();
                        mySwipe.loadAllData();
                        mySwipe.setLoading(false);
                    } else {
                        mySwipe.loadAllData();
                        mySwipe.setLoading(false);
                        if (currentPage == 0) {
                            listView.setVisibility(View.GONE);
                            tv_query_nodata.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                hideProgressDialog();
                System.out.println("error");
                Toast.makeText(BalanceDetailsActivity.this, getString(R.string.poor_network), Toast.LENGTH_LONG)
                        .show();
            }
        };

        JSONObject jsonRequest;
        try {
            JSONObject header = new JSONObject();
            header.put("channel", "android");
            header.put("agent_id", Constant.AGENT_ID);
            header.put("version_no", HRTApplication.versionName);
            JSONObject body = new JSONObject();
            body.put("cardNo", cardNo);
            body.put("page", page);
            body.put("rowsPerPage", size);
            jsonRequest = new JSONObject();
            jsonRequest.put("header", header);
            jsonRequest.put("body", body);
            VolleySingleton.getInstance(this).addJsonObjectRequest(listener, errorListener, jsonRequest, NetUrl.TEABS_DETAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }
    }
}
