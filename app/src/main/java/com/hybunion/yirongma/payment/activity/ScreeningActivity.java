package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.DataSelectSpinnerAdapter;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.adapter.GridViewAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.hybunion.yirongma.payment.view.MyDateTimePickDialog;
import com.hybunion.yirongma.payment.utils.YrmUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @description: 筛选
 * @author: luyafeng
 * @data: 2017/6/14.
 */


public class ScreeningActivity extends BasicActivity implements View.OnClickListener {
    @Bind(R.id.grid_pay_type)
    GridView grid_pay_type;
    @Bind(R.id.cb_auto)
    CheckBox cb_auto;
    @Bind(R.id.tvReturn_screening_activity)
    TextView mTvReturn;  // 交易状态-有退款
    @Bind(R.id.codeSpinner_screening_layout)
    Spinner mCodeSpinner;
    @Bind(R.id.sourceSpinner_screening_layout)
    Spinner mSourceSpinner;
    @Bind(R.id.tvToday_popupwindow_data_activity)
    TextView mTvToday;
    @Bind(R.id.tvYesterday_popupwindow_data_activity)
    TextView mTvYesterday;
    @Bind(R.id.tvThisWeek_popupwindow_data_activity)
    TextView mTvThisWeek;
    @Bind(R.id.tvThisMonth_popupwindow_data_activity)
    TextView mTvThisMonth;
    @Bind(R.id.returnParent_screening_activity)
    RelativeLayout mReturnParent; // 退款按钮
    @Bind(R.id.tv_less)
    TextView tv_less;
    @Bind(R.id.tv_more)
    TextView tv_more;
    @Bind(R.id.tvStartTime_screening_layout)
    TextView mTvStartTime;
    @Bind(R.id.tvEndTime_screening_layout)
    TextView mTvEndTime;
    @Bind(R.id.tvStoreName_screening_activity)
    TextView mTvStoreName;
    @Bind(R.id.arrow_select_screening_activity)
    ImageView mImgSelectArrow;


    private boolean mIsReturn; // 用来标识 交易状态的 退款 是否选中
    private GridViewAdapter adapter;
    private String checBoxFlag;
    private String dataStart, dataEnd;
    String sType;
    private Date curDate;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormat1;
    int payTypes = -1;
    private String isAmount = "";
    private String mIsAmount = "";  // 筛选 “<300” ，mIsAmount = "1" ; 筛选 “>=300” ，misAmount = "2" ，存数据库用
    private String mLoginType; // 0-老板  1-店长  2-店员
    private boolean mIsBoss;  // 当前登录的是否是老板
    private String mSelectedStoreId = ""; // 选中的门店 ID ，如果选中全部门店，那么id为 “”
    private String mStoreName = "";  // 选择的分店名称
    private String clickColor = "#f74948";
    private String unClickColor = "#252e44";


    /**
     * 工具对象
     */
    private MyBottonPopWindow mPopWindow;
    private int storePosition = 0;
    private String mSelectedKuanTaiId;
    private String mSelectStoreName;
    private String mSelectKuanTaiName;
    private String mStoreId, mKuanTaiId, mKuanTaiName;
    private String mStaffType;

