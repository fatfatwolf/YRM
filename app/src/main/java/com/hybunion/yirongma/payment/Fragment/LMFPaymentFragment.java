package com.hybunion.yirongma.payment.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.autonavi.bigwasp.sdk.BWHelper;
import com.autonavi.bigwasp.sdk.BigWaspListener;
import com.autonavi.bigwasp.sdk.Location;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.LMFMainActivity;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.activity.SettlementActivity;
import com.hybunion.yirongma.payment.activity.SummaryActivity;
import com.hybunion.yirongma.payment.bean.AutonymCertificationInfoBean;
import com.hybunion.yirongma.payment.bean.MoreNoticeBean;
import com.hybunion.yirongma.payment.bean.TextBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedUtil;
import com.hybunion.yirongma.common.util.jpush.JpushStatsConfig;
import com.hybunion.yirongma.payment.base.BaseFragment;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.activity.HuiValueCardActivity;
import com.hybunion.yirongma.payment.activity.HuiValueSuccessActivity;
import com.hybunion.yirongma.payment.activity.MemberManageActivity;
import com.hybunion.yirongma.payment.activity.NewBillingListActivity;
import com.hybunion.yirongma.payment.activity.SweepOrderActivity;
import com.hybunion.yirongma.payment.activity.ValueCardActivity;
import com.hybunion.yirongma.payment.activity.YouHuiQuanListActivity;
import com.hybunion.yirongma.payment.adapter.BannerViewPagerAdapter;
import com.hybunion.yirongma.payment.activity.LMFRedRainActivity;
import com.hybunion.yirongma.payment.activity.MoreNoticeActivity;
import com.hybunion.yirongma.payment.activity.NoticeDetailActivity;
import com.hybunion.yirongma.payment.activity.PosPayActivity;
import com.hybunion.yirongma.payment.utils.ObservableScrollView;
import com.hybunion.yirongma.payment.view.FloatWindowView.view.FloatLayout;
import com.hybunion.yirongma.payment.view.FloatWindowView.view.MyBottomDialog;
import com.hybunion.yirongma.payment.view.MyBannerView;
import com.hybunion.yirongma.payment.view.UPMarqueeView;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author lyj
 * @date 2017/8/26
 * @email freemars@yeah.net
 * @description
 */
