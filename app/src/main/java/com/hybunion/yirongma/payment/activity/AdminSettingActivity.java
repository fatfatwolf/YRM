package com.hybunion.yirongma.payment.activity;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.AdminSettingBean;
import com.hybunion.yirongma.payment.bean.QueryClerkListBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.AdminSettingListAdapter;
import com.hybunion.yirongma.payment.adapter.StoreListAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 管理员设置界面
 */

public class AdminSettingActivity extends BasicActivity implements AdminSettingListAdapter.OnDeleteClickListener {
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    @Bind(R.id.lv_admin_setting_activity)
    ListView mLv;
    @Bind(R.id.tv_null_admin_setting_activity)
    TextView mTvNull;
    @Bind(R.id.titleBar_admin_setting_activity)
    TitleBar mTitleBar;

    private AdminSettingListAdapter mAdapter;
    private List<AdminSettingBean.DataBean> mDataList = new ArrayList<>();
    private StoreListAdapter mStoreAdapter;
    private MyBottonPopWindow mPopWindow;
    private String mSelectedStoreId, mSelectedStoreName;  // 当前选中的门店名称和 id
    private String mSelectedClerkPhone;  // 当前选择的店员的手机号

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_admin_settting;
    }

    @Override
    public void initView() {
        super.initView();
        mAdapter = new AdminSettingListAdapter(this, mDataList, this);
        mLv.setAdapter(mAdapter);

        mStoreAdapter = new StoreListAdapter(this);

        mTitleBar.setRightTextClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void rightClick() {
                getStoreList();
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        handleList();
    }

    @Override
    protected void load() {
        super.load();
        getData();
    }

    private void handleList() {
        smartRefresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });
        //下拉刷新

    }

    public void getData(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mer_id", SharedPreferencesUtil.getInstance(AdminSettingActivity.this).getKey(SharedPConstant.MERCHANT_ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.ADMIN_SETTING_LIST;
        OkUtils.getInstance().post(AdminSettingActivity.this, url, jsonObject, new MyOkCallback<AdminSettingBean>() {
            @Override
            public void onStart() {
                smartRefresh_layout.finishRefresh();
                showLoading();
            }

            @Override
            public void onSuccess(AdminSettingBean dataBean) {
                    if (dataBean != null) {
                        if ("0".equals(dataBean.getStatus())) {
                            mDataList.clear();
                            List<AdminSettingBean.DataBean> data = dataBean.getData();
                            if (!YrmUtils.isEmptyList(data)) {
                                smartRefresh_layout.setVisibility(View.VISIBLE);
                                mTvNull.setVisibility(View.GONE);
                                mDataList.addAll(data);
                                mAdapter.updateList(mDataList);
                            } else {
                                smartRefresh_layout.setVisibility(View.GONE);
                                mTvNull.setVisibility(View.VISIBLE);
                            }

                        } else {   //  status=1 系统内部错误    status=2 商户 id 为空
                            String msg = dataBean.getMessage();
                            if (!TextUtils.isEmpty(msg)) {
                                ToastUtil.show(msg);
                            }
                        }

                }
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return AdminSettingBean.class;
            }
        });

    }

    private List<StoreManageBean.ObjBean> mStoreList = new ArrayList<>();
    int storePosition = -1;
    // 获取门店列表
    private void getStoreList() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url= NetUrl.MEMBER_STORE_LIST;
        OkUtils.getInstance().post(AdminSettingActivity.this, url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String data = response.optString("data");
                Gson gson = new Gson();
                List<StoreManageBean.ObjBean> dataList = gson.fromJson(data,
                        new TypeToken<List<StoreManageBean.ObjBean>>() {
                        }.getType());

                mStoreList.clear();
                if (dataList != null) {
                    mStoreList.addAll(dataList);
                }
                mStoreAdapter.addAllList(mStoreList);
                if (mStoreList != null) {
                    mPopWindow = new MyBottonPopWindow(AdminSettingActivity.this, mStoreList);
                    mPopWindow.setTitle("门店/店长");
                    mPopWindow.showPopupWindow(storePosition);
                    mPopWindow.setStoreItemListListener(new MyBottonPopWindow.OnStoreItemListener() {
                        @Override
                        public void setStoreItemListener(int position) {
                            storePosition = position;
                            mSelectedStoreId = mStoreList.get(position).getStoreId();
                            mSelectedStoreName = mStoreList.get(position).getStoreName();
                            mSelectedClerkPhone = "";
                            getStoreClerk();  // 获取门店下店员数据

                        }
                    });

                    mPopWindow.setButtonClickListener(new MyBottonPopWindow.OnSureClickListener() {
                        @Override
                        public void setButtonClickListener() {
                            if (TextUtils.isEmpty(mSelectedStoreId) || TextUtils.isEmpty(mSelectedStoreName)) {
                                ToastUtil.show("请选择门店");
                                return;
                            }

                            if (TextUtils.isEmpty(mSelectedClerkPhone)) {
                                ToastUtil.show("请选择店长");
                                return;
                            }
                            addOrDeleteManager(mSelectedClerkPhone, "1");
                            mPopWindow.dismiss();

                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

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

    private List<QueryClerkListBean.ObjBean> mStoreClerkList = new ArrayList<>();

    // 根据 storeId 获取 门店下所有的店员
    private void getStoreClerk() {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("storeId", mSelectedStoreId);
            jsonRequest.put("employName", "");
            jsonRequest.put("page", "0");
            jsonRequest.put("position", "1");  // 1-只请求店长  2-只请求店员
            jsonRequest.put("rowsPerPage", "10000");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = NetUrl.QUERY_CLERK_LIST;
        OkUtils.getInstance().post(AdminSettingActivity.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
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
                if ("0".equals(status)) {
                    String data = response.optString("data");
                    Gson gson = new Gson();
                    List<QueryClerkListBean.ObjBean> dataList = gson.fromJson(data,
                            new TypeToken<List<QueryClerkListBean.ObjBean>>() {
                            }.getType());
                    mStoreClerkList.clear();
                    if (!YrmUtils.isEmptyList(dataList)) {
                        mStoreClerkList.addAll(dataList);
                        mPopWindow.showClerkList(mStoreClerkList, new MyBottonPopWindow.OnKuanTaiItemListener() {
                            @Override
                            public void setKuanTaiItemListener(int position) {
                                mSelectedClerkPhone = mStoreClerkList.get(position).getEmployPhone();
                            }
                        });
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
                return String.class;
            }
        });

    }


    // 侧滑点击删除按钮监听
    @Override
    public void onDeletClick(int position, AdminSettingBean.DataBean dataBean) {
        ToastUtil.show("删除啦！！");
        if (dataBean != null) {
            addOrDeleteManager(dataBean.phone, "2");
            getData();// 刷新列表
        }
    }

    public void addOrDeleteManager(String loginName, String type ){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginName", loginName);
            jsonObject.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.ADD_MANAGER;
        OkUtils.getInstance().post(AdminSettingActivity.this, url, jsonObject, new MyOkCallback<AdminSettingBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(AdminSettingBean result) {
                if (result!=null){
                    String status = result.getStatus();
                    String msg = result.getMessage();
                    if ("0".equals(status)){
                        ToastUtil.show("添加成功");
                        getData();
                    }else{
                        if (!TextUtils.isEmpty(msg))
                            ToastUtil.show(msg);
                    }
                }else{
                    ToastUtil.show("网络连接不佳");
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return AdminSettingBean.class;
            }
        });
    }


}
