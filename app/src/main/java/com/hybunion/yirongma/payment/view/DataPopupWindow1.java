package com.hybunion.yirongma.payment.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 首页 数据 界面的 PopupWindow
 * 时间日期选择控件用的系统自带的控件，不能选 秒
 */

public class DataPopupWindow1 extends PopupWindow implements View.OnClickListener {
    private TextView mTvToday;
    private TextView mTvYesterday;
    private TextView mTvThisWeek;
    private TextView mTvThisMonth;
    private RelativeLayout mStartDateParent;
    private RelativeLayout mEndDateParent;
    private TextView mTvReset;
    private TextView mTvOk;
    private TextView mTvStartDate, mTvEndDate;
    private String clickColor = "#f74948";
    private String unClickColor = "#252e44";
    private DatePickerDialog mDatePickerDialog;
    private Context mContext;
//    private String mStartTimeStr;
//    private String mEndTimeStr;
    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    private int mStartHour, mStartMinute, mStartSecond, mEndHour, mEndMinute, mEndSecond;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;  // 设置筛选控件初始值用
    private String mStartPickedTime = ""; // 时间选择器选择完毕后的 开始 时间
    private String mEndPickedTime = "";   // 时间选择器选择完毕后的 结束 时间
    private OnDataPopWindowListener mListener;
    private String mNameStr; // 外面显示的文字

    private DataPopupWindow1(View contentView, int width, int height) {
        super(contentView, width, height);
        setFocusable(true);
        if (contentView != null) {
            initView(contentView);
        }
    }

    public DataPopupWindow1(Context context, View contentView, int width, int height) {
        this(contentView, width, height);
        mContext = context;
    }

    private boolean mIsPickTime = true;

    // 是否选择时分秒  如果为 false， 这选择年月日，不选择时分秒
    public void isPickTime(boolean isPickTime) {
        mIsPickTime = isPickTime;
    }

    // 初始化控件
    private void initView(View contentView) {
        mTvToday = (TextView) contentView.findViewById(R.id.tvToday_popupwindow_data_activity);
        mTvYesterday = (TextView) contentView.findViewById(R.id.tvYesterday_popupwindow_data_activity);
        mTvThisWeek = (TextView) contentView.findViewById(R.id.tvThisWeek_popupwindow_data_activity);
        mTvThisMonth = (TextView) contentView.findViewById(R.id.tvThisMonth_popupwindow_data_activity);
        mStartDateParent = (RelativeLayout) contentView.findViewById(R.id.startDateParent_popupwindow_data_activity);
        mEndDateParent = (RelativeLayout) contentView.findViewById(R.id.endDateParent_popupwindow_data_activity);
        mTvStartDate = (TextView) contentView.findViewById(R.id.startDate_popupwindow_data_activity);
        mTvEndDate = (TextView) contentView.findViewById(R.id.endDate_popupwindow_data_activity);
        mTvReset = (TextView) contentView.findViewById(R.id.reset_popupwindow_data_activity);
        mTvOk = (TextView) contentView.findViewById(R.id.ok_popupwindow_data_activity);
        mTvToday.setOnClickListener(this);
        mTvYesterday.setOnClickListener(this);
        mTvThisWeek.setOnClickListener(this);
        mTvThisMonth.setOnClickListener(this);
        mStartDateParent.setOnClickListener(this);
        mEndDateParent.setOnClickListener(this);
        mTvReset.setOnClickListener(this);
        mTvOk.setOnClickListener(this);

    }

