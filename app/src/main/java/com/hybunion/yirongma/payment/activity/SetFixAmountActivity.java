package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.SetFixAmountBean;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.SharedPConstant;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.view.NameInputFilter;
import com.hybunion.yirongma.payment.utils.YrmUtils;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

public class SetFixAmountActivity extends BasicActivity implements View.OnClickListener{

    @Bind(R.id.et_fix_account)
    EditText et_fix_account;
    @Bind(R.id.tv_cancel_fix_amount)
    TextView tv_cancel_fix_amount;
    @Bind(R.id.bt_fix_amount_sure)
    Button bt_fix_amount_sure;
    @Bind(R.id.et_pay_content)
    EditText et_pay_content;
    @Bind(R.id.tv_start_num)
    TextView tv_start_num;
    @Bind(R.id.ll_pay_content)
    LinearLayout ll_pay_content;
    private String tid;
    private String remark;
    private String limitAmt;
    private String isLimitAmt;//获取是否设置了固定金额
    private String iSremark;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set_fix_amount;
    }

    @Override
    public void initView() {
        super.initView();
        tid = getIntent().getStringExtra("tid");
        isLimitAmt = getIntent().getStringExtra("limitAmt");
        iSremark = getIntent().getStringExtra("remark");
        if(TextUtils.isEmpty(isLimitAmt)){
            isLimitAmt = "";
            ll_pay_content.setVisibility(View.GONE);
        }else {
            et_fix_account.setText(YrmUtils.decimalTwoPoints(isLimitAmt));
            et_pay_content.setText(iSremark);
            ll_pay_content.setVisibility(View.VISIBLE);
            tv_cancel_fix_amount.setTextColor(getResources().getColor(R.color.blue_color2));
        }
        SpannableString ss = new SpannableString("未设置,请输入固定金额");

        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(13,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_fix_account.setHint(new SpannedString(ss));
        et_fix_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //输入前的监听
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //输入的内容变花监听
                Log.i("xjz","输入时");
                et_fix_account.setTextColor(getResources().getColor(R.color.text_color2));
                tv_cancel_fix_amount.setTextColor(getResources().getColor(R.color.blue_color2));
                ll_pay_content.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入后的监听
                if(TextUtils.isEmpty(et_fix_account.getText().toString())){
                    SpannableString ss = new SpannableString("未设置,请输入最低金额");
                    AbsoluteSizeSpan ass = new AbsoluteSizeSpan(13,true);//设置字体大小 true表示单位是sp
                    ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    et_fix_account.setHint(new SpannedString(ss));
                    et_fix_account.setTextColor(getResources().getColor(R.color.grey));
                    tv_cancel_fix_amount.setTextColor(getResources().getColor(R.color.grey));
                    ll_pay_content.setVisibility(View.GONE);
                }

            }
        });
        tv_cancel_fix_amount.setOnClickListener(this);
        bt_fix_amount_sure.setOnClickListener(this);
        et_pay_content.setFilters(new InputFilter[]{new NameInputFilter()});
        et_pay_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int count = editable.toString().length();
                if(count>15){
                    ToastUtil.show("最多输入15个字符");
                    int start = et_pay_content.getSelectionStart();
                    int end = et_pay_content.getSelectionEnd();
                    editable.delete(start-1, end);
                    et_pay_content.setText(editable);
                    et_pay_content.setSelection(15);
                    return;
                }else {
                    tv_start_num.setText(String.valueOf(count));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel_fix_amount:
                SpannableString ss = new SpannableString("未设置,请输入最低金额");
                AbsoluteSizeSpan ass = new AbsoluteSizeSpan(13,true);//设置字体大小 true表示单位是sp
                ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                et_fix_account.setHint(new SpannedString(ss));
                et_fix_account.setText("");
                et_pay_content.setText("");
                et_fix_account.setTextColor(getResources().getColor(R.color.grey));
                tv_cancel_fix_amount.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.bt_fix_amount_sure:
                limitAmt = et_fix_account.getText().toString();
                if(TextUtils.isEmpty(isLimitAmt) && TextUtils.isEmpty(limitAmt)){
                    finish();
                }else {
                limitAmt = et_fix_account.getText().toString();
                remark = et_pay_content.getText().toString();
                if(!TextUtils.isEmpty(limitAmt) && TextUtils.isEmpty(remark)){
                    ToastUtil.show("请输入收款内容");
                    return;
                }
                if(TextUtils.isEmpty(limitAmt) || "0".equals(limitAmt)){
                    limitAmt = "";
                }
                setFixAmount(tid,remark,limitAmt);
                }
                break;
        }
    }

    public void setFixAmount(String tid,String remark,String limitAmt){
        JSONObject jsonObject = new JSONObject();
        try {
            String loginType = SharedPreferencesUtil.getInstance(SetFixAmountActivity.this).getKey(SharedPConstant.LOGINTYPE);
            if("0".equals(loginType)){
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(SetFixAmountActivity.this).getKey(SharedPConstant.MERCHANT_ID));
            }else {
                jsonObject.put("merId", SharedPreferencesUtil.getInstance(SetFixAmountActivity.this).getKey(SharedPConstant.SHOP_ID));
            }
            jsonObject.put("tid",tid);
            jsonObject.put("limitAmt",limitAmt);
            jsonObject.put("remark",remark);
            jsonObject.put("type","0");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = NetUrl.FIX_AMOUNT;
        OkUtils.getInstance().post(SetFixAmountActivity.this, url, jsonObject, new MyOkCallback<SetFixAmountBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(SetFixAmountBean setFixAmountBean) {
                String status = setFixAmountBean.getStatus();
                String message = setFixAmountBean.getMessage();
                if(status.equals("0")){
                    showDialog();
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
                return SetFixAmountBean.class;
            }
        });



    }

    public void showDialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.amount_dialog,null);
        final Dialog dialog = new Dialog(this,R.style.MyDialogs);
        dialog.setContentView(view);
        TextView tv_invoice = view.findViewById(R.id.tv_invoice);
        TextView tv_amount = view.findViewById(R.id.tv_amount);
        Button bt_sure = view.findViewById(R.id.bt_sure);
        if(TextUtils.isEmpty(limitAmt)){
            tv_invoice.setText("取消设置");
            tv_amount.setText("取消固定收款金额设置,30分钟内即可生效");
        }
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
}
