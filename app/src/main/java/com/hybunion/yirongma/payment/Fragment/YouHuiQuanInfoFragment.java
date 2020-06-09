package com.hybunion.yirongma.payment.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MakeNoteBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.bean.YouHuiQuanDetailsDataBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BasicFragment;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.activity.NoUseTimeActivity;
import com.hybunion.yirongma.payment.activity.YouHuiQuanDetailsActivity;
import com.hybunion.yirongma.payment.adapter.StoreListAdapter;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.MyBottomDialog;
import com.hybunion.yirongma.payment.view.MyBottonPopWindow;
import com.hybunion.yirongma.payment.view.MyDateTimePickDialog;
import com.hybunion.yirongma.payment.view.TitleBar;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 优惠券详情页 - 券信息
 */

public class YouHuiQuanInfoFragment extends BasicFragment implements View.OnClickListener{
    @Bind(R.id.tv_type_info_fragment)
    TextView mTvType; // 券类型
    @Bind(R.id.tv_name_info_fragment)
    EditText mTvName;  // 券名称
    @Bind(R.id.tv_num_info_fragment)
    EditText mTvNum;  // 券数量
    @Bind(R.id.tv_youhui_info_fragment)
    EditText mTvYouHui;  // 单券金额
    @Bind(R.id.et_use_condition)
    EditText mTvTiaoJian;  // 使用条件
    @Bind(R.id.tv_start_time_make_note_activity)
    TextView mTvStartTime;  // 开始时间
    @Bind(R.id.tv_end_time_make_note_activity)
    TextView mTvEndTime; // 截止时间
    @Bind(R.id.tv_take_effect)
    TextView mTvShengXiao;  // 优惠券生效

    @Bind(R.id.tv_state_info_fragment)
    TextView mTvState; // 券状态
    @Bind(R.id.iv_take_effect)
    ImageView iv_take_effect;
    @Bind(R.id.iv_time_effect)
    ImageView iv_time_effect;
    @Bind(R.id.iv_choose_store)
    ImageView iv_choose_store;
    @Bind(R.id.tv_time_effect)
    TextView tv_time_effect;
    @Bind(R.id.ll_validays_time)
    LinearLayout ll_validays_time;
    @Bind(R.id.ll_validays)
    LinearLayout ll_validays;
    @Bind(R.id.ll_no_use_time)
    LinearLayout ll_no_use_time;

    @Bind(R.id.rv_caldener_start)
    RelativeLayout rv_caldener_start;
    @Bind(R.id.rv_caldener_end)
    RelativeLayout rv_caldener_end;
    @Bind(R.id.tv_startUseTime)
    TextView tv_startUseTime;
    @Bind(R.id.tv_endUseTime)
    TextView tv_endUseTime;
    @Bind(R.id.switch_time_use)
    Switch switchButton;
    @Bind(R.id.tv_shop_name)
    TextView tv_shop_name;
    @Bind(R.id.tv_noUseTime1)
    TextView tv_noUseTime1;
    @Bind(R.id.tv_noUseTime2)
    TextView tv_noUseTime2;

    @Bind(R.id.tv_canUseDay)
    EditText tv_canUseDay;

    @Bind(R.id.bt_make_notes)
    Button bt_make_notes;
    @Bind(R.id.rv_take_effect)
    RelativeLayout rv_take_effect;
    @Bind(R.id.rv_time_effect)
    RelativeLayout rv_time_effect;
    @Bind(R.id.rv_refresh)
    RelativeLayout rv_refresh;
    @Bind(R.id.rv_use_shop)
    RelativeLayout rv_use_shop;

    private String mCouponId;
    private boolean mIsCanLoad, mIsLoaded;

