package com.hybunion.yirongma.payment.activity;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MemberListBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.MemberListAdapter;
import com.hybunion.yirongma.payment.adapter.StoreListAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MemberListActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.ll_all_store)
    LinearLayout ll_all_store;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tv_storeName)
    TextView tv_storeName;
    @Bind(R.id.arrow_select_lmf_billing_da)
    ImageView mImgSelectArrow;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    @Bind(R.id.tv_vip_count)
    TextView tv_vip_count;
    @Bind(R.id.tv_no_data)
    TextView tv_no_data;
    MemberListAdapter listAdapter;
    List<MemberListBean.DataBean> dataList;
    List<StoreManageBean.ObjBean> mStoreList = new ArrayList<>();//门店列表的list
    StoreListAdapter storeListAdapter;
    MyBottonPopWindow popWindow;
    private String mStoreId = "",mStoreName;
    public String merId;
    private String mSelectedStoreId,mSelectStoreName;//选中的门店ID,门店名字
    private String loginType;
    private boolean isRefresh = true;
    int length = 0;
    int page = 0;
    private String vipCount;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_member_list;
    }

    @Override
    public void initView() {
        super.initView();
        dataList = new ArrayList<>();
        handleList();
        vipCount = getIntent().getStringExtra("vipCount");
        tv_vip_count.setText(vipCount+"人");
        loginType = SharedPreferencesUtil.getInstance(MemberListActivity.this).getKey("loginType");
        if(loginType.equals("0")){//老板
            merId = SharedPreferencesUtil.getInstance(MemberListActivity.this).getKey("merchantID");
            mImgSelectArrow.setVisibility(View.VISIBLE);
        }else {
            mStoreName = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeName");
            tv_storeName.setText(mStoreName);
            merId = SharedPreferencesUtil.getInstance(MemberListActivity.this).getKey("shopId");
            mStoreId = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId");
            mImgSelectArrow.setVisibility(View.GONE);
        }
        storeListAdapter = new StoreListAdapter(this);
        listAdapter = new MemberListAdapter(this,dataList);
        listView.setAdapter(listAdapter);
        ll_all_store.setOnClickListener(this);
    }


    @Override
    protected void canDo() {
        super.canDo();
        setMemberDetails(merId, page, loginType,"",mStoreId);

    }

    public void setMemberDetails(String merId,int page,String loginType,String query, String storeId){
        String url = NetUrl.MEMBER_DETAIL;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("merId",merId);
            jsonRequest.put("storeId",storeId);
            jsonRequest.put("loginType",loginType);
            jsonRequest.put("query", query);
            jsonRequest.put("start",page);
            jsonRequest.put("limit",20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(MemberListActivity.this, url, jsonRequest, new MyOkCallback<MemberListBean>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishRefresh();
                smartRefresh_layout.finishLoadMore();
                showLoading();
            }

            @Override
            public void onSuccess(MemberListBean memberListBean) {
                List<MemberListBean.DataBean> data = memberListBean.getData();
                if (isRefresh)
                    dataList.clear();

                if(data!=null && data.size()>0){
                    tv_vip_count.setText(data.size()+"人");
                    dataList.addAll(data);
                    length = data.size();
                    smartRefresh_layout.setVisibility(View.VISIBLE);
                    listAdapter.updateList(dataList);
                    tv_no_data.setVisibility(View.GONE);
                }else{
                    length = 0;
                    tv_vip_count.setText("0人");
                }

                if(dataList == null || dataList.size() == 0) {
                    smartRefresh_layout.setVisibility(View.GONE);
                    tv_no_data.setVisibility(View.VISIBLE);
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
                return MemberListBean.class;
            }
        });
    }



    public void addView(){
        View view = LayoutInflater.from(MemberListActivity.this).inflate(R.layout.list_footer_view,null);
        listView.addFooterView(view);
        isShowHead = false;
    }
    boolean isShowHead = true;
    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                if (length == 20) {
                    page++;
                    // 请求数据
                    showLoading();
                    setMemberDetails(merId, page, loginType,"",mStoreId);
                } else {
                    if(isShowHead)
                        addView();

                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 0;
                smartRefresh_layout.setEnableLoadMore(true);
                setMemberDetails(merId, page, loginType,"",mStoreId);
            }
        });


    }



    public void putStoreList(String merId){
        String url = NetUrl.MEMBER_STORE_LIST;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(MemberListActivity.this, url, jsonObject, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(StoreManageBean result) {
                mStoreList = result.getData();
                showStoreList();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_all_store:
                if(loginType.equals("0")){//老板
                    putStoreList(merId);
                }
                break;
        }
    }
    int storePosition = -1;
    private void showStoreList() {
        if (popWindow == null)
            popWindow = new MyBottonPopWindow(this, mStoreList);

        mImgSelectArrow.setImageResource(R.drawable.arrow_up);
        popWindow.showPopupWindow(storePosition);
        popWindow.setStoreItemListListener(new MyBottonPopWindow.OnStoreItemListener() {
            @Override
            public void setStoreItemListener(int position) {
                storePosition = position;
                mSelectStoreName = mStoreList.get(position).getStoreName();
                mSelectedStoreId = mStoreList.get(position).getStoreId();
            }
        });

        popWindow.setDissmissListener(new MyBottonPopWindow.onDissmissListener() {
            @Override
            public void setDissmissListener() {
                mImgSelectArrow.setImageResource(R.drawable.arrow_down);
            }
        });

        popWindow.setOnCloseListener(new MyBottonPopWindow.onCloseListener() {
            @Override
            public void setOnCloseListener() {
                mImgSelectArrow.setImageResource(R.drawable.arrow_down);

            }
        });

        popWindow.setButtonClickListener(new MyBottonPopWindow.OnSureClickListener() {
            @Override
            public void setButtonClickListener() {
                mImgSelectArrow.setImageResource(R.drawable.arrow_down);
                if(TextUtils.isEmpty(mSelectedStoreId)){
                    ToastUtil.show("请先选择门店");
                    return;
                }
                mStoreId = mSelectedStoreId;
                mStoreName = mSelectStoreName;
                tv_storeName.setText(mStoreName);
                setMemberDetails(merId, page, loginType,"",mStoreId);

            }
        });

    }



}
