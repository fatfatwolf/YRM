package com.hybunion.yirongma.payment.Fragment;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.android.common.logging.Log;
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
import com.hybunion.yirongma.payment.activity.HuiChuZhiDetailActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.db.BillingDataListDBManager;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 惠储值列表
 */

public class HuiChuZhiListFragment extends BasicFragment {
    @Bind(R.id.total_amt_billing_list_fragment)
    TextView mTvInVestAmt;  // 充值金额
    @Bind(R.id.total_count_billing_list_fragment)
    TextView mTvInvestCount;  // 充值笔数
    @Bind(R.id.tv_str_billing_list_fragment)
    TextView mTvStr1;
    @Bind(R.id.total_amt1_billing_list_fragment)
    TextView mTvAmt;   // 消费金额
    @Bind(R.id.total_count1_billing_list_fragment)
    TextView mTvCount;  // 消费笔数
    @Bind(R.id.tv_str1_billing_list_fragment)
    TextView mTvStr2;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    @Bind(R.id.lv_billing_list_fragment)
    ListView mLv;
    @Bind(R.id.tv_no_data_billing_list_fragment)
    TextView mTvNull;

    private SimpleDateFormat mFormat, mFormat1;
    private String mStartDate, mEndDate;
    private long mStartDateLong, mEndDateLong;
    private String mLoginType, mMerId, mStoreId;
    private List<QueryTransBean.DataBean> mDataList = new ArrayList<>();
    private CommonAdapter1 mAdapter;
    private boolean mIsRefreshNow;  // 用户是否开启了实时刷新
    private boolean mIsShowLoading = true, mIsRefresh = true;
    private int mLength, mPage;
    private boolean mCanLoad, mIsLoaded;  // 是否可以加载数据，是否加载过数据
    private double mRechargeAmt, mAmt;   // 充值金额、消费金额
    private int mRechargeCount, mCount;    // 充值笔数、消费笔数

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

