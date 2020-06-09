package com.hybunion.yirongma.payment.utils;
import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class SharedPreferencesUtil {
    private static final String LOGIN_INFO = "config";
    public static final String AGENT_ID = "agentId";
    private static Editor editor;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferencesUtil spUtil;
    public static final String longitude = "longitude";
    public static final String latitude = "latitude";
    //储存是否进行图片切换的状态
    private static final String UPDATE_PIC_STATUS = "update_pic_status";
    //记录是否在1月23日当天重启过 App
    private static final String IS_OPEN_APP = "openApp";
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    public SharedPreferencesUtil() {

    }

    public static SharedPreferencesUtil getInstance(Context context) {
//        context = context.getApplicationContext();

        if (spUtil == null) {
            spUtil = new SharedPreferencesUtil();
            sharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return spUtil;
    }

    public boolean putKey(String key, String value) {
        if(!TextUtils.isEmpty(key)){
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    public boolean putKey(String key, int value){
        if(!TextUtils.isEmpty(key)){
            editor.putInt(key, value);
            return editor.commit();
        }

        return  false;
    }

    public boolean putKey(String key, float value){
        if(!TextUtils.isEmpty(key)){
            editor.putFloat(key, value);
            return editor.commit();
        }

        return  false;
    }

    public String getKey(String key) {
        return sharedPreferences.getString(key, "");
    }

    public int getIntKey(String key){
        return sharedPreferences.getInt(key, 0);
    }
    public int getIntKey1(String key){
        return sharedPreferences.getInt(key, -1);
    }
    public float getFloatKey(String key){
        return sharedPreferences.getFloat(key,0f);
    }

    public boolean putBooleanKey(String key, Boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public Boolean getBooleanKey(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    public Boolean getBooleanKey(String key, boolean def) {
        return sharedPreferences.getBoolean(key, def);
    }

    /**
     * 记录极光推送信息
     * @param context 上下文
     * @param key 键
     * @param value 值
     */
    public static void setNewSP(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("jPush",Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * 从SP文件中获取值
     * @param context 上下文
     * @param key 键
     * @return 值
     *
     */
    public static String  getAlias(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("jPush",Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    /**
     * 保存是否更换图片的状态
     * @param context 上下文
     * @param key 保存图片状态的 key
     * @param status 需要保存的图片状态
     * @return true:保存成功，false:保存失败
     */
    public static boolean setPicStatus(Context context,String key,String status){
        //SharePreferences 的名字是：update_pic_url
        SharedPreferences sharedPreferences = context.getSharedPreferences(UPDATE_PIC_STATUS,Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key,status);
        return editor.commit();
    }

    /**
     * 从 SharedPreferences 中获取保存的图片状态
     * @param context 上下文
     * @param key 保存图片状态的 key
     * @param defaultValue 返回的默认值
     * @return 保存的图片状态
     */
    public static String getPicStatus(Context context,String key,String defaultValue){
        //SharePreferences 的名字是：update_pic_url
        SharedPreferences sharedPreferences = context.getSharedPreferences(UPDATE_PIC_STATUS,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,defaultValue);
    }

    /**
     * 保存是否在1月23日当天重启过 App
     */
    public static boolean setIsOpen23(Context context,String key,String status){
        //SharePreferences 的名字是：update_pic_url
        SharedPreferences sharedPreferences = context.getSharedPreferences(IS_OPEN_APP,Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key,status);
        return editor.commit();
    }

    /**
     * 是否在1月23日当天重启过 App
     */
    public static String getIsOpen23(Context context,String key,String defaultValue){
        //SharePreferences 的名字是：update_pic_url
        SharedPreferences sharedPreferences = context.getSharedPreferences(IS_OPEN_APP,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,defaultValue);
    }



}
