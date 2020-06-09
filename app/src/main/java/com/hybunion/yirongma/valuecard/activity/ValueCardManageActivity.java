package com.hybunion.yirongma.valuecard.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.base.BaseActivity;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.common.util.GetResourceUtil;
import com.hybunion.yirongma.common.view.MySwipe;
import com.hybunion.yirongma.payment.utils.Constant;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.view.DialogF;
import com.hybunion.yirongma.common.net.VolleySingleton;
import com.hybunion.yirongma.valuecard.adapter.ValueCardManagerAdapter;
import com.hybunion.yirongma.valuecard.model.MerCardInfoItemBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：储值卡管理
 * 编写人： myy
 * 创建时间：2017/3/6
 */
public class ValueCardManageActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_head;
    private MySwipe mySwipe;
    private ListView listView;
    private TextView tv_nodata;
    private int page = 0;
    private String merchantID;//商户ID
    private ValueCardManagerAdapter mAdapter;
    private ImageView iv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valuecardmanage);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_head = (TextView) findViewById(R.id.tv_head);
        mySwipe = (MySwipe) findViewById(R.id.myswipe);
        listView = (ListView) findViewById(R.id.listcard);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        ll_back.setOnClickListener(this);
        tv_head.setText("卡管理");
        merchantID = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        showProgressDialog("");
        getData(0);
        handleList();
        // 长按下线卡片
        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
        iv_info = (ImageView) findViewById(R.id.right_iv);
        iv_info.setImageDrawable(GetResourceUtil.getDrawable(R.drawable.img_customer));
        iv_info.setVisibility(View.VISIBLE);
        iv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(ValueCardManageActivity.this);
//                builder.setMessage(GetResourceUtil.getString(R.string.delete_card_info));
//                builder.create().show();
//                HelpInfoDialog.Builder builder = new HelpInfoDialog.Builder(ValueCardManageActivity.this);
//                builder.setContent(GetResourceUtil.getString(R.string.delete_card_info));
//                builder.create().show();
                dialog();
            }
        });
    }

    private void dialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.vip_dialog, null);
        final DialogF builder = new DialogF(this, 0, 0, view,  0 ,0);
        //部分机型的标题有一根蓝线问题
        Context context = builder.getContext();
        int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = builder.findViewById(divierId);
        if(divider!=null) {
            divider.setBackgroundColor(Color.alpha(0));
        }
        builder.setCancelable(true);
        TextView tv_title,tv_content;
        tv_title = (TextView) view.findViewById(R.id.help_info_title);
        tv_content= (TextView) view.findViewById(R.id.tv);
        tv_title.setText(GetResourceUtil.getString(R.string.card_manage_help));
        tv_content.setText(GetResourceUtil.getString(R.string.delete_card_info));
        Button but = (Button) view.findViewById(R.id.cancel);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.show();
    }

    /**
     * 加载刷新监听
     */
    private void handleList() {
        mySwipe.setChildView(listView);
        mySwipe.addFooterView();
        mySwipe.setOnLoadListener(mySwipe.new MyLoad(this) {
            public void onLoad() {
                super.onLoad();
                getData(page);
            }

            @Override
            public void onLoadEnd() {
                mySwipe.clearFootAnimation();
                super.onLoadEnd();
            }
        });
        mySwipe.startOnRefresh(new MySwipe.MyOnRefresh() {
            @Override
            public void onRefresh() {
                page = 0;
                getData(page);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    /**
     * 卡管理列表
     *
     * @param page2 请求页码
     */
    private void getData(final int page2) {
        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                LogUtils.d("myy", "response" + response.toString());
                try {
                    String status = response.getString("status");
                    String message = response.getString("message");

                    JSONObject body = response.getJSONObject("body");
                    JSONArray data = null;
                    if (body != null) {
                        data = body.getJSONArray("data");
                    }

                    if ("0".equals(status)) {
                        mySwipe.setLoading(false);
                        mySwipe.setRefreshing(false);
                        String hasData = body.getString("hasNext");
                        if (null != data) {
                            Gson gson = new Gson();
                            List<MerCardInfoItemBean> list2 = gson.fromJson(data.toString(),
                                    new TypeToken<ArrayList<MerCardInfoItemBean>>() {
                                    }.getType());

                            if (page2 == 0) {
                                if (null == list2 || list2.size() == 0) {
                                    tv_nodata.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                            tv_nodata.setVisibility(View.GONE);

                            if (mAdapter == null) {
                               // mAdapter = new ValueCardManagerAdapter(ValueCardManageActivity.this);
                            }
                            if (page2 == 0) {
                                mAdapter.setList(list2);
                            } else {
                                mAdapter.addList(list2);
                            }
                            if (listView.getAdapter() == null) {
                                listView.setAdapter(mAdapter);
                            }
                            page++;
                            if ("0".equals(hasData)) {
                                mySwipe.resetText();
                            } else {
                                mySwipe.loadAllData();
                            }
                        } else {
                            tv_nodata.setVisibility(View.VISIBLE);
                        }
                    } else if ("2".equals(status)) {
                        mySwipe.setLoading(false);
                        mySwipe.setRefreshing(false);
                        if (page2 == 0) {
                            tv_nodata.setVisibility(View.VISIBLE);
                        } else {
                            mySwipe.loadAllData();
                        }
                    } else {
                        Toast.makeText(ValueCardManageActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorlistener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                hideProgressDialog();
                Toast.makeText(ValueCardManageActivity.this, "请稍后重试",
                        Toast.LENGTH_SHORT).show();
            }
        };
        JSONObject jsonRequest;
        try {
            JSONObject dataParam = new JSONObject();
            dataParam.put("merId", merchantID);
            dataParam.put("page", page + "");
            dataParam.put("pages", Constant.PAGE_SIZE + "");

            jsonRequest = new JSONObject();
            jsonRequest.put("body", dataParam);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, NetUrl.MERCARDINFO,
                    jsonRequest, listener, errorlistener);
            requestQueue.add(request);
            LogUtils.d("myy", "jsonRequest" + jsonRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue.start();
    }

}
