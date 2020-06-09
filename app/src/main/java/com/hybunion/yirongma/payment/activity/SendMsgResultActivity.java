package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hybunion.yirongma.payment.bean.StoreManageBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SendMsgResultActivity extends BasicActivity {

    @Bind(R.id.tv_history_msg)
    TextView tv_history_msg;
    @Bind(R.id.tv_back)
    TextView tv_back;
    private String storeId,mSelectedStoreName;
    private List<StoreManageBean.ObjBean> mStoreList = new ArrayList(); // 加入商圈的门店 List
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_send_msg_result;
    }

    @Override
    public void initView() {
        super.initView();
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        storeId = getIntent().getStringExtra("storeId");
        mStoreList = (List<StoreManageBean.ObjBean>) getIntent().getSerializableExtra("mStoreList");
        mSelectedStoreName = getIntent().getStringExtra("mStoreName");
        tv_history_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(YrmUtils.isFastDoubleClick())  return;

                Intent intent = new Intent(SendMsgResultActivity.this,MessageHistoryActivity.class);
                intent.putExtra("mStoreList", (Serializable) mStoreList);
                intent.putExtra("storeId",storeId);
                intent.putExtra("mSelectedStoreName",mSelectedStoreName);
                startActivity(intent);
            }
        });
    }
}
