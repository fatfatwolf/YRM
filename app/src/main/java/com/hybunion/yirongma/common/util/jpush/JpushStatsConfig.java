package com.hybunion.yirongma.common.util.jpush;

import android.content.Context;

import com.hybunion.yirongma.HRTApplication;

import java.util.Map;

import cn.jiguang.analytics.android.api.CountEvent;
import cn.jiguang.analytics.android.api.Event;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class JpushStatsConfig {
    //整个App的上下文对象
    private static HRTApplication mContext = HRTApplication.getInstance();



    public static void initStats(){
        //极光统计
        JAnalyticsInterface.setDebugMode(true);
        JAnalyticsInterface.init(mContext);
    }
    /**开启crashlog日志上报**/
    public void openCrashLog(){
        JAnalyticsInterface.initCrashHandler(mContext);
    }

    /**关闭crashlog日志上报**/
    public void closeCrashLog(){
        JAnalyticsInterface.stopCrashHandler(mContext);
    }


    /**
     * 计数事件
     *
     * @param context
     * @param eventId   事件ID
     * @param extra     附加信息
     */
    public static void onCountEvent(Context context, String eventId, Map<String, String> extra) {
        Event countEvent = new CountEvent()
                .setEventId(eventId)
                .addExtMap(extra);
        JAnalyticsInterface.onEvent(context, countEvent);
    }

}
