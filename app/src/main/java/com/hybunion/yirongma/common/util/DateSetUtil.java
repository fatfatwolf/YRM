package com.hybunion.yirongma.common.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * @author SunBingbing
 * @date 2017/3/2
 * @email freemars@yeah.net
 * @description 将日期填充到时间选择框
 */

public class DateSetUtil {

    // 日历对象
    private static Calendar mCalendar;

    /**
     * 通过时间选择器选择时间并比较时间是否合法（开始时间不能晚于结束时间）
     * @param context 上下文
     * @param datePicker 要填充时间的输入框
     * @param compareTv 要比较的输入框
     * @param type 类型（0 是和起始时间比较）
     * @param callback 回调（用于执行方法）
     * @return
     */
    public static void setDate(final Context context, final TextView datePicker, TextView compareTv, final int type, final DatePickerCallback callback){
        mCalendar = Calendar.getInstance();
        final String compareDate = compareTv.getText().toString().trim();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dateFormat = DateFormatUtil.formatDate(year,monthOfYear+1,dayOfMonth);
                if (0 == type){
                    if (DateCompareUtil.compareDate(compareDate,dateFormat)){
                        datePicker.setText(dateFormat);
                        callback.loadData();
                    }else {
                        callback.showErrorMessage();
                    }
                }else {
                    if (DateCompareUtil.compareDate(dateFormat,compareDate)){
                        datePicker.setText(dateFormat);
                        callback.loadData();
                    }else {
                        callback.showErrorMessage();
                    }
                }
            }
        },mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

    public interface DatePickerCallback {
        void loadData();
        void showErrorMessage();
    }
}