    private List<StoreManageBean.ObjBean> mDataList = new ArrayList<>();
    private List<StoreManageBean.ObjBean> mCodeDataList = new ArrayList<>();
    private List<StoreManageBean.ObjBean> mSourceDataList = new ArrayList<>();
    private DataSelectSpinnerAdapter mCodeDataAdapter;
    private DataSelectSpinnerAdapter mSourceDataAdapter;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_screening_layout;
    }

    @Override
    public void initView() {
        // 获取当前日期
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        curDate = new Date(System.currentTimeMillis());//获取当前时间
        dataEnd = dateFormat.format(curDate); // 默认的开始、结束时间都是当天
        dataStart = dateFormat.format(curDate);
        tv_more.setOnClickListener(this);
        tv_less.setOnClickListener(this);
        mTvToday.setOnClickListener(this);
        mTvYesterday.setOnClickListener(this);
        mTvThisWeek.setOnClickListener(this);
        mTvThisMonth.setOnClickListener(this);
        tv_less.setText("＜300");
        tv_more.setText("≥300");
        mTvStartTime.setText(YrmUtils.getNowDay("yyyy-MM-dd") + " " + "00:00:00");
        mTvEndTime.setText(YrmUtils.getNowDay("yyyy-MM-dd") + " " + "23:59:59");
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        mStoreName = SharedPreferencesUtil.getInstance(this).getKey("storeName");
        mIsBoss = "0".equals(mLoginType);

        switch (mLoginType) {
            case "0":
                mMerId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
                mStoreId = "";
                break;
            case "1":
                mMerId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
                mTvStoreName.setText(mStoreName);
                mStoreId = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId");
                mKuanTaiId = "";
                break;
            case "2":
                mMerId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
                mTvStoreName.setText(mStoreName);
                mImgSelectArrow.setVisibility(View.GONE);
                mStaffType = SharedPreferencesUtil.getInstance(this).getKey("staffType");
                mStoreId = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId");
//                if (mStaffType.equals("0")) {//门店员工
//                } else if (mStaffType.equals("1")) {//款台员工
//                    mKuanTaiId = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("storeId");
//                }
                break;

        }
        setSpinnerListener();

    }

    private String mMerId;

    @Override
    public void initData() {
        adapter = new GridViewAdapter(this);
        adapter.setSelection(payTypes);
        grid_pay_type.setAdapter(adapter);
        grid_pay_type.setSelector(new ColorDrawable(Color.TRANSPARENT));
        grid_pay_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (payTypes == position) {
                    payTypes = -1;
                    adapter.setSelection(-1);
                } else if (position == 2) {//云闪付
                    if (payTypes == 3) {//选中就取消
                        payTypes = -1;
                        adapter.setSelection(-1);
                    } else {//没选中
                        payTypes = 3;
                        adapter.setSelection(position);
                    }
                } else if (position == 3) {
                    if (payTypes == 5) {
                        payTypes = -1;
                        adapter.setSelection(-1);
                    } else {
                        payTypes = 5;
                        adapter.setSelection(position);
                    }
                } else {
                    payTypes = position;
                    adapter.setSelection(position);
                }
            }
        });

    }

    // 选择门店-款台
    @OnClick(R.id.selectStoreParent_screening_activity)
    public void selectStore() {
        if ("2".equals(mLoginType)) return;
        getStoreList();
    }

    private List<StoreManageBean.ObjBean> mStoreDataList;

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
                        mPopWindow = new MyBottonPopWindow(ScreeningActivity.this, mStoreDataList);
                    }
                    mImgSelectArrow.setImageResource(R.drawable.arrow_up);
                    mPopWindow.showPopupWindow(storePosition);

                    mPopWindow.setStoreItemListListener(new MyBottonPopWindow.OnStoreItemListener() {
                        @Override
                        public void setStoreItemListener(int position) {
                            storePosition = position;
                            mSelectedStoreId = mStoreDataList.get(position).getStoreId();
                            mSelectedKuanTaiId = "";
                            mSelectStoreName = mStoreDataList.get(position).getStoreName();
                            mSelectKuanTaiName = "";
                            getKuanTaiList(mSelectedStoreId);
                        }
                    });
                    mPopWindow.setDissmissListener(new MyBottonPopWindow.onDissmissListener() {
                        @Override
                        public void setDissmissListener() {
                            mImgSelectArrow.setImageResource(R.drawable.arrow_down);
                        }
                    });

                    mPopWindow.setOnCloseListener(new MyBottonPopWindow.onCloseListener() {
                        @Override
                        public void setOnCloseListener() {
                            mImgSelectArrow.setImageResource(R.drawable.arrow_down);
                        }
                    });

                    mPopWindow.setButtonClickListener(new MyBottonPopWindow.OnSureClickListener() {
                        @Override
                        public void setButtonClickListener() {
                            mImgSelectArrow.setImageResource(R.drawable.arrow_down);
                            mStoreId = mSelectedStoreId;
                            mKuanTaiId = mSelectedKuanTaiId;
                            if (TextUtils.isEmpty(mKuanTaiId)) {//只选中门店
                                getCodeData(mStoreId);
                            } else {//选中了款台
                                getCodeData(mKuanTaiId);
                            }
                            mStoreName = mSelectStoreName;
                            mKuanTaiName = mSelectKuanTaiName;
                            if (TextUtils.isEmpty(mStoreId)) {
                                mStoreId = mStoreDataList.get(0).getStoreId();
                                mStoreName = mStoreDataList.get(0).getStoreName();
                            }
                            if (!TextUtils.isEmpty(mKuanTaiName))
                                mTvStoreName.setText(mStoreName + "   " + mKuanTaiName);
                            else
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

    private List<StoreManageBean.ObjBean> mKuanTaiDataList;

    private void getKuanTaiList(String storeId) {
        JSONObject body = new JSONObject();
        try {
            body.put("storeName", "");
            body.put("storeId", storeId);
            body.put("limit", 10000);
            body.put("start", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.QUERY_CASHIER_LIST, body, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(StoreManageBean bean) {
                String status = bean.getStatus();
                if ("0".equals(status)) {
                    mKuanTaiDataList = bean.getData();
                    if (!YrmUtils.isEmptyList(mKuanTaiDataList)) {
                        mPopWindow.showKuanTaiList(mKuanTaiDataList, new MyBottonPopWindow.OnKuanTaiItemListener() {
                            @Override
                            public void setKuanTaiItemListener(int position) {
                                mSelectedKuanTaiId = mKuanTaiDataList.get(position).getStoreId();
                                mSelectKuanTaiName = mKuanTaiDataList.get(position).getStoreName();
                            }
                        });
                    } else {
                        hideLoading();
                        mPopWindow.showKuanTaiList(mKuanTaiDataList, new MyBottonPopWindow.OnKuanTaiItemListener() {
                            @Override
                            public void setKuanTaiItemListener(int position) {
                                mSelectedKuanTaiId = "";
                                mSelectKuanTaiName = "";
                            }
                        });
                    }
                } else {
                    String msg = bean.getMessage();
                    if (!TextUtils.isEmpty(msg))
                        ToastUtil.showShortToast(msg);
                    else
                        ToastUtil.showShortToast("网络连接不佳");
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

    private void setSpinnerListener() {
        mCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mCodeDataList != null && mCodeDataList.size() != 0) {
                    mTid = TextUtils.isEmpty(mCodeDataList.get(position).tid) ? "" : mCodeDataList.get(position).tid;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSourceDataList != null && mSourceDataList.size() != 0) {
                    mSourceId = TextUtils.isEmpty(mSourceDataList.get(position).tid) ? "" : mSourceDataList.get(position).tid;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected void canDo() {
        super.canDo();
        // 请求收款码列表和收款台插件列表数据
        getCodeData(mStoreId);
    }

    // 选择时间
    MyDateTimePickDialog mStartPickDialog, mEndPickDialog;

    @OnClick({R.id.startTimeParent_screening_layout, R.id.endTimeParent_screening_layout})
    public void setStartEndTime(RelativeLayout layout) {
        switch (layout.getId()) {
            case R.id.startTimeParent_screening_layout:
                if (mStartPickDialog == null)
                    mStartPickDialog = new MyDateTimePickDialog(ScreeningActivity.this);
                // 每次打开的初始值是 tvStartTime 上显示的值。
                int[] startDateAndTime = getDateAndTime(mTvStartTime.getText().toString().trim());
                if (startDateAndTime.length == 6) {
                    mStartPickDialog.setDateAndTime(startDateAndTime[0], startDateAndTime[1], startDateAndTime[2], startDateAndTime[3], startDateAndTime[4], startDateAndTime[5]);
                }
                mStartPickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        clearState();
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

            case R.id.endTimeParent_screening_layout:
                if (mEndPickDialog == null)
                    mEndPickDialog = new MyDateTimePickDialog(ScreeningActivity.this);
                int[] endDateAndTime = getDateAndTime(mTvEndTime.getText().toString().trim());
                if (endDateAndTime.length == 6) {
                    mEndPickDialog.setDateAndTime(endDateAndTime[0], endDateAndTime[1], endDateAndTime[2], endDateAndTime[3], endDateAndTime[4], endDateAndTime[5]);
                }
                mEndPickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        clearState();
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

    // 交易状态 两个按钮点击监听
    @OnClick(R.id.returnParent_screening_activity)
    public void stateButton() {
        if (mIsReturn) {
            mReturnParent.setBackgroundResource(R.drawable.shape_gray_bg);
            mTvReturn.setTextColor(Color.parseColor("#333333"));
        } else {
            mReturnParent.setBackgroundResource(R.drawable.shape_kuang_rid);
            mTvReturn.setTextColor(Color.parseColor("#f74948"));
        }
        mIsReturn = !mIsReturn;


    }

    private void getCodeData(String id) {
        JSONObject jb = new JSONObject();
        try {
            jb.put("merId", mMerId);
            jb.put("storeId", id);
            jb.put("loginType", mLoginType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.QUERY_CODE_LIST, jb, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(StoreManageBean result) {
                String status = result.getStatus();
                String message = result.getMessage();

                if (!TextUtils.isEmpty(status) && ("0".equals(status) || "2".equals(status))) {  // 查询成功
                    mCodeDataList.clear();
                    mCodeDataList = result.getData();
                    StoreManageBean.ObjBean bean = new StoreManageBean.ObjBean();
                    bean.tidName = "全部";
                    bean.tid = "";
                    mCodeDataList.add(0, bean);
                    mCodeDataAdapter = new DataSelectSpinnerAdapter(ScreeningActivity.this, getStringS(mCodeDataList, 1));
                    mCodeSpinner.setAdapter(mCodeDataAdapter);
                    if (TextUtils.isEmpty(mKuanTaiId)) {//只选中门店
                        getSourceData(mStoreId);
                    } else {//选中了款台
                        getSourceData(mKuanTaiId);
                    }
                } else {
                    if (!TextUtils.isEmpty(message))
                        ToastUtil.showShortToast(message);
                    else
                        ToastUtil.showShortToast("网络连接不佳");
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

    private void getSourceData(String id) {
        JSONObject jb = new JSONObject();
        try {
            jb.put("merId", mMerId);
            jb.put("storeId", id);
            jb.put("loginType", mLoginType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(this, NetUrl.QUERY_SOURCE_LIST, jb, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }
            @Override
            public void onSuccess(StoreManageBean result) {
                String status = result.getStatus();
                String message = result.getMessage();

                if (!TextUtils.isEmpty(status) && ("0".equals(status) || "2".equals(status))) {  // 查询成功
                    mSourceDataList.clear();
                    mSourceDataList = result.getData();
                    StoreManageBean.ObjBean bean = new StoreManageBean.ObjBean();
                    bean.tidName = "全部";
                    bean.tid = "";
                    mSourceDataList.add(0, bean);
                    mSourceDataAdapter = new DataSelectSpinnerAdapter(ScreeningActivity.this, getStringS(mSourceDataList, 1));
                    mSourceSpinner.setAdapter(mSourceDataAdapter);

                }else{
                    if (!TextUtils.isEmpty(message))
                        ToastUtil.showShortToast(message);
                    else
                        ToastUtil.showShortToast("网络连接不佳");
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

    //重置
    @OnClick(R.id.btn_reset)
    public void reset() {
        clearState();
        cb_auto.setChecked(false);
        mTvStartTime.setText(dataStart + " 00:00:00");
        mTvEndTime.setText(dataEnd + " 23:59:59");
        adapter.setSelection(-1);
        payTypes = -1;
        mSourceId = "";
        mTid = "";
        mCodeSpinner.setAdapter(mCodeDataAdapter);
        mSourceSpinner.setAdapter(mSourceDataAdapter);
        // 清空 交易状态选择
        mIsReturn = false;
        mTvReturn.setTextColor(Color.parseColor("#333333"));
        mReturnParent.setBackgroundResource(R.drawable.shape_gray_bg);
        tv_more.setBackgroundResource(R.drawable.background_corner1);
        tv_less.setBackgroundResource(R.drawable.background_corner1);
        tv_less.setTextColor(getResources().getColor(R.color.text_color2));
        tv_more.setTextColor(getResources().getColor(R.color.text_color2));
//        mTvReturn.setBackgroundResource(R.drawable.shape_kuang_gray);
    }

    //确定
    @OnClick(R.id.btn_ensure)
    public void ensure() {
        try {
            String startTime = mTvStartTime.getText().toString().trim();
            String endTime = mTvEndTime.getText().toString().trim();
            String[] startDateAndTime = mTvStartTime.getText().toString().trim().split(" ");
            String startDateStr = startDateAndTime[0];
            String startTimeStr = startDateAndTime[1];
            String[] endDateAndTime = mTvEndTime.getText().toString().trim().split(" ");
            String endDateStr = endDateAndTime[0];
            String endTimeStr = endDateAndTime[1];
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
            } else {
                if (mIsReturn) {
                    checBoxFlag = "1";
                } else {
                    checBoxFlag = "2";
                }

//                SharedPreferencesUtil.getInstance(this).putKey("checBoxFlag", checBoxFlag);
//                SharedPreferencesUtil.getInstance(this).putKey("dataStart", startDateStr);
//                SharedPreferencesUtil.getInstance(this).putKey("dataEnd", endDateStr);
//                SharedPreferencesUtil.getInstance(this).putKey("timeStart", startTimeStr);
//                SharedPreferencesUtil.getInstance(this).putKey("timeEnd", endTimeStr);
                sType = String.valueOf(payTypes);
//                SharedPreferencesUtil.getInstance(this).putKey("payType", sType);
                String data = startDateStr + " " + startTimeStr;
                String time = endDateStr + " " + endTimeStr;
                Intent intent = new Intent(this, ScreeningListActivity.class);
                intent.putExtra("dataStart", data);
                intent.putExtra("dataEnd", time);
                intent.putExtra("checBox", checBoxFlag);
                intent.putExtra("payType", payTypes);
                intent.putExtra("accountFlag", "1");
//                intent.putExtra("storeId", mSelectedStoreId);
                intent.putExtra("screenStoreId", mStoreId);
                intent.putExtra("screenKuanTaiId", mKuanTaiId);
                intent.putExtra("tid", mTid);
                intent.putExtra("isAmount", mIsAmount);
                intent.putExtra("sourceId", mSourceId);
                intent.putExtra("storeName", mStoreName);
                intent.putExtra("isRefresh", "Y");
                intent.putExtra("storeId", mStoreId);
                intent.putExtra("kuanTaiId", mKuanTaiId);
                intent.putExtra("kuanTaiName", mKuanTaiName);
                startActivity(intent);  // 跳转筛选用的列表页。

//                setResult(RESULT_OK, intent);
//                finish();

            }
        } catch (ParseException e) {
            e.printStackTrace();
            ToastUtil.showShortToast("网络错误，请重试。");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //    private CommonAdapter1 mAdapter1;
    // type: 0-查询门店用  非0-查询TID 和 查询收银台插件用  1-查询Tid   2-查询收银台插件  belowView-用来标识PopupWindow在那个 View 下面弹出
    private String mTid = "", mSourceId = "";


    private String[] getStringS(List<StoreManageBean.ObjBean> data, int type) {
        String[] strs = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            if (type == 0) {
                strs[i] = data.get(i).getStoreName();
            } else {
                strs[i] = data.get(i).tidName;
            }

        }
        return strs;
    }

    private Calendar mCalendar;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tvToday_popupwindow_data_activity:// 今日
                clearState();
                long todayLong = System.currentTimeMillis();
                mTvToday.setTextColor(Color.parseColor(clickColor));
                mTvToday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                mTvStartTime.setText(YrmUtils.stringDateFormat(true, false, todayLong) + " 00:00:00");
                mTvEndTime.setText(YrmUtils.stringDateFormat(true, false, todayLong) + " 23:59:59");
                break;
            case R.id.tvYesterday_popupwindow_data_activity:      // 昨日
                long yesterday = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
                clearState();
                mTvYesterday.setTextColor(Color.parseColor(clickColor));
                mTvYesterday.setBackgroundResource(R.drawable.shape1_popupwindow_data_activity);
                mTvStartTime.setText(YrmUtils.stringDateFormat(true, false, yesterday) + " 00:00:00");
                mTvEndTime.setText(YrmUtils.stringDateFormat(true, false, yesterday) + " 23:59:59");
                break;
            case R.id.tvThisWeek_popupwindow_data_activity:       // 本周
                clearState();
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
            case R.id.tvThisMonth_popupwindow_data_activity:      // 本月
                long todayLong2 = System.currentTimeMillis();
                clearState();
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

            case R.id.tv_less:
                isAmount = "<300";
                mIsAmount = "1";
                tv_less.setBackgroundResource(R.drawable.background_corner2);
                tv_less.setTextColor(getResources().getColor(R.color.red));
                tv_more.setBackgroundResource(R.drawable.background_corner1);
                tv_more.setTextColor(getResources().getColor(R.color.text_color2));
                break;
            case R.id.tv_more:
                isAmount = ">=300";
                mIsAmount = "2";
                tv_less.setBackgroundResource(R.drawable.background_corner1);
                tv_more.setTextColor(getResources().getColor(R.color.red));
                tv_more.setBackgroundResource(R.drawable.background_corner2);
                tv_less.setTextColor(getResources().getColor(R.color.text_color2));
                break;
        }
    }

    // 清空 “今日” 等四个按钮的选中状态
    private void clearState() {
        mTvToday.setTextColor(Color.parseColor(unClickColor));
        mTvYesterday.setTextColor(Color.parseColor(unClickColor));
        mTvThisWeek.setTextColor(Color.parseColor(unClickColor));
        mTvThisMonth.setTextColor(Color.parseColor(unClickColor));
        mTvToday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvYesterday.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvThisWeek.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
        mTvThisMonth.setBackgroundResource(R.drawable.shape2_popupwindow_data_activity);
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


}
