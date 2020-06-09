package com.hybunion.yirongma.payment.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.util.jpush.JpushStatsConfig;
import com.hybunion.yirongma.payment.base.BaseFragment;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.activity.ClerkSettingActivity;
import com.hybunion.yirongma.payment.activity.ClerkSettingActivity1;
import com.hybunion.yirongma.payment.activity.MyNewWalletActivity;
import com.hybunion.yirongma.payment.activity.VoiceSettingActivity;
import com.hybunion.yirongma.payment.adapter.KuanTaiListAdapter2;
import com.hybunion.yirongma.payment.adapter.StoreListAdapter2;
import com.hybunion.yirongma.payment.activity.ClerkRecordActivity;
import com.hybunion.yirongma.payment.activity.ClerkWorkActivity;
import com.hybunion.yirongma.payment.activity.LMFSettingActivity;
import com.hybunion.yirongma.payment.activity.BussinessInformationActivity;
import com.hybunion.yirongma.payment.activity.MyWalletActivity;
import com.hybunion.yirongma.payment.activity.StoreManageActivity2;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class LMFAccountFragment extends BaseFragment implements View.OnClickListener {
    private View root_view;
    private String merchantID, mid, msg, status, merchantName, areaType, contactPerson;

    private TextView tv_title;
    private ImageView iv_search;
    private LinearLayout ll_lmf_merchant_info, ll_store_manage, ll_my_account, ll_setting, ll_clerk_account;
    String loginType, storeName;
    private ImageView iv_identify;
    private TextView tv_name;
    private String empName;
    View view1,view5,view3;
    private PopupWindow popupWindow;
    private Button bt_clerk_end;
    private LinearLayout ll_clerk_record;
    private LinearLayout mAgentParent;
    private boolean mHasWallet;
    // 钱包
    private RelativeLayout mWalletParent;
    private TextView mTvWalletBalance;
    private SmartRefreshLayout smartRefresh_layout;
    private String mIsHRTWallet; // 是否是 HRT 钱包商户
    private View mLineView;
    // 员工管理
    private LinearLayout mStaffManageParent;
    List<StoreManageBean.ObjBean> storeList = new ArrayList<>();
    StoreListAdapter2 storeListAdapter;
    private String mSelectedKuanTaiId = "";
    private String storeId = "";
    private String mStoreName,mKuanTaiName;
    private String mSelectStoreName,mSelectKuanTaiName;
    private LinearLayout mllVoice; // 收款语音设置入口。


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = inflater.inflate(R.layout.fragment_lmfaccount_fragment2, null);
            initView(root_view);
        } else {
            ViewGroup viewGroup = ((ViewGroup) root_view.getParent());
            if (viewGroup != null) {
                viewGroup.removeView(root_view);
            }
        }
        return root_view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 需要判断有没有钱包，然后再调接口
        if ("0".equals(mIsHRTWallet))
            queryWallet();
    }

    private void initView(final View root_view) {
        merchantName = SharedPreferencesUtil.getInstance(getActivity()).getKey("merchantName");
        empName = SharedPreferencesUtil.getInstance(getActivity()).getKey("empName");
        loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
        storeName = SharedPreferencesUtil.getInstance(getActivity()).getKey("storeName");
        contactPerson = SharedPreferencesUtil.getInstance(getActivity()).getKey("contactPerson");
        areaType = SharedPreferencesUtil.getInstance(getActivity()).getKey(Constants.AREATYPE);
        view5 = root_view.findViewById(R.id.view5);
        view3 = root_view.findViewById(R.id.view3);
        tv_title = root_view.findViewById(R.id.tv_title);
        tv_name = root_view.findViewById(R.id.tv_name);
        bt_clerk_end = root_view.findViewById(R.id.bt_clerk_end);
        iv_identify = root_view.findViewById(R.id.iv_identify);
        ll_my_account = root_view.findViewById(R.id.ll_my_account);
        ll_clerk_account = root_view.findViewById(R.id.ll_clerk_account);
        ll_setting = root_view.findViewById(R.id.ll_setting);
        view1 = root_view.findViewById(R.id.view1);
        ll_store_manage = root_view.findViewById(R.id.ll_store_manage);
        ll_lmf_merchant_info = root_view.findViewById(R.id.ll_lmf_merchant_info);
        ll_clerk_record = root_view.findViewById(R.id.ll_clerk_record);
        mAgentParent = root_view.findViewById(R.id.agent_lmfaccount_fragment2);
        mWalletParent = root_view.findViewById(R.id.newWalletParent_lmfaccount_fragment2);
        mWalletParent.setOnClickListener(this);
        mTvWalletBalance = root_view.findViewById(R.id.walletBalance_lmfaccount_fragment2);
        mLineView = root_view.findViewById(R.id.line_lmfaccount_fragment2);
        mStaffManageParent = root_view.findViewById(R.id.ll_staff_manage);
        mllVoice = root_view.findViewById(R.id.voice_lmfaccount_fragment2);
        mllVoice.setOnClickListener(this);
//        storeListAdapter = new StoreListAdapter2(getActivity());
        kuanTaiListAdapter = new KuanTaiListAdapter2(getActivity());
        mIsHRTWallet = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.IS_HRT_WALLET);
