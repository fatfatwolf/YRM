package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.TerminalBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.TerminalManageAdapter;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

public class ManageTerminalActivity extends BasicActivity {
    private String storeId,kuanTaiId,storeName,kuanTaiName;
    @Bind(R.id.ll_dimen)
    LinearLayout ll_dimen;
    @Bind(R.id.ll_code_details)
    LinearLayout ll_code_details;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    @Bind(R.id.lv_code_list)
    ListView lv_code_list;
    @Bind(R.id.ll_no_code_layout)
    LinearLayout ll_no_code_layout;
    @Bind(R.id.rv_terminal)
    RelativeLayout rv_terminal;
    private int page;
    private TerminalManageAdapter terminalManageAdapter;
    int length = 0;
    private int isFirst;
    private List<TerminalBean.DataBean> list = new ArrayList<>();
    private List<TerminalBean.DataBean> dataList = new ArrayList<>();


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_manage_terminal;
    }

    @Override
    public void initView() {
        super.initView();
        storeId = getIntent().getStringExtra("storeId");
        kuanTaiId = getIntent().getStringExtra("kuanTaiId");
        storeName = getIntent().getStringExtra("storeName");
        kuanTaiName = getIntent().getStringExtra("kuanTaiName");
        terminalManageAdapter = new TerminalManageAdapter(this);
        lv_code_list.setAdapter(terminalManageAdapter);
        lv_code_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManageTerminalActivity.this, SetFixAmountActivity.class);
                intent.putExtra("tidName",dataList.get(position).tidName);
                intent.putExtra("tid",dataList.get(position).tid);
                intent.putExtra("type",dataList.get(position).type);
                intent.putExtra("createDate",dataList.get(position).createDate);
                intent.putExtra("tidUrl",dataList.get(position).tidUrl);
                intent.putExtra("limitAmt",dataList.get(position).limitAmt);
                intent.putExtra("remark",dataList.get(position).remark);
                if(TextUtils.isEmpty(kuanTaiName)){
                    intent.putExtra("storeName",storeName);
                }else {
                    intent.putExtra("storeName",kuanTaiName);
                }
                startActivity(intent);
            }
        });
        handleList();
    }

    @Override
    public void initData() {
        super.initData();

    }

    /**
     * 加载刷新监听
     */
    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (length == 20) {
                    isFirst = 0;
                    page = page +20;
                    // 请求数据
                    queryTerminalList(isFirst);
                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isFirst = 1;
                page = 0;
                smartRefresh_layout.setEnableLoadMore(true);
                queryTerminalList(isFirst);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        queryTerminalList(isFirst);
    }

    private void queryTerminalList(final int isFirst) {

        JSONObject jsonRequest = new JSONObject();
        try {
            if(TextUtils.isEmpty(kuanTaiId)){
                jsonRequest.put("storeId", storeId);
            }else {
                jsonRequest.put("storeId", kuanTaiId);
            }
            jsonRequest.put("query","");
            jsonRequest.put("limit","20");
            jsonRequest.put("type","");
            jsonRequest.put("start",String.valueOf(page));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.QUERY_STORE_BIND_INFO;
        OkUtils.getInstance().postNoHeader(ManageTerminalActivity.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishLoadMore();
                smartRefresh_layout.finishRefresh();
                if(isFirst == 0){
                    showLoading();
                }
            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String status = response.optString("status");
                if(status.equals("0")){
                    Gson mGson = new Gson();
                    String count = response.optString("count");
                    if("0".equals(count)){
                        ll_code_details.setVisibility(View.GONE);
                        ll_no_code_layout.setVisibility(View.VISIBLE);
                        rv_terminal.setBackgroundColor(getResources().getColor(R.color.white));
                    }else {
                        dataList.clear();
                        list.clear();
                        JSONArray body = response.optJSONArray("data");
                        list = mGson.fromJson(body.toString(), new
                                TypeToken<List<TerminalBean.DataBean>>() {
                                }.getType());
                        length = list.size();
                        if(list!=null && list.size()>0){
                            for(int i=0;i<list.size();i++){
                                if(list.get(i).type.equals("0")){
                                    dataList.add(list.get(i));
                                }
                            }
                            if(dataList.size()>0){
                                ll_no_code_layout.setVisibility(View.GONE);
                                rv_terminal.setBackgroundColor(getResources().getColor(R.color.bg_f8));
                                ll_code_details.setVisibility(View.VISIBLE);
                                ll_dimen.setVisibility(View.GONE);
                                terminalManageAdapter.addAllList(dataList);
                            }else {
                                ll_dimen.setVisibility(View.VISIBLE);
                                ll_no_code_layout.setVisibility(View.VISIBLE);
                                ll_code_details.setVisibility(View.VISIBLE);
                                rv_terminal.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                        }else {
                            ll_dimen.setVisibility(View.VISIBLE);
                            ll_no_code_layout.setVisibility(View.VISIBLE);
                            ll_code_details.setVisibility(View.VISIBLE);
                            rv_terminal.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }
                }else {
                    String message = response.optString("message");
                    ToastUtil.show(message);
                }
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
                return String.class;
            }
        });
    }


}
