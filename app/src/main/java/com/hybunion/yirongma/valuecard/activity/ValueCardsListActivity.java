package com.hybunion.yirongma.valuecard.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.ValueCardsListBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.valuecard.adapter.ValueCardsListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2018/1/9.
 */

public class ValueCardsListActivity extends BasicActivity {
    @Bind(R.id.tv_titlebar_back_title)
    TextView title;
    @Bind(R.id.lv_value_card)
    ListView lvlist;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    String phone,memId;
    private int page = 0;
    ValueCardsListAdapter valueCardsListAdapter;
    public static ValueCardsListActivity instance = null;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }
    @Override
    protected int getContentView() {
        return R.layout.value_cards_list_activity;
    }

    @Override
    public void initView() {
        instance=this;
        title.setText("");
        phone = getIntent().getStringExtra("phone");
        title.setText(phone);
        valueCardsListAdapter = new ValueCardsListAdapter(ValueCardsListActivity.this);
        lvlist.setAdapter(valueCardsListAdapter);
        lvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ValueCardsListActivity.this,ValueCardsDetailActivity.class);
                intent.putExtra("cardNo",valueCardsListAdapter.valueCardBean.get(position).getCardNo());
                intent.putExtra("cardName",valueCardsListAdapter.valueCardBean.get(position).getCardName());
                intent.putExtra("cardBalace",valueCardsListAdapter.valueCardBean.get(position).getBalance());
                intent.putExtra("cardType",valueCardsListAdapter.valueCardBean.get(position).getCardType());
                intent.putExtra("cardExpireDate",valueCardsListAdapter.valueCardBean.get(position).getExpireDate());
                intent.putExtra("discountRate",valueCardsListAdapter.valueCardBean.get(position).getDiscountRate());
                intent.putExtra("memId",memId);
                intent.putExtra("phone",phone);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getValueCard(phone,page);
    }

    public void getValueCard(String phone, final int page) {
        String url = NetUrl.QUERY_VALUE_CARD;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
            jsonRequest.put("phone",phone);
            jsonRequest.put("page",String.valueOf(page));
            jsonRequest.put("pages","1000");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(ValueCardsListActivity.this, url, jsonRequest, new MyOkCallback<ValueCardsListBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ValueCardsListBean bean) {
                String status = bean.getStatus();
                String msg = bean.getMsg();
                memId = bean.getMemId();
                if("0".equals(status)){
                    if (null != bean.getData()) {
                        List<ValueCardsListBean.DataBean> list = bean.getData();
                        if (null != list && list.size() > 0) {
                            tv_nodata.setVisibility(View.INVISIBLE);
                            if (page==0){
                                valueCardsListAdapter.valueCardBean.clear();
                            }
                            valueCardsListAdapter.valueCardBean.addAll(list);
                            valueCardsListAdapter.notifyDataSetChanged();
                        }else {
                            tv_nodata.setVisibility(View.VISIBLE);
                            valueCardsListAdapter.clearData();
                        }
                    }
                }else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return ValueCardsListBean.class;
            }
        });

    }


    @OnClick(R.id.ll_titlebar_back)
    public void goBack() {
        finish();
    }
}
