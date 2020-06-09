package com.hybunion.net.utils;

import android.util.Log;


/**
 * Created by lcy on 2015/7/10.
 */
public class LogUtil {
    private static final String TAG = "find";

    public static void d(String msg) {
            Log.d(TAG, msg);
    }

    public static void i(String msg) {
            Log.i(TAG, msg);
    }

    public static void w(String msg) {
            Log.w(TAG, msg);
    }

    public static void e(String msg) {
            Log.e(TAG, msg);
    }


    public static void v(String msg) {
            Log.v(TAG, msg);
    }
}
