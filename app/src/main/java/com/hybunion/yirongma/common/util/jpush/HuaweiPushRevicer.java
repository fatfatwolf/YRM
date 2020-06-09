/*
 * Copyright (C) Huawei Technologies Co., Ltd. 2016. All rights reserved.
 * See LICENSE.txt for this sample's licensing information.
 */

package com.hybunion.yirongma.common.util.jpush;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huawei.hms.support.api.push.PushReceiver;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.QueryTransBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.common.net.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 应用需要创建一个子类继承com.huawei.hms.support.api.push.PushReceiver，
 * 实现onToken，onPushState ，onPushMsg，onEvent，这几个抽象方法，用来接收token返回，push连接状态，透传消息和通知栏点击事件处理。
 * onToken 调用getToken方法后，获取服务端返回的token结果，返回token以及belongId
 * onPushState 调用getPushState方法后，获取push连接状态的查询结果
 * onPushMsg 推送消息下来时会自动回调onPushMsg方法实现应用透传消息处理。本接口必须被实现。 在开发者网站上发送push消息分为通知和透传消息
 * 通知为直接在通知栏收到通知，通过点击可以打开网页，应用 或者富媒体，不会收到onPushMsg消息
 * 透传消息不会展示在通知栏，应用会收到onPushMsg
 * onEvent 该方法会在设置标签、点击打开通知栏消息、点击通知栏上的按钮之后被调用。由业务决定是否调用该函数。
 * <p>
 * Application needs to create a subclass to inherit com.huawei.hms.support.api.push.PushReceiver,
 * implement Ontoken,onpushstate, Onpushmsg,onevent,
 * these several abstract methods, Used to receive token return, push connection status,
 * pass through message and notification bar click event handling.
 * After Ontoken calls the GetToken method, gets the token result returned by the server, returns token,
 * and after Belongidonpushstate invokes the Getpushstate method,
 * gets the query result of the push connection state onpushmsg When a push message comes down,
 * it will automatically callback the Onpushmsg method to implement the application of the message processing.
 * This interface must be implemented.
 * On the developer website to send a push message is divided into notification and message notification for direct notification in the notice bar,
 * by clicking can open the Web page, application or rich media,
 * will not receive ONPUSHMSG messages will not be displayed in the notification bar,
 * application will receive onpushmsgonevent the method will It is called after you set the label,
 * click to open the Notification bar message, and click the button on the notification bar.
 * The business decides whether to call the function.
 */
public class HuaweiPushRevicer extends PushReceiver {

    public static final String TAG = "HuaweiPushRevicer";

    public static final String ACTION_UPDATEUI = "action.updateUI";
    //    public static final String ACTION_TOKEN = "action.updateToken";
    private String loginType;
    private int emuiApiLevel = 0;
    private String VoiceSwitch;


    private static List<IPushCallback> pushCallbacks = new ArrayList<IPushCallback>();
    private static final Object CALLBACK_LOCK = new Object();

    public interface IPushCallback {
        void onReceive(Intent intent);
    }

    public static void registerPushCallback(IPushCallback callback) {
        synchronized (CALLBACK_LOCK) {
            pushCallbacks.add(callback);
        }
    }

    public static void unRegisterPushCallback(IPushCallback callback) {
        synchronized (CALLBACK_LOCK) {
            pushCallbacks.remove(callback);
        }
    }

