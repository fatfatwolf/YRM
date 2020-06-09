package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.SPUtil;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.Fragment.BillingListFragment;
import com.hybunion.yirongma.payment.Fragment.HuiChuZhiListFragment;
import com.hybunion.yirongma.payment.adapter.DataSummaryAdapter;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.bean.DataSummaryBean;
import com.hybunion.yirongma.payment.bean.PayTypeSummaryBean;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.hybunion.yirongma.payment.view.DataPopupWindow1;
import com.hybunion.yirongma.payment.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

// 首页 数据汇总，点击门店列表跳转，依然跳转本界面
// 根据传进来的身份、时间，展示数据和界面
public class SummaryActivity extends BasicActivity {
    @Bind(R.id.tab_parent_summary_activity)
    RelativeLayout mTabParent;
    @Bind(R.id.pay_code_str_summary_activity)
    TextView mTvPayCodeStr;
    @Bind(R.id.line1_summary_activity)
    View mLine1;
    @Bind(R.id.huiChuZhi_str_summary_activity)
    TextView mTvHuiChuZhiStr;
    @Bind(R.id.line2_summary_activity)
    View mLine2;
    @Bind(R.id.lv_store_summary_activity)
    ListView mLvStore;
    private DataPopupWindow1 mDataPopupWindow;
    @Bind(R.id.img_select_arrow_summary_activity)
    ImageView mImgSelectedArrow;
    @Bind(R.id.seleted_parent_summary_activity)
    RelativeLayout mSelectedParent;
    @Bind(R.id.tv_select_time_summary_activity)
    TextView mTvTimeStr;
    @Bind(R.id.tv_date_str_summary_activity)
    TextView mTvDateStr;
    @Bind(R.id.tv_total_amt_summary_activity)
    TextView mTvTotalAmt;
    @Bind(R.id.tv_total_count_summary_activity)
    TextView mTvTotalCount;
    @Bind(R.id.tv_total_refund_amt_summary_activity)
    TextView mTvTotalRefuntAmt;
    @Bind(R.id.tv_total_refund_count_summary_activity)
    TextView mTvTotalRefuntCount;
    @Bind(R.id.store_list_parent_summary_activity)
    LinearLayout mStoreListParent;
    @Bind(R.id.tv_text1_summary_activity)
    TextView mTvStoreStr;
    @Bind(R.id.tv_text2_summary_activity)
    TextView mTvPayTypeStr;
    @Bind(R.id.pay_type_parent_summary_activity)
    LinearLayout mPayTypeDetailsParent;  // 点击收款方式显示的各项收款方式的明细。
    @Bind(R.id.lv_pay_type_summary_activity)
    ListView mLvPayType;
    @Bind(R.id.titleBar_summary_activity)
    TitleBar mTitleBar;
    @Bind(R.id.tv_type_name)
    TextView mTvTypeName;
    // 惠储值用
    @Bind(R.id.huichuzhi_parent1_summary_activity)
    LinearLayout mHuiChuZhiParent;
    @Bind(R.id.tv_recharge_amt_summary_activity)
    TextView mTvRechargeAmt;
    @Bind(R.id.tv_recharge_count_summary_activity)
    TextView mTvRechargeCount;
    @Bind(R.id.tv_trans_amt_summary_activity)
    TextView mTvTransAmt;
    @Bind(R.id.tv_trans_count_summary_activity)
    TextView mTvTransCount;
    @Bind(R.id.pay_god_summary_activity)
    LinearLayout mPayGod;

    private boolean mIsStoreNow = true;  // 当前是否点击了门店按钮
    private CommonAdapter1 mPayTypeAdapter;
    private List<PayTypeSummaryBean.DataBean> mPayTypeDataList = new ArrayList<>();
    private String mLoginType, mStaffType, mTimeName, mStoreId;
    private String mHStartTime, mHEndTime;  // 惠储值筛选用
    private boolean mIsFirstTime = true; // 是否刚进入第一次调接口。第一次给惠储值也赋值。
    private String mTimeNameH = "今日"; // 惠储值用

