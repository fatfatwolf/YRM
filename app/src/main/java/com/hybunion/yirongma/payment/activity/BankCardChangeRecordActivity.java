package com.hybunion.yirongma.payment.activity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.ChangeRecordBean;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.adapter.ChangeRecordListAdapter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 银行卡变更记录
 * Created by lyf on 2017/5/21.
 */

public class BankCardChangeRecordActivity extends BasicActivity {

    @Bind(R.id.lv_record_message)
    ListView lv_record_message;
    @Bind(R.id.tv_no_data)
    TextView tv_no_data;

    private ChangeRecordListAdapter recordListAdapter;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bank_card_change_record;
    }


    @Override
    protected void load() {
        super.load();
        getChangeRecordRequest();
    }

    /**
     * 查询审核记录的请求
     */
    private void getChangeRecordRequest() {
        String mid = SharedPreferencesUtil.getInstance(BankCardChangeRecordActivity.this).getKey(SharedPConstant.MID);
        String url = NetUrl.GET_APPROVE_MESSAGE;
        Map map = new HashMap();
        map.put("mid",mid);

        OkUtils.getInstance().postFormData(BankCardChangeRecordActivity.this, url, map, new MyOkCallback<ChangeRecordBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(ChangeRecordBean recordBean) {
                if (recordBean != null) {
                    boolean success = recordBean.getSuccess();
                    String msg = recordBean.getMsg();
                    if(recordBean.getObj()!=null){
                        if(null!=recordBean.getObj().getRows()){
                            List<ChangeRecordBean.ObjEntity.RowsEntity> mList = recordBean.getObj().getRows();
                            if (mList.size() == 0) {
                                tv_no_data.setVisibility(View.VISIBLE);
                            }
                            if (success) {
                                if (recordListAdapter == null) {
                                    recordListAdapter = new ChangeRecordListAdapter(context(), mList);
                                    lv_record_message.setAdapter(recordListAdapter);
                                } else {
                                    recordListAdapter.notifyDataSetChanged();
                                }
                            } else {
                                ToastUtil.show(msg);
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onFinish() {
                hideLoading();
            }

            @Override
            public Class getClazz() {
                return ChangeRecordBean.class;
            }
        });


    }
}
