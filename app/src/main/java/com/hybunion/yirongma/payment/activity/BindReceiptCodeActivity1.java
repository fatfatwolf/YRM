package com.hybunion.yirongma.payment.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.hybunion.netlibrary.utils.net.MyOkCallback;
import com.hybunion.netlibrary.utils.net.OkUtils;
import com.hybunion.yirongma.common.net.NetUrl;
import com.hybunion.yirongma.payment.base.BasePresenter;
import com.hybunion.yirongma.payment.bean.AutonymCertificationInfoBean;
import com.hybunion.yirongma.payment.bean.BindReceiptCodeBean;
import com.hybunion.yirongma.payment.utils.LogUtil;
import com.hybunion.yirongma.payment.utils.SharedUtil;
import com.hybunion.yirongma.payment.utils.ToastUtil;
import com.hybunion.yirongma.payment.utils.NameFilter;
import com.hybunion.yirongma.R;
import com.hybunion.yirongma.payment.utils.LogUtils;
import com.hybunion.yirongma.payment.utils.SharedPreferencesUtil;
import com.hybunion.yirongma.payment.base.BasicActivity;
import com.hybunion.yirongma.payment.view.HRTToast;
import com.hybunion.yirongma.payment.view.MaxLenghtInputFilter;
import com.hybunion.yirongma.payment.view.TwoButtonDialog;
import com.hybunion.yirongma.payment.zxing.activity.CaptureActivity1;
import com.hybunion.yirongma.payment.utils.YrmUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 绑定收款码
 */

public class BindReceiptCodeActivity1 extends BasicActivity {

    @Bind(R.id.bind_receipt_code_btn_scan)
    Button btnScan;
    @Bind(R.id.bind_receipt_code_edit_text_name)
    EditText mText;
    @Bind(R.id.bind_receipt_code_text_store)
    TextView storeOwned;
    String content;
    String storeId,storeName,shopName;
    private String  status, message;

    public static void start(Context context, String storeName, String storeId){
        Intent intent = new Intent(context,BindReceiptCodeActivity1.class);
        intent.putExtra("storeName",storeName);
        intent.putExtra("storeId",storeId);
        context.startActivity(intent);
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
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
        storeId = getIntent().getStringExtra("storeId");
        storeName = getIntent().getStringExtra("storeName");
        storeOwned.setText(storeName);
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
        map.put("mid", SharedPreferencesUtil.getInstance(this).getKey("mid"));

        OkUtils.getInstance().postFormData(BindReceiptCodeActivity1.this, url, map, new MyOkCallback<String>() {
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
                        shopName = bean.getObj().getRows().getRname();
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
        if(!YrmUtils.isHavePermission(BindReceiptCodeActivity1.this, Manifest.permission.CAMERA,YrmUtils.REQUEST_PERMISSION_CAMERA))  return;

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
                .setCaptureActivity(CaptureActivity1.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描
    }

    public void onResult(String s ,String signContents) {
        showLoading();
        String mid = SharedPreferencesUtil.getInstance(this).getKey("mid");
        String tidName = mText.getText().toString().trim();
        bindReceiptCode(tidName,storeId,mid, s,signContents, shopName);

    }

    public void bindReceiptCode(String tidName,String storeId,String mid, String code,String signContents, String shopName) {
        String url = NetUrl.BIND_RECEIPT_CODE;
        Map map = new HashMap();
        map.put("mid",mid);
        map.put("tidName",tidName);
        map.put("qrtid", code);
        map.put("storeId",storeId);
        map.put("qrPwd",signContents);
        map.put("Shopname", shopName);

        OkUtils.getInstance().postFormData(BindReceiptCodeActivity1.this, url, map, new MyOkCallback<BindReceiptCodeBean>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(BindReceiptCodeBean baseBean) {
                if (baseBean.isSuccess()) {
                    showSwipeCardDialog(baseBean.getMsg());
                } else {
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
        tipDialog = new TwoButtonDialog(BindReceiptCodeActivity1.this).builder()
                .setTitle(BindReceiptCodeActivity1.this.getString(R.string.tips))
                .setMsg(msg)
                .setCancelable(false)
                .setLeftButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BindReceiptCodeActivity1.this.finish();
                    }
                });
        tipDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==11 && data!=null){
            String signContents = data.getStringExtra("signCon");
            content = data.getStringExtra("content");
            startScanPaying(content,signContents); //请求数据
        }
    }
    private void startScanPaying(final String resultData ,final String signContents) {
        String url = NetUrl.CHECK_TID;
        JSONObject jsonRequest = null;

        try {
            String loginType = SharedPreferencesUtil.getInstance(BindReceiptCodeActivity1.this).getKey("loginType");
            jsonRequest = new JSONObject();
            jsonRequest.put("tid",resultData);
            jsonRequest.put("storeId",storeId);
            if(loginType.equals("0")){
                jsonRequest.put("merId",  SharedPreferencesUtil.getInstance(BindReceiptCodeActivity1.this).getKey("merchantID"));
            }else {
                jsonRequest.put("merId",  SharedPreferencesUtil.getInstance(BindReceiptCodeActivity1.this).getKey("shopId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkUtils.getInstance().post(BindReceiptCodeActivity1.this, url, jsonRequest, new MyOkCallback<String>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(String str) {
                JSONObject response = null;
                try {
                    response = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    LogUtils.d("lyf--response:" + response.toString());
                    status = response.getString("status");
                    message = response.getString("msg");
                    if ("0".equals(status)){
                        onResult(content,signContents );
                    }else {
                        com.hybunion.yirongma.payment.utils.ToastUtil.shortShow(BindReceiptCodeActivity1.this,message);
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
