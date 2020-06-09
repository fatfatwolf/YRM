package com.hybunion.yirongma;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.support.api.push.TokenResult;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.activity.LoginActivity;
import com.hybunion.yirongma.payment.bean.CommonBean;
import com.hybunion.yirongma.payment.bean.QueryMerInfoNoneBean;
import com.hybunion.yirongma.payment.bean.QueryTransBean;
import com.hybunion.yirongma.common.util.huawei.HMSAgent;
import com.hybunion.yirongma.common.util.huawei.common.handler.ConnectHandler;
import com.hybunion.yirongma.common.util.huawei.push.handler.EnableReceiveNormalMsgHandler;
import com.hybunion.yirongma.common.util.huawei.push.handler.GetPushStateHandler;
import com.hybunion.yirongma.common.util.huawei.push.handler.GetTokenHandler;
import com.hybunion.yirongma.common.util.jpush.HuaweiPushRevicer;
import com.hybunion.yirongma.common.util.jpush.JPushReceiver;
import com.hybunion.yirongma.payment.activity.NoticeDialog;
import com.hybunion.yirongma.payment.base.BaseFragment;
import com.hybunion.yirongma.payment.receiver.LoginReceiver;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SaveMerInfoUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.view.NoticeDialog2;
import com.hybunion.yirongma.payment.Fragment.LMFAccountFragment;
import com.hybunion.yirongma.payment.Fragment.LMFPaymentFragment;
import com.hybunion.yirongma.payment.Fragment.MessageCenterFragment;
import com.hybunion.yirongma.payment.activity.BussinessInformationActivity;
import com.hybunion.yirongma.payment.activity.NewBillingListActivity;
import com.hybunion.yirongma.payment.base.BaseActivity;
import com.hybunion.yirongma.payment.db.BillingDataListDBManager;
import com.hybunion.yirongma.payment.receiver.HeadSetReceiver;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.hybunion.yirongma.payment.view.engine.PushStartService;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFUserInfo;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * @author xjz
 * @date 2019/11/6
 * @email freemars@yeah.net
 * @description 保持信息与极光注册放到首页处理
 */
