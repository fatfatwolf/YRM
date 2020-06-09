package com.hybunion.yirongma.payment.view.engine;

import android.content.Context;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author SunBingbing
 * @date 2017/2/23
 * @email freemars@yeah.net
 * @description 极光推送功能的封装类
 */

public class PushEngine {

    /**
     * 初始化推送引擎
     * @param context 上下文
     * @param buildType App 编译类型
     */
    public static void initPushEngine(Context context, boolean buildType){
        JPushInterface.init(context);
        JPushInterface.setDebugMode(buildType);
    }

    /**
     * 向极光注册设置别名
     * @param context 上下文
     * @param alias 别名（注意非法字符的使用）
     * @param callback 注册回调
     */
    public static void setAlias(Context context, String alias, PushEngineRegisterCallback callback){
        JPushInterface.setAlias(context,alias,callback);
    }

    /**
     * 通过抽象类来处理极光注册回调接口
     */
    public static abstract class PushEngineRegisterCallback implements TagAliasCallback {

        /**
         * 注册失败
         * @param code 错误码
         */
        abstract void onFail(int code);

        /**
         * 注册成功
         * @param alias 成功返回的别名
         */
        abstract void onSuccess(String alias);

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code){
                case 0: // 设置别名成功
                    onSuccess(alias);
                    break;
                case 6002: // 请求超时（需要稍后重试）
                    onFail(code);
                    break;
                default: // 因为其他原因，没有注册成功
                    onFail(code);
                    break;
            }
        }
    }

    /**
     * 清空通知（根据指定的通知 ID）
     * @param context 上下文
     * @param notificationId 通知 ID
     */
    public static void clearNotificationById(Context context, int notificationId){
        JPushInterface.clearNotificationById(context, notificationId);
    }

    // 收到推送通知
    public static String receiveNotification(){
        return JPushInterface.ACTION_NOTIFICATION_RECEIVED;
    }

    // 打开了推送通知
    public static String openNotification(){
        return JPushInterface.ACTION_NOTIFICATION_OPENED;
    }

    // 通知标题
    public static String notificationTitle(){
        return JPushInterface.EXTRA_NOTIFICATION_TITLE;
    }

    // 通知内容
    public static String notificationContent(){
        return JPushInterface.EXTRA_ALERT;
    }

    // 通知自定义字段内容
    public static String notificationExtra(){
        return JPushInterface.EXTRA_EXTRA;
    }

    // 通知 ID
    public static String notificationId(){
        return JPushInterface.EXTRA_NOTIFICATION_ID;
    }


}