    // 选择时间
    MyDateTimePickDialog mStartPickDialog, mEndPickDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvToday_popupwindow_data_activity:           // 今日
                clearState();
                mTvToday.setTextColor(Color.parseColor(clickColor));
                mTvToday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                handleFourButton(0);
                break;
            case R.id.tvYesterday_popupwindow_data_activity:      // 昨日
                clearState();
                mTvYesterday.setTextColor(Color.parseColor(clickColor));
                mTvYesterday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                handleFourButton(1);
                break;
            case R.id.tvThisWeek_popupwindow_data_activity:       // 本周
                clearState();
                mTvThisWeek.setTextColor(Color.parseColor(clickColor));
                mTvThisWeek.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                handleFourButton(2);
                break;
            case R.id.tvThisMonth_popupwindow_data_activity:      // 本月
                clearState();
                mTvThisMonth.setTextColor(Color.parseColor(clickColor));
                mTvThisMonth.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                handleFourButton(3);
                break;
            case R.id.startDateParent_popupwindow_data_activity:   // 开始时间
//                showDatePickDialog(true);
                if (mStartPickDialog == null)
                    mStartPickDialog = new MyDateTimePickDialog(mContext);
                // 每次打开的初始值是 tvStartTime 上显示的值。
                int[] startDateAndTime = getDateAndTime(mTvStartDate.getText().toString().trim());
                if (startDateAndTime.length==6){
                    mStartPickDialog.setDateAndTime(startDateAndTime[0],startDateAndTime[1],startDateAndTime[2],startDateAndTime[3],startDateAndTime[4],startDateAndTime[5]);
                }
                mStartPickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        clearState();
                        String startTime = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
//                        mStartTimeStr = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
                        mNameStr = startTime + "至" + mEndPickedTime;
                        mTvStartDate.setText(startTime);
                        mStartPickDialog.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mStartPickDialog.dismiss();
                    }
                });
                break;
            case R.id.endDateParent_popupwindow_data_activity:     // 结束时间
