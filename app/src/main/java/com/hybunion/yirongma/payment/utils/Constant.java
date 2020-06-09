package com.hybunion.yirongma.payment.utils;


import com.hybunion.yirongma.BuildConfig;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;

public class Constant {
    public static final boolean LOG_DEBUG = true;
    /**
     * 生成公网地址
     */

    public static boolean DUIZHANG_DEBUG = BuildConfig.LOG_DEBUG;

    public static int changed = Integer.valueOf(HRTApplication.getInstance().getApplicationContext().getResources().getString(R.string.changed));
    public static final int PAGE_SIZE = 20;
    public static int AGENT_ID = 0;
    public static int AGENT_ID_DIFF = Integer.valueOf(HRTApplication.getInstance().getApplicationContext().getResources().getString(R.string.AGENT_ID));






}

