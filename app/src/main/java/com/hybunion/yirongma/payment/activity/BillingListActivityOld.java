package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.bean.QueryTransBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.util.GetApplicationInfoUtil;
import com.hybunion.yirongma.common.view.MySwipe;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.adapter.BranchAdapter;
import com.hybunion.yirongma.payment.adapter.KuanTaiListAdapter;
import com.hybunion.yirongma.payment.adapter.LMFBillDataDetailAdapter;
import com.hybunion.yirongma.payment.adapter.StoreListAdapter;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 具体的门店列表
 */

public class BillingListActivityOld extends BaseActivity implements View.OnClickListener {
    private ListView mExpandableListView;
    private TextView tv_noData, tv_head;
    private int pageKuanTai = 0, flag = 0, payType = -1, pageStore = 0, page2 = 0;
    private String mStoreId = ""; // 筛选选中的 storeId
    private Button img_add, img_donw;
    private BranchAdapter mBranchAdapter;
    private LMFBillDataDetailAdapter mLMFMemberBillingDetailAP;
    private static final int SUCCESS = 0x1001; // 网络回调成功
    private static final int ERROR = 0x1002; // 网络回调失败
    private Gson mGson;
    private Button bt_scree;
    private String merchantID, count, mid, dataStart, dataEnd, checBox, accountFlag;
    private LinearLayout ll_branch_name, ib_back;
    private String merchantName, storeName;
    QueryTransBean bean;
    private String mLoginType; // 0-老板  非0-店长或店员
    private String isAmount = "", type;
    private SmartRefreshLayout smartRefresh_layout;
    private TextView tv_count;
    private TextView tv_time;
    List<StoreManageBean.ObjBean> storeList = new ArrayList<>();
    KuanTaiListAdapter kuanTaiListAdapter;
    StoreListAdapter storeListAdapter;
    private boolean mDataListIsRefresh = true;
    private boolean mIsStoreListRefresh = true;
    private boolean mIsKuanTaiListRefresh = true;
    public static String ACTION_DIALOG_RECEIVER = "com.hybunion.yirongma.jpush.dialog";
    private int lengthDataList = 0;
    private String mSelectedKuanTaiId = "", staffType, mKuanTaiId = "";
    private String isRefresh;//判断是否刷新
    public List<StoreManageBean.ObjBean> kuantaiList = new ArrayList<>();
    List<QueryTransBean.DataBean> dataBeanXes = new ArrayList<>();
    private String mSelectStoreName, mStoreName, mSelectKuanTaiName, mKuanTaiName;
    private String mSelectedStoreId;
    MyBottonPopWindow popWindow;
    private TextView tv_youhui_card;
    private int couponCount = 0;   // 总笔数
    private double couponTotalAmt = 0.00;   // 总金额
    int storePosition = 0;
    private Calendar mCalendar;
    private SimpleDateFormat formatter;
    private SimpleDateFormat formatter1;

    @Override

