package com.hybunion.yirongma.common.util;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.hybunion.yirongma.HRTApplication;

/**
 * @author SunBingbing
 * @date 2017/3/27
 * @email freemars@yeah.net
 * @description 获取系统资源工具类
 */

public class GetResourceUtil {

    /**
     * 获取颜色资源值
     * @param colorId 颜色资源 Id
     * @return 颜色值
     */
    public static int getColor(int colorId){
        return HRTApplication.getInstance().getResources().getColor(colorId);
    }

    /**
     * 获取图片资源值
     * @param drawableId 图片资源 Id
     * @return 图片 Drawable
     */
    public static Drawable getDrawable(int drawableId){
        return ContextCompat.getDrawable(HRTApplication.getInstance(),drawableId);
    }

    /**
     * 获取字符串
     * @param stringId 字符串资源Id
     * @return 字符串
     */
    public static String getString(int stringId){
        return HRTApplication.getInstance().getResources().getString(stringId);
    }
}
