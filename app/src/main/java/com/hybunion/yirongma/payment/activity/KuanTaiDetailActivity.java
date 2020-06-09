package com.hybunion.yirongma.payment.activity;

import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.DataSummaryBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.adapter.DataSummaryAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class KuanTaiDetailActivity extends BasicActivity {
    DataSummaryAdapter adapter;
    private String storeId;
    private String mStartTime;
    private String mEndTime;
    @Bind(R.id.tv_order_amount)
    TextView tv_order_amount;
    @Bind(R.id.tv_order_count)
    TextView tv_order_count;
    @Bind(R.id.lv_kuanTai)
    ListView lv_kuanTai;
    @Bind(R.id.tv_data_type)
    TextView tv_data_type;
    @Bind(R.id.tv_name)
    TextView tv_name;
    private String totalCount,totalAmt,storeName;
    List<DataSummaryBean.DataBean> mDataList = new ArrayList<>();

    @Override
    protected BasePresenter getPresenter() {
        return null ;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_kuan_tai_detail;
    }

    @Override
    public void initView() {
        super.initView();
        storeId = getIntent().getStringExtra("storeId");
        mStartTime = getIntent().getStringExtra("mStartTime");
        mEndTime = getIntent().getStringExtra("mEndTime");
        storeName = getIntent().getStringExtra("storeName");
        if(!TextUtils.isEmpty(storeName))
             tv_name.setText(storeName);

    }

    @Override
    protected void onResume() {
        super.onResume();
        queryDeskYrmTransSum(mStartTime,mEndTime,storeId);
    }

    public void queryDeskYrmTransSum(String mStartTime,String mEndTime,String storeId){
        String url = NetUrl.QUERY_DESK_TRANS_SUM;

        JSONObject object = new JSONObject();
        try {  // merId 属于必传字段
//            body.put("merId", SharedPreferencesUtil.getInstance(mContext).getKey("merchantID"));
            object.put("storeId", storeId);
            object.put("desk", "");
            object.put("loginType","2");
            object.put("endDate",mEndTime);
            object.put("startDate",mStartTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OkUtils.getInstance().post(KuanTaiDetailActivity.this, url, object, new MyOkCallback<DataSummaryBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(DataSummaryBean dataSummaryBean) {
                String status = dataSummaryBean.getStatus();
                totalCount = dataSummaryBean.totalCount;
                totalAmt = dataSummaryBean.totalAmt;
                tv_order_amount.setText(YrmUtils.decimalTwoPoints(totalAmt));
                tv_order_count.setText(totalCount);
                if(status.equals("0")){
                    mDataList = dataSummaryBean.getData();
                    if( mDataList!=null){
                        if(mDataList.size()>0){
                            adapter = new DataSummaryAdapter(KuanTaiDetailActivity.this,mDataList);
                            lv_kuanTai.setAdapter(adapter);
                        }

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
                return DataSummaryBean.class;
            }
        });
    }


}
