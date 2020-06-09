package com.hybunion.yirongma.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.util.CommonUtil;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lcy on 2015/12/21.
 */
public class BaseFragmentActivity extends AutoLayoutActivity {

    private TextView title;
    private RelativeLayout mProgressDialog;
    private boolean isActive = true;
    private Timer timer;
    private TimerTask task;
    private  MyHandler myHandler = new MyHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, getResources().getColor(R.color.lmf_main_color));
        //initTransparencyTitle();
        LayoutInflater li = LayoutInflater.from(this);
        mProgressDialog = (RelativeLayout) li.inflate(R.layout.dialog_progressbar, null);
        title = (TextView) mProgressDialog.findViewById(R.id.tv_loading_title);
    }
    protected void initTransparencyTitle() {
        initTransparencyTitle(0);
    }
    protected void initTransparencyTitle(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            Window window = this.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View statusBarView = new View(this);
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(this, 25));
            if (color == 0) {
                statusBarView.setBackgroundColor(getResources().getColor(setStatusBarColor()));
            } else {
                statusBarView.setBackgroundColor(getResources().getColor(color));
            }

            ViewGroup view = (ViewGroup) this.getWindow().getDecorView();
            statusBarView.setLayoutParams(lParams);
            view.addView(statusBarView);
            for (int i = 0; i < view.getChildCount(); i++) {
                LogUtils.d("hxs", view.getChildAt(i).toString());
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (color == 0) {
                window.setStatusBarColor(getResources().getColor(setStatusBarColor()));//calculateStatusColor(Color.WHITE, (int) alphaValue)
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
        return R.color.lmf_main_color;
    }
    public void showProgressDialog(String titleString) {
        if (titleString != null) {
            // mProgressDialog.setMessage(title);
            title.setText(titleString);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) mProgressDialog.getParent();
                if (parent != null) {
                    parent.removeView(mProgressDialog);
                }
                ViewGroup top = (ViewGroup) getWindow().getDecorView();
                top.addView(mProgressDialog);
            }
        });
    }

    public void hideProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) mProgressDialog.getParent();
                if (parent != null) {
                    parent.removeView(mProgressDialog);
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 友盟数据统计代码
        myHandler.removeMessages(0);
        isActive = true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 友盟数据统计功能
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (!CommonUtil.isAppOnForeground(this)) {
            LogUtils.d("BaseFrag_onStop");
            //app 进入后台
//            myHandler.sendEmptyMessageDelayed(0,5 * 60 * 1000);
        }
    }

    public static class MyHandler extends Handler{
        WeakReference<BaseFragmentActivity> weakReference ;
        MyHandler(BaseFragmentActivity baseFragmentActivity){
            weakReference = new WeakReference<>(baseFragmentActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            weakReference.get().isActive = false;
        }
    }
}
