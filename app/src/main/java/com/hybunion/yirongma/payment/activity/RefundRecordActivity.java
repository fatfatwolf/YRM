package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.RefundRecordBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.RefundRecordAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
public class RefundRecordActivity extends BasicActivity {

    @Bind(R.id.lv_refund_record)
    ListView lv_refund_record;
    private RefundRecordAdapter adapter;
    private List<RefundRecordBean.DataBean> list = new ArrayList<>();
    private String orderNo;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_record;
    }

    @Override
    public void initView() {
        super.initView();
        adapter = new RefundRecordAdapter(this);
        orderNo = getIntent().getStringExtra("orderNo");
        lv_refund_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RefundRecordActivity.this,RefundItemActivity.class);
                intent.putExtra("UUID",list.get(position).UUID);
                intent.putExtra("amount",list.get(position).amount);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void load() {
        super.load();
        queryRefundInfo();
    }

    private void queryRefundInfo(){
        JSONObject bodyObject = new JSONObject();
        try {
            bodyObject.put("JhMid", SharedPreferencesUtil.getInstance(this).getKey("mid"));//商户号
            bodyObject.put("orderNo", orderNo);//卡表主键
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(this, NetUrl.QUERY_REFUND_INFO, bodyObject, new MyOkCallback<RefundRecordBean>() {
            @Override
            public void onStart() {
                showLoading();
            }
            @Override
            public void onSuccess(RefundRecordBean bean) {
                if("0".equals(bean.getStatus())){
                    list = bean.getData();
                    adapter.addAllList(list);
                    lv_refund_record.setAdapter(adapter);
                }else if(bean.getStatus().equals("2")){
                    ToastUtil.showShortToast("无退款明细");
                }else {
                    ToastUtil.showShortToast("查询失败");
                }
            }
            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }
            @Override
            public void onFinish() {
                hideLoading();
            }
            @Override
            public Class getClazz() {
                return RefundRecordBean.class;
            }
        });

    }

}
