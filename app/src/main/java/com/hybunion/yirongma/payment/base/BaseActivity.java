package com.hybunion.yirongma.payment.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.StatusBarCompat;
import com.hybunion.yirongma.common.util.CommonUtil;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.inteface.IBaseView;
import com.hybunion.yirongma.payment.inteface.ILoadDataView;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.MainFrameTask;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by king on 2016/6/16.
 */

public abstract class BaseActivity extends AutoLayoutActivity
        implements IBaseView, ILoadDataView {


    //@Nullable
    // @Bind(R.id.rl_progress)
    protected RelativeLayout rl_progress;

    @Nullable
    @Bind(R.id.rl_retry)
    protected RelativeLayout rl_retry;
    @Nullable
    @Bind(R.id.bt_retry)
    protected Button bt_retry;
    @Nullable
    @Bind(R.id.tv_titlebar_back_title)
    protected TextView title;
    @Nullable
    @Bind(R.id.ll_titlebar_back)
    protected LinearLayout ll_back;
    private MainFrameTask frameTask;//自定义的进入条
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private String phoneType;


    /**
     * get contentView's id
     *
     * @return
     */
    protected abstract int getContentView();

    /**
     * load data from sever
     */
    protected abstract void loadData();

    /**
     * state bar's color
     *
     * @return
     */
    protected int setStatusBarColor() {
        return R.color.main_style_color;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.hybunion.yirongma.payment.utils.LogUtil.d("========= 当前进入 ========  " + this.getClass().getSimpleName());
        StatusBarCompat.compat(this, getResources().getColor(R.color.main_color2));
        if (getContentView() != 0) {
            initTransparencyTitle();

            setContentView(getContentView());
            rl_progress = (RelativeLayout) findViewById(R.id.rl_progress);
            ButterKnife.bind(this);
            //自定义的进入条
            frameTask = new MainFrameTask(this);
            phoneType = android.os.Build.BRAND;
            if(phoneType.toUpperCase().equals("VIVO") || phoneType.toUpperCase().equals("OPPO")){
                Log.i("手机型号",phoneType.toUpperCase());
                String phoneNum = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.LOGIN_NUMBER);
                CrashReport.postCatchedException(new Throwable("调用屏幕常亮，手机号："+phoneNum));
                powerManager = (PowerManager)this.getSystemService(this.POWER_SERVICE);
                wakeLock = this.powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Lock");
            }


            initView();
            initData();
            if (savedInstanceState == null) {
                loadData();
            }
        }
    }

    private void initTransparencyTitle() {
        initTransparencyTitle(0);
    }

    public void initTransparencyTitle(int color) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
//            Window window = getWindow();
//
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            View statusBarView = new View(this);
//            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(BaseActivity.this, 25));
//            if (color == 0) {
//                statusBarView.setBackgroundColor(getResources().getColor(setStatusBarColor()));
//            } else {
//                statusBarView.setBackgroundColor(getResources().getColor(color));
//            }
//
//            ViewGroup view = (ViewGroup) getWindow().getDecorView();
//            statusBarView.setLayoutParams(lParams);
//            view.addView(statusBarView);
//            for (int i = 0; i < view.getChildCount(); i++) {
//                Log.i("hxs", view.getChildAt(i).toString());
//            }
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            if (color == 0) {
//                window.setStatusBarColor(getResources().getColor(setStatusBarColor()));//calculateStatusColor(Color.WHITE, (int) alphaValue)
//            } else {
//                window.setStatusBarColor(getResources().getColor(color));
//            }
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    protected void addFragment(int containerViewId, Fragment fragment) {
//        FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
//        fragmentTransaction.add(containerViewId, fragment);
//        fragmentTransaction.commit();
//    }

    /**
     * show a view with a progress bar
     */
    @Override
    public void showLoading() {
        if (!this.isFinishing())
            frameTask.startProgressDialog("");

//        rl_progress.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);
    }

    /**
     * hide a loading view
     */
    @Override
    public void hideLoading() {
        if (!this.isFinishing())
            frameTask.stopProgressDialog();
//        rl_progress.setVisibility(View.GONE);
//        setProgressBarIndeterminateVisibility(false);
    }

    /**
     * Show a retry view in case of an error when retrieving data.
     */
    @Override
    public void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    /**
     * Hide a retry view
     */
    @Override
    public void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    /**
     * Show an error message
     *
     * @param message A string representing an error.
     */
    @Override
    public void showError(String message) {
        showToastMessage(message);
        if (rl_progress != null) {
            this.rl_progress.setVisibility(View.GONE);
        }
    }

    @Override
    public Context context() {
        return this;
    }

    protected void showToastMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Nullable
    @OnClick(R.id.ll_titlebar_back)
    public void onClickBack() {
        this.finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


    /**
     * 点击空白处，隐藏软键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Nullable
    @OnClick(R.id.title_image)
    protected void onClickRightImg() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(phoneType.toUpperCase().equals("VIVO") || phoneType.toUpperCase().equals("OPPO")){
            wakeLock.acquire();
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
        if (!CommonUtil.isAppOnForeground(this)) {
            // 需要唤醒屏幕
            SharedPreferencesUtil.getInstance(this).putKey(SharedPConstant.NEED_WAKE_UP, 1);
        }
    }

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
                        ToastUtil.show("相机权限未申请，请去应用管理-权限中开启权限");
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
                        ToastUtil.show("存储权限未申请，请去应用管理-权限中开启权限");
                    }
                }
                break;
            case YrmUtils.REQUEST_PERMISSION_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                    if (!showRequestPermission) {//false为未申请
                        ToastUtil.show("电话状态未申请，请去应用管理-权限中开启权限");
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
