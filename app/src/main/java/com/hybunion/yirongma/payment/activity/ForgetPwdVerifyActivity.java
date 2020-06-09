package com.hybunion.yirongma.payment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.utils.OCJDesUtil;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.SeparatedEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Jairus on 2018/12/20.
 * 输入验证码界面
 */

public class ForgetPwdVerifyActivity extends BasicActivity {
    @Bind(R.id.tv_tips_forget_pwd_verify)
    TextView mTvTips;
    @Bind(R.id.tv_sms_forget_pwd_verify)
    TextView mTvSms;
    @Bind(R.id.tv_voice_forget_pwd_verify)
    TextView mTvVoice;
    @Bind(R.id.tvNotice_forget_pwd_verify)
    TextView mTvNotice;
    @Bind(R.id.sedt_forget_pwd_verify)
    SeparatedEditText mSEdt;

    private String mPhoneNum, mUUID;
    private int mSendType = 0; // 0-短信验证码   1-语音验证码
    private int mTime = 60;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mTime > 0) {
                if (msg.what == 0) {  // 短信验证码
                    mTvSms.setText(mTime + "s 后重新发送");
                } else if (msg.what == 1) {   // 语音验证码
                    mTvVoice.setText(mTime + "s 后重新发送");
                }
                mTime--;
                mHandler.sendEmptyMessageDelayed(mSendType,1000);
            }else{
                setBtStatu(true);
                mTime=60;
            }


        }
    };

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_forget_pwd_verify;
    }

    // 将上个界面获取验证码接口返回的 uuid 传过来
    public static void start(Context from, String phoneNum, String uuid) {
        Intent intent = new Intent(from, ForgetPwdVerifyActivity.class);
        intent.putExtra("phoneNum", phoneNum);
        intent.putExtra("uuid", uuid);
        from.startActivity(intent);
    }

    @Override
    public void initView() {
        super.initView();
        Intent intent = getIntent();
        mPhoneNum = intent.getStringExtra("phoneNum");
        mUUID = intent.getStringExtra("uuid");
        mTvTips.setText("验证码已发送至 " + mPhoneNum);
        mHandler.sendEmptyMessage(mSendType);
        setBtStatu(false);
    }

    // 点击获取验证码
    @OnClick({R.id.tv_sms_forget_pwd_verify, R.id.tv_voice_forget_pwd_verify})
    public void sendCode(TextView tv) {
        switch (tv.getId()) {
            case R.id.tv_sms_forget_pwd_verify:   // 短信验证
                mSendType = 0;
                break;

            case R.id.tv_voice_forget_pwd_verify:   // 语音验证
                mSendType = 1;
                break;
        }
        mHandler.sendEmptyMessage(mSendType);
        setBtStatu(false);
        getCode();
    }

    // 获取验证码
    private void getCode() {
        if (TextUtils.isEmpty(mPhoneNum)) return;
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            JSONObject body = new JSONObject();
            body.put("sendType", mSendType);
            body.put("loginName", OCJDesUtil.encryptThreeDESECB(mPhoneNum));
            jsonRequest.put("body", body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = NetUrl.FORGET_Encryption_CODE_URL;
        OkUtils.getInstance().post(ForgetPwdVerifyActivity.this, url, jsonRequest, new MyOkCallback<String>() {
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
                    mUUID = response.getString("UUID");

                    if ("0".equals(state)) {  // 获取验证码成功
                        ToastUtil.show("验证码已发送，请查收");
                        mTvNotice.setVisibility(View.INVISIBLE);
                    } else {
                        if (!TextUtils.isEmpty(message)) {
                            ToastUtil.show(message);
                            mTvNotice.setVisibility(View.VISIBLE);
                            mTvNotice.setText(message);
                        }
                    }
                } catch (JSONException e) {
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

    // 下一步
    @OnClick(R.id.bt_set_password_forget_pwd_verify)
    public void next(){
        String verifyCode = mSEdt.getText().toString(); // 用户输入的验证码
        if (TextUtils.isEmpty(verifyCode) || verifyCode.length()!=6){
            setCodeState(false,"请输入正确的验证码");
        }else{
            setCodeState(true,"");
            verifyCode(verifyCode);

        }

    }

    // 验证 验证码
    private void verifyCode(String verifyCode){
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            jsonRequest.put("loginName", mPhoneNum);
            jsonRequest.put("validateCode", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.CHECK_CODE_URL;
        OkUtils.getInstance().postNoHeader(ForgetPwdVerifyActivity.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String str) {
                try {
                    JSONObject response = new JSONObject(str);
                    String status = response.getString("status"); //status 为1表示验证通过
                    String message = response.getString("message"); //失败提示信息
                    hideLoading();
                    if(status.equals("1")){
                        setCodeState(true,"");
                        ResetPwdActivity.start(ForgetPwdVerifyActivity.this,mPhoneNum,mUUID);
                    } else{
                        if (!TextUtils.isEmpty(message))
                            setCodeState(false,message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                return String.class;
            }
        });

    }




    // canUse-发送短信、语音验证码 按钮是否可用
    private void setBtStatu(boolean canUse) {
        mTvSms.setClickable(canUse);
        mTvVoice.setClickable(canUse);
        if (canUse) {
            mTvSms.setText("重新发送");
            mTvVoice.setText("语音验证");
            mTvSms.setTextColor(Color.parseColor("#5280fa"));
            mTvVoice.setTextColor(Color.parseColor("#5280fa"));
        } else {
            mTvSms.setTextColor(Color.parseColor("#b6bdd0"));
            mTvVoice.setTextColor(Color.parseColor("#b6bdd0"));
        }
    }

    // 根据 isCodeRight（验证码是否正确，验证码校验接口返回结果），来设置输入框颜色和下面的文字
    private void setCodeState(boolean isCodeRight, String text){
        if (isCodeRight){
            mSEdt.setBorderColor(Color.parseColor("#b6bdd0"));
            mTvNotice.setVisibility(View.INVISIBLE);

        }else{
            mSEdt.setBorderColor(Color.parseColor("#F74948"));
            mTvNotice.setText(text);
            mTvNotice.setVisibility(View.VISIBLE);
        }

    }


}