    /**
     * 需要传入参数
     *
     * @param from
     * @param loginType 身份I 老板/店长/店员
     * @param staffType 身份II  门店店员/款台店员
     * @param startTime 展示数据的开始时间
     * @param endTime   展示数据的结束时间
     * @param timeName  时间控件后面的文字
     *                  <p>
     *                  当前页面和点击门店列表跳转本页面后，根据上面传进来的参数，展示相应界面和数据
     */
    public static void start(Context from, String loginType, String staffType, String startTime, String endTime, String timeName, String storeId, String storeName) {
        Intent intent = new Intent(from, SummaryActivity.class);
        intent.putExtra("loginType", loginType);
        intent.putExtra("staffType", staffType);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("timeName", timeName);
        intent.putExtra("storeId", storeId);
        intent.putExtra("storeName", storeName);
        from.startActivity(intent);

    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_summary_layout;
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();

        mStartTime = intent.getStringExtra("startTime");
        mEndTime = intent.getStringExtra("endTime");
        mLoginType = intent.getStringExtra("loginType");
        mStaffType = intent.getStringExtra("staffType");
        mTimeName = intent.getStringExtra("timeName");
        mStoreId = intent.getStringExtra("storeId") == null ? "" : intent.getStringExtra("storeId");
        String storeName = intent.getStringExtra("storeName");
        if (!TextUtils.isEmpty(storeName))
            mTitleBar.setTv_titlebar_back_titleText(storeName);
        mTvDateStr.setText(mTimeName);
        mTvTimeStr.setText(mStartTime + " 至 " + mEndTime);

        if (TextUtils.isEmpty(mLoginType)) {
            ToastUtil.showShortToast("网络连接不佳");
            finish();
        }

        mHStartTime = YrmUtils.getNowDay("yyyy-MM-dd") + " 00:00:00";
        mHEndTime = YrmUtils.getNowDay("yyyy-MM-dd") + " 23:59:59";

        if (!"0".equals(mLoginType)) {
            mTvStoreStr.setText("款台");
            mTvTypeName.setText("款台名称");
        }
        // 开通储值卡，才展示 惠储值 （只有老板展示）
        String vcSale = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.VC_SALE);
        String isCoupon = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.IS_COUPON);
        String loginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if(!"0".equals(isCoupon) || "2".equals(vcSale) || !"0".equals(loginType)){
            mTabParent.setVisibility(View.GONE);
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_popupwindow_data_activity, null);
        mDataPopupWindow = new DataPopupWindow1(this, view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDataPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mImgSelectedArrow.setImageResource(R.drawable.arrow_down);
            }
        });

        mLvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String loginType = "", staffType = "";
                if ("0".equals(mLoginType)) {
                    loginType = "1";
                } else if ("1".equals(mLoginType)) {
                    loginType = "2";
                    if (mStoreId.equals(mDataList.get(position).storeId)) {  // 当前点到的条目是门店
                        staffType = "0"; // 跳到下一个页面时的身份就应该是 门店下的店员
                    } else {  // 当前点到的是款台
                        staffType = "1";  // 跳到下一个页面时的身份就应该是 款台下的店员
                    }
                }
                SummaryActivity.start(SummaryActivity.this, loginType, staffType, mStartTime, mEndTime,
                        mTimeName, mDataList.get(position).storeId, mDataList.get(position).storeName);
            }
        });

        mPayTypeAdapter = new CommonAdapter1<PayTypeSummaryBean.DataBean>(this, mPayTypeDataList, R.layout.item_pay_type_list) {
            @Override
            public void convert(ViewHolder holder, PayTypeSummaryBean.DataBean item, int position) {
                TextView tvAmtStr = holder.findView(R.id.tv_amt_str_item_pay_type_list);
                TextView tvCountStr = holder.findView(R.id.tv_count_str_pay_type_list);
                TextView tvAmt = holder.findView(R.id.tv_total_amt_pay_type_list);
                TextView tvCount = holder.findView(R.id.tv_total_count_pay_type_list);
                TextView tvRefundAmt = holder.findView(R.id.tv_total_refund_pay_type_list);
                TextView tvRefundCount = holder.findView(R.id.tv_total_refund_count_pay_type_list);
                tvAmtStr.setText(item.payChannel + "收款金额（元）");
                tvCountStr.setText(item.payChannel + "收款笔数（笔）");
                tvAmt.setText(YrmUtils.decimalTwoPoints(item.transAmount));
                tvCount.setText(item.transCount);
                if (!TextUtils.isEmpty(item.refundAmount) && !"0.00".equals(item.refundAmount) && !"0".equals(item.refundAmount)) {
                    tvRefundAmt.setVisibility(View.VISIBLE);
                    tvRefundAmt.setText("含退款：" + YrmUtils.decimalTwoPoints(item.refundAmount));
                } else {
                    tvRefundAmt.setVisibility(View.INVISIBLE);
                }
                if (!TextUtils.isEmpty(item.refundCount) && !"0".equals(item.refundCount)) {
                    tvRefundCount.setVisibility(View.VISIBLE);
                    tvRefundCount.setText("含退款：" + item.refundCount);
                } else {
                    tvRefundCount.setVisibility(View.INVISIBLE);
                }

            }
        };
        mLvPayType.setAdapter(mPayTypeAdapter);

        getSummaryData();  // 门店数据
        payTypeData();     // 收款方式数据
    }

    @OnClick({R.id.pay_code_parent_summary_activity, R.id.huichuzhi_parent_summary_activity, R.id.selectedParent_summary_activity,
            R.id.tv_text1_summary_activity, R.id.tv_text2_summary_activity})
    public void tabClick(View view) {
        switch (view.getId()) {
            case R.id.pay_code_parent_summary_activity:   // 收款码
                setTabs(0);
                mPayGod.setVisibility(View.VISIBLE);
                mHuiChuZhiParent.setVisibility(View.GONE);
                mTvTimeStr.setText(mStartTime + " 至 " + mEndTime);
                mTvDateStr.setText(mTimeName);
                if (mDataPopupWindow!=null)
                    mDataPopupWindow.setState(mTimeName);
                break;
            case R.id.huichuzhi_parent_summary_activity:  // 惠储值
                setTabs(1);
                mPayGod.setVisibility(View.GONE);
                mHuiChuZhiParent.setVisibility(View.VISIBLE);
                mTvTimeStr.setText(mHStartTime + " 至 " + mHEndTime);
                mTvDateStr.setText(mTimeNameH);
                if (mDataPopupWindow!=null)
                    mDataPopupWindow.setState(mTimeNameH);
                break;
            case R.id.tv_text1_summary_activity:   // 点击门店按钮
                if (!mIsStoreNow) {
                    mIsStoreNow = true;
                    mTvStoreStr.setTextColor(getResources().getColor(R.color.white));
                    mTvStoreStr.setBackgroundResource(R.drawable.shape_summary_bt_bg_left);
                    mTvPayTypeStr.setTextColor(getResources().getColor(R.color.blue_0076ff));
                    mTvPayTypeStr.setBackgroundResource(R.drawable.shape_summary_bt_bg_stroke_right);
                    if ("2".equals(mLoginType)) {
                        mStoreListParent.setVisibility(View.GONE);
                    } else {
                        if (!YrmUtils.isEmptyList(mDataList))
                            mStoreListParent.setVisibility(View.VISIBLE);
                    }
                    mPayTypeDetailsParent.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_text2_summary_activity:  // 点击收款方式按钮
                if (mIsStoreNow) {
                    mIsStoreNow = false;
                    mTvStoreStr.setTextColor(getResources().getColor(R.color.blue_0076ff));
                    mTvStoreStr.setBackgroundResource(R.drawable.shape_summary_bt_bg_stroke_left);
                    mTvPayTypeStr.setTextColor(getResources().getColor(R.color.white));
                    mTvPayTypeStr.setBackgroundResource(R.drawable.shape_summary_bt_bg_right);
                    if (!YrmUtils.isEmptyList(mPayTypeDataList))
                        mPayTypeDetailsParent.setVisibility(View.VISIBLE);
                    mStoreListParent.setVisibility(View.GONE);
                }
                break;
        }

    }

    private int mChooseType;  // 0-收款  1-惠储值

    private void setTabs(int position) {
        mTvPayCodeStr.setTextSize(13);
        mLine1.setVisibility(View.GONE);
        mTvHuiChuZhiStr.setTextSize(13);
        mLine2.setVisibility(View.GONE);
        mChooseType = position;
        switch (position) {
            case 0:
                mTvPayCodeStr.setTextSize(16);
                mLine1.setVisibility(View.VISIBLE);
                break;
            case 1:
                mTvHuiChuZhiStr.setTextSize(16);
                mLine2.setVisibility(View.VISIBLE);
                break;
        }
    }

    private String mStartTime, mEndTime; // 筛选用   时间默认是今日的
    private String mNameTime;

    @OnClick(R.id.selectedParent_summary_activity)
    public void showSelectedDialog() {
        mImgSelectedArrow.setImageResource(R.drawable.arrow_up);
        if (mDataPopupWindow != null) {
            String startTime, endTime;
            if (mChooseType == 0) {
                startTime = mStartTime;
                endTime = mEndTime;
            } else {
                startTime = mHStartTime;
                endTime = mHEndTime;
            }
            mDataPopupWindow.showThisPopWindow(mSelectedParent, startTime, endTime, new DataPopupWindow1.OnDataPopWindowListener() {
                @Override
                public void onPickFinish(String startPickedTime, String endPickedTime, String nameStr) {
                    if (mChooseType == 0) {
                        mStartTime = startPickedTime;
                        mEndTime = endPickedTime;
                    } else {
                        mHStartTime = startPickedTime;
                        mHEndTime = endPickedTime;
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Date startDate, endDate;
                        if (mChooseType == 0) {
                            startDate = sdf.parse(mStartTime);
                            endDate = sdf.parse(mEndTime);
                        } else {
                            startDate = sdf.parse(mHStartTime);
                            endDate = sdf.parse(mHEndTime);
                        }

                        long startMillis = startDate.getTime();
                        long endMillis = endDate.getTime();
                        // endMillis 应该 >= startMillis
                        if (endMillis < startMillis) {
                            ToastUtil.showShortToast("开始时间不能晚于结束时间");
                            return;
                        }
                        long day31 = 31L * 24 * 60 * 60 * 1000;
                        if ((endMillis - startMillis) > day31) {
                            ToastUtil.showShortToast("查询区间不能大于31天");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (mChooseType == 0) {
                        mNameTime = mStartTime + " 至 " + mEndTime;
                    } else {
                        mNameTime = mHStartTime + "至" + mHEndTime;
                    }
                    setSelectDayText(nameStr, mNameTime);
                    getSummaryData();
                    if (mChooseType == 0)
                        payTypeData();
                    mDataPopupWindow.dismiss();
                }

                @Override
                public void onPickReset() {  // 只还原筛选框筛选的值，不请求接口，也不 dismiss
//                    mDataPopupWindow.dismiss();
//                    mStartTime = YrmUtils.getNowDay("yyyy-MM-dd") + " 00:00:00";
//                    mEndTime = YrmUtils.getNowDay("yyyy-MM-dd") + " 23:59:59";
//                    mHStartTime = YrmUtils.getNowDay("yyyy-MM-dd") + " 00:00:00";
//                    mHEndTime = YrmUtils.getNowDay("yyyy-MM-dd") + " 23:59:59";
//                    getSummaryData();
//                    if (mChooseType == 0)
//                        payTypeData();
                }
            });
        }
    }

    public void setSelectDayText(String nameStr, String mNameTime) {
        if (nameStr.trim().equals("今日")) {
            mTvDateStr.setText(nameStr);
            mTvTimeStr.setText(mNameTime);
            if (mChooseType == 0)
                mTimeName = nameStr;
            else
                mTimeNameH = nameStr;
        } else if (nameStr.trim().equals("昨日")) {
            mTvDateStr.setText(nameStr);
            mTvTimeStr.setText(mNameTime);
            if (mChooseType == 0)
                mTimeName = nameStr;
            else
                mTimeNameH = nameStr;
        } else if (nameStr.trim().equals("本周")) {
            mTvDateStr.setText(nameStr);
            mTvTimeStr.setText(mNameTime);
            if (mChooseType == 0)
                mTimeName = nameStr;
            else
                mTimeNameH = nameStr;
        } else if (nameStr.trim().equals("本月")) {
            mTvDateStr.setText(nameStr);
            mTvTimeStr.setText(mNameTime);
            if (mChooseType == 0)
                mTimeName = nameStr;
            else
                mTimeNameH = nameStr;
        } else {
            mTvDateStr.setText("日期");
            mTvTimeStr.setText(mNameTime);
            if (mChooseType == 0)
                mTimeName = "日期";
            else
                mTimeNameH = "日期";
        }
    }

    List<DataSummaryBean.DataBean> mDataList;

    // 请求接口 汇总数据和门店数据and惠储值数据
    private void getSummaryData() {

        JSONObject body = new JSONObject();
        try {
            if (mLoginType.equals("0")) {
                body.put("merId", SharedPreferencesUtil.getInstance(this).getKey("merchantID"));
                body.put("storeId", "");
            } else if (mLoginType.equals("1")) {
                body.put("merId", SharedPreferencesUtil.getInstance(this).getKey("shopId"));
                body.put("storeId", mStoreId);
                body.put("desk", "");
            } else {
                body.put("merId", SharedPreferencesUtil.getInstance(this).getKey("merchantID"));
                if ("0".equals(mStaffType)) {
                    body.put("storeId", mStoreId);
                    body.put("desk", "");
                } else {
                    body.put("desk", mStoreId);
                    body.put("storeId", "");
                }
            }
            body.put("loginType", mLoginType);
            if (mChooseType == 0) {
                body.put("endDate", mEndTime);
                body.put("startDate", mStartTime);
            } else {
                body.put("endDate", mHEndTime);
                body.put("startDate", mHStartTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.SUMMARY, body, new MyOkCallback<DataSummaryBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(DataSummaryBean dataSummaryBean) {
                String status = dataSummaryBean.getStatus();
                String msg = dataSummaryBean.getMessage();
                if ("0".equals(status)) {
                    if (mChooseType == 0) {
                        mTvTotalAmt.setText(YrmUtils.decimalTwoPoints(dataSummaryBean.totalAmt));
                        mTvTotalCount.setText(dataSummaryBean.totalCount);
                        if (!TextUtils.isEmpty(dataSummaryBean.totalRefundAmt) && !"0.00".equals(dataSummaryBean.totalRefundAmt)) {
                            mTvTotalRefuntAmt.setVisibility(View.VISIBLE);
                            mTvTotalRefuntAmt.setText("含退款：" + YrmUtils.decimalTwoPoints(dataSummaryBean.totalRefundAmt));
                        } else {
                            mTvTotalRefuntAmt.setVisibility(View.INVISIBLE);
                        }
                        if (!TextUtils.isEmpty(dataSummaryBean.totalRefundCount) && !"0".equals(dataSummaryBean.totalRefundCount)) {
                            mTvTotalRefuntCount.setVisibility(View.VISIBLE);
                            mTvTotalRefuntCount.setText("含退款：" + dataSummaryBean.totalRefundCount);
                        } else {
                            mTvTotalRefuntCount.setVisibility(View.INVISIBLE);
                        }
                        // 设置列表
                        mDataList = dataSummaryBean.getData();
                        if (!YrmUtils.isEmptyList(mDataList)) {
                            if (!"2".equals(mLoginType)) {
                                DataSummaryAdapter adapter = new DataSummaryAdapter(SummaryActivity.this, mDataList);
                                mLvStore.setAdapter(adapter);
                                if (mIsStoreNow)
                                    mStoreListParent.setVisibility(View.VISIBLE);
                            }

                        } else {
                            mStoreListParent.setVisibility(View.GONE);
                        }
                        if (mIsFirstTime) {
                            mTvRechargeAmt.setText(YrmUtils.decimalTwoPoints(dataSummaryBean.totalRegAmt) + " 元");
                            if (!TextUtils.isEmpty(dataSummaryBean.totalRegCount)) {
                                mTvRechargeCount.setVisibility(View.VISIBLE);
                                mTvRechargeCount.setText(dataSummaryBean.totalRegCount + "笔");
                            } else {
                                mTvRechargeCount.setVisibility(View.INVISIBLE);
                            }
                            mTvTransAmt.setText(YrmUtils.decimalTwoPoints(dataSummaryBean.totaltransAmt) + " 元");
                            if (!TextUtils.isEmpty(dataSummaryBean.totaltransCount)) {
                                mTvTransCount.setVisibility(View.VISIBLE);
                                mTvTransCount.setText(dataSummaryBean.totaltransCount + "笔");
                            } else {
                                mTvTransCount.setVisibility(View.INVISIBLE);
                            }
                        }

                    } else {
                        // 惠储值
                        mTvRechargeAmt.setText(YrmUtils.decimalTwoPoints(dataSummaryBean.totalRegAmt) + " 元");
                        if (!TextUtils.isEmpty(dataSummaryBean.totalRegCount)) {
                            mTvRechargeCount.setVisibility(View.VISIBLE);
                            mTvRechargeCount.setText(dataSummaryBean.totalRegCount + "笔");
                        } else {
                            mTvRechargeCount.setVisibility(View.INVISIBLE);
                        }
                        mTvTransAmt.setText(YrmUtils.decimalTwoPoints(dataSummaryBean.totaltransAmt) + " 元");
                        if (!TextUtils.isEmpty(dataSummaryBean.totaltransCount)) {
                            mTvTransCount.setVisibility(View.VISIBLE);
                            mTvTransCount.setText(dataSummaryBean.totaltransCount + "笔");
                        } else {
                            mTvTransCount.setVisibility(View.INVISIBLE);
                        }
                    }


                } else {
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
                return DataSummaryBean.class;
            }
        });
    }

    // 收款方式 数据接口
    private void payTypeData() {
        JSONObject body = new JSONObject();
        try {
            if (mLoginType.equals("0")) {
                body.put("merId", SharedPreferencesUtil.getInstance(this).getKey("merchantID"));
                body.put("storeId", "");
            } else if (mLoginType.equals("1")) {
                body.put("merId", SharedPreferencesUtil.getInstance(this).getKey("merchantID"));
                body.put("storeId", mStoreId);
                body.put("desk", "");
            } else {
                body.put("merId", SharedPreferencesUtil.getInstance(this).getKey("merchantID"));
                if ("0".equals(mStaffType)) {
                    body.put("storeId", mStoreId);
                    body.put("desk", "");
                } else {
                    body.put("desk", mStoreId);
                    body.put("storeId", "");
                }
            }
            body.put("loginType", mLoginType);
            body.put("endDate", mEndTime);
            body.put("startDate", mStartTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.SUMMARY_PAY_TYPE, body, new MyOkCallback<PayTypeSummaryBean>() {
            @Override
            public void onStart() {
//                showLoading();
            }

            @Override
            public void onSuccess(PayTypeSummaryBean bean) {
                String status = bean.getStatus();
                String msg = bean.getMessage();
                if ("0".equals(status)) {
                    mPayTypeDataList.clear();
                    List<PayTypeSummaryBean.DataBean> dataList = bean.getData();
                    if (!YrmUtils.isEmptyList(dataList)) {
                        mPayTypeDataList.addAll(dataList);
                        mPayTypeAdapter.updateList(mPayTypeDataList);
                        if (!mIsStoreNow)
                            mPayTypeDetailsParent.setVisibility(View.VISIBLE);
                    }else{
                        mPayTypeDetailsParent.setVisibility(View.GONE);
                    }

                } else {
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
//                hideLoading();
            }

            @Override
            public Class getClazz()   {
                return PayTypeSummaryBean.class;
            }
        });


    }


}
