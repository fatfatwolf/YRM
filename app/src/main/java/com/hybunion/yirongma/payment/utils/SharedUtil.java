package com.hybunion.yirongma.payment.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 */
public class SharedUtil {

    private static final String CONFIG = "config";
    private static SharedPreferences sharedPreferences;
    private static SharedUtil sharedUtil;

    public static SharedUtil getInstance(Context context){
        if(sharedUtil == null){
            sharedUtil = new SharedUtil();
                sharedPreferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        }
        return sharedUtil;
    }

    /**
     * boolean类型
     */
    public  void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public  boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * String类型 ֵ
     */
    public  void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public  String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public String getString(String key){
        return getString(key, "");
    }

    /**
     * int类型 ֵ
     */
    public  void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public  int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }


    public static void clearAll(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("PersonalRealName");
        editor.remove("PersonalIDNum");
        editor.remove("PersonalSettlementNumber");
        editor.remove("PersonalPaymentBank");
        editor.remove("PersonalAccountBankImage");
        editor.remove("pic4");
        editor.remove("pic5");
        editor.remove("pic6");
        editor.remove("CompanyBusinessLicense");
        editor.remove("CompanyBusinessCode");
        editor.remove("CompanyLegalPersonName");
        editor.remove("CompanyLegalPersonIDNum");
        editor.remove("CompanysettlementNumber");
        editor.remove("CompanyPaymentBank");
        editor.remove("CompanyAccountBranchBank");
        editor.remove("CompanyComPaymentLine");
        editor.remove("CompanyPaymentBankImg");
        editor.commit();
    }

}
