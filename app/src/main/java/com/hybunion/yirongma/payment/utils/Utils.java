package com.hybunion.yirongma.payment.utils;

import android.app.Application;
import android.content.Context;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("You can't initiate me...");
    }

    private static Application sApplication;

    public static void init(Application application) {
        sApplication = application;
    }

    public static Application getApp() {
        if (sApplication == null) {
            // 未在Application中调用Utils.init(application)初始化Utils
            throw new NullPointerException("You should init first...");
        }
        return sApplication;
    }

    public static Context getContext() {
        return getApp().getApplicationContext();
    }

}
