package com.hybunion.yirongma.payment.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.QueryClerkListBean;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.util.GetResourceUtil;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.adapter.ClerkListViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 员工划拨
 * @date 2017/8/16
 * @email freemars@yeah.net
 * @description
 */

public class ClerkSettingActivity2 extends BaseActivity implements View.OnClickListener{
    int page=0;
    private SmartRefreshLayout smartRefresh_layout;
    private ListView listClerk;
    private TextView tvHead;
    private EditText et_search;
    private LinearLayout ll_back,ll_gone_clerk;
    private Button bt_clerk_sure;
    private List<QueryClerkListBean.ObjBean> beanList;
    private List<QueryClerkListBean.ObjBean> beanList1 = new ArrayList<>();
    private ClerkListViewAdapter mClerSettingkAdapter;
    private static final int SUCCESS = 0x1001; // 网络回调成功
    private static final int ERROR = 0x1002; // 网络回调失败
    private String workerName = "";
    private String mStoreId;//门店ID
    private String storeId;//款台ID
    private Gson mGson;
    private Button btn_head_right;
    private IWXAPI api;
    private boolean isRefresh;
    private int length = 0;
    private String type;
    private final String APP_ID="wx91edf936606e9712";
    private String employId = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    hideProgressDialog();
                    JSONObject response = (JSONObject) msg.obj;
                    LogUtils.d(response+"接受数据");
                    QueryClerkListBean entry = new QueryClerkListBean();
                    entry.setStatus(response.optString("status"));
                    String data = response.optString("data");
                    entry.setMessage(response.optString("message"));
                    String storeName = response.optString("storeName");
                    SharedPreferencesUtil.getInstance(ClerkSettingActivity2.this).putKey("storeName",storeName);
                    entry.setHasNextPage(Boolean.valueOf(response.optString("hasNextPage")));
                    String status = entry.getStatus();
                    String message = entry.getMessage();
                    LogUtils.d(status+message+"返回数据");
                    if ("0".equals(status)){
                        if ("查询列表为空！".equals(message)){
                            smartRefresh_layout.setVisibility(View.GONE);
                            listClerk.setVisibility(View.GONE);
                            ll_gone_clerk.setVisibility(View.VISIBLE);
                        }else {
                            mGson = new Gson();
                            if(!TextUtils.isEmpty(data)){
                                beanList = mGson.fromJson(data,
                                        new TypeToken<List<QueryClerkListBean.ObjBean>>() {
                                        }.getType());
                                if (beanList!=null){
                                    if (isRefresh)
                                        beanList1.clear();

                                    beanList1.addAll(beanList);
                                    length = beanList.size();
                                }
                                if (beanList1 !=null &&  beanList1.size() > 0){
                                    smartRefresh_layout.setVisibility(View.VISIBLE);
                                    listClerk.setVisibility(View.VISIBLE);
                                    ll_gone_clerk.setVisibility(View.GONE);
                                    mClerSettingkAdapter.updateList(beanList1,isRefresh);
                                }else {
                                    ll_gone_clerk.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }else {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(ClerkSettingActivity2.this, APP_ID);
        api.registerApp(APP_ID);
        setContentView(R.layout.activity_clerk_setting2);
        initViews();
        initDatas();
    }
    private void initDatas() {
        // 初始化 ListView
        handleList();
    }
    private void initViews() {
        tvHead=(TextView) findViewById(R.id.tv_head);
        tvHead.setText("员工划拨");
        type = getIntent().getStringExtra("type");
        listClerk = (ListView) findViewById(R.id.lv_clerk);
        smartRefresh_layout = (SmartRefreshLayout) findViewById(R.id.smartRefresh_layout);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        et_search = (EditText) findViewById(R.id.et_search);
//        ll_add_clerk = (RelativeLayout) findViewById(R.id.ll_add_clerk);
        bt_clerk_sure = findViewById(R.id.bt_clerk_sure);
        btn_head_right = (Button) findViewById(R.id. btn_head_right);
        btn_head_right.setVisibility(View.GONE);
        btn_head_right.setText("添加员工");
        btn_head_right.setOnClickListener(this);
//        ll_add_clerk.setOnClickListener(this);
        bt_clerk_sure.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_gone_clerk = (LinearLayout) findViewById(R.id.ll_gone_clerk);
        mStoreId = getIntent().getStringExtra("mStoreId");
        storeId = getIntent().getStringExtra("storeId");//kuantaiId
        mClerSettingkAdapter  = new ClerkListViewAdapter(ClerkSettingActivity2.this, beanList1);
        listClerk.setAdapter(mClerSettingkAdapter);
        listClerk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                beanList1.get(position).isClicked = !beanList1.get(position).isClicked;
                mClerSettingkAdapter.notifyDataSetChanged();
            }
        });
    }
    /**
     * 加载刷新监听
     */
    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                if (length == 20) {
                    page++;
                    // 请求数据
                    QueryClerk(page, false);
                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(true);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 0;
                QueryClerk(page, false);
            }
        });


    }
    private void  QueryClerk(final int page, final boolean isShow) {
        String url = NetUrl.QUERY_CLERK_LIST;
        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("storeId",mStoreId);
            jsonRequest.put("employName", workerName);
            jsonRequest.put("page", page);
            jsonRequest.put("position","2");
            jsonRequest.put("rowsPerPage", 20);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkSettingActivity2.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishLoadMore();
                smartRefresh_layout.finishRefresh();
                if(isShow)
                showProgressDialog("");
            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {

                }
                Message msg = Message.obtain();
                msg.obj = response;
                msg.what = SUCCESS;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络链接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }
    Handler handler  = new Handler(){
        @Override


        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mClerSettingkAdapter.notifyDataSetChanged();
        }
    };

    StringBuilder builder;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_clerk_sure:
                if(builder == null){
                    builder = new StringBuilder();
                }else {
                    builder.delete(0,builder.length());
                }
                for(int i=0;i<beanList1.size();i++){

                    if(beanList1.get(i).isClicked){
                        Log.i("xjz111---employId","走到了");
                        builder.append(beanList1.get(i).getEmployId() + ",");
                    }
                }
                if(builder.toString().equals("")){

                }else {
                    employId = builder.toString().substring(0,builder.length()-1);
                }
                if(TextUtils.isEmpty(employId)){
                    ToastUtil.show("请选择划拨的员工");
                }else {
                    updateStoreIdOfEmployee(employId);
                }


                break;
        }
    }

    private void  updateStoreIdOfEmployee(String employId) {
        String url = NetUrl.UPDATE_STORE_EMPLOYEE;
        JSONObject jsonRequest = new JSONObject();

        try {
            jsonRequest.put("currentStoreId",storeId);
            jsonRequest.put("employId", employId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkSettingActivity2.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showProgressDialog("");

            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(response!=null){
                    String status = response.optString("status");
                    String message = response.optString("message");

                    if(TextUtils.isEmpty(message))
                        ToastUtil.show(message);

                    if("0".equals(status)){
                        finish();
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRefresh = true;
        QueryClerk(page, true);
    }

}
