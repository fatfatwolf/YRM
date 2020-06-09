package com.hybunion.yirongma.valuecard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 完全展开的list
 * Created by myy on 2017/3/2.
 */
public class ExpandList extends ListView {

    public ExpandList(Context context) {
        super(context);
    }

    public ExpandList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}