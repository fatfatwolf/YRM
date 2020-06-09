package com.hybunion.yirongma.payment.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hybunion.netlibrary.utils.ToastUtil;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.RefundSecondBean;
import com.hybunion.yirongma.payment.utils.Constants;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.SeparatedEditText;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class RefundSecondActivity extends BasicActivity {
    @Bind(R.id.et_amount)
    EditText et_amount;
    @Bind(R.id.iv_clear)
    ImageView iv_clear;
    @Bind(R.id.tv_order_amount)
    TextView tv_order_amount;
    @Bind(R.id.tv_order_no)
    TextView tv_order_no;
    @Bind(R.id.bt_sure)
    Button bt_sure;
    private String loginType,rePassword,merId,sevenDate,orderNo;
    private String merchantId,oldPassword,transAmount,refundAmount;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_refund_second;
    }

    @Override
    public void initView() {
        super.initView();
        final Intent intent = getIntent();
        merId = intent.getStringExtra("merId");
        sevenDate = intent.getStringExtra("sevenDate");
        transAmount = intent.getStringExtra("transAmount");
        orderNo = intent.getStringExtra("orderNo");
        if(!TextUtils.isEmpty(transAmount)){
            tv_order_amount.setText(transAmount);
        }
        if(!TextUtils.isEmpty(orderNo)){
            tv_order_no.setText(orderNo);
        }
        loginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if ("0".equals(loginType)) {  // 老板
            merchantId = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID);
        } else if ("1".equals(loginType)) {
            merchantId = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("shopId");
        } else {
            merchantId = SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey("shopId");
        }
        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_amount.setText("");
            }
        });

        SpannableString ss = new SpannableString("请输入退款金额");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_amount.setHint(new SpannedString(ss));
        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double tixianD;
                if (!TextUtils.isEmpty(s)) {
                    if (".".equals(s.toString())) {
                        et_amount.setText("");
                        return;
                    }
                    if (s.toString().matches("[0][0-9]")) {
                        et_amount.setText("0");
                        et_amount.setSelection(et_amount.getText().toString().length());
                        return;
                    }
                    if (s.toString().matches("[0-9]+[.][0-9]{3}")) {
                        String sn = s.toString().substring(0, s.toString().length() - 1);
                        et_amount.setText(sn);

                        et_amount.setSelection(et_amount.getText().toString().length());
                        return;
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(et_amount.getText().toString())){
                    et_amount.setTypeface(null, Typeface.NORMAL);
                }else {
                    et_amount.setTypeface(null, Typeface.BOLD);
                    et_amount.setTextSize(30);
                }


            }
        });
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refundAmount = et_amount.getText().toString().trim();
                if(TextUtils.isEmpty(refundAmount)){
                    ToastUtil.showShortToast("请输入退款金额");
                    return;
                }
                double refund = YrmUtils.stringToDouble(refundAmount);
                double refund2 =YrmUtils.stringToDouble(transAmount);
                if(refund == 0.00){
                    ToastUtil.showShortToast("请输入正确的退款金额");
                    return;
                }
                if(refund>refund2){
                    ToastUtil.showShortToast("退款金额不能大于订单金额");
                    return;
                }
                refund();
            }
        });

    }


    public void refund() {
            rePassword =  SharedPreferencesUtil.getInstance(RefundSecondActivity.this).getKey("rePassword");
            if (("0").equals(loginType)){
                if(rePassword.equals("1")){
                    showRefundDialog2();
                }else {
                    showRefundDialog3();
                }
            }else if("1".equals(loginType)){
                if(rePassword.equals("1")){

                    showRefundDialog();
                }else {
                    showRefundDialog3();
                }
            }else {
                ToastUtil.showShortToast("店员无退款权限");
            }

    }
    public void showRefundDialog2() {
        final Dialog mDialog = new Dialog(RefundSecondActivity.this, R.style.MyDialogStyle);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mPopup = inflater.inflate(R.layout.refund_dialog2, null);
        mDialog.setContentView(mPopup);
        mDialog.setCanceledOnTouchOutside(true);
        Button bt_sure = (Button) mPopup.findViewById(R.id.bt_sure);
        RelativeLayout rl_image = (RelativeLayout) mPopup.findViewById(R.id.rl_image);
        rl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RefundSecondActivity.this,NoMerchantManageActivity.class);
                startActivity(intent);
                mDialog.dismiss();
            }
        });

        mDialog.show();

    }

    SeparatedEditText set_pwd;
    public void showRefundDialog3() {
        final Dialog mDialog = new Dialog(RefundSecondActivity.this, R.style.MyDialogStyle);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mPopup = inflater.inflate(R.layout.refund_dialog3, null);
        mDialog.setContentView(mPopup);
        mDialog.setCanceledOnTouchOutside(true);
        Button bt_sure = (Button) mPopup.findViewById(R.id.bt_sure);
        TextView tv_merchant_pwd = (TextView) mPopup.findViewById(R.id.tv_merchant_pwd);
        TextView tv_refund_amount = (TextView) mPopup.findViewById(R.id.tv_refund_amount);
        RelativeLayout rl_image = (RelativeLayout) mPopup.findViewById(R.id.rl_image);
        rl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        tv_refund_amount.setText(refundAmount);
        set_pwd = (SeparatedEditText) mPopup.findViewById(R.id.set_pwd);
        if(loginType.equals("1")){
            tv_merchant_pwd.setVisibility(View.GONE);
        }else {
            tv_merchant_pwd.setVisibility(View.VISIBLE);
        }
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword = set_pwd.getText().toString().trim();
                queryIsRefoundPawd(oldPassword,mDialog);
            }
        });
        tv_merchant_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RefundSecondActivity.this,VerificationCodeActivity.class);
                startActivity(intent);
            }
        });

        mDialog.show();

    }
    //查询退款密码是否正确
    public void queryIsRefoundPawd(String oldPassword, final Dialog mDialog){

            JSONObject dataParam = new JSONObject();
        try {
            if(loginType.equals("0")){
                dataParam.put("merId", SharedPreferencesUtil.getInstance(this).getKey("merchantID"));
            }else {
                dataParam.put("merId", SharedPreferencesUtil.getInstance(this).getKey("shopId"));
            }
            dataParam.put("oldPassword",oldPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(this, NetUrl.QUERY_IS_REFUNDPWD, dataParam, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }
            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject response = new JSONObject(o);
                    String message = response.getString("message");
                    String status = response.getString("status");
                    if("0".equals(status)){
                        showDialogMsg(0,"");
                        mDialog.dismiss();
                    }else {
                        ToastUtil.showShortToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
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

    private void showDialogMsg(final int flag, String msg) {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_refund, null);
        dialog.setContentView(view);
        Button cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
        Button ensure_btn = (Button) view.findViewById(R.id.ensure_btn);
        TextView tv_description = (TextView) view.findViewById(R.id.tv_description);
        dialog.setCanceledOnTouchOutside(false);
        if (flag == 1) {
            tv_description.setText("        " + msg);
            cancel_btn.setVisibility(View.GONE);
        }
        ensure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (flag == 0) {
//                    presenter.getRefund(orderNo,refundAmount);
                    getRefund();
                }

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getRefund(){
        Map<String, String> mMap = new HashMap<>();
        String loginType = SharedPreferencesUtil.getInstance(this).getKey("loginType");
        if ("0".equals(loginType)) {
            mMap.put("merid", SharedPreferencesUtil.getInstance(this).getKey(Constants.MERCHANTID));
        }else if("1".equals(loginType)){
            mMap.put("merid", SharedPreferencesUtil.getInstance(this).getKey("shopId"));
        }
        mMap.put("oriOrderId", orderNo);
        mMap.put("refundAmount",refundAmount);

//        Map<String, File> imgPath = new HashMap<>();
//        imgPath.put("refundImg", new File(""));

        OkUtils.getInstance().postFile(this, NetUrl.CHECK_REFUND, mMap, null, new MyOkCallback<RefundSecondBean>() {
            @Override
            public void onStart() {
                showLoading();
            }
            @Override
            public void onSuccess(RefundSecondBean refundSecondBean) {
                String msg = refundSecondBean.getMsg();
                String reFundstatus = refundSecondBean.getStatus();
                if ("0".equals(reFundstatus)){
                    ToastUtil.showShortToast(msg);
//                    finish();
                }else {
                    if(!TextUtils.isEmpty(msg)){
                        ToastUtil.showShortToast(msg);
                    }
//                    finish();
                }
            }
            @Override
            public void onError(Exception e) {
                ToastUtil.showShortToast("网络连接不佳");
            }

            @Override
            public void onFinish() {
                hideLoading();
                finish();
            }

            @Override
            public Class getClazz() {
                return RefundSecondBean.class;
            }
        });


    }

    public void showRefundDialog() {
        final Dialog mDialog = new Dialog(RefundSecondActivity.this, R.style.MyDialogStyle);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mPopup = inflater.inflate(R.layout.refund_dialog, null);
        mDialog.setContentView(mPopup);
        mDialog.setCanceledOnTouchOutside(true);
        Button bt_sure = (Button) mPopup.findViewById(R.id.bt_sure);
        RelativeLayout rl_image = (RelativeLayout) mPopup.findViewById(R.id.rl_image);
        rl_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });

        mDialog.show();

    }
}
