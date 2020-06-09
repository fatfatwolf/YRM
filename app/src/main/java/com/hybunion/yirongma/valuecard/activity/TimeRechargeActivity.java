package com.hybunion.yirongma.valuecard.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ${TJUNJIE} on 2015/5/30.
 * 储值卡-报表-时段充值率表
 */
public class TimeRechargeActivity extends BaseActivity implements View.OnClickListener {
    private EditText etTime1,etTime2,etTime3,etTime4,etTime5;
    private LinearLayout ib_back;
    private PieChart mChart;
    private  int rechargeTime1,rechargeTime2,rechargeTime3,rechargeTime4,rechargeTime5;
    private Button btnReset,btnSumbit;
    private PieDataSet pieDataSet;
    private final String TYPE = "type";
    private int type = 100;
    private TextView text;
    private Typeface tf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_recharge);
        ib_back = (LinearLayout) findViewById(R.id.ib_back);
        ib_back.setOnClickListener(this);

        mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        etTime1 = (EditText) findViewById(R.id.et_time1);
        etTime2 = (EditText) findViewById(R.id.et_time2);
        etTime3 = (EditText) findViewById(R.id.et_time3);
        etTime4 = (EditText) findViewById(R.id.et_time4);
        etTime5 = (EditText) findViewById(R.id.et_time5);
        text = (TextView) findViewById(R.id.value_card_time_recharge_title_text);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnSumbit = (Button) findViewById(R.id.btn_submit);
        btnReset.setOnClickListener(this);
        btnSumbit.setOnClickListener(this);
        Intent intent = getIntent();
        type = intent.getIntExtra(TYPE, 100);
        if (type == 100) {
            this.finish();
            return;
        }
        if (type == 0) {
            text.setText(R.string.value_card_time_consume_table);
        }
      //  compare();
      int[] defaultArray = {4,8, 12,16, 20};
      timeRecharge(defaultArray);
    }
    private boolean compare() {
        Toast toast = null;
        if(TextUtils.isEmpty(etTime1.getText().toString().trim())
                ||TextUtils.isEmpty(etTime2.getText().toString().trim())
                ||TextUtils.isEmpty(etTime3.getText().toString().trim())
                ||TextUtils.isEmpty(etTime4.getText().toString().trim())
                ||TextUtils.isEmpty(etTime5.getText().toString().trim()))
        {
            toast = Toast.makeText(TimeRechargeActivity.this,R.string.not_null,Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return  false;
        }
        rechargeTime1 = Integer.parseInt(etTime1.getText().toString().trim());
        rechargeTime2 = Integer.parseInt(etTime2.getText().toString().trim());
        rechargeTime3 = Integer.parseInt(etTime3.getText().toString().trim());
        rechargeTime4 = Integer.parseInt(etTime4.getText().toString().trim());
        rechargeTime5 = Integer.parseInt(etTime5.getText().toString().trim());

        if(rechargeTime1>=rechargeTime2 || rechargeTime2>=rechargeTime3||rechargeTime3>=rechargeTime4||rechargeTime4>=rechargeTime5
           ||rechargeTime5>=25) {
            toast = Toast.makeText(TimeRechargeActivity.this,R.string.illegal_input,Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            return  false;
        }
        return true;
    }

        private void save() {

        int[] currentArray = {rechargeTime1 ,rechargeTime2, rechargeTime3, rechargeTime4, rechargeTime5};
        timeRecharge(currentArray);
    }


    private void clearData() {
        etTime1.setText("");
        etTime2.setText("");
        etTime3.setText("");
        etTime4.setText("");
        etTime5.setText("");
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);
        pieChart.setHoleRadius(40f);  //半径 中间空心部分圆的半径
        pieChart.setTransparentCircleRadius(30f); // 半透明圈
        //pieChart.setHoleRadius(0); //实心圆
       // pieChart.setDescription("单位:%");
       //  pieChart.setDescription(TimeRechargeActivity.this.getResources().getString(R.string.unit_percent));
        pieChart.setDescription(text.getText().toString());
        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);


        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

      //  pieChart.setCenterText("Time Value");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();


        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(3f);
/***
 *  Legend l = mChart.getLegend();
 l.setPosition(LegendPosition.RIGHT_OF_CHART);
 l.setXEntrySpace(7f);
 l.setYEntrySpace(0f);
 l.setYOffset(0f);
 */
        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     *
     *  分成几部分
     *  只展示非零的数据，如果数据为0则不统计
     */
    private PieData getPieData(String[] intArray,float[] intArray1) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        for (int i = 0; i < intArray.length; i++) {
            if(intArray1[i]!=0) {
                xValues.add(String.valueOf(intArray[i]));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
            }
        }
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        for(int i=0;i<intArray1.length;i++){
            if (intArray1[i]!=0){
                yValues.add(new BarEntry(intArray1[i], i));
            }
        }
        System.out.println("value = " + yValues.toString());
        //y轴的集合
        pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setSelectionShift(5f);
        mChart.setDrawCenterText(true);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(149, 253, 64));
        colors.add(Color.rgb(255, 126, 0));
        colors.add(Color.rgb(252, 255, 25));
        colors.add(Color.rgb(254, 57, 86));
        colors.add(Color.rgb(255, 58, 136));
        colors.add(Color.rgb(0, 203, 239));



        PieData data = new PieData(xValues, pieDataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tf);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();


        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:
                finish();
            break;
            case R.id.btn_reset:
                clearData();
            break;
            case R.id.btn_submit:
                if(compare())
                    save();
            break;
        }
    }