public class LMFMainActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_payment, tv_account, tv_message_center;
    private ImageView iv_payment, iv_account, iv_service;
    private View view;
    private String noticeContent;
    private String noticeTitle;
    //在 Activity-onCreate()方法中传回的 Intent,来判断启动来源
    private List<Fragment> mFragments = new ArrayList<>();
    private String loginType;
    BroadcastReceiver broadcastReceiver = new LMFMainActivity.Broadcast();
    /**
     * 实例
     */
    private String merchantID,storeId,loginName,UID;
    String noticeId;
    private int emuiApiLevel = 0;
    private String token = "";
    BroadcastReceiver INSTANCE;
    private TextView mTvDot;  // 服务中心上面的红点
    private String IsReadProtocol;//判断是否阅读协议
    private FragmentManager mFragmentManager;
    private LMFPaymentFragment mPaymentFragment;
    private MessageCenterFragment mMessageCenterFragment;
    private LMFAccountFragment mAccountFragment;
    private BaseFragment mShowFragment; // 当前 show 的 Fragment
    String intentType;//1为登录跳转过来 2 为动画跳转过来

    private static final String APP_TYPE = "appType"; //app类型：0-会员端 1-商户端
    private static final String DEVICE_TOKEN = "deviceToken"; //此处为向极光注册获得的注册ID
    private static final String OS_TYPE = "osType"; //系统类型：0-android 1-ios
    private static final String USERID = "userId"; //会员或者商户的id(待确定)
    @Override
    protected int getContentView() {
        return R.layout.activity_lmf_main;
    }

    @Override
    public void initView() {
        HRTApplication.getInstance().addActivity(this);
        loginType = SharedPreferencesUtil.getInstance(LMFMainActivity.this).getKey("loginType");
        merchantID = SharedPreferencesUtil.getInstance(LMFMainActivity.this).getKey(SharedPConstant.MERCHANT_ID);
        IsReadProtocol = SharedPreferencesUtil.getInstance(LMFMainActivity.this).getKey(SharedPConstant.IS_READ_PROTOCOL);
        loginName = SharedPreferencesUtil.getInstance(LMFMainActivity.this).getKey(SharedPConstant.LOGIN_NUMBER);
        UID = SharedPreferencesUtil.getInstance(LMFMainActivity.this).getKey(SharedPConstant.UID);
        iv_service = findViewById(R.id.iv_service);
        tv_message_center = findViewById(R.id.tv_message_center);
        findViewById(com.hybunion.yirongma.R.id.rl_payment).setOnClickListener(this);
        findViewById(R.id.rl_service_center).setOnClickListener(this);
        findViewById(com.hybunion.yirongma.R.id.rl_account).setOnClickListener(this);
        tv_payment = (TextView) findViewById(com.hybunion.yirongma.R.id.tv_payment);
        tv_account = (TextView) findViewById(com.hybunion.yirongma.R.id.tv_account);
        iv_payment = (ImageView) findViewById(com.hybunion.yirongma.R.id.iv_payment);
        iv_account = (ImageView) findViewById(com.hybunion.yirongma.R.id.iv_account);
        mTvDot = (TextView) findViewById(R.id.numDot_main_activity);
        intentType = getIntent().getStringExtra("intentType");
        initFragments();
        setTabSelection(com.hybunion.yirongma.R.id.rl_payment);
        if ("0".equals(loginType)) {
            if ("Y".equals(IsReadProtocol)) {

            } else {//没有阅读协议，进行弹框
                final NoticeDialog2 noticeDialog2 = new NoticeDialog2(LMFMainActivity.this);
                noticeDialog2.show();
                noticeDialog2.setButtonClickListener(new NoticeDialog2.OnButtonClickListener() {
                    @Override

                    public void onButtonClick() {
                        Intent intent = new Intent(LMFMainActivity.this, BussinessInformationActivity.class);
                        startActivity(intent);
                        getReadprotocol();
                    }
                });
            }
        }
        noticeId = com.hybunion.yirongma.payment.utils.SharedPreferencesUtil.getInstance(this).getKey("noticeId");

        INSTANCE = new HeadSetReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("android.intent.action.HEADSET_PLUG");
        // 或者使用Intent.ACTION_HEADSET_PLUG
        this.registerReceiver(INSTANCE, filter1);
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(INSTANCE, bluetoothFilter);
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            if (TextUtils.isEmpty((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}))) {
                emuiApiLevel = 0;
            } else {
                emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(emuiApiLevel>10){
            // 华为推送回调
            HuaweiPushRevicer.registerPushCallback(new HuaweiPushRevicer.IPushCallback() {
                @Override
                public void onReceive(Intent intent) {
                    if (intent == null) return;
                    Log.d("huawei111", "执行 回调 ！！！！");
                    String action = intent.getAction();
                    if (HuaweiPushRevicer.ACTION_UPDATEUI.equals(action)) {
                        QueryTransBean.DataBean bean = (QueryTransBean.DataBean) intent.getSerializableExtra("dataBean");
                        if (bean == null) return;
                        // 保证 订单金额、订单号、交易时间都有，才插入数据库
                        if (!TextUtils.isEmpty(bean.transAmount) && !TextUtils.isEmpty(bean.orderNo) && !TextUtils.isEmpty(bean.transTime)){
                            Activity activity = HRTApplication.list.get(HRTApplication.list.size() - 1);
                            if (activity instanceof NewBillingListActivity) {  // 当前在列表页
                                NewBillingListActivity billingListActivity = (NewBillingListActivity) activity;
                                billingListActivity.handlePush("2", bean);
                            } else {  // 当前不在列表页，只将数据插入数据库
                                if (bean.isHuiChuZhi){
                                    BillingDataListDBManager.getInstance(LMFMainActivity.this).insertHuiChuZhiData(bean,null);
                                }else{
                                    BillingDataListDBManager.getInstance(LMFMainActivity.this).insertData(bean);
                                }
                            }
                        }
                    }
                }
            });
        }

        // 注册 接到极光推送的广播，插入数据库。
        registerJPushReceiver();
        // 注册登录过期广播
        registerLoginReceiver();

    }
    @Override
    public void initData() {
        if("2".equals(intentType)){
            getSuecessMsg();
        }
        checkUpdate();
        getLMFNotice();
    }
    @Override
    protected void loadData() {
    }
    private LoginReceiver mLoginReceiver;
    private void registerLoginReceiver() {
        mLoginReceiver = new LoginReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(OkUtils.getInstance().loginAction);
        registerReceiver(mLoginReceiver, filter);
    }
    // 查询商户信息 并保存本地
    private void getSuecessMsg(){
        String url = NetUrl.QUERY_MERINFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UID", UID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(LMFMainActivity.this, url, jsonObject, new MyOkCallback<QueryMerInfoNoneBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(QueryMerInfoNoneBean queryMerInfoNoneBean) {
                String status = queryMerInfoNoneBean.getStatus();
                String loginType = queryMerInfoNoneBean.getLoginType();
                if (status.equals("0")) {
                    merchantID = queryMerInfoNoneBean.getMerchantID();
                    storeId = queryMerInfoNoneBean.getStoreId();
                    CrashReport.putUserData(LMFMainActivity.this, "merchantID", merchantID);
                    CrashReport.putUserData(LMFMainActivity.this, "loginName", loginName);
                    CrashReport.setUserId("BOSS");
                    Constant.AGENT_ID = TextUtils.isEmpty(queryMerInfoNoneBean.getAgentId()) ? 0 : Integer.parseInt(queryMerInfoNoneBean.getAgentId());

                    // 保存用户数据
                    SaveMerInfoUtil.saveAll(LMFMainActivity.this,queryMerInfoNoneBean);

                    //用户登录成功，将用户的别名和标签向极光初始化
                    //将用户信息上传，作为推送的依据(向公司服务器上传，同一用户只上传一次)
                    String registrationID = JPushInterface.getRegistrationID(HRTApplication.getInstance());
                    Log.i("xjz--registrationID", registrationID + "");
                    pushJPushToServer(registrationID);
                    registerHWorJG();//注册极光或者华为

                    // 设置客服用信息
                    if ("0".equals(loginType)) {
                        setCustomerInfo(queryMerInfoNoneBean.getMerchantName(), queryMerInfoNoneBean.getMerchantID(), loginName, queryMerInfoNoneBean.getMid());
                    } else {
                        setCustomerInfo(queryMerInfoNoneBean.getStoreName(), queryMerInfoNoneBean.getMerchantID(), loginName, queryMerInfoNoneBean.getStoreId());
                    }
                }else if (!"-99".equals(status)){
                    ToastUtil.show("信息请求失败，请重新登录");
                    Intent intent = new Intent(LMFMainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    HRTApplication.finishAllExceptActivity(LoginActivity.class);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳，信息请求失败，请重新登录");
                Intent intent = new Intent(LMFMainActivity.this, LoginActivity.class);
                startActivity(intent);
                HRTApplication.finishAllExceptActivity(LoginActivity.class);
            }

            @Override
            public void onFinish() {
            }

            @Override
            public Class getClazz() {
                return QueryMerInfoNoneBean.class;
            }
        });
    }


    public void registerHWorJG(){


        if (emuiApiLevel > 10) {//华为手机

            HMSAgent.Push.getToken(new GetTokenHandler() {
                @Override
                public void onResult(int rst, TokenResult result) {
                    if (result != null) {
                        if (result.getTokenRes() != null) {
                            token = result.getTokenRes().getToken();
                            Log.i("xjz", "token是否成功 " + rst);
                        }
                    }

                }

            });
            /**
             * 获取push状态 | Get Push State
             */
            HMSAgent.Push.getPushState(new GetPushStateHandler() {
                @Override
                public void onResult(int rst, TokenResult result) {
                    Log.i("xjz", "getPushState:end code=" + rst);
                    if(0!=rst){//状态未连接上，调华为注册
                        HMSAgent.connect(LMFMainActivity.this, new ConnectHandler() {
                            @Override
                            public void onConnect(int rst) {
                                Log.i("xjz", "HMS connect end:" + rst);
                                Log.i("huawei111", "HMS connect end:" + rst);
                                if (rst != 0) {
                                    boolean connected = JPushInterface.getConnectionState(LMFMainActivity.this);
                                    if(!connected){
                                        if (JPushInterface.isPushStopped(LMFMainActivity.this)) {
                                            JPushInterface.resumePush(getApplicationContext());
                                        }else {
                                            // 使用服务向极光注册
                                            startService(new Intent(LMFMainActivity.this, PushStartService.class));
                                        }
                                    }

                                    SharedPreferencesUtil.getInstance(LMFMainActivity.this).putKey(SharedPConstant.HUAWEI_IS_REGISTE, "失败");
                                } else {
                                    JPushInterface.stopPush(LMFMainActivity.this);
                                    Log.i("xjz", "注销了极光推送");
                                    SharedPreferencesUtil.getInstance(LMFMainActivity.this).putKey(SharedPConstant.HUAWEI_IS_REGISTE, "成功");
                                }
                            }
                        });
                    }else {//判断极光有没有连接，有则stop
                        boolean connected = JPushInterface.getConnectionState(LMFMainActivity.this);
                        if(connected){
                            JPushInterface.stopPush(LMFMainActivity.this);
                        }
                    }
                }
            });
            /**
             * 设置是否接收普通透传消息 | Set whether to receive normal pass messages
             * @param enable 是否开启 | enabled or not
             */
            HMSAgent.Push.enableReceiveNormalMsg(true, new EnableReceiveNormalMsgHandler() {
                @Override
                public void onResult(int rst, TokenResult result) {
                    Log.i("xjz", "enableReceiveNormalMsg:end code=" + rst);
                }
            });

        } else {//使用极光注册
            boolean connected = JPushInterface.getConnectionState(LMFMainActivity.this);
            if(!connected){
                if (JPushInterface.isPushStopped(LMFMainActivity.this)) {
                    JPushInterface.resumePush(getApplicationContext());
                }
                Log.i("xjz","主页面极光开始注册");
                // 使用服务向极光注册
                startService(new Intent(LMFMainActivity.this, PushStartService.class));
            }else {
                Log.i("xjz","主页面极光连接状态成功");
            }
        }


    }


    /**
     * 向服务器上传极光推送的相关信息
     */
    private void pushJPushToServer(String registrationId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(APP_TYPE, "1");
            jsonObject.put(DEVICE_TOKEN, registrationId);
            jsonObject.put(OS_TYPE, "0");
            if (("0").equals(loginType)) {
                jsonObject.put(USERID, merchantID);
            } else {
                jsonObject.put(USERID, storeId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.KEEP_DEVICE_TOKEN;
        OkUtils.getInstance().postNoHeader(LMFMainActivity.this, url, jsonObject, new MyOkCallback<CommonBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(CommonBean bean) {
                if ("0".equals(bean.getStatus())) {
                    Log.d("xjz", "向服务器上传极光rid 成功!");
                } else {
                    Log.d("xjz", "向服务器上传极光rid 失败!");
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d("xjz", "向服务器上传极光信息 失败!");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return CommonBean.class;
            }
        });
    }

    // 将用户信息传给客服
    private void setCustomerInfo(String merchantName, String merchantId, String userName, String mid) {
        YSFUserInfo userInfo = new YSFUserInfo();
        userInfo.userId = merchantId;
        userInfo.data =
                "[{ \"key\": \"real_name\", \"value\": " + merchantName + "}," +
                        "{ \"key\": \"mobile_phone\", \"value\": " + userName + "}," +
                        "{ \"key\": \"email\", \"value\": " + mid + "}," +
                        "{ \"key\": \"appName_version\", \"value\": " + YrmUtils.getAppName(this) + "-安卓-" + YrmUtils.getVersionName(this) + "}]";
        Unicorn.setUserInfo(userInfo);
    }

    // 注册 接到极光推送的广播，插入数据库。
    private UpdateDataReceiver mUpdateReceiver;

    private void registerJPushReceiver() {
        mUpdateReceiver = new UpdateDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(JPushReceiver.PUSH_UPDATE_DATA_RECEIVER);
        registerReceiver(mUpdateReceiver, filter);
    }

    /**
     * 收到推送，插入数据库
     */
    public class UpdateDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            if (intent != null) {
                QueryTransBean.DataBean bean = (QueryTransBean.DataBean) intent.getSerializableExtra(JPushReceiver.DATA_RECEIVER_KEY);
                if (bean == null) return;
                Activity activity = HRTApplication.list.get(HRTApplication.list.size() - 1);
                if (activity instanceof NewBillingListActivity) {  // 当前在列表页
                    NewBillingListActivity billingListActivity = (NewBillingListActivity) activity;
                    billingListActivity.handlePush("1",bean);
                } else {  // 当前不在列表页，只将数据插入数据库
                    // transType 不为空，则为 惠储值 数据
                    if (bean.isHuiChuZhi){
                        BillingDataListDBManager.getInstance(LMFMainActivity.this).insertHuiChuZhiData(bean,null);
                    }else{
                        BillingDataListDBManager.getInstance(LMFMainActivity.this).insertData(bean);
                    }

                }
            }
        }

    }
    /**
     * 检查更新
     */
    private void checkUpdate() {
        String version = YrmUtils.getVersionName(this);
        if (version != null) {
            queryLastApkVersion(version);
        }
    }

    /**
     * 获取最新版本号
     *
     * @param version
     */
    private void queryLastApkVersion(final String version) {
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            jsonRequest.put("version_no", HRTApplication.versionName);
            jsonRequest.put("appType", HRTApplication.getInstance().getApplicationContext().getResources().getString(R.string.appType));
            jsonRequest.put("os", "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().postNoHeader(LMFMainActivity.this, NetUrl.QUERY_APK_VERSION, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String string) {
                Log.i("lyj", string + "更新返回信息");
                JSONObject response = null;
                try {
                    response = new JSONObject(string);
                    String status = response.getString("status");
                    if (status.equals("0")) {
                        String lastVersion = response.getString("apkversion");
                        if (lastVersion.contains(".")) {
                            lastVersion = lastVersion.replace(".", "");
                        }
                        String localVersion = "";
                        if (version.contains(".")) {
                            localVersion = version.replace(".", "");
                        }

                        if (Integer.parseInt(lastVersion) > Integer.parseInt(localVersion)) {
                            //有新版本
                            String updateUrl = response.getString("url");
                            String updateDesc = response.getString("versionDesc");
                            String isForceUpdate = response.optString("isForceUpdate");
                            showUpdateDailog(updateUrl, updateDesc, isForceUpdate);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

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

    public void getReadprotocol() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merchantID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(LMFMainActivity.this, NetUrl.ISREADPROCTO, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String response) {//操作结果无须处理
                LogUtils.d(response + "返回数据");
            }

            @Override
            public void onError(Exception e) {

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

    private void initFragments() {
        if (mFragmentManager == null)
            mFragmentManager = this.getSupportFragmentManager();

        mPaymentFragment = new LMFPaymentFragment();
        mMessageCenterFragment = new MessageCenterFragment();
        mAccountFragment = new LMFAccountFragment();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.add(R.id.fragment_parent_lmf_main_activity, mPaymentFragment, "LMFPaymentFragment");
        ft.add(R.id.fragment_parent_lmf_main_activity, mMessageCenterFragment, "MessageCenterFragment");
        ft.add(R.id.fragment_parent_lmf_main_activity, mAccountFragment, "LMFAccountFragment");
        ft.show(mPaymentFragment).hide(mMessageCenterFragment).hide(mAccountFragment);
        mShowFragment = mPaymentFragment;
        ft.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("finish");
        this.registerReceiver(this.broadcastReceiver, filter);

    }

    /**
     * 获取提示用户公告内容
     */
    private void getLMFNotice() {
        String url = NetUrl.LMF_NOTICE;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("platForm", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(LMFMainActivity.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String string) {
                JSONObject response;
                try {
                    response = new JSONObject(string);
                    String status = response.optString("status");
                        if ("0".equals(status)) {
                            noticeContent = response.getString("content");
                            noticeTitle = response.getString("title");
                            boolean isHint = SharedPreferencesUtil.getInstance(LMFMainActivity.this).getBooleanKey("is_lmf_hint", false);
                            if (!isHint) {
                                String noticeId1 = response.getString("noticeId");
                                com.hybunion.yirongma.payment.utils.SharedPreferencesUtil.getInstance(LMFMainActivity.this).putKey("noticeId", noticeId1);
                                if (noticeId1.equals(noticeId)) {
                                    Log.i("xjz111--注册广播", "注册");
                                    Intent intent = new Intent();
                                    intent.setAction(LMFPaymentFragment.KEY);
                                    intent.putExtra("key", "1");
                                    sendBroadcast(intent);
                                } else {
                                    ShowNotiDailog(noticeTitle, noticeContent);
                                }
                            }
                        } else {
                            Intent intent = new Intent();
                            intent.setAction(LMFPaymentFragment.KEY);
                            sendBroadcast(intent);
                        }

                } catch (Exception e) {
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


    private void ShowNotiDailog(String title, String content) {
        NoticeDialog dialog = new NoticeDialog(this, title, content);
        dialog.show();
    }

    /**
     * 两次点击返回键，退出应用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 提示更新的对话框
     */
    private void showUpdateDailog(final String updateUrl, String updateDesc, final String isForceUpdate) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LMFMainActivity.this);
        builder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(com.hybunion.yirongma.R.layout.activity_update, null);
        builder.setTitle(getResources().getString(com.hybunion.yirongma.R.string.update_notification));
        TextView textView = view.findViewById(com.hybunion.yirongma.R.id.tv_version_desc);
        textView.setText(getResources().getString(com.hybunion.yirongma.R.string.update_notification_title) + "\n" + (TextUtils.isEmpty(updateDesc) ? "" : updateDesc));
        builder.setView(view);
        builder.setNegativeButton(isForceUpdate.equals("0") ? "退出" : "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (isForceUpdate.equals("0")) {
                    HRTApplication.getInstance().finishAllActivities();
                    //System.exit(0);
                    android.os.Process.killProcess(android.os.Process.myPid());
                } else {
                    arg0.dismiss();
                    if ("0".equals(SharedPreferencesUtil.getInstance(LMFMainActivity.this).getKey("agentId"))) {
                        //getData();
                    }
                }
            }
        }).setPositiveButton(getResources().getString(com.hybunion.yirongma.R.string.ok), new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                SharedPreferencesUtil.getInstance(LMFMainActivity.this).putKey("gengxin", "1");
                try {
                    if (TextUtils.isEmpty(updateUrl)) return;
                    Uri uri = Uri.parse(updateUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    if ("0".equals(isForceUpdate)) {//强制更新
                        LoginDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show("更新失败");

                }
            }
        });

        builder.create();
        builder.show();
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), getResources().getString(com.hybunion.yirongma.R.string.press_again_to_quit),
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            HRTApplication.getInstance().finishAllActivities();
        }
    }


    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 2:
                    Toast.makeText(getApplicationContext(), getResources().getString(com.hybunion.yirongma.R.string.fail_to_download), Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), getResources().getString(com.hybunion.yirongma.R.string.unable_to_download_without_sdcard), Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }

            isExit = false;
        }
    };
    @Override
    protected void onDestroy() {
        HRTApplication.getInstance().removeActivity(this);
        super.onDestroy();
        if (mUpdateReceiver != null)
            unregisterReceiver(mUpdateReceiver);
        this.unregisterReceiver(broadcastReceiver);

        if (mLoginReceiver != null){
            unregisterReceiver(mLoginReceiver);
        }

        unregisterReceiver(INSTANCE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用于 LMFPaymentFragment 中接收悬浮窗设置权限回调
        mFragments.get(0).onActivityResult(requestCode, resultCode, data);
    }


    private class Broadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    /**
     * 设置底部tab按钮选中状态
     *
     * @param tabId
     */
    private void setTabSelection(int tabId) {
        int color_off = Color.parseColor("#515e81");
        int color_on = Color.parseColor("#FF6633");
        tv_payment.setTextColor(color_off);
        tv_account.setTextColor(color_off);
        tv_message_center.setTextColor(color_off);
//        tv_wallet.setTextColor(color_off);
        iv_payment.setImageResource(com.hybunion.yirongma.R.drawable.img_lmf_gray);
        iv_account.setImageResource(com.hybunion.yirongma.R.drawable.img_account_gray);
        iv_service.setImageResource(R.drawable.serviceuncheck);
        switch (tabId) {
            case com.hybunion.yirongma.R.id.rl_payment:
                tv_payment.setTextColor(color_on);
                iv_payment.setImageResource(com.hybunion.yirongma.R.drawable.img_lmf_red);
                break;
            case com.hybunion.yirongma.R.id.rl_service_center:
                tv_message_center.setTextColor(color_on);
                iv_service.setImageResource(R.drawable.servicecheck);
                break;
            case com.hybunion.yirongma.R.id.rl_account:
                tv_account.setTextColor(color_on);
                iv_account.setImageResource(com.hybunion.yirongma.R.drawable.img_account_red);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View arg0) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        switch (arg0.getId()) {
            case com.hybunion.yirongma.R.id.rl_payment:
                if (mShowFragment instanceof LMFPaymentFragment) {

                } else {
                    ft.hide(mShowFragment).show(mPaymentFragment);
                    setTabSelection(R.id.rl_payment);
                    mShowFragment = mPaymentFragment;
                }
                break;
            case com.hybunion.yirongma.R.id.rl_service_center:
                if (mShowFragment instanceof MessageCenterFragment) {

                } else {
                    ft.hide(mShowFragment).show(mMessageCenterFragment);
                    setTabSelection(R.id.rl_service_center);
                    mShowFragment = mMessageCenterFragment;
                }
                break;
            case com.hybunion.yirongma.R.id.rl_account:
                if (mShowFragment instanceof LMFAccountFragment) {

                } else {
                    ft.hide(mShowFragment).show(mAccountFragment);
                    setTabSelection(R.id.rl_account);
                    mShowFragment = mAccountFragment;
                }
                break;
            default:
                break;
        }
        ft.commit();
    }

    public void setDot(boolean isShowDot, String num) {
        if (isShowDot) {
            mTvDot.setVisibility(View.VISIBLE);
            mTvDot.setText(TextUtils.isEmpty(num) ? "1" : num);
        } else {
            mTvDot.setVisibility(View.GONE);
        }
    }
    public void LoginDialog() {
        LayoutInflater inflater = LMFMainActivity.this.getLayoutInflater();
        View view = inflater.inflate(com.hybunion.yirongma.R.layout.login_dialog, null);
        final Dialog dialog = new Dialog(LMFMainActivity.this, com.hybunion.yirongma.R.style.MyDialogs);
        dialog.setContentView(view);
        dialog.show();
        dialog.setCancelable(false);
    }


}