    /**
     * 申请token会触发启动Push服务，token申请成功后，结果会通过广播的方式返回token给应用。
     * 调用getToken方法发起请求，返回申请token的PendingResult对象，根据对象可以获取接口调用是否成功，但是不直接返回token 结果。
     *
     * @param context
     * @param tokenIn
     * @param extras
     */
    @Override
    public void onToken(Context context, String tokenIn, Bundle extras) {
        loginType = SharedPreferencesUtil.getInstance(context).getKey("loginType");

        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            emuiApiLevel = Integer.parseInt((String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String belongId = extras.getString("belongId");

//        Intent intent = new Intent();
//        intent.setAction(ACTION_TOKEN);
//        intent.putExtra(ACTION_TOKEN, tokenIn);
//        callBack(intent);

//        intent = new Intent();
//        intent.setAction(ACTION_UPDATEUI);
//        intent.putExtra("log", "belongId is:" + belongId + " Token is:" + tokenIn);
        Log.i("xjz", "belongId is:" + belongId + " Token is:" + tokenIn);
//        callBack(intent);
        if (emuiApiLevel > 10) {
            keepToken(context, tokenIn);
            SharedPreferencesUtil.getInstance(context).putKey(SharedPConstant.HUAWEI_TOKEN, tokenIn);
        }

    }

    private void keepToken(final Context context, String token) {

        JSONObject bodyJson = new JSONObject();
        try {
            if (loginType.equals("0")) {
                bodyJson.put("merId", SharedPreferencesUtil.getInstance(context).getKey(SharedPConstant.MERCHANT_ID));
                bodyJson.put("storeId", "");
            } else {
                bodyJson.put("merId", SharedPreferencesUtil.getInstance(context).getKey("shopId"));
                bodyJson.put("storeId", SharedPreferencesUtil.getInstance(context).getKey("storeId"));
            }

            if (emuiApiLevel > 10) {
                bodyJson.put("type", "0");
            } else {
                bodyJson.put("type", "1");
            }
            bodyJson.put("loginType", loginType);
            bodyJson.put("deviceToken", token);

        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(null, NetUrl.KEEP_TOKEN, bodyJson, new MyOkCallback<String>() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject response = new JSONObject(o);
                    String status = response.getString("status");
                    String message = response.getString("message");
                    if ("0".equals(status)) {
                        Log.i("xjz111", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    String msg1;
    String content;
    Gson gson;
    QueryTransBean.DataBean bean;

    /**
     * 供子类继承实现后，推送消息下来时会自动回调onPushMsg方法实现应用透传消息处理。本接口必须被实现
     *
     * @param context
     * @param msg
     * @param bundle
     * @return
     */
    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            Log.d("huawei111", "收到华为推送！！！！");
            //CP可以自己解析消息内容，然后做相应的处理 | CP can parse message content on its own, and then do the appropriate processing
            content = new String(msg, "UTF-8");
            Log.i("xjz", "Receive a push pass message with the message:" + content);
            gson = new Gson();
            bean = gson.fromJson(content
                    , new TypeToken<QueryTransBean.DataBean>() {
                    }.getType());
            if (bean == null) return false;
            msg1 = bean.redMsg;
            String payName = bean.payName;
            bean.payChannel = "支付成功";
            // transType 为 1 或 2 或 3 时，是惠储值数据
            if (!TextUtils.isEmpty(bean.transType) && ("1".equals(bean.transType) || "2".equals(bean.transType) || "3".equals(bean.transType))) {
                bean.isHuiChuZhi = true;
                bean.orderType = bean.transType;
                String orderNo = bean.svcOrderNo;
                // 订单号手动拼接 “SVC”，避免和收款码订单号重复
                if (!TextUtils.isEmpty(orderNo) && !orderNo.contains("SVC")) {
                    bean.svcOrderNo = "SVC" + orderNo;
                }
                bean.orderNo = bean.svcOrderNo;
            }

            if (!TextUtils.isEmpty(payName)) {
                if (payName.contains("微信")) {
                    bean.payType = "0";
                    bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/WeChatPay.png";
                } else if (payName.contains("支付宝")) {
                    bean.payType = "1";
                    bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/Alipay.png";
                } else if (payName.contains("云闪付")) {
                    bean.payType = "3";
                    bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/UnionPay.png";
                } else if (payName.contains("福利卡")) {
                    bean.payType = "4";
                    bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/FuLiCard.png";
                } else if (payName.contains("和卡")) {
                    bean.payType = "5";
                    bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/HeCard.png";
                }
            }

            if (!TextUtils.isEmpty(bean.transDate)) {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bean.transDate);
                bean.transTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
            } else {
                bean.transTime = "";
            }
            bean.transAmount = bean.payableAmt;

            Intent intent = new Intent();
            intent.setAction(ACTION_UPDATEUI);
//            intent.putExtra("log", "Receive a push pass message with the message:" + msg1);
            intent.putExtra("dataBean", bean);
            callBack(intent);
            VoiceSwitch = SharedPreferencesUtil.getInstance(context).getKey("VoiceSwitch");
//            Intent messageIntent = new Intent(SuccessfulReceipt.ACTION_INTENT_RECEIVER);
//            context.sendBroadcast(messageIntent);
            if (TextUtils.isEmpty(VoiceSwitch) || "1".equals(VoiceSwitch)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            synchronized (this) {
//
                                VoicePlayManager.getInstance(HRTApplication.getInstance()).playMutilSounds(msg1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
//            callBack(intent);
        } catch (Exception e) {
            e.printStackTrace();
//            Intent intent = new Intent();
//            intent.setAction(ACTION_UPDATEUI);
//            intent.putExtra("log", "Receive push pass message, e.getMessage():" + e.getMessage());
//            Log.i("xjz","Receive push pass message, e.getMessage():" + e.getMessage());
//            callBack(intent);
        }
        return false;
    }

    /**
     * 供子类继承实现，实现业务事件。该方法会在设置标签、点击打开通知栏消息、点击通知栏上的按钮之后被调用。由业务决定是否调用该函数。
     *
     * @param context
     * @param event
     * @param extras
     */
    public void onEvent(Context context, Event event, Bundle extras) {
//        Intent intent = new Intent();

//        intent.setAction(ACTION_UPDATEUI);

        int notifyId = 0;
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
        }

        String message = extras.getString(BOUND_KEY.pushMsgKey);
//        intent.putExtra("log", "Received event,notifyId:" + notifyId + " msg:" + message);
        Log.i("xjz", "Received event,notifyId:" + notifyId + " msg:" + message);
//        callBack(intent);
        super.onEvent(context, event, extras);
    }

    /**
     * 当push处于连接状态的时候，才能正常接收push消息
     *
     * @param context
     * @param pushState
     */
    @Override
    public void onPushState(Context context, boolean pushState) {
//        Intent intent = new Intent();
//        intent.setAction(ACTION_UPDATEUI);
//        intent.putExtra("log", "The Push connection status is:" + pushState);
//        Log.i("xjz","   The Push connection status is:" + pushState);
//        callBack(intent);
    }

    private static void callBack(Intent intent) {
        synchronized (CALLBACK_LOCK) {
            for (IPushCallback callback : pushCallbacks) {
                if (callback != null) {
                    callback.onReceive(intent);
                }
            }
        }
    }
}
