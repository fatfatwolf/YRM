package com.hybunion.yirongma.common.util;

import java.util.Calendar;

/**
 * @author SunBingbing
 * @date 2017/4/17
 * @email freemars@yeah.net
 * @description 时间格式化工具
 */

public class FormatDateUtil {

    /**
     * 将年月日时分秒的时间格式转换为“xx月xx日”的形式
     * 例如：2016-11-28 13:18:41 --> 11月28日
     */
    public static String getFormatTime(String realTime) {
        if (realTime == null || "".equals(realTime)) {
            return "未知";
        }
        Calendar calendar = Calendar.getInstance();
        int systemYear = calendar.get(Calendar.YEAR);
        String year = realTime.substring(0, 4);
        String month = realTime.substring(5, 7);
        String day = realTime.substring(8, 10);
        int currentYear = Integer.parseInt(year);
        if (systemYear > currentYear) {
            return year + "年" + month + "月" + day + "日";
        } else {
            return month + "月" + day + "日";
        }
    }

    /**
     * 将年月日时分秒的时间格式转换为“xx月xx日”的形式
     * 例如：2016-11-28 13:18:41 --> 11月28日 xx:xx
     */
    public static String getLongFormatTime(String realTime) {
        if (realTime == null || "".equals(realTime)) {
            return "未知";
        }
        Calendar calendar = Calendar.getInstance();
        int systemYear = calendar.get(Calendar.YEAR);
        String year = realTime.substring(0, 4);
        String month = realTime.substring(5, 7);
        String day = realTime.substring(8, 10);
        String minute = realTime.substring(11,13);
        String second = realTime.substring(14,16);
        int currentYear = Integer.parseInt(year);
        if (systemYear > currentYear) {
            return year + "年" + month + "月" + day + "日 " + minute +":"+ second;
        } else {
            return month + "月" + day + "日 "+ minute +":"+ second;
        }
    }


}
