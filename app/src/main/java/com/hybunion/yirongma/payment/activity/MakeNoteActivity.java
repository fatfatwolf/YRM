package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MakeNoteBean;
import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.adapter.StoreListAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
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

public class MakeNoteActivity extends BasicActivity implements View.OnClickListener {
    @Bind(R.id.rv_caldener_start)
    RelativeLayout rv_caldener_start;
    @Bind(R.id.rv_caldener_end)
    RelativeLayout rv_caldener_end;
    @Bind(R.id.tv_start_time_make_note_activity)
    TextView mTvStartTime;
    @Bind(R.id.tv_end_time_make_note_activity)
    TextView mTvEndTime;
    @Bind(R.id.rv_take_effect)
    RelativeLayout rv_take_effect;
    @Bind(R.id.rv_time_effect)
    RelativeLayout rv_time_effect;
    @Bind(R.id.rv_refresh)
    RelativeLayout rv_refresh;
    @Bind(R.id.rv_use_shop)
    RelativeLayout rv_use_shop;
    @Bind(R.id.titleBar)
    TitleBar titleBar;
    @Bind(R.id.ll_no_use_time)
    LinearLayout ll_no_use_time;
    @Bind(R.id.tv_shop_name)
    TextView tv_shop_name;
    @Bind(R.id.tv_take_effect)
    TextView tv_take_effect;
    @Bind(R.id.tv_time_effect)
    TextView tv_time_effect;
    @Bind(R.id.tv_noUseTime1)
    TextView tv_noUseTime1;
    @Bind(R.id.tv_noUseTime2)
    TextView tv_noUseTime2;
    @Bind(R.id.switch_time_use)
    Switch switchButton;
    @Bind(R.id.bt_make_notes)
    Button bt_make_notes;
    @Bind(R.id.et_notes_name)
    EditText et_notes_name;
    @Bind(R.id.et_notesCount)
    EditText et_notesCount;
    @Bind(R.id.et_notes_amount)
    EditText et_notes_amount;
    @Bind(R.id.et_use_condition)
    EditText et_use_condition;
    @Bind(R.id.tv_canUseDay)
    EditText tv_canUseDay;
    @Bind(R.id.ll_validays_time)
    LinearLayout ll_validays_time;
    @Bind(R.id.ll_validays)
    LinearLayout ll_validays;
    @Bind(R.id.tv_startUseTime)
    TextView tv_startUseTime;
    @Bind(R.id.tv_endUseTime)
    TextView tv_endUseTime;

