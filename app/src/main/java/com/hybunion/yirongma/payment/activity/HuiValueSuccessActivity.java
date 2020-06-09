package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.CardOrderDataBean;
import com.hybunion.yirongma.payment.bean.HuiValueSuccessBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.DataPopupWindow1;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

public class HuiValueSuccessActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.tv_change_ruler)
    TextView tv_change_ruler;
    @Bind(R.id.tv_rechange)
    TextView tv_rechange;
    @Bind(R.id.tv_consume)
    TextView tv_consume;
    @Bind(R.id.view_rechange)
    View view_rechange;
    @Bind(R.id.view_consume)
    View view_consume;
    @Bind(R.id.ll_rechange)
    LinearLayout ll_rechange;
    @Bind(R.id.ll_consume)
    LinearLayout ll_consume;
    @Bind(R.id.ll_receive_money)
    LinearLayout ll_receive_money;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.ll_rechange_money)
    LinearLayout ll_rechange_money;
    @Bind(R.id.view2)
    View view2;
    @Bind(R.id.ll_rechange_count)
    LinearLayout ll_rechange_count;
    @Bind(R.id.tv_tv_rechange_money_name)
    TextView tv_tv_rechange_money_name;
    @Bind(R.id.tv_receive_money_name)
    TextView tv_receive_money_name;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.iv_down)
    ImageView iv_down;
    @Bind(R.id.tv_otherMoney)
    TextView tv_otherMoney;
    @Bind(R.id.tv_storedPeople)
    TextView tv_storedPeople;
    @Bind(R.id.tv_receive_money)
    TextView tv_receive_money;
    @Bind(R.id.tv_rechange_money)
    TextView tv_rechange_money;
    @Bind(R.id.tv_rechange_count)
    TextView tv_rechange_count;
    @Bind(R.id.tv_hui_detail)
    TextView tv_hui_detail;
    @Bind(R.id.iv_hui_detail)
    ImageView iv_hui_detail;
    @Bind(R.id.tv_history_data)
    TextView tv_history_data;
    @Bind(R.id.ll_boss_history)
    LinearLayout ll_boss_history;

    private DataPopupWindow1 mDataPopupWindow;
    private String mStartTime, mEndTime; // 筛选用   时间默认是今日的
    private String loginType;
    private String merId;
    private String type;
    public List<CardOrderDataBean.DataBean> orderData;
    private String storeId,storeName;
    boolean isConsume = true;//判断当前是在消费页还是充值页，true充值页，false消费页

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_hui_value_success;
    }

    @Override
    public void initView() {
        super.initView();
        tp1 = tv_rechange .getPaint();
        tp1.setFakeBoldText(true);
        tv_change_ruler.setOnClickListener(this);
        ll_rechange.setOnClickListener(this);
        ll_consume.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        iv_down.setOnClickListener(this);
        tv_hui_detail.setOnClickListener(this);
        iv_hui_detail.setOnClickListener(this);
        merId = SharedPreferencesUtil.getInstance(HuiValueSuccessActivity.this).getKey(SharedPConstant.MERCHANT_ID);
        loginType = SharedPreferencesUtil.getInstance(HuiValueSuccessActivity.this).getKey("loginType");
        storeId = SharedPreferencesUtil.getInstance(HuiValueSuccessActivity.this).getKey("storeId");
        storeName = SharedPreferencesUtil.getInstance(HuiValueSuccessActivity.this).getKey("storeName");
        if("0".equals(loginType)){
            type = "1";
            ll_boss_history.setVisibility(View.VISIBLE);
            tv_storedPeople.setVisibility(View.VISIBLE);
        }else {
            type = "2";
            ll_boss_history.setVisibility(View.GONE);
            tv_storedPeople.setVisibility(View.GONE);
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_popupwindow_data_activity, null);
        mDataPopupWindow = new DataPopupWindow1(this, view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        getTodayDateAndTime();

    }


    @Override
    protected void load() {
        super.load();
        if("0".equals(loginType)){
            getCardHistoryData(type);
        }else {
            getCardOrderData(storeId,this.type,mStartTime+" 00:00:00",mEndTime+" 23:59:59");
        }

    }
    public void getCardHistoryData(String type2){
        String url = NetUrl.GET_CARD_HISTORY_DATA;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merId);
            jsonObject.put("type", type2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(HuiValueSuccessActivity.this, url, jsonObject, new MyOkCallback<HuiValueSuccessBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(HuiValueSuccessBean huiValueSuccessBean) {
                HuiValueSuccessBean.DataBean dataBean = huiValueSuccessBean.historyData;
                if(null!=dataBean){
                    if(!TextUtils.isEmpty(dataBean.consumeMoney)){
                        tv_history_data.setText(dataBean.consumeMoney);
                    }

                    if(!TextUtils.isEmpty(dataBean.otherMoney)){
                        tv_otherMoney.setText(dataBean.otherMoney);
                    }

                    if(!TextUtils.isEmpty(dataBean.storedPeople)){
                        tv_storedPeople.setText("总充值人数 "+dataBean.storedPeople+" 人");
                    }
                }
                getCardOrderData(merId,type,mStartTime+" 00:00:00",mEndTime+" 23:59:59");
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
                return HuiValueSuccessBean.class;
            }
        });
    }


    public void getCardOrderData(String merId,String type,String startDate,String endDate){
        String url = NetUrl.GET_CARD_ORDER_DATA;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merId);
            jsonObject.put("type", type);
            jsonObject.put("startDate",startDate);
            jsonObject.put("endDate",endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(HuiValueSuccessActivity.this, url, jsonObject, new MyOkCallback<CardOrderDataBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(CardOrderDataBean cardOrderDataBean) {
                orderData = cardOrderDataBean.orderData;
                if(isConsume){//充值页
                    if(null!=orderData && orderData.size()>0){
                        ll_rechange_count.setVisibility(View.VISIBLE);
                        tv_receive_money.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).acturalMoney));
                        tv_rechange_money.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).storedMoney));
                        tv_rechange_count.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).storedNum));
                    }else {
                        ll_rechange_count.setVisibility(View.VISIBLE);
                        tv_receive_money.setText(YrmUtils.judgeMsgIsNull("0"));
                        tv_rechange_money.setText(YrmUtils.judgeMsgIsNull("0"));
                        tv_rechange_count.setText(YrmUtils.judgeMsgIsNull("0"));
                    }
                }else {
                    if(null!=orderData && orderData.size()>0){
                        ll_rechange_count.setVisibility(View.INVISIBLE);
                        tv_rechange_money.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).consumeNum));
                        tv_receive_money.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).consumeMoney));
                    }else {
                        ll_rechange_count.setVisibility(View.INVISIBLE);
                        tv_rechange_money.setText(YrmUtils.judgeMsgIsNull("0"));
                        tv_receive_money.setText(YrmUtils.judgeMsgIsNull("0"));
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
                return CardOrderDataBean.class;
            }
        });

    }



    TextPaint tp;
    TextPaint tp1;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_change_ruler:
                if("0".equals(loginType)){
                    Intent intent = new Intent(HuiValueSuccessActivity.this,ModifyRulersBossActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(HuiValueSuccessActivity.this,ModifyRulersShopActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_rechange:
                isConsume = true;
                tv_rechange.setTextColor(getResources().getColor(R.color.red_F84B33));
                tv_consume.setTextColor(getResources().getColor(R.color.text_313131));
                tv_consume.setTextSize(14);
                tp = tv_consume .getPaint();
                tp.setFakeBoldText(false);
                tp1 = tv_rechange .getPaint();
                tp1.setFakeBoldText(true);
                tv_rechange.setTextSize(16);
                view_rechange.setVisibility(View.VISIBLE);
                view_consume.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                ll_rechange_count.setVisibility(View.VISIBLE);
                tv_tv_rechange_money_name.setText("充卡金额（元）");
                tv_receive_money_name.setText("实收金额（元）");
                if(null!=orderData && orderData.size()>0){
                    tv_receive_money.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).acturalMoney));
                    tv_rechange_money.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).storedMoney));
                    tv_rechange_count.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).storedNum));
                }else {
                    tv_receive_money.setText(YrmUtils.judgeMsgIsNull("0"));
                    tv_rechange_money.setText(YrmUtils.judgeMsgIsNull("0"));
                    tv_rechange_count.setText(YrmUtils.judgeMsgIsNull("0"));
                }
                break;
            case R.id.ll_consume:
                isConsume = false;
                tp = tv_consume .getPaint();
                tp.setFakeBoldText(true);
                tp1 = tv_rechange .getPaint();
                tp1.setFakeBoldText(false);
                tv_rechange.setTextColor(getResources().getColor(R.color.text_313131));
                tv_consume.setTextColor(getResources().getColor(R.color.red_F84B33));
                tv_consume.setTextSize(16);
                tv_rechange.setTextSize(14);

                view_rechange.setVisibility(View.GONE);
                view_consume.setVisibility(View.VISIBLE);
                view2.setVisibility(View.INVISIBLE);
                ll_rechange_count.setVisibility(View.INVISIBLE);
                tv_receive_money_name.setText("消费金额（元）");
                tv_tv_rechange_money_name.setText("消费笔数（笔）");
                if(null!=orderData && orderData.size()>0){
                    tv_rechange_money.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).consumeNum));
                    tv_receive_money.setText(YrmUtils.judgeMsgIsNull(orderData.get(0).consumeMoney));
                }else {
                    tv_rechange_money.setText(YrmUtils.judgeMsgIsNull("0"));
                    tv_receive_money.setText(YrmUtils.judgeMsgIsNull("0"));
                }
                break;
            case R.id.tv_time:
            case R.id.iv_down:
                selectedClick();
                break;
            case R.id.tv_hui_detail:
            case R.id.iv_hui_detail:
                if("0".equals(loginType)){
                    HuiChuZhiScreeningListActivity.start(HuiValueSuccessActivity.this,storeName,storeId,mStartTime+" 00:00:00",mEndTime+" 23:59:59");
                }else if("1".equals(loginType)){
                    HuiChuZhiScreeningListActivity.start(HuiValueSuccessActivity.this,storeName,storeId,mStartTime+" 00:00:00",mEndTime+" 23:59:59");
                }

                break;

        }
    }



    public void selectedClick() {
        if (mDataPopupWindow != null) {
            mDataPopupWindow.showThisPopWindow(tv_time, mStartTime+" 00:00:00", mEndTime+" 23:59:59", new DataPopupWindow1.OnDataPopWindowListener() {
                @Override
                public void onPickFinish(String startPickedTime, String endPickedTime, String nameStr) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date startDate = sdf.parse(startPickedTime);
                        Date endDate = sdf.parse(endPickedTime);
                        long startMillis = startDate.getTime();
                        long endMillis = endDate.getTime();
                        mStartTime = sdf.format(startDate);
                        mEndTime = sdf.format(endDate);
                        // endMillis 应该 >= startMillis
                        if (endMillis < startMillis) {
                            ToastUtil.show("开始时间不能晚于结束时间");
                            return;
                        }
                        long day31 = 31L * 24 * 60 * 60 * 1000;
                        if ((endMillis - startMillis) > day31) {
                            ToastUtil.show("查询区间不能大于31天");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

//                    mNameTime = mStartTime + " 至 " + mEndTime;
                    tv_time.setText(mStartTime + " 至 " + mEndTime);
                    if("0".equals(loginType)){
                        getCardOrderData(merId,type,mStartTime+" 00:00:00",mEndTime+" 23:59:59");
                    }else {
                        getCardOrderData(storeId,type,mStartTime+" 00:00:00",mEndTime+" 23:59:59");
                    }

                    mDataPopupWindow.dismiss();
                }

                @Override
                public void onPickReset() {
//                    mDataPopupWindow.dismiss();
//                    getTodayDateAndTime();

                }
            });
        }
    }

    // 筛选时间默认是今日
    private void getTodayDateAndTime() {
        SimpleDateFormat formatStart = new SimpleDateFormat("yyyy-MM-dd");  // 开始时间时分秒都是 0
        mStartTime = formatStart.format(new Date());
        mEndTime = formatStart.format(new Date());
        tv_time.setText(mStartTime + " 至 " + mEndTime);
    }
}
