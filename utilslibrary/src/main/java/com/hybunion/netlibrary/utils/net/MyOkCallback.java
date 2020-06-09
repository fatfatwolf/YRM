package com.hybunion.netlibrary.utils.net;

public interface MyOkCallback<T> {

    void onStart();

    void onSuccess(T t);

    void onError(Exception e);

    void onFinish();

    Class getClazz();

}
