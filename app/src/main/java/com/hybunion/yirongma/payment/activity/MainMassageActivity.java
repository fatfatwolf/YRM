package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MainMassageBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.MainMassageAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by admin on 2018/2/27.
 */

public class MainMassageActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.ll_titlebar_back)
    LinearLayout ll_titlebar_back;
    @Bind(R.id.tv_titlebar_back_title)
    TextView title;
    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    @Bind(R.id.ll_gone_clerk)
    LinearLayout ll_gone_clerk;
    MainMassageAdapter mainMassageAdapter;
    List<MainMassageBean.DataBean> list;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.main_massage_activity;
    }

    @Override
    public void initView() {
        title.setText("推送消息");
        ll_titlebar_back.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainMassageActivity.this,MainMessageDetailAC.class);
                intent.putExtra("title",list.get(i).getTitle());
                intent.putExtra("message",list.get(i).getMessage());
                intent.putExtra("time",list.get(i).getCreateDate());
                intent.putExtra("msgId",list.get(i).getMsgId());
                intent.putExtra("urlQr",list.get(i).getUrlQr());
                startActivity(intent);
            }
        });
    }


    @Override
    public void initData() {
        super.initData();

    }

    @Override
    protected void load() {
        super.load();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainMsg();
    }

    public void mainMsg() {
        String url = NetUrl.GET_MAIN_MESSAGE;
        JSONObject object = new JSONObject();
        try {
            String loginType = SharedPreferencesUtil.getInstance(MainMassageActivity.this).getKey("loginType");
            if(("0").equals(loginType)){
                object.put("merId", SharedPreferencesUtil.getInstance(MainMassageActivity.this).getKey(Constants.MERCHANTID));
            }else {
                object.put("merId", SharedPreferencesUtil.getInstance(MainMassageActivity.this).getKey("shopId"));
            }
            object.put("page", "0");
            object.put("pageSize","20");
            object.put("type","7");
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(MainMassageActivity.this, url, object, new MyOkCallback<MainMassageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(MainMassageBean mainMassageBean) {
                String status = mainMassageBean.getStatus();
                String msg = mainMassageBean.getMsg();
                list = mainMassageBean.getData();
                if ("0".equals(status)) {
                    if (null != list && list.size() > 0) {
                        tv_nodata.setVisibility(View.GONE);
                        ll_gone_clerk.setVisibility(View.GONE);
                        mainMassageAdapter = new MainMassageAdapter(context(), list);
                        mainMassageAdapter.notifyDataSetChanged();
                        listView.setAdapter(mainMassageAdapter);
                    } else {
                        tv_nodata.setVisibility(View.VISIBLE);
                    }
                }else {
                    ll_gone_clerk.setVisibility(View.VISIBLE);
                    ToastUtil.shortShow(MainMassageActivity.this,msg);
                    return;
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
                return MainMassageBean.class;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_titlebar_back:
                finish();
                break;
        }
    }
}
