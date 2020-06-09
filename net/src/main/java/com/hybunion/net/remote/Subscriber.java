package com.hybunion.net.remote;


/**
 * Created by king on 2016/6/19.
 */

public interface  Subscriber<T> {

    void onCompleted(T result);

    void onError(Throwable e);

    void onProcess(LoadingBean bean);

    Class<?> getType();

}
