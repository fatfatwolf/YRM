package com.hybunion.yirongma.payment.utils;

/**
 * Created by guoliang on 2018/2/3.
 */
public final class ConvertUtils {

    private ConvertUtils() {
        throw new UnsupportedOperationException("You can't initiate me...");
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / Utils.getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int dip2px(float dipValue) {
        return (int) (dipValue * Utils.getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int px2sp(float pxValue) {
        return (int) (pxValue / Utils.getContext().getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }

    public static int sp2px(float spValue) {
        return (int) (spValue * Utils.getContext().getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
}
