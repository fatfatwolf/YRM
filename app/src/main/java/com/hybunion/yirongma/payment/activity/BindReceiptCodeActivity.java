package com.hybunion.yirongma.payment.activity;

import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.HRTApplication;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.BindReceiptCodeBean;
import com.hybunion.yirongma.payment.utils.NameFilter;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.bean.AutonymCertificationInfoBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.utils.SharedUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.utils.SavedInfoUtil;
import com.hybunion.yirongma.payment.view.HRTToast;
import com.hybunion.yirongma.payment.view.MaxLenghtInputFilter;
import com.hybunion.yirongma.payment.view.TwoButtonDialog;
import com.journeyapps.barcodescanner.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by king on 2017/5/19.
 */

public class BindReceiptCodeActivity extends BasicActivity {

    @Bind(R.id.bind_receipt_code_btn_scan)
    Button btnScan;
    @Bind(R.id.bind_receipt_code_edit_text_name)
    EditText mText;
    @Bind(R.id.bind_receipt_code_text_store)
    TextView storeOwned;
    String signContents,content;
    String storeId,tidName;
    private String  status, message;
    @Override
    protected BasePresenter getPresenter() {
        return null ;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bind_receipt_code;
    }

    @Override
    public void initView() {
        String pic0 = SharedUtil.getInstance(context()).getString("pic0");
        String pic1 = SharedUtil.getInstance(context()).getString("pic1");
        mText.setFilters(new InputFilter[]{new NameFilter(), new MaxLenghtInputFilter(30)});
        storeId = SharedPreferencesUtil.getInstance(this).getKey("storeId");
        LogUtil.e("请求pic0"+pic0);
        LogUtil.e("请求pic1"+pic1);
        queryMerchantInfo();
    }

    @Override
    public void initData() {
    }
    private void queryMerchantInfo() {
        String url = NetUrl.QUERY_MERCHENT_INFO;
        Map<String, String> map = new HashMap<>();
        map.put("mid", SavedInfoUtil.getMid(this));

        OkUtils.getInstance().postFormData(BindReceiptCodeActivity.this, url, map, new MyOkCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        Gson gson = new Gson();
                        AutonymCertificationInfoBean bean = gson.fromJson(response
                                , new TypeToken<AutonymCertificationInfoBean>() {
                                }.getType());
                        tidName = bean.getObj().getRows().getRname();
                        storeOwned.setText(tidName);
                        mText.setText(tidName);
                        if (!TextUtils.isEmpty(tidName)) {
                            mText.setSelection(tidName.length());
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

            }

            @Override
            public Class getClazz() {
                return String.class;
            }
        });



    }

    @Override
    protected void load() {

    }

    @OnClick(R.id.bind_receipt_code_edit_text_name)
    public void editText() {
        String inputContent = mText.getText().toString().trim();
        if (!TextUtils.isEmpty(inputContent)) {
            mText.setSelection(inputContent.length());
        }
    }

    @OnClick(R.id.bind_receipt_code_btn_scan)
    public void onClick() {
        if (TextUtils.isEmpty(mText.getText().toString())) {
            HRTToast.showToast("名称不能为空", this);
            return;
        }
        startScan();
    }

    /**
     * 开启扫码
     */
    private void startScan() {
        new IntentIntegrator(this)
                .setOrientationLocked(false)
                .setCaptureActivity(CaptureActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描
    }

    public void onResult(String s ,String signContents) {
        showLoading();
        String mid = SharedPreferencesUtil.getInstance(this).getKey("mid");
        String shopName = mText.getText().toString().trim();
        bindReceiptCode(tidName,storeId,mid, s,signContents, shopName);
    }

    public void bindReceiptCode(String tidName,String storeId,String mid, String code,String signContents, String shopName) {
        String url = NetUrl.BIND_RECEIPT_CODE;
        Map map = new HashMap();
        map.put("mid", mid);
        map.put("tidName",tidName);
        map.put("qrtid", code);
        map.put("storeId",storeId);
        map.put("qrPwd",signContents);
        map.put("Shopname", shopName);

        OkUtils.getInstance().postFormData(BindReceiptCodeActivity.this, url, map, new MyOkCallback<BindReceiptCodeBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BindReceiptCodeBean baseBean) {
                if (baseBean.isSuccess()) {
                    showSwipeCardDialog(baseBean.getMsg());
                } else {
                    if(!TextUtils.isEmpty(baseBean.getMsg()))
                        ToastUtil.show(baseBean.getMsg());
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
                return BindReceiptCodeBean.class;
            }
        });
    }
    private void showSwipeCardDialog(String msg) {
        final TwoButtonDialog tipDialog;
        tipDialog = new TwoButtonDialog(BindReceiptCodeActivity.this).builder()
                .setTitle(BindReceiptCodeActivity.this.getString(R.string.tips))
                .setMsg(msg)
                .setCancelable(false)
                .setLeftButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BindReceiptCodeActivity.this.finish();
                    }
                });
        tipDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "扫码失败,请重新扫码", Toast.LENGTH_LONG).show();
            } else {
                String code = result.getContents();
                LogUtil.d("code = " + code);
                int starIndex = code.indexOf("qr");
                if (starIndex > 0) {
                     content = code.substring(starIndex + 3, code.indexOf(".", starIndex + 3));
                    int lastIndex = code.lastIndexOf("sign=");
                    if (lastIndex > 0){
                        signContents = code.substring(lastIndex + 5);
                    }else {
                        signContents = "";
                    }
                    com.hybunion.yirongma.payment.utils.LogUtil.d(content+"tid"+"\n"+signContents+"签名");
                    startScanPaying(content,signContents); //请求数据
                } else {
                    Toast.makeText(this.getApplicationContext(), "请扫描正确二维码", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void startScanPaying(final String resultData ,final String signContents) {
        String url = NetUrl.CHECK_TID;
        JSONObject jsonRequest = null;
        try {
            jsonRequest = new JSONObject();
            jsonRequest.put("tid",resultData);
            jsonRequest.put("storeId",storeId);
            jsonRequest.put("merId", SharedPreferencesUtil.getInstance(HRTApplication.getInstance()).getKey(com.hybunion.yirongma.payment.utils.Constants.MERCHANTID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(BindReceiptCodeActivity.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String str) {
                try {
                    JSONObject response = new JSONObject(str);
                    status = response.getString("status");
                    message = response.getString("msg");
                    if ("0".equals(status)){
                        onResult(content,signContents );
                    }else {
                        com.hybunion.yirongma.payment.utils.ToastUtil.shortShow(BindReceiptCodeActivity.this,message);
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
}
