package com.hybunion.net.http;

import android.content.Context;

import com.hybunion.net.remote.Subscriber;

public class RequestImpl {

    private static RequestImpl request = null;

    private HYBUnionAsyncHttpEngine httpEngine;

    private RequestImpl(Context context) {
        httpEngine = new HYBUnionAsyncHttpEngine(context);
    }

    public void setSubscriber(Subscriber subscriber) {
        httpEngine.setSubscriber(subscriber);
    }

    public static RequestImpl getInstance(Context context) {
        if (request == null) {
            request = new RequestImpl(context);
        }
        return request;
    }
}
