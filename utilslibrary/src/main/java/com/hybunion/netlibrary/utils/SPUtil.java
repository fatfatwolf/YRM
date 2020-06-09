package com.hybunion.netlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hybunion.netlibrary.UtilsLib;


// SharedPreferences 工具类
public class SPUtil {
    private static String CONFIG = "config";

    private static SharedPreferences mSp;

    static {
        mSp = UtilsLib.getContext().getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
    }

    /**
     * boolean类型
     */
    public static void putBoolean(String key, boolean value) {
        mSp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    /**
     * String类型 ֵ
     */
    public static void putString(String key, String value) {
        mSp.edit().putString(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    /**
     * int类型 ֵ
     */
    public static void putInt(String key, int value) {
        mSp.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }


}
