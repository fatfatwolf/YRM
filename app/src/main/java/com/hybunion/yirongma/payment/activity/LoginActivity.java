package com.hybunion.yirongma.payment.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.hms.support.api.push.TokenResult;
import com.hybunion.net.utils.LogUtil;
import com.hybunion.netlibrary.utils.SPUtil;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.BuildConfig;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.util.huawei.HMSAgent;
import com.hybunion.yirongma.common.util.huawei.common.handler.ConnectHandler;
import com.hybunion.yirongma.common.util.huawei.push.handler.EnableReceiveNormalMsgHandler;
import com.hybunion.yirongma.common.util.huawei.push.handler.GetPushStateHandler;
import com.hybunion.yirongma.common.util.huawei.push.handler.GetTokenHandler;
import com.hybunion.yirongma.payment.bean.CommonBean;
import com.hybunion.yirongma.payment.bean.LoginBean;
import com.hybunion.yirongma.payment.bean.QueryMerInfoNoneBean;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.LMFMainActivity;
import com.hybunion.yirongma.payment.Location.GaodeLocation;
import com.hybunion.yirongma.payment.Location.LocationInfo;
import com.hybunion.yirongma.payment.Location.LocationWrapper;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.util.jpush.JPushUtils;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.view.engine.PushStartService;
import com.hybunion.yirongma.payment.adapter.InputAdapter;
import com.hybunion.yirongma.payment.db.DBHelper;
import com.hybunion.yirongma.payment.db.LoginModel;
import com.hybunion.yirongma.payment.db.RemoveListner;
import com.hybunion.yirongma.payment.model.QueryEmployeeMenu;
import com.hybunion.yirongma.payment.utils.CommonMethod;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.db.BillingDataListDBManager;
import com.hybunion.yirongma.payment.utils.ShareKeys;
import com.hybunion.yirongma.payment.utils.SaveMerInfoUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFUserInfo;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * 用户登录
 *
 * @author LZ
 */
public class LoginActivity extends BaseActivity implements OnClickListener, RemoveListner, AdapterView.OnItemClickListener {

    private EditText etLoginNumber;
    private EditText etLoginPassword;
    private Button btnLogin;
    private TextView tvForgetPassword;
    private LinearLayout ibBack;
    private String UID;
    private RelativeLayout clear_input_number;
    private ImageView img_query_num;
    private InputMethodManager manager;
    // 定义一个变量，来标识是否退出
    private boolean isExit = false;
    private String longitude = "0", latitude = "0";
    private String permission = "";
    private String loginName;
    private String userPassword;
    private String VERSION;
    public ListView lv_account;
    InputAdapter inputAdapter;
    private List<LoginModel> list_result = new ArrayList();
    private boolean passwd_visible = false;
    private boolean query_num = true;
    private ImageView password_visible;
    String userPswd;
    String storeId, loginType;
    private int emuiApiLevel = 0;
    private String token = "";
    private boolean mIsFirstIn; // 是否首次进入登录页
    /**
     * 极光推送相关标记
     */
    private static final String APP_TYPE = "appType"; //app类型：0-会员端 1-商户端
    private static final String DEVICE_TOKEN = "deviceToken"; //此处为向极光注册获得的注册ID
    private static final String OS_TYPE = "osType"; //系统类型：0-android 1-ios
    private static final String USERID = "userId"; //会员或者商户的id(待确定)
    boolean isLogin;
    /**
     * 实例
     */
    private String merchantID = null;
    private CheckBox auto_login;
    private TextView tv_test_flag; //测试环境标志


