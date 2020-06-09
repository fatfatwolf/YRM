package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.TiXianNewRecordBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.view.DataPopupWindow;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * t0钱包提现记录 列表
 */

public class TiXianNewRecordActivity extends BasicActivity {
    @Bind(R.id.tvNull_tixian_record_activity)
    TextView mTvNull;
    @Bind(R.id.listView_tixian_record_activity)
    ListView mLv;
    @Bind(R.id.smartRefresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @Bind(R.id.titleBar_tixian_new_record_activity)
    TitleBar mTitleBar;

    private CommonAdapter1 mAdapter;
    private boolean mIsRefresh = true;
    private int mPage = 1;
    private DataPopupWindow mDataPopupWindow;
    private String mMid;
    private String mStartDate = "", mEndDate = "";
    private Date startOfThisWeekDate, nowDate;
    private int mRows = 20;

    public static void start(Context from) {
        Intent intent = new Intent(from, TiXianNewRecordActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tixian_new_record_layout;
    }

    @Override
    public void initView() {
        super.initView();

        mLv.setAdapter(mAdapter = new CommonAdapter1<TiXianNewRecordBean.DataBean>(this, mDataList, R.layout.item_tixian_record) {
            @Override
            public void convert(ViewHolder holder, TiXianNewRecordBean.DataBean item, int position) {
                TextView date = holder.findView(R.id.date_item_tixian_record);
                TextView amt = holder.findView(R.id.amt_item_tixian_record);
                TextView state = holder.findView(R.id.state_item_tixian_record);
                if (!TextUtils.isEmpty(item.CASHDATE)) {
                    String cashDateNew = item.CASHDATE.replace(" ", "\n");
                    date.setText(cashDateNew);
                }
                amt.setText(item.CASHAMT+"");
                switch (item.CASHSTATUS){
                    case "1":
                        state.setText("审核中");
                        break;
                    case "2":
                    case "4":
                        state.setText("提现成功");
                        break;
                    default:
                        state.setText("处理中");

                }
            }
        });

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double amt = mDataList.get(position).CASHAMT;
                DecimalFormat df = new DecimalFormat("0.00");
                TiXianRecordDetailsActivity.start(TiXianNewRecordActivity.this,mDataList.get(position).CASHSTATUS,
                        df.format(amt),mDataList.get(position).CASHFEE,mDataList.get(position).CASHDATE);
            }
        });

        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mSize == mRows){
                    mIsRefresh = false;
                    mPage++;
                    getTiXianRecord(true);
                }else{
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.setEnableLoadMore(false);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mIsRefresh = true;
                mPage = 1;
                getTiXianRecord(false);
            }
        });

        mTitleBar.setRightTextClickListener(new TitleBar.OnRightClickListener() {
            @Override
            public void rightClick() {
                showPopWindow();
            }
        });


        nowDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nowDate);
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {  // 从 Sunday 开始，Sunday = 1
            dayofweek += 7;
        }
        calendar.add(Calendar.DATE, 2 - dayofweek);
        long startOfThisWeekMillis = calendar.getTimeInMillis();
        startOfThisWeekDate = new Date(startOfThisWeekMillis);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mNowDate = format.format(nowDate);
        mStartOfThisWeek = format.format(startOfThisWeekDate);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_data_activity, null);
        mDataPopupWindow = new DataPopupWindow(this, view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDataPopupWindow.isPickTime(false);

    }

    private String mNowDate, mStartOfThisWeek;

    @Override
    protected void load() {
        super.load();
        mStartDate = mStartOfThisWeek;
        mEndDate = mNowDate;
        mMid = SharedPreferencesUtil.getInstance(this).getKey(Constants.MID);
        getTiXianRecord(true);
    }

    // 筛选 Popupwindow ，默认选择本周
    private void showPopWindow() {
        if (mDataPopupWindow == null) return;
        mDataPopupWindow.showThisPopWindow(mTitleBar, mStartDate, mEndDate, new DataPopupWindow.OnDataPopWindowListener() {
            @Override
            public void onPickFinish(String startPickedTime, String endPickedTime, String nameStr) {
                mIsRefresh = true;
                mPage = 1;
                mStartDate = startPickedTime;
                mEndDate = endPickedTime;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date startDate = sdf.parse(mStartDate);
                    Date endDate = sdf.parse(mEndDate);
                    long startMillis = startDate.getTime();
                    long endMillis = endDate.getTime();
                    if (endMillis<startMillis){
                        ToastUtil.showShortToast("开始时间不能晚于结束时间");
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getTiXianRecord(true);
                mDataPopupWindow.dismiss();
            }

            @Override
            public void onPickReset() {

            }
        });


    }

    private void getTiXianRecord(final boolean isShowLoad){
        JSONObject jb = new JSONObject();
        try {
            jb.put("mid", mMid);
            jb.put("page", mPage);
            jb.put("rows", mRows+"");
            jb.put("startDate", mStartDate);
            jb.put("endDate", mEndDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(this, NetUrl.TIXIAN_NEW_RECORD, jb, new MyOkCallback<TiXianNewRecordBean>() {
            @Override
            public void onStart() {
                if (isShowLoad)
                    showLoading();
            }

            @Override
            public void onSuccess(TiXianNewRecordBean bean) {
                if (bean!=null){
                    String msg = bean.msg;
                    boolean success = bean.success;
                    if (success){
                        if (mIsRefresh)
                            mDataList.clear();
                        if (bean.obj!=null){
                            mSize = bean.obj.size();
                            mDataList.addAll(bean.obj);
                            if (mAdapter != null)
                                mAdapter.updateList(mDataList);
                        }else{
                            mSize = 0;
                        }
                        if (mDataList.size()==0)
                            mTvNull.setVisibility(View.VISIBLE);
                        else
                            mTvNull.setVisibility(View.GONE);
                    }else{
                        if (!TextUtils.isEmpty(msg))
                            ToastUtil.showShortToast(msg);
                        mTvNull.setVisibility(View.VISIBLE);
                    }
                }else{
                    mTvNull.setVisibility(View.VISIBLE);
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
                return TiXianNewRecordBean.class;
            }
        });


    }



    private List<TiXianNewRecordBean.DataBean> mDataList = new ArrayList<>();
    private int mSize;
}
