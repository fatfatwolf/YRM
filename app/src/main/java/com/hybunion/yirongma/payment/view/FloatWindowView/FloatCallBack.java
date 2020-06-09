package com.hybunion.yirongma.payment.view.FloatWindowView;

import com.hybunion.yirongma.payment.view.FloatWindowView.view.FloatLayout;

/**
 * Author:xishuang
 * Date:2017.08.01
 * Des:暴露一些与悬浮窗交互的接口
 */
public interface FloatCallBack {
//    void guideUser(int type);

    void show();

    void hide();

    void floatClick(FloatLayout.OnFloatClickListener listener);

//    void addObtainNumer();

//    void setObtainNumber(int number);
}
