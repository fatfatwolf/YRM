package com.hybunion.netlibrary.utils;

import android.util.Log;

import com.hybunion.netlibrary.UtilsLib;

public class LogUtil {

    private static final String TAG = "hrt";


    public static void d(String msg) {
        if (UtilsLib.isDebug()) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (UtilsLib.isDebug()) {
            Log.d(TAG, tag + ": " + msg);
        }
    }

    public static void e(String msg) {
        if (UtilsLib.isDebug())
            Log.e(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (UtilsLib.isDebug()) {
            Log.e(TAG, tag + ": " + msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (UtilsLib.isDebug()) {
            Log.e(TAG, tag + ": " + msg, e);
        }
    }

    public static void i(String msg) {
        if (UtilsLib.isDebug())
            Log.i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (UtilsLib.isDebug())
            Log.i(TAG, tag + ": " + msg);
    }


}
