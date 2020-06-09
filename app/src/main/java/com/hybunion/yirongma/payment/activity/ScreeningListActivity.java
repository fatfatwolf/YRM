package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.QueryTransBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.LMFBillDataDetailAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 订单列表-筛选后 跳转的列表
 */

public class ScreeningListActivity extends BasicActivity {
    @Bind(R.id.tv_time_screening_list)
    TextView mTvTime;
    @Bind(R.id.total_amt_screening_list)
    TextView mTvTotalAmt;
    @Bind(R.id.total_count_screening_list)
    TextView mTvTotalCount;
    @Bind(R.id.total_amt1_screening_list)
    TextView mTvCouponTotalAmt;
    @Bind(R.id.total_count1_screening_list)
    TextView mTvCouponTotalCount;


    @Bind(R.id.tv_noData_screening_list_activity)
    TextView mTvNull;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    @Bind(R.id.list_screening_list_activity)
    ListView mLv;
    @Bind(R.id.title_bar_screening_list)
    TitleBar mTitleBar;
    @Bind(R.id.data_line_billing_list)
    View mDataLine;
    @Bind(R.id.coupon_data_parent_billing_list)
    RelativeLayout mCouponDataParent;

    private String mLoginType;
    private String mMerchantId;
    private String mStartDate, mEndDate, mCheckBoxFlag, mStoreId, mDeskId,
            mIsAmount = "", //筛选 “<300” ，mIsAmount = "1" ; 筛选 “>=300” ，misAmount = "2"
            mTid,  // 收款码 id
            mSourceTid; // 收银插件 id
    private int mPayType = -1; // 默认为 -1， 即没有选择付款方式，就是全都查询
    private long mStartDateLong, mEndDateLong;
    private int mPage = 0;
    private String mStaffType;
    private boolean mIsRefresh;
    private int mDataListSize;
    private boolean mIsShowLoading = true;  // 是否需要展示 loading
    private List<QueryTransBean.DataBean> mDataList = new ArrayList<>();
    private LMFBillDataDetailAdapter mAdapter;
    public static final int PAGE_SIZE = 20;
    private String mStoreName, mKuanTaiName;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_screening_list_layout;
    }

    @Override
    public void initView() {
        super.initView();
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if ("0".equals(mLoginType)) {
            mMerchantId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        } else {
            mMerchantId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
        }
        mStaffType = SharedPreferencesUtil.getInstance(this).getKey("staffType");

        Intent intent = getIntent();
        mStartDate = intent.getStringExtra("dataStart");
        mEndDate = intent.getStringExtra("dataEnd");
        mTvTime.setText(mStartDate + " 至 " + mEndDate);
        mStartDateLong = YrmUtils.getLongDate(mStartDate);
        mEndDateLong = YrmUtils.getLongDate(mEndDate);
        // 如果是 “1” ，则筛选了退款，transType应该传 3，否则传 ”“
        mCheckBoxFlag = intent.getStringExtra("checBox");
        mStoreId = intent.getStringExtra("screenStoreId");
        mDeskId = intent.getStringExtra("screenKuanTaiId");
        mIsAmount = intent.getStringExtra("isAmount");
        mTid = intent.getStringExtra("tid");
        mSourceTid = intent.getStringExtra("sourceId");
        mPayType = intent.getIntExtra("payType", -1);  // 根据筛选条件传过来的。
        mStoreName = TextUtils.isEmpty(intent.getStringExtra("storeName")) ? "" : intent.getStringExtra("storeName");
        mKuanTaiName = TextUtils.isEmpty(intent.getStringExtra("kuanTaiName")) ? "" : intent.getStringExtra("kuanTaiName");
        if (TextUtils.isEmpty(mStoreName) && TextUtils.isEmpty(mKuanTaiName)) {
            mTitleBar.setTv_titlebar_back_titleText("所有门店");
        } else {
            mTitleBar.setTv_titlebar_back_titleText(mStoreName + " " + mKuanTaiName);
        }
        // 没有加入商圈， 不显示优惠券统计数据
//        String isCoupon = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.IS_COUPON);
//        if (!"0".equals(isCoupon)){
//            mDataLine.setVisibility(View.INVISIBLE);
//            mCouponDataParent.setVisibility(View.INVISIBLE);
//        }

        mAdapter = new LMFBillDataDetailAdapter(this, mDataList);
        mLv.setAdapter(mAdapter);
        handleList();
    }


    @Override
    protected void load() {
        super.load();
        getData();
    }

    private void getData(){
        JSONObject dataParam = new JSONObject();
        try {
            dataParam.put("startDate", mStartDate);
            dataParam.put("endDate", mEndDate);
            dataParam.put("merId", mMerchantId);
            if (("0").equals(mLoginType)) {
                dataParam.put("storeId", mStoreId);
                dataParam.put("desk", mDeskId); // 款台 id
            } else if ("1".equals(mLoginType)) {
                dataParam.put("storeId", mStoreId);
                dataParam.put("desk", mDeskId); // 款台 id
            } else if ("2".equals(mLoginType)) {
                if (mStaffType.equals("0")) {//门店员工
                    dataParam.put("storeId", mStoreId);
                } else if (mStaffType.equals("1")) {//款台员工
                    dataParam.put("desk", mStoreId); // 款台 id
                }
            }
            dataParam.put("page", mPage); //  第几页
            dataParam.put("pageSize", PAGE_SIZE + "");
            if ("".equals(mIsAmount)) {
                dataParam.put("isAmount", "");
            } else {
                dataParam.put("isAmount", "1".equals(mIsAmount) ? "<300" : ">=300");
            }
            dataParam.put("tid", mTid);
            dataParam.put("sourceTid", mSourceTid);
            dataParam.put("payChannel", (mPayType == -1 ? "" : mPayType) + "");
            if ("1".equals(mCheckBoxFlag)) {
                dataParam.put("transType", "3");
            } else {
                dataParam.put("transType", "");
            }
            dataParam.put("loginType", mLoginType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.BILLING_LIST, dataParam, new MyOkCallback<QueryTransBean>() {
            @Override
            public void onStart() {
                if (mIsShowLoading)
                    showLoading();
            }

            @Override
            public void onSuccess(QueryTransBean bean) {
                String status = bean.getStatus();
                String msg = bean.getMessage();
                if (!"0".equals(status)){
                    if (!TextUtils.isEmpty(msg)){
                        ToastUtil.showShortToast(msg);
                    }else{
                        ToastUtil.showShortToast("网络连接不佳");
                    }
                    return;
                }
                if (mIsRefresh) {
                    mDataList.clear();
                }
                mDataList.addAll(bean.getData());
                mDataListSize = bean.getData().size();
                mTvTotalAmt.setText(YrmUtils.addSeparator(bean.totalAmt));
                mTvTotalCount.setText(bean.totalCount + "笔");
                String discountAmt = bean.refundAmount;
                String discountCount = bean.refundCount;
                mTvCouponTotalAmt.setText(YrmUtils.addSeparator(discountAmt));
                mTvCouponTotalCount.setText(discountCount + "笔");
                if (mDataList == null || mDataList.size() == 0) {
                    smartRefresh_layout.setVisibility(View.GONE);
                    mTvNull.setVisibility(View.VISIBLE);
                } else {
                    smartRefresh_layout.setVisibility(View.VISIBLE);
                    mTvNull.setVisibility(View.GONE);
                    mAdapter.addAllList(mDataList);
                }
            }

            @Override
            public void onError(Exception e) {
                com.hybunion.netlibrary.utils.ToastUtil.showShortToast("网络连接不佳");
                if (YrmUtils.isEmptyList(mDataList))
                    mTvNull.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                smartRefresh_layout.finishLoadMore();
                smartRefresh_layout.finishRefresh();
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return QueryTransBean.class;
            }
        });

    }

    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mIsShowLoading = true;
                mIsRefresh = false;
                if (mDataListSize == PAGE_SIZE) {
                    mPage++;
                    // 请求数据
                    load();

                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mIsShowLoading = false;
                mIsRefresh = true;
                smartRefresh_layout.setEnableLoadMore(true);
                mPage = 0;
                load();
            }
        });
    }

}
