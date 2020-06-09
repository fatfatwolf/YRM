package com.hybunion.yirongma.payment.model;

import android.webkit.JavascriptInterface;

/**
 * @author SunBingbing
 * @date 2017/9/1
 * @email freemars@yeah.net
 * @description
 */
public class JavaScriptInterface {
    public OnJsCallBack onJsCallBack;
    public JavaScriptInterface(OnJsCallBack onJsCallBack) {
        this.onJsCallBack = onJsCallBack;
    }

    @JavascriptInterface
    public  void callNative(String version){
        onJsCallBack.onCallNative(version);
    }
    public  interface OnJsCallBack {
        void onCallNative(String value);
    }
}