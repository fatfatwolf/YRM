package com.hybunion.yirongma.payment.utils;

import android.util.Log;

/**
 * Created by lcy on 2015/7/9.
 */
public class LogUtils {
    public static void d(String tag, String msg) {
        if (Constant.DUIZHANG_DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void dlyj(String msg) {
        if (Constant.DUIZHANG_DEBUG) {
            Log.d("lyj", msg);
        }
    }


    public static void iking(String msg) {
        if (CommonMethod.DEBUG_ZHI) {
            Log.i("king", msg);
        }
    }


    public static void elcy(String msg) {
        if (Constant.DUIZHANG_DEBUG) {
            Log.e("lcy", msg);
        }
    }

    public static void d(String msg) {
        if (Constant.DUIZHANG_DEBUG) {
            Log.d("find", msg);
        }
    }

}
