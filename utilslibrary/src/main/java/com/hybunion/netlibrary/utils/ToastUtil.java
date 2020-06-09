package com.hybunion.netlibrary.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.hybunion.netlibrary.R;
import com.hybunion.netlibrary.UtilsLib;


/**
 * 单例的Toast
 */
public final class ToastUtil {

    private ToastUtil() {
    }

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static CharSequence sText;
    private static int sDuration;

    private static final class LazyHolder {
        private LazyHolder() {
        }

        private static final Toast TOAST_INSTANCE;
        private static final TextView VIEW_INSTANCE;

        static {
            VIEW_INSTANCE = (TextView) LayoutInflater.from(UtilsLib.getContext()).inflate(R.layout.toast_custom2, null);
            TOAST_INSTANCE = new Toast(UtilsLib.getContext());
            TOAST_INSTANCE.setDuration(Toast.LENGTH_LONG);
            TOAST_INSTANCE.setView(VIEW_INSTANCE);
            TOAST_INSTANCE.setGravity(Gravity.BOTTOM, 0, 500);
        }
    }

    private static TextView getViewInstance() {
        return LazyHolder.VIEW_INSTANCE;
    }

    private static Toast getToastInstance() {
        return LazyHolder.TOAST_INSTANCE;
    }

    public static void showShortToast(CharSequence text) {
        sText = text;
        sDuration = Toast.LENGTH_SHORT;
        show();
    }

    public static void showShortToast(@StringRes int resId) {
        sText = UtilsLib.getContext().getString(resId);
        sDuration = Toast.LENGTH_SHORT;
        show();
    }

    public static void showLongToast(CharSequence text) {
        sText = text;
        sDuration = Toast.LENGTH_LONG;
        show();
    }

    public static void showLongToast(@StringRes int resId) {
        sText = UtilsLib.getContext().getString(resId);
        sDuration = Toast.LENGTH_LONG;
        show();
    }

    /**
     * 安全地显示Toast(可在子线程调用)
     */
    private static void show() {
        if (isEmpty(sText)) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    getViewInstance().setText(sText);
                    getToastInstance().setDuration(sDuration);
                    getToastInstance().show();
                }
            });
        } else {
            getViewInstance().setText(sText);
            getToastInstance().setDuration(sDuration);
            getToastInstance().show();
        }
    }

    public static void cancel() {
        getToastInstance().cancel();
    }

    private static boolean isEmpty(CharSequence text) {
        return text == null || TextUtils.isEmpty(text.toString().trim());
    }
}
