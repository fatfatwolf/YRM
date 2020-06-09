package com.hybunion.yirongma.common.util.jpush;


import com.hybunion.yirongma.HRTApplication;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Iron Man on 2016/7/21.
 * 极光推送相关工具类，用来完成极光推送的各种功能
 */
public class JPushUtils {

    //整个App的上下文对象
    private static HRTApplication application = HRTApplication.getInstance();

    /**极光推送的初始化*/
    public static void initJPush(){
        JPushInterface.setDebugMode(true);
        JPushInterface.init(application);
    }

    /**停止极光推送*/
    public static void stopPush(){
        if(JPushInterface.isPushStopped(application)){
            return ;
        }
        JPushInterface.stopPush(application);
    }

    /**恢复极光推送*/
    public static void resumePush(){
        if(JPushInterface.isPushStopped(application)){
            JPushInterface.resumePush(application);
        }
    }

    /**判断极光推送额状态*/
    public static boolean isPushAlive(){
        return !JPushInterface.isPushStopped(application);
    }

    /**
     * 为用户设置别名和标签
     * @param alias 别名
     * @param tags 标签（用来标志一类人群）
     * @param jPushCallback 处理极光推送回调的接口
     */
    public static void setAliasAndTags(String alias, Set<String> tags, final JPushCallback jPushCallback){

        //调用极光推送的方法
        JPushInterface.setAliasAndTags(application, alias, tags, new TagAliasCallback() {
            @Override
            public void gotResult(int responseCode, String alias, Set<String> tags) {
                if(jPushCallback!=null)
                     jPushCallback.gotResult(responseCode,alias,tags);
            }
        });


    }

    /**
     * 移除极光别名
     * @param alias 别名
     * @param tags 标签（用来标志一类人群）
     *   处理极光推送回调的接口
     */
    public static void removeSetAliasAndTagsCallBack(String alias, Set<String> tags){

        //调用极光推送的方法
        JPushInterface.setAliasAndTags(application, alias, tags,null);

    }
    /**自定义回调接口,接受极光返回的结果*/
    public interface JPushCallback{
        //responseCode-返回码(0:调用成功)，alias:别名，tags:标签
        void gotResult(int responseCode, String alias, Set<String> tags);
    }

}
