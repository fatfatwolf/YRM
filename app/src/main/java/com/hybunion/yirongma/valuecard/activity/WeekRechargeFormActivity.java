package com.hybunion.yirongma.valuecard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ${TJUNJIE} on 2015/5/29.
 * 储值卡-报表-星期充值率报表
 */
public class WeekRechargeFormActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ib_back;
    private BarChart mBarChart;
    private BarData mBarData;
    private final String TYPE = "type";
    private int type = 100;
    private TextView weekText;
    private Typeface mTf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_recharge);
        ib_back= (LinearLayout) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);
        weekText = (TextView) findViewById(R.id.value_card_week_recharge_title_text);
        mBarChart = (BarChart) findViewById(R.id.spread_bar_chart);
        mBarChart.setDrawGridBackground(false);

        ValueFormatter custom = new MyValueFormatter();
        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8,true);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8,true);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        Intent intent = getIntent();
        type = intent.getIntExtra(TYPE, 100);
        if (type == 100) {
            this.finish();
            return;
        }
        if (type == 0) {
            weekText.setText(R.string.value_card_week_consume_table);
        }
        weekRecharge();
    }

    private void showBarChart(BarChart barChart, BarData barData)
     {
        barChart.setDrawBorders(false);  ////是否在折线图上添加边框

        barChart.setDescription("");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription("You need to provide data for the chart.");

        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        barChart.setTouchEnabled(true); // 设置是否可以触摸

        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放

        barChart.setPinchZoom(false);//

//      barChart.setBackgroundColor();// 设置背景

        barChart.setDrawBarShadow(true);

        barChart.setData(barData); // 设置数据

        Legend mLegend = barChart.getLegend(); // 设置比例图标示
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色
        mLegend.setTextSize(11f);
        mLegend.setXEntrySpace(4f);

//      X轴设定
//      XAxis xAxis = barChart.getXAxis();
//      xAxis.setPosition(XAxisPosition.BOTTOM);

        barChart.animateX(2500); // 立即执行的动画,x轴
    }

     private BarData getBarData(float[] intArray)

     {
        ArrayList<String> xValues = new ArrayList<String>();

         String [] nums = new String [] {WeekRechargeFormActivity.this.getResources().getString(R.string.monday),
                 WeekRechargeFormActivity.this.getResources().getString(R.string.tuesday),
                 WeekRechargeFormActivity.this.getResources().getString(R.string.wednesday),
                 WeekRechargeFormActivity.this.getResources().getString(R.string.thursday),
                 WeekRechargeFormActivity.this.getResources().getString(R.string.friday),
                 WeekRechargeFormActivity.this.getResources().getString(R.string.saturday),
                 WeekRechargeFormActivity.this.getResources().getString(R.string.sunday)};

        for (int i = 0; i < nums.length; i++) {
            xValues.add(nums[i]);
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();

        for (int i = 0; i < intArray.length; i++) {
           // float value = (float) (Math.random() * range/*100以内的随机数*/) + 3;
            yValues.add(new BarEntry(intArray[i], i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, WeekRechargeFormActivity.this.getResources().getString(R.string.unit));
        barDataSet.setBarSpacePercent(35f);

        barDataSet.setColor(Color.rgb(114, 188, 223));

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSets);
         barData.setValueTextSize(10f);
         barData.setValueTypeface(mTf);

         mBarChart.setData(barData);

        return barData;
    }

    /***
     * 获取后台星期储值数值
     */

    private void weekRecharge() {
        showProgressDialog("奋力加载中...");
        final RequestQueue mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        JSONObject weekRechargeJson= new JSONObject();
        try {
            weekRechargeJson.put("merId", SharedPreferencesUtil.getInstance(getApplicationContext()).getKey("merchantID"));
            weekRechargeJson.put("type", type);
        }catch (Exception e){
            return;
        }
                Response.Listener listener= new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        hideProgressDialog();
                        String status = "",message = "";
                        try{
                            status = jsonObject.getString("status");
                            message = jsonObject.getString("message");
                            try {
                                JSONArray array = jsonObject.getJSONArray("total");
                                float[] intArray = new float[array.length()];
                                for (int i = 0; i < array.length(); i++){
                                    float n =(float)array.getDouble(i);
                                    intArray[i] = n;
                                    System.out.println("星期储值=" + n);
                                }
                                mBarData = getBarData(intArray);
                                showBarChart(mBarChart, mBarData);
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(WeekRechargeFormActivity.this,R.string.data_query_failed, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }catch (Exception e){
                            Toast.makeText(WeekRechargeFormActivity.this, R.string.returned_information_failed, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if("1".equals(status)){
                          //  Toast.makeText(WeekRechargeFormActivity.this, R.string.data_query_success, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(WeekRechargeFormActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                };
        Response.ErrorListener errorListener=   new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                hideProgressDialog();
                Toast.makeText(WeekRechargeFormActivity.this, getString(R.string.poor_network), Toast.LENGTH_SHORT).show();
            }
        };
        VolleySingleton.getInstance(this).addJsonObjectRequest(listener, errorListener, weekRechargeJson, NetUrl.REPORT_FORM);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                finish();
            break;
        }
    }
}
