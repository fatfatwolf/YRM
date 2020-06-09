package com.hybunion.yirongma.payment.Fragment;

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
import com.hybunion.yirongma.base.BasicFragment;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.adapter.LMFBillDataDetailAdapter;
import com.hybunion.yirongma.payment.db.BillingDataListDBManager;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 订单列表
 */

public class BillingListFragment extends BasicFragment {
    @Bind(R.id.total_amt_billing_list_fragment)
    TextView mTvTotalAmt;
    @Bind(R.id.total_count_billing_list_fragment)
    TextView mTvTotalCount;
    @Bind(R.id.total_amt1_billing_list_fragment)
    TextView mTvCouponTotalAmt;
    @Bind(R.id.total_count1_billing_list_fragment)
    TextView mTvCouponTotalCount;
    @Bind(R.id.tv_no_data_billing_list_fragment)
    TextView mTvNull;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    @Bind(R.id.lv_billing_list_fragment)
    ListView mLv;
    @Bind(R.id.data_line_billing_list)
    View mDataLine;
    @Bind(R.id.coupon_data_parent_billing_list)
    RelativeLayout mCouponDataParent;

    private String mLoginType, mMerchantID, mMid, mStaffType, mStoreId;
    private String mStartDate, mEndDate; // 请求数据用的开始、结束时间
    private SimpleDateFormat mFormat1, mFormat;
    private int mPage;
    private double mTotalAmt = 0.00, mRefundTotalAmt = 0.00;
    private int mCount = 0, mRefundCount = 0;
    private List<QueryTransBean.DataBean> mDataList = new ArrayList<>(); // 列表数据源
    private int mLengthDataList;
    private LMFBillDataDetailAdapter mLMFMemberBillingDetailAP;
    private boolean mIsShowLoading = true;
    private boolean mDataListIsRefresh = true;
    private long mStartDateLong, mEndDateLong;
    private boolean mIsRefreshNow;  // 用户是否开启了实时刷新

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_billing_list;
    }

    @Override
    protected void initView() {
        super.initView();
        mLoginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
        if (!"0".equals(mLoginType))  // 不是老板，进入此界面首先获取当前门店 id
            mStoreId = SharedPreferencesUtil.getInstance(getActivity()).getKey("storeId");
        mMerchantID = SharedPreferencesUtil.getInstance(getActivity()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        mMid = SharedPreferencesUtil.getInstance(getActivity()).getKey("mid");
        mStaffType = SharedPreferencesUtil.getInstance(getActivity()).getKey("staffType");

        // 没有加入商圈， 不显示优惠券统计数据
//        String isCoupon = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.IS_COUPON);
//        if (!"0".equals(isCoupon)) {
//            mDataLine.setVisibility(View.INVISIBLE);
//            mCouponDataParent.setVisibility(View.INVISIBLE);
//        }
        mLMFMemberBillingDetailAP = new LMFBillDataDetailAdapter(getActivity(), mDataList);
        mLv.setAdapter(mLMFMemberBillingDetailAP);

        String key = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.REFRESH_IS_OPEN);
        mIsRefreshNow = "1".equals(key);

        updateDateNTime();
        handleList();
    }

    @Override
    protected void load() {
        super.load();
        getData(); // 请求数据
    }


    //  更新时间
    private void updateDateNTime() {
        mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        mEndDate = mFormat.format(new Date(System.currentTimeMillis()));
        mStartDate = mFormat1.format(new Date(System.currentTimeMillis())) + " 00:00:00";
        mStartDateLong = YrmUtils.getLongDate(mStartDate);
        mEndDateLong = YrmUtils.getLongDate(mEndDate);
    }

    private void handleList() {
        smartRefresh_layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mIsShowLoading = true;
                mDataListIsRefresh = false;
                if (mLengthDataList == 20) {
                    mPage++;
                    // 请求数据
                    getData();
                } else {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mIsShowLoading = false;
                mDataListIsRefresh = true;
                smartRefresh_layout.setEnableLoadMore(true);
                mPage = 0;
                getData();
            }
        });

    }


    // 列表请求数据
    private void getData() {
        if (mIsShowLoading)
            showProgressDialog("");

        if (mDataListIsRefresh)  // 下拉刷新，更新时间
            updateDateNTime();

        try {
            JSONObject dataParam = new JSONObject();
            dataParam.put("startDate", mStartDate);
            dataParam.put("endDate", mEndDate);

            if (("0").equals(mLoginType)) {
                dataParam.put("merId", mMerchantID);
                dataParam.put("storeId", mStoreId);
                dataParam.put("desk", ""); // 款台 id
            } else if ("1".equals(mLoginType)) {
                dataParam.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey("shopId"));
                dataParam.put("storeId", mStoreId);
                dataParam.put("desk", ""); // 款台 id
            } else if ("2".equals(mLoginType)) {
                dataParam.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey("shopId"));
                if (mStaffType.equals("0")) {//门店员工
                    dataParam.put("storeId", mStoreId);
                } else if (mStaffType.equals("1")) {//款台员工
                    dataParam.put("desk", mStoreId); // 款台 id
                }
            }
            dataParam.put("page", mPage); //  第几页
            dataParam.put("pageSize", "20");
            dataParam.put("loginType", mLoginType);
            OkUtils.getInstance().post(getActivity(), NetUrl.BILLING_LIST, dataParam, new MyOkCallback<QueryTransBean>() {
                @Override
                public void onStart() {
                    if (mIsShowLoading)
                        showProgressDialog("");
                }

                @Override
                public void onSuccess(QueryTransBean bean) {
                    String status = bean.getStatus();
                    String msg = bean.getMessage();
                    if (!"0".equals(status)) {
                        if (!TextUtils.isEmpty(msg)) {
                            ToastUtil.showShortToast(msg);
                        } else {
                            ToastUtil.showShortToast("网络连接不佳");
                        }
                        return;
                    }
                    String countStr = bean.totalCount;
                    String totalAmtStr = bean.totalAmt;
                    if (!TextUtils.isEmpty(countStr) && countStr.matches("[0-9]+")) {
                        mCount = Integer.parseInt(countStr);
                    }
                    mTotalAmt = YrmUtils.stringToDouble(totalAmtStr);
                    mTvTotalAmt.setText(YrmUtils.addSeparator(mTotalAmt + ""));
                    mTvTotalCount.setText(mCount + "笔");

                    String refundAmt = bean.refundAmount;
                    String refundCount = bean.refundCount;
                    if (!TextUtils.isEmpty(refundCount) && refundCount.matches("[0-9]+")) {
                        mRefundCount = Integer.parseInt(refundCount);
                    }
                    mRefundTotalAmt = YrmUtils.stringToDouble(refundAmt);
                    mTvCouponTotalAmt.setText(YrmUtils.addSeparator(mRefundTotalAmt + ""));
                    mTvCouponTotalCount.setText(mRefundCount + "笔");

                    if (mIsRefreshNow) {   // 用户开启了实时刷新功能

                        // 将请求来的数据 做插入或更新操作，没有新数据则不处理。
                        if (bean.getData() == null || bean.getData().size() == 0) {
                            mLengthDataList = 0;
                        } else {
                            List<QueryTransBean.DataBean> dataList = bean.getData();
                            mLengthDataList = dataList.size();
                            for (int i = 0; i < dataList.size(); i++) {
                                // 根据 payStyle 字段，给 payType 赋值，筛选用
                                if (!TextUtils.isEmpty(dataList.get(i).payStyle)) {
                                    if (dataList.get(i).payStyle.contains("微信")) {
                                        dataList.get(i).payType = "0";
//                                    dataList.get(i).iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/WeChatPay.png";
                                    } else if (dataList.get(i).payStyle.contains("支付宝")) {
                                        dataList.get(i).payType = "1";
//                                    dataList.get(i).iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/Alipay.png";
                                    } else if (dataList.get(i).payStyle.contains("云闪付")) {
                                        dataList.get(i).payType = "3";
//                                    dataList.get(i).iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/UnionPay.png";
                                    } else if (dataList.get(i).payStyle.contains("福利卡")) {
                                        dataList.get(i).payType = "4";
//                                    dataList.get(i).iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/FuLiCard.png";
                                    } else if (dataList.get(i).payStyle.contains("和卡")) {
                                        dataList.get(i).payType = "5";
//                                    dataList.get(i).iconUrl = "https://image.hybunion.cn/CubeImages/jhPayTypeLogo/FuLiCard.png";
                                    }
                                }
                            }
                            BillingDataListDBManager.getInstance(getActivity()).insertDatas(dataList);
                        }
                        // 从数据库取出数据，更新界面。无论上拉下拉，都从数据库重新拿相应条数的数据，更新界面。
                        // 从数据库取出的每次都是 20*(page+1) 条数据
                        mDataList.clear();
                        mDataList.addAll(updateList());
                        if (YrmUtils.isEmptyList(mDataList)) {
                            mTvNull.setVisibility(View.VISIBLE);
                        } else {
                            mTvNull.setVisibility(View.GONE);
                            mLMFMemberBillingDetailAP.addAllList(mDataList);
                        }

                    } else {  // 未开启实时刷新功能

                        if (mDataListIsRefresh)
                            mDataList.clear();
                        mDataList.addAll(bean.getData());
                        if (YrmUtils.isEmptyList(mDataList)) {
                            mTvNull.setVisibility(View.VISIBLE);
                        } else {
                            mTvNull.setVisibility(View.GONE);
                            mLMFMemberBillingDetailAP.addAllList(mDataList);
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    ToastUtil.showShortToast("网络连接不佳");
                    if (YrmUtils.isEmptyList(mDataList))
                        mTvNull.setVisibility(View.GONE);
                }

                @Override
                public void onFinish() {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.finishRefresh();
                    hideProgressDialog();
                }

                @Override
                public Class getClazz() {
                    return QueryTransBean.class;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // 从数据库根据当前时间拿出相应的数据，并更新数据列表
    private List<QueryTransBean.DataBean> updateList() {
        List<QueryTransBean.DataBean> dataList = BillingDataListDBManager.getInstance(getActivity()).
                queryDataByDate(mStartDateLong + "", mEndDateLong + "", mPage);
        return dataList;
    }

    public void handleHuaWeiPush(final QueryTransBean.DataBean bean) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handlePush(bean);
            }
        });
    }

    // 收到推送后，将推送数据插入数据库
    // 插入成功，说明该条推送和之前请求的后台数据不重复，那么后台返回的交易金额和笔数就不包括这一条
    // so，将这一条的金额和笔数加入总数。
    public void handlePush(final QueryTransBean.DataBean bean) {
        BillingDataListDBManager.getInstance(getActivity()).insertData(bean, new BillingDataListDBManager.OnInsertDataListener() {
            @Override
            public void insertSuccess() {
                mDataList.add(0, bean);
                mLMFMemberBillingDetailAP.addAllList(mDataList);
                mTvNull.setVisibility(View.GONE);
                double trans = 0.00;
                if (!TextUtils.isEmpty(bean.transAmount) && (bean.transAmount.matches("[0-9]+")
                        || bean.transAmount.matches("[0-9]+[.][0-9]+"))) {
                    trans = Double.parseDouble(bean.transAmount);
                }
                BigDecimal bd = new BigDecimal(Double.valueOf(mTotalAmt).toString());
                BigDecimal bd1 = new BigDecimal(Double.valueOf(trans).toString());
                mTotalAmt = bd.add(bd1).doubleValue();
                mTvTotalAmt.setText(mTotalAmt + "");
                mTvTotalCount.setText(++mCount + "笔");
            }
        });
    }


}
