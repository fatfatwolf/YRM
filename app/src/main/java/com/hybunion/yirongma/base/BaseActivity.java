package com.hybunion.yirongma.base;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.util.CommonUtil;
import com.hybunion.yirongma.common.util.shortcutbadger.ShortcutBadgeException;
import com.hybunion.yirongma.common.util.shortcutbadger.ShortcutBadger;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.view.MyDialogView;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 所有Activity继承BaseActivity
 *
 * @author LZ
 */
public class BaseActivity extends AutoLayoutActivity {
    protected int displayHeight;
    protected int displayWidth;
    private RelativeLayout mProgressDialog;
    private Calendar calendar;
    private String time;
    private TextView title;
    private String str;
    private MyDialogView myDialogView;
    private View activity_dialog;
    private boolean isActive = true;
    private Toast toast;
    private MyHndler handler = new MyHndler(this);

    private Gson mGson;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private String phoneType;

    /**
     * 非静态内部类会默认持有外部类的引用，为了防止内存泄漏所以使用静态内部类，
     * 并且使用弱引用来回收activity.
     */
    public static class MyHndler extends android.os.Handler {
        WeakReference<BaseActivity> softReference;

        public MyHndler(BaseActivity baseActivity) {
            softReference = new WeakReference<BaseActivity>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseActivity baseActivity = softReference.get();
            if (baseActivity != null) {
                // 更新ui
                switch (msg.what) {
                    case 0:
                        baseActivity.myDialogView.startAnim();
                        break;
                    case 1:
                        ViewGroup parent = (ViewGroup) baseActivity.activity_dialog.getParent();
                        if (parent != null) {
                            parent.removeView(baseActivity.activity_dialog);
                        }
                        break;
                    case 2:
                        baseActivity.isActive = false;
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initTransparencyTitle();
        LogUtil.d("========= 当前进入 ========  " + this.getClass().getSimpleName());
        StatusBarCompat.compat(this, getResources().getColor(R.color.main_color2));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mGson = new Gson();
        displayWidth = dm.widthPixels; // 得到宽度
        displayHeight = dm.heightPixels; // 得到高度
        LayoutInflater li = LayoutInflater.from(this);
        activity_dialog = getLayoutInflater().inflate(R.layout.activity_dialog_icon, null);
        myDialogView = (MyDialogView) activity_dialog.findViewById(R.id.myDialog);
        mProgressDialog = (RelativeLayout) li.inflate(R.layout.dialog_progressbar, null);
        title = (TextView) mProgressDialog.findViewById(R.id.tv_loading_title);
        //HRTApplication.getInstance().setA(BaseActivity.this);
        HRTApplication.getInstance().addActivity(this);
        phoneType = android.os.Build.BRAND;
        if(phoneType.toUpperCase().equals("VIVO") || phoneType.toUpperCase().equals("OPPO")){
            Log.i("手机型号",phoneType.toUpperCase());
            String phoneNum = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.LOGIN_NUMBER);
            CrashReport.postCatchedException(new Throwable("调用屏幕常亮，手机号："+phoneNum));
            powerManager = (PowerManager)this.getSystemService(this.POWER_SERVICE);
            wakeLock = this.powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Lock");

        }

//        AnalyticsConfig.enableEncrypt(true);
        initView();
        initData();
    }

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initTransparencyTitle() {
        initTransparencyTitle(0);
    }

    protected void initTransparencyTitle(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View statusBarView = new View(this);
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(BaseActivity.this, 25));
            if (color == 0) {
                statusBarView.setBackgroundColor(getResources().getColor(setStatusBarColor()));
            } else {
                statusBarView.setBackgroundColor(getResources().getColor(color));
            }

            ViewGroup view = (ViewGroup) getWindow().getDecorView();
            statusBarView.setLayoutParams(lParams);
            view.addView(statusBarView);
            for (int i = 0; i < view.getChildCount(); i++) {
                LogUtils.d("hxs", view.getChildAt(i).toString());
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (color == 0) {
                window.setStatusBarColor(getResources().getColor(setStatusBarColor()));
            } else {
                window.setStatusBarColor(getResources().getColor(color));
            }
        }
    }

    protected int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    protected int setStatusBarColor() {
        return R.color.main_color;
    }

    public Resources getResources() {

        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (toast != null) {
            toast.cancel();
        }
        //防止内存泄漏
        handler.removeCallbacksAndMessages(null);
        HRTApplication.getInstance().removeActivity(this);
    }

