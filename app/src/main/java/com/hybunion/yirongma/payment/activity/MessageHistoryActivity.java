package com.hybunion.yirongma.payment.activity;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MessageHistoryBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.HistoryMsgListAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MessageHistoryActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.selectStoreParent_applet_push_activity)
    LinearLayout selectStoreParent_applet_push_activity;
    @Bind(R.id.arrow_select_applet_push_activity)
    ImageView mImgArrow;
    @Bind(R.id.tvStoreName_applet_push_activity)
    TextView mTvStoreName;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    @Bind(R.id.tv_no_data)
    TextView tv_no_data;

    private boolean isRefresh = true;
    int length = 0;
    int page = 1;

    HistoryMsgListAdapter listAdapter;
    public List<MessageHistoryBean.DataBean> dataList;
    private String storeId="";
    private String loginType;
    private List<StoreManageBean.ObjBean> mStoreList = new ArrayList(); // 加入商圈的门店 List
    private String mSelectedStoreId, mSelectedStoreName;
    private String mStoreName;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_message_history;
    }

    @Override
    public void initView() {
        super.initView();
        loginType = SharedPreferencesUtil.getInstance(MessageHistoryActivity.this).getKey("loginType");
        storeId = getIntent().getStringExtra("storeId");
        mStoreName = getIntent().getStringExtra("storeName");
        if(!TextUtils.isEmpty(mStoreName)){
            mTvStoreName.setText(mStoreName);
        }
        if("1".equals(loginType))
            mImgArrow.setVisibility(View.GONE);

        dataList = new ArrayList<>();
        listAdapter = new HistoryMsgListAdapter(this,dataList);
        listView.setAdapter(listAdapter);
        selectStoreParent_applet_push_activity.setOnClickListener(this);
        mStoreList = (List<StoreManageBean.ObjBean>) getIntent().getSerializableExtra("mStoreList");
        handleList();
    }


    @Override
    protected void canDo() {
        super.canDo();
        showLoading();
        setHistoryMsg(storeId,String.valueOf(page),"20");
    }


    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                if (length == 20) {
                    page++;
                    showLoading();
                    // 请求数据
                    setHistoryMsg(storeId,String.valueOf(page),"20");
                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                smartRefresh_layout.setEnableLoadMore(true);
                setHistoryMsg(storeId,String.valueOf(page),"20");
            }
        });
    }

    public void setHistoryMsg(String storeId,String page,String limit){
        String url = NetUrl.MSH_HISTORY;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("storeId",storeId);
            jsonRequest.put("page",page);
            jsonRequest.put("limit",limit);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(MessageHistoryActivity.this, url, jsonRequest, new MyOkCallback<MessageHistoryBean>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishLoadMore();
                smartRefresh_layout.finishRefresh();
                showLoading();
            }

            @Override
            public void onSuccess(MessageHistoryBean messageHistoryBean) {
                if("0".equals(messageHistoryBean.getStatus())){
                    if (isRefresh)
                        dataList.clear();

                    List<MessageHistoryBean.DataBean> dataList2 = messageHistoryBean.getData();

                    if(dataList2!=null && dataList2.size()>0){
                        length = dataList2.size();
                        dataList.addAll(dataList2);
                        smartRefresh_layout.setVisibility(View.VISIBLE);
                        tv_no_data.setVisibility(View.GONE);
                        listAdapter.updateList(dataList);
                    }else {
                        length = 0;
                    }

                    if(dataList==null || dataList.size() == 0 ){
                        smartRefresh_layout.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    }
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
                return MessageHistoryBean.class;
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectStoreParent_applet_push_activity:
                if(loginType.equals("0")){//老板
                    showStoreList();
                }
                break;
        }
    }

    private MyBottonPopWindow popWindow;
    int storePosition = -1;
    private void showStoreList() {
        if (popWindow == null)
            popWindow = new MyBottonPopWindow(this, mStoreList);

        mImgArrow.setImageResource(R.drawable.arrow_up);
        popWindow.showPopupWindow(storePosition);
        popWindow.setStoreItemListListener(new MyBottonPopWindow.OnStoreItemListener() {
            @Override
            public void setStoreItemListener(int position) {
                storePosition = position;
                mSelectedStoreName = mStoreList.get(position).getStoreName();
                mSelectedStoreId = mStoreList.get(position).getStoreId();
            }
        });

        popWindow.setDissmissListener(new MyBottonPopWindow.onDissmissListener() {
            @Override
            public void setDissmissListener() {
                mImgArrow.setImageResource(R.drawable.arrow_down);
            }
        });

        popWindow.setOnCloseListener(new MyBottonPopWindow.onCloseListener() {
            @Override
            public void setOnCloseListener() {
                mImgArrow.setImageResource(R.drawable.arrow_down);

            }
        });

        popWindow.setButtonClickListener(new MyBottonPopWindow.OnSureClickListener() {
            @Override
            public void setButtonClickListener() {
                mImgArrow.setImageResource(R.drawable.arrow_down);
                if(TextUtils.isEmpty(mSelectedStoreId)){
                    ToastUtil.show("请先选择门店");
                }
                storeId = mSelectedStoreId;
                mTvStoreName.setText(mSelectedStoreName);
                page = 1;
                setHistoryMsg(storeId,String.valueOf(page),"20");




            }
        });

    }


}
