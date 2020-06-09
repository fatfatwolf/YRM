package com.hybunion.netlibrary.utils.net;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

public class OkCallback<T> implements Callback {
    @Override
    public void onStart(Request request) {

    }

    @Override
    public void onSuccess(Response response) {

    }

    @Override
    public void onCacheSuccess(Response response) {

    }

    @Override
    public void onError(Response response) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadProgress(Progress progress) {

    }

    @Override
    public void downloadProgress(Progress progress) {

    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        return null;
    }
}
