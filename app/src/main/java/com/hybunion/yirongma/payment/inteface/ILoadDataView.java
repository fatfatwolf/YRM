package com.hybunion.yirongma.payment.inteface;

import android.content.Context;

/**
 * Created by king on 2016/6/17.
 */

public interface ILoadDataView {

    void showLoading();

    void hideLoading();

    void showRetry();

    void hideRetry();

    void showError(String message);

    Context context();

}
