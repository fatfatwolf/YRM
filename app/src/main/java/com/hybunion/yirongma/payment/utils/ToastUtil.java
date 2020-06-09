package com.hybunion.yirongma.payment.utils;

import android.content.Context;
import android.widget.Toast;

import com.hybunion.yirongma.HRTApplication;

/**
 * @author  SunBingbing
 * @date  2017/3/27
 * @email  bingbing_sun@yeah.net
 * @description Toast 工具类(待改进，准备使用自定义布局)
 */
public class ToastUtil {

    /**
     * 3s Toast 展示
     * @param msg 提示内容
     */
    public static void show(String msg){
        Toast.makeText(HRTApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 5s Toast 展示
     * @param msg 提示内容
     */
    public static void longShow(String msg){
        Toast.makeText(HRTApplication.getInstance(),msg,Toast.LENGTH_LONG).show();
    }




    public static void shortShow(Context context, String msg) {
        Toast.makeText(HRTApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }

    public static void shortShowToast(Context context, String msg) {
        Toast.makeText(HRTApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }
}
