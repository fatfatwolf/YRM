package com.hybunion.yirongma.payment.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.hybunion.yirongma.payment.view.MyDateTimePickDialog;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 惠储值列表的筛选界面
 */

public class HuiChuZhiScreeningActivity extends BasicActivity {
    @Bind(R.id.tvStoreName_huichuzhi_screening)
    TextView mTvStoreName;
    @Bind(R.id.arrow_select_huichuzhi_screening)
    ImageView mImgArrow;
    @Bind(R.id.tvToday_huichuzhi_screening)
    TextView mTvToday;
    @Bind(R.id.tvYesterday_huichuzhi_screening)
    TextView mTvYesterday;
    @Bind(R.id.tvThisWeek_huichuzhi_screening)
    TextView mTvThisWeek;
    @Bind(R.id.tvThisMonth_huichuzhi_screening)
    TextView mTvThisMonth;
    @Bind(R.id.startDate_huichuzhi_screening)
    TextView mTvStartTime;
    @Bind(R.id.endDate_huichuzhi_screening)
    TextView mTvEndTime;

    private String mLoginType;
    private boolean mIsBoss;  // 是否是老板
    private List<StoreManageBean.ObjBean> mStoreDataList;
    private MyBottonPopWindow mPopWindow;
    private int storePosition = -1;
    private String mSelectedStoreId, mSelectStoreName, mStoreId, mStoreName;
    private Calendar mCalendar;
    private SimpleDateFormat dateFormat, dateFormat1;
    private MyDateTimePickDialog mStartPickDialog;
    private String mStartTimeStr, mEndTimeStr;
    private String mNameStr;
    private MyDateTimePickDialog mEndPickDialog;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_huichuzhi_screening;
    }

    @Override
    public void initView() {
        super.initView();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 默认显示今天的时间
        mTvStartTime.setText(dateFormat.format(new Date(System.currentTimeMillis()))+" 00:00:00");
        mTvEndTime.setText(dateFormat.format(new Date(System.currentTimeMillis()))+" 23:59:59");

        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        mIsBoss = "0".equals(mLoginType);
        if (mIsBoss) {
            mTvStoreName.setText("所有门店");
        } else {
            mStoreId = SharedPreferencesUtil.getInstance(this).getKey("storeId");
            mStoreName = SharedPreferencesUtil.getInstance(this).getKey("storeName");
            mTvStoreName.setText(mStoreName);
            mImgArrow.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.startDateParent_huichuzhi_screening, R.id.endDateParent_huichuzhi_screening})
    public void chooseTime(RelativeLayout layout) {
        switch (layout.getId()) {
            case R.id.startDateParent_huichuzhi_screening:   // 选择开始时间
                if (mStartPickDialog == null)
                    mStartPickDialog = new MyDateTimePickDialog(HuiChuZhiScreeningActivity.this);
                // 每次打开的初始值是 tvStartTime 上显示的值。
                int[] startDateAndTime = getDateAndTime(mTvStartTime.getText().toString().trim());
                if (startDateAndTime.length==6){
                    mStartPickDialog.setDateAndTime(startDateAndTime[0],startDateAndTime[1],startDateAndTime[2],startDateAndTime[3],startDateAndTime[4],startDateAndTime[5]);
                }
                mStartPickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        clearTime();
                        mStartTimeStr = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
                        mNameStr = mStartTimeStr + "至" + mEndTimeStr;
                        mTvStartTime.setText(year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second);
                        mStartPickDialog.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mStartPickDialog.dismiss();
                    }
                });


                break;
            case R.id.endDateParent_huichuzhi_screening:     // 选择结束时间
                if (mEndPickDialog == null)
                    mEndPickDialog = new MyDateTimePickDialog(HuiChuZhiScreeningActivity.this);
                int[] endDateAndTime = getDateAndTime(mTvEndTime.getText().toString().trim());
                if (endDateAndTime.length==6){
                    mEndPickDialog.setDateAndTime(endDateAndTime[0],endDateAndTime[1],endDateAndTime[2],endDateAndTime[3],endDateAndTime[4],endDateAndTime[5]);
                }

                mEndPickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        clearTime();
                        mNameStr = mStartTimeStr + "至" + mEndTimeStr;
                        mEndTimeStr = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
                        mTvEndTime.setText(year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second);
                        mEndPickDialog.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mEndPickDialog.dismiss();
                    }
                });
                break;
        }
    }

    @OnClick({R.id.reset_huichuzhi_screening, R.id.ok_huichuzhi_screening})
    public void buttonClick(TextView tv) {
        switch (tv.getId()) {
            case R.id.reset_huichuzhi_screening:    // 重置
                clearTime();
                mTvStartTime.setText(mTodayStartTime);
                mTvEndTime.setText(mTodayEndTime);
                break;

            case R.id.ok_huichuzhi_screening:       // 确定
                try {
                    String startTime = mTvStartTime.getText().toString().trim();
                    String endTime = mTvEndTime.getText().toString().trim();
                    String[] startDateAndTime = mTvStartTime.getText().toString().trim().split(" ");
                    String startDateStr = startDateAndTime[0];
                    String[] endDateAndTime = mTvEndTime.getText().toString().trim().split(" ");
                    String endDateStr = endDateAndTime[0];
                    Date startdate = dateFormat.parse(startDateStr);
                    Date enddate = dateFormat.parse(endDateStr);
                    Date startDate1 = dateFormat1.parse(startTime);
                    Date endDate1 = dateFormat1.parse(endTime);
                    long betweenTime = enddate.getTime() - startdate.getTime();
                    long betweenTime1 = endDate1.getTime() - startDate1.getTime();
                    if (betweenTime1 < 0) {
                        ToastUtil.showShortToast("开始时间不能晚于结束时间");
                        return;
                    }
                    betweenTime = betweenTime / 1000 / 60 / 60 / 24;
                    if (betweenTime > 30) {
                        ToastUtil.showShortToast("开始时间与结束时间间隔不能大于31天");
                        return;
                    }

                    // 将筛选的 storeId 和 时间 传到列表页
                    HuiChuZhiScreeningListActivity.start(HuiChuZhiScreeningActivity.this,mStoreName, mStoreId, startTime, endTime);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private String clickColor = "#f74948";
    private String unClickColor = "#252e44";
    private String mTodayStartTime, mTodayEndTime;

    @OnClick({R.id.tvToday_huichuzhi_screening, R.id.tvYesterday_huichuzhi_screening, R.id.tvThisWeek_huichuzhi_screening, R.id.tvThisMonth_huichuzhi_screening})
    public void chooseDayOrWeek(TextView tv) {
        clearTime();
        switch (tv.getId()) {
            case R.id.tvToday_huichuzhi_screening:
                long todayLong = System.currentTimeMillis();
                mTvToday.setTextColor(Color.parseColor(clickColor));
                mTvToday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                mTodayStartTime = YrmUtils.stringDateFormat(true, false, todayLong) + " 00:00:00";
                mTodayEndTime = YrmUtils.stringDateFormat(true, false, todayLong) + " 23:59:59";
                mTvStartTime.setText(mTodayStartTime);
                mTvEndTime.setText(mTodayEndTime);
                break;
            case R.id.tvYesterday_huichuzhi_screening:
                long yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
                mTvYesterday.setTextColor(Color.parseColor(clickColor));
                mTvYesterday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                mTvStartTime.setText(YrmUtils.stringDateFormat(true, false, yesterday) + " 00:00:00");
                mTvEndTime.setText(YrmUtils.stringDateFormat(true, false, yesterday) + " 23:59:59");
                break;
            case R.id.tvThisWeek_huichuzhi_screening:
                mTvThisWeek.setTextColor(Color.parseColor(clickColor));
                mTvThisWeek.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
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
                long todayLong1 = System.currentTimeMillis();
                String mStartPickedTime = YrmUtils.stringDateFormat(true, false, timeInMillis);
                String mEndPickedTime = YrmUtils.stringDateFormat(false, false, todayLong1);
                mTvStartTime.setText(mStartPickedTime + " 00:00:00");
                mTvEndTime.setText(mEndPickedTime + " 23:59:59");
                break;
            case R.id.tvThisMonth_huichuzhi_screening:
                long todayLong2 = System.currentTimeMillis();
                mTvThisMonth.setTextColor(Color.parseColor(clickColor));
                mTvThisMonth.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                if (mCalendar == null)
                    mCalendar = Calendar.getInstance();
                mCalendar.set(YrmUtils.getNowYear(), YrmUtils.getNowMonth() - 1, 1);
                long timeInMillis1 = mCalendar.getTimeInMillis();
                String mStartPickedTime1 = YrmUtils.stringDateFormat(true, false, timeInMillis1);
                String mEndPickedTime1 = YrmUtils.stringDateFormat(false, false, todayLong2);
                mTvStartTime.setText(mStartPickedTime1 + " 00:00:00");
                mTvEndTime.setText(mEndPickedTime1 + " 23:59:59");
                break;
        }
    }

    private void clearTime() {
        mTvToday.setTextColor(Color.parseColor(unClickColor));
        mTvYesterday.setTextColor(Color.parseColor(unClickColor));
        mTvThisWeek.setTextColor(Color.parseColor(unClickColor));
        mTvThisMonth.setTextColor(Color.parseColor(unClickColor));
        mTvToday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvYesterday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvThisWeek.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvThisMonth.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
    }


    // 筛选门店
    @OnClick(R.id.selectStoreParent_huichuzhi_screening)
    public void chooseStore() {
        if (!mIsBoss) return;
        getStoreList();
    }
    private void getStoreList() {
            JSONObject jsonObject = new JSONObject();
        try {
            if (mLoginType.equals("0")) {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            } else {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("shopId"));
                jsonObject.put("storeId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId"));
            }
            jsonObject.put("query", "");
            jsonObject.put("limit", 10000);
            jsonObject.put("start", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().postNoHeader(this, NetUrl.STORE_LIST, jsonObject, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }
            @Override
            public void onSuccess(StoreManageBean bean) {
                mStoreDataList = bean.getData();
                if (!YrmUtils.isEmptyList(mStoreDataList)) {
                    if (mPopWindow == null) {
                        mPopWindow = new MyBottonPopWindow(HuiChuZhiScreeningActivity.this, mStoreDataList);
                    }
                    mPopWindow.setTitle("门店");
                    mImgArrow.setImageResource(R.drawable.arrow_up);
                    mPopWindow.showPopupWindow(storePosition);

                    mPopWindow.setStoreItemListListener(new MyBottonPopWindow.OnStoreItemListener() {
                        @Override
                        public void setStoreItemListener(int position) {
                            storePosition = position;
                            mSelectedStoreId = mStoreDataList.get(position).getStoreId();
                            mSelectStoreName = mStoreDataList.get(position).getStoreName();
                        }
                    });
                    mPopWindow.setDissmissListener(new MyBottonPopWindow.onDissmissListener() {
                        @Override
                        public void setDissmissListener() {
                            mImgArrow.setImageResource(R.drawable.arrow_down);
                        }
                    });

                    mPopWindow.setOnCloseListener(new MyBottonPopWindow.onCloseListener() {
                        @Override
                        public void setOnCloseListener() {
                            mImgArrow.setImageResource(R.drawable.arrow_down);
                        }
                    });

                    mPopWindow.setButtonClickListener(new MyBottonPopWindow.OnSureClickListener() {
                        @Override
                        public void setButtonClickListener() {
                            mImgArrow.setImageResource(R.drawable.arrow_down);
                            mStoreId = mSelectedStoreId;
                            mStoreName = mSelectStoreName;
                            if (TextUtils.isEmpty(mStoreId)) {
                                mStoreId = mStoreDataList.get(0).getStoreId();
                                mStoreName = mStoreDataList.get(0).getStoreName();
                            }
                            mTvStoreName.setText(mStoreName);
                        }
                    });
                }
            }
            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }
            @Override
            public void onFinish() {
                hideLoading();
            }
            @Override
            public Class getClazz() {
                return StoreManageBean.class;
            }
        });
    }

    // 将时间拆成数组
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

}
