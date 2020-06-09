package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.KuanTaiListAdapter;
import com.hybunion.yirongma.payment.adapter.StoreListAdapter;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class StoreListActivity extends BasicActivity {

    @Bind(R.id.listview_store)
    ListView listview_store;
    @Bind(R.id.listview_kuantai)
    ListView listview_kuantai;
    @Bind(R.id.allParent_store)
    RelativeLayout mAllParent;
    @Bind(R.id.bt_sure)
    Button bt_sure;
    @Bind(R.id.bt_cancel)
    Button bt_cancel;
    StoreListAdapter storeListAdapter;
    KuanTaiListAdapter kuanTaiListAdapter;
    List<StoreManageBean.ObjBean> storeList = new ArrayList<>();
    public List<StoreManageBean.ObjBean> kuantaiList = new ArrayList<>();
    private String mLoginType; // 0-老板  非0-店长或店员
    private String mStoreId;
    private String mSelectedStoreId, mKuanTaiId, mSelectKuanTaiId, mStoreName, mKuanTaiName, mSelectStoreName, mSelectKuanTiaName;
    private boolean mIsKuanTaiListRefresh = true;
    private int mType; // 用来标识是哪里使用
    private boolean mIsKuanTaiSelected; // 是否选择了款台

    // type为 1 ,从“账户”-“员工管理”进入
    public static void start(Context from, int type) {
        Intent intent = new Intent(from, StoreListActivity.class);
        intent.putExtra("type", type);
        from.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_store_list;
    }

    @Override
    public void initView() {
        super.initView();
        mType = getIntent().getIntExtra("type", 0);
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        storeListAdapter = new StoreListAdapter(this);
        kuanTaiListAdapter = new KuanTaiListAdapter(this);
        listview_store.setAdapter(storeListAdapter)
        ;
        listview_kuantai.setAdapter(kuanTaiListAdapter);
        getStoreList();
        isClick();
    }

    public void isClick() {
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreListActivity.this.finish();
            }
        });
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mStoreId)) {
                    ToastUtil.show("请选择门店或款台");
                }
                mStoreName = mSelectStoreName;
                mKuanTaiName = mSelectKuanTiaName;
                mStoreId = mSelectedStoreId;
                mKuanTaiId = mSelectKuanTaiId;

                if (mType == 1) {  // 员工管理
                    if (!mIsKuanTaiSelected) {  // 只选择了门店，没有选择款台，跳转门店的员工管理列表
                        Intent intent = new Intent(StoreListActivity.this, ClerkSettingActivity.class);
                        intent.putExtra("type", "1");
                        intent.putExtra("storeName", mStoreName);
                        intent.putExtra("storeId", mStoreId);
                        StoreListActivity.this.startActivity(intent);

                    } else {  // 既选择了门店，也选择了款台，跳转 款台的 员工管理列表
                        Intent intent = new Intent(StoreListActivity.this, ClerkSettingActivity1.class);
                        intent.putExtra("type", "2");
                        intent.putExtra("storeName", mStoreName);
                        intent.putExtra("mStoreId", mStoreId);
                        intent.putExtra("storeId", mKuanTaiId);
                        StoreListActivity.this.startActivity(intent);

                    }
                } else {
                    Intent intent = new Intent(StoreListActivity.this, ManageTerminalActivity.class);
                    intent.putExtra("storeId", mStoreId);
                    intent.putExtra("kuanTaiId", mKuanTaiId);
                    intent.putExtra("storeName", mStoreName);
                    intent.putExtra("kuanTaiName", mKuanTaiName);
                    startActivity(intent);
                }

            }
        });

        listview_store.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mSelectedStoreId = storeList.get(position).getStoreId();
                mSelectKuanTaiId = "";
                mSelectStoreName = storeList.get(position).getStoreName();
                mSelectKuanTiaName = "";
                storeListAdapter.setSelectedPosition(position);
                storeListAdapter.notifyDataSetChanged();
                getKuanTaiList(mSelectedStoreId);
                kuanTaiListAdapter.setSelectedPosition(-1);
                kuanTaiListAdapter.notifyDataSetChanged();
                mIsKuanTaiSelected = false;
            }
        });

        listview_kuantai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mSelectKuanTaiId = kuantaiList.get(position).getStoreId();
                mSelectKuanTiaName = kuantaiList.get(position).getStoreName();
                kuanTaiListAdapter.setSelectedPosition(position);
                kuanTaiListAdapter.notifyDataSetChanged();
                mIsKuanTaiSelected = true;
            }
        });
    }

    private void getStoreList() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            if (mLoginType.equals("0")) {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            } else {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("shopId"));
                jsonObject.put("storeId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId"));
            }
            jsonObject.put("query", "");
            jsonObject.put("limit", 10000);
            jsonObject.put("start", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.STORE_LIST;
        OkUtils.getInstance().postNoHeader(StoreListActivity.this, url, jsonObject, new MyOkCallback<String>() {
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

                storeList.clear();
                if (dataList != null && dataList.size() != 0) {
                    storeList.addAll(dataList);
                }
                storeListAdapter.addAllList(storeList);
                if (storeList != null && storeList.size() > 0) {
                    mStoreId = storeList.get(0).getStoreId();
                    mStoreName = storeList.get(0).getStoreName();
                    mSelectStoreName = storeList.get(0).getStoreName();
                    mSelectedStoreId = storeList.get(0).getStoreId();
                    storeListAdapter.setSelectedPosition(0);
                    getKuanTaiList(mSelectedStoreId);
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

    private void getKuanTaiList(String storeId) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("storeName", "");
            jsonObject.put("storeId", storeId);
            jsonObject.put("limit", 10000);
            jsonObject.put("start", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.QUERY_CASHIER_LIST;
        OkUtils.getInstance().post(StoreListActivity.this, url, jsonObject, new MyOkCallback<String>() {
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
                String message = response.optString("message");
                if ("0".equals(status)) {
                    String data = response.optString("data");
                    Gson gson = new Gson();

                    List<StoreManageBean.ObjBean> dataList = gson.fromJson(data,
                            new TypeToken<List<StoreManageBean.ObjBean>>() {
                            }.getType());
                    if (mIsKuanTaiListRefresh) {
                        kuantaiList.clear();
                    }

                    if (dataList != null && dataList.size() != 0) {
                        kuantaiList.addAll(dataList);
                    }
                    kuanTaiListAdapter.addAllList(kuantaiList);

                } else {
                    if (TextUtils.isEmpty(message))
                        ToastUtil.show(message);
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
}
