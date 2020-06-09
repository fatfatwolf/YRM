package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.baidu.android.common.logging.Log;
import com.huawei.hms.support.api.push.TokenResult;
import com.hybunion.netlibrary.utils.SPUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.SharedUtil;
import com.hybunion.yirongma.common.util.huawei.HMSAgent;
import com.hybunion.yirongma.common.util.huawei.push.handler.DeleteTokenHandler;
import com.hybunion.yirongma.common.util.jpush.JPushUtils;
import com.hybunion.yirongma.common.util.jpush.JpushStatsConfig;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.qiyukf.unicorn.api.Unicorn;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lyj
 * @date 2017/8/28
 * @email freemars@yeah.net
 * @description
 */

public class LMFSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_feedback, ll_about_us, ll_min_account, ll_fix_amount;
    private RelativeLayout llQuit;
    private StringBuffer sb;
    private String address, rePassword, loginType, remarkSwitch;
    private TextView tv_msg;
    private LinearLayout ll_change_password;
    private SharedPreferences sp;
    //    private Switch switchButton;
    private Switch switchRemark;
    private TextView mTvLoginName;
    boolean isClicked = false;
    private String isRemark;

    private RelativeLayout rv_remark;
    View view1, view2;
    private String isForceRemark;
    private LinearLayout merManagePwd_setting_fragment;
    private LinearLayout ll_voice_setting;
    private TextView mTvVoiceSetting;
    private LinearLayout mClearAllData; // 清理缓存（清除数据库所有数据，但保留表结构）
    private Switch mRefreshSwitch;  // 订单实时刷新开关
    private boolean mCanDo = true;
    private TextView mTvCache;
    private LinearLayout mAdminSettingParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lmf_setting);
        ll_change_password = (LinearLayout) findViewById(R.id.ll_change_password);
        ll_change_password.setOnClickListener(this);
        // 如果当前商户是惠买单的话就隐藏“加入惠买单功能”
        ll_about_us = (LinearLayout) findViewById(R.id.ll_about_us);
        ll_about_us.setOnClickListener(this);
        ll_feedback = (LinearLayout) findViewById(R.id.ll_feedback1);
        ll_feedback.setOnClickListener(this);
        llQuit = (RelativeLayout) findViewById(R.id.ll_quit_app);
        llQuit.setOnClickListener(this);
        rv_remark = findViewById(R.id.rv_remark);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        ll_min_account = findViewById(R.id.ll_min_account);
        ll_min_account.setOnClickListener(this);
        ll_fix_amount = findViewById(R.id.ll_fix_amount);
        ll_fix_amount.setOnClickListener(this);
        ll_voice_setting = findViewById(R.id.ll_voice_setting);
        ll_voice_setting.setOnClickListener(this);
        mTvVoiceSetting = findViewById(R.id.voice_setting);
        mTvVoiceSetting.setOnClickListener(this);
        merManagePwd_setting_fragment = (LinearLayout) findViewById(R.id.merManagePwd_setting_fragment);
//        switchButton = (Switch) findViewById(R.id.switch_button);
        switchRemark = findViewById(R.id.switch_remark);
        isForceRemark = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.IS_FORCE_REMARK);
        findViewById(R.id.merManagePwd_setting_fragment).setOnClickListener(this);  //  商户管理密码
        mTvLoginName = (TextView) findViewById(R.id.loginName_setting_fragment);
        mClearAllData = findViewById(R.id.clear_all_data);
        mClearAllData.setOnClickListener(this);
        mRefreshSwitch = findViewById(R.id.refresh_switch);
        mTvCache = findViewById(R.id.tv_cache_setting_activity);
        String loginName = SharedPreferencesUtil.getInstance(this).getKey("loginName");
        mTvLoginName.setText(TextUtils.isEmpty(YrmUtils.handlePhoneNum(loginName)) ? loginName : YrmUtils.handlePhoneNum(loginName));
