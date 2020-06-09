package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.CommonBean;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.Fragment.LMFPaymentFragment;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;

public class SetMinAccountActivity extends BasicActivity implements View.OnClickListener{
    @Bind(R.id.et_min_account)
    EditText et_min_account;
    @Bind(R.id.tv_cancel_min_amount)
    TextView tv_cancel_min_amount;
    @Bind(R.id.bt_min_amount_sure)
    Button bt_min_amount_sure;
    private String minAmount;
    private String isMinAmount;//获取是否设置过最低金额

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set_min_account;
    }

    @Override
    public void initView() {
        super.initView();
        isMinAmount = SharedPreferencesUtil.getInstance(this).getKey(SharedPConstant.MIN_ACCOUNT);
        if(TextUtils.isEmpty(isMinAmount)){
            SpannableString ss = new SpannableString("未设置,请输入最低金额");
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(13,true);//设置字体大小 true表示单位是sp
            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            et_min_account.setHint(new SpannedString(ss));
        }else {
            et_min_account.setText(YrmUtils.decimalTwoPoints(isMinAmount));
            tv_cancel_min_amount.setTextColor(getResources().getColor(R.color.blue_color2));
        }

        et_min_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 //输入前的监听
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入的内容变花监听
                Log.i("xjz","输入时");
                et_min_account.setTextColor(getResources().getColor(R.color.text_color2));
                tv_cancel_min_amount.setTextColor(getResources().getColor(R.color.blue_color2));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入后的监听
                if(et_min_account.getText().toString().equals("")){
                    SpannableString ss = new SpannableString("未设置,请输入最低金额");
                    AbsoluteSizeSpan ass = new AbsoluteSizeSpan(13,true);//设置字体大小 true表示单位是sp
                    ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    et_min_account.setHint(new SpannedString(ss));
                    et_min_account.setTextColor(getResources().getColor(R.color.grey));
                    tv_cancel_min_amount.setTextColor(getResources().getColor(R.color.grey));
                }

            }
        });
        tv_cancel_min_amount.setOnClickListener(this);
        bt_min_amount_sure.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel_min_amount:
                SpannableString ss = new SpannableString("未设置,请输入最低金额");
                AbsoluteSizeSpan ass = new AbsoluteSizeSpan(13,true);//设置字体大小 true表示单位是sp
                ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                et_min_account.setHint(new SpannedString(ss));
                et_min_account.setText("");
                et_min_account.setTextColor(getResources().getColor(R.color.grey));
                tv_cancel_min_amount.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.bt_min_amount_sure:
                minAmount = et_min_account.getText().toString();
                if(TextUtils.isEmpty(isMinAmount) && TextUtils.isEmpty(minAmount)){
                    finish();
                }else {
                    if("0".equals(minAmount)||TextUtils.isEmpty(minAmount)){
                        minAmount = "";
                    }
                    setMinAccount(minAmount);
                }
                break;

        }
    }

    public void setMinAccount(final String minAmount){
        JSONObject jsonObject = new JSONObject();
        try {
            String loginType = SharedPreferencesUtil.getInstance(SetMinAccountActivity.this).getKey(SharedPConstant.LOGINTYPE);
            if(loginType.equals("0")){
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(SetMinAccountActivity.this).getKey(SharedPConstant.MERCHANT_ID));
            }else {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(SetMinAccountActivity.this).getKey(SharedPConstant.SHOP_ID));
            }
            jsonObject.put("minAmount",minAmount);
            jsonObject.put("type","1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.MIN_ACCOUNT;
        OkUtils.getInstance().post(SetMinAccountActivity.this, url, jsonObject, new MyOkCallback<CommonBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(CommonBean commonBean) {
                String status = commonBean.getStatus();
                String message = commonBean.getMessage();
                if(status.equals("0")){
                    showDialog();
                    SharedPreferencesUtil.getInstance(SetMinAccountActivity.this).putKey(SharedPConstant.MIN_ACCOUNT,minAmount);
                    Intent msgIntent = new Intent(LMFPaymentFragment.ACTION_INTENT_RECEIVER);
                    sendBroadcast(msgIntent);
                }else {
                    if(message!=null)
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
                return CommonBean.class;
            }
        });
    }

    @Override
    public void showInfo(Map map) {
        super.showInfo(map);

    }

    public void showDialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.amount_dialog,null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        Button bt_sure = view.findViewById(R.id.bt_sure);
        TextView tv_invoice = view.findViewById(R.id.tv_invoice);
        TextView tv_amount = view.findViewById(R.id.tv_amount);
        if(TextUtils.isEmpty(minAmount)){
            tv_invoice.setText("取消设置");
            tv_amount.setText("取消最低收款金额设置,30分钟内即可生效");
        }else {
            tv_amount.setText("最低收款金额设置成功,30分钟内即可效");
        }
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