    /**
     * 极光保存别名和标签的回调
     */
    private JPushUtils.JPushCallback jPushCallback = new JPushUtils.JPushCallback() {
        @Override
        public void gotResult(int responseCode, String alias, Set<String> tags) {
            switch (responseCode) {
                case 0: //设置别名成功,保存到本地中(下次判断本地存在，就不再设置，节省内存和流量)
                    SharedPreferencesUtil.setNewSP(HRTApplication.getInstance(), "alias", alias);
                    break;
            }
        }
    };

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
        OkUtils.getInstance().postNoHeader(LoginActivity.this, url, jsonObject, new MyOkCallback<CommonBean>() {
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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
            }
        }
    };

    protected void initView() {
        setContentView(R.layout.layout_login);
        init();
    }

    private void init(){
        tv_test_flag = findViewById(R.id.tv_test_flag);
        etLoginNumber = findViewById(R.id.et_login_number);
        etLoginPassword = findViewById(R.id.et_login_password);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        btnLogin = findViewById(R.id.btn_login);
        tvForgetPassword = findViewById(R.id.tv_forget_password);
        tvForgetPassword.setClickable(true);
        tvForgetPassword.setFocusable(true);
        auto_login = findViewById(R.id.cb_auto);
        clear_input_number = findViewById(R.id.clear_input_number);
        clear_input_number.setOnClickListener(this);
        img_query_num = findViewById(R.id.img_query_num);
        img_query_num.setImageResource(R.drawable.account_arrow_down);
        img_query_num.setOnClickListener(this);
        lv_account = findViewById(R.id.lv_account);
        lv_account.setOnItemClickListener(LoginActivity.this);
        password_visible = findViewById(R.id.password_visible);
        password_visible.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        etLoginNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //判断清空按键隐藏或显示
                if (etLoginNumber.getText().toString().trim().length() > 0) {
                    clear_input_number.setVisibility(View.VISIBLE);
                } else {
                    clear_input_number.setVisibility(View.INVISIBLE);
                }
            }

            //判断登录按键是否可以点击
            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        etLoginNumber.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_account.setVisibility(View.GONE);
                img_query_num.setImageResource(R.drawable.account_arrow_down);
            }
        });

        if (!BuildConfig.DEBUG) {
            tv_test_flag.setVisibility(View.GONE);
        }else {
            tv_test_flag.setVisibility(View.VISIBLE);
            switch (BuildConfig.TYPE){
                case "1":   // 测试
                    tv_test_flag.setText("当前测试环境：" + HRTApplication.versionName + Constants.TEST_CODE);
                    break;

                case "2":   // uat
                    tv_test_flag.setText("当前uat环境：" + HRTApplication.versionName + Constants.TEST_CODE);
                    break;

            }
        }
        queryList(HRTApplication.getInstance().db);

        VERSION = Build.VERSION.RELEASE;
        String loginNumber = SharedPreferencesUtil.getInstance(LoginActivity.this).getKey(SharedPConstant.LOGIN_NUMBER);
        String password = SharedPreferencesUtil.getInstance(LoginActivity.this).getKey(SharedPConstant.PASSWORD);
        etLoginNumber.setText(loginNumber);
        etLoginNumber.setSelection(loginNumber.length());
        etLoginPassword.setText(password);
        isLogin = SharedPreferencesUtil.getInstance(LoginActivity.this).getBooleanKey(SharedPConstant.ISAUTO_LOGIN,false);

        //设置自动登录,获取不到，默认为false
        auto_login.setChecked(SharedPreferencesUtil.getInstance(LoginActivity.this).getBooleanKey(SharedPConstant.ISAUTO_LOGIN,false));
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.getInstance(LoginActivity.this).putBooleanKey(SharedPConstant.ISAUTO_LOGIN,true);
                } else {
                    SharedPreferencesUtil.getInstance(LoginActivity.this).putBooleanKey(SharedPConstant.ISAUTO_LOGIN,false);
                }
            }
        });
    }

    /**
     * 点击返回按钮，两次退出，间隔时间为2秒
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtil.showShortToast(getResources().getString(R.string.press_again_to_quit));
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            HRTApplication.getInstance().finishAllActivities();
        }
    }

    /**
     * 点击空白处，隐藏软键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    String deviceId;
    boolean isLogin1 = true;

    /**
     * 用户登录
     */
    @SuppressLint("MissingPermission")
    public void userLogin() {
        if(!YrmUtils.isHavePermission(LoginActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE,YrmUtils.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE))  return;
        loginName = etLoginNumber.getText().toString().trim();
        userPassword = etLoginPassword.getText().toString().trim();
        final JSONObject jsonRequest;
        jsonRequest = new JSONObject();
        // 判断之前登录的用户名和当前登录的用户名是否一致，不一致则清除所有数据库数据。
        String loginN = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.LOGIN_NUMBER);
        if (!TextUtils.isEmpty(loginN)) {
            if (!loginN.equals(loginName)) {
                BillingDataListDBManager.getInstance(this).deleteAll();
            }
        }

        try {
            jsonRequest.put("loginName",loginName);
            jsonRequest.put("userPassword",userPassword);
            jsonRequest.put("agentId", Constant.AGENT_ID_DIFF + "");
            jsonRequest.put("versionNo", HRTApplication.versionName);
            jsonRequest.put("channel", "android" + VERSION);
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (!YrmUtils.isHavePermission(LoginActivity.this,Manifest.permission.READ_PHONE_STATE,YrmUtils.REQUEST_PERMISSION_READ_PHONE_STATE)) return;
                deviceId = tm.getDeviceId();
            if (deviceId == null || "".equals(deviceId)) {
                deviceId = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
            }
            jsonRequest.put("deviceId", deviceId);
            jsonRequest.put("model", Build.MODEL + "," + Build.VERSION.RELEASE + "," + Build.VERSION.SDK_INT);
            final LocationWrapper locationWrapper = GaodeLocation.getSingleton(this);
            locationWrapper.startLocation();
            locationWrapper.setOnLocationFinishListener(new GaodeLocation.OnLocationFinish() {
                @Override
                public void locationStart() {
                }

                @Override
                public void locationFinish(LocationInfo location) {
                    if (location.getLatitude() != 0) {
                        try {
                            jsonRequest.put("city", location.getCity());
                            jsonRequest.put("address", location.getAddress());
                            jsonRequest.put("country", location.getCountry());
                            jsonRequest.put("district", location.getDistrict());
                            jsonRequest.put("street", location.getStreet());
                            DecimalFormat dFormat = new DecimalFormat("#.000000");
                            longitude = dFormat.format(location.getLongitude());
                            latitude = dFormat.format(location.getLatitude());
                            LogUtils.d("longitude---" + longitude + "latitude---" + latitude);
                            jsonRequest.put("longitude", longitude);
                            jsonRequest.put("latitude", latitude);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            jsonRequest.put("city", "");
                            jsonRequest.put("address", "");
                            jsonRequest.put("country", "");
                            jsonRequest.put("district", "");
                            jsonRequest.put("street", "");
                            jsonRequest.put("longitude", 0);
                            jsonRequest.put("latitude", 0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.ADCODE,location.getAdCode());//点亮高德使用
                    SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.ACCUARY,location.getAccuracy());//点亮高德使用
                    SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(ShareKeys.CITY, location.getCity());
                    SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(ShareKeys.PROVINCE, location.getProvince());
                    SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(ShareKeys.DISTRICT, location.getDistrict());
                    SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.SAVE_TIME_LIST2, location.getCity());
                    locationWrapper.stopLocation();

                    if(!isLogin1)  return;
                    OkUtils.getInstance().post(LoginActivity.this, NetUrl.LOGIN_URL, jsonRequest, new MyOkCallback<LoginBean>() {
                        @Override
                        public void onStart() {
                            showProgressDialog("");
                        }

                        @Override
                        public void onSuccess(LoginBean loginBean) {
                            String status = loginBean.getStatus();
                            if("0".equals(status)){
                                UID = loginBean.UID;
                                SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.LOGIN_NUMBER, loginName);
                                SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.LOGIN_NAME, loginName);
                                SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.UID,UID);
                                SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.LONGITUDE,longitude);//点亮高德使用
                                SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.LATITUDE,latitude);//点亮高德使用
                                SPUtil.putString(SharedPConstant.TOKEN, loginBean.token);
                                getSuecessMsg();
                            }else {
                                hideProgressDialog();
                                String message = loginBean.getMessage();
                                if (!TextUtils.isEmpty(message)) {
                                    ToastUtil.showShortToast(message);
                                }else{
                                    ToastUtil.showShortToast("网络连接不佳");
                                }
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            hideProgressDialog();
                            ToastUtil.showShortToast("网络连接不佳");
                        }

                        @Override
                        public void onFinish() {
                        }

                        @Override
                        public Class getClazz() {
                            return LoginBean.class;
                        }
                    });
                }

                @Override
                public void locationStop() {
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // 查询商户信息 并保存本地
    private void getSuecessMsg() {
        SharedPreferencesUtil.getInstance(this).putKey(SharedPConstant.FLOAT_IS_SHOW, 1); // 用户默认开启悬浮球功能
        String url = NetUrl.QUERY_MERINFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UID", UID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(LoginActivity.this, url, jsonObject, new MyOkCallback<QueryMerInfoNoneBean>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(QueryMerInfoNoneBean queryMerInfoNoneBean) {
                String status = queryMerInfoNoneBean.getStatus();
                loginType = queryMerInfoNoneBean.getLoginType();
                if (status.equals("0")) {
                    merchantID = queryMerInfoNoneBean.getMerchantID();
                    storeId = queryMerInfoNoneBean.getStoreId();
                    CrashReport.putUserData(LoginActivity.this, "merchantID", merchantID);
                    CrashReport.putUserData(LoginActivity.this, "loginName", loginName);
                    CrashReport.setUserId("BOSS");
                    Constant.AGENT_ID = TextUtils.isEmpty(queryMerInfoNoneBean.getAgentId()) ? 0 : Integer.parseInt(queryMerInfoNoneBean.getAgentId());

                    // 保存用户数据
                    SaveMerInfoUtil.saveAll(LoginActivity.this,queryMerInfoNoneBean);

                    //用户登录成功，将用户的别名和标签向极光初始化
                    //将用户信息上传，作为推送的依据(向公司服务器上传，同一用户只上传一次)
                    String registrationID = JPushInterface.getRegistrationID(HRTApplication.getInstance());
                    Log.i("xjz--registrationID", registrationID + "");
                    pushJPushToServer(registrationID);
                    registerHWorJG();//注册极光或者华为
                    if (auto_login.isChecked()) {
                        insertMessage(HRTApplication.getInstance().db, queryMerInfoNoneBean.getMerchantID(), loginName, userPassword);
                    } else {
                        insertMessage(HRTApplication.getInstance().db, queryMerInfoNoneBean.getMerchantID(), loginName, null);
                    }

                    // 设置客服用信息
                    if ("0".equals(loginType)) {
                        setCustomerInfo(queryMerInfoNoneBean.getMerchantName(), queryMerInfoNoneBean.getMerchantID(), loginName, queryMerInfoNoneBean.getMid());
                    } else {
                        setCustomerInfo(queryMerInfoNoneBean.getStoreName(), queryMerInfoNoneBean.getMerchantID(), loginName, queryMerInfoNoneBean.getStoreId());

                    }
                    postData();
                }else if ("-99".equals(status)){   // token 过期
                    ToastUtil.showShortToast("登录过期，请重新登录");
                }else{
                    ToastUtil.showShortToast("网络连接不佳");
                }


            }

            @Override
            public void onError(Exception e) {
                hideProgressDialog();
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return QueryMerInfoNoneBean.class;
            }
        });

    }

    public void registerHWorJG(){
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

        if (emuiApiLevel > 10) {//华为手机
            HMSAgent.connect(LoginActivity.this, new ConnectHandler() {
                @Override
                public void onConnect(int rst) {
                    Log.i("xjz", "HMS connect end:" + rst);
                    Log.i("huawei111", "HMS connect end:" + rst);
                    if (rst != 0) {
                        boolean connected = JPushInterface.getConnectionState(LoginActivity.this);
                        if(!connected){
                            if (JPushInterface.isPushStopped(LoginActivity.this)) {
                                JPushInterface.resumePush(getApplicationContext());
                            }else {
                                // 使用服务向极光注册
                                startService(new Intent(LoginActivity.this, PushStartService.class));
                            }
                        }

                        SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.HUAWEI_IS_REGISTE, "失败");
                    } else {
                        boolean connected = JPushInterface.getConnectionState(LoginActivity.this);
                        if(connected){
                            JPushInterface.stopPush(LoginActivity.this);
                            Log.i("xjz", "注销了极光推送");
                        }
                        SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.HUAWEI_IS_REGISTE, "成功");
                    }
                }
            });
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
            if (JPushInterface.isPushStopped(LoginActivity.this)) {
                JPushInterface.resumePush(getApplicationContext());
                // 使用服务向极光注册
            }
            startService(new Intent(LoginActivity.this, PushStartService.class));

        }

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


    private void insertMessage(SQLiteDatabase db, String uid, String name, String pswd) {
        if (queryUid(HRTApplication.getInstance().db, name)) {
            queryTwo(HRTApplication.getInstance().db, name);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("uid", uid);
            contentValues.put("uname", name);
            contentValues.put("upswd", pswd);
            // 调用insert()方法插入数据
            long result_msg = db.insert("loginTable", null, contentValues);
        }
    }

    /**
     * 查询数据：带参数和条件的
     *
     * @param db
     */
    private void queryTwo(SQLiteDatabase db, String uname) {
        Cursor cursor = db.query("loginTable", new String[]{"upswd"}, "uname=?", new String[]
                {uname}, null, null, null);
        while (cursor.moveToNext()) {
            userPswd = cursor.getString(cursor.getColumnIndex("upswd"));
            if (userPswd != null) {
                if (auto_login.isChecked()) {
                } else {
                    userPswd = null;
                }
            } else {
                if (auto_login.isChecked()) {
                    userPswd = userPassword;
                } else {
                    userPswd = null;
                }
            }
        }
        upadteData(HRTApplication.getInstance().db, userPswd, uname);
    }

    private boolean queryUid(SQLiteDatabase db, String name) {
        boolean flag;
        // 查询获得游标
        Cursor cursor = db.query("loginTable", null, "uname=?", new String[]{name}, null, null, null);
        cursor.getColumnCount();
        // 判断游标是否为空
        if (cursor.moveToFirst()) {
            flag = true;
            int index = cursor.getColumnIndex("uname");
        } else {
            flag = false;
        }
        return flag;
    }


    private void postData() {
        final String loginName = SharedPreferencesUtil.getInstance(this).getKey("loginNumber");
        final String agentID = String.valueOf(Constant.AGENT_ID_DIFF);
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            jsonRequest.put("loginName", loginName);
            jsonRequest.put("agentID", agentID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.EMPLOYEEMENU;
        OkUtils.getInstance().postNoHeader(LoginActivity.this, url, jsonRequest, new MyOkCallback<QueryEmployeeMenu>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(QueryEmployeeMenu queryEmployeeMenu) {
                try {
                    isLogin1 = false;
                    String status = queryEmployeeMenu.getStatus();
                    if ("1".equals(status)) {
                        int one = queryEmployeeMenu.getData().size();
                        for (int i = 0; i < one; i++) {
                            QueryEmployeeMenu.MyData myData = queryEmployeeMenu.getData().get(i);
                            int two = myData.getChildren().size();
                            for (int j = 0; j < two; j++) {
                                QueryEmployeeMenu.MyData myData2 = myData.getChildren().get(j);
                                permission += myData.getText() + myData2.getText() + ",";
                                LogUtil.d("lyf---permission:" + permission);
                            }
                        }
                        SharedPreferencesUtil.getInstance(LoginActivity.this).putKey("permission", permission);
                        SharedPreferencesUtil.getInstance(LoginActivity.this).putKey("exit", "0");
                        Intent intent = new Intent(LoginActivity.this, LMFMainActivity.class);
                        intent.putExtra("intentType","1");
                        startActivity(intent);

                        LoginActivity.this.finish();
                    } else if ("3".equals(status)) {
                        permission = "-";
                        SharedPreferencesUtil.getInstance(LoginActivity.this).putKey("permission", permission);
                        SharedPreferencesUtil.getInstance(LoginActivity.this).putKey("exit", "0");
                        Intent intent = new Intent(LoginActivity.this,
                                LMFMainActivity.class);
                        intent.putExtra("intentType","1");
                        startActivity(intent);

                        LoginActivity.this.finish();
                    }else if ("-99".equals(status)) {   // token 过期
                        ToastUtil.showShortToast("登录过期，请重新登录");
                    } else {
                        permission = "";
                        ToastUtil.showShortToast("您没有被授权使用此软件,请联系主账户");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                return QueryEmployeeMenu.class;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                final String loginName = etLoginNumber.getText().toString().trim();
                final String userPassword = etLoginPassword.getText().toString().trim();
                if (CommonMethod.isEmpty(loginName)) {
                    ToastUtil.showShortToast( getResources().getString(R.string.null_phone_number));
                } else if (TextUtils.isEmpty(userPassword)) {
                    ToastUtil.showShortToast(getResources().getString(R.string.null_pwd));
                } else if (userPassword.length() < 6 || userPassword.length() > 18) {
                    ToastUtil.showShortToast(getResources().getString(R.string.pwd_len_limit));
                } else {

                    userLogin();
                }
                break;
            case R.id.tv_forget_password:
                Intent intent2 = new Intent(LoginActivity.this,
                        ForgetPwdActivity.class);
                startActivity(intent2);
                break;
            case R.id.clear_input_number:
                if (clear_input_number.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(etLoginNumber.getText().toString())) {
                    etLoginNumber.setText("");
                    etLoginNumber.setHint("请输入手机号");
                    etLoginPassword.setText("");
                    etLoginPassword.setHint("请输入密码");
        }
        break;
        case R.id.img_query_num:
                if (query_num) {
                    query_num = false;
                    img_query_num.setImageResource(R.drawable.account_arrow_up);
                    lv_account.setVisibility(View.VISIBLE);
                    queryList(HRTApplication.getInstance().db);
                    InputMethodManager imms = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imms.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                } else {
                    query_num = true;
                    lv_account.setVisibility(View.GONE);
                    img_query_num.setImageResource(R.drawable.account_arrow_down);
                }
                break;
            case R.id.password_visible:
                passwd_visible = !passwd_visible;
                if (passwd_visible) {//明文
                    password_visible.setBackgroundResource(R.drawable.img_visible_passwd);
                    etLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    etLoginPassword.setSelection(etLoginPassword.getText().toString().trim().length());
                } else { //暗文
                    password_visible.setBackgroundResource(R.drawable.img_invisible_passwd);
                    etLoginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etLoginPassword.setSelection(etLoginPassword.getText().toString().trim().length());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 查询数据：不带参数和条件的
     *
     * @param db
     */
    private void queryList(SQLiteDatabase db) {
        LogUtils.dlyj(list_result.size() + "查询数据");
        if (list_result.size() >= 0) {
//            img_query_num.setVisibility(View.GONE);
            list_result.clear();
        }
        // 查询获得游标
        Cursor cursor = db.query("loginTable", null, null, null, null, null, null);
        // 判断游标是否为空
        LoginModel loginModel;
        while (cursor.moveToNext()) {
            String uid = cursor.getString(cursor.getColumnIndex("uid"));
            String uname = cursor.getString(cursor.getColumnIndex("uname"));
            String upswd = cursor.getString(cursor.getColumnIndex("upswd"));

            if ("11".equals(uname)) {
                deleteData(db, uid);
                continue;
            }

            loginModel = new LoginModel();
            loginModel.setUid(uid);
            loginModel.setUname(uname);
            loginModel.setUpswd(upswd);
            list_result.add(loginModel);
            LogUtils.dlyj(list_result.size() + "====查询出来的数据");
            if (list_result.size() > 0) {
                img_query_num.setVisibility(View.VISIBLE);
            } else {
//                    img_query_num.setVisibility(View.GONE);
            }

            LogUtils.dlyj(list_result.size() + "查询出来的数据");
            LogUtils.dlyj(loginModel.toString());
        }
        // 关闭数据库
        cursor.close();
        inputAdapter = new InputAdapter(this, list_result, LoginActivity.this);
        lv_account.setAdapter(inputAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String name = intent.getStringExtra("loginName");
        String passWord = intent.getStringExtra("userPassword");
        LogUtils.dlyj(name + passWord + "账号、密码");
        if (name != null && !"".equals(name)) {
            etLoginNumber.setText(name);
            etLoginPassword.setText(passWord);
        }
        hideProgressDialog();
    }

    /**
     * 在生命周期之前将静态的activity置为空，
     * 防止一些监听持有对他的引用内存泄漏
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除设置别名的回调接口 并将回调接口置为
        jPushCallback = null;
    }

    @Override
    public void setRemove(int pos, LoginModel loginModel) {
        deleteData(HRTApplication.getInstance().db, loginModel.getUid());
        clear_input_number.setVisibility(View.INVISIBLE);
        etLoginNumber.setText("");
        etLoginNumber.setHint("请输入手机号");
        etLoginPassword.setText("");
        etLoginPassword.setHint("请输入密码");
        //删除数据库里数据的同时也要把本地的登录信息清空
        SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.LOGIN_NUMBER,"");
        SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.PASSWORD,"");
        SharedPreferencesUtil.getInstance(LoginActivity.this).putKey(SharedPConstant.LOGIN_NAME,"");
    }

    /**
     * 删除数据 ,第一种方法的代码：
     */
    private void deleteData(SQLiteDatabase db, String time) {
        // 删除条件
        String whereClause = "uid=?";
        // 删除条件参数
        String[] whereArgs = {String.valueOf(time)};
        Log.e("lyj", time + "=====》id");
        // 执行删除
        int result_msg = db.delete("loginTable", whereClause, whereArgs);
        if (result_msg != -1) {
            queryList(HRTApplication.getInstance().db);
        }
    }

    private void upadteData(SQLiteDatabase db, String userPswd, String name) {
        ContentValues value = new ContentValues();
        value.put("upswd", userPswd);
        DBHelper helper = new DBHelper(LoginActivity.this, "loginTable");
        db = helper.getWritableDatabase();
        db.update("loginTable", value, "uname=?", new String[]{name});
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String userName = inputAdapter.list_msg.get(position).getUname();
        String password = inputAdapter.list_msg.get(position).getUpswd();
        LogUtils.dlyj(password + "登录密码");
        lv_account.setVisibility(View.GONE);
        etLoginNumber.setText(userName);
        if (password == null || "".equals(password)) {
            auto_login.setChecked(false);
            etLoginPassword.setText("");
        } else {
            auto_login.setChecked(true);
            etLoginPassword.setText(password);
        }
        query_num = true;
        lv_account.setVisibility(View.GONE);
        img_query_num.setImageResource(R.drawable.account_arrow_down);
        LogUtils.dlyj(userName + password + "切换账号");
    }

}