//        VoiceSwitch = SharedPreferencesUtil.getInstance(LMFSettingActivity.this).getKey("VoiceSwitch");
        remarkSwitch = SharedPreferencesUtil.getInstance(LMFSettingActivity.this).getKey(SharedPConstant.REMARK_SWITCH);
        loginType = SharedPreferencesUtil.getInstance(LMFSettingActivity.this).getKey("loginType");
        rePassword = SharedPreferencesUtil.getInstance(LMFSettingActivity.this).getKey("rePassword");
        findViewById(R.id.voice_setting).setOnClickListener(this);
        mAdminSettingParent = findViewById(R.id.admin_setting_activity);
        mAdminSettingParent.setOnClickListener(this);
        findViewById(R.id.ll_agreement).setOnClickListener(this);
        findViewById(R.id.ll_hide_agreement).setOnClickListener(this);
        // 管理员设置 只有老板显示
        if (!"0".equals(loginType)){
            mAdminSettingParent.setVisibility(View.GONE);
        }

        if (loginType.equals("0") && !TextUtils.isEmpty(rePassword)) {
            merManagePwd_setting_fragment.setVisibility(View.VISIBLE);
        } else {
            merManagePwd_setting_fragment.setVisibility(View.GONE);
        }

        if (loginType.equals("2")) {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.GONE);
            rv_remark.setVisibility(View.GONE);
            ll_fix_amount.setVisibility(View.GONE);
            ll_min_account.setVisibility(View.GONE);
        } else if (loginType.equals("1")) {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            rv_remark.setVisibility(View.GONE);   // 店长也不显示，只有老板显示
            ll_fix_amount.setVisibility(View.VISIBLE);
            ll_min_account.setVisibility(View.GONE);
        } else {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.VISIBLE);
            rv_remark.setVisibility(View.VISIBLE);
            ll_fix_amount.setVisibility(View.VISIBLE);
            ll_min_account.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(isForceRemark) || "1".equals(isForceRemark)) {
            switchRemark.setChecked(false);
        } else {
            switchRemark.setChecked(true);
        }

        switchRemark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                JpushStatsConfig.onCountEvent(LMFSettingActivity.this, "account_setting_forcedRemark", null);
                if (isChecked) {
                    isRemark = "0";
                    remarkUpdate();
                    SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey(SharedPConstant.IS_FORCE_REMARK, "0");
                    ToastUtil.show("您开启了强制添加备注");

                } else {
                    isRemark = "1";
                    remarkUpdate();
                    ToastUtil.show("您关闭了强制添加备注");
                    SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey(SharedPConstant.IS_FORCE_REMARK, "1");
                }
            }
        });

        // 订单实时刷新，默认开启。  0-关闭  1-开启
        String key = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.REFRESH_IS_OPEN);
        if (TextUtils.isEmpty(key) || "1".equals(key)) {  // 开启状态
            mRefreshSwitch.setChecked(true);
        } else {
            mRefreshSwitch.setChecked(false);
        }
        mRefreshSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCanDo)
                    showNoticeDialog(isChecked);
                mCanDo = true;
            }
        });

        String length = YrmUtils.getDBFileLength(this);
        if (!TextUtils.isEmpty(length)) {
            mTvCache.setText(length);
        }

    }

    private Dialog mNoticeDialog;
    private ImageView mImgClose;
    private TextView mTvAccount, mTvContent, mTvButton;

    private void showNoticeDialog(final boolean isOpen) {
        if (mNoticeDialog == null) {
            mNoticeDialog = new Dialog(this);
            mNoticeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mNoticeDialog.setContentView(R.layout.dialog_notice);
        }
        mImgClose = mNoticeDialog.findViewById(R.id.img_close_notice_dialog);
        mTvAccount = mNoticeDialog.findViewById(R.id.tv_account_notice_dialog);
        mTvContent = mNoticeDialog.findViewById(R.id.tv_content_notice_dialog);
        mTvButton = mNoticeDialog.findViewById(R.id.ok_button_notice_dialog);
        mImgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoticeDialog != null) {
                    mNoticeDialog.dismiss();
                    mCanDo = false;
                    mRefreshSwitch.setChecked(!isOpen);
                }
            }
        });
        mTvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoticeDialog.dismiss();
                if (isOpen) {  // 立即开启
                    mRefreshSwitch.setChecked(true);
                    SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey(SharedPConstant.REFRESH_IS_OPEN, "1");
                } else {  // 关闭
                    mRefreshSwitch.setChecked(false);
                    SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey(SharedPConstant.REFRESH_IS_OPEN, "0");
                }
            }
        });

        if (isOpen) {
            mTvAccount.setText("开启提示");
            mTvContent.setText("开启“订单实时刷新”后，无需手动操作，订单信息可实时显示在订单列表中");
            mTvButton.setText("立即开启");
        } else {
            mTvAccount.setText("关闭提示");
            mTvContent.setText("关闭“订单实时刷新”后，如无手动操作，订单信息则无法实时显示。");
            mTvButton.setText("关闭");
        }
        mNoticeDialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_agreement:  // 用户服务协议
                WebViewActivity.start(LMFSettingActivity.this, "用户服务协议", NetUrl.AGREEMENT);
                break;

            case R.id.ll_hide_agreement:  // 隐私政策
                WebViewActivity.start(LMFSettingActivity.this, "隐私政策", NetUrl.PERSONALMENT);
                break;
            case R.id.ll_voice_setting:  // 设置语音开关
            case R.id.voice_setting:
                JpushStatsConfig.onCountEvent(LMFSettingActivity.this, "account_setting_voiceSetting", null);
                startActivity(new Intent(this, VoiceSettingActivity.class));
                break;

            case R.id.ll_quit_app://退出
                showDialog();
                break;
