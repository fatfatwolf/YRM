package com.hybunion.yirongma.payment.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Jairus on 2018/7/26.
 */

public class NoScrollLlistView extends ListView {
    public NoScrollLlistView(Context context) {
        super(context);
    }

    public NoScrollLlistView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollLlistView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
