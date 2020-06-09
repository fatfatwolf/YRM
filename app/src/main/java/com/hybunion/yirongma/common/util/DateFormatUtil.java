package com.hybunion.yirongma.common.util;

/**
 * @author SunBingbing
 * @date 2017/3/2
 * @email freemars@yeah.net
 * @description 将日期装换成标准的请求格式（yyyy-MM-dd）
 */

public class DateFormatUtil {

    /**
     * 将日期装换成标准的请求格式（yyyy-MM-dd）
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 日期的标准请求格式
     */
    public static String formatDate(int year, int month, int day) {

        String monthFormat = month + "";

        //月分数不足两位的前面加“0”
        if (month < 10) {
            monthFormat = "0" + monthFormat;
        }

        String dayFormat = day + "";
        //日期不足两位的前面加“0”
        if (day < 10) {
            dayFormat = "0" + dayFormat;
        }
        return year + "-" + monthFormat + "-" + dayFormat;
    }
}
