package com.hybunion.yirongma.payment.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.AddKuanTaiBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.Bind;

public class AddKuanTaiActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.tv_head)
    TextView tv_head;
    @Bind(R.id.ll_back)
    LinearLayout ll_back;
    @Bind(R.id.bt_new_kuantai)
    Button bt_new_kuantai;
    @Bind(R.id.et_kuantai_name)
    EditText et_kuantai_name;

    private String mMerId,mStoreId;
    private String mLoginType,kuanTaiName;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_kuan_tai;
    }

    @Override
    public void initView() {
        super.initView();
        tv_head.setText("新增款台");
        bt_new_kuantai.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        mLoginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        mStoreId = getIntent().getStringExtra("storeId");
        if ("0".equals(mLoginType)) {
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        } else {
            mMerId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.bt_new_kuantai:
                kuanTaiName = et_kuantai_name.getText().toString().trim();
                if(kuanTaiName.equals("")){
                    ToastUtil.show("请输入款台名称");
                }else {
                    addKuanTai(TextUtils.isEmpty(mStoreId) ? "" : mStoreId, kuanTaiName);
                }
                break;
        }
    }

    // 添加新款台
    public void addKuanTai(String storeId, String kuanTaiName){
        String url = NetUrl.ADD_NEW_KUANTAI;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("storeId",storeId);
            jsonObject.put("storeName",kuanTaiName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(AddKuanTaiActivity.this, NetUrl.ADD_NEW_KUANTAI, jsonObject, new MyOkCallback<AddKuanTaiBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(AddKuanTaiBean addKuanTaiBean) {
                String message = addKuanTaiBean.getMessage();
                if(!TextUtils.isEmpty(message))
                    ToastUtil.show(message);

                hideLoading();
                finish();
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
                return AddKuanTaiBean.class;
            }
        });

    }
}
