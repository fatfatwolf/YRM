package com.hybunion.yirongma.payment.activity;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.utils.OCJDesUtil;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Jairus on 2018/12/20.
 * 忘记密码 界面
 */

public class ForgetPwdActivity extends BasicActivity {
    @Bind(R.id.edtPhoneNum_forget_pwd_activity)
    EditText mEdtPhoneNum;
    @Bind(R.id.tvNotice_forget_pwd_activity)
    TextView mTvNotice;
    @Bind(R.id.line_find_password_activity)
    View mLine;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_forget_pwd;
    }

    // 下一步
    @OnClick(R.id.tvNext_forget_pwd_activity)
    public void next() {
        String phoneNum = mEdtPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
            ToastUtil.show("请输入正确的手机号");
            return;
        }
        getCode(phoneNum);

    }

    /**
     * 获取手机验证码
     */
    private void getCode(final String phoneNum) {
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            jsonRequest.put("sendType", "0");  // 发送的是短信验证按   0-短信验证   1-语音验证
            jsonRequest.put("loginName", OCJDesUtil.encryptThreeDESECB(phoneNum));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url =  NetUrl.FORGET_Encryption_CODE_URL;
        OkUtils.getInstance().post(ForgetPwdActivity.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String str) {
                try {
                    JSONObject response = new JSONObject(str);
                    String state = response.getString("status");
                    String message = response.getString("message");
                    String UUID = response.getString("UUID");

                    if ("0".equals(state)){  // 获取验证码成功
                        ToastUtil.show(getResources().getString(R.string.send_code_hint));
                        mLine.setBackgroundColor(Color.parseColor("#e5e5e5"));
                        mTvNotice.setVisibility(View.INVISIBLE);
                        // 跳转输入验证码界面
                        ForgetPwdVerifyActivity.start(ForgetPwdActivity.this,phoneNum,UUID);
                    }else{
                        if (!TextUtils.isEmpty(message)){
//                            ToastUtil.show(message);
                            mTvNotice.setVisibility(View.VISIBLE);
                            mTvNotice.setText(message);
                            mLine.setBackgroundColor(Color.parseColor("#F74948"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                return String.class;
            }
        });

    }


}
