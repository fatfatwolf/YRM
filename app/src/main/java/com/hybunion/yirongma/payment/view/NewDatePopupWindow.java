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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 首页 数据 界面的 PopupWindow
 * 时间日期选择控件用的自定义控件，可以选年月日时分秒
 */

public class NewDatePopupWindow extends PopupWindow implements View.OnClickListener {
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
    private String mStartDateAndTimeStr;
    private String mEndDateAndTimeStr;
    private int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    private int mStartHour, mStartMinute, mStartSecond, mEndHour, mEndMinute, mEndSecond;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;  // 设置筛选控件初始值用
    private String mStartPickedTime = ""; // 时间选择器选择完毕后的 开始 时间
    private String mEndPickedTime = "";   // 时间选择器选择完毕后的 结束 时间
    private OnDataPopWindowListener mListener;
    private String mNameStr; // 外面显示的文字
    private MyDateTimePickDialog mStartDateTimePickDialog, mEndDateTimePickDialog;


    private NewDatePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        setFocusable(true);
        if (contentView != null) {
            initView(contentView);
        }
    }

    public NewDatePopupWindow(Context context, View contentView, int width, int height) {
        this(contentView, width, height);
        mContext = context;
    }

    // 传入 开始时间 和 结束时间，如果不传，默认 今天
    public NewDatePopupWindow(Context context, View contentView, int width, int height, String startDateAndTime, String endDateAndTime) {
        this(context, contentView, width, height);
        mStartDateAndTimeStr = startDateAndTime;  // 用于缓存，重置用
        mEndDateAndTimeStr = endDateAndTime;
        mStartPickedTime = startDateAndTime;  // 一开始赋值，防止没有选择直接点击确定。
        mEndPickedTime = endDateAndTime;

        mTvStartDate.setText(mStartDateAndTimeStr);
        mTvEndDate.setText(mEndDateAndTimeStr);
    }

    // 初始化控件
    private void initView(View contentView) {
        mTvToday = contentView.findViewById(R.id.tvToday_popupwindow_data_activity);
        mTvYesterday = contentView.findViewById(R.id.tvYesterday_popupwindow_data_activity);
        mTvThisWeek = contentView.findViewById(R.id.tvThisWeek_popupwindow_data_activity);
        mTvThisMonth = contentView.findViewById(R.id.tvThisMonth_popupwindow_data_activity);
        mStartDateParent = contentView.findViewById(R.id.startDateParent_popupwindow_data_activity);
        mEndDateParent = contentView.findViewById(R.id.endDateParent_popupwindow_data_activity);
        mTvStartDate = contentView.findViewById(R.id.startDate_popupwindow_data_activity);
        mTvEndDate = contentView.findViewById(R.id.endDate_popupwindow_data_activity);
        mTvReset = contentView.findViewById(R.id.reset_popupwindow_data_activity);
        mTvOk = contentView.findViewById(R.id.ok_popupwindow_data_activity);
        mTvToday.setOnClickListener(this);
        mTvYesterday.setOnClickListener(this);
        mTvThisWeek.setOnClickListener(this);
        mTvThisMonth.setOnClickListener(this);
        mStartDateParent.setOnClickListener(this);
        mEndDateParent.setOnClickListener(this);
        mTvReset.setOnClickListener(this);
        mTvOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvToday_popupwindow_data_activity:           // 今日
                mTvToday.setTextColor(Color.parseColor(clickColor));
                mTvYesterday.setTextColor(Color.parseColor(unClickColor));
                mTvThisWeek.setTextColor(Color.parseColor(unClickColor));
                mTvThisMonth.setTextColor(Color.parseColor(unClickColor));
                mTvToday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                mTvYesterday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                mTvThisWeek.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                mTvThisMonth.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                handleFourButton(0);
                break;
            case R.id.tvYesterday_popupwindow_data_activity:      // 昨日
                mTvYesterday.setTextColor(Color.parseColor(clickColor));
                mTvToday.setTextColor(Color.parseColor(unClickColor));
                mTvThisWeek.setTextColor(Color.parseColor(unClickColor));
                mTvThisMonth.setTextColor(Color.parseColor(unClickColor));
                mTvYesterday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                mTvToday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                mTvThisWeek.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                mTvThisMonth.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                handleFourButton(1);
                break;
            case R.id.tvThisWeek_popupwindow_data_activity:       // 本周
                mTvThisWeek.setTextColor(Color.parseColor(clickColor));
                mTvToday.setTextColor(Color.parseColor(unClickColor));
                mTvYesterday.setTextColor(Color.parseColor(unClickColor));
                mTvThisMonth.setTextColor(Color.parseColor(unClickColor));
                mTvThisWeek.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                mTvToday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                mTvYesterday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                mTvThisMonth.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                handleFourButton(2);
                break;
            case R.id.tvThisMonth_popupwindow_data_activity:      // 本月
                mTvThisMonth.setTextColor(Color.parseColor(clickColor));
                mTvToday.setTextColor(Color.parseColor(unClickColor));
                mTvYesterday.setTextColor(Color.parseColor(unClickColor));
                mTvThisWeek.setTextColor(Color.parseColor(unClickColor));
                mTvThisMonth.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                mTvToday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                mTvYesterday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                mTvThisWeek.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
                handleFourButton(3);
                break;
            case R.id.startDateParent_popupwindow_data_activity:   // 开始时间
                showDateAndTimePickDialog(true);
                break;
            case R.id.endDateParent_popupwindow_data_activity:     // 结束时间
                showDateAndTimePickDialog(false);
                break;
            case R.id.reset_popupwindow_data_activity:     // 重置
                if (mListener != null)
                    mListener.onPickReset();
                reset4Button(true);
                break;
            case R.id.ok_popupwindow_data_activity:        // 确定
                if (mListener != null) {
                    mNameStr = mStartPickedTime+" ~ "+mEndPickedTime;
                    mListener.onPickFinish(mStartPickedTime, mEndPickedTime, mNameStr);
                }
                break;
        }

    }

    // show 时间日期选择 Dialog
    // isStart  用来标识 点击的是开始时间还是结束时间
    private void showDateAndTimePickDialog(boolean isStart) {
        if (isStart) {
            if (mStartDateTimePickDialog == null)
                mStartDateTimePickDialog = new MyDateTimePickDialog(mContext);
            String startTime = mTvStartDate.getText().toString().trim();
            if (!TextUtils.isEmpty(startTime)) {
                int[] startDateTimes = getDateAndTime(startTime);
                if (startDateTimes.length == 6) {
                    mStartDateTimePickDialog.setDateAndTime(startDateTimes[0], startDateTimes[1], startDateTimes[2], startDateTimes[3], startDateTimes[4], startDateTimes[5]);
                }
                mStartDateTimePickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        mStartPickedTime = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
//                        ToastUtil.show("选择的开始时间是：" + mStartPickedTime);
                        mTvStartDate.setText(mStartPickedTime);
                        if (mStartDateTimePickDialog!=null)
                            mStartDateTimePickDialog.dismiss();
                        reset4Button(false);
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        if (mStartDateTimePickDialog!=null)
                            mStartDateTimePickDialog.dismiss();
                    }
                });
            }
        } else {
            if (mEndDateTimePickDialog == null)
                mEndDateTimePickDialog = new MyDateTimePickDialog(mContext);
            String startTime = mTvEndDate.getText().toString().trim();
            if (!TextUtils.isEmpty(startTime)) {
                int[] endDateTimes = getDateAndTime(startTime);
                if (endDateTimes.length == 6) {
                    mEndDateTimePickDialog.setDateAndTime(endDateTimes[0], endDateTimes[1], endDateTimes[2],
                            endDateTimes[3], endDateTimes[4], endDateTimes[5]);
                }
                mEndDateTimePickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        mEndPickedTime = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
//                        ToastUtil.show("选择的结束时间是：" + mEndPickedTime);
                        mTvEndDate.setText(mEndPickedTime);
                        if (mEndDateTimePickDialog!=null)
                            mEndDateTimePickDialog.dismiss();
                        reset4Button(false);

                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        if (mEndDateTimePickDialog!=null)
                            mEndDateTimePickDialog.dismiss();
                    }
                });
            }
        }


    }

    private int[] getDateAndTime(String dateAndTime) {
        if (TextUtils.isEmpty(dateAndTime)) return null;
        String[] dateAndTimeStrs = dateAndTime.split(" ");
        String dateStr = dateAndTimeStrs[0];
        String timeStr = dateAndTimeStrs[1];
        String[] dateStrs = dateStr.split("-");
        String[] timeStrs = timeStr.split(":");
        String[] returnStrs = new String[(dateStrs.length + timeStrs.length)];
        for (int i = 0; i < dateStrs.length; i++) {
            returnStrs[i] = dateStrs[i];
        }
        for (int i = 0; i < timeStrs.length; i++) {
            returnStrs[(dateStrs.length + i)] = timeStrs[i];
        }
        int[] returnInts = new int[returnStrs.length];
        for (int i = 0; i < returnStrs.length; i++) {
            if (returnStrs[i].matches("[0-9]+")) {
                returnInts[i] = Integer.parseInt(returnStrs[i]);
            } else {
                returnInts[i] = 0;
            }
        }
        return returnInts;

    }


    // 外部调用此方法，设置 上一次的 开始结束时间
    // 如果是第一次打开，设置 开始时间、结束时间为今天，时间格式为  2018-06-09 23:59:59
    public void showThisPopWindow(View view, OnDataPopWindowListener listener) {
        mListener = listener;

        this.showAsDropDown(view);
    }

    // type: 0-今日  1-昨日  2-本周   3-本月
    private void handleFourButton(int type) {
        long todayLong = System.currentTimeMillis();
        switch (type) {
            case 0:    // 今日
                mStartPickedTime = stringDateFormat(true, false, todayLong);
                mEndPickedTime = stringDateFormat(false, false, todayLong);
                mNameStr = "今日";
                break;
            case 1:     // 昨日
                long yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
                mStartPickedTime = stringDateFormat(true, true, yesterday);
                mEndPickedTime = stringDateFormat(false, true, yesterday);
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
                mStartPickedTime = stringDateFormat(true, false, timeInMillis);
                mEndPickedTime = stringDateFormat(false, false, todayLong);
                mNameStr = "本周";
                break;
            case 3:    // 本月
                if (mCalendar == null)
                    mCalendar = Calendar.getInstance();
                mCalendar.set(getNowYear(), getNowMonth() - 1, 1);
                long timeInMillis1 = mCalendar.getTimeInMillis();
                mStartPickedTime = stringDateFormat(true, false, timeInMillis1);
                mEndPickedTime = stringDateFormat(false, false, todayLong);
                mNameStr = "本月";
                break;
        }


        mTvStartDate.setText(mStartPickedTime);
        mTvEndDate.setText(mEndPickedTime);
//        mTimeStr = mStartPickedTime + " 至 " + mEndPickedTime;
    }

    private Calendar mCalendar;

    // isPickTime false - HRT 钱包——提现记录 中的筛选用。（此筛选，时间不需要时分秒）
    private String stringDateFormat(boolean isStart, boolean isYesterday, long date) {
        SimpleDateFormat format;
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
            mTvStartDate.setText(mStartDateAndTimeStr);
            mTvEndDate.setText(mEndDateAndTimeStr);
        }


    }


    public interface OnDataPopWindowListener {
        void onPickFinish(String startPickedTime, String endPickedTime, String nameStr);   // 确定
        void onPickReset();    // 重置
    }


}
