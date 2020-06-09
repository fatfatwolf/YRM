package com.hybunion.yirongma.common.util.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.hybunion.yirongma.payment.bean.QueryTransBean;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.common.util.shortcutbadger.ShortcutBadger;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.activity.MainMassageActivity;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;

/**
 * 接受极光推送信息的广播接收者，可以获得极光推送的信息并进行处理
 */
public class JPushReceiver extends BroadcastReceiver {


    String redMsg, VoiceSwitch;
    public static final String PUSH_UPDATE_DATA_RECEIVER = "com.hybunion.yirongma.jpush.update_data";
    public static final String DATA_RECEIVER_KEY = "updateData";
    private Intent updateDataIntent;

    @Override
    public void onReceive(final Context context, Intent intent) {

        VoiceSwitch = SharedPreferencesUtil.getInstance(context).getKey("VoiceSwitch");
        //获取传送过来的数据
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();
        //获得自定义信息
        String resultJson = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i("xjz", "收到推送信息了" + resultJson);
        //和服务器沟通的指令（用于指示推送通知的类型）
        String type = null;
        JSONObject jsonObject = null;
        try {
            if (!TextUtils.isEmpty(resultJson)) {
                jsonObject = new JSONObject(resultJson);
                type = jsonObject.optString("type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // 是否需要唤醒屏幕 0-不需要 1-需要，默认值为 0
        int needWakeUp = SharedPreferencesUtil.getInstance(context).getIntKey(SharedPConstant.NEED_WAKE_UP);
        if (needWakeUp == 1) {
            Intent wakeUp = new Intent(context, WakeUpActivity.class);
            wakeUp.putExtras(bundle);
            wakeUp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.
                    FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(wakeUp);
        }
        //判断 type 类型是否为空
        if (TextUtils.isEmpty(type)) return;
        switch (type) {
            case "1": //消费推送
                redMsg = jsonObject.optString("audioMsg");
                QueryTransBean.DataBean bean = new QueryTransBean().new DataBean();
                bean.orderNo = jsonObject.optString("orderNo");  // 交易订单号
                String payName = jsonObject.optString("payName");  // 根据“微信“  ”支付宝“ 等文字判断支付类型

                // transType 1购卡 2充值 3消费 这个字段不为空，说明是惠储值的数据，否则是收款码的数据
                String transType = jsonObject.optString("transType");
                // transType 为 1 或 2 或 3 时，是惠储值数据
                if (!TextUtils.isEmpty(transType) && ("1".equals(transType) || "2".equals(transType) || "3".equals(transType))){
                    bean.isHuiChuZhi = true;
                    bean.orderType = transType;
                    // 订单号手动拼接 “SVC”，避免和收款码订单号重复
//                    String orderNo = bean.orderNo;
                    String orderNo = jsonObject.optString("svcOrderNo"); // 惠储值数据订单号字段为 svcOrderNo
                    if (!TextUtils.isEmpty(orderNo) && !orderNo.contains("SVC")){
                        bean.orderNo = "SVC"+orderNo;
                    }else{
                        bean.orderNo = orderNo;
                    }
                }
                if (!TextUtils.isEmpty(payName)){
                    if (payName.contains("微信")){
                        bean.payType = "0";
                        bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/WeChatPay.png";
                    }else if (payName.contains("支付宝")){
                        bean.payType = "1";
                        bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/Alipay.png";
                    }else if (payName.contains("云闪付")){
                        bean.payType = "3";
                        bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/UnionPay.png";
                    }else if(payName.contains("福利卡")){
                        bean.payType = "4";
                        bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/FuLiCard.png";
                    }else if (payName.contains("和卡")){
                        bean.payType = "5";
                        bean.iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/HeCard.png";
                    }
                }
                bean.transAmount=jsonObject.optString("payableAmt");
                bean.storeId = jsonObject.optString("storeId");
                String transTime = jsonObject.optString("transDate");
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(transTime);
                    bean.transTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    bean.transTime = "";
                }
                bean.payChannel = "支付成功";
                // 保证 订单金额、订单号、交易时间都有，才插入数据库
                if (!TextUtils.isEmpty(bean.orderNo) && !TextUtils.isEmpty(bean.transAmount) && !TextUtils.isEmpty(transTime)){
                    updateDataIntent = new Intent(PUSH_UPDATE_DATA_RECEIVER);
                    updateDataIntent.putExtra(DATA_RECEIVER_KEY,bean);
                    context.sendBroadcast(updateDataIntent);
                }

                if (redMsg != null) {
                    if (TextUtils.isEmpty(VoiceSwitch) || "1".equals(VoiceSwitch)) {
                        new Thread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run() {
                                try {
                                    VoicePlayManager.getInstance(HRTApplication.getInstance()).playMutilSounds(redMsg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }
                }
                break;

        }

        //在sp里记录角标数量 易宝付中没有添加此功能
        SharedPreferences sp = context.getSharedPreferences("jPush", Context.MODE_PRIVATE);
        int badgeCount = sp.getInt("badgeCount", 0);
        badgeCount++;
        boolean success = false;

        if (Build.VERSION.SDK_INT>=23) {
            success = ShortcutBadger.applyCount(context, badgeCount);
        }
        if (success) {
            //打开应用的时候清除角标
            //生成角标成功 保存角标
            sp.edit().putInt("badgeCount", badgeCount).commit();
        }

        //收到推送通知时(可以在此进行相关业务处理)
        if (action.equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {

        }else if(action.equals(JPushInterface.ACTION_NOTIFICATION_OPENED)){
            switch (type){
                case "9":
                    Intent intent1 = new Intent(context, MainMassageActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(intent1);
                    break;
            }
        }
    }
}