//            case R.id.ll_feedback1://意见反馈
//                UmengEngine.onEvent(this, ConstantField.CLICK_SET_COMMIT_SUGGEST);
//                Intent feedback1 = new Intent(this, NewFeedBackActivity.class);
//                feedback1.putExtra("flag","2");
//                startActivity(feedback1);
//                break;
            case R.id.ll_about_us://关于我们
                JpushStatsConfig.onCountEvent(LMFSettingActivity.this, "account_setting_aboutUs", null);
                Intent aboutUs = new Intent(this, AboutUsActivity.class);
                startActivity(aboutUs);
                break;
            case R.id.ll_change_password:
                JpushStatsConfig.onCountEvent(LMFSettingActivity.this, "account_setting_modifyPass", null);
                Intent intent = new Intent(LMFSettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.merManagePwd_setting_fragment:  // 商户管理密码
                JpushStatsConfig.onCountEvent(LMFSettingActivity.this, "account_setting_merchantPass", null);
                rePassword = SharedPreferencesUtil.getInstance(LMFSettingActivity.this).getKey("rePassword");
                if (rePassword.equals("0")) {
                    startActivity(new Intent(this, MerchantManageActivity.class));
                } else if (rePassword.equals("1")) {
                    startActivity(new Intent(this, NoMerchantManageActivity.class));
                }

                break;
            case R.id.ll_min_account:
                JpushStatsConfig.onCountEvent(this, "account_setting_minMoney", null);
                startActivity(new Intent(this, SetMinAccountActivity.class));
                break;

            case R.id.ll_fix_amount:
                JpushStatsConfig.onCountEvent(this, "account_setting_fixedMoney", null);
                startActivity(new Intent(this, StoreListActivity.class));
                break;
            case R.id.clear_all_data:   // 清理缓存
//                BillingDataListDBManager.getInstance(LMFSettingActivity.this).deleteAll();
                YrmUtils.deleteDb(LMFSettingActivity.this);
                ToastUtil.show("缓存清理成功");
                mTvCache.setText("0KB");
                break;
            case R.id.admin_setting_activity:   // 管理员设置
                startActivity(new Intent(LMFSettingActivity.this, AdminSettingActivity.class));
                break;
            default:
                break;
        }
    }

    private void remarkUpdate() {
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject();
            if (loginType.equals("0")) {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.MERCHANT_ID));
            } else {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.SHOP_ID));
            }
            jsonObject.put("isForceRemark", isRemark);
            jsonObject.put("type", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.REMARK_UPDATE;
        OkUtils.getInstance().post(LMFSettingActivity.this, url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String str) {
                try {
                    JSONObject response = new JSONObject(str);
                    String status = response.getString("status");
                    String message = response.getString("message");
                    if (status.equals("0")) {
                        if (isRemark.equals("0")) {//开启备注
                            SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey(SharedPConstant.REMARK_SWITCH, "1");
                        } else {
                            SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey(SharedPConstant.REMARK_SWITCH, "2");
                        }
                    } else {
                        ToastUtil.show(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                return String.class;
            }
        });

    }

    /**
     * created by lyf 2016.3.30 自定义对话框
     * 确定取消对话框
     */
    private void showDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog, null);
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(view);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText(R.string.sure_to_exit);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Set<String> tags = new HashSet<>();
                //退出时，将各种数据置空
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey("merchantID", "");
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey("shopId", "");
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey("merchantName", "");
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey("landmark", "");
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putBooleanKey("legall_card", false);
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey(SharedPreferencesUtil.AGENT_ID, "0");
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey("mid", "");
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey("exit", "1");
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey("storeId", "");
                SPUtil.putString(SharedPConstant.TOKEN,"");
                SharedUtil.getInstance(LMFSettingActivity.this).putString(Constants.MID, ""); // 清空 mid
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey(SharedPConstant.UID,"");
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putBooleanKey(SharedPConstant.ISAUTO_LOGIN,false);
                // 注销极光
                JPushUtils.removeSetAliasAndTagsCallBack("", tags);
                SharedPreferencesUtil.setNewSP(LMFSettingActivity.this, "alias", "");
                JPushUtils.stopPush();
                Log.d("xjz","极光推送注销");
                // 注销华为
                String huaweiToken = SharedPreferencesUtil.getInstance(LMFSettingActivity.this).getKey(SharedPConstant.HUAWEI_TOKEN);
                if (!TextUtils.isEmpty(huaweiToken))
                    HMSAgent.Push.deleteToken(huaweiToken, new DeleteTokenHandler() {
                        @Override
                        public void onResult(int rst, TokenResult result) {
                            Log.d("xjz","华为推送注销："+rst);
                        }
                    });

                Intent loginActivity = new Intent(LMFSettingActivity.this, LoginActivity.class);
                loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SharedPreferencesUtil.getInstance(LMFSettingActivity.this).putKey(SharedPConstant.transNo, 0);
                startActivity(loginActivity);
                Intent intent = new Intent();//发送广播
                intent.setAction("finish");
                sendBroadcast(intent);
                Unicorn.logout();
                finish();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        HRTApplication hrtApp = (HRTApplication) getApplication();
        hrtApp.activities.remove(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideProgressDialog();
        super.onActivityResult(requestCode, resultCode, data);
    }

}
