package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.VerificationCodeBean;
import com.hybunion.yirongma.payment.utils.OCJDesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.SeparatedEditText;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 身份验证（验证码）
 */

public class VerificationCodeActivity extends BasicActivity {
    @Bind(R.id.phoneNum_verification_code_activity)
    TextView mTvPhoneNum;
    @Bind(R.id.separatedEdt_verification_code_activity)
    SeparatedEditText mSEdt;
    @Bind(R.id.tv_sms_verification_code_activity)
    TextView mTvSms; // 短信
    @Bind(R.id.tv_voice_verification_code_activity)
    TextView mTvVoice; // 语音

    private int mFlag = 0;  // 0-短信  1-语音
    private int mTime = 60;
    private String mLoginPhone;
    private int count = 0;
    private int flag = 0;//0短信验证 1 语音验证

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mTime==0){
                mTvSms.setText("重新发送");
                mTvVoice.setText("语音验证");
                mTvSms.setTextColor(Color.parseColor("#5280fa"));
                mTvVoice.setTextColor(Color.parseColor("#5280fa"));
                mTvSms.setClickable(true);
                mTvVoice.setClickable(true);
            }else{
                int what = msg.what;
                switch (what){
                    case 0:  // 短信
                        mTime--;
                        mTvSms.setText(mTime+"s 后重新发送");
                        mHandler.sendEmptyMessageDelayed(what,1000);
                        break;
                    case 1:  // 语音
                        mTime--;
                        mTvVoice.setText(mTime+"s 后重新发送");
                        mHandler.sendEmptyMessageDelayed(what,1000);
                        break;
                }
            }


        }
    };

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_verification_code;
    }


    @Override
    public void initView() {
        super.initView();
        mLoginPhone = SharedPreferencesUtil.getInstance(VerificationCodeActivity.this).getKey(SharedPConstant.LOGIN_NAME);
        mTvPhoneNum.setText("验证码已发送至"+mLoginPhone);

    }



    @Override
    protected void onResume() {
        super.onResume();
        getCode();
    }

    @OnClick({R.id.tv_sms_verification_code_activity, R.id.tv_voice_verification_code_activity})
    public void smsOrVoice(TextView tv){
        switch (tv.getId()){
            case R.id.tv_sms_verification_code_activity:   // 短信
                mFlag = 0;
                mTvSms.setTextColor(Color.parseColor("#b6bdd0"));
                mTvVoice.setTextColor(Color.parseColor("#b6bdd0"));
                mTvSms.setClickable(false);
                mTvVoice.setClickable(false);
                mHandler.sendEmptyMessage(mFlag);
                getCode();
                break;

            case R.id.tv_voice_verification_code_activity:   // 语音
                mFlag = 1;
                mTvSms.setTextColor(Color.parseColor("#b6bdd0"));
                mTvVoice.setTextColor(Color.parseColor("#b6bdd0"));
                mTvSms.setClickable(false);
                mTvVoice.setClickable(false);
                mHandler.sendEmptyMessage(mFlag);
                getCode();
                break;
        }

    }



    public void getCode(){
        SimpleDateFormat s = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        try {
            String tempcount = SharedPreferencesUtil.getInstance(this).getKey("findpwsmscount");
            if (tempcount.equals("5")){
                String tempdate = SharedPreferencesUtil.getInstance(this).getKey("findpwsmstime");
                if (!tempdate.equals("")) {
                    Date d = s.parse(tempdate);
                    if (((System.currentTimeMillis() - d.getTime()) / 1000 / 60) > 30) {
                        SharedPreferencesUtil.getInstance(this).putKey("findpwsmscount", "0");
                        SharedPreferencesUtil.getInstance(this).putKey("findpwsmstime", "");
                        count = 0;
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.send_msg_hint1) + (30 - ((System.currentTimeMillis() - d.getTime()) / 1000 / 60)) + getResources().getString(R.string.send_msg_hint2), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            if (count >= 5){
                Toast.makeText(this, getResources().getString(R.string.send_msg_hint3), Toast.LENGTH_LONG).show();
                SharedPreferencesUtil.getInstance(this).putKey("findpwsmscount","5");
                String smsdate = s.format(new Date());
                SharedPreferencesUtil.getInstance(this).putKey("findpwsmstime",smsdate);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonRequest = new JSONObject();
        try {
            if (flag==0){
                jsonRequest.put("sendType","0");
            }else {
                jsonRequest.put("sendType","1");
            }
            jsonRequest.put("loginName", OCJDesUtil.encryptThreeDESECB(mLoginPhone));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = NetUrl.FORGET_Encryption_CODE_URL;
        OkUtils.getInstance().post(VerificationCodeActivity.this, url, jsonRequest, new MyOkCallback<VerificationCodeBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(VerificationCodeBean verificationCodeBean) {
                count++;
                String status = verificationCodeBean.getStatus();
                String message = verificationCodeBean.getMessage();
                if(status.equals("0")){
                    mTvSms.setTextColor(Color.parseColor("#b6bdd0"));
                    mTvVoice.setTextColor(Color.parseColor("#b6bdd0"));
                    mTvSms.setClickable(false);
                    mTvVoice.setClickable(false);
                    mHandler.sendEmptyMessage(mFlag);
                    ToastUtil.show(message);
                }else {
                    ToastUtil.show(message);
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
                return VerificationCodeBean.class;
            }
        });
    }

    // 下一步
    @OnClick(R.id.bt_set_password_verification_code_activity)
    public void next(){
        String verfiCode = mSEdt.getText().toString().trim();
        if(verfiCode.equals("")){
            ToastUtil.show("请输入验证码");
        }else {
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("loginName", mLoginPhone);
                jsonRequest.put("validateCode", verfiCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = NetUrl.CHECK_CODE_URL;
            OkUtils.getInstance().postNoHeader(VerificationCodeActivity.this, url, jsonRequest, new MyOkCallback<VerificationCodeBean>() {
                @Override
                public void onStart() {
                    showLoading();
                }

                @Override
                public void onSuccess(VerificationCodeBean verificationCodeBean) {
                    hideLoading();
                    String status1 = verificationCodeBean.getStatus();
                    String message1 = verificationCodeBean.getMessage();
                    if(status1.equals("1")){
                        Intent intent = new Intent(VerificationCodeActivity.this,SetPassWordActivity.class);
                        intent.putExtra("type","0");
                        startActivity(intent);
                        ToastUtil.show(message1);
                        finish();
                    }else {
                        ToastUtil.show(message1);
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
                    return VerificationCodeBean.class;
                }
            });

        }

    }




}
