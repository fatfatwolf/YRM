package com.hybunion.yirongma.common.util.huawei.common;

import com.huawei.hms.support.api.push.TokenResult;
import com.hybunion.yirongma.common.util.huawei.common.handler.ICallbackCode;

/**
 * 回调线程
 */
public class CallbackCodeRunnable implements Runnable {

    private ICallbackCode handlerInner;
    private int rtnCodeInner;
    private TokenResult result;

    public CallbackCodeRunnable(ICallbackCode handler, int rtnCode) {
        handlerInner = handler;
        rtnCodeInner = rtnCode;
    }

    public CallbackCodeRunnable(ICallbackCode handler, int rtnCode, TokenResult result) {
        handlerInner = handler;
        rtnCodeInner = rtnCode;
        this.result = result;
    }

    @Override
    public void run() {
        if (handlerInner != null) {
            handlerInner.onResult(rtnCodeInner,result);
        }
    }
}