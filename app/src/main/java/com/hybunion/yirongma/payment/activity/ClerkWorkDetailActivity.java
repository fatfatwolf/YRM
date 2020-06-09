package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.ClerkWorkDetailBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.ClerkWorkDetailAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.DataPopupWindow;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class ClerkWorkDetailActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.lv_clerk_end)
    ListView lv_clerk_end;
    @Bind(R.id.ll_titlebar_back)
    LinearLayout ll_titlebar_back;
    @Bind(R.id.imgSelectedArrow_boss_bill_layout)
    ImageView mImgSelectedArrow;
    @Bind(R.id.tv_select_time)
    TextView tv_select_time;
    @Bind(R.id.tv_clerk_record)
    TextView tv_clerk_record;
    @Bind(R.id.tv_total_amount)
    TextView tv_total_amount;
    @Bind(R.id.tv_total_count)
    TextView tv_total_count;
    @Bind(R.id.tv_view_detail)
    TextView tv_view_detail;

    @Bind(R.id.ll_select_time)
    LinearLayout ll_select_time;
    private DataPopupWindow mDataPopupWindow;
    private String mStartTime, mEndTime; // 筛选用   时间默认是今日的
    private String mNameTime = "今日";
    ClerkWorkDetailAdapter adapter;
    List<ClerkWorkDetailBean.DataBean> list = new ArrayList<>();


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_clerk_work_detail;
    }

    @Override
    public void initView() {
        super.initView();
        ll_titlebar_back.setOnClickListener(this);
        ll_select_time.setOnClickListener(this);
        tv_clerk_record.setOnClickListener(this);
        tv_view_detail.setOnClickListener(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_popupwindow_data_activity, null);
        mDataPopupWindow = new DataPopupWindow(this, view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDataPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mImgSelectedArrow.setImageResource(R.drawable.xpay_account_book_arrow);
            }
        });

        getTodayDateAndTime();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_titlebar_back:
                finish();
                break;
            case R.id.ll_select_time:
                selectedClick();
                break;
            case R.id.tv_view_detail:
                Intent intent = new Intent(ClerkWorkDetailActivity.this,BillingListActivityOld.class);
                intent.putExtra("type","2");
                startActivity(intent);
                break;
            case R.id.tv_clerk_record:
                Intent intent1 = new Intent(ClerkWorkDetailActivity.this, ClerkRecordActivity.class);
                startActivity(intent1);
                break;
        }
    }

    View view;
    TextView tv_refund_count;
    TextView tv_refund_sum;

    public void selectedClick() {
        mImgSelectedArrow.setImageResource(R.drawable.xpay_account_book_arrowup);
        if (mDataPopupWindow != null) {
            mDataPopupWindow.showThisPopWindow(ll_select_time, mStartTime, mEndTime, new DataPopupWindow.OnDataPopWindowListener() {
                @Override
                public void onPickFinish(String startPickedTime, String endPickedTime, String nameStr) {
                    mStartTime = startPickedTime;
                    mEndTime = endPickedTime;
                    mNameTime = mStartTime + " 至 " + mEndTime;
                    tv_select_time.setText(mNameTime);
//                    presenter.getAllBillList(startPickedTime, endPickedTime, merId);
                    mDataPopupWindow.dismiss();
                }

                @Override
                public void onPickReset() {
                    mDataPopupWindow.dismiss();
                    getTodayDateAndTime();
//                    presenter.getAllBillList(mStartTime, mEndTime, merId);

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       querySetUpSum();
    }

    public void querySetUpSum(){
        String url = NetUrl.QUERY_SET_UP_SUM;
        JSONObject object = new JSONObject();
        try {
            object.put("storeId", SharedPreferencesUtil.getInstance(ClerkWorkDetailActivity.this).getKey("storeId"));
            object.put("endDate",mEndTime);
            object.put("startDate",mStartTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ClerkWorkDetailActivity.this, url, object, new MyOkCallback<ClerkWorkDetailBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ClerkWorkDetailBean clerkWorkDetailBean) {
                if(clerkWorkDetailBean!=null){
                    tv_total_amount.setText(clerkWorkDetailBean.totalAmt);
                    tv_total_count.setText(clerkWorkDetailBean.totalCount);
                    list = (List<ClerkWorkDetailBean.DataBean>) clerkWorkDetailBean.getData();
                    if(view == null){
                        view = LayoutInflater.from(ClerkWorkDetailActivity.this).inflate(R.layout.item_foot_view, null);
                        tv_refund_count = (TextView) view.findViewById(R.id.tv_refund_count);
                        tv_refund_sum = (TextView) view.findViewById(R.id.tv_refund_sum);
                        lv_clerk_end.addFooterView(view);
                    }
                    tv_refund_count.setText(clerkWorkDetailBean.reTotalCount);
                    tv_refund_sum.setText(clerkWorkDetailBean.reTotalAmt);

                    if(list!=null){
                        adapter = new ClerkWorkDetailAdapter(ClerkWorkDetailActivity.this,list);
                        lv_clerk_end.setAdapter(adapter);
                    }
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
                return ClerkWorkDetailBean.class;
            }
        });
    }

    // 筛选时间默认是今日
    private void getTodayDateAndTime() {
        SimpleDateFormat formatStart = new SimpleDateFormat("yyyy-MM-dd");  // 开始时间时分秒都是 0
        SimpleDateFormat formatEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mStartTime = formatStart.format(new Date());
        mStartTime += " " + "00:00:00";
        mEndTime = formatEnd.format(new Date());
        tv_select_time.setText(mStartTime + " 至 " + mEndTime);
    }




}
