package com.hybunion.yirongma.payment.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hybunion.yirongma.R;


/**
 * 自定义的显示在屏幕中间的Toast
 * Created by hxs on 2016/7/1.
 */
public class HRTToast {

    private static Toast mToast;

    private HRTToast() {
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void showToast(String text, Context context, int duration) {
        Context appContext;
        try {
            appContext = context.getApplicationContext();
        } catch (Exception e) {
            appContext = context;
        }

        mToast = new Toast(appContext);
        mToast.setDuration(Toast.LENGTH_LONG);
        View view = View.inflate(appContext, R.layout.toast_custom, null);
        TextView tv_toast = (TextView) view.findViewById(R.id.tv_toast);
        tv_toast.setText(text);
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setDuration(duration);
        mToast.show();

    }

    public static void showToast(int id, Context context, int duration) {
        String strtext = context.getResources().getString(id);
        showToast(strtext, context, duration);
    }

    public static void showToast(String text, Context context) {
        showToast(text, context, Toast.LENGTH_LONG);
    }

    public static void showToast(int id, Context context) {
        showToast(context.getResources().getString(id), context, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context,String text) {
        showToast(text, context);
    }

    /**
     * toast取消
     */
    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }
}