public class LMFPaymentFragment extends BaseFragment implements View.OnClickListener {
    private View root_view;
    private TextView tv_title, tv_rp, tv_bd, tv_rc;
    private String permission, mid, isJhMidBindTid;
    private LinearLayout ll_lmf_rp, ll_lmf_bd, ll_lmf_rc, ll_lmf_data_summary, ll_value_card, ll_sweep_order, ll_loan, ll_buss_act,ll_bussiness_loan,ll_next_loan;
    private RelativeLayout tl_mote_notice;
    private ImageView gallery, iv_ad;
    List<TextBean.DataBean> list = new ArrayList<>();
    List<MoreNoticeBean.DataBean> rolllist = new ArrayList<>();
    int page = 0;
    List<View> views = new ArrayList<>();
    private UPMarqueeView upview;
    String merchantName, areaType, closeData, reason;
    private MessageReceiver mMessageReceiver;
    private HuiVlueCardReceiver huiVlueCardReceiver;
    private TextView tv_total, tv_today_account,tv_resume,tv_rechange;
    private UnorderedReceiver unorderedReceiver;
    private ObservableScrollView scrollView;
    private MyBannerView mBannerView;
    public static String ACTION_INTENT_RECEIVER = "com.hybunion.yirongma.common.util.jpush";
    public static String HUI_VALUE_CARD_INTENT_RECEIVER = "com.hybunion.yirongma.hui.value.card";
    public static String KEY = "com.hybunion.yirongma.common.util.key";
    boolean flag1 = false;
    boolean flag2 = false;
    boolean flag3 = false;
    String appStatus, loginType, storeName;
    String dataStart, dataEnd, merchantID, storeId, staffType;
    private SmartRefreshLayout smartRefresh_layout;
    private boolean mIsHidding; // 是否已经隐藏
    private RelativeLayout mParent; // 父布局，放 FloatView 用。
    //    private TextSwitcher mTextSwitcher;
    private TextView mTvMsg;
    private FloatLayout mFloatLayout;
    private int mIsFloatOpen;  // 用户是否开启了悬浮球设置  0-未开启  1-开启
    private String mVoiceOpen; // 语音播报是否开启   "1"或空-开启    "2"-未开启
    private LinearLayout mYouHuiQuanParent;  // 优惠券入口
    private LinearLayout ll_discount_coupon;
    private LinearLayout ll_hui_value;
    private LinearLayout ll_hui_consume;
    private String longitude,latitude,AdCode;
    private float accuracy;
    private String vcSale;
    String isCoupon;
    private double mRechargeAmt, mAmt;   // 充值金额、消费金额
    private int mRechargeCount, mCount;    // 充值笔数、消费笔数
    private String isShowLoans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root_view == null) {
            root_view = inflater.inflate(R.layout.activity_lmf, null, false);
            permission = SharedPreferencesUtil.getInstance(getActivity()).getKey("permission");
            registerMessageReceiver();
            registerHuiReceiver();
            closeNotice();
        } else {
            ViewGroup viewGroup = ((ViewGroup) root_view.getParent());
            merchantName = SharedPreferencesUtil.getInstance(getActivity()).getKey("merchantName");
            tv_title = (TextView) root_view.findViewById(R.id.tv_title);
            tv_title.setText(merchantName);
            if (viewGroup != null) {
                viewGroup.removeView(root_view);
            }

        }
        return root_view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mid = SharedPreferencesUtil.getInstance(getActivity()).getKey("mid");
        merchantName = SharedPreferencesUtil.getInstance(getActivity()).getKey("merchantName");
        merchantID = SharedPreferencesUtil.getInstance(getActivity()).getKey("merchantID");
        loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
        storeName = SharedPreferencesUtil.getInstance(getActivity()).getKey("storeName");
        storeId = SharedPreferencesUtil.getInstance(getActivity()).getKey("storeId");
        staffType = SharedPreferencesUtil.getInstance(getActivity()).getKey("staffType");
        vcSale = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.VC_SALE);
        longitude = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.LONGITUDE);//点亮高德使用
        latitude = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.LATITUDE);//点亮高德使用
        AdCode = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.ADCODE);//点亮高德使用
        accuracy = SharedPreferencesUtil.getInstance(getActivity()).getFloatKey(SharedPConstant.ACCUARY);//点亮高德使用
        isShowLoans = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.IS_SHOW_LOANS);
        mFloatLayout = root_view.findViewById(R.id.floatLayout_payment_fragment);
        mTvMsg = root_view.findViewById(R.id.tv_message_payment_fragment); // 轮播
        mParent = root_view.findViewById(R.id.parent_payment_fragment);
        ll_next_loan = root_view.findViewById(R.id.ll_next_loan);
        mYouHuiQuanParent = root_view.findViewById(R.id.youhuiquan_parent_payment_activity);
        ll_lmf_rp = (LinearLayout) root_view.findViewById(R.id.ll_lmf_rp);
        tv_today_account = (TextView) root_view.findViewById(R.id.tv_today_account);
        ll_lmf_bd = (LinearLayout) root_view.findViewById(R.id.ll_lmf_bd);
        ll_lmf_rc = (LinearLayout) root_view.findViewById(R.id.ll_lmf_rc);
        ll_hui_consume = root_view.findViewById(R.id.ll_hui_consume);
        ll_buss_act = root_view.findViewById(R.id.ll_buss_act);
        ll_buss_act.setOnClickListener(this);
        ll_hui_value = root_view.findViewById(R.id.ll_hui_value);
        ll_hui_value.setOnClickListener(this);
        ll_value_card = root_view.findViewById(R.id.ll_value_card);
        ll_value_card.setOnClickListener(this);
        tv_total = (TextView) root_view.findViewById(R.id.tv_total);
        tv_resume = root_view.findViewById(R.id.tv_resume);
        tv_rechange = root_view.findViewById(R.id.tv_rechange);
        iv_ad = (ImageView) root_view.findViewById(R.id.iv_ad);
        ll_sweep_order = root_view.findViewById(R.id.ll_sweep_order);
        ll_sweep_order.setOnClickListener(this);
        ll_bussiness_loan = root_view.findViewById(R.id.ll_bussiness_loan);
        ll_bussiness_loan.setOnClickListener(this);
        ll_loan = root_view.findViewById(R.id.ll_loan);
        ll_loan.setOnClickListener(this);
        ll_discount_coupon = root_view.findViewById(R.id.ll_discount_coupon);
        ll_discount_coupon.setOnClickListener(this);
        ll_lmf_data_summary = (LinearLayout) root_view.findViewById(R.id.ll_lmf_data_summary);
        ll_lmf_data_summary.setOnClickListener(this);
        upview = (UPMarqueeView) root_view.findViewById(R.id.upview);
