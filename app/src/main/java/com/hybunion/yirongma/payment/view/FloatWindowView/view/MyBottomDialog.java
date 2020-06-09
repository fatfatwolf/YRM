package com.hybunion.yirongma.payment.view.FloatWindowView.view;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.activity.DialogActivity;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.activity.LMFRedRainActivity;
import com.hybunion.yirongma.payment.activity.VoiceSettingActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.view.FloatWindowView.CheckBean;
import com.hybunion.yirongma.payment.utils.BoBaoUtils;
import com.hybunion.yirongma.payment.utils.NetWorkUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MyBottomDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TextView mTvNoCheck;
    private AutoRelativeLayout mCheckContentParent;
    private AutoLinearLayout mCheckingParent;
    private LayoutInflater mInflater;
    private TextView mTvBottomButton;
    private Dialog mDialog;
    private boolean mIsStopCheck;   // 最下面的按钮是否显示了停止检测
    private ImageView mImgCheck, mImgCheckNoProblem;
    private List<CheckBean> mCheckBeanList = new ArrayList<>();
    private ListView mLv;
    private TextView mTvTopContent;
    private TextView mTvPhoneOk;
    private AutoRelativeLayout mCheckingTopParent, mResultTopParent;
    private TextView mTvProblemNum;
    private boolean mCanCheck = true; // 是否可以检测。如果用户点击了停止，就置为 false，不再继续检测剩下的项

    private static final int NOTIFICATION = 1;  // 通知权限检测
    private static final int BATTERY_CHECK = 2;  // 电池优化检测
    private static final int NET_CHECK = 3; // 网络检测
    private static final int PUSH_CHECK = 4; // 推送检测
    private static final int VOICE_CHECK = 5;  // 是否开启语音播报
    private List<Integer> mTypeList = new ArrayList<>(5);
    private int mNextType;  // 保存停止的时候执行到的检测，再次开始时，从下一个开始。
    private ImageView mImgTopChecking;
    private boolean mIsGreat; // 是否全部检测完成并且没有问题。
    private boolean mIsLastFinish = true; // 上一条是否执行完

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NOTIFICATION:    // 检测通知权限
                    checkNotification();
                    break;
                case BATTERY_CHECK:  // 检测忽略电池优化
                    mIsLastFinish = true;
                    if (mOaNotification != null)
                        mOaNotification.cancel();
                    if (mImgNotification != null)
                        mImgNotification.setVisibility(View.GONE);
                    if (mImgOkNotification != null)
                        mImgOkNotification.setVisibility(View.VISIBLE);
                    if (!mCanCheck) {
                        afterCheckFinish();
                        mNextType = BATTERY_CHECK;
                        return;
                    }
                    checkBattery();
                    break;
                case NET_CHECK:    // 检测网络
                    mIsLastFinish = true;
                    if (mOaNotification != null)
                        mOaNotification.cancel();
                    if (mImgNotification != null)
                        mImgNotification.setVisibility(View.GONE);
                    if (mImgOkNotification != null)
                        mImgOkNotification.setVisibility(View.VISIBLE);

                    if (mOaBattery != null)
                        mOaBattery.cancel();
                    if (mImgBattery != null)
                        mImgBattery.setVisibility(View.GONE);
                    if (mImgOkBattery != null)
                        mImgOkBattery.setVisibility(View.VISIBLE);

                    if (!mCanCheck) {
                        afterCheckFinish();
                        mNextType = NET_CHECK;
                        return;
                    }
                    checkNet();
                    break;
                case PUSH_CHECK:   // 检测推送注册
                    mIsLastFinish = true;
                    if (mOaNetCheck != null)
                        mOaNetCheck.cancel();
                    if (mImgNet != null)
                        mImgNet.setVisibility(View.GONE);
                    if (mImgOkNet != null)
                        mImgOkNet.setVisibility(View.VISIBLE);

                    if (!mCanCheck) {
                        afterCheckFinish();
                        mNextType = PUSH_CHECK;
                        return;
                    }
                    checkPush();
                    break;
                case VOICE_CHECK:    // 检测播报是否开启
                    mIsLastFinish = true;
                    if (mOaPush != null)
                        mOaPush.cancel();
                    if (mImgPush != null)
                        mImgPush.setVisibility(View.GONE);
                    if (mImgOkPush != null)
                        mImgOkPush.setVisibility(View.VISIBLE);

                    if (!mCanCheck) {
                        afterCheckFinish();
                        mNextType = VOICE_CHECK;
                        return;
                    }
                    checkVoiceSwitch();
                    break;
            }
        }
    };

    public MyBottomDialog(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public MyBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyBottomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initView() {
        mTypeList.add(NOTIFICATION);
        mTypeList.add(BATTERY_CHECK);
        mTypeList.add(NET_CHECK);
        mTypeList.add(PUSH_CHECK);
        mTypeList.add(VOICE_CHECK);

        mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.layout_my_bottom_dialog1, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.drawable.shape_my_bottom_dialog);
        window.setWindowAnimations(R.style.dialog_anim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mTvNoCheck = view.findViewById(R.id.tv_no_check);
        mCheckContentParent = view.findViewById(R.id.check_content_parent);
        mCheckingParent = view.findViewById(R.id.checkingParent);
        mTvBottomButton = view.findViewById(R.id.tv_bottom_button);
        mLv = view.findViewById(R.id.listView_check);
        mTvTopContent = view.findViewById(R.id.tv_check1);
        mTvPhoneOk = view.findViewById(R.id.tv_phone_ok);
        mCheckingTopParent = view.findViewById(R.id.checking_parent);
        mResultTopParent = view.findViewById(R.id.result_parent);
        mTvProblemNum = view.findViewById(R.id.tv_problem_num);
        mImgTopChecking = view.findViewById(R.id.img_checking);
        mTvBottomButton.setOnClickListener(this);
        window.setAttributes(params);
        // 暂不检测 按钮监听
        mTvNoCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBottomDialog.this.dismiss();
            }
        });

        mImgCheck = view.findViewById(R.id.img_check_click);
        mImgCheckNoProblem = view.findViewById(R.id.img_check_click_no_problem);
        mImgCheck.setOnClickListener(this);
    }

    // 外部调用
    public void showThisDialog() {
        if (getOwnerActivity() != null && !getOwnerActivity().isFinishing())
            this.show();
    }

    private ObjectAnimator mObjectAnimator;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_check_click:   // 开始检测 按钮监听。
                mIsStopCheck = true;
                mCanCheck = true;
                mTvBottomButton.setText("停止检测");
                mImgCheck.setVisibility(View.GONE);
                mImgTopChecking.setVisibility(View.VISIBLE);
                mTvTopContent.setText("语音播报系统检测中......");
                if (mNextType == 0) {  // 第一次点击开始检测
                    mTvNoCheck.setVisibility(View.GONE);
                    mCheckContentParent.setVisibility(View.VISIBLE);
                    if (mIsLastFinish)
                        goToCheck();
                } else {   // 暂停后又点了开始检测。
                    if (mNextType != 0 && (mNextType - 1) <= mTypeList.size() - 1) {
                        if (mIsLastFinish)
                            mHandler.sendEmptyMessage(mTypeList.get(mNextType - 1));
                    }
                }
                mObjectAnimator = ObjectAnimator.ofFloat(mImgTopChecking, "rotation", 0f, 360f);
                mObjectAnimator.setDuration(2000);
                mObjectAnimator.setRepeatCount(80);
                mObjectAnimator.start();

                break;

            case R.id.tv_bottom_button:  // 最下面的按钮，正在检测时是停止检测，检测完成时是完成
                if (mIsGreat) {   // 检测完成并且没有问题时，直接退出.
                    this.dismiss();
                    return;
                }

                DialogActivity.OnButtonClickListener ls = new DialogActivity.OnButtonClickListener(){

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void ok() {
                        if (mIsStopCheck){
                            mCanCheck = false;
                            mImgCheck.setVisibility(View.VISIBLE);
                            mImgTopChecking.setVisibility(View.GONE);
                            if (mObjectAnimator != null)
                                mObjectAnimator.cancel();
                        }else{
                            MyBottomDialog.this.dismiss();
                        }
                    }
                };

                if (mIsStopCheck) {
                    DialogActivity.start(mContext, "停止检测", "停止后将无法全面检测语音系统设置",ls);
                }else{
                    DialogActivity.start(mContext, "提示", "系统检测问题项未修复完毕，页面关闭后将无法继续进行，是否确认放弃。",ls);
                }

                break;
        }
    }


    // 检测方法
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void goToCheck() {
        mHandler.sendEmptyMessage(NOTIFICATION);
    }

    // ========== 通知权限检测 ===========
    private ObjectAnimator mOaNotification;
    private ImageView mImgNotification, mImgOkNotification;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void checkNotification() {
        mIsLastFinish = false;
        View view = mInflater.inflate(R.layout.item_checking_layout, null);
        mImgNotification = view.findViewById(R.id.img_item_checking_layout);
        mImgOkNotification = view.findViewById(R.id.img_item_check_ok_layout);
        TextView tv = view.findViewById(R.id.tv_check_content_item_checking);
        tv.setText("系统通知权限检测中...");
        mCheckingParent.addView(view);
        mOaNotification = initAnimator(mImgNotification);
        boolean isNotificationOpen = BoBaoUtils.isNotificationEnabled(mContext);

        if (isNotificationOpen) {
            tv.setText("系统通知权限已开启");
        } else {
            tv.setText("系统通知权限未开启");
            CheckBean notiBean = new CheckBean();
            notiBean.type = NOTIFICATION;
            notiBean.content = "设备未开启通知权限";
            mCheckBeanList.add(notiBean);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mHandler.sendEmptyMessageDelayed(BATTERY_CHECK, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(NET_CHECK, 2000);
        }

    }

    // ======= 是否忽略电池优化检测 仅支持6.0及以上 =======
    private ObjectAnimator mOaBattery;
    private ImageView mImgBattery, mImgOkBattery;

    private void checkBattery() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            mIsLastFinish = false;
            View view1 = mInflater.inflate(R.layout.item_checking_layout, null);
            mImgBattery = view1.findViewById(R.id.img_item_checking_layout);
            mImgOkBattery = view1.findViewById(R.id.img_item_check_ok_layout);
            TextView tv1 = view1.findViewById(R.id.tv_check_content_item_checking);
            tv1.setText("电池优化权限检测中...");
            mCheckingParent.addView(view1);
            mOaBattery = initAnimator(mImgBattery);
            boolean isBatteryOpen = BoBaoUtils.isBatteryOpen(mContext);
            if (isBatteryOpen) {
                tv1.setText("电池优化权限已开启");
            } else {
                tv1.setText("电池优化权限未开启");
                CheckBean notiBean = new CheckBean();
                notiBean.type = BATTERY_CHECK;
                notiBean.content = "设备已开启电池优化，建议关闭";
                mCheckBeanList.add(notiBean);
            }

        }
        mHandler.sendEmptyMessageDelayed(NET_CHECK, 2000);

    }

    // ======= 检测网络 =======
    private ObjectAnimator mOaNetCheck;
    private ImageView mImgNet, mImgOkNet;

    private void checkNet() {
        mIsLastFinish = false;
        View view2 = mInflater.inflate(R.layout.item_checking_layout, null);
        mImgNet = view2.findViewById(R.id.img_item_checking_layout);
        mImgOkNet = view2.findViewById(R.id.img_item_check_ok_layout);
        final TextView tv2 = view2.findViewById(R.id.tv_check_content_item_checking);
        tv2.setText("网络环境检测中...");
        mCheckingParent.addView(view2);
        mOaNetCheck = initAnimator(mImgNet);
        NetWorkUtils.getInstance().getDelay(new NetWorkUtils.NetWorkUtilsListener() {
            @Override
            public void netWork(boolean object) {
                if (object) {
                    tv2.setText("网络环境良好");
                } else {
                    tv2.setText("网络连接不稳定");
                    CheckBean notiBean = new CheckBean();
                    notiBean.type = NET_CHECK;
                    notiBean.content = "网络连接不稳定，请切换优质网络";
                    mCheckBeanList.add(notiBean);
                }

                mHandler.sendEmptyMessage(PUSH_CHECK);
            }
        });

    }

    // ======== 检测推送注册 ========
    private ObjectAnimator mOaPush;
    private ImageView mImgPush, mImgOkPush;

    private void checkPush() {
        mIsLastFinish = false;
        View view3 = mInflater.inflate(R.layout.item_checking_layout, null);
        mImgPush = view3.findViewById(R.id.img_item_checking_layout);
        mImgOkPush = view3.findViewById(R.id.img_item_check_ok_layout);
        TextView tv3 = view3.findViewById(R.id.tv_check_content_item_checking);
        mCheckingParent.addView(view3);
        mOaPush = initAnimator(mImgPush);
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            Method method = cls.getDeclaredMethod("get", new Class[]{String.class});
            String apiLevel = (String) method.invoke(cls, new Object[]{"ro.build.hw_emui_api_level"});
            int emuiApiLevel = Integer.parseInt(TextUtils.isEmpty(apiLevel) ? "0" : apiLevel);
            if (emuiApiLevel > 10) {  // 检测华为推送
                tv3.setText("华为推送注册检测中...");
                String resultHuaWei = SharedPreferencesUtil.getInstance(mContext).getKey(SharedPConstant.HUAWEI_IS_REGISTE);
                if ("成功".equals(resultHuaWei)) {
                    tv3.setText("华为推送注册成功");
                } else {
                    tv3.setText("华为推送注册失败");
                }
            } else {  // 检测极光推送
                tv3.setText("极光平台注册检测中...");
                String resultJiGuang = SharedPreferencesUtil.getInstance(mContext).getKey(SharedPConstant.JPUSH_IS_REGISTE);
                if ("成功".equals(resultJiGuang)) {
                    tv3.setText("极光平台注册成功");
                } else {
                    tv3.setText("极光平台注册失败");
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            tv3.setText("推送检测失败");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            tv3.setText("推送检测失败");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            tv3.setText("推送检测失败");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            tv3.setText("推送检测失败");
        }
        mHandler.sendEmptyMessageDelayed(VOICE_CHECK, 2000);
    }

    //=========== 检测是否开启语音播报 ============
    private void checkVoiceSwitch() {
        View view4 = mInflater.inflate(R.layout.item_checking_layout, null);
        ImageView img4 = view4.findViewById(R.id.img_item_checking_layout);
        ImageView imgOk4 = view4.findViewById(R.id.img_item_check_ok_layout);
        TextView tv4 = view4.findViewById(R.id.tv_check_content_item_checking);
        mCheckingParent.addView(view4);
        tv4.setText("语音播报开关检测中...");
        ObjectAnimator objectAnimator4 = initAnimator(img4);
        String type = SharedPreferencesUtil.getInstance(mContext).getKey("VoiceSwitch");
        if ("1".equals(type) || TextUtils.isEmpty(type)) {  // 开启了语音播报，第一次为空
            tv4.setText("语音播报已开启");
        } else {  // 未开启语音播报  VOICE_CHECK
            tv4.setText("语音播报开关未开启");
            CheckBean notiBean = new CheckBean();
            notiBean.type = VOICE_CHECK;
            notiBean.content = "语音播报开关未开启";
            mCheckBeanList.add(notiBean);
        }
        objectAnimator4.cancel();
        img4.setVisibility(View.GONE);
        imgOk4.setVisibility(View.VISIBLE);

        // 全部检测完成
        afterCheckFinish();
    }


    // ============= 检测项全部完成后的操作 =============
    private void afterCheckFinish() {
        mIsStopCheck = false;
        mTvBottomButton.setText("完成");
        if (mObjectAnimator != null)
            mObjectAnimator.cancel();

        if (!mCanCheck) {  // 不是检测完成走到这里，而是点击了暂停走到这里

        } else {  // 检测全部完成后执行
            mTvTopContent.setText("仍未解决您的问题请点击 >");
            mTvTopContent.setOnClickListener(new View.OnClickListener() {  // 跳转到手机设置的帮助页面
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LMFRedRainActivity.class);
                    intent.putExtra("webViewUrl", "3");
                    mContext.startActivity(intent);
                }
            });

            // 如果没有问题，显示 “最佳状态“ 图片，如果有问题，显示 “n项异常" 布局
            if (mCheckBeanList.size() == 0) {
                mImgCheck.setVisibility(View.GONE);
                mImgTopChecking.setVisibility(View.GONE);
                mImgCheckNoProblem.setVisibility(View.VISIBLE);
                mIsGreat = true;
            } else {
                mCheckingTopParent.setVisibility(View.GONE);
                mResultTopParent.setVisibility(View.VISIBLE);
                mTvProblemNum.setText(mCheckBeanList.size() + "");
            }
        }

        // 设置 ListView
        setListView();

    }


    private void setListView() {
        mTvNoCheck.setVisibility(View.GONE);
        // 如果有问题，再设置 ListView ， 没问题显示文字
        if (mCheckBeanList.size() != 0) {
            mLv.setVisibility(View.VISIBLE);
            mLv.setAdapter(new CommonAdapter1<CheckBean>(mContext, mCheckBeanList, R.layout.item_check_list_layout) {
                @Override
                public void convert(ViewHolder holder, CheckBean item, int position) {
                    final TextView tvContent = holder.findView(R.id.tv_check_result);
                    TextView tvTrim = holder.findView(R.id.tv_go_to_trim);
                    tvTrim.setVisibility(View.VISIBLE);
                    final int checkType = mCheckBeanList.get(position).type;
                    switch (checkType) {
                        case NOTIFICATION:   // 通知检测
                            tvContent.setText("系统通知权限未开启");
                            break;
                        case BATTERY_CHECK:   // 电池优化检测
                            tvContent.setText("设备已开启电池优化，建议关闭");
                            break;
                        case VOICE_CHECK:
                            tvContent.setText("语音播报开关未开启");
                            break;
                        case NET_CHECK:   // 网络检测
                            tvContent.setText("网络环境不稳定");
                            break;
                        default:
                            tvTrim.setVisibility(View.GONE);
                    }

                    tvTrim.setOnClickListener(new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            switch (checkType) {
                                case NOTIFICATION:   // 通知检测
                                    BoBaoUtils.intentToNotification(mContext);
                                    break;
                                case BATTERY_CHECK:   // 电池优化检测   // 6.0 以上用
                                    BoBaoUtils.ignoreBatteryOptimization(mContext);
                                    break;
                                case VOICE_CHECK:    // 语音播报开关检测
                                    //  跳转语音设置界面
                                    mContext.startActivity(new Intent(mContext, VoiceSettingActivity.class));
                                    break;
                                case NET_CHECK:   // 网络检测
                                    BoBaoUtils.goToSetWIFI(mContext);
                                    break;
                            }
                        }
                    });

                }
            });

        } else {
            if (mCanCheck)  // 如果是暂停走这里，就不再显示这句话了。
                mTvPhoneOk.setVisibility(View.VISIBLE);
        }


    }


    private ObjectAnimator initAnimator(ImageView img) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(img, "rotation", 0f, 360f);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(50);
        objectAnimator.start();
        return objectAnimator;
    }


}
