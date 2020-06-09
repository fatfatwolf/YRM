package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.TiXianRecordBean;
import com.hybunion.yirongma.payment.utils.RequestIndex;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.base.CommonAdapter1;
import com.hybunion.yirongma.payment.base.ViewHolder;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 代理商计划，钱包提现记录 列表
 */

public class TiXianRecordActivity extends BasicActivity {
    @Bind(R.id.tvNull_tixian_record_activity)
    TextView mTvNull;
    @Bind(R.id.listView_tixian_record_activity)
    ListView mLv;

    private CommonAdapter1 mAdapter;
    private String mLoginType;
    private String mMerId;

    public static void start(Context from) {
        Intent intent = new Intent(from, TiXianRecordActivity.class);
        from.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_tixian_record_layout;
    }

    @Override
    public void initView() {
        super.initView();

        mLv.setAdapter(mAdapter = new CommonAdapter1<TiXianRecordBean.DataBean>(this, mDataList, R.layout.item_tixian_record) {
            @Override
            public void convert(ViewHolder holder, TiXianRecordBean.DataBean item, int position) {
                TextView date = holder.findView(R.id.date_item_tixian_record);
                TextView amt = holder.findView(R.id.amt_item_tixian_record);
                TextView state = holder.findView(R.id.state_item_tixian_record);
                if (!TextUtils.isEmpty(item.cashDate)){
                    String cashDateNew = item.cashDate.replace(" ","\n");
                    date.setText(cashDateNew);
                }
                amt.setText(item.cashAmt);
                if (TextUtils.isEmpty(item.status)){
                    state.setText("提现中");
                }else{
                    state.setText("1".equals(item.status)?"提现成功":"提现中");
                }


            }
        });

        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if ("0".equals(mLoginType)) {  // 老板
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        } else {
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
        }
    }

    @Override
    protected void load() {
        super.load();
        getTiXianRecord(mMerId);
    }


    public void getTiXianRecord(String merId) {
        String url = NetUrl.TIXIAN_RECORD;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId", merId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(TiXianRecordActivity.this, url, jsonObject, new MyOkCallback<TiXianRecordBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(TiXianRecordBean bean) {
                if ("0".equals(bean.getStatus())) {
                    if (bean.getData()!=null && bean.getData().size()!=0){
                        mDataList.addAll(bean.getData());
                        if (mAdapter!=null)
                            mAdapter.updateList(mDataList);
                    }else{
                        mTvNull.setVisibility(View.VISIBLE);
                    }

                } else {
                    String msg = bean.getMessage();
                    if (!TextUtils.isEmpty(msg)) {
                        ToastUtil.show(msg);
                    }
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
                return TiXianRecordBean.class;
            }
        });

    }


    private List<TiXianRecordBean.DataBean> mDataList = new ArrayList<>();
    @Override
    public void showInfo(Map map, RequestIndex type) {
        super.showInfo(map, type);
        switch (type) {
            case TIXIAN_RECORD:
                if (map != null) {
                    TiXianRecordBean bean = (TiXianRecordBean) map.get("data");
                    if (bean.getData()!=null && bean.getData().size()!=0){
                        mDataList.addAll(bean.getData());
                        if (mAdapter!=null)
                            mAdapter.updateList(mDataList);
                    }else{
                        mTvNull.setVisibility(View.VISIBLE);
                    }
                } else {
                    mTvNull.setVisibility(View.VISIBLE);
                }


                break;
        }


    }
}
