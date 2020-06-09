package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.QueryTransBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 *
 */

public class HuiChuZhiScreeningListActivity extends BasicActivity {
    @Bind(R.id.title_bar_screening_list)
    TitleBar mTitleBar;
    @Bind(R.id.tv_time_screening_list)
    TextView mTvTime;
    @Bind(R.id.total_amt_screening_list)
    TextView mTvChargeAmt;
    @Bind(R.id.total_count_screening_list)
    TextView mTvChargeCount;
    @Bind(R.id.tv_str_screening_list)
    TextView mTvStr1;
    @Bind(R.id.total_amt1_screening_list)
    TextView mTvConsumeAmt;
    @Bind(R.id.total_count1_screening_list)
    TextView mTvConsumeCount;
    @Bind(R.id.tv_str1_screening_list)
    TextView mTvStr2;
    @Bind(R.id.tv_noData_screening_list_activity)
    TextView mTvNull;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;
    @Bind(R.id.list_screening_list_activity)
    ListView mLv;

    private String mStoreName, mStoreId, mStartTime, mEndTime;
    private int mPage;
    private boolean mIsShowLoading = true;
    private boolean mIsRefresh = true;
    private int mLength;
    private CommonAdapter1<QueryTransBean.DataBean> mAdapter;
    private String mMerId,mLoginType;

    // 将筛选的门店id 和 起始、结束时间传过来
    // 如果是老板，可以不传门店 id ，默认查全部的。
    public static void start(Context from, String storeName, String storeId, String startTime, String endTime) {
        Intent intent = new Intent(from, HuiChuZhiScreeningListActivity.class);
        intent.putExtra("storeName", storeName);
        intent.putExtra("storeId", storeId);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        from.startActivity(intent);
    }

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
        mMerId = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.MERCHANT_ID);
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        mTvStr1.setText("充值金额（元）");
        mTvStr2.setText("消费金额（元）");
        Intent intent = getIntent();
        mStoreName = intent.getStringExtra("storeName");
        mStoreId = TextUtils.isEmpty(intent.getStringExtra("storeId")) ? "" : intent.getStringExtra("storeId");
        mStartTime = intent.getStringExtra("startTime");
        mEndTime = intent.getStringExtra("endTime");
        if(!TextUtils.isEmpty(mStoreName))
              mTitleBar.setTv_titlebar_back_titleText(mStoreName);

        mTvTime.setText(mStartTime + " 至 " + mEndTime);
        handleList();
        mLv.setAdapter(mAdapter = new CommonAdapter1<QueryTransBean.DataBean>(this, mDataList, R.layout.value_card_billing_da_child_item1) {
            @Override
            public void convert(ViewHolder holder, QueryTransBean.DataBean item, int position) {
                ImageView img = holder.findView(R.id.img_czk_type);
                TextView tvStatus = holder.findView(R.id.tv_czk_type);
                TextView tvOrderNum = holder.findView(R.id.tv_order_no);
                TextView tvTime = holder.findView(R.id.tv_czk_data);
                TextView tvGone = holder.findView(R.id.tv_youhui);
                TextView tv_czk_money = holder.findView(R.id.tv_czk_money);

                if ("3".equals(item.orderType)) {   // 3--消费  1--购卡   2--充值
                    img.setImageResource(R.drawable.img_value_card);
                    tvStatus.setText("支付成功");
                } else if ("1".equals(item.orderType)){
                    tvStatus.setText("购卡成功");
                    img.setImageResource(R.drawable.img_wechat);
                }else if ("2".equals(item.orderType)){
                    tvStatus.setText("充值成功");
                    img.setImageResource(R.drawable.img_wechat);
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
                String orderNo = "";
                if ("3".equals(mDataList.get(position).orderType)){    // 消费的订单号可以直接用
                    orderNo = mDataList.get(position).orderNo;
                }else{                                                 //  购卡和充值 需要手动将订单号最前面的 SVC 截掉
                    String[] cs = mDataList.get(position).orderNo.split("C");
                    if (cs!=null && cs.length>1){
                        orderNo = cs[1];
                    }
                }
                HuiChuZhiDetailActivity.start(HuiChuZhiScreeningListActivity.this, orderNo);

            }
        });

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

    @Override
    protected void load() {
        super.load();
       getHuiChuZhiList(mPage, mStoreId, mStartTime, mEndTime, mMerId, mLoginType);
    }

    /**
     * 惠储值 列表
     */
    public void getHuiChuZhiList(int page, String storeId, String startDate, String endDate, String merId, String loginType) {
        String url = NetUrl.HUICHUZHI_LIST;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId", storeId);
            jsonObject.put("startDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("page", page);
            jsonObject.put("pageSize", "20");
            jsonObject.put("merId",merId);
            jsonObject.put("loginType",loginType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(HuiChuZhiScreeningListActivity.this, url, jsonObject, new MyOkCallback<QueryTransBean>() {
            @Override
            public void onStart() {
                if (mIsShowLoading)
                    showLoading();
            }

            @Override
            public void onSuccess(QueryTransBean bean) {

                if (bean == null) {
                    ToastUtil.show("网络连接不佳");
                    if (YrmUtils.isEmptyList(mDataList))
                        mTvNull.setVisibility(View.VISIBLE);
                    return;
                }

                if (!TextUtils.isEmpty(bean.totalRechargeAmount))
                    mTvChargeAmt.setText(YrmUtils.addSeparator(bean.totalRechargeAmount));
                if (!TextUtils.isEmpty(bean.totalRechargeCount))
                    mTvChargeCount.setText(bean.totalRechargeCount+" 笔");
                if (!TextUtils.isEmpty(bean.totalCostAmount))
                    mTvConsumeAmt.setText(YrmUtils.addSeparator(bean.totalCostAmount));
                if (!TextUtils.isEmpty(bean.totalCostCount))
                    mTvConsumeCount.setText(bean.totalCostCount+" 笔");

                List<QueryTransBean.DataBean> data = bean.getData();
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

            @Override
            public void onError(Exception e) {
                if (YrmUtils.isEmptyList(mDataList))
                    mTvNull.setVisibility(View.VISIBLE);

                ToastUtil.show("网络连接不佳");
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

    private List<QueryTransBean.DataBean> mDataList = new ArrayList<>();




}
