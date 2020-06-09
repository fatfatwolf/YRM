package com.hybunion.yirongma.payment.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.ClerkWorkBean;
import com.hybunion.yirongma.payment.bean.ClerkWorkDetailBean;
import com.hybunion.yirongma.payment.bean.DutyTimeBean;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.MyDateTimePickDialog;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.Bind;

public class ClerkWorkActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.tv_head)
    TextView tv_head;
    @Bind(R.id.ll_back)
    LinearLayout ll_back;
    @Bind(R.id.bt_clerk_end)
    Button bt_clerk_end;

    @Bind(R.id.startDateParent_popupwindow_data_activity)
     RelativeLayout mStartDateParent;
    @Bind(R.id.endDateParent_popupwindow_data_activity)
    RelativeLayout mEndDateParent;
    @Bind(R.id.startDate_popupwindow_data_activity)
    TextView mTvStartDate;
    @Bind(R.id.endDate_popupwindow_data_activity)
    TextView mTvEndDate;

    String endDate,startDate;

    private String totalAmt,totalCount;
    String mStartTime,mEndTime,staffId;
    private String todayStartTime, todayEndTime;
    private SimpleDateFormat dateFormat;


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_clerk_work;
    }

    @Override
    public void initView() {
        super.initView();
        tv_head.setText("班结");
        staffId = SharedPreferencesUtil.getInstance(this).getKey("staffId");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mTvStartDate.setOnClickListener(this);
        mTvEndDate.setOnClickListener(this);
        mStartDateParent.setOnClickListener(this);
        mEndDateParent.setOnClickListener(this);
        bt_clerk_end.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        getTodayDateAndTime();
    }

    // 选择时间
    MyDateTimePickDialog mStartPickDialog, mEndPickDialog;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.startDateParent_popupwindow_data_activity:
                if (mStartPickDialog == null)
                    mStartPickDialog = new MyDateTimePickDialog(ClerkWorkActivity.this);
                // 每次打开的初始值是 tvStartTime 上显示的值。
                if(!TextUtils.isEmpty(mTvStartDate.getText().toString().trim())){
                    int[] startDateAndTime = getDateAndTime(mTvStartDate.getText().toString().trim());
                    if (startDateAndTime.length==6){
                        mStartPickDialog.setDateAndTime(startDateAndTime[0],startDateAndTime[1],startDateAndTime[2],startDateAndTime[3],startDateAndTime[4],startDateAndTime[5]);
                    }
                }else {
                    int[] startDateAndTime = getDateAndTime(todayStartTime);
                    mStartPickDialog.setDateAndTime(startDateAndTime[0],startDateAndTime[1],startDateAndTime[2],startDateAndTime[3],startDateAndTime[4],startDateAndTime[5]);
                }
                mStartPickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        mTvStartDate.setText(year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second);
                        mStartPickDialog.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mStartPickDialog.dismiss();
                    }
                });
                break;
            case R.id.endDateParent_popupwindow_data_activity:
                if (mEndPickDialog == null)
                    mEndPickDialog = new MyDateTimePickDialog(ClerkWorkActivity.this);
                if(!TextUtils.isEmpty(mTvEndDate.getText().toString().trim())){
                    int[] endDateAndTime = getDateAndTime(mTvEndDate.getText().toString().trim());
                    if (endDateAndTime.length==6){
                        mEndPickDialog.setDateAndTime(endDateAndTime[0],endDateAndTime[1],endDateAndTime[2],endDateAndTime[3],endDateAndTime[4],endDateAndTime[5]);
                    }
                }else {
                    int[] endDateAndTime = getDateAndTime(todayEndTime);
                    if (endDateAndTime.length==6){
                        mEndPickDialog.setDateAndTime(endDateAndTime[0],endDateAndTime[1],endDateAndTime[2],endDateAndTime[3],endDateAndTime[4],endDateAndTime[5]);
                    }
                }

                mEndPickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        mTvEndDate.setText(year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second);
                        mEndPickDialog.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mEndPickDialog.dismiss();
                    }
                });
                break;
            case R.id.bt_clerk_end:
                if (!YrmUtils.isFastDoubleClick(R.id.bt_clerk_end)){
                    try {
                        mStartTime = mTvStartDate.getText().toString().trim();
                        mEndTime = mTvEndDate.getText().toString().trim();
                        if(mStartTime.equals("") || mEndTime.equals("")){
                            ToastUtil.show("请选择班结时间");
                            return;
                        }

                    Date startdate = dateFormat.parse(mStartTime);
                    Date enddate = dateFormat.parse(mEndTime);
                    long betweenTime = enddate.getTime() - startdate.getTime();
                        if (betweenTime<0){
                            ToastUtil.show("开始时间不能晚于结束时间");
                            return;
                        }

                Log.i("xjzjiekou","mStartTime"+mStartTime+"    mEndTime"+mEndTime);
                 querySetUpSum(mStartTime,mEndTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;

        }
    }


    // 查询班结金额
    public void querySetUpSum(String startTime, String endTime){
        String url = NetUrl.QUERY_SET_UP_SUM;
        JSONObject object = new JSONObject();
        try {
            object.put("storeId", SharedPreferencesUtil.getInstance(ClerkWorkActivity.this).getKey("storeId"));
            object.put("endDate",endTime);
            object.put("startDate",startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkWorkActivity.this, url, object, new MyOkCallback<ClerkWorkDetailBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ClerkWorkDetailBean clerkWorkDetailBean) {
                if (clerkWorkDetailBean!=null){
                    String status = clerkWorkDetailBean.getStatus();
                    if (!TextUtils.isEmpty(status) && "0".equals(status)){
                        totalAmt = clerkWorkDetailBean.totalAmt;
                        totalCount = clerkWorkDetailBean.totalCount;
                        finishDailyDuty(mStartTime,mEndTime,totalCount,totalAmt);
                    }
                }else{
                    ToastUtil.show("请求失败");
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return ClerkWorkDetailBean.class;
            }
        });
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

    public void finishDailyDuty(String mStartTime,String mEndTime,String transCount,String transAmount){
        String url = NetUrl.FINISH_DAILY_DUTY;
        JSONObject object = new JSONObject();
        try {
            object.put("employeeId", SharedPreferencesUtil.getInstance(ClerkWorkActivity.this).getKey("staffId"));
            object.put("endDate",mEndTime);
            object.put("startDate",mStartTime);
            object.put("transCount",Integer.parseInt(transCount));
            object.put("transAmount",transAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkWorkActivity.this, url, object, new MyOkCallback<ClerkWorkBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ClerkWorkBean clerkWorkBean) {
                String status = clerkWorkBean.getStatus();
                String message = clerkWorkBean.getMessage();
                if(status.equals("0")){
                    ToastUtil.show(message);
                    Intent intent = new Intent(ClerkWorkActivity.this,ClerkWorkDetailActivity.class);
                    startActivity(intent);
                }else {
                    ToastUtil.show(message);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return ClerkWorkBean.class;
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        queryDutyTime();
    }

    public void queryDutyTime(){
        String url = NetUrl.QUERY_DUTY_TIME;
        JSONObject object = new JSONObject();
        try {
            object.put("storeId", SharedPreferencesUtil.getInstance(ClerkWorkActivity.this).getKey("storeId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkWorkActivity.this, url, object, new MyOkCallback<DutyTimeBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(DutyTimeBean dutyTimeBean) {
                DutyTimeBean.DataBean bean = dutyTimeBean.getData();
                if(!TextUtils.isEmpty(bean.startDate)){
                    startDate = bean.startDate;
                    mTvStartDate.setText(startDate);

                }
                if(!TextUtils.isEmpty(bean.endDate)){
                    endDate = bean.endDate;
                    mTvEndDate.setText(endDate);

                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return DutyTimeBean.class;
            }
        });
    }

    private void getTodayDateAndTime() {
        SimpleDateFormat formatStart = new SimpleDateFormat("yyyy-MM-dd");  // 开始时间时分秒都是 0
        SimpleDateFormat formatEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        todayStartTime = formatStart.format(new Date());
        todayStartTime += " " + "00:00:00";
        todayEndTime = formatEnd.format(new Date());
    }








}