    public void showProgressDialog(String titleString) {
        if (titleString != null) {
            title.setText(titleString);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) activity_dialog.getParent();
                if (parent != null) {
                    parent.removeView(activity_dialog);
                }
                ViewGroup top = (ViewGroup) getWindow().getDecorView();
                top.addView(activity_dialog);
                handler.sendEmptyMessageDelayed(0, 50);
            }
        });
    }

    public void showMyProgressDialog(final ViewGroup top, String titleString) {
        if (titleString != null) {
            title.setText(titleString);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) activity_dialog.getParent();
                if (parent != null) {
                    parent.removeView(activity_dialog);
                }
                if (top != null) {
                    top.addView(activity_dialog);
                    activity_dialog.bringToFront();
                }
                handler.sendEmptyMessageDelayed(0, 50);
            }
        });
    }


    public void hideProgressDialog() {
        handler.sendEmptyMessageDelayed(1, 50);
    }

    public void showShortTipToast(String message, int drawres) {
        View toastRoot = getLayoutInflater().inflate(R.layout.recordtiptoast, null);
        TextView msgtv = (TextView) toastRoot.findViewById(R.id.tv_toast_tip);
        msgtv.setText(message);
        ImageView msgiv = (ImageView) toastRoot.findViewById(R.id.iv_toast);
        msgiv.setImageResource(drawres);
        Toast mShortToast = new Toast(this);
        mShortToast.setGravity(Gravity.CENTER, 0, 0);
        mShortToast.setDuration(Toast.LENGTH_SHORT);
        mShortToast.setView(toastRoot);
        mShortToast.show();
    }

    /**
     * 显示时间Dialog
     *
     * @param
     */
    public void showDateDialog(final EditText et_date) {
        calendar = Calendar.getInstance();
        final DatePickerDialog datePicker = new DatePickerDialog(this, new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int dayOfMonth) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String srt = sdf.format(new Date());
                try {
                    Date d1 = sdf.parse(srt);
                    Date d2 = sdf.parse(year + "-" + (month + 1) + "-" + dayOfMonth);
                    et_date.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.show();
    }

    public void showConfirmDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(msg).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public void showConfirmDialog(String msg) {
        showConfirmDialog("", msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(phoneType.toUpperCase().equals("VIVO") || phoneType.toUpperCase().equals("OPPO")){
            wakeLock.acquire();
        }

        isActive = true;
        //清除角标
        try {
            ShortcutBadger.removeCountOrThrow(this);
            SharedPreferences sp = this.getSharedPreferences("jPush", MODE_PRIVATE);
            sp.edit().putInt("badgeCount", 0).commit();
        } catch (ShortcutBadgeException e) {
            e.printStackTrace();
        }
        // 是否需要收到推送时唤醒屏幕 0-不需要
        SharedPreferencesUtil.getInstance(this).putKey(SharedPConstant.NEED_WAKE_UP, 0);


    }


    @Override
    protected void onPause() {
        super.onPause();
        if(phoneType.toUpperCase().equals("VIVO") || phoneType.toUpperCase().equals("OPPO")){
            wakeLock.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 锁屏、HOME键、屏幕自己熄灭，都会进入 if ，正常销毁 Activity 则不会。
        if (!CommonUtil.isAppOnForeground(this)) {
            SharedPreferencesUtil.getInstance(this).putKey(SharedPConstant.NEED_WAKE_UP, 1);
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
//
//    //权限的回调处理
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case YrmUtils.REQUEST_PERMISSION_LIST:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {//未申请权限
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                        if (showRequestPermission) {//已申请

                        } else {//未申请
                            ToastUtil.show("请到应用管理中开启相应权限");
                        }
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_CAMERA:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                    if (!showRequestPermission) {
                        ToastUtil.show("获取相机权限未申请，请去应用管理-权限中开启权限");
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_PHONE:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                    if (!showRequestPermission) {
                        ToastUtil.show("获取电话权限未申请，请去应用管理-权限中开启权限");
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                    if (!showRequestPermission) {
                        ToastUtil.show("获取存储权限未申请，请去应用管理-权限中开启权限");
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                    if (!showRequestPermission) {//false为未申请
                        ToastUtil.show("获取电话信息未申请，请去应用管理-权限中开启权限");
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_LOCATION:  // 定位  包括两个权限
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    if (permissions.length>1){
                        boolean showRequestPermission1 = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                        boolean showRequestPermission2 = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1]);
                        if (!showRequestPermission1 && !showRequestPermission2) {//false为未申请
                            ToastUtil.show("定位权限未申请，请去应用管理-权限中开启权限");
                        }
                    }else{
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                        if (!showRequestPermission) {//false为未申请
                            ToastUtil.show("定位权限未申请，请去应用管理-权限中开启权限");
                        }
                    }

                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
 