    String noUseTime1;
    String noUseTime2;
    int effectType = 0;//0立即生效 1次日生效
    int validType = 2;//1绝对时效 2相对时效
    int isDisable = 1;//1关闭 2开启
    String loginType;//判断登录的类型
    List<StoreManageBean.ObjBean> storeList = new ArrayList<>();//门店列表的list
    StoreListAdapter storeListAdapter;
    MyBottonPopWindow popWindow;
    int storePosition = 0;
    private String mSelectedStoreId,mSelectStoreName;//选中的门店ID,门店名字
    private String mStoreId,mStoreName,disableTimeOne,disableTimeTwo;
    String createUser;
    String validDays;//生效后几天可以适用
    Gson gson;
    private boolean isShowDialog = true;
    private String memberType;
    private String merId;
    List<StoreManageBean.ObjBean> mStoreList = new ArrayList<>();//门店列表的list
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_make_note;
    }

    @Override
    public void initView() {
        super.initView();
        memberType = getIntent().getStringExtra("memberType");//1为优惠券页面跳转，其他为正常制券
        titleBar.setTitleBarBackClickListener(new TitleBar.OnTitleBackClickListener() {
            @Override
            public void titleBackClick() {
                showMyDialog3();
            }
        });
        gson = new Gson();
        merId = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        SharedPreferencesUtil.getInstance(MakeNoteActivity.this).putKey(SharedPConstant.SAVE_DAY_list,"");
        SharedPreferencesUtil.getInstance(MakeNoteActivity.this).putKey(SharedPConstant.SAVE_TIME_LIST,"");
        SharedPreferencesUtil.getInstance(MakeNoteActivity.this).putKey(SharedPConstant.SAVE_TIME_LIST2,"");
        loginType = SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey("loginType");
        createUser = SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey("loginName");
        if(loginType.equals("1")){
            mStoreId = SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey("storeId");
            mStoreName = SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey("storeName");
            tv_shop_name.setText(mStoreName);
        }
        storeListAdapter = new StoreListAdapter(this);
        rv_caldener_start.setOnClickListener(this);
        rv_caldener_end.setOnClickListener(this);
        ll_no_use_time.setOnClickListener(this);
        rv_take_effect.setOnClickListener(this);
        rv_time_effect.setOnClickListener(this);
        rv_use_shop.setOnClickListener(this);
        rv_refresh.setOnClickListener(this);
        tv_time_effect.setOnClickListener(this);
        tv_shop_name.setOnClickListener(this);
        tv_take_effect.setOnClickListener(this);
        bt_make_notes.setOnClickListener(this);
        tv_startUseTime.setOnClickListener(this);
        tv_endUseTime.setOnClickListener(this);
        tv_startUseTime.setText(YrmUtils.getNowDay("yyyy-MM-dd"));
        tv_endUseTime.setText(YrmUtils.getNowDay("yyyy-MM-dd"));
        et_notes_amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_use_condition.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mTvStartTime.setHint(YrmUtils.getNowDay("yyyy/MM/dd"));
        mTvStartTime.setTextColor(Color.parseColor("#B6BDD0"));
        mTvEndTime.setHint(YrmUtils.getNowDay("yyyy/MM/dd"));
        mTvEndTime.setTextColor(Color.parseColor("#B6BDD0"));

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//开启
                    isDisable = 2;
                    if(TextUtils.isEmpty(noUseTime1) && TextUtils.isEmpty(noUseTime2)){//没有设置过
                        isShowDialog = true;
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


    private MyDateTimePickDialog mStartTimePicker, mEndTimePicker;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_caldener_start:
                if (mStartTimePicker == null)
                    mStartTimePicker = new MyDateTimePickDialog(MakeNoteActivity.this);
                mStartTimePicker.setType(1);
//                int[] dateInts = getDate();
//                if (dateInts!=null && dateInts.length==3){
//                    mStartTimePicker.setDateAndTime(1, dateInts[0], dateInts[1], dateInts[2], 0, 0, 0);
//                }
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
                    mEndTimePicker = new MyDateTimePickDialog(MakeNoteActivity.this);
                mEndTimePicker.setType(1);
//                int[] dateInts = getDate();
//                if (dateInts!=null && dateInts.length==3){
//                    mStartTimePicker.setDateAndTime(1, dateInts[0], dateInts[1], dateInts[2], 0, 0, 0);
//                }
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
            case R.id.ll_no_use_time:
                Intent intent = new Intent(MakeNoteActivity.this,NoUseTimeActivity.class);
                intent.putExtra("noUseTime1",noUseTime1);
                intent.putExtra("noUseTime2",noUseTime2);
                intent.putExtra("clickType","1");
                startActivityForResult(intent,0);
                break;
            case R.id.tv_shop_name:
                if("0".equals(loginType)){//老板
                    getStoreList(true);
                }
                break;
            case R.id.tv_take_effect:
                showMyBottomDialog();
                break;
            case R.id.tv_time_effect:
                showMyBottomDialog2();
                break;

            case R.id.tv_startUseTime:
                if (mStartTimePicker == null)
                    mStartTimePicker = new MyDateTimePickDialog(MakeNoteActivity.this);
                mStartTimePicker.setType(1);
//                int[] dateInts = getDate();
//                if (dateInts!=null && dateInts.length==3){
//                    mStartTimePicker.setDateAndTime(1, dateInts[0], dateInts[1], dateInts[2], 0, 0, 0);
//                }
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
                    mStartTimePicker = new MyDateTimePickDialog(MakeNoteActivity.this);
                mStartTimePicker.setType(1);
//                int[] dateInts = getDate();
//                if (dateInts!=null && dateInts.length==3){
//                    mStartTimePicker.setDateAndTime(1, dateInts[0], dateInts[1], dateInts[2], 0, 0, 0);
//                }
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
            case R.id.bt_make_notes:
                if(YrmUtils.isFastDoubleClick())  return;

                String couponName  = et_notes_name.getText().toString().trim();
                String takeAmount  = et_notesCount.getText().toString().trim();
                String usedAmount  = et_notes_amount.getText().toString().trim();
                String withAmount  = et_use_condition.getText().toString().trim();
                String startTime  = mTvStartTime.getText().toString().trim();
                String endTime = mTvEndTime.getText().toString().trim();
                mStoreName = tv_shop_name.getText().toString().trim();

                validDays = tv_canUseDay.getText().toString().trim();
                String noTime1 = tv_noUseTime1.getText().toString().trim();
                String validStartTime = tv_startUseTime.getText().toString().trim();
                String validEndTime = tv_endUseTime.getText().toString().trim();

                if(TextUtils.isEmpty(couponName)){
                    ToastUtil.showShortToast("请输入优惠券名称");
                    return;
                }


                if(TextUtils.isEmpty(takeAmount)){
                    ToastUtil.showShortToast("券数量不能为空");
                    return;
                }

                if(Integer.parseInt(takeAmount)<50 || Integer.parseInt(takeAmount)>500){
                    ToastUtil.showShortToast("券数量为50到500之间");
                    return;
                }
                if(TextUtils.isEmpty(usedAmount)){
                    ToastUtil.showShortToast("单券金额不能为空");
                    return;
                }
                if(Double.valueOf(usedAmount)>Double.valueOf("500") || Double.valueOf(usedAmount)<Double.valueOf("0.10")){
                    ToastUtil.showShortToast("单券金额为0.1元-500元之间");
                    return;
                }

                if(TextUtils.isEmpty(withAmount)){
                    ToastUtil.showShortToast("请输入使用条件");
                    return;
                }

                if(Double.valueOf(withAmount)<Double.valueOf("0.01")){
                    ToastUtil.showShortToast("输入金额不能小于0.01元");
                    return;
                }
                if(Double.valueOf(withAmount)<Double.valueOf(usedAmount)){
                    ToastUtil.showShortToast("使用条件金额必须大于单券金额");
                    return;
                }
                if(TextUtils.isEmpty(startTime)){
                    ToastUtil.showShortToast("请输入起始时间");
                    return;
                }

                if(TextUtils.isEmpty(endTime)){
                    ToastUtil.showShortToast("请输入截止时间");
                    return;
                }

                Long startDay = YrmUtils.DayChangeToLong(startTime);
                Long endDay = YrmUtils.DayChangeToLong(endTime);

                if(endDay<startDay){
                    ToastUtil.showShortToast("终止时间不可小于起始时间");
                    return;
                }

                if(validType == 1){//绝对时效
                    if(TextUtils.isEmpty(validStartTime)){
                        ToastUtil.showShortToast("生效后开始时间不能为空");
                        return;
                    }

                    if(TextUtils.isEmpty(validEndTime)){
                        ToastUtil.showShortToast("生效后截止时间不能为空");
                        return;
                    }
                }else {//相对时效
                    if(TextUtils.isEmpty(validDays)){
                        ToastUtil.showShortToast("可用时间不能为空");
                        return;
                    }

                    if(Integer.parseInt(validDays)<2){
                        ToastUtil.showShortToast("可用时间不可低于2天");
                        return;
                    }

                    if(Integer.parseInt(validDays)>30){
                        ToastUtil.showShortToast("可用时间不可大于30天");
                        return;
                    }
                }


                if(TextUtils.isEmpty(mStoreName) || TextUtils.isEmpty(mStoreId)){
                    ToastUtil.showShortToast("适用门店不能为空");
                    return;
                }
                if(isDisable == 2){
                    if(TextUtils.isEmpty(noTime1) || TextUtils.isEmpty(disableTimeOne)){
                        ToastUtil.showShortToast("请选择不可用时间段1");
                        return;
                    }
                }
                showLoading();
                if(validType == 1){
                    makeNotes(couponName,takeAmount,usedAmount,withAmount,startTime,endTime
                            ,effectType,validType,isDisable,disableTimeOne,disableTimeTwo,mStoreId,createUser,
                            validStartTime,validEndTime);
                }else if(validType == 2){
                    makeNotes2(couponName,takeAmount,usedAmount,withAmount,startTime,endTime
                            ,effectType,validType,Integer.parseInt(validDays),isDisable,disableTimeOne,disableTimeTwo,mStoreId,createUser);
                }

                break;
        }
    }

    public void makeNotes(String couponName,String takeAmount,String usedAmount,String withAmount,String startTime,
                          String endTime,int effectType,int validType,int isDisable,String disableTimeOne,
                          String disableTimeTwo,String storeId,String createUser,String validStartTime,String validEndTime) {
        String url = NetUrl.MAKE_NOTES;
        JSONObject object = new JSONObject();
        try {
            String loginType = SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey("loginType");
            if(("0").equals(loginType)){
                object.put("merId", SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey(Constants.MERCHANTID));
            }else {
                object.put("merId", SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey("shopId"));
            }
            object.put("couponName", couponName);
            object.put("takeAmount",takeAmount);
            object.put("usedAmount",usedAmount);
            object.put("withAmount",withAmount);
            object.put("startTime",startTime);
            object.put("endTime",endTime);
            object.put("effectType",effectType);
            object.put("validType",validType);
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

        OkUtils.getInstance().post(MakeNoteActivity.this, url, object, new MyOkCallback<MakeNoteBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(MakeNoteBean baseBean) {
                if("1".equals(memberType)){
                    if(loginType.equals("0")){//老板制券
                        showMyDialog4();

                    }else if(loginType.equals("1")){//店长制券
                        showMyDialog5();
                    }
                }else {
                    if(!TextUtils.isEmpty(baseBean.getMessage())){
                        ToastUtil.showShortToast(baseBean.getMessage());
                    }
                    if(baseBean.getStatus().equals("0")){
                        setResult(0);
                        finish();
                    }
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
                return MakeNoteBean.class;
            }
        });



    }

    public void makeNotes2(String couponName,String takeAmount,String usedAmount,String withAmount,String startTime,
                           String endTime,int effectType,int validType,int validDays,int isDisable,String disableTimeOne,
                           String disableTimeTwo,String storeId,String createUser) {

        String url = NetUrl.MAKE_NOTES;
        JSONObject object = new JSONObject();
        try {
            String loginType = SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey("loginType");
            if(("0").equals(loginType)){
                object.put("merId", SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey(Constants.MERCHANTID));
            }else {
                object.put("merId", SharedPreferencesUtil.getInstance(MakeNoteActivity.this).getKey("shopId"));
            }
            object.put("couponName", couponName);
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
        OkUtils.getInstance().post(MakeNoteActivity.this, url, object, new MyOkCallback<MakeNoteBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(MakeNoteBean baseBean) {
                if("1".equals(memberType)){
                    if("0".equals(baseBean.getStatus())){
                        if(loginType.equals("0")){//老板制券
                            showMyDialog4();

                        }else if(loginType.equals("1")){//店长制券
                            showMyDialog5();
                        }
                    }else {
                        if(!TextUtils.isEmpty(baseBean.getMessage())){
                            ToastUtil.showShortToast(baseBean.getMessage());
                        }
                    }

                }else {
                    if(!TextUtils.isEmpty(baseBean.getMessage())){
                        ToastUtil.showShortToast(baseBean.getMessage());
                    }
                    if(baseBean.getStatus().equals("0")){
                        setResult(0);
                        finish();
                    }
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
                return MakeNoteBean.class;
            }
        });

    }



    Dialog mMyDialog4;
    private void showMyDialog4() {
        mMyDialog4 = new Dialog(this);
        mMyDialog4.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyDialog4.setContentView(R.layout.activity_quan_dialog);
        mMyDialog4.setCanceledOnTouchOutside(false);
        mMyDialog4.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeNoteActivity.this,YouHuiQuanListActivity.class);
                startActivity(intent);
                finish();
                HRTApplication.finishActivity(MemberManageActivity.class);
                mMyDialog4.dismiss();
            }
        });
        mMyDialog4.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyDialog4.dismiss();
                finish();
            }
        });
        mMyDialog4.show();
    }


    Dialog mMyDialog5;
    private void showMyDialog5() {
        mMyDialog5 = new Dialog(this);
        mMyDialog5.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyDialog5.setContentView(R.layout.activity_dianzhang_dialog);
        mMyDialog5.setCanceledOnTouchOutside(false);
        mMyDialog5.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyDialog5.dismiss();
                finish();
            }
        });
        mMyDialog5.show();
    }

    private void getStoreList(final boolean isClick) {
        JSONObject body = new JSONObject();
        try {
            body.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.MEMBER_STORE_LIST, body, new MyOkCallback<StoreManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }
            @Override
            public void onSuccess(StoreManageBean bean) {
                List<StoreManageBean.ObjBean> dataList = bean.getData();
                storeList.clear();
                if (!YrmUtils.isEmptyList(dataList)) {
                    storeList.addAll(dataList);
                }
                storeListAdapter.addAllList(storeList);
                if (!YrmUtils.isEmptyList(storeList)) {
                    if (isClick) {//点击获取的门店列表
                        if (popWindow == null) {
                            popWindow = new MyBottonPopWindow(MakeNoteActivity.this, storeList);
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
                return StoreManageBean.class;
            }
        });


        showLoading();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                hideLoading();
                LogUtils.d(response + "返回数据");
                String count = response.optString("count");
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
                            popWindow = new MyBottonPopWindow(MakeNoteActivity.this, storeList);
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
                    hideLoading();
                }

            }
        };
        Response.ErrorListener errorlistener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                hideLoading();
                ToastUtil.showShortToast("网络状况不佳");
            }
        };

    }


    private void showNoteDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_stuatus,null);
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
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
                isShowDialog = false;
                switchButton.setChecked(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private Dialog mMyDialog;
    private Dialog mMyTakeDialog;

    private void showMyTakeDialog(int dialogLayout) {
        mMyTakeDialog = new Dialog(this);
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
        mMyTimeDialog = new Dialog(this);
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




    private Dialog mMyUseDialog;

    private void showMyUseDialog(int dialogLayout) {
        mMyUseDialog = new Dialog(this);
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
        mMyShopDialog = new Dialog(this);
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




    private void showMyDialog2(int dialogLayout) {
        mMyDialog = new Dialog(this);
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
                Intent intent = new Intent(MakeNoteActivity.this,NoUseTimeActivity.class);
                intent.putExtra("clickType","1");
                startActivityForResult(intent,0);
                mMyDialog.dismiss();
            }
        });
        mMyDialog.show();
    }


    private MyBottomDialog mDialog;
    private MyBottomDialog mDialog2;

    public void showMyBottomDialog() {

        List<String> strList = new ArrayList<>();
        strList.add("立即生效");
        strList.add("次日生效");
        if (mDialog == null) {
            mDialog = new MyBottomDialog(this);
        }
        mDialog.showThisDialog("领取后生效时间", strList, new MyBottomDialog.OnDialogItemListener() {
            @Override
            public void itemListener(int position) {
                if (position == 0) {
                    effectType = 0;
                    tv_take_effect.setText("立即生效");

                }else if(position == 1){
                    effectType = 1;
                    tv_take_effect.setText("次日生效");
                }
                if (mDialog != null)
                    mDialog.dismiss();
            }
        });



    }
    public void showMyBottomDialog2() {

        List<String> strList = new ArrayList<>();
        strList.add("相对时效");
        strList.add("绝对时效");
        if (mDialog2 == null) {
            mDialog2 = new MyBottomDialog(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
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

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showMyDialog3();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TextUtils.isEmpty(noUseTime1)){//从设置不可用时间界面返回键回到该界面
            isShowDialog = false;
            switchButton.setChecked(false);//关闭switchButton

        }
    }

    Dialog mMyDialog3;
    private void showMyDialog3() {
        mMyDialog3 = new Dialog(this);
        mMyDialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mMyDialog3.setContentView(R.layout.activity_back_dialog);
        mMyDialog3.setCanceledOnTouchOutside(false);
        mMyDialog3.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mMyDialog3.dismiss();
            }
        });
        mMyDialog3.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyDialog3.dismiss();
            }
        });
        mMyDialog3.show();
    }

}
