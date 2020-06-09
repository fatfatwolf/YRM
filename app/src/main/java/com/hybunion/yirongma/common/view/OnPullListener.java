package com.hybunion.yirongma.common.view;

import android.view.View;

/**
 * Created by Cmad on 2015/5/12.
 */
public interface OnPullListener {
    void onPulling(View headview, int currentTop);
    void onCanRefreshing(View headview);
    void onRefreshing(View headview);
}