        mTvStr1.setText("充值金额（元）");
        mTvStr2.setText("消费金额（元）");
        mLoginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
        mMerId = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.MERCHANT_ID);
        if (!"0".equals(mLoginType))
            mStoreId = SharedPreferencesUtil.getInstance(getActivity()).getKey("storeId");
        String key = SharedPreferencesUtil.getInstance(getActivity()).getKey(SharedPConstant.REFRESH_IS_OPEN);
        mIsRefreshNow = "1".equals(key);
        handleList();
        mLv.setAdapter(mAdapter = new CommonAdapter1<QueryTransBean.DataBean>(getActivity(), mDataList, R.layout.value_card_billing_da_child_item1) {
            @Override
            public void convert(ViewHolder holder, QueryTransBean.DataBean item, int position) {
                ImageView img = holder.findView(R.id.img_czk_type);
                TextView tvStatus = holder.findView(R.id.tv_czk_type);
                TextView tvOrderNum = holder.findView(R.id.tv_order_no);
                TextView tvTime = holder.findView(R.id.tv_czk_data);
                TextView tvGone = holder.findView(R.id.tv_youhui);
                TextView tv_czk_money = holder.findView(R.id.tv_czk_money);
                holder.findView(R.id.tv_vc_num).setVisibility(View.GONE);
                Log.d("332211", "orderType 的值为：" + item.orderType);
                if ("1".equals(item.orderType)) {// 3、6--消费  1--购卡   2--充值
                    tvStatus.setText("购卡成功");
                    img.setImageResource(R.drawable.img_wechat);
                } else if ("2".equals(item.orderType)) {
                    tvStatus.setText("充值成功");
                    img.setImageResource(R.drawable.img_wechat);
                } else {
                    img.setImageResource(R.drawable.img_value_card);
                    tvStatus.setText("支付成功");
                }

                tvOrderNum.setText(YrmUtils.sub4Last(item.orderNo));
                tvTime.setText(item.transTime);
                Double amtD = YrmUtils.stringToDouble(item.transAmount);
                DecimalFormat df = new DecimalFormat("0.00");
                tv_czk_money.setText(df.format(amtD));
                tvGone.setVisibility(View.GONE);

            }
        });

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    protected void load() {
        super.load();
        mCanLoad = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mCanLoad && !mIsLoaded) {
            getData();
        }
    }

    private void getData() {
        if (mIsRefresh)
            updateTime();
        if (mIsShowLoading)
            showProgressDialog("");

        JSONObject jb = new JSONObject();
        try {
            jb.put("storeId", mStoreId);
            jb.put("startDate", mStartDate);
            jb.put("endDate", mEndDate);
            jb.put("page", mPage);
            jb.put("pageSize", "20");
            jb.put("merId", mMerId);
            jb.put("loginType", mLoginType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), NetUrl.HUICHUZHI_LIST, jb, new MyOkCallback<QueryTransBean>() {
            @Override
            public void onStart() {

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
                if (mIsRefreshNow) {
                    mRechargeAmt = YrmUtils.stringToDouble(bean.totalRechargeAmount);
                    mAmt = YrmUtils.stringToDouble(bean.totalCostAmount);
                    if (!TextUtils.isEmpty(bean.totalRechargeCount) && bean.totalRechargeCount.matches("[0-9]+")) {
                        mRechargeCount = Integer.parseInt(bean.totalRechargeCount);
                    }
                    if (!TextUtils.isEmpty(bean.totalCostCount) && bean.totalCostCount.matches("[0-9]+")) {
                        mCount = Integer.parseInt(bean.totalCostCount);
                    }
                }
                mTvInVestAmt.setText(YrmUtils.addSeparator(mRechargeAmt + ""));
                mTvAmt.setText(YrmUtils.addSeparator(mAmt + ""));
                if (!TextUtils.isEmpty(bean.totalRechargeCount))
                    mTvInvestCount.setText(bean.totalRechargeCount + " 笔");
                if (!TextUtils.isEmpty(bean.totalCostCount))

                    mTvCount.setText(bean.totalCostCount + " 笔");

                List<QueryTransBean.DataBean> data = bean.getData();
                if (mIsRefreshNow) {    // 开了实时刷新
                    if (YrmUtils.isEmptyList(data)) {
                        mLength = 0;
                        if (YrmUtils.isEmptyList(mDataList))
                            mTvNull.setVisibility(View.VISIBLE);
                    } else {
                        mLength = data.size();
                        // 数据全部插入数据库
                        for (int i = 0; i < mLength; i++) {
                            BillingDataListDBManager.getInstance(getActivity()).insertHuiChuZhiData(data.get(i), null);
                        }
                        mDataList.clear();
                        mDataList.addAll(queryDataList());
                        if (YrmUtils.isEmptyList(mDataList)) {
                            mTvNull.setVisibility(View.VISIBLE);
                        } else {
                            mTvNull.setVisibility(View.GONE);
                            mAdapter.updateList(mDataList);
                        }
                    }
                } else {   // 没开实时刷新
                    if (mIsRefresh)
                        mDataList.clear();
                    if (data == null)
                        mLength = 0;
                    else mLength = data.size();

                    mDataList.addAll(data);
                    if (YrmUtils.isEmptyList(mDataList)) {
                        mTvNull.setVisibility(View.VISIBLE);
                    } else {
                        mTvNull.setVisibility(View.GONE);
                        mAdapter.updateList(mDataList);
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
                mIsLoaded = true;
                smartRefresh_layout.finishLoadMore();
                smartRefresh_layout.finishRefresh();
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return QueryTransBean.class;
            }
        });


    }


    private void updateTime() {
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
                mIsRefresh = false;
                if (mLength == 20) {
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
                mIsRefresh = true;
                mPage = 0;
                smartRefresh_layout.setEnableLoadMore(true);
                getData();
            }
        });
    }

    public void huaWeiPushData(final QueryTransBean.DataBean bean) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                handlePushData(bean);
            }
        });
    }

    public void handlePushData(final QueryTransBean.DataBean bean) {
        BillingDataListDBManager.getInstance(getActivity()).insertHuiChuZhiData(bean, new BillingDataListDBManager.OnInsertDataListener() {
            @Override
            public void insertSuccess() {
                // 插入数据库成功，说明后台没有返回过这一条。
                // 如果当前界面已经请求过了数据，更新列表数据源；区分充值还是消费，然后增加金额和笔数
                // 如果当前界面还没有请求过数据，那么只插入数据库，不更新界面上的东西
                if (mIsLoaded) {
                    mTvNull.setVisibility(View.GONE);
                    mDataList.add(0, bean);
                    mAdapter.updateList(mDataList);
                    if (!TextUtils.isEmpty(bean.transAmount) &&
                            (bean.transAmount.matches("[0-9]+") || bean.transAmount.matches("[0-9]+[.][0-9]+"))) {
                        if ("3".equals(bean.orderType)) {   // 3-消费
                            BigDecimal bd = new BigDecimal(mAmt + "");
                            BigDecimal bd1 = new BigDecimal(Double.valueOf(bean.transAmount).toString());
                            mAmt = bd.add(bd1).doubleValue();
                            mTvAmt.setText(mAmt + "");
                            mTvCount.setText(++mCount + " 笔");

                        } else {  // 1-购卡  2-充值
                            BigDecimal bd = new BigDecimal(mRechargeAmt + "");
                            BigDecimal bd1 = new BigDecimal(Double.valueOf(bean.transAmount).toString());
                            mRechargeAmt = bd.add(bd1).doubleValue();
                            mTvInVestAmt.setText(mRechargeAmt + "");
                            mTvInvestCount.setText(++mRechargeCount + " 笔");
                        }
                    }
                }
            }
        });
    }

    private List<QueryTransBean.DataBean> queryDataList() {
        List<QueryTransBean.DataBean> dataList = BillingDataListDBManager.getInstance(getActivity()).
                huiChuZhiQueryData(mStartDateLong + "", mEndDateLong + "", mPage);
        return dataList;

    }

}
