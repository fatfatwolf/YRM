package com.hybunion.yirongma.payment.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.util.DateCompareUtil;
import com.hybunion.yirongma.common.util.DateSetUtil;
import com.hybunion.yirongma.common.util.GetResourceUtil;
import com.hybunion.yirongma.common.view.MySwipe;
import com.hybunion.yirongma.payment.bean.CashTransactionInfoBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.payment.adapter.CashTransationAdapter;
import com.hybunion.yirongma.payment.utils.SavedInfoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @description: 提现记录
 * @author: luyafeng
 * @data: 2017/6/13.
 */

public class CashWithDrawRecordActivity extends BaseActivity {

    @Bind(R.id.start_date)
    TextView tv_start_date; //起始时间
    @Bind(R.id.end_date)
    TextView tv_end_date; //结束时间

    @Bind(R.id.tv_no_data)
    TextView tv_no_data; //无数据

    @Bind(R.id.lv_record_data)
    MySwipe mySwipe;
    @Bind(R.id.lv_dish)
    ListView lv_data;
    private int page = 1;
    private int rows = 10;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Date startTimeDate, endTimeDate, tempDate;

    private CashTransationAdapter recordAdapter;
    private String total; //请求数据的总条数
    private String dataStart, dataEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cash_transaction);
        ButterKnife.bind(this);
        initViews();
        initDatas();
        initListener();
    }

    private void initViews() {
        // 获取当前日期
        Calendar mCalendar = Calendar.getInstance();
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        mCalendar.set(Calendar.DATE, mCalendar.get(Calendar.DATE) - 6);
        Date nowTime = mCalendar.getTime();
        dataEnd = DateFormat.format(curDate);
        dataStart = DateFormat.format(nowTime);

        tv_start_date.setText(dataStart);
        tv_end_date.setText(dataEnd);

    }

    private void initDatas() {
        getRecordDataRequest();
    }

    private void initListener() {
        mySwipe.setChildView(lv_data);
        mySwipe.addFooterView();
        //上拉时操作
        mySwipe.setOnLoadListener(mySwipe.new MyLoad(this) {
            @Override
            public void onLoad() {
                super.onLoad();

                page++;
                getRecordDataRequest();

            }

            @Override
            public void onLoadEnd() {
                super.onLoadEnd();
                mySwipe.clearFootAnimation();
            }
        });
        //刷新时操作
        mySwipe.startOnRefresh(new MySwipe.MyOnRefresh() {
            @Override
            public void onRefresh() {
                page = 1;
                getRecordDataRequest();
            }
        });

        recordAdapter = new CashTransationAdapter(this);
        lv_data.setAdapter(recordAdapter);

    }

    private void getRecordDataRequest() {
        String url = NetUrl.WITHDRAW_RECORD_LIST;
        Map<String, String> map = new HashMap<>();
        map.put("mid", SavedInfoUtil.getMid(this));
        map.put("page", String.valueOf(page));
        map.put("rows", String.valueOf(rows));
        map.put("startdate", tv_start_date.getText().toString().trim());
        map.put("enddate", tv_end_date.getText().toString().trim());
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    LogUtil.d("lyf====res:" + response);
                    total = Result(response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.optBoolean("success");
                    if (success) {
                        String msg = jsonObject.optString("msg");
                        if (msg.contains("无提现记录") && page != 1) {
                            mySwipe.setRefreshing(false);
                            mySwipe.setLoading(false);
                            mySwipe.loadAllData();
                        }
                        if ((msg.contains("无提现记录") || "0".equals(total)) && page == 1) {
                            tv_no_data.setVisibility(View.VISIBLE);
                            mySwipe.setVisibility(View.GONE);
                            lv_data.setVisibility(View.GONE);
                            ToastUtil.show("无提现记录");
                            return;
                        } else {
                            tv_no_data.setVisibility(View.GONE);
                            mySwipe.setVisibility(View.VISIBLE);
                            lv_data.setVisibility(View.VISIBLE);
                        }
                        try {
                            Gson gson = new Gson();
                            JSONArray jsonArray = jsonObject.optJSONArray("obj");
                            List<CashTransactionInfoBean.ObjBean> transInfo = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<CashTransactionInfoBean.ObjBean>>() {
                            }.getType());
                            if (transInfo.size() == Integer.parseInt(total)) { //一页
                                mySwipe.setRefreshing(false);
                                mySwipe.setLoading(false);
                                mySwipe.loadAllData();
                            } else {
                                if ((transInfo.size() + recordAdapter.data.size()) < Integer.parseInt(total)) { //多次加载
                                    mySwipe.setLoading(false);
                                    mySwipe.setRefreshing(false);
                                    mySwipe.resetText();
                                } else {
                                    mySwipe.setRefreshing(false);
                                    mySwipe.setLoading(false);
                                    mySwipe.loadAllData();
                                }
                                if (transInfo.size() < Integer.parseInt(total)) { //第一次加载
                                    mySwipe.setLoading(false);
                                    mySwipe.setRefreshing(false);
                                    mySwipe.resetText();
                                }
                            }
                            if (page == 1) {
                                recordAdapter.clearData();
                            }
                            if (transInfo != null && transInfo.size() != 0) {

                                recordAdapter.addData(transInfo);
                                recordAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            LogUtil.d("lyf---error:" + e.toString());
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.show("网络连接不佳");

            }
        };
        VolleySingleton.getInstance(this).addMap(listener, errorListener, map, url);

    }

    public String Result(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("countTxnAmount")) {
                return jsonObject.getString("countTxnAmount");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "0";
        }
        return "0";
    }


    @OnClick(R.id.iv_back)
    public void close() {
        finish();
    }

    //确定按钮
    @OnClick(R.id.btn_commit)
    public void commitData() {
        String startDate = tv_start_date.getText().toString().trim();
        String endDate = tv_end_date.getText().toString().trim();
        if (startDate != null && endDate != null && setDatetime()) {
            getRecordDataRequest();
        }
    }

    //起始时间
    @OnClick(R.id.start_date)
    public void startTime() {
        DateSetUtil.setDate(this, tv_start_date, tv_end_date, 1, new DateSetUtil.DatePickerCallback() {
            @Override
            public void loadData() {

            }

            @Override
            public void showErrorMessage() {
                if(!DateCompareUtil.futureTime){
                    ToastUtil.show(GetResourceUtil.getString(R.string.dateError));
                }
            }
        });
    }


    //结束时间
    @OnClick(R.id.end_date)
    public void endTime() {
        DateSetUtil.setDate(this, tv_end_date, tv_start_date, 0, new DateSetUtil.DatePickerCallback() {
            @Override
            public void loadData() {
            }
            @Override
            public void showErrorMessage() {
                if(!DateCompareUtil.futureTime){
                    ToastUtil.show(GetResourceUtil.getString(R.string.dateError));
                }
            }
        });
    }
    private boolean setDatetime() {
        try {
            startTimeDate = sdf.parse(tv_start_date.getText().toString().trim());
            endTimeDate = sdf.parse(tv_end_date.getText().toString().trim());
            Calendar tempCal = Calendar.getInstance();
            tempCal.setTime(endTimeDate);
            tempCal.add(Calendar.DATE, -6);
            tempDate = tempCal.getTime();
            if (startTimeDate.compareTo(tempDate) < 0) {
                Toast.makeText(this, "请求时间不能超过7天", Toast.LENGTH_SHORT).show();
                return false;
            } else if (startTimeDate.compareTo(endTimeDate) > 0) {
                Toast.makeText(this, "起始时间不能大于截止时间", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}