/***
 * 获取后台时间段信息
 */
  private void timeRecharge(int[] time) {
      showProgressDialog("");
    final RequestQueue mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();
    JSONObject timeRechargeJson= new JSONObject();
      int len = time.length;
      if(len <= 0)
          return;
    try {
        timeRechargeJson.put("merId", SharedPreferencesUtil.getInstance(getApplicationContext()).getKey("merchantID"));
        timeRechargeJson.put("type",type);
        for(int i = 1; i <= len; i ++){
            timeRechargeJson.put("time" + i,time[i-1]);
        }

    }catch (Exception e){
        return;
    }
      Response.Listener listener=   new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    hideProgressDialog();
                    String status = "",message = "";
                    LogUtils.dlyj(jsonObject+"返回数据");
                    try{
                        status = jsonObject.getString("status");
                        message = jsonObject.getString("message");

                        if ("1".equals(status)){
                            JSONArray array = jsonObject.getJSONArray("legend");
                            String[] intArray = new String[array.length()];
                            for (int i = 0; i < array.length(); i++) {
                                String n = array.getString(i);
                                intArray[i] = n;
                            }
                            JSONArray totalarray = jsonObject.getJSONArray("total");
                            float[] intArray1 = new float[totalarray.length()];
                            for (int i = 0; i < totalarray.length(); i++){
                                float m =(float)totalarray.getDouble(i);
                                intArray1[i] =m;
                            }
                            LogUtils.dlyj(intArray.toString()+"===="+intArray1.toString()+"总和");
                            PieData mPieData = getPieData(intArray,intArray1);
                            showChart(mChart, mPieData);
                        }else {
                            Toast.makeText(TimeRechargeActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        }catch (Exception e){
                            e.printStackTrace();
                            return;
                        }
                }
            };
      Response.ErrorListener errorListener=  new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            hideProgressDialog();
            Toast.makeText(TimeRechargeActivity.this,getString(R.string.poor_network), Toast.LENGTH_SHORT).show();
        }
    };
      VolleySingleton.getInstance(this).addJsonObjectRequest(listener, errorListener, timeRechargeJson, NetUrl.REPORT_TIME_FORM);
}

}
