package com.hybunion.yirongma.common.util;

import com.hybunion.yirongma.payment.utils.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author SunBingbing
 * @date 2017/3/2
 * @email freemars@yeah.net
 * @description 比较两个日期是否合法（开始时间不能晚于结束时间）
 * 时间格式是：xxxx-xx-xx 和 xxxx-xx-xx (例如:2017-03-08)
 */

public class DateCompareUtil {

    public static boolean futureTime = false;

    public static boolean compareDate(String startDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        long startTime = 0;
        long endTime = 0;
        long curTime = System.currentTimeMillis();
        try {
            startTime = formatter.parse(startDate).getTime();
            endTime = formatter.parse(endDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LogUtil.e("获取显示日期" + startDate + "\n" + endDate);
        int startYear = Integer.valueOf(startDate.substring(0, 4));
        int endYear = Integer.valueOf(endDate.substring(0, 4));
        if (startYear > endYear) {
            futureTime = false;
            return false;
        } else if (startYear == endYear) {
            int startMonth = Integer.valueOf(startDate.substring(5, 7));
            int endMonth = Integer.valueOf(endDate.substring(5, 7));
            if (startMonth > endMonth) {
                futureTime = false;
                return false;
            } else if (startMonth == endMonth) {
                int startDay = Integer.valueOf(startDate.substring(8));
                int endDay = Integer.valueOf(endDate.substring(8));
                if (startDay > endDay) {
                    futureTime = false;
                    return false;
                }
            }
        }
        if (startTime > curTime || endTime > curTime) {
            futureTime = true;
            return false;
        }

        return true;
    }
}
