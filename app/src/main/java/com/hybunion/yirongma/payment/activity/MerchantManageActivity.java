package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.MerchantManageBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.SeparatedEditText;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.Bind;

public class MerchantManageActivity extends BasicActivity {
    @Bind(R.id.bt_oldPassword)
    TextView bt_oldPassword;
    @Bind(R.id.separatedEdt_verification_code_activity)
    SeparatedEditText spText;
    @Bind(R.id.tv_forget_password)
    TextView tv_forget_password;
    @Bind(R.id.tv_error_code)
    TextView tv_error_code;
    String merId;
    String loginType;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_merchant_manage;
    }

    @Override
    public void initView() {
        super.initView();
        loginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        merId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
    }

    @Override
    public void initData() {
        super.initData();
        bt_oldPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verfiyCode();
            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MerchantManageActivity.this,VerificationCodeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void verfiyCode(){
        String oldPassword = spText.getText().toString().trim();
        if(oldPassword.equals("")){
            ToastUtil.show("请输入密码");
        }else {
            queryIsRefoundPawd(oldPassword);
        }
    }

    public void queryIsRefoundPawd(String oldPassword){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merId",merId);
            jsonObject.put("oldPassword",oldPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url= NetUrl.QUERY_IS_REFUNDPWD;
        OkUtils.getInstance().post(MerchantManageActivity.this, url, jsonObject, new MyOkCallback<MerchantManageBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(MerchantManageBean merchantManageBean) {
                hideLoading();
                String status = merchantManageBean.getStatus();
                String message = merchantManageBean.getMessage();
                if("0".equals(status)){
                    ToastUtil.show(message);
                    tv_error_code.setVisibility(View.GONE);
                    Intent intent = new Intent(MerchantManageActivity.this,SetPassWordActivity.class);
                    intent.putExtra("type","1");
                    startActivity(intent);
                    finish();
                }else {
                    tv_error_code.setVisibility(View.VISIBLE);
                    tv_error_code.setText(message);
                }


            }

            @Override
            public void onError(Exception e) {
                hideLoading();
                ToastUtil.show("网络连接不佳");
            }

            @Override
            public void onFinish() {

            }

            @Override
            public Class getClazz() {
                return MerchantManageBean.class;
            }
        });
    }
}