//        mIsHRTWallet = "0";
        if ("0".equals(mIsHRTWallet)) {
            mWalletParent.setVisibility(View.VISIBLE); // 显示钱包功能
            mLineView.setVisibility(View.VISIBLE);
        }

        if ("0".equals(mIsHRTWallet))
            queryWallet();

        smartRefresh_layout = root_view.findViewById(R.id.smartRefresh_layout);
        smartRefresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if ("0".equals(mIsHRTWallet)) {
                    queryWallet();
                } else {
                    smartRefresh_layout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            smartRefresh_layout.setEnableRefresh(false);
                        }
                    }, 1000);
                }
            }
        });

        if ("0".equals(loginType)) {
            tv_title.setText(merchantName);
            tv_name.setText(contactPerson);
            iv_identify.setImageResource(R.drawable.img_boss);
            view5.setVisibility(View.GONE);
            view3.setVisibility(View.VISIBLE);
        } else if ("1".equals(loginType)) {
            tv_title.setText(storeName);
            tv_name.setText(empName);
            view1.setVisibility(View.GONE);
            ll_my_account.setVisibility(View.VISIBLE);
            ll_lmf_merchant_info.setVisibility(View.GONE);
            iv_identify.setImageResource(R.drawable.img_shop_manager);
        } else if ("2".equals(loginType)) {
            tv_title.setText(storeName);
            ll_my_account.setVisibility(View.GONE);
            ll_lmf_merchant_info.setVisibility(View.GONE);
            ll_store_manage.setVisibility(View.GONE);
            mStaffManageParent.setVisibility(View.GONE);
            tv_name.setText(empName);
            view5.setVisibility(View.VISIBLE);
            view3.setVisibility(View.GONE);

            ll_clerk_account.setVisibility(View.VISIBLE);
            iv_identify.setImageResource(R.drawable.img_shop_assistant);
        }


        ll_setting.setOnClickListener(this);
        bt_clerk_end.setOnClickListener(this);
        merchantID = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        mid = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("mid");

        ll_store_manage.setOnClickListener(this);
        mStaffManageParent.setOnClickListener(this);
        ll_lmf_merchant_info.setOnClickListener(this);
        ll_clerk_record.setOnClickListener(this);
        mHasWallet = "Y".equals(SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.has_wallet));
        if (mHasWallet) {
            mAgentParent.setVisibility(View.VISIBLE);
            mAgentParent.setOnClickListener(this);
        } else {
            mAgentParent.setVisibility(View.GONE);
        }
    }

    MyBottonPopWindow popWindow;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.voice_lmfaccount_fragment2:   // 收款语音设置入口
                startActivity(new Intent(getActivity(),VoiceSettingActivity.class));
                break;
            case R.id.ll_lmf_merchant_info:
                JpushStatsConfig.onCountEvent(getActivity(),"account_home_merchantInfo",null);
                if ("0".equals(loginType)) {//总店
                    startActivity(new Intent(getActivity(), BussinessInformationActivity.class));
                } else {//分店
                    Toast.makeText(getActivity(), "没有权限查看", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_store_manage:
//                showPopupWindow();
                JpushStatsConfig.onCountEvent(getActivity(),"account_home_storeManager",null);
                startActivity(new Intent(getActivity(), StoreManageActivity2.class));
                break;
            case R.id.ll_setting:
                JpushStatsConfig.onCountEvent(getActivity(),"account_home_setting",null);
                startActivity(new Intent(getActivity(), LMFSettingActivity.class));
                break;
            case R.id.bt_clerk_end:
                Intent intent = new Intent(getActivity(), ClerkWorkActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_clerk_record:
                Intent intent1 = new Intent(getActivity(), ClerkRecordActivity.class);
                startActivity(intent1);
                break;
            case R.id.agent_lmfaccount_fragment2:   // 代理商计划入口
                // 首先弹框提示声明，点击确定进入下一步，点击空白处取消弹框，不做任何操作。
                JpushStatsConfig.onCountEvent(getActivity(),"account_home_agent",null);
                showBalanceDialog();
                break;
            case R.id.newWalletParent_lmfaccount_fragment2:  // 钱包入口
                JpushStatsConfig.onCountEvent(getActivity(),"account_home_wallet",null);
                startActivity(new Intent(getActivity(), MyNewWalletActivity.class));
                break;
            case R.id.ll_staff_manage:  // 员工管理 入口
                JpushStatsConfig.onCountEvent(getActivity(),"account_home_employeesManager",null);
                getStoreList();
                break;

        }
    }

    private String mSelectedStoreId;
    private String kuanTaiId;
    KuanTaiListAdapter2 kuanTaiListAdapter;

    public List<StoreManageBean.ObjBean> kuantaiList = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getKuanTaiList(String storeId) {
        JSONObject body = new JSONObject();
        try {
            body.put("storeName", "");
            body.put("storeId", storeId);
            body.put("limit", 10000);
            body.put("start", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), NetUrl.QUERY_CASHIER_LIST, body, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(StoreManageBean bean) {
                String status = bean.getStatus();
                String msg = bean.getMessage();
                if ("0".equals(status)) {
                    List<StoreManageBean.ObjBean> dataList = bean.getData();
                    kuantaiList.clear();
                    if (!YrmUtils.isEmptyList(dataList)) {
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
                } else {
                    if (!TextUtils.isEmpty(msg)) {
                        ToastUtil.showShortToast(msg);
                    } else {
                        ToastUtil.showShortToast("网络连接不佳");
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return StoreManageBean.class;
            }
        });

    }
    private void getStoreList() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (loginType.equals("0")) {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            } else {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("shopId"));
                jsonObject.put("storeId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId"));
            }
            jsonObject.put("query", "");
            jsonObject.put("limit", 10000);
            jsonObject.put("start", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().postNoHeader(getActivity(), NetUrl.STORE_LIST, jsonObject, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(StoreManageBean bean) {
                List<StoreManageBean.ObjBean> dataList = bean.getData();
                storeList.clear();
                if (!YrmUtils.isEmptyList(dataList)) {
                    storeList.addAll(dataList);
                }
                if (storeList != null) {
                    if (getActivity().isFinishing()) return;
                    popWindow = new MyBottonPopWindow(getActivity(), storeList);
                    popWindow.showPopupWindow(-1);
                    popWindow.setStoreItemListListener(new MyBottonPopWindow.OnStoreItemListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void setStoreItemListener(int position) {
                            mSelectedStoreId = storeList.get(position).getStoreId();
                            mSelectedKuanTaiId = "";
                            mSelectStoreName = storeList.get(position).getStoreName();
                            getKuanTaiList(mSelectedStoreId);
                        }
                    });
                    popWindow.setButtonClickListener(new MyBottonPopWindow.OnSureClickListener() {
                        @Override
                        public void setButtonClickListener() {
                            storeId = mSelectedStoreId;
                            kuanTaiId = mSelectedKuanTaiId;
                            mStoreName = mSelectStoreName;
                            mKuanTaiName = mSelectKuanTaiName;
                            if (TextUtils.isEmpty(storeId)) {
                                ToastUtil.showShortToast("还未选择门店");
                                return;
                            }
                            if (TextUtils.isEmpty(kuanTaiId)) {  // 只选择了门店，没有选择款台，跳转门店的员工管理列表
                                Intent intent = new Intent(getActivity(), ClerkSettingActivity.class);
                                intent.putExtra("type", "1");
                                intent.putExtra("storeName", mStoreName);
                                intent.putExtra("storeId", storeId);
                                startActivity(intent);

                            } else {  // 既选择了门店，也选择了款台，跳转 款台的 员工管理列表
                                Intent intent = new Intent(getActivity(), ClerkSettingActivity1.class);
                                intent.putExtra("type", "2");
                                intent.putExtra("storeName", mKuanTaiName);
                                intent.putExtra("mStoreId", storeId);
                                intent.putExtra("storeId", kuanTaiId);
                                startActivity(intent);

                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }
            @Override
            public void onFinish() {
                hideProgressDialog();
            }
            @Override
            public Class getClazz() {
                return StoreManageBean.class;
            }
        });

    }


    private Dialog mBalanceDialog;
    private void showBalanceDialog() {
        if (mBalanceDialog == null) {
            mBalanceDialog = new Dialog(getActivity());
            mBalanceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mBalanceDialog.setContentView(R.layout.dialog_my_wallet_fragment);
            TextView tvAccount = (TextView) mBalanceDialog.findViewById(R.id.tv_account_my_wallet_dialog);
            TextView tvContent = (TextView) mBalanceDialog.findViewById(R.id.tv_content_my_wallet_dialog);
            tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
            TextView okButton = (TextView) mBalanceDialog.findViewById(R.id.ok_button_my_wallet_dialog);
            okButton.setText("确定");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), MyWalletActivity.class));
                    if (mBalanceDialog != null)
                        mBalanceDialog.dismiss();
                }
            });
            tvAccount.setText("代理“商”计划奖励声明");
            String title = getResources().getString(R.string.app_name);
            String companyName = getResources().getString(R.string.company_name);
            tvContent.setText("代理“商”计划奖励是"+title+"对商户的相关人员提供的额外奖励，与商户正常的门店交易无关，不会因为代理“商”计划奖励导致商户的正常交易金额异常。本活动相关声明版权及其修改权、更新权和最终解释权均属"+companyName+"所有。");

        }
        mBalanceDialog.show();

    }




    // 查询钱包余额
    private void queryWallet() {
        JSONObject dataParam = new JSONObject();
        try {
            dataParam.put("mid", SharedPreferencesUtil.getInstance(getActivity()).getKey("mid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(getActivity(), NetUrl.WALLET_NEW_BALANCE, dataParam, new MyOkCallback<String>() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject response = new JSONObject(o);
                    if (response != null) {
                        try {
                            String message = response.getString("message");
                            String status = response.getString("status");
                            if ("0".equals(status)) {
                                String balance = response.getString("balance");
                                String cashStatus = response.getString("cashstatus"); // 是否开通实时到账(需要审核) 0-审核成功  1-审核失败  2-审核中
                                if (TextUtils.isEmpty(cashStatus)) cashStatus = "1";  // 默认未开通
                                SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.IS_SHISHI,cashStatus);
                                if (TextUtils.isEmpty(balance) || "null".equals(balance))
                                    mTvWalletBalance.setText("0.00");
                                else{
                                    if (balance.matches("[0-9]+") || balance.matches("[0-9]+[.][0-9]+")){
                                        double balanceD = Double.parseDouble(balance);
                                        DecimalFormat df = new DecimalFormat("0.00");
                                        mTvWalletBalance.setText(df.format(balanceD));
                                    }else{
                                        mTvWalletBalance.setText("0.00");
                                    }
                                }
                            } else {
                                if (!TextUtils.isEmpty(message))
                                    ToastUtil.showShortToast(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        ToastUtil.showShortToast("网络连接不佳");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShortToast("网络连接不佳");
                }
            }
            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }
            @Override
            public void onFinish() {
            }
            @Override
            public Class getClazz() {
                return String.class;
            }
        });
    }
}