//        mTextSwitcher = root_view.findViewById(R.id.textSwitch_payment_fragment);
        // 判断是否展示优惠券入口
        isCoupon = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.IS_COUPON);
        if ("0".equals(isCoupon)) {
            ll_hui_value.setVisibility(View.VISIBLE);
            ll_value_card.setVisibility(View.GONE);
            ll_discount_coupon.setVisibility(View.VISIBLE);
            ll_buss_act.setVisibility(View.GONE);
            mYouHuiQuanParent.setVisibility(View.VISIBLE);
            ll_sweep_order.setVisibility(View.GONE);
        }else{
            ll_hui_value.setVisibility(View.GONE);
            ll_value_card.setVisibility(View.VISIBLE);
            ll_discount_coupon.setVisibility(View.GONE);
            mYouHuiQuanParent.setVisibility(View.GONE);
            ll_buss_act.setVisibility(View.VISIBLE);
            ll_sweep_order.setVisibility(View.VISIBLE);

        }

        if ("0".equals(isCoupon)) {
            if(loginType.equals("2")){
                ll_hui_consume.setVisibility(View.INVISIBLE);
            }else {
                ll_hui_consume.setVisibility(View.VISIBLE);
            }
        }else{
            ll_hui_consume.setVisibility(View.INVISIBLE);

        }

        if("0".equals(isShowLoans) && "0".equals(loginType)){
            ll_next_loan.setVisibility(View.VISIBLE);
        }else {
            ll_next_loan.setVisibility(View.GONE);
        }

        tv_title = (TextView) root_view.findViewById(R.id.tv_title);
        if ("0".equals(loginType)) {
            tv_title.setText(merchantName);
        } else {
            tv_title.setText(storeName);
            ll_lmf_rc.setVisibility(View.GONE);
        }

        tl_mote_notice = (RelativeLayout) root_view.findViewById(R.id.tl_mote_notice);
        String isLmfHead = SharedPreferencesUtil.getInstance(getActivity()).getKey("isLmfHead");
        LogUtil.d(isLmfHead + "=====isHeadOffice");

        gallery = (ImageView) root_view.findViewById(R.id.gallery);
        mYouHuiQuanParent.setOnClickListener(this);
        ll_lmf_rp.setOnClickListener(this);
        ll_lmf_bd.setOnClickListener(this);
        ll_lmf_rc.setOnClickListener(this);
        gallery.setOnClickListener(this);
        tl_mote_notice.setOnClickListener(this);
        scrollView = (ObservableScrollView) root_view.findViewById(R.id.scrollview);
        tv_rp = (TextView) root_view.findViewById(R.id.tv_rp);
        tv_bd = (TextView) root_view.findViewById(R.id.tv_bd);
        tv_rc = (TextView) root_view.findViewById(R.id.tv_rc);
        mBannerView = root_view.findViewById(R.id.myBannerView_lmf_activity);
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nowTime1 = System.currentTimeMillis();
        long todayStartTime = nowTime1 - (nowTime1 + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
        mCalendar.set(Calendar.DATE, mCalendar.get(Calendar.DATE));
        Date nowTime = mCalendar.getTime();
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        Date curDate1 = new Date(todayStartTime);//获取当前时间
        dataStart = formatter.format(curDate1);
        dataEnd = formatter.format(nowTime);
        initRefresh();
        getNoticeInfo();
//        queryMessage();
        queryBillingData(page, mid);
        queryWisaData();

    }

    private MyBottomDialog mDialog;

    public void showCheckDialog() {
        mDialog = new MyBottomDialog(getActivity());
        mDialog.setOwnerActivity(getActivity());
//        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mFloatLayout.setVisibility(View.GONE);
        mDialog.showThisDialog();
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mFloatLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    // 下拉刷新
    private void initRefresh() {
        smartRefresh_layout = (SmartRefreshLayout) root_view.findViewById(R.id.smartRefresh_layout);
        smartRefresh_layout.setEnableLoadMore(false);
        smartRefresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Calendar mCalendar = Calendar.getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date nowTime = mCalendar.getTime();
                dataEnd = formatter.format(nowTime);
                long nowTime1 = System.currentTimeMillis();
                long todayStartTime = nowTime1 - (nowTime1 + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
                Date curDate1 = new Date(todayStartTime);//获取当前时间
                dataStart = formatter.format(curDate1);
                getNoticeInfo();
//                queryMessage();
                queryBillingData(page, mid);
                queryWisaData();
                queryMerchantInfo();
                isJhMidBindTid = SharedPreferencesUtil.getInstance(getActivity()).getKey("isJhMidBindTid");
            }
        });
    }

    private String totalAmount;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.youhuiquan_parent_payment_activity:  // 优惠券入口
                if("0".equals(loginType) || "1".equals(loginType)){
                    startActivity(new Intent(getActivity(), YouHuiQuanListActivity.class));
                }

                break;
            case R.id.ll_lmf_rp:
                JpushStatsConfig.onCountEvent(getActivity(), "newxpay_home_receive_money", null);
                if (!YrmUtils.isFastDoubleClick()) {
                    startActivity(new Intent(getActivity(), PosPayActivity.class));
                }
                break;
            case R.id.ll_lmf_bd:
                if (!YrmUtils.isFastDoubleClick()) {
                    NewBillingListActivity.start(getActivity());
                }
