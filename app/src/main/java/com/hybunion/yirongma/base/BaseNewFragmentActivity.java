package com.hybunion.yirongma.base;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.util.CommonUtil;
import com.hybunion.yirongma.common.util.shortcutbadger.ShortcutBadgeException;
import com.hybunion.yirongma.common.util.shortcutbadger.ShortcutBadger;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.view.MyDialogView;

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
public class BaseNewFragmentActivity extends FragmentActivity {

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
    /**
     * 非静态内部类会默认持有外部类的引用，为了防止内存泄漏所以使用静态内部类，
     * 并且使用弱引用来回收activity.
     */
    public static   class MyHndler extends android.os.Handler{
        WeakReference<BaseNewFragmentActivity> softReference;
        public MyHndler(BaseNewFragmentActivity baseActivity){
            softReference=new WeakReference<BaseNewFragmentActivity>(baseActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseNewFragmentActivity baseActivity = softReference.get();
            if(baseActivity!=null){
                // 更新ui
                switch (msg.what){
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
                        baseActivity.isActive=false;
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, getResources().getColor(R.color.main_color2));
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayWidth = dm.widthPixels; // 得到宽度
        displayHeight = dm.heightPixels; // 得到高度
        LayoutInflater li = LayoutInflater.from(this);
        activity_dialog = getLayoutInflater().inflate(R.layout.activity_dialog_icon, null);
        myDialogView = (MyDialogView) activity_dialog.findViewById(R.id.myDialog);
        mProgressDialog = (RelativeLayout) li.inflate(R.layout.dialog_progressbar, null);
        title = (TextView) mProgressDialog.findViewById(R.id.tv_loading_title);
        HRTApplication.getInstance().addActivity(this);
//        AnalyticsConfig.enableEncrypt(true);
        initView();
        initData();
    }

    protected void initView() {

    }

    protected void initData() {

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
        if(toast != null){
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


        isActive = true;
        //清除角标
        try {
            ShortcutBadger.removeCountOrThrow(this);
            SharedPreferences sp = this.getSharedPreferences("jPush", MODE_PRIVATE);
            sp.edit().putInt("badgeCount",0).commit();
        } catch (ShortcutBadgeException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!CommonUtil.isAppOnForeground(this)) {
            LogUtils.d("BaseAty_onStop");
            //app 进入后台
//            handler.sendEmptyMessageDelayed(2,10*1000);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
 