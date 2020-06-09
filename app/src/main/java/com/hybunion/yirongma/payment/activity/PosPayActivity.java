package com.hybunion.yirongma.payment.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BaseActivity;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.KuanTaiListAdapter;
import com.hybunion.yirongma.payment.adapter.StoreListAdapter;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.hybunion.yirongma.payment.zxing.activity.CaptureActivity2;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.LocationUtil;
import com.hybunion.yirongma.payment.utils.Keyboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author liuyujia
 * @date 2017/6/14
 * @email freemars@yeah.net
 * @description
 */

public class PosPayActivity extends BasicActivity implements Keyboard.ConfirmListener, View.OnClickListener {
    @Bind(R.id.keyboard)
    Keyboard keyboard;
    @Bind(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.tv_titlebar_back_title)
    TextView tv_title;
    @Bind(R.id.lin_bg_input)
    RelativeLayout lin_bg_input;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.ll_titlebar_back)
    LinearLayout ll_titlebar_back;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    @Bind(R.id.rel_btnbg)
    RelativeLayout rel_btnbg;
    @Bind(R.id.ll_wx)
    LinearLayout ll_wx;
    @Bind(R.id.tvStoreName_lmf_billing_da)
    TextView mTvStoreName; // 门店名称
    @Bind(R.id.selectStoreParent_lmf_billing_da)
    LinearLayout mSelectStoreParent;
    @Bind(R.id.arrow_select_lmf_billing_da)
    ImageView mImgSelectArrow;
    private String mStoreId;
    private String mLoginType; // 0-老板  非0-店长或店员
    KuanTaiListAdapter kuanTaiListAdapter;
    StoreListAdapter storeListAdapter;
    String merchantName, isPartnerMer, city, address;
    String storeId;
    private String mKuanTaiId = "";
    int chooseType;
    int pageStore = 0;
    int pageKuanTai = 0;
    private String mSelectedStoreId;
    private boolean mIsKuanTaiListRefresh = true;
    List<StoreManageBean.ObjBean> storeList = new ArrayList<>();
    public List<StoreManageBean.ObjBean> kuantaiList = new ArrayList<>();
    private static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 100;
    private LocationManager lm;//【位置管理】
    MyBottonPopWindow popWindow;
    int storePosition = 0;
    private String mSelectStoreName, mStoreName, mSelectKuanTaiName, mKuanTaiName, mSelectedKuanTaiId = "", storeName;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE

    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.actitivy_pospay;
    }

    @Override
    public void initView() {
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        merchantName = SharedPreferencesUtil.getInstance(PosPayActivity.this).getKey("merchantName");
        isPartnerMer = SharedPreferencesUtil.getInstance(PosPayActivity.this).getKey("isPartnerMer");

        tv_title.setText(TextUtils.isEmpty(mStoreName) ? "扫码收款" : mStoreName);
        ll_titlebar_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        ll_wx = (LinearLayout) findViewById(R.id.ll_wx);
        Intent intent = getIntent();
        storeListAdapter = new StoreListAdapter(this);
        kuanTaiListAdapter = new KuanTaiListAdapter(this);
        if ("0".equals(mLoginType)) {  // 老板，点击选择门店
            mSelectStoreParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showPopupWindow();
                    getStoreList(true);
                }
            });
        } else if ("1".equals(mLoginType)) {
            storeName = SharedPreferencesUtil.getInstance(this).getKey("storeName");
            mStoreId = SharedPreferencesUtil.getInstance(this).getKey("storeId");
            mSelectStoreParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getStoreList(true);
                }
            });
            mTvStoreName.setText(storeName);
        } else {
            storeName = SharedPreferencesUtil.getInstance(this).getKey("storeName");
            mStoreId = SharedPreferencesUtil.getInstance(this).getKey("storeId");
            mTvStoreName.setText(storeName);
            mImgSelectArrow.setVisibility(View.GONE);
        }

    }

    @Override
    public void initData() {
        keyboard.setConfirmListener(this);
    }

    @Override
    protected void load() {
        super.load();
        if (SharedPreferencesUtil.getInstance(this).getKey("loginType").equals("2")) {

        } else {
            getStoreList(false);
        }
    }


    private void getStoreList(final boolean isClick) {
        String url = NetUrl.STORE_LIST;
        JSONObject jsonObject = new JSONObject();
        try {

            if (mLoginType.equals("0")) {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            } else {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("shopId"));
                jsonObject.put("storeId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId"));
            }
            jsonObject.put("query", "");
            jsonObject.put("limit", 10000);
            jsonObject.put("start", pageStore);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().postNoHeader(PosPayActivity.this, url, jsonObject, new MyOkCallback<String>() {
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
                if (dataList != null) {
                    storeList.addAll(dataList);
                }
                storeListAdapter.addAllList(storeList);
                if (storeList != null) {
                    if (isClick) {//点击获取的门店列表
                        if (popWindow == null) {
                            popWindow = new MyBottonPopWindow(PosPayActivity.this, storeList);
                        }
                        mImgSelectArrow.setImageResource(R.drawable.arrow_up);
                        popWindow.showPopupWindow(storePosition);

                        popWindow.setStoreItemListListener(new MyBottonPopWindow.OnStoreItemListener() {
                            @Override
                            public void setStoreItemListener(int position) {
                                storePosition = position;
                                mSelectedStoreId = storeList.get(position).getStoreId();
                                mSelectedKuanTaiId = "";
                                mSelectStoreName = storeList.get(position).getStoreName();
                                mSelectKuanTaiName = "";
                                getKuanTaiList(mSelectedStoreId, true);
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
                                mStoreId = mSelectedStoreId;
                                mKuanTaiId = mSelectedKuanTaiId;
                                mStoreName = mSelectStoreName;
                                mKuanTaiName = mSelectKuanTaiName;
                                if (TextUtils.isEmpty(mStoreId)) {
                                    mStoreId = storeList.get(0).getStoreId();
                                    mStoreName = storeList.get(0).getStoreName();
                                }
                                if ("0".equals(mLoginType)) {
                                    if (!TextUtils.isEmpty(mKuanTaiName))
                                        mTvStoreName.setText(mStoreName + "   " + mKuanTaiName);
                                    else
                                        mTvStoreName.setText(mStoreName);
                                } else {
                                    if (!TextUtils.isEmpty(mKuanTaiName))
                                        mTvStoreName.setText(storeName + "   " + mKuanTaiName);
                                    else
                                        mTvStoreName.setText(storeName);
                                }
                            }
                        });
                    } else {//第一次进来请求的门店列表
                        mStoreId = storeList.get(0).getStoreId();
                        mStoreName = storeList.get(0).getStoreName();
                        getKuanTaiList(mStoreId, false);
                    }
                } else {
                    hideLoading();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
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


    private void getKuanTaiList(String storeId, final boolean isClick) {
        String url = NetUrl.QUERY_CASHIER_LIST;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeName", "");
            jsonObject.put("storeId", storeId);
            jsonObject.put("limit", 10000);
            jsonObject.put("start", pageKuanTai);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(PosPayActivity.this, url, jsonObject, new MyOkCallback<String>() {
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
                    if (isClick) {//点击进来获取到的款台列表
                        if (dataList != null && dataList.size() != 0) {
                            kuantaiList.addAll(dataList);

                            popWindow.showKuanTaiList(kuantaiList, new MyBottonPopWindow.OnKuanTaiItemListener() {
                                @Override
                                public void setKuanTaiItemListener(int position) {
                                    mSelectedKuanTaiId = kuantaiList.get(position).getStoreId();
                                    mSelectKuanTaiName = kuantaiList.get(position).getStoreName();
                                }
                            });

                        } else {
                            popWindow.showKuanTaiList(kuantaiList, new MyBottonPopWindow.OnKuanTaiItemListener() {
                                @Override
                                public void setKuanTaiItemListener(int position) {
                                    mSelectedKuanTaiId = "";
                                    mSelectKuanTaiName = "";
                                }
                            });
                        }
                    } else {//第一次进来获取到的门店列表
                        if (dataList != null && dataList.size() != 0) {
                            kuantaiList.addAll(dataList);
                            mKuanTaiId = kuantaiList.get(0).getStoreId();
                            mKuanTaiName = kuantaiList.get(0).getStoreName();

                        } else {
                            mKuanTaiId = "";
                            mKuanTaiName = "";
                        }

                        if ("0".equals(mLoginType)) {//是老板
                            if (!TextUtils.isEmpty(mKuanTaiName))
                                mTvStoreName.setText(mStoreName + "   " + mKuanTaiName);
                            else
                                mTvStoreName.setText(mStoreName);
                        } else {//非老板，门店固定
                            if (!TextUtils.isEmpty(mKuanTaiName))
                                mTvStoreName.setText(storeName + "   " + mKuanTaiName);
                            else
                                mTvStoreName.setText(storeName);
                        }
                    }

                } else {
                    if (TextUtils.isEmpty(message))
                        ToastUtil.showShortToast(message);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
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


    @Override
    public void onKeyboardClick() {
        if (YrmUtils.isHavePermissionList(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},YrmUtils.REQUEST_PERMISSION_LOCATION)) {
            isLocationEnabled();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:  // 这个没用貌似
//                if (YrmUtils.isHavePermissionList(this,
//                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                        YrmUtils.REQUEST_PERMISSION_LOCATION));
//                isLocationEnabled();
                break;
            case R.id.ll_titlebar_back:
                PosPayActivity.this.finish();
                break;
        }
    }


    public void locaking() {
        final LocationUtil mLocationUtil = new LocationUtil(PosPayActivity.this);
        mLocationUtil.setOnLocationLitener(new LocationUtil.OnLocationLitener() {
            @Override
            public void onLocationSucc(AMapLocation location) {
                if ("12".equals(location.getErrorCode())) {
                    ToastUtil.showShortToast("请开启定位权限");
                    return;
                } else {
                    city = location.getCity();
                    address = location.getAddress();
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    if ((latitude + "").equals("") || (longitude + "").equals("")) {
                        ToastUtil.showShortToast("获取的经纬度为空，请检查定位设置重新获取");
                        return;
                    }
                    if (!YrmUtils.isHavePermission(PosPayActivity.this, Manifest.permission.CAMERA, YrmUtils.REQUEST_PERMISSION_CAMERA))
                        return;
                    Intent intent3 = new Intent(PosPayActivity.this, CaptureActivity2.class);
                    intent3.putExtra("type", chooseType);
                    intent3.putExtra("amt", keyboard.getAmount());
                    intent3.putExtra("flag", 1);
                    intent3.putExtra("finalFlag", 20);
                    if (TextUtils.isEmpty(mStoreId)) {
                        ToastUtil.showShortToast("获取门店ID失败，请退出重进该界面");
                        return;
                    }
                    if (TextUtils.isEmpty(mKuanTaiId)) {
                        intent3.putExtra("storeId", mStoreId);
                        intent3.putExtra("merName", mStoreName);
                    } else {
                        intent3.putExtra("merName", mKuanTaiName);
                        intent3.putExtra("storeId", mKuanTaiId);
                    }
                    if (TextUtils.isEmpty(city)) {
                        city = location.getDistrict();
                    }
                    intent3.putExtra("city", city);
                    intent3.putExtra("longitude", longitude);
                    intent3.putExtra("latitude", latitude);
                    intent3.putExtra("address", address);
                    startActivity(intent3);
                    PosPayActivity.this.finish();
                }
            }

            @Override
            public void onLocationFail(String error) {
//                LocationDialog();
                ToastUtil.showShortToast("定位失败，请检查权限是否开启");
            }
        });
        mLocationUtil.start(true);
    }

    public void LocationDialog() {
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ok) {
            mPermissionList.clear();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(PosPayActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了

            } else {//请求权限方法
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                ActivityCompat.requestPermissions(PosPayActivity.this, permissions, WRITE_COARSE_LOCATION_REQUEST_CODE);
            }
        } else {
            LayoutInflater inflater = PosPayActivity.this.getLayoutInflater();
            View view = inflater.inflate(R.layout.location_dialog, null);
            final Dialog dialog = new Dialog(PosPayActivity.this, R.style.MyDialogs);
            dialog.setContentView(view);
            Button btn_ok = (Button) view.findViewById(R.id.bt_confirm);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 1315);
                    } catch (Exception e) {
                        ToastUtil.showShortToast("请手动开启定位");
                        e.printStackTrace();
                    } finally {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

    /**
     * 判断定位服务是否开启
     *
     * @param
     * @return true 表示开启
     */
    public boolean isLocationEnabled() {
        // 兼容商米POS，6.0一下，直接调定位
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            locaking();
        } else {
            locaking();
        }

        return true;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == WRITE_COARSE_LOCATION_REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(PosPayActivity.this, permissions[i]);
                    if (showRequestPermission) {
                        ToastUtil.showShortToast( "权限未申请");
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
