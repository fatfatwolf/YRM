package com.hybunion.yirongma.common.util;

import java.util.Calendar;

/**
 * @author SunBingbing
 * @date 2017/3/2
 * @email freemars@yeah.net
 * @description 获取年月日时分秒的工具类
 */

public class DateTimeGetUtil {

    // 日历对象
    private static Calendar mCalendar;

    /**
     * 获取当前年份
     * @return 当前年份
     */
    public static int getYear(){
        mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * @return 当前月份
     */
    public static int getMonth(){
        mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日期
     * @return 当前日期
     */
    public static int getDay(){
        mCalendar = Calendar.getInstance();
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }
}
