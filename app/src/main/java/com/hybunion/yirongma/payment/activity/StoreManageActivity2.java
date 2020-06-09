package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.adapter.StoreManageAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 总店
 */
public class StoreManageActivity2 extends BasicActivity implements View.OnClickListener {
    @Bind(R.id.tv_head)
    TextView tv_head;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.bt_new_store)
    Button bt_new_store;
    @Bind(R.id.et_search)
    EditText et_search;
    @Bind(R.id.tv_search)
    TextView tv_search;
    @Bind(R.id.ll_back)
    LinearLayout ll_back;
    LinearLayout rl_billing_title;
    String searchText = "";
    TextView tv_noData;
    StoreManageAdapter storeManageAdapter;
    private SmartRefreshLayout smartRefresh_layout;
    int page = 0;
    private int isFirst = 0;
    String storeName;
    private String mLoginType;
    private boolean mIsBoss;
    private boolean isRefresh = true;
    private static final int ADD_STORE_SUCCESS = 11;


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_store_manage2;
    }

    @Override
    public void initView() {
        super.initView();
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        tv_head.setText("门店管理");
        bt_new_store.setOnClickListener(this);
        smartRefresh_layout = (SmartRefreshLayout) findViewById(R.id.smartRefresh_layout);
        tv_noData = (TextView) findViewById(R.id.tv_consume_record_nodata);
        rl_billing_title = (LinearLayout) findViewById(R.id.rl_billing_title);
        storeManageAdapter = new StoreManageAdapter(this);
        ll_back.setOnClickListener(this);
        listview.setAdapter(storeManageAdapter);
        tv_search.setOnClickListener(this);
        mIsBoss = "0".equals(mLoginType);
        if (mLoginType.equals("1")) {
            rl_billing_title.setVisibility(View.GONE);
            bt_new_store.setVisibility(View.GONE);
        } else if (mLoginType.equals("0")) {
            rl_billing_title.setVisibility(View.VISIBLE);
            bt_new_store.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void canDo() {
        super.canDo();
        handleList();
        queryStoreList(isFirst, page, "", mIsBoss);
    }

    public void queryStoreList(final int isFirst, int page, String searchText, boolean isBoss){
        String url = NetUrl.QUERY_STORE_LIST;
        JSONObject jsonRequest = new JSONObject();
        try {
            if (isBoss) {
                jsonRequest.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            } else {
                jsonRequest.put("storeId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId"));
            }
            jsonRequest.put("query", searchText);
            jsonRequest.put("limit", "20");
            jsonRequest.put("start", page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().postNoHeader(StoreManageActivity2.this, url, jsonRequest, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishLoadMore();
                smartRefresh_layout.finishRefresh();
                if(isFirst == 0){
                    showLoading();
                }
            }

            @Override
            public void onSuccess(StoreManageBean storeManageBean) {
                String count = storeManageBean.getCount();
                List<StoreManageBean.ObjBean> data = storeManageBean.getData();
                if (data != null && data.size() > 0) {
                    if (isRefresh)
                        mDataList.clear();

                    mDataList.addAll(data);
                    length = data.size();
                }
                if ("0".equals(count)) {
                    tv_noData.setVisibility(View.VISIBLE);
                    smartRefresh_layout.setVisibility(View.GONE);
                } else {
                    storeManageAdapter.addAllList(mDataList, isRefresh);
                    tv_noData.setVisibility(View.GONE);
                    smartRefresh_layout.setVisibility(View.VISIBLE);
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
                return StoreManageBean.class;
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
                isRefresh = false;
                if (length == 20) {
                    isFirst = 0;
                    page = page + 20;
                    // 请求数据
                    queryStoreList(isFirst, page, "", mIsBoss);
                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 0;
                isFirst = 1;
                smartRefresh_layout.setEnableLoadMore(true);
                queryStoreList(isFirst, page, "", mIsBoss);
            }
        });

    }
    int length = 0;
    private List<StoreManageBean.ObjBean> mDataList = new ArrayList<>();
    
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bt_new_store:
                Intent intent = new Intent(StoreManageActivity2.this, OpenNewStoreActivity.class);
                startActivityForResult(intent, ADD_STORE_SUCCESS);
                break;
            case R.id.tv_search:
                isFirst = 0;
                page = 0;
                searchText = et_search.getText().toString().trim();
                queryStoreList(isFirst, page, searchText, mIsBoss);
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_STORE_SUCCESS){
            isFirst = 0;
            page = 0;
            queryStoreList(isFirst, page, "", mIsBoss);
        }

    }
}