    protected void initView() {
        setContentView(R.layout.activity_billing_list_old_layout);
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        merchantID = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        mid = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("mid");
        smartRefresh_layout = (SmartRefreshLayout) findViewById(R.id.smartRefresh_layout);
        img_add = (Button) findViewById(R.id.img_add);
        img_donw = (Button) findViewById(R.id.img_donw);
        mBranchAdapter = new BranchAdapter(BillingListActivityOld.this);
        storeListAdapter = new StoreListAdapter(this);
        mExpandableListView = (ListView) findViewById(R.id.el_billing_da);
        ll_branch_name = (LinearLayout) findViewById(R.id.ll_branch_name);
        tv_youhui_card = findViewById(R.id.tv_youhui_card);
        staffType = SharedPreferencesUtil.getInstance(BillingListActivityOld.this).getKey("staffType");
        type = getIntent().getStringExtra("type");
        isRefresh = getIntent().getStringExtra("isRefresh");
        if (null == isRefresh) {
            isRefresh = "N";
        }
//        ll_branch_name.setOnClickListener(this);
        tv_head = (TextView) findViewById(R.id.tv_head);
        tv_noData = (TextView) findViewById(R.id.tv_billing_record_nodata);
        bt_scree = (Button) findViewById(R.id.bt_scree);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_time = (TextView) findViewById(R.id.tv_date);
        bt_scree.setOnClickListener(this);
        ib_back = (LinearLayout) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        kuanTaiListAdapter = new KuanTaiListAdapter(this);
        mLMFMemberBillingDetailAP = new LMFBillDataDetailAdapter(BillingListActivityOld.this, dataBeanXes);
        mExpandableListView.setAdapter(mLMFMemberBillingDetailAP);
        if (!"0".equals(mLoginType))  // 不是老板，进入此界面首先获取当前门店 id
            mStoreId = SharedPreferencesUtil.getInstance(this).getKey("storeId");

        if (mCalendar == null)
            mCalendar = Calendar.getInstance();

        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        mCalendar.set(Calendar.DATE, mCalendar.get(Calendar.DATE));
        Date nowTime = mCalendar.getTime();
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowTimeStr = formatter1.format(nowTime) + " 00:00:00";
        tv_time.setText(sdf.format(nowTime));
        dataStart = TextUtils.isEmpty(getIntent().getStringExtra("dateStart")) ? nowTimeStr : getIntent().getStringExtra("dateStart");
        dataEnd = TextUtils.isEmpty(getIntent().getStringExtra("dateEnd")) ? formatter.format(curDate) : getIntent().getStringExtra("dateEnd");
//        storeId = SharedPreferencesUtil.getInstance(this).getKey("storeId");
        merchantName = SharedPreferencesUtil.getInstance(this).getKey("merchantName");
        storeName = SharedPreferencesUtil.getInstance(this).getKey("storeName");
        if ("1".equals(type)) {
            tv_head.setText("订单明细");
        } else if ("2".equals(type)) {
            tv_head.setText("收款明细");
        }

//        tv_time.setText(dataStart + " 至 " + dataEnd);
        handleList();

//        registerDialogReceiver();
        mExpandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final QueryTransBean.DataBean bean = dataBeanXes.get(position);

                Intent detailIntent = new Intent(BillingListActivityOld.this, RefundDetailsActivity.class);
                detailIntent.putExtra("transAmount", bean.transAmount);
                detailIntent.putExtra("orderNo", bean.orderNo);
                detailIntent.putExtra("payChannel", bean.payChannel);
                detailIntent.putExtra("transDate", bean.transDate);
                detailIntent.putExtra("payStyle", bean.payStyle);
                detailIntent.putExtra("periodType", bean.periodType);
                if (("0").equals(mLoginType)) {
                    detailIntent.putExtra("merId", GetApplicationInfoUtil.getMerchantId());
                } else {
                    detailIntent.putExtra("merId", SharedPreferencesUtil.getInstance(BillingListActivityOld.this).getKey("shopId"));
                }
                detailIntent.putExtra("UUID", bean.UUID);
                startActivity(detailIntent);

            }
        });
    }


    Dialog dialog;
    TextView tvContent, tv_text;
    TextView tv_dialog_title;
    Button okButton;

    private void showBalanceDialog(String msg1, String msg2) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_jpush, null);
        if (dialog == null) {
            dialog = new Dialog(this, R.style.MyDialog);
            dialog.setContentView(view);
            tvContent = (TextView) view.findViewById(R.id.tvContent);
            tv_text = view.findViewById(R.id.tv_text);
            okButton = (Button) view.findViewById(R.id.bt_sure);
            tv_dialog_title = view.findViewById(R.id.tv_dialog_title);
            tv_dialog_title.setText("收款成功");
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        tvContent.setText(Html.fromHtml(msg1));
        tv_text.setText(msg2);

        dialog.show();


    }

    private String mTid = "", mSourceTid = "";  // 增加了收款码、收银插件 id
    private boolean mIsShowLoading = true; // 是否显示 Loading

    private void queryBillingData(int page2) {
        JSONObject dataParam = new JSONObject();
        try {
            dataParam.put("startDate", dataStart);
            dataParam.put("endDate", dataEnd);

            if (("0").equals(mLoginType)) {
                dataParam.put("merId", merchantID);
                dataParam.put("storeId", mStoreId);
                dataParam.put("desk", mKuanTaiId); // 款台 id
            } else if ("1".equals(mLoginType)) {
                dataParam.put("merId", SharedPreferencesUtil.getInstance(this).getKey("shopId"));
                dataParam.put("storeId", mStoreId);
                dataParam.put("desk", mKuanTaiId); // 款台 id
            } else if ("2".equals(mLoginType)) {
                dataParam.put("merId", SharedPreferencesUtil.getInstance(this).getKey("shopId"));
                if ("2".equals(type)) {   // 从班结汇总进来
                    dataParam.put("storeId", "");
                    dataParam.put("desk", mStoreId);
                } else {
                    if (staffType.equals("0")) {//门店员工
                        dataParam.put("storeId", mStoreId);
                    } else if (staffType.equals("1")) {//款台员工
                        dataParam.put("desk", mStoreId); // 款台 id
                    }
                }

            }
            dataParam.put("page", page2); //  第几页
            dataParam.put("pageSize", "20");
            dataParam.put("isAmount", isAmount);
            dataParam.put("tid", mTid);
            dataParam.put("sourceTid", mSourceTid);
            dataParam.put("quickChannel", "0");
            dataParam.put("payChannel", (payType == -1 ? "" : payType) + "");
            if ("1".equals(checBox)) {
                dataParam.put("transType", "3");
            } else {
                dataParam.put("transType", "");
            }
            dataParam.put("loginType", mLoginType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.QUERY_LMF_NEW_BILLING_DA, dataParam, new MyOkCallback<QueryTransBean>() {
            @Override
            public void onStart() {
                if (mIsShowLoading) {
                    showProgressDialog("");
                }
            }

            @Override
            public void onSuccess(QueryTransBean bean) {
                try {
                    count = bean.totalCount;
                    String totalAmt = bean.totalAmt;
                    if (TextUtils.isEmpty(
                            totalAmt)) {
                        tv_count.setText("交易总计:¥" + "0.00" + " | " + count + "笔");
                    } else {
                        if (totalAmt.matches("[0-9]+") || totalAmt.matches("[0-9]+[.][0-9]+")) {
                            double totalAmtD = Double.parseDouble(totalAmt);
                            DecimalFormat df = new DecimalFormat("0.00");
                            tv_count.setText("交易总计:¥" + df.format(totalAmtD) + " | " + count + "笔");
                        } else {
                            tv_count.setText("0.00");
                        }
                    }
                    String discountAmt = bean.discountAmt;
                    String discountCount = bean.discountCount;
                    if (!TextUtils.isEmpty(discountCount) && discountCount.matches("[0-9]+")) {
                        couponCount = Integer.parseInt(discountCount);
                    }
                    if (!TextUtils.isEmpty(discountAmt) && (discountAmt.matches("[0-9]+") || discountAmt.matches("[0-9]+[.][0-9]+]"))) {
                        couponTotalAmt = Double.parseDouble(discountAmt);
                    }
                    if (couponCount == 0) {
                        tv_youhui_card.setVisibility(View.GONE);
                    } else {
                        tv_youhui_card.setVisibility(View.VISIBLE);
                        tv_youhui_card.setText("券核销：¥" + discountAmt + "元 | " + discountCount + "笔");

                    }

                    String status = bean.getStatus();
                    if ("0".equals(status)) {
                        mCalendar.set(Calendar.DATE, mCalendar.get(Calendar.DATE));
                        Date nowTime = mCalendar.getTime();
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String nowTimeStr = formatter1.format(nowTime) + " 00:00:00";
                        dataStart = TextUtils.isEmpty(getIntent().getStringExtra("dateStart")) ? nowTimeStr : getIntent().getStringExtra("dateStart");
                        dataEnd = TextUtils.isEmpty(getIntent().getStringExtra("dateEnd")) ? formatter.format(curDate) : getIntent().getStringExtra("dateEnd");
//                                tv_time.setText(dataStart + " 至 " + dataEnd);

                        if (bean.getData() != null) {
                            if (mDataListIsRefresh) {
                                dataBeanXes.clear();
                            }
                            dataBeanXes.addAll(bean.getData());
                            if (bean.getData() != null && bean.getData().size() != 0)
                                lengthDataList = bean.getData().size();
                            else
                                lengthDataList = 0;
                            lengthDataList = bean.getData().size();
                            LogUtil.d(dataBeanXes.size() + "====大小");
                            if (dataBeanXes.size() == 0) {
//                                    mDataListIsRe
// fresh = false;

                                smartRefresh_layout.setVisibility(View.GONE);
                                tv_noData.setVisibility(View.VISIBLE);
                            } else {
//                                    mDataListIsRefresh = true;
                                smartRefresh_layout.setVisibility(View.VISIBLE);
                                tv_noData.setVisibility(View.GONE);
                                mLMFMemberBillingDetailAP.addAllList(dataBeanXes);
                            }
                        }
                    } else {
                        hideProgressDialog();
                        tv_noData.setVisibility(View.VISIBLE);
                        dataBeanXes.clear();
                        mLMFMemberBillingDetailAP.addAllList(dataBeanXes);
//                            mLMFMemberBillingDetailAP.clearData();
                    }
                } catch (JsonSyntaxException e) {
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
                smartRefresh_layout.finishRefresh();
                smartRefresh_layout.finishLoadMore();
            }

            @Override
            public Class getClazz() {
                return QueryTransBean.class;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.bt_scree:
                Intent intent = new Intent(BillingListActivityOld.this, ScreeningActivity.class);
                if (mLoginType.equals("0")) {
                    if (!TextUtils.isEmpty(mStoreId)) {
                        if (TextUtils.isEmpty(mKuanTaiId)) {
                            intent.putExtra("storeId", mStoreId);
                        } else {
                            intent.putExtra("storeId", mKuanTaiId);
                        }
                    } else {
                        intent.putExtra("storeId", "");
                    }
                } else if (mLoginType.equals("1")) {
                    if (TextUtils.isEmpty(mKuanTaiId)) {
                        intent.putExtra("storeId", mStoreId);
                    } else {
                        intent.putExtra("storeId", mKuanTaiId);
                    }

                } else {
                    intent.putExtra("storeId", mStoreId);
                }

                startActivityForResult(intent, 101);
                break;
            default:
        }
    }

    /**
     * 加载刷新监听
     */
    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mDataListIsRefresh = false;
                if (lengthDataList == 20) {
                    page2++;
                    // 请求数据
                    queryBillingData(page2);

                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mIsShowLoading = false;
                mDataListIsRefresh = true;
                page2 = 0;
                smartRefresh_layout.setEnableLoadMore(true);
                queryBillingData(page2);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsShowLoading = true;
        mCalendar.set(Calendar.DATE, mCalendar.get(Calendar.DATE));
        Date nowTime = mCalendar.getTime();
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowTimeStr = formatter1.format(nowTime) + " 00:00:00";
        dataStart = TextUtils.isEmpty(getIntent().getStringExtra("dateStart")) ? nowTimeStr : getIntent().getStringExtra("dateStart");
        dataEnd = TextUtils.isEmpty(getIntent().getStringExtra("dateEnd")) ? formatter.format(curDate) : getIntent().getStringExtra("dateEnd");
        if ("Y".equals(isRefresh)) {
            isRefresh = "N";
            queryBillingData(page2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(dialogReceiver);
        SharedPreferencesUtil.getInstance(this).putKey("timeStart", "");
        SharedPreferencesUtil.getInstance(this).putKey("timeEnd", "");
        SharedPreferencesUtil.getInstance(this).putKey("dataStart", "");
        SharedPreferencesUtil.getInstance(this).putKey("dataEnd", "");
        SharedPreferencesUtil.getInstance(this).putKey("checBoxFlag", "");
        SharedPreferencesUtil.getInstance(this).putKey("payType", "");
    }
}
