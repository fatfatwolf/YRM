package com.hybunion.yirongma.payment.activity;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.RewardBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.DataPopupWindow;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * 奖励详情列表
 */

public class RewardListActivity extends BasicActivity {
    @Bind(R.id.titleBar_reward_list_activity)
    TitleBar mTitleBar;
    @Bind(R.id.tv_null_reward_list_activity)
    TextView mTvNull;
    @Bind(R.id.listView_reward_list_activity)
    ListView mLv;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefresh_layout;

    private SimpleDateFormat mSdf;
    private DataPopupWindow mDataPopupWindow;
    private boolean mIsRefresh;
    private String mStartDate,mEndDate;
    private int mPage;  // 这个是从 0 开始的
    private CommonAdapter1 mAdapter;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.layout_reward_list_activity;
    }

    @Override
    public void initView() {
        super.initView();
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_data_activity, null);
        mDataPopupWindow = new DataPopupWindow(this, view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDataPopupWindow.isPickTime(false);
        mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateToday = new Date();
        mStartDate = mSdf.format(dateToday);
        mEndDate = mSdf.format(dateToday);
        mTitleBar.setRightTextClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void rightClick() {
                showPopWindow();
            }
        });

        mLv.setAdapter(mAdapter = new CommonAdapter1<RewardBean.DataBean>(this,mDataList,R.layout.item_reward_list) {
            @Override
            public void convert(ViewHolder holder, RewardBean.DataBean item, int position) {
                TextView tvAmt = holder.findView(R.id.tv_rewardAmt_item_reward_list);
                TextView tvCreateDate = holder.findView(R.id.tv_create_date_item_reward_list);
                TextView tvRemark = holder.findView(R.id.tv_remark_item_reward_list);
                tvAmt.setText("+"+mDataList.get(position).rewardAmt);
                tvCreateDate.setText(mDataList.get(position).createDate);
                tvRemark.setText(mDataList.get(position).remark);
            }
        });

        smartRefresh_layout.setOnRefreshListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mHasNextPage){
                    mIsRefresh = false;
                    mPage++;
                    getRewardList(getDate(mStartDate),getDate(mEndDate),mPage+"",true);
                }else{
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mIsRefresh = true;
                mPage = 0;
                smartRefresh_layout.setEnableLoadMore(true);
                getRewardList(getDate(mStartDate),getDate(mEndDate),"0",false);
            }
        });

    }

    @Override
    protected void load() {
        super.load();
        getRewardList(getDate(mStartDate),getDate(mEndDate),"0",true);

    }

    public void getRewardList(String startDate, String endDate, String page, final boolean needLoading) {
        String url = NetUrl.REWARD_LIST;

        String mid = SharedPreferencesUtil.getInstance(RewardListActivity.this).getKey("mid");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", mid);
            jsonObject.put("startDate", startDate);
            jsonObject.put("endDate", endDate);
            jsonObject.put("page", page);
            jsonObject.put("rowsPerPage", "15");

            OkUtils.getInstance().post(RewardListActivity.this, url, jsonObject, new MyOkCallback<RewardBean>() {
                @Override
                public void onStart() {
                    smartRefresh_layout.finishLoadMore();
                    smartRefresh_layout.finishRefresh();
                    if (needLoading)
                        showLoading();
                }

                @Override
                public void onSuccess(RewardBean dataBean) {
                    String state = dataBean.getStatus();
                    String msg = dataBean.getMessage();
                    if ("0".equals(state)){
                        mHasNextPage = dataBean.isHasNextPage();
                        List<RewardBean.DataBean> dataList = dataBean.getData();
                        if (mIsRefresh)
                            mDataList.clear();
                        mDataList.addAll(dataList);
                        if (mAdapter!=null)
                            mAdapter.updateList(mDataList);

                    }else{
                        ToastUtil.show(msg);
                        mHasNextPage = false;
                    }
                    if (mDataList.size() == 0){
                        mTvNull.setVisibility(View.VISIBLE);
                    }else{
                        mTvNull.setVisibility(View.GONE);
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
                    return RewardBean.class;
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    private String getDate(String dateAndTime){
        String[] dateAndTimes = dateAndTime.split(" ");
        if (dateAndTimes.length==2)
            return dateAndTimes[0];
        return dateAndTime;

    }

    // 筛选 Popupwindow ，默认选择今天
    private void showPopWindow() {
        if (mDataPopupWindow == null) return;
        mDataPopupWindow.showThisPopWindow(mTitleBar, mStartDate, mEndDate, new DataPopupWindow.OnDataPopWindowListener() {
            @Override
            public void onPickFinish(String startPickedTime, String endPickedTime, String nameStr) {
//                Log.e("123123","当前选择的时间是："+startPickedTime+"   "+endPickedTime);
                mIsRefresh = true;
                mPage = 0;
                mStartDate = startPickedTime;
                mEndDate = endPickedTime;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startDate = sdf.parse(mStartDate);
                    Date endDate = sdf.parse(mEndDate);
                    long startMillis = startDate.getTime();
                    long endMillis = endDate.getTime();
                    // endMillis 应该 >= startMillis
                    if (endMillis<startMillis){
                        ToastUtil.show("开始时间不能晚于结束时间");
                        return;
                    }
                    getRewardList(mStartDate,mEndDate,mPage+"",true);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mDataPopupWindow.dismiss();
            }
            @Override
            public void onPickReset() {
            }
        });

    }

    private boolean mHasNextPage;
    private List<RewardBean.DataBean> mDataList = new ArrayList<>();

}
