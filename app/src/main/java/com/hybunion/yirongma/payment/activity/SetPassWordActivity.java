package com.hybunion.yirongma.payment.activity;

import android.view.View;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.SetPassWordBean;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.SeparatedEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

public class SetPassWordActivity extends BasicActivity {
    @Bind(R.id.bt_setPassword)
    TextView bt_setPassword;
    @Bind(R.id.tv_error_code)
    TextView tv_error_code;

    @Bind(R.id.separatedEdt_verification_code_activity)
    SeparatedEditText mSEdt;
    String newPassWord;

    String loginType;
    String merId,type;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set_pass_word;
    }

    @Override
    public void initView() {
        super.initView();
        type = getIntent().getStringExtra("type");
        loginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if(loginType.equals("0")){
            merId = SharedPreferencesUtil.getInstance(this).getKey("merchantID");
        }else {
            merId = SharedPreferencesUtil.getInstance(this).getKey("shopId");
        }
        if(type.equals("1")){
            bt_setPassword.setText("确定修改");
        }else {
            bt_setPassword.setText("确定");
        }

        bt_setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewPassword();
            }
        });
    }


    public void setNewPassword(){
        newPassWord = mSEdt.getText().toString().trim();
        if(newPassWord.equals("") || newPassWord.length()!=6){
            ToastUtil.show("请输入新密码");
        }else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("merId",merId);
                jsonObject.put("password",newPassWord);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url= NetUrl.UPDATE_REFUND_PASSWORD;
            OkUtils.getInstance().post(SetPassWordActivity.this, url, jsonObject, new MyOkCallback<SetPassWordBean>() {
                @Override
                public void onStart() {
                    showLoading();
                }

                @Override
                public void onSuccess(SetPassWordBean setPassWordBean) {
                    hideLoading();
                    String status = setPassWordBean.getStatus();
                    String message = setPassWordBean.getMessage();
                    if(status.equals("0")){
                        tv_error_code.setVisibility(View.GONE);
                        ToastUtil.show(message);
                        SharedPreferencesUtil.getInstance(SetPassWordActivity.this).putKey(SharedPConstant.RE_PASSWORD,"0");
                        HRTApplication.finishActivity(VerificationCodeActivity.class);  // 杀掉中间两个页面。
                        HRTApplication.finishActivity(NoMerchantManageActivity.class);
                        finish();
                    }else {
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
                    return SetPassWordBean.class;
                }
            });
        }
    }



}