    int effectType = 0;//0立即生效 1次日生效
    int validType = 2;//1绝对时效 2相对时效
    int isDisable = 1;//1关闭 2开启
    String loginType;//判断登录的类型
    String noUseTime1;
    String noUseTime2;
    StoreListAdapter storeListAdapter;
    String createUser;
    int storePosition = 0;
    private String mSelectedStoreId,mSelectStoreName;//选中的门店ID,门店名字
    private String mStoreId,mStoreName,disableTimeOne,disableTimeTwo,couponAdmin;
    String validDays;//生效后几天可以适
    public boolean isShowDialog = true;
    YouHuiQuanDetailsActivity activity;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_youhuiquan_info;
    }

    @Override
    protected void initView() {
        super.initView();
        activity = (YouHuiQuanDetailsActivity) getActivity();
        SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.SAVE_DAY_list2,"");
        SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.SAVE_TIME_LIST3,"");
        SharedPreferencesUtil.getInstance(getActivity()).putKey(SharedPConstant.SAVE_TIME_LIST4,"");
        Bundle bundle = getArguments();
        mCouponId = (String) bundle.get("couponId");
        loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
        createUser = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginName");
        couponAdmin = SharedPreferencesUtil.getInstance(getActivity()).getKey("couponAdmin");
        storeListAdapter = new StoreListAdapter(getActivity());
        mTvName.setFocusable(false);
        mTvNum.setFocusable(false);
        mTvYouHui.setFocusable(false);
        mTvTiaoJian.setFocusable(false);
        tv_canUseDay.setFocusable(false);
        switchButton.setClickable(false);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//开启
                    isDisable = 2;
                    isShowDialog = true;
                    if(TextUtils.isEmpty(noUseTime1) && TextUtils.isEmpty(noUseTime2)){//没有设置过
                        showMyDialog2(R.layout.dialog_no_use_effect);
                    }else {
                        ll_no_use_time.setVisibility(View.VISIBLE);
                    }
                } else {//关闭
                    isDisable = 1;
                    if(isShowDialog){
                        showNoteDialog();
                    }
                    ll_no_use_time.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void load() {
        super.load();
        mIsCanLoad = true;
    }

    private Dialog mMyDialog;



    private void showNoteDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_stuatus,null);
        final Dialog dialog = new Dialog(getActivity(), R.style.MyDialog);
        dialog.setContentView(view);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setText("关闭【不可用时间按钮】后，优惠券将全时段可用！");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchButton.setChecked(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private Dialog mMyTakeDialog;

    private void showMyTakeDialog(int dialogLayout) {
        mMyTakeDialog = new Dialog(getActivity());
        mMyTakeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyTakeDialog.setContentView(dialogLayout);
        TextView tv_content1 = mMyTakeDialog.findViewById(R.id.tv_content1);
        TextView tv_content2 = mMyTakeDialog.findViewById(R.id.tv_content2);
        tv_content1.setText(Html.fromHtml("<b>立即生效：</b>顾客领券以后即刻生效，可直接消费再次抵扣。"));
        tv_content2.setText(Html.fromHtml("<b>次日生效：</b>顾客领券后当日不可使用，需次日生效后方可使用。"));
        mMyTakeDialog.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyTakeDialog.dismiss();
            }
        });
        mMyTakeDialog.show();
    }


    private Dialog mMyTimeDialog;

    private void showMyTimeDialog(int dialogLayout) {
        mMyTimeDialog = new Dialog(getActivity());
        mMyTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyTimeDialog.setContentView(dialogLayout);
        TextView tv_content1 = mMyTimeDialog.findViewById(R.id.tv_content1);
        TextView tv_content2 = mMyTimeDialog.findViewById(R.id.tv_content2);
        TextView tv_content3 = mMyTimeDialog.findViewById(R.id.tv_content3);
        TextView tv_content4 = mMyTimeDialog.findViewById(R.id.tv_content4);
        tv_content1.setText(Html.fromHtml("<b>相对时效：</b>使用时间设置相对于生效时间，生效后几天内可用。"));
        tv_content2.setText(Html.fromHtml(" <b>例：</b>优惠券生效后 3 天内可用，6月24日领取，有效期至26日，27日过期不可用。"));
        tv_content3.setText(Html.fromHtml("<b>绝对时效：</b>一般为节日促销券，顾客领取后，设定具体可使用的时间段。"));
        tv_content4.setText(Html.fromHtml(" <b>例：</b>绝对时效时间为2019-06-15 至 2019-06-30,15~30号期间优惠券可用。"));
        mMyTimeDialog.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyTimeDialog.dismiss();
            }
        });
        mMyTimeDialog.show();
    }

    private void showMyDialog2(int dialogLayout) {
        mMyDialog = new Dialog(getActivity());
        mMyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyDialog.setContentView(dialogLayout);
        mMyDialog.setCanceledOnTouchOutside(false);
        TextView tv_content1 = mMyDialog.findViewById(R.id.tv_content1);
        TextView tv_content2 = mMyDialog.findViewById(R.id.tv_content2);
        tv_content1.setText(Html.fromHtml("<b>可用时间：</b>不可设置优惠券具体不可用时间，一般为商家高峰时间，可分散高峰期流量。"));
        tv_content2.setText(Html.fromHtml(" <b>例：</b>周一至周五，11:20~12:20 不可用"));
        Button button = mMyDialog.findViewById(R.id.btn_login);
        button.setText("去设置");
        mMyDialog.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NoUseTimeActivity.class);
                intent.putExtra("clickType","2");
                startActivityForResult(intent,0);
                mMyDialog.dismiss();
            }
        });
        mMyDialog.show();
    }


    private Dialog mMyUseDialog;

    private void showMyUseDialog(int dialogLayout) {
        mMyUseDialog = new Dialog(getActivity());
        mMyUseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyUseDialog.setContentView(dialogLayout);
        TextView tv_content1 = mMyUseDialog.findViewById(R.id.tv_content1);
        TextView tv_content2 = mMyUseDialog.findViewById(R.id.tv_content2);
        tv_content1.setText(Html.fromHtml("<b>可用时间：</b>不可设置优惠券具体不可用时间，一般为商家高峰时间，可分散高峰期流量。"));
        tv_content2.setText(Html.fromHtml(" <b>例：</b>周一至周五，11:20~12:20 不可用"));
        mMyUseDialog.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyUseDialog.dismiss();
            }
        });
        mMyUseDialog.show();
    }

    private Dialog mMyShopDialog;

    private void showShopDialog(int dialogLayout) {
        mMyShopDialog = new Dialog(getActivity());
        mMyShopDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyShopDialog.setContentView(dialogLayout);
        TextView tv_shop = mMyShopDialog.findViewById(R.id.tv_shop);
        tv_shop.setText(Html.fromHtml("<b>适用门店：</b>仅为商户下已加入商圈的门店，可不在同一商圈，每个优惠券只可适用一家门店。"));
        mMyShopDialog.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyShopDialog.dismiss();
            }
        });
        mMyShopDialog.show();
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mIsCanLoad && !mIsLoaded){
            showProgressDialog("");
            getInfoData(mCouponId);
            mIsLoaded = true;
        }

    }



    // 请求 券信息  将 优惠券id 传过来
    public void getInfoData(String couponId){
        String url = NetUrl.YOUHUIQUAN_DETAILS_INFO;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("couponId", couponId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<YouHuiQuanDetailsDataBean>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(YouHuiQuanDetailsDataBean dataBean) {
                    if (dataBean != null) {
                        mTotalData = dataBean.totalData;
                        if (mTotalData!=null){
                            setUI();   // 设置页面数据
                        }else{
                            ToastUtil.show("网络连接不佳");
                        }
                    }else{
                        ToastUtil.show("网络连接不佳");
                    }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return YouHuiQuanDetailsDataBean.class;
            }
        });


    }



    private YouHuiQuanDetailsDataBean.TotalDataBean mTotalData;
    @Override
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map, type);
        switch (type) {
            case YOUHUIQUAN_DETAILS_INFO:
                hideProgressDialog();
                if (map != null) {
                    YouHuiQuanDetailsDataBean dataBean = (YouHuiQuanDetailsDataBean) map.get("bean");
                    if (dataBean != null) {
                        mTotalData = dataBean.totalData;
                        if (mTotalData!=null){
                            setUI();   // 设置页面数据
                        }else{
                            ToastUtil.show("网络连接不佳");
                        }
                    }else{
                        ToastUtil.show("网络连接不佳");
                    }
                }else{
                    ToastUtil.show("网络连接不佳");
                }
                break;
            case MAKE_NOTES:
                hideProgressDialog();


                break;

        }
    }

    boolean isClickTitle = false;//刚进来没有点击
    // 设置页面数据
    private void setUI(){
        if("未上线".equals(mTotalData.couponStatus)){
            activity.titleBar.setRightTexViewVisible(true);
            activity.titleBar.setRightTextClickListener(new TitleBar.OnRightClickListener() {
                @Override
                public void rightClick() {
                    if(loginType.equals("0") || (loginType.equals("1") && "1".equals(couponAdmin))){
                    isClickTitle = !isClickTitle;
                    if(isClickTitle){//true可编辑
                        ToastUtil.show("界面可编辑");
                        activity.titleBar.setTv_rightText("取消");
                        mTvNum.setFocusable(true);
                        mTvNum.setFocusableInTouchMode(true);
                        mTvYouHui.setFocusable(true);
                        mTvName.setFocusable(true);
                        mTvName.setFocusableInTouchMode(true);
                        mTvYouHui.setFocusableInTouchMode(true);
                        mTvTiaoJian.setFocusable(true);
                        mTvTiaoJian.setFocusableInTouchMode(true);
                        tv_canUseDay.setFocusable(true);
                        tv_canUseDay.setFocusableInTouchMode(true);
                        switchButton.setClickable(true);
                        bt_make_notes.setVisibility(View.VISIBLE);
                        rv_caldener_start.setOnClickListener(YouHuiQuanInfoFragment.this);
                        rv_caldener_end.setOnClickListener(YouHuiQuanInfoFragment.this);
                        mTvShengXiao.setOnClickListener(YouHuiQuanInfoFragment.this);
                        tv_time_effect.setOnClickListener(YouHuiQuanInfoFragment.this);
                        tv_shop_name.setOnClickListener(YouHuiQuanInfoFragment.this);
                        tv_startUseTime.setOnClickListener(YouHuiQuanInfoFragment.this);
                        tv_endUseTime.setOnClickListener(YouHuiQuanInfoFragment.this);
                        bt_make_notes.setOnClickListener(YouHuiQuanInfoFragment.this);
                        ll_validays_time.setOnClickListener(YouHuiQuanInfoFragment.this);
                        ll_no_use_time.setOnClickListener(YouHuiQuanInfoFragment.this);
                        rv_take_effect.setOnClickListener(YouHuiQuanInfoFragment.this);
                        rv_time_effect.setOnClickListener(YouHuiQuanInfoFragment.this);
                        rv_use_shop.setOnClickListener(YouHuiQuanInfoFragment.this);
                        rv_refresh.setOnClickListener(YouHuiQuanInfoFragment.this);
                    }else {//取消 不可编辑
                        ToastUtil.show("界面不可编辑");
                        activity.titleBar.setTv_rightText("编辑");
                        mTvNum.setFocusable(false);
                        mTvYouHui.setFocusable(false);
                        mTvTiaoJian.setFocusable(false);
                        tv_canUseDay.setFocusable(false);
                        switchButton.setClickable(false);
                        bt_make_notes.setVisibility(View.GONE);
                        rv_caldener_start.setOnClickListener(null);
                        rv_caldener_end.setOnClickListener(null);
                        mTvShengXiao.setOnClickListener(null);
                        tv_time_effect.setOnClickListener(null);
                        tv_shop_name.setOnClickListener(null);
                        tv_startUseTime.setOnClickListener(null);
                        tv_endUseTime.setOnClickListener(null);
                        ll_validays_time.setOnClickListener(null);
                        ll_no_use_time.setOnClickListener(null);
                        rv_take_effect.setOnClickListener(null);
                        rv_time_effect.setOnClickListener(null);
                        rv_use_shop.setOnClickListener(null);
                        rv_refresh.setOnClickListener(null);
                    }
                }else {
                        ToastUtil.show("暂无权限");
                    }
                }
            });
            isShowDialog = false;
            iv_take_effect.setVisibility(View.VISIBLE);
            iv_time_effect.setVisibility(View.VISIBLE);
            iv_choose_store.setVisibility(View.VISIBLE);



        }else if("已上线".equals(mTotalData.couponStatus)|| "已下线".equals(mTotalData.couponStatus)){
            activity.titleBar.setRightTexViewVisible(false);
            iv_take_effect.setVisibility(View.GONE);
            iv_time_effect.setVisibility(View.GONE);
            iv_choose_store.setVisibility(View.GONE);
            bt_make_notes.setVisibility(View.GONE);
            mTvName.setFocusable(false);
            mTvNum.setFocusable(false);
            mTvYouHui.setFocusable(false);
            mTvTiaoJian.setFocusable(false);

            tv_canUseDay.setFocusable(false);
            switchButton.setClickable(false);

        }


        mTvType.setText(mTotalData.couponType);
        mTvName.setText(mTotalData.couponName);
        // 券 数量
        String couponNum = YrmUtils.getNumFromStr(mTotalData.couponNumber);
        if (!TextUtils.isEmpty(couponNum)){
                mTvNum.setText(couponNum);
        }
        // 单券金额
        String youhui = YrmUtils.getNumFromStr(String.valueOf(mTotalData.couponMoney));
        if (!TextUtils.isEmpty(youhui)){
                mTvYouHui.setText(youhui);
        }
        // 使用条件
        String tiaoJian = YrmUtils.getNumFromStr(mTotalData.conditions);
        mTvTiaoJian.setText(tiaoJian);
        mTvStartTime.setText(mTotalData.startDate);
        mTvEndTime.setText(mTotalData.endDate);
        mTvShengXiao.setText(mTotalData.effectRules);
        tv_shop_name.setText(mTotalData.storeName);
        mTvState.setText(mTotalData.couponStatus);
        mStoreId = mTotalData.storeId;
        if(mTotalData.validType==1){
            validType = 1;
            tv_time_effect.setText("绝对时效");
            ll_validays.setVisibility(View.GONE);
            ll_validays_time.setVisibility(View.VISIBLE);
            tv_startUseTime.setText(mTotalData.validStartTime);
            tv_endUseTime.setText(mTotalData.validEndTime);
        }else {
            validType = 2;
            tv_time_effect.setText("相对时效");
            ll_validays.setVisibility(View.VISIBLE);
            ll_validays_time.setVisibility(View.GONE);
            tv_canUseDay.setText(String.valueOf(mTotalData.validDays));//几天可用
        }
        if("1".equals(mTotalData.isDisabled)){//关闭
            ll_no_use_time.setVisibility(View.GONE);
            switchButton.setChecked(false);
        }else if("2".equals(mTotalData.isDisabled)){

            ll_no_use_time.setVisibility(View.VISIBLE);

            if(!TextUtils.isEmpty(mTotalData.disableTimeOne)){
                noUseTime1 = mTotalData.disableTimeOne;
                tv_noUseTime1.setText(mTotalData.disableTimeOne);
//                String strTime1[] = mTotalData.disableTimeOne.split(" ");
//                switch (str)
                disableTimeOne = YrmUtils.changeToDisable(mTotalData.disableTimeOne);
            }

            if(!TextUtils.isEmpty(mTotalData.disableTimeTwo)){
                noUseTime2 = mTotalData.disableTimeTwo;
                tv_noUseTime2.setText(mTotalData.disableTimeTwo);
                disableTimeTwo = YrmUtils.changeToDisable(mTotalData.disableTimeTwo);
            }
            switchButton.setChecked(true);
        }

    }
    private MyDateTimePickDialog mStartTimePicker, mEndTimePicker;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rv_caldener_start:
                if (mStartTimePicker == null)
                    mStartTimePicker = new MyDateTimePickDialog(getActivity());
                mStartTimePicker.setType(1);
                mStartTimePicker.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        mTvStartTime.setText(year + "-" + month + "-" + date);
                        mTvStartTime.setTextColor(Color.parseColor("#4E69E2"));
                        mStartTimePicker.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mStartTimePicker.dismiss();
                    }
                });
                break;

            case R.id.rv_caldener_end:
                if (mEndTimePicker == null)
                    mEndTimePicker = new MyDateTimePickDialog(getActivity());
                mEndTimePicker.setType(1);
                mEndTimePicker.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        mTvEndTime.setText(year + "-" + month + "-" + date);
                        mTvEndTime.setTextColor(Color.parseColor("#4E69E2"));
                        mEndTimePicker.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mEndTimePicker.dismiss();
                    }
                });

                break;
            case R.id.tv_take_effect:
                showMyBottomDialog();
                break;
            case R.id.tv_time_effect:
                showMyBottomDialog2();
                break;
            case R.id.rv_take_effect:
                showMyTakeDialog(R.layout.dialog_take_effect);
                break;
            case R.id.rv_time_effect:
                showMyTimeDialog(R.layout.dialog_time_effect);
                break;
            case R.id.rv_refresh:
                showMyUseDialog(R.layout.dialog_no_use_effect);
                break;
            case R.id.rv_use_shop:
                showShopDialog(R.layout.dialog_shop_use_effect);
                break;
            case R.id.tv_startUseTime:
                if (mStartTimePicker == null)
                    mStartTimePicker = new MyDateTimePickDialog(getActivity());
                mStartTimePicker.setType(1);
                mStartTimePicker.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        tv_startUseTime.setText(year + "-" + month + "-" + date);
                        tv_startUseTime.setTextColor(Color.parseColor("#4E69E2"));
                        mStartTimePicker.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mStartTimePicker.dismiss();
                    }
                });
                break;
            case R.id.tv_endUseTime:
                if (mStartTimePicker == null)
                    mStartTimePicker = new MyDateTimePickDialog(getActivity());
                mStartTimePicker.setType(1);
                mStartTimePicker.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        tv_endUseTime.setText(year + "-" + month + "-" + date);
                        tv_endUseTime.setTextColor(Color.parseColor("#4E69E2"));
                        mStartTimePicker.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mStartTimePicker.dismiss();
                    }
                });
                break;
            case R.id.tv_shop_name:
                if("0".equals(loginType)){//老板
                    getStoreList(true);
                }
                break;

            case R.id.ll_no_use_time:
                Intent intent = new Intent(getActivity(),NoUseTimeActivity.class);
                intent.putExtra("noUseTime1",noUseTime1);
                intent.putExtra("noUseTime2",noUseTime2);
                intent.putExtra("clickType","2");
                startActivityForResult(intent,1);
                break;
            case R.id.bt_make_notes:

                String couponName  = mTvName.getText().toString().trim();
                String takeAmount  = mTvNum.getText().toString().trim();
                String usedAmount  = mTvYouHui.getText().toString().trim();
                String withAmount  = mTvTiaoJian.getText().toString().trim();
                String startTime  = mTvStartTime.getText().toString().trim();
                String endTime = mTvEndTime.getText().toString().trim();
                mStoreName = tv_shop_name.getText().toString().trim();
                Long startDay = YrmUtils.DayChangeToLong(startTime);
                Long endDay = YrmUtils.DayChangeToLong(endTime);
                validDays = tv_canUseDay.getText().toString().trim();
                String noTime1 = tv_noUseTime1.getText().toString().trim();
                String validStartTime = tv_startUseTime.getText().toString().trim();
                String validEndTime = tv_endUseTime.getText().toString().trim();

                if(TextUtils.isEmpty(couponName)){
                    ToastUtil.show("请输入优惠券名称");
                    return;
                }
                if(Integer.parseInt(takeAmount)<50 || Integer.parseInt(takeAmount)>500){
                    ToastUtil.show("券数量为50到500之间");
                    return;
                }


                if(TextUtils.isEmpty(usedAmount)){
                    ToastUtil.show("请输入单券金额");
                    return;
                }

                if(Double.valueOf(usedAmount)>Double.valueOf("500") || Double.valueOf(usedAmount)<Double.valueOf("0.10")){
                    ToastUtil.show("单券金额为0.1元-500元之间");
                    return;
                }

                if(TextUtils.isEmpty(withAmount)){
                    ToastUtil.show("请输入使用条件");
                    return;
                }

                if(Double.valueOf(withAmount)<Double.valueOf("0.01")){
                    ToastUtil.show("输入金额不能小于0.01元");
                    return;
                }
                if(Double.valueOf(withAmount)<Double.valueOf(usedAmount)){
                    ToastUtil.show("使用条件金额必须大于单券金额");
                    return;
                }
                if(TextUtils.isEmpty(startTime)){
                    ToastUtil.show("请输入起始时间");
                    return;
                }

                if(TextUtils.isEmpty(endTime)){
                    ToastUtil.show("请输入截止时间");
                    return;
                }
                if(endDay<startDay){
                    ToastUtil.show("终止时间不可小于起始时间");
                    return;
                }

                if(validType == 1){//绝对时效
                    if(TextUtils.isEmpty(validStartTime)){
                        ToastUtil.show("生效后开始时间不能为空");
                        return;
                    }

                    if(TextUtils.isEmpty(validEndTime)){
                        ToastUtil.show("生效后截止时间不能为空");
                        return;
                    }
                }else {//相对时效
                    if(TextUtils.isEmpty(validDays)){
                        ToastUtil.show("可用时间不能为空");
                        return;
                    }

                    if(Integer.parseInt(validDays)<2){
                        ToastUtil.show("可用时间不可低于2天");
                        return;
                    }

                    if(Integer.parseInt(validDays)>30){
                        ToastUtil.show("可用时间不可大于30天");
                        return;
                    }
                }


                if(TextUtils.isEmpty(mStoreName) || TextUtils.isEmpty(mStoreId)){
                    ToastUtil.show("适用门店不能为空");
                    return;
                }
                if(isDisable == 2){
                    if(TextUtils.isEmpty(noTime1) || TextUtils.isEmpty(disableTimeOne)){
                        ToastUtil.show("请选择不可用时间段1");
                        return;
                    }
                }
                showProgressDialog("");
                if(1==validType){
                    makeNotes(mCouponId,couponName,takeAmount,usedAmount,withAmount,startTime,endTime
                            ,effectType,validType,isDisable,disableTimeOne,disableTimeTwo,mStoreId,createUser,
                            validStartTime,validEndTime);
                }else if(2==validType){
                    makeNotes2(mCouponId,couponName,takeAmount,usedAmount,withAmount,startTime,endTime
                            ,effectType,validType,Integer.parseInt(validDays),isDisable,disableTimeOne,disableTimeTwo,mStoreId,createUser
                            );
                }

                break;
        }

    }

    /**
     * 制券
     */
    public void makeNotes(String couponId,String couponName,String takeAmount,String usedAmount,String withAmount,String startTime,
                          String endTime,int effectType,int validType,int isDisable,String disableTimeOne,
                          String disableTimeTwo,String storeId,String createUser,String validStartTime,String validEndTime) {
        String url = NetUrl.MAKE_NOTES;

        JSONObject object = new JSONObject();
        try {
            String loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
            if(("0").equals(loginType)){
                object.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey(Constants.MERCHANTID));
            }else {
                object.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey("shopId"));
            }
            object.put("couponName", couponName);
            object.put("couponId", couponId);
            object.put("takeAmount",takeAmount);
            object.put("usedAmount",usedAmount);
            object.put("withAmount",withAmount);
            object.put("startTime",startTime);
            object.put("endTime",endTime);
            object.put("effectType",effectType);
            object.put("validType",validType);
            object.put("validDays","");
            object.put("isDisable",isDisable);
            object.put("createUser",createUser);
            object.put("validStartTime",validStartTime);
            object.put("validEndTime",validEndTime);
            if(isDisable == 2){
                object.put("disableTimeOne",disableTimeOne);
                if(!TextUtils.isEmpty(disableTimeTwo))
                    object.put("disableTimeTwo",disableTimeTwo);
            }

            object.put("storeId",storeId);
            object.put("protocolType","0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, object, new MyOkCallback<MakeNoteBean>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(MakeNoteBean result) {
                String status = result.getStatus();
                String message = result.getMessage();
                if (!TextUtils.isEmpty(status) && "0".equals(status)){

                    if(!TextUtils.isEmpty(message))
                        ToastUtil.show(message);

                    getActivity().setResult(0);
                    hideProgressDialog();
                    getActivity().finish();
                }else {
                    if(!TextUtils.isEmpty(message))
                        ToastUtil.show(message);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return MakeNoteBean.class;
            }
        });
    }



    public void makeNotes2(String couponId,String couponName,String takeAmount,String usedAmount,String withAmount,String startTime,
                           String endTime,int effectType,int validType,int validDays,int isDisable,String disableTimeOne,
                           String disableTimeTwo,String storeId,String createUser) {
        String url = NetUrl.MAKE_NOTES;
        JSONObject object = new JSONObject();
        try {
            String loginType = SharedPreferencesUtil.getInstance(getActivity()).getKey("loginType");
            if(("0").equals(loginType)){
                object.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey(Constants.MERCHANTID));
            }else {
                object.put("merId", SharedPreferencesUtil.getInstance(getActivity()).getKey("shopId"));
            }
            object.put("couponName", couponName);
            object.put("couponId", couponId);
            object.put("takeAmount",takeAmount);
            object.put("usedAmount",usedAmount);
            object.put("withAmount",withAmount);
            object.put("startTime",startTime);
            object.put("endTime",endTime);
            object.put("effectType",effectType);
            object.put("validType",validType);
            object.put("validDays",validDays);
            object.put("isDisable",isDisable);
            object.put("createUser",createUser);
            if(isDisable == 2){
                object.put("disableTimeOne",disableTimeOne);
                if(!TextUtils.isEmpty(disableTimeTwo))
                    object.put("disableTimeTwo",disableTimeTwo);
            }

            object.put("storeId",storeId);
            object.put("protocolType","0");
        } catch (Exception e) {
            e.printStackTrace();
        }


        OkUtils.getInstance().post(getActivity(), url, object, new MyOkCallback<MakeNoteBean>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(MakeNoteBean result) {
                String status = result.getStatus();
                String message = result.getMessage();
                if (!TextUtils.isEmpty(status) && "0".equals(status)){
                    if(!TextUtils.isEmpty(message))
                        ToastUtil.show(message);

                    getActivity().setResult(0);
                    hideProgressDialog();
                    getActivity().finish();
                }else {
                    if(!TextUtils.isEmpty(message))
                        ToastUtil.show(message);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public Class getClazz() {
                return MakeNoteBean.class;
            }
        });

    }




    private MyBottomDialog mDialog;
    private MyBottomDialog mDialog2;

    public void showMyBottomDialog() {

        List<String> strList = new ArrayList<>();
        strList.add("立即生效");
        strList.add("次日生效");
        if (mDialog == null) {
            mDialog = new MyBottomDialog(getActivity());
        }
        mDialog.showThisDialog("领取后生效时间", strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                if (position == 0) {
                    effectType = 0;
                    mTvShengXiao.setText("立即生效");

                }else if(position == 1){
                    effectType = 1;
                    mTvShengXiao.setText("次日生效");
                }
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });



    }

    List<StoreManageBean.ObjBean> storeList = new ArrayList<>();//门店列表的list
    MyBottonPopWindow popWindow;
    private void getStoreList(final boolean isClick) {
        String url = NetUrl.MEMBER_STORE_LIST;
        JSONObject jsonObject = new JSONObject();
        try {
            if (loginType.equals("0")) {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(getActivity(), url, jsonObject, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showProgressDialog("");
            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String data = response.optString("data");
                Gson gson = new Gson();
                List<StoreManageBean.ObjBean> dataList = gson.fromJson(data,
                        new TypeToken<List<StoreManageBean.ObjBean>>() {
                        }.getType());


                storeList.clear();
                if (dataList != null) {
                    storeList.addAll(dataList);
                }
                storeListAdapter.addAllList(storeList);
                if (storeList != null) {
                    if (isClick) {//点击获取的门店列表
                        if (popWindow == null) {
                            popWindow = new MyBottonPopWindow(getActivity(), storeList);
                        }
                        popWindow.showPopupWindow(storePosition);

                        popWindow.setStoreItemListListener(new MyBottonPopWindow.OnStoreItemListener() {
                            @Override
                            public void setStoreItemListener(int position) {
                                storePosition = position;
                                mSelectedStoreId = storeList.get(position).getStoreId();
                                mSelectStoreName = storeList.get(position).getStoreName();
                            }
                        });

                        popWindow.setDissmissListener(new MyBottonPopWindow.onDissmissListener() {
                            @Override
                            public void setDissmissListener() {
//
                            }
                        });

                        popWindow.setOnCloseListener(new MyBottonPopWindow.onCloseListener() {
                            @Override
                            public void setOnCloseListener() {
//
                            }
                        });

                        popWindow.setButtonClickListener(new MyBottonPopWindow.OnSureClickListener() {
                            @Override
                            public void setButtonClickListener() {
                                mStoreId = mSelectedStoreId;
                                mStoreName = mSelectStoreName;
                                if (TextUtils.isEmpty(mStoreId)) {
                                    mStoreId = storeList.get(0).getStoreId();
                                    mStoreName = storeList.get(0).getStoreName();
                                }

                                if ("0".equals(loginType))
                                    tv_shop_name.setText(mStoreName);

                            }
                        });
                    }
                } else {
                    hideProgressDialog();
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
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

    public void showMyBottomDialog2() {

        List<String> strList = new ArrayList<>();
        strList.add("相对时效");
        strList.add("绝对时效");
        if (mDialog2 == null) {
            mDialog2 = new MyBottomDialog(getActivity());
        }
        mDialog2.showThisDialog("使用时效", strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                if (position == 0) {
                    validType = 2;
                    tv_time_effect.setText("相对时效");
                    ll_validays.setVisibility(View.VISIBLE);
                    ll_validays_time.setVisibility(View.GONE);
                }else if(position == 1){
                    validType = 1;
                    tv_time_effect.setText("绝对时效");
                    ll_validays.setVisibility(View.GONE);
                    ll_validays_time.setVisibility(View.VISIBLE);
                }
                if (mDialog2 != null)
                    mDialog2.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(TextUtils.isEmpty(noUseTime1)){
            isShowDialog = false;
            switchButton.setChecked(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode){
            case 10:
                isShowDialog = true;
                ll_no_use_time.setVisibility(View.VISIBLE);
                noUseTime1 = intent.getStringExtra("noUseTime1");
                noUseTime2 = intent.getStringExtra("noUseTime2");
                disableTimeOne = intent.getStringExtra("disableTimeOne");
                disableTimeTwo = intent.getStringExtra("disableTimeTwo");
                tv_noUseTime1.setText(noUseTime1);
                tv_noUseTime2.setText(noUseTime2);
                break;
        }
    }


}
