package com.hybunion.yirongma.valuecard.activity;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

/**
 * 表格的坐标数据格式化器
 */
public class MyValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;
    
    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }
    
    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + "¥";
    }

}
