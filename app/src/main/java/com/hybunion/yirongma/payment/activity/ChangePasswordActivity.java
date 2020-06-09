package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.ChangePasswordBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;

public class ChangePasswordActivity extends BasicActivity implements View.OnClickListener{

    @Bind(R.id.tv_forget_password)
    TextView tv_forget_password;
    @Bind(R.id.et_old_password)
    EditText et_old_password;
    @Bind(R.id.et_new_password)
    EditText et_new_password;
    @Bind(R.id.btn_save_password)
    Button btn_save_password;
    private String newPassword,oldPassword;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView() {
        super.initView();
        tv_forget_password.setOnClickListener(this);
        btn_save_password.setOnClickListener(this);
    }

    @Override
    public void showInfo(Map map) {
        super.showInfo(map);
        ChangePasswordBean changePasswordBean = (ChangePasswordBean) map.get("changePasswordBean");
        String status = changePasswordBean.getStatus();
        String message = changePasswordBean.getMessage();
        if(status.equals("0")){
            ToastUtil.show(message);
            finish();
        }else {
            ToastUtil.show(message);
        }
     }

     public void changePassword(String newPassword,String oldPassword){
         JSONObject jsonObject = new JSONObject();

         try {
             jsonObject.put("merId", SharedPreferencesUtil.getInstance(ChangePasswordActivity.this).getKey(SharedPConstant.MERCHANT_ID));
             jsonObject.put("phone",SharedPreferencesUtil.getInstance(ChangePasswordActivity.this).getKey(SharedPConstant.LOGIN_NUMBER));
             jsonObject.put("newPassword",newPassword);
             jsonObject.put("oldPassword",oldPassword);
         } catch (JSONException e) {
             e.printStackTrace();
         }

         String url = NetUrl.CHANGE_PASSWORD_URL;
         OkUtils.getInstance().post(ChangePasswordActivity.this, url, jsonObject, new MyOkCallback<ChangePasswordBean>() {
             @Override
             public void onStart() {
                 showLoading();
             }

             @Override
             public void onSuccess(ChangePasswordBean changePasswordBean) {
                 String status = changePasswordBean.getStatus();
                 String message = changePasswordBean.getMessage();
                 if(status.equals("0")){
                     ToastUtil.show(message);
                     finish();
                 }else {
                     ToastUtil.show(message);
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
                 return ChangePasswordBean.class;
             }
         });

     }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_forget_password:
                Intent intent = new Intent(ChangePasswordActivity.this, ForgetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_save_password:
                newPassword = et_new_password.getText().toString().trim();
                oldPassword = et_old_password.getText().toString().trim();
                if(TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(oldPassword)){
                    ToastUtil.show("密码不能为空");
                    return;
                }
                changePassword(newPassword,oldPassword);
                break;
        }
    }
}
