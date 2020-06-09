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
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.KuanTaiBean;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.adapter.KuanTaiAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class KuanTaiActivity extends BasicActivity implements View.OnClickListener {
    @Bind(R.id.tv_head)
    TextView tv_head;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.bt_new_kuantai)
    Button bt_new_kuantai;
    @Bind(R.id.et_search)
    EditText et_search;
    @Bind(R.id.tv_search)
    TextView tv_search;
    @Bind(R.id.ll_back)
    LinearLayout ll_back;
    @Bind(R.id.ll_kuantai)
    LinearLayout ll_kuantai;
    @Bind(R.id.ll_gone_clerk)
    LinearLayout ll_gone_clerk;
    @Bind(R.id.rl_billing_title)
    LinearLayout rl_billing_title;
    private SmartRefreshLayout smartRefresh_layout;
    String searchText = "";
    KuanTaiAdapter kuanTaiAdapter;
    String storeName;
    private String mLoginType;
    private boolean mIsBoss;
    private boolean isRefresh = true;
    private int isFirst = 0;
    int page = 0;
    int length = 0;
    private String mMerId, mStoreId;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_kuan_tai;
    }

    @Override
    public void initView() {
        super.initView();
        tv_head.setText("款台管理");
        bt_new_kuantai.setOnClickListener(this);
        smartRefresh_layout = (SmartRefreshLayout) findViewById(R.id.smartRefresh_layout);
        ll_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        mStoreId = getIntent().getStringExtra("storeId");
        storeName = getIntent().getStringExtra("storeName");
        kuanTaiAdapter = new KuanTaiAdapter(this);
        listview.setAdapter(kuanTaiAdapter);
        mIsBoss = "0".equals(mLoginType);
        if ("0".equals(mLoginType)) {
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        } else {
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
        }
    }

    @Override
    public void initData() {
        super.initData();
        handleList();
    }

    public String getStoreId() {
        return mStoreId;
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
                    queryKuanTai(isFirst,page, "", mIsBoss, mStoreId);
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
                queryKuanTai(isFirst,page, "", mIsBoss, mStoreId);
            }
        });

    }

    public void queryKuanTai(final int isFirst, int page, String query, boolean isBoss, String storeId){

        String url = NetUrl.QUERY_CASHIER_LIST;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("storeId",storeId);
            jsonRequest.put("storeName", query);
            jsonRequest.put("start",page);
            jsonRequest.put("limit",20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(KuanTaiActivity.this, url, jsonRequest, new MyOkCallback<KuanTaiBean>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishLoadMore();
                smartRefresh_layout.finishRefresh();
                if(isFirst == 0){
                    showLoading();
                }
            }

            @Override
            public void onSuccess(KuanTaiBean kuanTaiBean) {
                List<KuanTaiBean.DataBean> data = kuanTaiBean.getData();
                if (isRefresh)
                    mDataList.clear();

                mDataList.addAll(data);
                if (mDataList == null || mDataList.size()==0){
                    ll_kuantai.setBackgroundColor(getResources().getColor(R.color.white));
                    ll_gone_clerk.setVisibility(View.VISIBLE);
                    rl_billing_title.setVisibility(View.GONE);
                    smartRefresh_layout.setVisibility(View.GONE);
                }else{
                    length = data.size();
                    ll_kuantai.setBackgroundColor(getResources().getColor(R.color.bg_f8));
                    ll_gone_clerk.setVisibility(View.GONE);
                    rl_billing_title.setVisibility(View.VISIBLE);
                    smartRefresh_layout.setVisibility(View.VISIBLE);
                    kuanTaiAdapter.addAllList(mDataList, isRefresh);
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
                return KuanTaiBean.class;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isRefresh = true;
        isFirst = 0;
        queryKuanTai(isFirst,page, "", mIsBoss, mStoreId);
    }

    private List<KuanTaiBean.DataBean> mDataList = new ArrayList<>();

    @Override
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map, type);
        hideLoading();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_new_kuantai:
                Intent intent = new Intent(KuanTaiActivity.this, AddKuanTaiActivity.class);
                intent.putExtra("storeId", mStoreId);
                startActivity(intent);
                break;
            case R.id.tv_search:
                page = 0;
                isFirst = 0;
                searchText = et_search.getText().toString().trim();
                queryKuanTai(isFirst,page, searchText, mIsBoss, mStoreId);
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

}