//                JpushStatsConfig.onCountEvent(getActivity(), "newxpay_home_transaction", null);
//                if (!YrmUtils.isFastDoubleClick()) {
//                    String key = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.REFRESH_IS_OPEN);
//                    boolean isRefresh = "1".equals(key);
//                    Intent intent3 = new Intent();
//                    if (isRefresh)
//                        intent3.setClass(getActivity(), BillingListActivity.class);
//                    else
//                        intent3.setClass(getActivity(), BillingListActivityOld.class);
//                    intent3.putExtra("type", "1");
//                    intent3.putExtra("isRefresh", "Y");
//                    startActivity(intent3);
//                }


                    break;
                    case R.id.ll_lmf_rc:
                        JpushStatsConfig.onCountEvent(getActivity(), "newxpay_home_bills", null);
                        if (!YrmUtils.isFastDoubleClick(R.id.ll_lmf_rc))
                            startActivity(new Intent(getActivity(), SettlementActivity.class));

                        break;
                    case R.id.ll_buss_act:

                        JpushStatsConfig.onCountEvent(getActivity(), "xpay_home_footer_activity", null);
                        if (!YrmUtils.isFastDoubleClick()) {
                            Intent helpIntent = new Intent(getActivity(), LMFRedRainActivity.class);
                            helpIntent.putExtra("webViewUrl", "7");
                            startActivity(helpIntent);
                        }
                        break;
                    case R.id.gallery:
                        if (!YrmUtils.isFastDoubleClick()) {
                            Intent intent = new Intent(getActivity(), LMFRedRainActivity.class);
                            intent.putExtra("webViewUrl", "1");
                            startActivity(intent);
                        }
                        break;
                    case R.id.tl_mote_notice:
                        if (!YrmUtils.isFastDoubleClick()) {
                            Intent intent2 = new Intent(getActivity(), MoreNoticeActivity.class);
                            startActivity(intent2);
                        }
                        break;
                    case R.id.ll_lmf_data_summary:
                        JpushStatsConfig.onCountEvent(getActivity(), "newxpay_home_receive_summary", null);
                        if (!YrmUtils.isFastDoubleClick()) {
                            String startTime = YrmUtils.getNowDay("yyyy-MM-dd")+" 00:00:00";
                            String endTime = YrmUtils.getNowDay("yyyy-MM-dd")+" 23:59:59";
                            String loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
                            String staffType = SharedPreferencesUtil.getInstance(getActivity()).getKey("staffType");
                            // storeId 如果当前是款台店员，这个值是款台id；如果当前是店长或者门店下店员，这个值是门店id；如果当前是老板，这个值为null
                            String storeId = SharedPreferencesUtil.getInstance(getActivity()).getKey("storeId");
                            String storeName = SharedPreferencesUtil.getInstance(getActivity()).getKey("storeName");
                            String timeName = "今日";
                            SummaryActivity.start(getActivity(), loginType, staffType, startTime, endTime, timeName, storeId, storeName);
                        }
                        break;
                    case R.id.ll_value_card:

                        JpushStatsConfig.onCountEvent(getActivity(), "xpay_home_footer_valueCard", null);
                        if (!YrmUtils.isFastDoubleClick()) {
                            if ("0".equals(loginType)) {
                                Intent intent3 = new Intent(getActivity(), ValueCardActivity.class);
                                startActivity(intent3);
                            } else {
                                ToastUtil.showShortToast("暂无使用权限");
                            }
                        }

                        break;
                    case R.id.ll_sweep_order:
                        JpushStatsConfig.onCountEvent(getActivity(), "xpay_home_footer_order", null);
                        if (!YrmUtils.isFastDoubleClick()) {
                            if ("0".equals(loginType)) {
                                queryXiuCanMerInfo();
                            } else {
                                ToastUtil.showShortToast("暂无使用权限");
                            }
                        }
                        break;

                    case R.id.ll_discount_coupon://会员管理
                        Intent intent = new Intent(getActivity(), MemberManageActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.ll_loan:
                        if(!YrmUtils.isHavePermission(getActivity(), Manifest.permission.CAMERA,YrmUtils.REQUEST_PERMISSION_CAMERA)) return;
                        if (!YrmUtils.isHavePermissionList(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION}, YrmUtils.REQUEST_PERMISSION_LOCATION)) return;
                        JpushStatsConfig.onCountEvent(getActivity(), "xpay_home_footer_loans", null);
                        if (!YrmUtils.isFastDoubleClick()) {
                            if ("0".equals(loginType)) {
                                showProgressDialog("");
                                BWHelper.getInstance().setEnvironment(false);//true为预发环境,false为正式环境
                                BWHelper.getInstance().initVariable(new BigWaspListener.InitVariable() {

                                    @Override
                                    public String tid() {
                                        return android.os.Build.MODEL;
                                    }

                                    @Override

                                    public String userId() {
                                        return merchantID;
                                    }

                                    @Override
                                    public Location location() {
                                        return (new Location(Double.valueOf(latitude), Double.valueOf(longitude), (int) accuracy, AdCode));
                                    }
                                });
                                try {
                                    BWHelper.getInstance().startPage(mid, "BGC", "A", new BigWaspListener.LoadCartoon() {
                                        public void loading() {
                                            showProgressDialog("");
                                            Log.i("xjz", "加载");
                                        }

                                        public void unLoad(STATUS status) {
                                            hideProgressDialog();
                                            Log.i("xjz", "卸载");
                                        }
                                    });
                                    return;
                                } catch (Exception e) {
                                    hideProgressDialog();
                                    e.printStackTrace();
                                    return;
                                }


                            } else {
                                ToastUtil.showShortToast("暂无使用权限");
                            }
                        }
                        break;
            case R.id.ll_hui_value://惠储值
                if("0".equals(isCoupon)){//是商圈用户

                if("0".equals(loginType)){ //老板
                    if("2".equals(vcSale)){
                        Intent intent1 = new Intent(getActivity(), HuiValueCardActivity.class);
                        startActivity(intent1);
                    }else {
                        Intent intent1 = new Intent(getActivity(), HuiValueSuccessActivity.class);
                        startActivity(intent1);
                    }

                }else if("1".equals(loginType)){
                    if("2".equals(vcSale)){
                        ToastUtil.showShortToast("暂未加入商圈门店");
                    }else {
                        Intent intent1 = new Intent(getActivity(), HuiValueSuccessActivity.class);
                        startActivity(intent1);
                    }
                }else {
                    ToastUtil.showShortToast("暂无权限");
                }
                }else {//非商圈用户
                    ToastUtil.showShortToast("暂无权限");
                }

                break;
            case R.id.ll_bussiness_loan:
                if (!YrmUtils.isFastDoubleClick()) {
                    Intent intent2 = new Intent(getActivity(), LMFRedRainActivity.class);
                    intent2.putExtra("webViewUrl", "10");
                    startActivity(intent2);

                }
                break;
        }
    }

    public void queryXiuCanMerInfo() {
        JSONObject dataParam = new JSONObject();
        try {
            dataParam.put("jhMid", SharedPreferencesUtil.getInstance(getActivity()).getKey("mid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), NetUrl.QUERY_XIUCAN_INFO, dataParam, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject response = new JSONObject(o);
                    String status = response.getString("status");
                    String body = response.getString("data");
                    if ("0".equals(status)) {
                        showOrderDialog(body);
                    } else {
                        showOrderDialog(body);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });


    }

    public void showOrderDialog(final String body) {
        final Dialog mDialog = new Dialog(getActivity(), R.style.MyDialogStyle);
//        LayoutInflater inflater = (LayoutInflater) HRTApplication.getInstance().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(HRTApplication.getInstance().getApplicationContext());
        View mPopup = inflater.inflate(R.layout.show_order_dialog, null);
        mDialog.setContentView(mPopup);
        mDialog.setCanceledOnTouchOutside(true);
        mPopup.findViewById(R.id.iv_order_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SweepOrderActivity.class);
                if (body.equals("")) {
                    intent.putExtra("body", body);
                } else {
                    intent.putExtra("body", body);
                }
                startActivity(intent);
                mDialog.dismiss();
            }
        });
        mPopup.findViewById(R.id.iv_close_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        mDialog.show();
    }

    private void getNoticeInfo() {
        String url = NetUrl.NOTICEINFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("platForm", "1");
            jsonObject.put("page", String.valueOf(page));
            jsonObject.put("pageSize", String.valueOf(Constant.PAGE_SIZE));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getUpMarqueeView();
                String status;
                String msg;
                try {
                    status = response.getString("status");
                    msg = response.getString("msg");
                    Gson mGson = new Gson();
                    if ("0".equals(status)) {
                        JSONArray body = response.optJSONArray("data");
                        rolllist = mGson.fromJson(body.toString(), new
                                TypeToken<List<MoreNoticeBean.DataBean>>() {
                                }.getType());
                        if (null != rolllist && rolllist.size() > 0) {
                            tl_mote_notice.setVisibility(View.VISIBLE);
                                initUpMarqueeViewView();
                        } else {
                            mTvMsg.setText("暂无消息");
                        }
                    } else {
                        mTvMsg.setText("暂无消息");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });

    }

    /**
     * 公告
     */
    private void initUpMarqueeViewView() {
        mTvMsg.setText(rolllist.get(0).getTitle());
        if (rolllist.size() > 1) {
            mHandler.removeCallbacksAndMessages(null);  // 发送的时候，先清空，避免重复发送消息
            mHandler.sendEmptyMessageDelayed(1, 3000);
        }

        mTvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == rolllist.size()) return;
                Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
                intent.putExtra("title", rolllist.get(index).getTitle());
                intent.putExtra("time", rolllist.get(index).getCreateDate());
                intent.putExtra("content", rolllist.get(index).getContent());
                startActivity(intent);


            }
        });
    }

    private int index;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            index++;
            if (index == rolllist.size() || index == 3)  // 消息多于 3 条，只轮播 3 条。
                index = 0;
            mTvMsg.setText(rolllist.get(index).getTitle());

            mHandler.sendEmptyMessageDelayed(1, 3000);


        }
    };





    //广告位
    protected void initViewPager() {
        mBannerView.setData(list, new BannerViewPagerAdapter.OnBannerItemListener() {
            @Override
            public void bannerItemListener(int position) {
                JpushStatsConfig.onCountEvent(getActivity(), list.get(position % list.size()).getAdId(), null);
                Intent intent = new Intent(getActivity(), LMFRedRainActivity.class);
                intent.putExtra("webViewUrl", "1");
                intent.putExtra("url", list.get(position % list.size()).getLink());
                getActivity().startActivity(intent);
            }
        });
    }

    private void getUpMarqueeView() {
        String url = NetUrl.BANNER;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("adChannel", "4");
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String status;
                try {
                    status = response.getString("status");
                    Gson mGson = new Gson();
                    if ("1".equals(status)) {
                        JSONArray body = response.optJSONArray("rollingAdsList");
                        list = mGson.fromJson(body.toString(), new
                                TypeToken<List<TextBean.DataBean>>() {
                                }.getType());
                        LogUtil.d(list.size() + "list大小");
                        if (null != list && list.size() > 0) {
                            initViewPager();
                        } else {
                            iv_ad.setVisibility(View.VISIBLE);
                            mBannerView.setVisibility(View.GONE);
                        }
                    } else {
                        iv_ad.setVisibility(View.VISIBLE);
                        mBannerView.setVisibility(View.GONE);
//                        ToastUtil.show(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });

    }


    /**
     * 查询商户信息，进行本地保存
     */
    private void queryMerchantInfo() {
        Map map = new HashMap();
        map.put("mid",mid);
        String url = NetUrl.QUERY_MERCHENT_INFO;
//        String url = "http://10.51.130.132:8080/JHAdminConsole/phone/phoneMicroMerchantInfo_queryMicroMerchant.action";
        OkUtils.getInstance().postFormData(getActivity(), url, map, new MyOkCallback<AutonymCertificationInfoBean>() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(AutonymCertificationInfoBean bean) {
                if (!bean.isSuccess()){
                    ToastUtil.showShortToast("网络连接不佳");
                    return;
                }

                AutonymCertificationInfoBean.RowModel rowModel = bean.getObj().getRows();
                appStatus = rowModel.getAPPROVESTATUS();
                reason = rowModel.getPROCESSCONTEXT();
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.Name, rowModel.getBankAccName());  //账户名称
                SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.BANK_ACCNO, rowModel.getBankAccNo());  //结算账户
                SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.BANK_BRANCH, rowModel.getBankBranch());  //结算账户银行
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.LEGAL_NUM, rowModel.getLegalNum());  //身份证号
                SharedUtil.getInstance(getActivity()).putString(Constants.LEGAL_NUM, rowModel.getLegalNum());
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.MERCHANT_NAME, rowModel.getRname());  //注册店名
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.CONTACT_PHONE, rowModel.getContactPhone());  //联系电话
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.ADDR, rowModel.getRaddr());  //注册地址
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.BNO, rowModel.getBno());  //营业执照号
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.RNAME, rowModel.getRname());  //收款码名称
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.ACCNUM, rowModel.getAccNum()); //个人入账身份证号码
                SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.MIN_ACCOUNT, rowModel.getMinAmount());//最小金额
                SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.IS_FORCE_REMARK, rowModel.getIsForceRemark());
                areaType = rowModel.getAreaType();
                SharedPreferencesUtil.getInstance(getActivity()).putKey(Constants.AREATYPE, areaType);  //类型*//**//**//**//**//*
                LogUtil.d("数据保存成功" + rowModel.getBankAccName());
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }
            @Override
            public void onFinish() {
            }
            @Override
            public Class getClazz() {
                return AutonymCertificationInfoBean.class;
            }
        });
    }


    private void registerMessageReceiver() {
        flag1 = true;
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_INTENT_RECEIVER);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }


    private void registerHuiReceiver() {
        flag3 = true;
        huiVlueCardReceiver = new HuiVlueCardReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(HUI_VALUE_CARD_INTENT_RECEIVER);
        getActivity().registerReceiver(huiVlueCardReceiver, filter);
    }

    private void closeNotice() {
        flag2 = true;
        unorderedReceiver = new UnorderedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(KEY);
        getActivity().registerReceiver(unorderedReceiver, filter);
    }

    public class UnorderedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            closeData = intent.getStringExtra("key");
            if (flag2) {
                flag2 = false;
                if (getActivity() != null)
                    getActivity().unregisterReceiver(unorderedReceiver);
            }
            if (TextUtils.isEmpty(mid)) {
//                queryMerchantInfo();
//                commonMethodUtil.showPopWindow(getActivity());
            } else {
                queryMerchantInfo();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (flag1) {
            flag1 = false;
            if(mMessageReceiver!=null)
                getActivity().unregisterReceiver(mMessageReceiver);
        }
        if(flag2){
            flag2 = false;
            if(unorderedReceiver!=null)
                getActivity().unregisterReceiver(unorderedReceiver);

        }
        if(flag3){
            flag3 = false;
            if(huiVlueCardReceiver!=null)
                getActivity().unregisterReceiver(huiVlueCardReceiver);
        }
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent)  {
            queryMerchantInfo();
        }
    }


    public class HuiVlueCardReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent)  {
            vcSale = "3";
            if(flag3){
                getActivity().unregisterReceiver(huiVlueCardReceiver);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        重新获取当前时间
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime = mCalendar.getTime();
        dataEnd = formatter.format(nowTime);
        long nowTime1 = System.currentTimeMillis();
        long todayStartTime = nowTime1 - (nowTime1 + TimeZone.getDefault().getRawOffset()) % (1000 * 3600 * 24);
        Date curDate1 = new Date(todayStartTime);//获取当前时间
        dataStart = formatter.format(curDate1);
        isJhMidBindTid = SharedPreferencesUtil.getInstance(getActivity()).getKey("isJhMidBindTid");
        mVoiceOpen = SharedPreferencesUtil.getInstance(getActivity()).getKey("VoiceSwitch");
        mIsFloatOpen = SharedPreferencesUtil.getInstance(getActivity()).getIntKey1(SharedPConstant.FLOAT_IS_SHOW);
        if (("1".equals(mVoiceOpen) || TextUtils.isEmpty(mVoiceOpen)) && mIsFloatOpen == 1) {
            mFloatLayout.setVisibility(View.VISIBLE);
            mFloatLayout.setOnFloatClickListener(new FloatLayout.OnFloatClickListener() {
                @Override
                public void onFloatClick() {
                    showCheckDialog();
                }
            });
        }else{
            mFloatLayout.setVisibility(View.GONE);
        }


    }

    private void queryBillingData(int page, String mid) {
        String url = NetUrl.QUERY_LMF_NEW_BILLING_DA;
        JSONObject jsonRequest = null;
        try {


            jsonRequest = new JSONObject();

            jsonRequest.put("startDate", dataStart);
            jsonRequest.put("endDate", dataEnd);

            if (("0").equals(loginType)) {
                jsonRequest.put("merId", merchantID);
                jsonRequest.put("storeId", "");
                jsonRequest.put("desk", ""); // 款台 id
            } else if ("1".equals(loginType)) {
                jsonRequest.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey("shopId"));
                jsonRequest.put("storeId", storeId);
                jsonRequest.put("desk", ""); // 款台 id
            } else if ("2".equals(loginType)) {
                jsonRequest.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey("shopId"));
                if (staffType.equals("0")) {//门店员工
                    jsonRequest.put("storeId", storeId);
                } else if (staffType.equals("1")) {//款台员工
                    jsonRequest.put("desk", storeId); // 款台 id
                }
            }
            jsonRequest.put("page", 0); //  第几页
            jsonRequest.put("pageSize", "20");
            jsonRequest.put("isAmount", "");
            jsonRequest.put("quickChannel", "0");
            jsonRequest.put("payChannel", "");


            jsonRequest.put("transType", "");

            jsonRequest.put("loginType", loginType);

            LogUtils.d(jsonRequest.toString() + "----jsonRequest");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(getActivity(), url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                smartRefresh_layout.finishRefresh();
                try {
                    String status = response.optString("status");
                    String meessage = response.optString("message");
                    if (status.equals("0")) {
                        String totalAmt = response.getString("totalAmt");
                        String totalCount = response.getString("totalCount");
                        if (TextUtils.isEmpty(totalAmt)) {
                            tv_total.setText("0.00");
                        } else {
                            if (totalAmt.matches("[0-9]+") || totalAmt.matches("[0-9]+[.][0-9]+")) {
                                double totalAmtD = Double.parseDouble(totalAmt);
                                DecimalFormat df = new DecimalFormat("0.00");
                                totalAmount = df.format(totalAmtD);

                                tv_total.setText(df.format(totalAmtD));
                            } else {
                                totalAmount = "0.00";
                                tv_total.setText("0.00");
                            }
                        }

                        tv_today_account.setText("收款码交易" + totalCount + "笔,总计 (元)");
                    } else {
                        if(!TextUtils.isEmpty(meessage))
                            ToastUtil.showShortToast(meessage);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
               ToastUtil.showShortToast("网络连接不佳");
                smartRefresh_layout.finishRefresh();
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });

    }
    private void queryWisaData() {
        String url = NetUrl.HUICHUZHI_LIST;
        JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("storeId", storeId);
                jsonRequest.put("startDate", dataStart);
                jsonRequest.put("endDate", dataEnd);
                jsonRequest.put("page", page);
                jsonRequest.put("pageSize", "20");
                jsonRequest.put("merId",merchantID);
                jsonRequest.put("loginType",loginType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        OkUtils.getInstance().post(getActivity(), url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                smartRefresh_layout.finishRefresh();

                try {
                    String status = response.optString("status");
                    String meessage = response.optString("message");
                    if (status.equals("0")) {
                        String totalRechargeAmount = response.getString("totalRechargeAmount");
                        String totalCostAmount = response.getString("totalCostAmount");
                        String totalRechargeCount = response.getString("totalRechargeCount");
                        String totalCostCount = response.getString("totalCostCount");
                        mRechargeAmt = YrmUtils.stringToDouble(totalRechargeAmount);
                        mAmt = YrmUtils.stringToDouble(totalCostAmount);
                        if (!TextUtils.isEmpty(totalRechargeCount) && totalRechargeCount.matches("[0-9]+")) {
                            mRechargeCount = Integer.parseInt(totalRechargeCount);
                        }
                        if (!TextUtils.isEmpty(totalCostCount) && totalCostCount.matches("[0-9]+")) {
                            mCount = Integer.parseInt(totalCostCount);
                        }
                        tv_resume.setText("储值卡: 充值 "+mRechargeCount+" 笔 | "+mRechargeAmt+" 元;");
                        tv_rechange.setText(" 交易 "+mCount+" 笔 | "+mAmt+" 元");
                    } else {
                        if(!TextUtils.isEmpty(meessage))
                            ToastUtil.showShortToast(meessage);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                smartRefresh_layout.finishRefresh();
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 隐藏
            Log.d("444222", "隐藏了");
            mIsHidding = true;
        } else {  // 显示
            Log.d("444222", "显示了");
            mIsHidding = false;
        }
    }




}
