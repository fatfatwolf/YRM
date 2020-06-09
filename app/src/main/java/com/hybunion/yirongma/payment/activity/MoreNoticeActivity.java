package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MoreNoticeBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.adapter.MoreNoticeAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2017/11/6.
 */

public class MoreNoticeActivity extends BasicActivity {
    private static final int SUCCESS = 0x1001; // 网络回调成功
    private static final int ERROR = 0x1002; // 网络回调失败
    private List<MoreNoticeBean.DataBean> rolllist;
    @Bind(R.id.tv_titlebar_back_title)
    TextView tv_titlebar;
    @Bind(R.id.notice_smart)
    SmartRefreshLayout smartRefreshLayout;
    @Bind(R.id.notice_listview)
    ListView notice_listview;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    String status;
    boolean hasData;//是否有下一页
    int page = 0;
    MoreNoticeAdapter moreNoticeAdapter;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                    JSONObject response = (JSONObject) msg.obj;
                    LogUtil.d(response+"返回参数");
                    String status;
                    try {
                        status = response.getString("status");
                        hasData = response.getBoolean("hasNextPage");
                        Gson mGson = new Gson();
                        if ("0".equals(status)) {
                            JSONArray body = response.optJSONArray("data");
                            rolllist = mGson.fromJson(body.toString(), new
                                    TypeToken<List<MoreNoticeBean.DataBean>>() {
                                    }.getType());
                            if (null != rolllist && rolllist.size() > 0) {
                                tv_nodata.setVisibility(View.GONE);
                                smartRefreshLayout.setVisibility(View.VISIBLE);

                                if (page==0){
                                    moreNoticeAdapter.mNotice.clear();
                                }
                                LogUtil.d(rolllist.size()+"====mMemberInforBeen");
                                moreNoticeAdapter.mNotice.addAll(rolllist);
                                moreNoticeAdapter.notifyDataSetChanged();
                            }else {
                                smartRefreshLayout.setVisibility(View.INVISIBLE);
                                tv_nodata.setVisibility(View.VISIBLE);
                                moreNoticeAdapter.clearData();
                            }
                        } else {
                            smartRefreshLayout.setVisibility(View.GONE);
//                            ToastUtil.show(massge);
                            tv_nodata.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.more_notice_activity;
    }

    @Override
    public void initView() {
        super.initView();
        tv_titlebar.setText("公告消息");
        moreNoticeAdapter=new MoreNoticeAdapter(MoreNoticeActivity.this);
        notice_listview.setAdapter(moreNoticeAdapter);
        notice_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>=notice_listview.getCount() -1)
                    return;

                Intent intent=new Intent(MoreNoticeActivity.this,NoticeDetailActivity.class);
                intent.putExtra("title",rolllist.get(i).getTitle());
                intent.putExtra("time",rolllist.get(i).getCreateDate());
                intent.putExtra("content",rolllist.get(i).getContent());
                startActivity(intent);
            }
        });
    }
    @Override
    public void initData() {
        super.initData();
        getData(0);
        handleList();
    }
    /**
     * 加载刷新监听
     */
    private void handleList() {
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {//下拉加载更多
                if(hasData){//有下一页
                    page++;
                    getData(1);
                }else {
                    smartRefreshLayout.setEnableLoadMore(false);
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {//上拉刷新
                page = 0;
                getData(1);
            }
        });

    }
    @OnClick(R.id.ll_titlebar_back)
    public void backUp(){
        finish();
    }


    private void getData(final int type) {
        String url = NetUrl.NOTICEINFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("platForm", "1");
            jsonObject.put("page", String.valueOf(page));
            jsonObject.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(MoreNoticeActivity.this, url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                if(type == 0){
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
                Message msg = Message.obtain();
                msg.obj = response;
                msg.what = SUCCESS;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
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
