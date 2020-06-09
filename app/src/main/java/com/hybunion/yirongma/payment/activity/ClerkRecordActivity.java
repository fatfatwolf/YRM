package com.hybunion.yirongma.payment.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.ClerkRecordBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.ClerkRecordAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ClerkRecordActivity extends BasicActivity {
    @Bind(R.id.lv_clerk_detail)
    ListView lv_clerk_detail;
    @Bind(R.id.ll_titlebar_back)
    LinearLayout ll_titlebar_back;
    private int page = 0;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    private int length = 0;
    ClerkRecordAdapter clerkRecordAdapter;
    private List<ClerkRecordBean.DataBean> list = new ArrayList<>();

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_clerk_record;
    }

    @Override
    public void initView() {
        super.initView();
        ll_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        clerkRecordAdapter = new ClerkRecordAdapter(this);
        lv_clerk_detail.setAdapter(clerkRecordAdapter);
        handleList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryDutyList(0);
    }

    /**
     * 加载刷新监听
     */
    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (length == 20) {
                    page++;
                    // 请求数据
                    queryDutyList(page);
                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                smartRefresh_layout.setEnableLoadMore(true);
                queryDutyList(page);
            }
        });


    }

    public void queryDutyList(int page){
        String url = NetUrl.QUERY_DUTY_LISY;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("employeeId", SharedPreferencesUtil.getInstance(ClerkRecordActivity.this).getKey("staffId"));
            jsonObject.put("limit",20);
            jsonObject.put("start",page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(ClerkRecordActivity.this, url, jsonObject, new MyOkCallback<ClerkRecordBean>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishRefresh();
                smartRefresh_layout.finishLoadMore();
                showLoading();
            }

            @Override
            public void onSuccess(ClerkRecordBean clerkRecordBean) {

                if(list==null){
                    list = new ArrayList<>();
                }
                list.clear();
                if(clerkRecordBean.getData()!=null){
                    list = clerkRecordBean.getData();
                    clerkRecordAdapter.addAll(list);
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
                return ClerkRecordBean.class;
            }
        });
    }
}
