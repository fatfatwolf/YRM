package com.hybunion.yirongma.payment.utils;

import android.util.Log;

import com.hybunion.yirongma.BuildConfig;

/**
 * @author SunBingbing
 * @date 2017/5/10
 * @email freemars@yeah.net
 * @description Log　工具类
 */

public class LogUtil {

    private static final String TAG = "xjz";

    /**
     * 打印 Exception 信息
     * @param info 错误信息
     * */
    public static void e (String info){
        if (BuildConfig.LOG_DEBUG){
            Log.e(TAG,info);
        }
    }

    /**
     * 打印信息
     * @param info 信息
     * */
    public static void d (String info){
        if (BuildConfig.LOG_DEBUG){
            Log.d(TAG,info);
        }
    }

}