//                showDatePickDialog(false);
                if (mEndPickDialog == null)
                    mEndPickDialog = new MyDateTimePickDialog(mContext);
                int[] endDateAndTime = getDateAndTime(mTvEndDate.getText().toString().trim());
                if (endDateAndTime.length==6){
                    mEndPickDialog.setDateAndTime(endDateAndTime[0],endDateAndTime[1],endDateAndTime[2],endDateAndTime[3],endDateAndTime[4],endDateAndTime[5]);
                }

                mEndPickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        clearState();
                        String endTime = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
                        mNameStr = mStartPickedTime + "至" + endTime;
                        mTvEndDate.setText(endTime);
                        mEndPickDialog.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mEndPickDialog.dismiss();
                    }
                });
                break;
            case R.id.reset_popupwindow_data_activity:     // 重置
                if (mListener != null)
                    mListener.onPickReset();
                reset4Button(true);
                break;
            case R.id.ok_popupwindow_data_activity:        // 确定
                if (mListener != null) {
                    mListener.onPickFinish(mTvStartDate.getText().toString().trim(), mTvEndDate.getText().toString(), mNameStr);
                }
                break;
        }

    }

    // 清空“今日”等按钮的选中状态
    private void clearState(){
        mTvToday.setTextColor(Color.parseColor(unClickColor));
        mTvYesterday.setTextColor(Color.parseColor(unClickColor));
        mTvThisWeek.setTextColor(Color.parseColor(unClickColor));
        mTvThisMonth.setTextColor(Color.parseColor(unClickColor));
        mTvToday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvYesterday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvThisWeek.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvThisMonth.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
    }

    /**
     * 设置 “今日” 等按钮的选中状态
     * @param timeName
     */
    public void setState(String timeName){
        clearState();
        if (mTvToday.getText().toString().equals(timeName)){
            mTvToday.setTextColor(Color.parseColor(clickColor));
            mTvToday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
        }else if (mTvYesterday.getText().toString().equals(timeName)){
            mTvYesterday.setTextColor(Color.parseColor(clickColor));
            mTvYesterday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
        }else if (mTvThisWeek.getText().toString().equals(timeName)){
            mTvThisWeek.setTextColor(Color.parseColor(clickColor));
            mTvThisWeek.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
        }else if (mTvThisMonth.getText().toString().equals(timeName)){
            mTvThisMonth.setTextColor(Color.parseColor(clickColor));
            mTvThisMonth.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
        }

    }

    private int[] getDateAndTime(String dateAndTime){
        if (TextUtils.isEmpty(dateAndTime)) return null;
        String[] dateAndTimeStrs = dateAndTime.split(" ");
        String dateStr = dateAndTimeStrs[0];
        String timeStr = dateAndTimeStrs[1];
        String[] dateStrs = dateStr.split("-");
        String[] timeStrs = timeStr.split(":");
        String[] returnStrs = new String[(dateStrs.length+timeStrs.length)];
        for(int i=0;i<dateStrs.length;i++){
            returnStrs[i] = dateStrs[i];
        }
        for (int i=0;i<timeStrs.length;i++){
            returnStrs[(dateStrs.length+i)] = timeStrs[i];
        }
        int[] returnInts = new int[returnStrs.length];
        for (int i=0;i<returnStrs.length;i++){
            if (returnStrs[i].matches("[0-9]+")){
                returnInts[i] = Integer.parseInt(returnStrs[i]);
            }else{
                returnInts[i] = 0;
            }
        }
        return returnInts;

    }

    // 外部调用此方法，设置 上一次的 开始结束时间
    // 如果是第一次打开，设置 开始时间、结束时间为今天，时间格式为  2018-06-09 23:59:59
    public void showThisPopWindow(View view, String startTime, String endTime, OnDataPopWindowListener listener) {
        mListener = listener;
        if (mIsPickTime){
            mStartPickedTime = startTime;  // 选择后的开始结束时间，初始值是传进来的时间。
            mEndPickedTime = endTime;
        }else{
            mStartPickedTime = startTime.substring(0,10);  // 截出年月日
            mEndPickedTime = endTime.substring(0,10);
        }

        mTvStartDate.setText(mStartPickedTime);
        mTvEndDate.setText(mEndPickedTime);
//        if (!mIsPickTime) {
//            String[] startDates = startTime.split(" ");
//            String[] endDates = endTime.split(" ");
//            if (startDates != null && startDates.length == 2) {
//                mTvStartDate.setText(startDates[0]);
//                mStartTimeStr = startDates[0];
//            }
//            if (endDates != null && endDates.length == 2) {
//                mTvEndDate.setText(endDates[0]);
//                mEndTimeStr = endDates[0];
//            }


//        } else {
//            mTvStartDate.setText(startTime);
//            mTvEndDate.setText(endTime);
//            mStartTimeStr = startTime;  // 保存，留作复位用。
//            mEndTimeStr = endTime;
//        }
        getIntDateAndTime(true, startTime);
        getIntDateAndTime(false, endTime);
        mNameStr = startTime + " 至 " + endTime;
        this.showAsDropDown(view);
    }

    // 将传进来的开始，结束时间，转换成 int 年月日，供DatePickerDialog 和 TimePickerDialog 使用。
    private void getIntDateAndTime(boolean isStartTime, String time) {
        if (!TextUtils.isEmpty(time)) {
            String[] dateAndTime = time.split(" ");
            if (dateAndTime != null && dateAndTime.length == 2) {
                // 将年月日拆分开
                String dateStr = dateAndTime[0];
                String[] dates = dateStr.split("-");
                if (dates != null && dates.length == 3) {
                    String yearStr = dates[0];
                    String monthStr = dates[1];
                    String dayStr = dates[2];
                    if (yearStr.matches("[0-9]+") && monthStr.matches("[0-9]+") && dayStr.matches("[0-9]+")) { // 保证截出的年月日是数字
                        if (isStartTime) {
                            mStartYear = Integer.parseInt(yearStr);
                            mStartMonth = Integer.parseInt(monthStr);
                            mStartDay = Integer.parseInt(dayStr);
                        } else {
                            mEndYear = Integer.parseInt(yearStr);
                            mEndMonth = Integer.parseInt(monthStr);
                            mEndDay = Integer.parseInt(dayStr);
                        }
                    }
                }
                if (!isStartTime) {
                    // 将 时分秒 拆分开
                    String timeStr = dateAndTime[1];
                    String[] times = timeStr.split(":");
                    if (times != null && times.length == 3) {
                        String hourStr = times[0];
                        String minuteStr = times[1];
                        String secondStr = times[2];
                        if (hourStr.matches("[0-9]+") && minuteStr.matches("[0-9]+") && secondStr.matches("[0-9]+")) {
                            mEndHour = Integer.parseInt(hourStr);
                            mEndMinute = Integer.parseInt(minuteStr);
                            mEndSecond = Integer.parseInt(secondStr);

                        }
                    }
                } else {  // 开始时间的时分秒默认都是 0
                    mStartHour = mStartMinute = mStartSecond = 0;
                }

            }
        }
    }

    // type: 0-今日  1-昨日  2-本周   3-本月
    private void handleFourButton(int type) {
        long todayLong = System.currentTimeMillis();
        switch (type) {
            case 0:    // 今日
                mStartPickedTime = stringDateFormat(true, false, todayLong, mIsPickTime);
                mEndPickedTime = stringDateFormat(false, false, todayLong, mIsPickTime);
                mNameStr = "今日";
                break;
            case 1:     // 昨日
                long yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
                mStartPickedTime = stringDateFormat(true, true, yesterday, mIsPickTime);
                mEndPickedTime = stringDateFormat(false, true, yesterday, mIsPickTime);
                mNameStr = "昨日";
                break;
            case 2:    // 本周
                Date date = new Date();
                if (mCalendar == null)
                    mCalendar = Calendar.getInstance();
                mCalendar.setTime(date);
                int dayofweek = mCalendar.get(Calendar.DAY_OF_WEEK);
                if (dayofweek == 1) {  // 从 Sunday 开始，Sunday = 1
                    dayofweek += 7;
                }
                mCalendar.add(Calendar.DATE, 2 - dayofweek);
                long timeInMillis = mCalendar.getTimeInMillis();
                mStartPickedTime = stringDateFormat(true, false, timeInMillis, mIsPickTime);
                mEndPickedTime = stringDateFormat(false, false, todayLong, mIsPickTime);
                mNameStr = "本周";
                break;
            case 3:    // 本月
                if (mCalendar == null)
                    mCalendar = Calendar.getInstance();
                mCalendar.set(getNowYear(), getNowMonth() - 1, 1);
                long timeInMillis1 = mCalendar.getTimeInMillis();
                mStartPickedTime = stringDateFormat(true, false, timeInMillis1, mIsPickTime);
                mEndPickedTime = stringDateFormat(false, false, todayLong, mIsPickTime);
                mNameStr = "本月";
                break;
        }


        mTvStartDate.setText(mStartPickedTime);
        mTvEndDate.setText(mEndPickedTime);
//        mTimeStr = mStartPickedTime + " 至 " + mEndPickedTime;
    }

    private Calendar mCalendar;

    // isPickTime false - HRT 钱包——提现记录 中的筛选用。（此筛选，时间不需要时分秒）
    private String stringDateFormat(boolean isStart, boolean isYesterday, long date, boolean isPickTime) {
        SimpleDateFormat format;
        if (!isPickTime) {
            format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = new Date(date);
            return format.format(date1);
        } else {
            if (isStart || isYesterday)
                format = new SimpleDateFormat("yyyy-MM-dd");
            else
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date1 = new Date();
            date1.setTime(date);
            String dateAndTime = format.format(date1);
            if (isStart)
                dateAndTime += " " + "00:00:00";
            if (!isStart && isYesterday)
                dateAndTime += " " + "23:59:59";
            return dateAndTime;
        }


    }

    //获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(Calendar.YEAR));
    }

    //获取本月是哪一月
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(Calendar.MONTH) + 1;
    }

    // 默认选中 今日
    private void reset4Button(boolean isReset) {
        mTvToday.setTextColor(Color.parseColor(unClickColor));
        mTvYesterday.setTextColor(Color.parseColor(unClickColor));
        mTvThisWeek.setTextColor(Color.parseColor(unClickColor));
        mTvThisMonth.setTextColor(Color.parseColor(unClickColor));
        mTvToday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvYesterday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvThisWeek.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvThisMonth.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        if (isReset) {
            String startTimeDefault, endTimeDefault;  // 默认今天
            String time = YrmUtils.getNowDay("yyyy-MM-dd");
            if (mIsPickTime){
                startTimeDefault = time+" 00:00:00";
                endTimeDefault = time+" 23:59:59";
            }else{
                startTimeDefault = time;
                endTimeDefault = time;
            }
            mTvStartDate.setText(startTimeDefault);
            mTvEndDate.setText(endTimeDefault);
        }


    }


    public interface OnDataPopWindowListener {
        void onPickFinish(String startPickedTime, String endPickedTime, String nameStr);   // 确定

        void onPickReset();    // 重置
    }


}
